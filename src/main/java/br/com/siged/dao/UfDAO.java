package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.Uf;

/**
 *
 * @author Rodrigo
 */
@Repository("ufDao")
public class UfDAO {
	protected EntityManager entityManager;

	public UfDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Uf find(Long id) {
		return entityManager.find(Uf.class, id);
	}

	@Transactional
	public void persist(Uf Uf) {
		entityManager.persist(Uf);
	}

	@Transactional
	public void merge(Uf Uf) {
		entityManager.merge(Uf);
	}

	@Transactional
	public void remove(Uf Uf) {
		entityManager.remove(Uf);
	}

	@SuppressWarnings("unchecked")
	public List<Uf> findAll() {
		return entityManager.createNamedQuery("Uf.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Uf> findByPaisId(Long paisId) {
		Query query = entityManager.createNamedQuery("Uf.findByPaisId");
		
		query.setParameter("paisId", paisId);
		
		return query.getResultList();
	}
	
	public Uf findById(Long codigo) {
		try {
			return (Uf) entityManager.createNamedQuery("Uf.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

}
