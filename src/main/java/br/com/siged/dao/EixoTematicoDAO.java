package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.EixoTematico;

/**
 *
 * @author Thiago
 */
@Repository("eixotematicoDao")
public class EixoTematicoDAO {
	protected EntityManager entityManager;

	public EixoTematicoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EixoTematico find(Long id) {
		return entityManager.find(EixoTematico.class, id);
	}

	@Transactional
	public void persist(EixoTematico EixoTematico) {
		entityManager.persist(EixoTematico);
	}

	@Transactional
	public void merge(EixoTematico EixoTematico) {
		entityManager.merge(EixoTematico);
	}

	@Transactional
	public void remove(EixoTematico EixoTematico) {
		entityManager.remove(EixoTematico);
	}

	@SuppressWarnings("unchecked")
	public List<EixoTematico> findAll() {
		return entityManager.createNamedQuery("EixoTematico.findAll").getResultList();
	}
	public EixoTematico findById(Long codigo) {
		try {
			return (EixoTematico) entityManager.createNamedQuery("EixoTematico.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
}
}