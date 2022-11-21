package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.FonteGasto;
/**
 *
 * @author Rodrigo
 */
@Repository("fontegastoDao")
public class FonteGastoDAO {
	protected EntityManager entityManager;

	public FonteGastoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public FonteGasto find(Long id) {
		return entityManager.find(FonteGasto.class, id);
	}

	@Transactional
	public void persist(FonteGasto FonteGasto) {
		entityManager.persist(FonteGasto);
	}

	@Transactional
	public void merge(FonteGasto FonteGasto) {
		entityManager.merge(FonteGasto);
	}

	@Transactional
	public void remove(FonteGasto FonteGasto) {
		entityManager.remove(FonteGasto);
	}

	@SuppressWarnings("unchecked")
	public List<FonteGasto> findAll() {
		return entityManager.createNamedQuery("FonteGasto.findAll").getResultList();
	}
	public FonteGasto findById(Long codigo) {
		try {
			return (FonteGasto) entityManager.createNamedQuery("FonteGasto.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
}
}