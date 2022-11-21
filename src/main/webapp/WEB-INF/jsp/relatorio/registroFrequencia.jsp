<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
<spring:url value="/frequencia/procuraModulo"
	var="procuraModuloUrl" />
<spring:url value="/frequencia/procuraTurno"
	var="procuraTurnoUrl" />
</head>
<body>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1>Registro de Frequência</h1>

<c:if test="${mensagem != null}">
<div class="message">${mensagem}</div>
</c:if>

<c:url var="url" value="/relatorio/registroFrequencia/" />

<form:form action="${url}" method="POST" modelAttribute="relRegistroFrequencia">
	<div class="filter">
	<table>
		<tbody>
			<tr>
				<td></td>
				<td style="text-align:right;" valign="top" class="name">(*) Campos Obrigatórios</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message
					code="evento.evento.label" /></label></td>
				<td valign="top">
				<form:select path="eventoId" id="evento" onchange="updateModulo('${procuraModuloUrl}', 'evento', 'modulo');">
					<form:option value="0">Selecione...</form:option>
					<form:options items="${eventoPresencialList}" itemLabel="nome" itemValue="id" />
				</form:select>* <br ><form:errors path="eventoId" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td>
					<label for="modulo">Módulo</label>
				</td>
				<td valign="top" class="value">
					<form:select path="modulo" onchange="updateTurno('${procuraTurnoUrl}', 'modulo', 'turno');">
						<form:option value="0">Selecione...</form:option>
					</form:select>* <form:errors path="modulo" cssClass="error" />
				</td>
			</tr>			
			<tr>
				<td>
					<label for="turno">Turno</label>
				</td>
				<td>
					<form:select path="turno">
						<form:option value="">Selecione...</form:option>
					</form:select>* <form:errors path="turno" cssClass="error" />
				</td>
			<tr>
				<td><label for="date">Data</label></td>
				<td valign="top" class="data"><form:input path="data" id="data" alt="date" />* <form:errors path="data" cssClass="error" /></td>
				
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="publicoAlvo">Tipo Participante</label></td>
				<td valign="top">
				<form:select path="publicoAlvoId" id="evento_publico_alvo_id">
					<form:option value="0">TODOS</form:option>
					<form:options items="${tipoPublicoAlvoList}" itemLabel="descricao" itemValue="id" />
				</form:select>
				</td>
			</tr>
			
			      <tr>
                    <td>
                      <input id="filtrar" type="submit" class="botao"
						value="Filtrar" />
                    </td>
                    <td></td>
                  </tr>
                  
		</tbody>
	</table>
	</div>
</form:form></div>
</body>
</html>