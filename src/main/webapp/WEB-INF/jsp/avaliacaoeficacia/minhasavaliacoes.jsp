<%@ include file="../includes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="avaliacaoEficacia.listagem.label" /></title>
</head>
<body>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<spring:message code="avaliacaoEficacia.evento.label" var="coluna1" />
<spring:message code="avaliacaoEficacia.participante.label" var="coluna2" />
<spring:message code="avaliacaoEficacia.melhoria.label" var="coluna3" />
<spring:message code="avaliacaoEficacia.desempenhoServico.label" var="coluna4" />
<h1>Minhas Avaliações (Eficácia)</h1>
<br />
<div class="list">
<br />
<h3>Não Realizadas</h3>
<br />
<display:table uid="avaliacaoEficacia"	name="${avaliacoeseficacianaorealizadas}" defaultsort="1" 
				defaultorder="ascending" pagesize="10" requestURI="">
	
	<c:url var="url" value="/avaliacaoeficacia" />
	
	
	<display:column sortable="true"	title="${coluna1}" maxLength="80">
		${avaliacaoEficacia[5]}	 	
	 	(<fmt:formatDate value="${avaliacaoEficacia[0]}" pattern="dd/MM/yyyy"/>
	 		a
		<fmt:formatDate value="${avaliacaoEficacia[1]}" pattern="dd/MM/yyyy"/>)</display:column>
	
	
	<display:column sortable="true"	title="Período Realização" maxLength="80">
	 	<fmt:formatDate value="${avaliacaoEficacia[2]}" pattern="dd/MM/yyyy"/>
	 		a
		<fmt:formatDate value="${avaliacaoEficacia[3]}" pattern="dd/MM/yyyy"/></display:column>	
	
	
	<display:column property="[7]" sortable="true"
		title="${coluna2}"
		maxLength="80" />
	
	
	<display:column class="crudlist">
		<form:form action="${url}/formaluno/${avaliacaoEficacia[4]}/${avaliacaoEficacia[6]}" method="GET">
			<input alt="<spring:message code="default.add.label" />"
				src="<c:url value="../static/images/skin/database_save.png"/>"
				title="<spring:message code="default.add.label" />"
				type="image"
				value="<spring:message code="default.add.label" />" />
		</form:form>
	</display:column>
</display:table>
<br />
<br />
<h3>Realizadas</h3>
<br />
<display:table uid="avaliacaoeficacia"
	name="${avaliacoeseficacia}" 
	pagesize="10" requestURI="">
	<c:url var="url" value="/avaliacaoeficacia/${avaliacaoeficacia.id}" />
	<display:column property="eventoId" sortable="true"
		title="${coluna1}"
		maxLength="80" />
	<display:column property="participanteId" sortable="true"
		title="${coluna2}"
		maxLength="80" />
	<display:column property="melhoria" sortable="true"
		title="${coluna3}"
		maxLength="80" />
	<display:column property="desempenhoServico" sortable="true"
		title="${coluna4}"
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
	<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
	<display:column class="crudlist">
		<form:form action="${url}/form" method="GET">
			<input alt="<spring:message code="default.button.save.label" />"
				src="<c:url value="/static/images/update.png"/>"
				title="<spring:message code="default.button.save.label" />"
				type="image"
				value="<spring:message code="default.button.save.label" />" />
		</form:form>
	</display:column>
</sec:authorize>
</display:table>
<br />
<br />
</div>
</div>
</body>
</html>
