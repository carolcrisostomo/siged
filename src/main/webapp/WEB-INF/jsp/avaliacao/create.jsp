<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="avaliacao.label" /></title>
<spring:url value="/avaliacao/procuraModulo" var="procuraModuloUrl" />
<spring:url value="/avaliacao/procuraParticipante" var="procuraParticipanteUrl" />
<spring:url value="/avaliacao/procuraInstrutor" var="procuraInstrutorUrl" />
</head>
<body>
	<div class="nav">
		<span class="menuButton"><a href="<c:url value="/avaliacao/"/>"
			class="list"><spring:message code="default.button.list.label" /></a></span>
	</div>
	<div class="body">
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>
		<h1>
			<spring:message code="default.add.label" />
			-
			<spring:message code="avaliacao.label" />
		</h1>
		<c:url var="url" value="/avaliacao/formadmin" />
		<form:form action="${url}" method="GET" modelAttribute="avaliacao">
			<div class="dialog">
				<table>
					<tbody>
						
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">(*)
								Campos Obrigatórios</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name">
								<label for="evento"><spring:message	code="avaliacao.evento.label" />:</label>
							</td>
							<td>
								<form:select path="eventoId" id="eventoId" onchange="updateModulo('${procuraModuloUrl}', 'eventoId', 'moduloId');updateParticipante('${procuraParticipanteUrl}', 'eventoId', 'participanteId')">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${eventoListAvaliacoes}" itemLabel="nome" itemValue="id" />									
								</form:select>* <br /><form:errors path="eventoId" cssClass="error" />
							</td>

						</tr>
						
						<tr class="prop">
							<td valign="top" class="name">
								<label for="modulo"><spring:message code="avaliacao.modulo.label" />:</label>
							</td>

							<td><form:select path="moduloId" id="moduloId"	onchange="updateInstrutor('${procuraInstrutorUrl}', 'moduloId', 'instrutorId')">
									<form:option value="0">Selecione...</form:option>
								</form:select>* <form:errors path="moduloId" cssClass="error" />
							</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name">
								<label for="participante"><spring:message code="avaliacao.participante.label" />:</label>
							</td>

							<td><form:select path="participanteId" id="participanteId"
									itemValue="id" itemLabel="nomeCpf">
									<form:option value="0">Selecione...</form:option>
								</form:select>
								<form:errors path="participanteId" cssClass="error" />
							</td>
						</tr>

					</tbody>
				</table>
			</div>
			
			<div class="buttons">
				<input id="criar" type="submit" class="save" value="<spring:message code="default.avaliar.label" />" />
			</div>
			
		</form:form>
	</div>
</body>
</html>
