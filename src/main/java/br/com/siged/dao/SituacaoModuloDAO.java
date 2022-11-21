package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.SituacaoModulo;
/**
 *
 * @author Rodrigo
 */
@Repository("situacaomoduloDao")
public class SituacaoModuloDAO {
	protected EntityManager entityManager;

	public SituacaoModuloDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public SituacaoModulo find(Long id) {
		return entityManager.find(SituacaoModulo.class, id);
	}

	@Transactional
	public void persist(SituacaoModulo SituacaoModulo) {
		entityManager.persist(SituacaoModulo);
	}

	@Transactional
	public void merge(SituacaoModulo SituacaoModulo) {
		entityManager.merge(SituacaoModulo);
	}

	@Transactional
	public void remove(SituacaoModulo SituacaoModulo) {
		entityManager.remove(SituacaoModulo);
	}

	@SuppressWarnings("unchecked")
	public List<SituacaoModulo> findAll() {
		return entityManager.createNamedQuery("SituacaoModulo.findAll").getResultList();
	}
	public SituacaoModulo findById(Long codigo) {
		try {
			return (SituacaoModulo) entityManager.createNamedQuery("SituacaoModulo.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
}
}