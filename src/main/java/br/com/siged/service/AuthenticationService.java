package br.com.siged.service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.Usuario;
import br.com.siged.service.exception.BusinessException;


@Component
public class AuthenticationService {

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	
	public static final String PASSWORD_DEFAULT = "12345";
	
	@Transactional
	public String login(String username, String password) {
		
			if ( username == null || password == null )
				throw new RuntimeException("Login ou senha inválidos!");
		
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,toMd5(password));

			Authentication authenticate;			
			
			try {
				 authenticate = authenticationManager.authenticate(token);
			} catch (DisabledException e) {
				e.printStackTrace();
				throw new BusinessException("Usuário desabilitado!");
			} catch (LockedException e) {
				e.printStackTrace();
				throw new BusinessException("Usuário bloqueado!");
			} catch (CredentialsExpiredException e) {
				e.printStackTrace();
				throw new BusinessException("Conta expirada!");
			} catch (UsernameNotFoundException e) {
				e.printStackTrace();
				throw new BusinessException("Nenhum usuário com esse login foi encontrado!");
			} catch (BadCredentialsException e) {
				e.printStackTrace();
				throw new BusinessException("Login ou senha inválidos!");
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException("Erro ao logar!");
			}			
			
			SecurityContextHolder.getContext().setAuthentication(authenticate);
			
			
			Usuario usuario = (Usuario) getUsuarioLogado();
			
			if (!usuario.isEnabled()) {
				throw new BusinessException("Usuário não está ativo!");
			}			
			
			if (usuario.isSenhaexpirada()) {
				throw new BusinessException("Senha expirada!");
			}
			
			if (authenticate.isAuthenticated()) {
				return "ok";
			}
		
		return null;
	}

	public static Usuario getUsuarioLogado() throws ClassCastException {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public static Usuario getUsuarioLogadoOrNull() throws ClassCastException {
		Usuario usuarioLogado;
		try {
			usuarioLogado = AuthenticationService.getUsuarioLogado();
		} catch (ClassCastException e) {
			usuarioLogado = null;
		}
		return usuarioLogado;
	}

	public static String toMd5(String valor){
		MessageDigest md = null;
		String md5 = null;
		try {
			md = MessageDigest.getInstance("MD5");			
			BigInteger hashForm = new BigInteger(1, md.digest(valor.getBytes("UTF-8")));
			md5 = hashForm.toString(16);	
			if (md5.length() %2 != 0) 
				md5 = "0" + md5; 
				  
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {			
			e.printStackTrace();
		}
		return md5;
	}
	
	public static void addUserAuthority(String role) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(auth.getAuthorities());
		authorities.add(new GrantedAuthorityImpl(role));
		Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), authorities);
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}

	public static void removeUserAuthority(String role) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(auth.getAuthorities());
		authorities.remove(new GrantedAuthorityImpl(role));
		Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), authorities);
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}
	
    public static Boolean hasPasswordDefault(Usuario usuario) {
    	if(usuario.getPassword().equals(PASSWORD_DEFAULT))
    		return true;
    	if(usuario.getPassword().equals(toMd5(PASSWORD_DEFAULT)))
    		return true;
    	return false;
    }
    
    public static String getEncodePasswordDefault() {
    	return toMd5(PASSWORD_DEFAULT);
    }

	public static void main(String[] args) {
		try {
			byte utf[] = "!@#$%é".getBytes("UTF-8");
			byte iso[] = "!@#$%é".getBytes("ISO-8859-1");
			
			byte utfToIso[] = new String(utf).getBytes("ISO-8859-1");
			byte isoToUTF[] = new String(iso).getBytes("UTF-8");
			
			System.out.println("UTF: " + toMd5(new String(utf)));
			System.out.println("UTF: " + toMd5(new String(utfToIso)));
			System.out.println("ISO: " + toMd5(new String(iso)));
			System.out.println("ISO: " + toMd5(new String(isoToUTF)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(toMd5("12345"));
	}
}
