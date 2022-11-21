package br.com.siged.dao;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.AvaliacaoEficacia;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Generica;
import br.com.siged.entidades.externas.UsuarioSCA;

/**
 * 
 * @author Rodrigo
 */
@Repository("avaliacaoeficaciaDao")
public class AvaliacaoEficaciaDAO {
	protected EntityManager entityManager;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ParticipanteDAO participanteDao;

	public AvaliacaoEficaciaDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public AvaliacaoEficacia find(Long id) {
		return entityManager.find(AvaliacaoEficacia.class, id);
	}

	@Transactional
	public void persist(AvaliacaoEficacia AvaliacaoEficacia) {
		entityManager.persist(AvaliacaoEficacia);
	}

	@Transactional
	public void merge(AvaliacaoEficacia AvaliacaoEficacia) {
		entityManager.merge(AvaliacaoEficacia);
	}

	@Transactional
	public void remove(AvaliacaoEficacia AvaliacaoEficacia) {
		entityManager.remove(AvaliacaoEficacia);
	}

	@SuppressWarnings("unchecked")
	public List<AvaliacaoEficacia> findAll() {
		return entityManager.createNamedQuery("AvaliacaoEficacia.findAll")
				.getResultList();
	}
	
	
	public AvaliacaoEficacia findById(Long codigo) {
		try {
			return (AvaliacaoEficacia) entityManager
					.createNamedQuery("AvaliacaoEficacia.findById")
					.setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {			
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AvaliacaoEficacia> findBySetor(Long codigo, String cpf) {
		return entityManager.createNamedQuery("AvaliacaoEficacia.findBySetor").setParameter("setorId", codigo).setParameter("cpf", cpf)
				.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<AvaliacaoEficacia> findByResponsavel(UsuarioSCA responsavel) {
		return entityManager.createNamedQuery("AvaliacaoEficacia.findByResponsavel").setParameter("responsavel", responsavel).getResultList();
	}
	
	
	public AvaliacaoEficacia findByEventoIdAndParticipanteId(Long eventoId, Long participanteId) {
		try {
			return (AvaliacaoEficacia) entityManager
					.createNamedQuery("AvaliacaoEficacia.findByEventoIdAndParticipanteId")
					.setParameter("eventoId", eventoId)
					.setParameter("participanteId", participanteId).getSingleResult();
		} catch (Exception e) {			
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Generica> findNaoRealizadasByParticipante(String cpf) {
		/*
		 * DONE: __Modalidade Evento: Definir qual será a Modalidade do Evento na consulta (SQL)
		 * @deprecated Consulta agora verifica se o evento é presencial por meio do módulo
		String sqlModalidadePresencial = "evento.modalidade_id = 1";
		 */
		String sqlModalidadePresencial = "evento.id IN (SELECT e.id from modulo m INNER JOIN evento e on m.evento_id = e.id WHERE m.modalidade_id = 1)";
		return entityManager.createNativeQuery(
				"SELECT * FROM (SELECT evento.data_inicio_previsto, evento.data_fim_previsto, evento.data_inicio_realizacao, evento.data_fim_realizacao, evento.id as evento_id, tipo_evento.descricao || ' ' || evento.titulo as evento, participante.id as participante_id, participante.nome as participante FROM participante " +
				"INNER JOIN inscricao ON (inscricao.participante_id = participante.id) " +
				"INNER JOIN evento ON (evento.id = inscricao.evento_id) " +
				"INNER JOIN tipo_evento ON (evento.tipo_id = tipo_evento.id) " +
				"WHERE inscricao.confirmada = 'S' " +
				" and participante.cpf = '" +cpf+ "' " +
				" and evento.carga_horaria >=16 "+
				" and " + sqlModalidadePresencial +
				" and evento.data_fim_realizacao + 90 <= SYSDATE and evento.data_fim_realizacao + 181 >= SYSDATE " +
				"MINUS " +
				"SELECT evento.data_inicio_previsto, evento.data_fim_previsto, evento.data_inicio_realizacao, evento.data_fim_realizacao, evento.id as evento_id, tipo_evento.descricao || ' ' || evento.titulo as evento, participante.id as participante_id, participante.nome as participante FROM avaliacao_eficacia " +
				"INNER JOIN evento ON (evento.id = avaliacao_eficacia.evento_id) " +
				"INNER JOIN tipo_evento ON (evento.tipo_id = tipo_evento.id) " +
				"INNER JOIN participante ON (participante.id = avaliacao_eficacia.participante_id) " +
				"WHERE " + sqlModalidadePresencial + ") resultado ORDER BY data_inicio_previsto DESC").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Generica> findNaoRealizadasByChefeSetor(Long setorId, String cpf) {		
		/*
		 * DONE: __Modalidade Evento: Definir qual será a Modalidade do Evento na consulta (SQL)
		 * @deprecated Consulta agora verifica se o evento é presencial por meio do módulo
		String sqlModalidadePresencial = "evento.modalidade_id = 1";
		 */
		String sqlModalidadePresencial = "evento.id IN (SELECT e.id from modulo m INNER JOIN evento e on m.evento_id = e.id WHERE m.modalidade_id = 1)";
		return entityManager.createNativeQuery(
				"SELECT * FROM (SELECT evento.data_inicio_previsto, evento.data_fim_previsto, evento.data_inicio_realizacao, evento.data_fim_realizacao, evento.id as evento_id, tipo_evento.descricao || ' ' || evento.titulo as evento, participante.id as participante_id, participante.nome as participante FROM participante " +
				"INNER JOIN inscricao ON (inscricao.participante_id = participante.id) " +
				"INNER JOIN evento ON (evento.id = inscricao.evento_id) " +
				"INNER JOIN tipo_evento ON (evento.tipo_id = tipo_evento.id) " +
				"WHERE inscricao.confirmada = 'S' " +
				" and participante.setor_id = " +setorId+
				" and participante.cpf <> '" +cpf+ "' " +
				" and evento.carga_horaria >=16 "+
				" and " + sqlModalidadePresencial +
				" and evento.data_fim_realizacao + 90 <= SYSDATE and evento.data_fim_realizacao + 181 >= SYSDATE " +
				"MINUS " +
				"SELECT evento.data_inicio_previsto, evento.data_fim_previsto, evento.data_inicio_realizacao, evento.data_fim_realizacao, evento.id as evento_id, tipo_evento.descricao || ' ' || evento.titulo as evento, participante.id as participante_id, participante.nome as participante FROM avaliacao_eficacia " +
				"INNER JOIN evento ON (evento.id = avaliacao_eficacia.evento_id) " +
				"INNER JOIN tipo_evento ON (evento.tipo_id = tipo_evento.id) " +
				"INNER JOIN participante ON (participante.id = avaliacao_eficacia.participante_id) " +
				"WHERE " + sqlModalidadePresencial + ") resultado ORDER BY data_inicio_previsto DESC").getResultList();
		
	}
	
	
	@SuppressWarnings("unchecked")
	public Collection<AvaliacaoEficacia> findAvaliacaoEficaciaByCriteria(Long eventoId, Long participanteId, Date data_cadastro1, Date data_cadastro2) {
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria aux = sess.createCriteria(AvaliacaoEficacia.class);
		if (eventoId != null && eventoId != 0) {
			aux.add(Restrictions.eq("eventoId", eventoDao.findById(eventoId)));
		}
		if (participanteId != null && participanteId != 0) {
			aux.add(Restrictions.eq("participanteId", participanteDao.findById(participanteId)));
		}
		if (data_cadastro1 != null) {
			aux.add(Restrictions.ge("dataCadastro", data_cadastro1));
		}
		if (data_cadastro1 != null) {
			aux.add(Restrictions.lt("dataCadastro", this.getTomorrowDate(data_cadastro2)));
		}
		aux.createCriteria("eventoId", "e").addOrder(Order.desc("e.dataInicioPrevisto"));
		return aux.list();
	    
	}
	
	
	@SuppressWarnings("unchecked")
	public Collection<AvaliacaoEficacia> findAvaliacaoEficaciaByCriteria(Long eventoId) {
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria aux = sess.createCriteria(AvaliacaoEficacia.class);
		if (eventoId != 0) {
			aux.add(Restrictions.eq("eventoId", eventoDao.findById(eventoId)));
		}
		
	    return aux.list();
	    
	}
	
	@SuppressWarnings("unchecked")
	public List<Evento> findByEventosCujaEficaciaDeveSerAvaliada(){
		/*
		 * DONE: __Modalidade Evento: Definir qual será a Modalidade do Evento na consulta (SQL)
		 * @deprecated Consulta agora verifica se o evento é presencial por meio do módulo
		String sqlModalidadePresencial = "evento.modalidade_id = 1";
		 */
		String sqlModalidadePresencial = "evento.id IN (SELECT e.id from modulo m INNER JOIN evento e on m.evento_id = e.id WHERE m.modalidade_id = 1)";
		return entityManager.createNativeQuery(
				"SELECT evento.id, evento.titulo " +
				"FROM evento " +
				"WHERE evento.carga_horaria >= 16 AND " + sqlModalidadePresencial + " " +
				"AND evento.data_fim_realizacao + 90 <= SYSDATE and evento.data_fim_realizacao + 181 >= SYSDATE  " +				
				"ORDER BY evento.data_inicio_realizacao DESC", Evento.class).getResultList();
	}
	
	
	private Date getTomorrowDate(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.DATE, 1);
	    return cal.getTime();
	}
}