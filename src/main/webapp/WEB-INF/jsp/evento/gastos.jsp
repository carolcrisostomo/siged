<%@ include file="../includes.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="gasto.label" /></title>
</head>
<body>
	<div class="body">
		<% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
		<spring:message code="gasto.evento.label" var="coluna1" />
		<spring:message code="gasto.tipoGasto.label" var="coluna2" />
		<spring:message code="gasto.fonteGasto.label" var="coluna3" />
		<spring:message code="gasto.valor.label" var="coluna4" />
		<spring:message code="gasto.total.label" var="coluna5" />
		<!-- <h1><spring:message code="gasto.label" /></h1> -->
				
		<div class="list" style="max-height:423px; overflow:auto; border-right: 1px solid #ddd">
			<%-- pagesize="50" --%>
			<display:table uid="gasto" name="${gastos}" defaultsort="1" defaultorder="ascending" requestURI="">
				<c:url var="url" value="/gasto/${gasto.id}" />
				<display:column property="eventoId" title="${coluna1}" maxLength="80" />
				<display:column property="tipoId" title="${coluna2}" maxLength="80" />
				<display:column property="fonteId" title="${coluna3}" maxLength="80" />
				<display:column property="valor" title="${coluna4}" maxLength="50" format="{0, number, #,##0.00}" />
				<%-- <display:footer>
		  			<tr>
		  				<td style="font-weight: bold">${coluna5}</td>
		  				<td></td>
		  				<td></td>
		  				<td ><fmt:formatNumber type="NUMBER" value="${total}" maxFractionDigits ="2" minFractionDigits="2" /></td>
		   			</tr>
		  		</display:footer> --%>
			</display:table>
		</div>
		<c:if test="${fn:length(gastos) > 0}">		
			<div class="paginateButtons">Gasto total: R$ <fmt:formatNumber type="NUMBER" value="${total}" maxFractionDigits ="2" minFractionDigits="2" /></div>			
		</c:if>
	</div>
</body>
</html>
