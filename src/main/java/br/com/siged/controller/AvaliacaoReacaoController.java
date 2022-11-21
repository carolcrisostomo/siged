package br.com.siged.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.displaytag.pagination.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.siged.dao.AvaliacaoReacaoDAO;
import br.com.siged.dao.AvaliacaoReacaoNotaDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.InscricaoDAO;
import br.com.siged.dao.InstrutorDAO;
import br.com.siged.dao.ModuloDAO;
import br.com.siged.dao.NotaQuestaoDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.QuestaoDAO;
import br.com.siged.dao.TipoQuestaoDAO;
import br.com.siged.dao.pagination.DisplayTagPageable;
import br.com.siged.dto.AvaliacaoReacaoDTO;
import br.com.siged.editor.AvaliacaoReacaoNotaEditor;
import br.com.siged.editor.EventoEditor;
import br.com.siged.editor.InscricaoEditor;
import br.com.siged.editor.InstrutorEditor;
import br.com.siged.editor.ModuloEditor;
import br.com.siged.editor.NotaQuestaoEditor;
import br.com.siged.editor.ParticipanteEditor;
import br.com.siged.editor.QuestaoEditor;
import br.com.siged.editor.TipoQuestaoEditor;
import br.com.siged.entidades.AvaliacaoReacao;
import br.com.siged.entidades.AvaliacaoReacaoNota;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Generica;
import br.com.siged.entidades.Instrutor;
import br.com.siged.entidades.Modalidade;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.NotaQuestao;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.Questao;
import br.com.siged.entidades.TipoQuestao;
import br.com.siged.entidades.Usuario;
import br.com.siged.filtro.AvaliacaoFiltro;
import br.com.siged.filtro.RelatorioFiltro;
import br.com.siged.service.AuthenticationService;
import br.com.siged.service.AvaliacaoReacaoService;
import br.com.siged.service.RelatorioService;
import br.com.siged.service.exception.AcessoNegadoException;
import br.com.siged.service.exception.BusinessException;
import br.com.siged.service.exception.HttpStatusException;
import br.com.siged.service.exception.RequisicaoMalFormuladaException;
import br.com.siged.util.comparator.AvaliacaoReacaoNotaComparator;


/**
 * @author estag_12977 (Rafael de Castro)
 */
@Controller
@RequestMapping("/avaliacaoreacao/**")
public class AvaliacaoReacaoController {
	
	@Autowired
	private ParticipanteDAO participanteDao;
	
	@Autowired
	private EventoDAO eventoDao;

	@Autowired
	private InstrutorDAO instrutorDao;
	
	@Autowired
	private AvaliacaoReacaoDAO avaliacaoreacaoDao;
	
	@Autowired
	private AvaliacaoReacaoNotaDAO avaliacaoreacaonotaDao;
	
	@Autowired
	private NotaQuestaoDAO notaquestaoDao;
	
	@Autowired
	private QuestaoDAO questaoDao;
	
	@Autowired
	private TipoQuestaoDAO tipoquestaoDao;
	
	@Autowired
	private ModuloDAO moduloDao;
	
	@Autowired
	private InscricaoDAO inscricaoDao;
	
	@Autowired
	private AvaliacaoReacaoService avaliacaoReacaoService;
	
