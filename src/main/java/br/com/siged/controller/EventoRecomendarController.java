package br.com.siged.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import br.com.siged.dao.EventoRecomendarDAO;
import br.com.siged.dao.MunicipioDAO;
import br.com.siged.dao.PaisDAO;
import br.com.siged.dao.ResponsavelSetorDAO;
import br.com.siged.dao.SetorDAO;
import br.com.siged.dao.UfMunicipioDAO;
import br.com.siged.dao.UsuarioSCADAO;
import br.com.siged.editor.MunicipioEditor;
import br.com.siged.editor.PaisEditor;
import br.com.siged.editor.SetorEditor;
import br.com.siged.editor.UfMunicipioEditor;
import br.com.siged.editor.UsuarioSCAEditor;
import br.com.siged.entidades.EventoRecomendar;
import br.com.siged.entidades.Pais;
import br.com.siged.entidades.externas.Setor;
import br.com.siged.entidades.externas.UfMunicipio;
import br.com.siged.entidades.externas.UsuarioSCA;
import br.com.siged.filtro.EventoRecomendarFiltro;
import br.com.siged.util.EmailUtil;

@Controller
@RequestMapping("/eventorecomendar/**")
public class EventoRecomendarController {
	
	@Autowired
	private EventoRecomendarDAO eventorecomendarDao;
	@Autowired
	private SetorDAO setorDao;
	@Autowired
	private UsuarioSCADAO usuarioDao;	
	@Autowired
	private PaisDAO paisDao;
	@Autowired
	private MunicipioDAO municipioDao;
	@Autowired
	private UfMunicipioDAO ufMunicipioDao;
	@Autowired
	private ResponsavelSetorDAO responsavelSetorDao;
	@Autowired
	private EmailUtil emailUtil;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Setor.class, new SetorEditor(setorDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.externas.UsuarioSCA.class, new UsuarioSCAEditor(usuarioDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Pais.class,	new PaisEditor(paisDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.externas.UfMunicipio.class, new UfMunicipioEditor(ufMunicipioDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Municipio.class, new MunicipioEditor(municipioDao));
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		DecimalFormat df = new DecimalFormat(",##0,00");
		dataBinder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, df, true));
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
		dataBinder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}

	@RequestMapping(value = "/eventorecomendar/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("eventorecomendar", eventorecomendarDao.find(id));
		return "eventorecomendar/show";
	}

	@RequestMapping(value = "/eventorecomendar", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("eventorecomendarfiltro",
				new EventoRecomendarFiltro());
		modelMap.addAttribute("eventosrecomendar",
				eventorecomendarDao.findAll());
		return "eventorecomendar/list";
	}

	@RequestMapping(value = "/eventorecomendar/search", method = RequestMethod.GET)
	public String search(EventoRecomendarFiltro eventorecomendarfiltro,
			ModelMap modelMap, ServletRequest Request, ServletResponse Response)
			throws IOException {
		modelMap.addAttribute("eventorecomendarfiltro", eventorecomendarfiltro);
		modelMap.addAttribute("eventosrecomendar", eventorecomendarDao
				.findEventoRecomendarByCriteria(
						eventorecomendarfiltro.getData1(),
						eventorecomendarfiltro.getData2(),
						eventorecomendarfiltro.getTitulo(),
						eventorecomendarfiltro.getSetor(),
						eventorecomendarfiltro.getAprovado()));
		return "eventorecomendar/list";
	}

	@RequestMapping(value = "/eventorecomendar/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		eventorecomendarDao.remove(eventorecomendarDao.find(id));
		return "redirect:/eventorecomendar";
	}

	@RequestMapping(value = "/eventorecomendar/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("eventorecomendar", new EventoRecomendar());
		return "eventorecomendar/create";
	}

