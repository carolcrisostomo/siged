package br.com.siged.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.siged.dao.TipoInstrutorDAO;

@Controller
@RequestMapping("/tipoinstrutor/**")
public class TipoInstrutorController {
	@Autowired
	private TipoInstrutorDAO tipoinstrutorDao;

	@RequestMapping(value = "/tipoinstrutor/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("tipoinstrutor", tipoinstrutorDao.find(id));
		return "tipoinstrutor/show";
	}	

	@RequestMapping(value = "/tipoinstrutor", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("tiposinstrutor", tipoinstrutorDao.findAll());
		return "tipoinstrutor/list";
	}

	/*
	@RequestMapping(value = "/tipoinstrutor/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		try {
			tipoinstrutorDao.remove(tipoinstrutorDao.find(id));
			model.addAttribute("mensagem","Excluído com sucesso!");
		} catch (Exception e) {
			model.addAttribute("mensagem","Registro em uso!");
		}
		return "redirect:/tipoinstrutor";
	}*/

	/*
	@RequestMapping(value = "/tipoinstrutor/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("tipoinstrutor", new TipoInstrutor());
		return "tipoinstrutor/create";
	}*/

	/*
	@RequestMapping(value = "/tipoinstrutor", method = RequestMethod.POST)
	public String create(@ModelAttribute("tipoinstrutor") @Valid TipoInstrutor tipoinstrutor, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {  
			return "tipoinstrutor/create";
        }  
		model.addAttribute("mensagem","Incluído com sucesso!");
		tipoinstrutorDao.persist(tipoinstrutor);
		return "redirect:/tipoinstrutor";
	}*/

	/*
	@RequestMapping(value = "/tipoinstrutor/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("tipoinstrutor", tipoinstrutorDao.find(id));
		return "tipoinstrutor/update";
	}*/

	/*
	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("tipoinstrutor") @Valid TipoInstrutor tipoinstrutor, BindingResult result, ModelMap model) {

		if (result.hasErrors()) {
			return "tipoinstrutor/update";
		}
		model.addAttribute("mensagem","Alterado com sucesso!");
		tipoinstrutorDao.merge(tipoinstrutor);
		return "redirect:/tipoinstrutor";
	}*/

}