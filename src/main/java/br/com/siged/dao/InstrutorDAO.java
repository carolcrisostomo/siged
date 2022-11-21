package br.com.siged.dao;

import java.util.Collection;
import java.util.List;

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
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.dao.pagination.PaginatedListAdapter;
import br.com.siged.dao.pagination.PaginationUtil;
import br.com.siged.entidades.Instrutor;
import br.com.siged.filtro.InstrutorFiltro;
import br.com.siged.util.StringUtil;
/**
 *
 * @author Rodrigo
 */
@Repository("instrutorDao")
public class InstrutorDAO {
	protected EntityManager entityManager;
	
	@Autowired
	private SituacaoInstrutorDAO situacaoInstrutorDao;
	
	@Autowired
	private TipoInstrutorDAO tipoInstrutorDao;
	
	@Autowired
	private PaginationUtil paginationUtil;
	
	public InstrutorDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Instrutor find(Long id) {
		return entityManager.find(Instrutor.class, id);
	}

	@Transactional
	public void persist(Instrutor Instrutor) {
		entityManager.persist(Instrutor);
	}

	@Transactional
	public void merge(Instrutor Instrutor) {
		entityManager.merge(Instrutor);
	}

	@Transactional
	public void remove(Instrutor Instrutor) {
		entityManager.remove(Instrutor);
	}

	@SuppressWarnings("unchecked")
	public List<Instrutor> findAll() {
		return entityManager.createNamedQuery("Instrutor.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public PaginatedList filtrar(InstrutorFiltro filtro, Pageable pageable) {
		Criteria criteria = createCriteria();
		
		paginationUtil.preparar(criteria, pageable);
		adicionarFiltro(criteria, filtro);
		
		if(pageable.getSort() == null)
			criteria.addOrder(Order.asc("nome"));
		
		return new PaginatedListAdapter(new PageImpl<>(criteria.list(), pageable, total(filtro)));
	}
	
	private void adicionarFiltro(Criteria criteria, InstrutorFiltro filtro) {
		if (filtro.getNome() != null && !filtro.getNome().isEmpty())
			criteria.add(Restrictions.sqlRestriction(StringUtil.sqlTranslateQuery("{alias}.nome", filtro.getNome())));
		
		if (filtro.getSituacao() != null && filtro.getSituacao() != 0)
			criteria.add(Restrictions.eq("situacaoId", situacaoInstrutorDao.findById(filtro.getSituacao())));
		
		if (filtro.getTipo() != null && filtro.getTipo() != 0)
			criteria.add(Restrictions.eq("tipoId", tipoInstrutorDao.findById(filtro.getTipo())));
	}
	
	private Long total(InstrutorFiltro filtro) {
		Criteria criteria = createCriteria();
		adicionarFiltro(criteria, filtro);
		criteria.setProjection(Projections.rowCount());
		
		return (Long) criteria.uniqueResult();
	}
	
	private Criteria createCriteria() {
		Session sess = (Session) entityManager.getDelegate();
		return sess.createCriteria(Instrutor.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Instrutor> findBySituacao() {
		return entityManager.createNamedQuery("Instrutor.findBySituacao").setParameter("situacao", situacaoInstrutorDao.findById(Long.parseLong("1"))).getResultList();
	}
	public Instrutor findById(Long codigo) {
		try {
			return (Instrutor) entityManager.createNamedQuery("Instrutor.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Instrutor> findInstrutorByCriteria(String nome, Long situacaoId, Long tipoId) {
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria aux = sess.createCriteria(Instrutor.class);
		if (nome != null && !nome.isEmpty()) {
			aux.add(Restrictions.sqlRestriction(StringUtil.sqlTranslateQuery("{alias}.nome", nome)));
			//aux.add(Restrictions.ilike("nome", "%" + nome + "%"));
		}
		
		if (situacaoId != 0) {
			aux.add(Restrictions.eq("situacaoId", situacaoInstrutorDao.findById(situacaoId)));
		}
		
		if (tipoId != 0) {
			aux.add(Restrictions.eq("tipoId", tipoInstrutorDao.findById(tipoId)));
		}
	    return aux.list();
	    
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Instrutor> findInstrutoresByEventoIdAndTipoId(Long eventoId, Long tipoId) {
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria instrutor = sess.createCriteria(Instrutor.class, "i");
		Criteria modulo = instrutor.createCriteria("moduloList", "m", Criteria.INNER_JOIN);
		
		if(eventoId != null && eventoId > 0){
			modulo.add(Restrictions.eq("m.eventoId.id", eventoId));
		}
		if (tipoId != null && tipoId > 0) {
			instrutor.add(Restrictions.eq("tipoId.id", tipoId));
		}
		
		return instrutor.addOrder(Order.asc("nome")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	    
	}
	
	@SuppressWarnings("unchecked")
	public List<Instrutor> findByModulo(Long moduloId) {
		Criteria criteria = createCriteria();
		criteria.createCriteria("moduloList", "m", Criteria.INNER_JOIN);
		
		if(moduloId != null && moduloId > 0) {
			criteria.add(Restrictions.eq("m.id", moduloId));
		}
		
		return criteria.addOrder(Order.asc("nome")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	
	public Instrutor findByCpf(String cpf) {
		try {
			return (Instrutor) entityManager.createNamedQuery("Instrutor.findByCpf").setParameter("cpf", cpf).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	public Instrutor findByEmail(String email) {
		try {
			return (Instrutor) entityManager.createNamedQuery("Instrutor.findByEmail").setParameter("email", email).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Instrutor> findInstrutoresByEventoId(Long eventoId) {
		return entityManager
				.createNativeQuery(
						"SELECT DISTINCT i.id, i.nome, i.cpf " +
						"FROM evento e INNER JOIN modulo m ON e.id = m.evento_id " +
						"INNER JOIN modulo_instrutor mi ON mi.modulo_id = m.id " +
						"INNER JOIN instrutor i ON i.id = mi.instrutor_id " +
						"WHERE e.id =  " + eventoId +
						"ORDER BY i.nome",
						Instrutor.class).getResultList();
	}
	
}