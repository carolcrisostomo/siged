package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.Modalidade;
/**
 *
 * @author Rodrigo
 */
@Repository("modalidadeDao")
public class ModalidadeDAO {
	protected EntityManager entityManager;

	public ModalidadeDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Modalidade find(Long id) {
		return entityManager.find(Modalidade.class, id);
	}

	@Transactional
	public void persist(Modalidade Modalidade) {
		entityManager.persist(Modalidade);
	}

	@Transactional
	public void merge(Modalidade Modalidade) {
		entityManager.merge(Modalidade);
	}

	@Transactional
	public void remove(Modalidade Modalidade) {
		entityManager.remove(Modalidade);
	}

	@SuppressWarnings("unchecked")
	public List<Modalidade> findAll() {
		return entityManager.createNamedQuery("Modalidade.findAll").getResultList();
	}
	public Modalidade findById(Long codigo) {
		try {
			return (Modalidade) entityManager.createNamedQuery("Modalidade.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
}
}