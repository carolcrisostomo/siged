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

import br.com.siged.entidades.Certificado;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Participante;
/**
 *
 * @author Rodrigo
 */
@Repository("certificadoDao")
public class CertificadoDAO {
	protected EntityManager entityManager;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	
	

	public CertificadoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Certificado find(Long id) {
		return entityManager.find(Certificado.class, id);
	}

	@Transactional
	public void persist(Certificado Certificado) {
		entityManager.persist(Certificado);
	}

	@Transactional
	public void merge(Certificado Certificado) {
		entityManager.merge(Certificado);
	}

	@Transactional
	public void remove(Certificado Certificado) {
		entityManager.remove(Certificado);
	}

	@SuppressWarnings("unchecked")
	public List<Certificado> findAll() {
		return entityManager.createNamedQuery("Certificado.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Certificado> findByEvento(Long codigoEvento) {
		return entityManager.createNamedQuery("Certificado.findByEvento").setParameter("evento", eventoDao.find(codigoEvento)).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Certificado> findByEventoAndParticipante(Evento evento, Participante participante) {
		return entityManager.createNamedQuery("Certificado.findByEventoAndParticipante").setParameter("evento", evento).setParameter("participante", participante).getResultList();
	}
	
	public Certificado findById(Long codigo) {
		try {
			return (Certificado) entityManager.createNamedQuery("Certificado.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Certificado> findCertificadoByCriteria(Long eventoId, Long participanteId) {
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria aux = sess.createCriteria(Certificado.class);
		if (eventoId != 0) {
			aux.add(Restrictions.eq("eventoId", eventoDao.findById(eventoId)));
		}
		if (participanteId != 0) {
			aux.add(Restrictions.eq("participanteId", participanteDao.findById(participanteId)));
		}
		
		aux.createCriteria("eventoId", "e").addOrder(Order.desc("e.dataInicioPrevisto"));
		aux.createCriteria("participanteId", "p").addOrder(Order.asc("p.nome"));
		
	    return aux.list();
	    
	}
}