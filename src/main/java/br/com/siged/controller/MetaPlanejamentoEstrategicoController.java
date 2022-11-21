package br.com.siged.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.displaytag.pagination.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.siged.controller.validators.MetaPlanejamentoEstrategicoValidator;
import br.com.siged.dao.MetaPlanejamentoEstrategicoDAO;
import br.com.siged.dao.pagination.DisplayTagPageable;
import br.com.siged.entidades.MetaPlanejamentoEstrategico;
import br.com.siged.filtro.MetaPlanejamentoEstrategicoFiltro;
import br.com.siged.service.MetaPlanejamentoEstrategicoService;
import br.com.siged.service.exception.HttpStatusException;

@Controller
@RequestMapping("/meta/planejamentoEstrategico/**")
public class MetaPlanejamentoEstrategicoController {

	
	@Autowired
	private MetaPlanejamentoEstrategicoDAO metaPlanejamentoEstrategicoDao;
	
	@Autowired
	private MetaPlanejamentoEstrategicoValidator metaPlanejamentoEstrategicoValidator ;
	
	@Autowired
	private MetaPlanejamentoEstrategicoService metaPlanejamentoEstrategicoService;
	
	private DecimalFormat decimalFormat = new DecimalFormat("#00.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, this.decimalFormat, true));
		if(dataBinder.getTarget() instanceof MetaPlanejamentoEstrategico) {
        	dataBinder.setValidator(metaPlanejamentoEstrategicoValidator);
        }
	}
	
	@RequestMapping(value = "/meta/planejamentoEstrategico", method = RequestMethod.GET)
	public String list(ModelMap modelMap, HttpServletRequest request, ServletResponse Response) {
		return search(new MetaPlanejamentoEstrategicoFiltro(), modelMap, request, Response);
	}
	
	@RequestMapping(value = "/meta/planejamentoEstrategico/search", method = RequestMethod.GET)
	public String search(MetaPlanejamentoEstrategicoFiltro filtro, ModelMap modelMap, HttpServletRequest request, ServletResponse Response) {
		
		DisplayTagPageable pageable = new DisplayTagPageable(request);
		PaginatedList displayTagList = metaPlanejamentoEstrategicoDao.filtrar(filtro, pageable);
		
		modelMap.addAttribute("filtro", filtro);
		modelMap.addAttribute("metas", displayTagList);
		
		return "metaPlanejamentoEstrategico/list";
	}
	
	@RequestMapping(value = "/meta/planejamentoEstrategico/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		MetaPlanejamentoEstrategico meta = metaPlanejamentoEstrategicoDao.find(id);
		
		if (meta == null) {
			throw new HttpStatusException(HttpStatus.NOT_FOUND, "Meta não encontrada");
		}
		
		modelMap.addAttribute("meta", meta);
		return "metaPlanejamentoEstrategico/show";
	}
	
	@RequestMapping(value = "/meta/planejamentoEstrategico/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("meta", new MetaPlanejamentoEstrategico());
		return "metaPlanejamentoEstrategico/create";
	}
	
	@RequestMapping(value = "/meta/planejamentoEstrategico/", method = RequestMethod.POST)
	public String create(@ModelAttribute("meta") @Valid MetaPlanejamentoEstrategico meta, BindingResult result, ModelMap modelMap) {
		
		if (result.hasErrors()) {
			modelMap.addAttribute("meta", meta);
			return "metaPlanejamentoEstrategico/create";
        }		
		
		metaPlanejamentoEstrategicoService.salvar(meta);
		
		return "redirect:/meta/planejamentoEstrategico?messageSucesso=Meta dos Indicadores do Planejamento Estratégico salva com sucesso";
	}
	
	@RequestMapping(value = "/meta/planejamentoEstrategico/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		
		MetaPlanejamentoEstrategico meta = metaPlanejamentoEstrategicoDao.find(id);
		
		if (meta == null) {
			throw new HttpStatusException(HttpStatus.NOT_FOUND, "Meta não encontrada");
		}
		
		modelMap.addAttribute("meta", meta);
		
		return "metaPlanejamentoEstrategico/update";
	}
	
	@RequestMapping(value = "/meta/planejamentoEstrategico/{id}", method = RequestMethod.PUT)
	public String update(@PathVariable("id") Long id, @ModelAttribute("meta") @Valid MetaPlanejamentoEstrategico meta, BindingResult result, ModelMap modelMap) {
		MetaPlanejamentoEstrategico metaDoBanco = metaPlanejamentoEstrategicoDao.find(id);
		
		if (metaDoBanco == null) {
			throw new HttpStatusException(HttpStatus.NOT_FOUND, "Meta não encontrada");
		}
		
		if (result.hasErrors()) {
			modelMap.addAttribute("meta", meta);
			
			return "metaPlanejamentoEstrategico/update";
        }

		metaPlanejamentoEstrategicoService.salvar(meta);
		
		return "redirect:/meta/planejamentoEstrategico?messageSucesso=Meta dos Indicadores do Planejamento Estratégico atualizada com sucesso";
	}
	
	@RequestMapping(value = "/meta/planejamentoEstrategico/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		MetaPlanejamentoEstrategico metaDoBanco = metaPlanejamentoEstrategicoDao.find(id);
		
		if (metaDoBanco == null) {
			throw new HttpStatusException(HttpStatus.NOT_FOUND, "Meta não encontrada");
		}
		
		String message = "Meta excluída com sucesso";
		try{
			metaPlanejamentoEstrategicoService.excluir(metaDoBanco);
		}catch (Exception e){
			e.printStackTrace();
			message = "Não foi possível excluir a Meta";
		}
		
		return "redirect:/meta/planejamentoEstrategico?messageSucesso=" + message;
	}
	
	@ModelAttribute("anosList")
	public Collection<Integer> anos() {
		int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
		List<Integer> anos = new ArrayList<>();
		for(int i = 2012; i <= anoAtual; i++) {
			anos.add(i);
		}
        return anos;
    }
}
