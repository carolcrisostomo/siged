package br.com.siged.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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

import br.com.siged.dao.AvaliacaoDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.InscricaoDAO;
import br.com.siged.dao.InstrutorDAO;
import br.com.siged.dao.ModuloDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.UsuarioSCADAO;
import br.com.siged.editor.EventoEditor;
import br.com.siged.editor.InstrutorEditor;
import br.com.siged.editor.ModuloEditor;
import br.com.siged.editor.ParticipanteEditor;
import br.com.siged.entidades.Avaliacao;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Generica;
import br.com.siged.entidades.Inscricao;
import br.com.siged.entidades.Instrutor;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Participante;
import br.com.siged.filtro.AvaliacaoFiltro;

/**
 * @deprecated
 */
@Controller
@RequestMapping("/avaliacao/**")
public class AvaliacaoController {
	
	@Autowired
	private AvaliacaoDAO avaliacaoDao;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ModuloDAO moduloDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private InstrutorDAO instrutorDao;
	@Autowired
	private UsuarioSCADAO usuarioDao;
	@Autowired
	private InscricaoDAO inscricaoDAO;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(br.com.siged.entidades.Evento.class, new EventoEditor(eventoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Modulo.class, new ModuloEditor(moduloDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Participante.class, new ParticipanteEditor(participanteDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Instrutor.class, new InstrutorEditor(instrutorDao));
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
	}

	//@RequestMapping(value = "/avaliacao/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		Avaliacao avaliacao = avaliacaoDao.findById(id);
		modelMap.addAttribute("avaliacao", avaliacao);
		Modulo modulocons = moduloDao.findById(avaliacao.getModuloId().getId());
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
			i = i + 1;
		}
		modelMap.addAttribute("evento",	eventoDao.find(avaliacao.getEventoId().getId()));
		modelMap.addAttribute("instrutor", avaliacao.getInstrutorId());

		return "avaliacao/show";
	}

	//@RequestMapping(value = "/avaliacao", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("avaliacaofiltro", new AvaliacaoFiltro());
		modelMap.addAttribute("avaliacoes", avaliacaoDao.findAll());
		Participante participanteNull = new Participante();
		participanteNull.setNome("NÃO IDENTIFICADO");
		List<Avaliacao> avaliacoes = avaliacaoDao.findAll();
		for (int i = 0; i < avaliacoes.size(); i++) {
			if (avaliacoes.get(i).getParticipanteId() == null)
				avaliacoes.get(i).setParticipanteId(participanteNull);
		}

