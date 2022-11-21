package br.com.siged.dao;

import java.text.SimpleDateFormat;
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

import br.com.siged.entidades.Avaliacao;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Generica;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Participante;

/**
 *
 * @author Rodrigo
 * @deprecated - Substituir por AvaliacaoReacaoDAO
 */
@Repository("avaliacaoDao")
public class AvaliacaoDAO{
	protected EntityManager entityManager;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ModuloDAO moduloDao;	
	@Autowired
	private ParticipanteDAO participanteDao;
	public AvaliacaoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	public Avaliacao find(Long id) {
		return entityManager.find(Avaliacao.class, id);
	}
	
	@Transactional
	public void persist(Avaliacao Avaliacao) {
		entityManager.persist(Avaliacao);
	}
	
	@Transactional
	public void merge(Avaliacao Avaliacao) {
		entityManager.merge(Avaliacao);
	}

	@Transactional
	public void remove(Avaliacao Avaliacao) {
		entityManager.remove(Avaliacao);
	}

	@SuppressWarnings("unchecked")
	public List<Avaliacao> findAll() {
		return entityManager.createNamedQuery("Avaliacao.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Avaliacao> findByEventoAndParticipante(Evento evento, Participante participante) {
		return entityManager.createNamedQuery("Avaliacao.findByEventoAndParticipante").setParameter("evento", evento).setParameter("participante", participante).getResultList();
	}
	
	public Avaliacao findById(Long codigo) {
		try {
			return (Avaliacao) entityManager.createNamedQuery("Avaliacao.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {			
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public List<Avaliacao> findByModulo(Modulo modulo) {
		return entityManager.createNamedQuery("Avaliacao.findByModulo").setParameter("modulo", modulo).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Avaliacao> findByParticipanteCpf(String cpf) {
		return entityManager.createNamedQuery("Avaliacao.findByParticipanteCpf").setParameter("cpf", cpf).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Generica> findNaoRealizadasByParticipanteCpf(String cpf) {	

		return entityManager.createNativeQuery(
				"(SELECT evento.id as evento_id, tipo_evento.descricao || ' ' || evento.titulo || ' (' || evento.data_inicio_realizacao || ' a ' || evento.data_fim_realizacao || ')' as evento, modulo.id as modulo_id, modulo.titulo as modulo, participante.id as participante_id, participante.nome as participante FROM participante " +
				"INNER JOIN inscricao ON (inscricao.participante_id = participante.id) " +
				"INNER JOIN evento ON (evento.id = inscricao.evento_id) " +
				"INNER JOIN modulo ON (evento.id = modulo.evento_id) " +
				"INNER JOIN tipo_evento ON (evento.tipo_id = tipo_evento.id) " +
				"WHERE inscricao.confirmada = 'S' and SYSDATE > evento.data_fim_realizacao + 1 AND SYSDATE <= evento.data_fim_realizacao + 31 and participante.cpf = '" + cpf + "' " +
				"AND evento.tipo_id = 1 )" +
				"MINUS " +
				"(SELECT evento.id as evento_id,tipo_evento.descricao || ' '|| evento.titulo || ' (' || evento.data_inicio_realizacao || ' a ' || evento.data_fim_realizacao || ')' as evento, modulo.id as modulo_id, modulo.titulo as modulo, participante.id as participante_id, participante.nome as participante FROM avaliacao " +
				"INNER JOIN evento ON (evento.id = avaliacao.evento_id) " +
				"INNER JOIN modulo ON (modulo.id = avaliacao.modulo_id) " +
				"INNER JOIN tipo_evento ON (evento.tipo_id = tipo_evento.id) " +
				"INNER JOIN participante ON (participante.id = avaliacao.participante_id) " +
				"WHERE participante.cpf = '"+ cpf +"')").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Generica> findNaoRealizadasByParticipanteCpfAndEvento(String cpf, String evento) {
		return entityManager.createNativeQuery(
				"(SELECT evento.id as evento_id, evento.titulo as evento, modulo.id as modulo_id, modulo.titulo as modulo, instrutor.id as instrutor_id, instrutor.nome as instrutor, participante.id as participante_id, participante.nome as participante FROM participante " +
				"INNER JOIN inscricao ON (inscricao.participante_id = participante.id) " +
				"INNER JOIN evento ON (evento.id = inscricao.evento_id) " +
				"INNER JOIN modulo ON (evento.id = modulo.evento_id) " +
				"INNER JOIN modulo_instrutor ON (modulo_instrutor.modulo_id = modulo.id) " +
				"INNER JOIN instrutor ON (instrutor.id = modulo_instrutor.instrutor_id) " +
				"WHERE inscricao.confirmada = 'S' and participante.cpf = '"+ cpf +"' and evento.id = "+ evento + ") " +
				"MINUS " +
				"(SELECT evento.id as evento_id, evento.titulo as evento, modulo.id as modulo_id, modulo.titulo as modulo, instrutor.id as instrutor_id, instrutor.nome as instrutor, participante.id as participante_id, participante.nome as participante FROM avaliacao " +
				"INNER JOIN evento ON (evento.id = avaliacao.evento_id) " +
				"INNER JOIN modulo ON (modulo.id = avaliacao.modulo_id) " +
				"INNER JOIN instrutor ON (instrutor.id = avaliacao.instrutor_id) " +
				"INNER JOIN participante ON (participante.id = avaliacao.participante_id) " +
				"WHERE participante.cpf = '"+ cpf +"' and evento.id = "+ evento + ")").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Avaliacao> findAvaliacaoByCriteria(Long eventoId, Long moduloId, Long participanteId, Date data_cadastro1, Date data_cadastro2) {
		
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
		
		Criteria aux = sess.createCriteria(Avaliacao.class);
		if (eventoId != 0) {
			aux.add(Restrictions.eq("eventoId", eventoDao.findById(eventoId)));
		}
		if (moduloId != 0) {
			aux.add(Restrictions.eq("moduloId", moduloDao.findById(moduloId)));
		}
		if (participanteId != 0) {
			aux.add(Restrictions.eq("participanteId", participanteDao.findById(participanteId)));
		}
		if (data_cadastro1 != null) {
			aux.add(Restrictions.ge("dataCadastro", dataInicio));
		}
		if (data_cadastro2 != null) {
			aux.add(Restrictions.le("dataCadastro", dataFim));
		}		
		
		aux.createCriteria("eventoId", "e").addOrder(Order.desc("e.dataInicioPrevisto"));
		
	    return aux.list();
	    
	}
}