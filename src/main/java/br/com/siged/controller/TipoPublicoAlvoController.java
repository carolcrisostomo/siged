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

import br.com.siged.dao.TipoPublicoAlvoDAO;
import br.com.siged.entidades.TipoPublicoAlvo;

@Controller
@RequestMapping("/tipopublicoalvo/**")
public class TipoPublicoAlvoController {
	@Autowired
	private TipoPublicoAlvoDAO tipopublicoalvoDao;

	@RequestMapping(value = "/tipopublicoalvo/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("tipopublicoalvo", tipopublicoalvoDao.find(id));
		return "tipopublicoalvo/show";
	}	

	@RequestMapping(value = "/tipopublicoalvo", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("tipospublicoalvo", tipopublicoalvoDao.findAll());
		return "tipopublicoalvo/list";
	}

	@RequestMapping(value = "/tipopublicoalvo/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		tipopublicoalvoDao.remove(tipopublicoalvoDao.find(id));
		return "redirect:/tipopublicoalvo";
	}

	@RequestMapping(value = "/tipopublicoalvo/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("tipopublicoalvo", new TipoPublicoAlvo());
		return "tipopublicoalvo/create";
	}

	@RequestMapping(value = "/tipopublicoalvo", method = RequestMethod.POST)
	public String create(@ModelAttribute("tipopublicoalvo") @Valid TipoPublicoAlvo tipopublicoalvo, BindingResult result) {
		
		if (result.hasErrors()) {  
			return "tipopublicoalvo/create";
        }  
		
		tipopublicoalvoDao.persist(tipopublicoalvo);
		return "redirect:/tipopublicoalvo";
	}

	@RequestMapping(value = "/tipopublicoalvo/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("tipopublicoalvo", tipopublicoalvoDao.find(id));
		return "tipopublicoalvo/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("tipopublicoalvo") @Valid TipoPublicoAlvo tipopublicoalvo) {
		tipopublicoalvoDao.merge(tipopublicoalvo);
		return "redirect:/tipopublicoalvo";
	}	

}
