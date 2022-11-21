package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.externas.Municipio;
import br.com.siged.entidades.externas.UfMunicipio;
/**
 *
 * @author Rodrigo
 */
@Repository("UfMunicipioDao")
public class UfMunicipioDAO {
	protected EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Municipio find(Long id) {
		return entityManager.find(Municipio.class, id);
	}

	@Transactional
	public void persist(UfMunicipio ufMunicipio) {
		entityManager.persist(ufMunicipio);
	}

	@Transactional
	public void merge(UfMunicipio ufMunicipio) {
		entityManager.merge(ufMunicipio);
	}

	@Transactional
	public void remove(UfMunicipio ufMunicipio) {
		entityManager.remove(ufMunicipio);
	}

	@SuppressWarnings("unchecked")
	public List<UfMunicipio> findAll() {
		return entityManager.createNamedQuery("UfMunicipio.findAll").getResultList();
	}
	
	public UfMunicipio findByUf(String uf) {
		try {
			return (UfMunicipio) entityManager.createNamedQuery("UfMunicipio.findByUf").setParameter("uf", uf).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}