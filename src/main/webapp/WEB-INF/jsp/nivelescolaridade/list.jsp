<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="nivelEscolaridade.label" /></title>
</head>
<body>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<spring:message code="nivelEscolaridade.descricao.label" var="coluna1" />
<h1><spring:message code="nivelEscolaridade.label" /></h1>

<div class="list"><display:table uid="nivelescolaridade"
	name="${niveisescolaridade}" defaultsort="1" defaultorder="ascending"
	pagesize="10" requestURI="">
	<c:url var="url" value="/nivelescolaridade/${nivelescolaridade.id}" />
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

</display:table></div>
</div>
</body>
</html>
