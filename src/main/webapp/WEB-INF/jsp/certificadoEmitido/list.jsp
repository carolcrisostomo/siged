<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="certificadoemitido.label" /></title>
<spring:url value="/ajax/procuraParticipantePorNome" var="procuraParticipantePorNomeUrl" />
</head>
<body>
	<script type="text/javascript">
		jQuery(document).ready( function($) {

			updateParticipantePorNome('${procuraParticipantePorNomeUrl}', 'nomeParticipante', 'participante', 'participanteErro', '${certificadoEmitidoFiltro.participante}', 'true', '');

			$("#buscarParticipante").click( function($) {
				updateParticipantePorNome('${procuraParticipantePorNomeUrl}', 'nomeParticipante', 'participante', 'participanteErro', '', 'true', 'true');
			});

			$(document).ajaxStop($.unblockUI);
		});
	</script>

	<div class="body">		
		<spring:message code="certificadoemitido.evento.label" var="coluna1" />
		<spring:message code="certificadoemitido.participante.label" var="coluna2" />
		<spring:message code="certificadoemitido.dataemissao.label" var="coluna3" />
		<spring:message code="certificadoemitido.codigo.label" var="coluna4" />
		
		<h1>
			<spring:message code="certificadoemitido.label" />
		</h1>
		
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>
		<c:if test="${mensagemErro != null}">
			<div class="messageErro">${mensagemErro}</div>
		</c:if>
		
		<div class="filter">
			<c:url var="url" value="/certificadoEmitido/search" />
			<form:form action="${url}" method="GET"	modelAttribute="certificadoEmitidoFiltro">
				<table>
					<tbody>
						<tr>
							<td><label for="eventoId"><spring:message code="certificadoemitido.evento.label" /></label></td>
							<td valign="top" class="value">
								<form:select path="evento">
									<form:option value="0">TODOS</form:option>
									<form:options items="${eventosComCertificadoEmitido}"
										itemLabel="nome" itemValue="id" />
								</form:select>
							</td>
						</tr>
						<tr>
                    		<td>
                      			<label for="participante"><spring:message code="certificadoemitido.participante.label" /></label>
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
                    		<td>
                      			<label for="periodo">Período</label>
                    		</td>
                    		<td valign="top" class="value">
                      			<form:input cssStyle="width:90px" id="data1" maxlength="255" path="data1" size="30" alt="date"/>
                       			a <form:input cssStyle="width:90px" id="data2" maxlength="255" path="data2" size="30" alt="date"/>
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
			<display:table uid="certificadoEmitido" name="${certificadosEmitidos}" pagesize="10" requestURI="" defaultsort="3">				
				<display:column property="evento" sortable="true"
					title="${coluna1}" maxLength="80" />
				<display:column property="participante" sortable="true"
					title="${coluna2}" maxLength="80" />
				<display:column sortable="true" title="${coluna3}" maxLength="80" >
					<fmt:formatDate value="${certificadoEmitido.dataEmissao}" pattern="dd/MM/yyyy HH:mm:ss"/>
				</display:column>
				<display:column property="codigoVerificacaoComMascara" sortable="true" title="${coluna4}"
					maxLength="80" />
				<display:column class="crudlist">
					<a
							href="<c:url value="/certificado/certificadoEmitido/${certificadoEmitido.codigoVerificacao}" />"
							title="<spring:message code="evento.emitirCertificado.label" />">
							<img width="auto" height="25px"
							src="<c:url value="/static/images/certificado.png"/>"
							id="icon_certificado" />
					</a>					
				</display:column>
				
				<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
					<display:column class="crudlist">
						<c:url var="urlDelete" value="/certificadoEmitido/${certificadoEmitido.id}"/>
						<form:form action="${urlDelete}" method="DELETE">
							<input
								alt="<spring:message code="default.button.delete.label" />"
								src="<c:url value="/static/images/delete.png"/>"
								title="<spring:message code="default.button.delete.label" />"
								type="image"
								value="<spring:message code="default.button.delete.label" />"
								onclick="return confirm('<spring:message code="default.button.delete.confirm.message" />');" />
						</form:form>
					</display:column>
				</sec:authorize>
			</display:table>
		</div>
		
	</div>
</body>
</html>
