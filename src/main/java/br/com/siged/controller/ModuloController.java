package br.com.siged.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.displaytag.pagination.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
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

import br.com.siged.controller.validators.ModuloValidator;
import br.com.siged.dao.AvaliacaoReacaoDAO;
import br.com.siged.dao.CertificadoEmitidoDAO;
import br.com.siged.dao.CidadeDAO;
import br.com.siged.dao.DesempenhoDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.FrequenciaDAO;
import br.com.siged.dao.InstrutorDAO;
import br.com.siged.dao.ModalidadeDAO;
import br.com.siged.dao.ModuloDAO;
import br.com.siged.dao.MunicipioDAO;
import br.com.siged.dao.NotaDAO;
import br.com.siged.dao.PaisDAO;
import br.com.siged.dao.TipoLocalizacaoEventoDAO;
import br.com.siged.dao.UfDAO;
import br.com.siged.dao.UfMunicipioDAO;
import br.com.siged.dao.pagination.DisplayTagPageable;
import br.com.siged.editor.CidadeEditor;
import br.com.siged.editor.EventoEditor;
import br.com.siged.editor.InstrutorEditor;
import br.com.siged.editor.ModalidadeEditor;
import br.com.siged.editor.MunicipioEditor;
import br.com.siged.editor.PaisEditor;
import br.com.siged.editor.TipoLocalizacaoEventoEditor;
import br.com.siged.editor.UfEditor;
import br.com.siged.editor.UfMunicipioEditor;
import br.com.siged.entidades.AvaliacaoReacao;
import br.com.siged.entidades.CertificadoEmitido;
import br.com.siged.entidades.Cidade;
import br.com.siged.entidades.Desempenho;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Frequencia;
import br.com.siged.entidades.Instrutor;
import br.com.siged.entidades.Modalidade;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Nota;
import br.com.siged.entidades.Pais;
import br.com.siged.entidades.TipoLocalizacaoEvento;
import br.com.siged.entidades.TipoLocalizacaoModalidade;
import br.com.siged.entidades.externas.UfMunicipio;
import br.com.siged.filtro.ModuloFiltro;
import br.com.siged.service.ModuloService;
import br.com.siged.service.exception.NaoPodeSalvarModuloException;

@Controller
@RequestMapping("/modulo/**")
public class ModuloController {
	
