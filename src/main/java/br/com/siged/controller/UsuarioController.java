package br.com.siged.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.UsuarioDAO;
import br.com.siged.dao.UsuarioSCADAO;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.Usuario;
import br.com.siged.entidades.externas.UsuarioSCA;
import br.com.siged.service.AuthenticationService;
import br.com.siged.service.UsuarioService;
import br.com.siged.service.exception.BusinessException;
import br.com.siged.service.exception.CpfException;
import br.com.siged.service.exception.EmailException;
import br.com.siged.util.EmailUtil;
import br.com.siged.util.Util;

@Controller
@RequestMapping("/usuario/**")
public class UsuarioController {
	
	@Autowired
	private UsuarioSCADAO usuarioSCADao;
	@Autowired
	private UsuarioDAO usuarioDao;	
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private Util util;
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private UsuarioService usuarioService;

	@RequestMapping(value = "/usuario/trocarsenha", method = RequestMethod.GET)
	public String updatesenhaForm(ModelMap modelMap, HttpServletRequest request) {

		Usuario usuario = usuarioDao.findByLogin(request.getRemoteUser());

		modelMap.addAttribute("usuarioexterno", usuario);
		
		return "usuario/trocarsenha";
	}

	@RequestMapping(value = "/usuario/trocarsenha", method = RequestMethod.PUT)
	public String updatesenha(@ModelAttribute("usuarioexterno") Usuario usuarioexterno,	BindingResult result, ModelMap modelMap) {
		
		if (usuarioexterno.getPassword().compareToIgnoreCase("12345") == 0) {
			result.addError(new FieldError("usuarioexterno", "password", "A nova senha deve ser diferente de 12345"));
		} else if (usuarioexterno.getPassword() == null || usuarioexterno.getPassword().equals("")) {
			result.addError(new FieldError("usuarioexterno", "password", "Informe a nova senha!"));
		} else if (usuarioexterno.getPassword().compareToIgnoreCase(usuarioexterno.getRepetirsenha()) != 0) {
			result.addError(new FieldError("usuarioexterno", "repetirsenha", "As senhas informadas não são iguais!"));
		}

		if (result.hasErrors()) {
			return "usuario/trocarsenha";
		}

		Usuario usuarioexternoantigo = usuarioDao.find(usuarioexterno.getId());			
		
		usuarioexternoantigo.setPassword(AuthenticationService.toMd5(usuarioexterno.getPassword()));
		
		AuthenticationService.removeUserAuthority("ROLE_PASSWORD_CHANGES_REQUIRED");
		
		usuarioDao.merge(usuarioexternoantigo);
		
		modelMap.addAttribute("mensagemSucesso", "Sua senha foi alterada com sucesso!");
		
		return "usuario/trocarsenha";
//		return "redirect:/evento/previstos";
	}

	@RequestMapping(value = "/usuario/atualizaremail", method = RequestMethod.GET)
	public String updateemailForm(ModelMap modelMap, HttpServletRequest request) {

		Usuario usuario = usuarioDao.findByLogin(request.getRemoteUser());
		usuario.setEmail("");
		
		modelMap.addAttribute("usuarioexterno", usuario);
		
		return "usuario/atualizaremail";
	}

