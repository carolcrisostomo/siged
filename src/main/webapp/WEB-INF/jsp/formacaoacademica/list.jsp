<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="formacaoAcademica.label" /></title>
</head>
<body>


<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<spring:message code="formacaoAcademica.descricao.label" var="coluna1" />
<h1><spring:message code="formacaoAcademica.label" /></h1>


		<sec:authorize ifNotGranted="ROLE_ADMINISTRADORCONS">
			<a class="btn btn-primary btn-sm"
				href="<c:url value="/formacaoacademica/form"/>"> <i
				class="bi bi-plus-circle"></i> <spring:message
					code="default.add.label" />
			</a>

		</sec:authorize>

<div class="list"><display:table uid="formacaoacademica"
	name="${formacoesacademicas}" defaultsort="1" defaultorder="ascending"
	pagesize="10" requestURI="">
	<c:url var="url" value="/formacaoacademica/${formacaoacademica.id}" />
	<display:column property="descricao" sortable="true"
		title="${coluna1}"
		maxLength="80" />
	<display:column class="crudlist">
		<form:form action="${url}" method="GET">
			<input alt="<spring:message code="default.button.show.label" />"
				src="<c:url value="/static/images/show.png"/>"
				title="<spring:message code="default.button.show.label" />"
				type="image"
				value="<spring:message code="default.button.show.label" />" />
		</form:form>
	</display:column>
	<sec:authorize ifNotGranted="ROLE_ADMINISTRADORCONS" >
		<display:column class="crudlist">
			<form:form action="${url}/form" method="GET">
				<input alt="<spring:message code="default.button.edit.label" />"
					src="<c:url value="/static/images/update.png"/>"
					title="<spring:message code="default.button.edit.label" />"
					type="image"
					value="<spring:message code="default.button.edit.label" />" />
			</form:form>
		</display:column>
		<display:column class="crudlist">
			<form:form action="${url}" method="DELETE">
				<input alt="<spring:message code="default.button.delete.label" />"
					src="<c:url value="/static/images/delete.png"/>"
					title="<spring:message code="default.button.delete.label" />"
					type="image"
					value="<spring:message code="default.button.delete.label" />"
					onclick="return confirm('<spring:message code="default.areyousure2.message" />');" />
			</form:form>
		</display:column>
	</sec:authorize>

</display:table></div>
</div>
</body>
</html>
