package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.externas.Grupo;

/**
 *
 * @author Rodrigo
 */
@Repository("grupoDao")
public class GrupoDAO {
	protected EntityManager entityManager;

	public GrupoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Grupo find(Long id) {
		return entityManager.find(Grupo.class, id);
	}

	@Transactional
	public void persist(Grupo Grupo) {
		entityManager.persist(Grupo);
	}

	@Transactional
	public void merge(Grupo Grupo) {
		entityManager.merge(Grupo);
	}

	@Transactional
	public void remove(Grupo Grupo) {
		entityManager.remove(Grupo);
	}

	@SuppressWarnings("unchecked")
	public List<Grupo> findAll() {
		return entityManager.createNamedQuery("Grupo.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Grupo> findByNome(String nome) {
		return entityManager.createNamedQuery("Grupo.findByNome").setParameter("nome", nome).getResultList();
	}
	
	public Grupo findById(Long codigo) {
		try {
			return (Grupo) entityManager.createNamedQuery("Grupo.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

}
