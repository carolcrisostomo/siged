<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="frequencia.label" /></title>
<spring:url value="/frequencia/procuraModulo" var="procuraModuloUrl" />
<spring:url value="/ajax/procuraParticipantePorNome" var="procuraParticipantePorNomeUrl" />
</head>
<body>
	<script type="text/javascript">
		jQuery(document).ready( function($) {

			updateParticipantePorNome('${procuraParticipantePorNomeUrl}', 'nomeParticipante', 'participante', 'participanteErro', '${frequenciafiltro.participante}', 'true', '');

			$("#buscarParticipante").click( function($) {
				updateParticipantePorNome('${procuraParticipantePorNomeUrl}', 'nomeParticipante', 'participante', 'participanteErro', '', 'true', 'true');
			});

			$(document).ajaxStop($.unblockUI);
		});
	</script>
	
	<div class="body">
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>
		<spring:message code="frequencia.evento.label" var="coluna1" />
		<spring:message code="frequencia.modulo.label" var="coluna2" />
		<spring:message code="frequencia.participante.label" var="coluna3" />
		<spring:message code="frequencia.data.label" var="coluna4" />
		<spring:message code="frequencia.presenca.label" var="coluna5" />
		<spring:message code="frequencia.turno.label" var="coluna6" />
		<h1>
			<spring:message code="frequencia.label" />
		</h1>
		
		<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
			<a class="btn btn-primary btn-sm"
				href="<c:url value="/frequencia/form"/>"> <i
				class="bi bi-plus-circle"></i> <spring:message
					code="default.add.label" />
			</a>

		</sec:authorize>

		<div class="filter">
			<c:url var="url" value="/frequencia/search" />
			<form:form action="${url}" method="GET"
				modelAttribute="frequenciafiltro">
				<table>
					<tbody>
						<tr>
							<td><label for="evento"><spring:message code="frequencia.evento.label" /></label></td>
							<td valign="top" class="value">
								<form:select path="evento" id="evento" onchange="updateModulo('${procuraModuloUrl}', 'evento', 'modulo');">
									<form:option value="0">TODOS</form:option>
									<form:options items="${eventoList}" itemLabel="nome" itemValue="id" />
								</form:select></td>
						</tr>
						<tr>
							<td><label for="modulo"><spring:message	code="frequencia.modulo.label" /></label></td>
							<td valign="top" class="value">
								<form:select path="modulo">
									<form:option value="0">TODOS</form:option>
								</form:select>
							</td>
						</tr>
						
						<tr>
                    		<td>
                      			<label for="participante"><spring:message code="nota.participante.label" /></label>
                    		</td>
                    		<td valign="top" class="value">
                    			
                    			<form:input	cssStyle="width:90px" maxlength="255" path="nomeParticipante"/>
								
								<input id="buscarParticipante" class="search" type="button" />
                      			
                      			<form:select path="participante" cssStyle="width:450px">
					  				<form:option value="0">TODOS</form:option>                      				
                      			</form:select><span id="participanteErro" class="error"></span>
                    		</td>
                  		</tr>
                  		
						<tr>
							<td><label for="data">Período:</label></td>
							<td valign="top" class="value"><form:input id="data1"
									path="data1" alt="date" /> a <form:input id="data2"
									path="data2" alt="date" /></td>
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
			<display:table uid="frequencia" name="${frequencias}" pagesize="10"
				requestURI="">
				<c:url var="url" value="/frequencia/${frequencia.id}" />
				<display:column property="eventoId" sortable="true"
					title="${coluna1}" maxLength="80" />
				<display:column property="moduloId" sortable="true"
					title="${coluna2}" maxLength="80" />
				<display:column property="data" sortable="true" title="${coluna4}"
					maxLength="50" format="{0,date,dd/MM/yyyy}" />
				<display:column property="turno" sortable="true" title="${coluna6}"
					maxLength="80" />

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
