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

import br.com.siged.dao.TipoAreaTribunalDAO;
import br.com.siged.entidades.TipoAreaTribunal;

@Controller
@RequestMapping("/tipoareatribunal/**")
public class TipoAreaTribunalController {
	@Autowired
	private TipoAreaTribunalDAO tipoareatribunalDao;

	@RequestMapping(value = "/tipoareatribunal/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("tipoareatribunal", tipoareatribunalDao.find(id));
		return "tipoareatribunal/show";
	}	

	@RequestMapping(value = "/tipoareatribunal", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("tiposareatribunal", tipoareatribunalDao.findAll());
		return "tipoareatribunal/list";
	}

	@RequestMapping(value = "/tipoareatribunal/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		tipoareatribunalDao.remove(tipoareatribunalDao.find(id));
		return "redirect:/tipoareatribunal";
	}

	@RequestMapping(value = "/tipoareatribunal/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("tipoareatribunal", new TipoAreaTribunal());
		return "tipoareatribunal/create";
	}

	@RequestMapping(value = "/tipoareatribunal", method = RequestMethod.POST)
	public String create(@ModelAttribute("tipoareatribunal") @Valid TipoAreaTribunal tipoareatribunal, BindingResult result) {
		
		if (result.hasErrors()) {  
			return "tipoareatribunal/create";
        }  
		
		tipoareatribunalDao.persist(tipoareatribunal);
		return "redirect:/tipoareatribunal";
	}

	@RequestMapping(value = "/tipoareatribunal/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("tipoareatribunal", tipoareatribunalDao.find(id));
		return "tipoareatribunal/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("tipoareatribunal") @Valid TipoAreaTribunal tipoareatribunal) {
		tipoareatribunalDao.merge(tipoareatribunal);
		return "redirect:/tipoareatribunal";
	}	

}
