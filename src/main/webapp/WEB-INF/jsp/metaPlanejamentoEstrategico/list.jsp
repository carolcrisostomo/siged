<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Indicadores</title>
<spring:url value="/meta/planejamentoEstrategico/search" var="searchUrl" />
</head>
<body>

	<div class="body">				
		<h1>
			<spring:message code="meta.planejamentoEstrategico.label" />
		</h1>
		
					<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
			<a class="btn btn-primary btn-sm"
				href="<c:url value="/meta/planejamentoEstrategico/form"/>"> <i
				class="bi bi-plus-circle"></i> <spring:message
					code="default.add.label" />
			</a>

		</sec:authorize>
		
		
		<c:if test="${param.mensagem != null}">
			<div class="message">${param.mensagem}</div>
		</c:if>
		<c:if test="${param.messageSucesso != null}">
			<div class="messageSucesso">${param.messageSucesso}</div>
		</c:if>
		<c:if test="${param.mensagemErro != null}">
			<div class="messageErro">${param.mensagemErro}</div>
		</c:if>
		<div class="filter">
			<form:form action="${searchUrl}" method="GET" modelAttribute="filtro">
				<table>
					<tbody>
					
						<tr>
							<td width="60px><label for="anoId">Ano</label></td>
							<td valign="top" class="value" >
								<form:select path="ano" id="anoId">
									<form:option value="0">TODOS</form:option>
									<form:options items="${anosList}" />
								</form:select>
							</td>
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
		
		<spring:message code="meta.planejamentoEstrategico.meta1.label" var="meta1" />		
		<spring:message code="meta.planejamentoEstrategico.meta2.label" var="meta2" />
		<spring:message code="meta.planejamentoEstrategico.meta3.label" var="meta3" />
		<spring:message code="meta.planejamentoEstrategico.meta4.label" var="meta4" />
				
		<c:if test="${metas != null}">		
			<div class="list">
			
				<display:table uid="meta" name="${metas}" sort="external" partialList="true" size="${metas.fullListSize}" 
					requestURI="${searchUrl}">
					
					<c:url var="url" value="/meta/planejamentoEstrategico/${meta.id}" />
	
					<display:column property="ano" title="Ano" maxLength="80" sortable="true" sortProperty="ano"/>
					<display:column property="quantidadeAcoes" title="${meta1}" />
					<display:column property="percentualJurisdicionadosCapacitadosPercent" title="${meta2}" />
					<display:column property="percentualServidoresCapacitadosPercent" title="${meta3}" />
					<display:column property="percentualAcoesDoPlanoPercent" title="${meta4}" />
					
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
		</c:if>	
	</div>
</body>
</html>
