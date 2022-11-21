<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css" media="screen">
	@import url("<c:url value="/static/styles/style.css"/>");
</style>
<title><spring:message code="eventoRecomendar.label" /></title>
</head>
<body>
	
	<div class="body">
		
		<h1>
			<spring:message code="default.button.show.label" />
			-
			<spring:message code="eventoRecomendar.label" />
		</h1>
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>
		<c:url var="url" value="/eventorecomendar/${eventorecomendar.id}" />
		<div class="dialog">
			<table>
				<tbody>
					<tr class="prop">
						<td valign="top" class="name"><label for="titulo"><spring:message
									code="eventoRecomendar.titulo.label" />:</label></td>
						<td valign="top" class="value">${eventorecomendar.titulo}</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="objetivo"><spring:message
									code="eventoRecomendar.objetivo.label" />:</label></td>
						<td valign="top" class="value">${eventorecomendar.objetivo}</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="cargaHoraria"><spring:message
									code="eventoRecomendar.cargaHoraria.label" />:</label></td>
						<td valign="top" class="value">${eventorecomendar.cargaHoraria}
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="dataInicio"><spring:message
									code="eventoRecomendar.dataInicio.label" />:</label></td>
						<td valign="top" class="value"><fmt:formatDate
								pattern="dd/MM/yyyy" value="${eventorecomendar.dataInicio}" /></td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="dataFim"><spring:message
									code="eventoRecomendar.dataFim.label" />:</label></td>
						<td valign="top" class="value"><fmt:formatDate
								pattern="dd/MM/yyyy" value="${eventorecomendar.dataFim}" /></td>
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
							target="_blank">${eventorecomendar.materialNome}</a></td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="setor"><spring:message
									code="eventoRecomendar.setor.label" />:</label></td>
						<td valign="top" class="value">${eventorecomendar.setorId}</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="valor"><spring:message
									code="eventoRecomendar.valor2.label" />:</label></td>
						<td valign="top" class="value"><fmt:formatNumber
								type="currency">${eventorecomendar.valor}</fmt:formatNumber></td>
					</tr>
					<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
						<tr class="prop">
							<td valign="top" class="name"><label for="observacao"><spring:message
										code="eventoRecomendar.observacao.label" />:</label></td>
							<td valign="top" class="value">${eventorecomendar.observacao}
							</td>
						</tr>
					</sec:authorize>	
					
					<tr class="prop">
						<td valign="top" class="name"><label for="aprovado"><spring:message
									code="eventoRecomendar.aprovado.label" />:</label></td>
						<td valign="top" class="value">${eventorecomendar.aprovado}</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="justificativa"><spring:message
									code="eventoRecomendar.justificativa.label" />:</label></td>
						<td valign="top" class="value">${eventorecomendar.justificativa}
						</td>
					</tr>
					<%-- <tr class="prop">
						<td valign="top" class="name"><label for="justificativachefe">Justificativa
								Chefe:</label></td>
						<td valign="top" class="value">${eventorecomendar.justificativachefe}
						</td>
					</tr> --%>
					<tr class="prop">
						<td valign="top" class="name"><label for="dataCadastro"><spring:message
									code="eventoRecomendar.dataCadastro.label" />:</label></td>
						<td valign="top" class="value"><fmt:formatDate
								pattern="dd/MM/yyyy" value="${eventorecomendar.dataCadastro}" /></td>
					</tr>

				</tbody>
			</table>
		</div>
		<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/eventorecomendar/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>

				</div>
			</div>
</body>
</html>
