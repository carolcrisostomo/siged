package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.Pais;
/**
 *
 * @author Rodrigo
 */
@Repository("paisDao")
public class PaisDAO {
	protected EntityManager entityManager;

	public PaisDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Pais find(Long id) {
		return entityManager.find(Pais.class, id);
	}

	@Transactional
	public void persist(Pais Pais) {
		entityManager.persist(Pais);
	}

	@Transactional
	public void merge(Pais Pais) {
		entityManager.merge(Pais);
	}

	@Transactional
	public void remove(Pais Pais) {
		entityManager.remove(Pais);
	}

	@SuppressWarnings("unchecked")
	public List<Pais> findAll() {
		List<Pais> paises = entityManager.createNamedQuery("Pais.findAll").getResultList();
		for(int i = 0; i < paises.size(); i++) {
			if(paises.get(i).getId() == 1) {
				Pais br = paises.get(i);
				paises.remove(i);
				paises.add(0, br);
			}
		}
		


		return paises;
	}
	
	public Pais findById(Long codigo) {
		try{
			return (Pais) entityManager.createNamedQuery("Pais.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

}
