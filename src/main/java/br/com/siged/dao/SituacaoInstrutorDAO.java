package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.SituacaoInstrutor;
/**
 *
 * @author Rodrigo
 */
@Repository("situacaoinstrutorDao")
public class SituacaoInstrutorDAO {
	protected EntityManager entityManager;

	public SituacaoInstrutorDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public SituacaoInstrutor find(Long id) {
		return entityManager.find(SituacaoInstrutor.class, id);
	}

	@Transactional
	public void persist(SituacaoInstrutor SituacaoInstrutor) {
		entityManager.persist(SituacaoInstrutor);
	}

	@Transactional
	public void merge(SituacaoInstrutor SituacaoInstrutor) {
		entityManager.merge(SituacaoInstrutor);
	}

	@Transactional
	public void remove(SituacaoInstrutor SituacaoInstrutor) {
		entityManager.remove(SituacaoInstrutor);
	}

	@SuppressWarnings("unchecked")
	public List<SituacaoInstrutor> findAll() {
		return entityManager.createNamedQuery("SituacaoInstrutor.findAll").getResultList();
	}
	public SituacaoInstrutor findById(Long codigo) {
		try {
			return (SituacaoInstrutor) entityManager.createNamedQuery("SituacaoInstrutor.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
}
}