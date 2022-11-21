package br.com.siged.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.UsuarioDAO;
import br.com.siged.dao.UsuarioSCADAO;
import br.com.siged.entidades.Usuario;
import br.com.siged.entidades.externas.UsuarioSCA;

@Controller
public class IntegracaoSigedAvaController {
	
	@Autowired
	private UsuarioSCADAO usuarioDao;
	@Autowired
	private UsuarioDAO usuarioexternoDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	
	private static final String KEY = "integracao_siged_ava";
	
	@RequestMapping(value = "/integracao/siged/ava/autenticacao/usuario", method = RequestMethod.POST)
	@ResponseBody
	public Boolean integracao(@RequestParam String username, @RequestParam String password, @RequestParam String token){
		
		if(getToken().equals(token)){
			if(autenticar(username, password))
				return true;
			return false;
		}
		
		return false;
	}
	
	private String getToken() {
		return DigestUtils.md5DigestAsHex(KEY.getBytes());
	}
	
	private String md5(String key){
		return DigestUtils.md5DigestAsHex(key.getBytes());
	}
	
	private Boolean autenticar(String username, String password) {
		
		UsuarioSCA usuarioSCA = usuarioDao.findByCpf(username);
		
		// SE TEM USUÁRIO ATIVO NO SCA E É SERVIDOR
		if (usuarioSCA != null && usuarioSCA.isAtivo() && participanteDao.findServidorByCpf(usuarioSCA.getCpf()) != null) {
			if(md5(password).equals(usuarioSCA.getSenha())){
				return true;
			}
			return false;
		} else {

			Usuario usuarioSIGED = usuarioexternoDao.findByLogin(username);

			// SE TEM USUÁRIO ATIVO NO SIGED
			if (usuarioSIGED != null && usuarioSIGED.isEnabled()) {
				if(md5(password).equals(usuarioSIGED.getPassword())){
					return true;
				}
				return false;					
			}
		}
		
		return false;
	}

}
