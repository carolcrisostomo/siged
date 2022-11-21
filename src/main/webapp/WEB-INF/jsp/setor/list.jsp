<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="setor.label" /></title>
</head>
<body>
<spring:message code="setor.sigla.label" var="coluna1" />
<spring:message code="setor.descricao.label" var="coluna2" />
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1><spring:message code="setor.label" /></h1>

<div class="list"><display:table uid="setor" name="${setores}"
	defaultsort="1" defaultorder="ascending" pagesize="10" requestURI="">
	<c:url var="url" value="/setor/${setor.id}" />
	<display:column property="sigla" sortable="true"
		title="${coluna1}" maxLength="30" />
	<display:column property="descricao" sortable="true"
		title="${coluna2}" maxLength="80" />
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
