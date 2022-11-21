<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="default.button.edit.label" /> - <spring:message
		code="eventoRecomendar.label" /></title>
</head>
<body>
	<div class="nav">
		<sec:authorize ifAnyGranted="ROLE_CHEFE">
			<span class="menuButton"><a
				href="<c:url value="/inscricao/minhasindicacoes"/>" class="list">Minhas
					Indica��es</a></span>
		</sec:authorize>
	</div>
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
			<spring:message code="eventoRecomendar.label" />
		</h1>
		<c:url var="url"
			value="/eventorecomendar/${eventorecomendar.id}/chefe" />
		<form:form action="${url}" method="POST"
			modelAttribute="eventorecomendar">
			<div class="dialog">
				<table>
					<tbody>
						<tr class="prop">
							<td valign="top" class="name"><label for="titulo"><spring:message
										code="eventoRecomendar.titulo.label" />:</label></td>
							<td valign="top" class="value">${eventorecomendar.titulo}<form:hidden
									path="titulo" />
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="objetivo"><spring:message
										code="eventoRecomendar.objetivo.label" />:</label></td>
							<td valign="top" class="value">${eventorecomendar.objetivo}<form:hidden
									path="objetivo" />
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="cargaHoraria"><spring:message
										code="eventoRecomendar.cargaHoraria.label" />:</label></td>
							<td valign="top" class="value">${eventorecomendar.cargaHoraria}<form:hidden
									path="cargaHoraria" />
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="dataInicio"><spring:message
										code="eventoRecomendar.dataInicio.label" />:</label></td>
							<td valign="top" class="value"><fmt:formatDate
									pattern="dd/MM/yyyy" value="${eventorecomendar.dataInicio}" />
								<form:hidden path="dataInicio" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="dataFim"><spring:message
										code="eventoRecomendar.dataFim.label" />:</label></td>
							<td valign="top" class="value"><fmt:formatDate
									pattern="dd/MM/yyyy" value="${eventorecomendar.dataFim}" /> <form:hidden
									path="dataFim" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="pais"><spring:message
										code="eventoRecomendar.pais.label" />:</label></td>
							<td valign="top" class="value">${eventorecomendar.paisId}</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="uf"><spring:message
										code="eventoRecomendar.uf.label" />:</label></td>
							<td valign="top" class="value">${eventorecomendar.municipio.ufMunicipio}
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="cidade"><spring:message
										code="eventoRecomendar.cidade.label" />:</label></td>
							<td valign="top" class="value">${eventorecomendar.municipio.nome}
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="material"><spring:message
										code="eventoRecomendar.material2.label" />:</label></td>
							<td valign="top" class="value"><a
								href="<c:url value="/eventorecomendar/material/${eventorecomendar.id}"/>"
								target="_blank">${eventorecomendar.materialNome}</a> <form:hidden
									path="material" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="setor"><spring:message
										code="eventoRecomendar.setor.label" />:</label></td>
							<td valign="top" class="value">${eventorecomendar.setorId}<form:hidden
									path="setorId" />
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="valor"><spring:message
										code="eventoRecomendar.valor2.label" />:</label></td>
							<td valign="top" class="value"><fmt:formatNumber
									type="currency">${eventorecomendar.valor}</fmt:formatNumber> <form:hidden
									path="valor" /></td>
						</tr>
						<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
							<tr class="prop">
								<td valign="top" class="name"><label for="observacao"><spring:message
											code="eventoRecomendar.observacao.label" />:</label></td>
								<td valign="top" class="value">${eventorecomendar.observacao}<form:hidden
										path="observacao" />
								</td>
							</tr>
						</sec:authorize>
						<sec:authorize ifAnyGranted="ROLE_CHEFE,ROLE_ADMINISTRADOR">
							<tr class="prop">
								<td valign="top" class="name"><h1>�rea Chefe</h1></td>
								<td></td>
							</tr>

							<tr class="prop">
								<td valign="top" class="name"><label for="aprovado"><spring:message
											code="eventoRecomendar.aprovado.label" />:</label></td>
								<td><form:select path="aprovado" items="${SNList}" /></td>
							</tr>
							<tr class="prop">
								<td valign="top" class="name"><label for="justificativa"><spring:message
											code="eventoRecomendar.justificativa.label" />:</label></td>

								<td><form:textarea path="justificativa" cols="70" rows="5" />
									<form:errors path="justificativa" cssClass="error" /></td>
							</tr>

						</sec:authorize>
					</tbody>
				</table>
			</div>
			<div class="buttons">
				<input id="atualizar" type="submit" class="edit"
					value="<spring:message code="default.button.save.label" />" />
			</div>
			<form:hidden path="id" />
		</form:form>
	</div>
</body>
</html>
