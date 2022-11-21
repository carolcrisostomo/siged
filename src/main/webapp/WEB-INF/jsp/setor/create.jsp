<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="setor.label" /></title>
</head>
<body>
<div class="nav"><span class="menuButton"><a
	href="<c:url value="/setor/"/>" class="list"><spring:message
	code="default.button.list.label" /></a></span></div>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1><spring:message code="default.add.label" /> - <spring:message
	code="setor.label" /></h1>

<c:url var="url" value="/setor" /> <form:form action="${url}"
	method="POST" modelAttribute="setor">
	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="sigla"><spring:message
					code="setor.sigla.label" />:</label></td>

				<td><form:input cssStyle="width:100px" maxlength="255"
					path="sigla" size="30" /> <form:errors path="sigla"
					cssClass="error" /></td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="descricao"><spring:message
					code="setor.descricao.label" />:</label></td>

				<td><form:input cssStyle="width:400px" maxlength="50"
					path="descricao" size="30" />* <form:errors path="descricao"
					cssClass="error" /></td>
			</tr>

		</tbody>
	</table>
	</div>
	<div class="buttons"><input id="criar" type="submit" class="save"
		value="<spring:message code="default.add.label" />" /></div>
</form:form></div>
</body>
</html>
