package br.com.siged.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.EventoMaterial;
import br.com.siged.util.StringUtil;

@Repository("eventoMaterialDao")
public class EventoMaterialDAO {
	
	protected EntityManager entityManager;
	
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ModuloDAO moduloDao;
		
	
	public EventoMaterialDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EventoMaterial find(Long id) {
		return entityManager.find(EventoMaterial.class, id);
	}

	@Transactional
	public void persist(EventoMaterial material) {
		entityManager.persist(material);
	}

	@Transactional
	public void merge(EventoMaterial material) {
		entityManager.merge(material);
	}

	@Transactional
	public void remove(EventoMaterial material) {
		entityManager.remove(material);
	}

	@SuppressWarnings("unchecked")
	public List<EventoMaterial> findAll() {
		return entityManager.createNamedQuery("EventoMaterial.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<EventoMaterial> findByEventoETipo(Long eventoId, Long codigoTipo) {
		
		return entityManager.createNamedQuery("EventoMaterial.findByEventoETipo").setParameter("eventoId", eventoId).setParameter("materialTipo", codigoTipo).getResultList();
	}	
	
	@SuppressWarnings("unchecked")
	public Collection<EventoMaterial> findMaterialByCriteria(Long eventoId, Long moduloId, Long materialTipo, String descricao) {
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria aux = sess.createCriteria(EventoMaterial.class);
		
		if (eventoId != 0) {
			aux.add(Restrictions.eq("eventoId", eventoDao.findById(eventoId)));
		}
		if (moduloId != 0) {
			aux.add(Restrictions.eq("moduloId", moduloDao.findById(moduloId)));
		}
		if (materialTipo != 0) {
			aux.add(Restrictions.eq("materialTipo", materialTipo));
		}
		if (descricao != null && !descricao.isEmpty()) {
			aux.add(Restrictions.sqlRestriction(StringUtil.sqlTranslateQuery("{alias}.descricao", descricao)));
			//aux.add(Restrictions.ilike("descricao", "%" + descricao + "%"));
		}
		
		aux.createCriteria("eventoId", "e").addOrder(Order.desc("e.dataInicioPrevisto"));
		aux.addOrder(Order.asc("descricao"));
		
	    return aux.list();
	    
	}
}