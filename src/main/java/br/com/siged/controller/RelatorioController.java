package br.com.siged.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.siged.dao.DesempenhoDAO;
import br.com.siged.dao.EixoTematicoDAO;
import br.com.siged.dao.EntidadeDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.EventoExtraDAO;
import br.com.siged.dao.GastoDAO;
import br.com.siged.dao.InstrutorDAO;
import br.com.siged.dao.LocalidadeDAO;
import br.com.siged.dao.MetaPlanejamentoEstrategicoDAO;
import br.com.siged.dao.ModalidadeDAO;
import br.com.siged.dao.ModuloDAO;
import br.com.siged.dao.MunicipioDAO;
import br.com.siged.dao.PaisDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.ProgramaDAO;
import br.com.siged.dao.ProvedorEventoDAO;
import br.com.siged.dao.RelatorioDAO;
import br.com.siged.dao.SetorDAO;
import br.com.siged.dao.TipoEventoDAO;
import br.com.siged.dao.TipoGastoDAO;
import br.com.siged.dao.TipoLocalizacaoEventoDAO;
import br.com.siged.dao.TipoPublicoAlvoDAO;
import br.com.siged.dao.UfMunicipioDAO;
import br.com.siged.dao.UsuarioSCADAO;
import br.com.siged.editor.ProvedorEventoEditor;
import br.com.siged.entidades.EixoTematico;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Gasto;
import br.com.siged.entidades.Inscricao;
import br.com.siged.entidades.Instrutor;
import br.com.siged.entidades.MetaPlanejamentoEstrategico;
import br.com.siged.entidades.Modalidade;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Pais;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.Programa;
import br.com.siged.entidades.ProvedorEvento;
import br.com.siged.entidades.RelatorioBean;
import br.com.siged.entidades.StatusEvento;
import br.com.siged.entidades.TipoEvento;
import br.com.siged.entidades.TipoGasto;
import br.com.siged.entidades.TipoLocalizacaoEvento;
import br.com.siged.entidades.TipoPublicoAlvo;
import br.com.siged.entidades.externas.Entidade;
import br.com.siged.entidades.externas.Setor;
import br.com.siged.entidades.externas.UfMunicipio;
import br.com.siged.entidades.externas.UsuarioSCA;
import br.com.siged.filtro.EventoFiltro;
import br.com.siged.filtro.RelatorioFiltro;
import br.com.siged.service.RelatorioService;
import br.com.siged.service.exception.BusinessException;
import br.com.siged.service.exception.DataInvalidaException;
import br.com.siged.service.exception.SemestreInvalidoException;
import br.com.siged.util.StringUtil;
import br.com.siged.util.Util;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@RequestMapping("/relatorio/**")
public class RelatorioController {

