package br.com.siged.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.siged.dao.SituacaoModuloDAO;
import br.com.siged.entidades.SituacaoModulo;

@Controller
@RequestMapping("/situacaomodulo/**")
public class SituacaoModuloController {
	@Autowired
	private SituacaoModuloDAO situacaomoduloDao;

	@RequestMapping(value = "/situacaomodulo/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("situacaomodulo", situacaomoduloDao.find(id));
		return "situacaomodulo/show";
	}	

	@RequestMapping(value = "/situacaomodulo", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("situacoesmodulos", situacaomoduloDao.findAll());
		return "situacaomodulo/list";
	}

	@RequestMapping(value = "/situacaomodulo/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		situacaomoduloDao.remove(situacaomoduloDao.find(id));
		return "redirect:/situacaomodulo";
	}

	@RequestMapping(value = "/situacaomodulo/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("situacaomodulo", new SituacaoModulo());
		return "situacaomodulo/create";
	}

	@RequestMapping(value = "/situacaomodulo", method = RequestMethod.POST)
	public String create(@ModelAttribute("situacaomodulo") @Valid SituacaoModulo situacaomodulo, BindingResult result) {
		
		if (result.hasErrors()) {  
			return "situacaomodulo/create";
        }  
		
		situacaomoduloDao.persist(situacaomodulo);
		return "redirect:/situacaomodulo";
	}

	@RequestMapping(value = "/situacaomodulo/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("situacaomodulo", situacaomoduloDao.find(id));
		return "situacaomodulo/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("situacaomodulo") @Valid SituacaoModulo situacaomodulo) {
		situacaomoduloDao.merge(situacaomodulo);
		return "redirect:/situacaomodulo";
	}	

}
