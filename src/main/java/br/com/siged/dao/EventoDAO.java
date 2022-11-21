package br.com.siged.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.displaytag.pagination.PaginatedList;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import br.com.siged.controller.EventoController;
import br.com.siged.dao.pagination.PaginatedListAdapter;
import br.com.siged.dao.pagination.PaginationUtil;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.EventoProvedorJoin;
import br.com.siged.entidades.Generica;
import br.com.siged.entidades.Modalidade;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.ProvedorEvento;
import br.com.siged.entidades.StatusEvento;
import br.com.siged.entidades.Usuario;
import br.com.siged.filtro.EventoFiltro;
import br.com.siged.service.AuthenticationService;
import br.com.siged.service.EventoService;
import br.com.siged.util.StringUtil;
import br.com.siged.util.Util;

/**
 * 
 * @author Rodrigo
 */
@Repository("eventoDao")
public class EventoDAO {
	
	protected EntityManager entityManager;
	
	@Autowired
	private TipoEventoDAO tipoEventoDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private EventoService eventoService;
	@Autowired
	private PaginationUtil paginationUtil;
	@Autowired
	private Util util;

	public EventoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Evento find(Long id) {
		return entityManager.find(Evento.class, id);
	}

	@Transactional
	public void persist(Evento Evento) {
		entityManager.persist(Evento);
	}

	@Transactional
	public void merge(Evento Evento) {
		entityManager.merge(Evento);
	}

	@Transactional
	public void remove(Evento Evento) {
		entityManager.remove(Evento);
	}

	@SuppressWarnings("unchecked")
	public List<Evento> findAll() {
		return entityManager.createNamedQuery("Evento.findAll").getResultList();
	}
	
	/**
	 * Filtro para paginação lazy
	 * @param filtro
	 * @param pageable
	 * @return
	 */
	public PaginatedList filtrar(EventoFiltro filtro, Pageable pageable) {
		Criteria criteria = createCriteria();
		
		paginationUtil.preparar(criteria, pageable);
		if(pageable.getSort() == null)
			criteria.addOrder(Order.desc("dataInicioPrevisto"));
		adicionarFiltro(criteria, filtro);
		
		List<Evento> eventos = transformResult(criteria);
		if(filtro.getParticipanteCPF() != null && !filtro.getParticipanteCPF().isEmpty()) {
			if(filtro.getComoInstrutor() != 1) {
				Participante participante = participanteDao.findByCpf(filtro.getParticipanteCPF());
				eventoService.populateDesempenhoTransientInfo(eventos, participante);
			} else {
				eventoService.populateInstrutorTransientInfo(eventos);
			}
		} else {
			eventoService.populateDesempenhoTransientInfo(eventos);
		}
		
		return new PaginatedListAdapter(new PageImpl<>(eventos, pageable, total(filtro)));
	}
	
	
	public PaginatedList filtrar(EventoFiltro filtro, Pageable pageable, boolean exportable) {
		Criteria criteria = createCriteria();
		if(!exportable) {
			
			paginationUtil.preparar(criteria, pageable);
		}
		if(pageable.getSort() == null)
			criteria.addOrder(Order.desc("dataInicioPrevisto"));
		adicionarFiltro(criteria, filtro);
		
		List<Evento> eventos = transformResult(criteria);
		if(filtro.getParticipanteCPF() != null && !filtro.getParticipanteCPF().isEmpty()) {
			if(filtro.getComoInstrutor() != 1) {
				Participante participante = participanteDao.findByCpf(filtro.getParticipanteCPF());
				eventoService.populateDesempenhoTransientInfo(eventos, participante);
			} else {
				eventoService.populateInstrutorTransientInfo(eventos);
			}
		} else {
			eventoService.populateDesempenhoTransientInfo(eventos);
		}
		
		return new PaginatedListAdapter(new PageImpl<>(eventos, pageable, total(filtro)));
	}
	
