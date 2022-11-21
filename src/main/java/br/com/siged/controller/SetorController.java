package br.com.siged.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.siged.dao.SetorDAO;

@Controller
@RequestMapping("/setor/**")
public class SetorController {
	@Autowired
	private SetorDAO setorDao;

	@RequestMapping(value = "/setor/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("setor", setorDao.find(id));
		return "setor/show";
	}	

	@RequestMapping(value = "/setor", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("setores", setorDao.findAll());
		return "setor/list";
	}

}
