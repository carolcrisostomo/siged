package br.com.siged.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.displaytag.pagination.PaginatedList;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.dao.pagination.PaginatedListAdapter;
import br.com.siged.dao.pagination.PaginationUtil;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Modalidade;
import br.com.siged.entidades.Modulo;
import br.com.siged.filtro.ModuloFiltro;
import br.com.siged.util.StringUtil;
/**
 *
 * @author Rodrigo
 */
@Repository("moduloDao")
public class ModuloDAO {
	protected EntityManager entityManager;
	
	@Autowired
	private PaginationUtil paginationUtil;
	
	public ModuloDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Modulo find(Long id) {
		return entityManager.find(Modulo.class, id);
	}

	@Transactional
	public void persist(Modulo Modulo) {
		entityManager.persist(Modulo);
	}

	@Transactional
	public void merge(Modulo Modulo) {
		entityManager.merge(Modulo);
	}

	@Transactional
	public void remove(Modulo Modulo) {
		entityManager.remove(Modulo);
	}

	@SuppressWarnings("unchecked")
	public List<Modulo> findAll() {
		return entityManager.createNamedQuery("Modulo.findAll").getResultList();
	}
	
	public Modulo findById(Long codigo) {
		try {
			return (Modulo) entityManager.createNamedQuery("Modulo.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public PaginatedList filtrar(ModuloFiltro filtro, Pageable pageable) {
		Criteria criteria = createCriteria();
		
		paginationUtil.preparar(criteria, pageable);
		adicionarFiltro(criteria, filtro);
		
		if(pageable.getSort() == null)
			criteria.addOrder(Order.desc("dataInicio"));
		
		return new PaginatedListAdapter(new PageImpl<>(criteria.list(), pageable, total(filtro)));
	}
	
	private void adicionarFiltro(Criteria criteria, ModuloFiltro filtro) {
		Criteria evento = criteria.createCriteria("eventoId", "e", Criteria.INNER_JOIN);
		if (filtro.getData1() != null)
			criteria.add(Restrictions.ge("dataInicio", filtro.getData1()));

		if (filtro.getData2() != null)
			criteria.add(Restrictions.le("dataInicio", filtro.getData2()));
		
		if (filtro.getEvento() != null && filtro.getEvento() != 0) 
			criteria.add(Restrictions.eq("e.id", filtro.getEvento()));
		else if (filtro.getEventoTitulo() != null && !filtro.getEventoTitulo().isEmpty())
			evento.add(Restrictions.sqlRestriction(StringUtil.sqlTranslateQuery("{alias}.titulo", filtro.getEventoTitulo())));
	}
	
	private Long total(ModuloFiltro filtro) {
		Criteria criteria = createCriteria();
		adicionarFiltro(criteria, filtro);
		criteria.setProjection(Projections.rowCount());
		
		return (Long) criteria.uniqueResult();
	}
	
	private Criteria createCriteria() {
		Session sess = (Session) entityManager.getDelegate();
		return sess.createCriteria(Modulo.class);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Modulo> findModuloByCriteria(Long eventoId, String eventoTitulo, Date data1, Date data2) {
		
		Criteria modulo = createCriteria();
		Criteria evento = modulo.createCriteria("eventoId", "e", Criteria.INNER_JOIN);
		if (data1 != null) {
			modulo.add(Restrictions.ge("dataInicio", data1));
		}
		if (data2 != null) {
			modulo.add(Restrictions.le("dataInicio", data2));
		}
		if (eventoId != 0) {
			evento.add(Restrictions.eq("id", eventoId));
		} else {
			if (eventoTitulo != null && !eventoTitulo.isEmpty()) {
				evento.add(Restrictions.sqlRestriction(StringUtil.sqlTranslateQuery("{alias}.titulo", eventoTitulo)));
				//modulo.add(Restrictions.ilike("e.titulo", "%" + eventoTitulo + "%"));
			}
		}
		
	    return modulo.list();
	    
	}
	
	@SuppressWarnings("unchecked")
	public List<Modulo> findByEventoIdByNota(Long eventoId) {
		Query query = entityManager.createNamedQuery("Modulo.findByEventoIdByNota");
		
		query.setParameter("eventoId", eventoId);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Modulo> findByEventoId(Long eventoId) {
		Query query = entityManager.createNamedQuery("Modulo.findByEventoId");
		
		query.setParameter("eventoId", eventoId);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Modulo> findRealizadosByEvento(Long eventoId) {
		String sql = "SELECT m FROM Modulo m WHERE m.eventoId.id = :eventoId AND trunc(SYSDATE, 'DDD') > m.dataFim";
		
		Query query = entityManager.createQuery(sql)
				.setParameter("eventoId", eventoId);
		
		return query.getResultList();
	}
	
	public Modulo findApurado(Long moduloId) {
		String sql = "SELECT m FROM Modulo m "
				+ "WHERE m.id in (SELECT DISTINCT d.moduloId.id FROM Desempenho d) "
				+ "AND m.id = :id";
		Query query = entityManager.createQuery(sql, Modulo.class)
					.setParameter("id", moduloId);
		
		try {
			return (Modulo) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		
	}
	
	public List<Modulo> findModuloComAvaliacaoPendenteADMIN(Long eventoId){
		return entityManager.createQuery(
				"SELECT m FROM Modulo m INNER JOIN m.eventoId e WHERE e.id = :eventoId "
				+ "AND SYSDATE > m.dataFim + 1", Modulo.class)
				.setParameter("eventoId", eventoId)
				.getResultList();
	}
	
	public List<Modulo> findModuloComAvaliacoesRespondidas(Long eventoId){
		return entityManager.createQuery(
				"SELECT DISTINCT m FROM Modulo m INNER JOIN m.eventoId e INNER JOIN m.avalicoesReacaoList ars WHERE e.id = :eventoId AND size(ars) > 0 ORDER BY m.dataInicio", Modulo.class)
				.setParameter("eventoId", eventoId)
				.getResultList();
	}
	
	public Modulo findByEvento(Evento eventoId) {
		Query query = entityManager.createNamedQuery("Modulo.findByEvento");
		
		query.setParameter("evento", eventoId);
		
		return (Modulo) query.getSingleResult();
	}
	
	public List<Modulo> findPresencialByEvento(Long eventoId) {
		String jpql = "Select m From Modulo m Where m.eventoId.id = :eventoId and m.modalidade.id = :modalidade";
		
		return entityManager.createQuery(jpql, Modulo.class)
				.setParameter("eventoId", eventoId)
				.setParameter("modalidade", Modalidade.PRESENCIAL)
				.getResultList();
	}
}