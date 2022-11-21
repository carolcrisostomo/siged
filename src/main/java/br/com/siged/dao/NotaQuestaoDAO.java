package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.NotaQuestao;

@Repository("notaquestaoDao")
public class NotaQuestaoDAO {

	protected EntityManager entityManager;
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager){
		this.entityManager = entityManager;
	}
	
	public NotaQuestao find(Long id){
		return this.entityManager.find(NotaQuestao.class, id);
	}
	
	@Transactional
	public void persist(NotaQuestao notaQuestao){
		this.entityManager.persist(notaQuestao);
	}
	
	@Transactional
	public void merge(NotaQuestao notaQuestao){
		this.entityManager.merge(notaQuestao);
	}
	
	@Transactional
	public void remove(NotaQuestao notaQuestao){
		this.entityManager.remove(notaQuestao);
	}
	
	@SuppressWarnings("unchecked")
	public List<NotaQuestao> findAll(){
		String hql = "SELECT nq FROM NotaQuestao nq";
		return this.entityManager.createQuery(hql).getResultList();
	}
	
	public NotaQuestao findById(Long id){
		String hql = "SELECT nq FROM NotaQuestao nq WHERE nq.id = :id";
		try{
			return (NotaQuestao) this.entityManager
					.createQuery(hql)
					.setParameter("id", id)
					.getSingleResult();
		} catch(Exception e){
			return null;
		}
	}
}
