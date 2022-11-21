package br.com.siged.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.ModuloDAO;
import br.com.siged.dao.NotaDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.editor.EventoEditor;
import br.com.siged.editor.ModuloEditor;
import br.com.siged.editor.ParticipanteEditor;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Nota;
import br.com.siged.entidades.Participante;
import br.com.siged.filtro.NotaFiltro;
import br.com.siged.service.NotaService;
import br.com.siged.service.exception.BusinessException;
import br.com.siged.util.comparator.NotaComparator;

@Controller
@RequestMapping("/nota/**")
public class NotaController {
	
	@Autowired
	private NotaDAO notaDao;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ModuloDAO moduloDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private NotaService notaService;

	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(br.com.siged.entidades.Evento.class, new EventoEditor(eventoDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Modulo.class, new ModuloEditor(moduloDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Participante.class, new ParticipanteEditor(participanteDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Nota.class,"nota", new CustomNumberEditor(BigDecimal.class, DecimalFormat.getNumberInstance(new Locale("pt", "BR")), true));
        dataBinder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }	
	
	@RequestMapping(value = "/nota/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("nota", notaDao.find(id));
		return "nota/show";
	}	

	@RequestMapping(value = "/nota", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("notafiltro", new NotaFiltro());		
		modelMap.addAttribute("notas", notaDao.findAll());
		return "nota/list";
	}	
	
	@RequestMapping(value = "/nota/search", method = RequestMethod.GET)
	public String search(NotaFiltro notafiltro, ModelMap modelMap, ServletRequest Request, ServletResponse Response) throws IOException {
		modelMap.addAttribute("notafiltro", notafiltro);
		modelMap.addAttribute("notas", notaDao.findNotaByCriteria(notafiltro.getEvento(), notafiltro.getParticipante(), notafiltro.getModulo()));
		return "nota/list";
	}

	@RequestMapping(value = "/nota/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		notaDao.remove(notaDao.find(id));
		return "redirect:/nota";
	}

	@RequestMapping(value = "/nota/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("nota", new Nota());
		return "nota/create";
	}
	
	@RequestMapping(value = "/nota/form", method = RequestMethod.POST)
    public String formCSVFile(@RequestParam("file") MultipartFile file, ModelMap modelMap, HttpServletRequest request) {

		if (file.isEmpty()) {
			modelMap.addAttribute("nota", new Nota());
			modelMap.addAttribute("mensagem", "Arquivo não informado");
			return "nota/create";
		}
		
		Evento evento = eventoDao.find(Long.parseLong(request.getParameter("eventoId")));
		Modulo modulo = moduloDao.find(Long.parseLong(request.getParameter("moduloId")));
		Boolean calcularMedia = Boolean.valueOf(request.getParameter("calcularMedia"));
		
		try {
			int fatorMedia = 1;
			if(calcularMedia)
				fatorMedia = Integer.parseInt(request.getParameter("mediaValor"));
			
			List<Nota> notas = notaService.loadFromCSVFile(file, modulo, evento , fatorMedia);
			List<Modulo> modulos = moduloDao.findByEventoId(evento.getId());
			
			Nota notaModel = new Nota();
			notaModel.setEventoId(evento);
			notaModel.setModuloId(modulo);
			Collections.sort(notas, NotaComparator.OrderByParticipante.asc());
			
			modelMap.addAttribute("nota", notaModel);
			modelMap.addAttribute("modulos", modulos);
			modelMap.addAttribute("notas", notas);
			
			return "nota/create";
		} catch (BusinessException e) {
			modelMap.addAttribute("nota", new Nota());
			modelMap.addAttribute("mensagemErro", e.getMessage());
			return "nota/create";
		} catch (NumberFormatException e) {
			modelMap.addAttribute("nota", new Nota());
			modelMap.addAttribute("mensagemErro", "Informe um valor válido para média");
			return "nota/create";
		}
    }

	@RequestMapping(value = "/nota", method = RequestMethod.POST)	
	public String create(HttpServletRequest request) {		
		
		String [] notas         = request.getParameterValues("nota");
		String [] participantes = request.getParameterValues("participanteId");
		
		Evento eventoId = eventoDao.find(Long.parseLong(request.getParameter("eventoId")));
		Modulo moduloId = moduloDao.find(Long.parseLong(request.getParameter("moduloId")));
		
		int notasCadastradas = 0;
		if (notas != null) {
			for (int i = 0; i < notas.length; i++) {		
				if (!notas[i].equals("")) {
					Participante participanteId = participanteDao.find(Long.parseLong(participantes[i]));
					Nota nota = new Nota();
					nota.setEventoId(eventoId);
					nota.setModuloId(moduloId);
					nota.setParticipanteId(participanteId);
					nota.setNota(notas[i].replace(",","."));
					notaDao.persist(nota);
					notasCadastradas++;
				}
			}
		}
		
		if (notasCadastradas != 0) {
			return "redirect:/nota?mensagem=Nota(s) cadastrada(s) com sucesso.";
		} else if (notasCadastradas == 0) {
			return "redirect:/nota?mensagem=Nenhuma nota foi cadastrada.";
		}
		
		return "redirect:/nota";	
	}

	@RequestMapping(value = "/nota/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("nota", notaDao.find(id));
		modelMap.addAttribute("listaParticipante", participanteDao.findByEventoId(Long.parseLong(notaDao.find(id).getEventoId().getId().toString())));
		modelMap.addAttribute("listaModulo", moduloDao.findByEventoId(Long.parseLong(notaDao.find(id).getEventoId().getId().toString())));
		return "nota/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@ModelAttribute("nota") @Valid Nota nota) {
		nota.setNota(nota.getNota().replace(",", "."));
		notaDao.merge(nota);
		return "redirect:/nota";
	}
	
	@RequestMapping(value = "/nota/procuraParticipante", method = RequestMethod.GET)
    @ResponseBody
    public List<Participante> procuraParticipante(
        @RequestParam(value="eventoId") Long eventoId, @RequestParam(value="moduloId") Long moduloId) {
        
        return participanteDao.findByEventoIdAndModuloId(eventoId, moduloId);
    }
	
	@RequestMapping(value = "/nota/procuraModulo", method = RequestMethod.GET)
    @ResponseBody
    public List<Modulo> procuraModulo(
        @RequestParam(value="eventoId") Long eventoId) {
        
        return moduloDao.findByEventoIdByNota(eventoId);
    }
	
	@RequestMapping(value = "/nota/procuraModuloPorEvento", method = RequestMethod.GET)
    @ResponseBody
    public List<Modulo> procuraModuloPorEvento(
        @RequestParam(value="eventoId") Long eventoId) {
        
        return moduloDao.findByEventoId(eventoId);
    }

	
	
	@ModelAttribute("eventoList")
	public List<Evento> populateEventos() {        
		List<Evento> resultado = eventoDao.findByNaoPrevistoComNota();		
		return resultado;		
    }
	
	@ModelAttribute("todosEventosList")
	public List<Evento> populateTodosEventos() {
        return eventoDao.findAll();		
    }
	
	
}