	@Autowired
	private UsuarioSCADAO usuarioDao;
	@Autowired
	private EntidadeDAO entidadeDao;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private GastoDAO gastoDao;
	@Autowired
	private TipoGastoDAO tipoGastoDao;
	@Autowired
	private InstrutorDAO instrutorDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private PaisDAO paisDao;	
	@Autowired
	private ModalidadeDAO modalidadeDao;
	@Autowired
	private ProvedorEventoDAO provedoreventoDao;	
	@Autowired
	private TipoLocalizacaoEventoDAO tipoLocalizacaoEventoDao;
	@Autowired
	private TipoPublicoAlvoDAO tipoPublicoAlvoDao;	
	@Autowired
	private ModuloDAO moduloDao;
	@Autowired
	private TipoEventoDAO tipoEventoDao;	
	@Autowired
	private SetorDAO setorDao;		
	@Autowired
	private DesempenhoDAO desempenhoDao;
	@Autowired
	private EventoExtraDAO eventoExtraDao;
	@Autowired
	private EixoTematicoDAO eixotematicoDao;
	@Autowired
	private ProgramaDAO programaDao;
	@Autowired
	private UfMunicipioDAO ufMunicipioDao;
	@Autowired
	private MunicipioDAO municipioDao;
	@Autowired
	private Util util;	
	@Autowired
	private RelatorioDAO relatorioDao;
	@Autowired
	private RelatorioService relatorioService;
	@Autowired
	private LocalidadeDAO localidadeDao;
	@Autowired
	private MetaPlanejamentoEstrategicoDAO indicadorPlanejamentoEstrategicoDao;
	
	
	/**
	 * Usar somente quando não precisar comparar hora
	 */
	private final static String PARCIAL_SYSDATE = "TO_DATE(TO_CHAR(SYSDATE, 'DD/MM/YYYY'), 'DD/MM/YYYY')";
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
		dataBinder.registerCustomEditor(br.com.siged.entidades.ProvedorEvento.class, new ProvedorEventoEditor(provedoreventoDao));
	}

	// ..................GETS DE TODOS OS RELATORIOS..................
	
	// AVALIACOES ATIVIDAES TRIMESTRAL E ANUAL DO IPC

	@RequestMapping(value = "/relatorio/atividadesTrimestralAnualIPC", method = RequestMethod.GET)
	public String atividadesTrimestralAnualIPC(ModelMap modelMap) {
		modelMap.addAttribute("relAtividadesTrimestralAnualIPC", new RelatorioFiltro());
		return "relatorio/atividadesTrimestralAnualIPC";
	}

	// AVALIACOES DE EFICACIA DO CHEFE POR EVENTO/PARTICIPANTE

	@RequestMapping(value = "/relatorio/avaliacoesEficaciaTreinamentoEventoParticipante", method = RequestMethod.GET)
	public String avaliacoesEficaciaTreinamentoEventoParticipante(ModelMap modelMap) {
		modelMap.addAttribute("relAvaliacoesEficaciaTreinamentoEventoParticipante",	new RelatorioFiltro());
		return "relatorio/avaliacoesEficaciaTreinamentoEventoParticipante";
	}

	// AVALIAÇÕES DE EFICACIA POR EVENTO

	@RequestMapping(value = "/relatorio/avaliacoesEficaciaPorEvento", method = RequestMethod.GET)
	public String avaliacoesEficaciaPorEvento(ModelMap modelMap) {
		modelMap.addAttribute("eventosComEficaciaAvaliada", eventoDao.findEventoEficaciaAvaliada());
		modelMap.addAttribute("relAvaliacoesEficaciaPorEvento",	new RelatorioFiltro());
		return "relatorio/avaliacoesEficaciaPorEvento";
	}

	// AVALIACOES REACAO PARTICIPANTE POR EVENTO

	@RequestMapping(value = "/relatorio/avaliacoesReacaoParticipantePorEvento", method = RequestMethod.GET)
	public String avaliacoesReacaoParticipantePorEvento(ModelMap modelMap) {
		modelMap.addAttribute("eventoAvaliadoList", eventoDao.findByEventoAvaliado());
		modelMap.addAttribute("relAvaliacoesReacaoParticipantePorEvento", new RelatorioFiltro());
		return "relatorio/avaliacoesReacaoParticipantePorEvento";
	}	

	// CRACHAS

	@RequestMapping(value = "/relatorio/impressaoCrachas", method = RequestMethod.GET)
	public String crachas(ModelMap modelMap) {
		modelMap.addAttribute("relCracha", new RelatorioFiltro());
		return "relatorio/crachas";
	}

	// CRONOGRAMA

	@RequestMapping(value = "/relatorio/cronograma", method = RequestMethod.GET)
	public String cronograma(ModelMap modelMap) {
		modelMap.addAttribute("relCronograma", new RelatorioFiltro());
		return "relatorio/cronograma";
	}

	// DECLARACOES

	@RequestMapping(value = "/relatorio/impressaoDeclaracoes", method = RequestMethod.GET)
	public String declaracoes(ModelMap modelMap) {
		modelMap.addAttribute("relDeclaracao", new RelatorioFiltro());
		return "relatorio/declaracao";
	}

	// EVENTOS

	@RequestMapping(value = "/relatorio/eventos", method = RequestMethod.GET)
	public String eventos(ModelMap modelMap) {
		RelatorioFiltro filtro = new RelatorioFiltro();
		filtro.setIncluirTipo1(true);
		filtro.setIncluirTipo2(true);
		filtro.setIncluirTipo3(true);
		filtro.setIncluirTipo4(true);
		filtro.setIncluirTipoParticipante1(true);
		filtro.setIncluirTipoParticipante2(true);
		filtro.setIncluirTipoParticipante3(true);
		filtro.setIncluirTipoParticipante4(true);
		modelMap.addAttribute("relEventos", filtro);
		return "relatorio/eventos";
	}

	// EVENTOS POR INSTRUTOR

	@RequestMapping(value = "/relatorio/eventosPorInstrutor", method = RequestMethod.GET)
	public String eventosPorInstrutor(ModelMap modelMap) {
		modelMap.addAttribute("relEventosPorInstrutor", new RelatorioFiltro());
		return "relatorio/eventosPorInstrutor";
	}

	// EVENTOS POR PARTICIPANTE

	@RequestMapping(value = "/relatorio/eventosPorParticipante", method = RequestMethod.GET)
	public String eventosPorParticipante(ModelMap modelMap,	HttpServletRequest request) {
		modelMap.addAttribute("relEventosPorParticipante", new RelatorioFiltro());
		return "relatorio/eventosPorParticipante";
	}

	// FICHA TÉCNICA DE EVENTO

	@RequestMapping(value = "/relatorio/fichaTecnicaDeEvento", method = RequestMethod.GET)
	public String fichaTecnica(ModelMap modelMap) {
		modelMap.addAttribute("relFichaTecnica", new RelatorioFiltro());
		return "relatorio/fichaTecnicaDeEvento";
	}

	// GASTOS

	@RequestMapping(value = "/relatorio/gastos", method = RequestMethod.GET)
	public String gastos(ModelMap modelMap) {
		modelMap.addAttribute("relGastos", new RelatorioFiltro());
		modelMap.addAttribute("localidadeList", localidadeDao.findByMunicipios());
		return "relatorio/gastos";
	}

	// IMPRESSAO DE CAPAS
	/*
	@RequestMapping(value = "/relatorio/impressaoDeCapas", method = RequestMethod.GET)
	public String impressaoDeCapas(ModelMap modelMap) {
		modelMap.addAttribute("relImpressaoDeCapas", new RelatorioFiltro());
		return "relatorio/impressaoDeCapas";
	}

	// IMPRESSAO DE CAPAS - AVALIACAO REACAO

	@RequestMapping(value = "/relatorio/impressaoDeCapas/avaliacaoReacao", method = RequestMethod.GET)
	public String avaliacaoReacao(ModelMap modelMap) {		
		modelMap.addAttribute("relImpressaoDeCapasAvaliacaoReacao", new RelatorioFiltro());
		return "relatorio/impressaoDeCapas/avaliacaoReacao";
	}

	// IMPRESSAO DE CAPAS - AVALIACAO EFICACIA

	@RequestMapping(value = "/relatorio/impressaoDeCapas/avaliacaoEficacia", method = RequestMethod.GET)
	public String avaliacaoEficacia(ModelMap modelMap) {
		modelMap.addAttribute("relImpressaoDeCapasAvaliacaoEficacia", new RelatorioFiltro());
		return "relatorio/impressaoDeCapas/avaliacaoEficacia";
	}

	// IMPRESSAO DE CAPAS - FREQUENCIA

	@RequestMapping(value = "/relatorio/impressaoDeCapas/frequencia", method = RequestMethod.GET)
	public String frequencia(ModelMap modelMap) {
		modelMap.addAttribute("relImpressaoDeCapasFrequencia", new RelatorioFiltro());
		return "relatorio/impressaoDeCapas/frequencia";
	}

	// IMPRESSAO DE CAPAS - INDICACAO PARA TREINAMENTO

	@RequestMapping(value = "/relatorio/impressaoDeCapas/indicacao", method = RequestMethod.GET)
	public String indicacao(ModelMap modelMap) {
		modelMap.addAttribute("relImpressaoDeCapasIndicacao", new RelatorioFiltro());
		return "relatorio/impressaoDeCapas/indicacao";
	}
	*/
	// INDICADORES

	@RequestMapping(value = "/relatorio/indicadoresPlanejamento", method = RequestMethod.GET)
	public String indicadoresPlanejamento(ModelMap modelMap) {
		modelMap.addAttribute("relIndicadoresPlanejamento", new RelatorioFiltro());
		return "relatorio/indicadoresPlanejamento";
	}
	
	@RequestMapping(value = "/relatorio/indicadoresDesempenho", method = RequestMethod.GET)
	public String indicadoresDesempenho(ModelMap modelMap) {
		modelMap.addAttribute("relIndicadoresDesempenho", new RelatorioFiltro());
		return "relatorio/indicadoresDesempenho";
	}
	
	@RequestMapping(value = "/relatorio/indicadoresOutros", method = RequestMethod.GET)
	public String indicadoresOutros(ModelMap modelMap) {
		modelMap.addAttribute("relIndicadoresOutros", new RelatorioFiltro());
		return "relatorio/indicadoresOutros";
	}

	// INDICADORES SETORIAIS

	@RequestMapping(value = "/relatorio/indicadoresSetoriais", method = RequestMethod.GET)
	public String indicadoresSetoriais(ModelMap modelMap) {
		modelMap.addAttribute("relIndicadoresSetoriais", new RelatorioFiltro());
		return "relatorio/indicadoresSetoriais";
	}

	// INDICES

	@RequestMapping(value = "/relatorio/indices", method = RequestMethod.GET)
	public String indices(ModelMap modelMap) {
		modelMap.addAttribute("relIndice", new RelatorioFiltro());
		return "relatorio/indices";
	}

	// INDICES - AVALIACAO EFICACIA

	@RequestMapping(value = "/relatorio/indices/avaliacaoEficacia", method = RequestMethod.GET)
	public String IndiceAvaliacaoEficacia(ModelMap modelMap) {
		modelMap.addAttribute("relIndiceAvaliacaoEficacia", new RelatorioFiltro());
		return "relatorio/indices/avaliacaoEficacia";
	}

	// INDICES - AVALIACAO REACAO

	@RequestMapping(value = "/relatorio/indices/avaliacaoReacao", method = RequestMethod.GET)
	public String indiceAvaliacaoReacao(ModelMap modelMap) {
		modelMap.addAttribute("relIndiceAvaliacaoReacao", new RelatorioFiltro());
		return "relatorio/indices/avaliacaoReacao";
	}

	// INDICES - FREQUENCIA

	@RequestMapping(value = "/relatorio/indices/frequencia", method = RequestMethod.GET)
	public String indiceFrequencia(ModelMap modelMap) {
		modelMap.addAttribute("relIndiceFrequencia", new RelatorioFiltro());
		return "relatorio/indices/frequencia";
	}

	// INDICES - INDICACAO

	@RequestMapping(value = "/relatorio/indices/indicacao", method = RequestMethod.GET)
	public String indiceIndicacao(ModelMap modelMap) {
		modelMap.addAttribute("relIndiceIndicacao", new RelatorioFiltro());
		return "relatorio/indices/indicacao";
	}

	// INSCRICOES

	@RequestMapping(value = "/relatorio/inscricoes", method = RequestMethod.GET)
	public String inscricoes(ModelMap modelMap) {
		modelMap.addAttribute("relInscricoes", new RelatorioFiltro());
		return "relatorio/inscricoes";
	}	

	// PARTICIPANTES

	@RequestMapping(value = "/relatorio/participantes", method = RequestMethod.GET)
	public String participantes(ModelMap modelMap) {
		modelMap.addAttribute("relParticipantes", new RelatorioFiltro());
		modelMap.addAttribute("localidadeList", localidadeDao.findByMunicipios());
		return "relatorio/participantes";
	}

	// PARTICIPANTES EXTERNOS

	@RequestMapping(value = "/relatorio/participantesExternos", method = RequestMethod.GET)
	public String participantesExternos(ModelMap modelMap) {
		modelMap.addAttribute("relParticipantesExternos", new RelatorioFiltro());
		modelMap.addAttribute("localidadeList", localidadeDao.findByMunicipios());
		return "relatorio/participantesExternos";
	}

	// @deprecated PARTICIPANTES POR EVENTO

	/**
	 * @deprecated Relatório é chamado usando a jsp relatorio/participantesPorEvento, mas não existe mais link para esse endpoint no sistema. 
	 * Portanto o mesmo é passível de remoção.
	 */
	@Deprecated
	@RequestMapping(value = "/relatorio/participantesPorEvento", method = RequestMethod.GET)
	public String participantesPorEvento(ModelMap modelMap) {
		modelMap.addAttribute("relParticipantesPorEvento", new RelatorioFiltro());
		return "relatorio/participantesPorEvento";
	}

	/**
	 * @deprecated Relatório é chamado usando a jsp relatorio/participantesPorEvento, mas não existe mais link para esse endpoint no sistema. 
	 * Portanto o mesmo é passível de remoção.
	 */
	@Deprecated
	@RequestMapping(value = "/relatorio/participantesPorEvento", method = RequestMethod.POST)
	public void participantesPorEvento(RelatorioFiltro relatorioFiltro,
			ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/participantes_por_evento.jasper");

		String paramWhere = "WHERE 1=1";
		if (relatorioFiltro.getEventoId() != 0) {
			paramWhere += " and evento.id = " + relatorioFiltro.getEventoId().toString();
		}
		if (relatorioFiltro.getPublicoAlvoId() != 0) {
			paramWhere += " and tipo_publico_alvo.id = " + relatorioFiltro.getPublicoAlvoId().toString();
		}

		paramWhere += " ORDER BY participante.nome";
	
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paramWhere", paramWhere);

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "participantesPorEvento", response);
	}

	// PARTICIPANTES POR TIPO POR EVENTO

	@RequestMapping(value = "/relatorio/participantesPorTipoPorEvento", method = RequestMethod.GET)
	public String participantesPorTipoPorEvento(ModelMap modelMap) {
		modelMap.addAttribute("relparticipantesPorTipoPorEvento", new RelatorioFiltro());
		return "relatorio/participantesPorTipoPorEvento";
	}

	// REGISTRO DE FREQUENCIA

	@RequestMapping(value = "/relatorio/registroFrequencia", method = RequestMethod.GET)
	public String registroFrequencia(ModelMap modelMap) {
		modelMap.addAttribute("relRegistroFrequencia", new RelatorioFiltro());
		return "relatorio/registroFrequencia";
	}

	// SOLICITAÇÕES

	@RequestMapping(value = "/relatorio/solicitacoes", method = RequestMethod.GET)
	public String solicitacoes(ModelMap modelMap) {
		modelMap.addAttribute("relSolicitacoes", new RelatorioFiltro());
		modelMap.addAttribute("solicitacoesList", eventoExtraDao.findAll());
		modelMap.addAttribute("usuarioList", usuarioDao.findAll());
		return "relatorio/solicitacoes";
	}

	
	
	
	// ..................POSTS DE TODOS OS RELATORIOS..................
	
	// AVALIACOES ATIVIDAES TRIMESTRAL E ANUAL DO IPC

	@RequestMapping(value = "/relatorio/atividadesTrimestralAnualIPC", method = RequestMethod.POST)
	public String atividadesTrimestralAnualIPC(@ModelAttribute("relAtividadesTrimestralAnualIPC") @Valid RelatorioFiltro relatorioFiltro, BindingResult result, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		
		if (relatorioFiltro.getDataInicio2() == null) {
			result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Campo obrigatório"));
		}
		if (relatorioFiltro.getDataInicio1() == null) {
			result.addError(new FieldError("relatorioFiltro", "dataInicio1", "Campo obrigatório"));
		}
		if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() != null) {
			if (relatorioFiltro.getDataInicio1().after(relatorioFiltro.getDataInicio2())) {
				result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período inválido"));
				result.addError(new FieldError("relatorioFiltro", "dataInicio1", ""));
			}
		}

		if (result.hasErrors()) {
			return "relatorio/atividadesTrimestralAnualIPC";
		}
		
		byte[] relatorio = relatorioService.atividadesTrimestralAnualIPC(relatorioFiltro.getDataInicio1(), relatorioFiltro.getDataInicio2());
		relatorioService.abrirTXT(relatorio, "informacoes_para_relatorio_de_atividades_ipc", response);
		
		return null;
	}

	// AVALIACOES DE EFICACIA DO CHEFE POR EVENTO/PARTICIPANTE

	@RequestMapping(value = "/relatorio/avaliacoesEficaciaTreinamentoEventoParticipante", method = RequestMethod.POST)
	public String avaliacoesEficaciaTreinamentoEventoParticipante(
			@ModelAttribute("relAvaliacoesEficaciaTreinamentoEventoParticipante") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {

		if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() == null) {
			result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período incompleto"));
		}
		if (relatorioFiltro.getDataInicio1() == null && relatorioFiltro.getDataInicio2() != null) {
			result.addError(new FieldError("relatorioFiltro", "dataInicio1", "Período incompleto"));
		}
		if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() != null) {
			if (relatorioFiltro.getDataInicio1().after(relatorioFiltro.getDataInicio2())) {
				result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período inválido"));
				result.addError(new FieldError("relatorioFiltro", "dataInicio1", ""));
			}
		}

		if (result.hasErrors()) {
			return "relatorio/avaliacoesEficaciaTreinamentoEventoParticipante";
		}

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/avaliacoes_eficacia_chefe_por_evento_participante.jasper");

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		String paramWhere = "WHERE 1=1";
		if (relatorioFiltro.getEventoId() != null	&& relatorioFiltro.getEventoId() != 0) {
			paramWhere += " and evento.id = " + relatorioFiltro.getEventoId().toString();
			parameters.put("nomeEvento", eventoDao.findById(relatorioFiltro.getEventoId()).getNome());
		}
		if (relatorioFiltro.getDataInicio1() != null) {
			paramWhere += " and evento.data_inicio_realizacao >= TO_DATE('"	+ util.formataData(relatorioFiltro.getDataInicio1()) + "', 'dd/MM/yyyy')";
			parameters.put("data1",	util.formataData(relatorioFiltro.getDataInicio1()));
		}
		if (relatorioFiltro.getDataInicio2() != null) {
			paramWhere += " and evento.data_inicio_realizacao <= TO_DATE('"	+ util.formataData(relatorioFiltro.getDataInicio2()) + "', 'dd/MM/yyyy')";
			parameters.put("data2",	util.formataData(relatorioFiltro.getDataInicio2()));
		}
		if (relatorioFiltro.getParticipanteId() != 0) {
			paramWhere += " and avaliacao_eficacia.participante_id = " + relatorioFiltro.getParticipanteId().toString();
			parameters.put("participante", participanteDao.findById(relatorioFiltro.getParticipanteId()).getNomeCpf());
		}
		
		parameters.put("paramWhere", paramWhere);

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);

		if (relatorio.length < 1024) {
			modelMap.addAttribute("relAvaliacoesEficaciaTreinamentoEventoParticipante",	relatorioFiltro);
			modelMap.addAttribute("mensagemRel", "Os filtros selecionados não geraram resultado.");
			return "relatorio/avaliacoesEficaciaTreinamentoEventoParticipante";
		}

		relatorioService.abrirPdf(relatorio, "avaliacoes_eficacia_treinamento_evento_participante", response);

		return null;
	}

	// AVALIACOES DE EFICACIA POR EVENTO

	@RequestMapping(value = "/relatorio/avaliacoesEficaciaPorEvento", method = RequestMethod.POST)
	public String avaliacoesEficaciaPorEvento(
			@ModelAttribute("relAvaliacoesEficaciaPorEvento") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		if (relatorioFiltro.getEventoId() == null	|| relatorioFiltro.getEventoId() == 0) {
			result.addError(new FieldError("relatorioFiltro", "eventoId", "Campo Obrigatório"));
		} else {
			parameters.put("paramWhere", relatorioFiltro.getEventoId().toString());
			parameters.put("nomeEvento", eventoDao.findById(relatorioFiltro.getEventoId()).getNome());
		}

		if (result.hasErrors()) {
			modelMap.addAttribute("eventoCujaEficaciaDeveSerAvaliada", eventoDao.findByEventosCujaEficaciaDeveSerAvaliada());
			return "relatorio/avaliacoesEficaciaPorEvento";
		}

		InputStream is = null;

		if (eventoDao.findById(relatorioFiltro.getEventoId()).temProvedorIPC()) {
			is = this.getClass().getClassLoader().getResourceAsStream("reports/avaliacao_eficacia_por_evento.jasper");
		} else {
			is = this.getClass().getClassLoader().getResourceAsStream("reports/avaliacao_eficacia_por_evento_externo.jasper");
		}

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "relatorio_avaliacoes_de_eficacia_por_evento", response);

		return null;

	}

	
	// AVALIACOES REACAO PARTICIPANTE POR EVENTO
	
	@RequestMapping(value = "/relatorio/avaliacoesReacaoParticipantePorEvento", method = RequestMethod.POST)
	public String avaliacoesReacaoParticipantePorEventoNovo(
			@ModelAttribute("relAvaliacoesReacaoParticipantePorEvento") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {

		if (relatorioFiltro.getEventoId() == null || relatorioFiltro.getEventoId() == 0) {
			result.addError(new FieldError("relatorioFiltro", "eventoId",	"Campo Obrigatório"));
		}

		if (result.hasErrors()) {
			modelMap.addAttribute("eventoAvaliadoList", eventoDao.findByEventoAvaliado());
			return "/relatorio/avaliacoesReacaoParticipantePorEvento";
		}
		
		relatorioService.gerarRelatorioAvaliacoesReacaoParticipantePorEvento(relatorioFiltro, response);
		
		return null;
	}
	

	// CRACHAS

	@RequestMapping(value = "/relatorio/crachas", method = RequestMethod.POST)
	public String crachas(
			@ModelAttribute("relCracha") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {

		if (relatorioFiltro.getEventoId() == null	|| relatorioFiltro.getEventoId() == 0) {
			result.addError(new FieldError("relatorioFiltro", "eventoId", "Campo Obrigatório"));
		}

		if (result.hasErrors()) {
			return "/relatorio/crachas";
		}

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/cracha.jasper");

		String paramWhere = "WHERE evento.id = "
				+ relatorioFiltro.getEventoId().toString()
				+ " AND inscricao.confirmada = 'S'";

		if (relatorioFiltro.getParticipanteId() != 0) {
			paramWhere += " AND participante.id = "	+ relatorioFiltro.getParticipanteId().toString();
		}

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paramWhere", paramWhere);

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);

		if (relatorio.length < 1024) {
			modelMap.addAttribute("relCracha", relatorioFiltro);
			modelMap.addAttribute("mensagemRel", "Os filtros selecionados não geraram resultado.");
			return "relatorio/crachas";
		}

		relatorioService.abrirPdf(relatorio, "crachas", response);

		return null;
	}

	// CRONOGRAMA

	@RequestMapping(value = "/relatorio/cronograma", method = RequestMethod.POST)
	public void cronograma(RelatorioFiltro relatorioFiltro, ModelMap modelMap,
			ServletRequest request, HttpServletResponse response)
			throws IOException {

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/cronograma.jasper");

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		String param = "FROM evento, tipo_evento, modulo";
		if (relatorioFiltro.getPublicoAlvoId() != 0) {
			param += ", evento_tipopublico";
			param += " WHERE evento_tipopublico.evento_id = evento.id";
			param += " AND evento_tipopublico.publico_alvo_id = " + relatorioFiltro.getPublicoAlvoId();
		} else {
			param += " WHERE 1=1";
		}

		param += " AND evento.tipo_id = tipo_evento.id AND modulo.evento_id = evento.id";

		if (Integer.parseInt(relatorioFiltro.getAno()) != 0) {
			param += " AND EXTRACT(YEAR FROM evento.data_inicio_previsto) = '" + relatorioFiltro.getAno() + "'";
		}

		if (relatorioFiltro.getModalidadeId() != 0) {
			String condicionalModalidadeDoEvento = "";
			if(relatorioFiltro.getModalidadeId().equals(Modalidade.PRESENCIAL))
				condicionalModalidadeDoEvento = " AND evento.id IN (SELECT e.id from modulo m INNER JOIN evento e on m.evento_id = e.id WHERE m.modalidade_id = 1)";
			else if( relatorioFiltro.getModalidadeId().equals(Modalidade.EAD))
				condicionalModalidadeDoEvento = " AND evento.id NOT IN (SELECT e.id from modulo m INNER JOIN evento e on m.evento_id = e.id WHERE m.modalidade_id = 1)";
			
			/*
			 * DONE: __Modalidade Evento: Definir qual será a Modalidade do Evento na consulta (SQL)
			 * @deprecated Consulta agora verifica se o evento é presencial por meio do módulo
			param += " AND evento.modalidade_id = "	+ relatorioFiltro.getModalidadeId().toString();
			 */
			param += condicionalModalidadeDoEvento;
			
			parameters.put("modalidade", modalidadeDao.find(relatorioFiltro.getModalidadeId()).getDescricao());
		} else {
			parameters.put("modalidade", "TODOS");
		}

		param += " ORDER BY evento.data_inicio_previsto";
		
		parameters.put("param", param);
		parameters.put("ano", relatorioFiltro.getAno());
		if (relatorioFiltro.getPublicoAlvoId() != 0) {
			parameters.put("tipo", tipoPublicoAlvoDao.find(relatorioFiltro.getPublicoAlvoId()).getDescricao());
		} else {
			parameters.put("tipo", "TODOS");
		}

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "cronograma", response);
	}

	// DECLARACAO

	@RequestMapping(value = "/relatorio/declaracao", method = RequestMethod.POST)
	public String declaracoes(
			@ModelAttribute("relDeclaracao") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {

		if (relatorioFiltro.getEventoId() == null	|| relatorioFiltro.getEventoId() == 0) {
			result.addError(new FieldError("relatorioFiltro", "eventoId",	"Campo Obrigatório"));
		}

		if (relatorioFiltro.getParticipanteId() == null
				|| relatorioFiltro.getParticipanteId() == 0) {
			result.addError(new FieldError("relatorioFiltro", "participanteId", "Campo Obrigatório"));
		}

		if (result.hasErrors()) {
			return "/relatorio/declaracao";
		}

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/declaracao.jasper");

		String paramWhere = "WHERE 1=1";
		if (relatorioFiltro.getEventoId() != 0) {
			paramWhere += " and evento.id = " + relatorioFiltro.getEventoId().toString();
		}
		if (relatorioFiltro.getParticipanteId() != 0) {
			paramWhere += " and participante.id = " + relatorioFiltro.getParticipanteId().toString();
		}

		paramWhere += " ORDER BY evento.titulo";
		
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("assinatura", "images/assinatura_hilaria.jpg");
		parameters.put("paramWhere", paramWhere);

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "declaracao_de_participante", response);

		return null;
	}

	// EVENTOS

	@RequestMapping(value = "/relatorio/eventos", method = RequestMethod.POST)
	public String eventos( 
			@ModelAttribute("relEventos") 
			@Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		if (relatorioFiltro.getDataInicioPrevisto1() != null && relatorioFiltro.getDataInicioPrevisto2() == null) {
			result.addError(new FieldError("relatorioFiltro", "dataInicioPrevisto2", "Período incompleto"));
		}
		if (relatorioFiltro.getDataInicioPrevisto1() == null && relatorioFiltro.getDataInicioPrevisto2() != null) {
			result.addError(new FieldError("relatorioFiltro",	"dataInicioPrevisto1", "Período incompleto"));
		}
		if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() == null) {
			result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período incompleto"));
		}
		if (relatorioFiltro.getDataInicio1() == null && relatorioFiltro.getDataInicio2() != null) {
			result.addError(new FieldError("relatorioFiltro", "dataInicio1", "Período incompleto"));
		}
		if (relatorioFiltro.getDataInicioPrevisto1() != null && relatorioFiltro.getDataInicioPrevisto2() != null) {
			if (relatorioFiltro.getDataInicioPrevisto1().after(relatorioFiltro.getDataInicioPrevisto2())) {
				result.addError(new FieldError("relatorioFiltro", "dataInicioPrevisto2", "Período inválido"));
			}
		}
		if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() != null) {
			if (relatorioFiltro.getDataInicio1().after(relatorioFiltro.getDataInicio2())) {
				result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período inválido"));
			}
		}

		if (result.hasErrors()) {
			return "relatorio/eventos";
		}

		InputStream is;

		if (relatorioFiltro.getAutomatico())
			is = this.getClass().getClassLoader().getResourceAsStream("reports/eventos_com_qntd_aprovados.jasper");
		else		
			is = this.getClass().getClassLoader().getResourceAsStream("reports/eventos.jasper");

		ClassLoader clazzLoader = RelatorioController.class.getClassLoader();
		Properties propriedades = new Properties();
		propriedades.load(clazzLoader.getResourceAsStream("messages.properties"));
		
		String tipoEvento = null;
		String tituloEvento = null;
		String publicoAlvo = "";
		String eixoTematico = null;
		String localizacao = null;
		String provedor = null;
		String programa = null;
		String cidade = null;
		String uf = null;
		String pais = null;
		String periodoPrevisto1 = null;
		String periodoPrevisto2 = null;
		String periodoRealizacao1 = null;
		String periodoRealizacao2 = null;
		String situacao = null;
		String modalidade = null;
		String tipoParticipante = null;
		String tipoParticipanteWhere = "";
		StringBuilder paramWhere = new StringBuilder("WHERE 1 = 1");
		String subQuery =  " SELECT      tpa2.id"
	                     + " FROM        EVENTO_TIPOPUBLICO etp2"
	                     + " INNER JOIN  EVENTO e2                    ON etp2.EVENTO_ID = e2.id"
	                     + " INNER JOIN  TIPO_PUBLICO_ALVO tpa2       ON etp2.PUBLICO_ALVO_ID = tpa2.ID"                                                                   
	                     + " WHERE       e2.ID                        = e.id";
		
		if(!relatorioFiltro.getIncluirTipo1() || !relatorioFiltro.getIncluirTipo2() || !relatorioFiltro.getIncluirTipo3() || !relatorioFiltro.getIncluirTipo4()) {
		
			paramWhere.append(" AND ( ");
			
			if (relatorioFiltro.getIncluirTipo1()) {
				paramWhere.append(" 1 IN (" + subQuery + ")");
				publicoAlvo += propriedades.getProperty("tipoPublicoAlvo.servidor") + ", ";
			}			
			if (relatorioFiltro.getIncluirTipo2()) {
				if (relatorioFiltro.getIncluirTipo1()) {
					paramWhere.append(" OR 2 IN (" + subQuery + ")");
				} else {
					paramWhere.append(" 2 IN (" + subQuery + ")");
				}				
				publicoAlvo += propriedades.getProperty("tipoPublicoAlvo.jurisdicionado") + ", ";
			}		
			if (relatorioFiltro.getIncluirTipo3()) {
				if (relatorioFiltro.getIncluirTipo1() || relatorioFiltro.getIncluirTipo2()) {
					paramWhere.append(" OR 3 IN (" + subQuery + ")");
				} else {
					paramWhere.append(" 3 IN (" + subQuery + ")");
				}				
				publicoAlvo += propriedades.getProperty("tipoPublicoAlvo.sociedade") + ", ";
			}
			if (relatorioFiltro.getIncluirTipo4()) {
				if (relatorioFiltro.getIncluirTipo1() || relatorioFiltro.getIncluirTipo2() || relatorioFiltro.getIncluirTipo3()) {
					paramWhere.append(" OR 4 IN (" + subQuery + ")");
				} else {
					paramWhere.append(" 4 IN (" + subQuery + ")");
				}				
				publicoAlvo += propriedades.getProperty("tipoPublicoAlvo.membro") + ", ";
			}

			paramWhere.append(" ) ");	
		}			
		if(relatorioFiltro.getExcluirTipo1() || relatorioFiltro.getExcluirTipo2() || relatorioFiltro.getExcluirTipo3() || relatorioFiltro.getExcluirTipo4()){
				
			publicoAlvo += "EXCETO ";
			
			if (relatorioFiltro.getExcluirTipo1()) {
				paramWhere.append(" AND 1 NOT IN (" + subQuery + ")");
				publicoAlvo += propriedades.getProperty("tipoPublicoAlvo.servidor") + ", ";
			}
			
			if (relatorioFiltro.getExcluirTipo2()) {
				paramWhere.append(" AND 2 NOT IN (" + subQuery + ")");
				publicoAlvo += propriedades.getProperty("tipoPublicoAlvo.jurisdicionado") + ", ";
			}
			
			if (relatorioFiltro.getExcluirTipo3()) {
				paramWhere.append(" AND 3 NOT IN (" + subQuery + ")");
				publicoAlvo += propriedades.getProperty("tipoPublicoAlvo.sociedade") + ", ";
			}
			
			if (relatorioFiltro.getExcluirTipo4()) {
				paramWhere.append(" AND 4 NOT IN (" + subQuery + ")");
				publicoAlvo += propriedades.getProperty("tipoPublicoAlvo.membro") + ", ";
			}
		}		
		if(publicoAlvo.length() > 0){
			publicoAlvo = publicoAlvo.substring(0, publicoAlvo.length() - 2);
		} else {
			publicoAlvo = null;
		}
		
		if (!relatorioFiltro.getIncluirTipoParticipante1() || !relatorioFiltro.getIncluirTipoParticipante2() || !relatorioFiltro.getIncluirTipoParticipante3() || !relatorioFiltro.getIncluirTipoParticipante4()) {
			
			if (relatorioFiltro.getIncluirTipoParticipante1()) {
				tipoParticipanteWhere = " AND p.tipo_id in ( 1";
				tipoParticipante = propriedades.getProperty("tipoPublicoAlvo.servidor") + ", ";				
			}
			
			if (relatorioFiltro.getIncluirTipoParticipante2()) {
				if(tipoParticipanteWhere.length() > 0){
					tipoParticipanteWhere += ", 2";
					tipoParticipante += propriedades.getProperty("tipoPublicoAlvo.jurisdicionado") + ", ";
				} else {
					tipoParticipanteWhere = " AND p.tipo_id in ( 2";
					tipoParticipante = propriedades.getProperty("tipoPublicoAlvo.jurisdicionado") + ", ";
				}				
			}
			
			if (relatorioFiltro.getIncluirTipoParticipante3()) {
				if(tipoParticipanteWhere.length() > 0){
					tipoParticipanteWhere += ", 3";
					tipoParticipante += propriedades.getProperty("tipoPublicoAlvo.sociedade") + ", ";
				} else {
					tipoParticipanteWhere = " AND p.tipo_id in ( 3";
					tipoParticipante = propriedades.getProperty("tipoPublicoAlvo.sociedade") + ", ";
				}
			}
			
			if (relatorioFiltro.getIncluirTipoParticipante4()) {
				if(tipoParticipanteWhere.length() > 0){
					tipoParticipanteWhere += ", 4";
					tipoParticipante += propriedades.getProperty("tipoPublicoAlvo.membro") + ", ";
				} else {
					tipoParticipanteWhere = " AND p.tipo_id in ( 4";
					tipoParticipante = propriedades.getProperty("tipoPublicoAlvo.membro") + ", ";
				}
			}
			
			tipoParticipanteWhere += " )";
			
			if(tipoParticipante != null && tipoParticipante.length() > 0){
				tipoParticipante = tipoParticipante.substring(0, tipoParticipante.length() - 2);
			}
		}
		
		if (relatorioFiltro.getIncluirTipoParticipante2()) {
			if (relatorioFiltro.getAdministracaoPublica().equals("estadual")) {
				tipoParticipanteWhere += " and o.tpentidadeesfera = 2 ";
			} else if (relatorioFiltro.getAdministracaoPublica().equals("municipal")) {
				tipoParticipanteWhere += " and o.tpentidadeesfera = 3 ";
			} else if (relatorioFiltro.getAdministracaoPublica().equals("demais casos")) {
				tipoParticipanteWhere += " and (o.tpentidadeesfera not in (2,3) or p.orgao_id is null) ";
			}
		}
		
		if (relatorioFiltro.getTipoEventoId() != 0) {
			paramWhere.append(" AND te.id = " + relatorioFiltro.getTipoEventoId().toString());
			tipoEvento = tipoEventoDao.findById(relatorioFiltro.getTipoEventoId()).getDescricao();
		}
		if (relatorioFiltro.getTituloEvento() != null) {
			tituloEvento = relatorioFiltro.getTituloEvento().trim();
			if(!tituloEvento.isEmpty()){
				paramWhere.append(" AND " + StringUtil.sqlTranslateQuery("e.titulo", tituloEvento));
				//paramWhere.append(" AND upper(e.titulo) like upper('%" + tituloEvento + "%')");
			}
		}
		if (relatorioFiltro.getEixoTematicoId() != 0) {
			paramWhere.append(" AND et.id = " + relatorioFiltro.getEixoTematicoId().toString());
			eixoTematico = eixotematicoDao.findById( relatorioFiltro.getEixoTematicoId()).getDescricao();
		}
		if(relatorioFiltro.getProgramaId() != 0) {
			paramWhere.append(" AND epr.programa_id = " + relatorioFiltro.getProgramaId().toString());
			programa = programaDao.findById(relatorioFiltro.getProgramaId()).getDescricao();
		}
		if (relatorioFiltro.getDataInicioPrevisto1() != null) {
			paramWhere.append(" AND e.data_inicio_previsto >= TO_DATE('"
					+ util.formataData(relatorioFiltro.getDataInicioPrevisto1())
					+ "', 'dd/MM/yyyy')");
			periodoPrevisto1 = util.formataData(relatorioFiltro.getDataInicioPrevisto1());
		}
		if (relatorioFiltro.getDataInicioPrevisto2() != null) {
			paramWhere.append(" AND e.data_inicio_previsto <= TO_DATE('"
					+ util.formataData(relatorioFiltro.getDataInicioPrevisto2())
					+ "', 'dd/MM/yyyy')");
			periodoPrevisto2 = util.formataData(relatorioFiltro.getDataInicioPrevisto2());
		}
		if (relatorioFiltro.getDataInicio1() != null) {
			paramWhere.append(" AND e.data_inicio_realizacao >= TO_DATE('" + util.formataData(relatorioFiltro.getDataInicio1()) + "', 'dd/MM/yyyy')");
			periodoRealizacao1 = util.formataData(relatorioFiltro.getDataInicio1());
		}
		if (relatorioFiltro.getDataInicio2() != null) {
			paramWhere.append(" AND e.data_inicio_realizacao <= TO_DATE('" + util.formataData(relatorioFiltro.getDataInicio2()) + "', 'dd/MM/yyyy')");
			periodoRealizacao2 = util.formataData(relatorioFiltro.getDataInicio2());
		}
		if (relatorioFiltro.getProvedoresTerceiros()) {
			paramWhere.append(" AND 1 NOT IN (select epj2.provedor_id from evento_provedor_join epj2 inner join evento e2 on epj2.evento_id = e2.id where e2.id = e.id) ");
			provedor = "TERCEIROS";
		} else if (relatorioFiltro.getProvedorId() != 0) {
			paramWhere.append(" AND epj.provedor_id = " + relatorioFiltro.getProvedorId().toString());
			provedor = provedoreventoDao.findById(relatorioFiltro.getProvedorId()).getDescricao();
		}
		if (relatorioFiltro.getPaisId() != 0) {
			paramWhere.append(" AND p.id = " + relatorioFiltro.getPaisId().toString());
			pais = paisDao.findById(relatorioFiltro.getPaisId()).getDescricao();
		}
		if (relatorioFiltro.getUfMunicipioId() != null && !relatorioFiltro.getUfMunicipioId().equals("0")) {
			paramWhere.append(" AND m.uf = '" + relatorioFiltro.getUfMunicipioId() + "'");
			uf = relatorioFiltro.getUfMunicipioId();
		}
		if (relatorioFiltro.getMunicipioId() != null && relatorioFiltro.getMunicipioId() != 0L) {
			paramWhere.append(" AND m.id = " + relatorioFiltro.getMunicipioId().toString());
			cidade = municipioDao.find(relatorioFiltro.getMunicipioId()).getNome().toUpperCase();
		}		
		if (relatorioFiltro.getSituacao() != 0) {
			if (relatorioFiltro.getSituacao() == 1) {
				paramWhere.append(" AND ((e.data_inicio_realizacao IS NOT NULL AND e.data_inicio_realizacao > " + PARCIAL_SYSDATE + ")");
				paramWhere.append(" OR (e.data_inicio_realizacao IS NULL AND e.data_inicio_previsto IS NOT NULL AND e.data_inicio_previsto > " + PARCIAL_SYSDATE + "))");
				situacao = "PREVISTO";
			}
			if (relatorioFiltro.getSituacao() == 2) {
				paramWhere.append(" AND e.data_inicio_realizacao <= " + PARCIAL_SYSDATE + " AND e.data_fim_realizacao >= " + PARCIAL_SYSDATE);
				situacao = "EM ANDAMENTO";
			}
			if (relatorioFiltro.getSituacao() == 3) {
				paramWhere.append(" AND e.data_fim_realizacao < " + PARCIAL_SYSDATE);
				situacao = "REALIZADO";
			}
		}
		/*
		 * DONE: __Modalidade Evento: Definir qual será a Modalidade do Evento na consulta (SQL)
		 * @deprecated Consulta agora verifica se o evento é presencial por meio do módulo
		if (relatorioFiltro.getModalidadeId() != 0) {
			paramWhere.append(" AND e.modalidade_id = " + relatorioFiltro.getModalidadeId().toString());
			modalidade = modalidadeDao.find(relatorioFiltro.getModalidadeId()).getDescricao();
		}
		 */
		if (relatorioFiltro.getModalidadeId() != 0) {
			String condicionalModalidadeDoEvento = "";
			if(relatorioFiltro.getModalidadeId().equals(Modalidade.PRESENCIAL))
				condicionalModalidadeDoEvento = " AND e.id IN (SELECT e.id from modulo m INNER JOIN evento e on m.evento_id = e.id WHERE m.modalidade_id = 1)";
			else if( relatorioFiltro.getModalidadeId().equals(Modalidade.EAD))
				condicionalModalidadeDoEvento = " AND e.id NOT IN (SELECT e.id from modulo m INNER JOIN evento e on m.evento_id = e.id WHERE m.modalidade_id = 1)";
				
			paramWhere.append(condicionalModalidadeDoEvento);
			modalidade = modalidadeDao.find(relatorioFiltro.getModalidadeId()).getDescricao();
		}
		if (relatorioFiltro.getLocalizacaoId() != 0) {
			paramWhere.append(" AND tle.id = " + relatorioFiltro.getLocalizacaoId().toString());
			localizacao = tipoLocalizacaoEventoDao.findById(relatorioFiltro.getLocalizacaoId()).getDescricao();
		}

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paramWhere", paramWhere.toString());
		parameters.put("tipoEvento", tipoEvento);
		parameters.put("tituloEvento", tituloEvento);
		parameters.put("publicoAlvo", publicoAlvo);
		parameters.put("eixoTematico", eixoTematico);
		parameters.put("programa", programa);
		parameters.put("localizacao", localizacao);
		parameters.put("provedor", provedor);
		parameters.put("cidade", cidade);
		parameters.put("uf", uf);
		parameters.put("pais", pais);
		parameters.put("periodoPrevisto1", periodoPrevisto1);
		parameters.put("periodoPrevisto2", periodoPrevisto2);
		parameters.put("periodoRealizacao1", periodoRealizacao1);
		parameters.put("periodoRealizacao2", periodoRealizacao2);
		parameters.put("situacao", situacao);
		parameters.put("modalidade", modalidade);
		parameters.put("tipoParticipante", tipoParticipante);
		parameters.put("esfera", relatorioFiltro.getAdministracaoPublica());
		parameters.put("tipoParticipanteWhere", tipoParticipanteWhere);

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);

		if (relatorio.length < 1024) {
			modelMap.addAttribute("relEventos", relatorioFiltro);
			modelMap.addAttribute("mensagemRel", "Os filtros selecionados não geraram resultado.");
			return "relatorio/eventos";
		}

		relatorioService.abrirPdf(relatorio, "relatorio_de_eventos", response);

		return null;
	}

	// EVENTOS POR INSTRUTOR

	@RequestMapping(value = "/relatorio/eventosPorInstrutor", method = RequestMethod.POST)
	public String eventosPorInstrutor(
			@ModelAttribute("relEventosPorInstrutor") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {

		if (relatorioFiltro.getInstrutorId() == null || relatorioFiltro.getInstrutorId() == 0) {
			result.addError(new FieldError("relatorioFiltro", "instrutorId", "Campo Obrigatório"));
		}

		if ((relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() == null)
				|| (relatorioFiltro.getDataInicio1() == null && relatorioFiltro.getDataInicio2() != null)) {

			result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período incompleto."));
		}

		if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() != null) {

			if (relatorioFiltro.getDataInicio1().after(relatorioFiltro.getDataInicio2())) {
				result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período inválido."));
			}
		}

		if (result.hasErrors()) {
			return "/relatorio/eventosPorInstrutor";
		}

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/eventos_por_instrutor.jasper");

		String paramWhere = "WHERE 1=1";
		if (relatorioFiltro.getTipoEventoId() != 0) {
			paramWhere += " and tipo_evento.id = " + relatorioFiltro.getTipoEventoId().toString();
		}

		paramWhere += " and instrutor.id = " + relatorioFiltro.getInstrutorId().toString();

		if (relatorioFiltro.getDataInicio1() != null) {
			paramWhere += " AND COALESCE(evento.data_inicio_realizacao, evento.data_inicio_previsto) >= "
					+ "TO_DATE('" + util.formataData(relatorioFiltro.getDataInicio1()) + "', 'dd/mm/yyyy')";
		}
		if (relatorioFiltro.getDataInicio2() != null) {
			paramWhere += " AND COALESCE(evento.data_inicio_realizacao, evento.data_inicio_previsto) <= "
					+ "TO_DATE('" + util.formataData(relatorioFiltro.getDataInicio2()) + "', 'dd/mm/yyyy')";
		}
		if (relatorioFiltro.getSituacao() != 0) {
			if (relatorioFiltro.getSituacao() == 1) {
				paramWhere += " and (evento.data_inicio_realizacao > SYSDATE OR evento.data_inicio_realizacao is null)";
			}
			if (relatorioFiltro.getSituacao() == 2) {
				paramWhere += " and evento.data_inicio_realizacao <= SYSDATE and evento.data_fim_realizacao >= SYSDATE";
			}
			if (relatorioFiltro.getSituacao() == 3) {
				paramWhere += " and evento.data_fim_realizacao < SYSDATE";
			}
		}

		
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paramWhere", paramWhere);

		parameters.put("instrutor", instrutorDao.find(relatorioFiltro.getInstrutorId()).getNome());

		if (relatorioFiltro.getTipoEventoId() == 0)
			parameters.put("tipo_evento", "TODOS");
		else
			parameters.put("tipo_evento", tipoEventoDao.find(relatorioFiltro.getTipoEventoId()).getDescricao());

		if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() != null)
			parameters.put(	"periodo", "De " + util.formataData(relatorioFiltro.getDataInicio1())
							+ " a "
							+ util.formataData(relatorioFiltro.getDataInicio2()));
		else
			parameters.put("periodo", "");

		if (relatorioFiltro.getSituacao() == 0) {
			parameters.put("situacao", "TODOS");
		} else {
			if (relatorioFiltro.getSituacao() == 1) {
				parameters.put("situacao", "Previsto");
			} else {
				if (relatorioFiltro.getSituacao() == 2) {
					parameters.put("situacao", "Em andamento");
				} else {
					if (relatorioFiltro.getSituacao() == 3) {
						parameters.put("situacao", "Realizado");
					}
				}
			}
		}

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);

		if (relatorio.length < 1024) {
			modelMap.addAttribute("relEventosPorInstrutor", relatorioFiltro);
			modelMap.addAttribute("mensagemRel", "Os filtros selecionados não geraram resultado.");
			return "relatorio/eventosPorInstrutor";
		}

		relatorioService.abrirPdf(relatorio, "eventos_por_instrutor", response);

		return null;
	}

	// EVENTOS POR PARTICIPANTE

	@RequestMapping(value = "/relatorio/eventosPorParticipante", method = RequestMethod.POST)
	public String eventosPorParticipante(
			@ModelAttribute("relEventosPorParticipante") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {

		if (relatorioFiltro.getParticipanteId() == null || relatorioFiltro.getParticipanteId() == 0) {
			result.addError(new FieldError("relatorioFiltro", "participanteId", "Campo Obrigatório"));
		}

		if ((relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() == null)
				|| (relatorioFiltro.getDataInicio1() == null && relatorioFiltro.getDataInicio2() != null)) {

			result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período incompleto"));
		}

		if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() != null) {

			if (relatorioFiltro.getDataInicio1().after(relatorioFiltro.getDataInicio2())) {
				result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período inválido"));
			}
		}

		if (result.hasErrors()) {
			return "/relatorio/eventosPorParticipante";
		}

		InputStream is;

		if (request.getParameter("comoInstrutor") == null) {
			is = this.getClass().getClassLoader().getResourceAsStream("reports/eventos_por_participante.jasper");
		} else {
			is = this.getClass().getClassLoader().getResourceAsStream("reports/eventos_como_instrutor.jasper");
		}

		String paramWhere = "WHERE 1=1";

		if (request.getParameter("comoInstrutor") == null) {
			paramWhere += " AND inscricao.confirmada = 'S'";
			paramWhere += " AND participante.id = "	+ relatorioFiltro.getParticipanteId().toString();
		} else {
			try {
				paramWhere += " AND instrutor.id = " + instrutorDao.findByCpf(util.formatarCpf(participanteDao.findById(relatorioFiltro.getParticipanteId()).getCpf())).getId();
			} catch (NullPointerException e) {
				return "redirect:/relatorio/eventosPorParticipante/?mensagem=Este participante ainda não atuou como instrutor.";
			}
		}

		if (relatorioFiltro.getTipoEventoId() != 0) {
			paramWhere += " AND tipo_evento.id = " + relatorioFiltro.getTipoEventoId().toString();
		}
		if (relatorioFiltro.getLocalizacaoId() != 0) {
			paramWhere += " AND tipo_localizacao_evento.id = " + relatorioFiltro.getLocalizacaoId().toString();
		}

		if (relatorioFiltro.getDataInicio1() != null) {
			paramWhere += " AND COALESCE(evento.data_inicio_realizacao, evento.data_inicio_previsto) >= TO_DATE('" + util.formataData(relatorioFiltro.getDataInicio1()) + "', 'dd/mm/yyyy')";
		}
		if (relatorioFiltro.getDataInicio2() != null) {
			paramWhere += " AND COALESCE(evento.data_inicio_realizacao, evento.data_inicio_previsto) <= TO_DATE('" + util.formataData(relatorioFiltro.getDataInicio2()) + "', 'dd/mm/yyyy')";
		}
		if (relatorioFiltro.getProvedorId() != 0) {
			paramWhere += " AND epj.provedor_id = " + relatorioFiltro.getProvedorId().toString();
		}
		if (relatorioFiltro.getSituacao() != 0) {
			if (relatorioFiltro.getSituacao() == 1) {
				paramWhere += " AND (evento.data_inicio_realizacao > SYSDATE OR evento.data_inicio_realizacao IS NULL)";
			}
			if (relatorioFiltro.getSituacao() == 2) {
				paramWhere += " AND evento.data_inicio_realizacao <= SYSDATE AND evento.data_fim_realizacao >= SYSDATE";
			}
			if (relatorioFiltro.getSituacao() == 3) {
				paramWhere += " AND evento.data_fim_realizacao < SYSDATE";
			}
		}

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paramWhere", paramWhere);

		if (request.getParameter("comoInstrutor") == null) {
			parameters.put("participante", participanteDao.find(relatorioFiltro.getParticipanteId()).getNomeCpf());
		} else {
			parameters.put("instrutor",	instrutorDao.findByCpf(util.formatarCpf(participanteDao.findById(relatorioFiltro.getParticipanteId()).getCpf())).getNome());
		}

		if (relatorioFiltro.getTipoEventoId() == 0)
			parameters.put("tipo_evento", "TODOS");
		else
			parameters.put("tipo_evento", tipoEventoDao.find(relatorioFiltro.getTipoEventoId()).getDescricao());

		if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() != null)
			parameters.put("periodo", "De " + util.formataData(relatorioFiltro.getDataInicio1())
							+ " a "
							+ util.formataData(relatorioFiltro.getDataInicio2()));
		else
			parameters.put("periodo", "");

		if (relatorioFiltro.getLocalizacaoId() == 0)
			parameters.put("localizacao", "TODOS");
		else
			parameters.put("localizacao", tipoLocalizacaoEventoDao.find(relatorioFiltro.getLocalizacaoId()).getDescricao());

		if (relatorioFiltro.getProvedorId() == 0)
			parameters.put("provedor", "TODOS");
		else
			parameters.put("provedor", provedoreventoDao.find(relatorioFiltro.getProvedorId()).getDescricao());

		if (relatorioFiltro.getSituacao() == 0) {
			parameters.put("situacao", "TODOS");
		} else {
			if (relatorioFiltro.getSituacao() == 1) {
				parameters.put("situacao", "Previsto");
			} else {
				if (relatorioFiltro.getSituacao() == 2) {
					parameters.put("situacao", "Em andamento");
				} else {
					if (relatorioFiltro.getSituacao() == 3) {
						parameters.put("situacao", "Realizado");
					}
				}
			}
		}
		
		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);		
		
		if (relatorio.length < 1024) {
			modelMap.addAttribute("relEventosPorParticipante", relatorioFiltro);
			modelMap.addAttribute("mensagemRel", "Os filtros selecionados não geraram resultado.");
			return "relatorio/eventosPorParticipante";
		}

		relatorioService.abrirPdf(relatorio, "eventos_por_participante", response);

		return null;
		
	}

	// FICHA TÉCNICA DE EVENTO

	@RequestMapping(value = "/relatorio/fichaTecnicaDeEvento", method = RequestMethod.POST)
	public String fichaTecnica(
			@ModelAttribute("relFichaTecnica") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {

		if (relatorioFiltro.getEventoId() == null	|| relatorioFiltro.getEventoId() == 0)
			result.addError(new FieldError("relatorioFiltro", "eventoId", "Campo Obrigatório"));

		if (result.hasErrors()) 
			return "/relatorio/fichaTecnicaDeEvento";

		byte[] relatorio = relatorioService.fichaTecnicaDoEvento(relatorioFiltro.getEventoId());
		relatorioService.abrirPdf(relatorio, "ficha_tecnica_de_evento", response);

		return null;
	}

	// GASTOS

	@RequestMapping(value = "/relatorio/gastos", method = RequestMethod.POST)
	public String gastos(
			@ModelAttribute("relGastos") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {

		if ((relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() == null)
				|| (relatorioFiltro.getDataInicio1() == null && relatorioFiltro.getDataInicio2() != null)) {

			result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período incompleto"));
		}

		if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() != null) {

			if (relatorioFiltro.getDataInicio1().after(relatorioFiltro.getDataInicio2())) {
				result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período inválido"));
			}
		}

		if (result.hasErrors()) {
			modelMap.addAttribute("relGastos", relatorioFiltro);
			modelMap.addAttribute("localidadeList", localidadeDao.findByMunicipios());
			return "/relatorio/gastos";
		}

		InputStream is;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		byte[] relatorio;
		boolean semGastos = false;
		
		//Popula parametros para o relatório Simplificado
		if (relatorioFiltro.getAutomatico() && relatorioFiltro.getEventoId() != null && relatorioFiltro.getEventoId() != 0) {
			
			is = this.getClass().getClassLoader().getResourceAsStream("reports/gastos-simplificado.jasper");
			
			RelatorioBean geral = new RelatorioBean();
			double custoTotal = 0;
			double custoPorParticipante = 0;
			Long keyNull = new Long(-1);
			
			List<Participante> servidores = new ArrayList<>();
			List<Participante> jurisdicionados = new ArrayList<>();
			List<Participante> sociedades = new ArrayList<>();
			List<Participante> membros = new ArrayList<>();
			
			Map<Long, RelatorioBean> setores = new HashMap<>();
			Map<Long, RelatorioBean> orgaos = new HashMap<>();	
			Map<Long, RelatorioBean> membrosSetores = new HashMap<>();
			
			Evento evento = eventoDao.find(relatorioFiltro.getEventoId());
			
			geral.setField1(evento.getNome());
			
			for (Gasto gasto : evento.getGastoList()) {
				custoTotal += gasto.getValor().doubleValue();
			}		
			
			geral.setField2(util.formataMonetario(custoTotal));
			
			for (Inscricao inscricao : evento.getInscricaoList()) {
				if(inscricao.getConfirmada().equals("S")){
					if (inscricao.getParticipanteId().getTipoId().getId() == 1){
						servidores.add(inscricao.getParticipanteId());						
					} else if (inscricao.getParticipanteId().getTipoId().getId() == 2){
						jurisdicionados.add(inscricao.getParticipanteId());
					} else if (inscricao.getParticipanteId().getTipoId().getId() == 3){
						sociedades.add(inscricao.getParticipanteId());
					} else if (inscricao.getParticipanteId().getTipoId().getId() == 4){
						membros.add(inscricao.getParticipanteId());
					} 
				}			
			}
			
			custoPorParticipante = custoTotal / (servidores.size() + jurisdicionados.size() + sociedades.size() + membros.size());
			
			geral.setField3(Integer.toString(servidores.size() + jurisdicionados.size() + sociedades.size() + membros.size()));
			geral.setField4(util.formataMonetario(custoPorParticipante));
			geral.setField5(Integer.toString(servidores.size()));
			geral.setField6(Integer.toString(jurisdicionados.size()));
			geral.setField7(Integer.toString(sociedades.size()));
			geral.setField8(Integer.toString(membros.size()));
			
			for (Participante participante : servidores) {
				if(participante.getSetorId() != null){
					if(setores.containsKey(participante.getSetorId().getId())){
						RelatorioBean gastoSetor = setores.get(participante.getSetorId().getId());
						gastoSetor.setDoubleField1(gastoSetor.getDoubleField1() + custoPorParticipante);
						gastoSetor.setIntField1(gastoSetor.getIntField1() + 1);
					} else {
						RelatorioBean gastoSetor = new RelatorioBean();
						gastoSetor.setField1(participante.getSetorId().getDescricao());
						gastoSetor.setDoubleField1(custoPorParticipante);
						gastoSetor.setIntField1(1);
						setores.put(participante.getSetorId().getId(), gastoSetor);
					}
				} else {
					if(setores.containsKey(keyNull)){
						RelatorioBean gastoSetor = setores.get(keyNull);
						gastoSetor.setDoubleField1(gastoSetor.getDoubleField1() + custoPorParticipante);
						gastoSetor.setIntField1(gastoSetor.getIntField1() + 1);
					} else {
						RelatorioBean gastoSetor = new RelatorioBean();
						gastoSetor.setField1("-");
						gastoSetor.setDoubleField1(custoPorParticipante);
						gastoSetor.setIntField1(1);
						setores.put(keyNull, gastoSetor);
					}
				}
			}
			
			for (Participante participante : jurisdicionados) {
				if(participante.getOrgaoId() != null){
					if(orgaos.containsKey(participante.getOrgaoId().getIdentidade())){
						RelatorioBean gastoOrgao = orgaos.get(participante.getOrgaoId().getIdentidade());
						gastoOrgao.setDoubleField1(gastoOrgao.getDoubleField1() + custoPorParticipante);
						gastoOrgao.setIntField1(gastoOrgao.getIntField1() + 1);
					} else {
						RelatorioBean gastoOrgao = new RelatorioBean();
						gastoOrgao.setField1(participante.getOrgaoId().getDsentidade());
						gastoOrgao.setDoubleField1(custoPorParticipante);
						gastoOrgao.setIntField1(1);
						orgaos.put(participante.getOrgaoId().getIdentidade(), gastoOrgao);
					}
				} else {
					if(orgaos.containsKey(keyNull)){
						RelatorioBean gastoOrgao = orgaos.get(keyNull);
						gastoOrgao.setDoubleField1(gastoOrgao.getDoubleField1() + custoPorParticipante);
						gastoOrgao.setIntField1(gastoOrgao.getIntField1() + 1);
					} else {
						RelatorioBean gastoOrgao = new RelatorioBean();
						gastoOrgao.setField1("-");
						gastoOrgao.setDoubleField1(custoPorParticipante);
						gastoOrgao.setIntField1(1);
						orgaos.put(keyNull, gastoOrgao);
					}
				}
			}
			
			for (Participante participante : membros) {
				if(participante.getSetorId() != null){
					if(membrosSetores.containsKey(participante.getSetorId().getId())){
						RelatorioBean gastoSetor = membrosSetores.get(participante.getSetorId().getId());
						gastoSetor.setDoubleField1(gastoSetor.getDoubleField1() + custoPorParticipante);
						gastoSetor.setIntField1(gastoSetor.getIntField1() + 1);
					} else {
						RelatorioBean gastoSetor = new RelatorioBean();
						gastoSetor.setField1(participante.getSetorId().getDescricao());
						gastoSetor.setDoubleField1(custoPorParticipante);
						gastoSetor.setIntField1(1);
						membrosSetores.put(participante.getSetorId().getId(), gastoSetor);
					}
				} else {
					if(membrosSetores.containsKey(keyNull)){
						RelatorioBean gastoSetor = membrosSetores.get(keyNull);
						gastoSetor.setDoubleField1(gastoSetor.getDoubleField1() + custoPorParticipante);
						gastoSetor.setIntField1(gastoSetor.getIntField1() + 1);
					} else {
						RelatorioBean gastoSetor = new RelatorioBean();
						gastoSetor.setField1("-");
						gastoSetor.setDoubleField1(custoPorParticipante);
						gastoSetor.setIntField1(1);
						membrosSetores.put(keyNull, gastoSetor);
					}
				}
			}
			
			RelatorioBean sociedadeBean = new RelatorioBean();
			sociedadeBean.setField1("PARTICIPANTES");
			sociedadeBean.setIntField1(sociedades.size());
			sociedadeBean.setDoubleField1(sociedades.size() * custoPorParticipante);
			
			parameters.put("geral", geral);	
			parameters.put("setores", new JRBeanCollectionDataSource(setores.values()));	
			parameters.put("orgaos", new JRBeanCollectionDataSource(orgaos.values()));
			parameters.put("membros", new JRBeanCollectionDataSource(membrosSetores.values()));
			parameters.put("sociedade", sociedadeBean);
			
			relatorio = relatorioService.chamarRelatorioJRdataSource(is, parameters, null);
		
		//Popula parametros para o relatório Convencional
		} else {	
			
			is = this.getClass().getClassLoader().getResourceAsStream("reports/gastos-javabeancollection.jasper");
			Long eventoId = relatorioFiltro.getEventoId();
			Long participanteId = relatorioFiltro.getParticipanteId();
			Long instrutorId = relatorioFiltro.getInstrutorId();
			Long tipoGastoId = relatorioFiltro.getTipoGastoId();
			String eventoDataInicio = "";
			String eventoDataFim = "";
			
			Integer esferaId = null;
			Long setorId = relatorioFiltro.getSetorId();
			Long orgaoId = relatorioFiltro.getOrgaoId();
			boolean esferaFiltro = false;
			boolean setorFiltro = false;
			boolean orgaoFiltro = false;
			
			parameters.put("EVENTO", relatorioFiltro.getEventoId() != null && relatorioFiltro.getEventoId() != 0 ? 
					eventoDao.findById(relatorioFiltro.getEventoId()).getNome(): "TODOS");
			
			parameters.put("PARTICIPANTE", relatorioFiltro.getParticipanteId() != null && relatorioFiltro.getParticipanteId() != 0 ? 
					participanteDao.findById(relatorioFiltro.getParticipanteId()).getNome() : "TODOS");
			
			parameters.put("INSTRUTOR", relatorioFiltro.getInstrutorId() != null && relatorioFiltro.getInstrutorId() != 0 ? 
					instrutorDao.findById(relatorioFiltro.getInstrutorId()).getNome() : "TODOS");
			
			parameters.put("GASTO", relatorioFiltro.getTipoGastoId() != null && relatorioFiltro.getTipoGastoId() != 0 ?
					tipoGastoDao.findById(relatorioFiltro.getTipoGastoId()).getDescricao() : "TODOS");
			
			if(relatorioFiltro.getSetorId() != null && relatorioFiltro.getSetorId() != -1){
				parameters.put("SETOR", setorDao.findById(relatorioFiltro.getSetorId()).getDescricao());
				setorFiltro = true;
			} else {
				parameters.put("SETOR", "-");
			}
			
			String esfera = relatorioFiltro.getAdministracaoPublica();
			if(esfera != null) {
				if(esfera.equalsIgnoreCase("estadual")) {
					esferaId = 2;
				} else if (esfera.equalsIgnoreCase("municipal")){
					esferaId = 3;
				}
				esferaFiltro = true;
				parameters.put("ESFERA", esfera.toUpperCase());
			} else {
				parameters.put("ESFERA", "-");
			}
				
			
			if(relatorioFiltro.getOrgaoId() != null && relatorioFiltro.getOrgaoId() != 0){
				parameters.put("ORGAO", entidadeDao.findById(relatorioFiltro.getOrgaoId()).getDsentidade());
				orgaoFiltro = true;
			} else {
				parameters.put("ORGAO", "-");
			}
			
			if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() != null) {
				parameters.put("DATA_INICIO", util.formataData(relatorioFiltro.getDataInicio1()));
				parameters.put("DATA_FIM", util.formataData(relatorioFiltro.getDataInicio2()));
				eventoDataInicio = util.formataData(relatorioFiltro.getDataInicio1());
				eventoDataFim = util.formataData(relatorioFiltro.getDataInicio2());
			}
			
			Collection<Gasto> gastos = gastoDao.findGastoByCriteria(eventoId, participanteId, instrutorId, tipoGastoId, eventoDataInicio, eventoDataFim, setorId, esferaId, orgaoId);
			
			if(gastos.size() == 0 || gastos == null)
				semGastos = true;
			
			Collection<RelatorioBean> geral = new ArrayList<RelatorioBean>();
			double gastosTotal = 0;
			String periodoEvento;
			
			//Caso Especifico: Quando seleciona o Filtro de Setor ou Orgão o calculo do Valor no relatório deve levar em conta
			// --> (numero de Participantes / gasto Total do Evento)
			if(setorFiltro || esferaFiltro || orgaoFiltro){
				String setorOrgao = "";
				if(setorFiltro){
					setorOrgao = setorDao.find(setorId).getDescricao();
				}else if(orgaoFiltro){
					setorOrgao = entidadeDao.find(orgaoId).getDsentidade();
				}
				//Serve somente para não iterar duas vezes no mesmo Evento
				List<Long> eventoIds = new ArrayList<>();
				
				for (Gasto gasto : gastos) {
					int numeroParticipantesFiltro = 0;
					int totalParticipantes = 0;
					double gastoTotalEvento = 0;
					periodoEvento = "";
					Evento eventoAtual = gasto.getEventoId();
					if(!eventoIds.contains(eventoAtual.getId())){
						eventoIds.add(eventoAtual.getId());
						
						//Necessário para pegar o gasto Total real pelo Evento
						for (Gasto gastoEvento : eventoAtual.getGastoList()) {
							gastoTotalEvento += gastoEvento.getValor().doubleValue();
						}
						List<Inscricao> inscricoes = gasto.getEventoId().getInscricaoList();
						for (Inscricao inscricao : inscricoes) {
							if(inscricao.getConfirmada().equals("S")){								
								//Verificar se o Filtro é setor ou orgão e também se o tipo de participante bate com Servidor ou Juridicionado
								//Verificar também se o Setor ou Orgão do Participante bate com o respectivo filtro
								Participante participante = inscricao.getParticipanteId();
								if((setorFiltro && participante.getTipoId().isServidor()) &&
										(participante.getSetorId() != null && participante.getSetorId().getId().equals(setorId))){
									numeroParticipantesFiltro++;
								} else if((orgaoFiltro && participante.getTipoId().isJurisdicionado()) &&
										(participante.getOrgaoId() != null && participante.getOrgaoId().getIdentidade().equals(orgaoId))) {
									numeroParticipantesFiltro++;
								} else if(esferaFiltro && participante.getOrgaoId() != null) {
									Entidade entidade = participante.getOrgaoId(); 
									if(!entidade.isTCE() && entidade.getTpentidadeesfera().equals(esferaId))
										numeroParticipantesFiltro++;
								}
								
								totalParticipantes++;
							}
						}
						if(numeroParticipantesFiltro > 0){
							double gastoPorParticipante = gastoTotalEvento / totalParticipantes;
							RelatorioBean bean = new RelatorioBean();
							bean.setField1(gasto.getEventoId().getNome2());
							if(gasto.getEventoId().getDataInicioRealizacao() != null){
								periodoEvento += util.formataData(gasto.getEventoId().getDataInicioRealizacao());
							} else {
								periodoEvento += util.formataData(gasto.getEventoId().getDataInicioPrevisto());
							}
							if(gasto.getEventoId().getDataFimRealizacao() != null){
								periodoEvento += " à " + util.formataData(gasto.getEventoId().getDataFimRealizacao());
							} else {
								periodoEvento += " à " + util.formataData(gasto.getEventoId().getDataFimPrevisto());
							}
							bean.setField2(periodoEvento);
							bean.setField3(" " + String.valueOf(numeroParticipantesFiltro) + " de " + String.valueOf(totalParticipantes));
							bean.setField7(gasto.getInstrutor() != null ? gasto.getInstrutor().getNome() : "");
							bean.setField4(setorOrgao);
							bean.setField5(gasto.getTipoId() != null ? gasto.getTipoId().getDescricao() : "");
							bean.setField6(gasto.getNumeroProcesso() != null ? gasto.getNumeroProcesso() : "");
							bean.setDoubleField1(gastoPorParticipante * numeroParticipantesFiltro);
							geral.add(bean);
							gastosTotal += gastoPorParticipante * numeroParticipantesFiltro;
						}
						
					}
					
				}
			//Caso Geral: o Valor leva em conta o objGasto.getValor() somente;
			} else { 
				for (Gasto gasto : gastos) {
					periodoEvento = "";
					gastosTotal += gasto.getValor().doubleValue();
					RelatorioBean bean = new RelatorioBean();
					bean.setField1(gasto.getEventoId().getNome2());
					if(gasto.getEventoId().getDataInicioRealizacao() != null){
						periodoEvento += util.formataData(gasto.getEventoId().getDataInicioRealizacao());
					} else {
						periodoEvento += util.formataData(gasto.getEventoId().getDataInicioPrevisto());
					}
					
					if(gasto.getEventoId().getDataFimRealizacao() != null){
						periodoEvento += " à " + util.formataData(gasto.getEventoId().getDataFimRealizacao());
					} else {
						periodoEvento += " à " + util.formataData(gasto.getEventoId().getDataFimPrevisto());
					}
					bean.setField2(periodoEvento);
					bean.setField3(gasto.getParticipanteId() != null ? gasto.getParticipanteId().getNome() : "");
					if(gasto.getParticipanteId() != null && gasto.getParticipanteId().getSetorId() != null){
						bean.setField4(gasto.getParticipanteId().getSetorId().getDescricao());
					} else if(gasto.getParticipanteId() != null && gasto.getParticipanteId().getOrgaoId() != null){
						bean.setField4(gasto.getParticipanteId().getOrgaoId().getDsentidade());
					} else {
						bean.setField4("");
					}
					bean.setField7(gasto.getInstrutor() != null ? gasto.getInstrutor().getNome() : "");
					
					bean.setField5(gasto.getTipoId() != null ? gasto.getTipoId().getDescricao() : "");
					bean.setField6(gasto.getNumeroProcesso() != null ? gasto.getNumeroProcesso() : "");		
					bean.setDoubleField1(gasto.getValor().doubleValue());
					geral.add(bean);
				}
				
			}
			parameters.put("geral", new JRBeanCollectionDataSource(geral));
			parameters.put("TOTAL", gastosTotal);
			relatorio = relatorioService.chamarRelatorioJRdataSource(is, parameters, null);
		}		

		if (relatorio.length < 1024 || semGastos) {
			modelMap.addAttribute("relGastos", relatorioFiltro);
			modelMap.addAttribute("localidadeList", localidadeDao.findByMunicipios());
			modelMap.addAttribute("mensagemRel", "Os filtros selecionados não geraram resultado.");
			return "relatorio/gastos";
		}

		relatorioService.abrirPdf(relatorio, "gastos", response);

		return null;
	}

	// IMPRESSAO DE CAPAS - AVALIACAO EFICACIA

	@RequestMapping(value = "/relatorio/impressaoDeCapas/avaliacaoEficacia", method = RequestMethod.POST)
	public void avaliacaoEficacia(RelatorioFiltro relatorioFiltro,
			ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/impressao_capas_avaliacao_eficacia.jasper");

		String paramWhere = "WHERE 1=1";
		if (relatorioFiltro.getEventoId() != 0) {
			paramWhere += " and evento.id = " + relatorioFiltro.getEventoId().toString();
		}

		paramWhere += " ORDER BY evento.data_inicio_realizacao";
	
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paramWhere", paramWhere);

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "capas_de_eventos_avaliacao_eficacia", response);
	}

	// IMPRESSAO DE CAPAS - AVALIACAO REACAO

	@RequestMapping(value = "/relatorio/impressaoDeCapas/avaliacaoReacao", method = RequestMethod.POST)
	public void avaliacaoReacao(RelatorioFiltro relatorioFiltro, ModelMap modelMap,
			ServletRequest request, HttpServletResponse response)
			throws IOException {

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/impressao_capas_avaliacao_reacao.jasper");

		String paramWhere = "WHERE 1=1";
		if (relatorioFiltro.getEventoId() != 0) {
			paramWhere += " and evento.id = " + relatorioFiltro.getEventoId().toString();
		}

		paramWhere += " ORDER BY evento.data_inicio_realizacao";
	
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paramWhere", paramWhere);

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "capas_de_eventos_avaliacao_reacao", response);
	}

	// IMPRESSAO DE CAPAS - FREQUENCIA

	@RequestMapping(value = "/relatorio/impressaoDeCapas/frequencia", method = RequestMethod.POST)
	public void frequencia(RelatorioFiltro relatorioFiltro, ModelMap modelMap,
			ServletRequest request, HttpServletResponse response)
			throws IOException {

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/impressao_capas_frequencia.jasper");

		String paramWhere = "WHERE 1=1";
		if (relatorioFiltro.getEventoId() != 0) {
			paramWhere += " and evento.id = " + relatorioFiltro.getEventoId().toString();
		}

		paramWhere += " ORDER BY evento.data_inicio_realizacao";
	
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paramWhere", paramWhere);

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "capas_de_eventos_frequencia", response);
	}

	// IMPRESSAO DE CAPAS - INDICACAO PARA TREINAMENTO

	@RequestMapping(value = "/relatorio/impressaoDeCapas/indicacao", method = RequestMethod.POST)
	public void indicacao(RelatorioFiltro relatorioFiltro, ModelMap modelMap,
			ServletRequest request, HttpServletResponse response)
			throws IOException {

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/impressao_capas_indicacao.jasper");
		
		String paramWhere = "WHERE 1=1";
		if (relatorioFiltro.getEventoId() != 0) {
			paramWhere += " and evento.id = " + relatorioFiltro.getEventoId().toString();
		}

		paramWhere += " ORDER BY evento.data_inicio_realizacao";
	
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paramWhere", paramWhere);

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "capas_de_eventos_indicacao_para_treinamento", response);
	}	

	// INDICADORES

	@RequestMapping(value = "/relatorio/indicadoresPlanejamento", method = RequestMethod.POST)
	public String indicadoresPlanejamento(
			@ModelAttribute("relIndicadoresPlanejamento") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/indicadores_planejamento.jasper");

		String periodo_inicio = null;
		String periodo_fim = null;
		String data_filtro = null;

		periodo_inicio = "01/01/" + relatorioFiltro.getAno();
		periodo_fim = "31/12/" + relatorioFiltro.getAno();
		data_filtro = "31/12/" + relatorioFiltro.getAno();
			

		if (relatorioFiltro.getParcial_ate() != null) {
			if (!relatorioFiltro.getParcial_ate()) {
				data_filtro = util.formataData(new Date());				
			}
		}

		if (relatorioFiltro.getParcial_data() != null) {
			if (relatorioFiltro.getParcial_data().before(util.parseDate(periodo_inicio)) || relatorioFiltro.getParcial_data().after(util.parseDate(periodo_fim))) {
				result.addError(new FieldError("relatorioFiltro", "parcial_data", "Selecione uma data válida!"));
			}
		}

		if (result.hasErrors()) {
			return "relatorio/indicadoresPlanejamento";
		}

		if (relatorioFiltro.getParcial_data() != null) {
			data_filtro = util.formataData(relatorioFiltro.getParcial_data());
		}
		
		MetaPlanejamentoEstrategico meta = indicadorPlanejamentoEstrategicoDao.findByAno(Integer.valueOf(relatorioFiltro.getAno()));
		
		if (meta == null) {
			modelMap.addAttribute("mensagemErro", "Meta não encontrada");
			return "relatorio/indicadoresPlanejamento";
		}

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("periodo_inicio", util.parseDate(periodo_inicio));
		parameters.put("periodo_fim", util.parseDate(periodo_fim));
		parameters.put("data_filtro", util.parseDate(data_filtro));
		parameters.put("parcial_ate", relatorioFiltro.getParcial_ate());		
		parameters.put("indicadorManual1", relatorioFiltro.getIndicadorManual1());
		parameters.put("indicadorManual2", relatorioFiltro.getIndicadorManual2());
		parameters.put("indicadorManual3", relatorioFiltro.getIndicadorManual3());
		
		parameters.put("meta_quantidade_acoes", meta.getQuantidadeAcoes().toString());
		parameters.put("meta_perc_juri_capacitados", meta.getPercentualJurisdicionadosCapacitados().toString() + "%");
		parameters.put("meta_perc_serv_capacitados", meta.getPercentualServidoresCapacitados().toString() + "%");
		parameters.put("meta_perc_acoes_plano", meta.getPercentualAcoesDoPlano().toString() + "%");

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "indicadores_do_planejamento_estrategico", response);

		return null;
	}

	@RequestMapping(value = "/relatorio/indicadoresDesempenho", method = RequestMethod.POST)
	public String indicadoresDesempenho(@ModelAttribute("relIndicadoresDesempenho") @Valid RelatorioFiltro relatorioFiltro, 
			BindingResult result, ModelMap modelMap, 
			HttpServletResponse response) {
		
		try {
			relatorioService.gerarRelatorioIndicadoresDesempenho(relatorioFiltro, response);
		} catch (SemestreInvalidoException e) {
			result.addError(new FieldError("relatorioFiltro", "semestre", e.getMessage()));
		} catch (DataInvalidaException e) {
			result.addError(new FieldError("relatorioFiltro", "parcial_data", e.getMessage()));
		} catch (ParseException e) {
			e.printStackTrace();
			modelMap.addAttribute("mensagem", "Não foi possível converter os períodos informados");
			return "relatorio/indicadoresDesempenho";
		} catch (BusinessException e) {
			modelMap.addAttribute("mensagemErro", e.getMessage());
			return "relatorio/indicadoresDesempenho";
		}

		if (result.hasErrors()) {
			return "relatorio/indicadoresDesempenho";
		}

		return null;
	}
	
	@RequestMapping(value = "/relatorio/indicadoresOutros", method = RequestMethod.POST)
	public String indicadoresOutros(
			@ModelAttribute("relIndicadoresOutros") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {


		if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() == null) {
			result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período incompleto"));
		}
		if (relatorioFiltro.getDataInicio1() == null && relatorioFiltro.getDataInicio2() != null) {
			result.addError(new FieldError("relatorioFiltro", "dataInicio1", "Período incompleto"));
		}
		if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() != null) {
			if (relatorioFiltro.getDataInicio1().after(relatorioFiltro.getDataInicio2())) {
				result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período inválido"));
				result.addError(new FieldError("relatorioFiltro", "dataInicio1", ""));
			}
		}

		if (result.hasErrors()) {
			return "relatorio/indicadoresOutros";
		}

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/indicadores_outros_medias.jasper");
		
		RelatorioBean relatorioBean = new RelatorioBean();
		BigDecimal cargaHorariaCursada;
		BigDecimal totalParticipantes;
		BigDecimal totalOportunidades;
		BigDecimal totalSetoresComParticipante;
		
		Map<String, BigDecimal> map = relatorioDao.getParametrosParaIndicadoresOutros(relatorioFiltro);
		cargaHorariaCursada = map.get("cargahorariacursada");
		totalParticipantes = map.get("totalparticipantes");
		totalOportunidades = map.get("totaloportunidades");
		totalSetoresComParticipante = map.get("totalsetorescomparticipante");		
		
		Integer totalServidores = participanteDao.findByTipo(1L).size();
		Integer totalSetores = setorDao.findAll().size();
		
		
		if (totalParticipantes.doubleValue() != 0) {
			relatorioBean.setField1(util.formataDouble(cargaHorariaCursada.doubleValue()/totalParticipantes.doubleValue()));
			relatorioBean.setField5(util.formataDouble(totalOportunidades.doubleValue()/totalParticipantes.doubleValue()));
		} else {
			relatorioBean.setField1("0");
			relatorioBean.setField5("0");
		}
		
		if (totalOportunidades.doubleValue() != 0)
			relatorioBean.setField4(util.formataDouble(cargaHorariaCursada.doubleValue()/totalOportunidades.doubleValue()));
		else
			relatorioBean.setField4("0");
		
		if (totalOportunidades.doubleValue() != 0)
			relatorioBean.setField6(util.formataDouble(cargaHorariaCursada.doubleValue()/totalSetoresComParticipante.doubleValue()));
		else
			relatorioBean.setField6("0");
		
		relatorioBean.setField2(util.formataDouble((totalParticipantes.doubleValue()/totalServidores)*100) + "%");
		relatorioBean.setField3(Integer.toString(totalServidores - totalParticipantes.intValue()));		
		relatorioBean.setField7(util.formataDouble((totalSetoresComParticipante.doubleValue()/totalSetores)*100) + "%");
		relatorioBean.setField8(Integer.toString(totalSetores - totalSetoresComParticipante.intValue()));		
		
		
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() != null){
			parameters.put("periodoRealizacao", util.formataData(relatorioFiltro.getDataInicio1()) + " a " + util.formataData(relatorioFiltro.getDataInicio2()) );
		}		
		if (relatorioFiltro.getTipoParticipanteId() != null && relatorioFiltro.getTipoParticipanteId() != 0){
			parameters.put("tipoParticipante", tipoPublicoAlvoDao.find(relatorioFiltro.getTipoParticipanteId()).getDescricao());
			parameters.put("tipoParticipanteId", relatorioFiltro.getTipoParticipanteId().toString());
		} else {
			parameters.put("tipoParticipante", 	"TODOS");
			parameters.put("tipoParticipanteId", "");
		}
		if (relatorioFiltro.getTipoEventoId() != null && relatorioFiltro.getTipoEventoId() != 0){
			parameters.put("tipoEvento", tipoEventoDao.find(relatorioFiltro.getTipoEventoId()).getDescricao());
		} else {
			parameters.put("tipoEvento", "TODOS");
		}
		if (relatorioFiltro.getModalidadeId() != null && relatorioFiltro.getModalidadeId() != 0){
			parameters.put("modalidade", modalidadeDao.find(relatorioFiltro.getModalidadeId()).getDescricao());
		} else {
			parameters.put("modalidade", "TODOS");
		}
		parameters.put("esfera", relatorioFiltro.getAdministracaoPublica().toUpperCase());
		parameters.put("relatorioBean", relatorioBean);		
		

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "outros_indicadores", response);

		return null;
	}
	
	
	// INDICADORES SETORIAIS

	@RequestMapping(value = "/relatorio/indicadoresSetoriais", method = RequestMethod.POST)
	public void indicadoresSetoriais(RelatorioFiltro relatorioFiltro,
			ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/indicadores_setoriais.jasper");

		String paramWhere = "WHERE 1=1";
		if (relatorioFiltro.getTrimestre_data1() != null) {
			paramWhere += " and evento.data_inicio_realizacao >= '" + util.formataData(relatorioFiltro.getTrimestre_data1()) + "'";
		}
		if (relatorioFiltro.getTrimestre_data2() != null) {
			paramWhere += " and evento.data_inicio_realizacao <= '"	+ util.formataData(relatorioFiltro.getTrimestre_data2()) + "'";
		}

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paramWhere", paramWhere);
		parameters.put("trimestre_data1", relatorioFiltro.getTrimestre_data1());
		parameters.put("trimestre_data2", relatorioFiltro.getTrimestre_data2());

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "indicadoresSetoriais", response);
	}

	// INDICES - AVALIACAO EFICACIA

	@RequestMapping(value = "/relatorio/indices/avaliacaoEficacia", method = RequestMethod.POST)
	public void indiceAvaliacaoEficacia(RelatorioFiltro relatorioFiltro,
			ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/indice_avaliacao_eficacia.jasper");

		String paramWhere = "WHERE 1=1";
		if (relatorioFiltro.getDataInicio1() != null) {
			paramWhere += " and evento.data_inicio_realizacao >= '"	+ util.formataData(relatorioFiltro.getDataInicio1()) + "'";
		}
		if (relatorioFiltro.getDataInicio2() != null) {
			paramWhere += " and evento.data_inicio_realizacao <= '"	+ util.formataData(relatorioFiltro.getDataInicio2()) + "'";
		}

		paramWhere += " ORDER BY evento.data_inicio_realizacao";
	
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paramWhere", paramWhere);

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "indice_avaliacao_eficacia", response);
	}

	// INDICES - AVALIACAO REACAO

	@RequestMapping(value = "/relatorio/indices/avaliacaoReacao", method = RequestMethod.POST)
	public void indiceAvaliacaoReacao(RelatorioFiltro relatorioFiltro,
			ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/indice_avaliacao_reacao.jasper");

		String paramWhere = "WHERE 1=1";
		if (relatorioFiltro.getDataInicio1() != null) {
			paramWhere += " and evento.data_inicio_realizacao >= '"	+ util.formataData(relatorioFiltro.getDataInicio1()) + "'";
		}
		if (relatorioFiltro.getDataInicio2() != null) {
			paramWhere += " and evento.data_inicio_realizacao <= '"	+ util.formataData(relatorioFiltro.getDataInicio2()) + "'";
		}
		
		paramWhere += " ORDER BY evento.data_inicio_realizacao";

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paramWhere", paramWhere);

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "indice_avaliacao_reacao", response);
	}

	// INDICES - FREQUENCIA

	@RequestMapping(value = "/relatorio/indices/frequencia", method = RequestMethod.POST)
	public void indiceFrequencia(RelatorioFiltro relatorioFiltro,
			ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/indice_frequencia.jasper");

		String paramWhere = "WHERE 1=1";
		if (relatorioFiltro.getDataInicio1() != null) {
			paramWhere += " and evento.data_inicio_realizacao >= '" + util.formataData(relatorioFiltro.getDataInicio1()) + "'";
		}
		if (relatorioFiltro.getDataInicio2() != null) {
			paramWhere += " and evento.data_inicio_realizacao <= '" + util.formataData(relatorioFiltro.getDataInicio2()) + "'";
		}
		paramWhere += " ORDER BY evento.data_inicio_realizacao";

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paramWhere", paramWhere);

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "indice_frequencia", response);
	}

	// INDICES - INDICACAO

	@RequestMapping(value = "/relatorio/indices/indicacao", method = RequestMethod.POST)
	public void indiceIndicacao(RelatorioFiltro relatorioFiltro, ModelMap modelMap,
			ServletRequest request, HttpServletResponse response)
			throws IOException {

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/indice_indicacao.jasper");

		String paramWhere = "WHERE 1=1";
		if (relatorioFiltro.getDataInicio1() != null) {
			paramWhere += " and evento.data_inicio_realizacao >= '" + util.formataData(relatorioFiltro.getDataInicio1()) + "'";
		}
		if (relatorioFiltro.getDataInicio2() != null) {
			paramWhere += " and evento.data_inicio_realizacao <= '"	+ util.formataData(relatorioFiltro.getDataInicio2()) + "'";
		}
		
		paramWhere += " ORDER BY evento.data_inicio_realizacao";
	
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paramWhere", paramWhere);

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "indice_indicacao", response);
	}

	// INSCRICOES

	@RequestMapping(value = "/relatorio/inscricoes", method = RequestMethod.POST)
	public String inscricoes(
			@ModelAttribute("relInscricoes") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {
		
		if (relatorioFiltro.getEventoId() == 0 && relatorioFiltro.getParticipanteId() == 0 && (relatorioFiltro.getDataPreInscricao1() == null || relatorioFiltro.getDataPreInscricao2() == null)) {
			if(relatorioFiltro.getAgruparPor() == 0)
				result.addError(new FieldError("relatorioFiltro", "eventoId", "Pelo menos um dos campos com * deve ser preenchido!"));
		}
		if (relatorioFiltro.getDataPreInscricao1() != null && relatorioFiltro.getDataPreInscricao2() != null) {
			if (relatorioFiltro.getDataPreInscricao1().after(relatorioFiltro.getDataPreInscricao2())) {
				result.addError(new FieldError("relatorioFiltro", "dataPreInscricao2", "Período inválido"));
				result.addError(new FieldError("relatorioFiltro", "dataPreInscricao1", ""));
			}
		}
		if (relatorioFiltro.getDataPreInscricao1() != null && relatorioFiltro.getDataPreInscricao2() == null) {
			result.addError(new FieldError("relatorioFiltro", "dataPreInscricao2", "Período incompleto"));
		}
		if (relatorioFiltro.getDataPreInscricao1() == null && relatorioFiltro.getDataPreInscricao2() != null) {
			result.addError(new FieldError("relatorioFiltro",	"dataPreInscricao1", "Período incompleto"));
		}
		
		//Se agrupar e conseguir selecionar o PARTICIPANTE ou o EVENTO
		if (relatorioFiltro.getAgruparPor() != 0 && (relatorioFiltro.getParticipanteId() != 0 || relatorioFiltro.getEventoId() != 0)){
			result.addError(new FieldError("relatorioFiltro","agruparPor", "Não é possível agrupar com  os campos Evento ou Participante selecionados"));
		}
		//Se filtro de agrupar por Setor do TCE-CE e o tipo de participante não for SERVIDOR
		if(relatorioFiltro.getAgruparPor() == 2 && relatorioFiltro.getTipoParticipanteId() != 1){
			result.addError(new FieldError("relatorioFiltro","agruparPor", "Para agrupar por setor do TCE-CE o participante precisa ser do tipo SERVIDOR"));
		}
		
		if(relatorioFiltro.getAgruparPor() == 3 && (relatorioFiltro.getTipoParticipanteId() != 2)){
			result.addError(new FieldError("relatorioFiltro","agruparPor", "Para agrupar por órgão o participante precisa ser do tipo JURISDICIONADO"));
		}
		
		if (result.hasErrors()) {
			return "relatorio/inscricoes";
		}
		
		InputStream is = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		
		//Condicional para verificar o radio agruparPor e carregar o relatório correspondente
		//foi inserido um novo label o relatorio jasper (AGRUPAR:), então é preciso enviar o Map o parametro 'agruparPor'
		if(relatorioFiltro.getAgruparPor() == 0 ){
			is = this.getClass().getClassLoader().getResourceAsStream("reports/pre_inscricoes.jasper");
			parameters.put("agruparPor", "NÃO AGRUPADO");
		} else if (relatorioFiltro.getAgruparPor() == 1) {
			is = this.getClass().getClassLoader().getResourceAsStream("reports/pre_inscricoes_por_participante.jasper");
			parameters.put("agruparPor", "POR PARTICIPANTE");
		} else if (relatorioFiltro.getAgruparPor() == 2) {
			is = this.getClass().getClassLoader().getResourceAsStream("reports/pre_inscricoes_por_setor_tce.jasper");
			parameters.put("agruparPor", "POR SETOR DO TCE-CE");
		} else if (relatorioFiltro.getAgruparPor() == 3) {
			is = this.getClass().getClassLoader().getResourceAsStream("reports/pre_inscricoes_por_orgao.jasper");
			parameters.put("agruparPor", "POR ÓRGÃO");
		} else if(relatorioFiltro.getAgruparPor() == 4) {
			is = this.getClass().getClassLoader().getResourceAsStream("reports/pre_inscricoes_por_programa.jasper");
			parameters.put("agruparPor", "POR PROGRAMA");
		}

		String paramWhere = "WHERE 1=1";
		
		//se evento estiver selecionado não pode usar opção de agrupar
		if (relatorioFiltro.getAgruparPor() == 0 && relatorioFiltro.getEventoId() != 0) {
			paramWhere += " and e.id = " + relatorioFiltro.getEventoId().toString();
			parameters.put("nomeEvento", eventoDao.findById(relatorioFiltro.getEventoId()).getNome());
		}
		
		if (relatorioFiltro.getTipoEventoId() != 0) {
			paramWhere += " and e.tipo_id = " + relatorioFiltro.getTipoEventoId().toString();
			parameters.put("tipoEventoFiltro", tipoEventoDao.findById(relatorioFiltro.getTipoEventoId()).getDescricao());
		}

		if (relatorioFiltro.getTipoParticipanteId() != 0) {
			paramWhere += " and p.tipo_id = " + relatorioFiltro.getTipoParticipanteId().toString();
			parameters.put("tipoParticipante", tipoPublicoAlvoDao.findById(relatorioFiltro.getTipoParticipanteId()).getDescricao());
		}
		
		if (relatorioFiltro.getAdministracaoPublica().equals("estadual")) {
			paramWhere += " and o.tpentidadeesfera = 2 ";
		} else if (relatorioFiltro.getAdministracaoPublica().equals("municipal")) {
			paramWhere += " and o.tpentidadeesfera = 3 ";
		} else if (relatorioFiltro.getAdministracaoPublica().equals("demais casos")) {
			paramWhere += " and (o.tpentidadeesfera not in (2,3) or p.orgao_id is null) ";
		}
		parameters.put("esfera", relatorioFiltro.getAdministracaoPublica().toUpperCase());
		
		if (relatorioFiltro.getAgruparPor() == 0 && relatorioFiltro.getParticipanteId() != 0) {
			paramWhere += " and p.id = " + relatorioFiltro.getParticipanteId().toString();
			parameters.put("participante", participanteDao.findById(relatorioFiltro.getParticipanteId()).getNomeCpf());
		}
		
		if(relatorioFiltro.getProgramaId() == 0) {
			parameters.put("programa", "TODOS");
		}
		
		if(relatorioFiltro.getProgramaId() != 0) {
			paramWhere += " and prog.id = " + relatorioFiltro.getProgramaId().toString();
			parameters.put("programa", programaDao.findById(relatorioFiltro.getProgramaId()).getDescricao());
		}
		
		if (!relatorioFiltro.getIndicada().equals("0")) {
			paramWhere += " and i.indicada = '" + relatorioFiltro.getIndicada().toString() + "'";
			parameters.put("indicada", relatorioFiltro.getIndicada());
		}

		if (!relatorioFiltro.getConfirmada().equals("0")) {
			paramWhere += " and i.confirmada = '" + relatorioFiltro.getConfirmada().toString() + "'";
			parameters.put("confirmada", relatorioFiltro.getConfirmada());
		}
		//Só pode filtrar por chefe se o filtro participante for setado e seja Servidor do TCE
		if ((relatorioFiltro.getResponsavelPelaIndicacaoId() != null && relatorioFiltro.getResponsavelPelaIndicacaoId() != 0) && relatorioFiltro.getTipoParticipanteId() == 1) {
			paramWhere += " and i.chefe_id = " + relatorioFiltro.getResponsavelPelaIndicacaoId().toString();
			UsuarioSCA chefe = usuarioDao.findById(relatorioFiltro.getResponsavelPelaIndicacaoId());
			parameters.put("respIndicacao", chefe.getNome());
		}

		if (relatorioFiltro.getDataPreInscricao1() != null) {
			paramWhere += " and i.data >= TO_DATE('" + util.formataData(relatorioFiltro.getDataPreInscricao1()) + "', 'dd/MM/yyyy')";
			parameters.put("data1",	util.formataData(relatorioFiltro.getDataPreInscricao1()));
		}

		if (relatorioFiltro.getDataPreInscricao2() != null) {
			paramWhere += " and i.data - 1 <= TO_DATE('" + util.formataData(relatorioFiltro.getDataPreInscricao2()) + "', 'dd/MM/yyyy')";
			parameters.put("data2",	util.formataData(relatorioFiltro.getDataPreInscricao2()));
		}
		
		parameters.put("paramWhere", paramWhere);
		
		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);

		if (relatorio.length < 1024) {
			modelMap.addAttribute("relInscricoes", relatorioFiltro);
			modelMap.addAttribute("mensagemRel", "Os filtros selecionados não geraram resultado.");
			return "relatorio/inscricoes";
		}
		
		relatorioService.abrirPdf(relatorio, "pre_inscricoes", response);

		return null;

	}	

	// PARTICIPANTES

	@RequestMapping(value = "/relatorio/participantes", method = RequestMethod.POST)
	public String participantes(
			@ModelAttribute("relParticipantes") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {

		if (relatorioFiltro.getEventoId() != 0 && relatorioFiltro.getDesempenho() != null && !relatorioFiltro.getDesempenho().equals("TODOS")) {
			if (desempenhoDao.findByEventoId(relatorioFiltro.getEventoId()) == null || desempenhoDao.findByEventoId(relatorioFiltro.getEventoId()).isEmpty()) {
				result.addError(new FieldError("relatorioFiltro", "eventoId", "Evento não apurado"));
			}
		}

		if (result.hasErrors()) {
			modelMap.addAttribute("relParticipantes", relatorioFiltro);
			modelMap.addAttribute("localidadeList", localidadeDao.findByMunicipios());
			return "/relatorio/participantes";
		}

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/participantes.jasper");

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		String paramWhere = "WHERE 1=1";

		if (relatorioFiltro.getPublicoAlvoId() != 0) {
			paramWhere += " AND tipo_publico_alvo.id = " + relatorioFiltro.getPublicoAlvoId().toString();
			parameters.put("publico_alvo", tipoPublicoAlvoDao.findById(relatorioFiltro.getPublicoAlvoId()).getDescricao());
		} else {
			parameters.put("publico_alvo", "TODOS");
		}
		if (relatorioFiltro.getSetorId() != null && relatorioFiltro.getSetorId() != -1) {
			paramWhere += " AND setor.idsetor = " + relatorioFiltro.getSetorId().toString();
			parameters.put("setor", setorDao.findById(relatorioFiltro.getSetorId()).getDescricao());
		} else {
			parameters.put("setor", "TODOS");
		}
		if (relatorioFiltro.getAdministracaoPublica() != null) {
			if(relatorioFiltro.getAdministracaoPublica().equals("estadual")) {
				paramWhere += " AND entidade.tpentidadeesfera = 2 ";			
			} else if (relatorioFiltro.getAdministracaoPublica().equals("municipal")) {
				paramWhere += " AND entidade.tpentidadeesfera = 3 ";
			} else if (relatorioFiltro.getAdministracaoPublica().equals("demais casos")) {
				paramWhere += " AND (entidade.tpentidadeesfera not in (2,3) or participante.orgao_id is null )";
			}
			parameters.put("esfera", relatorioFiltro.getAdministracaoPublica().toUpperCase());			
		} else {
			parameters.put("esfera", "TODAS");
		}
		if(relatorioFiltro.getLocalidadeId() != null && relatorioFiltro.getLocalidadeId() != 0) {
			paramWhere += " AND localidade.idlocalidade = " + relatorioFiltro.getLocalidadeId();
			parameters.put("municipio", localidadeDao.find(relatorioFiltro.getLocalidadeId()).getDsLocalidade());
		} else {
			parameters.put("municipio", "TODOS");
		}		
		if (relatorioFiltro.getOrgaoId() != null && relatorioFiltro.getOrgaoId() != 0) {
			paramWhere += " AND entidade.identidade = "	+ relatorioFiltro.getOrgaoId().toString();
			parameters.put("orgao",	entidadeDao.findById(relatorioFiltro.getOrgaoId()).getDsentidade());
		} else {
			parameters.put("orgao", "TODOS");
		}
		if (relatorioFiltro.getEventoId() != 0) {
			paramWhere += " AND evento.id = " + relatorioFiltro.getEventoId().toString() + " AND inscricao.confirmada = 'S'";
			Evento evento = eventoDao.findById(relatorioFiltro.getEventoId());
			parameters.put("evento", evento.getNome());

			if (relatorioFiltro.getDesempenho() != null) {

				if (relatorioFiltro.getDesempenho().equals("APROVADOS"))
					paramWhere += " AND participante.id not in (SELECT DISTINCT participante_id FROM desempenho WHERE evento_id = " + relatorioFiltro.getEventoId().toString() + " and aprovado = 'N')";
				if (relatorioFiltro.getDesempenho().equals("REPROVADOS"))
					paramWhere += " AND participante.id in (SELECT DISTINCT participante_id FROM desempenho WHERE evento_id = "	+ relatorioFiltro.getEventoId().toString() + " and aprovado = 'N')";
			}

		} else {
			parameters.put("evento", "TODOS");
		}

		if (relatorioFiltro.getDesempenho() == null) {
			parameters.put("desempenho", "TODOS");
		} else {
			parameters.put("desempenho", relatorioFiltro.getDesempenho());
		}

		parameters.put("paramWhere", paramWhere);

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);

		if (relatorio.length < 1024) {
			modelMap.addAttribute("relParticipantes", relatorioFiltro);
			modelMap.addAttribute("localidadeList", localidadeDao.findByMunicipios());
			modelMap.addAttribute("mensagemRel", "Os filtros selecionados não geraram resultado.");
			return "relatorio/participantes";
		}

		relatorioService.abrirPdf(relatorio, "participantes", response);

		return null;
	}

	// PARTICIPANTES EXTERNOS

	@RequestMapping(value = "/relatorio/participantesExternos", method = RequestMethod.POST)
	public String participantesExternos(
			@ModelAttribute("relParticipantesExternos") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {

		if (relatorioFiltro.getOrgaoId() == null || relatorioFiltro.getOrgaoId() == 0)
			result.addError(new FieldError("relatorioFiltro", "orgaoId", "Campo Obrigatório"));

		if ((relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() == null)
				|| (relatorioFiltro.getDataInicio1() == null && relatorioFiltro.getDataInicio2() != null)) {
			result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período incompleto."));
		}

		if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() != null) {
			if (relatorioFiltro.getDataInicio1().after(relatorioFiltro.getDataInicio2())) {
				result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período inválido."));
			}
		}

		if (result.hasErrors()) {
			modelMap.addAttribute("relParticipantesExternos", relatorioFiltro);
			modelMap.addAttribute("localidadeList", localidadeDao.findByMunicipios());
			return "/relatorio/participantesExternos";
		}

		byte[] relatorio = relatorioService.participantesExternos(relatorioFiltro);

		if (relatorio.length < 1024) {
			modelMap.addAttribute("relParticipantesExternos", relatorioFiltro);
			modelMap.addAttribute("localidadeList", localidadeDao.findByMunicipios());
			modelMap.addAttribute("mensagemRel", "Os filtros selecionados não geraram resultado.");
			return "relatorio/participantesExternos";
		}

		relatorioService.abrirPdf(relatorio, "participantes_externos", response);

		return null;
	}

	// PARTICIPANTES POR TIPO POR EVENTO

	@RequestMapping(value = "/relatorio/participantesPorTipoPorEvento", method = RequestMethod.POST)
	public String participantesPorTipoPorEvento(
			@ModelAttribute("relparticipantesPorTipoPorEvento") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException, JRException {

		if (relatorioFiltro.getEventoId() == null || relatorioFiltro.getEventoId() == 0) {
			result.addError(new FieldError("relatorioFiltro", "eventoId",	"Campo Obrigatório"));
		}

		if (result.hasErrors()) {
			return "/relatorio/participantesPorTipoPorEvento";
		}

		byte[] relatorio = relatorioService.participantesPorTipoPorEvento(relatorioFiltro);
		relatorioService.abrirPdf(relatorio, "resumo_de_evento", response);

		return null;
	}

	// REGISTRO DE FREQUENCIA

	@RequestMapping(value = "/relatorio/registroFrequencia", method = RequestMethod.POST)
	public String registroFrequencia(
			@ModelAttribute("relRegistroFrequencia") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {

		Modulo modulo = null;

		if (relatorioFiltro.getEventoId() == null || relatorioFiltro.getEventoId() == 0) {
			result.addError(new FieldError("relatorioFiltro", "eventoId",	"Campo Obrigatório"));
		}

		if (relatorioFiltro.getModulo() == null || relatorioFiltro.getModulo() == 0) {
			result.addError(new FieldError("relatorioFiltro", "modulo", "Campo Obrigatório"));
		}

		if (relatorioFiltro.getTurno() == null || relatorioFiltro.getTurno().equals("")) {
			result.addError(new FieldError("relatorioFiltro", "turno", "Campo Obrigatório"));
		} else {
			modulo = moduloDao.findById(relatorioFiltro.getModulo());
		}

		if (relatorioFiltro.getData() == null	|| relatorioFiltro.getData().equals("")) {
			result.addError(new FieldError("relatorioFiltro", "data",	"Campo Obrigatório"));
		} else if (modulo != null) {
			if (util.parseDate(relatorioFiltro.getData()).before(modulo.getDataInicio())
					|| util.parseDate(relatorioFiltro.getData()).after(modulo.getDataFim())) {
				result.addError(new FieldError("relatorioFiltro", "data",	"Data inválida."));
			}
		}

		if (result.hasErrors()) {
			return "relatorio/registroFrequencia";
		}

		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream("reports/registro_frequencia.jasper");

		String paramWhere = "WHERE 1=1 AND inscricao.confirmada = 'S'";
		if (relatorioFiltro.getEventoId() != 0) {
			paramWhere += " and evento.id = " + relatorioFiltro.getEventoId().toString();
		}

		if (relatorioFiltro.getModulo() != 0) {
			paramWhere += " and modulo.id = " + relatorioFiltro.getModulo().toString();
		}

		if (relatorioFiltro.getPublicoAlvoId() != 0) {
			paramWhere += " and participante.tipo_id = " + relatorioFiltro.getPublicoAlvoId().toString();
		}

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paramWhere", paramWhere);
		parameters.put("data", relatorioFiltro.getData());
		parameters.put("modulo", relatorioFiltro.getModulo());
		parameters.put("turno", relatorioFiltro.getTurno());
		if (relatorioFiltro.getPublicoAlvoId() != 0) {
			parameters.put("tipo", tipoPublicoAlvoDao.find(relatorioFiltro.getPublicoAlvoId()).getDescricao());
		} else {
			parameters.put("tipo", "TODOS");
		}
		List<Instrutor> instrutorList = moduloDao.findById(relatorioFiltro.getModulo()).getInstrutorList();
		String instrutores = "";
		for (int i = 0; i < instrutorList.size(); i++) {
			instrutores += instrutorList.get(i).getNome();
			if (i < instrutorList.size() - 1) {
				instrutores += ", ";
			}
		}
		parameters.put("instrutores", instrutores);

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		relatorioService.abrirPdf(relatorio, "registro_de_frequencia", response);

		return null;
	}

	// SOLICITAÇÕES

	@RequestMapping(value = "/relatorio/solicitacoes", method = RequestMethod.POST)
	public String solicitacoes(
			@ModelAttribute("relSolicitacoes") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {

		if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() == null) {
			result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período incompleto."));
		}
		if (relatorioFiltro.getDataInicio1() == null && relatorioFiltro.getDataInicio2() != null) {
			result.addError(new FieldError("relatorioFiltro", "dataInicio1", "Período incompleto."));
		}
		if (relatorioFiltro.getDataInicio1() != null && relatorioFiltro.getDataInicio2() != null) {
			if (relatorioFiltro.getDataInicio1().after(relatorioFiltro.getDataInicio2())) {
				result.addError(new FieldError("relatorioFiltro", "dataInicio2", "Período inválido."));
				result.addError(new FieldError("relatorioFiltro", "dataInicio1", ""));
			}
		}

		if (result.hasErrors()) {
			return "relatorio/solicitacoes";
		}

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("reports/solicitacoes.jasper");

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		String paramWhere = "WHERE 1=1";

		if (relatorioFiltro.getParticipanteId() != 0L) {
			paramWhere += " and ee.solicitante_id = " + relatorioFiltro.getParticipanteId().toString();
			parameters.put("participante", usuarioDao.findById(relatorioFiltro.getParticipanteId()).getNome());
		}

		if (relatorioFiltro.getResponsavelPelaIndicacaoId() != 0L) {
			paramWhere += " and ee.chefe_id = "	+ relatorioFiltro.getResponsavelPelaIndicacaoId().toString();
			parameters.put("respIndicacao",	usuarioDao.findById(relatorioFiltro.getResponsavelPelaIndicacaoId()).getNome());
		}

		if (relatorioFiltro.getDataInicio1() != null) {
			paramWhere += " and cast(ee.data_cadastro as date) >= TO_DATE('" + util.formataData(relatorioFiltro.getDataInicio1()) + "', 'dd/MM/yyyy')";
			parameters.put("data1", util.formataData(relatorioFiltro.getDataInicio1()));
		}
		if (relatorioFiltro.getDataInicio2() != null) {
			paramWhere += " and cast(ee.data_cadastro as date) <= TO_DATE('" + util.formataData(relatorioFiltro.getDataInicio2()) + "', 'dd/MM/yyyy')";
			parameters.put("data2", util.formataData(relatorioFiltro.getDataInicio2()));
		}

		parameters.put("paramWhere", paramWhere);

		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
		
		if (relatorio.length < 1024) {
			modelMap.addAttribute("relSolicitacoes", relatorioFiltro);
			modelMap.addAttribute("solicitacoesList", eventoExtraDao.findAll());
			modelMap.addAttribute("usuarioList", usuarioDao.findAll());
			modelMap.addAttribute("mensagemRel", "Os filtros selecionados não geraram resultado.");
			return "relatorio/solicitacoes";
		}
		
		relatorioService.abrirPdf(relatorio, "solicitacoes", response);

		return null;
	}	
		

	@RequestMapping(value = "/relatorio/procuraParticipanteAprovado", method = RequestMethod.GET)
	@ResponseBody
	public List<Participante> procuraParticipante(@RequestParam(value = "eventoId") Long eventoId) {
		List<Participante> participanteList = participanteDao.findParticipantesAprovados(eventoId);
		return util.initializeAndUnproxy(participanteList);
	}

	@RequestMapping(value = "/relatorio/procuraInstrutoresDoEvento", method = RequestMethod.GET)
	@ResponseBody
	public List<Instrutor> procuraInstrutoresDoEvento(@RequestParam(value = "eventoId") Long eventoId) {		
		List<Instrutor> instrutorList = new ArrayList<Instrutor>();
		instrutorList = instrutorDao.findInstrutoresByEventoId(eventoId);
		return instrutorList;
	}

	@RequestMapping(value = "/relatorio/procuraParticipantePorEvento", method = RequestMethod.GET)
	@ResponseBody
	public List<Participante> procuraParticipantePorEvento(@RequestParam(value = "eventoId") Long eventoId) {
		return participanteDao.findByEventoId(eventoId);
	}
	
	@RequestMapping(value = "/relatorio/servicoMalaDireta", method = RequestMethod.GET)
	public String servicoMalaDireta(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		return "relatorio/servicoMalaDireta";
	}
	
	
	@ModelAttribute("eventoList")
	public Collection<Evento> populateEventos() {
		return eventoDao.findAll();
	}

	@ModelAttribute("eventoPresencialList")
	public Collection<Evento> populateEventoPresencial() {
		return eventoDao.findPresencial();
	}

	@ModelAttribute("tipoGastoList")
	public Collection<TipoGasto> populateTipoGastos() {
		return tipoGastoDao.findAll();
	}

	@ModelAttribute("instrutorList")
	public Collection<Instrutor> populateInstrutores() {
		return instrutorDao.findAll();
	}	

	@ModelAttribute("servidorTceList")
	public Collection<Participante> populateServidores() {
		return participanteDao.findByTipo(1L);
	}
	@ModelAttribute("servidorEMembriosTceList")
	public Collection<Participante> populateServidoresEMemebros() {
		return participanteDao.findByTipos(new Long[] {TipoPublicoAlvo.SERVIDOR_ID, TipoPublicoAlvo.MEMBRO_ID});
	}

	@ModelAttribute("paisList")
	public Collection<Pais> populatePaises() {
		return paisDao.findAll();
	}

	@ModelAttribute("modalidadeList")
	public Collection<Modalidade> populateModalidades() {
		return modalidadeDao.findAll();
	}

	@ModelAttribute("provedorEventoList")
	public Collection<ProvedorEvento> populateProvedorEventos() {
		return provedoreventoDao.findAll();
	}

	@ModelAttribute("tipoLocalizacaoEventoList")
	public Collection<TipoLocalizacaoEvento> populateTipoLocalizacaoEventos() {
		return tipoLocalizacaoEventoDao.findAll();
	}

	@ModelAttribute("tipoPublicoAlvoList")
	public Collection<TipoPublicoAlvo> populateTipoPublicoAlvos() {
		return tipoPublicoAlvoDao.findAll();
	}

	@ModelAttribute("eventoGastoList")
	public Collection<Evento> populateEventosGastos() {
		return eventoDao.findEventosGastos();
	}

	@ModelAttribute("nomeEventoAvaliacaoEficaciaList")
	public Collection<Evento> populateNomeEventoAvaliacaoEficacia() {
		return eventoDao.findEventoEficaciaAvaliada();
	}

	@ModelAttribute("nomeParticipanteAvaliacaoEficaciaList")
	public Collection<Participante> populateParticipanteEventoAvaliacaoEficacia() {
		return participanteDao.findParticipanteEficaciaAvaliada();
	}

	@ModelAttribute("tipoEventoList")
	public Collection<TipoEvento> populateTipoEventos() {
		return tipoEventoDao.findAll();
	}

	@ModelAttribute("SNList")
	public Map<String, String> populateMelhorias() {

		Map<String, String> populate = new LinkedHashMap<String, String>();
		populate.put("S", "S");
		populate.put("N", "N");
		return populate;
	}

	@ModelAttribute("anoList")
	public Map<Integer, Integer> populateSN() {
		Map<Integer, Integer> populate = new LinkedHashMap<Integer, Integer>();
		Integer anoInicial = 2012;
		Date dataAtual = new Date();
		@SuppressWarnings("deprecation")
		Integer anoAtual = dataAtual.getYear() + 1901;
		for (int ano = anoAtual; ano >= anoInicial; ano--) {
			populate.put(ano, ano);
		}
		return populate;
	}
	
	@ModelAttribute("anoListIndicador")
	public Map<Integer, Integer> populateSNIndicador() {
		Map<Integer, Integer> populate = new LinkedHashMap<Integer, Integer>();
		Integer anoInicial = 2012;
		Date dataAtual = new Date();
		@SuppressWarnings("deprecation")
		Integer anoAtual = dataAtual.getYear() + 1900;
		for (int ano = anoAtual; ano >= anoInicial; ano--) {
			populate.put(ano, ano);
		}
		return populate;
	}
	
	@ModelAttribute("semestreList")
	public Map<String, String> populateSemestres() {
		Map<String, String> populate = new LinkedHashMap<String, String>();
		populate.put("1", "1º semestre");
		populate.put("2", "2º semestre");
		return populate;
	}

	@ModelAttribute("ufList")
	public Collection<UfMunicipio> populateUfs() {
		return ufMunicipioDao.findAll();
	}

	@ModelAttribute("situacaoList")
	public Map<String, String> populateSituacao() {

		Map<String, String> populate = new LinkedHashMap<String, String>();
		populate.put("1", "Previsto");
		populate.put("2", "Em Andamento");
		populate.put("3", "Realizado");
		return populate;

	}

	@ModelAttribute("eventosApuradosList")
	public Collection<Evento> populateEventosApurados() {
		return eventoDao.findEventosApurados();
	}

	@ModelAttribute("eventosApuradosDoIPCList")
	public Collection<Evento> populateEventosApuradosDoIPC() {
		return eventoDao.findEventosApuradosDoIPC();
	}

	@ModelAttribute("setorList")
	public Collection<Setor> populateSetors() {
		return setorDao.findAll();
	}	
	
	@ModelAttribute("chefeList")
	public Collection<UsuarioSCA> populateChefe() {
		return usuarioDao.findServidores();
	}

	@ModelAttribute("eventoRealizadoList")
	public Collection<Evento> populateEventosRealizados() {
		return eventoDao.findByFiltro(new EventoFiltro(StatusEvento.REALIZADO), false, false);
	}
	
	@ModelAttribute("eventoEmAndamentoList")
	public Collection<Evento> populateEventosEmAndamento() {
		return eventoDao.findByFiltro(new EventoFiltro(StatusEvento.EMANDAMENTO), false, false);
	}

	@ModelAttribute("eixoTematicoList")
	public Collection<EixoTematico> populateEixoTematico() {
		return eixotematicoDao.findAll();
	}
	
	@ModelAttribute("programaList")
	public Collection<Programa> populatePrograma() {
		return programaDao.findAll();
	}
	
}