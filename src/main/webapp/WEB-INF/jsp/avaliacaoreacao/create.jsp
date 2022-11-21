<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title><spring:message code="avaliacao.label" /></title>
	<spring:url value="/avaliacaoreacao/procuraModuloAvaliacao" var="procuraModuloUrl" />
	<spring:url value="/avaliacaoreacao/participanteSemAvaliacaoNoModulo" var="participanteSemAvaliacaoNoModuloURL" />
	<spring:url value="/avaliacaoreacao/procuraInstrutor" var="procuraInstrutorUrl" />
</head>
<body>
	
	<div class="body">
		<% if (request.getParameter("mensagem") != null) { %>
			<div class="message"><%=request.getParameter("mensagem")%></div>
		<% } %>
		<c:if test="${mensagem != null}">
			<div class="messageErro">${mensagem}</div>
		</c:if>
		<h1>
			<spring:message code="default.add.label" />
			-
			<spring:message code="avaliacao.label" />
		</h1>
		<c:url var="url" value="/avaliacaoreacao/formadmin" />
		<form:form action="${url}" method="GET" modelAttribute="avaliacaoFiltro">
			<div class="dialog">
				<table>
					<tbody>
						
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">
								(*) Campos Obrigatórios
							</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name">
								<label for="evento"><spring:message	code="avaliacao.evento.label" />:</label>
							</td>
							<td>
								<form:select path="evento" id="eventoId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${eventoListAvaliacoes}" itemLabel="nome" itemValue="id" />									
								</form:select>* <br /><form:errors path="evento" cssClass="error" />
							</td>

						</tr>
						
						<tr class="prop">
							<td valign="top" class="name">
								<label for="modulo"><spring:message code="avaliacao.modulo.label" />:</label>
							</td>

							<td><form:select path="modulo" id="moduloId">
									<form:option value="0">Selecione...</form:option>
								</form:select>* <form:errors path="modulo" cssClass="error" />
							</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name">
								<label for="participante"><spring:message code="avaliacao.participante.label" />:</label>
							</td>

							<td>
								<form:select path="participante" id="participanteId" itemValue="id" itemLabel="nomeCpf">
									<form:option value="0">Selecione...</form:option>
								</form:select>
								<form:errors path="participante" cssClass="error" />
							</td>
						</tr>

					</tbody>
				</table>
			</div>
			
			
			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/avaliacaoreacao/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="criar" type="submit" class="btn btn-primary">
 						<spring:message code="default.avaliar.label" />
					</button>

				</div>
			</div>
			
		</form:form>
		<script type="text/javascript">
			jQuery(document).ready(function($) {
				var option = '<option value="0">Selecione...</option>';
				$('#eventoId').change(function(){
					if($(this).val() != 0){
						updateModulo('${procuraModuloUrl}', 'eventoId', 'moduloId');
						$('#participanteId').html(option).trigger('chosen:updated');
					} else {
						$('#moduloId').html(option).trigger('chosen:updated');
						$('#participanteId').html(option).trigger('chosen:updated');
					}
				});
				$('#moduloId').change(function(){
					if($(this).val() != 0){
						updateParticipanteByModulo('${participanteSemAvaliacaoNoModuloURL}', 'moduloId', 'participanteId');
					} else {
						$('#participanteId').html(option).trigger('chosen:updated');
					}
				});
			});
		</script>
	</div>
</body>
</html>
