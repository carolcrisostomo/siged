package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.externas.Entidade;
/**
 *
 * @author Rodrigo
 */
@Repository("entidadeDAO")
public class EntidadeDAO {
	protected EntityManager entityManager;

	public EntidadeDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Entidade find(Long identidade) {
		return entityManager.find(Entidade.class, identidade);
	}

	@Transactional
	public void persist(Entidade Entidade) {
		entityManager.persist(Entidade);
	}

	@Transactional
	public void merge(Entidade Entidade) {
		entityManager.merge(Entidade);
	}

	@Transactional
	public void remove(Entidade Entidade) {
		entityManager.remove(Entidade);
	}

	@SuppressWarnings("unchecked")
	public List<Entidade> findAll() {
		return entityManager.createNamedQuery("Entidade.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Entidade> findEntidadesEstaduais() {
		return entityManager.createQuery("SELECT e FROM Entidade e WHERE e.tpentidadeesfera=2 and e.fiscalizado = 1 and e.localidade.idLocalidade = 1 and e.identidade <> 87 ORDER BY e.dsentidade").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Entidade> findEntidadeMunicipais() {
		Query query = entityManager.createQuery("SELECT e FROM Entidade e WHERE e.tpentidadeesfera=3 and e.fiscalizado = 1 ORDER BY e.dsentidade");
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Entidade> findEntidadeMunicipaisByLocalidade(Long idLocalidade) {
		Query query = entityManager.createQuery("SELECT e FROM Entidade e WHERE e.tpentidadeesfera=3 and e.fiscalizado = 1 and e.localidade.idLocalidade = :idLocalidade ORDER BY e.dsentidade");
		query.setParameter("idLocalidade", idLocalidade);
		return query.getResultList();
	}
	
	
	public Entidade findById(Long codigo) {
		try {
			return (Entidade) entityManager.createNamedQuery("Entidade.findById").setParameter("identidade", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}