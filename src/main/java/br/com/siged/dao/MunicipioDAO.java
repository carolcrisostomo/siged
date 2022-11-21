package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.externas.Municipio;
/**
 *
 * @author Rodrigo
 */
@Repository("MunicipioDao")
public class MunicipioDAO {
	protected EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Municipio find(Long id) {
		return entityManager.find(Municipio.class, id);
	}

	@Transactional
	public void persist(Municipio municipio) {
		entityManager.persist(municipio);
	}

	@Transactional
	public void merge(Municipio municipio) {
		entityManager.merge(municipio);
	}

	@Transactional
	public void remove(Municipio municipio) {
		entityManager.remove(municipio);
	}

	@SuppressWarnings("unchecked")
	public List<Municipio> findAll() {
		return entityManager.createNamedQuery("Municipio.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Municipio> findByUfMunicipio(String ufMunicipio) {
		return entityManager.createNamedQuery("Municipio.findByUfMunicipio").setParameter("ufMunicipio", ufMunicipio).getResultList();
	}
	
	public Municipio findById(Long id) {
		try {
			return (Municipio) entityManager.createNamedQuery("Municipio.findById").setParameter("id", id).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}