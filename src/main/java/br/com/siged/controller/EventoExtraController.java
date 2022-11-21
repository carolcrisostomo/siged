package br.com.siged.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import br.com.siged.controller.validators.EventoExtraValidator;
import br.com.siged.dao.EventoExtraDAO;
import br.com.siged.dao.InscricaoDAO;
import br.com.siged.dao.MunicipioDAO;
import br.com.siged.dao.PaisDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.ResponsavelSetorDAO;
import br.com.siged.dao.UfMunicipioDAO;
import br.com.siged.dao.UsuarioSCADAO;
import br.com.siged.editor.InscricaoEditor;
import br.com.siged.editor.MunicipioEditor;
import br.com.siged.editor.PaisEditor;
import br.com.siged.editor.UfMunicipioEditor;
import br.com.siged.editor.UsuarioSCAEditor;
import br.com.siged.entidades.EventoExtra;
import br.com.siged.entidades.Pais;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.ResponsavelSetor;
import br.com.siged.entidades.externas.Setor;
import br.com.siged.entidades.externas.UfMunicipio;
import br.com.siged.entidades.externas.UsuarioSCA;
import br.com.siged.filtro.EventoExtraFiltro;
import br.com.siged.util.EmailUtil;

@Controller
@RequestMapping("/eventoextra/**")
public class EventoExtraController {
	
	@Autowired
	private EventoExtraDAO eventoextraDao;
	@Autowired
	private PaisDAO paisDao;
	@Autowired
	private MunicipioDAO municipioDao;
	@Autowired
	private UfMunicipioDAO ufMunicipioDao;
	@Autowired
	private UsuarioSCADAO usuarioDao;	
	@Autowired
	private ParticipanteDAO participanteDao;	
	@Autowired
	private InscricaoDAO inscricaoDao;
	@Autowired
	private ResponsavelSetorDAO responsavelSetorDao;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private EventoExtraValidator eventoExtraValidator;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		dataBinder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Pais.class, new PaisEditor(paisDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.externas.UfMunicipio.class, new UfMunicipioEditor(ufMunicipioDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Municipio.class, new MunicipioEditor(municipioDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.externas.UsuarioSCA.class, new UsuarioSCAEditor(usuarioDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Inscricao.class, new InscricaoEditor(inscricaoDao));
		DecimalFormat df = new DecimalFormat("#,###,###.00");
		dataBinder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, df, true));
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
		dataBinder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		
		if(dataBinder.getTarget() instanceof EventoExtra) {
			dataBinder.setValidator(eventoExtraValidator);
		}
	}

	@RequestMapping(value = "/eventoextra/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap, HttpServletRequest request) {
		
		EventoExtra solicitacao = eventoextraDao.find(id);
		
		UsuarioSCA usuarioLogado = usuarioDao.findByLogin(request.getRemoteUser());

		if (request.isUserInRole("ROLE_ADMINISTRADOR") || usuarioLogado.equals(solicitacao.getSolicitanteId())) {
			modelMap.addAttribute("exibirBotoes", true);		
		}
		
		modelMap.addAttribute("eventoextra", solicitacao);
		modelMap.addAttribute("participante", participanteDao.findByCpf(eventoextraDao.find(id).getSolicitanteId().getCpf().replace(".", "").replace("-", "")));
		if (inscricaoDao.findBySolicitacaoId(id) != null) {
			modelMap.addAttribute("inscricao", inscricaoDao.findBySolicitacaoId(id));
		}
		return "eventoextra/show";
	}

	@RequestMapping(value = "/eventoextra", method = RequestMethod.GET)
	public String list(ModelMap modelMap, HttpServletRequest request) {
		modelMap.addAttribute("eventoextrafiltro", new EventoExtraFiltro());

		if (request.isUserInRole("ROLE_SERVIDOR")) {
			String usuario = request.getRemoteUser();
			modelMap.addAttribute("eventosextras", eventoextraDao.findBySolicitanteLogin(usuario));
		} else
			modelMap.addAttribute("eventosextras", eventoextraDao.findAll());

		return "eventoextra/list";
	}

	@RequestMapping(value = "/eventoextra/search", method = RequestMethod.GET)
	public String search(EventoExtraFiltro eventoextrafiltro,
			ModelMap modelMap, ServletRequest Request, ServletResponse Response)
			throws IOException, ParseException {
		if (eventoextrafiltro.getServidor() != null) {
			if (eventoextrafiltro.getServidor() != 0) {
				eventoextrafiltro.setCpf(participanteDao.findById(
						eventoextrafiltro.getServidor()).getCpf());
			} else {
				eventoextrafiltro.setCpf("");
			}
		}
		modelMap.addAttribute("eventoextrafiltro", eventoextrafiltro);
		modelMap.addAttribute(
				"eventosextras",
				eventoextraDao.findEventoExtraByCriteria(
						eventoextrafiltro.getCpf(),
						eventoextrafiltro.getCurso(),
						eventoextrafiltro.getData1(),
						eventoextrafiltro.getData2(),
						eventoextrafiltro.getIndicada()));
		return "eventoextra/list";
	}

