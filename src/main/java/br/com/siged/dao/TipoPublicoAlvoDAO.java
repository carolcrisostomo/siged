package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.TipoPublicoAlvo;

/**
 *
 * @author Rodrigo
 */
@Repository("tipopublicoalvoDao")
public class TipoPublicoAlvoDAO {
	protected EntityManager entityManager;

	public TipoPublicoAlvoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public TipoPublicoAlvo find(Long id) {
		return entityManager.find(TipoPublicoAlvo.class, id);
	}

	@Transactional
	public void persist(TipoPublicoAlvo TipoPublicoAlvo) {
		entityManager.persist(TipoPublicoAlvo);
	}

	@Transactional
	public void merge(TipoPublicoAlvo TipoPublicoAlvo) {
		entityManager.merge(TipoPublicoAlvo);
	}

	@Transactional
	public void remove(TipoPublicoAlvo TipoPublicoAlvo) {
		entityManager.remove(TipoPublicoAlvo);
	}

	@SuppressWarnings("unchecked")
	public List<TipoPublicoAlvo> findAll() {
		return entityManager.createNamedQuery("TipoPublicoAlvo.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoPublicoAlvo> findAllNotServidor() {
		return entityManager.createNamedQuery("TipoPublicoAlvo.findAllNotServidor").getResultList();
	}
	
	public TipoPublicoAlvo findById(Long codigo) {
		try {
			return (TipoPublicoAlvo) entityManager.createNamedQuery("TipoPublicoAlvo.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
}
}