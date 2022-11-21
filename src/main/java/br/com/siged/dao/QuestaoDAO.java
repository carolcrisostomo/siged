package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.Questao;

@Repository("questaoDao")
public class QuestaoDAO {
	
	protected EntityManager entityManager;
	
	public QuestaoDAO(){
		
	}
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager){
		this.entityManager = entityManager;
	}
	
	public Questao find(Long id){
		return this.entityManager.find(Questao.class, id);
	}
	
	@Transactional
	public void persist(Questao questao){
		this.entityManager.persist(questao);
	}
	
	@Transactional
	public void merge(Questao questao){
		this.entityManager.merge(questao);
	}
	
	@Transactional
	public void remove(Questao questao){
		this.entityManager.remove(questao);
	}
	
	@SuppressWarnings("unchecked")
	public List<Questao> findAll(){
		String hql = "SELECT q FROM Questao q JOIN q.tipoQuestao tp order by tp.id, q.ordem";
		return this.entityManager.createQuery(hql).getResultList();
	}
	
	public Questao findById(Long id){
		String hql = "SELECT q FROM Questao q WHERE q.id = :id";
		try{
			return (Questao) this.entityManager
					.createQuery(hql)
					.setParameter("id", id)
					.getSingleResult();
		} catch (Exception e){
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Questao> findByTipoQuestaoId(Long tipoQuestaoId){
		String hql = "SELECT q FROM Questao q JOIN q.tipoQuestao tp WHERE tp.id = :tpid order by q.ordem";
		return this.entityManager
				.createQuery(hql)
				.setParameter("tpid", tipoQuestaoId)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Questao> findByTipoQuestaoIdWithModalidades(Long tipoQuestaoId){
		String hql = "SELECT q FROM Questao q JOIN q.tipoQuestao tp WHERE tp.id = :tpid order by q.ordem";
		List<Questao> questoes = this.entityManager
				.createQuery(hql)
				.setParameter("tpid", tipoQuestaoId)
				.getResultList();
		for(Questao questao : questoes) {
			Hibernate.initialize(questao.getModalidades());
		}
		return questoes;
	}

}
