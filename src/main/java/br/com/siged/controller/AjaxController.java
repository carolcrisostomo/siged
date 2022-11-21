package br.com.siged.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.siged.dao.EntidadeDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.EventoExtraDAO;
import br.com.siged.dao.MetaDesempenhoProdutividadeDAO;
import br.com.siged.dao.MetaPlanejamentoEstrategicoDAO;
import br.com.siged.dao.InscricaoDAO;
import br.com.siged.dao.InstrutorDAO;
import br.com.siged.dao.ModuloDAO;
import br.com.siged.dao.MunicipioDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.UsuarioDAO;
import br.com.siged.dao.UsuarioSCADAO;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.EventoExtra;
import br.com.siged.entidades.MetaDesempenhoProdutividade;
import br.com.siged.entidades.MetaPlanejamentoEstrategico;
import br.com.siged.entidades.Inscricao;
import br.com.siged.entidades.Instrutor;
import br.com.siged.entidades.Modalidade;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.Usuario;
import br.com.siged.entidades.externas.Entidade;
import br.com.siged.entidades.externas.Municipio;
import br.com.siged.filtro.InscricaoFiltro;
import br.com.siged.mailing.MalaDireta;
import br.com.siged.service.EventoService;
import br.com.siged.util.HibernateUtil;
import br.com.siged.util.ValidatorUtil;
import br.com.siged.util.comparator.ModuloComparator;
import br.com.siged.util.comparator.ParticipanteComparator;

@Controller
@RequestMapping("/ajax/**")
public class AjaxController {
	
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private MunicipioDAO municipioDAO;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ModuloDAO moduloDao;
	@Autowired
	private InscricaoDAO inscricaoDao;
	@Autowired
	private EventoExtraDAO eventoextraDao;
	@Autowired
	private UsuarioSCADAO usuarioDao;
	@Autowired
	private UsuarioDAO usuarioexternoDao;
	@Autowired
	private EntidadeDAO entidadeDao;
	@Autowired
	private InstrutorDAO instrutorDao;
	@Autowired
	private EventoService eventoService;
	@Autowired
	private MetaPlanejamentoEstrategicoDAO indicadorPlanejamentoEstrategicoDao;
	@Autowired
	private MetaDesempenhoProdutividadeDAO indicadorDesempenhoProdutividadeDao;
	
	@Autowired
	private ValidatorUtil validatorUtil;
	
	@RequestMapping(value = "/ajax/procuraParticipantePorNome", method = RequestMethod.GET)
	@ResponseBody
	public List<Participante> procuraParticipantePorNome(@RequestParam(value="nome") String nome) {
		return participanteDao.findByNome(nome);
	}
	
	@RequestMapping(value = "/ajax/procuraParticipantePorNomeOuCPF", method = RequestMethod.GET)
	@ResponseBody
	public List<Participante> procuraParticipantePorNomeOuCPF(@RequestParam(value="nome") String nomeOuCpf) {
		if(validatorUtil.validarCPF(nomeOuCpf)) {
			List<Participante> participantes = new ArrayList<>();
			String cpf = nomeOuCpf.replace(".", "").replace("-", "");
			Participante participante = participanteDao.findByCpf(cpf);
			if(participante != null) {
				participantes.add(participante);
			}
			return participantes;
		}
		
		return participanteDao.findByNome(nomeOuCpf);
	}
	
	@RequestMapping(value = "/ajax/procuraEventoExportarParaAva", method = RequestMethod.GET)
	@ResponseBody
	public List<Evento> procuraEventoExportarParaAva(@RequestParam(value="titulo") String titulo) {
		return eventoDao.findForInscricoesSIGEDtoAVA(titulo);
	}
	
