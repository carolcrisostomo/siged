<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="programa.label" /></title>
</head>
<body>


<div class="body">
<c:if test="${param.mensagem != null}">
	<div class="message">${param.mensagem}</div>
</c:if>
<c:if test="${param.mensagemErro != null}">
	<div class="messageErro">${param.mensagemErro}</div>
</c:if>
<spring:message code="programa.descricao.label" var="coluna1" />
<h1><spring:message code="programa.label" /></h1>


		<sec:authorize ifNotGranted="ROLE_ADMINISTRADORCONS">
			<a class="btn btn-primary btn-sm"
				href="<c:url value="/programa/form"/>"> <i
				class="bi bi-plus-circle"></i> <spring:message
					code="default.add.label" />
			</a>

		</sec:authorize>

<div class="list"><display:table uid="programa"
	name="${programas}" defaultsort="1" defaultorder="ascending"
	pagesize="10" requestURI="">
	<c:url var="url" value="/programa/${programa.id}" />
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
