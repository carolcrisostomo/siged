package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.siged.entidades.externas.Localidade;

@Repository("localidadeDAO")
public class LocalidadeDAO {
	
	protected EntityManager entityManager;
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public Localidade find(Long identidade) {
		return entityManager.find(Localidade.class, identidade);
	}
	
	@SuppressWarnings("unchecked")
	public List<Localidade> findByMunicipios() {
		
		Query query = entityManager.createQuery("Select l from Localidade l where l.idLocalidade != 1");		
		return query.getResultList();
		
	}

}
