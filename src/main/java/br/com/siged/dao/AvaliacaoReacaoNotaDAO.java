package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.AvaliacaoReacaoNota;
import br.com.siged.entidades.Instrutor;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Questao;
import br.com.siged.entidades.TipoQuestao;

@Repository("avaliacaoreacaonotaDao")
public class AvaliacaoReacaoNotaDAO {

	protected EntityManager entityManager;
	
	public AvaliacaoReacaoNotaDAO(){
		
	}
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager){
		this.entityManager = entityManager;
	}
	
	public AvaliacaoReacaoNota find(Long id){
		return this.entityManager.find(AvaliacaoReacaoNota.class, id);
	}
	
	@Transactional
	public void persist(AvaliacaoReacaoNota avaliacaoReacaoNota){
		this.entityManager.persist(avaliacaoReacaoNota);
	}
	
	@Transactional
	public void merge(AvaliacaoReacaoNota avaliacaoReacaoNota){
		this.entityManager.merge(avaliacaoReacaoNota);
	}
	
	@Transactional
	public void remove(AvaliacaoReacaoNota avaliacaoReacaoNota){
		this.entityManager.remove(avaliacaoReacaoNota);
	}
	
	@SuppressWarnings("unchecked")
	public List<AvaliacaoReacaoNota> findAll(){
		String hql = "SELECT arn FROM AvaliacaoReacaoNota arn";
		return this.entityManager.createQuery(hql).getResultList();
	}
	
	public AvaliacaoReacaoNota findById(Long id){
		String hql = "SELECT arn FROM AvaliacaoReacaoNota arn WHERE arn.id = :id";
		try{
			return (AvaliacaoReacaoNota) this.entityManager.createQuery(hql).setParameter("id", id).getSingleResult();
		} catch (Exception e){
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AvaliacaoReacaoNota> findByCriteria(Modulo modulo, Instrutor instrutor, Questao questao, String nota, TipoQuestao tipoQuestao){
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria arn = sess.createCriteria(AvaliacaoReacaoNota.class);
		Criteria ar = arn.createCriteria("avaliacaoReacao", "ar", Criteria.INNER_JOIN);
		
		Criteria m = ar.createCriteria("modulo", "m", Criteria.INNER_JOIN);
		Criteria i = arn.createCriteria("instrutor", "i", Criteria.LEFT_JOIN);
		
		Criteria q = arn.createCriteria("questao", "q", Criteria.INNER_JOIN);
		Criteria tq = q.createCriteria("tipoQuestao", "tq", Criteria.INNER_JOIN);
		
		Criteria n = arn.createCriteria("notaQuestao", "n", Criteria.INNER_JOIN);
		
		
		if(modulo != null && modulo.getId() > 0){
			m.add(Restrictions.eq("id", modulo.getId()));
		}
		if(instrutor != null && instrutor.getId() > 0){
			i.add(Restrictions.eq("id", instrutor.getId()));
		}
		if (questao != null && questao.getId() > 0) {
			q.add(Restrictions.eq("id", questao.getId()));
		}
		if (nota != null) {
			n.add(Restrictions.like("descricao", "%" + nota + "%"));
		}
		if(tipoQuestao != null && tipoQuestao.getId() > 0) {
			tq.add(Restrictions.eq("id", tipoQuestao.getId()));
		}
		
		return arn.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<AvaliacaoReacaoNota> findByNotaAndEvento(String nota, Long eventoId){
		
		Session sess = (Session) entityManager.getDelegate();
		
		Criteria arn = sess.createCriteria(AvaliacaoReacaoNota.class);
		Criteria e = arn.createCriteria("avaliacaoReacao", "ar", Criteria.INNER_JOIN)
				.createCriteria("modulo", "m", Criteria.INNER_JOIN)
				.createCriteria("eventoId", "e", Criteria.INNER_JOIN);
		
		Criteria n = arn.createCriteria("notaQuestao", "n", Criteria.INNER_JOIN);
		
		if(eventoId != null && eventoId > 0)
			e.add(Restrictions.eq("id", eventoId));

		if (nota != null && !nota.isEmpty())
			n.add(Restrictions.eq("descricao", nota));
		
		return arn.list();
	}
	
}
