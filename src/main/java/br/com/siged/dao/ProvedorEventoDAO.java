package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.ProvedorEvento;
/**
 *
 * @author Rodrigo
 */
@Repository("provedoreventoDao")
public class ProvedorEventoDAO {
	protected EntityManager entityManager;

	public ProvedorEventoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public ProvedorEvento find(Long id) {
		return entityManager.find(ProvedorEvento.class, id);
	}

	@Transactional
	public void persist(ProvedorEvento ProvedorEvento) {
		entityManager.persist(ProvedorEvento);
	}

	@Transactional
	public void merge(ProvedorEvento ProvedorEvento) {
		entityManager.merge(ProvedorEvento);
	}

	@Transactional
	public void remove(ProvedorEvento ProvedorEvento) {
		entityManager.remove(ProvedorEvento);
	}

	@SuppressWarnings("unchecked")
	public List<ProvedorEvento> findAll() {
		return entityManager.createNamedQuery("ProvedorEvento.findAll").getResultList();
	}
	
	public ProvedorEvento findById(Long codigo) {
		try {
			return (ProvedorEvento) entityManager.createNamedQuery("ProvedorEvento.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	public ProvedorEvento findByEventoId(Long eventoId) {
		return (ProvedorEvento) entityManager
					.createNativeQuery("SELECT * FROM PROVEDOR_EVENTO p "
							+ "INNER JOIN EVENTO e on e.PROVEDOR_ID = p.id "
							+ "WHERE e.id = :eventoId", ProvedorEvento.class)
					.setParameter("eventoId", eventoId)
					.getSingleResult();
	}
}