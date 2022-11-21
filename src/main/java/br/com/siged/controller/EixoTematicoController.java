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

import br.com.siged.dao.EixoTematicoDAO;
import br.com.siged.entidades.EixoTematico;

@Controller
@RequestMapping("/eixotematico/**")
public class EixoTematicoController {
	@Autowired
	private EixoTematicoDAO eixotematicoDao;

	@RequestMapping(value = "/eixotematico/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("eixotematico", eixotematicoDao.find(id));
		return "eixotematico/show";
	}	

	@RequestMapping(value = "/eixotematico", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("eixostematicos", eixotematicoDao.findAll());
		return "eixotematico/list";
	}

	@RequestMapping(value = "/eixotematico/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		try {
			eixotematicoDao.remove(eixotematicoDao.find(id));
			model.addAttribute("mensagem","Excluído com sucesso!");
		} catch (Exception e) {
			model.addAttribute("mensagem","O registro não pode ser excluído pois está em uso.");
		}
		
		return "redirect:/eixotematico";
	}

	@RequestMapping(value = "/eixotematico/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("eixotematico", new EixoTematico());
		return "eixotematico/create";
	}

	@RequestMapping(value = "/eixotematico", method = RequestMethod.POST)
	public String create(@ModelAttribute("eixotematico") @Valid EixoTematico eixotematico, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {  
			return "eixotematico/create";
        }
		model.addAttribute("mensagem","Incluído com sucesso!");
		eixotematicoDao.persist(eixotematico);
		return "redirect:/eixotematico";
	}

	@RequestMapping(value = "/eixotematico/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("eixotematico", eixotematicoDao.find(id));
		return "eixotematico/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("eixotematico") @Valid EixoTematico eixotematico, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {  
			return "eixotematico/update";
        }
		model.addAttribute("mensagem","Alterado com sucesso!");
		eixotematicoDao.merge(eixotematico);
		return "redirect:/eixotematico";
	}	

}
