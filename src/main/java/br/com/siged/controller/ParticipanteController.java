package br.com.siged.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.displaytag.pagination.PaginatedList;
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

import br.com.siged.controller.validators.ParticipanteValidator;
import br.com.siged.dao.CidadeDAO;
import br.com.siged.dao.EntidadeDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.FormacaoAcademicaDAO;
import br.com.siged.dao.LocalidadeDAO;
import br.com.siged.dao.MunicipioDAO;
import br.com.siged.dao.NivelEscolaridadeDAO;
import br.com.siged.dao.PaisDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.SetorDAO;
import br.com.siged.dao.TipoPublicoAlvoDAO;
import br.com.siged.dao.UfDAO;
import br.com.siged.dao.UfMunicipioDAO;
import br.com.siged.dao.UsuarioDAO;
import br.com.siged.dao.pagination.DisplayTagPageable;
import br.com.siged.editor.CidadeEditor;
import br.com.siged.editor.EntidadeEditor;
import br.com.siged.editor.EventoEditor;
import br.com.siged.editor.FormacaoAcademicaEditor;
import br.com.siged.editor.LocalidadeEditor;
import br.com.siged.editor.MunicipioEditor;
import br.com.siged.editor.NivelEscolaridadeEditor;
import br.com.siged.editor.PaisEditor;
import br.com.siged.editor.SetorEditor;
import br.com.siged.editor.TipoPublicoAlvoEditor;
import br.com.siged.editor.UfEditor;
import br.com.siged.editor.UfMunicipioEditor;
import br.com.siged.entidades.FormacaoAcademica;
import br.com.siged.entidades.NivelEscolaridade;
import br.com.siged.entidades.Pais;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.TipoPublicoAlvo;
import br.com.siged.entidades.Usuario;
import br.com.siged.entidades.externas.Setor;
import br.com.siged.entidades.externas.UfMunicipio;
import br.com.siged.filtro.InscricaoFiltro;
import br.com.siged.filtro.ParticipanteFiltro;
import br.com.siged.service.AuthenticationService;
import br.com.siged.util.EmailUtil;

