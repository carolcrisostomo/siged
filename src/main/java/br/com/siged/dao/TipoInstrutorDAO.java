package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.TipoInstrutor;

/**
 *
 * @author Marcelo
 */
@Repository("tipoinstrutorDao")
public class TipoInstrutorDAO {
	protected EntityManager entityManager;

	public TipoInstrutorDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public TipoInstrutor find(Long id) {
		return entityManager.find(TipoInstrutor.class, id);
	}

	@Transactional
	public void persist(TipoInstrutor TipoInstrutor) {
		entityManager.persist(TipoInstrutor);
	}

	@Transactional
	public void merge(TipoInstrutor TipoInstrutor) {
		entityManager.merge(TipoInstrutor);
	}

	@Transactional
	public void remove(TipoInstrutor TipoInstrutor) {
		entityManager.remove(TipoInstrutor);
	}

	@SuppressWarnings("unchecked")
	public List<TipoInstrutor> findAll() {
		return entityManager.createNamedQuery("TipoInstrutor.findAll").getResultList();
	}
	public TipoInstrutor findById(Long codigo) {
		try {
			return (TipoInstrutor) entityManager.createNamedQuery("TipoInstrutor.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
}
}