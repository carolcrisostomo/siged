<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="participante.label" /></title>
</head>
<body>
	<div class="body">
		<% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
		<spring:message code="participante.nome.label" var="coluna1" />
		<!-- <h1><spring:message code="participante.label" /></h1> -->
		            
		<div class="list" style="max-height:423px; overflow:auto; border-right: 1px solid #ddd">
			<%-- pagesize="169" --%>
			<display:table uid="participante" name="${participantes}" defaultsort="1" defaultorder="ascending" requestURI="">			
				<display:column property="participanteId" sortable="false" title="Participante" maxLength="80" />			
			</display:table>
		</div>
		
		<c:if test="${fn:length(participantes) > 0}">		
			<div class="paginateButtons">Total de participantes: <c:out value="${fn:length(participantes)}"/></div>			
		</c:if> 		
	</div>
</body>
</html>
