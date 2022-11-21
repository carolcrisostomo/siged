<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1>Crachás</h1>
<c:if test="${mensagemRel != null}">
   		<div class="message" id="msgId"><c:out value="${mensagemRel}" /></div>
</c:if>
<c:url var="url" value="/relatorio/crachas/" />
<spring:url value="/relatorio/procuraParticipantePorEvento"
	var="procuraParticipanteUrl" /> 
<form:form action="${url}" method="POST" modelAttribute="relCracha" id="form1">
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
				<form:select path="eventoId" id="eventoId" onchange="updateParticipante('${procuraParticipanteUrl}', 'eventoId', 'participanteId')">
					<form:option value="0">Selecione...</form:option>
					<form:options items="${eventoList}" itemLabel="nome" itemValue="id" />
				</form:select>*	<br/><form:errors path="eventoId" cssClass="error" />
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message
					code="evento.participante.label" /></label></td>
				<td valign="top">
				<form:select path="participanteId" id="participanteId">
					<form:option value="0">Selecione...</form:option>
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