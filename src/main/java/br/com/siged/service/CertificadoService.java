package br.com.siged.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import br.com.siged.dao.CertificadoEmitidoDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dto.relatorio.certificados.CertificadoInstrutorDTO;
import br.com.siged.entidades.CertificadoEmitido;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Instrutor;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.StatusDesempenho;
import br.com.siged.service.exception.NaoPodeEmitirCertificadoException;
import br.com.siged.util.Util;

@Service
public class CertificadoService {

	@Autowired
	private CertificadoEmitidoDAO certificadoEmitidoDao;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private DesempenhoService desempenhoService;
	@Autowired
	private RelatorioService relatorioService;
	@Autowired
	private ModuloService moduloService;
	@Autowired
	private Util util;

	private static final String CERTIFICADO_PERSONALIZADO_CONTEXTUAL_PATH = "reports/certificado_personalizado/";

	public boolean podeEmitirCertificado(Participante participante, Evento evento) {

		try {

			if (evento.getDataFimRealizacao().before(new Date()) && evento.getPermiteCertificado().equals("S")) {

				StatusDesempenho statusDesempenho = desempenhoService.statusDesempenhoParticipanteNoEvento(participante,
						evento);

				if (statusDesempenho.equals(StatusDesempenho.APROVADO)) {

					if (evento.temProvedorIPC()) {

						if (evento.getInstrutores().size() == evento.getInstrutoresComAssinatura().size()) {

							return true;

						}

					} else if (evento.temProvedorRedeEscolas() || evento.isCertificadoPersonalizado()) {

						return true;

					}

				}

			}

			return false;

		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}

	}