	@RequestMapping(value = "/ajax/procuraParticipanteExternoPorNome", method = RequestMethod.GET)
	@ResponseBody
	public List<Participante> procuraParticipanteExternoPorNome(@RequestParam(value="nome") String nome) {
		List<Participante> participantesComUsuarios = new ArrayList<>();
		for(Participante participante : participanteDao.findByNome(nome)) {
			if(participante.getTipoId().isJurisdicionado() || participante.getTipoId().isSociedade()) {
				Usuario usuario = usuarioexternoDao.findByCpf(participante.getCpf());
				if(usuario != null) {
					participantesComUsuarios.add(participante);
				}
			}
		}
		return participantesComUsuarios;
	}
	
	@RequestMapping(value = "/ajax/procuraOrgao", method = RequestMethod.GET)
    @ResponseBody
    public List<Entidade> procuraOrgao(@RequestParam(value = "id") Long id) {
		if (id.intValue() == 1)
			return entidadeDao.findEntidadesEstaduais();
		
		return entidadeDao.findEntidadeMunicipaisByLocalidade(id);		
	}
	
	@RequestMapping(value = "/ajax/procuraMunicipio", method = RequestMethod.GET)
    @ResponseBody
    public List<Municipio> procuraMunicipio(@RequestParam(value = "ufMunicipio") String ufMunicipio) {
		return municipioDAO.findByUfMunicipio(ufMunicipio);		
	}
	
	/**
	 * Chamada ajax que era usada para verificar a Modalidade do Evento na criação e edição do Módulo, 
	 * com objetivo de limitar a localização do módulo como INTERNET se o evento fosse EAD. <p/>
	 * 
	 * Com a depreciação do campo Modalidade no Evento em 07/12/2018 e a inclusão do mesmo no Módulo, 
	 * esta chamada não está sendo mais usada na criação e edição do Módulo.
	 * 
	 * @param eventoId
	 * @return
	 */
	@RequestMapping(value = "/ajax/procuraModalidadeDoEvento", method = RequestMethod.GET)
	@ResponseBody
	public Long procuraModalidadeDoEvento(@RequestParam(value="eventoId") Long eventoId) {
		
		Evento evento = eventoDao.findById(eventoId);
		/*
		 * Campo modalidade no evento foi depreciado.
		 * if(evento != null && evento.getModalidadeId() != null) return evento.getModalidadeId().getId();
		 */
		 if(evento != null) {
			 if(evento.isEAD()) return Modalidade.EAD;
			 if(evento.isPresencial()) return Modalidade.EAD;
		 }
		
		return -1L;
	}
		
	@RequestMapping(value = "/ajax/procuraParticipanteSolicitacaoSemInscricao", method = RequestMethod.GET)
	@ResponseBody
	public List<EventoExtra> procuraParticipanteSolicitacao(@RequestParam(value="participanteId") Long participanteId) {
		return eventoextraDao.findBySolicitanteSemInscricao(usuarioDao.findByCpf(participanteDao.find(participanteId).getCpf()));
	}

	@RequestMapping(value = "/ajax/procuraParticipantePorTipo", method = RequestMethod.GET)
	@ResponseBody
	public List<Participante> procuraParticipantePorTipoEEvento(@RequestParam(value="tipoId") Long tipoId) {
		return participanteDao.findByTipo(tipoId);
	}

	@RequestMapping(value = "/ajax/procuraParticipantePorTipoEEvento", method = RequestMethod.GET)
	@ResponseBody
	public List<Participante> procuraParticipantePorTipoEEvento(@RequestParam(value="tipoId") Long tipoId, @RequestParam(value="eventoId") Long eventoId) {
		return participanteDao.findByTipoAndEvento(tipoId, eventoId);
	}
	
	@RequestMapping(value = "/ajax/procuraInscricaoPendenteNoEvento", method = RequestMethod.GET)
	@ResponseBody
	public List<Inscricao> procuraInscricaoPendenteNoEvento(@RequestParam(value="eventoId") Long eventoId) {
		
		List<Inscricao> inscricoes = inscricaoDao.findByEventoAndConfirmadaOrdenadaPorParticipante(eventoId, "-");
		
		int ultimo = 0;
		
		if (inscricoes.size() > 0){
			if (inscricoes.size() < 100)
				ultimo = inscricoes.size();
			else
				ultimo = 100;
		}		 
		
		List<Inscricao> parteDasInscricoes = inscricoes.subList(0, ultimo);
		
		return parteDasInscricoes;
	}
	
