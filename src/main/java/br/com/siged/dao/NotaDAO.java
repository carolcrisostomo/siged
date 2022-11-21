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

import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Nota;
import br.com.siged.entidades.Participante;
/**
 *
 * @author Rodrigo
 */
@Repository("notaDao")
public class NotaDAO {
	protected EntityManager entityManager;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ModuloDAO moduloDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	
	

	public NotaDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Nota find(Long id) {
		return entityManager.find(Nota.class, id);
	}

	@Transactional
	public void persist(Nota Nota) {
		entityManager.persist(Nota);
	}

	@Transactional
	public void merge(Nota Nota) {
		entityManager.merge(Nota);
	}

	@Transactional
	public void remove(Nota Nota) {
		entityManager.remove(Nota);
	}

	@SuppressWarnings("unchecked")
	public List<Nota> findAll() {
		return entityManager.createNamedQuery("Nota.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Nota> findByEvento(Long codigoEvento) {
		return entityManager.createNamedQuery("Nota.findByEvento").setParameter("evento", eventoDao.find(codigoEvento)).getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<Nota> findByModulo(Modulo modulo) {
		return entityManager.createNamedQuery("Nota.findByModulo").setParameter("modulo", modulo).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Nota> findByEventoAndParticipante(Evento evento, Participante participante) {
		return entityManager.createNamedQuery("Nota.findByEventoAndParticipante").setParameter("evento", evento).setParameter("participante", participante).getResultList();
	}
	
	
	public Nota findByEventoAndModuloAndParticipante(Evento evento, Modulo modulo, Participante participante) {
		try {
		return (Nota) entityManager.createNamedQuery("Nota.findByEventoAndModuloAndParticipante").setParameter("evento", evento).setParameter("modulo", modulo).setParameter("participante", participante).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	public Nota findById(Long codigo) {
		try {
			return (Nota) entityManager.createNamedQuery("Nota.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Nota> findNotaByCriteria(Long eventoId, Long participanteId, Long moduloId) {
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria aux = sess.createCriteria(Nota.class);
		if (eventoId != 0) {
			aux.add(Restrictions.eq("eventoId", eventoDao.findById(eventoId)));
		}
		
		if (moduloId != 0) {
			aux.add(Restrictions.eq("moduloId", moduloDao.findById(moduloId)));
		}
		
		if (participanteId != 0) {
			aux.add(Restrictions.eq("participanteId", participanteDao.findById(participanteId)));
		}
		
		aux.createCriteria("eventoId", "e").addOrder(Order.desc("e.dataInicioPrevisto"));
		aux.createCriteria("participanteId", "p").addOrder(Order.asc("p.nome"));
		
	    return aux.list();
	    
	}
}