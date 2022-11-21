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
import br.com.siged.entidades.MetaDesempenhoProdutividade;
import br.com.siged.filtro.MetaDesempenhoProdutividadeFiltro;

@Repository("metaDesempenhoProdutividadeDao")
public class MetaDesempenhoProdutividadeDAO {

	protected EntityManager entityManager;
	
	@Autowired
	private PaginationUtil paginationUtil;

	public MetaDesempenhoProdutividadeDAO() {
	}
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public MetaDesempenhoProdutividade find(Long id) {
		return entityManager.find(MetaDesempenhoProdutividade.class, id);
	}
	
	public void persist(MetaDesempenhoProdutividade meta) {
		entityManager.persist(meta);
	}

	public void merge(MetaDesempenhoProdutividade meta) {
		entityManager.merge(meta);
	}
	
	public void remove(MetaDesempenhoProdutividade meta) {
		entityManager.remove(meta);
	}
	
	public MetaDesempenhoProdutividade findByAnoAndSemestre(Integer ano, Integer semestre)  {
		String hql = "Select md From MetaDesempenhoProdutividade md Where md.ano = :ano and md.semestre = :semestre";
		try {
			return (MetaDesempenhoProdutividade) entityManager.createQuery(hql, MetaDesempenhoProdutividade.class)
					.setParameter("ano", ano)
					.setParameter("semestre", semestre)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public PaginatedList filtrar(MetaDesempenhoProdutividadeFiltro filtro, Pageable pageable) {
		Criteria criteria = createCriteria();
		
		paginationUtil.preparar(criteria, pageable);
		adicionarFiltro(criteria, filtro);
		
		if(pageable.getSort() == null)
			criteria.addOrder(Order.asc("ano"));
		
		return new PaginatedListAdapter(new PageImpl<>(criteria.list(), pageable, total(filtro)));
	}
	
	private void adicionarFiltro(Criteria criteria, MetaDesempenhoProdutividadeFiltro filtro) {
		if (filtro.getAno() != null && filtro.getAno() > 0)
			criteria.add(Restrictions.eq("ano", filtro.getAno()));
		
		if (filtro.getSemestre() != null && filtro.getSemestre() > 0)
			criteria.add(Restrictions.eq("semestre", filtro.getSemestre()));
	}
	
	private Long total(MetaDesempenhoProdutividadeFiltro filtro) {
		Criteria criteria = createCriteria();
		adicionarFiltro(criteria, filtro);
		criteria.setProjection(Projections.rowCount());
		
		return (Long) criteria.uniqueResult();
	}
	
	private Criteria createCriteria() {
		Session sess = (Session) entityManager.getDelegate();
		return sess.createCriteria(MetaDesempenhoProdutividade.class);
	}
}
