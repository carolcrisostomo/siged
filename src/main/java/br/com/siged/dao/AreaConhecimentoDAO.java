package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.AreaConhecimento;
/**
*
* @author Rodrigo
*/
@Repository("areaconhecimentoDao")
public class AreaConhecimentoDAO {
	protected EntityManager entityManager;

	public AreaConhecimentoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public AreaConhecimento find(Long id) {
		return entityManager.find(AreaConhecimento.class, id);
	}

	@Transactional
	public void persist(AreaConhecimento AreaConhecimento) {
		entityManager.persist(AreaConhecimento);
	}

	@Transactional
	public void merge(AreaConhecimento AreaConhecimento) {
		entityManager.merge(AreaConhecimento);
	}

	@Transactional
	public void remove(AreaConhecimento AreaConhecimento) {
		entityManager.remove(AreaConhecimento);
	}

	@SuppressWarnings("unchecked")
	public List<AreaConhecimento> findAll() {
		return entityManager.createNamedQuery("AreaConhecimento.findAll").getResultList();
	
	}

	public AreaConhecimento findById(Long codigo) {
		try {
			return (AreaConhecimento) entityManager.createNamedQuery("AreaConhecimento.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
}
	
}
