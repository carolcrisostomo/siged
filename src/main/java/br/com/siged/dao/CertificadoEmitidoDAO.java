package br.com.siged.dao;

import java.util.Collection;
import java.util.Date;
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

import br.com.siged.entidades.CertificadoEmitido;
import br.com.siged.util.Util;

/**
 * 
 * @author André
 */
@Repository("certificadoEmitidoDao")
public class CertificadoEmitidoDAO {
	protected EntityManager entityManager;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private Util util;

	public CertificadoEmitidoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public CertificadoEmitido find(Long id) {
		return entityManager.find(CertificadoEmitido.class, id);
	}

	@Transactional
	public void persist(CertificadoEmitido certificadoEmitido) {
		entityManager.persist(certificadoEmitido);
	}
	
	@Transactional
	public CertificadoEmitido persistAndFlush(CertificadoEmitido certificadoEmitido) {
		entityManager.persist(certificadoEmitido);
		entityManager.flush();
		return certificadoEmitido;
	}

	@Transactional
	public void merge(CertificadoEmitido certificadoEmitido) {
		entityManager.merge(certificadoEmitido);
	}

	@Transactional
	public void remove(CertificadoEmitido certificadoEmitido) {
		entityManager.remove(certificadoEmitido);
	}

	@SuppressWarnings("unchecked")
	public List<CertificadoEmitido> findAll() {
		return entityManager.createNamedQuery("CertificadoEmitido.findAll")
				.getResultList();

	}

	public CertificadoEmitido findById(Long id) {
		try {
			return (CertificadoEmitido) entityManager
					.createNamedQuery("CertificadoEmitido.findById")
					.setParameter("id", id).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	public CertificadoEmitido findByCodigoVerificacao(String codigoVerificacao) {
		try {
			return (CertificadoEmitido) entityManager
					.createNamedQuery("CertificadoEmitido.findByCodigoVerificacao")
					.setParameter("codigoVerificacao", codigoVerificacao).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<CertificadoEmitido> findByEventoId(Long eventoId) {
		
		return entityManager.createNamedQuery("CertificadoEmitido.findByEventoId")
				.setParameter("eventoId", eventoId)
				.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CertificadoEmitido> findByParticipanteId(Long participanteId) {
		
		return entityManager.createNamedQuery("CertificadoEmitido.findByParticipanteId")
				.setParameter("participanteId", participanteId)
				.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CertificadoEmitido> findByEventoIdAndParticipanteId(Long eventoId, Long participanteId) {
		
		return entityManager.createNamedQuery("CertificadoEmitido.findByEventoIdAndParticipanteId")
				.setParameter("eventoId", eventoId)
				.setParameter("participanteId", participanteId)
				.getResultList();
		
	}
	
	@Transactional
	public void deleteByEventoId(Long evento_id) {
		entityManager.createNativeQuery("DELETE FROM certificado_emitido WHERE evento_id = " + evento_id).executeUpdate();
	}
	
	@Transactional
	public void deleteByParticipanteId(Long participante_id) {
		entityManager.createNativeQuery("DELETE FROM certificado_emitido WHERE participante_id = " + participante_id).executeUpdate();
	}
	
	@Transactional
	public void deleteByEventoIdAndParticipanteId(Long evento_id, Long participante_id) {
		entityManager.createNativeQuery("DELETE FROM certificado_emitido WHERE evento_id = " + evento_id + " AND participante_id = " + participante_id).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public Collection<CertificadoEmitido> findCertificadoEmitidoByCriteria(Long eventoId, Long participanteId, Date data1, Date data2) {
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria aux = sess.createCriteria(CertificadoEmitido.class);
		if (eventoId != 0) {
			aux.add(Restrictions.eq("evento", eventoDao.findById(eventoId)));
		}
		if (participanteId != 0) {
			aux.add(Restrictions.eq("participante", participanteDao.findById(participanteId)));
		}
		if (data1 != null) {
			aux.add(Restrictions.ge("dataEmissao", data1));
		}
		if (data2 != null) {
			aux.add(Restrictions.lt("dataEmissao", util.getTomorrowDate(data2)));
		}
		
		aux.createCriteria("evento", "e").addOrder(Order.desc("e.dataInicioPrevisto"));
		aux.createCriteria("participante", "p").addOrder(Order.asc("p.nome"));
		
	    return aux.list();
	    
	}

}