	@RequestMapping(value = "/eventorecomendar", method = RequestMethod.POST)
	public String create(
			@ModelAttribute("eventorecomendar") @Valid EventoRecomendar eventorecomendar,
			BindingResult result,
			@RequestParam("material") MultipartFile material, ModelMap model)
			throws IOException, Exception {

		if (eventorecomendar.getDataInicio() != null && eventorecomendar.getDataFim() != null) {
			if (eventorecomendar.getDataInicio().after(eventorecomendar.getDataFim())) {
				result.addError(new FieldError("eventorecomendar", "dataInicio", "Data inicial maior que a data final"));
			}
		}

		if (eventorecomendar.getSetorId() != null && eventorecomendar.getSetorId().getId() == 0) {
			result.addError(new FieldError("eventorecomendar", "setorId", "Campo Obrigatório"));
		}
		
		if (eventorecomendar.getPaisId() == null) {
			result.addError(new FieldError("eventorecomendar", "paisId", "Campo Obrigatório"));
		}
		
		if (eventorecomendar.getPaisId() != null && eventorecomendar.getPaisId().getId() == 1) {

			if (eventorecomendar.getUfMunicipio() == null || eventorecomendar.getUfMunicipio().getUf().equals("0")) {
				result.addError(new FieldError("eventorecomendar", "ufMunicipio", "Campo Obrigatório"));
			}
			if (eventorecomendar.getMunicipio() == null || eventorecomendar.getMunicipio().getId() == 0) {
				result.addError(new FieldError("eventorecomendar", "municipio", "Campo Obrigatório"));
			}
		}

		if (result.hasErrors()) {
			return "eventorecomendar/create";
		}

		eventorecomendar.setAprovado("-");

		if (material.getOriginalFilename() != null) {
			eventorecomendar.setMaterial(material.getBytes());
			eventorecomendar.setMaterialNome(material.getOriginalFilename());
			eventorecomendar.setMaterialTipo(material.getContentType());
		}

		final UsuarioSCA chefe = responsavelSetorDao.findAtivoBySetorId(eventorecomendar.getSetorId().getId()).getResponsavel();
		

		if (chefe != null && chefe.getEmail() != null) {
			eventorecomendar.setDataCadastro(new Date());
			eventorecomendarDao.persist(eventorecomendar);
			emailUtil.emailSugestao(eventorecomendar, chefe);
		} else {
			model.addAttribute(
					"mensagem",
					"Não foi possível incluir a sugestão! O setor selecionado não possui responsável cadastrado no SIGED ou não foi encontrado seu e-mail.");
			return "redirect:/eventorecomendar/form";
		}

		return "redirect:/eventorecomendar";
	}

	@RequestMapping(value = "/eventorecomendar/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		
		EventoRecomendar eventorecomendar = eventorecomendarDao.findById(id);

		if(eventorecomendar.getMunicipio() != null)
			eventorecomendar.setUfMunicipio(ufMunicipioDao.findByUf(eventorecomendar
					.getMunicipio().getUfMunicipio()));

		modelMap.addAttribute("eventorecomendar", eventorecomendar);		
		
		return "eventorecomendar/update";
	}

	@RequestMapping(value = "/eventorecomendar/{id}/formchefe", method = RequestMethod.GET)
	public String updatechefeForm(@PathVariable("id") Long id, ModelMap modelMap) {
		
		EventoRecomendar eventorecomendar = eventorecomendarDao.findById(id);

		if(eventorecomendar.getMunicipio() != null)
			eventorecomendar.setUfMunicipio(ufMunicipioDao.findByUf(eventorecomendar
					.getMunicipio().getUfMunicipio()));

		modelMap.addAttribute("eventorecomendar", eventorecomendar);
		
		return "eventorecomendar/updatechefe";
	}

