package br.com.siged.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.siged.entidades.Parametro;

@Repository("parametroDao")
public class ParametroDAO {

	protected EntityManager entityManager;
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Parametro findByNome(String nome) {
		try {			
			return (Parametro) entityManager.createNamedQuery("Parametro.findByNome").setParameter("nome", nome).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

}
