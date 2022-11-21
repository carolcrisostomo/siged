<%@ include file="../includes.jsp" %>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <style type="text/css" media="screen">   
    @import url("<c:url value="/static/styles/style.css"/>");
  </style> 
<title><spring:message code="eventoExtra.label" /></title>
</head>
<body>
  
    <div class="body">
	<h1><spring:message code="default.button.show.label" /> - <spring:message code="eventoExtra.label" /></h1>	
	<% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
   	<c:url var="url" value="/eventoextra/${eventoextra.id}" />
   	<div class="dialog">
	<form:form modelAttribute="eventoextra" >
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="cpf"><spring:message code="eventoExtra.solicitante.label" />:</label>
				</td>
				<td valign="top" class="value">${eventoextra.solicitanteId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="setor"><spring:message code="eventoExtra.setor.label" />:</label>
				</td>
				<td valign="top" class="value">${participante.setorId.descricao}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="curso"><spring:message code="eventoExtra.curso.label" />:</label>
				</td>
				<td valign="top" class="value">${eventoextra.curso}
				</td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="site"><spring:message code="eventoExtra.site.label" />:</label>
				</td>
				<td valign="top" class="value">${eventoextra.site}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="material"><spring:message code="eventoExtra.material2.label" />:</label>
				</td>
				<td valign="top" class="value"><a href="<c:url value="/eventoextra/material/${eventoextra.id}"/>" target="_blank">${eventoextra.materialNome}</a>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="dataInicio"><spring:message code="eventoExtra.dataInicio.label" />:</label>
				</td>
				<td valign="top" class="value"><fmt:formatDate pattern="dd/MM/yyyy" value="${eventoextra.dataInicio}"/>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="dataFim"><spring:message code="eventoExtra.dataFim.label" />:</label>
				</td>
				<td valign="top" class="value"><fmt:formatDate pattern="dd/MM/yyyy" value="${eventoextra.dataFim}"/>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="valor"><spring:message code="eventoExtra.valor2.label" />:</label>
				</td>
				<td valign="top" class="value"><fmt:formatNumber type="currency">${eventoextra.valor}</fmt:formatNumber>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="cargaHoraria"><spring:message code="eventoExtra.cargaHoraria.label" />:</label>
				</td>
				<td valign="top" class="value">${eventoextra.horario}
				</td>
			</tr>			
			<tr class="prop">
				<td valign="top" class="name"><label for="pais"><spring:message code="eventoExtra.pais.label" />:</label>
				</td>
				<td valign="top" class="value">${eventoextra.paisId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="uf"><spring:message code="eventoExtra.uf.label" />:</label>
				</td>
				<td valign="top" class="value">${eventoextra.municipio.ufMunicipio}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="cidade"><spring:message code="eventoExtra.cidade.label" />:</label>
				</td>
				<td valign="top" class="value">${eventoextra.municipio.nome}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="turno"><spring:message code="eventoExtra.turno.label" />:</label>
				</td>
				<td valign="top" class="value">${eventoextra.turnoString}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="provedor"><spring:message code="eventoExtra.provedor.label" />:</label>
				</td>
				<td valign="top" class="value">${eventoextra.provedor}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="justificativa"><spring:message code="eventoExtra.justificativa.label" />:</label>
				</td>
				<td valign="top" class="value">${eventoextra.justificativa}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="indicada"><spring:message code="eventoExtra.indicada.label" />:</label>
				</td>
				<td valign="top" class="value">${eventoextra.indicada}
				</td>
			</tr>
				<tr class="prop">
				<td valign="top" class="name"><label for="dataIndicada"><spring:message code="eventoExtra.dataIndicacao.label" />:</label>
				</td>
				<td valign="top" class="value"><fmt:formatDate pattern="dd/MM/yyyy" value="${eventoextra.dataIndicacao}"/>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name">Resp. Indica��o:
				</td>
				<td valign="top" class="value">${eventoextra.chefeId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="justificativa">Justificativa Chefe:</label>
				</td>
				<td valign="top" class="value">${eventoextra.justificativachefe}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="justificativa">Usada em Inscri��o:</label>
				</td>
				<td valign="top" class="value">
					<c:choose>
						<c:when test="${inscricao.solicitacaoId.id != null}">
							S (Evento: ${inscricao.eventoId.nome})
						</c:when>
						<c:otherwise>
							N
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			
			<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
			<tr class="prop">
				<td valign="top" class="name">
					<label for="observacaoIpc"><spring:message code="eventoExtra.observacaoIpc.label" />:</label>
				</td>
				<td>
					${eventoextra.observacaoIpc}
				</td>
			</tr>
			</sec:authorize>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="dataCadastro"><spring:message code="eventoExtra.dataCadastro.label" />:</label>
				</td>
				<td valign="top" class="value"><fmt:formatDate pattern="dd/MM/yyyy" value="${eventoextra.dataCadastro}"/>
				</td>
			</tr>

		</tbody>
		</table>
	</form:form>
	</div>

	<c:if test="${eventoextra.indicada=='N' && exibirBotoes}">
			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/eventoextra/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>

				</div>
			</div>
    </c:if>
  
</div>
</body>
</html>
