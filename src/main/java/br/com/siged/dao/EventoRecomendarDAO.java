package br.com.siged.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.EventoRecomendar;
import br.com.siged.util.StringUtil;

/**
 * 
 * @author Rodrigo
 */
@Repository("eventorecomendarDao")
public class EventoRecomendarDAO {
	protected EntityManager entityManager;

	@Autowired
	private SetorDAO setorDao;
	
	public EventoRecomendarDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EventoRecomendar find(Long id) {
		return entityManager.find(EventoRecomendar.class, id);
	}

	@Transactional
	public void persist(EventoRecomendar EventoRecomendar) {
		entityManager.persist(EventoRecomendar);
	}

	@Transactional
	public void merge(EventoRecomendar EventoRecomendar) {
		entityManager.merge(EventoRecomendar);
	}

	@Transactional
	public void remove(EventoRecomendar EventoRecomendar) {
		entityManager.remove(EventoRecomendar);
	}
	
	@SuppressWarnings("unchecked")
	public List<EventoRecomendar> findBySetorId(String setorId) {
		return entityManager.createNamedQuery("EventoRecomendar.findBySetorId").setParameter("setorId", setorDao.find(Long.parseLong(setorId))).getResultList();
	}


	@SuppressWarnings("unchecked")
	public List<EventoRecomendar> findAll() {
		return entityManager.createNamedQuery("EventoRecomendar.findAll").getResultList();
	}
	public EventoRecomendar findById(Long codigo) {
		try {
			return (EventoRecomendar) entityManager.createNamedQuery("EventoRecomendar.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Collection<EventoRecomendar> findEventoRecomendarByCriteria(Date data1, Date data2, String titulo, Long setor, String aprovado) {
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria aux = sess.createCriteria(EventoRecomendar.class);
		if (data1 != null) {
			aux.add(Restrictions.ge("dataInicio", data1));
		}
		if (data2 != null) {
			aux.add(Restrictions.le("dataInicio", data2));
		}
		if (titulo != null && !titulo.isEmpty()) {
			aux.add(Restrictions.sqlRestriction(StringUtil.sqlTranslateQuery("{alias}.titulo", titulo)));
			//aux.add(Restrictions.ilike("titulo", "%" + titulo + "%"));
		}
		if (setor != 0) {
			aux.add(Restrictions.eq("setorId", setorDao.findById(setor)));
		}		
		if (!aprovado.equals("0")) {
			aux.add(Restrictions.ilike("aprovado","%" + aprovado + "%") );
		}
	    return aux.list();
	    
	}
}