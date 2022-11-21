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

import br.com.siged.entidades.Gasto;
/**
 *
 * @author Rodrigo
 */
@Repository("gastoDao")
public class GastoDAO {
	protected EntityManager entityManager;
	@Autowired
	EventoDAO eventoDao;
	@Autowired
	TipoGastoDAO tipoGastoDao;
	@Autowired
	FonteGastoDAO fonteGastoDao;
	@Autowired
	ParticipanteDAO participanteDao;
	@Autowired
	SetorDAO setorDao;
	@Autowired
	EntidadeDAO entidadeDao;
	
	public GastoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Gasto find(Long id) {
		return entityManager.find(Gasto.class, id);
	}

	@Transactional
	public void persist(Gasto Gasto) {
		entityManager.persist(Gasto);
	}

	@Transactional
	public void merge(Gasto Gasto) {
		entityManager.merge(Gasto);
	}

	@Transactional
	public void remove(Gasto Gasto) {
		entityManager.remove(Gasto);
	}

	@SuppressWarnings("unchecked")
	public List<Gasto> findAll() {
		return entityManager.createNamedQuery("Gasto.findAll").getResultList();
	}	
	
	public Gasto findById(Long codigo) {
		try {
			return (Gasto) entityManager.createNamedQuery("Gasto.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Gasto> findByEvento(Long codigoEvento) {
		return entityManager.createNamedQuery("Gasto.findByEvento").setParameter("evento", eventoDao.find(codigoEvento)).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Gasto> findGastoByCriteria(Long eventoId, Long tipoId, Long fonteId, Long participanteId, String numeroEmpenho, String numeroProcesso) {
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria aux = sess.createCriteria(Gasto.class);
		if (eventoId != 0) {
			aux.add(Restrictions.eq("eventoId", eventoDao.findById(eventoId)));
		}
		if (tipoId != 0) {
			aux.add(Restrictions.eq("tipoId", tipoGastoDao.findById(tipoId)));
		}
		
		if (fonteId != 0) {
			aux.add(Restrictions.eq("fonteId", fonteGastoDao.findById(fonteId)));
		}
		
		if (participanteId != 0) {
			aux.add(Restrictions.eq("participanteId", participanteDao.findById(participanteId)));
		}
		
		if (numeroEmpenho != "") {
			aux.add(Restrictions.ilike("numeroEmpenho", "%" + numeroEmpenho + "%"));
		}
		
		if (numeroProcesso != "") {
			aux.add(Restrictions.ilike("numeroProcesso", "%" + numeroProcesso + "%"));
		}
		
		aux.createCriteria("eventoId", "e").addOrder(Order.desc("e.dataInicioPrevisto"));
		
	    return aux.list();
	    
	}
	
	@SuppressWarnings({"unchecked", "unused"})
	public Collection<Gasto> findGastoByCriteria(Long eventoId, Long participanteId, Long instrutorId, Long tipoGastoId, String eventoDataInicio, String eventoDataFim, 
			Long setorId, Integer esferaId, Long orgaoId){
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria gasto = sess.createCriteria(Gasto.class, "g");
		Criteria evento = gasto.createCriteria("eventoId", "e", Criteria.INNER_JOIN);
		Criteria inscricao = evento.createCriteria("inscricaoList", "i", Criteria.INNER_JOIN);
		Criteria instrutor = gasto.createCriteria("instrutor", "inst", Criteria.LEFT_JOIN);
		
		//Participante, Setor e Orgao vinculado ao Evento que teve o Gasto
		Criteria participanteEvento = inscricao.createCriteria("participanteId", "pe", Criteria.INNER_JOIN);
		Criteria setorEvento = participanteEvento.createCriteria("setorId", "se", Criteria.LEFT_JOIN);
		Criteria orgaoEvento = participanteEvento.createCriteria("orgaoId", "oe", Criteria.LEFT_JOIN);
		
		//Participante, Setor e Orgao vinculado diretamente ao Gasto
		Criteria participanteDoGasto = gasto.createCriteria("participanteId", "pg", Criteria.LEFT_JOIN);
		Criteria setorDoGasto = participanteDoGasto.createCriteria("setorId", "sg", Criteria.LEFT_JOIN);
		Criteria orgaoDoGasto = participanteDoGasto.createCriteria("orgaoId", "og", Criteria.LEFT_JOIN);
		
		if (eventoId != null && eventoId != 0) {
			gasto.add(Restrictions.eq("eventoId", eventoDao.findById(eventoId)));
		}
		
		if (participanteId != null && participanteId != 0) {
			gasto.add(
				Restrictions.or(
					Restrictions.eq("pg.id", participanteId), 
					Restrictions.eq("pe.id", participanteId)
				));
			//participanteDoGasto.add(Restrictions.eq("id", participanteId));
		}
		
		if(instrutorId != null && instrutorId != 0) {
			instrutor.add(Restrictions.eq("id", instrutorId));
		}
		
		if(setorId != null && setorId != -1){
			gasto.add(
				Restrictions.or(
					Restrictions.eq("sg.id", setorId), 
					Restrictions.eq("se.id", setorId)
				));
			//setorDoGasto.add(Restrictions.eq("id", setorId));
		}
		
		if(esferaId != null) {
			if(esferaId.equals(2)) {
				gasto.add(
						Restrictions.conjunction()
							.add(Restrictions.disjunction()
									.add(Restrictions.eq("og.tpentidadeesfera", 2))
									.add(Restrictions.eq("oe.tpentidadeesfera", 2)))
							.add(Restrictions.disjunction()
									.add(Restrictions.eq("og.fiscalizado", 1))
									.add(Restrictions.eq("oe.fiscalizado", 1)))
							.add(Restrictions.disjunction()
									.add(Restrictions.ne("og.identidade", 87l))
									.add(Restrictions.ne("oe.identidade", 87l)))
				);
			} else if(esferaId.equals(3)) {
				gasto.add(
						Restrictions.conjunction()
							.add(Restrictions.disjunction()
									.add(Restrictions.eq("og.tpentidadeesfera", 3))
									.add(Restrictions.eq("oe.tpentidadeesfera", 3)))
							.add(Restrictions.disjunction()
									.add(Restrictions.eq("og.fiscalizado", 1))
									.add(Restrictions.eq("oe.fiscalizado", 1)))
				);
			}
		}
		
		if(orgaoId != null && orgaoId != 0){
			gasto.add(
				Restrictions.or(
					Restrictions.eq("og.identidade", orgaoId), 
					Restrictions.eq("oe.identidade", orgaoId)
				));
			//orgaoDoGasto.add(Restrictions.eq("id", orgaoId));
		}
		
		if (tipoGastoId != null && tipoGastoId != 0) {
			gasto.add(Restrictions.eq("tipoId", tipoGastoDao.findById(tipoGastoId)));
		}
		
		if((eventoDataInicio != null && eventoDataFim != null) && (!eventoDataInicio.equals("") && !eventoDataFim.equals(""))){
			evento.add(Restrictions.sqlRestriction("NVL(data_inicio_realizacao, data_inicio_previsto) >= TO_DATE('" + eventoDataInicio + "', 'dd/MM/yyyy')"));
			evento.add(Restrictions.sqlRestriction("NVL(data_inicio_realizacao, data_inicio_previsto) <= TO_DATE('" + eventoDataFim + "', 'dd/MM/yyyy')"));
		}
		
		inscricao.add(Restrictions.eq("confirmada", "S"));
		
		gasto.addOrder(Order.desc("e.dataInicioRealizacao")).addOrder(Order.desc("e.dataInicioPrevisto")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
	    return gasto.list();
	}
}