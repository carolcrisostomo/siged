package br.com.siged.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.siged.dao.AvaliacaoReacaoDAO;
import br.com.siged.dao.AvaliacaoReacaoNotaDAO;
import br.com.siged.dao.EntidadeDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.GenericDAO;
import br.com.siged.dao.InscricaoDAO;
import br.com.siged.dao.MetaDesempenhoProdutividadeDAO;
import br.com.siged.dao.ModuloDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.QuestaoDAO;
import br.com.siged.dao.TipoQuestaoDAO;
import br.com.siged.dto.relatorio.avaliacaoreacao.Comentario;
import br.com.siged.dto.relatorio.avaliacaoreacao.ModuloDTO;
import br.com.siged.dto.relatorio.avaliacaoreacao.QuestaoDTO;
import br.com.siged.dto.relatorio.avaliacaoreacao.TipoQuestaoDTO;
import br.com.siged.dto.relatorio.participantesexternos.EventoDTO;
import br.com.siged.dto.relatorio.participantesexternos.EventoDTOComparator;
import br.com.siged.dto.relatorio.participantesexternos.ParticipanteExternoDTO;
import br.com.siged.entidades.AvaliacaoReacao;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Inscricao;
import br.com.siged.entidades.Instrutor;
import br.com.siged.entidades.MetaDesempenhoProdutividade;
import br.com.siged.entidades.Modalidade;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.NotaQuestao;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.Questao;
import br.com.siged.entidades.RelatorioBean;
import br.com.siged.entidades.StatusDesempenho;
import br.com.siged.entidades.TipoPublicoAlvo;
import br.com.siged.entidades.TipoQuestao;
import br.com.siged.filtro.EventoFiltro;
import br.com.siged.filtro.RelatorioFiltro;
import br.com.siged.service.exception.BusinessException;
import br.com.siged.service.exception.DataInvalidaException;
import br.com.siged.service.exception.RelatorioException;
import br.com.siged.service.exception.SemestreInvalidoException;
import br.com.siged.util.Anexo;
import br.com.siged.util.StringUtil;
import br.com.siged.util.Util;
import br.com.siged.util.comparator.InscricaoComparator;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class RelatorioService {
	
	/**
	 * Separador para os campos e valores dos arquivos CSV
	 */
	public static final String CSV_SEPARADOR = ",";
	
	/**
	 * Delimitador para campos textos
	 */
	public static final String DELIMITADOR = "\"";
	
	/**
	 * Quebra de linha dos arquivos
	 */
	public static final String LINE_SEPARATOR = "\r\n";/*System.getProperty("line.separator")*/
	
	
	@Autowired
	private QuestaoDAO questaoDao;
	
	@Autowired
	private AvaliacaoReacaoNotaDAO avaliacaoreacaonotaDao;
	
	@Autowired
	private AvaliacaoReacaoDAO avaliacaoreacaoDao;
	
	@Autowired
	private TipoQuestaoDAO tipoquestaoDao;
	
	@Autowired
	private EventoDAO eventoDao;
	
	@Autowired
	private ModuloDAO moduloDao;
	
	@Autowired
	private GenericDAO genericDao;
	
	@Autowired
	private InscricaoDAO inscricaoDao;
	
	@Autowired
	private EntidadeDAO entidadeDao;
	
	@Autowired
	private ParticipanteDAO participanteDao;
	
	@Autowired
	private Util util;
		
	private byte[] relatorio;
	
	@Autowired
	private DesempenhoService desempenhoService;
	
	@Autowired
	private MetaDesempenhoProdutividadeDAO indicadorDesempenhoProdutividadeDao;
	
	/**
	 * Gera relatório das Avaliações de reação respondidas do Evento, separadas por módulo.
	 * 
	 * @author Rafael de Castro
	 * @param evento
	 * @param relatorioFiltro
	 * @param response
	 */
	public void gerarRelatorioAvaliacoesReacaoParticipantePorEvento(RelatorioFiltro relatorioFiltro, HttpServletResponse response) throws RelatorioException {
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/avaliacoes_reacao_por_evento.jasper");
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		
		if(relatorioFiltro.getEventoId() == null || relatorioFiltro.getEventoId() == 0)
			throw new RelatorioException("Evento não informado");
		
		Evento eventoConsulta = eventoDao.findById(relatorioFiltro.getEventoId());
		List<Modulo> moduloList = new ArrayList<>();
		
		if(relatorioFiltro.getModulo() != null && relatorioFiltro.getModulo() > 0) {
			moduloList.add(moduloDao.findById(relatorioFiltro.getModulo()));
			parameters.put("CONSULTA_POR_MODULO", Boolean.TRUE);
		} else {
			moduloList.addAll(eventoConsulta.getModuloList());
			parameters.put("CONSULTA_POR_MODULO", Boolean.FALSE);
		}
		
		List<ModuloDTO> moduloDTOList = new ArrayList<>();
		/*
		 * Campo modalidade no evento foi depreciado.
		Modalidade modalidade = eventoConsulta.getModalidadeId();
		 */
		TipoQuestao tipoQuestaoInstrutor = tipoquestaoDao.findTipoInstrutor();
		
		//Agrupa por Módulo
		for(Modulo modulo : moduloList) {
			/*
			 * A modalidade será verificada no Módulo
			 * @since 07/12/2018
			 */
			Modalidade modalidade = modulo.getModalidade();
			if(!modulo.getAvalicoesReacaoList().isEmpty()){
				ModuloDTO moduloDTO = new ModuloDTO();
				moduloDTO.setTitulo(modulo.getTitulo());
				moduloDTO.setQuantidadeAvaliacoes(modulo.getAvalicoesReacaoList().size());
				
				for(AvaliacaoReacao avaliacaoReacao : modulo.getAvalicoesReacaoList()) {
					if(avaliacaoReacao != null && avaliacaoReacao.isSatisfatoria()) {
						moduloDTO.incrementarSatisfatorias();
					}
				}
				
				if(relatorioFiltro.getAutomatico()) {
					moduloDTO.setListaComentarios(instanciarComentarios(modulo.getId()));
				}
				
				/**
				 * Agrupa por Instrutor
				 */
				for(Instrutor instrutor : modulo.getInstrutorList()) {
					moduloDTO.getTiposQuestoes().add(instanciarTipoQuestao(modulo, modalidade, tipoQuestaoInstrutor, instrutor));
				}
				
				/**
				 * Agrupa por Tipo excluindo instrutor que foi agrupado acima
				 */
				for(TipoQuestao tipoQuestao : tipoquestaoDao.findAllExludeOne(1L)) {
					moduloDTO.getTiposQuestoes().add(instanciarTipoQuestao(modulo, modalidade, tipoQuestao, null));
				}
				
				/**
				 * Monta as informações para o gráfico das avaliações do Módulo
				 */
				moduloDTO.incluirGrafico();
				moduloDTOList.add(moduloDTO);
			}
		}
		
		Integer totalQuestoesRespondidas = 0;
		Integer totalAvaliacoesReacaoDoEvento = 0;
		Integer satisfatorias = 0;
		Integer totalExcelente = 0;
		Integer totalBom = 0;
		Integer totalRegular = 0;
		Integer totalInsuficiente = 0;
		
		/**
		 * Informações para o gráfico das avaliações do Evento
		 */
		for(ModuloDTO moduloDTO : moduloDTOList){
			totalExcelente += moduloDTO.getTotalQuestoesExcelente();
			totalBom += moduloDTO.getTotalQuestoesBom();
			totalRegular += moduloDTO.getTotalQuestoesRegular();
			totalInsuficiente += moduloDTO.getTotalQuestoesInsuficiente();
			totalQuestoesRespondidas += moduloDTO.getTotalQuestoesRespondidas();
			satisfatorias += moduloDTO.getQuantidadeSatisfatorias();
			totalAvaliacoesReacaoDoEvento += moduloDTO.getQuantidadeAvaliacoes();
		}
		
		Double porcentagem = (1.0 * satisfatorias / totalAvaliacoesReacaoDoEvento) * 100;
		
		List<RelatorioBean> listaDeComentarios = new ArrayList<>();
		
		parameters.put("PERCENTUAL_SATISFATORIAS", String.format("%.1f",porcentagem) + "%" );
		parameters.put("TOTAL_EXCELENTE", totalExcelente);
		parameters.put("TOTAL_BOM", totalBom);
		parameters.put("TOTAL_REGULAR", totalRegular);
		parameters.put("TOTAL_INSUFICIENTE", totalInsuficiente);
		parameters.put("TOTAL_GERAL", totalQuestoesRespondidas);
		
		parameters.put("EVENTO", eventoConsulta.getNome2().toUpperCase());
		parameters.put("PERIODO", "DE " + util.formataData(eventoConsulta.getDataInicioRealizacao()) + " A " + util.formataData(eventoConsulta.getDataFimRealizacao()));
		parameters.put("PROVEDOR", eventoConsulta.getProvedoresLista());
		parameters.put("CARGA_HORARIA", eventoConsulta.getCargaHoraria());
		parameters.put("QTDE_MODULOS", moduloList.size());
		parameters.put("EXIBE_COMENTARIOS", relatorioFiltro.getAutomatico());
		parameters.put("EXIBE_GRAFICO_MODULO", moduloDTOList.size() > 1 ? Boolean.TRUE : Boolean.FALSE);
		parameters.put("LISTA_COMENTARIOS", listaDeComentarios);
		
		byte[] relatorio = chamarRelatorioJRdataSource(is, parameters, new JRBeanCollectionDataSource(moduloDTOList));
		abrirPdf(relatorio, "avaliacoes_de_reacao_por_evento", response);
		
	}
	
	
	/**
	 * Gera relatório das Avaliações de reação em branco.
	 * 
	 * @author Felipe Augusto
	 * @param evento
	 * @param relatorioFiltro
	 * @param response
	 */
	public void gerarRelatorioAvaliacoesReacaoEmBranco(RelatorioFiltro relatorioFiltro, HttpServletResponse response) throws RelatorioException {
		
		if(relatorioFiltro.getEventoId() == null || relatorioFiltro.getEventoId() == 0)
			throw new RelatorioException("Evento não informado");
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/avaliacoes_reacao_por_evento_em_branco.jasper");
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		
		
		Evento eventoConsulta = eventoDao.findById(relatorioFiltro.getEventoId());
		List<Modulo> moduloList = new ArrayList<>();
		
		if(relatorioFiltro.getModulo() != null && relatorioFiltro.getModulo() > 0) {
			moduloList.add(moduloDao.findById(relatorioFiltro.getModulo()));			
		} else {
			moduloList.addAll(eventoConsulta.getModuloList());
		}
		
		List<ModuloDTO> moduloDTOList = new ArrayList<>();
		
		TipoQuestao tipoQuestaoInstrutor = tipoquestaoDao.findTipoInstrutor();
				
		for(Modulo modulo : moduloList) {
			
			Modalidade modalidade = modulo.getModalidade();
			
			ModuloDTO moduloDTO = new ModuloDTO();
			moduloDTO.setTitulo(modulo.getTitulo());			
			
			/**
			 * Agrupa por Instrutor
			 */
			for(Instrutor instrutor : modulo.getInstrutorList()) {
				moduloDTO.getTiposQuestoes().add(instanciarTipoQuestao(modulo, modalidade, tipoQuestaoInstrutor, instrutor));
			}
			
			/**
			 * Agrupa por Tipo excluindo instrutor que foi agrupado acima
			 */
			for(TipoQuestao tipoQuestao : tipoquestaoDao.findAllExludeOne(1L)) {
				moduloDTO.getTiposQuestoes().add(instanciarTipoQuestao(modulo, modalidade, tipoQuestao, null));
			}
			
			moduloDTOList.add(moduloDTO);
		}		
		
		parameters.put("EVENTO", eventoConsulta.getNome2().toUpperCase());
		if (eventoConsulta.getDataInicioRealizacao() != null && util.formataData(eventoConsulta.getDataFimRealizacao()) != null) {
			parameters.put("PERIODO", "DE " + util.formataData(eventoConsulta.getDataInicioRealizacao()) + " À " + util.formataData(eventoConsulta.getDataFimRealizacao()));			
		} else {
			parameters.put("PERIODO", "DE " + util.formataData(eventoConsulta.getDataInicioPrevisto()) + " À " + util.formataData(eventoConsulta.getDataFimPrevisto()));
		}
		
		parameters.put("PROVEDOR", eventoConsulta.getProvedoresLista());
		parameters.put("CARGA_HORARIA", eventoConsulta.getCargaHoraria());
		parameters.put("QTDE_MODULOS", moduloList.size());
		
		byte[] relatorio = chamarRelatorioJRdataSource(is, parameters, new JRBeanCollectionDataSource(moduloDTOList));
		abrirPdf(relatorio, "avaliacoes_de_reacao_por_evento_em_branco", response);
		
	}
	
	
	
	/**
	 * Instancia os comentários em relação ao Módulo
	 * 
	 * @author Rafael de Castro
	 * @param moduloId
	 * @return
	 */
	private List<Comentario> instanciarComentarios(Long moduloId){
		List<Comentario> comentarios = new ArrayList<>();
		for (AvaliacaoReacao avaliacao : avaliacaoreacaoDao.findByModuloIdComComentarios(moduloId)) {
			Comentario comentario = new Comentario();
			if (avaliacao.getParticipante() != null) {
				comentario.setNomeParticipante(avaliacao.getParticipante().getNome());
			}
			if (!avaliacao.getObservacao().isEmpty()) {
				comentario.setComentario(avaliacao.getObservacao());
				comentarios.add(comentario);
			}
		}
		return comentarios;
	}
	
	/**
	 * Agrupa as questões por Tipo 
	 * 
	 * @author Rafael de Castro
	 * @param modulo
	 * @param modalidade
	 * @param tipoQuestao
	 * @param instrutor
	 * @return
	 */
	private TipoQuestaoDTO instanciarTipoQuestao(Modulo modulo, Modalidade modalidade, TipoQuestao tipoQuestao, Instrutor instrutor){
		TipoQuestaoDTO tipoQuestaoDTO = new TipoQuestaoDTO();
		if(instrutor != null){
			tipoQuestaoDTO.setDescricao("INSTRUTOR: " + instrutor.getNome());
		} else {
			tipoQuestaoDTO.setDescricao(tipoQuestao.getDescricao().toUpperCase());
		}
		

		// Agrupo por Questao
		for (Questao questao : questaoDao.findByTipoQuestaoIdWithModalidades(tipoQuestao.getId())) {
			if (questao.isModalidade(modalidade)) {
				tipoQuestaoDTO.getQuestoes().add(instanciarQuestao(modulo, instrutor, questao));
			}
		}
		
		return tipoQuestaoDTO;
	}
	
	/**
	 * Instancia as notas de cada Questão
	 * 
	 * @author Rafael de Castro
	 * @param modulo
	 * @param instrutor
	 * @param questao
	 * @return
	 */
	private QuestaoDTO instanciarQuestao(Modulo modulo, Instrutor instrutor, Questao questao){
		int excelente = avaliacaoreacaonotaDao.findByCriteria(modulo, instrutor, questao, "Excelente", null).size();
		int bom = avaliacaoreacaonotaDao.findByCriteria(modulo, instrutor, questao, "Bom", null).size();
		int regular = avaliacaoreacaonotaDao.findByCriteria(modulo, instrutor, questao, "Regular", null).size();
		int insuficiente = avaliacaoreacaonotaDao.findByCriteria(modulo, instrutor, questao, "Insuficiente", null).size();
		
		QuestaoDTO questaoDTO = new QuestaoDTO();
		questaoDTO.setDescricao(questao.getDescricao());
		questaoDTO.setExcelente(excelente);
		questaoDTO.setBom(bom);
		questaoDTO.setRegular(regular);
		questaoDTO.setInsuficiente(insuficiente);
		questaoDTO.atualizarTotalNotas();
		
		return questaoDTO;
	}
	
	
	
	/**
	 * Gera relatório de indicadores de Desempenho
	 * 
	 * @author Rafael de Castro
	 * @param relatorioFiltro
	 * @param response
	 * @throws ParseException
	 * @throws SemestreInvalidoException
	 * @throws DataInvalidaException
	 */
	public void gerarRelatorioIndicadoresDesempenho(RelatorioFiltro relatorioFiltro, HttpServletResponse response) 
			throws ParseException, SemestreInvalidoException, DataInvalidaException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/indicadores_desempenho.jasper");

		String semestre = "";
		String ano = "";

		String semestre_data1 = null;
		String semestre_data2 = null;
		String semestre_data2_periodo = null;
		
		Calendar dataAtual = Calendar.getInstance();
		Integer anoAtual = dataAtual.get(Calendar.YEAR);
		Integer mesAtual = dataAtual.get(Calendar.MONTH) + 1;
		
		if (relatorioFiltro.getSemestre() != null)
			semestre = relatorioFiltro.getSemestre();
		if (relatorioFiltro.getAno() != null)
			ano = relatorioFiltro.getAno();

		if (relatorioFiltro.getAno().equals(anoAtual.toString()) && mesAtual < 7 && semestre.equals("2")) {
			throw new SemestreInvalidoException("Selecione um semestre válido!");
		}

		if (semestre.equals("1")) {
			semestre_data1 = "01/01/";
			semestre_data2 = "30/06/";
			semestre_data2_periodo = "30/06/";
		} else if (semestre.equals("2")) {
			semestre_data1 = "01/07/";
			semestre_data2 = "31/12/";
			semestre_data2_periodo = "31/12/";
		}
		
		semestre_data1 += ano;
		semestre_data2 += ano;
		semestre_data2_periodo += ano;	

		if (relatorioFiltro.getParcial_ate() != null && !relatorioFiltro.getParcial_ate()) {
			Date hoje = util.getDataFormatada(dataAtual.getTime());
			if (hoje.after(util.parseDate(semestre_data1)) && hoje.before(util.parseDate(semestre_data2))) {
				semestre_data2 = util.formataData(dataAtual.getTime());				
			}			
		}

		if (relatorioFiltro.getParcial_data() != null) {
			if (relatorioFiltro.getParcial_data().before(util.parseDate(semestre_data1)) || relatorioFiltro.getParcial_data().after(util.parseDate(semestre_data2))) {
				throw new DataInvalidaException("Selecione uma data válida!");
			}
		}


		if (relatorioFiltro.getParcial_data() != null) {
			semestre_data2 = util.formataData(relatorioFiltro.getParcial_data());
		}		

		HashMap<String, Object> parameters = new HashMap<String, Object>();		
		parameters.put("semestre_data1", util.parseDate(semestre_data1));
		parameters.put("semestre_data2", util.parseDate(semestre_data2));
		parameters.put("semestre_data2_periodo", util.parseDate(semestre_data2_periodo));
		parameters.put("parcial_data", relatorioFiltro.getParcial_data());
		parameters.put("parcial_ate", relatorioFiltro.getParcial_ate());
		
		MetaDesempenhoProdutividade meta = indicadorDesempenhoProdutividadeDao.findByAnoAndSemestre(Integer.valueOf(relatorioFiltro.getAno()), Integer.valueOf(relatorioFiltro.getSemestre()));
		if (meta == null) {
			throw new BusinessException("Meta não encontrada");
		}
		parameters.put("meta_ind_capacitacao", meta.getIndiceCapacitacao().toString() + "%");
		parameters.put("meta_ind_avaliacao_reacao", meta.getIndiceAvaliacaoReacao().toString() + "%");
		
		byte[] relatorio = chamarRelatorio(is, parameters);
		abrirPdf(relatorio, "indicadores_de_desempenho", response);
	}
	
	public byte[] fichaTecnicaDoEvento(Long eventoId) throws IOException {

		Evento evento = eventoDao.find(eventoId);
		
		Date inicioPrevisto = null;
		Date fimPrevisto = null;
		if(evento.getDataInicioPrevisto() != null && evento.getDataFimPrevisto() != null) {
			inicioPrevisto = util.getDataFormatada(evento.getDataInicioPrevisto());
			fimPrevisto = util.getDataFormatada(evento.getDataFimPrevisto());
		}
		
		Date inicioInscricao = null;
		Date fimInscricao = null;
		if(evento.getDataInicioPreInscricao() != null && evento.getDataFimPreInscricao() != null) {
			inicioInscricao = util.getDataFormatada(evento.getDataInicioPreInscricao());
			fimInscricao = util.getDataFormatada(evento.getDataFimPreInscricao());
		}
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/ficha_tecnica_de_evento.jasper");

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("EVENTO_ID", eventoId.toString());
		parameters.put("isPeriodo1Dia", (inicioPrevisto != null && fimPrevisto != null) && inicioPrevisto.compareTo(fimPrevisto) == 0);
		parameters.put("isInscricao1Dia", (inicioInscricao != null && fimInscricao != null) && inicioInscricao.compareTo(fimInscricao) == 0);
		parameters.put("listaModalidades", evento.getModalidadeModulosLista());
		parameters.put("listaLocalizacoes", evento.getLocalizacaoModulosLista());
		parameters.put("qtdeVagas", evento.getTotalVagas());
		parameters.put("linkEvento", evento.getLinkEvento());
		parameters.put("linkGravacao", evento.getLinkGravacao());

		return chamarRelatorio(is, parameters);
	}
	
	public byte[] participantesExternos(RelatorioFiltro relatorioFiltro) {
		List<Inscricao> inscricoes = inscricaoDao.findToRelatorioParticipantesExternos(relatorioFiltro);
		HashMap<Long, EventoDTO> listaRelatorioMap = new HashMap<>();
		for(Inscricao inscricao : inscricoes) {
			Evento evento = inscricao.getEventoId();
			Participante participante = inscricao.getParticipanteId();
			Boolean apurado = eventoDao.findByEventoApurado(evento.getId()) != null;
			
			String nota = desempenhoService.maiorNotaDoParticipanteNoEvento(participante, evento);
			String frequencia = desempenhoService.maiorFrequenciaDoParticipanteNoEvento(participante, evento);
			StatusDesempenho statusDesemepenho = desempenhoService.statusDesempenhoParticipanteNoEvento(participante, evento);
			ParticipanteExternoDTO  participanteExternoDTO = new ParticipanteExternoDTO(participante.getId(), participante.getNome(), participante.getCpf(), frequencia, nota, statusDesemepenho);
			
			if(!listaRelatorioMap.containsKey(evento.getId())) {
				EventoDTO eventoDTO = new EventoDTO(evento.getId(), evento.getTipoId().getDescricao(), evento.getTitulo(), 
						evento.getDataInicioRealizacao(), evento.getDataFimRealizacao(), 
						apurado);
				eventoDTO.addParticipanteExterno(participanteExternoDTO);
				listaRelatorioMap.put(evento.getId(), eventoDTO);
			} else {
				listaRelatorioMap.get(evento.getId()).addParticipanteExterno(participanteExternoDTO);
			}
		}
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/participantes_externos.jasper");

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("ORGAO", entidadeDao.findById(relatorioFiltro.getOrgaoId()).getDsentidade());

		if (relatorioFiltro.getEventoId() != null	&& relatorioFiltro.getEventoId() != 0) {
			Evento evento = eventoDao.findById(relatorioFiltro.getEventoId());
			parameters.put("EVENTO", evento.getNome2());
			parameters.put("INICIO_REALIZACAO",	evento.getDataInicioRealizacao());
			parameters.put("FIM_REALIZACAO", evento.getDataFimRealizacao());
		} else {
			parameters.put("EVENTO", "TODOS");
		}
		if (relatorioFiltro.getDataInicio1() != null)
			parameters.put("DATA_INICIO", util.formataData(relatorioFiltro.getDataInicio1()));

		if (relatorioFiltro.getDataInicio2() != null)
			parameters.put("DATA_FIM", util.formataData(relatorioFiltro.getDataInicio2()));

		
		List<EventoDTO> listaRelatorio = new ArrayList<>(listaRelatorioMap.values());
		Collections.sort(listaRelatorio, EventoDTOComparator.OrderByDataInicio.desc());
		
		return chamarRelatorioJRdataSource(is, parameters, new JRBeanCollectionDataSource(listaRelatorio));
	}
	
	public byte[] participantesPorTipoPorEvento(RelatorioFiltro relatorioFiltro) {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/participantes_por_tipo_por_evento.jasper");

		Long eventoId = relatorioFiltro.getEventoId();
		Evento evento = eventoDao.find(eventoId);
		
		Integer total_participante = participanteDao.findByEventoId(eventoId).size();

		String instrutores = "";
		
		List<String> instrutorList = new ArrayList<String>();
		List<Modulo> moduloList = moduloDao.findByEventoId(eventoId);
		for (Modulo modulo : moduloList) {
			for (Instrutor instrutor : modulo.getInstrutorList()) {
				if (!instrutorList.contains(instrutor.getNome()))
					instrutorList.add(instrutor.getNome());
			}
		}
		
		for (int i = 0; i < instrutorList.size(); i++) {
			instrutores += instrutorList.get(i);
			if (i < instrutorList.size() - 1) {
				instrutores += ", ";
			}
		}
		
		int total_questao_excelente = avaliacaoreacaonotaDao.findByNotaAndEvento(NotaQuestao.EXCELENTE, eventoId).size();
		int total_questao_bom = avaliacaoreacaonotaDao.findByNotaAndEvento(NotaQuestao.BOM, eventoId).size();
		int total_questao_regular = avaliacaoreacaonotaDao.findByNotaAndEvento(NotaQuestao.REGULAR, eventoId).size();
		int total_questao_insuficiente = avaliacaoreacaonotaDao.findByNotaAndEvento(NotaQuestao.INSUFICIENTE, eventoId).size();
		int total_questoes = total_questao_excelente + total_questao_bom + total_questao_regular + total_questao_insuficiente;
		
		String participanteTipoId = "";
		
		/*if(relatorioFiltro.getAutomatico() && relatorioFiltro.getPublicoAlvoId() != null && relatorioFiltro.getPublicoAlvoId() != 0){
			participanteTipoId = "AND participante.tipo_id = " +  relatorioFiltro.getPublicoAlvoId();
		}*/
		
		/*
		 * Campo localização no evento foi depreciado. Usar evento.getLocalizacaoModulosLista()
		String localizacao = evento.getLocalizacaoId().getDescricao();
		 */
		String localizacao = evento.getLocalizacaoModulosLista();
		
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("evento_id", eventoId.toString());
		parameters.put("instrutores", instrutores);		
		parameters.put("total_participante", total_participante.toString());		
		parameters.put("simplificado", false);
		parameters.put("participante_tipo_id", participanteTipoId);
		parameters.put("localizacao", localizacao);
		
		parameters.put("total_questao_excelente", total_questao_excelente);
		parameters.put("total_questao_bom", total_questao_bom);
		parameters.put("total_questao_regular", total_questao_regular);
		parameters.put("total_questao_insuficiente", total_questao_insuficiente);
		parameters.put("total_questoes", total_questoes);
		
		parameters.put("percentual_questao_excelente", String.format("%.1f",(Double.valueOf(total_questao_excelente) / total_questoes) * 100) + "%");
		parameters.put("percentual_questao_bom", String.format("%.1f",(Double.valueOf(total_questao_bom) / total_questoes) * 100) + "%");
		parameters.put("percentual_questao_regular", String.format("%.1f",(Double.valueOf(total_questao_regular) / total_questoes) * 100) + "%");
		parameters.put("percentual_questao_insuficiente", String.format("%.1f",(Double.valueOf(total_questao_insuficiente) / total_questoes) * 100) + "%");
		
		return chamarRelatorio(is, parameters);
	}
	
	public byte[] inscricoesToAVA(Long eventoId, String cursoNoAVA) {
		List<Inscricao> inscricoes = inscricaoDao.findByEventoAndConfirmada(eventoId, "S");
		Collections.sort(inscricoes, InscricaoComparator.OrderByNomeParticipante);
		
		StringBuilder out = new StringBuilder();
		String header = DELIMITADOR + "USERNAME" + DELIMITADOR + CSV_SEPARADOR + 
				DELIMITADOR + "PASSWORD" + DELIMITADOR + CSV_SEPARADOR + 
				DELIMITADOR + "AUTH" + DELIMITADOR + CSV_SEPARADOR + 
				DELIMITADOR + "FIRSTNAME" + DELIMITADOR + CSV_SEPARADOR + 
				DELIMITADOR + "LASTNAME" + DELIMITADOR + CSV_SEPARADOR + 
				DELIMITADOR + "EMAIL" + DELIMITADOR + CSV_SEPARADOR + 
				DELIMITADOR + "COURSE1" + DELIMITADOR + CSV_SEPARADOR + 
				DELIMITADOR + "TYPE1" + DELIMITADOR + 
				LINE_SEPARATOR;
		out.append(header);
		
		for(Inscricao inscricao : inscricoes) {
			Participante participante = inscricao.getParticipanteId();
			out.append(DELIMITADOR + participante.getCpf() + DELIMITADOR + CSV_SEPARADOR);
			out.append(DELIMITADOR + "12345" + DELIMITADOR + CSV_SEPARADOR);
			out.append(DELIMITADOR + "siged" + DELIMITADOR + CSV_SEPARADOR);
			out.append(DELIMITADOR + StringUtil.removerAcentos(participante.getNome()).toUpperCase().trim() + DELIMITADOR + CSV_SEPARADOR);
			out.append(DELIMITADOR + "-" + DELIMITADOR + CSV_SEPARADOR);
			
			if(participante.getEmail() != null)
				out.append(DELIMITADOR + participante.getEmail().toLowerCase().trim() + DELIMITADOR + CSV_SEPARADOR);
			else
				out.append(DELIMITADOR + "" + DELIMITADOR + CSV_SEPARADOR);
			
			out.append(DELIMITADOR + cursoNoAVA + DELIMITADOR + CSV_SEPARADOR);
			out.append(DELIMITADOR + "1" + DELIMITADOR);
			out.append(LINE_SEPARATOR);
		}
		
		return out.toString().getBytes();
	}
	
	public byte[] atividadesTrimestralAnualIPC(Date realizacao1, Date realizacao2) {
		StringBuilder out = new StringBuilder();
		out.append("Informações para Relatório de Atividades IPC" + LINE_SEPARATOR);
		out.append("-----------------------------------------------------------------" + LINE_SEPARATOR);
		out.append(LINE_SEPARATOR);
		
		EventoFiltro filtro = new EventoFiltro();
		filtro.setRealizacao1(realizacao1);
		filtro.setRealizacao2(realizacao2);
		
		out.append("Considerar o período " + util.formataData(realizacao1) + " a " + util.formataData(realizacao2) + " para consulta dos dados abaixo:" + LINE_SEPARATOR);
		out.append(LINE_SEPARATOR);
		
		out.append("1. TOTAL GERAL DE CAPACITAÇÕES" + LINE_SEPARATOR);
		out.append(LINE_SEPARATOR);
		List<Evento> eventos = eventoDao.findByFiltro(filtro, false, false);
		Map<String, Integer> eventosMap = getAtividadesTrimestralAnualIPCMap(eventos, false, false);
		montarAtividadesTrimestralAnualIPC(out, eventosMap);
		
		
		out.append("2. SERVIDORES/MEMBROS DO TCE (REALIZADAS PELO IPC E POR OUTRAS INSTITUIÇÕES)" + LINE_SEPARATOR);
		out.append(LINE_SEPARATOR);
		List<Evento> eventosParaServidoresOuMembros = eventoDao.findByPublicoAlvoPorPeriodoRealizacao(realizacao1, realizacao2, TipoPublicoAlvo.SERVIDOR_ID, TipoPublicoAlvo.MEMBRO_ID);
		eventosMap = getAtividadesTrimestralAnualIPCMap(eventosParaServidoresOuMembros, true, false);
		montarAtividadesTrimestralAnualIPC(out, eventosMap);
		
		out.append("Durante o período de referência, do total de capacitações, elencamos as ofertas para " + 
				"este público, nas modalidades presencial e a distância, realizadas pelo próprio IPC:" + LINE_SEPARATOR);
		out.append(LINE_SEPARATOR);
		out.append("Curso/evento, Período da realização, Oportunidades, Carga horária");
		out.append(LINE_SEPARATOR);
		
		for(Evento evento : eventosParaServidoresOuMembros) {
			if(evento.temProvedorIPC()) {
				out.append(evento.getNome2() + ", " + 
						util.formataData(evento.getDataInicioRealizacao()) + " a " + util.formataData(evento.getDataFimRealizacao()) + ", " +
						evento.getTotalInscricoesConfirmadas(TipoPublicoAlvo.SERVIDOR_ID, TipoPublicoAlvo.MEMBRO_ID) + ", " +
						evento.getCargaHoraria());
				out.append(LINE_SEPARATOR);
			}
		}
		out.append(LINE_SEPARATOR);
		
		
		out.append("3. JURISDICIONADOS (*)/SOCIEDADE (REALIZADAS PELO IPC E POR OUTRAS INSTITUIÇÕES)" + LINE_SEPARATOR);
		out.append(LINE_SEPARATOR);
		List<Evento> eventosParaJurisdicionadoOuSociedade = eventoDao.findByPublicoAlvoPorPeriodoRealizacao(realizacao1, realizacao2, TipoPublicoAlvo.JURISDICIONADO_ID, TipoPublicoAlvo.SOCIEDADE_ID);
		eventosMap = getAtividadesTrimestralAnualIPCMap(eventosParaJurisdicionadoOuSociedade, false, true);
		montarAtividadesTrimestralAnualIPC(out, eventosMap);
		
		out.append("Durante o período de referência, do total de capacitações, elencamos as ofertas para " + 
				"este público, nas modalidades presencial e a distância, realizadas pelo próprio IPC:" + LINE_SEPARATOR);
		out.append(LINE_SEPARATOR);
		out.append("Curso/evento, Período da realização, Oportunidades, Carga horária");
		out.append(LINE_SEPARATOR);
		
		for(Evento evento : eventosParaJurisdicionadoOuSociedade) {
			if(evento.temProvedorIPC()) {
				out.append(evento.getNome2() + ", " + 
						util.formataData(evento.getDataInicioRealizacao()) + " a " + util.formataData(evento.getDataFimRealizacao()) + ", " +
						evento.getTotalInscricoesConfirmadas(TipoPublicoAlvo.JURISDICIONADO_ID, TipoPublicoAlvo.SOCIEDADE_ID) + ", " +
						evento.getCargaHoraria());
				out.append(LINE_SEPARATOR);
			}
		}
		
		return out.toString().getBytes(StandardCharsets.UTF_8);
	}
	
	private Map<String, Integer> getAtividadesTrimestralAnualIPCMap(List<Evento> eventos, boolean isServidorFiltro, boolean isExternoFiltro) {
		int totalEventos = eventos.size();
		int totalEventosIPC = 0;
		int totalEventosPresenciais = 0;
		int totalEventosPresenciaisIPC = 0;
		int totalEventosEAD = 0;
		int totalEventosEADIPC = 0;
		
		int totalOportunidades = 0;
		int totalOportunidadesIPC = 0;
		int totalOportunidadesPresenciais = 0;
		int totalOportunidadesPresenciaisIPC = 0;
		int totalOportunidadesEAD = 0;
		int totalOportunidadesEADIPC = 0;
		
		int totalHoras = 0;
		int totalHorasIPC = 0;
		int totalHorasPresenciais = 0;
		int totalHorasPresenciaisIPC = 0;
		int totalHorasEAD = 0;
		int totalHorasEADIPC = 0;
		
		
		for(Evento evento : eventos) {
			int quantidadeInscricoes = 0;
			int quantidadeHoras = 0;
			
			if(evento.getInscricaoList() != null) {
				if(!isServidorFiltro && !isExternoFiltro) {
					quantidadeInscricoes = evento.getTotalInscricoesConfirmadas();
				} else {
					if(isServidorFiltro) {
						quantidadeInscricoes = evento.getTotalInscricoesConfirmadas(TipoPublicoAlvo.SERVIDOR_ID, TipoPublicoAlvo.MEMBRO_ID);
					} else if(isExternoFiltro) {
						quantidadeInscricoes = evento.getTotalInscricoesConfirmadas(TipoPublicoAlvo.JURISDICIONADO_ID, TipoPublicoAlvo.SOCIEDADE_ID);
					}
				}
				
			}
			try {
				quantidadeHoras = Integer.valueOf(evento.getCargaHoraria());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			totalOportunidades += quantidadeInscricoes;
			totalHoras += quantidadeHoras;
			if(evento.temProvedorIPC()) {
				totalEventosIPC++;
				totalOportunidadesIPC += quantidadeInscricoes;
				totalHorasIPC += quantidadeHoras;
			}
			
			/*
			 * @deprecated Campo modalidade no evento foi depreciado.
			if(evento.getModalidadeId().isPresencial()) {
				totalEventosPresenciais++;
				totalOportunidadesPresenciais += quantidadeInscricoes;
				totalHorasPresenciais += quantidadeHoras;
				if(evento.temProvedorIPC()) {
					totalEventosPresenciaisIPC++;
					totalOportunidadesPresenciaisIPC += quantidadeInscricoes;
					totalHorasPresenciaisIPC += quantidadeHoras;
				}

			} else if(evento.getModalidadeId().isEAD()) {
				totalEventosEAD++;
				totalOportunidadesEAD += quantidadeInscricoes;
				totalHorasEAD += quantidadeHoras;
				if(evento.temProvedorIPC()) {
					totalEventosEADIPC++;
					totalOportunidadesEADIPC += quantidadeInscricoes;
					totalHorasEADIPC += quantidadeHoras;
				}
					
			}
			 */
			if(evento.isPresencial()) {
				totalEventosPresenciais++;
				totalOportunidadesPresenciais += quantidadeInscricoes;
				totalHorasPresenciais += quantidadeHoras;
				if(evento.temProvedorIPC()) {
					totalEventosPresenciaisIPC++;
					totalOportunidadesPresenciaisIPC += quantidadeInscricoes;
					totalHorasPresenciaisIPC += quantidadeHoras;
				}

			} else if(evento.isEAD()) {
				totalEventosEAD++;
				totalOportunidadesEAD += quantidadeInscricoes;
				totalHorasEAD += quantidadeHoras;
				if(evento.temProvedorIPC()) {
					totalEventosEADIPC++;
					totalOportunidadesEADIPC += quantidadeInscricoes;
					totalHorasEADIPC += quantidadeHoras;
				}
					
			}
		}
		
		Map<String, Integer> totalEventosMap = new HashMap<>();
		totalEventosMap.put("totalEventos", totalEventos);
		totalEventosMap.put("totalEventosIPC", totalEventosIPC);
		totalEventosMap.put("totalEventosPresenciais", totalEventosPresenciais);
		totalEventosMap.put("totalEventosPresenciaisIPC", totalEventosPresenciaisIPC);
		totalEventosMap.put("totalEventosEAD", totalEventosEAD);
		totalEventosMap.put("totalEventosEADIPC", totalEventosEADIPC);
		
		totalEventosMap.put("totalOportunidades", totalOportunidades);
		totalEventosMap.put("totalOportunidadesIPC", totalOportunidadesIPC);
		totalEventosMap.put("totalOportunidadesPresenciais", totalOportunidadesPresenciais);
		totalEventosMap.put("totalOportunidadesPresenciaisIPC", totalOportunidadesPresenciaisIPC);
		totalEventosMap.put("totalOportunidadesEAD", totalOportunidadesEAD);
		totalEventosMap.put("totalOportunidadesEADIPC", totalOportunidadesEADIPC);
		
		totalEventosMap.put("totalHoras", totalHoras);
		totalEventosMap.put("totalHorasIPC", totalHorasIPC);
		totalEventosMap.put("totalHorasPresenciais", totalHorasPresenciais);
		totalEventosMap.put("totalHorasPresenciaisIPC", totalHorasPresenciaisIPC);
		totalEventosMap.put("totalHorasEAD", totalHorasEAD);
		totalEventosMap.put("totalHorasEADIPC", totalHorasEADIPC);

		return totalEventosMap;
	}
	
	
	private void montarAtividadesTrimestralAnualIPC(StringBuilder out, Map<String, Integer> eventosMap) {
		out.append("Nº de Cursos/Eventos" + LINE_SEPARATOR);
		out.append(eventosMap.get("totalEventos") + LINE_SEPARATOR);
		out.append(String.format("(%d realizados pelo IPC)%s", eventosMap.get("totalEventosIPC"), LINE_SEPARATOR));
		out.append(LINE_SEPARATOR);
		out.append(String.format("Presenciais: %d%s", eventosMap.get("totalEventosPresenciais"), LINE_SEPARATOR));
		out.append(String.format("(%d realizados pelo IPC)%s", eventosMap.get("totalEventosPresenciaisIPC"), LINE_SEPARATOR));
		out.append(LINE_SEPARATOR);
		out.append(String.format("EAD: %d%s", eventosMap.get("totalEventosEAD"), LINE_SEPARATOR));
		out.append(String.format("(%d realizados pelo IPC)%s", eventosMap.get("totalEventosEADIPC"), LINE_SEPARATOR));
		out.append(LINE_SEPARATOR);
		
		out.append("Nº de Oportunidades" + LINE_SEPARATOR);
		out.append(eventosMap.get("totalOportunidades") + LINE_SEPARATOR);
		out.append(String.format("(%d pelo IPC)%s", eventosMap.get("totalOportunidadesIPC"), LINE_SEPARATOR));
		out.append(LINE_SEPARATOR);
		out.append(String.format("Presenciais: %d%s", eventosMap.get("totalOportunidadesPresenciais"), LINE_SEPARATOR));
		out.append(String.format("(%d pelo IPC)%s", eventosMap.get("totalOportunidadesPresenciaisIPC"), LINE_SEPARATOR));
		out.append(LINE_SEPARATOR);
		out.append(String.format("EAD: %d%s", eventosMap.get("totalOportunidadesEAD"), LINE_SEPARATOR));
		out.append(String.format("(%d pelo IPC)%s", eventosMap.get("totalOportunidadesEADIPC"), LINE_SEPARATOR));
		out.append(LINE_SEPARATOR);
		
		out.append("Nº de Horas" + LINE_SEPARATOR);
		out.append(eventosMap.get("totalHoras") + LINE_SEPARATOR);
		out.append(String.format("(%d pelo IPC)%s", eventosMap.get("totalHorasIPC"), LINE_SEPARATOR));
		out.append(LINE_SEPARATOR);
		out.append(String.format("Presenciais: %d%s", eventosMap.get("totalHorasPresenciais"), LINE_SEPARATOR));
		out.append(String.format("(%d pelo IPC)%s", eventosMap.get("totalHorasPresenciaisIPC"), LINE_SEPARATOR));
		out.append(LINE_SEPARATOR);
		out.append(String.format("EAD: %d%s", eventosMap.get("totalHorasEAD"), LINE_SEPARATOR));
		out.append(String.format("(%d pelo IPC)%s", eventosMap.get("totalHorasEADIPC"), LINE_SEPARATOR));
		out.append(LINE_SEPARATOR);
	}
	
	// .................CHAMADAS DO RELATORIO.................

	/**
	 * Chama relatório passando a conexão com o Banco de Dados como datasource
	 * 
	 * @param is Stream de entrada
	 * @param parameters Parâmetros adicionais para o relatório
	 * @return Binário do relatório gerado pelo JasperReport
	 */
	public byte[] chamarRelatorio(final InputStream is, final HashMap<String, Object> parameters) throws RelatorioException {

		Session sess = (Session) genericDao.getEntityManager().getDelegate();

		sess.doWork(new Work() {

			public void execute(Connection connection) {

				try {
					relatorio = JasperRunManager.runReportToPdf(is, parameters, connection);
					connection.close();
				} catch (Exception e) {					
					e.printStackTrace();
					throw new RelatorioException("Não foi possível gerar o arquivo. Favor entrar em contato com o IPC.");
				}

			}
		});
		
		return relatorio;
	}
	
	/**
	 * Chama o relatório passando JRDataSource como datasource. Usado para passar os dados para o relatório diretamente
	 *  
	 * @param is Stream de entrada
	 * @param parameters Parâmetros adicionais para o relatório
	 * @param ds DataSource do JasperReport
	 * @return Binário do relatório gerado pelo JasperReport
	 */
	public byte[] chamarRelatorioJRdataSource(final InputStream is, final HashMap<String, Object> parameters, JRDataSource ds) {

		try {
			if ( ds == null ) ds = new JREmptyDataSource();
			relatorio = JasperRunManager.runReportToPdf(is, parameters, ds);
		} catch (JRException e) {
			e.printStackTrace();
		}

		return relatorio;
	}

	/**
	 * Gera o arquivo pdf para download
	 * 
	 * @param relatorio Binário que representa o arquivo
	 * @param nomeRelatorio Nome do arquivo
	 * @param response Resposta da requisição onde serão adicionados os headers referente ao download do arquivo
	 * 
	 * @see #abrirArquivo(byte[], String, HttpServletResponse, br.com.siged.util.Anexo.FileType)
	 */
	public void abrirPdf(byte[] relatorio, String nomeRelatorio, HttpServletResponse response) {
		abrirArquivo(relatorio, nomeRelatorio, response, Anexo.FileType.PDF);
	}
	
	/**
	 * Gera o arquivo csv para download
	 * 
	 * @param relatorio Binário que representa o arquivo
	 * @param nomeRelatorio Nome do arquivo
	 * @param response Resposta da requisição onde serão adicionados os headers referente ao download do arquivo
	 * 
	 * @see #abrirArquivo(byte[], String, HttpServletResponse, br.com.siged.util.Anexo.FileType)
	 */
	public void abrirCSV(byte[] relatorio, String nomeRelatorio, HttpServletResponse response) {
		abrirArquivo(relatorio, nomeRelatorio, response, Anexo.FileType.CSV);
	}
	
	/**
	 * Gera o arquivo txt para download
	 * 
	 * @param relatorio Binário que representa o arquivo
	 * @param nomeRelatorio Nome do arquivo
	 * @param response Resposta da requisição onde serão adicionados os headers referente ao download do arquivo
	 * 
	 * @see #abrirArquivo(byte[], String, HttpServletResponse, br.com.siged.util.Anexo.FileType)
	 */
	public void abrirTXT(byte[] relatorio, String nomeRelatorio, HttpServletResponse response) {
		abrirArquivo(relatorio, nomeRelatorio, response, Anexo.FileType.TXT);
	}
	
	/**
	 * Força o download do arquivo diretamente no navegador
	 * 
	 * @param relatorio Binário que representa o arquivo
	 * @param nomeRelatorio Nome do arquivo
	 * @param response Resposta da requisição onde serão adicionados os headers referente ao download do arquivo
	 * @param arquivo Classe que contém a devida extensão e content type do arquivo
	 * 
	 * @see Anexo.FileType
	 */
	private void abrirArquivo(byte[] relatorio, String nomeRelatorio, HttpServletResponse response, Anexo.FileType arquivo) {

		response.addHeader("content-disposition", "attachment;filename=" + nomeRelatorio + arquivo.getExtension());
		response.setContentType(arquivo.getContentType());
		OutputStream ops = null;

		try {
			if (relatorio != null) {
				ops = response.getOutputStream();
				ops.write(relatorio);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ops != null) {
				try {
					ops.flush();
					ops.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
