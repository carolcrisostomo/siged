<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="responsavelSetor.label" /></title>
</head>
<body>
	<div class="body">
		<%
			if (request.getParameter("mensagem") != null) {
		%>
		<div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>
		<h1>
			<spring:message code="default.button.edit.label" />
			-
			<spring:message code="responsavelSetor.label" />
		</h1>

		<c:url var="url" value="/responsavelsetor" />
		
		<form:form action="${url}" method="PUT" modelAttribute="responsavelSetor">
			
			<div class="dialog">
				<table>
					<tbody>
						
						<form:hidden path="id" />
						
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
							<td>
								<form:select path="responsavelImediato">
									<form:options items="${responsavelImediatoList}" itemLabel="nome" itemValue="id" />
								</form:select> <form:errors path="responsavelImediato" cssClass="error" />
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
					<button  id="atualizar" type="submit" class="btn btn-primary">
 						<spring:message code="default.button.save.label" />
					</button>

				</div>
			</div>
		</sec:authorize>
		
		</form:form>
	</div>
</body>
</html>