	private void adicionarFiltro(Criteria criteria, EventoFiltro filtro) {
		if(filtro.getTipoPublicoAlvo() != null && filtro.getTipoPublicoAlvo() != 0)
			criteria.createCriteria("publicoAlvoList", "pa").add(Restrictions.eq("pa.id", filtro.getTipoPublicoAlvo()));
		
		if (filtro.getPrevisto1() != null)
			criteria.add(Restrictions.ge("dataInicioPrevisto", util.getDataFormatada(filtro.getPrevisto1())));
			
		if (filtro.getPrevisto2() != null)
			criteria.add(Restrictions.le("dataInicioPrevisto", util.getDataFormatada(filtro.getPrevisto2())));
		
		if (filtro.getRealizacao1() != null)
			criteria.add(Restrictions.ge("dataInicioRealizacao", util.getDataFormatada(filtro.getRealizacao1())));
		
		if (filtro.getRealizacao2() != null)
			criteria.add(Restrictions.le("dataInicioRealizacao", util.getDataFormatada(filtro.getRealizacao2())));
		
		if (filtro.getTipoEvento() != null && filtro.getTipoEvento() != 0)
			criteria.add(Restrictions.eq("tipoId", tipoEventoDao.findById(filtro.getTipoEvento())));
		
		if (filtro.getTitulo() != null && !filtro.getTitulo().isEmpty())
			criteria.add(Restrictions.sqlRestriction(StringUtil.sqlTranslateQuery("{alias}.titulo", filtro.getTitulo())));

		if (filtro.getProvedorId() != null && filtro.getProvedorId() != 0) {
			DetachedCriteria dc = DetachedCriteria.forClass(EventoProvedorJoin.class);
			dc.add(Restrictions.eq("id.provedor.id", filtro.getProvedorId()));
			dc.setProjection(Projections.property("id.evento"));
			
			criteria.add(Subqueries.propertyIn("id", dc));
		}
		
		if(filtro.getParticipanteCPF() != null && !filtro.getParticipanteCPF().isEmpty()) {
			if(filtro.getComoInstrutor() == 1) {
				criteria.createCriteria("moduloList", "m")
				.createCriteria("m.instrutorList", "i")
				.add(Restrictions.eq("i.cpf", filtro.getParticipanteCPF()));
			} else {
				criteria.createCriteria("inscricaoList", "i")
				.add(Restrictions.eq("i.confirmada", "S"))
				.createCriteria("i.participanteId", "p")
				.add(Restrictions.eq("p.cpf", filtro.getParticipanteCPF()));
			}
		}
		
		/**
		 * Por conta do filtro de instrutor, a paginação estava sendo feito de forma errada, pois o distinct do criteria estava sendo feito depois de pegar o resultado.
		 * Foi preciso adicionar uma projeção usando distinct, assim a paginação passa a ser feita depois da projeção retornar os resultados.
		 * 
		 * Dessa forma é necessário transformar o resultado para uma lista de Evento usando transformResult(Criteria criteria)
		 */
		criteria.setProjection(Projections
			.distinct(Projections
				.projectionList()
					.add(Projections.property("id"))
					.add(Projections.property("titulo"))
					.add(Projections.property("dataInicioPrevisto"))
					.add(Projections.property("dataFimPrevisto"))
					.add(Projections.property("dataInicioRealizacao"))
					.add(Projections.property("dataFimRealizacao"))));
	}
	
	private List<Evento> transformResult(Criteria criteria) {
		List<Evento> eventos = new ArrayList<>();
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = criteria.list();
		for (Object[] eventoMap : list) {
			long id = (Long) eventoMap[0];
			Evento evento = findById(id);
			if (evento != null) {
				eventos.add(evento);
			}
		}
		
		return eventos;
	}
	
	private Long total(EventoFiltro filtro) {
		Criteria criteria = createCriteria();
		adicionarFiltro(criteria, filtro);
		
		long total = 0;
		List<?> list = criteria.list();
		if (list != null) {
			total = list.size();
		}
		
		return total;
	}
	
	private Criteria createCriteria() {
		Session sess = (Session) entityManager.getDelegate();
		return sess.createCriteria(Evento.class);
	}

