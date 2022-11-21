package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.Cidade;

/**
 *
 * @author Rodrigo
 */
@Repository("cidadeDao")
public class CidadeDAO {
	protected EntityManager entityManager;

	public CidadeDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Cidade find(Long id) {
		return entityManager.find(Cidade.class, id);
	}

	@Transactional
	public void persist(Cidade Cidade) {
		entityManager.persist(Cidade);
	}

	@Transactional
	public void merge(Cidade Cidade) {
		entityManager.merge(Cidade);
	}

	@Transactional
	public void remove(Cidade Cidade) {
		entityManager.remove(Cidade);
	}

	@SuppressWarnings("unchecked")
	public List<Cidade> findAll() {
		return entityManager.createNamedQuery("Cidade.findAll").getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Cidade> findByUfId(Long ufId) {
		Query query = entityManager.createNamedQuery("Cidade.findByUfId");
		
		query.setParameter("ufId", ufId);
		
		return query.getResultList();
	}
	public Cidade findById(Long codigo) {
		try {
			return (Cidade) entityManager.createNamedQuery("Cidade.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		} 
	}
	
}