@Controller
@RequestMapping("/participante/**")
public class ParticipanteController {
	
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private TipoPublicoAlvoDAO tipoPublicoAlvoDao;
	@Autowired
	private FormacaoAcademicaDAO formacaoAcademicaDao;
	@Autowired
	private NivelEscolaridadeDAO nivelEscolaridadeDao;
	@Autowired
	private SetorDAO setorDao;
	@Autowired
	private EntidadeDAO entidadeDAO;
	@Autowired
	private UsuarioDAO usuarioExternoDao;
	@Autowired
	private MunicipioDAO municipioDAO;
	@Autowired
	private UfMunicipioDAO ufMunicipioDAO;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private LocalidadeDAO localidadeDao;
	@Autowired
	private PaisDAO paisDao;
	@Autowired
	private CidadeDAO cidadeDao;
	@Autowired
	private UfDAO ufDao;
	@Autowired
	private ParticipanteValidator participanteValidator;
	
	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(br.com.siged.entidades.Pais.class, new PaisEditor(paisDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Cidade.class, new CidadeEditor(cidadeDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Uf.class, new UfEditor(ufDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.NivelEscolaridade.class, new NivelEscolaridadeEditor(nivelEscolaridadeDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.TipoPublicoAlvo.class, new TipoPublicoAlvoEditor(tipoPublicoAlvoDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.FormacaoAcademica.class, new FormacaoAcademicaEditor(formacaoAcademicaDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Setor.class, new SetorEditor(setorDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Entidade.class, new EntidadeEditor(entidadeDAO));
        dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Localidade.class, new LocalidadeEditor(localidadeDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.externas.UfMunicipio.class, new UfMunicipioEditor(ufMunicipioDAO));
        dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Municipio.class, new MunicipioEditor(municipioDAO));
        dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Evento.class, new EventoEditor(eventoDao));
        
        if(dataBinder.getTarget() instanceof Participante) {
        	dataBinder.setValidator(participanteValidator);
        }
	}

	@RequestMapping(value = "/participante/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		
		modelMap.addAttribute("participante", participanteDao.find(id));
		
		return "participante/show";
	}	

	@RequestMapping(value = "/participante", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		
		modelMap.addAttribute("participantefiltro", new ParticipanteFiltro());
		
		return "participante/list";
	}
	
	@RequestMapping(value = "/participante/search", method = RequestMethod.GET)
	public String search(ParticipanteFiltro participantefiltro, ModelMap modelMap, HttpServletRequest request, ServletResponse Response) throws IOException {
		
		DisplayTagPageable pageable = new DisplayTagPageable(request);
		PaginatedList displayTagList = participanteDao.filtrar(participantefiltro, pageable);
		
		modelMap.addAttribute("participantefiltro", participantefiltro);
		modelMap.addAttribute("participantes", displayTagList);
		
		return "participante/list";
	}

	@RequestMapping(value = "/participante/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		
		try{
			participanteDao.remove(participanteDao.find(id));
		}catch (Exception e){
			e.printStackTrace();
			return "redirect:/participante?mensagem=Não foi possível excluir o participante. Verifique se este participante possui pré-inscrições e as exclua primeiro.";
		}
		
		return "redirect:/participante";
	}

	@RequestMapping(value = "/participante/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		
		modelMap.addAttribute("participante", new Participante());
		modelMap.addAttribute("localidadeList", localidadeDao.findByMunicipios());
		
		return "participante/create";
	}	
	
	@RequestMapping(value = "/participante", method = RequestMethod.POST)
	public String create(@ModelAttribute("participante") @Valid Participante participante, BindingResult result, ModelMap modelMap) {
	
		/*if (result.hasErrors()) {
			if(participante.getMunicipio() != null)
				participante.setUfMunicipio(ufMunicipioDAO.findByUf(participante.getMunicipio().getUfMunicipio()));			
			modelMap.addAttribute("participante", participante);
			modelMap.addAttribute("localidadeList", localidadeDao.findByMunicipios());
			return "participante/create";
        } */		
		if(participante.getPaisId() != null && participante.getPaisId().getId() == 1) {
			if(participante.getUfMunicipio() == null || participante.getUfMunicipio().getUf().equals("0")) {
				result.addError(new FieldError("participante", "ufMunicipio", "Campo Obrigatório"));
			}
			if(participante.getMunicipio() == null || participante.getMunicipio().getId() == 0) {
				result.addError(new FieldError("participante", "municipio", "Campo Obrigatório"));
			}
		}	
			
		if(participante.getTipoId().getId() == 2) {
			participante.setProfissao(null);
			participante.setEntidade(null);
		}else {
			participante.setMatricula(null);
			participante.setCargo(null);
			participante.setOrgaoId(null);
		}
				
		participante.setDataCadastro(new Date());
		
		participanteDao.persist(participante);
		modelMap.addAttribute("mensagemSucesso", "Incluído com sucesso!");
		
		Usuario usuarioSIGED = usuarioExternoDao.findByCpf(participante.getCpf());
		
		if(usuarioSIGED == null){
		
			usuarioSIGED = new Usuario(null,participante.getNome(),participante.getCpf().replace(".", "").replace("-", ""),AuthenticationService.toMd5("12345"),0,true,false,participante.getCpf(),new Date(),new Date(), participante.getEmail(),"");
		
			usuarioExternoDao.persist(usuarioSIGED);
			
			try {
				this.emailUtil.emailUsuario(usuarioSIGED);
			} catch (Exception e) {				
				e.printStackTrace();
			}
		
		}else{
			
			if( (participante.getEmail() != usuarioSIGED.getEmail()) || (participante.getNome() != usuarioSIGED.getNome()) ){
				
				usuarioSIGED.setEmail(participante.getEmail());
				usuarioSIGED.setNome(participante.getNome());
				
				usuarioExternoDao.merge(usuarioSIGED);
			}			
		}		
		
		return "redirect:/participante";
	}
		
	@RequestMapping(value = "/participante/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {	
		Participante participante = participanteDao.find(id);
		
		
		if(participante.getMunicipio() != null  ) {
			participante.setUfMunicipio(ufMunicipioDAO.findByUf(participante.getMunicipio().getUfMunicipio()));
		}
			
		/*
		if(participante.getPaisId().getId() == 1 ) {
			participante.setUfMunicipio(ufMunicipioDAO.findByUf(participante.getMunicipio().getUfMunicipio()));
		}*/			
		
		modelMap.addAttribute("participante", participante);
		modelMap.addAttribute("localidadeList", localidadeDao.findByMunicipios());
		
		return "participante/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("participante") @Valid Participante participante, BindingResult result, ModelMap modelMap) {
		
		if (result.hasErrors()) {
			modelMap.addAttribute("participante", participante);
			modelMap.addAttribute("localidadeList", localidadeDao.findByMunicipios());
			
			return "participante/update";
        }  
		
		participanteDao.merge(participante);
		
		Usuario usuarioSIGED = usuarioExternoDao.findByCpf(participante.getCpf());
		
		if( (participante.getEmail() != usuarioSIGED.getEmail()) || (participante.getNome() != usuarioSIGED.getNome()) ){
			
			usuarioSIGED.setEmail(participante.getEmail());
			usuarioSIGED.setNome(participante.getNome());
			
			usuarioExternoDao.merge(usuarioSIGED);
		}
		
		return "redirect:/participante";	
	}
	
	/**
	 * A view que usava esse método foi transferida para {@link EventoController#comunicado(ModelMap)}.
	 * Com a mudança passou-se a usar {@link AjaxController#procuraParticipanteByFiltro(InscricaoFiltro)} na consulta assíncrona que busca os participantes. 
	 * Assim o método ficou depreciado desde a mudança para a nova consulta.
	 * 
	 * @since Deprecated since 05/10/2018
	 */
	@Deprecated
	@RequestMapping(value = "/participante/procuraParticipantes", method = RequestMethod.GET)
	@ResponseBody
	public List<Participante> procuraParticipantes(@RequestParam(value="eventoId") Long eventoId) {
		return participanteDao.findByEventoId(eventoId);
	}	
	
	/**
	 * A view que usava esse método foi transferida para {@link EventoController#comunicado(ModelMap)}.
	 * Com a mudança passou-se a usar {@link AjaxController#procuraParticipanteByFiltro(InscricaoFiltro)} na consulta assíncrona que busca os participantes. 
	 * Assim o método ficou depreciado desde a mudança para a nova consulta.
	 * 
	 * @since Deprecated since 05/10/2018
	 */
	@Deprecated
	@RequestMapping(value = "/participante/procuraParticipantePorTipo", method = RequestMethod.GET)
	@ResponseBody
	public List<Participante> procuraParticipantePorTipoEEvento(@RequestParam(value="tipoId") Long tipoId) {
		return participanteDao.findByTipo(tipoId);
	}

	/**
	 * A view que usava esse método foi transferida para {@link EventoController#comunicado(ModelMap)}.
	 * Com a mudança passou-se a usar {@link AjaxController#procuraParticipanteByFiltro(InscricaoFiltro)} na consulta assíncrona que busca os participantes. 
	 * Assim o método ficou depreciado desde a mudança para a nova consulta.
	 * 
	 * @since Deprecated since 05/10/2018
	 */
	@Deprecated
	@RequestMapping(value = "/participante/procuraParticipantePorTipoEEvento", method = RequestMethod.GET)
	@ResponseBody
	public List<Participante> procuraParticipantePorTipoEEvento(@RequestParam(value="tipoId") Long tipoId, @RequestParam(value="eventoId") Long eventoId) {
		return participanteDao.findByTipoAndEvento(tipoId, eventoId);
	}
	
	@ModelAttribute("tipoParticipanteList")
	public Collection<TipoPublicoAlvo> populateTipoParticipante() {
		return tipoPublicoAlvoDao.findAll();
	}
	
	@ModelAttribute("tipoPublicoAlvoList")
	public Collection<TipoPublicoAlvo> populateTipoPublicoAlvos() {
        return tipoPublicoAlvoDao.findAllNotServidor();
    }
	
	@ModelAttribute("tipoPublicoAlvoList2")
	public Collection<TipoPublicoAlvo> populateTipoPublicoAlvos2() {
        return tipoPublicoAlvoDao.findAll();
    }
	
	@ModelAttribute("formacaoAcademicaList")
	public Collection<FormacaoAcademica> populateFormacaoAcademicas() {
        return formacaoAcademicaDao.findAll();
    }
	
	@ModelAttribute("nivelEscolaridadeList")
	public Collection<NivelEscolaridade> populateNivelEscolaridades() {
        return nivelEscolaridadeDao.findAll();
    }
	
	@ModelAttribute("listaPais")
	public Collection<Pais> populatePaises() {
		return paisDao.findAll();
    }
	
	@ModelAttribute("setorList")
	public Collection<Setor> populateSetors() {
        return setorDao.findAllSemTemp();
    }	
	@ModelAttribute("listaUf")
    public Collection<UfMunicipio> populateUfs() {
		return ufMunicipioDAO.findAll();
	}	
	
	@ModelAttribute("ufMunicipioList")
	public Collection<UfMunicipio> populateUfMunicipio() {
		return ufMunicipioDAO.findAll();
	}
    
}
