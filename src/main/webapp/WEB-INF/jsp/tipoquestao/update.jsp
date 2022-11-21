<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="default.button.edit.label" /> - <spring:message code="tipoquestao.label" /></title>
</head>
<body>
<div class="nav">
	<span class="menuButton">
		<a href="<c:url value="/tipoquestao/"/>" class="list"><spring:message code="default.button.list.label" /></a>
	</span>
	<span class="menuButton">
		<a href="<c:url value="/tipoquestao/form"/>" class="create"><spring:message code="default.add.label" /></a>
	</span>
</div>
<div class="body">
	<% if (request.getParameter("mensagem")!=null) {%>
		<div class="message"><%= request.getParameter("mensagem")%></div>
	<% } %>
	<h1><spring:message code="default.button.edit.label" /> - <spring:message code="tipoquestao.label" /></h1>
	<c:url var="url" value="/tipoquestao/${tipoquestao.id}" />
	
	<form:form action="${url}" method="PUT" modelAttribute="tipoquestao">
		<div class="dialog">
			<table>
				<tbody>
					<tr class="prop">
						<td valign="top" class="name">
							<label for="descricao"><spring:message code="tipoquestao.descricao.label" />:</label>
						</td>
						<td>
							<form:input cssStyle="width:250px" maxlength="255" path="descricao" size="30" />
							<form:errors path="descricao" cssClass="error" />
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="buttons"><input id="atualizar" type="submit" class="edit" value="<spring:message code="default.button.save.label" />" /></div>
		<form:hidden path="id" />
	</form:form>
</div>
</body>
</html>
