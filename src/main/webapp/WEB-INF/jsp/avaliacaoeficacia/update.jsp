<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="default.button.edit.label" /> - <spring:message
	code="avaliacaoEficacia.label" /></title>
<spring:url value="/avaliacaoeficacia/procuraParticipante"
	var="procuraParticipanteUrl" />
</head>
<body>
<div class="nav">
<sec:authorize ifAnyGranted="ROLE_CHEFE">
<span class="menuButton"><a href="<c:url value="/avaliacaoeficacia/minhasavaliacoes"/>" class="list"><spring:message code="avaliacaoEficacia.listagem.label" /></a></span>
</sec:authorize>
<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
<span class="menuButton"><a
	href="<c:url value="/avaliacaoeficacia/"/>" class="list"><spring:message
	code="default.button.list.label" /></a></span> <span class="menuButton"><a
	href="<c:url value="/avaliacaoeficacia/form"/>" class="create"><spring:message
	code="default.add.label" /></a></span>
</sec:authorize>
	</div>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1><spring:message code="default.button.edit.label" /> - <spring:message
	code="avaliacaoEficacia.label" /></h1>
<c:url var="url" value="/avaliacaoeficacia/${avaliacaoeficacia.id}" />
<form:form action="${url}" method="PUT"
	modelAttribute="avaliacaoeficacia">
	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message
					code="avaliacaoEficacia.evento.label" />:</label></td>

				<td>
					${evento}
					<form:hidden path="eventoId" value="${evento.id}" />
				</td>
				<td valign="top" class="name"></td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="participante"><spring:message
					code="avaliacaoEficacia.participante.label" />:</label></td>

				<td>
					${participante}
					<form:hidden path="participanteId" value="${participante.id}" />
				</td>
			</tr>			
			<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
				<tr class="prop">
					<td valign="top" class="name">
						<label for="responsavel">Respons�vel:</label>
					</td>
					<td>
						<form:select path="responsavel">
							<form:option value="0">Selecione...</form:option>
							<form:options items="${chefes}" itemValue="id" itemLabel="nome" />
						</form:select> 
					</td>
				</tr>
			</sec:authorize>
			<tr class="prop">
				<td valign="top" class="name"><label for="melhoria"><spring:message
					code="avaliacaoEficacia.melhoria.label" />:</label></td>

				<td><form:select path="melhoria">
						<form:option value="">Selecione...</form:option>
						<form:options items="${melhorias}" />
				 	</form:select>
				 <form:errors path="melhoria" cssClass="error" /></td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="desempenhoservico"><spring:message
					code="avaliacaoEficacia.desempenhoServico.label" />:</label></td>

				<td><form:select path="desempenhoServico">
				<form:option value="">Selecione...</form:option>
				<form:options items="${respostasQuestoes}" /> 
				</form:select>
				<form:errors path="desempenhoServico" cssClass="error" /></td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="observacao"><spring:message
					code="avaliacaoEficacia.observacao.label" />:</label></td>

				<td><form:textarea path="observacao" cols="70" rows="5" /> <form:errors
					path="observacao" cssClass="error" /></td>
			</tr>
		</tbody>
	</table>
	</div>
	<div class="buttons"><input id="atualizar" type="submit"
		class="edit"
		value="<spring:message code="default.button.save.label" />" /></div>
	<form:hidden path="id" />
	<form:hidden path="dataCadastro" />
</form:form></div>
</body>
</html>
