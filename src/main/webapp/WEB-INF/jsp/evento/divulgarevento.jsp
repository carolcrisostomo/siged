<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1>Divulgar Evento</h1>
<c:url var="url" value="/evento/divulgarevento" /> 
<form:form action="${url}" method="POST" modelAttribute="email">
	<div class="dialog">
	<table>
		<tbody>
			<tr>
				<td></td>
				<td style="text-align:right;" valign="top" class="name">(*) Campos Obrigatórios</td>
			</tr>
			<%-- <tr class="prop">
				<td valign="top" class="name">
				<label for="participantes"><spring:message
					code="participante.tipoPublicoAlvo.label" /></label>
				</td>
				<td valign="top">
				<form:select path="publicoAlvoId" id="tipo_publico_alvo">
					<form:option value="0">TODOS</form:option>
					<form:options items="${tipoPublicoAlvoList}" itemLabel="descricao" itemValue="id" />
				</form:select>
				</td>
			</tr> --%>		
			<tr class="prop">
				<td valign="top" class="name">
					<label for="evento"><spring:message code="participante.evento.label" /></label>
				</td>
				<td valign="top">
					<form:select path="eventoId" id="evento">
						<form:option value="0">Selecione...</form:option>
						<form:options items="${eventoList}" itemLabel="nome" itemValue="id" />				
					</form:select>*<br /><form:errors path="eventoId" cssClass="error" />
				</td>
			</tr>	
			<tr class="prop">
				<td>Para</td>
				<td>
					<form:input id="to" path="to" cssStyle="width:500px" />* <form:errors path="to" cssClass="error" />
					<br />Use vírgula para separar e-mails
				</td>
			</tr>
			<tr class="prop">
				<td>Informações Adicionais</td>
				<td>
					<form:textarea id="observacoes" rows="3"
					cssStyle="width:362px" path="observacoes" />
				</td>
			</tr>	
	      	<tr>
				<td>
					<input id="divulgar" type="submit" class="botao"
						value="Divulgar evento" />
				</td>
				<td></td>
			</tr>
		</tbody>
	</table>
	</div>
</form:form></div>
</body>
</html>