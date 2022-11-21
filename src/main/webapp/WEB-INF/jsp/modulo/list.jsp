<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="modulo.label" /></title>
<spring:url value="/modulo/search" var="modulosUrl"></spring:url>
</head>
<body>

<div class="body">
<c:if test="${param.mensagem != null}">
	<div class="message">${param.mensagem}</div>
</c:if>
<c:if test="${param.mensagemErro != null}">
	<div class="messageErro">${param.mensagemErro}</div>
</c:if>
<spring:message code="modulo.evento.label" var="coluna1" />
<spring:message code="modulo.titulo.label" var="coluna2" />
<spring:message code="modulo.cargaHoraria2.label" var="coluna3" />
<spring:message code="modulo.nota2.label" var="coluna4" />
<spring:message code="modulo.frequenciaMinima2.label" var="coluna5" />
<spring:message code="modulo.dataInicio.label" var="coluna6" />
<spring:message code="modulo.dataFim.label" var="coluna7" />
<h1><spring:message code="modulo.label" /></h1>

	<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
			<a class="btn btn-primary btn-sm"
				href="<c:url value="/modulo/form"/>"> <i
				class="bi bi-plus-circle"></i> <spring:message
					code="default.add.label" />
			</a>

		</sec:authorize>

            <div class="filter">
            <c:url var="url" value="/modulo/search" />
            <form:form action="${url}" method="GET" modelAttribute="modulofiltro">
              <table>
                <tbody>
						<%-- <tr>
							<td><label for="titulo">Título do Evento</label></td>
							<td valign="top" class="value"><form:input
									cssStyle="width:300px" id="titulo" maxlength="255"
									path="eventoTitulo" size="30" /></td>
						</tr> --%>
						<tr>
							<td><label for="eventoId"><spring:message
										code="evento.label" /></label></td>
							<td valign="top" class="value"><form:select path="evento">
									<form:option value="0">TODOS</form:option>
									<form:options items="${eventoList}" itemLabel="nome"
										itemValue="id" />
								</form:select></td>
						</tr>
						<tr>
                    <td>
                      <label for="periodo">Período</label>
                    </td>
                    <td valign="top" class="value">
                      <form:input cssStyle="width:90px" id="data1" maxlength="255" path="data1" size="30" alt="date"/>
                       a <form:input cssStyle="width:90px" id="data2" maxlength="255" path="data2" size="30" alt="date"/>
                    </td>
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
		<display:table uid="modulo" name="${modulos}" sort="external" partialList="true" size="${modulos.fullListSize}" 
			requestURI="${modulosUrl}">
			
			<c:url var="url" value="/modulo/${modulo.id}" />
			
			<display:column property="eventoId" title="${coluna1}" maxLength="80" 
				sortable="true" sortProperty="e.titulo"/>
			
			<display:column property="titulo" title="${coluna2}" maxLength="80" 
				sortable="true" sortProperty="titulo"/>
				
			<display:column style="text-align : center;" property="cargaHoraria" title="${coluna3}" maxLength="10" 
				sortable="true" sortProperty="cargaHoraria"/>
			
			<display:column style="text-align : center;" property="nota" title="${coluna4}" maxLength="10" 
				sortable="true" sortProperty="nota"/>
				
			<display:column style="text-align : center;" property="frequencia" title="${coluna5}" maxLength="10" 
				sortable="true" sortProperty="frequencia"/>
				
			<display:column property="dataInicio" title="${coluna6}" maxLength="50" format="{0,date,dd/MM/yyyy}"
				sortable="true" sortProperty="dataInicio"/>
				
			<display:column property="dataFim" title="${coluna7}" maxLength="50" format="{0,date,dd/MM/yyyy}"
				sortable="true" sortProperty="dataFim"/>
				
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
