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

import br.com.siged.dao.SituacaoInstrutorDAO;
import br.com.siged.entidades.SituacaoInstrutor;

@Controller
@RequestMapping("/situacaoinstrutor/**")
public class SituacaoInstrutorController {
	@Autowired
	private SituacaoInstrutorDAO situacaoinstrutorDao;

	@RequestMapping(value = "/situacaoinstrutor/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("situacaoinstrutor", situacaoinstrutorDao.find(id));
		return "situacaoinstrutor/show";
	}	

	@RequestMapping(value = "/situacaoinstrutor", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("situacoesinstrutores", situacaoinstrutorDao.findAll());
		return "situacaoinstrutor/list";
	}

	@RequestMapping(value = "/situacaoinstrutor/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		situacaoinstrutorDao.remove(situacaoinstrutorDao.find(id));
		return "redirect:/situacaoinstrutor";
	}

	@RequestMapping(value = "/situacaoinstrutor/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("situacaoinstrutor", new SituacaoInstrutor());
		return "situacaoinstrutor/create";
	}

	@RequestMapping(value = "/situacaoinstrutor", method = RequestMethod.POST)
	public String create(@ModelAttribute("situacaoinstrutor") @Valid SituacaoInstrutor situacaoinstrutor, BindingResult result) {
		
		if (result.hasErrors()) {  
			return "situacaoinstrutor/create";
        }  
		
		situacaoinstrutorDao.persist(situacaoinstrutor);
		return "redirect:/situacaoinstrutor";
	}

	@RequestMapping(value = "/situacaoinstrutor/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("situacaoinstrutor", situacaoinstrutorDao.find(id));
		return "situacaoinstrutor/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("situacaoinstrutor") @Valid SituacaoInstrutor situacaoinstrutor) {
		situacaoinstrutorDao.merge(situacaoinstrutor);
		return "redirect:/situacaoinstrutor";
	}	

}
