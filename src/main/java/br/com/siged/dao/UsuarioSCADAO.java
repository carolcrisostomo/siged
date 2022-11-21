package br.com.siged.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.externas.UsuarioSCA;

/**
 *
 * @author Rodrigo
 */
@Repository("usuarioDao")
public class UsuarioSCADAO {

	protected EntityManager entityManager;

	public UsuarioSCADAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public UsuarioSCA find(Long id) {
		return entityManager.find(UsuarioSCA.class, id);
	}

	@Transactional
	public void persist(UsuarioSCA Usuario) {
		entityManager.persist(Usuario);
	}

	@Transactional
	public void merge(UsuarioSCA Usuario) {
		entityManager.merge(Usuario);
	}

	@Transactional
	public void remove(UsuarioSCA Usuario) {
		entityManager.remove(Usuario);
	}

	@SuppressWarnings("unchecked")
	public List<UsuarioSCA> findAll() {
		return entityManager.createNamedQuery("UsuarioSCA.findAll").getResultList();
	}
	
	public UsuarioSCA findById(Long codigo) {
		try {
			return (UsuarioSCA) entityManager.createNamedQuery("UsuarioSCA.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {			
			return null;
		}
	}
	
	public UsuarioSCA findByCpf(String cpf) {
		try {
			return (UsuarioSCA) entityManager.createNamedQuery("UsuarioSCA.findByCpf").setParameter("cpf", cpf).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public UsuarioSCA findChefeSetor(Long setorId) {
		try {
			List<UsuarioSCA> usuario = entityManager
			.createNativeQuery(
					 "SELECT u.id, u.nome, u.login, u.senha, u.tipo, u.ativo, u.senhaexpirada, u.cpf, u.dataalteracao, u.datainclusao, u.email, u.observacao "
					 + "FROM SRH_TB_REPRESENTACAOFUNCIONAL rf "
					 + "INNER JOIN SAPJAVA_SETOR s ON (rf.IDSETOR = s.IDSETOR) "
					 + "INNER JOIN SRH_TB_FUNCIONAL f ON (rf.IDFUNCIONAL = f.ID) "
					 + "INNER JOIN SRH_TB_PESSOAL p ON (f.IDPESSOAL = p.ID) "
					 + "INNER JOIN SRH_TB_REPRESENTACAOSETOR rs ON (rf.IDSETOR = rs.IDSETOR) "
					 + "INNER JOIN SCA_USUARIO u ON (u.cpf = p.cpf) "
					 + "WHERE rf.IDREPRESENTACAOCARGO = rs.IDREPRESENTACAOCARGO and rs.HIERARQUIA=1 AND rf.FIM Is Null AND rs.FIM Is Null AND s.IDSETOR = :setorId ORDER BY u.id"
					, UsuarioSCA.class)
			.setParameter("setorId", setorId).getResultList();
			
			return usuario.get(0);
			
		} catch (Exception e) {			
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<UsuarioSCA> findResponsavelIndicacao() {
			return entityManager.createNativeQuery(
					"SELECT DISTINCT u.id, u.nome, u.login, u.senha, u.tipo,  u.ativo, u.senhaexpirada, u.cpf, u.dataalteracao, u.datainclusao, u.email, u.observacao" +
					" FROM sca_usuario u" +
					" INNER JOIN srh_TB_PESSOAL p ON u.cpf = p.cpf" +
					" INNER JOIN srh_TB_FUNCIONAL f ON p.id = f.idpessoal" +
					" INNER JOIN srh_TB_OCUPACAO o ON f.idocupacao = o.id" +
					" WHERE (f.status = 1 AND f.idsituacao = 1 AND f.ativofp = 1 AND f.doesaida IS NULL AND f.datasaida IS NULL AND f.idrepresentacaocargo IS NOT NULL)" +
					" OR (o.id IN (8, 9, 10) AND f.status = 1 AND f.idsituacao = 1 AND f.ativofp = 1 AND f.doesaida IS NULL AND f.datasaida IS NULL)" +
					" ORDER BY u.nome" 
					, UsuarioSCA.class).getResultList();
	}	
	
	@SuppressWarnings("unchecked")
	public List<UsuarioSCA> findServidores() {
			return entityManager.createNativeQuery(
					"SELECT DISTINCT u.id, u.nome, u.login, u.senha, u.tipo,  u.ativo, u.senhaexpirada, u.cpf, u.dataalteracao, u.datainclusao, u.email, u.observacao" +
					" FROM sca_usuario u" +
					" INNER JOIN srh_TB_PESSOAL p ON u.cpf = p.cpf" +
					" INNER JOIN srh_TB_FUNCIONAL f ON p.id = f.idpessoal" +
					" WHERE f.ATIVOFP = 1 AND (f.STATUS = 1 OR f.STATUS = 2) AND f.DATASAIDA IS NULL" +
					" ORDER BY u.nome" 
					, UsuarioSCA.class).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<UsuarioSCA> findServidorBySetorId(Long setorId) {
			return entityManager.createNativeQuery(
					"SELECT DISTINCT u.id, u.nome, u.login, u.senha, u.tipo,  u.ativo, u.senhaexpirada, u.cpf, u.dataalteracao, u.datainclusao, u.email, u.observacao" +
					" FROM sca_usuario u" +
					" INNER JOIN sapjava_membro m ON m.idmembro = u.id" +
					" INNER JOIN sapjava_setormembro sm ON sm.idmembro = m.idmembro" +
					" INNER JOIN sapjava_setor s ON s.idsetor = sm.idsetor" +
					" INNER JOIN srh_TB_PESSOAL p ON u.cpf = p.cpf" +
					" INNER JOIN srh_TB_FUNCIONAL f ON p.id = f.idpessoal" +
					" WHERE s.idsetor = :idSetor AND f.ATIVOFP = 1 AND (f.STATUS = 1 OR f.STATUS = 2) AND f.DATASAIDA IS NULL" +
					" ORDER BY u.nome" 
					, UsuarioSCA.class).setParameter("idSetor", setorId).getResultList();
	}	
	
	
	public UsuarioSCA findByLoginSCA(String login) {
		try {
			return (UsuarioSCA) entityManager.createNamedQuery("UsuarioSCA.findByLogin").setParameter("login", login).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}	
	
	public UsuarioSCA findByLogin(String login) {
		
		return (UsuarioSCA) entityManager
		.createNativeQuery(
				"(SELECT id, nome, login, senha, tipo, ativo, senhaexpirada, cpf, dataalteracao, datainclusao, email, observacao FROM sca_usuario u WHERE UPPER(u.login) = UPPER(:user)) " +
				"UNION ALL " +
				"(SELECT id, nome, login, senha, tipo, ativo, senhaexpirada, cpf, dataalteracao, datainclusao, email, observacao FROM usuario u WHERE UPPER(u.login) = UPPER(:user))", UsuarioSCA.class)
		.setParameter("user", login).getSingleResult();

	}
	
	@SuppressWarnings("unchecked")
	public List<UsuarioSCA> findBySetorId(Long setorId) {
			return entityManager.createNativeQuery(
					"SELECT DISTINCT u.id, u.nome, u.login, u.senha, u.tipo,  u.ativo, u.senhaexpirada, u.cpf, u.dataalteracao, u.datainclusao, u.email, u.observacao" +
					" FROM sca_usuario u" +
					" INNER JOIN sapjava_membro m ON m.idmembro = u.id" +
					" INNER JOIN sapjava_setormembro sm ON sm.idmembro = m.idmembro" +
					" INNER JOIN sapjava_setor s ON s.idsetor = sm.idsetor" +
					" WHERE s.idsetor = :idSetor" +
					" ORDER BY u.nome" 
					, UsuarioSCA.class).setParameter("idSetor", setorId).getResultList();
	}

	
}