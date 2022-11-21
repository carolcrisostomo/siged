package br.com.siged.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.entidades.Desempenho;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Participante;

/**
 *
 * @author Marcelo
 */
@Repository("desempenhoDao")
public class DesempenhoDAO {
	protected EntityManager entityManager;
	@Autowired
	private SetorDAO setorDao;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ModuloDAO moduloDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	
	public DesempenhoDAO() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Desempenho find(Long id) {
		return entityManager.find(Desempenho.class, id);
	}

	@Transactional
	public void persist(Desempenho desempenho) {
		entityManager.persist(desempenho);
	}
	
	@Transactional
	public Desempenho persistAndFlush(Desempenho desempenho) {
		entityManager.persist(desempenho);
		entityManager.flush();
		return desempenho;
	}
	
	@Transactional
	public void merge(Desempenho Desempenho) {
		entityManager.merge(Desempenho);
	}

	@Transactional
	public void remove(Desempenho Desempenho) {
		entityManager.remove(Desempenho);
	}

	@SuppressWarnings("unchecked")
	public List<Desempenho> findByEvento(Evento evento) {
		return entityManager.createNamedQuery("Desempenho.findByEvento").setParameter("evento", evento).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Desempenho> findByEventoId(Long eventoId) {
		return entityManager.createNamedQuery("Desempenho.findByEventoId").setParameter("eventoId", eventoId).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Desempenho> findByModulo(Modulo modulo) {
		return entityManager.createNamedQuery("Desempenho.findByModulo").setParameter("modulo", modulo).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Desempenho> findByEventoAndParticipanteAndParcial(Evento evento, Participante participante) {
		return entityManager.createNamedQuery("Desempenho.findByEventoAndParticipanteAndParcial").setParameter("evento", evento).setParameter("participante", participante).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Desempenho> findByEventoAndParticipante(Evento evento, Participante participante) {
		return entityManager.createNamedQuery("Desempenho.findByEventoAndParticipante").setParameter("evento", evento).setParameter("participante", participante).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Desempenho> findByEventoAndParticipanteAndAprovado(Evento evento, Participante participante, String aprovado) {
		return entityManager.createNamedQuery("Desempenho.findByEventoAndParticipanteAndAprovado").setParameter("evento", evento).setParameter("participante", participante).setParameter("aprovado", aprovado).getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<Desempenho> findByEventoAndSetor(Evento evento, String setor) {
		return entityManager.createNamedQuery("Desempenho.findByEventoAndSetor").setParameter("evento", evento).setParameter("setor", setorDao.find(Long.parseLong(setor))).getResultList();
	}
	
	@Transactional
	public void deleteByEventoId(Long evento_id) {
		entityManager.createNativeQuery("DELETE FROM desempenho WHERE evento_id = " + evento_id).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<Desempenho> findNotaAndFrequenciaByEventoId(Evento evento) {
		
		String sql = "SELECT rownum AS id, e.id evento_id, m.id modulo_id, i.participante_id participante_id," +
				" DECODE(n.nota, NULL, '-', n.nota) nota," +
				" TO_CHAR((SELECT COUNT(*) FROM frequencia_participante fp INNER JOIN frequencia f ON f.id = fp.frequencia_id WHERE fp.participante_id = i.participante_id AND f.evento_id = e.id AND f.modulo_id = m.id)) frequencia," +
				" TO_CHAR(0) AS aprovado," +
				" TO_CHAR(0) AS parcial" + 
				" FROM evento e" +
				" INNER JOIN modulo m ON m.evento_id = e.id" +
				" INNER JOIN inscricao i ON i.evento_id = e.id" +
				" INNER JOIN participante p ON p.id = i.participante_id" +
				" LEFT JOIN nota n ON (n.evento_id = e.id AND n.modulo_id = m.id AND n.participante_id = i.participante_id)" +
				" WHERE e.id = :evento AND i.confirmada = 'S' ORDER BY p.nome, m.id";
				
		List<Desempenho> desempenhoList = entityManager.createNativeQuery(sql, Desempenho.class).setParameter("evento", evento).getResultList(); 
		
		return desempenhoList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Desempenho> findFrequenciaByEventoId(Evento evento) {
		
		String sql = "SELECT rownum AS id, i.evento_id AS evento_id, m.id AS modulo_id, i.participante_id AS participante_id" +
				", '-' AS nota" +
				", to_char((SELECT Count(*) FROM frequencia_participante" +
				" INNER JOIN frequencia ON frequencia.id = frequencia_participante.frequencia_id" +
				" WHERE frequencia_participante.participante_id = i.participante_id" +
				" AND frequencia.evento_id = i.evento_id" +
				" AND modulo_id = m.id)) AS frequencia" +
				", to_char(0) AS aprovado" +
				", to_char(0) AS parcial" +
				" FROM evento e" +
				" INNER JOIN inscricao i ON i.evento_id = e.id" +
				" INNER JOIN participante p ON p.id = i.participante_id" +
				" INNER JOIN modulo m ON m.evento_id = e.id" +
				" WHERE e.id = :evento " +
				" AND i.confirmada = 'S'" +
				" ORDER BY p.nome, m.id";
		
		return entityManager.createNativeQuery(sql, Desempenho.class).setParameter("evento", evento).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Desempenho> findNotaAndFrequenciaByModuloId(Modulo modulo) {

		String frequenciaSubQuery = "SELECT COUNT(*) FROM frequencia_participante fp INNER JOIN frequencia f ON f.id = fp.frequencia_id WHERE fp.participante_id = i.participante_id AND f.modulo_id = m.id";
		String sql = "SELECT rownum id," +
				" m.evento_id evento_id," +
				" m.id modulo_id," +
				" i.participante_id participante_id," +
				" COALESCE(n.nota, '-') nota," +
				" TO_CHAR((" + frequenciaSubQuery + ")) frequencia," +
				" TO_CHAR(0) aprovado," +
				" TO_CHAR(1) parcial" +
				" FROM modulo m" +
				" INNER JOIN inscricao i ON i.evento_id = m.evento_id" +
				" INNER JOIN participante p ON i.participante_id = p.id" + 
				" LEFT JOIN nota n ON (n.modulo_id = m.id AND n.participante_id = i.participante_id)" +
				" WHERE m.id = :modulo_id AND i.confirmada = 'S' ORDER BY p.nome, m.id";
		
		/**
		 * JPA não estava fazendo o cast corretamente, provavelmente por algum conflito no field.
		 * Então a princípio a solução foi forçar o cast de Object para Desempenho diretamente
		 * 
		 * List<Desempenho> desempenhoList = entityManager.createNativeQuery(sql, Desempenho.class).setParameter("modulo_id", modulo.getId()).getResultList();
		 */
		List<Object[]> desempenhoListObject = entityManager.createNativeQuery(sql).setParameter("modulo_id", modulo.getId()).getResultList();
		
		List<Desempenho> desempenhoFinalList = new ArrayList<>();
		for(Object[] obj : desempenhoListObject) {
			Desempenho desempenho = new Desempenho();
			desempenho.setEventoId(eventoDao.findById(((BigDecimal)obj[1]).longValue()));
			desempenho.setModuloId(moduloDao.findById(((BigDecimal)obj[2]).longValue()));
			desempenho.setParticipanteId(participanteDao.findById(((BigDecimal)obj[3]).longValue()));
			desempenho.setNota((String)obj[4]);
			desempenho.setFrequencia((String)obj[5]);
			desempenhoFinalList.add(desempenho);
		}
		
		return desempenhoFinalList;
	}
	
	public boolean isApuracaoParcial(Evento evento) {
		String hql = "Select d From Desempenho d Where d.eventoId = :evento and d.parcial = 1";
		
		List<Desempenho> parcial = entityManager.createQuery(hql, Desempenho.class).setParameter("evento", evento).setMaxResults(1).getResultList();
		if(parcial.size() > 0)
			return true;
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> countAprovadosByEvento(Evento evento) {
		
		String sql = "SELECT p.id," +
				" TO_CHAR(DECODE(SUM(DECODE(d.aprovado, 'S', 1, 0)), :modulo_qtd, 'S', 'N')) AS aprovado" +
				" FROM desempenho d" +
				" INNER JOIN participante p ON p.id = d.participante_id" +
				" WHERE d.evento_id = :evento" +
				" AND (d.parcial = 0 OR d.parcial IS NULL)" +
				" GROUP BY p.id, p.nome" +
				" ORDER BY p.nome";
		
		return entityManager.createNativeQuery(sql).setParameter("evento", evento).setParameter("modulo_qtd", evento.getModuloList().size()).getResultList();
		
	}
	
	public Desempenho findById(Long codigo) {
		try {
			return (Desempenho) entityManager.createNamedQuery("Desempenho.findById").setParameter("id", codigo).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
}