	@Transactional
	@RequestMapping(value = "/usuario/atualizaremail", method = RequestMethod.PUT)
	public String updateemail(@ModelAttribute("usuarioexterno") Usuario usuarioexterno,	BindingResult result) {

		if (usuarioexterno.getEmail() == null || usuarioexterno.getEmail().equals("")) {
			result.addError(new FieldError("usuarioexterno", "email", "Campo Obrigatório"));
		}else if (!util.validaEmail(usuarioexterno.getEmail())) {
			result.addError(new FieldError("usuarioexterno", "email", "E-mail inválido"));
		}
		
		Usuario usuarioexternoantigo = usuarioDao.find(usuarioexterno.getId());
		
		List<Participante> participantes = participanteDao.findByEmail(usuarioexterno.getEmail());
		
		List<Usuario> usuarios = usuarioDao.findByEmail(usuarioexterno.getEmail());
				
		if( (participantes.size() > 1 || participantes.size() > 1) 
				|| (participantes.size() == 1 && !participantes.get(0).getCpf().equals(usuarioexternoantigo.getCpf()))
						|| (usuarios.size() == 1 && !usuarios.get(0).getCpf().equals(usuarioexternoantigo.getCpf())) ) {
			
			result.addError(new FieldError("usuarioexterno", "email", "E-mail já utilizado por outro usuário"));
		} 

		if (result.hasErrors()) {
			return "usuario/atualizaremail";
		}
				
		Participante participante = participanteDao.findByCpf(usuarioexternoantigo.getCpf());
		
		if (participante != null) {
			participante.setEmail(usuarioexterno.getEmail().trim().toLowerCase());
			participanteDao.merge(participante);
		}
			
		usuarioexternoantigo.setEmail(usuarioexterno.getEmail().trim().toLowerCase());
		usuarioexternoantigo.setForcarAtualizacaoEmail(false);
		
		usuarioDao.merge(usuarioexternoantigo);
		
		
		AuthenticationService.removeUserAuthority("ROLE_UPDATE_EMAIL_REQUIRED");		
		
		
		return "redirect:/evento/previstos";
	}
	
	
	@RequestMapping(value = "/usuario/esquecisenha", method = RequestMethod.GET)
	public String esquecisenhaForm(ModelMap modelMap, HttpServletRequest request) {

		modelMap.addAttribute("usuarioexterno", new Usuario());
		return "usuario/esquecisenha";
	}

	// TODO Tratar com a classe UsuarioService. Método já implementado, só falta testar
	@RequestMapping(value = "/usuario/esquecisenha", method = RequestMethod.POST)
	public String esquecisenha(@ModelAttribute("usuarioexterno") @Valid Usuario usuarioExterno,	BindingResult result, ModelMap modelMap) throws Exception {
		
		Usuario usuarioSIGED = null;
		
		String cpf = usuarioExterno.getCpf();

		if (cpf.equals("")) {
			result.addError(new FieldError("usuarioexterno", "cpf",	"Campo Obrigatório"));
		}else{			
			
			cpf = cpf.replace(".", "").replace("-", "");
			
			UsuarioSCA usuarioSCA = usuarioSCADao.findByCpf(cpf);
					
			// SE TEM USUÁRIO ATIVO NO SCA E É SERVIDOR
			if (usuarioSCA != null && usuarioSCA.isAtivo() && participanteDao.findServidorByCpf(usuarioSCA.getCpf()) != null) {
				result.addError(new FieldError("usuarioexterno", "cpf",	"Solicite sua senha junto à Secretaria de TI"));
			}else{
				
				usuarioSIGED = usuarioDao.findByCpf(cpf);
				
				if (usuarioSIGED == null) {
					result.addError(new FieldError("usuarioexterno", "cpf", "Usuário não cadastrado. Realize uma pré-inscrição em detalhes do Evento (após confirmação da inscrição seu login e senha serão enviados) ou entre em contato com o IPC"));
				}else{
					
					if(usuarioSIGED.isEnabled()){
					
						if(participanteDao.findByCpf(usuarioSIGED.getCpf()) == null){
							result.addError(new FieldError("usuarioexterno", "cpf",	"Participante não cadastrado. Realize uma pré-inscrição em detalhes do Evento (após confirmação da inscrição seu login e senha serão enviados) ou entre em contato com o IPC"));
						}else{
							
							if (usuarioExterno.getEmail().equals("")) {
								result.addError(new FieldError("usuarioexterno", "email", "Campo Obrigatório"));
							} else {						
								
								if(usuarioSIGED.getEmail() == null){
									result.addError(new FieldError("usuarioexterno", "email", "Usuário sem E-mail cadastrado. Favor entrar em contato com o IPC"));
								}else{
									if (!usuarioSIGED.getEmail().trim().equalsIgnoreCase(usuarioExterno.getEmail().trim()))
										result.addError(new FieldError("usuarioexterno", "email", "E-mail não confere com o cadastrado. Favor entrar em contato com o IPC"));
								}							
							}
						}					
						
					}else{
						result.addError(new FieldError("usuarioexterno", "cpf", "Seu cadastramento está em análise. Favor aguardar confirmação do seu login. Para mais informações, entre em contato com o IPC."));
					}
				}
			}
			
		}		

		if (result.hasErrors()) {
			return "usuario/esquecisenha";
		}

		String novasenha = util.gerarSenhaAleatoria();
		usuarioSIGED.setPassword(AuthenticationService.toMd5(novasenha));
		usuarioDao.merge(usuarioSIGED);
		emailUtil.emailEsqueciSenha(usuarioSIGED, novasenha);

		return "redirect:/evento/previstos?mensagem=Uma nova senha foi enviada para o E-mail informado.";
	}
	