	@RequestMapping(value = "/eventoextra/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		
		EventoExtra solicitacao = eventoextraDao.find(id);		
		eventoextraDao.remove(solicitacao);		
		return "redirect:/eventoextra";
	
	}

	@RequestMapping(value = "/eventoextra/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("eventoextra", new EventoExtra());
		return "eventoextra/create";
	}

	@RequestMapping(value = "/eventoextra", method = RequestMethod.POST)
	public String create(
			@ModelAttribute("eventoextra") @Valid EventoExtra eventoextra,
			BindingResult result, ModelMap modelMap,
			HttpServletRequest request,
			@RequestParam("material") MultipartFile material)
			throws IOException {

		if (result.hasErrors()) {
			return "eventoextra/create";
		}

		if (material != null) {
			eventoextra.setMaterial(material);
			eventoextra.setMaterialNome(material.getOriginalFilename());
			eventoextra.setMaterialTipo(material.getContentType());
		}

		UsuarioSCA solicitante = usuarioDao.findByLogin(request.getRemoteUser());
		
		eventoextra.setSolicitanteId(solicitante);
		
		UsuarioSCA chefe = null;		
		
		ResponsavelSetor responsavelSetor = responsavelSetorDao.findAtivoBySetorId(Long.parseLong(request.getSession().getAttribute("SETORID").toString()));
		
		if (responsavelSetor != null) {
			if (solicitante.equals(responsavelSetor.getResponsavel())) {
				chefe = responsavelSetor.getResponsavelImediato();
			} else {
				chefe = responsavelSetor.getResponsavel();
			}
		} else {
			modelMap.addAttribute("mensagem","Seu setor não possui responsável cadastrado no SIGED. Favor entrar em contato com o IPC.");
			return "redirect:/eventoextra";
		}				

		eventoextra.setChefeId(chefe);
		eventoextra.setDataCadastro(new Date());
		eventoextra.setIndicada("N");

		eventoextraDao.persist(eventoextra);

		try {
			emailUtil.emailSolicitacao(eventoextra, (Participante) request.getSession().getAttribute("PARTICIPANTE"), chefe);
		} catch (Exception e) {		
			e.printStackTrace();
		}

		return "redirect:/eventoextra";
	}

	@RequestMapping(value = "/eventoextra/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {

		EventoExtra solicitacao = eventoextraDao.findById(id);				
				
		if(solicitacao.getMunicipio() != null)
			solicitacao.setUfMunicipio(ufMunicipioDao.findByUf(solicitacao.getMunicipio().getUfMunicipio()));

		modelMap.addAttribute("eventoextra", solicitacao);
		modelMap.addAttribute("participante", participanteDao.findByCpf(eventoextraDao.find(id).getSolicitanteId().getCpf().replace(".", "").replace("-", "")));

		return "eventoextra/update";
	}

	@RequestMapping(value = "/eventoextra/{id}/update", method = RequestMethod.POST)
	public String update(
			@ModelAttribute("eventoextra") @Valid EventoExtra eventoextra,
			BindingResult result,
			@RequestParam("material") MultipartFile material,
			ModelMap modelMap, HttpServletRequest request) throws IOException {		

		EventoExtra eventoextraaantigo = eventoextraDao.find(eventoextra.getId());
		if (result.hasErrors()) {
			modelMap.addAttribute("participante", participanteDao.findByCpf(eventoextraaantigo.getSolicitanteId().getCpf().replace(".", "").replace("-", "")));
			return "eventoextra/update";
		}
		
		eventoextra.setSolicitanteId(eventoextraaantigo.getSolicitanteId());

		if (material.getOriginalFilename() != "") {
			eventoextra.setMaterial(material);
			eventoextra.setMaterialNome(material.getOriginalFilename());
			eventoextra.setMaterialTipo(material.getContentType());
		} else {
			eventoextra.setMaterial(eventoextraaantigo.getMaterial());
			eventoextra.setMaterialNome(eventoextraaantigo.getMaterialNome());
			eventoextra.setMaterialTipo(eventoextraaantigo.getMaterialTipo());
		}

		if (request.isUserInRole("ROLE_SERVIDOR") && eventoextra.getChefeId() == null) {
			eventoextra.setChefeId(eventoextraaantigo.getChefeId());
		}

		eventoextraDao.merge(eventoextra);

		return "redirect:/eventoextra";
	}

