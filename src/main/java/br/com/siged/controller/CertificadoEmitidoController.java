package br.com.siged.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.siged.dao.CertificadoEmitidoDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.entidades.CertificadoEmitido;
import br.com.siged.entidades.Evento;
import br.com.siged.filtro.CertificadoEmitidoFiltro;
import br.com.siged.filtro.Email;
import br.com.siged.util.EmailUtil;

@Controller
@RequestMapping("/certificadoEmitido/**")
public class CertificadoEmitidoController {
	
	@Autowired
	private CertificadoEmitidoDAO certificadoEmitidoDao;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private EmailUtil emailUtil;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
	}
	
	@RequestMapping(value = "/certificadoEmitido", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("certificadoEmitidoFiltro", new CertificadoEmitidoFiltro());
		modelMap.addAttribute("certificadosEmitidos", certificadoEmitidoDao.findAll());
		return "certificadoEmitido/list";
	}
	
	@RequestMapping(value = "/certificadoEmitido/search", method = RequestMethod.GET)
	public String search(CertificadoEmitidoFiltro certificadoEmitidoFiltro,
			ModelMap modelMap, ServletRequest Request, ServletResponse Response)
			throws IOException {
		
		modelMap.addAttribute("certificadosEmitidos",
						certificadoEmitidoDao.findCertificadoEmitidoByCriteria(
						certificadoEmitidoFiltro.getEvento(),
						certificadoEmitidoFiltro.getParticipante(),
						certificadoEmitidoFiltro.getData1(),
						certificadoEmitidoFiltro.getData2()));
		
		modelMap.addAttribute("certificadoEmitidoFiltro", certificadoEmitidoFiltro);
		
		return "certificadoEmitido/list";
	}

	@RequestMapping(value = "/certificadoEmitido/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		
		try {
		
			CertificadoEmitido certificado = certificadoEmitidoDao.find(id);
			
			Email email = new Email();
			email.setTipoDoEvento(certificado.getEvento().getTipoId().getDescricao());
			email.setTitulo(certificado.getEvento().getTitulo());
			email.setNomeParticipante(certificado.getParticipante().getNome());
			email.setCodigoCertificado(certificado.getCodigoVerificacaoComMascara());
			email.setTo(certificado.getParticipante().getEmail());
			
			certificadoEmitidoDao.remove(certificado);		
		
			emailUtil.emailCertificadoInvalidadoDoEvento(email);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/certificadoEmitido?mensagem=Ocorreu um erro ao enviar o email ao participante sobre o certificado invalidado.";
		}				
		
		return "redirect:/certificadoEmitido";
	}
	
	
	@RequestMapping(value = "/verificar/form", method = RequestMethod.GET)
	public String certificadoemitidoform(ModelMap modelMap) {
		modelMap.addAttribute("certificadoEmitido", new CertificadoEmitido());
		return "certificadoEmitido/verificar";
	}
		
	@RequestMapping(value = "/verificar", method = RequestMethod.POST)
	public String certificadoemitidolist(@ModelAttribute("certificadoEmitido") @Valid CertificadoEmitido certificadoEmitidoForm, ModelMap modelMap) {
		CertificadoEmitido certificadoEmitido = certificadoEmitidoDao.findByCodigoVerificacao(certificadoEmitidoForm.getCodigoVerificacao());
		
		List<CertificadoEmitido> certificadosEmitidos = new ArrayList<CertificadoEmitido>();
		
		if(certificadoEmitido != null){
			certificadosEmitidos.add(certificadoEmitido);
		}else{
			modelMap.addAttribute("mensagem", "Código inválido");
		}
		
		modelMap.addAttribute("certificadosEmitidos", certificadosEmitidos);						
		
		return "certificadoEmitido/verificar";
	}
	
	
	
	@ModelAttribute("eventosComCertificadoEmitido")
	public Collection<Evento> populateEventosRealizadosTerceirosList() {
		return eventoDao.findEventosComCertificadosEmitidos();
	}	

}
