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

import br.com.siged.dao.ModalidadeDAO;
import br.com.siged.entidades.Modalidade;

@Controller
@RequestMapping("/modalidade/**")
public class ModalidadeController {
	@Autowired
	private ModalidadeDAO modalidadeDao;

	@RequestMapping(value = "/modalidade/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("modalidade", modalidadeDao.find(id));
		return "modalidade/show";
	}	

	@RequestMapping(value = "/modalidade", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("modalidades", modalidadeDao.findAll());
		return "modalidade/list";
	}

	@RequestMapping(value = "/modalidade/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		modalidadeDao.remove(modalidadeDao.find(id));
		return "redirect:/modalidade";
	}

	@RequestMapping(value = "/modalidade/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("modalidade", new Modalidade());
		return "modalidade/create";
	}

	@RequestMapping(value = "/modalidade", method = RequestMethod.POST)
	public String create(@ModelAttribute("modalidade") @Valid Modalidade modalidade, BindingResult result) {
		
		if (result.hasErrors()) {  
			return "modalidade/create";
        }  
		
		modalidadeDao.persist(modalidade);
		return "redirect:/modalidade";
	}

	@RequestMapping(value = "/modalidade/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("modalidade", modalidadeDao.find(id));
		return "modalidade/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("modalidade") @Valid Modalidade modalidade) {
		modalidadeDao.merge(modalidade);
		return "redirect:/modalidade";
	}	

}
