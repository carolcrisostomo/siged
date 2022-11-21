<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="participante.label" /></title>
<spring:url value="/participante/search" var="participantesUrl" />
</head>
<body>
	
	<div class="body">				
		<h1>
			<spring:message code="participante.label" />
		</h1>
		
		<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
			<a class="btn btn-primary btn-sm"
				href="<c:url value="/participante/form"/>"> <i
				class="bi bi-plus-circle"></i> <spring:message
					code="default.add.label" />
			</a>

		</sec:authorize>
		
		<c:if test="${param.mensagem != null}"><div class="message info"><c:out value="${param.mensagem}" /></div></c:if>
		<c:if test="${param.mensagemSucesso != null}"><div class="messageSucesso messageRemove"><c:out value="${param.mensagemSucesso}" /></div></c:if>
		<div class="filter">
			<c:url var="url" value="/participante/search" />
			<form:form action="${url}" method="GET"
				modelAttribute="participantefiltro">
				<table>
					<tbody>

						<tr>
							<td><label for="nome"><spring:message
										code="participante.nome.label" /></label></td>
							<td valign="top" class="value"><form:input
									cssStyle="width:200px" maxlength="255" path="nome" size="30" />
							</td>
						</tr>
						<tr>
							<td><label for="tipoId"><spring:message
										code="participante.tipo.label" /></label></td>
							<td valign="top" class="value"><form:select
									path="tipoPublicoAlvo">
									<form:option value="0">TODOS</form:option>
									<form:options items="${tipoPublicoAlvoList2}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>
						<tr>
							<td><label for="cpf"><spring:message
										code="participante.cpf.label" /></label></td>
							<td valign="top" class="value"><form:input
									cssStyle="width:110px" id="cpf" maxlength="255" path="cpf"
									size="30" alt="cpf" /></td>
						</tr>
						<tr>
							<td><label for="formacaoId"><spring:message
										code="participante.formacao.label" /></label></td>
							<td valign="top" class="value"><form:select
									path="formacaoAcademica">
									<form:option value="0">TODOS</form:option>
									<form:options items="${formacaoAcademicaList}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>
						<tr>
							<td><label for="escolaridadeId"><spring:message
										code="participante.escolaridade.label" /></label></td>
							<td valign="top" class="value"><form:select
									path="nivelEscolaridade">
									<form:option value="0">TODOS</form:option>
									<form:options items="${nivelEscolaridadeList}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
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
		
		<spring:message code="participante.nome.label" var="coluna1" />		
		<spring:message code="participante.cpf.label" var="coluna2" />
		<spring:message code="participante.tipo.label" var="coluna3" />
					
		<c:if test="${participantes != null}">		
			<div class="list">
			
				<display:table uid="participante" name="${participantes}" sort="external" partialList="true" size="${participantes.fullListSize}" 
					requestURI="${participantesUrl}">
					
					<c:url var="url" value="/participante/${participante.id}" />
	
					<display:column property="nome" title="${coluna1}" maxLength="80" sortable="true" />
					<display:column property="cpfFormatado" title="${coluna2}" maxLength="80" sortable="true" sortProperty="cpf"/>
					<display:column property="tipoId" title="${coluna3}" maxLength="80" />
					
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
							<c:if test="${participante.tipoId.id eq 2 or participante.tipoId.id eq 3}">
								<form:form action="${url}/form" method="GET">
									<input alt="<spring:message code="default.button.edit.label" />"
										src="<c:url value="/static/images/update.png"/>"
										title="<spring:message code="default.button.edit.label" />"
										type="image"
										value="<spring:message code="default.button.edit.label" />" />
								</form:form>
							</c:if>
						</display:column>
						<display:column class="crudlist">
							<c:if test="${participante.tipoId.id eq 2 or participante.tipoId.id eq 3}">
								<form:form action="${url}" method="DELETE">
									<input
										alt="<spring:message code="default.button.delete.label" />"
										src="<c:url value="/static/images/delete.png"/>"
										title="<spring:message code="default.button.delete.label" />"
										type="image"
										value="<spring:message code="default.button.delete.label" />"
										onclick="return confirm('<spring:message code="default.areyousure2.message" />');" />
								</form:form>
							</c:if>
						</display:column>
					</sec:authorize>
					
				</display:table>
				
			</div>
		</c:if>	
	</div>
</body>
</html>
