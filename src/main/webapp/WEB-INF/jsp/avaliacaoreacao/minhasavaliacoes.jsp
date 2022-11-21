<%@ include file="../includes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title><spring:message code="inscricao.minhas.label" /></title>
</head>
<body>
<div class="body">
	<% if (request.getParameter("mensagemErro")!=null) {%>
		<div class="messageErro"><%= request.getParameter("mensagemErro")%></div>
	<% } %>
	<% if (request.getParameter("mensagem")!=null) {%>
		<div class="message"><%= request.getParameter("mensagem")%></div>
	<% } %>
	<spring:message code="avaliacao.evento.label" var="coluna1" />
	<spring:message code="avaliacao.modulo.label" var="coluna2" />
	<spring:message code="avaliacao.instrutor.label" var="coluna3" />
	<spring:message code="avaliacao.participante.label" var="coluna4" />
	<h1>Minhas Avaliações (Reação)</h1>
	<br />
	<div class="list">
		<br />
		<h3>Não Realizadas</h3>
		<br />
		<display:table uid="avaliacao"
			name="${avaliacoesnaorealizadas}" defaultsort="1" defaultorder="ascending"
			pagesize="10" requestURI="">
			<c:url var="url" value="/avaliacaoreacao" />
			<display:column property="[1]" sortable="true" title="${coluna1}" maxLength="80" />
			<display:column property="[3]" sortable="true" title="${coluna2}" maxLength="80" />
			<display:column class="crudlist">
				<form:form action="${url}/formaluno/${avaliacao[0]}/${avaliacao[2]}/${avaliacao[4]}" method="GET">
					<input alt="<spring:message code="default.add.label" />"
						src="<c:url value="/static/images/skin/database_save.png"/>"
						title="<spring:message code="default.add.label" />"
						type="image"
						value="<spring:message code="default.add.label" />" />
				</form:form>
			</display:column>
		</display:table>
		<br /><br />
		<h3>Realizadas</h3>
		<br />
		<display:table uid="avaliacao2"
			name="${avaliacoes}" 
			pagesize="10" requestURI="">
			<c:url var="url" value="/avaliacaoreacao/${avaliacao2.id}" />
			<display:column property="modulo.eventoId" sortable="true" title="${coluna1}" maxLength="80" />
			<display:column property="modulo" sortable="true" title="${coluna2}" maxLength="80" />
			<display:column class="crudlist">
				<form:form action="${url}" method="GET">
					<input alt="<spring:message code="default.button.show.label" />"
						src="<c:url value="/static/images/show.png"/>"
						title="<spring:message code="default.button.show.label" />"
						type="image"
						value="<spring:message code="default.button.show.label" />" />
				</form:form>
			</display:column>
		</display:table>
		<br /><br />
	</div>
</div>
</body>
</html>
