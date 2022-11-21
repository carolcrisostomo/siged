<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css" media="screen">
@import
url(
"<c:url value="
/
static
/styles/style.css"/>");
</style>
<title><spring:message code="modulo.label" /></title>
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
			<spring:message code="modulo.label" />
		</h1>
		<c:url var="url" value="/modulo/${modulo.id}" />
		<div class="dialog">
			<table>
				<tbody>
					<tr class="prop">
						<td valign="top" class="name"><label for="evento"><spring:message
									code="modulo.evento.label" />:</label></td>
						<td valign="top" class="value">${modulo.eventoId}</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="titulo"><spring:message
									code="modulo.titulo.label" />:</label></td>
						<td valign="top" class="value">${modulo.titulo}</td>
					</tr>
					<c:if test="${!modulo.modalidade.isEAD()}">
					
					<tr class="prop">
						<td valign="top" class="name"><label for="paisId"><spring:message
									code="tipoLocalizacaoEvento.pais.label" />:</label></td>
						<td valign="top" class="value">${modulo.paisId}
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="uf"><spring:message
									code="tipoLocalizacaoEvento.uf.label" />:</label></td>
						<td valign="top" class="value">${modulo.municipio.ufMunicipio}
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="cidadeId"><spring:message
									code="tipoLocalizacaoEvento.cidade.label" />:</label></td>
						<td valign="top" class="value">${fn:toUpperCase(modulo.municipio.nome)}
						</td>
					</tr>

					<tr class="prop" id="cidadePaisLinha">
						<td valign="top" class="name"><label for="cidadeId"><spring:message
									code="tipoLocalizacaoEvento.cidade.label" />:</label></td>
						<td valign="top" class="value">${modulo.cidadePais}
						</td>
					</tr>
			      </c:if>

					<tr class="prop">
						<td valign="top" class="name"><label for="modalidade"><spring:message
									code="modulo.modalidade.label" />:</label></td>
						<td valign="top" class="value">${modulo.modalidade}</td>
					</tr>

					<tr class="prop">
						<td valign="top" class="name"><label
							for="tipoLocalizacaoEvento"><spring:message
									code="modulo.localizacao.label" />:</label></td>
						<td valign="top" class="value">${modulo.localizacaoId}</td>
					</tr>


					<tr class="prop">
						<td valign="top" class="name"><label for="cargaHoraria"><spring:message
									code="modulo.cargaHoraria.label" />:</label></td>
						<td valign="top" class="value">${modulo.cargaHoraria}</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="dataInicio"><spring:message
									code="modulo.dataInicio.label" />:</label></td>
						<td valign="top" class="value"><fmt:formatDate
								pattern="dd/MM/yyyy" value="${modulo.dataInicio}" /></td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="dataFim"><spring:message
									code="modulo.dataFim.label" />:</label></td>
						<td valign="top" class="value"><fmt:formatDate
								pattern="dd/MM/yyyy" value="${modulo.dataFim}" /></td>
					</tr>
					<c:if
						test="${modulo.modalidade.id == 1 || modulo.modalidade.id == 2}">
						<tr class="prop">
							<td valign="top" class="name"><label for="horaInicioTurno1"><spring:message
										code="modulo.horaInicioTurno1.label" />:</label></td>
							<td valign="top" class="value">${modulo.horaInicioTurno1}</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="horaFimTurno1"><spring:message
										code="modulo.horaFimTurno1.label" />:</label></td>
							<td valign="top" class="value">${modulo.horaFimTurno1}</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="horaInicioTurno2"><spring:message
										code="modulo.horaInicioTurno2.label" />:</label></td>
							<td valign="top" class="value">${modulo.horaInicioTurno2}</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="horaFimTurno2"><spring:message
										code="modulo.horaFimTurno2.label" />:</label></td>
							<td valign="top" class="value">${modulo.horaFimTurno2}</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="horaInicioTurno3"><spring:message
										code="modulo.horaInicioTurno3.label" />:</label></td>
							<td valign="top" class="value">${modulo.horaInicioTurno3}</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="horaFimTurno3"><spring:message
										code="modulo.horaFimTurno3.label" />:</label></td>
							<td valign="top" class="value">${modulo.horaFimTurno3}</td>
						</tr>
					</c:if>
					<tr class="prop">
						<td valign="top" class="name"><label for="nota"><spring:message
									code="modulo.nota.label" />:</label></td>
						<td valign="top" class="value">${modulo.nota}</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="frequencia"><spring:message
									code="modulo.frequenciaMinima.label" />:</label></td>
						<td valign="top" class="value">${modulo.frequencia}</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="observacao"><spring:message
									code="modulo.observacao.label" />:</label></td>
						<td valign="top" class="value">${modulo.observacao}</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="instrutor"><spring:message
									code="modulo.instrutor.label" />:</label></td>
						<td valign="top" class="value">
							<ul>
								<c:forEach items="${modulo.instrutorList}" var="ins">
									<li>${ins.nome}</li>
								</c:forEach>
							</ul>
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="dataCadastro"><spring:message
									code="modulo.dataCadastro.label" />:</label></td>
						<td valign="top" class="value"><fmt:formatDate
								pattern="dd/MM/yyyy" value="${modulo.dataCadastro}" /></td>
					</tr>
				</tbody>
			</table>
			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/modulo/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>

				</div>
			</div>
		</div>
		
	</div>
</body>
</html>
