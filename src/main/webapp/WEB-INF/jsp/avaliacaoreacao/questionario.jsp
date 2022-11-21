<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title><spring:message code="avaliacao.label" /></title>
	<spring:url value="/ajax/modulo/procuraPorEvento" var="procuraModuloUrl" />
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
			Impressão de questionário de avaliação de reação
		</h1>
		<c:url var="url" value="/avaliacaoreacao/questionario" />
		<form:form action="${url}" method="POST" modelAttribute="avaliacaoFiltro">
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
								<label for="evento"><spring:message	code="avaliacao.evento.label" />*:</label>
							</td>
							<td>
								<form:select path="eventoId" id="eventoId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${eventoList}" itemLabel="nome" itemValue="id" />									
								</form:select> 
								<br />
								<form:errors path="eventoId" cssClass="error" />
							</td>

						</tr>
						
						<tr class="prop">
							<td valign="top" class="name">
								<label for="modulo"><spring:message code="avaliacao.modulo.label" />:</label>
							</td>

							<td><form:select path="modulo" id="moduloId">
									<form:option value="0">TODOS</form:option>
								</form:select>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
						
			
			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/avaliacaoreacao/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="gerar-questionario" type="submit" class="btn btn-primary">
 						Gerar questionário
					</button>

				</div>
			</div>
			
		</form:form>
		<script type="text/javascript">
			jQuery(document).ready(function($) {				
				updateModulo('${procuraModuloUrl}', 'eventoId', 'moduloId', true);				
				
				$('#eventoId').change(function(){
					updateModulo('${procuraModuloUrl}', 'eventoId', 'moduloId', true);
				});				
			});
		</script>
	</div>
</body>
</html>
