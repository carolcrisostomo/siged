<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<spring:url value="/material/procuraModulo" var="procuraModuloUrl" />
</head>
<body>
	<script type="text/javascript">
		jQuery(document).ready(
				function($) {
					updateModulo('${procuraModuloUrl}', 'eventoId', 'moduloId',
							true);

					$("#eventoId").change(
							function() {
								updateModulo('${procuraModuloUrl}', 'eventoId',
										'moduloId', true);
							});

				});
	</script>


	<div class="body">
		
		<c:if test="${param.mensagem != null}">
			<div class="message info">${param.mensagem}</div>
		</c:if>
		<c:if test="${param.mensagemErro != null}">
			<div class="messageErro">${param.mensagemErro}</div>
		</c:if>
		
		<h1>Material Didático/Divulgação</h1>



		<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
			<a class="btn btn-primary btn-sm"
				href="<c:url value="/material/form"/>"> <i
				class="bi bi-plus-circle"></i> <spring:message
					code="default.add.label" />
			</a>

		</sec:authorize>
		
		<div class="filter">
			<c:url var="url" value="/material/search" />
			<form:form action="${url}" method="GET"
				modelAttribute="materialFiltro">
				<table>
					<tbody>
						<tr class="prop">
							<td valign="top" class="name"><label for="eventoId">Evento</label></td>
							<td><form:select path="eventoId">
									<form:option value="0">TODOS</form:option>
									<form:options items="${eventoList}" itemLabel="nome"
										itemValue="id" />
								</form:select></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="moduloId">Módulo</label></td>
							<td><form:select path="moduloId">
									<form:option value="0">TODOS</form:option>
								</form:select></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="materialTipo">Tipo
									Material</label></td>
							<td><form:select path="materialTipo">
									<form:option value="0">TODOS</form:option>
									<form:options items="${materialTipoList}" />
								</form:select></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="descricao">Descrição</label>
							</td>
							<td><form:input cssStyle="width:400px" maxlength="100"
									path="descricao"
									onkeyup="javascript:this.value=this.value.toUpperCase();" /></td>
						</tr>
						<tr>
							<td>
								<button  id="filtrar" type="submit" class="btn btn-primary btn-sm">
 							Filtrar
					</button>
								</td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</form:form>
		</div>
		<div class="list">
			<display:table uid="material" name="${materiais}" pagesize="10"
				requestURI="" >
				<c:url var="url" value="/material/${material.id}" />
				<display:column property="eventoId" sortable="true" title="Evento"
					maxLength="80" />
				
				<display:column property="moduloId" sortable="true" title="Modulo"
					maxLength="80" />
				<display:column sortable="true" title="Tipo" maxLength="80">
					<c:if test="${material.materialTipo eq 1}">
						<c:out value="MATERIAL DIDÁTICO" />
					</c:if>
					<c:if test="${material.materialTipo eq 2}">
						<c:out value="MATERIAL DE DIVULGAÇÃO" />
					</c:if>
				</display:column>
				<display:column title="Descrição">
					<a href="<c:url value="/material/arquivo/${material.id}"/>">${material.descricao}</a>
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
