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

import br.com.siged.dao.PaisDAO;
import br.com.siged.entidades.Pais;

@Controller
@RequestMapping("/pais/**")
public class PaisController {
	@Autowired
	private PaisDAO paisDao;

	@RequestMapping(value = "/pais/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("pais", paisDao.find(id));
		return "pais/show";
	}	

	@RequestMapping(value = "/pais", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("paises", paisDao.findAll());
		return "pais/list";
	}

	@RequestMapping(value = "/pais/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		try {
			paisDao.remove(paisDao.find(id));
			model.addAttribute("mensagem","Excluido com sucesso!");
		} catch (Exception e) {
			model.addAttribute("mensagem","O registro não pode ser excluído pois está em uso.");
		}
		return "redirect:/pais";
	}

	@RequestMapping(value = "/pais/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("pais", new Pais());
		return "pais/create";
	}

	@RequestMapping(value = "/pais", method = RequestMethod.POST)
	public String create(@ModelAttribute("pais") @Valid Pais pais, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {  
			return "pais/create";
        }  
		model.addAttribute("mensagem","Incluído com sucesso!");
		paisDao.persist(pais);
		return "redirect:/pais";
	}

	@RequestMapping(value = "/pais/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("pais", paisDao.find(id));
		return "pais/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("pais") @Valid Pais pais, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
			return "pais/update";
		}
		model.addAttribute("mensagem","Alterado com sucesso!");
		paisDao.merge(pais);
		return "redirect:/pais";
	}	

}