<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="nota.label" /></title>
</head>
<body>
	<div class="body">
		<% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
		<spring:message code="nota.evento.label" var="coluna1" />
		<spring:message code="nota.modulo.label" var="coluna2" />
		<spring:message code="nota.participante.label" var="coluna3" />
		<spring:message code="nota.nota.label" var="coluna4" />
		<spring:message code="frequencia.label" var="coluna5" />
				
		<div class="list" style="max-height:350px; overflow:auto; border-right: 1px solid #ddd">
			<%-- pagesize="50" --%>
			<display:table uid="nota" name="${desempenhos}"	defaultsort="1" defaultorder="ascending" requestURI="">
				<display:column property="eventoId" title="Evento" maxLength="80" />
				<display:column property="moduloId" title="M�dulo" maxLength="80" />
				<display:column property="participanteId" title="Participante" maxLength="80" />
				<display:column property="nota" title="Nota" maxLength="80" />
				<display:column property="frequencia" title="Freq(%)" maxLength="80" />
				<display:column property="aprovado" title="Aprovado" maxLength="80" />
			</display:table>
		</div>		
		
		<c:if test="${parcial}">
			<h1>Apura��o parcial at� o presente momento!</h1>
		</c:if>
		<c:if test="${!parcial}">
			<br />
			<h1>Desempenho geral no evento</h1>
		
			<br />
			<div class="list" style="max-height:350px; overflow:auto; border-right: 1px solid #ddd">
				<%-- pagesize="10" --%> 
				<display:table uid="pais" name="${desempenhoFinal}" defaultsort="1" defaultorder="ascending" requestURI="">
					<display:column property="participanteId" sortable="false" title="Participante" maxLength="80" />
					<display:column property="aprovado" sortable="false" title="Aprovado" maxLength="80" />
				</display:table>				
			</div>
			<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS,ROLE_CHEFE">
				<div class="paginateButtons">
					Total de participantes: <c:out value="${fn:length(desempenhoFinal)}"/> <br />
					<c:if test="${fn:length(desempenhoFinal) > 0}">		
							Total de aprovados: <c:out value="${totalDeAprovados}"/> <br />
							Taxa de aproveitamento: <c:out value="${taxaDeAproveitamento}"/> %
					</c:if>
				</div>
			</sec:authorize>
		</c:if>		
	</div>
</body>
</html>
