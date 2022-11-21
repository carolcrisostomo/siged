package br.com.siged.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Frequencia;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Participante;
/**
 *
 * @author Rodrigo
 */
@Repository("frequenciaDao")
public class FrequenciaDAO {
	protected EntityManager entityManager;
	
	public FrequenciaDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Frequencia find(Long id) {
		return entityManager.find(Frequencia.class, id);
	}

	@Transactional
	public void persist(Frequencia Frequencia) {
		entityManager.persist(Frequencia);
	}

	@Transactional
	public void merge(Frequencia Frequencia) {
		entityManager.merge(Frequencia);
	}

	@Transactional
	public void remove(Frequencia Frequencia) {
		entityManager.remove(Frequencia);
	}

	@SuppressWarnings("unchecked")
	public List<Frequencia> findAll() {
		return entityManager.createNamedQuery("Frequencia.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Frequencia> findByEvento(Evento evento) {
		return entityManager.createNamedQuery("Frequencia.findByEvento").setParameter("evento", evento).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Frequencia> findByModulo(Modulo modulo) {
		return entityManager.createNamedQuery("Frequencia.findByModulo").setParameter("modulo", modulo).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Frequencia> findByEventoAndModuloAndParticipante(Evento evento, Modulo modulo, Participante participante) {
		return entityManager.createNamedQuery("Frequencia.findByEventoAndModuloAndParticipante").setParameter("evento", evento).setParameter("modulo", modulo).setParameter("participante", participante).getResultList();
	}
	
	public Frequencia findByEventoAndModuloAndData(Evento evento, Modulo modulo, Date data, String turno) {
		try {
			return (Frequencia) entityManager.createNamedQuery("Frequencia.findByEventoAndModuloAndData").setParameter("evento", evento).setParameter("modulo", modulo).setParameter("data", data).setParameter("turno", turno).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Frequencia> findByEventoAndParticipante(Evento evento, Participante participante) {
		return entityManager.createNamedQuery("Frequencia.findByEventoAndParticipante").setParameter("evento", evento).setParameter("participante", participante).getResultList();
	}
	
	public Frequencia findById(Long codigo) {
		try {
			return (Frequencia) entityManager.createNamedQuery("Frequencia.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
//	@SuppressWarnings("unchecked")
//	public Collection<Frequencia> findFrequenciaByCriteria(Long eventoId, Long moduloId, Date data1, Date data2) {
//		
//		Session sess = (Session) entityManager.getDelegate();
//		
//		Criteria aux = sess.createCriteria(Frequencia.class);
//		if (eventoId != 0) {
//			aux.add(Restrictions.eq("eventoId", eventoDao.findById(eventoId)));
//		}
//		if (moduloId != 0) {
//			aux.add(Restrictions.eq("moduloId", moduloDao.findById(moduloId)));
//		}
//		
//		if (data1 != null) {
//			aux.add(Restrictions.ge("data", data1));
//		}
//		if (data2 != null) {
//			aux.add(Restrictions.le("data", data2));
//		}
//		
//	    return aux.list();
//	    
//	}
	
	@SuppressWarnings("unchecked")
	public List<Frequencia> findFrequenciaByCriteria(Long eventoId, Long moduloId,Long participanteId, Date data1, Date data2) {
		//entityManager.createNamedQuery(arg0, arg1)
		StringBuilder sql = new StringBuilder("SELECT FREQUENCIA_PARTICIPANTE.FREQUENCIA_ID AS ID, FREQUENCIA.DATA, FREQUENCIA.TURNO, FREQUENCIA.EVENTO_ID, FREQUENCIA.MODULO_ID, FREQUENCIA.PARTICIPANTEID, EVENTO.DATA_INICIO_PREVISTO" );
		sql.append(" FROM EVENTO INNER JOIN (MODULO INNER JOIN (PARTICIPANTE " );
		sql.append(" INNER JOIN (FREQUENCIA INNER JOIN FREQUENCIA_PARTICIPANTE ON FREQUENCIA.ID = FREQUENCIA_PARTICIPANTE.FREQUENCIA_ID) ");
		sql.append(" ON PARTICIPANTE.ID = FREQUENCIA_PARTICIPANTE.PARTICIPANTE_ID) ON MODULO.ID = FREQUENCIA.MODULO_ID) ");
		sql.append(" ON EVENTO.ID = FREQUENCIA.EVENTO_ID ");
		sql.append(" WHERE 1=1 " );
		if (eventoId != 0) 
			sql.append(" AND EVENTO.ID ="+ eventoId );
		if (participanteId != 0) 
			sql.append(" AND PARTICIPANTE.ID="+ participanteId  );
		if (moduloId != 0) 
			sql.append(" AND MODULO.ID="+ moduloId );
		if (data1 != null) 	 
			sql.append(" AND To_Date(To_Char(FREQUENCIA.DATA,'dd/mm/yyyy'),'dd/mm/yyyy') >=  To_Date('"+new SimpleDateFormat("dd/MM/yyyy").format(data1)+"','dd/mm/yyyy')");
		if (data2 != null) 
			sql.append(" AND To_Date(To_Char(FREQUENCIA.DATA,'dd/mm/yyyy'),'dd/mm/yyyy') <=  To_Date('"+new SimpleDateFormat("dd/MM/yyyy").format(data2)+"','dd/mm/yyyy')");
		 
		sql.append(" GROUP BY FREQUENCIA_PARTICIPANTE.FREQUENCIA_ID, FREQUENCIA.DATA, FREQUENCIA.TURNO, FREQUENCIA.EVENTO_ID, FREQUENCIA.MODULO_ID, FREQUENCIA.PARTICIPANTEID, EVENTO.DATA_INICIO_PREVISTO");
		sql.append(" ORDER BY EVENTO.DATA_INICIO_PREVISTO DESC" );
		
		return  entityManager.createNativeQuery(sql.toString(),Frequencia.class).getResultList();
	}

}