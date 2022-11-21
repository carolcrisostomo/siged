package br.com.siged.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Participante;
import br.com.siged.filtro.ParticipanteFiltro;
import br.com.siged.util.StringUtil;

/**
 *
 * @author Marcelo
 */
@Repository("participanteDAO")
public class ParticipanteDAO {
	protected EntityManager entityManager;
	@Autowired
	private TipoPublicoAlvoDAO tipoPublicoAlvoDao;
	@Autowired
	private FormacaoAcademicaDAO formacaoAcademicaDao;
	@Autowired
	private NivelEscolaridadeDAO nivelEscolaridadeDao;
	@Autowired
	private SetorDAO setorDao;
	@Autowired
	private PaginationUtil paginationUtil;
	
	public ParticipanteDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Participante find(Long id) {
		return entityManager.find(Participante.class, id);
	}

	@Transactional
	public void persist(Participante Participante) {
		entityManager.persist(Participante);
	}

	@Transactional
	public void merge(Participante Participante) {
		entityManager.merge(Participante);
	}

	@Transactional
	public void remove(Participante Participante) {
		entityManager.remove(Participante);
	}

	@SuppressWarnings("unchecked")
	public List<Participante> findAll() {
		return entityManager.createNamedQuery("Participante.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public PaginatedList filtrar(ParticipanteFiltro filtro, Pageable pageable) {
		Criteria criteria = createCriteria();
		
		paginationUtil.preparar(criteria, pageable);
		adicionarFiltro(criteria, filtro);
		
		if(pageable.getSort() == null)
			criteria.addOrder(Order.asc("nome"));
		
		return new PaginatedListAdapter(new PageImpl<>(criteria.list(), pageable, total(filtro)));
	}
	
	private void adicionarFiltro(Criteria criteria, ParticipanteFiltro filtro) {
		if (filtro.getNome() != null && !filtro.getNome().isEmpty())
			criteria.add(Restrictions.sqlRestriction(StringUtil.sqlTranslateQuery("{alias}.nome", filtro.getNome())));
		
		if (filtro.getTipoPublicoAlvo() != null && filtro.getTipoPublicoAlvo() != 0)
			criteria.add(Restrictions.eq("tipoId", tipoPublicoAlvoDao.findById(filtro.getTipoPublicoAlvo())));
		
		if (filtro.getCpf() != null && filtro.getCpf() != "")
			criteria.add(Restrictions.like("cpf", "%" + filtro.getCpf().replace(".", "").replace("-", "") + "%"));
		
		if (filtro.getFormacaoAcademica() != null && filtro.getFormacaoAcademica() != 0)
			criteria.add(Restrictions.eq("formacaoId", formacaoAcademicaDao.findById(filtro.getFormacaoAcademica())));
		
		if (filtro.getNivelEscolaridade() != null && filtro.getNivelEscolaridade() != 0)
			criteria.add(Restrictions.eq("escolaridadeId", nivelEscolaridadeDao.findById(filtro.getNivelEscolaridade())));
	}
	
	private Long total(ParticipanteFiltro filtro) {
		Criteria criteria = createCriteria();
		adicionarFiltro(criteria, filtro);
		criteria.setProjection(Projections.rowCount());
		
		return (Long) criteria.uniqueResult();
	}
	
	private Criteria createCriteria() {
		Session sess = (Session) entityManager.getDelegate();
		return sess.createCriteria(Participante.class);
	}

	@SuppressWarnings("unchecked")
	public List<Participante> findParticipanteEficaciaAvaliada() {
		return entityManager.createNamedQuery("Participante.findParticipanteEficaciaAvaliada").getResultList();
	}

	public Participante findById(Long codigo) {
		try{
			return (Participante) entityManager.createNamedQuery("Participante.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Participante> findByTipo(Long tipoId) {
		return entityManager.createNamedQuery("Participante.findByTipo").setParameter("tipoId", tipoId).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Participante> findByTipos(Long[] tipoId) {
		String hql = "SELECT p FROM Participante p WHERE ";
		for(int i = 0; i < tipoId.length; i++){
			if(i == 0) {
				hql += "p.tipoId.id = :tipoId" + i;
			} else {
				hql += " OR p.tipoId.id = :tipoId" + i;
			}
		}
		hql += " ORDER BY p.nome";
		Query query = entityManager.createQuery(hql, Participante.class);
		for(int i = 0; i < tipoId.length; i++){
			query.setParameter("tipoId" + i, tipoId[i]);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Participante> findByTipoAndEvento(Long tipoId, Long eventoId) { 
		return entityManager.createNamedQuery("Participante.findByTipoAndEventoId").setParameter("tipoId", tipoId).setParameter("eventoId", eventoId).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Participante> findByTiposAndEvento(Long[] tipoId, Long eventoId) {
		String detachHql = "SELECT p FROM Participante p WHERE ";
		for(int i = 0; i < tipoId.length; i++){
			if(i == 0) {
				detachHql += "p.tipoId.id = :tipoId" + i;
			} else {
				detachHql += " OR p.tipoId.id = :tipoId" + i;
			}
		}
		String hql = "SELECT i.participanteId FROM Inscricao i WHERE i.participanteId.id in ("
				+ detachHql
				+ ") AND i.eventoId.id = :eventoId AND i.confirmada = 'S' ORDER BY i.participanteId.nome";
		Query query = entityManager.createQuery(hql, Participante.class);
		for(int i = 0; i < tipoId.length; i++){
			query.setParameter("tipoId" + i, tipoId[i]);
		}
		return query.setParameter("eventoId", eventoId).getResultList();
	}

	public Participante findByCpf(String cpf) {
		try {  
			return (Participante) entityManager.createNamedQuery("Participante.findByCpf").setParameter("cpf", cpf).getSingleResult();
		} catch ( NoResultException nre ) {  
			return null;  
		} 
	}
	
	@SuppressWarnings("unchecked")
	public List<Participante> findByEmail(String email) {
		return entityManager.createNamedQuery("Participante.findByEmail").setParameter("email", email).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Participante> findByNome(String nome) {
		return entityManager.createNativeQuery("SELECT p.* FROM Participante p"
				+ " WHERE " + StringUtil.sqlTranslateQuery("p.nome", nome)
				+ " ORDER BY p.nome", Participante.class).getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<Participante> findParticipanteByCriteria(String nome,Long tipoId, String cpf, Long formacaoId, Long escolaridadeId) {

		Session sess = (Session) entityManager.getDelegate();

		Criteria aux = sess.createCriteria(Participante.class);
		if (nome != "") {
			aux.add(Restrictions.ilike("nome", "%" + nome + "%"));
		}
		if (tipoId != 0) {
			aux.add(Restrictions.eq("tipoId", tipoPublicoAlvoDao.findById(tipoId)));
		}

		if (cpf != "") {
			aux.add(Restrictions.like("cpf", "%" + cpf.replace(".", "").replace("-", "") + "%"));
		}
		if (formacaoId != 0) {
			aux.add(Restrictions.eq("formacaoId", formacaoAcademicaDao.findById(formacaoId)));
		}
		if (escolaridadeId != 0) {
			aux.add(Restrictions.eq("escolaridadeId", nivelEscolaridadeDao.findById(escolaridadeId)));
		}

		return aux.list();

	}

	@SuppressWarnings("unchecked")
	public List<Participante> findByEventoIdAndModuloId(Long eventoId, Long moduloId) {
		Query query = entityManager.createNamedQuery("Participante.findByEventoIdAndModuloId");
		query.setParameter("eventoId", eventoId);
		query.setParameter("moduloId", moduloId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Participante> findByEventoId(Long eventoId) {
		Query query = entityManager.createNamedQuery("Participante.findByEventoId");

		query.setParameter("eventoId", eventoId);

		return query.getResultList();
	}
	
	public List<Participante> semAvaliacaoNoModulo(Modulo modulo) {
		if(modulo == null){
			return new ArrayList<Participante>();
		}
		
		String hql = "SELECT p FROM Inscricao i INNER JOIN i.participanteId p WHERE i.eventoId = :evento AND i.confirmada = 'S' "
				+ "AND p NOT IN (SELECT p2 FROM AvaliacaoReacao a INNER JOIN a.participante p2 WHERE a.modulo = :modulo) ORDER BY p.nome";
		
		return entityManager.createQuery(hql, Participante.class)
				.setParameter("evento", modulo.getEventoId())
				.setParameter("modulo", modulo)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Participante> findServidoresInscricaoConfirmada(Long eventoId) {
		Query query = entityManager.createNamedQuery("Participante.findSevidoresInscricaoConfirmada");

		query.setParameter("eventoId", eventoId);

		return query.getResultList();
	}
	

	//Este método lista, para um evento específico, os participantes cuja eficácia foi avaliada.
	@SuppressWarnings("unchecked")
	public List<Participante> findByEventoIdParticipanteEficaciaAvaliada(Long eventoId) {
		Query query = entityManager.createNamedQuery("Participante.findByEventoIdParticipanteEficaciaAvaliada");

		query.setParameter("eventoId", eventoId);

		return query.getResultList();
	}

	public Participante findServidorByCpf(String cpf) {
		try {

			/**
			 * A condição (f.ATIVOFP = 1 OR toc.id = 8) foi adicionada para contemplar servidores que estão cedidos sem remuneração (“Cessão de Servidor sem nenhuma remuneração”).
			 * Antes estava somente f.ATIVOFP = 1, ou seja, somente quem está ativo na folha de pagamento. Logo os servidores que se encaixam na condição acima não poderiam logar no sistema.
			 */
			Object[] res = (Object[]) entityManager
					.createNativeQuery(
							"SELECT DISTINCT 	p.celular 					AS celular, "
											+ "	cast(p.cpf as varchar(11))	AS cpf, "
											+ "	p.email 					AS email, "
											+ "	p.nome				AS nome, "
											+ "	p.telefone 					AS telefone, "
//											+ " p.idTipoOcupacao 			AS idTipoOcupacao, " //TODO: verificar
											+ " 1			AS idTipoOcupacao, "
											+ "	1 	AS setorId "
									+ "FROM  participante p where p.cpf = :cpf").setParameter("cpf", cpf)
									.getSingleResult();


			Participante participante = new Participante();
			if (res[0] != null) 
				participante.setCelular(res[0].toString());

			if (res[1] != null) 
				participante.setCpf(res[1].toString());

			if (res[2] != null) 
				participante.setEmail(res[2].toString());
			
			if (res[3] != null) 
				participante.setNome(res[3].toString());

			if (res[4] != null) 
				participante.setTelefone(res[4].toString());

			if (res[5] != null) 
				participante.setIdTipoOcupacao(Long.parseLong(res[5].toString()));

			if (res[6] != null) 
				participante.setSetorId(setorDao.find(Long.parseLong(res[6].toString())));

			return participante;
		} catch (NoResultException nre) {
			return null;
		}

	}
	
	public List<Participante> findByResponsaveisPeloEvento() {
		return  entityManager.createQuery(
				"SELECT p FROM Participante p " + 
				"WHERE p.responsavelEvento = true " + 
				"ORDER BY p.nome", Participante.class).getResultList();
	}
	
	
	public List<Participante> findServidoresAprovados(Long eventoId){
		String hql = "select p from Inscricao i inner join i.participanteId p where i.eventoId.id = :eventoId and i.confirmada = 'S' and p.tipoId.id in (1, 4) "
				+ "and p NOT IN (select distinct p from Desempenho d inner join d.participanteId p where d.eventoId.id = :eventoId and d.aprovado = 'N') order by p.nome";
		return entityManager.createQuery(hql, Participante.class).setParameter("eventoId", eventoId).getResultList();
		
		/**
		 * Os fields estavam invalidando a consulta
		 * 
		return entityManager.createNativeQuery(
				"SELECT p.nome, p.cpf, p.id " +
				"FROM inscricao i INNER JOIN participante p on i.participante_id = p.id " + 
				"WHERE  i.evento_id = " + eventoId + " and i.confirmada = 'S' and p.tipo_id in (1, 4) " + 
				"AND p.id NOT IN (SELECT DISTINCT participante_id FROM desempenho WHERE evento_id = " + eventoId + " AND aprovado = 'N') " + 
				"ORDER by p.nome", Participante.class).getResultList();
		*/
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<Participante> findParticipantesAprovados(Long eventoId){
		
		return entityManager.createQuery(
				"SELECT new Participante (i.participanteId.id, i.participanteId.cpf, i.participanteId.nome)" +
				"FROM Inscricao i " + 
				"WHERE i.eventoId.id = :eventoId and i.confirmada = 'S' " + 
				"AND i.participanteId.id NOT IN (SELECT DISTINCT d.participanteId.id FROM Desempenho d WHERE d.eventoId.id = :eventoId AND d.aprovado = 'N') " + 
				"ORDER by i.participanteId.nome").setParameter("eventoId", eventoId).getResultList();
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<Participante> findParticipantesQueEmitiramCertificadosNoEvento(Long eventoId){
			
		return entityManager.createNativeQuery(
				"SELECT DISTINCT p.* " +
				"FROM certificado_emitido ce INNER JOIN participante p ON ce.PARTICIPANTE_ID = p.ID " + 
				"WHERE ce.EVENTO_ID = " + eventoId, Participante.class).getResultList();
		
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Participante> findParticipantesComCertificadosEmitidos(){
			
		return entityManager.createNamedQuery("Participante.findParticipantesComCertificadosEmitidos").getResultList();
		
	}
	
	

}