package br.com.siged.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.siged.dao.ModalidadeDAO;
import br.com.siged.dao.QuestaoDAO;
import br.com.siged.dao.TipoQuestaoDAO;
import br.com.siged.editor.ModalidadeEditor;
import br.com.siged.editor.QuestaoEditor;
import br.com.siged.editor.TipoQuestaoEditor;
import br.com.siged.entidades.Modalidade;
import br.com.siged.entidades.Questao;
import br.com.siged.entidades.TipoQuestao;

@Controller
@RequestMapping("/questao/**")
public class QuestaoController {
	@Autowired
	private QuestaoDAO questaoDao;
	@Autowired
	private TipoQuestaoDAO tipoquestaoDao;
	@Autowired
	private ModalidadeDAO modalidadeDao;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder){
		dataBinder.registerCustomEditor(br.com.siged.entidades.Questao.class, new QuestaoEditor(questaoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.TipoQuestao.class, new TipoQuestaoEditor(tipoquestaoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Modalidade.class, new ModalidadeEditor(modalidadeDao));
	}
	
	@RequestMapping(value = "/questao", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("questoes", questaoDao.findAll());
		return "questao/list";
	}
	
	@RequestMapping(value = "/questao/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("questao", new Questao());
		return "questao/create";
	}
	
	@RequestMapping(value = "/questao", method = RequestMethod.POST)
	public String create(@ModelAttribute("questao") @Valid Questao questao, BindingResult result) {
		
		if (result.hasErrors()) {  
			return "questao/create";
        }  
		
		questaoDao.persist(questao);
		return "redirect:/questao";
	}
	
	@RequestMapping(value = "/questao/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("questao", questaoDao.find(id));
		return "questao/show";
	}
	
	@RequestMapping(value = "/questao/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		try {
			questaoDao.remove(questaoDao.find(id));
			model.addAttribute("mensagem","Excluído com sucesso!");
		} catch (Exception e) {
			model.addAttribute("mensagem","O registro não pode ser excluído pois está em uso.");
		}
		return "redirect:/questao";
	}
	
	@RequestMapping(value = "/questao/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("questao", questaoDao.find(id));
		return "questao/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("questao") @Valid Questao questao, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "questao/update";
		}
		model.addAttribute("mensagem","Alterado com sucesso!");
		questaoDao.merge(questao);
		return "redirect:/questao";
	}
	
	@ModelAttribute("listaModalidades")
	public Collection<Modalidade> populateModalidades(){
		return modalidadeDao.findAll();
	}
	
	@ModelAttribute("listaTiposQuestoes")
	public Collection<TipoQuestao> populateTiposQuestoes() {
        return tipoquestaoDao.findAll();
    }

}
