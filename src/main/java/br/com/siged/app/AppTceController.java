package br.com.siged.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nimbusds.jwt.SignedJWT;

import br.com.siged.app.to.AutenticacaoTO;
import br.com.siged.app.to.DesempenhoTO;
import br.com.siged.app.to.EventoTO;
import br.com.siged.app.to.MensagemTO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.Usuario;
import br.com.siged.service.AuthenticationService;
import br.com.siged.service.CertificadoService;
import br.com.siged.service.DesempenhoService;

@Controller
@RequestMapping("/app/**")
public class AppTceController {
	
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private CertificadoService certificadoService;
	@Autowired
	private DesempenhoService desempenhoService;
	@Autowired
	private AuthenticationService authenticationService;
		
	
	@RequestMapping(value = "/app/autentica", method = RequestMethod.POST)
	public @ResponseBody Object autentica(HttpServletRequest request){
		
		try {
			
			String usuario = request.getParameter("usuario"); 
			String senha = request.getParameter("senha");
			
			authenticationService.login(usuario, senha);
			
			Usuario usuarioLogado = AuthenticationService.getUsuarioLogado();
			
			AutenticacaoTO to = new AutenticacaoTO(usuarioLogado);
			
			//Primeiro parâmentro indica o username, mas neste caso foi passado o CPF
			to.setToken(JwtUtils.generateHMACToken(to.getCpf(), usuarioLogado.getAuthorities(), JwtUtils.getSecretKey(), JwtUtils.TOKEN_EXPIRATION_MINUTES));
						
			return to;			
		
		} catch (Exception e) {
			e.printStackTrace();
			return new MensagemTO(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/app/eventos/{situacao}/{registroInicial}/{quantidadeDeRegistros}", method = RequestMethod.GET)
	public @ResponseBody List<EventoTO> eventos(
			@PathVariable String situacao, 
			@PathVariable int registroInicial,
			@PathVariable int quantidadeDeRegistros) throws Exception {
			
		List<Evento> eventos = new ArrayList<Evento>();
		
		if ("previstos".equals(situacao)) {
			eventos = eventoDao.findByPrevisto(registroInicial, quantidadeDeRegistros);
		} else if ("emandamento".equals(situacao)) {
			eventos = eventoDao.findByEmAndamento(registroInicial, quantidadeDeRegistros);
		}else if ("realizados".equals(situacao)){
			eventos = eventoDao.findByRealizado(registroInicial, quantidadeDeRegistros);
		}		
		
		return toListEventoTO(eventos, false, false, null);
	}	
	
	
	@RequestMapping(value = "/app/meuseventos/{registroInicial}/{quantidadeDeRegistros}", method = RequestMethod.GET)
	public @ResponseBody List<?> meusEventos( 
			@PathVariable int registroInicial,
			@PathVariable int quantidadeDeRegistros,
			HttpServletRequest request) throws Exception {		
		
		try {
			
			String token = request.getParameter("token");
			
			if ( token != null ) {			
				
				String cpf = validaToken(token);
				
				Participante participante = participanteDao.findByCpf(cpf);
				
				List<Evento> eventos = eventoDao.findMeuEventoByCriteria(cpf, null, null, null, null, null);			
				
				return toListEventoTO(eventos, true, true, participante);		
			
			} else {
				
				return Arrays.asList(new MensagemTO("É obrigatório o envio do token gerado pela autenticação no SIGED."));
			}
			
		} catch (Exception e){
			
			e.printStackTrace();
			return Arrays.asList(new MensagemTO(e.getMessage()));
			
		}
		
	}
	
	
	@RequestMapping(value = "/app/certificado/{eventoId}/{participanteId}", method = RequestMethod.GET)
	public @ResponseBody MensagemTO certificadosAluno(
				@PathVariable("eventoId") Long eventoId,
				@PathVariable("participanteId") Long participanteId,
				ModelMap modelMap, 
				HttpServletRequest request,
				HttpServletResponse response) throws IOException {
		
		try {
			
			String token = request.getParameter("token");
		
			if ( token != null ) {
				
				validaToken(token);
			
				String mensagem = "ok";		
		
				String retorno = certificadoService.gerarCertificado(eventoId, participanteId, modelMap, response);
								
				if (retorno != null && retorno.length() > 8 && retorno.substring(0, 8).equals("redirect") ) {
				
					mensagem = String.valueOf(modelMap.get("mensagemErro"));
				
				}				
					
				return new MensagemTO(mensagem);
			
			} else {
				
				return new MensagemTO("É obrigatório o envio do token gerado pela autenticação no SIGED.");
				
			}
		
		} catch (Exception e) {
			
			e.printStackTrace();
			return new MensagemTO(e.getMessage());
			
		}		
		
		
	}
	
	
	private String validaToken(String token) throws Exception{
		
		try{
			
			SignedJWT signedJWT = JwtUtils.parse(token);
			
			//JwtUtils.assertNotExpired(signedJWT);
			JwtUtils.assertValidSignature(signedJWT, JwtUtils.getSecretKey());
			
			//Retornará o CPF ao invés do username
			return JwtUtils.getUsername(signedJWT);		
		
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());			
		}
		
	}
	
	private List<EventoTO> toListEventoTO (List<Evento> eventos, boolean incluirCertificados, boolean incluirDesempenho, Participante participante) throws Exception{
		
		List<EventoTO> eventosJSON = new ArrayList<EventoTO>();	
		
		if (!eventos.isEmpty()) {
			
			for (Evento evento: eventos){
				
				EventoTO eventoTO = new EventoTO(evento); 
				
				if( incluirDesempenho && participante != null) {
					
					eventoTO.setDesempenho(new DesempenhoTO(
							
							desempenhoService.statusDesempenhoParticipanteNoEvento(participante, evento).getDescricao(),
							desempenhoService.notaDoParticipanteNoEvento(participante, evento),
							desempenhoService.frequenciaDoParticipanteNoEvento(participante, evento)
					));
					
				}
				
				
				if( incluirCertificados 
						&& participante != null
						&& certificadoService.podeEmitirCertificado(participante, evento) ){
					
					eventoTO.setLinkCertificado("certificado/" + evento.getId() + "/" + participante.getId());
					
				}
				
				eventosJSON.add(eventoTO);
			}
		}
		
		return eventosJSON;
	}
	
	

}
