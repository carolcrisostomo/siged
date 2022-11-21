package br.com.siged.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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
import br.com.siged.dao.FrequenciaDAO;
import br.com.siged.dao.ModuloDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.editor.EventoEditor;
import br.com.siged.editor.ModuloEditor;
import br.com.siged.editor.ParticipanteEditor;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Frequencia;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Participante;
import br.com.siged.filtro.FrequenciaFiltro;
import br.com.siged.filtro.TurnoJson;

@Controller
@RequestMapping("/frequencia/**")
public class FrequenciaController {
	
	@Autowired
	private FrequenciaDAO frequenciaDao;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ModuloDAO moduloDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	
	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(br.com.siged.entidades.Evento.class, new EventoEditor(eventoDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Modulo.class, new ModuloEditor(moduloDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Participante.class, new ParticipanteEditor(participanteDao));
        dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
    }

	@RequestMapping(value = "/frequencia/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("frequencia", frequenciaDao.find(id));
		return "frequencia/show";
	}	

	@RequestMapping(value = "/frequencia", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("frequenciafiltro", new FrequenciaFiltro());
		modelMap.addAttribute("frequencias", frequenciaDao.findAll());
		return "frequencia/list";
	}	

	@RequestMapping(value = "/frequencia/search", method = RequestMethod.GET)
	public String search(FrequenciaFiltro frequenciafiltro, ModelMap modelMap, ServletRequest Request, ServletResponse Response) throws IOException {
		modelMap.addAttribute("frequenciafiltro", frequenciafiltro);
		modelMap.addAttribute("frequencias", frequenciaDao.findFrequenciaByCriteria(frequenciafiltro.getEvento(), frequenciafiltro.getModulo(),frequenciafiltro.getParticipante(), frequenciafiltro.getData1(), frequenciafiltro.getData2()));
		return "frequencia/list";
	}
	
	@RequestMapping(value = "/frequencia/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		frequenciaDao.remove(frequenciaDao.find(id));
		return "redirect:/frequencia";
	}

	@RequestMapping(value = "/frequencia/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("frequencia", new Frequencia());
		return "frequencia/create";
	}

	@RequestMapping(value = "/frequencia", method = RequestMethod.POST)
	public String create(@ModelAttribute("frequencia") @Valid Frequencia frequencia, BindingResult result, ModelMap modelMap) {
	
		GregorianCalendar horaAtual = new GregorianCalendar();  
		SimpleDateFormat teste2 = new SimpleDateFormat("HH:mm");  
		SimpleDateFormat fmData = new SimpleDateFormat("dd/MM/yyyy"); 
		String hrAtual = teste2.format(horaAtual.getTime());

		int numHoraAtual = Integer.parseInt(hrAtual.substring(0,2));
		
		if (frequencia.getData() != null) {
			if (frequencia.getData().before(frequencia.getModuloId().getDataInicio()) || frequencia.getData().after(frequencia.getModuloId().getDataFim())) {
				result.addError(new FieldError("frequencia", "data", "Data fora do período de realização do módulo"));
			}
			if (frequencia.getData().after(new Date())) {
				result.addError(new FieldError("frequencia", "data", "Não é possível lançar frequências para datas futuras"));
			}
			 
			if (fmData.format(frequencia.getData()).equals(fmData.format (new Date()))) {
				
			    String horaTermino1 = frequencia.getModuloId().getHoraFimTurno1();
			    String horaTermino2 = frequencia.getModuloId().getHoraFimTurno2();
			    String horaTermino3 = frequencia.getModuloId().getHoraFimTurno3();
			    
				switch (Integer.parseInt(""+frequencia.getTurno().subSequence(0, 1))) {
				case 1:
					if ( numHoraAtual < Integer.parseInt(horaTermino1.substring(0,2)) )
						result.addError(new FieldError("frequencia", "turno", "Não é possível lançar frequências para horários futuros"));						
					break;
				case 2:   
					if ( numHoraAtual < Integer.parseInt(horaTermino2.substring(0,2)) )
						result.addError(new FieldError("frequencia", "turno", "Não é possível lançar frequências para horários futuros"));	
					break;
				case 3:   
					if ( numHoraAtual < Integer.parseInt(horaTermino3.substring(0,2)) )
						result.addError(new FieldError("frequencia", "turno", "Não é possível lançar frequências para horários futuros"));	
					break;	
				default:
					break;
				}
			}
			
		}
		if (frequenciaDao.findByEventoAndModuloAndData(frequencia.getEventoId(), frequencia.getModuloId(), frequencia.getData(), frequencia.getTurno()) != null){
			result.addError(new FieldError("frequencia", "data", "Não é possível incluir 2 frequências para a mesma data/turno"));
		}
		if (result.hasErrors()) {
			modelMap.addAttribute("frequencia", frequencia);
			return "frequencia/create";
        }  
		frequenciaDao.persist(frequencia);
		return "redirect:/frequencia";
	}

	@RequestMapping(value = "/frequencia/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("frequencia", frequenciaDao.find(id));
		modelMap.addAttribute("listaParticipante", participanteDao.findByEventoId(Long.parseLong(frequenciaDao.find(id).getEventoId().getId().toString())));
		modelMap.addAttribute("listaModulo", moduloDao.findByEventoId(Long.parseLong(frequenciaDao.find(id).getEventoId().getId().toString())));
		return "frequencia/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("frequencia") @Valid Frequencia frequencia, BindingResult result) {
		
		Frequencia frequenciaantigo = frequenciaDao.find(frequencia.getId());
		frequencia.setEventoId(frequenciaantigo.getEventoId());
		frequencia.setModuloId(frequenciaantigo.getModuloId());
		
		frequenciaDao.merge(frequencia);
		return "redirect:/frequencia";
	}
	
	
	@RequestMapping(value = "/frequencia/procuraParticipante", method = RequestMethod.GET)
    @ResponseBody
    public List<Participante> procuraParticipante(@RequestParam(value="eventoId") Long eventoId) {        
        return participanteDao.findByEventoId(eventoId);
    }
	
	@RequestMapping(value = "/frequencia/procuraModulo", method = RequestMethod.GET)
    @ResponseBody
    public List<Modulo> procuraModulo(@RequestParam(value="eventoId") Long eventoId) {        
        return moduloDao.findPresencialByEvento(eventoId);
    }
	
	@RequestMapping(value = "/frequencia/procuraTurno", method = RequestMethod.GET)
    @ResponseBody
    public TurnoJson procuraTurno(@RequestParam(value="moduloId") Long moduloId) {
        
		Modulo modulo = moduloDao.find(moduloId);
		List<String> turnos = new ArrayList<>();
		
		if(modulo.getHoraInicioTurno1() != null) {
			turnos.add("1º TURNO " + "(" + modulo.getHoraInicioTurno1() + " - " + modulo.getHoraFimTurno1() + ")");
		}
		
		if(modulo.getHoraInicioTurno2() != null) {
			turnos.add("2º TURNO " + "(" + modulo.getHoraInicioTurno2() + " - " + modulo.getHoraFimTurno2() + ")");
		}

		if(modulo.getHoraInicioTurno3() != null) {
			turnos.add("3º TURNO " + "(" + modulo.getHoraInicioTurno3() + " - " + modulo.getHoraFimTurno3() + ")");
		}		
	
		TurnoJson populate = new TurnoJson();		
		populate.setTurnos(turnos.toArray(new String[turnos.size()]));

		return populate;
    }	
	
		
	@ModelAttribute("eventoList")
	public Collection<Evento> populateEventos() {
		return eventoDao.findPresencialByNaoPrevistoComModulo();
	}	
	
	@ModelAttribute("SNList")
    public Map<String,String> populateSN() {
		
		Map<String,String> populate = new LinkedHashMap<String,String>();
		populate.put("S", "S");
		populate.put("N", "N");
		return populate;
	}

}
