package br.com.siged.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.FonteGastoDAO;
import br.com.siged.dao.GastoDAO;
import br.com.siged.dao.InstrutorDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.TipoGastoDAO;
import br.com.siged.dao.TipoInstrutorDAO;
import br.com.siged.editor.EventoEditor;
import br.com.siged.editor.FonteGastoEditor;
import br.com.siged.editor.InstrutorEditor;
import br.com.siged.editor.ParticipanteEditor;
import br.com.siged.editor.TipoGastoEditor;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.FonteGasto;
import br.com.siged.entidades.Gasto;
import br.com.siged.entidades.Instrutor;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.TipoGasto;
import br.com.siged.entidades.TipoInstrutor;
import br.com.siged.entidades.TipoPublicoAlvo;
import br.com.siged.filtro.GastoFiltro;
import br.com.siged.util.Util;

@Controller
@RequestMapping("/gasto/**")
public class GastoController {
	
	@Autowired
	private GastoDAO gastoDao;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private TipoGastoDAO tipoGastoDao;
	@Autowired
	private FonteGastoDAO fonteGastoDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private InstrutorDAO instrutorDao;
	@Autowired
	private TipoInstrutorDAO tipoInstrutorDao;
	
	private DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
	
	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(br.com.siged.entidades.TipoGasto.class, new TipoGastoEditor(tipoGastoDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.FonteGasto.class, new FonteGastoEditor(fonteGastoDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Evento.class, new EventoEditor(eventoDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Participante.class, new ParticipanteEditor(participanteDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Instrutor.class, new InstrutorEditor(instrutorDao));
        dataBinder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, this.decimalFormat, true));
        dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
	}

	@RequestMapping(value = "/gasto/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("gasto", gastoDao.find(id));
		return "gasto/show";
	}	

	@RequestMapping(value = "/gasto", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("gastofiltro", new GastoFiltro());
		modelMap.addAttribute("gastos", gastoDao.findAll());
		return "gasto/list";
	}
	
	@RequestMapping(value = "/gasto/procuraParticipante", method = RequestMethod.GET)
    @ResponseBody
    public List<Participante> procuraParticipante(
        @RequestParam(value="eventoId") Long eventoId) {
        
        return participanteDao.findByEventoId(eventoId);
    }
	
	@RequestMapping(value = "/gasto/procuraServidores", method = RequestMethod.GET)
    @ResponseBody
    public List<Participante> procuraServidores(
        @RequestParam(value="eventoId") Long eventoId) {
        
        return participanteDao.findByTipoAndEvento(1L, eventoId);
    }
	
	@RequestMapping(value = "/gasto/procuraServidoresEMembros", method = RequestMethod.GET)
    @ResponseBody
    public List<Participante> procuraServidoresEMembros(@RequestParam(value="eventoId") Long eventoId) {
		Long tipoId[] = {1L, 4L};
        return participanteDao.findByTiposAndEvento(tipoId, eventoId);
    }
	
	@RequestMapping(value = "/gasto/procuraInstrutoresPorTipoEEvento/{tipoInstrutorId}/{eventoId}")
	@ResponseBody
	public Collection<Instrutor> procuraInstrutoresPorTipoEEvento(@PathVariable(value = "tipoInstrutorId") Long tipoInstrutorId, @PathVariable(value = "eventoId") Long eventoId) {
		Collection<Instrutor> instrutores = new ArrayList<>();
		TipoInstrutor tipoInstrutor = tipoInstrutorDao.find(tipoInstrutorId);
		if(tipoInstrutor != null){
			instrutores = instrutorDao.findInstrutoresByEventoIdAndTipoId(eventoId, tipoInstrutorId);
		}
		return instrutores;
	}
	
	@RequestMapping(value = "/gasto/search", method = RequestMethod.GET)
	public String search(GastoFiltro gastofiltro, ModelMap modelMap, ServletRequest Request, ServletResponse Response) throws IOException {
		modelMap.addAttribute("gastofiltro", gastofiltro);
		modelMap.addAttribute("gastos", gastoDao.findGastoByCriteria(gastofiltro.getEvento(), gastofiltro.getTipoGasto(), gastofiltro.getFonteGasto(), gastofiltro.getParticipante(), gastofiltro.getNumeroEmpenho(), gastofiltro.getNumeroProcesso()));
		return "gasto/list";
	}

	@RequestMapping(value = "/gasto/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		gastoDao.remove(gastoDao.find(id));
		return "redirect:/gasto";
	}

	@RequestMapping(value = "/gasto/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap ) {
		modelMap.addAttribute("gasto", new Gasto());
		return "gasto/create";
	}
	
	@RequestMapping(value = "/gasto", method = RequestMethod.POST)
	public String create(@ModelAttribute("gasto") @Valid Gasto gasto, BindingResult result) {
		
		verificaGastoErrors(gasto, result);
		
		if (result.hasErrors()) {  
			return "gasto/create";
        } 
		
		gastoDao.persist(gasto);
		return "redirect:/gasto";
	}

	@RequestMapping(value = "/gasto/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		Gasto gasto = gastoDao.find(id);
		modelMap.addAttribute("gasto", gasto);
		
		montarModelUpdate(modelMap, gasto);
	
		return "gasto/update";
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("gasto") @Valid Gasto gasto , BindingResult result , ModelMap modelMap) {
		
		verificaGastoErrors(gasto, result);
		
		if (result.hasErrors()) { 
			montarModelUpdate(modelMap, gasto);
			return "gasto/update";	
		}
		
		gastoDao.merge(gasto);
		
		return "redirect:/gasto";
	}	
	
	@ModelAttribute("eventoList")
	public Collection<Evento> populateEventos() {
        return eventoDao.findAll();
    }
	@ModelAttribute("tipoGastoList")
	public Collection<TipoGasto> populateTipoGastos() {
        return tipoGastoDao.findAll();
    }
	@ModelAttribute("fonteGastoList")
	public Collection<FonteGasto> populateFonteGastos() {
        return fonteGastoDao.findAll();
    }
	
	private void verificaGastoErrors(Gasto gasto, BindingResult result) {
		if (gasto.getNumeroProcesso() == null || gasto.getNumeroProcesso()== "") {
			result.addError(new FieldError("gasto", "numeroProcesso", "Campo Obrigatório"));
		}
		
		BigDecimal valorzero = new BigDecimal(0.00);
		
		if (gasto.getValor().compareTo(valorzero)== 0)  {
			result.addError(new FieldError("gasto", "valor", "O valor não pode ser igual a zero"));
		}
		
		if((gasto.getTipoId() != null) && (gasto.getTipoId().isInstrutorInterno() || gasto.getTipoId().isInstrutorExterno())) {
			if(gasto.getParticipanteId() != null) {
				result.addError(new FieldError("gasto", "participanteId", "Não é possível selecionar participante para tipo de gasto Instrutor Interno ou Externo"));
			}
			if(gasto.getInstrutor() == null ) {
				result.addError(new FieldError("gasto", "instrutor", "É preciso selecionar o instrutor para tipo de gasto Instrutor Interno ou Externo"));
			}
		}
	}
	
	private void montarModelUpdate(ModelMap modelMap, Gasto gasto) {
		Long[] tipoPublicoAlvoIds = {TipoPublicoAlvo.SERVIDOR_ID, TipoPublicoAlvo.MEMBRO_ID};
		List<Participante> participantes = new ArrayList<>();
		Collection<Instrutor> instrutores = new ArrayList<>();
		
		if(gasto.getEventoId() != null) {
			participantes = participanteDao.findByTiposAndEvento(tipoPublicoAlvoIds, gasto.getEventoId().getId());
			if(gasto.getTipoId() != null) {
				if(gasto.getTipoId().isInstrutorInterno()) {
					instrutores = instrutorDao.findInstrutoresByEventoIdAndTipoId(gasto.getEventoId().getId(), TipoInstrutor.INTERNO_ID);
				} else if(gasto.getTipoId().isInstrutorExterno()) {
					instrutores = instrutorDao.findInstrutoresByEventoIdAndTipoId(gasto.getEventoId().getId(), TipoInstrutor.EXTERNO_ID);
				}
			}
		}
		
		if(gasto.getInstrutor() != null) {
			modelMap.addAttribute("selected", gasto.getInstrutor().getId());
		} else {
			modelMap.addAttribute("selected", Long.valueOf(0l));
		}
		
		modelMap.addAttribute("valorFormatado", this.decimalFormat.format(gasto.getValor()));
		modelMap.addAttribute("listaParticipante", participantes);
		modelMap.addAttribute("listaInstrutores", instrutores);
		modelMap.addAttribute("disableSelectInstrutor", instrutores.size() > 0 ? false : true);
	}

	/**
	 * Filtrar gastos com instrutores (TipoGasto.getId().equals(Long.valueOf(5))
	 * @param modelMap
	 * @return
	 */
	//@RequestMapping(value = "/gasto/printInConsole", method = RequestMethod.GET)
	public String comTipoInstrutores(ModelMap modelMap) {
	    Util util = new Util();
	    List<Gasto> gastos = gastoDao.findAll();
	    for (Gasto gasto : gastos) {
	        if(gasto.getTipoId() != null && gasto.getTipoId().getId().equals(Long.valueOf(5))) {
	            System.out.print(gasto.getId() + ";");
	            System.out.print(gasto.getNumeroProcesso() + ";");
	            System.out.print(util.formataMonetario(gasto.getValor().doubleValue()) + ";");
	            System.out.print(gasto.getEventoId().getNome2() + ";");
	            
	            List<Instrutor> instrutorList = new ArrayList<Instrutor>();
	            for (Modulo m : gasto.getEventoId().getModuloList()) {
	                for (Instrutor i : m.getInstrutorList()) {
	                    if (!instrutorList.contains(i)) {
	                        instrutorList.add(i);
	                    }
	                }
	            }
	        
	            for (Instrutor instrutor : instrutorList) {
	                System.out.print(instrutor.getNome() + ";");
	                if(instrutor.getTipoId() != null) {
	                    System.out.print(instrutor.getTipoId().getDescricao() + ";");
	                }
	            }
	            System.out.println();
	        }
	    }
	    return "redirect:/gasto";
	}
}
