<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
<spring:url value="/ajax/evento/statusApuracao" var="procuraEventoApuradoUrl" />
<spring:url value="/ajax/evento/isRealizado" var="procuraEventoRealizadoUrl" />
</head>
<body>
<div class="body">
<c:if test="${param.mensagem != null}">
	<div class="message">${param.mensagem}</div>
</c:if>
<c:if test="${param.mensagemErro != null}">
	<div class="messageErro">${param.mensagemErro}</div>
</c:if>
<h1>Apurar Evento</h1>
<c:url var="url" value="/evento/apurardesempenho" /> 
<form:form action="${url}" method="POST" modelAttribute="evento">
	<div class="dialog">
	<table>
		<tbody>
			
			<tr>
				<td></td>
				<td style="text-align: right;" valign="top" class="name">(*)
					Campo Obrigatório</td>
			</tr>
					
			<tr class="name">
				<td><label for="evento">
					<spring:message code="participante.evento.label" /></label>
				</td>
				<td valign="top">
					<form:select path="id" id="eventoId" name="evento" 
						class="js_evento_select"
						data-evento_realizado_url = "${procuraEventoRealizadoUrl}"
						data-evento_apurado_url = "${procuraEventoApuradoUrl}">
						<form:option value="0">Selecione...</form:option>
						<form:options items="${eventoRealizadoOuEmAndamentoComModuloRealizadoList}" itemLabel="nome" itemValue="id" />
					</form:select>*
				</td>
			</tr>
			
	      	<tr>
				<td></td>
				<td>
					<div id="apurar-div"></div>					
				</td>				
			</tr>
			
			<tr>
				<td></td>
			</tr>
			
			<tr>
				<td colspan="2">
					<input id="apurar" type="submit" class="botao" 
						value="Apurar Evento" onclick="return confirm('<spring:message code="default.areyousure.message" />');" />
				</td>
				<td></td>
			</tr>
		</tbody>
	</table>
	</div>
</form:form></div>
<script type="text/javascript" src="<c:url value="/static/js/ajax.loading.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/evento.apurar.js"/>"></script>
</body>
</html>