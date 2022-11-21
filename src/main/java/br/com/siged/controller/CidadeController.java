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

import br.com.siged.dao.CidadeDAO;
import br.com.siged.dao.UfDAO;
import br.com.siged.editor.UfEditor;
import br.com.siged.entidades.Cidade;
import br.com.siged.entidades.Uf;

@Controller
@RequestMapping("/cidade/**")
public class CidadeController {
	@Autowired
	private CidadeDAO cidadeDao;
	@Autowired
	private UfDAO ufDao;
	
	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(br.com.siged.entidades.Uf.class, new UfEditor(ufDao));
     
    }

	@RequestMapping(value = "/cidade/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("cidade", cidadeDao.find(id));
		return "cidade/show";
	}	

	@RequestMapping(value = "/cidade", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("cidades", cidadeDao.findAll());
		return "cidade/list";
	}

	@RequestMapping(value = "/cidade/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		try {
			cidadeDao.remove(cidadeDao.find(id));
			model.addAttribute("mensagem","Excluido com sucesso!");
		} catch (Exception e) {
			model.addAttribute("mensagem","O registro não pode ser excluído pois está em uso.");
		}
		return "redirect:/cidade";
	}

	@RequestMapping(value = "/cidade/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("cidade", new Cidade());
		return "cidade/create";
	}

	@RequestMapping(value = "/cidade", method = RequestMethod.POST)
	public String create(@ModelAttribute("cidade") @Valid Cidade cidade, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {  
			return "cidade/create";
        }  
		model.addAttribute("mensagem","Incluído com sucesso!");
		cidadeDao.persist(cidade);
		return "redirect:/cidade";
	}

	@RequestMapping(value = "/cidade/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("cidade", cidadeDao.find(id));
		return "cidade/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("cidade") @Valid Cidade cidade, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {  
			return "cidade/update";
        }
		
		model.addAttribute("mensagem","Alterado com sucesso!");
		cidadeDao.merge(cidade);
		return "redirect:/cidade";
	}
	
	@ModelAttribute("listaUfs")
	public Collection<Uf> populateUfs() {
        return ufDao.findAll();
    }

}
