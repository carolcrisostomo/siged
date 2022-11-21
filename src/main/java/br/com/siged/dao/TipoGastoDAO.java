package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.TipoGasto;

/**
 *
 * @author Marcelo
 */
@Repository("tipogastoDao")
public class TipoGastoDAO {
	protected EntityManager entityManager;

	public TipoGastoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public TipoGasto find(Long id) {
		return entityManager.find(TipoGasto.class, id);
	}

	@Transactional
	public void persist(TipoGasto TipoGasto) {
		entityManager.persist(TipoGasto);
	}

	@Transactional
	public void merge(TipoGasto TipoGasto) {
		entityManager.merge(TipoGasto);
	}

	@Transactional
	public void remove(TipoGasto TipoGasto) {
		entityManager.remove(TipoGasto);
	}

	@SuppressWarnings("unchecked")
	public List<TipoGasto> findAll() {
	    return entityManager.createQuery("SELECT t FROM TipoGasto t WHERE t.id != 5 ORDER BY t.descricao ASC").getResultList();
	}
	
	public TipoGasto findById(Long codigo) {
		try {
			return (TipoGasto) entityManager.createNamedQuery("TipoGasto.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
}
}