	public String gerarCertificado(Long eventoId, Long participanteId, ModelMap modelMap, HttpServletResponse response)
			throws IOException {
		Evento evento = eventoDao.findByIdLazy(eventoId);
		Participante participante = participanteDao.findById(participanteId);
		CertificadoEmitido certificadoEmitido = null;
		String filename = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try {
			certificadoEmitido = this.salvar(evento, participante);
			filename = montarLayoutCertificado(evento, parameters);
		} catch (NaoPodeEmitirCertificadoException e) {
			modelMap.addAttribute("mensagemErro", e.getMessage());
			return "redirect:/evento/meuseventos";
		}

		String paramWhere = "WHERE inscricao.confirmada = 'S' AND certificado_emitido.codigoverificacao = '"
				+ certificadoEmitido.getCodigoVerificacao() + "'";
		parameters.put("paramWhere", paramWhere);

		InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);
		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "certificado", response);

		return null;

	}

	/**
	 * Encapsula as regras de neg??cio referente a emiss??o de certificado.
	 * 
	 * @author Rafael de Castro
	 * @param evento
	 * @param participante
	 * @return certificadoEmitido
	 * @throws NaoPodeEmitirCertificadoException
	 */
	public CertificadoEmitido salvar(Evento evento, Participante participante)
			throws NaoPodeEmitirCertificadoException {

		/**
		 * Condicional Principal: Referente ?? validade do Evento e do Participante - Se
		 * o Evento permite Certificado; - Se a Date de fim da realiza????o do Evento ??
		 * antes da Date de solicita????o do Certificado; - Se o Participante foi aprovado
		 * no Evento.
		 */
		if (evento != null && participante != null) {
			if (!evento.getPermiteCertificado().equals("S")) {
				throw new NaoPodeEmitirCertificadoException("Evento informado n??o permite certificado!");
			} else if (evento.getDataFimRealizacao() == null || !evento.getDataFimRealizacao().before(new Date())) {
				throw new NaoPodeEmitirCertificadoException("Certificado ainda n??o est?? dispon??vel");
			} else if (!desempenhoService.statusDesempenhoParticipanteNoEvento(participante, evento)
					.equals(StatusDesempenho.APROVADO)) {
				throw new NaoPodeEmitirCertificadoException("Participante n??o aprovado no evento informado");
			}
		} else {
			throw new NaoPodeEmitirCertificadoException(
					"N??o foi poss??vel emitir o Certificado. Favor entrar em contato com o IPC.");
		}

		/**
		 * Condicional Secund??rio: Referente aos Certificados cujo o Provedor seja o IPC
		 * - Se o Evento possui Instrutor; - Se o quantidade de Instrutores equivale a
		 * quantidade de Instrutores com assinatura.
		 */
		if (evento.temProvedorIPC()) {
			/*List<Instrutor> instrutorList = evento.getInstrutores();
			Integer cInstrutor = instrutorList.size();
			Integer cAssinatura = evento.getInstrutoresComAssinatura().size();

			if (cInstrutor == 0) {
				throw new NaoPodeEmitirCertificadoException("O Evento n??o possui Instrutor");
			} else if (cInstrutor == 1 && cAssinatura != cInstrutor && instrutorList.get(0).getId().intValue() != 50) { 																														
				// ID 50 O REGISTRO DO INSTRUTOR DIVERSOS																																																								
				throw new NaoPodeEmitirCertificadoException("Instrutor sem assinatura!");
			} else if (cInstrutor > 1 && cAssinatura != cInstrutor) {
				throw new NaoPodeEmitirCertificadoException("Instrutor(es) sem assinatura(s)!");
			}*/
		} else if (!evento.temProvedorRedeEscolas() && !evento.isCertificadoPersonalizado()
				&& !evento.temProvedorIRB()) {
			throw new NaoPodeEmitirCertificadoException(
					"N??o foi poss??vel emitir o Certificado. Favor entrar em contato com o IPC.");
		}

		if (evento.isCertificadoPersonalizado()) {
			List<String> filenames = this.getListaLaytoutsPersonalizadosDisponiveis();
			String certificadoPersonalizado = evento.getCertificadoPersonalizadoName();
			if (!filenames.contains(certificadoPersonalizado)) {
				throw new NaoPodeEmitirCertificadoException(
						"Layout personalizado cadastrado para o certificado n??o existe");
			}
		}

		CertificadoEmitido certificadoEmitido = this.criarCertificadoEmitido(evento, participante);
		if (certificadoEmitido != null && certificadoEmitido.getCodigoVerificacao() != null) {
			return certificadoEmitidoDao.persistAndFlush(certificadoEmitido);
		} else {
			throw new NaoPodeEmitirCertificadoException(
					"N??o foi poss??vel emitir o Certificado. Favor entrar em contato com o IPC.");
		}

	}

	/**
	 * 
	 * Verifica as regras para selecionar o layout do certificado e seta os devidos
	 * parametros do relat??rio.
	 * 
	 * @author Rafael Castro
	 * 
	 * @param evento
	 * @param parameters
	 * @return filename
	 * @throws NaoPodeEmitirCertificadoException Quando n??o ?? poss??vel gerar o
	 *                                           certificado
	 * 
	 * @since 24/05/2019 Ticket#-2019042610000319 - Manuten????o corretiva rotina de
	 *        emiss??o de certificados do SIGED</br>
	 *        <p>
	 *        Se o evento tem certificado personalizado, ent??o usa o layout
	 *        personalizado cadastrado (assinaturas fixas)
	 *        </p>
	 * 
	 *        Sen??o usa o certifcado padr??o:
	 *        <ul>
	 *        <li>Usa o modelo da REDE ESCOLAS caso o evento tenha provedor Rede de
	 *        Escolas e n??o tenha provedor IPC</li>
	 *        <li>Usa o modelo padr??o do IPC caso o evento tenha o provedor IPC
	 *        verificando:</br>
	 *        - Se tem 01 instrutor e instrutor != "DIVERSOS", e instrutor tem
	 *        assinatura, monta certificado com 01 assinatura</br>
	 *        - Sen??o, se tem 02 instrutores e instrutores tem assinaturas, monta
	 *        certificado com 02 assinaturas</br>
	 *        - Sen??o lan??ar {@link NaoPodeEmitirCertificadoException} com a
	 *        mensagem de instrutor(es) sem assinatura(s)</li>
	 *        </ul>
	 * 
	 */
	public String montarLayoutCertificado(Evento evento, HashMap<String, Object> parameters)
			throws NaoPodeEmitirCertificadoException {

		String filename = "";

		if (evento.isCertificadoPersonalizado()) {
			List<String> filenames = this.getListaLaytoutsPersonalizadosDisponiveis();
			String certificadoPersonalizado = evento.getCertificadoPersonalizadoName();
			if (filenames.contains(certificadoPersonalizado)) {
				filename = CERTIFICADO_PERSONALIZADO_CONTEXTUAL_PATH + certificadoPersonalizado;
			} else {
				throw new NaoPodeEmitirCertificadoException(
						"Layout personalizado cadastrado para o certificado n??o existe");
			}
		} else {
			Boolean isProvedorRedeEscolas = evento.temProvedorRedeEscolas();
			Boolean isProvedorIPC = evento.temProvedorIPC();
			Boolean isProvedorIRB = evento.temProvedorIRB();

			// IRB
			if (isProvedorIRB) {
				filename = "reports/certificadoIRB";
			} else if (isProvedorRedeEscolas || isProvedorIPC) {
				filename = "reports/certificado";
			}

			/**
			 * Chaveamento para Certificado da REDE_ESCOLAS: No relat??rio ser?? verificado o
			 * parameter isProvedorRedeEscolas e chavear a imagem de background caso true.
			 */
			parameters.put("isProvedorRedeEscolas", isProvedorRedeEscolas && !isProvedorIPC);
			
			if (isProvedorIPC) {
				/*List<Instrutor> instrutorList = evento.getInstrutores();
				Integer cInstrutor = instrutorList.size();
				Integer cAssinatura = evento.getInstrutoresComAssinatura().size();

				if (cInstrutor == 0) {
					throw new NaoPodeEmitirCertificadoException("O Evento n??o possui Instrutor");
				} else if (cInstrutor == 1 && instrutorList.get(0).getId().intValue() != 50) { 
					// ID 50 ?? O REGISTRO DO INSTRUTOR DIVERSOS
					if (cAssinatura == cInstrutor) {
						parameters.put("assinatura1", instrutorList.get(0).getAssinatura());
					} else {
						throw new NaoPodeEmitirCertificadoException("Instrutor sem assinatura!");
					}
				} else if (cInstrutor == 2) {
					if (cAssinatura == cInstrutor) {
						filename += "assinaturas";
						parameters.put("assinatura1", instrutorList.get(0).getAssinatura());
						parameters.put("assinatura2", instrutorList.get(1).getAssinatura());
					} else {
						throw new NaoPodeEmitirCertificadoException("Instrutor(es) sem assinatura(s)!");
					}
				}*/
			}
			 
		}

		instanciarParametrosCertificadoAluno(evento, parameters);

		if (!filename.isEmpty()) {
			filename += ".jasper";
		} else {
			throw new NaoPodeEmitirCertificadoException("Layout para este certificado n??o encontrado");
		}

		return filename;
	}

	public CertificadoInstrutorDTO montarLayoutCertificadoInstrutor(Evento evento, Modulo modulo, Instrutor instrutor,
			HashMap<String, Object> parameters) throws NaoPodeEmitirCertificadoException {
		if (evento == null) {
			throw new NaoPodeEmitirCertificadoException("Evento n??o encontrado");
		} else if (modulo == null) {
			throw new NaoPodeEmitirCertificadoException("M??dulo n??o encontrado");
		} else if (instrutor == null) {
			throw new NaoPodeEmitirCertificadoException("Instrutor n??o encontrado");
		}

		if (!moduloService.isRealizado(modulo))
			throw new NaoPodeEmitirCertificadoException(
					"N??o ?? poss??vel emitir certificado de Instrutor para M??dulo n??o realizado");

		boolean isInstrutorDoModulo = false;
		for (Instrutor instrutorDoModulo : modulo.getInstrutorList()) {
			if (instrutor.getId().equals(instrutorDoModulo.getId())) {
				isInstrutorDoModulo = true;
				break;
			}
		}

		if (!isInstrutorDoModulo)
			throw new NaoPodeEmitirCertificadoException("Instrutor n??o ministrou o M??dulo informado");

		String local = " ";
		String periodo = "";

		/*
		 @deprecated Campo modalidade no evento foi depreciado. Boolean eventoEAD =
		 evento.getModalidadeId() != null && evento.getModalidadeId().isEAD();
		 */
		final Boolean isModuloUnico = evento.getModuloUnico().equals("S") && evento.getModuloList().size() == 1 ? true : false;

		/*
		 * @deprecated Campo localiza????o no evento foi depreciado. Ser?? est??tico no
		 * corpo do texto do certificado de aluno: ??????realizado em Fortaleza-CE???
		 * if(evento.getLocalizacaoId() != null &&
		 * evento.getLocalizacaoId().getMunicipio() != null) { TipoLocalizacaoEvento
		 * localEvento = evento.getLocalizacaoId(); String cidade =
		 * localEvento.getMunicipio().getNome(); String uf =
		 * localEvento.getMunicipio().getUfMunicipio(); local = ", realizado em " +
		 * cidade + "-" + uf + ", "; }
		 */

		CertificadoInstrutorDTO certificadoInstrutor = new CertificadoInstrutorDTO(
				"reports/certificadoinstrutor.jasper");
		certificadoInstrutor.setModuloUnico(isModuloUnico);
		/*
		 * @deprecated Campo modalidade no evento foi depreciado.
		 * certificadoInstrutor.setEventoEAD(eventoEAD);
		 */
		certificadoInstrutor.setNomeInstrutor(instrutor.getNome().trim());
		certificadoInstrutor.setConteudoProgramatico(evento.getConteudo());

		String modalidadeDescricao = "";
		if (isModuloUnico) {
			certificadoInstrutor.setSituacao("ministrou o evento " + evento.getNome2());
			certificadoInstrutor.setCargaHoraria(evento.getCargaHoraria());
			certificadoInstrutor.setDataFimRealizacaoPorExtenso(
					new SimpleDateFormat("dd 'de' MMMMM 'de' yyyy", new Locale("pt", "BR"))
							//.format(evento.getDataFimRealizacao()));
							.format(new Date()));

			if (util.getDataFormatada(evento.getDataInicioRealizacao())
					.compareTo(util.getDataFormatada(evento.getDataFimRealizacao())) == 0)
				periodo = "no dia " + util.formataData(evento.getDataInicioRealizacao());
			else
				periodo = "no per??odo de " + util.formataData(evento.getDataInicioRealizacao()) + " a "
						+ util.formataData(evento.getDataFimRealizacao());

			if (modulo.getModalidade().isEAD())
				modalidadeDescricao = "Evento na modalidade a dist??ncia (EAD).";

			/*
			 * Regra abaixo foi definida no Ticket#-2019041610000098 ??? Manuten????o cerficado
			 * SIGED em 16/04/2019: - No certificado para o provedor IPC, incluir a
			 * localiza????o (cidade-uf) do m??dulo (quando o evento tiver M??DULO ??NICO) ap??s o
			 * texto "..., realizado em...". - Quando o evento tiver mais de um m??dulo, n??o
			 * exibir o texto "..., realizado em..." no certificado.
			 */
			if (evento.temProvedorIPC() && !modulo.getCidadeEUF().isEmpty()) {
				local = ", realizado em " + modulo.getCidadeEUF() + ", ";
			}
		} else {
			certificadoInstrutor
					.setSituacao("ministrou o m??dulo " + modulo.getTitulo() + " do evento " + evento.getNome2());
			certificadoInstrutor.setCargaHoraria(modulo.getCargaHoraria());
			certificadoInstrutor.setDataFimRealizacaoPorExtenso(
					new SimpleDateFormat("dd 'de' MMMMM 'de' yyyy", new Locale("pt", "BR"))
							.format(modulo.getDataFim()));

			if (util.getDataFormatada(modulo.getDataInicio())
					.compareTo(util.getDataFormatada(modulo.getDataFim())) == 0)
				periodo = "no dia " + util.formataData(modulo.getDataInicio());
			else
				periodo = "no per??odo de " + util.formataData(modulo.getDataInicio()) + " a "
						+ util.formataData(modulo.getDataFim());

			if (evento.temProvedorIPC() && modulo.getModalidade().isEAD())
				modalidadeDescricao = "M??dulo na modalidade a dist??ncia (EAD).";
		}
		certificadoInstrutor.setPeriodo(periodo);
		certificadoInstrutor.setLocal(local);
		certificadoInstrutor.setModalidadeDescricao(modalidadeDescricao);

		return certificadoInstrutor;
	}

	public CertificadoEmitido criarCertificadoEmitido(Evento evento, Participante participante) {
		String codigoVerificacao = null;
		CertificadoEmitido certificadoEmitido = null;

		while (certificadoEmitido == null) {
			codigoVerificacao = util.gerarCodigoVerificacao().replaceAll("[-/().]", "");
			if (certificadoEmitidoDao.findByCodigoVerificacao(codigoVerificacao) == null)
				certificadoEmitido = new CertificadoEmitido(codigoVerificacao, evento, participante);
		}

		return certificadoEmitido;
	}

	private void instanciarParametrosCertificadoAluno(Evento evento, HashMap<String, Object> parameters) {
		String periodo = "";
		String tituloEvento = "";
		String cargaHoraria = "";
		String dataEventoPorExtenso = "";
		String local = " ";
		if (util.getDataFormatada(evento.getDataInicioRealizacao())
				.compareTo(util.getDataFormatada(evento.getDataFimRealizacao())) == 0)
			periodo = "no dia " + util.formataData(evento.getDataInicioRealizacao());
		else
			periodo = "no per??odo de " + util.formataData(evento.getDataInicioRealizacao()) + " a "
					+ util.formataData(evento.getDataFimRealizacao());

		if (evento.getNome2() != null) {
			tituloEvento = evento.getNome2();
		}

		if (evento.getCargaHoraria() != null) {
			cargaHoraria = evento.getCargaHoraria();
		}

		if (evento.getDataFimRealizacao() != null) {
			dataEventoPorExtenso = new SimpleDateFormat("dd 'de' MMMMM 'de' yyyy", new Locale("pt", "BR"))
					//.format(evento.getDataFimRealizacao());
					.format(new Date());
		}

		/**
		 * @deprecated Campo localiza????o no evento foi depreciado. Ser?? est??tico no
		 * corpo do texto do certificado de aluno: ?????? realizado em Fortaleza-CE???
		 */
		/*if (evento.getLocalizacaoId() != null && evento.getLocalizacaoId().getMunicipio() != null) {
			TipoLocalizacaoEvento localEvento = evento.getLocalizacaoId();
			String cidade = localEvento.getMunicipio().getNome();
			String uf = localEvento.getMunicipio().getUfMunicipio();
			local = ", realizado em " + cidade.toUpperCase() + "-" + uf.toUpperCase() + ", ";
		}*/
		
		/**
		 * Regra abaixo foi definida no Ticket#-2019041610000098 ??? Manuten????o cerficado SIGED em 16/04/2019:
		 * - No certificado para o provedor IPC, incluir a localiza????o (cidade-uf) do
		 * m??dulo (quando o evento tiver M??DULO ??NICO) ap??s o texto
		 * "..., realizado em...". - Quando o evento tiver mais de um m??dulo, n??o exibir
		 * o texto "..., realizado em..." no certificado.
		 */
		if (evento.temProvedorIPC() && evento.getModuloList() != null && evento.getModuloList().size() == 1) {
			Modulo moduloUnico = evento.getModuloList().get(0);
			if (!moduloUnico.getCidadeEUF().isEmpty()) {
				local = ", realizado em " + moduloUnico.getCidadeEUF() + ", ";
			}
		}

		parameters.put("periodo", periodo);
		parameters.put("tituloEvento", tituloEvento);
		parameters.put("cargaHoraria", cargaHoraria);
		parameters.put("dataEventoPorExtenso", dataEventoPorExtenso);
		parameters.put("local", local);
		// System.out.println("participou do evento "+ tituloEvento + periodo + ", com
		// carga hor??ria de " + cargaHoraria + " horas.");
	}

	private List<String> getListaLaytoutsPersonalizadosDisponiveis() {
		List<String> filenames = new ArrayList<>();

		File file = null;
		try {
			file = new File(
					this.getClass().getClassLoader().getResource(CERTIFICADO_PERSONALIZADO_CONTEXTUAL_PATH).getPath());
		} catch (Exception e) {
			return filenames;
		}

		if (file != null && file.isDirectory()) {
			for (String name : file.list()) {
				if (name.endsWith(".jasper")) {
					filenames.add(name.replace(".jasper", ""));
				}
			}
		}

		return filenames;
	}

}
