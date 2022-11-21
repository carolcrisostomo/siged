package br.com.siged.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.EventoMaterialDAO;
import br.com.siged.dao.ModuloDAO;
import br.com.siged.dao.ParametroDAO;
import br.com.siged.editor.EventoEditor;
import br.com.siged.editor.ModuloEditor;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.EventoMaterial;
import br.com.siged.entidades.Inscricao;
import br.com.siged.entidades.Modulo;
import br.com.siged.filtro.EventoMaterialFiltro;
import br.com.siged.mailing.MalaDireta;
import br.com.siged.util.EmailUtil;
import br.com.siged.util.Util;

@Controller
@RequestMapping("/material/**")
public class EventoMaterialController {
	
	private @Value("${siged.material.caminho}") String materialPath;

	@Autowired
	private EventoMaterialDAO eventoMaterialDao;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ModuloDAO moduloDao;
	@Autowired
	private ParametroDAO parametroDao;
	@Autowired
	private Util util;
	@Autowired
	private EmailUtil emailUtil;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(br.com.siged.entidades.Evento.class,
				new EventoEditor(eventoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Modulo.class,
				new ModuloEditor(moduloDao));
		dataBinder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}

	@RequestMapping(value = "/material/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("material", eventoMaterialDao.find(id));
		return "material/show";
	}

	@RequestMapping(value = "/material", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("materialFiltro", new EventoMaterialFiltro());
		modelMap.addAttribute("materiais", eventoMaterialDao.findAll());
		return "material/list";
	}

	@RequestMapping(value = "/material/search", method = RequestMethod.GET)
	public String search(EventoMaterialFiltro eventoMaterialFiltro,
			ModelMap modelMap, ServletRequest Request, ServletResponse Response)
			throws IOException {
		modelMap.addAttribute("materialFiltro", eventoMaterialFiltro);
		modelMap.addAttribute("materiais", eventoMaterialDao
				.findMaterialByCriteria(eventoMaterialFiltro.getEventoId(),
						eventoMaterialFiltro.getModuloId(),
						eventoMaterialFiltro.getMaterialTipo(),
						eventoMaterialFiltro.getDescricao()));
		return "material/list";
	}

	@RequestMapping(value = "/material/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		
		EventoMaterial material = eventoMaterialDao.find(id);
		eventoMaterialDao.remove(material);
		
		/*Parametro materialPath = parametroDao.findByNome("MATERIALPATH");
		
		String caminho = materialPath.getValor()
				+ util.getAnoArquivo(material.getArquivoTCE()) + "/"
				+ material.getArquivoTCE();*/
		
		String caminho = this.materialPath
				+ util.getAnoArquivo(material.getArquivoTCE()) + "/"
				+ material.getArquivoTCE();
		
		caminho = caminho.replace("/", File.separator);
		
		File file = new File(caminho);
		file.delete();
		
		return "redirect:/material";
	}

	@RequestMapping(value = "/material/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("material", new EventoMaterial());
		return "material/create";
	}

