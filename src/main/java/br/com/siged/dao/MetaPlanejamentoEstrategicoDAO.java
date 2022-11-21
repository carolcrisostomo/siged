package br.com.siged.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

import br.com.siged.dao.pagination.PaginatedListAdapter;
import br.com.siged.dao.pagination.PaginationUtil;
import br.com.siged.entidades.MetaPlanejamentoEstrategico;
import br.com.siged.filtro.MetaPlanejamentoEstrategicoFiltro;

@Repository("metaPlanejamentoEstrategicoDao")
public class MetaPlanejamentoEstrategicoDAO {
	
	protected EntityManager entityManager;
	
	@Autowired
	private PaginationUtil paginationUtil;

	public MetaPlanejamentoEstrategicoDAO() {
	}
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public MetaPlanejamentoEstrategico find(Long id) {
		return entityManager.find(MetaPlanejamentoEstrategico.class, id);
	}
	
	public void persist(MetaPlanejamentoEstrategico meta) {
		entityManager.persist(meta);
	}

	public void merge(MetaPlanejamentoEstrategico meta) {
		entityManager.merge(meta);
	}
	
	public void remove(MetaPlanejamentoEstrategico meta) {
		entityManager.remove(meta);
	}
	
	public MetaPlanejamentoEstrategico findByAno(Integer ano)  {
		String hql = "Select mp From MetaPlanejamentoEstrategico mp Where mp.ano = :ano";
		try {
			return (MetaPlanejamentoEstrategico) entityManager.createQuery(hql, MetaPlanejamentoEstrategico.class)
					.setParameter("ano", ano)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public PaginatedList filtrar(MetaPlanejamentoEstrategicoFiltro filtro, Pageable pageable) {
		Criteria criteria = createCriteria();
		
		paginationUtil.preparar(criteria, pageable);
		adicionarFiltro(criteria, filtro);
		
		if(pageable.getSort() == null)
			criteria.addOrder(Order.asc("ano"));
		
		return new PaginatedListAdapter(new PageImpl<>(criteria.list(), pageable, total(filtro)));
	}
	
	private void adicionarFiltro(Criteria criteria, MetaPlanejamentoEstrategicoFiltro filtro) {
		if (filtro.getAno() != null && filtro.getAno() > 0)
			criteria.add(Restrictions.eq("ano", filtro.getAno()));
	}
	
	private Long total(MetaPlanejamentoEstrategicoFiltro filtro) {
		Criteria criteria = createCriteria();
		adicionarFiltro(criteria, filtro);
		criteria.setProjection(Projections.rowCount());
		
		return (Long) criteria.uniqueResult();
	}
	
	private Criteria createCriteria() {
		Session sess = (Session) entityManager.getDelegate();
		return sess.createCriteria(MetaPlanejamentoEstrategico.class);
	}
}
