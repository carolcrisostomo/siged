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

import br.com.siged.dao.FormacaoAcademicaDAO;
import br.com.siged.entidades.FormacaoAcademica;

@Controller
@RequestMapping("/formacaoacademica/**")
public class FormacaoAcademicaController {
	@Autowired
	private FormacaoAcademicaDAO formacaoacademicaDao;

	@RequestMapping(value = "/formacaoacademica/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("formacaoacademica", formacaoacademicaDao.find(id));
		return "formacaoacademica/show";
	}	

	@RequestMapping(value = "/formacaoacademica", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("formacoesacademicas", formacaoacademicaDao.findAll());
		return "formacaoacademica/list";
	}

	@RequestMapping(value = "/formacaoacademica/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		try {
			formacaoacademicaDao.remove(formacaoacademicaDao.find(id));
			model.addAttribute("mensagem","Excluído com sucesso!");
		} catch (Exception e) {
			model.addAttribute("mensagem","O registro não pode ser excluído pois está em uso.");
		}
		
		return "redirect:/formacaoacademica";
	}

	@RequestMapping(value = "/formacaoacademica/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("formacaoacademica", new FormacaoAcademica());
		return "formacaoacademica/create";
	}

	@RequestMapping(value = "/formacaoacademica", method = RequestMethod.POST)
	public String create(@ModelAttribute("formacaoacademica") @Valid FormacaoAcademica formacaoacademica, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {  
			return "formacaoacademica/create";
        }  
		model.addAttribute("mensagem","Incluído com sucesso!");
		formacaoacademicaDao.persist(formacaoacademica);
		return "redirect:/formacaoacademica";
	}

	@RequestMapping(value = "/formacaoacademica/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("formacaoacademica", formacaoacademicaDao.find(id));
		return "formacaoacademica/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("formacaoacademica") @Valid FormacaoAcademica formacaoacademica, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
			return "formacaoacademica/update";
		}
		model.addAttribute("mensagem","Alterado com sucesso!");
		formacaoacademicaDao.merge(formacaoacademica);
		return "redirect:/formacaoacademica";
	}	

}