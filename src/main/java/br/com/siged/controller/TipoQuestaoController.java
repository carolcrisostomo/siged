package br.com.siged.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.siged.dao.TipoQuestaoDAO;
import br.com.siged.editor.TipoQuestaoEditor;

@Controller
@RequestMapping("/tipoquestao/**")
public class TipoQuestaoController {
	@Autowired
	private TipoQuestaoDAO tipoquestaoDao;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder){
		dataBinder.registerCustomEditor(br.com.siged.entidades.TipoQuestao.class, new TipoQuestaoEditor(tipoquestaoDao));
	}
	
	@RequestMapping(value = "/tipoquestao", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("tiposquestoes", tipoquestaoDao.findAll());
		return "tipoquestao/list";
	}
	
	/*@RequestMapping(value = "/tipoquestao/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("tipoquestao", new TipoQuestao());
		return "tipoquestao/create";
	}
	
	@RequestMapping(value = "/tipoquestao", method = RequestMethod.POST)
	public String create(@ModelAttribute("tipoquestao") @Valid TipoQuestao tipoquestao, BindingResult result) {
		
		if (result.hasErrors()) {  
			return "tipoquestao/create";
        }  
		
		tipoquestaoDao.persist(tipoquestao);
		return "redirect:/tipoquestao";
	}*/
	
	
	@RequestMapping(value = "/tipoquestao/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("tipoquestao", tipoquestaoDao.find(id));
		return "tipoquestao/show";
	}
	
	/*@RequestMapping(value = "/tipoquestao/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		tipoquestaoDao.remove(tipoquestaoDao.find(id));
		return "redirect:/tipoquestao";
	}*/
	
	/*@RequestMapping(value = "/tipoquestao/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("tipoquestao", tipoquestaoDao.find(id));
		return "tipoquestao/update";
	}*/

	/*@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("tipoquestao") @Valid TipoQuestao tipoquestao, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			return "tipoquestao/update";
		}
		tipoquestaoDao.merge(tipoquestao);
		model.addAttribute("mensagem","Alterado com sucesso!");
		return "redirect:/tipoquestao";
	}*/
	
}
