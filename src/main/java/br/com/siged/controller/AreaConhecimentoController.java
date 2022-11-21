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

import br.com.siged.dao.AreaConhecimentoDAO;
import br.com.siged.entidades.AreaConhecimento;

@Controller
@RequestMapping("/areaconhecimento/**")
public class AreaConhecimentoController{
	@Autowired
	private AreaConhecimentoDAO areaconhecimentoDao;
	
	@RequestMapping(value = "/areaconhecimento/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("areaconhecimento", areaconhecimentoDao.find(id));
		return "areaconhecimento/show";
	}	

	@RequestMapping(value = "/areaconhecimento", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("areasconhecimento", areaconhecimentoDao.findAll());
		return "areaconhecimento/list";
	}

	@RequestMapping(value = "/areaconhecimento/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		try {
			areaconhecimentoDao.remove(areaconhecimentoDao.find(id));
			model.addAttribute("mensagem","Excluido com sucesso!");
		} catch (Exception e) {
			model.addAttribute("mensagem","O registro não pode ser excluído pois está em uso.");
		}
		return "redirect:/areaconhecimento";
	}

	@RequestMapping(value = "/areaconhecimento/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("areaconhecimento", new AreaConhecimento());
		return "areaconhecimento/create";
	}

	@RequestMapping(value = "/areaconhecimento", method = RequestMethod.POST)
	public String create(@ModelAttribute("areaconhecimento") @Valid AreaConhecimento areaconhecimento, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {  
			return "areaconhecimento/create";
        }  
		model.addAttribute("mensagem","Incluído com sucesso!");
		areaconhecimentoDao.persist(areaconhecimento);
		return "redirect:/areaconhecimento";
	}

	@RequestMapping(value = "/areaconhecimento/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("areaconhecimento", areaconhecimentoDao.find(id));
		
		return "areaconhecimento/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("areaconhecimento") @Valid AreaConhecimento areaconhecimento, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {  
			return "areaconhecimento/update";
        }  
		model.addAttribute("mensagem","Alterado com sucesso!");
		areaconhecimentoDao.merge(areaconhecimento);
		return "redirect:/areaconhecimento";
	}
}
