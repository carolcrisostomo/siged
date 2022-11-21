<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1>Relatório de Participantes por Evento</h1>
<c:url var="url" value="/relatorio/participantesPorEvento/" /> 
<form:form action="${url}" method="POST" modelAttribute="relParticipantesPorEvento">
	<div class="filter">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="participantes"><spring:message
					code="participante.tipoPublicoAlvo.label" /></label></td>
				<td valign="top">
				<form:select path="publicoAlvoId" id="tipo_publico_alvo">
					<form:option value="0">TODOS</form:option>
					<form:options items="${tipoPublicoAlvoList}" itemLabel="descricao" itemValue="id" />
				</form:select>
				</td>
			</tr>		
			<tr class="prop">
				<td valign="top" class="name"><label for="participantes"><spring:message
					code="participante.evento.label" /></label></td>
				<td valign="top">
				<form:select path="eventoId" id="eventoid">
					<form:option value="0">TODOS</form:option>
					<form:options items="${eventoList}" itemLabel="nome" itemValue="id" />
				</form:select>
				</td>
			</tr>		
			
			
			      <tr>
                    <td>
                      <input id="filtrar" type="submit" class="botao"
						value="Filtrar" />
                    </td>
                    <td></td>
                  </tr>
                  
		</tbody>
	</table>
	</div>
</form:form></div>
</body>
</html>