	@RequestMapping(value = "/material", method = RequestMethod.POST)
	public String create(
			@ModelAttribute("material") @Valid EventoMaterial material,
			BindingResult result,
			@RequestParam("arquivo") MultipartFile arquivo, ModelMap modelMap)
			throws IOException, Exception {

		System.out.println(arquivo.getOriginalFilename());
		System.out.println(arquivo.getSize());

		if (material.getMaterialTipo() == 0L)
			result.addError(new FieldError("material", "materialTipo",
					"Campo Obrigatório"));
		

		if (material.getDescricao().equals(""))
			result.addError(new FieldError("material", "descricao",
					"Campo Obrigatório"));

		String[] nomeEExtensao = null;

		if (arquivo.getSize() == 0L)
			result.addError(new FieldError("material", "arquivo",
					"Selecione um arquivo"));
		else {
			
			long arquivoMaxSize = Long.parseLong(parametroDao.findByNome("ARQUIVOMAXSIZE").getValor());
			
			if(arquivo.getSize() > arquivoMaxSize){
				result.addError(new FieldError("material", "arquivo",
						"Arquivo muito grande. Selecione um arquivo de até 10MB"));
			}else{
				nomeEExtensao = arquivo.getOriginalFilename().split("\\.");

				if (nomeEExtensao.length < 2)
					result.addError(new FieldError("material", "arquivo",
							"Selecione um arquivo válido"));
			}
			
		}
		
		if(material.getMaterialTipo() == 1 && material.getModuloId() == null){
			result.addError(new FieldError("material", "moduloId",
					"Campo Obrigatório"));
		}

		if (result.hasErrors()) {
			return "material/create";
		}

		material.setArquivoOriginal(arquivo.getOriginalFilename());
		
		material.setArquivoTCE(util.getNovoNomeArquivo() + "."
				+ nomeEExtensao[nomeEExtensao.length - 1]);
		
		material.setArquivoTipo(arquivo.getContentType());

		/*Parametro materialPath = parametroDao.findByNome("MATERIALPATH");
		
		String caminho = materialPath.getValor()
				+ util.getAnoArquivo(material.getArquivoTCE()) + "/"
				+ material.getArquivoTCE();*/
		
		String caminho = this.materialPath
				+ util.getAnoArquivo(material.getArquivoTCE()) + "/"
				+ material.getArquivoTCE();
		
		caminho = caminho.replace("/", File.separator);
		
		boolean enviarEmail = false;
		List<String> emails = new ArrayList<>();
		if(material.isMaterialDidatico()) {
			for(Inscricao inscricao : material.getEventoId().getInscricaoList()) {
				if(inscricao.getConfirmada().equals("S") && 
					inscricao.getParticipanteId().getEmail() != null && 
					!inscricao.getParticipanteId().getEmail().isEmpty()
				) {
					emails.add(inscricao.getParticipanteId().getEmail());
				}
			}
			if(emails.size() > MalaDireta.LIMIT && MalaDireta.isAlive()) {
				return "redirect:/material?mensagemErro=Esta inclusão de material não pode ser realizada enquanto estiverem sendo enviados os e-mails relativos à inclusão de outro material didático. Favor aguardar!";
			}
			
			if (!emails.isEmpty()) { enviarEmail = true; }
		}
		
		File destino = new File(caminho);
		arquivo.transferTo(destino);
		
		eventoMaterialDao.persist(material);
		if (enviarEmail) { 
			emailUtil.emailInclusaoMaterialDidatico(material.getEventoId(), emails, material.getDescricao());
			return "redirect:/material?mensagem=E-mail sobre a inclusão de material didático enviado aos participantes com sucesso!";
		}
		
		return "redirect:/material";
	} 

	@RequestMapping(value = "/material/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("material", eventoMaterialDao.find(id));
		return "material/update";
	}

