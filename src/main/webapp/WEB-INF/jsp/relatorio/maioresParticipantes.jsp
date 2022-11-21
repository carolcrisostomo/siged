<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>
<div class="nav"><span class="menuButton"><a
	href="<c:url value="/relatorio/maioresParticipantes/"/>" class="list"><spring:message
	code="default.button.list.label" /></a></span></div>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
	<h1>Relatório de Maiores Participantes</h1>
	<c:url var="url" value="/relatorio/maioresParticipantes/" /> 
	<form:form action="${url}" method="POST" modelAttribute="relMaioresParticipantes">
	<div class="filter">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="periodorealizacao">Período Realização</label></td>
				<td valign="top"><form:input path="dataInicio1" id="data1" alt="date" /> a 
				<form:input path="dataInicio2" id="data2" alt="date"/></td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message
					code="participante.setor.label" /></label></td>
				<td valign="top">
				<form:select path="setorId" id="setor">
					<form:option value="0">TODOS</form:option>
					<form:options items="${setorList}" itemLabel="descricao" itemValue="id" />
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