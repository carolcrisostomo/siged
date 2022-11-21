package br.com.siged.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.siged.dao.CertificadoEmitidoDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.InstrutorDAO;
import br.com.siged.dao.ModuloDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dto.relatorio.certificados.CertificadoInstrutorDTO;
import br.com.siged.entidades.CertificadoEmitido;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Instrutor;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.Usuario;
import br.com.siged.filtro.EventoFiltro;
import br.com.siged.filtro.RelatorioFiltro;
import br.com.siged.service.AuthenticationService;
import br.com.siged.service.CertificadoService;
import br.com.siged.service.RelatorioService;
import br.com.siged.service.exception.HttpStatusException;
import br.com.siged.service.exception.NaoPodeEmitirCertificadoException;
import br.com.siged.util.Util;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@RequestMapping("/certificado/**")
public class CertificadoController {
	
	@Autowired
	private CertificadoEmitidoDAO certificadoEmitidoDao;	
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ModuloDAO moduloDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private InstrutorDAO instrutorDao;
	@Autowired
	private CertificadoService certificadoService;
	@Autowired
	private RelatorioService relatorioService;
	@Autowired
	private Util util;
	
	
	@RequestMapping(value = "/certificado/impressao", method = RequestMethod.GET)
	public String certificados(ModelMap modelMap, HttpServletRequest request) {
		
		List<Evento> eventos = montarEventosAdminOuAluno(request);
		modelMap.addAttribute("eventos", eventos);
		modelMap.addAttribute("relCertificado", new RelatorioFiltro());
		
		return "certificado/certificados";
	}

	
	@RequestMapping(value = "/certificado/impressao", method = RequestMethod.POST)
	public String certificados(
			@ModelAttribute("relCertificado") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		List<Evento> eventos = montarEventosAdminOuAluno(request);
		modelMap.addAttribute("eventos", eventos);
		
		if (relatorioFiltro.getEventoId() == null || relatorioFiltro.getEventoId() == 0) {
			result.addError(new FieldError("relatorioFiltro", "eventoId",	"Campo Obrigatório"));
		}
		if (relatorioFiltro.getParticipanteId() == null || relatorioFiltro.getParticipanteId() == 0) {
			result.addError(new FieldError("relatorioFiltro", "participanteId", "Campo Obrigatório"));
		}
		if (result.hasErrors()) {			
			return "/certificado/certificados";
		}
		
		Evento evento = eventoDao.findByIdLazy(relatorioFiltro.getEventoId());
		Participante participante = participanteDao.findById(relatorioFiltro.getParticipanteId());
		CertificadoEmitido certificadoEmitido = null;
		String filename = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		
		try {
			certificadoEmitido = certificadoService.salvar(evento, participante);
			filename = certificadoService.montarLayoutCertificado(evento, parameters);
		} catch (NaoPodeEmitirCertificadoException e) {
			modelMap.addAttribute("mensagemErro", e.getMessage());
			return "certificado/certificados";
		}
		
		String paramWhere = "WHERE inscricao.confirmada = 'S' AND certificado_emitido.codigoverificacao = '" + certificadoEmitido.getCodigoVerificacao() + "'";
		parameters.put("paramWhere", paramWhere);
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);
		
		byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);

		if (relatorio.length < 1024) {
			modelMap.addAttribute("relCertificado", relatorioFiltro);
			modelMap.addAttribute("mensagemRel", "Os filtros selecionados não geraram resultado.");
			return "certificado/certificados";
		}

		relatorioService.abrirPdf(relatorio, "certificado", response);
		return null;
		
	}	
	

	@RequestMapping(value = "/certificado/impressao/{eventoId}/{participanteId}", method = RequestMethod.GET)
	public String certificadosAluno(
				@ModelAttribute("eventofiltro") @Valid EventoFiltro eventofiltro,
				@PathVariable("eventoId") Long eventoId,
				@PathVariable("participanteId") Long participanteId,
				ModelMap modelMap, HttpSession session,
				HttpServletResponse response) throws IOException {
			
		session.setAttribute("certificado_path", null);
		
		return certificadoService.gerarCertificado(eventoId, participanteId, modelMap, response);
		
	}
	
		
	@RequestMapping(value = "/certificado/certificadoEmitido/{codidoVerificao}", method = RequestMethod.GET)
	public String emitirCertificado(
			@PathVariable("codidoVerificao") String codigo,
			ModelMap modelMap, ServletRequest request,
			HttpServletResponse response) throws IOException {
		
		CertificadoEmitido certificado = certificadoEmitidoDao.findByCodigoVerificacao(codigo);
		
		Usuario usuarioLogado = null;
		try {
			usuarioLogado = AuthenticationService.getUsuarioLogado();
		} catch (ClassCastException e) {
			usuarioLogado = null;
		}
		
		if (certificado != null && certificado.getCodigoVerificacao() != null) {
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			String filename = null;
			
			String paramWhere = "WHERE inscricao.confirmada = 'S' AND certificado_emitido.codigoverificacao = '" + certificado.getCodigoVerificacao() + "'";
			parameters.put("paramWhere", paramWhere);
			
			try {
				filename = certificadoService.montarLayoutCertificado(certificado.getEvento(), parameters);
			} catch (NaoPodeEmitirCertificadoException e) {
				modelMap.addAttribute("mensagemErro", e.getMessage());
				if(usuarioLogado != null && usuarioLogado.isAdministrador()){
					return "redirect:/certificadoEmitido/";
				} else {
					return "redirect:/certificadoEmitido/verificar/form";
				}
			}

			InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);
			byte[] relatorio = relatorioService.chamarRelatorio(is, parameters);
			relatorioService.abrirPdf(relatorio, "certificado", response);

			return null;
		
		} else {
			modelMap.addAttribute("mensagemErro", "Não foi possível emitir o Certificado. Favor entrar em contato com o IPC.");
			if(usuarioLogado != null && usuarioLogado.isAdministrador()){
				return "redirect:/certificadoEmitido/";
			} else {
				return "redirect:/certificadoEmitido/verificar/form";
			}
			
		}
	}
		
	
	
	// CERTIFICADO DE INSTRUTOR
	
	/**
	 * View de impressão de certificao de instrutor para perfil de Administrador
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/certificado/impressaoInstrutor", method = RequestMethod.GET)
	public String certificadosInstrutores(ModelMap modelMap) {
		modelMap.addAttribute("relCertificado", new RelatorioFiltro());
		modelMap.addAttribute("eventosParaCertificadoInstrutor", eventoDao.findForCertificadoDeInstrutor("", false));
		return "certificado/certificadoInstrutor";
	}
	
	/**
	 * Impressão de certificao de instrutor para perfil de Administrador
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/certificado/impressaoInstrutor", method = RequestMethod.POST)
	public String certificadosInstrutores(
			@ModelAttribute("relCertificado") @Valid RelatorioFiltro relatorioFiltro,
			BindingResult result, 
			ModelMap modelMap, 
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		return certificadoInstrutor(relatorioFiltro, result, modelMap, request, response, false);
	}
	
	/**
	 * Endpoint para filtrar o evento na lista de eventos ministrados pelo instrutor.<br/>
	 * Obs.: Todo instrutor tem o perfil de ALUNO
	 * @param eventoId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/certificado/impressaoInstrutorLogado/{eventoId}", method = RequestMethod.GET)
	public String certificadoInstrutorLogadoMeusEventos(@PathVariable Long eventoId, ModelMap model) {
		RelatorioFiltro filtro = new RelatorioFiltro();
		filtro.setEventoId(eventoId);
		return certificadoInstrutorLogado(model, filtro);
	}
	
	/**
	 * View de impressão de certificao de instrutor para perfil de Aluno.<br/>
	 * Obs.: Todo instrutor tem o perfil de ALUNO
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/certificado/impressaoInstrutorLogado", method = RequestMethod.GET)
	public String certificadoInstrutorLogado(ModelMap modelMap, RelatorioFiltro filtro) {
		if(filtro != null) {
			modelMap.addAttribute("relCertificado", filtro);
		} else {
			modelMap.addAttribute("relCertificado", new RelatorioFiltro());
		}
		modelMap.addAttribute("eventosParaCertificadoInstrutor", eventoDao.findForCertificadoDeInstrutor("", true));
		return "certificado/certificadoInstrutorLogado";
	}
	
	/**
	 * Impressão de certificao de instrutor para perfil de Aluno.<br/>
	 * Obs.: Todo instrutor tem o perfil de ALUNO
	 * @param result
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/certificado/impressaoInstrutorLogado", method = RequestMethod.POST)
	public String certificadoInstrutorLogado(
			@ModelAttribute("relCertificado") @Valid RelatorioFiltro relatorioFiltro, BindingResult result, ModelMap modelMap, 
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		return certificadoInstrutor(relatorioFiltro, result, modelMap, request, response, true);
	}
	
	private List<Evento> montarEventosAdminOuAluno(HttpServletRequest request) {
		List<Evento> eventos = new ArrayList<>();
		if (request.isUserInRole("ROLE_ADMINISTRADOR") || request.isUserInRole("ROLE_ADMINISTRADORCONS")) {
			eventos = eventoDao.findEventosApuradosDoIPCAndRedeEscolas();
		}else if(request.isUserInRole("ROLE_ALUNO")){
			Participante participante = participanteDao.find((long)request.getSession().getAttribute("PARTICIPANTE_ID"));
			eventos = eventoDao.findEventosIPCAndRedeEscolasByParticipanteAprovado(participante.getId());
		}
		return eventos;
	}
	
	private String certificadoInstrutor(RelatorioFiltro relatorioFiltro, BindingResult result, ModelMap modelMap, 
			HttpServletRequest request,
			HttpServletResponse response, 
			Boolean instrutorLogado) throws IOException {

		if (relatorioFiltro.getEventoId() == null || relatorioFiltro.getEventoId() == 0) {
			result.addError(new FieldError("relatorioFiltro", "eventoId", "Campo Obrigatório"));
		}
		
		if (relatorioFiltro.getModulo() == null || relatorioFiltro.getModulo() == 0) {
			result.addError(new FieldError("relatorioFiltro", "modulo", "Campo Obrigatório"));
		}

		String certificadoInstrutorPage = "";
		String certificadoInstrutorRedirectPage = "";
		Instrutor instrutor = null;
		if(instrutorLogado) {
			Usuario usuarioLogado = AuthenticationService.getUsuarioLogadoOrNull();
			if (usuarioLogado == null) {
				throw new HttpStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
			}
			certificadoInstrutorPage = "certificado/certificadoInstrutorLogado";
			certificadoInstrutorRedirectPage = "redirect:/certificado/impressaoInstrutorLogado";
			instrutor = instrutorDao.findByCpf(util.formatarCpf(usuarioLogado.getCpf()));
		} else {
			if (relatorioFiltro.getInstrutorId() == null || relatorioFiltro.getInstrutorId() == 0) {
				result.addError(new FieldError("relatorioFiltro", "instrutorId", "Campo Obrigatório"));
			}
			certificadoInstrutorPage = "certificado/certificadoInstrutor";
			certificadoInstrutorRedirectPage = "redirect:/certificado/impressaoInstrutor";
			instrutor = instrutorDao.findById(relatorioFiltro.getInstrutorId());
		}
		
		if (result.hasErrors()) {
			modelMap.addAttribute("eventosParaCertificadoInstrutor", eventoDao.findForCertificadoDeInstrutor("", instrutorLogado));
			return certificadoInstrutorPage;
		}

		CertificadoInstrutorDTO certificadoInstrutor = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		List<CertificadoInstrutorDTO> certificadoDTOList = new ArrayList<>();
		
		Evento evento = eventoDao.findById(relatorioFiltro.getEventoId());
		Modulo modulo = moduloDao.findById(relatorioFiltro.getModulo());
		
		try {
			certificadoInstrutor = certificadoService.montarLayoutCertificadoInstrutor(evento, modulo, instrutor, parameters);
			certificadoDTOList.add(certificadoInstrutor);
		} catch (NaoPodeEmitirCertificadoException e) {
			modelMap.addAttribute("eventosParaCertificadoInstrutor", eventoDao.findForCertificadoDeInstrutor("", instrutorLogado));
			return certificadoInstrutorRedirectPage + "?mensagemErro=" + e.getMessage();
		}
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(certificadoInstrutor.getFilename());
		
		byte[] relatorio = relatorioService.chamarRelatorioJRdataSource(is, parameters, new JRBeanCollectionDataSource(certificadoDTOList));
		relatorioService.abrirPdf(relatorio, "certificadoInstrutor", response);

		return null;
	}
	
}