	@RequestMapping(value = "/material/{id}/update", method = RequestMethod.POST)
	public String update(
			@ModelAttribute("material") @Valid EventoMaterial material,
			BindingResult result,
			@RequestParam("arquivo") MultipartFile arquivo, ModelMap modelMap)
			throws IOException {

		if (material.getMaterialTipo() == 0L)
			result.addError(new FieldError("material", "materialTipo",
					"Campo Obrigatório"));

		if (material.getDescricao().equals(""))
			result.addError(new FieldError("material", "descricao",
					"Campo Obrigatório"));

		String[] nomeEExtensao = null;

		if (arquivo.getSize() > 0L){			
			
			long arquivoMaxSize = Long.parseLong(parametroDao.findByNome("ARQUIVOMAXSIZE").getValor());
			
			if(arquivo.getSize() > arquivoMaxSize){
				result.addError(new FieldError("material", "arquivo",
						"Arquivo muito grande. Selecione um arquivo de até 10MB"));
			}else{
				nomeEExtensao = arquivo.getOriginalFilename().split("\\.");

				if (nomeEExtensao.length < 2)
					result.addError(new FieldError("material", "arquivo",
							"Selecione um arquivo válido"));
			}
			
		}
		System.out.println(arquivo.getName());
		
		if(material.getMaterialTipo() == 1 && material.getModuloId() == null){
			result.addError(new FieldError("material", "moduloId",
					"Campo Obrigatório"));
		}

		if (result.hasErrors()) {
			modelMap.addAttribute("material", material);
			return "material/update";
		}		

		EventoMaterial materialAntigo = eventoMaterialDao.find(material.getId());

		if (arquivo.getSize() > 0L) {
		
			/*Parametro materialPath = parametroDao.findByNome("MATERIALPATH");
			
			String caminho = materialPath.getValor()
					+ util.getAnoArquivo(materialAntigo.getArquivoTCE()) + "/"
					+ materialAntigo.getArquivoTCE();*/
			
			String caminho = this.materialPath
					+ util.getAnoArquivo(material.getArquivoTCE()) + "/"
					+ material.getArquivoTCE();
			
			caminho = caminho.replace("/", File.separator);
			
			File antigo = new File(caminho);
			antigo.delete();
						
			material.setArquivoOriginal(arquivo.getOriginalFilename());
			
			material.setArquivoTCE(util.getNovoNomeArquivo() + "."
					+ nomeEExtensao[nomeEExtensao.length - 1]);
			
			material.setArquivoTipo(arquivo.getContentType());

			/*caminho = materialPath.getValor()
					+ util.getAnoArquivo(material.getArquivoTCE()) + "/"
					+ material.getArquivoTCE();*/
			
			caminho = this.materialPath
					+ util.getAnoArquivo(material.getArquivoTCE()) + "/"
					+ material.getArquivoTCE();	
			
			caminho = caminho.replace("/", File.separator);

			File novo = new File(caminho);
			arquivo.transferTo(novo);

		} else {
			material.setArquivoOriginal(materialAntigo.getArquivoOriginal());
			material.setArquivoTCE(materialAntigo.getArquivoTCE());
			material.setArquivoTipo(materialAntigo.getArquivoTipo());
		}

		eventoMaterialDao.merge(material);

		return "redirect:/material";
	}

	@RequestMapping("/material/arquivo/{id}")
	public String download(@PathVariable("id") Long materialId,
			HttpServletResponse response) {

		EventoMaterial material = eventoMaterialDao.find(materialId);
		
		try {
			
			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ material.getArquivoOriginal() + "\"");			
			
			response.setContentType(material.getArquivoTipo());

			/*Parametro materialPath = parametroDao.findByNome("MATERIALPATH");
			
			String caminho = materialPath.getValor()
					+ util.getAnoArquivo(material.getArquivoTCE()) + "/"
					+ material.getArquivoTCE();*/
			
			String caminho = this.materialPath
					+ util.getAnoArquivo(material.getArquivoTCE()) + "/"
					+ material.getArquivoTCE();
			
			caminho = caminho.replace("/", File.separator);
			
			System.out.println(caminho);
			
			File file = new File(caminho);
			InputStream in = new FileInputStream(file);

			OutputStream out = response.getOutputStream();
			
			IOUtils.copy(in, out);

			in.close();
			
			out.flush();
			out.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}	

	@RequestMapping(value = "/material/didatico/{id}", method = RequestMethod.GET)
	public String didatico(@PathVariable("id") Long eventoId, ModelMap modelMap) {

		List<EventoMaterial> materialList = eventoMaterialDao
				.findByEventoETipo(eventoId, 1L);

		modelMap.addAttribute("materiais", materialList);
		modelMap.addAttribute("evento", eventoDao.find(eventoId));

		return "material/didatico";
	}

	@RequestMapping(value = "/material/procuraModulo", method = RequestMethod.GET)
	@ResponseBody
	public List<Modulo> procuraModulo(
			@RequestParam(value = "eventoId") Long eventoId) {

		return moduloDao.findByEventoId(eventoId);
	}

		
	@ModelAttribute("eventoList")
	public Collection<Evento> populateEventos() {
		return eventoDao.findAll();
	}

	@ModelAttribute("materialTipoList")
	public Map<String, String> populatematerialTipo() {

		Map<String, String> populate = new LinkedHashMap<String, String>();
		populate.put("1", "MATERIAL DIDÁTICO");
		populate.put("2", "MATERIAL DE DIVULGAÇÃO");
		return populate;
	}

}