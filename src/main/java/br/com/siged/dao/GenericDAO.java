package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.siged.entidades.externas.Grupo;
import br.com.siged.entidades.externas.UsuarioSCA;

/**
 * 
 * @author Rodrigo
 */
@Repository("genericDao")
public class GenericDAO {
	protected EntityManager entityManager;

	public GenericDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {

		if (entityManager == null)
			throw new IllegalStateException("Erro");

		return entityManager;

	}

	@SuppressWarnings("unchecked")
	public List<UsuarioSCA> findUserByUsername(String username) {
		return entityManager
				.createNativeQuery(
						"(SELECT id, nome, login, senha, tipo, ativo, senhaexpirada, cpf, dataalteracao, datainclusao, email, observacao  FROM sca_usuario u WHERE u.ativo = 1 and UPPER(u.login) = UPPER(:user)) "
						+ "UNION ALL "
						+ "(SELECT id, nome, login, senha, tipo, ativo, senhaexpirada, cpf, dataalteracao, datainclusao, email, observacao FROM usuario u WHERE u.ativo = 1 and UPPER(u.login) = UPPER(:user))",
						UsuarioSCA.class)
				.setParameter("user", username).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Grupo> findAuthoritiesByUsername(String username) {
		try {
			return entityManager
				.createNativeQuery(
						 "SELECT DISTINCT u.id, 'ROLE_SERVIDOR' as nome, '11' as sistema FROM SCA_USUARIO u "
						 + "INNER JOIN SRH_TB_PESSOAL p ON (trim(u.cpf) = trim(p.cpf)) "
						 + "WHERE UPPER(u.login) = UPPER(:user)"
						,Grupo.class)
				.setParameter("user", username).getResultList();
		} catch (Exception e) {
			return null;
		}
	}

}
