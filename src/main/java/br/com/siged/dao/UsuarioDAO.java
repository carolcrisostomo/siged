package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.Usuario;

/**
 *
 * @author Rodrigo
 */
@Repository("usuarioExternoDao")
public class UsuarioDAO {
	protected EntityManager entityManager;

	public UsuarioDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Usuario find(Long id) {
		return entityManager.find(Usuario.class, id);
	}

	@Transactional
	public void persist(Usuario UsuarioExterno) {
		entityManager.persist(UsuarioExterno);
	}

	@Transactional
	public void merge(Usuario UsuarioExterno) {
		entityManager.merge(UsuarioExterno);
	}

	@Transactional
	public void remove(Usuario UsuarioExterno) {
		entityManager.remove(UsuarioExterno);
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> findAll() {
		return entityManager.createNamedQuery("Usuario.findAll").getResultList();
	}
	
	public Usuario findById(Long codigo) {
		try {
			return (Usuario) entityManager.createNamedQuery("Usuario.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	public Usuario findByLogin(String login) {
		try {
			return (Usuario) entityManager.createNamedQuery("Usuario.findByLogin").setParameter("login", login).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> findByEmail(String email) {
		return entityManager.createNamedQuery("Usuario.findByEmail").setParameter("email", email).getResultList();		
	}
	
	public Usuario findByCpf(String cpf) {
		try {
			return (Usuario) entityManager.createNamedQuery("Usuario.findByCpf").setParameter("cpf", cpf).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
}