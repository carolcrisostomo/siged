<%@ include file="../includes.jsp"%>

<html>
<head>
<title><spring:message code="participante.justificativa.label" /></title>
</head>
<body>

	<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
		<c:url var="url" value="/evento/inscricaologado/${eventoId}" />
		<form:form action="${url}" method="POST" modelAttribute="inscricao">
			<div class="dialog">
				<table>
					<tbody>
					<tr class="prop">
						<td valign="top" class="name"><label for="justificativaParticipante"><spring:message
							code="participante.justificativa.label" />:</label></td>
		
						<td><form:textarea path="justificativaParticipante" cols="70" rows="5" /> <form:errors
							path="justificativaParticipante" cssClass="error" /></td>
					</tr>

					</tbody>
				</table>
			</div>
			<div class="buttons">
				<input id="atualizar" type="submit" class="edit" value="Confirmar" />
			</div>
		</form:form>
	</div>
</body>
</html>
