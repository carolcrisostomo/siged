<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1>Cronograma</h1>
<c:url var="url" value="/relatorio/cronograma/" /> 
<form:form action="${url}" method="POST" modelAttribute="relCronograma">
	<div class="filter">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="periodoPrevisto">Ano Previsto</label></td>
				<td valign="top">
				<!--<form:input path="ano" id="ano" />-->
				<form:select path="ano" id="ano">
					<form:options items="${anoList}" />
				</form:select>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="publicoAlvo"><spring:message
					code="evento.publicoAlvo.label" /></label></td>
				<td valign="top">
				<form:select path="publicoAlvoId" id="evento_publico_alvo_id">
					<form:option value="0">TODOS</form:option>
					<form:options items="${tipoPublicoAlvoList}" itemLabel="descricao" itemValue="id" />
				</form:select>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="modalidade"><spring:message
					code="evento.modalidade.label" /></label></td>

				<td>
					<form:select path="modalidadeId" id="modalidadeId">
						<form:option value="0">TODOS</form:option>
						<form:options items="${modalidadeList}" itemLabel="descricao" itemValue="id" />
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