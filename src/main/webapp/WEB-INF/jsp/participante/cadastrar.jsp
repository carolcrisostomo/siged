<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="participante.label" /></title>
</head>
<body>

<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1><spring:message code="default.add.label" /> - <spring:message
	code="participante.label" /></h1>

<div class="message">Não encontramos seu cadastro no sistema do RH, Por favor, nos ajude a completar seu cadastro.</div>

<c:url var="url" value="/participante/cadastrar" /> 
<form:form action="${url}" method="POST" modelAttribute="participante">
	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="tipoPublicoAlvo"><spring:message
					code="participante.tipoPublicoAlvo.label" />:</label></td>
				<td><form:select path="tipoId" items="${tipoPublicoAlvoList}"
					itemLabel="descricao" itemValue="id" /></td>
				<td valign="top" class="name">(*) Campos Obrigatórios</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="nome"><spring:message
					code="participante.nome.label" />:</label></td>
				<td><form:input cssStyle="width:250px" maxlength="100"
					path="nome"  size="100" value="${usuario.nome}" readonly="true"/>* <form:errors path="nome"
					cssClass="error" /></td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="entidade"><spring:message
					code="participante.entidade.label" />:</label></td>
				<td><form:input cssStyle="width:250px" maxlength="100"
					path="entidade" size="100" /> <form:errors path="entidade"
					cssClass="error" /></td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="cpf"><spring:message
					code="participante.cpf.label" />:</label></td>
				<td><form:input cssStyle="width:110px" maxlength="50"
					path="cpf" size="30" alt="cpf" value="${usuario.cpf}" readonly="true"/>* <form:errors path="cpf" cssClass="error" />
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="formacaoAcademica"><spring:message
					code="participante.formacao.label" />:</label></td>
				<td><form:select path="formacaoId"
					items="${formacaoAcademicaList}" itemLabel="descricao"
					itemValue="id" /></td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="nivelEscolaridade"><spring:message
					code="participante.escolaridade.label" />:</label></td>
				<td><form:select path="escolaridadeId"
					items="${nivelEscolaridadeList}" itemLabel="descricao"
					itemValue="id" /></td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="email"><spring:message
					code="participante.email.label" />:</label></td>
				<td><form:input cssStyle="width:250px" maxlength="50"
					path="email" size="30" /> <form:errors path="email"
					cssClass="error" /></td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="telefone"><spring:message
					code="participante.telefone.label" />:</label></td>
				<td><form:input cssStyle="width:110px" maxlength="50"
					path="telefone" size="30" alt="telefone"/> <form:errors path="telefone"
					cssClass="error" /></td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="celular"><spring:message
					code="participante.celular.label" />:</label></td>
				<td><form:input cssStyle="width:110px" maxlength="50"
					path="celular" size="30" alt="celular"/> <form:errors path="celular"
					cssClass="error" /></td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="setor"><spring:message
					code="participante.setor.label" />:</label></td>
				<td>
					<form:select path="setorId">
						<form:option value="0">Não se Aplica</form:option>
						<form:options items="${setorList}" itemLabel="descricao" itemValue="id" />
					</form:select>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="observacao"><spring:message
					code="participante.observacao.label" />:</label></td>

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
