package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.externas.Setor;
/**
 *
 * @author Rodrigo
 */
@Repository("setorDao")
public class SetorDAO {
	protected EntityManager entityManager;

	public SetorDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Setor find(Long id) {
		return entityManager.find(Setor.class, id);
	}

	@Transactional
	public void persist(Setor Setor) {
		entityManager.persist(Setor);
	}

	@Transactional
	public void merge(Setor Setor) {
		entityManager.merge(Setor);
	}

	@Transactional
	public void remove(Setor Setor) {
		entityManager.remove(Setor);
	}

	@SuppressWarnings("unchecked")
	public List<Setor> findAll() {
		return entityManager.createNamedQuery("Setor.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Setor> findAllSemTemp() {
		return entityManager.createNamedQuery("Setor.findAllSemTemp").getResultList();
	}
	
	public Setor findById(Long codigo) {
		try {
			return (Setor) entityManager.createNamedQuery("Setor.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
}
}