	@Autowired
	private ModuloDAO moduloDao;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private InstrutorDAO instrutorDao;
	@Autowired
	private TipoLocalizacaoEventoDAO tipoLocalizacaoEventoDao;
	@Autowired
	private AvaliacaoReacaoDAO avaliacaoreacaoDao;
	@Autowired
	private NotaDAO notaDao;
	@Autowired
	private FrequenciaDAO frequenciaDao;
	@Autowired
	private DesempenhoDAO desempenhoDAO;
	@Autowired
	private CertificadoEmitidoDAO certificadoEmitidoDao;
	@Autowired
	private ModuloService moduloService;
	@Autowired
	private ModuloValidator moduloValidator;
	@Autowired
	private ModalidadeDAO modalidadeDao;
	@Autowired
	private PaisDAO paisDao;
	@Autowired
	private CidadeDAO cidadeDao;
	@Autowired
	private UfDAO ufDao;
	@Autowired
	private UfMunicipioDAO ufMunicipioDao;
	@Autowired
	private MunicipioDAO municipioDao;

	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(br.com.siged.entidades.Evento.class, new EventoEditor(eventoDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Instrutor.class, new InstrutorEditor(instrutorDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.TipoLocalizacaoEvento.class, new TipoLocalizacaoEventoEditor(tipoLocalizacaoEventoDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Modalidade.class, new ModalidadeEditor(modalidadeDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Cidade.class, new CidadeEditor(cidadeDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Uf.class, new UfEditor(ufDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Pais.class, new PaisEditor(paisDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.externas.UfMunicipio.class, new UfMunicipioEditor(ufMunicipioDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Municipio.class, new MunicipioEditor(municipioDao));
        dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
        dataBinder.registerCustomEditor(List.class, "instrutorList", new CustomCollectionEditor(List.class) {
			protected Object convertElement(Object element) {
				String id = null;

				if (element instanceof String) {
					id = (String) element;
				}

				return id != null ? instrutorDao.find(Long.valueOf(id)) : null;
			}
		});
        
        if(dataBinder.getTarget() instanceof Modulo)
        	dataBinder.setValidator(moduloValidator);
    }
	
	@RequestMapping(value = "/modulo/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("modulo", moduloDao.find(id));
		return "modulo/show";
	}	

	@RequestMapping(value = "/modulo", method = RequestMethod.GET)
	public String list(ModelMap modelMap, HttpServletRequest request) {
		ModuloFiltro filtro = new ModuloFiltro();
		DisplayTagPageable pageable = new DisplayTagPageable(request);
		PaginatedList displayTagList = moduloDao.filtrar(filtro, pageable);
		
		modelMap.addAttribute("modulofiltro", filtro);
		modelMap.addAttribute("modulos", displayTagList);
		return "modulo/list";
	}
	
	@RequestMapping(value = "/modulo/search/byevento/{eventoId}", method = RequestMethod.GET)
	public String searchByEvento(@PathVariable("eventoId") Long eventoId, ModelMap modelMap, ServletRequest request, ServletResponse response) throws IOException {
		ModuloFiltro filtro = new ModuloFiltro();
		filtro.setEvento(eventoId);
		return search(filtro, modelMap, request, response);
	}
	
	@RequestMapping(value = "/modulo/search", method = RequestMethod.GET)
	public String search(ModuloFiltro modulofiltro, ModelMap modelMap, ServletRequest request, ServletResponse response) throws IOException {
		DisplayTagPageable pageable = new DisplayTagPageable(request);
		PaginatedList displayTagList = moduloDao.filtrar(modulofiltro, pageable);
		
		modelMap.addAttribute("modulofiltro", modulofiltro);
		modelMap.addAttribute("modulos", displayTagList);
		return "modulo/list";
	}

	@RequestMapping(value = "/modulo/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		Modulo modulo = moduloDao.find(id);
		List<AvaliacaoReacao> avaliacoesReacao = avaliacaoreacaoDao.findByModulo(modulo);
		List<Nota> notas = notaDao.findByModulo(modulo);
		List<Frequencia> frequencias = frequenciaDao.findByModulo(modulo);
		List<Desempenho> desempenhos = desempenhoDAO.findByModulo(modulo);
		
		if( avaliacoesReacao.size() == 0 && notas.size() == 0 && frequencias.size() == 0 && desempenhos.size() == 0 ){			
			moduloDao.remove(moduloDao.find(id));
		}else{
			model.addAttribute("mensagem","Não foi possivel efetuar a exclusão do módulo pois o mesmo possui outras informações cadastradas!");			
		}
		
		return "redirect:/modulo";
	}

	@RequestMapping(value = "/modulo/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("modulo", new Modulo());
		modelMap.addAttribute("localizacaoList", new ArrayList<TipoLocalizacaoEvento>());
		return "modulo/create";
	}

	@RequestMapping(value = "/modulo", method = RequestMethod.POST)
	public String create(@ModelAttribute("modulo") @Valid Modulo modulo, BindingResult result, ModelMap modelMap, HttpServletRequest request) {
		if ((modulo.getPaisId() == null || modulo.getPaisId().getId() == 0) && !modulo.getModalidade().isEAD()) {
			result.addError(new FieldError("modulo", "paisId", "Campo Obrigatório"));
		}
		
		if (modulo.getPaisId() != null && modulo.getPaisId().getId() == 1  && !modulo.getModalidade().isEAD()) {
			
			if (modulo.getUfMunicipio() == null || modulo.getUfMunicipio().getUf().equals("0") ){				
				result.addError(new FieldError("modulo", "ufMunicipio", "Campo Obrigatório"));
			}
			if (modulo.getMunicipio() == null || modulo.getMunicipio().getId() == 0){				
				result.addError(new FieldError("modulo", "municipio", "Campo Obrigatório"));
			}		
		}		
		if (result.hasErrors()) {
			System.out.println(result.getFieldErrors());
			Boolean readonly = Boolean.valueOf(request.getParameter("readonly"));
			modelMap.addAttribute("readonly", readonly);
			if (modulo.getModalidade() != null) {
				if(modulo.getModalidade().isEAD()) {
					modelMap.addAttribute("localizacaoList", tipoLocalizacaoEventoDao.findAllByModalidade(TipoLocalizacaoModalidade.EAD, TipoLocalizacaoModalidade.PRESENCIAL_E_EAD));
				} else if (modulo.getModalidade().isPresencial()) {
					modelMap.addAttribute("localizacaoList", tipoLocalizacaoEventoDao.findAllByModalidade(TipoLocalizacaoModalidade.PRESENCIAL, TipoLocalizacaoModalidade.PRESENCIAL_E_EAD));
				}
			} else {
				modelMap.addAttribute("localizacaoList", new ArrayList<TipoLocalizacaoEvento>());
			}
			return "modulo/create";
		}
		
		try {
			moduloService.salvar(modulo);
		} catch(NaoPodeSalvarModuloException e) {
			System.out.println("TEM ERRO SALVAR");
			return "redirect:/modulo?mensagemErro=" + e.getMessage();
		}
		
		return "redirect:/modulo";
	}

	@RequestMapping(value = "/modulo/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		Modulo modulo = moduloDao.find(id);		
		modelMap.addAttribute("modulo", modulo);
		if (modulo.getModalidade() != null) {
			if(modulo.getModalidade().isEAD()) {
				modelMap.addAttribute("localizacaoList", tipoLocalizacaoEventoDao.findAllByModalidade(TipoLocalizacaoModalidade.EAD, TipoLocalizacaoModalidade.PRESENCIAL_E_EAD));
			} else if (modulo.getModalidade().isPresencial()) {
				modelMap.addAttribute("localizacaoList", tipoLocalizacaoEventoDao.findAllByModalidade(TipoLocalizacaoModalidade.PRESENCIAL, TipoLocalizacaoModalidade.PRESENCIAL_E_EAD));
			}
		} else {
			modelMap.addAttribute("localizacaoList", new ArrayList<TipoLocalizacaoEvento>());
		}
		
		List<CertificadoEmitido> certificadosEmitidos = certificadoEmitidoDao.findByEventoId(modulo.getEventoId().getId());
		if ( certificadosEmitidos != null && certificadosEmitidos.size() > 0 )
			modelMap.addAttribute("temCertificadoEmitido", true);
		
		return "modulo/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("modulo") @Valid Modulo modulo, BindingResult result, ModelMap modelMap) {
		
		List<CertificadoEmitido> certificadosEmitidos = certificadoEmitidoDao.findByEventoId(modulo.getEventoId().getId());
		
		if (result.hasErrors()) {
			if ( certificadosEmitidos != null && certificadosEmitidos.size() > 0 ) {
				modelMap.addAttribute("temCertificadoEmitido", true);				
			}
			if (modulo.getModalidade() != null) {
				if(modulo.getModalidade().isEAD()) {
					modelMap.addAttribute("localizacaoList", tipoLocalizacaoEventoDao.findAllByModalidade(TipoLocalizacaoModalidade.EAD, TipoLocalizacaoModalidade.PRESENCIAL_E_EAD));
				} else if (modulo.getModalidade().isPresencial()) {
					modelMap.addAttribute("localizacaoList", tipoLocalizacaoEventoDao.findAllByModalidade(TipoLocalizacaoModalidade.PRESENCIAL, TipoLocalizacaoModalidade.PRESENCIAL_E_EAD));
				}
			} else {
				modelMap.addAttribute("localizacaoList", new ArrayList<TipoLocalizacaoEvento>());
			}
			/*
			 * Campo modalidade no evento foi depreciado
			modelMap.addAttribute("modalidadeIdEvento", modulo.getEventoId().getModalidadeId().getId());
			 */
			return "modulo/update";
        }
		
		try {
			moduloService.salvar(modulo);
		} catch (NaoPodeSalvarModuloException e) {
			return "redirect:/modulo?mensagemErro=" + e.getMessage();
		}
		
		return "redirect:/modulo";
	}
	
	@ModelAttribute("eventoList")
	public Collection<Evento> populateEvento() {
        return eventoDao.findAll();
    }
	@ModelAttribute("instrutorList")
	public Collection<Instrutor> populateInstrutor() {
        return instrutorDao.findBySituacao();
    }
	@ModelAttribute("tipoLocalizacaoEventoList")
	public Collection<TipoLocalizacaoEvento> populateTipoLocalizacaoEvento() {
        return tipoLocalizacaoEventoDao.findAll();
    }
	@ModelAttribute("listaUfs")
	public Collection<UfMunicipio> populateUfs() {
        return ufMunicipioDao.findAll();
    }
	
	@ModelAttribute("listaCidades")
	public Collection<Cidade> populateCidades() {
        return cidadeDao.findAll();
    }
	
	@ModelAttribute("listaPaises")
	public Collection<Pais> populatePaises() {
        return paisDao.findAll();
    }
	
	
	@ModelAttribute("modalidadeList")
	public Collection<Modalidade> populateModalidades() {
		return modalidadeDao.findAll();
	}

}
