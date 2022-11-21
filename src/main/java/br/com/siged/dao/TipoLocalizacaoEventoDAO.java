package br.com.siged.dao;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.TipoLocalizacaoEvento;
import br.com.siged.entidades.TipoLocalizacaoModalidade;

/**
 * 
 * @author Rodrigo
 */
@Repository("tipolocalizacaoeventoDao")
public class TipoLocalizacaoEventoDAO {
	protected EntityManager entityManager;

	public TipoLocalizacaoEventoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public TipoLocalizacaoEvento find(Long id) {
		return entityManager.find(TipoLocalizacaoEvento.class, id);
	}

	@Transactional
	public void persist(TipoLocalizacaoEvento TipoLocalizacaoEvento) {
		entityManager.persist(TipoLocalizacaoEvento);
	}

	@Transactional
	public void merge(TipoLocalizacaoEvento TipoLocalizacaoEvento) {
		entityManager.merge(TipoLocalizacaoEvento);
	}

	@Transactional
	public void remove(TipoLocalizacaoEvento TipoLocalizacaoEvento) {
		entityManager.remove(TipoLocalizacaoEvento);
	}

	@SuppressWarnings("unchecked")
	public List<TipoLocalizacaoEvento> findAll() {
		return entityManager.createNamedQuery("TipoLocalizacaoEvento.findAll").getResultList();
	}
	
	public List<TipoLocalizacaoEvento> findAllByModalidade(TipoLocalizacaoModalidade ... tipos) {
		List<TipoLocalizacaoModalidade> list = Arrays.asList(tipos);
		return entityManager
				.createQuery("SELECT t FROM TipoLocalizacaoEvento t WHERE t.tipoLocalizacaoModalidade IN (:tipos) ORDER BY t.descricao ASC", TipoLocalizacaoEvento.class)
				.setParameter("tipos", list)
				.getResultList();
	}
	
	public TipoLocalizacaoEvento findById(Long codigo) {
		try {
			return (TipoLocalizacaoEvento) entityManager.createNamedQuery("TipoLocalizacaoEvento.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}