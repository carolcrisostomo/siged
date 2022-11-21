<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="default.button.edit.label" /> - <spring:message
	code="frequencia.label" /></title>
<spring:url value="/frequencia/procuraModulo"
	var="procuraModuloUrl" />
<spring:url value="/frequencia/procuraParticipante"
	var="procuraParticipanteUrl" />
</head>
<body>

<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1><spring:message code="default.button.edit.label" /> - <spring:message
	code="frequencia.label" /></h1>
<c:url var="url" value="/frequencia/${frequencia.id}" /> <form:form
	action="${url}" method="PUT" modelAttribute="frequencia">
	<div class="dialog">
	<table>
		<tbody>
			<tr>
				<td></td>
				<td style="text-align: right;" valign="top" class="name">(*)
					Campos Obrigat�rios</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message
					code="frequencia.evento.label" />:</label></td>
				<td>
					${frequencia.eventoId}
					<form:hidden path="eventoId.id" />				
				</td>				
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="modulo"><spring:message
					code="frequencia.modulo.label" />:</label></td>
				<td>
					${frequencia.moduloId}
					<form:hidden path="moduloId.id" />
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="data"><spring:message
					code="frequencia.data.label" />:</label></td>
				<td><fmt:formatDate pattern="dd/MM/yyyy" value="${frequencia.data}"/>
				<form:hidden path="data" />
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="turno"><spring:message
					code="frequencia.turno.label" />:</label></td>
				<td>${frequencia.turno}</td>
				<form:hidden path="turno" />
			</tr>
			<tr class="prop">
				<td valign="top" class="name" nowrap="nowrap">
					<label for="participante"><spring:message code="participante.label" />:<br/>(Para selecionar mais de um,<br/> segure a tecla Control e clique)</label>
				</td>
				<td valign="top">					
					<form:select id="participantes" path="participanteList" multiple="true" size="${listaParticipante.size()}" maxsize="50">												
						<form:options items="${listaParticipante}" itemValue="id" itemLabel="nomeCpf"></form:options>					
					</form:select>* <form:errors path="participanteList" cssClass="error" />
				</td>
			</tr>
		</tbody>
	</table>
	</div>
	
		
		<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
		
				<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/frequencia/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="atualizar" type="submit" class="btn btn-primary">
 						<spring:message code="default.button.save.label" />
					</button>

				</div>
			</div>
		</sec:authorize>
	<form:hidden path="id" />
</form:form></div>
</body>
</html>
