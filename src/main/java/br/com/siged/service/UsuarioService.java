package br.com.siged.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.UsuarioDAO;
import br.com.siged.dao.UsuarioSCADAO;
import br.com.siged.entidades.Usuario;
import br.com.siged.entidades.externas.UsuarioSCA;
import br.com.siged.service.exception.BusinessException;
import br.com.siged.service.exception.CpfException;
import br.com.siged.service.exception.EmailException;
import br.com.siged.util.EmailUtil;
import br.com.siged.util.Util;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioDAO usuarioexternoDao;
	@Autowired
	private UsuarioSCADAO usuarioSCADao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private Util util;
	@Autowired
	private EmailUtil emailUtil;
	
	public void resetarSenha(Usuario usuario) throws BusinessException {
		Usuario usuariologado = AuthenticationService.getUsuarioLogado();

		if(usuariologado.hasAuthority("ROLE_RESET_PASSWORD")) {
			Usuario usuarioSIGED = getUsuarioSIGEDForcarSenha(usuario);
			
			usuarioSIGED.setPassword(AuthenticationService.getEncodePasswordDefault());
			usuarioexternoDao.merge(usuarioSIGED);
			
		} else {
			throw new BusinessException("Sem permissão para resetar senha");
		}
	}
	
	public void esqueciMinhaSenha(Usuario usuario) throws BusinessException {
		Usuario usuarioSIGED = getUsuarioSIGEDForcarSenha(usuario);
		
		try {
			String novasenha = util.gerarSenhaAleatoria();
			usuarioSIGED.setPassword(AuthenticationService.toMd5(novasenha));
			emailUtil.emailEsqueciSenha(usuarioSIGED, novasenha);
			usuarioexternoDao.merge(usuarioSIGED);
		} catch (Exception e) {
			throw new BusinessException("Senha não atualizada: Não foi possível enviar uma nova senha por e-mail");
		}
	}
	
	/**
	 * SE TEM USUÁRIO ATIVO NO SCA E É SERVIDOR
	 * @param cpf
	 * @return
	 */
	public Boolean isUsuarioAtivoSCAeServidor(String cpf) {
		UsuarioSCA usuarioSCA = usuarioSCADao.findByCpf(cpf);
		return usuarioSCA != null && usuarioSCA.isAtivo() && participanteDao.findServidorByCpf(usuarioSCA.getCpf()) != null;
	}
	
	private Usuario getUsuarioSIGEDForcarSenha(Usuario usuario) throws EmailException, CpfException {
		Usuario usuarioSIGED = null;
		if(usuario != null) {
			if(usuario.getCpf() == null || usuario.getCpf().equals("")) {
				throw new CpfException("CPF não informado");
			}
			String cpf = usuario.getCpf().replace(".", "").replace("-", "");
			if(isUsuarioAtivoSCAeServidor(cpf))
				throw new CpfException("Usuário Servidor: Solicite a senha junto à Secretaria de TI");
			
			usuarioSIGED = usuarioexternoDao.findByCpf(cpf);
			if(usuarioSIGED == null)
				throw new CpfException("Usuário não cadastrado: Realize uma pré-inscrição em detalhes do Evento (após confirmação da inscrição seu login e senha serão enviados) ou entre em contato com o IPC");
			
			if(usuarioSIGED.isEnabled()) {
				if(participanteDao.findByCpf(cpf) == null)
					throw new BusinessException("Participante não cadastrado: Realize uma pré-inscrição em detalhes do Evento (após confirmação da inscrição seu login e senha serão enviados) ou entre em contato com o IPC");
				if(usuarioSIGED.getEmail() == null)
					throw new EmailException("Usuário sem E-mail cadastrado: Favor entrar em contato com o IPC");
				
				if(usuario.getEmail() == null || usuario.getEmail().equals("")) 
					throw new EmailException("E-mail não informado");
				
				if (!usuarioSIGED.getEmail().trim().equalsIgnoreCase(usuario.getEmail().trim())) {
					throw new EmailException("E-mail não confere com o cadastrado: Favor entrar em contato com o IPC");
				}
			} else {
				throw new CpfException("Cadastro está em análise. Favor aguardar confirmação do seu login. Para mais informações, entre em contato com o IPC.");
			}
		} else {
			throw new CpfException("Usuário não informado");
		}
		return usuarioSIGED;
	}
	
}
