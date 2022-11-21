package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.TipoQuestao;

@Repository("tipoquestaoDao")
public class TipoQuestaoDAO {

	protected EntityManager entityManager;
	
	public TipoQuestaoDAO(){
		
	}
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager){
		this.entityManager = entityManager;
	}
	
	public TipoQuestao find(Long id){
		return this.entityManager.find(TipoQuestao.class, id);
	}
	
	@Transactional
	public void persist(TipoQuestao tipoQuestao){
		this.entityManager.persist(tipoQuestao);
	}
	
	@Transactional
	public void merge(TipoQuestao tipoQuestao){
		this.entityManager.merge(tipoQuestao);
	}
	
	@Transactional
	public void remove(TipoQuestao tipoQuestao){
		this.entityManager.remove(tipoQuestao);
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoQuestao> findAll(){
		String hql = "SELECT tq FROM TipoQuestao tq";
		return this.entityManager.createQuery(hql).getResultList();
	}
	
	public TipoQuestao findById(Long id){
		String hql = "SELECT tq FROM TipoQuestao tq WHERE tq.id = :id";
		try{
			return (TipoQuestao) this.entityManager
					.createQuery(hql)
					.setParameter("id", id)
					.getSingleResult();
		} catch(Exception e){
			return null;
		}
	}
	
	public TipoQuestao findByDescricao(String descricao){
		String hql = "SELECT tq FROM TipoQuestao tq WHERE tq.descricao = :descricao";
		try{
			return (TipoQuestao) this.entityManager
					.createQuery(hql)
					.setParameter("descricao", descricao)
					.getSingleResult();
		} catch(Exception e){
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoQuestao> findAllExludeOne(Long tipoQuestaoId) {
		String hql = "SELECT tq FROM TipoQuestao tq WHERE tq.id != :id Order By tq.id";
		return this.entityManager.createQuery(hql)
				.setParameter("id", tipoQuestaoId)
				.getResultList();
	}
	
	public TipoQuestao findTipoInstrutor(){
		String hql = "SELECT tq FROM TipoQuestao tq WHERE tq.id = :id";
		try{
			return (TipoQuestao) this.entityManager
					.createQuery(hql)
					.setParameter("id", 1L)
					.getSingleResult();
		} catch(Exception e){
			return null;
		}
	}
	
	public TipoQuestao findTipoConteudo(){
		String hql = "SELECT tq FROM TipoQuestao tq WHERE tq.id = :id";
		try{
			return (TipoQuestao) this.entityManager
					.createQuery(hql)
					.setParameter("id", 2L)
					.getSingleResult();
		} catch(Exception e){
			return null;
		}
	}
	
	public TipoQuestao findTipoAutoAvaliacao(){
		String hql = "SELECT tq FROM TipoQuestao tq WHERE tq.id = :id";
		try{
			return (TipoQuestao) this.entityManager
					.createQuery(hql)
					.setParameter("id", 3L)
					.getSingleResult();
		} catch(Exception e){
			return null;
		}
	}
	
	public TipoQuestao findTipoLogistica(){
		String hql = "SELECT tq FROM TipoQuestao tq WHERE tq.id = :id";
		try{
			return (TipoQuestao) this.entityManager
					.createQuery(hql)
					.setParameter("id", 4L)
					.getSingleResult();
		} catch(Exception e){
			return null;
		}
	}
}
