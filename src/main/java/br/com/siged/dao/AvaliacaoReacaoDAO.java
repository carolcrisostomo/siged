package br.com.siged.dao;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
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
import br.com.siged.entidades.AvaliacaoReacao;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Generica;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Participante;
import br.com.siged.filtro.AvaliacaoFiltro;

@Repository("avaliacaoreacaoDao")
public class AvaliacaoReacaoDAO {
	
	protected EntityManager entityManager;
	
	@Autowired
	private PaginationUtil paginationUtil;
	
	public AvaliacaoReacaoDAO(){
		
	}
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager){
		this.entityManager = entityManager;
	}
	
	public AvaliacaoReacao find(Long id){
		return this.entityManager.find(AvaliacaoReacao.class, id);
	}
	
	@Transactional
	public void persist(AvaliacaoReacao avaliacaoReacao){
		this.entityManager.persist(avaliacaoReacao);
	}
	
	@Transactional
	public void merge(AvaliacaoReacao avaliacaoReacao){
		this.entityManager.merge(avaliacaoReacao);
	}
	
	@Transactional
	public void remove(AvaliacaoReacao avaliacaoReacao){
		this.entityManager.remove(avaliacaoReacao);
	}
	
	@SuppressWarnings("unchecked")
	public List<AvaliacaoReacao> findAll(){
		String hql = "SELECT ar FROM AvaliacaoReacao ar ORDER BY ar.modulo.eventoId.dataInicioPrevisto DESC";
		return this.entityManager.createQuery(hql).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public PaginatedList filtrar(AvaliacaoFiltro filtro, Pageable pageable) {
		Criteria criteria = createCriteria();
		
		paginationUtil.preparar(criteria, pageable);
		adicionarFiltro(criteria, filtro);
		
		if(pageable.getSort() == null)
			criteria.addOrder(Order.desc("e.dataInicioPrevisto"));
		
		List<AvaliacaoReacao> avaliacoes = criteria.list();
		Participante participanteNull = new Participante();
		participanteNull.setNome("NÃO IDENTIFICADO");
		for(AvaliacaoReacao avaliacao : avaliacoes) {
			if(avaliacao.getParticipante() == null)
				avaliacao.setParticipante(participanteNull);
		}
		
		return new PaginatedListAdapter(new PageImpl<>(criteria.list(), pageable, total(filtro)));
	}
	
	private void adicionarFiltro(Criteria criteria, AvaliacaoFiltro filtro) {
		
		criteria.createCriteria("modulo", "m", Criteria.INNER_JOIN);
		criteria.createCriteria("participante", "p", Criteria.LEFT_JOIN);
		criteria.createCriteria("m.eventoId", "e", Criteria.INNER_JOIN);
		
		SimpleDateFormat padrao = new SimpleDateFormat("dd/MM/yyyy");
		
		String inicio = "";
		String fim = "";
		
		if (filtro.getData_cadastro1() != null) 
			inicio = padrao.format(filtro.getData_cadastro1()) + " 00:00:00";
		if (filtro.getData_cadastro2() != null)
			fim = padrao.format(filtro.getData_cadastro2()) + " 23:59:59";
		
		Date dataInicio = new Date() ;
		Date dataFim = new Date() ;
		try {  
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");  
            
            if (filtro.getData_cadastro1() != null)
            	dataInicio = (java.util.Date)formato.parse(inicio);  
            if (filtro.getData_cadastro2() != null)
            	dataFim = (java.util.Date)formato.parse(fim);
            
        } catch (Exception e) {              
        	e.printStackTrace();  
        }
		
		if(filtro.getEvento() != null && filtro.getEvento() > 0){
			criteria.add(Restrictions.eq("e.id", filtro.getEvento()));
		}
		if(filtro.getParticipante() != null && filtro.getParticipante() > 0){
			criteria.add(Restrictions.eq("p.id", filtro.getParticipante()));
		}
		if(filtro.getModulo() != null && filtro.getModulo() > 0){
			criteria.add(Restrictions.eq("m.id", filtro.getModulo()));
		}
		if (filtro.getData_cadastro1() != null) {
			criteria.add(Restrictions.ge("dataCadastro", dataInicio));
		}
		if (filtro.getData_cadastro2()!= null) {
			criteria.add(Restrictions.le("dataCadastro", dataFim));
		}
		
		if (filtro.getResultado() != null && filtro.getResultado() > 0){
			String condition = "";
			
			if(filtro.getResultado() == 1)
				condition = "{alias}.id in";
			else
				condition = "{alias}.id not in";
			
			String sql = condition + " (select satisfatoria_id from " + 
					"(select a1.id as satisfatoria_id, count(arn1.nota_id) as total_satisfatorias from avaliacao_reacao a1 " + 
					"inner join avaliacao_reacao_nota arn1 on arn1.avaliacao_id = a1.id " + 
					"where arn1.nota_id in (1,2) " + 
					"group by a1.id) dc1 " + 
					"inner join " + 
					"(select a2.id as respondidas_id, count(arn2.nota_id) as total_respondidas from avaliacao_reacao a2 " + 
					"inner join avaliacao_reacao_nota arn2 on arn2.avaliacao_id = a2.id " + 
					"group by a2.id) dc2 on dc2.respondidas_id = dc1.satisfatoria_id " + 
					"where total_respondidas > 0 and ((total_satisfatorias / total_respondidas) * 100) >= 70)";
			
			criteria.add(Restrictions.sqlRestriction(sql));
		} 
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	}
	
	private Long total(AvaliacaoFiltro filtro) {
		Criteria criteria = createCriteria();
		adicionarFiltro(criteria, filtro);
		criteria.setProjection(Projections.rowCount());
		
		return (Long) criteria.uniqueResult();
	}
	
	private Criteria createCriteria() {
		Session sess = (Session) entityManager.getDelegate();
		return sess.createCriteria(AvaliacaoReacao.class);
	}
	
	public List<AvaliacaoReacao> findByEventoAndParticipante(Evento evento, Participante participante) {
		String hql = "SELECT a FROM AvaliacaoReacao a WHERE a.modulo.eventoId = :evento and a.participante = :participante";
		return entityManager.createQuery(hql, AvaliacaoReacao.class).setParameter("evento", evento).setParameter("participante", participante).getResultList();
	}
	
	public List<AvaliacaoReacao> findByModuloAndParticipante(Modulo modulo, Participante participante) {
		String hql = "SELECT a FROM AvaliacaoReacao a WHERE a.modulo = :modulo and a.participante = :participante";
		return entityManager.createQuery(hql, AvaliacaoReacao.class).setParameter("modulo", modulo).setParameter("participante", participante).getResultList();
	}
	
	public AvaliacaoReacao findById(Long id){
		try{
			String hql = "SELECT ar FROM AvaliacaoReacao ar WHERE ar.id = :id";
			return (AvaliacaoReacao) this.entityManager
					.createQuery(hql)
					.setParameter("id", id)
					.getSingleResult();
		}catch(Exception e){
			return null;
		}
		
	}
	
	public List<AvaliacaoReacao> findByModulo(Modulo modulo) {
		String hql = "SELECT a FROM AvaliacaoReacao a WHERE a.modulo = :modulo";
		return entityManager.createQuery(hql, AvaliacaoReacao.class).setParameter("modulo", modulo).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<AvaliacaoReacao> findByEventoId(Long eventoId){
		String hql = "SELECT ar FROM AvaliacaoReacao ar WHERE ar.modulo.eventoId.id = :eventoid";
		return this.entityManager.createQuery(hql).setParameter("eventoid", eventoId).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<AvaliacaoReacao> findByEventoIdComComentarios(Long eventoId){
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria avaliacaoReacaoCriteria = sess.createCriteria(AvaliacaoReacao.class);
		Criteria moduloCriteria = avaliacaoReacaoCriteria.createCriteria("modulo");
		
		if(eventoId != null && eventoId > 0){
			moduloCriteria.add(Restrictions.eq("eventoId.id", eventoId));
		}
		
		avaliacaoReacaoCriteria
				.add(Restrictions.isNotNull("observacao"));
		
		avaliacaoReacaoCriteria.addOrder(Order.desc("dataCadastro"));
		
		return avaliacaoReacaoCriteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<AvaliacaoReacao> findByModuloIdComComentarios(Long moduloId){
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria avaliacaoReacaoCriteria = sess.createCriteria(AvaliacaoReacao.class);
		Criteria moduloCriteria = avaliacaoReacaoCriteria.createCriteria("modulo");
		
		if(moduloId != null && moduloId > 0){
			moduloCriteria.add(Restrictions.eq("id", moduloId));
		}
		
		avaliacaoReacaoCriteria.add(Restrictions.isNotNull("observacao"));
		
		avaliacaoReacaoCriteria.addOrder(Order.desc("dataCadastro"));
		
		return avaliacaoReacaoCriteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<AvaliacaoReacao> findByParticipanteId(Long participanteId){
		String hql = "SELECT ar FROM AvalliacaoReacao ar WHERE ar.participante.id = :participanteid";
		return this.entityManager.createQuery(hql).setParameter("participanteid", participanteId).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<AvaliacaoReacao> findByParticipanteCpf(String cpf){
		try{
			String hql = "SELECT a FROM AvaliacaoReacao a INNER JOIN a.participante p WHERE p.cpf = :cpf";
			return  this.entityManager
					.createQuery(hql)
					.setParameter("cpf", cpf)
					.getResultList();
		}catch (Exception e){
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Generica> findNaoRealizadasByParticipanteCpf(String cpf) {
		String periodoAvaliacaoEvento = "(SYSDATE > evento.data_fim_realizacao + 1 AND SYSDATE <= evento.data_fim_realizacao + 31)";
		String periodoAvaliacaoModulo = "(SYSDATE > modulo.data_fim + 1 AND SYSDATE <= modulo.data_fim + 31)";

		return entityManager.createNativeQuery(
				"(SELECT evento.id as evento_id, tipo_evento.descricao || ' ' || evento.titulo || ' (' || evento.data_inicio_realizacao || ' a ' || evento.data_fim_realizacao || ')' as evento, modulo.id as modulo_id, modulo.titulo as modulo, participante.id as participante_id, participante.nome as participante " +
				"FROM participante " +
				"INNER JOIN inscricao ON (inscricao.participante_id = participante.id) " +
				"INNER JOIN evento ON (evento.id = inscricao.evento_id) " +
				"INNER JOIN modulo ON (evento.id = modulo.evento_id) " +
				"INNER JOIN tipo_evento ON (evento.tipo_id = tipo_evento.id) " +
				"WHERE "
				+ "inscricao.confirmada = 'S' "
				+ "AND (" + periodoAvaliacaoEvento + " OR " + periodoAvaliacaoModulo + ")"
				+ "AND participante.cpf = '" + cpf + "' " +
				")" +
				"MINUS " +
				"(SELECT evento.id as evento_id,tipo_evento.descricao || ' '|| evento.titulo || ' (' || evento.data_inicio_realizacao || ' a ' || evento.data_fim_realizacao || ')' as evento, modulo.id as modulo_id, modulo.titulo as modulo, participante.id as participante_id, participante.nome as participante " + 
				"FROM avaliacao_reacao " +
				"INNER JOIN modulo ON (modulo.id = avaliacao_reacao.modulo_id)" + 
		        "INNER JOIN evento ON (evento.id = modulo.evento_id)" + 
		        "INNER JOIN tipo_evento ON (evento.tipo_id = tipo_evento.id)" + 
				"INNER JOIN participante ON (participante.id = avaliacao_reacao.participante_id)" + 
				"WHERE participante.cpf = '"+ cpf +"')").getResultList();
	}
	
	/*
	@SuppressWarnings("unchecked")
	public Collection<Generica> findNaoRealizadasByParticipanteCpfAndEvento(String cpf, String evento) {
		return entityManager.createNativeQuery(
				"(SELECT evento.id as evento_id, evento.titulo as evento, modulo.id as modulo_id, modulo.titulo as modulo, instrutor.id as instrutor_id, instrutor.nome as instrutor, participante.id as participante_id, participante.nome as participante " + 
				"FROM participante " +
				"INNER JOIN inscricao ON (inscricao.participante_id = participante.id) " +
				"INNER JOIN evento ON (evento.id = inscricao.evento_id) " +
				"INNER JOIN modulo ON (evento.id = modulo.evento_id) " +
				"INNER JOIN modulo_instrutor ON (modulo_instrutor.modulo_id = modulo.id) " +
				"INNER JOIN instrutor ON (instrutor.id = modulo_instrutor.instrutor_id) " +
				"WHERE inscricao.confirmada = 'S' and participante.cpf = '"+ cpf +"' and evento.id = "+ evento + ") " +
				"MINUS " +
				"(SELECT evento.id as evento_id, evento.titulo as evento, modulo.id as modulo_id, modulo.titulo as modulo, instrutor.id as instrutor_id, instrutor.nome as instrutor, participante.id as participante_id, participante.nome as participante " + 
				"FROM avaliacao_reacao " +
				"INNER JOIN modulo ON (modulo.id = avaliacao_reacao.modulo_id)" + 
		        "INNER JOIN evento ON (evento.id = modulo.evento_id)" + 
				"INNER JOIN modulo_instrutor ON (modulo_instrutor.modulo_id = modulo.id)" +
				"INNER JOIN instrutor ON (instrutor.id = modulo_instrutor.instrutor_id)" +
				"INNER JOIN participante ON (participante.id = avaliacao_reacao.participante_id)" +
				"WHERE participante.cpf = '"+ cpf +"' and evento.id = "+ evento + ")").getResultList();
	}*/
	
	@SuppressWarnings("unchecked")
	public List<AvaliacaoReacao> findAvaliacaoReacaoByCriteria(Long eventoId, Long moduloId, Long participanteId, Date data_cadastro1, Date data_cadastro2){
		
		Session sess = (Session) entityManager.getDelegate();
		
		SimpleDateFormat padrao = new SimpleDateFormat("dd/MM/yyyy");
		
		String inicio = "";
		String fim = "";
		
		if (data_cadastro1 != null) 
			inicio = padrao.format(data_cadastro1) + " 00:00:00";
		if (data_cadastro2 != null)
			fim = padrao.format(data_cadastro2) + " 23:59:59";
		
		Date dataInicio = new Date() ;
		Date dataFim = new Date() ;
		try {  
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");  
            
            if (data_cadastro1 != null)
            	dataInicio = (java.util.Date)formato.parse(inicio);  
            if (data_cadastro2 != null)
            	dataFim = (java.util.Date)formato.parse(fim);
            
        } catch (Exception e) {              
        	e.printStackTrace();  
        }
		
		Criteria avaliacaoReacao = sess.createCriteria(AvaliacaoReacao.class);
		Criteria modulo = avaliacaoReacao.createCriteria("modulo");
		Criteria evento = modulo.createCriteria("eventoId");
		
		if(eventoId != null && eventoId > 0){
			modulo.add(Restrictions.eq("eventoId.id", eventoId));
		}
		if(participanteId != null && participanteId > 0){
			avaliacaoReacao.add(Restrictions.eq("participante.id", participanteId));
		}
		if(moduloId != null && moduloId > 0){
			modulo.add(Restrictions.eq("id", moduloId));
		}
		if (data_cadastro1 != null) {
			avaliacaoReacao.add(Restrictions.ge("dataCadastro", dataInicio));
		}
		if (data_cadastro2 != null) {
			avaliacaoReacao.add(Restrictions.le("dataCadastro", dataFim));
		}
		
		evento.addOrder(Order.desc("dataInicioPrevisto"));
		
		return avaliacaoReacao.list();
	}
	
}
