<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="avaliacaoEficacia.label" /></title>
<spring:url value="/ajax/procuraParticipantePorNome" var="procuraParticipantePorNomeUrl" />
</head>
<body>
	<script type="text/javascript">
		jQuery(document).ready( function($) {

			updateParticipantePorNome('${procuraParticipantePorNomeUrl}', 'nomeParticipante', 'participante', 'participanteErro', '${avaliacaoeficaciafiltro.participante}', 'true', '');

			$("#buscarParticipante").click( function($) {
				updateParticipantePorNome('${procuraParticipantePorNomeUrl}', 'nomeParticipante', 'participante', 'participanteErro', '', 'true', 'true');
			});

			$(document).ajaxStop($.unblockUI);
		});
	</script>
<!-- 	<sec:authorize ifNotGranted="ROLE_ADMINISTRADORCONS">
		<div class="nav">
			<span class="menuButton"><a
				href="<c:url value="/avaliacaoeficacia/form"/>" class="create"><spring:message code="default.add.label" /></a>
			</span>
		</div>
	</sec:authorize> -->

	<div class="body">
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>
		<spring:message code="avaliacaoEficacia.evento.label" var="coluna1" />
		<spring:message code="avaliacaoEficacia.participante.label"	var="coluna2" />
		<spring:message code="avaliacaoEficacia.melhoria.label" var="coluna3" />
		<spring:message code="avaliacaoEficacia.desempenhoServico.label" var="coluna4" />
		<spring:message code="avaliacaoEficacia.observacao.label" var="coluna5" />
		<h1>
			<spring:message code="avaliacaoEficacia.label" />
		</h1>

		<div class="filter">
			<c:url var="url" value="/avaliacaoeficacia/search" />
			<form:form action="${url}" method="GET" modelAttribute="avaliacaoeficaciafiltro">
				<table>
					<tbody>
						<tr>
							<td><label for="eventoId"><spring:message code="avaliacaoEficacia.evento.label" /></label></td>
							<td valign="top" class="value">
								<form:select path="evento">
									<form:option value="0">TODOS</form:option>
									<form:options items="${eventoList}" itemLabel="nome" itemValue="id" />
								</form:select>
							</td>
						</tr>
						
						<tr>
                    		<td>
                      			<label for="participante"><spring:message code="avaliacaoEficacia.participante.label" /></label>
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
							<td><label for="periodo">Período</label></td>
							<td valign="top" class="value">
								<form:input	cssStyle="width:90px" id="data_cadastro1" maxlength="255" path="data_cadastro1" size="30" alt="date" /> a 
								<form:input	cssStyle="width:90px" id="data_cadastro2" maxlength="255" path="data_cadastro2" size="30" alt="date" />
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

		<div class="list">
			<display:table uid="avaliacaoeficacia" name="${avaliacoeseficacia}"
				pagesize="10" requestURI="">
				<c:url var="url" value="/avaliacaoeficacia/${avaliacaoeficacia.id}" />
				<display:column property="eventoId" sortable="true"
					title="${coluna1}" maxLength="80" />
				<display:column property="participanteId" sortable="true"
					title="${coluna2}" maxLength="80" />
				<display:column property="melhoria" sortable="true"
					title="${coluna3}" maxLength="80" />
				<display:column property="desempenhoServico" sortable="true"
					title="${coluna4}" maxLength="80" />
				<display:column property="observacao" sortable="true"
					title="${coluna5}" maxLength="80" />
				<display:column class="crudlist">
					<form:form action="${url}" method="GET">
						<input alt="<spring:message code="default.button.show.label" />"
							src="<c:url value="/static/images/show.png"/>"
							title="<spring:message code="default.button.show.label" />"
							type="image"
							value="<spring:message code="default.button.show.label" />" />
					</form:form>
				</display:column>
				<sec:authorize
					ifNotGranted="ROLE_ADMINISTRADOR, ROLE_ADMINISTRADORCONS">
					<display:column class="crudlist">
						<form:form action="${url}/form" method="GET">
							<input alt="<spring:message code="default.button.edit.label" />"
								src="<c:url value="/static/images/update.png"/>"
								title="<spring:message code="default.button.edit.label" />"
								type="image"
								value="<spring:message code="default.button.edit.label" />" />
						</form:form>
					</display:column>
				</sec:authorize>
				<sec:authorize ifNotGranted="ROLE_ADMINISTRADORCONS">
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
