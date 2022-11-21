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

import br.com.siged.dao.TipoGastoDAO;
import br.com.siged.entidades.TipoGasto;

@Controller
@RequestMapping("/tipogasto/**")
public class TipoGastoController {
	@Autowired
	private TipoGastoDAO tipogastoDao;

	@RequestMapping(value = "/tipogasto/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("tipogasto", tipogastoDao.find(id));
		return "tipogasto/show";
	}	

	@RequestMapping(value = "/tipogasto", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("tiposgasto", tipogastoDao.findAll());
		return "tipogasto/list";
	}

	@RequestMapping(value = "/tipogasto/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		try {
			TipoGasto tipoGasto = tipogastoDao.findById(id);
			if(tipoGasto.isInstrutorInterno() || tipoGasto.isInstrutorExterno()) {
				model.addAttribute("mensagem","Tipo de gasto Instrutor Interno e Externo não podem ser excluídos!");
				return "redirect:/tipogasto";
			}
			tipogastoDao.remove(tipoGasto);
			model.addAttribute("mensagem","Excluído com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("mensagem","O registro não pode ser excluído pois está em uso.");
		}
		
		return "redirect:/tipogasto";
	}

	@RequestMapping(value = "/tipogasto/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("tipogasto", new TipoGasto());
		return "tipogasto/create";
	}

	@RequestMapping(value = "/tipogasto", method = RequestMethod.POST)
	public String create(@ModelAttribute("tipogasto") @Valid TipoGasto tipogasto, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {  
			return "tipogasto/create";
        }  
		model.addAttribute("mensagem","Incluído com sucesso!");
		tipogastoDao.persist(tipogasto);
		return "redirect:/tipogasto";
	}

	@RequestMapping(value = "/tipogasto/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("tipogasto", tipogastoDao.find(id));
		return "tipogasto/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("tipogasto") @Valid TipoGasto tipogasto, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
			return "tipogasto/update";
		}
		model.addAttribute("mensagem","Alterado com sucesso!");
		tipogastoDao.merge(tipogasto);
		return "redirect:/tipogasto";
	}

}