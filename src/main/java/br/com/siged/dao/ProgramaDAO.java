package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.Programa;

@Repository("programaDao")
public class ProgramaDAO {
	
	protected EntityManager entityManager;
	
	public ProgramaDAO() {}
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public Programa find(Long id) {
		return entityManager.find(Programa.class, id);
	}
	
	@Transactional
	public void persist(Programa programa) {
		entityManager.persist(programa);
	}
	
	@Transactional
	public void merge(Programa programa) {
		entityManager.merge(programa);
	}
	
	@Transactional
	public void remove(Programa programa) {
		entityManager.remove(programa);
	}
	
	public List<Programa> findAll() {
		return entityManager.createNamedQuery("Programa.findAll", Programa.class).getResultList();
	}
	
	public Programa findById(Long id) {
		return entityManager.createNamedQuery("Programa.findById", Programa.class).setParameter("id", id).getSingleResult();
	}

}
