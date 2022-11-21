<%@ include file="../includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
		<
  		<div class="body">
			<h1><spring:message code="default.button.show.label" /> - Material Did�tico / Divulga��o</h1>
			<c:url var="url" value="/material/${material.id}" />
			<div class="dialog">
				<table>
					<tbody>
						<tr class="prop">
							<td valign="top" class="name">Tipo Material:</td>
							<td valign="top" class="value">
								<c:if test="${material.materialTipo eq 1}">
									<c:out value="MATERIAL DID�TICO" />
								</c:if>
								<c:if test="${material.materialTipo eq 2}">
									<c:out value="MATERIAL DE DIVULGA��O" />
								</c:if>
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name">Evento:</td>
							<td valign="top" class="value">${material.eventoId}</td>
						</tr>
						
						<c:if test="${material.moduloId != null}">							
							<tr class="prop">
								<td valign="top" class="name">M�dulo:</td>
								<td valign="top" class="value">${material.moduloId}</td>
							</tr>
						</c:if>
						
						<tr class="prop">
							<td valign="top" class="name">Descri��o:</td>
							<td valign="top" class="value">${material.descricao}</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name">Nome do arquivo original:</td>
							<td valign="top" class="value">${material.arquivoOriginal}</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name">Nome do arquivo armazenado:</td>
							<td valign="top" class="value">${material.arquivoTCE}</td>
						</tr>						
					</tbody>
				</table>
			</div>
			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/material/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>

				</div>
			</div>
		</div>
</body>
</html>
