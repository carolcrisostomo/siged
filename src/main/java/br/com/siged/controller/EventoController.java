package br.com.siged.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.aspectj.lang.ProceedingJoinPoint;
import org.displaytag.pagination.PaginatedList;
import org.displaytag.tags.TableTagParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import br.com.siged.controller.validators.EventoValidator;
import br.com.siged.controller.validators.ParticipanteValidator;
import br.com.siged.dao.AreaConhecimentoDAO;
import br.com.siged.dao.AvaliacaoEficaciaDAO;
import br.com.siged.dao.AvaliacaoReacaoDAO;
import br.com.siged.dao.CertificadoEmitidoDAO;
import br.com.siged.dao.DesempenhoDAO;
import br.com.siged.dao.EixoTematicoDAO;
import br.com.siged.dao.EntidadeDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.EventoHistoricoDAO;
import br.com.siged.dao.FormacaoAcademicaDAO;
import br.com.siged.dao.FrequenciaDAO;
import br.com.siged.dao.GastoDAO;
import br.com.siged.dao.InscricaoDAO;
import br.com.siged.dao.InstrutorDAO;
import br.com.siged.dao.LocalidadeDAO;
import br.com.siged.dao.ModalidadeDAO;
import br.com.siged.dao.ModuloDAO;
import br.com.siged.dao.MunicipioDAO;
import br.com.siged.dao.NivelEscolaridadeDAO;
import br.com.siged.dao.NotaDAO;
import br.com.siged.dao.PaisDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.ProgramaDAO;
import br.com.siged.dao.ProvedorEventoDAO;
import br.com.siged.dao.ResponsavelSetorDAO;
import br.com.siged.dao.SetorDAO;
import br.com.siged.dao.TipoAreaTribunalDAO;
import br.com.siged.dao.TipoEventoDAO;
import br.com.siged.dao.TipoLocalizacaoEventoDAO;
import br.com.siged.dao.TipoPublicoAlvoDAO;
import br.com.siged.dao.UfMunicipioDAO;
import br.com.siged.dao.UsuarioDAO;
import br.com.siged.dao.UsuarioSCADAO;
import br.com.siged.dao.pagination.DisplayTagPageable;
import br.com.siged.editor.AreaConhecimentoEditor;
import br.com.siged.editor.AvaliacaoEficaciaEditor;
import br.com.siged.editor.AvaliacaoReacaoEditor;
import br.com.siged.editor.EixoTematicoEditor;
import br.com.siged.editor.EntidadeEditor;
import br.com.siged.editor.EventoEditor;
import br.com.siged.editor.FormacaoAcademicaEditor;
import br.com.siged.editor.FrequenciaEditor;
import br.com.siged.editor.GastoEditor;
import br.com.siged.editor.InscricaoEditor;
import br.com.siged.editor.LocalidadeEditor;
import br.com.siged.editor.ModalidadeEditor;
import br.com.siged.editor.ModuloEditor;
import br.com.siged.editor.MunicipioEditor;
import br.com.siged.editor.NivelEscolaridadeEditor;
import br.com.siged.editor.NotaEditor;
import br.com.siged.editor.PaisEditor;
import br.com.siged.editor.ParticipanteEditor;
import br.com.siged.editor.ProgramaEditor;
import br.com.siged.editor.ProvedorEventoEditor;
import br.com.siged.editor.SetorEditor;
import br.com.siged.editor.StatusEventoEditor;
import br.com.siged.editor.TipoAreaTribunalEditor;
import br.com.siged.editor.TipoEventoEditor;
import br.com.siged.editor.TipoLocalizacaoEventoEditor;
import br.com.siged.editor.TipoPublicoAlvoEditor;
import br.com.siged.editor.UfMunicipioEditor;
import br.com.siged.entidades.CertificadoEmitido;
import br.com.siged.entidades.Desempenho;
import br.com.siged.entidades.EixoTematico;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.FormacaoAcademica;
import br.com.siged.entidades.Gasto;
import br.com.siged.entidades.Inscricao;
import br.com.siged.entidades.Modalidade;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.NivelEscolaridade;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.Programa;
import br.com.siged.entidades.ProvedorEvento;
import br.com.siged.entidades.ResponsavelSetor;
import br.com.siged.entidades.StatusDesempenho;
import br.com.siged.entidades.StatusEvento;
import br.com.siged.entidades.TipoAreaTribunal;
import br.com.siged.entidades.TipoEvento;
import br.com.siged.entidades.TipoLocalizacaoEvento;
import br.com.siged.entidades.TipoPublicoAlvo;
import br.com.siged.entidades.Usuario;
import br.com.siged.entidades.externas.Setor;
import br.com.siged.entidades.externas.UfMunicipio;
import br.com.siged.filtro.Email;
import br.com.siged.filtro.EventoFiltro;
import br.com.siged.service.AuthenticationService;
import br.com.siged.service.DesempenhoService;
import br.com.siged.service.EventoService;
import br.com.siged.service.InscricaoService;
import br.com.siged.service.exception.ExceptionHandler;
import br.com.siged.service.exception.HttpStatusException;
import br.com.siged.service.exception.NaoPodeApurarEventoException;
import br.com.siged.service.exception.NaoPodeEnviarEmailException;
import br.com.siged.service.exception.NaoPodeRealizarInscricaoException;
import br.com.siged.util.EmailUtil;
import br.com.siged.util.HibernateUtil;
import br.com.siged.util.Util;

@Controller
@RequestMapping("/evento/**")
public class EventoController {
	
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private EixoTematicoDAO eixotematicoDao;
	@Autowired
	private ProgramaDAO programaDao;
	@Autowired
	private ModalidadeDAO modalidadeDao;
	@Autowired
	private ProvedorEventoDAO provedorEventoDao;
	@Autowired
	private TipoAreaTribunalDAO tipoAreaTribunalDao;
	@Autowired
	private TipoLocalizacaoEventoDAO tipoLocalizacaoEventoDao;
	@Autowired
	private TipoPublicoAlvoDAO tipoPublicoAlvoDao;
	@Autowired
	private TipoEventoDAO tipoEventoDao;
	@Autowired
	private FrequenciaDAO frequenciaDao;
	@Autowired
	private GastoDAO gastoDao;
	@Autowired
	private InscricaoDAO inscricaoDao;	
	@Autowired
	private AvaliacaoReacaoDAO avaliacaoreacaoDao;
	@Autowired
	private AvaliacaoEficaciaDAO avaliacaoEficaciaDao;
	@Autowired
	private ModuloDAO moduloDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private InstrutorDAO instrutorDao;	
	@Autowired
	private NivelEscolaridadeDAO nivelEscolaridadeDao;
	@Autowired
	private FormacaoAcademicaDAO formacaoAcademicaDao;
	@Autowired
	private UsuarioSCADAO usuarioDao;
	@Autowired
	private UsuarioDAO usuarioExternoDao;	
	@Autowired
	private EventoHistoricoDAO eventoHistoricoDao;
	@Autowired
	private DesempenhoDAO desempenhoDao;
	@Autowired
	private EntidadeDAO entidadeDAO;	
	@Autowired
	private UfMunicipioDAO ufMunicipioDAO;
	@Autowired
	private MunicipioDAO municipioDAO;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private Util util;
	@Autowired
	private ResponsavelSetorDAO responsavelSetorDao;
	@Autowired
	private CertificadoEmitidoDAO certificadoEmitidoDao;
	@Autowired
	private SetorDAO setorDao;
	@Autowired
	private PaisDAO paisDao;
	@Autowired
	private NotaDAO notaDao;
	@Autowired
	private AreaConhecimentoDAO areaConhecimentoDao;
	@Autowired
	private LocalidadeDAO localidadeDao;