	@RequestMapping(value = "/ajax/obterTotalDeVagasETotalDeInscritosNoEvento", method = RequestMethod.GET)
	@ResponseBody
	public int[] obterTotalDeVagasETotalDeInscritosNoEvento(@RequestParam(value="eventoId") Long eventoId) {
		
		int[] vagasEInscricoes = {0, 0};
		Evento evento = eventoDao.find(eventoId);
		
		vagasEInscricoes[0] = evento.getTotalVagas();
		
		List <Inscricao> inscricoesNoEvento = inscricaoDao.findByEventoAndConfirmada(eventoId, "S");
		if (inscricoesNoEvento != null)
			vagasEInscricoes[1] = inscricoesNoEvento.size();					
		
		return vagasEInscricoes;
	}
	
	/*
	 * URLs padronizadas para mapeamento no SPRING SECURITY
	 * 
	 * 
	 * pattern="/ajax/instrutor/procuraPorModulo/{moduloId}" access="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS"
	 * pattern="/ajax/modulo/procuraRealizadosPorEvento/{eventoId}" access="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS,ROLE_SERVIDOR,ROLE_CHEFE,ROLE_ALUNO"
	 * pattern="/ajax/participante/buscarInscritos" access="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS"
	 * pattern="/ajax/malaDireta/logEnvio" access="ROLE_ADMINISTRADOR"
	 * 
	 */
	
	@RequestMapping(value = "/ajax/instrutor/procuraPorModulo")
	@ResponseBody
	public List<Instrutor> procuraPorModulo(@RequestParam(value = "moduloId") Long moduloId) {
		List<Instrutor> instrutores = new ArrayList<>();
		Modulo modulo = moduloDao.findById(moduloId);
		if(modulo != null) {
			instrutores = instrutorDao.findByModulo(modulo.getId());
		}
		return instrutores;
	}
	
	@RequestMapping(value = "/ajax/modulo/procuraRealizadosPorEvento", method = RequestMethod.GET)
	@ResponseBody
	public List<Modulo> procuraRealizadosPorEvento(@RequestParam(value = "eventoId") Long eventoId) {
		List<Modulo> modulosRealizados = moduloDao.findRealizadosByEvento(eventoId);
		Collections.sort(modulosRealizados, ModuloComparator.OrderByDataInicio.asc());
		return modulosRealizados;
	}
	
	@RequestMapping(value = "/ajax/modulo/procuraPorEvento", method = RequestMethod.GET)
	@ResponseBody
	public List<Modulo> procuraModuloPorEvento(@RequestParam(value = "eventoId") Long eventoId) {
		List<Modulo> modulos = moduloDao.findByEventoId(eventoId);
		return modulos;
	}
	
	@RequestMapping(value = "/ajax/evento/isRealizado", method = RequestMethod.GET)
	@ResponseBody
	public boolean procuraEventoRealizado(@RequestParam(value="id") Long id) {
		
		Evento evento = eventoDao.find(id);
		
		if(evento != null)
			return eventoService.isRealizado(evento);
		
		return false;
	}
	
	@RequestMapping(value = "/ajax/evento/{id}/isCurso", method = RequestMethod.GET)
	@ResponseBody
	public boolean procuraEventoCurso(@PathVariable Long id) {
		
		Evento evento = eventoDao.find(id);
		
		if(evento != null)
			return evento.getTipoId().isCurso();
		
		return false;
	}
	
