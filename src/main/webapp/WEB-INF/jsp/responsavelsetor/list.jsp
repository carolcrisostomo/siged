<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="responsavelSetor.label" /></title>
</head>
<body>


	<div class="body">

		<%
			if (request.getParameter("mensagemSucesso") != null) {
		%><div class="messageSucesso"><%=request.getParameter("mensagemSucesso")%></div>
		<%
			}
		%>
	
		<%
			if (request.getParameter("mensagemErro") != null) {
		%><div class="messageErro"><%=request.getParameter("mensagemErro")%></div>
		<%
			}
		%>
		
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>

		<spring:message code="responsavelSetor.id.label" var="coluna1" />
		<spring:message code="responsavelSetor.setor.descricao.label" var="coluna2" />
		<spring:message code="responsavelSetor.responsavel.nome.label" var="coluna3" />
		<spring:message code="responsavelSetor.responsavelImediato.nome.label" var="coluna4" />
		<spring:message code="responsavelSetor.ativo.label" var="coluna5" />

		<h1>
			<spring:message code="responsavelSetor.label" />
		</h1>
		
		<sec:authorize ifNotGranted="ROLE_ADMINISTRADORCONS">
			<a class="btn btn-primary btn-sm"
				href="<c:url value="/responsavelsetor/form"/>"> <i
				class="bi bi-plus-circle"></i> <spring:message
					code="default.add.label" />
			</a>

		</sec:authorize>

		<div class="filter">
			<c:url var="url" value="/responsavelsetor/search" />
			<form:form action="${url}" method="GET"
				modelAttribute="responsavelSetorfiltro">
				<table>
					<tbody>
						<tr>
							<td><label for="setorId"><spring:message
										code="evento.setor.label" /></label></td>
							<td valign="top" class="value"><form:select path="setor">
									<form:option value="9999">TODOS</form:option>
									<form:options items="${setorList}" itemLabel="descricao"
										itemValue="id" />
								</form:select></td>
						</tr>
						<tr>
							<td><input id="filtrar" type="submit" class="botao"
								value="Filtrar" /></td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</form:form>
		</div>

		<div class="list">
			<display:table uid="responsavel" name="${responsaveis}" defaultsort="1" defaultorder="ascending" pagesize="10" requestURI="">
				<c:url var="url" value="/responsavelsetor/${responsavel.id}" />
				<%-- <display:column property="id" sortable="true" title="${coluna1}"
					maxLength="80" /> --%>
				<display:column property="setor" sortable="true" title="${coluna2}"
					maxLength="80" />
				<display:column property="responsavel" sortable="true"
					title="${coluna3}" maxLength="80" />
				<display:column sortable="true" maxLength="50" title="Responsável Imediato">
					<c:if test="${responsavel.responsavelImediato.id == 0}">
						-
					</c:if>
					<c:if test="${responsavel.responsavelImediato.id != 0}">
						${responsavel.responsavelImediato}
					</c:if>
				</display:column>
				<display:column class="crudlist">
					<c:if test="${responsavel.ativo == true}">
						<input alt="<spring:message code="default.ativo.label" />"
							src="<c:url value="/static/images/tick.png"/>"
							title="<spring:message code="default.ativo.label" />"
							type="image"
							value="<spring:message code="default.button.save.label" />" />
					</c:if>
					<c:if test="${responsavel.ativo == false}">
						<form:form action="${url}/status" method="GET">						
							<input alt="<spring:message code="default.desativado.label" />"
								src="<c:url value="/static/images/tickoff.png"/>"
								title="<spring:message code="default.desativado.label" />"
								type="image"
								value="<spring:message code="default.button.save.label" />" />
							
						</form:form>
					</c:if>
				</display:column>
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
						<c:if test="${responsavel.ativo == true}">
							<form:form action="${url}/form" method="GET">
								<input alt="<spring:message code="default.button.save.label" />"
									src="<c:url value="/static/images/update.png"/>"
									title="<spring:message code="default.button.save.label" />"
									type="image"
									value="<spring:message code="default.button.save.label" />" />
							</form:form>
						</c:if>
					</display:column>
					<display:column class="crudlist">
						<c:if test="${responsavel.ativo == false}">
							<form:form action="${url}" method="DELETE">
								<input
									alt="<spring:message code="default.button.delete.label" />"
									src="<c:url value="/static/images/delete.png"/>"
									title="<spring:message code="default.button.delete.label" />"
									type="image"
									value="<spring:message code="default.button.delete.label" />"
									onclick="return confirm('<spring:message code="default.areyousure.message" />');" />
							</form:form>
						</c:if>
					</display:column>
				</sec:authorize>
			</display:table>
		</div>
	</div>
</body>
</html>
