<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="avaliacaoEficacia.label" /></title>
<spring:url value="/avaliacaoeficacia/procuraParticipantesAprovados"
	var="procuraParticipanteUrl" />
</head>
<body>

	<script type="text/javascript">
		jQuery(document).ready(function($) {

			if($("eventoId").val() != 0)
				updateParticipanteSelected('${procuraParticipanteUrl}', 'eventoId', 'participanteId', '${avaliacaoeficacia.participanteId.id}');
			
			$("eventoId").change(function(){
				updateParticipante('${procuraParticipanteUrl}', 'eventoId', 'participanteId');
			});			
			
		});
	</script>

 	<div class="nav">
		<span class="menuButton"> 
			<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
				<a href="<c:url value="/avaliacaoeficacia/"/>" class="list"><spring:message
						code="default.button.list.label" /></a>
			</sec:authorize> 
			<sec:authorize ifAnyGranted="ROLE_CHEFE">
				<a href="<c:url value="/avaliacaoeficacia/minhasavaliacoes"/>"
					class="list"><spring:message
						code="avaliacaoEficacia.listagem.label" /></a>
			</sec:authorize>
		</span>
	</div>
	<div class="body">
		<h1>
			<spring:message code="default.add.label" />
			-
			<spring:message code="avaliacaoEficacia.label" />
		</h1>
	
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>		
		
		<div class="message">Caso as melhorias não se apliquem, ou não
			seja possível avaliar, justifique no campo "Observação"</div>

		<c:if test="${mensagem != null}">
			<div class="message">${mensagem}</div>
		</c:if>

		<c:url var="url" value="/avaliacaoeficacia" />
		
		<form:form action="${url}" method="POST" modelAttribute="avaliacaoeficacia">
			<div class="dialog">
				<table>
					<tbody>
						<tr>
							<td></td>
							<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
								<td style="text-align: right;" valign="top" class="name">(*)
									Campos Obrigatórios</td>
							</sec:authorize> 
						</tr>
						<tr class="prop">
							<td valign="top" class="name2"><label for="evento"><spring:message
										code="avaliacaoEficacia.evento.label" />:</label></td>

							<td>
							<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">

								<form:select path="eventoId" id="eventoId"
									onchange="updateParticipante('${procuraParticipanteUrl}', 'eventoId', 'participanteId');">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${eventoCujaEficaciaDeveSerAvaliada}" itemValue="id" itemLabel="nome" />										
								</form:select>* <form:errors path="eventoId" cssClass="error" />

							</sec:authorize> 
							
							<sec:authorize ifNotGranted="ROLE_ADMINISTRADOR">
								
								${avaliacaoeficacia.eventoId}
								<form:hidden path="eventoId" value="${avaliacaoeficacia.eventoId.id}" />
								
							</sec:authorize></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name2"><label for="participante"><spring:message
										code="avaliacaoEficacia.participante.label" />:</label>
							</td>

							<td>
								
								<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
									<form:select path="participanteId" id="participanteId"
										itemValue="id" itemLabel="nomeCpf">
										<form:option value="0">Selecione...</form:option>
									</form:select>* <form:errors path="participanteId" cssClass="error" />
								</sec:authorize> 
								
								<sec:authorize ifNotGranted="ROLE_ADMINISTRADOR">
									${avaliacaoeficacia.participanteId}
									<form:hidden path="participanteId" value="${avaliacaoeficacia.participanteId.id}" />
								</sec:authorize></td>
						</tr>
						
						<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
							<tr class="prop">
								<td valign="top" class="name">
									<label for="responsavel">Responsável:</label>
								</td>
								<td>
									<form:select path="responsavel">
										<form:option value="0">Selecione...</form:option>
										<form:options items="${chefes}" itemValue="id" itemLabel="nome" />
									</form:select> 
								</td>
							</tr>
						</sec:authorize>
						
						<tr class="prop">
							<td valign="top" class="name2"><label for="melhoria"><spring:message
										code="avaliacaoEficacia.melhoria2.label" />:</label></td>

							<td><form:select path="melhoria">
									<form:option value="">Selecione...</form:option>
									<form:options items="${melhorias}" />
								</form:select> <form:errors path="melhoria" cssClass="error" /></td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name2"><label
								for="desempenhoservico"><spring:message
										code="avaliacaoEficacia.desempenhoServico2.label" />:</label></td>

							<td><form:select path="desempenhoServico">
									<form:option value="">Selecione...</form:option>
									<form:options items="${respostasQuestoes}" />
								</form:select> <form:errors path="desempenhoServico" cssClass="error" /></td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name2"><label for="observacao"><spring:message
										code="avaliacaoEficacia.observacao.label" />:</label></td>

							<td><form:textarea path="observacao" cols="70" rows="5" />
								<form:errors path="observacao" cssClass="error" /></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="buttons">
				<input id="criar" type="submit" class="save"
					value="<spring:message code="default.add.label" />" />
			</div>
		</form:form>
	</div>
</body>
</html>
