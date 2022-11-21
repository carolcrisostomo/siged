package br.com.siged.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.siged.dao.CidadeDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.MunicipioDAO;
import br.com.siged.dao.PaisDAO;
import br.com.siged.dao.ProvedorEventoDAO;
import br.com.siged.dao.UfDAO;
import br.com.siged.dao.UfMunicipioDAO;
import br.com.siged.editor.CidadeEditor;
import br.com.siged.editor.EventoEditor;
import br.com.siged.editor.MunicipioEditor;
import br.com.siged.editor.PaisEditor;
import br.com.siged.editor.UfEditor;
import br.com.siged.editor.UfMunicipioEditor;
import br.com.siged.entidades.Pais;
import br.com.siged.entidades.ProvedorEvento;
import br.com.siged.entidades.externas.UfMunicipio;

@Controller
@RequestMapping("/provedorevento/**")
public class ProvedorEventoController {
	
	@Autowired
	private ProvedorEventoDAO provedoreventoDao;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private PaisDAO paisDao;
	@Autowired
	private UfDAO ufDao;
	@Autowired
	private CidadeDAO cidadeDao;
	@Autowired
	private UfMunicipioDAO ufMunicipioDao;
	@Autowired
	private MunicipioDAO municipioDao;
	
	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(br.com.siged.entidades.Evento.class, new EventoEditor(eventoDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Pais.class, new PaisEditor(paisDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Uf.class, new UfEditor(ufDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Cidade.class, new CidadeEditor(cidadeDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.externas.UfMunicipio.class, new UfMunicipioEditor(ufMunicipioDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Municipio.class, new MunicipioEditor(municipioDao));
    }

	@RequestMapping(value = "/provedorevento/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("provedorevento", provedoreventoDao.find(id));
		return "provedorevento/show";
	}	

	@RequestMapping(value = "/provedorevento", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("provedoresevento", provedoreventoDao.findAll());
		return "provedorevento/list";
	}	

	@RequestMapping(value = "/provedorevento/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		try {
			provedoreventoDao.remove(provedoreventoDao.find(id));
			model.addAttribute("mensagem","Excluído com sucesso!");
		} catch (Exception e) {
			model.addAttribute("mensagem","O registro não pode ser excluído pois está em uso.");
		}
		
		return "redirect:/provedorevento";
	}

	@RequestMapping(value = "/provedorevento/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("provedorevento", new ProvedorEvento());
		return "provedorevento/create";
	}

	@RequestMapping(value = "/provedorevento", method = RequestMethod.POST)
	public String create(@ModelAttribute("provedorevento") @Valid ProvedorEvento provedorevento, BindingResult result, ModelMap model) {
		
		if (provedorevento.getPaisId() != null && provedorevento.getPaisId().getId() == 1) {
			
			if (provedorevento.getUfMunicipio() == null || provedorevento.getUfMunicipio().getUf().equals("0")){				
				result.addError(new FieldError("tipolocalizacaoevento", "ufMunicipio", "Campo Obrigatório"));
			}
			if (provedorevento.getMunicipio() == null || provedorevento.getMunicipio().getId() == 0){				
				result.addError(new FieldError("tipolocalizacaoevento", "municipio", "Campo Obrigatório"));
			}		
		}
		
		if (result.hasErrors()) {  
			return "provedorevento/create";
        }  
		model.addAttribute("mensagem","Incluído com sucesso!");
		provedoreventoDao.persist(provedorevento);
		return "redirect:/provedorevento";
	}

	@RequestMapping(value = "/provedorevento/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		ProvedorEvento provedorEvento = provedoreventoDao.find(id);
		
		if (provedorEvento.getMunicipio() != null) {
			provedorEvento.setUfMunicipio(ufMunicipioDao.findByUf(provedorEvento.getMunicipio().getUfMunicipio()));
		}
		
		modelMap.addAttribute("provedorevento", provedorEvento);
		return "provedorevento/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("provedorevento") @Valid ProvedorEvento provedorevento, BindingResult result, ModelMap model) {
			
		if (provedorevento.getPaisId() != null && provedorevento.getPaisId().getId() == 1) {
			
			if (provedorevento.getUfMunicipio() == null || provedorevento.getUfMunicipio().getUf().equals("0")){				
				result.addError(new FieldError("provedorevento", "ufMunicipio", "Campo Obrigatório"));		
			}
			if (provedorevento.getMunicipio() == null || provedorevento.getMunicipio().getId() == 0){				
				result.addError(new FieldError("provedorevento", "municipio", "Campo Obrigatório"));
			}		
		}
		
		if (result.hasErrors()) {
			return "provedorevento/update";
        } 
		model.addAttribute("mensagem","Alterado com sucesso!");
		provedoreventoDao.merge(provedorevento);
		return "redirect:/provedorevento";
	}	
	
	
	@ModelAttribute("listaPais")
	public Collection<Pais> populatePaises() {
		return paisDao.findAll();
    }
	
	@ModelAttribute("listaUf")
    public Collection<UfMunicipio> populateUfs() {
		return ufMunicipioDao.findAll();
	}	

}
