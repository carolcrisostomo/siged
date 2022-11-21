package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.ResponsavelSetor;
import br.com.siged.entidades.externas.Setor;
import br.com.siged.entidades.externas.UsuarioSCA;

/**
 * 
 * @author André
 */
@Repository("responsavelSetorDao")
public class ResponsavelSetorDAO {
	protected EntityManager entityManager;

	public ResponsavelSetorDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public ResponsavelSetor find(Long id) {
		return entityManager.find(ResponsavelSetor.class, id);
	}

	@Transactional
	public void persist(ResponsavelSetor responsavelSetor) {
		entityManager.persist(responsavelSetor);
	}

	@Transactional
	public void merge(ResponsavelSetor responsavelSetor) {
		entityManager.merge(responsavelSetor);
	}

	@Transactional
	public void remove(ResponsavelSetor responsavelSetor) {
		entityManager.remove(responsavelSetor);
	}

	public ResponsavelSetor findById(Long id) {
		return (ResponsavelSetor) entityManager
				.createNamedQuery("ResponsavelSetor.findById")
				.setParameter("id", id).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<ResponsavelSetor> findAll() {
		return entityManager.createNamedQuery("ResponsavelSetor.findAll")
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ResponsavelSetor> findBySetorId(Setor setor) {
		Query query = entityManager.createNamedQuery("ResponsavelSetor.findBySetorId");
		query.setParameter("setorId", setor.getId());
		return query.getResultList();
	}

	public ResponsavelSetor findAtivoBySetorId(Long setorId) {
		try {
			Query query = entityManager.createNamedQuery("ResponsavelSetor.findAtivoBySetorId");
			query.setParameter("setorId", setorId);
			query.setParameter("ativo", true);
			return (ResponsavelSetor) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ResponsavelSetor> findAtivoByResponsavel(UsuarioSCA responsavel) {
		try {
			Query query = entityManager.createNamedQuery("ResponsavelSetor.findAtivoByResponsavel");
			query.setParameter("responsavel", responsavel);
			query.setParameter("ativo", true);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ResponsavelSetor> findAtivoByResponsavelImediato(UsuarioSCA responsavelImediato) {
		Query query = entityManager.createNamedQuery("ResponsavelSetor.findAtivoByResponsavelImediato");
		query.setParameter("responsavelImediato", responsavelImediato);
		query.setParameter("ativo", true);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ResponsavelSetor> findResponsavelSetorByCriteria(Setor setor) {
		Session sess = (Session) entityManager.getDelegate();

		Criteria aux = sess.createCriteria(ResponsavelSetor.class);
		if (setor != null && setor.getId() != 9999) {
			aux.add(Restrictions.eq("setor", setor));
		}

		return aux.list();
	}

}
