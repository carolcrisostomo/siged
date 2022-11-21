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

import br.com.siged.entidades.EventoExtra;
import br.com.siged.entidades.externas.UsuarioSCA;
import br.com.siged.util.StringUtil;
/**
 *
 * @author Rodrigo
 */
@Repository("eventoextraDao")
public class EventoExtraDAO {
	protected EntityManager entityManager;
	
	@Autowired
	private UsuarioSCADAO usuarioDao;
	
	public EventoExtraDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EventoExtra find(Long id) {
		return entityManager.find(EventoExtra.class, id);
	}

	@Transactional
	public void persist(EventoExtra EventoExtra) {
		entityManager.persist(EventoExtra);
	}

	@Transactional
	public void merge(EventoExtra EventoExtra) {
		entityManager.merge(EventoExtra);
	}

	@Transactional
	public void remove(EventoExtra EventoExtra) {
		entityManager.remove(EventoExtra);
	}

	@SuppressWarnings("unchecked")
	public List<EventoExtra> findAll() {
		return entityManager.createNamedQuery("EventoExtra.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<EventoExtra> findBySolicitanteLogin(String solicitante) {
		return entityManager.createNamedQuery("EventoExtra.findBySolicitanteLogin").setParameter("solicitante", usuarioDao.findByLogin(solicitante)).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<EventoExtra> findBySolicitante(UsuarioSCA usuario) {
		return entityManager.createNamedQuery("EventoExtra.findBySolicitante").setParameter("solicitante", usuario).getResultList();
	}
	
	//Encontra as solicitações de um participante que ainda não foram atendidas(ainda não possui inscrição no evento fruto da solicitação)
	@SuppressWarnings("unchecked")
	public List<EventoExtra> findBySolicitanteSemInscricao(UsuarioSCA usuario) {
		return entityManager.createNamedQuery("EventoExtra.findBySolicitanteSemInscricao").setParameter("solicitante", usuario).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<EventoExtra> findByChefe(String chefe) {
		return entityManager.createNamedQuery("EventoExtra.findByChefe").setParameter("chefe", usuarioDao.findByLogin(chefe)).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<EventoExtra> findByChefeAndIndicada(Long chefeId, String indicada) {
		return entityManager.createNamedQuery("EventoExtra.findByChefeAndIndicada")
				.setParameter("chefeId", chefeId)
				.setParameter("indicada", indicada)
				.getResultList();
	}

	public EventoExtra findById(Long codigo) {
		try {
			return (EventoExtra) entityManager.createNamedQuery("EventoExtra.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Collection<EventoExtra> findEventoExtraByCriteria(String cpf, String curso, Date data1, Date data2, String indicada) {
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria aux = sess.createCriteria(EventoExtra.class);

		if (curso != null && !curso.isEmpty()) {
			aux.add(Restrictions.sqlRestriction(StringUtil.sqlTranslateQuery("{alias}.curso", curso)));
			//aux.add(Restrictions.ilike("curso", "%" + curso + "%"));
		}
		if (data1 != null) {
			aux.add(Restrictions.ge("dataInicio", data1));
		}
		if (data2 != null) {
			aux.add(Restrictions.le("dataInicio", data2));
		}
		if (!indicada.equalsIgnoreCase("0")) {
			aux.add(Restrictions.eq("indicada", indicada));
		}
		if (cpf != "") {
			aux.createCriteria("solicitanteId", "u").add(Restrictions.eq("u.cpf", cpf.replace(".", "").replace("-", "")));
		}
		
		
		
	    return aux.list();
	    
	}
	
}