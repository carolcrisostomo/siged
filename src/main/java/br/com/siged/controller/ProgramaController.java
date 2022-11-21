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

import br.com.siged.dao.ProgramaDAO;
import br.com.siged.entidades.Programa;

@Controller
@RequestMapping("/programa/**")
public class ProgramaController {

	@Autowired
	private ProgramaDAO programaDao;
	
	@RequestMapping(value = "/programa/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("programa", programaDao.findById(id));
		return "programa/show";
	}
	
	@RequestMapping(value = "/programa", method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("programas", programaDao.findAll());
		return "programa/list";
	}
	
	@RequestMapping(value = "/programa/form", method = RequestMethod.GET)
	public String form(ModelMap model) {
		model.addAttribute("programa", new Programa());
		return "programa/create";
	}
	
	@RequestMapping(value = "/programa", method = RequestMethod.POST)
	public String create(@ModelAttribute("programa") @Valid Programa programa, BindingResult result, ModelMap model) {
		
		if(result.hasErrors()) {
			return "programa/create";
		}
		
		model.addAttribute("mensagem", "Incluído com sucesso!");
		programaDao.persist(programa);
		
		return "redirect:/programa";
	}
	
	@RequestMapping(value = "/programa/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("programa", programaDao.findById(id));
		return "programa/update";
	}
	
	@RequestMapping(value = "/programa/{id}", method = RequestMethod.PUT)
	public String update(@ModelAttribute("programa") @Valid Programa programa, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			return "programa/update";
		}
		model.addAttribute("mensagem", "Alterado com sucesso!");
		programaDao.merge(programa);
		return "redirect:/programa";
	}
	
	@RequestMapping(value = "/programa/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		Programa programa = programaDao.findById(id);
		if(programa != null) {
			try {
				programaDao.remove(programa);
				model.addAttribute("mensagem", "Excluído com sucesso!");
			} catch (Exception e) {
				model.addAttribute("mensagemErro", "O registro não pode ser excluído pois está em uso.");
			}
		} else {
			model.addAttribute("mensagemErro", "Registro não encontrado!");
		}
		
		return "redirect:/programa";
	}
}
