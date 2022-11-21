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

import br.com.siged.dao.TipoEventoDAO;
import br.com.siged.entidades.TipoEvento;

@Controller
@RequestMapping("/tipoevento/**")
public class TipoEventoController {
	@Autowired
	private TipoEventoDAO tipoeventoDao;

	@RequestMapping(value = "/tipoevento/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("tipoevento", tipoeventoDao.find(id));
		return "tipoevento/show";
	}	

	@RequestMapping(value = "/tipoevento", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("tiposeventos", tipoeventoDao.findAll());
		return "tipoevento/list";
	}

	@RequestMapping(value = "/tipoevento/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		try {
			tipoeventoDao.remove(tipoeventoDao.find(id));
			model.addAttribute("mensagem","Excluído com sucesso!");
		} catch (Exception e) {
			model.addAttribute("mensagem","O registro não pode ser excluído pois está em uso.");
		}
		
		return "redirect:/tipoevento";
	}

	@RequestMapping(value = "/tipoevento/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("tipoevento", new TipoEvento());
		return "tipoevento/create";
	}

	@RequestMapping(value = "/tipoevento", method = RequestMethod.POST)
	public String create(@ModelAttribute("tipoevento") @Valid TipoEvento tipoevento, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {  
			return "tipoevento/create";
        }  
		model.addAttribute("mensagem","Incluído com sucesso!");
		tipoeventoDao.persist(tipoevento);
		return "redirect:/tipoevento";
	}

	@RequestMapping(value = "/tipoevento/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("tipoevento", tipoeventoDao.find(id));
		return "tipoevento/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("tipoevento") @Valid TipoEvento tipoevento, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
			return "tipoevento/update";
		}
		model.addAttribute("mensagem","Alterado com sucesso!");
		tipoeventoDao.merge(tipoevento);
		return "redirect:/tipoevento";
	}	

}