		return "avaliacao/list";
	}

	//@RequestMapping(value = "/avaliacao/search", method = RequestMethod.GET)
	public String search(AvaliacaoFiltro avaliacaofiltro, ModelMap modelMap, ServletRequest Request, ServletResponse Response) throws IOException {
		
		modelMap.addAttribute("avaliacaofiltro", avaliacaofiltro);
		
		Collection<Avaliacao> avaliacoesCollection = avaliacaoDao.findAvaliacaoByCriteria(avaliacaofiltro.getEvento(), avaliacaofiltro.getModulo(), avaliacaofiltro.getParticipante(), avaliacaofiltro.getData_cadastro1(), avaliacaofiltro.getData_cadastro2());
						
		Participante participanteNull = new Participante();
		participanteNull.setNome("NÃO IDENTIFICADO");
		
		List<Avaliacao> avaliacoes = new ArrayList<Avaliacao>(avaliacoesCollection.size());
		
		for (Avaliacao avaliacao : avaliacoesCollection) {
			
			if(avaliacao.getParticipanteId() == null){
				avaliacao.setParticipanteId(participanteNull);
			}
			
			if (avaliacaofiltro.getResultado() == 1){
				
				if (avaliacao.isSatisfatoria()){
					avaliacoes.add(avaliacao);
				}
				
			}else if (avaliacaofiltro.getResultado() == 2){
				
				if (!avaliacao.isSatisfatoria()){
					avaliacoes.add(avaliacao);
				}
				
			}else{
				avaliacoes.add(avaliacao);
			}

		}		
		
		modelMap.addAttribute("avaliacoes",	avaliacoes );
		
		return "avaliacao/list";
	}	

	//@RequestMapping(value = "/avaliacao/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		avaliacaoDao.remove(avaliacaoDao.find(id));
		return "redirect:/avaliacao";
	}

	//@RequestMapping(value = "/avaliacao/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("avaliacao", new Avaliacao());

		return "avaliacao/create";
	}

	//@RequestMapping(value = "/avaliacao/formaluno/{evento}/{modulo}/{participante}", method = RequestMethod.GET)
	public String formaluno(ModelMap modelMap, @PathVariable("evento") Long evento, @PathVariable("modulo") Long modulo, @PathVariable("participante") Long participante) {
		
		modelMap.addAttribute("evento", eventoDao.find(evento));
		modelMap.addAttribute("modulo", moduloDao.find(modulo));

		Modulo modulocons = moduloDao.findById(modulo);
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
			i = i + 1;
		}
		modelMap.addAttribute("participante",
				participanteDao.find(participante));
		modelMap.addAttribute("avaliacao", new Avaliacao());

		return "avaliacao/createaluno";
	}

	//@RequestMapping(value = "/avaliacao/formadmin", method = RequestMethod.GET)
	public String formadmin(@ModelAttribute("avaliacao") @Valid Avaliacao avaliacao, BindingResult result, ModelMap modelMap) {

		if (result.hasErrors()) {
			return "avaliacao/create";
		}
		
		modelMap.addAttribute("evento", avaliacao.getEventoId());
		modelMap.addAttribute("modulo", avaliacao.getModuloId());

		Modulo modulocons = avaliacao.getModuloId();
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
			i = i + 1;
		}
		if (avaliacao.getParticipanteId() != null)
			modelMap.addAttribute("participante", avaliacao.getParticipanteId());
		
		modelMap.addAttribute("avaliacao", new Avaliacao());

		return "avaliacao/createadmin";
	}

	//@RequestMapping(value = "/avaliacao/admin", method = RequestMethod.POST)
	public String createadmin(@ModelAttribute("avaliacao") @Valid Avaliacao avaliacao, BindingResult result, ModelMap modelMap) {

		boolean avaliacaoOk = true;
		String mensagem = "";

		if (avaliacao.getParticipanteId() != null && possuiAvaliacaoByParticipanteEventoModulo(avaliacao)) {
			mensagem = "Não é possível incluir mais de uma avaliação por participante em um módulo.";
			avaliacaoOk = false;
		} else if (!numeroAvaliacoesOk(avaliacao)) {
			mensagem = "Não é possível incluir mais avaliações do que participantes inscritos no módulo.";
			avaliacaoOk = false;
		} else if (campoVazioOk(avaliacao)) {
			mensagem = "Campos obrigatórios não foram preenchidos!";
			avaliacaoOk = false;
		}
		if (!avaliacaoOk) {
			modelMap.addAttribute("mensagem", mensagem);
			modelMap.addAttribute("evento", avaliacao.getEventoId());
			modelMap.addAttribute("modulo", avaliacao.getModuloId());

			Modulo modulocons = avaliacao.getModuloId();
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
				i = i + 1;
			}
			if (avaliacao.getParticipanteId() != null)
				modelMap.addAttribute("participante",
						avaliacao.getParticipanteId());
			modelMap.addAttribute("avaliacao", avaliacao);

			return "avaliacao/createadmin";
		}

		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		Date hoje = null;
		try {
			hoje = df.parse(df.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		avaliacao.setDataCadastro(hoje);
		avaliacaoDao.persist(avaliacao);
		modelMap.addAttribute("avaliacao", new Avaliacao());
		return "avaliacao/create";
	}

	//@RequestMapping(value = "/avaliacao/aluno", method = RequestMethod.POST)
	public String createaluno(@ModelAttribute("avaliacao") @Valid Avaliacao avaliacao, BindingResult result, ModelMap modelMap) {

		boolean avaliacaoOk = true;
		
		String mensagem = "";
		
		if (!numeroAvaliacoesOk(avaliacao)) {
			mensagem = "Não é possível incluir mais avaliações do que participantes inscritos no módulo.";
			avaliacaoOk = false;
		}		
		if (!avaliacaoOk) {
			modelMap.addAttribute("mensagem", mensagem);
			modelMap.addAttribute("evento",	eventoDao.find(avaliacao.getEventoId().getId()));
			modelMap.addAttribute("modulo",	moduloDao.find(avaliacao.getModuloId().getId()));
			modelMap.addAttribute("participante", participanteDao.find(avaliacao.getParticipanteId().getId()));

			return "avaliacao/createaluno";
		}
		if (avaliacao.getInstrutorId() != null
				&& !avaliacao.getInstrutorId().getNome().equals("DIVERSOS")) {
			if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor1())) {
				result.addError(new FieldError("avaliacao",	"questaoInstrutor1", "Campo Obrigatório"));
			}
			if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor2())) {
				result.addError(new FieldError("avaliacao", "questaoInstrutor2", "Campo Obrigatório"));
			}
			if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor3())) {
				result.addError(new FieldError("avaliacao", "questaoInstrutor3", "Campo Obrigatório"));
			}
			if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor4())) {
				result.addError(new FieldError("avaliacao",	"questaoInstrutor4", "Campo Obrigatório"));
			}
			if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor5())) {
				result.addError(new FieldError("avaliacao",	"questaoInstrutor5", "Campo Obrigatório"));
			}
			if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor6())) {
				result.addError(new FieldError("avaliacao",	"questaoInstrutor6", "Campo Obrigatório"));
			}
			if (avaliacao.getInstrutor2Id() != null) {
				modelMap.addAttribute("Instrutor2Id", true);
				if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor21())) {
					result.addError(new FieldError("avaliacao",	"questaoInstrutor21", "Campo Obrigatório"));
				}
				if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor22())) {
					result.addError(new FieldError("avaliacao", "questaoInstrutor22", "Campo Obrigatório"));
				}

				if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor23())) {
					result.addError(new FieldError("avaliacao", "questaoInstrutor23", "Campo Obrigatório"));
				}
				if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor24())) {
					result.addError(new FieldError("avaliacao",	"questaoInstrutor24", "Campo Obrigatório"));
				}

				if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor25())) {
					result.addError(new FieldError("avaliacao",	"questaoInstrutor25", "Campo Obrigatório"));
				}

				if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor26())) {
					result.addError(new FieldError("avaliacao",	"questaoInstrutor26", "Campo Obrigatório"));
				}
			}

			if (avaliacao.getInstrutor3Id() != null) {
				modelMap.addAttribute("Instrutor3Id", true);
				if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor31())) {
					result.addError(new FieldError("avaliacao",	"questaoInstrutor31", "Campo Obrigatório"));
				}
				if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor32())) {
					result.addError(new FieldError("avaliacao",	"questaoInstrutor32", "Campo Obrigatório"));
				}

				if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor33())) {
					result.addError(new FieldError("avaliacao",	"questaoInstrutor33", "Campo Obrigatório"));
				}
				if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor34())) {
					result.addError(new FieldError("avaliacao",	"questaoInstrutor34", "Campo Obrigatório"));
				}
				if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor35())) {
					result.addError(new FieldError("avaliacao",	"questaoInstrutor35", "Campo Obrigatório"));
				}

				if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor36())) {
					result.addError(new FieldError("avaliacao",	"questaoInstrutor36", "Campo Obrigatório"));
				}
			}
		}
		if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoConteudo1())) {
			result.addError(new FieldError("avaliacao", "questaoConteudo1",	"Campo Obrigatório"));
		}

		if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoConteudo2())) {
			result.addError(new FieldError("avaliacao", "questaoConteudo2",	"Campo Obrigatório"));
		}
		if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoConteudo3())) {
			result.addError(new FieldError("avaliacao", "questaoConteudo3",	"Campo Obrigatório"));
		}
		if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoAutoAvaliacao1())) {
			result.addError(new FieldError("avaliacao",	"questaoAutoAvaliacao1", "Campo Obrigatório"));
		}
		if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoAutoAvaliacao2())) {
			result.addError(new FieldError("avaliacao",	"questaoAutoAvaliacao2", "Campo Obrigatório"));
		}
		if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoOrganizacao())) {
			result.addError(new FieldError("avaliacao", "questaoOrganizacao", "Campo Obrigatório"));
		}
		if (avaliacao.getQuestaoEspacoFisico() != null) {
			if (GenericValidator.isBlankOrNull(avaliacao.getQuestaoEspacoFisico())) {
				result.addError(new FieldError("avaliacao",	"questaoEspacoFisico", "Campo Obrigatório"));
			}
		}

		if (result.hasErrors()) {			
			modelMap.addAttribute("evento",	eventoDao.find(avaliacao.getEventoId().getId()));
			modelMap.addAttribute("modulo",	moduloDao.find(avaliacao.getModuloId().getId()));

			Modulo modulocons = moduloDao.findById(avaliacao.getModuloId().getId());
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
				i = i + 1;
			}
			modelMap.addAttribute("participante", participanteDao.find(avaliacao.getParticipanteId().getId()));

			return "avaliacao/createaluno";

		}
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		Date hoje = null;
		try {
			hoje = df.parse(df.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		avaliacao.setDataCadastro(hoje);
		avaliacaoDao.persist(avaliacao);
		return "redirect:/avaliacao/minhasavaliacoes";
	}		

	//@RequestMapping(value = "/avaliacao/minhasavaliacoes", method = RequestMethod.GET)
	public String minhasavaliacoes(ModelMap modelMap, HttpServletRequest request) {
		Collection<Generica> generic = avaliacaoDao.findNaoRealizadasByParticipanteCpf(usuarioDao.findByLogin(request.getRemoteUser()).getCpf());
		modelMap.addAttribute("avaliacoesnaorealizadas", generic);
		modelMap.addAttribute("avaliacoes",	avaliacaoDao.findByParticipanteCpf(usuarioDao.findByLogin(request.getRemoteUser()).getCpf()));
		return "avaliacao/minhasavaliacoes";
	}
	
	@RequestMapping(value = "/avaliacao/procuraParticipante", method = RequestMethod.GET)
	@ResponseBody
	public List<Participante> procuraParticipante(@RequestParam(value = "eventoId") Long eventoId) {
		return participanteDao.findByEventoId(eventoId);
	}

	@RequestMapping(value = "/avaliacao/procuraModulo", method = RequestMethod.GET)
	@ResponseBody
	public List<Modulo> procuraModulo(@RequestParam(value = "eventoId") Long eventoId) {
		return moduloDao.findByEventoId(eventoId);
	}

	@RequestMapping(value = "/avaliacao/procuraInstrutor", method = RequestMethod.GET)
	@ResponseBody
	public List<Instrutor> procuraInstrutor(@RequestParam(value = "moduloId") Long moduloId) {
		return moduloDao.find(moduloId).getInstrutorList();
	}
	
	@ModelAttribute("respostasQuestoes")
	public Map<String, String> populateRespostas() {

		Map<String, String> populate = new LinkedHashMap<String, String>();

		populate.put(null, "Selecione...");
		populate.put("Insuficiente", "Insuficiente");
		populate.put("Regular", "Regular");
		populate.put("Bom", "Bom");
		populate.put("Excelente", "Excelente");
		return populate;
	}
	
	public boolean possuiAvaliacaoByParticipanteEventoModulo(Avaliacao avaliacao) {
		Collection<Avaliacao> avalicacoesParticipante = avaliacaoDao.findAvaliacaoByCriteria(avaliacao.getEventoId().getId(), avaliacao.getModuloId().getId(), avaliacao.getParticipanteId().getId(), null, null);
		if (!avalicacoesParticipante.isEmpty())
			return true;
		else
			return false;
	}

	public boolean numeroAvaliacoesOk(Avaliacao avaliacao) {
		List<Inscricao> inscricoes = inscricaoDAO.findByEventoAndConfirmada(avaliacao.getEventoId().getId(), "S");		
		Collection<Avaliacao> avalicacoesByModulos = avaliacaoDao.findAvaliacaoByCriteria(avaliacao.getEventoId().getId(), avaliacao.getModuloId().getId(), new Long(0), null, null);		
		if (avalicacoesByModulos.size() + 1 <= inscricoes.size())
			return true;
		else
			return false;
	}

	public boolean campoVazioOk(Avaliacao avaliacao) {
		boolean camposVazio = GenericValidator.isBlankOrNull(avaliacao.getQuestaoAutoAvaliacao1())
				&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoAutoAvaliacao2())
				&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoConteudo1())
				&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoConteudo2())
				&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoConteudo3())
				&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoOrganizacao());

		if (avaliacao.getInstrutorId() != null	&& !avaliacao.getInstrutorId().getNome().equals("DIVERSOS")) {
			camposVazio = camposVazio && GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor1())
					&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor2())
					&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor3())
					&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor4())
					&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor5())
					&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor6());
			if (avaliacao.getQuestaoInstrutor21() != null) {
				camposVazio = camposVazio && GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor21())
						&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor22())
						&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor23())
						&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor24())
						&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor25())
						&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor26());
			}
			if (avaliacao.getQuestaoInstrutor21() != null) {
				camposVazio = camposVazio && GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor31())
						&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor32())
						&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor33())
						&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor34())
						&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor35())
						&& GenericValidator.isBlankOrNull(avaliacao.getQuestaoInstrutor36());
			}

		}
		if (avaliacao.getQuestaoEspacoFisico() != null) {
			camposVazio = camposVazio && GenericValidator.isBlankOrNull(avaliacao.getQuestaoEspacoFisico());
		}

		return camposVazio;
	}
	
	
	@ModelAttribute("eventoList")
	public Collection<Evento> populateEventos() {
		return eventoDao.findAll();
	}	

	@ModelAttribute("eventoListAvaliacoes")
	public Collection<Evento> populateEventosAvaliacoes() {
		return eventoDao.findEventosAvaliacoes();
	}	
	
}