	@SuppressWarnings("unchecked")
	public List<Evento> findPresencial() {
		return entityManager.createNamedQuery("Evento.findPresencial").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Evento> findPresencialByNaoPrevistoComModulo() {
		return entityManager.createNamedQuery("Evento.findPresencialByNaoPrevistoComModulo").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Evento> findEventoEficaciaAvaliada() {
		return entityManager.createNamedQuery(
				"Evento.findEventoEficaciaAvaliada").getResultList();
	}

	public Evento findById(Long codigo) {
		try {
			return (Evento) entityManager.createNamedQuery("Evento.findById")
					.setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	public Evento findByIdLazy(Long eventoId) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Evento.class);
		criteria.createAlias("provedores", "p", CriteriaSpecification.LEFT_JOIN);
		if(eventoId != null) {
			criteria.add(Restrictions.eq("id", eventoId));
		}
		
		return (Evento) criteria.uniqueResult();
		
	}

	public Evento findByEventoApurado(Long id) {
		try {
			return (Evento) entityManager
					.createNamedQuery("Evento.findByEventoApurado")
					.setParameter("id", id).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	public Evento findByEventoApuradoParcial(Long id) {
		try {
			return (Evento) entityManager
					.createNamedQuery("Evento.findByEventoApuradoParcial")
					.setParameter("id", id).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Eventos que podem ser apurados
	 */
	public List<Evento> findRealizadoOrEmAndamentoComModuloRealizado() {
		String eventoRealizadoCondition = "e.dataFimRealizacao < trunc(current_date(),'DDD')";
		String eventoEmAndamentoCondition = "e.dataInicioRealizacao <= trunc(current_date(),'DDD') AND e.dataFimRealizacao >= trunc(current_date(),'DDD')";
		String eventoComModuloRealizadoCondition = "e.id in (SELECT m.eventoId FROM Modulo m WHERE trunc(SYSDATE, 'DDD') > m.dataFim)";
		
		String sql = "SELECT DISTINCT e FROM Evento e WHERE " + eventoRealizadoCondition +
				"OR (" + eventoEmAndamentoCondition + " AND "+ eventoComModuloRealizadoCondition + ")" +
				"ORDER BY e.dataFimRealizacao DESC";
		
		return entityManager.createQuery(sql, Evento.class).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Evento> findByNaoPrevistoComNota() {
		return entityManager
				.createNativeQuery(
						"SELECT EVENTO.*  FROM EVENTO WHERE ID in( "
								+ "SELECT distinct (EVENTO.id) "
								+ "FROM EVENTO INNER JOIN MODULO ON EVENTO.ID = MODULO.EVENTO_ID "
								+ "INNER JOIN TIPO_EVENTO ON EVENTO.TIPO_ID = TIPO_EVENTO.ID "
								+ "WHERE EVENTO.PUBLICADO = 'S' "
								+ "AND MODULO.NOTA is not null "
								+ "AND To_Date(To_Char(EVENTO.DATA_INICIO_REALIZACAO,'dd/mm/yyyy'),'dd/mm/yyyy') <=  SYSDATE) ORDER BY EVENTO.DATA_INICIO_PREVISTO DESC",
						Evento.class).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Evento> findByEventoAvaliado() {
		return entityManager
				.createNativeQuery(
						"SELECT DISTINCT "
								+ "evento.tipo_id, evento.titulo, evento.data_inicio_realizacao, evento.data_fim_realizacao,"
								+ "evento.id "
								+ "FROM avaliacao_reacao "
								+ "INNER JOIN modulo on modulo.id = avaliacao_reacao.modulo_id "
								+ "INNER JOIN evento on evento.id = modulo.evento_id "
								+ "ORDER BY evento.data_inicio_realizacao DESC",
						Evento.class).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Evento> findByEventosCujaEficaciaDeveSerAvaliada() {
		/*
		 * DONE: __Modalidade Evento: Definir qual será a Modalidade do Evento na consulta (SQL)
		 * @deprecated Consulta atualizada para verificar se o evento é presencial por meio dos módulos
		String sqlModalidadePresencial = "AND evento.modalidade_id = 1 ";
		 */
		String sqlModalidadePresencial = "AND evento.id IN (SELECT e.id from modulo m INNER JOIN evento e on m.evento_id = e.id WHERE m.modalidade_id = 1) ";
		return entityManager
				.createNativeQuery(
						"SELECT evento.id,evento.titulo "
								+ "FROM evento,evento_tipopublico "
								+ "WHERE evento.id = evento_tipopublico.evento_id "
								+ "AND evento.tipo_id = 1 AND evento_tipopublico.publico_alvo_id = 1 AND evento.carga_horaria >= 16 "
								+ sqlModalidadePresencial
								+ "AND MONTHS_BETWEEN(SYSDATE,DECODE(evento.data_fim_realizacao,NULL,evento.data_fim_previsto,evento.data_fim_realizacao)) >= 3 "
								+ "AND MONTHS_BETWEEN(SYSDATE,DECODE(evento.data_fim_realizacao,NULL,evento.data_fim_previsto,evento.data_fim_realizacao)) <= 6 "
								+ "ORDER BY evento.data_inicio_realizacao DESC",
						Evento.class).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Evento> findEventosApurados() {
		return entityManager.createNamedQuery("Evento.findEventosApurados")
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Evento> findEventosApuradosDoIPC() {
		String sql = "select e from Evento e"
				+ " where e.id in (select d.eventoId.id from Desempenho d)"
				+ " and :idIPC in (select p.id from e.provedores p)"
				+ " order by e.dataInicioPrevisto DESC";
		
		return entityManager.createQuery(sql)
				.setParameter("idIPC", ProvedorEvento.IPC)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Evento> findEventosAvaliacoes() {
		return entityManager.createNamedQuery("Evento.findEventosAvaliacoes")
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Evento> findEventosGastos() {
		return entityManager.createNamedQuery("Evento.findEventosGastos")
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Evento> findRealizadosByProvedorTerceiros() {
		@SuppressWarnings("unused")
		@Deprecated
		String sqlOld = "SELECT e FROM Evento e WHERE e.dataFimRealizacao < trunc(current_date(),'DDD') and e.provedorId.id <> 1 ORDER BY e.dataInicioPrevisto DESC";
		
		String sql = "select e from Evento e where e.dataFimRealizacao < trunc(current_date(), 'DDD')"
				+ " and :idIPC NOT IN (select p.id from e.provedores p)"
				+ " order by e.dataInicioPrevisto desc";
		
		Query query = entityManager.createQuery(sql);
		query.setParameter("idIPC", ProvedorEvento.IPC);
		
		return query.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Evento> findEventosApuradosDoIPCAndRedeEscolas() {
		@SuppressWarnings("unused")
		@Deprecated
		String sqlOld = "SELECT e FROM Evento e WHERE e.id in "
				+ "(SELECT d.eventoId.id FROM Desempenho d) "
				+ "AND (e.provedorId.id = :idIPC OR e.provedorId.id = :idRedeEscolas OR e.certificadoPersonalizado != null) "
				+ "AND e.permiteCertificado = 'S' "
				+ "ORDER BY e.dataInicioPrevisto DESC";
		
		String sql = "select e from Evento e"
				+ " JOIN e.provedores p"
				+ " where e.id in (select d.eventoId.id from Desempenho d where d.parcial = false)"
				+ " and ((p.id IN (:provedoresCertificado)) or e.certificadoPersonalizado != null)"
				+ " and e.permiteCertificado = 'S'"
				+ " order by e.dataInicioPrevisto desc";
		
		Query query = entityManager.createQuery(sql, Evento.class);
		query.setParameter("provedoresCertificado", ProvedorEvento.PROVEDORES_CERTIFICADO);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Evento> findEventosIPCAndRedeEscolasByParticipanteAprovado(Long participanteId) {
		@SuppressWarnings("unused")
		@Deprecated
		String sqlOld = "SELECT e.* "
				+ "FROM evento e "
				+ "INNER JOIN inscricao i ON i.evento_id = e.id "
				+ "INNER JOIN participante p ON i.participante_id = p.id "
				+ "WHERE e.id IN (SELECT DISTINCT d.evento_id FROM desempenho d WHERE d.participante_id = " + participanteId + ") "
				+ "AND e.id NOT IN (SELECT DISTINCT d.evento_id FROM desempenho d WHERE d.participante_id = " + participanteId + " AND d.aprovado = 'N') "
				+ "AND (e.provedor_id = " + ProvedorEvento.IPC + " OR e.provedor_id = " + ProvedorEvento.REDE_ESCOLAS + " OR e.certificado_personalizado IS NOT NULL) "
				+ "AND e.permite_certificado = 'S' "
				+ "AND p.id = " + participanteId + " "
				+ "ORDER BY e.data_inicio_previsto DESC";
		
		String sql = "SELECT e.* FROM evento e where e.id in "
				+ "(SELECT DISTINCT e.id "
				+ "FROM evento e "
				+ "INNER JOIN inscricao i ON i.evento_id = e.id "
				+ "INNER JOIN participante p ON i.participante_id = p.id "
				+ "INNER JOIN evento_provedor_join epj on e.id = epj.evento_id "
				+ "WHERE e.id IN (SELECT DISTINCT d.evento_id FROM desempenho d WHERE d.participante_id = " + participanteId + " AND d.parcial = 0) "
				+ "AND e.id NOT IN (SELECT DISTINCT d.evento_id FROM desempenho d WHERE d.participante_id = " + participanteId + " AND d.aprovado = 'N' AND d.parcial = 0) "
				+ "AND ((epj.provedor_id IN (:provedoresCertificado)) OR e.certificado_personalizado IS NOT NULL) "
				+ "AND e.permite_certificado = 'S' "
				+ "AND p.id = " + participanteId + ") "
				+ "ORDER BY e.data_inicio_previsto DESC";
		
		return entityManager
				.createNativeQuery(sql, Evento.class)
				.setParameter("provedoresCertificado", ProvedorEvento.PROVEDORES_CERTIFICADO)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Evento> findEventosComCertificadosEmitidos(){
		return entityManager.createNamedQuery("Evento.findEventosComCertificadosEmitidos").getResultList();
	}
	
	/**
	 * Filtra os eventos EAD com pré-inscrição encerrada para exportação das inscriçãos do SIGED para o AVA
	 * @param titulo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Evento> findForInscricoesSIGEDtoAVA(String titulo) {
		EventoFiltro filtro = new EventoFiltro();
		filtro.setTitulo(titulo);
		filtro.setModalidadeId(Modalidade.EAD);

		/*
		 * DONE: __Modalidade Evento: Definir qual será a Modalidade do Evento na consulta (Criteria)
		 * Consulta atualizada em EventoDAO.getCriteria
		 */
		Criteria criteria = getCriteria(filtro, true, false);
		Date hoje = util.getDataFormatada(new Date());
		criteria.add(Restrictions.lt("dataFimPreInscricao", hoje));
		
		return criteria.list();

	}
	
	/**
	 * Filtra os eventos para emissão de Certificado de Instrutor
	 * @param titulo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Evento> findForCertificadoDeInstrutor(String titulo, boolean instrutorLogado) {
		EventoFiltro filtro = new EventoFiltro();
		filtro.setTitulo(titulo);
		filtro.setProvedorId(ProvedorEvento.IPC);
		filtro.setStatusEventoList(StatusEvento.REALIZADO, StatusEvento.EMANDAMENTO);
		
		Criteria criteria = getCriteria(filtro, false, false);
		Usuario usuarioLogado = AuthenticationService.getUsuarioLogadoOrNull();
		if(instrutorLogado && usuarioLogado != null) {
			criteria
				.createCriteria("moduloList")
				.createCriteria("instrutorList")
					.add(Restrictions.eq("cpf", util.formatarCpf(usuarioLogado.getCpf())));
		}
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Evento> findByPublicoAlvoPorPeriodoRealizacao(Date realizacao1, Date realizacao2, Long... publicos) {
		EventoFiltro filtro = new EventoFiltro();
		filtro.setRealizacao1(realizacao1);
		filtro.setRealizacao2(realizacao2);
		
		Criteria criteria = getCriteria(filtro, false, false);
		Disjunction publicoAlvo = Restrictions.disjunction();
		for(Long publicoId : publicos) {
			publicoAlvo.add(Restrictions.eq("id", publicoId));
		}
		criteria.createCriteria("publicoAlvoList", "publico", Criteria.INNER_JOIN).add(publicoAlvo);
		
		return criteria.list();
	}
	
	/**
	 * Consulta que lista quais Eventos serão exibidos na página inicial do sistema:
	 * <ul>
	 * <li>{@link EventoController#previstos(ModelMap,HttpServletRequest, ServletResponse)}</li>
	 * <li>{@link EventoController#emandamento(ModelMap, HttpServletRequest)}</li>
	 * <li>{@link EventoController#realizados(ModelMap, HttpServletRequest, HttpServletResponse)}</li>
	 * </ul>
	 * </p> A exibição do evento leva em conta o ambiente deslogado, ambiente logado(ADMIN, ALUNO), eventos publicados e eventos com interessados
	 * 
	 * @param filtro Parâmetros para a consulta
	 * @return lista de Eventos já filtrados para a páginal inicial do sistema
	 * 
	 * @see {@link #findByFiltro(EventoFiltro, boolean, boolean)}
	 * @since 09/10/2018
	 */
	public List<Evento> findEventosPaginaInicial(EventoFiltro filtro) {
		boolean isPublicado = true;
		Usuario usuarioLogado = null;
		
		usuarioLogado = AuthenticationService.getUsuarioLogadoOrNull();
		
		if(usuarioLogado == null) {
			filtro.setProvedores(ProvedorEvento.IPC, ProvedorEvento.TCE_CE, ProvedorEvento.IRB ); 
		} else {
			isPublicado = !usuarioLogado.isAdministrador();
		}
		
		return findByFiltro(filtro, isPublicado, true);
	}
	
	/**
	 * Aplica os filtros setados no <tt>EventoFiltro</tt>, permitindo informar se é publicado e também se o <tt>Usuario</tt> logado está na lista de interessados do <tt>Evento</tt>. 
	 * <p/>
	 * Caso necessário informar um único <tt>StatusEvento</tt> basta setar com o método {@link EventoFiltro#setStatusEvento(StatusEvento)} no filtro.
	 * <p/> 
	 * Caso necessário informar mais de um <tt>StatusEvento</tt> basta setar com o método {@link EventoFiltro#setStatusEventoList(StatusEvento...)} no filtro.
	 *  
	 * @param filtro
	 * @param isEventoPublicado Caso necessário verificar se o Evento está publicado. Para ADMINS esse valor, no geral, deve ser <tt>false</tt>
	 * @param verificarInteressados Caso necessário verificar se o Usuario logado está entre os interessados do Evento. Para quando {@code isEventoPublicado = true}
	 * @return Lista de eventos de acordo com o filtro 
	 * 
	 * @see #getCriteria(EventoFiltro, boolean, boolean)
	 * @since 27/09/2018
	 * @author Rafael de Castro
	 */
	@SuppressWarnings("unchecked")
	public List<Evento> findByFiltro(EventoFiltro filtro, boolean isEventoPublicado, boolean verificarInteressados) {
		return getCriteria(filtro, isEventoPublicado, verificarInteressados).list();
	}
	
	/**
	 * Desde a versão 5.2 do Hibernate, a Hibernate Criteria API está depreciada e o novo desenvolvimento está focado na JPA Criteria API. 
	 * Versão usada no Projeto é a 3.5.1-Final.
	 * 
	 * @since 27/09/2018
	 * @author Rafael de Castro
	 */
	private Criteria getCriteria(EventoFiltro filtro, boolean isEventoPublicado, boolean verificarInteressados) {
		Criteria criteria = createCriteria();
		Date hoje = util.getDataFormatada(new Date());
		
		if(filtro != null) {
			if(filtro.getTipoPublicoAlvo() != null && filtro.getTipoPublicoAlvo() != 0)
				criteria.createCriteria("publicoAlvoList", "publico", Criteria.INNER_JOIN).add(Restrictions.eq("publico.id", filtro.getTipoPublicoAlvo()));
			
			if (filtro.getPrevisto1() != null)
				criteria.add(Restrictions.ge("dataInicioPrevisto", util.getDataFormatada(filtro.getPrevisto1())));
				
			if (filtro.getPrevisto2() != null)
				criteria.add(Restrictions.le("dataInicioPrevisto", util.getDataFormatada(filtro.getPrevisto2())));
			
			if (filtro.getRealizacao1() != null)
				criteria.add(Restrictions.ge("dataInicioRealizacao", util.getDataFormatada(filtro.getRealizacao1())));
			
			if (filtro.getRealizacao2() != null)
				criteria.add(Restrictions.le("dataInicioRealizacao", util.getDataFormatada(filtro.getRealizacao2())));
			
			if (filtro.getTipoEvento() != null && filtro.getTipoEvento() != 0)
				criteria.createCriteria("tipoId", "tipo", Criteria.INNER_JOIN).add(Restrictions.eq("tipo.id", filtro.getTipoEvento()));
			
			if (filtro.getTitulo() != null && !filtro.getTitulo().isEmpty())
				criteria.add(Restrictions.sqlRestriction(StringUtil.sqlTranslateQuery("{alias}.titulo", filtro.getTitulo())));

			/*
			 * Diferencia entre a lista e atributo único para o Provedor
			 */
			if(filtro.getProvedores() != null && filtro.getProvedores().length > 0) {
				DetachedCriteria dc = DetachedCriteria.forClass(EventoProvedorJoin.class);
				Disjunction provedores = Restrictions.disjunction();
				for(Long provedorId : filtro.getProvedores()) {
					provedores.add(Restrictions.eq("id.provedor.id", provedorId));
				}
				dc.add(provedores).setProjection(Projections.property("id.evento"));
				criteria.add(Subqueries.propertyIn("id", dc));
			} else if (filtro.getProvedorId() != null && filtro.getProvedorId() != 0) {
				DetachedCriteria dc = DetachedCriteria.forClass(EventoProvedorJoin.class);
				dc.add(Restrictions.eq("id.provedor.id", filtro.getProvedorId()));
				dc.setProjection(Projections.property("id.evento"));
				
				criteria.add(Subqueries.propertyIn("id", dc));
			}
			
			/*
			 * DONE: __Modalidade Evento: Definir qual será a Modalidade do Evento na consulta (Criteria)
			 * @deprecated Usar a regra definida em Evento.isPresencial ou Evento.isEAD
			 if (filtro.getModalidadeId() != null && filtro.getModalidadeId() != 0)
				criteria.createCriteria("modalidadeId", "modalidade", Criteria.INNER_JOIN).add(Restrictions.eq("modalidade.id", filtro.getModalidadeId()));
			 */
			if (filtro.getModalidadeId() != null && filtro.getModalidadeId() != 0) {
				DetachedCriteria dcModalidade = DetachedCriteria.forClass(Modulo.class);
				dcModalidade.createCriteria("modalidade", "modalidade", Criteria.INNER_JOIN).add(Restrictions.eq("id", Modalidade.PRESENCIAL));
				dcModalidade.createCriteria("eventoId", "evento", Criteria.INNER_JOIN);
				dcModalidade.setProjection(Projections.property("eventoId.id"));
				
				criteria.createCriteria("moduloList", "modulos", Criteria.INNER_JOIN);
				if(filtro.getModalidadeId().equals(Modalidade.PRESENCIAL)) {
					criteria.add(Subqueries.propertyIn("id", dcModalidade));
				} else if(filtro.getModalidadeId().equals(Modalidade.EAD)) {
					criteria.add(Subqueries.propertyNotIn("id", dcModalidade));
				}
					
			}
			
			Criterion previsto = Restrictions.disjunction()
					.add(Restrictions.conjunction()
							.add(Restrictions.isNotNull("dataInicioRealizacao"))
							.add(Restrictions.gt("dataInicioRealizacao", hoje)))
					.add(Restrictions.conjunction()
							.add(Restrictions.isNull("dataInicioRealizacao"))
							.add(Restrictions.isNotNull("dataInicioPrevisto"))
							.add(Restrictions.gt("dataInicioPrevisto", hoje)));
			
			Criterion emandamento = Restrictions.and(
					Restrictions.le("dataInicioRealizacao", hoje),
					Restrictions.ge("dataFimRealizacao", hoje));
			
			Criterion realizado = Restrictions.lt("dataFimRealizacao", hoje);
			
			/*
			 * Diferencia entre a lista e atributo único para o StatusEvento
			 */
			if(filtro.getStatusEventoList() != null) {
				Disjunction disj = Restrictions.disjunction();
				for(StatusEvento status : filtro.getStatusEventoList()) {
					if (status.equals(StatusEvento.PREVISTO)) {
						disj.add(previsto);
					} else if (status.equals(StatusEvento.EMANDAMENTO)) {
						disj.add(emandamento);
					} else if (status.equals(StatusEvento.REALIZADO)) {
						disj.add(realizado);
					}
				}
				criteria.add(disj);
			} else if(filtro.getStatusEvento() != null) {
				StatusEvento status = filtro.getStatusEvento();
				if (status.equals(StatusEvento.PREVISTO)) {
					criteria.add(previsto);
				} else if (status.equals(StatusEvento.EMANDAMENTO)) {
					criteria.add(emandamento);
				} else if (status.equals(StatusEvento.REALIZADO)) {
					criteria.add(realizado);
				}
			}
		}
		
		if(isEventoPublicado) {
			criteria.add(Restrictions.eq("publicado", "S"));
			
			/**
			 * Verifica se o usuario logado está na lista de interessados do evento. 
			 * - Usuario ADM e ADMCONSULTA não tem participante associado, por isso eles ficam fora da verificação de interessado.
			 * - Usuario não logado só ve os eventos que não tem lista de interessados
			 * 
			 */
			if(verificarInteressados) {
				Usuario usuarioLogado = null;
				try {
					usuarioLogado = AuthenticationService.getUsuarioLogado();
				} catch (ClassCastException e) {
					usuarioLogado = null;
				}
				
				criteria.createCriteria("interessados", "interessados", Criteria.LEFT_JOIN);
				Criterion semInteressados = Restrictions.isEmpty("interessados");
				if(usuarioLogado != null && !usuarioLogado.isAdministrador() && !usuarioLogado.isAdministradorConsulta()) {
					String[] cpfs = {usuarioLogado.getCpf()};
					Criterion comInteressados = Restrictions.conjunction()
							.add(Restrictions.isNotEmpty("interessados"))
							.add(Restrictions.in("interessados.cpf", cpfs));
					Criterion filtroInteressados = Restrictions.disjunction()
							.add(semInteressados)
							.add(comInteressados);
					criteria.add(filtroInteressados);
				} else if(usuarioLogado == null) {
					criteria.add(semInteressados);
				}
			}
		}
		
		return criteria.addOrder(Order.desc("dataInicioPrevisto")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	}
	
	
	/*  USADO PELO APP TCE  */
	
	@SuppressWarnings("unchecked")
	public List<Evento> findMeuEventoByCriteria(String cpf, Date realizacao1,
			Date realizacao2, Long tipoId, String titulo, Long provedorId) {

		Session sess = (Session) entityManager.getDelegate();

		Criteria aux = sess.createCriteria(Evento.class);

		if (realizacao1 != null) {
			aux.add(Restrictions.ge("dataInicioRealizacao", realizacao1));
		}
		if (realizacao2 != null) {
			aux.add(Restrictions.le("dataInicioRealizacao", realizacao2));
		}
		if (tipoId != null) {
			if (tipoId != 0) {
				aux.add(Restrictions.eq("tipoId",
						tipoEventoDao.findById(tipoId)));
			}
		}
		if (titulo != null) {
			if (!titulo.isEmpty()) {
				aux.add(Restrictions.sqlRestriction(StringUtil.sqlTranslateQuery("{alias}.titulo", titulo)));
			}
		}

		if (provedorId != null) {
			if (provedorId != 0) {
				DetachedCriteria dc = DetachedCriteria.forClass(EventoProvedorJoin.class);
				dc.add(Restrictions.eq("id.provedor.id", provedorId));
				dc.setProjection(Projections.property("id.evento"));
				
				aux.add(Subqueries.propertyIn("id", dc));
			}
		}
		
		aux.createCriteria("inscricaoList", "i")
				.add(Restrictions.eq("i.confirmada", "S"))
				.createCriteria("i.participanteId", "p")
				.add(Restrictions.eq("p.cpf", cpf));
		
		aux.addOrder(Order.desc("dataInicioPrevisto"));
		
		return aux.list();

	}
	
	@SuppressWarnings("unchecked")
	public List<Evento> findByPrevisto(int registroInicial, int quantidadeDeRegistros) {
		Criteria criteria = getCriteria(new EventoFiltro(StatusEvento.PREVISTO), true, true);
		return criteria
				.setFirstResult(registroInicial)
				.setMaxResults(quantidadeDeRegistros)
				.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Evento> findByEmAndamento(int registroInicial, int quantidadeDeRegistros) {
		Criteria criteria = getCriteria(new EventoFiltro(StatusEvento.EMANDAMENTO), true, true);
		return criteria
				.setFirstResult(registroInicial)
				.setMaxResults(quantidadeDeRegistros)
				.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Evento> findByRealizado(int registroInicial, int quantidadeDeRegistros) {
		Criteria criteria = getCriteria(new EventoFiltro(StatusEvento.REALIZADO), true, true);
		return criteria
				.setFirstResult(registroInicial)
				.setMaxResults(quantidadeDeRegistros)
				.list();
	}
	
	/*  USADO PELO APP TCE  */
	
	
	
	
	/*  TODO Consultas não referenciadas  */
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<Evento> findMeusEventosComoInstrutorByCriteria(String cpf, Date realizacao1,
			Date realizacao2, Long tipoId, String titulo, Long provedorId) {

		Session sess = (Session) entityManager.getDelegate();

		Criteria aux = sess.createCriteria(Evento.class,"e");

		if (realizacao1 != null) {
			aux.add(Restrictions.ge("dataInicioRealizacao", realizacao1));
		}
		if (realizacao2 != null) {
			aux.add(Restrictions.le("dataInicioRealizacao", realizacao2));
		}
		if (tipoId != null) {
			if (tipoId != 0) {
				aux.add(Restrictions.eq("tipoId",
						tipoEventoDao.findById(tipoId)));
			}
		}
		if (titulo != null) {
			if (!titulo.isEmpty()) {
				aux.add(Restrictions.sqlRestriction(StringUtil.sqlTranslateQuery("{alias}.titulo", titulo)));
				//aux.add(Restrictions.ilike("titulo", "%" + titulo + "%"));
			}
		}

		if (provedorId != null) {
			if (provedorId != 0) {
				//aux.add(Restrictions.eq("provedorId", provedorEventoDao.findById(provedorId)));
				DetachedCriteria dc = DetachedCriteria.forClass(EventoProvedorJoin.class);
				dc.add(Restrictions.eq("id.provedor.id", provedorId));
				dc.setProjection(Projections.property("id.evento"));
				
				aux.add(Subqueries.propertyIn("id", dc));
			}
		}

		aux.createCriteria("moduloList", "m")
				.createCriteria("m.instrutorList", "i")
				.add(Restrictions.eq("i.cpf", cpf)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.addOrder(Order.desc("e.dataInicioPrevisto"));
		
		return aux.list();
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<Evento> findRealizadosByProvedorId(Long provedorId) {
		@SuppressWarnings("unused")
		@Deprecated
		String sqlOld = "SELECT e FROM Evento e WHERE e.dataFimRealizacao < trunc(current_date(),'DDD') and e.provedorId.id = :provedorId ORDER BY e.dataInicioPrevisto DESC";
		
		String sql = "select e from Evento e"
				+ " where e.dataFimRealizacao < trunc(current_date(),'DDD')"
				+ " and :provedorId in (select p.id from e.provedores p)"
				+ " order by e.dataInicioPrevisto desc";
		Query query = entityManager.createQuery(sql);
		query.setParameter("provedorId", provedorId);
		
		return query.getResultList();
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public Collection<Evento> findEventoByCriteria(Long publicoAlvoId,
			Date previsto1, Date previsto2, Date realizacao1, Date realizacao2,
			Long tipoId, String identificador, String titulo, Long provedorId) {

		Session sess = (Session) entityManager.getDelegate();

		Criteria aux = sess.createCriteria(Evento.class);

		if (previsto1 != null) {
			aux.add(Restrictions.ge("dataInicioPrevisto", previsto1));
		}
		if (previsto2 != null) {
			aux.add(Restrictions.le("dataInicioPrevisto", previsto2));
		}
		if (realizacao1 != null) {
			aux.add(Restrictions.ge("dataInicioRealizacao", realizacao1));
		}
		if (realizacao2 != null) {
			aux.add(Restrictions.le("dataInicioRealizacao", realizacao2));
		}
		if (tipoId != 0) {
			aux.add(Restrictions.eq("tipoId", tipoEventoDao.findById(tipoId)));
		}		
		if (titulo != null && !titulo.isEmpty()) {
			aux.add(Restrictions.sqlRestriction(StringUtil.sqlTranslateQuery("{alias}.titulo", titulo)));
		}
		if (publicoAlvoId != 0) {
			aux.createCriteria("publicoAlvoList", "p").add(
					Restrictions.eq("p.id", publicoAlvoId));
		}
		if (provedorId != 0) {
			DetachedCriteria dc = DetachedCriteria.forClass(EventoProvedorJoin.class);
			dc.add(Restrictions.eq("id.provedor.id", provedorId));
			dc.setProjection(Projections.property("id.evento"));
			
			aux.add(Subqueries.propertyIn("id", dc));
		}
		
		aux.addOrder(Order.desc("dataInicioPrevisto"));
		
		return aux.list();
	}
	

	/**
	 * Consulta que era usada antes para eventos realizados filtrando pelo periodo
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public Collection<Evento> findEventoByCriteria(StatusEvento status,
			Long modalidadeId, Date realizacao1, Date realizacao2,
			String titulo,
			boolean publicado) {

		Session sess = (Session) entityManager.getDelegate();

		Criteria aux = sess.createCriteria(Evento.class);

		if (publicado) {
			aux.add(Restrictions.eq("publicado", "S"));
		}
		
		if (titulo != null && !titulo.isEmpty()) {
			aux.add(Restrictions.sqlRestriction(StringUtil.sqlTranslateQuery("{alias}.titulo", titulo)));
		}

		if (status != null) {
			if (status.equals(StatusEvento.PREVISTO)) {
				aux.add(Restrictions.or(
								Restrictions.gt("dataInicioRealizacao", new Date()),
								Restrictions.isNull("dataInicioRealizacao"))); 
				aux.add(Restrictions.and(
								Restrictions.isNotNull("dataInicioPrevisto"),
								Restrictions.gt("dataInicioPrevisto", new Date())));
			} else if (status.equals(StatusEvento.EMANDAMENTO)) {
				aux.add(Restrictions.and(
						Restrictions.le("dataInicioRealizacao", new Date()),
						Restrictions.ge("dataFimRealizacao", new Date())));			
			} else if (status.equals(StatusEvento.REALIZADO)) {
				aux.add(Restrictions.lt("dataFimRealizacao", new Date()));
			}
		}

		if (realizacao1 != null) {
			aux.add(Restrictions.ge("dataInicioRealizacao", realizacao1));
		}
		if (realizacao2 != null) {
			aux.add(Restrictions.le("dataInicioRealizacao", realizacao2));
		}

		if (modalidadeId != null) {
			if (modalidadeId != 0) {
				aux.createCriteria("modalidadeId", "m").add(
						Restrictions.eq("m.id", modalidadeId));
			}
		}
		aux.addOrder(Order.desc("dataInicioPrevisto"));
		
		return aux.list();

	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public Collection<Generica> findDesempenhoByCpf(String cpf, Long id) {
		return entityManager
				.createNativeQuery(
						"SELECT evento.titulo as evento, modulo.titulo as modulo, participante.nome as participante, nota.nota as nota, nota.nota as frequencia FROM participante "
								+ "INNER JOIN inscricao ON (inscricao.participante_id = participante.id) "
								+ "INNER JOIN evento ON (evento.id = inscricao.evento_id) "
								+ "INNER JOIN modulo ON (evento.id = modulo.evento_id) "
								+ "LEFT JOIN nota ON (nota.evento_id = evento.id and nota.modulo_id = modulo.id and nota.participante_id = participante.id) "
								+ "WHERE inscricao.confirmada = 'S' and evento.id = "
								+ id + " and participante.cpf = '" + cpf + "'")
				.getResultList();
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public Collection<Generica> findDesempenho(Long id) {
		return entityManager
				.createNativeQuery(
						"SELECT evento.titulo as evento, modulo.titulo as modulo, participante.nome as participante, nota.nota as nota, nota.nota as frequencia FROM participante "
								+ "INNER JOIN inscricao ON (inscricao.participante_id = participante.id) "
								+ "INNER JOIN evento ON (evento.id = inscricao.evento_id) "
								+ "INNER JOIN modulo ON (evento.id = modulo.evento_id) "
								+ "LEFT JOIN nota ON (nota.evento_id = evento.id and nota.modulo_id = modulo.id and nota.participante_id = participante.id) "
								+ "WHERE inscricao.confirmada = 'S' and evento.id = "
								+ id
								+ " ORDER BY evento.titulo, participante.nome")
				.getResultList();
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public Collection<Generica> findDesempenhoBySetor(Long id, String setorId) {
		return entityManager
				.createNativeQuery(
						"SELECT evento.titulo as evento, modulo.titulo as modulo, participante.nome as participante, nota.nota as nota, nota.nota as frequencia FROM participante "
								+ "INNER JOIN inscricao ON (inscricao.participante_id = participante.id) "
								+ "INNER JOIN evento ON (evento.id = inscricao.evento_id) "
								+ "INNER JOIN modulo ON (evento.id = modulo.evento_id) "
								+ "LEFT JOIN nota ON (nota.evento_id = evento.id and nota.modulo_id = modulo.id and nota.participante_id = participante.id) "
								+ "WHERE inscricao.confirmada = 'S' and evento.id = "
								+ id
								+ " and participante.setor_id = "
								+ setorId
								+ " ORDER BY evento.titulo, participante.nome")
				.getResultList();
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<Evento> findByNaoPrevistoComModuloTodos() {
		return entityManager.createNamedQuery("Evento.findByNaoPrevistoComModuloTodos").getResultList();
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<Evento> findByEmAndamento() {
		return entityManager.createNamedQuery("Evento.findByEmAndamento")
				.getResultList();
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<Evento> findByEmAndamentoTodos() {
		return entityManager.createNamedQuery("Evento.findByEmAndamentoTodos").getResultList();
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<Evento> findByPrevisto() {
		return entityManager.createNamedQuery("Evento.findByPrevisto")
				.getResultList();
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<Evento> findByPrevistoTodos() {
		return entityManager.createNamedQuery("Evento.findByPrevistoTodos")
				.getResultList();
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<Evento> findByNaoPrevisto() {
		return entityManager.createNamedQuery("Evento.findByNaoPrevisto")
				.getResultList();
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<Evento> findByRealizadoTodos() {
		return entityManager.createNamedQuery("Evento.findByRealizadoTodos")
				.getResultList();
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<Evento> findByRealizado() {
		return entityManager.createNamedQuery("Evento.findByRealizado")
				.getResultList();
	}
	
	/*  Consultas não referenciadas  */
	
}