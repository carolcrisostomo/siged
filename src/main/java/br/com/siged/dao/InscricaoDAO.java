package br.com.siged.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Inscricao;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.StatusDesempenho;
import br.com.siged.filtro.InscricaoFiltro;
import br.com.siged.filtro.RelatorioFiltro;
import br.com.siged.service.DesempenhoService;
import br.com.siged.util.StringUtil;
import br.com.siged.util.Util;

/**
 * 
 * @author Rodrigo
 */
@Repository("inscricaoDAO")
public class InscricaoDAO {
	protected EntityManager entityManager;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private UsuarioSCADAO usuarioDao;
	@Autowired
	private Util util;
	
	@Autowired
	private PaginationUtil paginationUtil;
	
	@Autowired
	private DesempenhoService desempenhoService;
	
	public InscricaoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Inscricao find(Long id) {
		return entityManager.find(Inscricao.class, id);
	}

	@Transactional
	public void persist(Inscricao Inscricao) {
		entityManager.persist(Inscricao);
	}

	@Transactional
	public void merge(Inscricao Inscricao) {
		entityManager.merge(Inscricao);
	}

	@Transactional
	public void remove(Inscricao Inscricao) {
		entityManager.remove(Inscricao);
	}

	@SuppressWarnings("unchecked")
	public List<Inscricao> findAll() {
		return entityManager.createNamedQuery("Inscricao.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public PaginatedList filtrar(InscricaoFiltro filtro, Pageable pageable) {
		Criteria criteria = createCriteria();
		criteria.createCriteria("eventoId", "e", Criteria.INNER_JOIN);
		criteria.createCriteria("participanteId", "p", Criteria.INNER_JOIN);
		criteria.createCriteria("p.tipoId", "t", Criteria.INNER_JOIN);
		
		paginationUtil.preparar(criteria, pageable);
		adicionarFiltro(criteria, filtro);
		
		if(pageable.getSort() == null)
			criteria.addOrder(Order.desc("data"));
		
		List<Inscricao> inscricoes = criteria.list();
		for(Inscricao inscricao : inscricoes) {
			if(!inscricao.getParticipanteId().getTipoId().isServidor())
				inscricao.setIndicada("-");
		}
		
		return new PaginatedListAdapter(new PageImpl<>(inscricoes, pageable, total(filtro)));
	}
	
	public PaginatedList filtrar(InscricaoFiltro filtro, Pageable pageable, boolean exportable) {
		Criteria criteria = createCriteria();
		criteria.createCriteria("eventoId", "e", Criteria.INNER_JOIN);
		criteria.createCriteria("participanteId", "p", Criteria.INNER_JOIN);
		criteria.createCriteria("p.tipoId", "t", Criteria.INNER_JOIN);
		
		if(!exportable) {			
			paginationUtil.preparar(criteria, pageable);
		}
		adicionarFiltro(criteria, filtro);
		
		if(pageable.getSort() == null)
			criteria.addOrder(Order.desc("data"));
		List<Inscricao> inscricoes = criteria.list();
		
		for(Inscricao inscricao : inscricoes) {
			if(!inscricao.getParticipanteId().getTipoId().isServidor())
				inscricao.setIndicada("-");
		}
		
		return new PaginatedListAdapter(new PageImpl<>(inscricoes, pageable, total(filtro)));
	}
	
	private void adicionarFiltro(Criteria criteria, InscricaoFiltro filtro) {
		if (filtro.getEvento() != null && filtro.getEvento() != 0)
			criteria.add(Restrictions.eq("e.id", filtro.getEvento()));
		
		if (filtro.getData1() != null)
			criteria.add(Restrictions.ge("data", filtro.getData1()));
		
		if (filtro.getData2() != null)
			criteria.add(Restrictions.lt("data", util.getTomorrowDate(filtro.getData2())));
		
		if (filtro.getIndicada() != null && !filtro.getIndicada().isEmpty())
			criteria.add(Restrictions.ilike("indicada", filtro.getIndicada()));
		
		if (filtro.getConfirmada() != null && !filtro.getConfirmada().isEmpty())
			criteria.add(Restrictions.ilike("confirmada", filtro.getConfirmada()));
			
		if (filtro.getParticipanteId() != null && filtro.getParticipanteId() != 0)
			criteria.add(Restrictions.eq("p.id", filtro.getParticipanteId()));
			
		if (filtro.getTipoParticipanteId() != null && filtro.getTipoParticipanteId()!= 0)
			criteria.add(Restrictions.eq("t.id", filtro.getTipoParticipanteId()));
	}
	
	private Long total(InscricaoFiltro filtro) {
		Criteria criteria = createCriteria();
		criteria.createCriteria("eventoId", "e", Criteria.INNER_JOIN);
		criteria.createCriteria("participanteId", "p", Criteria.INNER_JOIN);
		criteria.createCriteria("p.tipoId", "t", Criteria.INNER_JOIN);
		
		adicionarFiltro(criteria, filtro);
		criteria.setProjection(Projections.rowCount());
		
		return (Long) criteria.uniqueResult();
	}
	
	private Criteria createCriteria() {
		Session sess = (Session) entityManager.getDelegate();
		return sess.createCriteria(Inscricao.class);
	}
	
	public Inscricao findById(Long codigo) {
		try {
			return (Inscricao) entityManager.createNamedQuery("Inscricao.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	public Inscricao findByEventoAndParticipante(Long codigoEvento, Long codigoParticipante) {
		try {  
			return (Inscricao) entityManager.createNamedQuery("Inscricao.findByEventoAndParticipante").setParameter("evento", eventoDao.find(codigoEvento)).setParameter("participante", participanteDao.find(codigoParticipante)).getSingleResult();
	    } catch ( NoResultException nre ) {  
	        return null;  
	    }
	}
	
	@SuppressWarnings("unchecked")
	public List<Inscricao> findByEventoAndConfirmada(Long codigoEvento, String confirmada) {
		return entityManager.createNamedQuery("Inscricao.findByEventoAndConfirmada").setParameter("evento", eventoDao.find(codigoEvento)).setParameter("confirmada", confirmada).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Inscricao> findByEventoAndConfirmadaOrdenadaPorParticipante(Long codigoEvento, String confirmada) {
		return entityManager.createNamedQuery("Inscricao.findByEventoAndConfirmadaOrdenadaPorParticipante").setParameter("evento", eventoDao.find(codigoEvento)).setParameter("confirmada", confirmada).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Inscricao> findByParticipanteCpf(String cpf) {
		return entityManager.createNamedQuery("Inscricao.findByParticipanteCpf").setParameter("cpf", cpf).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Inscricao> findByChefe(String chefe) {
		return entityManager.createNamedQuery("Inscricao.findByChefe").setParameter("chefe", usuarioDao.findByLogin(chefe)).getResultList();
	}
	
	public Inscricao findBySolicitacaoId(Long solicitacaoId) {
		try {
			return (Inscricao) entityManager.createNamedQuery("Inscricao.findBySolicitacaoId").setParameter("solicitacaoId", solicitacaoId).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Inscricao> findByChefeAndIndicadaAndConfirmada(Long chefeId, String indicada, String confirmada) {
		return entityManager.createNamedQuery("Inscricao.findByChefeAndIndicadaAndConfirmada")
				.setParameter("chefe", chefeId)
				.setParameter("indicada", indicada)
				.setParameter("confirmada", confirmada).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Inscricao> findInscricaoByCriteria(Long eventoId, Date data1, Date data2, String indicada, String confirmada, Long participanteId, Long tipoParticipanteId) {
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria aux = sess.createCriteria(Inscricao.class);
		if (eventoId != 0) {
			aux.add(Restrictions.eq("eventoId", eventoDao.findById(eventoId)));
		}
		if (data1 != null) {
			aux.add(Restrictions.ge("data", data1));
		}
		if (data2 != null) {
			aux.add(Restrictions.lt("data", util.getTomorrowDate(data2)));
		}
		if (indicada != "") {
			aux.add(Restrictions.ilike("indicada", indicada));
		}
		if (confirmada != "") {
			aux.add(Restrictions.ilike("confirmada", confirmada));
		}		
		if (participanteId != 0 ) {
			aux.add(Restrictions.eq("participanteId", participanteDao.findById(participanteId)));
		}
		if (tipoParticipanteId != 0 ) {			
			aux.createCriteria("participanteId", "p").add(Restrictions.eq("p.tipoId.id", tipoParticipanteId));
		}
	    return aux.list();
	    
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Inscricao> findMinhasInscricaoByCriteria(Long eventoId, Date data1, Date data2, String indicada, String confirmada, Participante participante) {
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria aux = sess.createCriteria(Inscricao.class);
		
		aux.add(Restrictions.eq("participanteId", participante));
		
		if (eventoId != null && eventoId != 0) {
			aux.add(Restrictions.eq("eventoId", eventoDao.findById(eventoId)));
		}
		if (data1 != null) {
			aux.add(Restrictions.ge("data", data1));
		}
		if (data2 != null) {
			aux.add(Restrictions.lt("data", util.getTomorrowDate(data2)));
		}
		if (indicada != null && indicada != "") {
			aux.add(Restrictions.ilike("indicada", indicada));
		}
		if (confirmada != null && confirmada != "") {
			aux.add(Restrictions.ilike("confirmada", confirmada));
		}
		
	    return aux.list();
	    
	}	

	@SuppressWarnings("unchecked")
	public List<Long> findEventoIdByParticipanteId(Long participanteId) {
		return entityManager.createNamedQuery("Inscricao.findEventoIdByParticipanteId").setParameter("participanteId", participanteId).getResultList();
	}	
	
	@SuppressWarnings("unchecked")
	public List<Evento> findEventoByConfirmacaoPendente() {
		return  entityManager.createNativeQuery(
				"SELECT * FROM evento e WHERE e.id IN ("+
				"SELECT DISTINCT i.evento_id FROM inscricao i WHERE i.confirmada = '-') "+
				"ORDER BY e.data_inicio_previsto DESC", Evento.class).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Inscricao> findToRelatorioParticipantesExternos(RelatorioFiltro filtro) {
		Criteria criteria = createCriteria();
		Date hoje = util.getDataFormatada(new Date());
		
		criteria.createCriteria("participanteId", "p", Criteria.INNER_JOIN);
		criteria.createCriteria("eventoId", "e", Criteria.INNER_JOIN);
		criteria.createCriteria("e.tipoId", "tp", Criteria.INNER_JOIN);
		
		criteria.add(Restrictions.eq("confirmada", "S"));
		criteria.add(Restrictions.lt("e.dataFimRealizacao", hoje));
		
		if(filtro.getOrgaoId() != null && filtro.getOrgaoId() > 0)
			criteria.createCriteria("p.orgaoId", "o", Criteria.INNER_JOIN).add(Restrictions.eq("id", filtro.getOrgaoId()));
		
		if(filtro.getEventoId() != null && filtro.getEventoId() > 0)
			criteria.add(Restrictions.eq("e.id", filtro.getEventoId()));
		
		if(filtro.getDataInicio1() != null)
			criteria.add(Restrictions.ge("e.dataInicioRealizacao", filtro.getDataInicio1()));
		
		if(filtro.getDataInicio2() != null)
			criteria.add(Restrictions.le("e.dataFimRealizacao", filtro.getDataInicio2()));
		
		
		criteria.addOrder(Order.desc("e.dataInicioRealizacao")).addOrder(Order.asc("tp.descricao")).addOrder(Order.asc("e.titulo")).addOrder(Order.asc("p.nome"));
		return criteria.list();
	}
	
	/**
	 * Aplica os filtros setados no <tt>InscricaoFiltro</tt>. 
	 * <p/>
	 *  
	 * @param filtro
	 * @return Lista de inscrições de acordo com o filtro 
	 * 
	 * @see #getCriteria(InscricaoFiltro)
	 * @since 27/09/2018
	 * @author Rafael de Castro
	 */
	@SuppressWarnings("unchecked")
	public List<Inscricao> findByFiltro(InscricaoFiltro filtro) {
		List<Inscricao> inscricoes = getCriteria(filtro).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		if(filtro.getAprovado() != null && !filtro.getAprovado().isEmpty() ) {
			StatusDesempenho statusFiltro = StatusDesempenho.fromAprovado(filtro.getAprovado());
			List<Inscricao> inscricoesPorStatusDesempenho = new ArrayList<>();
			for(Inscricao inscricao : inscricoes) {
				StatusDesempenho status = desempenhoService.statusDesempenhoParticipanteNoEvento(inscricao.getParticipanteId(), inscricao.getEventoId());
				if(statusFiltro.equals(status)) {
					inscricoesPorStatusDesempenho.add(inscricao);
				}
			}
			
			return inscricoesPorStatusDesempenho;
		}
		
		return inscricoes;
	}
	
	/**
	 * Desde a versão 5.2 do Hibernate, a Hibernate Criteria API está depreciada e o novo desenvolvimento está focado na JPA Criteria API. 
	 * Versão usada no Projeto é a 3.5.1-Final.
	 * 
	 * @since 05/10/2018
	 * @author Rafael de Castro
	 */
	private Criteria getCriteria(InscricaoFiltro filtro) {
		
		Criteria criteria = createCriteria();
		Criteria participanteCriteria = criteria.createCriteria("participanteId", "p", Criteria.INNER_JOIN);
		
		if (filtro.getEvento() != null && filtro.getEvento() != 0)
			criteria.add(Restrictions.eq("eventoId", eventoDao.findById(filtro.getEvento())));

		if (filtro.getData1() != null)
			criteria.add(Restrictions.ge("data", filtro.getData1()));

		if (filtro.getData2() != null)
			criteria.add(Restrictions.lt("data", util.getTomorrowDate(filtro.getData2())));

		if (filtro.getIndicada() != null && !filtro.getIndicada().isEmpty())
			criteria.add(Restrictions.ilike("indicada", filtro.getIndicada()));
		
		if (filtro.getConfirmada() != null && !filtro.getConfirmada().isEmpty())
			criteria.add(Restrictions.ilike("confirmada", filtro.getConfirmada()));
			
		if (filtro.getParticipanteId() != null && filtro.getParticipanteId() != 0)
			criteria.add(Restrictions.eq("participanteId", participanteDao.findById(filtro.getParticipanteId())));
			
		if (filtro.getTipoParticipanteId() != null && filtro.getTipoParticipanteId() != 0) {
			participanteCriteria.add(Restrictions.eq("p.tipoId.id", filtro.getTipoParticipanteId()));
		} else if(filtro.getNomeParticipante() != null && !filtro.getNomeParticipante().isEmpty()) {
			participanteCriteria.add(Restrictions.sqlRestriction(StringUtil.sqlTranslateQuery("{alias}.nome", filtro.getNomeParticipante())));
		}
		
	    return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	}

}