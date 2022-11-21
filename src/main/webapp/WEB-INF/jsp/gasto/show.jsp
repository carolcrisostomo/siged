<%@ include file="../includes.jsp" %>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <style type="text/css" media="screen">   
    @import url("<c:url value="/static/styles/style.css"/>");
  </style> 
<title><spring:message code="gasto.label" /></title>
</head>
<body>
	
  		<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
  		<h1><spring:message code="default.button.show.label" /> - <spring:message code="gasto.label" /></h1>
    	<c:url var="url" value="/gasto/${gasto.id}" />
    	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message code="gasto.evento.label" />:</label>
				</td>
				<td valign="top" class="value">${gasto.eventoId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="tipoGasto"><spring:message code="gasto.tipoGasto.label" />:</label>
				</td>
				<td valign="top" class="value">${gasto.tipoId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="valor"><spring:message code="gasto.valor.label" />:</label>
				</td>
				<td valign="top" class="value"><fmt:formatNumber type="NUMBER" minFractionDigits="2" maxFractionDigits="2">${gasto.valor}</fmt:formatNumber>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="fonteGasto"><spring:message code="gasto.fonteGasto.label" />:</label>
				</td>
				<td valign="top" class="value">${gasto.fonteId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="numeroEmpenho"><spring:message code="gasto.numeroEmpenho.label" />:</label>
				</td>
				<td valign="top" class="value">${gasto.numeroEmpenho}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="dataEmpenho"><spring:message code="gasto.dataEmpenho.label" />:</label>
				</td>
				<td valign="top" class="value"><fmt:formatDate pattern="dd/MM/yyyy" value="${gasto.dataEmpenho}"/>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="numeroProcesso"><spring:message code="gasto.numeroProcesso.label" />:</label>
				</td>
				<td valign="top" class="value">${gasto.numeroProcesso}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="participante"><spring:message code="gasto.participante.label" />:</label>
				</td>
				<td valign="top" class="value">${gasto.participanteId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="instrutor"><spring:message code="gasto.instrutor.label" />:</label>
				</td>
				<td valign="top" class="value">${gasto.instrutor}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="observacao"><spring:message code="gasto.observacao.label" />:</label>
				</td>
				<td valign="top" class="value">${gasto.observacao}
				</td>
			</tr>
	</tbody>
		</table>
		<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/gasto/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>

				</div>
			</div>
	</div>
	
</div>
</body>
</html>
