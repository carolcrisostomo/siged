<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
<spring:url value="/ajax/procuraParticipantePorNome" var="procuraParticipantePorNomeUrl" />
</head>
<body>
	<script type="text/javascript">
		jQuery(document).ready( function($) {

			updateParticipantePorNome('${procuraParticipantePorNomeUrl}', 'nomeParticipante', 'participanteId', 'participanteErro', '${relEventosPorParticipante.participanteId}', '', '');

			$("#buscarParticipante").click( function($) {
				updateParticipantePorNome('${procuraParticipantePorNomeUrl}', 'nomeParticipante', 'participanteId', 'participanteErro', '', '', 'true');
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

		<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
			<h1>Relatório de Eventos por Participante</h1>
		</sec:authorize>
		<sec:authorize ifAnyGranted="ROLE_ALUNO,ROLE_SERVIDOR">
			<h1>Relatório de Eventos</h1>
		</sec:authorize>

		<div class="message">Selecione "Internet" em Localização do	Evento para eventos a distância</div>
		
		<c:if test="${mensagemRel != null}">
			<div class="message" id="msgId">
				<c:out value="${mensagemRel}" />
			</div>
		</c:if>
		
		<c:url var="url" value="/relatorio/eventosPorParticipante/" />
		
		<form:form action="${url}" method="POST" modelAttribute="relEventosPorParticipante" id="form1">
			<div class="filter">
				<table>
					<tbody>
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">(*) Campos Obrigatórios</td>
						</tr>
						<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">

							<tr>
	                    		<td>
	                      			<label for="participanteId"><spring:message code="evento.participante.label" /></label>
	                    		</td>
	                    		<td valign="top" class="value">
	                    			
	                    			<form:input	cssStyle="width:90px" maxlength="255" path="nomeParticipante"/>
									
									<input id="buscarParticipante" class="search" type="button" />
	                      			
	                      			<form:select path="participanteId" cssStyle="width:450px">
						  				<form:option value="0">Selecione...</form:option>                      				
	                      			</form:select>* <form:errors path="participanteId" cssClass="error" /><br />
	                      			<span id="participanteErro" class="error"></span>
	                    		</td>
                  			</tr>

						</sec:authorize>

						<sec:authorize ifAnyGranted="ROLE_ALUNO,ROLE_SERVIDOR">
							<form:hidden path="participanteId" id="participante_evento" value="${sessionScope.PARTICIPANTE_ID}" />
						</sec:authorize>

						<tr class="prop">
							<td valign="top" class="name"><label for="eventos"><spring:message code="evento.tipoEvento.label" /></label></td>
							<td valign="top">
								<form:select path="tipoEventoId" id="tipo_evento">
									<form:option value="0">TODOS</form:option>
									<form:options items="${tipoEventoList}" itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="periodoprevisto">Período
									de Realização</label></td>
							<td valign="top"><form:input path="dataInicio1" id="data1"
									alt="date" /> a <form:input path="dataInicio2" id="data2"
									alt="date" /> <form:errors path="dataInicio2" cssClass="error" />
							</td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="eventos">Localização</label></td>
							<td valign="top"><form:select path="localizacaoId"
									id="evento_localizacao_id">
									<form:option value="0">TODOS</form:option>
									<form:options items="${tipoLocalizacaoEventoList}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="provedor">Provedor</label></td>
							<td valign="top"><form:select path="provedorId"
									id="evento_provedor">
									<form:option value="0">TODOS</form:option>
									<form:options items="${provedorEventoList}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="setor"><spring:message
										code="evento.situacao.label" /></label></td>
							<td valign="top"><form:select path="situacao"
									id="evento_situacao">
									<form:option value="0">TODOS</form:option>
									<form:options items="${situacaoList}" />
								</form:select></td>
						</tr>
						<tr class="prop">
							<td colspan="2" valign="top" class="value">
								<input type="checkbox" name="comoInstrutor" value="" <%=request.getParameter("comoInstrutor") != null ? "checked": ""%> />
								&nbsp;&nbsp;Como Instrutor
							</td>
						</tr>

						<tr>
							<td><input id="filtrar" type="submit" class="botao" value="Filtrar" /></td>
							<td></td>
						</tr>

					</tbody>
				</table>
			</div>
		</form:form>
	</div>
</body>
</html>