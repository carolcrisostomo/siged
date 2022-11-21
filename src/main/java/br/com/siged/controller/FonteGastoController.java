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

import br.com.siged.dao.FonteGastoDAO;
import br.com.siged.entidades.FonteGasto;

@Controller
@RequestMapping("/fontegasto/**")
public class FonteGastoController {
	@Autowired
	private FonteGastoDAO fontegastoDao;

	@RequestMapping(value = "/fontegasto/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("fontegasto", fontegastoDao.find(id));
		return "fontegasto/show";
	}	

	@RequestMapping(value = "/fontegasto", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("fontegastos", fontegastoDao.findAll());
		return "fontegasto/list";
	}

	@RequestMapping(value = "/fontegasto/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		try {
			fontegastoDao.remove(fontegastoDao.find(id));
			model.addAttribute("mensagem","Excluído com sucesso!");
		} catch (Exception e) {
			model.addAttribute("mensagem","O registro não pode ser excluído pois está em uso.");
		}
		
		return "redirect:/fontegasto";
	}

	@RequestMapping(value = "/fontegasto/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("fontegasto", new FonteGasto());
		return "fontegasto/create";
	}

	@RequestMapping(value = "/fontegasto", method = RequestMethod.POST)
	public String create(@ModelAttribute("fontegasto") @Valid FonteGasto fontegasto, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {  
			return "fontegasto/create";
        }  
		model.addAttribute("mensagem","Incluído com sucesso!");
		fontegastoDao.persist(fontegasto);
		return "redirect:/fontegasto";
	}

	@RequestMapping(value = "/fontegasto/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("fontegasto", fontegastoDao.find(id));
		return "fontegasto/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("fontegasto") @Valid FonteGasto fontegasto, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
			return "fontegasto/update";
		}
		model.addAttribute("mensagem","Alterado com sucesso!");
		fontegastoDao.merge(fontegasto);
		return "redirect:/fontegasto";
	}	

}