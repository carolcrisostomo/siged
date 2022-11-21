package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.FormacaoAcademica;
/**
 *
 * @author Rodrigo
 */
@Repository("formacaoacademicaDao")
public class FormacaoAcademicaDAO {
	protected EntityManager entityManager;

	public FormacaoAcademicaDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public FormacaoAcademica find(Long id) {
		return entityManager.find(FormacaoAcademica.class, id);
	}

	@Transactional
	public void persist(FormacaoAcademica FormacaoAcademica) {
		entityManager.persist(FormacaoAcademica);
	}

	@Transactional
	public void merge(FormacaoAcademica FormacaoAcademica) {
		entityManager.merge(FormacaoAcademica);
	}

	@Transactional
	public void remove(FormacaoAcademica FormacaoAcademica) {
		entityManager.remove(FormacaoAcademica);
	}

	@SuppressWarnings("unchecked")
	public List<FormacaoAcademica> findAll() {
		return entityManager.createNamedQuery("FormacaoAcademica.findAll").getResultList();
	}
	public FormacaoAcademica findById(Long codigo) {
		try {
			return (FormacaoAcademica) entityManager.createNamedQuery("FormacaoAcademica.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
}
}