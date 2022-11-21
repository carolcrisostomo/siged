<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="cidade.label" /></title>
</head>
<body>
<div class="nav"><span class="menuButton"><a
	href="<c:url value="/cidade/"/>" class="list"><spring:message
	code="default.button.list.label" /></a></span></div>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1><spring:message code="default.add.label" /> - <spring:message
	code="cidade.label" /></h1>

<c:url var="url" value="/cidade" /> <form:form action="${url}"
	method="POST" modelAttribute="cidade">
	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="uf"><spring:message
					code="cidade.uf.label" />:</label></td>

				<td><form:select path="ufId" items="${listaUfs}"
					itemLabel="descricao" itemValue="id" /> <form:errors path="ufId"
					cssClass="error" /></td>
				<td valign="top" class="name">(*) Campos Obrigatórios</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="nome"><spring:message
					code="cidade.descricao.label" />:</label></td>

				<td><form:input cssStyle="width:400px" maxlength="255"
					path="descricao" onkeyup="javascript:this.value=this.value.toUpperCase();" size="30" />* <form:errors path="descricao"
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
