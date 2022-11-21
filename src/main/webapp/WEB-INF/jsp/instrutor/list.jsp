<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="instrutor.label" /></title>
<spring:url value="/instrutor/search" var="instrutoresUrl" />
</head>
<body>

<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
	<spring:message code="instrutor.nome.label" var="coluna1" />
	<spring:message code="instrutor.telefone.label" var="coluna2" />
	<spring:message code="instrutor.celular.label" var="coluna3" />
	<spring:message code="instrutor.tipoInstrutor.label" var="coluna4" />
	<spring:message code="instrutor.instituicaoVinculo.label" var="coluna5" />
	<spring:message code="instrutor.situacaoInstrutor.label" var="coluna6" />
	<h1><spring:message code="instrutor.label" /></h1>
	
	
		<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
			<a class="btn btn-primary btn-sm"
				href="<c:url value="/instrutor/form"/>"> <i
				class="bi bi-plus-circle"></i> <spring:message
					code="default.add.label" />
			</a>

		</sec:authorize>

	<div class="filter">
            <c:url var="url" value="/instrutor/search" />
            <form:form action="${url}" method="GET" modelAttribute="instrutorfiltro">
              <table>
                <tbody>
               	<tr>
                    <td>
                      <label for="nome"><spring:message code="instrutor.nome.label" /></label>
                    </td>
                    <td valign="top" class="value">
                      <form:input cssStyle="width:200px" maxlength="255" path="nome" size="30" onkeyup="this.value=this.value.toUpperCase()"/>
                    </td>
                </tr>
                <tr class="prop">
					<td>
						<label for="tipoInstrutor"><spring:message code="instrutor.tipoInstrutor.label" /></label>
					</td>
					<td valign="top" class="value">
						<form:select path="tipo"> 
						  <form:option value="0">TODOS</form:option>
						  <form:options items="${tipoInstrutorList}" itemLabel="descricao" itemValue="id" />
						</form:select> 
					</td>
				</tr>
                <tr>
                    <td>
                      <label for="situacaoId"><spring:message code="instrutor.situacaoInstrutor.label" /></label>
                    </td>
                    <td valign="top" class="value">
                      <form:select path="situacao">
					  	<form:option value="0">TODOS</form:option>
                      	<form:options items="${situacaoInstrutorList}" itemLabel="descricao" itemValue="id" />
                      </form:select>
                    </td>
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
		<display:table uid="instrutor" name="${instrutores}" sort="external" partialList="true" size="${instrutores.fullListSize}"
			requestURI="${instrutoresUrl}">
			
			<c:url var="url" value="/instrutor/${instrutor.id}" />
			
			<display:column property="nome" sortable="true" title="${coluna1}" maxLength="80" sortProperty="nome"/>
			<display:column property="telefone" sortable="true" title="${coluna2}" maxLength="80" />
			<display:column property="celular" sortable="true" title="${coluna3}" maxLength="80" />
			<display:column property="tipoId" sortable="true" title="${coluna4}" maxLength="80" />
			<display:column property="instituicao" sortable="true" title="${coluna5}" maxLength="80" />
			<display:column property="situacaoId" sortable="true" title="${coluna6}" maxLength="80" />
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
						<input alt="<spring:message code="default.button.edit.label" />"
							src="<c:url value="/static/images/update.png"/>"
							title="<spring:message code="default.button.edit.label" />"
							type="image"
							value="<spring:message code="default.button.edit.label" />" />
					</form:form>
				</display:column>
				<display:column class="crudlist">
					<form:form action="${url}" method="DELETE">
						<input alt="<spring:message code="default.button.delete.label" />"
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
