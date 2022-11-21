<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1>Relatório de Indicadores Setoriais</h1>
<c:url var="url" value="/relatorio/indicadoresSetoriais/" /> 
<form:form action="${url}" method="POST" modelAttribute="relIndicadoresSetoriais">
	<div class="filter">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="periodotrimestre">Período Trimestral</label></td>
				<td valign="top"><form:input path="trimestre_data1" id="trimestre_data1" alt="date" /> a 
				<form:input path="trimestre_data2" id="trimestre_data2" alt="date"/></td>
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