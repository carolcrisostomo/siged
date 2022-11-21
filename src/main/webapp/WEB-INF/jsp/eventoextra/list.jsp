<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="eventoExtra.label" /></title>
</head>
<body>

<script type="text/javascript">
	window.onload = function() {
		var evento = document.getElementById("curso");
		evento.onkeyup = function() {
			this.value = this.value.toUpperCase();
		};
	}
</script>

<sec:authorize ifAnyGranted="ROLE_SERVIDOR">
	<div class="nav">
	<span class="menuButton">
	<a href="<c:url value="/eventoextra/form"/>" class="create"><spring:message
		code="default.add.label" /></a>
	</span>
	</div>
</sec:authorize>

<div class="body">
<spring:message code="eventoExtra.solicitante.label" var="coluna1" />
<spring:message code="eventoExtra.curso.label" var="coluna2" />
<spring:message code="eventoExtra.dataInicio.label" var="coluna3" />
<spring:message code="eventoExtra.dataFim.label" var="coluna4" />
<spring:message code="eventoExtra.chefe.label" var="coluna5" />
<spring:message code="eventoExtra.dataIndicacao.label" var="coluna6" />
<spring:message code="eventoExtra.indicada.label" var="coluna7" />
<spring:message code="eventoExtra.usadaEmInscricao.label" var="coluna8" />

<sec:authorize ifAnyGranted="ROLE_SERVIDOR">
	<h1><spring:message code="eventoExtra.minhas.label" /></h1>
</sec:authorize>

<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
	<h1><spring:message code="eventoExtra.label" /></h1>
</sec:authorize>

<% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
            <div class="filter">
            <c:url var="url" value="/eventoextra/search" />
            <form:form action="${url}" method="GET" modelAttribute="eventoextrafiltro">
              <table>
                <tbody>
                <sec:authorize ifNotGranted="ROLE_SERVIDOR">
                  <tr>
                    <td>
                    	<label for="servidorId"><spring:message code="eventoExtra.solicitante.label"/>:</label>
                    </td>
                    <td>
                    	<form:select path="servidor" id="servidorId">
                    		<form:option value="0">TODOS</form:option>
                    		<form:options items="${servidorList}" itemValue="id" itemLabel="nomeCpf"/> 
                    	</form:select>	
                    </td>
                  </tr>
                 </sec:authorize>
                 <sec:authorize ifAnyGranted="ROLE_SERVIDOR">
					<form:hidden path="cpf" value="${sessionScope.PARTICIPANTE.cpf}"/>
                 </sec:authorize>
                  <tr>
                    <td>
                      <label for="curso">Evento:</label>
                    </td>
                    <td valign="top" class="value">
                      <form:input id="curso" path="curso" size="47"/>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <label for="dataInicio">Período:</label>
                    </td>
                    <td valign="top" class="value">
                      <form:input id="data1" path="data1" alt="date"/>
                       a <form:input id="data2" path="data2" alt="date"/>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <label for="indicada">Indicada:</label>
                    </td>
                    <td valign="top" class="value">
                      <form:select path="indicada">
                      <form:option value="0">TODOS</form:option>
                      <form:options items="${SNList}"/>
                      </form:select>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <input id="filtrar" type="submit" class="botao"
						value="Filtrar" />
                    </td>
                    <td></td>
                  </tr>

                </tbody>


              </table>
            </form:form>
            </div>

<div class="list"><display:table export="true" uid="eventoextra"
	name="${eventosextras}" defaultsort="3" defaultorder="descending"
	pagesize="10" requestURI="">
	<c:url var="url" value="/eventoextra/${eventoextra.id}" />
	   <!-- display:column property="solicitanteId" sortable="true"
		title="coluna1}" maxLength="80" -->
	<display:column property="solicitanteId" sortable="true"
		title="${coluna1}"
		maxLength="80" /> 
	<display:column property="curso" sortable="true"
		title="${coluna2}"
		maxLength="80" />
	<display:column property="dataInicio" sortable="true"
		title="${coluna3}"
		maxLength="50" format="{0,date,dd/MM/yyyy}" />
	<display:column property="dataFim" sortable="true"
		title="${coluna4}"
		maxLength="50" format="{0,date,dd/MM/yyyy}" />
	
	<display:column sortable="true"	title="${coluna5}"	maxLength="50" >
		
		<c:if test="${eventoextra.chefeId.id == 0}">
			Não exigido
		</c:if>
		<c:if test="${eventoextra.chefeId.id != 0}">
			${eventoextra.chefeId}
		</c:if>
		
	</display:column>	
	
	<display:column property="dataIndicacao" sortable="true"
		title="${coluna6}" format="{0,date,dd/MM/yyyy}"
		maxLength="80" />	
	<display:column property="indicada" sortable="true"
		title="${coluna7}"
		maxLength="80" />
	<c:if test="${fn:length(eventoextra.inscricaoList) == 0 }">
		<display:column sortable="true" title="${coluna8}" maxLength="80" value="N"/>
	</c:if>
	<c:if test="${fn:length(eventoextra.inscricaoList) > 0 }">
		<display:column sortable="true" title="${coluna8}" maxLength="80" value="S"/>
	</c:if>		
	<display:column class="crudlist" media="html"
	>
		<form:form action="${url}" method="GET">
			<input alt="<spring:message code="default.button.show.label" />"
				src="<c:url value="/static/images/show.png"/>"
				title="<spring:message code="default.button.show.label" />"
				type="image"
				value="<spring:message code="default.button.show.label" />" />
		</form:form>
	</display:column >
	<sec:authorize ifNotGranted="ROLE_ADMINISTRADORCONS" >
		<display:column class="crudlist" media="html">
			<c:if test="${eventoextra.indicada != 'S' }">
			<form:form action="${url}/form" method="GET">
				<input alt="<spring:message code="default.button.edit.label" />"
					src="<c:url value="/static/images/update.png"/>"
					title="<spring:message code="default.button.edit.label" />"
					type="image"
					value="<spring:message code="default.button.edit.label" />" />
			</form:form>
			</c:if>
		</display:column>
		<display:column class="crudlist" media="html">
			<c:if test="${fn:length(eventoextra.inscricaoList) == 0 }">
			<form:form action="${url}" method="DELETE">
				<input alt="<spring:message code="default.button.delete.label" />"
					src="<c:url value="/static/images/delete.png"/>"
					title="<spring:message code="default.button.delete.label" />"
					type="image"
					value="<spring:message code="default.button.delete.label" />"
					onclick="return confirm('<spring:message code="default.areyousure2.message" />');" />
			</form:form>
			</c:if>
		</display:column>
	</sec:authorize>
</display:table></div>
</div>
</body>
</html>
