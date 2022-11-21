package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.TipoAreaTribunal;

/**
 *
 * @author Rodrigo
 */
@Repository("tipoareatribunalDao")
public class TipoAreaTribunalDAO {
	protected EntityManager entityManager;

	public TipoAreaTribunalDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public TipoAreaTribunal find(Long id) {
		return entityManager.find(TipoAreaTribunal.class, id);
	}

	@Transactional
	public void persist(TipoAreaTribunal TipoAreaTribunal) {
		entityManager.persist(TipoAreaTribunal);
	}

	@Transactional
	public void merge(TipoAreaTribunal TipoAreaTribunal) {
		entityManager.merge(TipoAreaTribunal);
	}

	@Transactional
	public void remove(TipoAreaTribunal TipoAreaTribunal) {
		entityManager.remove(TipoAreaTribunal);
	}

	@SuppressWarnings("unchecked")
	public List<TipoAreaTribunal> findAll() {
		return entityManager.createNamedQuery("TipoAreaTribunal.findAll").getResultList();
	}
	public TipoAreaTribunal findById(Long codigo) {
		try {
			return (TipoAreaTribunal) entityManager.createNamedQuery("TipoAreaTribunal.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
}
}