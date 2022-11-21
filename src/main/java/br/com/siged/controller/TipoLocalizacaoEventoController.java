package br.com.siged.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.aspectj.weaver.NewFieldTypeMunger;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.siged.dao.CidadeDAO;
import br.com.siged.dao.MunicipioDAO;
import br.com.siged.dao.PaisDAO;
import br.com.siged.dao.TipoLocalizacaoEventoDAO;
import br.com.siged.dao.UfDAO;
import br.com.siged.dao.UfMunicipioDAO;
import br.com.siged.editor.CidadeEditor;
import br.com.siged.editor.MunicipioEditor;
import br.com.siged.editor.PaisEditor;
import br.com.siged.editor.UfEditor;
import br.com.siged.editor.UfMunicipioEditor;
import br.com.siged.entidades.Cidade;
import br.com.siged.entidades.Pais;
import br.com.siged.entidades.TipoLocalizacaoEvento;
import br.com.siged.entidades.TipoLocalizacaoModalidade;
import br.com.siged.entidades.Uf;
import br.com.siged.entidades.externas.UfMunicipio;
import br.com.siged.service.exception.BusinessException;

@Controller
@RequestMapping("/tipolocalizacaoevento/**")
public class TipoLocalizacaoEventoController {
	@Autowired
	private TipoLocalizacaoEventoDAO tipolocalizacaoeventoDao;
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
        dataBinder.registerCustomEditor(br.com.siged.entidades.Cidade.class, new CidadeEditor(cidadeDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Uf.class, new UfEditor(ufDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Pais.class, new PaisEditor(paisDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.externas.UfMunicipio.class, new UfMunicipioEditor(ufMunicipioDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Municipio.class, new MunicipioEditor(municipioDao));
    }

	@RequestMapping(value = "/tipolocalizacaoevento/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("tipolocalizacaoevento", tipolocalizacaoeventoDao.find(id));
		return "tipolocalizacaoevento/show";
	}	

	@RequestMapping(value = "/tipolocalizacaoevento", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("tiposlocalizacaoevento", tipolocalizacaoeventoDao.findAll());
		return "tipolocalizacaoevento/list";
	}

	@RequestMapping(value = "/tipolocalizacaoevento/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		try {
			tipolocalizacaoeventoDao.remove(tipolocalizacaoeventoDao.find(id));
			model.addAttribute("mensagem","Excluído com sucesso!");
		} catch (Exception e) {
			model.addAttribute("mensagem","O registro não pode ser excluído pois está em uso.");
		}
		
		return "redirect:/tipolocalizacaoevento";
	}

	@RequestMapping(value = "/tipolocalizacaoevento/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("tipolocalizacaoevento", new TipoLocalizacaoEvento());
		return "tipolocalizacaoevento/create";
	}

	@RequestMapping(value = "/tipolocalizacaoevento", method = RequestMethod.POST)
	public String create(@ModelAttribute("tipolocalizacaoevento") @Valid TipoLocalizacaoEvento tipolocalizacaoevento, BindingResult result, ModelMap modelMap) {
		
		/*if (tipolocalizacaoevento.getPaisId() == null || tipolocalizacaoevento.getPaisId().getId() == 0) {
			result.addError(new FieldError("tipolocalizacaoevento", "paisId", "Campo Obrigatório"));
			
		}*/		
		if (tipolocalizacaoevento.getPaisId() != null && tipolocalizacaoevento.getPaisId().getId() == 1) {
			
			if (tipolocalizacaoevento.getUfMunicipio() == null || tipolocalizacaoevento.getUfMunicipio().getUf().equals("0")){				
				result.addError(new FieldError("tipolocalizacaoevento", "ufMunicipio", "Campo Obrigatório"));
			}
			if (tipolocalizacaoevento.getMunicipio() == null || tipolocalizacaoevento.getMunicipio().getId() == 0){				
				result.addError(new FieldError("tipolocalizacaoevento", "municipio", "Campo Obrigatório"));
			}		
		}
		if (tipolocalizacaoevento.getCidadePais().isEmpty() && tipolocalizacaoevento.getPaisId().getId() != 1) {					
			//throw new BusinessException("Insira a cidade!");
			result.addError(new FieldError("tipolocalizacaoevento", "cidadePais", "Campo Obrigatório"));			
		}
			
		if (result.hasErrors()) {  
			System.out.println(tipolocalizacaoevento);
			return "tipolocalizacaoevento/create";
        }  
		
		modelMap.addAttribute("mensagem","Incluído com sucesso!");	
		tipolocalizacaoeventoDao.persist(tipolocalizacaoevento);		
		return "redirect:/tipolocalizacaoevento";
	}

	@RequestMapping(value = "/tipolocalizacaoevento/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		
		TipoLocalizacaoEvento tipoLocalizacaoEvento = tipolocalizacaoeventoDao.findById(id);
		if (tipoLocalizacaoEvento.getMunicipio() != null) {
			tipoLocalizacaoEvento.setUfMunicipio(ufMunicipioDao.findByUf(tipoLocalizacaoEvento.getMunicipio().getUfMunicipio()));
		}
		
		modelMap.addAttribute("tipolocalizacaoevento", tipoLocalizacaoEvento);			
		return "tipolocalizacaoevento/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("tipolocalizacaoevento") @Valid TipoLocalizacaoEvento tipolocalizacaoevento, BindingResult result, ModelMap model) {
		
		/*if (tipolocalizacaoevento.getPaisId() == null || tipolocalizacaoevento.getPaisId().getId() == 0) {
			result.addError(new FieldError("tipolocalizacaoevento", "paisId", "Campo Obrigatório"));
		}*/
		
		if (tipolocalizacaoevento.getPaisId() != null && tipolocalizacaoevento.getPaisId().getId() == 1) {
			
			if (tipolocalizacaoevento.getUfMunicipio() == null || tipolocalizacaoevento.getUfMunicipio().getUf().equals("0")){				
				result.addError(new FieldError("tipolocalizacaoevento", "ufMunicipio", "Campo Obrigatório"));
			}
			if (tipolocalizacaoevento.getMunicipio() == null || tipolocalizacaoevento.getMunicipio().getId() == 0){				
				result.addError(new FieldError("tipolocalizacaoevento", "municipio", "Campo Obrigatório"));
			}
			
		}	
		if (tipolocalizacaoevento.getCidadePais().isEmpty() && tipolocalizacaoevento.getPaisId().getId() != 1) {					
			//throw new BusinessException("Insira a cidade!");
			result.addError(new FieldError("tipolocalizacaoevento", "cidadePais", "Campo Obrigatório"));			
		}
		
		if (result.hasErrors()) {  
			return "tipolocalizacaoevento/update";
        }
		
		model.addAttribute("mensagem","Alterado com sucesso!");
		
		tipolocalizacaoeventoDao.merge(tipolocalizacaoevento);
		
		return "redirect:/tipolocalizacaoevento";
	}	
	
    @RequestMapping(value = "/tipolocalizacaoevento/procuraEstado", method = RequestMethod.GET)
    public @ResponseBody List<Uf> procuraEstado(@RequestParam(value="paisId") Long paisId) {        
        return ufDao.findByPaisId(paisId);
    }
    
    @RequestMapping(value = "/tipolocalizacaoevento/presencial", method = RequestMethod.GET)
	@ResponseBody
	public List<TipoLocalizacaoEvento> localizacoesPresenciais() {
		return tipolocalizacaoeventoDao.findAllByModalidade(TipoLocalizacaoModalidade.PRESENCIAL);
	}
	
    @RequestMapping(value = "/tipolocalizacaoevento/ead", method = RequestMethod.GET)
	@ResponseBody
	public List<TipoLocalizacaoEvento> localizacoesEAD() {
		return tipolocalizacaoeventoDao.findAllByModalidade(TipoLocalizacaoModalidade.EAD);
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
	
	@ModelAttribute("tiposLocalizacaoModalidades")
	public Collection<TipoLocalizacaoModalidade> populateTiposLocalizacaoModalidade() {
		List<TipoLocalizacaoModalidade> tipos = new ArrayList<>();
		for (TipoLocalizacaoModalidade tipo : TipoLocalizacaoModalidade.values()) {
			if (tipo != TipoLocalizacaoModalidade.PRESENCIAL_E_EAD) {
				tipos.add(tipo);
			}
		}
        return tipos;
    }

}