	@RequestMapping(value = "/eventoextra/indicacao", method = RequestMethod.POST)
	public String indicacao(
			@ModelAttribute("eventoextra") @Valid EventoExtra eventoextrapost,
			BindingResult result, ModelMap modelMap) {
		
		EventoExtra eventoextra = eventoextraDao.find(eventoextrapost.getId());

		if (eventoextrapost.getJustificativachefe() == null) {
			modelMap.addAttribute("eventoextra", eventoextrapost);
			modelMap.addAttribute("mensagemErro", "Você precisa justificar a indicação!");
			return "redirect:/inscricao/minhasindicacoes";
		}
		eventoextra.setJustificativachefe(eventoextrapost
				.getJustificativachefe());
		eventoextra.setDataIndicacao(new Date());
		eventoextra.setIndicada("S");
		eventoextraDao.merge(eventoextra);
		return "redirect:/inscricao/minhasindicacoes";
	}

	@RequestMapping(value = "/eventoextra/{id}/indicar/cancelar", method = RequestMethod.GET)
	public String indicacaocancelar(@PathVariable("id") Long id) {
				
		try{
		
			EventoExtra eventoextra = eventoextraDao.find(id);			
						
			eventoextra.setIndicada("N");
			eventoextra.setJustificativachefe("");
			eventoextra.setDataIndicacao(null);				
			
			UsuarioSCA chefe = null;
			
			Participante participante = participanteDao.findByCpf(eventoextra.getSolicitanteId().getCpf());
			
			if (participante.getTipoId().getId() == 1) {							
				
				Setor setor = participante.getSetorId();
				
				ResponsavelSetor responsavelSetor = responsavelSetorDao.findAtivoBySetorId(setor.getId());
									
				if (eventoextra.getSolicitanteId().equals(responsavelSetor.getResponsavel())) {
					chefe = responsavelSetor.getResponsavelImediato();
				} else {
					chefe = responsavelSetor.getResponsavel();
				}				
			}
			
			eventoextra.setChefeId(chefe);			
			
			eventoextraDao.merge(eventoextra);		
			return "redirect:/inscricao/minhasindicacoes";
			
		}catch (Exception e){
			return "redirect:/inscricao/minhasindicacoes?mensagemErro=Não foi possível cancelar a indicação. Favor entrar em contato com o IPC.";
		}			
	}

	@RequestMapping("/eventoextra/material/{id}")
	public String download(@PathVariable("id") Long eventoId,
			HttpServletResponse response) {

		EventoExtra eventoextra = eventoextraDao.findById(eventoId);
		try {
			response.setHeader("Content-Disposition", "inline;filename=\""
					+ eventoextra.getMaterialNome() + "\"");
			OutputStream out = response.getOutputStream();
			response.setContentType(eventoextra.getMaterialTipo());
			IOUtils.copy(new ByteArrayInputStream(eventoextra.getMaterial()),
					out);
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = "/eventoextra/justificativa/{id}", method = RequestMethod.GET)
	public String justificativa(@PathVariable("id") Long id, ModelMap modelMap) {
		EventoExtra eventoextra = eventoextraDao.find(id);
		modelMap.addAttribute("eventoextra", eventoextra);
		return "eventoextra/justificativa";
	}	
	
	
	@ModelAttribute("SNList")
	public Map<String, String> populateSN() {

		Map<String, String> populate = new LinkedHashMap<String, String>();
		populate.put("S", "S");
		populate.put("N", "N");
		return populate;
	}	

	@ModelAttribute("ufList")
	public Collection<UfMunicipio> populateUfs() {
		return ufMunicipioDao.findAll();
	}

	@ModelAttribute("paisList")
	public Collection<Pais> populatePais() {
		return paisDao.findAll();
	}

	@ModelAttribute("chefeList")
	public Collection<UsuarioSCA> populateChefe() {
		return usuarioDao.findServidores();
	}
	
	@ModelAttribute("servidorList")
	public Collection<Participante> populateServidor() {
		return participanteDao.findByTipo(1L);
	}

	@ModelAttribute("turnoList")
	public Map<String, String> populateTurnos() {

		Map<String, String> populate = new LinkedHashMap<String, String>();
		populate.put("MANHÃ", "MANHÃ");
		populate.put("TARDE", "TARDE");
		populate.put("NOITE", "NOITE");
		return populate;
	}
	
}