package br.com.siged.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.siged.entidades.Modalidade;
import br.com.siged.filtro.RelatorioFiltro;
import br.com.siged.util.Util;

@Repository("relatorioDao")
public class RelatorioDAO {
	
	protected EntityManager entityManager;	
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Autowired
	private Util util;
	
	/**
	 * Metodo para realizar a consulta dos parametos base para calcular os indicadores outros
	 * @param relatorioFiltro
	 * @return Retorna um Map<String, BigDecimal> com os parametros base para os indicadores outros. Retorna null caso a consulta falhe
	 * As chaves do map são 'cargahorariacursada', 'totalparticipantes', 'totaloportunidades' e 'totalsetorescomparticipante'
	 */
	public Map<String, BigDecimal> getParametrosParaIndicadoresOutros(RelatorioFiltro relatorioFiltro){
		StringBuilder consulta = new StringBuilder("SELECT NVL(SUM(e.carga_horaria), 0) as cargahorariacursada, "
				+ "COUNT(DISTINCT p.id) as totalparticipantes, "
				+ "COUNT(i.id) as totaloportunidades, "
				+ "COUNT(DISTINCT p.setor_id) totalsetorescomparticipante "
				+ "FROM inscricao i "
				+ "INNER JOIN participante p 	 ON i.participante_id = p.id "
				+ "INNER JOIN evento e  		 ON i.evento_id = e.id "
				+ "LEFT JOIN sapjava_entidade entidade ON p.orgao_id = entidade.identidade "
				+ "WHERE i.CONFIRMADA            = 'S' ");

		if(relatorioFiltro.getTipoParticipanteId() != null && relatorioFiltro.getTipoParticipanteId() != 0)
			consulta.append("AND p.TIPO_ID                 = " + relatorioFiltro.getTipoParticipanteId() + " ");
		if(relatorioFiltro.getAdministracaoPublica().equals("estadual"))
			consulta.append("AND entidade.TPENTIDADEESFERA = 2 ");
		else if(relatorioFiltro.getAdministracaoPublica().equals("municipal"))
			consulta.append("AND entidade.TPENTIDADEESFERA = 3 ");
		else if (relatorioFiltro.getAdministracaoPublica().equals("demais casos"))
			consulta.append("AND (entidade.TPENTIDADEESFERA NOT IN (2,3) OR p.orgao_id is null) ");	
		if(relatorioFiltro.getTipoEventoId() != null && relatorioFiltro.getTipoEventoId() != 0)
			consulta.append("AND e.TIPO_ID                 = " + relatorioFiltro.getTipoEventoId() + " ");
		
		/*
		 * DONE: __Modalidade Evento: Definir qual será a Modalidade do Evento na consulta (SQL)
		 * @deprecated Consulta agora verifica se o evento é presencial por meio do módulo
		consulta.append("AND e.MODALIDADE_ID           = " + relatorioFiltro.getModalidadeId() + " ");
		 */
		if(relatorioFiltro.getModalidadeId() != null && relatorioFiltro.getModalidadeId() != 0) {
			String condicionalModalidadeDoEvento = "";
			if(relatorioFiltro.getModalidadeId().equals(Modalidade.PRESENCIAL))
				condicionalModalidadeDoEvento = "AND e.id IN (SELECT e.id from modulo m INNER JOIN evento e on m.evento_id = e.id WHERE m.modalidade_id = 1) ";
			else if( relatorioFiltro.getModalidadeId().equals(Modalidade.EAD)) {
				condicionalModalidadeDoEvento = "AND e.id NOT IN (SELECT e.id from modulo m INNER JOIN evento e on m.evento_id = e.id WHERE m.modalidade_id = 1) "
						+ "AND e.id NOT IN (SELECT e.id from evento e INNER JOIN modulo m on m.evento_id = e.id WHERE m.evento_id is NULL) ";
			}
				
			
			consulta.append(condicionalModalidadeDoEvento);
		}
			
		
		if(relatorioFiltro.getDataInicio1() != null)
			consulta.append("AND e.DATA_INICIO_REALIZACAO >= TO_DATE('" + util.formataData(relatorioFiltro.getDataInicio1()) + "', 'dd/MM/yyyy')");
		if(relatorioFiltro.getDataInicio2() != null)
			consulta.append("AND e.DATA_INICIO_REALIZACAO <= TO_DATE('" + util.formataData(relatorioFiltro.getDataInicio2()) + "', 'dd/MM/yyyy')");
		
		Object[] result = (Object[]) entityManager.createNativeQuery(consulta.toString()).getSingleResult();
		Map<String, BigDecimal> resultMap = new HashMap<String, BigDecimal>();
		
		
		resultMap.put("cargahorariacursada", (BigDecimal) result[0]);
		resultMap.put("totalparticipantes", (BigDecimal) result[1]);
		resultMap.put("totaloportunidades", (BigDecimal) result[2]);
		resultMap.put("totalsetorescomparticipante", (BigDecimal) result[3]);
		
		return resultMap;
		
	}	
	

}