	@RequestMapping(value = "/usuario/resetarSenha", method = RequestMethod.GET)
	public String resertarSenhaForm(ModelMap modelMap, HttpServletRequest request) {
		modelMap.addAttribute("usuarioexterno", new Usuario());
		return "usuario/resetarsenha";
	}
	
	@RequestMapping(value = "/usuario/resetarSenha", method = RequestMethod.POST)
	public String resertarSenha(@ModelAttribute("usuarioexterno") @Valid Usuario usuarioExterno, BindingResult result, ModelMap modelMap) throws EmailException {
		
		try {
			usuarioService.resetarSenha(usuarioExterno);
		} catch (CpfException e) {
			result.addError(new FieldError("usuarioexterno", "cpf", e.getMessage()));
		} catch (EmailException e) {
			result.addError(new FieldError("usuarioexterno", "email", e.getMessage()));
		} catch (BusinessException e) {
			modelMap.addAttribute("mensagemErro", e.getMessage());
			return "redirect:/usuario/resetarSenha";
		}

		if (result.hasErrors()) {
			return "usuario/resetarsenha";
		}
		String cpf = usuarioExterno.getCpf().replace(".", "").replace("-", "");
		Participante participante = participanteDao.findByCpf(cpf);
		modelMap.addAttribute("mensagem", "Senha do participante " + participante.getNomeCpf() + " resetada para 12345");
		return "redirect:/usuario/resetarSenha";
	}

	@RequestMapping(value = "/usuario/usuariodesconhecido", method = RequestMethod.GET)
	public String usuariodesconhecidoForm(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
		return "/usuario/usuariodesconhecido";
	}	

	@RequestMapping(value = "/usuario/procuraUsuario", method = RequestMethod.GET)
	@ResponseBody
	public HashMap<String, String> procuraUsuario(@RequestParam(value = "cpf") String cpf) {

		cpf = cpf.replace(".", "").replace("-", "");
		HashMap<String, String> usuarioRetorno = new HashMap<String, String>();

		UsuarioSCA usuarioSCA = usuarioSCADao.findByCpf(cpf);
	
		// SE TEM USUÁRIO ATIVO NO SCA E É SERVIDOR
		if ( usuarioSCA != null && usuarioSCA.isAtivo() && participanteDao.findServidorByCpf(usuarioSCA.getCpf()) != null) { 
				
			usuarioRetorno.put("cpf", usuarioSCA.getCpf());
			usuarioRetorno.put("servidor", "1");
			usuarioRetorno.put("ativo", String.valueOf(usuarioSCA.isAtivo() ? 1 : 0));
			
			return usuarioRetorno;	
				
		} else {
					
			Usuario usuarioSIGED = usuarioDao.findByCpf(cpf);
									
			// SE TEM USUÁRIO E PARTICIPANTE NO SIGED
			if ( usuarioSIGED != null && participanteDao.findByCpf(usuarioSIGED.getCpf()) != null){
				
				usuarioRetorno.put("cpf", usuarioSIGED.getCpf());
				usuarioRetorno.put("servidor", "0");
				usuarioRetorno.put("ativo",	String.valueOf(usuarioSIGED.isEnabled() ? 1 : 0));
				
				return usuarioRetorno;
			}			
		}
		
		return null;
	}
	
	@RequestMapping(value = "/usuario/login", method = RequestMethod.POST)
	public String login(@RequestParam(value = "j_username") String username, @RequestParam(value = "j_password") String password) {
				
		try {
			
			authenticationService.login(username, password);
		
			return "redirect:/evento/previstos";			
		
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/evento/previstos/logininvalido?mensagem=" + e.getMessage();
		}		
		
	}
	
}