	@Autowired
	private RelatorioService relatorioService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder){
		dataBinder.registerCustomEditor(br.com.siged.entidades.Participante.class, new ParticipanteEditor(participanteDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Evento.class, new EventoEditor(eventoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Instrutor.class, new InstrutorEditor(instrutorDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.AvaliacaoReacaoNota.class, new AvaliacaoReacaoNotaEditor(avaliacaoreacaonotaDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.NotaQuestao.class, new NotaQuestaoEditor(notaquestaoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Questao.class, new QuestaoEditor(questaoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.TipoQuestao.class, new TipoQuestaoEditor(tipoquestaoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Modulo.class, new ModuloEditor(moduloDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Inscricao.class, new InscricaoEditor(inscricaoDao));
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
		
	}
	
	/**
	 * Visualizar uma Avaliação pelo seu ID
	 * @role Administrador, Administrador_Consulta, Aluno
	 * 
	 * @param id
	 * @param modelMap
	 * @return String view que será renderizada: "avaliacaoreacao/show"
	 */
	@RequestMapping(value = "/avaliacaoreacao/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		AvaliacaoReacao avaliacaoReacao = avaliacaoreacaoDao.findById(id);
		Usuario usuarioLogado = AuthenticationService.getUsuarioLogado();
		
		if(avaliacaoReacao == null || usuarioLogado == null)
			throw new HttpStatusException(HttpStatus.NOT_FOUND, "Avaliação não encontrada");
		
		if(usuarioLogado.isAluno()) {
			Participante participanteLogado = participanteDao.findByCpf(usuarioLogado.getCpf());
			if (!avaliacaoReacao.getParticipante().getId().equals(participanteLogado.getId()))
				throw new HttpStatusException(HttpStatus.FORBIDDEN);
		}
		
		/*
		 * Campo modalidade no evento foi depreciado.
		Modalidade modalidadeDoEvento = avaliacaoReacao.getModulo().getEventoId().getModalidadeId();
		 */
		Modalidade modalidadeDoEvento = avaliacaoReacao.getModulo().getModalidade();
		
		TipoQuestao tipoInstrutor = tipoquestaoDao.findTipoInstrutor();
		TipoQuestao tipoConteudo = tipoquestaoDao.findTipoConteudo();
		TipoQuestao tipoAutoAvaliacao = tipoquestaoDao.findTipoAutoAvaliacao();
		TipoQuestao tipoLogistica = tipoquestaoDao.findTipoLogistica();
		
		List<AvaliacaoReacaoNota> avaliacoesConteudo = avaliacaoReacao.agruparAvaliacoesPeloTipoQuestao(tipoConteudo);
		List<AvaliacaoReacaoNota> avaliacoesAutoAvaliacao = avaliacaoReacao.agruparAvaliacoesPeloTipoQuestao(tipoAutoAvaliacao);
		List<AvaliacaoReacaoNota> avaliacoesLogistica = avaliacaoReacao.agruparAvaliacoesPeloTipoQuestao(tipoLogistica);
		
		Map<Integer, List<AvaliacaoReacaoNota>> avaliacoesInstrutores = avaliacaoReacao.agruparAvaliacoesInstrutores();
		List<AvaliacaoReacaoNota> avaliacoesInstrutor1 = avaliacoesInstrutores.get(1);
		List<AvaliacaoReacaoNota> avaliacoesInstrutor2 = avaliacoesInstrutores.get(2);
		List<AvaliacaoReacaoNota> avaliacoesInstrutor3 = avaliacoesInstrutores.get(3);
		
		
		if(!avaliacoesInstrutor1.isEmpty()) {
			modelMap.addAttribute("instrutor", avaliacoesInstrutor1.get(0).getInstrutor());
		}
		if(!avaliacoesInstrutor2.isEmpty()) {
			modelMap.addAttribute("instrutor2", avaliacoesInstrutor2.get(0).getInstrutor());
		}
		if(!avaliacoesInstrutor3.isEmpty()) {
			modelMap.addAttribute("instrutor3", avaliacoesInstrutor3.get(0).getInstrutor());
		}
		
		modelMap.addAttribute("avaliacaoReacao", avaliacaoReacao);
		
		adicionarQuestoesNaoRespondidas(avaliacoesInstrutor1, modalidadeDoEvento, tipoInstrutor);
		modelMap.addAttribute("avaliacoesInstrutor", avaliacoesInstrutor1);
		
		adicionarQuestoesNaoRespondidas(avaliacoesInstrutor2, modalidadeDoEvento, tipoInstrutor);
		modelMap.addAttribute("avaliacoesInstrutor2", avaliacoesInstrutor2);
		
		adicionarQuestoesNaoRespondidas(avaliacoesInstrutor3, modalidadeDoEvento, tipoInstrutor);
		modelMap.addAttribute("avaliacoesInstrutor3", avaliacoesInstrutor3);
		
		adicionarQuestoesNaoRespondidas(avaliacoesConteudo, modalidadeDoEvento, tipoConteudo);
		modelMap.addAttribute("avaliacoesConteudo", avaliacoesConteudo);
		
		adicionarQuestoesNaoRespondidas(avaliacoesAutoAvaliacao, modalidadeDoEvento, tipoAutoAvaliacao);
		modelMap.addAttribute("avaliacoesAutoAvaliacao", avaliacoesAutoAvaliacao);
		
		adicionarQuestoesNaoRespondidas(avaliacoesLogistica, modalidadeDoEvento, tipoLogistica);
		modelMap.addAttribute("avaliacoesLogistica", avaliacoesLogistica);

		return "avaliacaoreacao/show";
	}
	
	/**
	 * Lista as Avaliações respondidas
	 * @role Administrador, Administrador_Consulta
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/avaliacaoreacao", method = RequestMethod.GET)
	public String list(ModelMap modelMap, ServletRequest request, ServletResponse response) {
		return search(new AvaliacaoFiltro(), modelMap, request, response);
	}
	
	/**
	 * Busca Avaliações respondidas
	 * @role Administrador, Administrador_Consulta
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/avaliacaoreacao/search", method = RequestMethod.GET)
	public String search(AvaliacaoFiltro avaliacaofiltro, ModelMap modelMap, ServletRequest request, ServletResponse response) {
		Participante participanteNull = new Participante();
		participanteNull.setNome("NÃO IDENTIFICADO");
		
		DisplayTagPageable pageable = new DisplayTagPageable(request);
		PaginatedList displayTagList = avaliacaoreacaoDao.filtrar(avaliacaofiltro, pageable);
		
		modelMap.addAttribute("avaliacaofiltro", avaliacaofiltro);
		modelMap.addAttribute("avaliacoesReacao", displayTagList);
		
		return "avaliacaoreacao/list";
	}
	
	/**
	 * Deleta uma Avaliação pelo ID
	 * @role Administrador
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/avaliacaoreacao/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		avaliacaoreacaoDao.remove(avaliacaoreacaoDao.find(id));
		return "redirect:/avaliacaoreacao";
	}
	
	/**
	 * GET para listar as Avaliações pendentes e realizadas pelo Aluno</p>
	 * Trecho de código estava lançando <tt>"javax.persistence.NonUniqueResultException: result returns more than one elements"</tt>
	 * quando o Participante tem Usuário na base do SIGED e no SCA com login sendo o CPF
	 * </p>
	 * 
	<pre>
	String usuarioCpf = usuarioDao.findByLogin(request.getRemoteUser()).getCpf();
	Collection<Generica> generic = avaliacaoreacaoDao.findNaoRealizadasByParticipanteCpf(usuarioCpf);
	modelMap.addAttribute("avaliacoesnaorealizadas", generic);
	modelMap.addAttribute("avaliacoes",	avaliacaoreacaoDao.findByParticipanteCpf(usuarioCpf));
	</pre>
	 * 
	 * @param modelMap
	 * @param request
	 * @return String View que lista as avaliações do aluno
	 * @since corrigido em 06/09/2018 [issue #2822]
	 * 
	*/
	@RequestMapping(value = "/avaliacaoreacao/minhasavaliacoes", method = RequestMethod.GET)
	public String minhasavaliacoes(ModelMap modelMap, HttpServletRequest request) {
		Usuario usuarioLogado = AuthenticationService.getUsuarioLogado();
		if(usuarioLogado == null)
			throw new HttpStatusException(HttpStatus.FORBIDDEN);
		
		Collection<Generica> generic = avaliacaoreacaoDao.findNaoRealizadasByParticipanteCpf(usuarioLogado.getCpf());
		modelMap.addAttribute("avaliacoesnaorealizadas", generic);
		modelMap.addAttribute("avaliacoes",	avaliacaoreacaoDao.findByParticipanteCpf(usuarioLogado.getCpf()));
		
		
		
		return "avaliacaoreacao/minhasavaliacoes";
	}
	
	/**
	 * GET para montar Avaliação para ser respondida pelo Aluno
	 * @role Aluno
	 *  
	 * @param modelMap
	 * @param evento
	 * @param modulo
	 * @param participante
	 * @param request
	 * @return String View que monta as questões da avaliação
	 */
	@RequestMapping(value = "/avaliacaoreacao/formaluno/{evento}/{modulo}/{participante}", method = RequestMethod.GET)
	public String formaluno(ModelMap modelMap
			, @PathVariable("evento") Long evento
			, @PathVariable("modulo") Long modulo
			, @PathVariable("participante") Long participante
			, HttpServletRequest request) {
		
		/**
		 * 
		 * Condicional para gerar o certificado depois de responder Avaliacao Reacao
		 * 
		 * Quando o aluno solicita a emissão do certificado e existem avaliações a serem respondidas
		 * Se o aluno deseja responder a avaliacao, então é gerado o atributo 'gcda' (gerar certificado depois da avaliacao) 
		 * para agendar a emissão do certificado logo após responder a avaliacao
		 */
		if(Boolean.valueOf(request.getParameter("gcda"))){
			request.getSession().setAttribute("gcda", true);
			return "redirect:/avaliacaoreacao/formaluno/"+ evento + "/" + modulo + "/" + participante;
		}
		
		Evento eventocons = eventoDao.find(evento);
		Modulo modulocons = moduloDao.findById(modulo);
		Participante participantecons = participanteDao.find(participante);
		
		try{
			avaliacaoReacaoService.verificarParametros(modulocons, eventocons, participantecons);
		} catch (AcessoNegadoException e) {
			throw new HttpStatusException(HttpStatus.FORBIDDEN);
		} catch (RequisicaoMalFormuladaException e) {
			throw new HttpStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (BusinessException e) {
			return "redirect:/avaliacaoreacao/minhasavaliacoes?mensagemErro=" + e.getMessage();
		}
		
		modelMap.addAttribute("evento", eventocons);
		modelMap.addAttribute("modulo", modulocons);
		adicionarInstrutoresNoModelMap(modelMap, modulocons);
		modelMap.addAttribute("instrutores", modulocons.getInstrutorList());
		modelMap.addAttribute("participante", participanteDao.find(participante));
		modelMap.addAttribute("avaliacaoReacao", new AvaliacaoReacao());

		return "avaliacaoreacao/createaluno";
	}
	
	/**
	 * POST para validar e inserir a avaliação respondida pelo aluno
	 * @role Aluno
	 * 
	 * @param avaliacaoReacao
	 * @param result
	 * @param modelMap
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = "/avaliacaoreacao/aluno", method = RequestMethod.POST)
	public String createaluno(@ModelAttribute("avaliacaoReacao") @Valid AvaliacaoReacao avaliacaoReacao
			, BindingResult result
			, ModelMap modelMap
			, HttpServletRequest request) {
		
		Boolean gerarCertificado = false;
		if (request.getSession().getAttribute("gcda") != null) {
			gerarCertificado = true;
		}

		boolean avaliacaoOk = true;

		avaliacaoReacao.setAvaliacoesReacaoNota(questoesAvaliadasList(avaliacaoReacao));
		Evento evento = eventoDao.find(avaliacaoReacao.getModulo().getEventoId().getId());
		Modulo modulo = moduloDao.find(avaliacaoReacao.getModulo().getId());
		Participante participante = participanteDao.find(avaliacaoReacao.getParticipante().getId());

		String mensagem = "";

		if (avaliacaoReacao.isNenhumaQuestaoRespondida()) {
			mensagem = "Todos os campos estão vazios. Preencha ao menos um dos campos.";
			avaliacaoOk = false;
		} else if (!avaliacaoReacaoService.isNumeroAvaliacoesOk(avaliacaoReacao)) {
			mensagem = "Não é possível incluir mais avaliações do que participantes inscritos no módulo.";
			avaliacaoOk = false;
		}
		if (!avaliacaoOk) {
			modelMap.addAttribute("mensagem", mensagem);
		}

		if (result.hasErrors() || !avaliacaoOk) {
			modelMap.addAttribute("evento", evento);
			modelMap.addAttribute("modulo", modulo);
			modelMap.addAttribute("participante", participante);

			adicionarInstrutoresNoModelMap(modelMap, modulo);

			return "avaliacaoreacao/createaluno";

		}
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		Date hoje = null;
		try {
			hoje = df.parse(df.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		avaliacaoReacao.setDataCadastro(hoje);

		try {
			avaliacaoReacaoService.salvar(avaliacaoReacao);
		} catch (BusinessException e) {
			modelMap.addAttribute("mensagem", e.getMessage());
			return "avaliacaoreacao/createaluno";
		}
		
		if (gerarCertificado) {
			AvaliacaoReacaoDTO avaliacaoReacaoDTO = avaliacaoReacaoService.avaliacoesPendentes(participante.getId(), evento.getId());
			if(avaliacaoReacaoDTO.getPendentes() > 0) {
				return "redirect:/avaliacaoreacao/formaluno/" 
						+ avaliacaoReacaoDTO.getEventoId() + "/" 
						+ avaliacaoReacaoDTO.getModuloId() + "/"
						+ avaliacaoReacaoDTO.getParticipanteId() + "?mensagemAlerta=Avaliações pendentes: " + avaliacaoReacaoDTO.getPendentes();
			} else {
				request.getSession().setAttribute("gcda", null);
				request.getSession().setAttribute("certificado_path", "/" + evento.getId() + "/" + participante.getId());
				return "redirect:/evento/meuseventos?mensagemSucesso=Todas as avaliações pendentes do evento foram respondidas";
			}
		}
		return "redirect:/avaliacaoreacao/minhasavaliacoes";

	}
	
	/**
	 * GET para montar o filtro com os campos necessários para achar a Avaliação
	 * @role Administrador
	 * 
	 * @param modelMap
	 * @return String View que monta os filtros para criar uma nova avaliação
	 */
	@RequestMapping(value = "/avaliacaoreacao/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("avaliacaoFiltro", new AvaliacaoFiltro());

		return "avaliacaoreacao/create";
	}
	
	/**
	 * GET para monstar a Avaliação que será preenchida pelo Administrador de acordo com as respostas do Aluno
	 * @role Administrador
	 * 
	 * @param avaliacao
	 * @param result
	 * @param modelMap
	 * @return String View para montar as questões da avaliação
	 */
	@RequestMapping(value = "/avaliacaoreacao/formadmin", method = RequestMethod.GET)
	public String formadmin(@ModelAttribute("avaliacaoFiltro") @Valid AvaliacaoFiltro avaliacao
			, BindingResult result
			, ModelMap modelMap
			, HttpServletRequest request) {
		
		if(avaliacao.getEvento() == null || avaliacao.getEvento() == 0){
			FieldError eventoError = new FieldError("avaliacao", "evento", " Campo Obrigatório");
			result.addError(eventoError);
		}
		if(avaliacao.getModulo() == null || avaliacao.getModulo() == 0){
			FieldError moduloError = new FieldError("avaliacao", "modulo", " Campo Obrigatório");
			result.addError(moduloError);
		}
		if (result.hasErrors()) {
			return "avaliacaoreacao/create";
		}
		
		Evento eventocons = eventoDao.find(avaliacao.getEvento());
		Modulo modulocons = moduloDao.find(avaliacao.getModulo());
		Participante participante = null;
		
		if (avaliacao.getParticipante() != null && avaliacao.getParticipante() != 0){
			participante = participanteDao.find(avaliacao.getParticipante());
		}
		
		modelMap.addAttribute("evento", eventocons);
		modelMap.addAttribute("modulo", modulocons);
		modelMap.addAttribute("participante", participante);
		adicionarInstrutoresNoModelMap(modelMap, modulocons);
		
		try {
			avaliacaoReacaoService.verificarParametros(modulocons, eventocons, participante);
		} catch (BusinessException e) {
			modelMap.addAttribute("mensagem", e.getMessage());
			return "avaliacaoreacao/create";
		}
		
		modelMap.addAttribute("avaliacaoReacao", new AvaliacaoReacao());

		return "avaliacaoreacao/createadmin";
	}
	
	/**
	 * POST para validar e inserir a Avaliação preenchida pelo Administrador
	 * @role Administrador
	 * 
	 * @param avaliacaoReacao
	 * @param result
	 * @param modelMap
	 * @return String View que monta os filtros para criar uma nova avaliação
	 */
	@RequestMapping(value = "/avaliacaoreacao/admin", method = RequestMethod.POST)
	public String createadmin(@ModelAttribute("avaliacaoReacao") @Valid AvaliacaoReacao avaliacaoReacao, BindingResult result, ModelMap modelMap) {

		boolean avaliacaoOk = true;
		
		avaliacaoReacao.setAvaliacoesReacaoNota(questoesAvaliadasList(avaliacaoReacao));
		
		String mensagem = "";
		if(avaliacaoReacao.isNenhumaQuestaoRespondida()) {
			mensagem = "Todos os campos estão vazios. Preencha ao menos um dos campos.";
			avaliacaoOk = false;
		} else if (avaliacaoReacao.getParticipante() != null && possuiAvaliacaoByParticipanteEventoModulo(avaliacaoReacao)) {
			mensagem = "Não é possível incluir mais de uma avaliação por participante em um módulo.";
			avaliacaoOk = false;
		} else if (!avaliacaoReacaoService.isNumeroAvaliacoesOk(avaliacaoReacao)) {
			mensagem = "Não é possível incluir mais avaliações do que participantes inscritos no módulo.";
			avaliacaoOk = false;
		}
		
		if (!avaliacaoOk) {
			modelMap.addAttribute("mensagem", mensagem);
			modelMap.addAttribute("evento", avaliacaoReacao.getModulo().getEventoId());
			modelMap.addAttribute("modulo", avaliacaoReacao.getModulo());

			Modulo modulocons = avaliacaoReacao.getModulo();
			adicionarInstrutoresNoModelMap(modelMap, modulocons);
			
			if (avaliacaoReacao.getParticipante() != null){
				modelMap.addAttribute("participante", avaliacaoReacao.getParticipante());
			}
			
			modelMap.addAttribute("avaliacaoReacao", avaliacaoReacao);

			return "avaliacaoreacao/createadmin";
		}

		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		Date hoje = null;
		try {
			hoje = df.parse(df.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		avaliacaoReacao.setDataCadastro(hoje);		
		
		try {
			avaliacaoReacaoService.salvar(avaliacaoReacao);
		} catch (BusinessException e) {
			modelMap.addAttribute("mensagem", e.getMessage());
			return "avaliacaoreacao/createadmin";
		}
		
		return "redirect:/avaliacaoreacao/form";
	}
	
	/**
	 * GET para montar o filtro com os campos necessários para achar a Avaliação
	 * @role Administrador
	 * 
	 * @param modelMap
	 * @return String View que monta os filtros para criar uma nova avaliação
	 */
	@RequestMapping(value = "/avaliacaoreacao/questionario", method = RequestMethod.GET)
	public String getQuestionario(ModelMap modelMap) {
		modelMap.addAttribute("avaliacaoFiltro", new RelatorioFiltro());
		return "avaliacaoreacao/questionario";
	}
	
	/**
	 * GET para monstar a Avaliação que será preenchida pelo Administrador de acordo com as respostas do Aluno
	 * @role Administrador
	 * 
	 * @param avaliacao
	 * @param result
	 * @param modelMap
	 * @return String View para montar as questões da avaliação
	 */
	@RequestMapping(value = "/avaliacaoreacao/questionario", method = RequestMethod.POST)
	public String imprimirQuestionario(@ModelAttribute("avaliacaoFiltro") @Valid RelatorioFiltro avaliacaoFiltro
			, BindingResult result
			, HttpServletResponse response) {
		
		if(avaliacaoFiltro.getEventoId() == null || avaliacaoFiltro.getEventoId() == 0){
			FieldError eventoError = new FieldError("avaliacao", "eventoId", " Campo Obrigatório");
			result.addError(eventoError);
		}
		
		if (result.hasErrors()) {
			return "avaliacaoreacao/questionario";
		}
		
		relatorioService.gerarRelatorioAvaliacoesReacaoEmBranco(avaliacaoFiltro, response);

		return null;
	}
	
	@RequestMapping(value = "/avaliacaoreacao/procuraParticipante", method = RequestMethod.GET)
	@ResponseBody
	public List<Participante> procuraParticipante(@RequestParam(value = "eventoId") Long eventoId) {
		return participanteDao.findByEventoId(eventoId);
	}

	@RequestMapping(value = "/avaliacaoreacao/procuraModulo", method = RequestMethod.GET)
	@ResponseBody
	public List<Modulo> procuraModulo(@RequestParam(value = "eventoId") Long eventoId) {
		return moduloDao.findByEventoId(eventoId);
	}
	
	@RequestMapping(value = "/avaliacaoreacao/participanteSemAvaliacaoNoModulo", method = RequestMethod.GET)
	@ResponseBody
	public List<Participante> participanteSemAvaliacaoNoModulo(@RequestParam(value = "moduloId") Long moduloId) {
		Modulo modulo = moduloDao.findById(moduloId);
		return participanteDao.semAvaliacaoNoModulo(modulo);
	}
	
	/**
	 * Procura os modulos habilitados para Avaliacao Reação
	 * 
	 * @param eventoId
	 * @return List<Modulo>
	 */
	@RequestMapping(value = "/avaliacaoreacao/procuraModuloAvaliacao", method = RequestMethod.GET)
	@ResponseBody
	public List<Modulo> procuraModuloParaAvaliacao(@RequestParam(value = "eventoId") Long eventoId) {
		return moduloDao.findModuloComAvaliacaoPendenteADMIN(eventoId);
	}
	
	/**
	 * Procura os modulos com Avaliações de Reação respondidas
	 * 
	 * @param eventoId
	 * @return List<Modulo>
	 */
	@RequestMapping(value = "/avaliacaoreacao/procuraModuloComAvaliacoes", method = RequestMethod.GET)
	@ResponseBody
	public List<Modulo> procuraModuloComAvaliacoes(@RequestParam(value = "eventoId") Long eventoId) {
		return moduloDao.findModuloComAvaliacoesRespondidas(eventoId);
	}

	@RequestMapping(value = "/avaliacaoreacao/procuraInstrutor", method = RequestMethod.GET)
	@ResponseBody
	public List<Instrutor> procuraInstrutor(@RequestParam(value = "moduloId") Long moduloId) {
		return moduloDao.find(moduloId).getInstrutorList();
	}
	
	@RequestMapping(value = "/avaliacaoreacao/avaliacoesPendentes/{participante_id}/{evento_id}", method = RequestMethod.GET)
	@ResponseBody
	public AvaliacaoReacaoDTO avaliacoesPendentes(@PathVariable("participante_id") Long participante_id, @PathVariable("evento_id") Long evento_id) {
		return avaliacaoReacaoService.avaliacoesPendentes(participante_id, evento_id);
	}
	
	@ModelAttribute("questoesList")
	public HashMap<Long, Collection<Questao>> populateQuestoesPorTipo() {
		TipoQuestao tipoQuestaoInstrutor = tipoquestaoDao.findTipoInstrutor();
		TipoQuestao tipoQuestaoConteudo = tipoquestaoDao.findTipoConteudo();
		TipoQuestao tipoQuestaoAutoAvaliacao = tipoquestaoDao.findTipoAutoAvaliacao();
		TipoQuestao tipoQuestaoLogistica = tipoquestaoDao.findTipoLogistica();
		
		HashMap<Long, Collection<Questao>> questoes = new HashMap<Long, Collection<Questao>>();
		questoes.put(tipoQuestaoInstrutor.getId(), questaoDao.findByTipoQuestaoId(tipoQuestaoInstrutor.getId()));
		questoes.put(tipoQuestaoConteudo.getId(), questaoDao.findByTipoQuestaoId(tipoQuestaoConteudo.getId()));
		questoes.put(tipoQuestaoAutoAvaliacao.getId(), questaoDao.findByTipoQuestaoId(tipoQuestaoAutoAvaliacao.getId()));
		questoes.put(tipoQuestaoLogistica.getId(), questaoDao.findByTipoQuestaoId(tipoQuestaoLogistica.getId()));
		
		return questoes;
	}
	
	@ModelAttribute("questoesInstrutorList")
	public List<Questao> populateQuestoesInstrutor(){
		return questaoDao.findByTipoQuestaoId(tipoquestaoDao.findTipoInstrutor().getId());
	}
	
	@ModelAttribute("questoesConteudoList")
	public List<Questao> populateQuestoesConteudo(){
		return questaoDao.findByTipoQuestaoId(tipoquestaoDao.findTipoConteudo().getId());
	}
	
	@ModelAttribute("questoesAutoAvaliacaoList")
	public List<Questao> populateQuestoesAutoAvaliacao(){
		return questaoDao.findByTipoQuestaoId(tipoquestaoDao.findTipoAutoAvaliacao().getId());
	}
	
	@ModelAttribute("questoesLogisticaList")
	public List<Questao> populateQuestoesLogistica(){
		return questaoDao.findByTipoQuestaoId(tipoquestaoDao.findTipoLogistica().getId());
	}
	
	@ModelAttribute("tiposQuestoesList")
	public Collection<TipoQuestao> populateTiposQuestoes(){
		return tipoquestaoDao.findAll();
	}
	
	@ModelAttribute("notasQuestoesList")
	public Collection<NotaQuestao> populateNotasQuestoes(){
		return notaquestaoDao.findAll();
	}
	
	@ModelAttribute("eventoList")
	public Collection<Evento> populateEventos() {
		return eventoDao.findAll();
	}	

	@ModelAttribute("eventoListAvaliacoes")
	public Collection<Evento> populateEventosAvaliacoes() {
		return eventoDao.findEventosAvaliacoes();
	}
	
	private boolean possuiAvaliacaoByParticipanteEventoModulo(AvaliacaoReacao avaliacaoReacao) {
		Collection<AvaliacaoReacao> avalicacoesReacaoParticipante = avaliacaoreacaoDao.findAvaliacaoReacaoByCriteria(
				avaliacaoReacao.getModulo().getEventoId().getId(), 
				avaliacaoReacao.getModulo().getId(), 
				avaliacaoReacao.getParticipante().getId(), null, null);
		if (!avalicacoesReacaoParticipante.isEmpty())
			return true;
		else
			return false;
	}
	
	/**
	 * Adiciona as questões que não foram respondidas na AvaliacaoReacaoNota para que possam aparecer na View a descrição das questões
	 * 
	 * @param arns
	 * @param modalidade
	 * @param tipoQuestao
	 */
	private void adicionarQuestoesNaoRespondidas(List<AvaliacaoReacaoNota> arns, Modalidade modalidade, TipoQuestao tipoQuestao) {
		List<AvaliacaoReacaoNota> questoesNaoRespondidas  = new ArrayList<>();
		for(Questao questao : questaoDao.findByTipoQuestaoId(tipoQuestao.getId())) {
			boolean adicionarQuestao = true;
			for(AvaliacaoReacaoNota arn : arns) {
				if(arn.getQuestao().getId().equals(questao.getId())){
					adicionarQuestao = false;
				}
			}
			
			if(questao.isModalidade(modalidade) && adicionarQuestao) {
				AvaliacaoReacaoNota arnNaoRespondida = new AvaliacaoReacaoNota();
				arnNaoRespondida.setId(null);
				arnNaoRespondida.setInstrutor(null);
				arnNaoRespondida.setNotaQuestao(null);
				arnNaoRespondida.setQuestao(questao);
				questoesNaoRespondidas.add(arnNaoRespondida);
			}
			
		}
		
		arns.addAll(questoesNaoRespondidas);
		
		Collections.sort(arns, AvaliacaoReacaoNotaComparator.OrderByOrdemQuestao.asc());
		
	}
	
	/**
	 * Método para vincular as AvaliacaoReacaoNota à AvaliacaoReacao e verificar as questoes respondidas
	 * Sem isso a JPA não consegue persistir a avaliacaoReacao, 
	 * pois cada elemento de avaliacaoReacao.getAvaliacoesReacaoNota() terá o atributo avaliacaoReacao null
	 * violando a Constraint do banco de dados
	 * 
	 * Por algum motivo o spring não faz o bind corretamenete quando este código está no setAvaliacoesReacaoNota em AvaliacaoReacao.class
	 * Sempre está deixando a propriedade 'avaliacoaReacao' null em cada AvaliacaReacaoNota da lista. Por isso esse bind está centralizado neste método
	 * 
	 * @param AvaliacaoReacao avaliacaoReacao
	 * @return List<AvaliacaoReacaoNota>
	 */
	private List<AvaliacaoReacaoNota> questoesAvaliadasList(AvaliacaoReacao avaliacaoReacao) {
		List<AvaliacaoReacaoNota> questoesAvaliadasList = new ArrayList<>();
		for (AvaliacaoReacaoNota avaliacaoReacaoNota : avaliacaoReacao.getAvaliacoesReacaoNota()) {
			if(avaliacaoReacaoNota.getNotaQuestao() != null){
				avaliacaoReacaoNota.setAvaliacaoReacao(avaliacaoReacao);
				questoesAvaliadasList.add(avaliacaoReacaoNota);
			}
		}
		
		return questoesAvaliadasList;
	}
	
	private void adicionarInstrutoresNoModelMap(ModelMap modelMap, Modulo modulocons) {
		int i = 1;
		for (Instrutor inst : modulocons.getInstrutorList()) {
			if (i == 1) {
				modelMap.addAttribute("instrutor", inst);
			}
			if (i == 2) {
				modelMap.addAttribute("instrutor2", inst);
			}
			if (i == 3) {
				modelMap.addAttribute("instrutor3", inst);
			}
			i++;
		}
	}


}
