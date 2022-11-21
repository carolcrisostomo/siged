package br.com.siged.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@Deprecated
public class UsuarioLogado {
	
	public Collection<GrantedAuthority> getAuthorities(){
		return getUserPrincipal().getAuthorities();
	}
	
	public User getUserPrincipal(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public boolean hasRole(String role){
		for(GrantedAuthority authority : getAuthorities()){
			if(authority.equals(role)){
				return true;
			}
		}
		return false;
	}

}