	/**
	 * 0 -> Não apurado
	 * 1 -> Apurado
	 * 2 -> Apurado parcialmente
	 * @param id
	 * @return status
	 */
	@RequestMapping(value = "/ajax/evento/statusApuracao", method = RequestMethod.GET)
	@ResponseBody
	public int procuraEventoApurado(@RequestParam(value="id") Long id) {
		int status = 0;
		Evento evento = eventoDao.find(id);
		Evento eventoApurado = null;
		
		if(evento != null) {
			eventoApurado = eventoDao.findByEventoApurado(id);
			
			if(eventoApurado != null)
				status = 1;
			else {
				eventoApurado = eventoDao.findByEventoApuradoParcial(id);
				if(eventoApurado != null)
					status = 2;
			}
		}
			
		return status;
	}
	
	/**
	 * Consulta os participantes de acordo com o filtro <tt>InscricaoFiltro</tt>. Essa consulta substitui as chamadas <tt>@ResponseBody</tt> a seguir:
	 * <ul>
	 * <li>{@link ParticipanteController#procuraParticipantes(Long)}</li>
	 * <li>{@link ParticipanteController#procuraParticipantePorTipoEEvento(Long)}</li>
	 * <li>{@link ParticipanteController#procuraParticipantePorTipoEEvento(Long, Long)}</li>
	 * </ul>
	 * </p>
	 * Caso informe não informe o evento no filtro, então irá buscar os participantes pelo tipo, independente da inscrição.
	 * 
	 * @param filtro Recebe os atributos da consulta via JSON
	 * @return Lista de participantes
	 * 
	 * @see {@link InscricaoFiltro}
	 */
	@RequestMapping(value = "/ajax/participante/buscarInscritos", headers = "Accept=application/json", method = RequestMethod.POST)
	@ResponseBody
	public List<Participante> procuraParticipanteByFiltro(@RequestBody InscricaoFiltro filtro) {		
		List<Participante> participantes = new ArrayList<>();
		
		if(filtro.getEvento() != null && filtro.getEvento() != 0) {
			for(Inscricao inscricao : inscricaoDao.findByFiltro(filtro)) {
				participantes.add(HibernateUtil.unproxy(inscricao.getParticipanteId()));
			}
		} else if(filtro.getTipoParticipanteId() != null && filtro.getTipoParticipanteId() != 0){
			participantes = participanteDao.findByTipo(filtro.getTipoParticipanteId());
		}
		
		Collections.sort(participantes, ParticipanteComparator.OrderByNome.asc());
		return participantes;
	}
	
	/**
	 * Acompanha o log de envio dos emails via {@link MalaDireta}
	 * @return Lista com os logs do tomcat referente a thread mala-direta-service
	 */
	@RequestMapping(value = "/ajax/malaDireta/logEnvio", method = RequestMethod.GET)
	@ResponseBody
	public List<String> acompanharEnvioMalaDireta() {
		return MalaDireta.log();
	}
	

	@RequestMapping(value = "/ajax/meta/indicadorPlanejamentoEstrategico/{ano}", method = RequestMethod.GET)
	@ResponseBody
	public MetaPlanejamentoEstrategico getIndicadorPorAno(@PathVariable Integer ano) {
		MetaPlanejamentoEstrategico indicador = indicadorPlanejamentoEstrategicoDao.findByAno(ano);
		return indicador;
	}
	
	@RequestMapping(value = "/ajax/meta/indicadorDesempenhoProdutividade/{ano}/{semestre}", method = RequestMethod.GET)
	@ResponseBody
	public MetaDesempenhoProdutividade getIndicadorPorAnoAndSemestre(@PathVariable Integer ano, @PathVariable Integer semestre) {
		MetaDesempenhoProdutividade indicador = indicadorDesempenhoProdutividadeDao.findByAnoAndSemestre(ano, semestre);
		return indicador;
	}

	/**
	 * Acompanha o progresso de envio dos emails via {@link MalaDireta}
	 * @return Double com valor de progresso de envio
	 */
	@RequestMapping(value = "/ajax/malaDireta/progresso", method = RequestMethod.GET)
	@ResponseBody
	public Double progressoEnvioMalaDireta() {
		if(MalaDireta.isAlive()) {
			return MalaDireta.progresso();
		}
		return 0.0;
	}
	
}
