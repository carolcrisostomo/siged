<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="eventoRecomendar.label" /></title>
</head>
<body>



	<div class="body">
		
		<spring:message code="eventoRecomendar.titulo.label" var="coluna1" />
		<spring:message code="eventoRecomendar.objetivo.label" var="coluna2" />
		<spring:message code="eventoRecomendar.cargaHoraria.label" var="coluna3" />
		<spring:message code="eventoRecomendar.dataInicio.label" var="coluna4" />
		<spring:message code="eventoRecomendar.dataFim.label" var="coluna5" />
		<spring:message code="eventoRecomendar.setor.label" var="coluna6" />
		<spring:message code="eventoRecomendar.aprovado.label" var="coluna7" />
		<h1>
			<spring:message code="eventoRecomendar.label" />
		</h1>
		
		
		<sec:authorize ifNotGranted="ROLE_ADMINISTRADORCONS">
			<a class="btn btn-primary btn-sm"
				href="<c:url value="/eventorecomendar/form"/>"> <i
				class="bi bi-plus-circle"></i> <spring:message
					code="default.add.label" />
			</a>

		</sec:authorize>
		
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>		

		<div class="filter">
			<c:url var="url" value="/eventorecomendar/search" />
			<form:form action="${url}" method="GET"
				modelAttribute="eventorecomendarfiltro">
				<table>
					<tbody>

						<tr>
							<td><label for="curso">Evento</label></td>
							<td valign="top" class="value"><form:input maxlength="255" cssStyle="width:370px" id="titulo" 
									path="titulo" /></td>
						</tr>
						<tr>
							<td><label for="periodo">Período</label></td>
							<td valign="top" class="value"><form:input
									cssStyle="width:90px" id="data1" maxlength="255" path="data1"
									size="30" alt="date" /> a <form:input cssStyle="width:90px"
									id="data2" maxlength="255" path="data2" size="30" alt="date" />
							</td>
						</tr>
						<tr>
							<td><label for="setorId">Setor</label></td>
							<td valign="top" class="value"><form:select path="setor">
									<form:option value="0">TODOS</form:option>
									<form:options items="${setorList}" itemLabel="descricao"
										itemValue="id" />
								</form:select></td>
						</tr>
						<tr>
							<td><label for="aprovado">Aprovada</label></td>
							<td><form:select path="aprovado">
									<form:option value="0">TODOS</form:option>
									<form:options items="${SNList}" />
								</form:select> <form:errors path="aprovado" cssClass="error" /></td>
						</tr>
						<tr>
							<td>
							<button id="filtrar" type="submit"
									class="btn btn-primary btn-sm">Filtrar</button>
							</td>
							<td></td>
						</tr>

					</tbody>



				</table>
			</form:form>
		</div>
		<div class="list">
			<display:table uid="eventorecomendar" name="${eventosrecomendar}"
				defaultsort="1" defaultorder="ascending" pagesize="10" requestURI="">
				<c:url var="url" value="/eventorecomendar/${eventorecomendar.id}" />

				<display:column property="titulo" sortable="true" title="${coluna1}"
					maxLength="80" />
				<!--display:column property="objetivo" sortable="true" title="coluna2}" maxLength="80" -->
				<display:column property="cargaHoraria" sortable="true"
					title="${coluna3}" maxLength="80" />
				<display:column property="dataInicio" sortable="true" maxLength="50"
					format="{0,date,dd/MM/yyyy}" />
				<display:column property="dataFim" sortable="true"
					title="${coluna5}" maxLength="50" format="{0,date,dd/MM/yyyy}" />
				<display:column property="setorId" sortable="true"
					title="${coluna6}" maxLength="80" />
				<display:column property="aprovado" sortable="true"
					title="${coluna7}" maxLength="2" />
				<display:column class="crudlist">
					<form:form action="${url}" method="GET">
						<input alt="<spring:message code="default.button.show.label" />"
							src="<c:url value="/static/images/show.png"/>"
							title="<spring:message code="default.button.show.label" />"
							type="image"
							value="<spring:message code="default.button.show.label" />" />
					</form:form>
				</display:column>
				<sec:authorize ifNotGranted="ROLE_ADMINISTRADORCONS">
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
							<input
								alt="<spring:message code="default.button.delete.label" />"
								src="<c:url value="/static/images/delete.png"/>"
								title="<spring:message code="default.button.delete.label" />"
								type="image"
								value="<spring:message code="default.button.delete.label" />"
								onclick="return confirm('<spring:message code="default.areyousure2.message" />');" />
						</form:form>
					</display:column>
				</sec:authorize>
			</display:table>
		</div>
	</div>
</body>
</html>
