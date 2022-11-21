package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.NivelEscolaridade;
/**
 *
 * @author Rodrigo
 */
@Repository("nivelescolaridadeDao")
public class NivelEscolaridadeDAO {
	protected EntityManager entityManager;

	public NivelEscolaridadeDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public NivelEscolaridade find(Long id) {
		return entityManager.find(NivelEscolaridade.class, id);
	}

	@Transactional
	public void persist(NivelEscolaridade NivelEscolaridade) {
		entityManager.persist(NivelEscolaridade);
	}

	@Transactional
	public void merge(NivelEscolaridade NivelEscolaridade) {
		entityManager.merge(NivelEscolaridade);
	}

	@Transactional
	public void remove(NivelEscolaridade NivelEscolaridade) {
		entityManager.remove(NivelEscolaridade);
	}

	@SuppressWarnings("unchecked")
	public List<NivelEscolaridade> findAll() {
		return entityManager.createNamedQuery("NivelEscolaridade.findAll").getResultList();
	}
	public NivelEscolaridade findById(Long codigo) {
		try {
			return (NivelEscolaridade) entityManager.createNamedQuery("NivelEscolaridade.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
}
}