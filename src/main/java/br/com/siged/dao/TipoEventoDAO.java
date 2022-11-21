package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.TipoEvento;

/**
 *
 * @author Marcelo
 */
@Repository("tipoeventoDao")
public class TipoEventoDAO {
	protected EntityManager entityManager;

	public TipoEventoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public TipoEvento find(Long id) {
		return entityManager.find(TipoEvento.class, id);
	}

	@Transactional
	public void persist(TipoEvento TipoEvento) {
		entityManager.persist(TipoEvento);
	}

	@Transactional
	public void merge(TipoEvento TipoEvento) {
		entityManager.merge(TipoEvento);
	}

	@Transactional
	public void remove(TipoEvento TipoEvento) {
		entityManager.remove(TipoEvento);
	}

	@SuppressWarnings("unchecked")
	public List<TipoEvento> findAll() {
		return entityManager.createNamedQuery("TipoEvento.findAll").getResultList();
	}
	public TipoEvento findById(Long codigo) {
		try {
			return (TipoEvento) entityManager.createNamedQuery("TipoEvento.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
}
}