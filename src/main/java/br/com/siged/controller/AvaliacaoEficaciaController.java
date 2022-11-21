package br.com.siged.controller;

import java.io.IOException;
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

import br.com.siged.dao.AvaliacaoEficaciaDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.ResponsavelSetorDAO;
import br.com.siged.dao.UsuarioSCADAO;
import br.com.siged.editor.EventoEditor;
import br.com.siged.editor.ParticipanteEditor;
import br.com.siged.editor.UsuarioSCAEditor;
import br.com.siged.entidades.AvaliacaoEficacia;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Generica;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.ResponsavelSetor;
import br.com.siged.entidades.externas.UsuarioSCA;
import br.com.siged.filtro.AvaliacaoEficaciaFiltro;

@Controller
@RequestMapping("/avaliacaoeficacia/**")
public class AvaliacaoEficaciaController {
	@Autowired
	private AvaliacaoEficaciaDAO avaliacaoeficaciaDao;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ParticipanteDAO participanteDao;	
	@Autowired
	private UsuarioSCADAO usuarioDao;
	@Autowired
	private ResponsavelSetorDAO responsavelSetorDao;
	
	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(br.com.siged.entidades.Evento.class, new EventoEditor(eventoDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Participante.class, new ParticipanteEditor(participanteDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.externas.UsuarioSCA.class, new UsuarioSCAEditor(usuarioDao));
        dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));

    }

	@RequestMapping(value = "/avaliacaoeficacia/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("avaliacaoeficacia", avaliacaoeficaciaDao.find(id));
		return "avaliacaoeficacia/show";
	}	

	@RequestMapping(value = "/avaliacaoeficacia", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("avaliacaoeficaciafiltro", new AvaliacaoEficaciaFiltro());
		modelMap.addAttribute("avaliacoeseficacia", avaliacaoeficaciaDao.findAll());
		return "avaliacaoeficacia/list";
	}	
	
	@RequestMapping(value = "/avaliacaoeficacia/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		avaliacaoeficaciaDao.remove(avaliacaoeficaciaDao.find(id));
		return "redirect:/avaliacaoeficacia";
	}

	@RequestMapping(value = "/avaliacaoeficacia/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		
		Collection<Evento> eventos = avaliacaoeficaciaDao.findByEventosCujaEficaciaDeveSerAvaliada();
		
		if(eventos != null && eventos.size() == 0){
			modelMap.addAttribute("mensagem", "Não há eventos cuja eficácia deva ser avaliada.");
			return "redirect:/avaliacaoeficacia";			
		}
		
		modelMap.addAttribute("avaliacaoeficacia", new AvaliacaoEficacia());
		modelMap.addAttribute("eventoCujaEficaciaDeveSerAvaliada", eventos);
		
		return "avaliacaoeficacia/create";
	}
	
	@RequestMapping(value = "/avaliacaoeficacia/search", method = RequestMethod.GET)
	public String search(AvaliacaoEficaciaFiltro avaliacaoeficaciafiltro, ModelMap modelMap, ServletRequest Request, ServletResponse Response) throws IOException {
		modelMap.addAttribute("avaliacaoeficaciafiltro", avaliacaoeficaciafiltro);
		modelMap.addAttribute("avaliacoeseficacia", avaliacaoeficaciaDao.findAvaliacaoEficaciaByCriteria(avaliacaoeficaciafiltro.getEvento(), avaliacaoeficaciafiltro.getParticipante(), avaliacaoeficaciafiltro.getData_cadastro1(), avaliacaoeficaciafiltro.getData_cadastro2()));
		return "avaliacaoeficacia/list";
	}

	@RequestMapping(value = "/avaliacaoeficacia", method = RequestMethod.POST)
	public String create(@ModelAttribute("avaliacaoeficacia") @Valid AvaliacaoEficacia avaliacaoeficacia, BindingResult result, ModelMap modelMap, HttpServletRequest request) {
		
		if(avaliacaoeficacia.getEventoId() != null && avaliacaoeficacia.getParticipanteId() != null){			
			AvaliacaoEficacia av = avaliacaoeficaciaDao.findByEventoIdAndParticipanteId(avaliacaoeficacia.getEventoId().getId(), avaliacaoeficacia.getParticipanteId().getId());
			
			if (av != null)
				return "redirect:/avaliacaoeficacia/form?mensagem=Este participante já foi avaliado para este evento.";
			
			if (avaliacaoeficacia.getDesempenhoServico() == "" && avaliacaoeficacia.getMelhoria() == "" && avaliacaoeficacia.getObservacao() == ""){
				modelMap.addAttribute("avaliacaoeficacia", avaliacaoeficacia);
				result.addError(new FieldError("avaliacaoeficacia", "observacao", "Justifique a impossibilidade de realizar a avaliação."));
			}			
		}		
		
		if (result.hasErrors()) {
			modelMap.addAttribute("eventoCujaEficaciaDeveSerAvaliada", avaliacaoeficaciaDao.findByEventosCujaEficaciaDeveSerAvaliada());
			return "avaliacaoeficacia/create";
        }
		
		if (request.isUserInRole("ROLE_CHEFE")){
			UsuarioSCA responsavel = usuarioDao.findByLogin(request.getRemoteUser());
			avaliacaoeficacia.setResponsavel(responsavel);
		}
		
		avaliacaoeficacia.setDataCadastro(new Date());		
		avaliacaoeficaciaDao.persist(avaliacaoeficacia);
		
		if (request.isUserInRole("ROLE_ADMINISTRADOR")) {
			return "redirect:/avaliacaoeficacia";
		} else {
			return "redirect:/avaliacaoeficacia/minhasavaliacoes";
		}
		
	}

	@RequestMapping(value = "/avaliacaoeficacia/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap ) {
		AvaliacaoEficacia av = avaliacaoeficaciaDao.find(id);
		modelMap.addAttribute("avaliacaoeficacia", av);
		modelMap.addAttribute("evento", av.getEventoId());
		modelMap.addAttribute("participante", av.getParticipanteId());		
		return "avaliacaoeficacia/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("avaliacaoeficacia") @Valid AvaliacaoEficacia avaliacaoeficacia ,BindingResult result, ModelMap modelMap, HttpServletRequest request) {

		if (avaliacaoeficacia.getDesempenhoServico() == "" && avaliacaoeficacia.getMelhoria()== "" && avaliacaoeficacia.getObservacao() == "") {
			result.addError(new FieldError("avaliacaoeficacia", "observacao", "Justifique a impossibilidade de realizar a avaliação."));
		}		
		
		if (result.hasErrors()) { 		
			modelMap.addAttribute("evento", avaliacaoeficacia.getEventoId());
			modelMap.addAttribute("participante", avaliacaoeficacia.getParticipanteId());
			return "avaliacaoeficacia/update";
		
	    } 	
			avaliacaoeficaciaDao.merge(avaliacaoeficacia);
			return "redirect:/avaliacaoeficacia";
	}	
	
	@RequestMapping(value = "/avaliacaoeficacia/formaluno/{evento}/{participante}", method = RequestMethod.GET)
	public String formaluno(ModelMap modelMap, @PathVariable("evento") Long evento, @PathVariable("participante") Long participante) {
		AvaliacaoEficacia av = new AvaliacaoEficacia();
		av.setEventoId(eventoDao.find(evento));
		av.setParticipanteId(participanteDao.find(participante));
		
		modelMap.addAttribute("avaliacaoeficacia", av);
		
		return "avaliacaoeficacia/create";
	}
	
	@RequestMapping(value = "/avaliacaoeficacia/minhasavaliacoes", method = RequestMethod.GET)
	public String minhasavaliacoes(ModelMap modelMap, HttpServletRequest request) {
		
		UsuarioSCA responsavel = usuarioDao.findByLogin(request.getRemoteUser());
		
		Long idSetor;
		String cpfResponsavel;
		
		Collection<Generica> avaliacoeseficacianaorealizadas = new ArrayList<Generica>();		
		
		List<ResponsavelSetor> rs = responsavelSetorDao.findAtivoByResponsavel(responsavel);
					
		for (ResponsavelSetor responsavelSetor : rs) {
			idSetor = responsavelSetor.getSetor().getId();
			cpfResponsavel = responsavelSetor.getResponsavel().getCpf();			
			avaliacoeseficacianaorealizadas.addAll(avaliacaoeficaciaDao.findNaoRealizadasByChefeSetor(idSetor, cpfResponsavel));
		}			
		
		List<ResponsavelSetor> rsList = responsavelSetorDao.findAtivoByResponsavelImediato(responsavel);
		
		for (ResponsavelSetor responsavelSetor : rsList ){
			cpfResponsavel = responsavelSetor.getResponsavel().getCpf();			
			avaliacoeseficacianaorealizadas.addAll(avaliacaoeficaciaDao.findNaoRealizadasByParticipante(cpfResponsavel));			
		}		
		
		modelMap.addAttribute("avaliacoeseficacianaorealizadas", avaliacoeseficacianaorealizadas);		
		modelMap.addAttribute("avaliacoeseficacia", avaliacaoeficaciaDao.findByResponsavel(responsavel));
		
		return "avaliacaoeficacia/minhasavaliacoes";
	}
	
	
	@RequestMapping(value = "/avaliacaoeficacia/procuraParticipante", method = RequestMethod.GET)
    @ResponseBody
    public List<Participante> procuraParticipante(@RequestParam(value="eventoId") Long eventoId) {        
        return participanteDao.findByEventoId(eventoId);
    }
	
	@RequestMapping(value = "/avaliacaoeficacia/procuraParticipantesAprovados", method = RequestMethod.GET)
    @ResponseBody
    public List<Participante> procuraParticipantesAprovados(@RequestParam(value="eventoId") Long eventoId) {        
        return participanteDao.findServidoresAprovados(eventoId);
    }
	

	@RequestMapping(value = "/avaliacaoeficacia/procuraParticipanteEficaciaAvaliadaByEventoId", method = RequestMethod.GET)
    @ResponseBody
    public List<Participante> procuraParticipanteEficaciaAvaliada(@RequestParam(value="eventoId") Long eventoId) {        
        return participanteDao.findByEventoIdParticipanteEficaciaAvaliada(eventoId);
    }
	
	@RequestMapping(value = "/avaliacaoeficacia/procuraParticipanteComAvaliacaodeEficacia", method = RequestMethod.GET)
    @ResponseBody
    public List<Participante> populateParticipanteEventoAvaliacaoEficacia() {
		return participanteDao.findParticipanteEficaciaAvaliada();
	}
	
		
	@ModelAttribute("respostasQuestoes")
    public Map<String,String> populateRespostas() {
		
		Map<String,String> populate = new LinkedHashMap<String,String>();
		
		populate.put("Nenhuma Melhoria", "Nenhuma Melhoria");
		populate.put("Pouca Melhoria", "Pouca Melhoria");
		populate.put("Melhoria", "Melhoria");
		populate.put("Significativa Melhoria", "Significativa Melhoria");
		return populate;
	}
	
	@ModelAttribute("melhorias")
    public Map<String,String> populateMelhorias() {
		
		Map<String,String> populate = new LinkedHashMap<String,String>();
		
		populate.put("S", "S");
		populate.put("N", "N");
		return populate;
	}
	
	@ModelAttribute("eventoList")
	public Collection<Evento> populateEventos() {
        return eventoDao.findAll();
    }	
	
	@ModelAttribute("chefes")
	public Collection<UsuarioSCA> populateChefes() {
        return usuarioDao.findServidores();
    }
	

}
