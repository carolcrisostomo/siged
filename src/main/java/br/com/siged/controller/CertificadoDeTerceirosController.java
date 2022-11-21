package br.com.siged.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.siged.dao.CertificadoDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.editor.EventoEditor;
import br.com.siged.editor.ParticipanteEditor;
import br.com.siged.entidades.Certificado;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Participante;
import br.com.siged.filtro.CertificadoFiltro;

@Controller
@RequestMapping("/certificadoDeTerceiros/**")
public class CertificadoDeTerceirosController {
	
	@Autowired
	private CertificadoDAO certificadoDao;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ParticipanteDAO participanteDao;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(br.com.siged.entidades.Evento.class, new EventoEditor(eventoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Participante.class,	new ParticipanteEditor(participanteDao));
		dataBinder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}

	@RequestMapping(value = "/certificadoDeTerceiros/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("certificado", certificadoDao.find(id));
		return "certificadoDeTerceiros/show";
	}

	@RequestMapping(value = "/certificadoDeTerceiros", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("certificadofiltro", new CertificadoFiltro());
		modelMap.addAttribute("certificados", certificadoDao.findAll());
		return "certificadoDeTerceiros/list";
	}	

	@RequestMapping(value = "/certificadoDeTerceiros/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		certificadoDao.remove(certificadoDao.find(id));
		return "redirect:/certificadoDeTerceiros";
	}

	@RequestMapping(value = "/certificadoDeTerceiros/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("certificado", new Certificado());
		return "certificadoDeTerceiros/create";
	}

	@RequestMapping(value = "/certificadoDeTerceiros", method = RequestMethod.POST)
	public String create(@ModelAttribute("certificado") @Valid Certificado certificado, BindingResult result, @RequestParam("arquivo") MultipartFile arquivo, ModelMap modelMap) throws IOException {
		if (arquivo.getSize() == 0L)
			result.addError(new FieldError("certificado", "arquivo", "Selecione um arquivo "));
		if (arquivo.getSize() != 0L && !arquivo.getName().contains(".pdf")) {
			result.addError(new FieldError("certificado", "arquivo", "Formato de arquivo invalido"));
		}

		if (result.hasErrors()) {
			return "certificadoDeTerceiros/create";
		}

		
		if (arquivo != null) {
			certificado.setArquivo(arquivo.getBytes());
			certificado.setArquivoNome(arquivo.getOriginalFilename());
			certificado.setArquivoTipo(arquivo.getContentType());
		}

		certificadoDao.persist(certificado);
		return "redirect:/certificadoDeTerceiros";
	}

	@RequestMapping(value = "/certificadoDeTerceiros/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("certificado", certificadoDao.find(id));
		return "certificadoDeTerceiros/update";
	}

	@RequestMapping(value = "/certificadoDeTerceiros/{id}/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("certificado") @Valid Certificado certificado, BindingResult result, @RequestParam("arquivo") MultipartFile arquivo, ModelMap modelMap) throws IOException {
		if (result.hasErrors()) {
			modelMap.addAttribute("certificado", certificado);
			return "certificadoDeTerceiros/update";
		}
		if (arquivo.getSize() != 0L && !(arquivo.getOriginalFilename().contains(".pdf") ||  arquivo.getOriginalFilename().contains(".jpg"))) {
			result.addError(new FieldError("instrutor", "curriculo", "Formato de arquivo invalido"));
			return "certificadoDeTerceiros/update";

		}
		if (arquivo.getOriginalFilename() != "") {
			certificado.setArquivo(arquivo);
			certificado.setArquivoNome(arquivo.getOriginalFilename());
			certificado.setArquivoTipo(arquivo.getContentType());
		} else {
			Certificado certificadoantigo = certificadoDao.find(certificado.getId());
			certificado.setArquivo(certificadoantigo.getArquivo());
			certificado.setArquivoNome(certificadoantigo.getArquivoNome());
			certificado.setArquivoTipo(certificadoantigo.getArquivoTipo());
		}

		certificadoDao.merge(certificado);
		return "redirect:/certificadoDeTerceiros";
	}	

	@RequestMapping(value = "/certificadoDeTerceiros/search", method = RequestMethod.GET)
	public String search(CertificadoFiltro certificadofiltro, ModelMap modelMap, ServletRequest Request, ServletResponse Response) throws IOException {
		modelMap.addAttribute("certificadofiltro", certificadofiltro);
		modelMap.addAttribute("certificados", certificadoDao.findCertificadoByCriteria(certificadofiltro.getEvento(), certificadofiltro.getParticipante()));
		return "certificadoDeTerceiros/list";
	}

	@RequestMapping("/certificadoDeTerceiros/arquivo/{id}")
	public String download(@PathVariable("id") Long certificadoId, HttpServletResponse response) {

		Certificado certificado = certificadoDao.findById(certificadoId);
		try {
			response.setHeader("Content-Disposition", "inline;filename=\"" + certificado.getArquivoNome() + "\"");
			OutputStream out = response.getOutputStream();
			response.setContentType(certificado.getArquivoTipo());
			IOUtils.copy(new ByteArrayInputStream(certificado.getArquivo()), out);
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping("/certificadoDeTerceiros/arquivo/{eventoId}/{participanteId}")
	public String downloadMeusEventos(@PathVariable("eventoId") Long eventoId, @PathVariable("participanteId") Long participanteId, ModelMap modelMap, HttpServletResponse response) {

		List<Certificado> certificados = (List<Certificado>) certificadoDao.findCertificadoByCriteria(eventoId, participanteId);

		if (certificados == null || certificados.size() == 0) {
			modelMap.addAttribute("mensagem", "Certificado não disponível. Favor entrar em contato com o IPC.");
			return "redirect:/evento/meuseventos";
		}

		try {
			response.setContentType(certificados.get(0).getArquivoTipo());
			response.setHeader("Content-Disposition", "attachment;filename=\"" + certificados.get(0).getArquivoNome() + "\"");
			OutputStream out = response.getOutputStream();
			IOUtils.copy(new ByteArrayInputStream(certificados.get(0).getArquivo()), out);
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	@RequestMapping(value = "/certificadoDeTerceiros/procuraServidores", method = RequestMethod.GET)
	@ResponseBody
	public List<Participante> procuraServidores(@RequestParam(value = "eventoId") Long eventoId) {
		return participanteDao.findServidoresInscricaoConfirmada(eventoId);
	}
	
	
	@ModelAttribute("eventosRealizadosTerceirosList")
	public Collection<Evento> populateEventosRealizadosTerceirosList() {
		return eventoDao.findRealizadosByProvedorTerceiros();
	}		
	
}
