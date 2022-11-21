<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="avaliacao.label" /></title>
<spring:url value="/avaliacao/procuraModulo"
	var="procuraModuloUrl" />
<spring:url value="/avaliacao/procuraParticipante"
	var="procuraParticipanteUrl" />
<spring:url value="/avaliacao/procuraInstrutor"
	var="procuraInstrutorUrl" />
</head>
<body>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1><spring:message code="default.add.label" /> - <spring:message
	code="avaliacao.label" /></h1>

<c:url var="url" value="/avaliacao" /> <form:form action="${url}"
	method="POST" modelAttribute="avaliacao">
	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message
					code="avaliacao.evento.label" />:</label></td>
				<td>
					${evento.titulo}
					<form:input path="eventoId" type="hidden" value="${evento.id}" />
				</td>
				<td valign="top" class="name">(*) Campos Obrigatórios</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="modulo"><spring:message
					code="avaliacao.modulo.label" />:</label></td>

				<td>
					<form:select path="moduloId" id="moduloId" onchange="updateInstrutor('${procuraInstrutorUrl}', 'moduloId', 'instrutorId')">
 						<form:option value="">Selecione...</form:option>
 						<form:options items="${modulosList}" itemLabel="nome" itemValue="id" />
 					</form:select>* <form:errors
					path="moduloId" cssClass="error" />
				</td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="instrutor"><spring:message
					code="avaliacao.instrutor.label" />:</label></td>

				<td>
					<form:select path="instrutorId">
						<form:option value="">Selecione...</form:option>
					</form:select>
				</td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="participante"><spring:message
					code="avaliacao.participante.label" />:</label></td>

				<td>
					${participante.nome}
					<form:input path="participanteId" type="hidden" value="${participante.id}" />
				</td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name" colspan="2">
				<h3>Instrutor</h3>
				</td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor1"><spring:message
					code="avaliacao.questaoInstrutor1.label" />:</label></td>

				<td><form:select path="questaoInstrutor1"
					items="${respostasQuestoes}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor2"><spring:message
					code="avaliacao.questaoInstrutor2.label" />:</label></td>

				<td><form:select path="questaoInstrutor2"
					items="${respostasQuestoes}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor3"><spring:message
					code="avaliacao.questaoInstrutor3.label" />:</label></td>

				<td><form:select path="questaoInstrutor3"
					items="${respostasQuestoes}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor4"><spring:message
					code="avaliacao.questaoInstrutor4.label" />:</label></td>

				<td><form:select path="questaoInstrutor4"
					items="${respostasQuestoes}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor5"><spring:message
					code="avaliacao.questaoInstrutor5.label" />:</label></td>

				<td><form:select path="questaoInstrutor5"
					items="${respostasQuestoes}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor6"><spring:message
					code="avaliacao.questaoInstrutor6.label" />:</label></td>

				<td><form:select path="questaoInstrutor6"
					items="${respostasQuestoes}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name" colspan="2">
				<h3>Evento/Módulo</h3>
				</td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoConteudo1"><spring:message
					code="avaliacao.questaoConteudo1.label" />:</label></td>

				<td><form:select path="questaoConteudo1"
					items="${respostasQuestoes}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoConteudo2"><spring:message
					code="avaliacao.questaoConteudo2.label" />:</label></td>

				<td><form:select path="questaoConteudo2"
					items="${respostasQuestoes}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoConteudo3"><spring:message
					code="avaliacao.questaoConteudo3.label" />:</label></td>

				<td><form:select path="questaoConteudo3"
					items="${respostasQuestoes}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label
					for="questaoAutoAvaliacao1"><spring:message
					code="avaliacao.questaoAutoAvaliacao1.label" />:</label></td>

				<td><form:select path="questaoAutoAvaliacao1"
					items="${respostasQuestoes}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label
					for="questaoAutoAvaliacao2"><spring:message
					code="avaliacao.questaoAutoAvaliacao2.label" />:</label></td>

				<td><form:select path="questaoAutoAvaliacao2"
					items="${respostasQuestoes}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="observacao"><spring:message
					code="avaliacao.observacao.label" />:</label></td>

				<td><form:textarea path="observacao" cols="70" rows="5" /> <form:errors
					path="observacao" cssClass="error" /></td>
			</tr>

		</tbody>
	</table>
	</div>
	<div class="buttons"><input id="criar" type="submit" class="save"
		value="<spring:message code="default.add.label" />" /></div>
</form:form></div>
</body>
</html>
