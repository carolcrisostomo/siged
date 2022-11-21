<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>
<script type="text/javascript">
	jQuery(document).ready(function($){
		
		tipoParticipanteChange();
		
		$('#tipoParticipanteId' ).change(function() {
			tipoParticipanteChange();
	    });
		
		function tipoParticipanteChange(){
			if ($('#tipoParticipanteId').val() == "2") {
				mostrarEsfera(true);
			} else {
				mostrarEsfera(false);
			}
		}
		
		function mostrarEsfera(mostrar){
			if(mostrar){
				$('#esferaTr').show();	
			} else {
				$('#esferaTr').hide();
				$('#checkTodasEsferas').attr('checked', 'checked');
				
			}
		}
	
	});
</script>

	<div class="body">
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<% } %>
		
		<h1>Outros Indicadores - Médias</h1>

		<c:if test="${mensagem != null}">
			<div class="message">${mensagem}</div>
		</c:if>

		<c:url var="url" value="/relatorio/indicadoresOutros/" />
		
		<form:form action="${url}" method="POST" modelAttribute="relIndicadoresOutros">
			
			<div class="filter">
				
				<table>
					<tbody>
						
						<tr class="prop">
							<td valign="top" class="name"><label for="dataInicio1">Período Realização</label></td>
							<td valign="top"><form:input path="dataInicio1" alt="date" />
							 a <form:input path="dataInicio2" id="data2" alt="date" /> 
							 	<form:errors path="dataInicio1" cssClass="error" />
								<form:errors path="dataInicio2" cssClass="error" />
							</td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="tipoParticipanteId"><spring:message code="participante.tipoPublicoAlvo.label" /></label></td>
							<td valign="top">
								<form:select path="tipoParticipanteId">
									<form:option value="0">TODOS</form:option>
									<form:options items="${tipoPublicoAlvoList}" itemLabel="descricao" itemValue="id" />
								</form:select>
							</td>
						</tr>
						
						<tr class="prop" id="esferaTr">
							<td valign="top" class="name"><label for=""><spring:message	code="administracaoPublica" />:</label></td>
							<td>
								<label>
									<form:radiobutton path="administracaoPublica" value="todas" id="checkTodasEsferas"  /> 
									Todas
								</label>
								<label>
									<form:radiobutton path="administracaoPublica" value="estadual" /> 
									Estadual
								</label>
								<label>
									<form:radiobutton path="administracaoPublica"  value="municipal" /> 
									Municipal
								</label>
								<label>
									<form:radiobutton path="administracaoPublica"  value="demais casos" /> 
									Demais Casos
								</label>
							</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label for=tipoEventoId><spring:message code="evento.tipoEvento.label" /></label></td>
							<td valign="top">
								<form:select path="tipoEventoId" id="evento_tipo_id">
									<form:option value="0">TODOS</form:option>
									<form:options items="${tipoEventoList}" itemLabel="descricao" itemValue="id" />
								</form:select>
							</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label for="modalidadeId"><spring:message code="evento.modalidade.label" /></label></td>
							<td>
								<form:select path="modalidadeId">
									<form:option value="0">TODOS</form:option>
									<form:options items="${modalidadeList}" itemLabel="descricao" itemValue="id" />
								</form:select>
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