	@RequestMapping(value = "/eventorecomendar/{id}/update", method = RequestMethod.POST)
	public String update(
			@ModelAttribute("eventorecomendar") @Valid EventoRecomendar eventorecomendar,
			BindingResult result,
			@RequestParam("material") MultipartFile material)
			throws IOException {

		if (eventorecomendar.getDataInicio() != null
				&& eventorecomendar.getDataFim() != null) {
			if (eventorecomendar.getDataInicio().after(
					eventorecomendar.getDataFim())) {
				result.addError(new FieldError("eventorecomendar",
						"dataInicio", "Data inicial maior que a data final"));
			}
		}
		
		if (eventorecomendar.getPaisId() != null
				&& eventorecomendar.getPaisId().getId() == 1) {

			if (eventorecomendar.getUfMunicipio() == null
					|| eventorecomendar.getUfMunicipio().getUf().equals("0")) {
				result.addError(new FieldError("eventorecomendar", "ufMunicipio",
						"Campo Obrigatório"));
			}
			if (eventorecomendar.getMunicipio() == null
					|| eventorecomendar.getMunicipio().getId() == 0) {
				result.addError(new FieldError("eventorecomendar", "municipio",
						"Campo Obrigatório"));
			}
		}

		if (result.hasErrors()) {
			return "eventorecomendar/update";
		}

		if (material.getOriginalFilename() != "") {
			eventorecomendar.setMaterial(material.getBytes());
			eventorecomendar.setMaterialNome(material.getOriginalFilename());
			eventorecomendar.setMaterialTipo(material.getContentType());
		} else {
			EventoRecomendar eventorecomendarantigo = eventorecomendarDao
					.find(eventorecomendar.getId());
			eventorecomendar.setMaterial(eventorecomendarantigo.getMaterial());
			eventorecomendar.setMaterialNome(eventorecomendarantigo
					.getMaterialNome());
			eventorecomendar.setMaterialTipo(eventorecomendarantigo
					.getMaterialTipo());
		}

		eventorecomendarDao.merge(eventorecomendar);
		return "redirect:/eventorecomendar";
	}	

	@RequestMapping(value = "/eventorecomendar/{id}/chefe", method = RequestMethod.POST)
	public String updatechefe(
			@ModelAttribute("eventorecomendar") EventoRecomendar eventorecomendar,
			BindingResult result) {

		EventoRecomendar eventorecomendarantigo = eventorecomendarDao.find(eventorecomendar.getId());

		eventorecomendarantigo.setAprovado(eventorecomendar.getAprovado());
		eventorecomendarantigo.setJustificativa(eventorecomendar.getJustificativa());

		if (result.hasErrors()) {
			return "eventorecomendar/updatechefe";
		}

		eventorecomendarDao.merge(eventorecomendarantigo);
		return "redirect:/inscricao/minhasindicacoes";
	}
	
	@RequestMapping("/eventorecomendar/material/{id}")
	public String download(@PathVariable("id") Long eventoId,
			HttpServletResponse response) {

		EventoRecomendar eventorecomendar = eventorecomendarDao
				.findById(eventoId);
		try {
			response.setHeader("Content-Disposition", "inline;filename=\""
					+ eventorecomendar.getMaterialNome() + "\"");
			OutputStream out = response.getOutputStream();
			response.setContentType(eventorecomendar.getMaterialTipo());
			IOUtils.copy(
					new ByteArrayInputStream(eventorecomendar.getMaterial()),
					out);
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	

	@ModelAttribute("ufList")
	public Collection<UfMunicipio> populateUfs() {
		return ufMunicipioDao.findAll();
	}

	@ModelAttribute("paisList")
	public Collection<Pais> populatePais() {
		return paisDao.findAll();
	}

	@ModelAttribute("SNList")
	public Map<String, String> populateSN() {

		Map<String, String> populate = new LinkedHashMap<String, String>();
		populate.put("-", "-");
		populate.put("S", "S");
		populate.put("N", "N");
		return populate;
	}

	@ModelAttribute("setorList")
	public Collection<Setor> populateSetors() {
		List<Setor> setores = setorDao.findAll();
		return setores;
	}	
	
}