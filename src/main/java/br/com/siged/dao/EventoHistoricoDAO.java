package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.Evento;
import br.com.siged.entidades.EventoHistorico;
/**
 *
 * @author Rodrigo
 */
@Repository("eventohistoricoDao")
public class EventoHistoricoDAO {
	protected EntityManager entityManager;
	
	public EventoHistoricoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EventoHistorico find(Long id) {
		return entityManager.find(EventoHistorico.class, id);
	}

	@Transactional
	public void persist(EventoHistorico EventoHistorico) {
		entityManager.persist(EventoHistorico);
	}

	@Transactional
	public void merge(EventoHistorico EventoHistorico) {
		entityManager.merge(EventoHistorico);
	}

	@Transactional
	public void remove(EventoHistorico EventoHistorico) {
		entityManager.remove(EventoHistorico);
	}

	@SuppressWarnings("unchecked")
	public List<EventoHistorico> findAll() {
		return entityManager.createNamedQuery("EventoHistorico.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<EventoHistorico> findByEvento(Evento evento) {
		return entityManager.createNamedQuery("EventoHistorico.findByEvento").setParameter("evento", evento).getResultList();
	}
	
	public EventoHistorico findById(Long codigo) {
		try {
			return (EventoHistorico) entityManager.createNamedQuery("EventoHistorico.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	
}