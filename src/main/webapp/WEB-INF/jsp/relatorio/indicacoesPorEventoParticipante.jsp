<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1>Relat?rio de Indica??es por Evento/Participante</h1>
<c:url var="url" value="/relatorio/indicacoesPorEventoParticipante/" /> 
<form:form action="${url}" method="POST" modelAttribute="relIndicacoesPorEventoParticipante">
	<div class="filter">
	<table>
		<tbody>
		<tr>
			<td><label for="cpf">CPF</label></td>
				<td valign="top" class="cpf"><form:input path="cpf" alt="cpf" id="cpf" /></td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="periodorealizacao">Per?odo Realiza??o</label></td>
				<td valign="top"><form:input path="dataInicio1" id="data1" alt="date" /> a 
				<form:input path="dataInicio2" id="data2" alt="date"/></td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message
					code="evento.evento.label" /></label></td>
				<td valign="top">
				<form:select path="eventoId" id="eventoId">
					<form:option value="0">TODOS</form:option>
					<form:options items="${eventoList}" itemLabel="nome" itemValue="id" />
				</form:select>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="evento">Servidor Chefe</label></td>
				<td valign="top">
				<form:select path="participanteId" id="participante_nome">
					<form:option value="0">TODOS</form:option>
					<form:options items="${participanteList}" itemLabel="nomeCpf" itemValue="id" />
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