	@Autowired
	private DesempenhoService desempenhoService;
	
	@Autowired
	private EventoService eventoService;
	
	@Autowired
	private InscricaoService inscricaoService;
	
	@Autowired
	private EventoValidator eventoValidator;
	
	@Autowired
	private ParticipanteValidator participanteValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(br.com.siged.entidades.Modalidade.class, new ModalidadeEditor(modalidadeDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.ProvedorEvento.class, new ProvedorEventoEditor(provedorEventoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.TipoAreaTribunal.class, new TipoAreaTribunalEditor(tipoAreaTribunalDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.TipoLocalizacaoEvento.class, new TipoLocalizacaoEventoEditor(tipoLocalizacaoEventoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.TipoPublicoAlvo.class, new TipoPublicoAlvoEditor(tipoPublicoAlvoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.TipoEvento.class, new TipoEventoEditor(tipoEventoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Frequencia.class, new FrequenciaEditor(frequenciaDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Gasto.class, new GastoEditor(gastoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Inscricao.class, new InscricaoEditor(inscricaoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.AvaliacaoReacao.class, new AvaliacaoReacaoEditor(avaliacaoreacaoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Modulo.class, new ModuloEditor(moduloDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Entidade.class, new EntidadeEditor(entidadeDAO));
		dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Localidade.class, new LocalidadeEditor(localidadeDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Evento.class, new EventoEditor(eventoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.externas.UfMunicipio.class, new UfMunicipioEditor(ufMunicipioDAO));		
		dataBinder.registerCustomEditor(br.com.siged.entidades.Participante.class, new ParticipanteEditor(participanteDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.EixoTematico.class, new EixoTematicoEditor(eixotematicoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Programa.class, new ProgramaEditor(programaDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Pais.class, new PaisEditor(paisDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Nota.class, new NotaEditor(notaDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.AreaConhecimento.class, new AreaConhecimentoEditor(areaConhecimentoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.AvaliacaoEficacia.class, new AvaliacaoEficaciaEditor(avaliacaoEficaciaDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.NivelEscolaridade.class, new NivelEscolaridadeEditor(nivelEscolaridadeDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.FormacaoAcademica.class, new FormacaoAcademicaEditor(formacaoAcademicaDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Setor.class, new SetorEditor(setorDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.externas.UfMunicipio.class, new UfMunicipioEditor(ufMunicipioDAO));
		dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Municipio.class, new MunicipioEditor(municipioDAO));
		dataBinder.registerCustomEditor(br.com.siged.entidades.StatusEvento.class, new StatusEventoEditor());
		
		dataBinder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
		dataBinder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, new DecimalFormat("#,###,###.00"), true));
		
		if(dataBinder.getTarget() instanceof Evento)
			dataBinder.setValidator(eventoValidator);
		
		if(dataBinder.getTarget() instanceof Participante)
			dataBinder.setValidator(participanteValidator);
	}

	@RequestMapping(value = "/evento/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		Evento evento = eventoDao.find(id);
		if(evento == null)
			throw new HttpStatusException(HttpStatus.NOT_FOUND);
		
		Usuario usuarioLogado = AuthenticationService.getUsuarioLogadoOrNull();
		if(usuarioLogado != null && usuarioLogado.isAluno()) {
			Participante participante = participanteDao.findByCpf(usuarioLogado.getCpf());
			Boolean isEventoRestrito = evento.getPublicado().equals("N") || (evento.getIsRestrito() && !evento.inInteressados(participante));
			if(isEventoRestrito && !evento.isParticipanteInscricaoConfirmada(participante))
				throw new HttpStatusException(HttpStatus.FORBIDDEN);
		}
		
		modelMap.addAttribute("evento", evento);
		modelMap.addAttribute("eventohistorico", eventoHistoricoDao.findByEvento(evento));
		modelMap.addAttribute("hoje", new SimpleDateFormat ("yyyy-MM-dd").format(new Date()));
		return "evento/show";
	}

	@RequestMapping(value = "/evento/divulgarevento", method = RequestMethod.GET)
	public String divulgarevento(ModelMap modelMap, HttpServletRequest request) {
		modelMap.addAttribute("email", new Email());
		modelMap.addAttribute("eventoList", eventoDao.findAll());
		return "evento/divulgarevento";
	}

	@RequestMapping(value = "/evento/divulgarevento", method = RequestMethod.POST)
	public String divulgarevento(@ModelAttribute("email") Email email, BindingResult result, ModelMap modelMap) throws Exception {

		if (email.getEventoId() == 0) {
			result.addError(new FieldError("email", "eventoId", "Campo Obrigatório"));			
		}else{			
			List<Modulo> modulos = moduloDao.findByEventoId(email.getEventoId());
			if(modulos.size() == 0)
				result.addError(new FieldError("email", "eventoId", "Evento selecionado não possui módulo"));
		}
		if (email.getTo() == null || email.getTo() == "") {
			result.addError(new FieldError("email", "to", "Campo Obrigatório"));			
		}
		if (result.hasErrors()) {
			modelMap.addAttribute("eventoList", eventoDao.findAll());
			return "/evento/divulgarevento";
		}
		
		emailUtil.emailDivulgarEvento(email);
		
		return "redirect:/evento/divulgarevento?mensagem=Divulgação do evento realizada com sucesso.";	
	}
	
	@RequestMapping(value = "/evento/apurardesempenho", method = RequestMethod.GET)
	public String apurardesempenho(ModelMap modelMap) {
		modelMap.addAttribute("evento", new Evento());
		modelMap.addAttribute("eventoRealizadoOuEmAndamentoComModuloRealizadoList", eventoDao.findRealizadoOrEmAndamentoComModuloRealizado());
		return "evento/apurardesempenho";
	}

	@RequestMapping(value = "/evento/apurardesempenho", method = RequestMethod.POST)
	public String apurardesempenho(Evento evento, ModelMap modelMap) {
		return "redirect:/evento/"+ evento.getId() + "/divulgardesempenho";
	}
	
	@RequestMapping(value = "/evento/{id}/divulgardesempenho", method = RequestMethod.GET)
	public String divulgardesempenho(@PathVariable("id") Long id, ModelMap modelMap) throws ParseException {
		Evento evento = eventoDao.find(id);
		try {
			desempenhoService.apurarEvento(evento, modelMap);
		} catch (NaoPodeApurarEventoException e) {
			modelMap.addAttribute("mensagemErro", e.getMessage());
			modelMap.addAttribute("evento", new Evento());
			modelMap.addAttribute("eventoRealizadoOuEmAndamentoComModuloRealizadoList", eventoDao.findRealizadoOrEmAndamentoComModuloRealizado());
			return "redirect:/evento/apurardesempenho";
		}
		
		return "evento/divulgardesempenho";
	}
	
	@RequestMapping(value = "/evento/{id}/divulgardesempenhopagination", method = RequestMethod.GET)
	public String divulgardesempenhopagination(@PathVariable("id") Long id, ModelMap modelMap) {
		Evento evento = eventoDao.find(id);
		
		int totalDeAprovados = 0;		

		List<Desempenho> desempenhoFinalList = new ArrayList<Desempenho>();
		List<Object[]> desempenhoObjectList = desempenhoDao.countAprovadosByEvento(evento);
		
		final boolean isParcial = desempenhoDao.isApuracaoParcial(evento);
		
		if(!isParcial) {
			for (Object[] o : desempenhoObjectList) {
				Desempenho df = new Desempenho();
				df.setParticipanteId(participanteDao.find(((BigDecimal)o[0]).longValue()));
				df.setAprovado(o[1].toString());
				if(o[1].toString().equals("S"))
					totalDeAprovados++;
				desempenhoFinalList.add(df);
			}
			modelMap.addAttribute("desempenhoFinal", desempenhoFinalList);
			modelMap.addAttribute("totalDeParticipantes", desempenhoFinalList.size());
			if( desempenhoFinalList.size() > 0 ){
				modelMap.addAttribute("totalDeAprovados",totalDeAprovados);
				modelMap.addAttribute("taxaDeAproveitamento",String.format("%.1f",(1.0*totalDeAprovados/desempenhoFinalList.size())*100));
			}
		}
		
		modelMap.addAttribute("desempenho", desempenhoDao.findByEvento(evento));
		modelMap.addAttribute("evento", evento);
		modelMap.addAttribute("parcial", isParcial);
		return "evento/divulgardesempenho";
	}
	
	@RequestMapping(value = "/evento/desempenhos/{id}", method = RequestMethod.GET)
	public String desempenhos(@PathVariable("id") Long id, ModelMap modelMap, HttpServletRequest request) {

		List<Desempenho> desempenhos = new ArrayList<Desempenho>();
		
		Participante participante = (Participante) request.getSession().getAttribute("PARTICIPANTE");

		if (request.isUserInRole("ROLE_ALUNO")) {
			desempenhos = desempenhoDao.findByEventoAndParticipante(eventoDao.find(id), participante);
		}
		if (request.isUserInRole("ROLE_ADMINISTRADOR") || request.isUserInRole("ROLE_ADMINISTRADORCONS")) {
			desempenhos = desempenhoDao.findByEvento(eventoDao.find(id));
		}
		if (request.isUserInRole("ROLE_CHEFE")) {
			
			List<ResponsavelSetor> rs = responsavelSetorDao.findAtivoByResponsavel(usuarioDao.findByLogin(request.getRemoteUser()));
			
			for (ResponsavelSetor responsavelSetor : rs) {
				if(responsavelSetor.getSetor() != null) {
					desempenhos.addAll(desempenhoDao.findByEventoAndSetor(eventoDao.find(id), responsavelSetor.getSetor().getId().toString()));
					
					LinkedHashSet<Desempenho> hashSet = new LinkedHashSet<>(desempenhos); // remove repetidos
					desempenhos = new ArrayList<Desempenho>(hashSet);
				}	
			}				
		}
		
		List<Desempenho> desempenhoFinal = new ArrayList<Desempenho>();
		Long idmoduloBase = desempenhos.get(0).getModuloId().getId();
		Desempenho desempenhofinal = new  Desempenho();
		Boolean isParcial = false;
				
		int totalDeAprovados = 0;

		for (int i = 0; i<desempenhos.size(); i++) {
			if(desempenhos.get(i).getModuloId().getId().equals(idmoduloBase) ) {
				StatusDesempenho statusDesempenho = desempenhoService.statusDesempenhoParticipanteNoEvento(desempenhos.get(i).getParticipanteId(), desempenhos.get(i).getEventoId());
				
				if(statusDesempenho.equals(StatusDesempenho.PARCIAL))
					isParcial = true;
				if(statusDesempenho.equals(StatusDesempenho.APROVADO))
					totalDeAprovados++;

				desempenhofinal = new Desempenho(desempenhos.get(i).getEventoId(), desempenhos.get(i).getModuloId(),  desempenhos.get(i).getParticipanteId(),"0", "0", statusDesempenho.getAprovado());
				desempenhoFinal.add(desempenhofinal);
				desempenhofinal = new Desempenho();
			} else {
				break;
			}
		}
		
		modelMap.addAttribute("desempenhoFinal",desempenhoFinal);
		modelMap.addAttribute("desempenhos",desempenhos);
		modelMap.addAttribute("parcial", isParcial);
		if( desempenhoFinal.size() > 0 ){
			modelMap.addAttribute("totalDeAprovados",totalDeAprovados);
			modelMap.addAttribute("taxaDeAproveitamento",String.format("%.1f",(1.0*totalDeAprovados/desempenhoFinal.size())*100));
		}
		return "evento/desempenhos";
	}
	
	@RequestMapping(value = "/evento/previstos/acessonegado", method = RequestMethod.GET)
	public String acessonegado(ModelMap modelMap, HttpServletRequest request) {

		modelMap.addAttribute("mensagemErro", "Acesso negado.");
		return "redirect:/evento/previstos";
	}
	
	@RequestMapping(value = "/evento/previstos/logininvalido", method = RequestMethod.GET)
	public String logininvalido(ModelMap modelMap, HttpServletRequest request) {
		
		String mensagem = "Não foi possível realizar a autenticação.";
		
		String mensagemErro = request.getParameter("mensagem");
		
		if ( mensagemErro != null && !mensagemErro.isEmpty() )
			mensagem = mensagemErro;		
		
		modelMap.addAttribute("mensagemErro", mensagem);
		
		return "redirect:/evento/previstos";
	}

	@RequestMapping(value = "/eventos/{status}/{modalidadeId}", method = RequestMethod.GET)
	public String eventos(@PathVariable("status") StatusEvento statusEvento, @PathVariable("modalidadeId") Long modalidadeId, EventoFiltro eventofiltro, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
		
		Modalidade modalidade = modalidadeDao.findById(modalidadeId);
		if(modalidade == null || statusEvento == null)
			throw new HttpStatusException(HttpStatus.NOT_FOUND);
		
		if (modalidade.getId() == 1) {
			modalidade.setDescricao("Presencial");
		} else if (modalidade.getId() == 2 ) {
			modalidade.setDescricao("A Distância (EAD)");
		}
		
		eventofiltro.setStatusEvento(statusEvento);
		eventofiltro.setModalidadeId(modalidadeId);
		
		request.setAttribute("eventofiltro", eventofiltro);
		request.setAttribute("modalidade", modalidade);
		
		if (!statusEvento.equals(StatusEvento.REALIZADO) && request.getAttribute("eventos") == null){
			/*
			 * DONE: __Modalidade Evento: Definir qual será a Modalidade do Evento na consulta (Criteria)
			 * Consulta atualizada em EventoDAO.getCriteria
			 */
			request.setAttribute("eventos", eventoDao.findEventosPaginaInicial(new EventoFiltro(statusEvento, modalidadeId)));
		}
		
		return "forward:/evento/" + statusEvento.getFiltro();
	}
	
	@RequestMapping(value = "/evento/searcheventos", method = RequestMethod.GET)
	public String searcheventos(EventoFiltro eventofiltro, ModelMap modelMap, HttpServletRequest request, ServletResponse Response) throws IOException {
		StatusEvento status = eventofiltro.getStatusEvento();
		Long modalidade = eventofiltro.getModalidadeId();
		
		request.setAttribute("eventofiltro", eventofiltro);
		
		/*
		 * DONE: __Modalidade Evento: Definir qual será a Modalidade do Evento na consulta (Criteria)
		 * Consulta atualizada em EventoDAO.getCriteria
		 */
		request.setAttribute("eventos", eventoDao.findEventosPaginaInicial(eventofiltro));
		
		if(modalidade != null && status != null) {
			return "forward:/evento/eventos/" + status.getFiltro() + "/" + modalidade;
		} else if (eventofiltro.getStatusEvento() != null) {
			return "forward:/evento/" + status.getFiltro();
		} else {
			throw new HttpStatusException(HttpStatus.NOT_FOUND);
		}
	}

	
	@RequestMapping(value = "/evento/previstos", method = RequestMethod.GET)
	public String previstos(ModelMap modelMap, HttpServletRequest request, ServletResponse Response) {		
		
		if (request.isUserInRole("ROLE_ADMINISTRADOR")) {			
			Usuario usuario = usuarioExternoDao.findByLogin(request.getRemoteUser());			
			request.getSession().setAttribute("USUARIO", usuario.getNome());
		} else if (request.isUserInRole("ROLE_ADMINISTRADORCONS")) {
			request.getSession().setAttribute("USUARIO", "ADMINISTRADORCONS");
		}
		
		if (request.isUserInRole("ROLE_SERVIDOR")) {
			
			Usuario usuario = AuthenticationService.getUsuarioLogadoOrNull();			
			Participante servidor = participanteDao.findServidorByCpf(usuario.getCpf()); // POSSUI DADOS DO SRH
			
			if (servidor != null && servidor.getNome() != null && servidor.getCpf() != null && servidor.getEmail() != null && servidor.getSetorId() != null) {
				
				// CLASSIFICA O SERVIDOR COMO MEMBRO OU SERVIDOR-TCE
				if(servidor.getIdTipoOcupacao() == 1L){
					servidor.setTipoId(tipoPublicoAlvoDao.find(4L)); // MEMBRO
				} else {
					servidor.setTipoId(tipoPublicoAlvoDao.find(1L)); // SERVIDOR-TCE
				}
				
				Participante participante = participanteDao.findByCpf(usuario.getCpf());
				
				//CASO SEJA SERVIDOR E NAO ESTEJA NA TABELA PARTICIPANTE, CADASTRA O SERVIDOR NA TABELA PARTICIPANTE
				if (participante == null) {									
					participanteDao.persist(new Participante(null, servidor.getCelular(), usuario.getCpf(), servidor.getEmail(), "TCE-CE" , servidor.getNome(), servidor.getTelefone(), "GERADO AUTOMATICAMENTE", null, null, servidor.getSetorId(), servidor.getTipoId(), null, null, null, null, null, null, new Date()));
				} else { //ATUALIZA DADOS DO PARTICIPANTE								
					participante.setNome(servidor.getNome());
					// participante.setCpf(servidor.getCpf());
					participante.setEmail(servidor.getEmail());
					participante.setSetorId(HibernateUtil.unproxy(servidor.getSetorId()));
					participante.setTipoId(servidor.getTipoId());
					
					participanteDao.merge(participante);					
				}
				
				participante = participanteDao.findByCpf(usuario.getCpf());
				
				request.getSession().setAttribute("PARTICIPANTE", participante);
				
				Setor setor = participante.getSetorId();
				if (setor != null) {
					request.getSession().setAttribute("SETORID", setor.getId());
				}
				
				request.getSession().setAttribute("USUARIO", participante.getNome());
				request.getSession().setAttribute("TIPO_PARTICIPANTE", participante.getTipoId().getDescricao());
				request.getSession().setAttribute("PARTICIPANTE_ID", participante.getId());			
				
			} else {
				modelMap.addAttribute("mensagem","Não foi possível efetuar o seu login devido a alguns dados ausentes na base de dados do SRH . \n Por favor, entrar em contato com o RH.");
				AuthenticationService.addUserAuthority("ROLE_UNKNOWN");	//ADICIONAR ROLE_UNKNOWN
				return "redirect:/usuario/usuariodesconhecido";
			}						
		
		} else if (request.isUserInRole("ROLE_ALUNO")) {
			
			Usuario usuario = usuarioExternoDao.findByLogin(request.getRemoteUser());			
			Participante participante = participanteDao.findByCpf(usuario.getCpf());
			
			if (participante == null){
				AuthenticationService.removeUserAuthority("ROLE_ALUNO");
				AuthenticationService.addUserAuthority("ROLE_ANONYMOUS");
				modelMap.addAttribute("mensagemErro","Usuário sem participante cadastrado. Realize uma pré-inscrição em detalhes do Evento ou entre em contato com o IPC.");
				return "redirect:/evento/previstos";
			}
			
			request.getSession().setAttribute("PARTICIPANTE", participante);
			request.getSession().setAttribute("USUARIO", participante.getNome());
			request.getSession().setAttribute("TIPO_PARTICIPANTE", participante.getTipoId().getDescricao());
			request.getSession().setAttribute("PARTICIPANTE_ID", participante.getId());
			
			//TROCAR SENHA CASO SENHA SEJA 12345
			if (usuario.getPassword().compareToIgnoreCase("827ccb0eea8a706c4c34a16891f84e7b") == 0 ) {				
				modelMap.addAttribute("mensagem","Por favor, troque sua senha. Ela deve ser diferente de \"12345\".");				
				return "redirect:/usuario/trocarsenha";
			}
			
			if (usuario.isForcarAtualizacaoEmail()) {			
				return "redirect:/usuario/atualizaremail";
			}
		}		
		
		if (request.getAttribute("eventos") == null){
			List<Evento> eventos = eventoDao.findEventosPaginaInicial(new EventoFiltro(StatusEvento.PREVISTO));
	
			List<Evento> eventoList = new ArrayList<Evento>();
			for (Evento evento : eventos) {
				if(evento.getModuloList().size() > 1){
					List<Modulo> modulos = evento.getModuloList();
					Collections.sort(modulos, util.getComparadorPorData());
					evento.setModuloList(modulos);
				}	
				eventoList.add(evento);
			}
		
			modelMap.addAttribute("eventos", eventoList);
		
		}else{			
			modelMap.addAttribute("eventos", request.getAttribute("eventos"));		
		}
		
		if (request.getAttribute("eventofiltro") == null)			
			modelMap.addAttribute("eventofiltro", new EventoFiltro());			
		else
			modelMap.addAttribute("eventofiltro", request.getAttribute("eventofiltro"));
		
		if (request.isUserInRole("ROLE_ALUNO") || request.isUserInRole("ROLE_SERVIDOR")) {		
			Long participanteId = (Long) request.getSession().getAttribute("PARTICIPANTE_ID");			
			modelMap.addAttribute("eventoIdInscritoList", inscricaoDao.findEventoIdByParticipanteId(participanteId));			
		}		
				
		modelMap.addAttribute("modalidade", request.getAttribute("modalidade"));
		modelMap.addAttribute("hoje", new SimpleDateFormat ("yyyy-MM-dd").format(new Date()));
		modelMap.addAttribute("random", new Random().nextInt());
		
		return "evento/previstos";
	}
	
	@RequestMapping(value = "/evento/emandamento", method = RequestMethod.GET)
	public String emandamento(ModelMap modelMap, HttpServletRequest request) {

		if (request.getAttribute("eventos") == null){
			
			List<Evento> eventos = eventoDao.findEventosPaginaInicial(new EventoFiltro(StatusEvento.EMANDAMENTO));
			
			List<Evento> eventoList = new ArrayList<Evento>();
			for (Evento evento : eventos) {
				if(evento.getModuloList().size()>1){
					List<Modulo> modulos = evento.getModuloList();
					Collections.sort(modulos, util.getComparadorPorData());
					evento.setModuloList(modulos);
				}
	
				eventoList.add(evento);
			}
		
			modelMap.addAttribute("eventos", eventoList);
		
		}else{			
			modelMap.addAttribute("eventos", request.getAttribute("eventos"));		
		}
		
		if (request.getAttribute("eventofiltro") == null)			
			modelMap.addAttribute("eventofiltro", new EventoFiltro());			
		else
			modelMap.addAttribute("eventofiltro", request.getAttribute("eventofiltro"));		
		
		if (request.isUserInRole("ROLE_ALUNO") || request.isUserInRole("ROLE_SERVIDOR")) {		
			Long participanteId = (Long) request.getSession().getAttribute("PARTICIPANTE_ID");			
			modelMap.addAttribute("eventoIdInscritoList", inscricaoDao.findEventoIdByParticipanteId(participanteId));			
		}

		modelMap.addAttribute("modalidade", request.getAttribute("modalidade"));	
		modelMap.addAttribute("hoje", new SimpleDateFormat ("yyyy-MM-dd").format(new Date()));
		modelMap.addAttribute("random", new Random().nextInt());
		
		return "evento/emandamento";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/evento/realizados", method = RequestMethod.GET)
	public String realizados(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
		boolean export = request.getParameter(TableTagParameters.PARAMETER_EXPORTING) != null;


		List<Evento> eventos = new ArrayList<Evento>();
		
		boolean mostrarMensagem = false;
		
		if (request.getAttribute("eventos") != null){			
			eventos = (List<Evento>) request.getAttribute("eventos");	
			mostrarMensagem = true;
		} 
		
		List<Evento> eventoList = new ArrayList<Evento>();
		for (Evento evento : eventos) {
			if (request.isUserInRole("ROLE_ALUNO")) {
				if (desempenhoDao.findByEventoAndParticipante(evento, (Participante)request.getSession().getAttribute("PARTICIPANTE")).size() != 0) {
					evento.setDesempenho("S");
				} else {
					evento.setDesempenho("N");
				}
			}
			if (request.isUserInRole("ROLE_ADMINISTRADOR") || request.isUserInRole("ROLE_ADMINISTRADORCONS")) {
				if (desempenhoDao.findByEvento(evento).size() != 0) {
					evento.setDesempenho("S");
				} else {
					evento.setDesempenho("N");
				}
			}
			if (request.isUserInRole("ROLE_CHEFE")) {
				if (desempenhoDao.findByEventoAndSetor(evento, request.getSession().getAttribute("SETORID").toString()).size() != 0) {
					evento.setDesempenho("S");
				} else {
					evento.setDesempenho("N");
				}
			}
			if(evento.getModuloList().size()>1){
				List<Modulo> modulos = evento.getModuloList();
				Collections.sort(modulos, util.getComparadorPorData());
				evento.setModuloList(modulos);
			}
			
			eventoList.add(evento);
		}		
				
		modelMap.addAttribute("eventos", eventoList);
		modelMap.addAttribute("mostrarMensagem", mostrarMensagem);
		
		if (request.getAttribute("eventofiltro") == null)			
			modelMap.addAttribute("eventofiltro", new EventoFiltro());			
		else
			modelMap.addAttribute("eventofiltro", request.getAttribute("eventofiltro"));		
		
		if (request.isUserInRole("ROLE_ALUNO") || request.isUserInRole("ROLE_SERVIDOR")) {		
			Long participanteId = (Long) request.getSession().getAttribute("PARTICIPANTE_ID");			
			modelMap.addAttribute("eventoIdInscritoList", inscricaoDao.findEventoIdByParticipanteId(participanteId));			
		}

		modelMap.addAttribute("modalidade", request.getAttribute("modalidade"));	
		modelMap.addAttribute("hoje", new SimpleDateFormat ("yyyy-MM-dd").format(new Date()));
		modelMap.addAttribute("random", new Random().nextInt());
		
		return "evento/realizados";
	}

	@RequestMapping(value = "/evento", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("eventofiltro", new EventoFiltro());		
		modelMap.addAttribute("random", new Random().nextInt());
		return "evento/list";
	}
	
	@RequestMapping(value = "/evento/search", method = RequestMethod.GET)
	public String search(EventoFiltro eventofiltro, ModelMap modelMap, HttpServletRequest request, ServletResponse Response) throws IOException {
		boolean export = request.getParameter(TableTagParameters.PARAMETER_EXPORTING) != null;

		DisplayTagPageable pageable = new DisplayTagPageable(request);
		
		
		PaginatedList displayTagList = eventoDao.filtrar(eventofiltro, pageable, export);
		
		System.out.println(displayTagList.getList().size());
		
		modelMap.addAttribute("eventofiltro", eventofiltro);
		modelMap.addAttribute("eventos", displayTagList);
		modelMap.addAttribute("random", new Random().nextInt());

		return "evento/list";
	}

	@RequestMapping(value = "/evento/meuseventos", method = RequestMethod.GET)
	public String meusEventos(EventoFiltro eventofiltro, ModelMap modelMap, HttpServletRequest request) {
		Participante participante = (Participante) request.getSession().getAttribute("PARTICIPANTE");
		String cpf = participante.getCpf();
		
		boolean eventosComoParticipante = true;
		if(eventofiltro.getComoInstrutor() == 1) {
			cpf = formatarCpf(cpf);
			eventosComoParticipante = false;
		}

		eventofiltro.setParticipanteCPF(cpf);
		DisplayTagPageable pageable = new DisplayTagPageable(request);
		PaginatedList displayTagList = eventoDao.filtrar(eventofiltro, pageable);
		
		modelMap.addAttribute("eventofiltro", eventofiltro);
		
		modelMap.addAttribute("eventos", displayTagList);
		modelMap.addAttribute("eventosComoParticipante", eventosComoParticipante);
		modelMap.addAttribute("participante", participante);
		modelMap.addAttribute("random", new Random().nextInt());
		
		return "evento/meuseventos";
	}
	
	@RequestMapping(value = "/evento/meuseventoscomoinstrutor", method = RequestMethod.GET)
	public String eventosComoInstrutor(EventoFiltro eventofiltro, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
		
		Participante participante = (Participante) request.getSession().getAttribute("PARTICIPANTE");
		String cpf = participante.getCpf();
		eventofiltro.setParticipanteCPF(formatarCpf(cpf));
		
		DisplayTagPageable pageable = new DisplayTagPageable(request);
		PaginatedList displayTagList = eventoDao.filtrar(eventofiltro, pageable);
		
		modelMap.addAttribute("eventofiltro", eventofiltro);
		modelMap.addAttribute("eventos", displayTagList);
		return "evento/meuseventos";
		
	}		

	@RequestMapping(value = "/evento/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		try {
			desempenhoDao.deleteByEventoId(id);
			certificadoEmitidoDao.deleteByEventoId(id);		
			eventoDao.remove(eventoDao.find(id));
		} catch (Exception e) {
			return "redirect:/evento?mensagem=Não foi possível excluir o evento. Verifique se existem notas ou frequências lançadas no evento.";
		}
		
		return "redirect:/evento";
	}

	@RequestMapping(value = "/evento/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("evento", new Evento());
		modelMap.addAttribute("interessadosList", new ArrayList<Participante>());
		return "evento/create";
	}

	@RequestMapping(value = "/evento", method = RequestMethod.POST)
	public String create(@ModelAttribute("evento") @Valid Evento evento, BindingResult result, ModelMap modelMap, HttpServletRequest request) throws IOException {			
		List<Participante> interessados = interessadosDoEventoList(request);
		evento.setInteressados(interessados);
		
		if (result.hasErrors()) {
			modelMap.addAttribute("interessadosList", interessados);
			return "evento/create";
		}
		
		eventoService.salvar(evento);

		if (evento.getModuloUnico().equals("S")) {
			/*
			 * Campo localização no evento foi depreciado.
			Modulo modulo = new Modulo(null, evento.getCargaHoraria(), evento.getDataFimPrevisto(), evento.getDataInicioPrevisto(), "MÓDULO ÚNICO", "", "75", new Date(), "", "", "", "", "", "", "", evento, evento.getLocalizacaoId());
			 */
			Modulo modulo = new Modulo(null, evento.getCargaHoraria(), evento.getDataFimPrevisto(), evento.getDataInicioPrevisto(), "MÓDULO ÚNICO", "", "75", new Date(), "", "", "", "", "", "", "", evento, null);

			modelMap.addAttribute("modulo", modulo);
			modelMap.addAttribute("readonly", true);
			modelMap.addAttribute("eventoList", eventoDao.findAll());
			modelMap.addAttribute("instrutorList", instrutorDao.findAll());
			return "modulo/create";
		}

		return "redirect:/evento";
	}

	@RequestMapping(value = "/evento/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		Evento evento = eventoDao.find(id);
		modelMap.addAttribute("evento", evento);
		modelMap.addAttribute("interessadosList", evento.getInteressados());
		modelMap.addAttribute("eventohistorico", eventoHistoricoDao.findByEvento(evento));
		
		List<CertificadoEmitido> certificadosEmitidos = certificadoEmitidoDao.findByEventoId(id);
		if ( certificadosEmitidos != null && certificadosEmitidos.size() > 0 )
			modelMap.addAttribute("temCertificadoEmitido", true);

		return "evento/update";
	}

	@Transactional
	@RequestMapping(value = "/evento/{id}/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("evento") @Valid Evento evento, BindingResult result, ModelMap modelMap, HttpServletRequest request) throws IOException {
		List<Participante> interessados = interessadosDoEventoList(request);
		evento.setInteressados(interessados);
		
		List<CertificadoEmitido> certificadosEmitidos = certificadoEmitidoDao.findByEventoId(evento.getId());
		
		if (result.hasErrors()) { 
			modelMap.addAttribute("eventohistorico", eventoHistoricoDao.findByEvento(evento));
			modelMap.addAttribute("interessadosList", interessados);
			if ( certificadosEmitidos != null && certificadosEmitidos.size() > 0 )
				modelMap.addAttribute("temCertificadoEmitido", true);
			
			return "evento/update";
		}		
		
		try {
			eventoService.salvar(evento);
		} catch (NaoPodeEnviarEmailException e) {
			return "redirect:/evento?mensagem=Ocorreu um erro ao enviar o email aos participantes sobre os certificados invalidados.";
		}
		
		return "redirect:/evento";
	}
	
	private List<Participante> interessadosDoEventoList(HttpServletRequest request) {
		String participantesId[] = request.getParameter("interessadosHidden").split(",");		
		
		List<Participante> interessados = new ArrayList<>();
		
		for (String id : participantesId) {
			if(!id.isEmpty()) {
				Participante participante = participanteDao.find(new Long(id));
				if(participante != null)
					interessados.add(participante);
			}
		}
		return interessados;
	}

	@RequestMapping(value = "/evento/participantes/{id}", method = RequestMethod.GET)
	public String participantes(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("participantes", inscricaoDao.findByEventoAndConfirmada(id,"S"));
		
		return "evento/participantes";
	}

	@RequestMapping(value = "/evento/gastos/{id}", method = RequestMethod.GET)
	public String gastos(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("gastos", gastoDao.findByEvento(id));

		List<Gasto> listgasto= gastoDao.findByEvento(id);

		BigDecimal total = new BigDecimal(0);
		for (int i=0; i <listgasto.size(); i++){
			total = total.add(listgasto.get(i).getValor()) ;
		}
		
		modelMap.addAttribute("total", total);
		
		return "evento/gastos";
	}
	
	/* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 * BEGIN: Inscrições feitas por alunos
	 */

	/**
	 * Monta a página de inscrição para usuários deslogados 
	 * @param id
	 * @param modelMap
	 * @return
	 * 
	 * @see {@link #verificarPossibilidadeDePreInscricaoAluno(Evento)}
	 */
	@RequestMapping(value = "/evento/inscricao/{id}", method = RequestMethod.GET)
	public String inscricao(@PathVariable("id") Long id, ModelMap modelMap) {
		
		Evento evento = eventoDao.find(id);
		verificarPossibilidadeDePreInscricaoAluno(evento);
		
		modelMap.addAttribute("participante", new Participante());
		modelMap.addAttribute("eventoId", id);		
		modelMap.addAttribute("nomeEvento", evento.getTipoId().getDescricao()+" "+ evento.getTitulo());
		modelMap.addAttribute("publicoAlvo", evento.getPublicoAlvoLista());
		modelMap.addAttribute("localidadeList", localidadeDao.findByMunicipios());
		return "evento/inscricao";
	}
	
	/**
	 * Salva a inscrição do aluno. Cria o usuário, caso seja a primeira inscrição do aluno e envia o email avisando que a pré-incrição foi recebida e será avaliada pelo IPC
	 * @param id
	 * @param participante
	 * @param result
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception Lançada por {@link EmailUtil#emailPreInscricao(Long, Participante)}
	 * 
	 * @see {@link #verificarPossibilidadeDePreInscricaoAluno(Evento)}
	 */
	@RequestMapping(value = "/evento/inscricao/{id}", method = RequestMethod.POST)
	public String inscricao(@PathVariable("id") Long id, @ModelAttribute("participante") @Valid Participante participante, BindingResult result, HttpServletRequest request, ModelMap modelMap) throws Exception {

		Evento evento = eventoDao.find(id);
		verificarPossibilidadeDePreInscricaoAluno(evento);
		
		/**
		 * Verificação necessária pois em InscricaoService#verificarPossibilidadeDePreInscricaoAluno(Evento evento) 
		 * não é possível identificar o tipo do participante para usuários não logados
		 */
		if(participante != null && participante.getTipoId() != null && !evento.inPublicoAlvo(participante))
			result.addError(new FieldError("participante", "tipoId", "Tipo do participante não compatível com o público-alvo do evento."));
		
		if (result.hasErrors()) { 
			modelMap.addAttribute("participante", participante);
			modelMap.addAttribute("eventoId", id);		
			modelMap.addAttribute("nomeEvento", evento.getTipoId().getDescricao()+" "+ evento.getTitulo());
			modelMap.addAttribute("publicoAlvo", evento.getPublicoAlvoLista());
			modelMap.addAttribute("localidadeList", localidadeDao.findByMunicipios());
			return "evento/inscricao";
		}
		
		Inscricao inscricao = new Inscricao(null, "-", null, new Date(), "-", null, evento, participante, null, participante.getObservacao());
		
		if(participante.getTipoId().getId() == 2) {
			participante.setProfissao(null);
			participante.setEntidade(null);
		}else {
			participante.setMatricula(null);
			participante.setCargo(null);
			participante.setOrgaoId(null);
		}

		participanteDao.persist(participante);
		
		Usuario usuarioSIGED = usuarioExternoDao.findByCpf(participante.getCpf());
		
		if (usuarioSIGED == null){
			
			Usuario usuario = new Usuario(null,participante.getNome(),participante.getCpf().replace(".", "").replace("-", ""),AuthenticationService.toMd5("12345"),0,false,false,participante.getCpf(),new Date(),new Date(), participante.getEmail(),"");
			
			usuarioExternoDao.persist(usuario);	
		}else{
			if( (participante.getEmail() != usuarioSIGED.getEmail()) || (participante.getNome() != usuarioSIGED.getNome()) ){
				
				usuarioSIGED.setEmail(participante.getEmail());
				usuarioSIGED.setNome(participante.getNome());
				
				usuarioExternoDao.merge(usuarioSIGED);
			}
		}
		
		inscricaoDao.persist(inscricao);
		
		emailUtil.emailPreInscricao(id, participante);				

		modelMap.addAttribute("mensagemSucesso", "Sua pré-inscrição foi realizada com sucesso! Você receberá um e-mail com mais informações.");
		
		return "redirect:/evento/previstos";
	}
	
	/**
	 * Monta a página de justificativa para pré-inscrição de usuários logados
	 * @param id
	 * @param modelMap
	 * @return
	 * 
	 * @see {@link #verificarPossibilidadeDePreInscricaoAluno(Evento)}
	 */
	@RequestMapping(value = "/evento/justificativa/{id}", method = RequestMethod.GET)
	public String justificativa(@PathVariable("id") Long id, ModelMap modelMap) {
		
		Evento evento = eventoDao.find(id);
		verificarPossibilidadeDePreInscricaoAluno(evento);	
		
		Inscricao inscricao = new Inscricao(null, "-", null, new Date(), "N", null, evento, null, null, null);
				
		modelMap.addAttribute("inscricao", inscricao);
		modelMap.addAttribute("eventoId", id);		
		
		return "evento/justificativa";
	}

	/**
	 * Verifica a possibilidade e trata as informações da pré-inscrição feita pelos usuários logados. 
	 * Participantes Servidores precisam ter responsável do setor cadastrado para poder solicitar a indicação do chefe.
	 * @param id
	 * @param inscricao
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 * 
	 * @see {@link #verificarPossibilidadeDePreInscricaoAluno(Evento)}
	 */
	@RequestMapping(value = "/evento/inscricaologado/{id}", method = RequestMethod.POST)
	public String inscricaologado(@PathVariable("id") Long id, @ModelAttribute("inscricao") Inscricao inscricao, HttpServletRequest request , ModelMap modelMap) throws Exception {
		try {
			inscricaoService.inscricaoLogado(id, inscricao.getJustificativaParticipante());
			modelMap.addAttribute("mensagem2", "Sua pré-inscrição foi realizada com sucesso! Você receberá um e-mail com mais informações.");
			return "redirect:/inscricao/minhasinscricoes";
		} catch (NaoPodeRealizarInscricaoException e) {
			modelMap.addAttribute("mensagemErro", e.getMessage());
			return "redirect:/evento/previstos";
		}
	}
	
	/**
	 * Verifica a possibilidade e trata as informações da pré-inscrição feita pelos usuários logados. Chamada Assincrona.
	 * Participantes Servidores precisam ter responsável do setor cadastrado para poder solicitar a indicação do chefe.
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/evento/{id}/inscricaoLogado", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> inscricaoLogadoAsync(@PathVariable("id") Long id) {
		try {
			inscricaoService.inscricaoLogado(id, null);
			return new ResponseEntity<>("Sua pré-inscrição foi realizada com sucesso! Você receberá um e-mail com mais informações.", HttpStatus.CREATED);
		} catch(NaoPodeRealizarInscricaoException ex) {
			if(ex.isAcessoNegado()) {
				return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
			}
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Verificação para evitar inscrições indevidas em eventos com restrições
	 * @param evento
	 * @throws HttpStatusException Redireciona de acordo com o código de status HTTP. Tratado pela <tt>ExceptionHandler</tt>
	 * 
	 * @see ExceptionHandler#handle(ProceedingJoinPoint)
	 */
	private void verificarPossibilidadeDePreInscricaoAluno(Evento evento) throws HttpStatusException {
		if(evento == null)
			throw new HttpStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado");
		
		try {
			inscricaoService.verificarPossibilidadeDePreInscricaoAluno(evento);
		} catch(NaoPodeRealizarInscricaoException ex) {
			if(ex.isAcessoNegado()) {
				throw new HttpStatusException(HttpStatus.FORBIDDEN);
			}
			throw new HttpStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/evento/{id}/possibilidadeInscricaoAluno", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> possibilidadeInscricaoAluno(@PathVariable("id") Long id) {
		Evento evento = eventoDao.find(id);
		if(evento == null)
			throw new HttpStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado");
		
		try {
			inscricaoService.verificarPossibilidadeDePreInscricaoAluno(evento);
			return new ResponseEntity<>("", HttpStatus.OK);
		} catch(NaoPodeRealizarInscricaoException ex) {
			if(ex.isAcessoNegado()) {
				return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
			}
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	 * END: Inscrições feitas por alunos
	 */
	
	@RequestMapping(value = "/evento/comunicado", method = RequestMethod.GET)
	public String comunicado(ModelMap modelMap) {
		modelMap.addAttribute("email", new Email());
		modelMap.addAttribute("eventosAll", eventoDao.findAll());
		return "evento/comunicado";
	}
	
	@RequestMapping(value = "/evento/comunicado", method = RequestMethod.POST)
	public String comunicado(@ModelAttribute("email") @Valid Email email, BindingResult result, ModelMap modelMap) throws Exception {
		
		if (email.getTitulo() == null || email.getTitulo().equals("")) {
			result.addError(new FieldError("email", "titulo", "Campo Obrigatório"));
		}
		if (email.getMensagem() == null || email.getMensagem().equals("")) {
			result.addError(new FieldError("email", "mensagem", "Campo Obrigatório"));
		}		
		if (result.hasErrors()) {
			modelMap.addAttribute("eventosAll", eventoDao.findAll());
			return "evento/comunicado";
        }  
		
		emailUtil.emailComunicado(email);
		modelMap.addAttribute("eventosAll", eventoDao.findAll());
//		modelMap.addAttribute("email", email);
		modelMap.addAttribute("email", new Email());		
		modelMap.addAttribute("aviso", "Comunicado enviado com sucesso!");
		
		return "evento/comunicado";
	}
	
	public String formatarCpf(String cpf) {
		
		if (cpf.indexOf(".") != -1) {
			cpf = cpf.replace(".", "").replace("-", "");
		} else {
			String parte1 = cpf.substring(0, 3);
			String parte2 = cpf.substring(3, 6);
			String parte3 = cpf.substring(6, 9);
			String parte4 = cpf.substring(9, 11);
			cpf = parte1 + "." + parte2 + "." + parte3 + "-" + parte4;
		}
		
		return cpf;		
	}
	
	@ModelAttribute("provedorEventoList")
	public Collection<ProvedorEvento> populateProvedorEventos() {
		return provedorEventoDao.findAll();
	}
	
	@ModelAttribute("programaList")
	public Collection<Programa> populateProgramas() {
		return programaDao.findAll();
	}
	
	@ModelAttribute("tipoLocalizacaoEventoList")
	public Collection<TipoLocalizacaoEvento> populateTipoLocalizacaoEventos() {
		return tipoLocalizacaoEventoDao.findAll();
	}
	
	@ModelAttribute("tipoEventoList")
	public Collection<TipoEvento> populateTipoEventos() {
		return tipoEventoDao.findAll();
	}
	
	@ModelAttribute("responsavelEventoList")
	public Collection<Participante> populateResponsavelEvento() {
		return participanteDao.findByResponsaveisPeloEvento();
	}
	
	@ModelAttribute("eventoRealizadoList")
	public Collection<Evento> populateEventosRealizados() {
		return eventoDao.findByFiltro(new EventoFiltro(StatusEvento.REALIZADO), false, false);
	}
	
	@ModelAttribute("modalidadeList")
	public Collection<Modalidade> populateModalidades() {
		return modalidadeDao.findAll();
	}

	@ModelAttribute("tipoAreaTribunalList")
	public Collection<TipoAreaTribunal> populateTipoAreaTribunals() {
		return tipoAreaTribunalDao.findAll();
	}

	@ModelAttribute("tipoPublicoAlvoList")
	public Collection<TipoPublicoAlvo> populateTipoPublicoAlvos() {
		return tipoPublicoAlvoDao.findAllNotServidor();
	}
	
	@ModelAttribute("tipoPublicoAlvoList2")
	public Collection<TipoPublicoAlvo> populateTipoPublicoAlvos2() {
		return tipoPublicoAlvoDao.findAll();
	}	

	@ModelAttribute("gastoList")
	public Collection<Gasto> populateGasto() {
		return gastoDao.findAll();
	}		

	@ModelAttribute("ufMunicipioList")
	public Collection<UfMunicipio> populateUfMunicipio() {
		return ufMunicipioDAO.findAll();
	}
	
	@ModelAttribute("SNList")
	public Map<String,String> populateMelhorias() {

		Map<String,String> populate = new LinkedHashMap<String,String>();
		populate.put("S", "S");
		populate.put("N", "N");
		return populate;
	}

	@ModelAttribute("NSList")
	public Map<String,String> populatePermiteCertificado() {

		Map<String,String> populate = new LinkedHashMap<String,String>();
		populate.put("N", "N");
		populate.put("S", "S");
		return populate;
	}
	
	@ModelAttribute("SNEnum")
	public Map<String,String> populateSN() {
		Map<String,String> populate = new LinkedHashMap<String,String>();
		populate.put("-", "-");
		populate.put("S", "S");
		populate.put("N", "N");
		return populate;
	}

	@ModelAttribute("respostasQuestoes")
	public Map<String,String> populateRespostas() {

		Map<String,String> populate = new LinkedHashMap<String,String>();
		populate.put("Insuficiente", "Insuficiente");
		populate.put("Regular", "Regular");
		populate.put("Bom", "Bom");
		populate.put("Excelente", "Excelente");
		return populate;
	}	
	
	@ModelAttribute("eixoTematicoList")
	public Collection<EixoTematico> populateEixoTematico() {
		return eixotematicoDao.findAll();
	}
	
	@ModelAttribute("formacaoAcademicaList")
	public Collection<FormacaoAcademica> populateFormacaoAcademicas() {
		return formacaoAcademicaDao.findAll();
	}
	
	@ModelAttribute("nivelEscolaridadeList")
	public Collection<NivelEscolaridade> populateNivelEscolaridades() {
		return nivelEscolaridadeDao.findAll();
	}
}