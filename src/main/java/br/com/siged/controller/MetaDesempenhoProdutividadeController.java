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

import br.com.siged.controller.validators.MetaDesempenhoProdutividadeValidator;
import br.com.siged.dao.MetaDesempenhoProdutividadeDAO;
import br.com.siged.dao.pagination.DisplayTagPageable;
import br.com.siged.entidades.MetaDesempenhoProdutividade;
import br.com.siged.filtro.MetaDesempenhoProdutividadeFiltro;
import br.com.siged.service.MetaDesempenhoProdutividadeService;
import br.com.siged.service.exception.HttpStatusException;

@Controller
@RequestMapping("/meta/desempenhoProdutividade/**")
public class MetaDesempenhoProdutividadeController {

	@Autowired
	private MetaDesempenhoProdutividadeDAO metaDesempenhoProdutividadeDao;
	
	@Autowired
	private MetaDesempenhoProdutividadeValidator metaDesempenhoProdutividadeValidator;
	
	@Autowired
	private MetaDesempenhoProdutividadeService metaDesempenhoProdutividadeService;
	
	private DecimalFormat decimalFormat = new DecimalFormat("#00.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, this.decimalFormat, true));
		if(dataBinder.getTarget() instanceof MetaDesempenhoProdutividade) {
        	dataBinder.setValidator(metaDesempenhoProdutividadeValidator);
        }
	}
	
	@RequestMapping(value = "/meta/desempenhoProdutividade", method = RequestMethod.GET)
	public String list(ModelMap modelMap, HttpServletRequest request, ServletResponse Response) {
		return search(new MetaDesempenhoProdutividadeFiltro(), modelMap, request, Response);
	}
	
	@RequestMapping(value = "/meta/desempenhoProdutividade/search", method = RequestMethod.GET)
	public String search(MetaDesempenhoProdutividadeFiltro filtro, ModelMap modelMap, HttpServletRequest request, ServletResponse Response) {
		
		DisplayTagPageable pageable = new DisplayTagPageable(request);
		PaginatedList displayTagList = metaDesempenhoProdutividadeDao.filtrar(filtro, pageable);
		
		modelMap.addAttribute("filtro", filtro);
		modelMap.addAttribute("metas", displayTagList);
		
		return "metaDesempenhoProdutividade/list";
	}
	
	@RequestMapping(value = "/meta/desempenhoProdutividade/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		MetaDesempenhoProdutividade meta = metaDesempenhoProdutividadeDao.find(id);
		
		if (meta == null) {
			throw new HttpStatusException(HttpStatus.NOT_FOUND, "Meta não encontrada");
		}
		
		modelMap.addAttribute("meta", meta);
		return "metaDesempenhoProdutividade/show";
	}
	
	@RequestMapping(value = "/meta/desempenhoProdutividade/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("meta", new MetaDesempenhoProdutividade());
		return "metaDesempenhoProdutividade/create";
	}
	
	@RequestMapping(value = "/meta/desempenhoProdutividade/", method = RequestMethod.POST)
	public String create(@ModelAttribute("meta") @Valid MetaDesempenhoProdutividade meta, BindingResult result, ModelMap modelMap) {
		
		if (result.hasErrors()) {
			modelMap.addAttribute("meta", meta);
			return "metaDesempenhoProdutividade/create";
        }		
		
		metaDesempenhoProdutividadeService.salvar(meta);
		
		return "redirect:/meta/desempenhoProdutividade?messageSucesso=Meta dos Indicadores de Desempenho/Produtividades salva com sucesso";
	}
	
	@RequestMapping(value = "/meta/desempenhoProdutividade/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		
		MetaDesempenhoProdutividade meta = metaDesempenhoProdutividadeDao.find(id);
		
		if (meta == null) {
			throw new HttpStatusException(HttpStatus.NOT_FOUND, "Meta não encontrada");
		}
		
		modelMap.addAttribute("meta", meta);
		
		return "metaDesempenhoProdutividade/update";
	}

	@RequestMapping(value = "/meta/desempenhoProdutividade/{id}", method = RequestMethod.PUT)
	public String update(@PathVariable("id") Long id, @ModelAttribute("indicador") @Valid MetaDesempenhoProdutividade indicador, BindingResult result, ModelMap modelMap) {
		MetaDesempenhoProdutividade indicadorDoBanco = metaDesempenhoProdutividadeDao.find(id);
		
		if (indicadorDoBanco == null) {
			throw new HttpStatusException(HttpStatus.NOT_FOUND, "Meta não encontrada");
		}
		
		if (result.hasErrors()) {
			modelMap.addAttribute("indicador", indicador);
			
			return "metaDesempenhoProdutividade/update";
        }

		metaDesempenhoProdutividadeService.salvar(indicador);
		
		return "redirect:/meta/desempenhoProdutividade?messageSucesso=Meta dos Indicadores de Desempenho/Produtividade atualizada com sucesso";
	}
	
	@RequestMapping(value = "/meta/desempenhoProdutividade/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		MetaDesempenhoProdutividade metaDoBanco = metaDesempenhoProdutividadeDao.find(id);
		
		if (metaDoBanco == null) {
			throw new HttpStatusException(HttpStatus.NOT_FOUND, "Meta não encontrada");
		}
		
		String message = "Meta excluída com sucesso";
		try{
			metaDesempenhoProdutividadeService.excluir(metaDoBanco);
		}catch (Exception e){
			e.printStackTrace();
			message = "Não foi possível excluir a Meta";
		}
		
		return "redirect:/meta/desempenhoProdutividade?messageSucesso=" + message;
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
	
	@ModelAttribute("semestresList")
	public Collection<Integer> semestres() {
		List<Integer> semestres = new ArrayList<>();
		semestres.add(1);
		semestres.add(2);
        return semestres;
    }
}
