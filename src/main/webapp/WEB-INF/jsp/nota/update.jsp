<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="default.button.edit.label" /> - <spring:message
		code="nota.label" /></title>
<spring:url value="/nota/procuraModulo" var="procuraModuloUrl" />
<spring:url value="/nota/procuraParticipante"
	var="procuraParticipanteUrl" />
</head>
<body>
	
	<div class="body">
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>
		<h1>
			<spring:message code="default.button.edit.label" />
			-
			<spring:message code="nota.label" />
		</h1>
		<c:url var="url" value="/nota/${nota.id}" />
		<form:form action="${url}" method="PUT" modelAttribute="nota">
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
										code="nota.evento.label" />:</label></td>
							<td><form:select path="eventoId" id="eventoId"
									onchange="updateModulo('${procuraModuloUrl}', 'eventoId', 'moduloId');updateParticipante('${procuraParticipanteUrl}', 'eventoId', 'participanteId')"
									items="${eventoList}" itemLabel="titulo" itemValue="id">
									<form:option value="0">Selecione...</form:option>
									<c:forEach items="${eventoList}" var="evento">
										<form:option value="${evento.id}">${evento.nome}</form:option>
									</c:forEach>
								</form:select>* <form:errors path="eventoId" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="modulo"><spring:message
										code="nota.modulo.label" />:</label></td>
							<td><form:select path="moduloId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${listaModulo}" itemLabel="titulo"
										itemValue="id" />
								</form:select>* <form:errors path="moduloId" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="participante"><spring:message
										code="nota.participante.label" />:</label></td>
							<td><form:select path="participanteId" id="participanteId"
									items="${listaParticipante}" itemValue="id" itemLabel="nomeCpf">
									<form:option value="0">Selecione...</form:option>
								</form:select>* <form:errors path="participanteId" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="nota"><spring:message
										code="nota.nota.label" />:</label></td>
							<td><form:input cssStyle="width:40px" maxlength="255"
									path="nota" size="30" />* <form:errors path="nota"
									cssClass="error" /></td>
						</tr>
					</tbody>
				</table>
			</div>
			
		
		
				<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/nota/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="atualizar" type="submit" class="btn btn-primary">
 						<spring:message code="default.button.save.label" />
					</button>

				</div>
			</div>
			<form:hidden path="id" />
		</form:form>
	</div>
</body>
</html>
