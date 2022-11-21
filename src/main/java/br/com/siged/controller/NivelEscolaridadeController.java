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

import br.com.siged.dao.NivelEscolaridadeDAO;
import br.com.siged.entidades.NivelEscolaridade;

@Controller
@RequestMapping("/nivelescolaridade/**")
public class NivelEscolaridadeController {
	@Autowired
	private NivelEscolaridadeDAO nivelescolaridadeDao;

	@RequestMapping(value = "/nivelescolaridade/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("nivelescolaridade", nivelescolaridadeDao.find(id));
		return "nivelescolaridade/show";
	}	

	@RequestMapping(value = "/nivelescolaridade", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("niveisescolaridade", nivelescolaridadeDao.findAll());
		return "nivelescolaridade/list";
	}

	@RequestMapping(value = "/nivelescolaridade/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		nivelescolaridadeDao.remove(nivelescolaridadeDao.find(id));
		return "redirect:/nivelescolaridade";
	}

	@RequestMapping(value = "/nivelescolaridade/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("nivelescolaridade", new NivelEscolaridade());
		return "nivelescolaridade/create";
	}

	@RequestMapping(value = "/nivelescolaridade", method = RequestMethod.POST)
	public String create(@ModelAttribute("nivelescolaridade") @Valid NivelEscolaridade nivelescolaridade, BindingResult result) {
		
		if (result.hasErrors()) {  
			return "nivelescolaridade/create";
        }  
		
		nivelescolaridadeDao.persist(nivelescolaridade);
		return "redirect:/nivelescolaridade";
	}

	@RequestMapping(value = "/nivelescolaridade/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("nivelescolaridade", nivelescolaridadeDao.find(id));
		return "nivelescolaridade/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("nivelescolaridade") @Valid NivelEscolaridade nivelescolaridade) {
		nivelescolaridadeDao.merge(nivelescolaridade);
		return "redirect:/nivelescolaridade";
	}	

}
