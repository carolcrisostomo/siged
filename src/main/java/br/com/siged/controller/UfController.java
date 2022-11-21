package br.com.siged.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.siged.dao.PaisDAO;
import br.com.siged.dao.UfDAO;
import br.com.siged.editor.PaisEditor;
import br.com.siged.entidades.Pais;
import br.com.siged.entidades.Uf;

@Controller
@RequestMapping("/uf/**")
public class UfController {
	@Autowired
	private UfDAO ufDao;
	@Autowired
	private PaisDAO paisDao;
	
	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(br.com.siged.entidades.Pais.class, new PaisEditor(paisDao));
     
    }

	@RequestMapping(value = "/uf/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("uf", ufDao.find(id));
		return "uf/show";
	}	

	@RequestMapping(value = "/uf", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("ufs", ufDao.findAll());
		return "uf/list";
	}

	@RequestMapping(value = "/uf/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		ufDao.remove(ufDao.find(id));
		return "redirect:/uf";
	}

	@RequestMapping(value = "/uf/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("uf", new Uf());
		return "uf/create";
	}

	@RequestMapping(value = "/uf", method = RequestMethod.POST)
	public String create(@ModelAttribute("uf") @Valid Uf uf, BindingResult result) {
		
		if (result.hasErrors()) {  
			return "uf/create";
        }  
		
		ufDao.persist(uf);
		return "redirect:/uf";
	}

	@RequestMapping(value = "/uf/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("uf", ufDao.find(id));
		return "uf/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("uf") @Valid Uf uf) {
		ufDao.merge(uf);
		return "redirect:/uf";
	}
	
	@ModelAttribute("listaPaises")
	public Collection<Pais> populatePaises() {
        return paisDao.findAll();
    }

}
