package br.com.siged.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import br.com.siged.dao.AvaliacaoEficaciaDAO;
import br.com.siged.dao.EventoExtraDAO;
import br.com.siged.dao.InscricaoDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.ResponsavelSetorDAO;
import br.com.siged.dao.UsuarioDAO;
import br.com.siged.dao.UsuarioSCADAO;
import br.com.siged.entidades.AvaliacaoEficacia;
import br.com.siged.entidades.EventoExtra;
import br.com.siged.entidades.Inscricao;
import br.com.siged.entidades.ResponsavelSetor;
import br.com.siged.entidades.Usuario;
import br.com.siged.entidades.externas.UsuarioSCA;
@Deprecated
@Service
public class LoginService implements UserDetailsService {

	@Autowired
	private InscricaoDAO inscricaoDao;
	@Autowired
	private EventoExtraDAO eventoExtraDao;
	@Autowired
	private UsuarioSCADAO usuarioDao;
	@Autowired
	private UsuarioDAO usuarioexternoDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private ResponsavelSetorDAO responsavelSetorDao;
	@Autowired
	private AvaliacaoEficaciaDAO avaliacaoEficaciaDao;

	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		UsuarioSCA usuarioSCA = usuarioDao.findByLoginSCA(username);
		
		// SE TEM USUÁRIO ATIVO NO SCA E É SERVIDOR
		if (usuarioSCA != null && usuarioSCA.isAtivo() && participanteDao.findServidorByCpf(usuarioSCA.getCpf()) != null) {

			authorities.add(new GrantedAuthorityImpl("ROLE_SERVIDOR"));	
			authorities.add(new GrantedAuthorityImpl("ROLE_ALUNO"));

			List<ResponsavelSetor> responsavelList = responsavelSetorDao.findAtivoByResponsavel(usuarioSCA);
			List<ResponsavelSetor> responsavelImediatoList = responsavelSetorDao.findAtivoByResponsavelImediato(usuarioSCA);
			
			if ((responsavelList != null && responsavelList.size() > 0) || (responsavelImediatoList != null && responsavelImediatoList.size() > 0)){
				authorities.add(new GrantedAuthorityImpl("ROLE_CHEFE"));
			}else{
			
				List<Inscricao> inscricaoList = inscricaoDao.findByChefe(usuarioSCA.getLogin());
				List<EventoExtra> solicitacaoList = eventoExtraDao.findByChefe(usuarioSCA.getLogin());
				List<AvaliacaoEficacia> avaliacaoEficaciaList = avaliacaoEficaciaDao.findByResponsavel(usuarioSCA);

				if (inscricaoList != null && inscricaoList.size() > 0 
						|| solicitacaoList != null && solicitacaoList.size() > 0
						|| avaliacaoEficaciaList != null &&  avaliacaoEficaciaList.size() > 0) {
					authorities.add(new GrantedAuthorityImpl("ROLE_CHEFE"));
				}
			
			}			
				
			System.out.println(authorities);

			return new User(usuarioSCA.getLogin(), usuarioSCA.getSenha(), true,	true, true, true, authorities);

		} else {

			Usuario usuarioSIGED = usuarioexternoDao.findByLogin(username);

			// SE TEM USUÁRIO ATIVO NO SIGED
			if (usuarioSIGED != null && usuarioSIGED.isEnabled()) {				
				
				if(usuarioSIGED.getTipo() == 1){
					
					authorities.add(new GrantedAuthorityImpl("ROLE_ADMINISTRADOR"));
				
				}else if(username.compareToIgnoreCase("ipcconsulta") == 0){
					
					authorities.add(new GrantedAuthorityImpl("ROLE_ADMINISTRADORCONS"));
				
				}else{ 
					
					authorities.add(new GrantedAuthorityImpl("ROLE_ALUNO"));
					
					if(usuarioSIGED.getPassword().equals(DigestUtils.md5DigestAsHex("12345".getBytes())))					
						authorities.add(new GrantedAuthorityImpl("ROLE_PASSWORD_CHANGES_REQUIRED"));
					
				}				

				System.out.println(authorities);
				
				return new User(usuarioSIGED.getUsername(), usuarioSIGED.getPassword(), true, true, true, true, authorities);					
			}
		}	

		return null;
		
	}

}