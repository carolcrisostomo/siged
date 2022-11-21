<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css" media="screen">
@import url("<c:url value="/static/styles/style.css"/>");
</style>
<title><spring:message code="responsavelSetor.label" /></title>
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
			<spring:message code="default.button.show.label" />
			-
			<spring:message code="responsavelSetor.label" />
		</h1>
		<c:url var="url" value="/responsavelsetor/${responsavelSetor.id}" />
		<div class="dialog">
			<table>
				<tbody>
					<tr class="prop">
						<td valign="top" class="name">
							<label for="setor"><spring:message code="responsavelSetor.setor.descricao.label" />:</label>
						</td>
						<td valign="top" class="value">${responsavelSetor.setor.descricao}</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name">
							<label for="responsavel"><spring:message code="responsavelSetor.responsavel.nome.label" />:</label>
						</td>
						<td valign="top" class="value">${responsavelSetor.responsavel.nome}</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name">
							<label for="responsavelImediato"><spring:message code="responsavelSetor.responsavelImediato.nome.label" />:</label>
						</td>
						<td valign="top" class="value">
							<c:if test="${responsavelSetor.responsavelImediato.id == 0}">
								-
							</c:if>
							<c:if test="${responsavelSetor.responsavelImediato.id != 0}">
								${responsavelSetor.responsavelImediato}
							</c:if>
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name">
							<label for="ativo"><spring:message code="responsavelSetor.ativo.label" />:</label></td>
						<td valign="top" class="value">
							<c:if test="${responsavelSetor.ativo == true}">S</c:if>
							<c:if test="${responsavelSetor.ativo == false}">N</c:if>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
				<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/responsavelsetor/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>

				</div>
			</div>
		</sec:authorize>
	</div>
</body>
</html>
