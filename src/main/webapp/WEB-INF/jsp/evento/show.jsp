<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css" media="screen">
@import url("<c:url value='/static/styles/style.css'/>");
</style>
<title><spring:message code="evento.label" /></title>
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
			<spring:message code="evento.label" />
		</h1>
		<c:url var="url" value="/evento/${evento.id}" />
		<div class="dialog">
			<table>
				<tbody>
					<tr class="prop">
						<td valign="top" class="name"><label for="titulo"><spring:message
									code="evento.titulo.label" />:</label></td>
						<td valign="top" class="value">${evento.titulo}</td>
					</tr>

					<tr class="prop">
						<td valign="top" class="name"><label for="tipoEvento"><spring:message
									code="evento.tipoEvento.label" />:</label></td>
						<td valign="top" class="value">${evento.tipoId}</td>
					</tr>
					
					<tr class="prop">
						<td valign="top" class="name"><label for="objetivoGeral"><spring:message
									code="evento.objetivoGeral.label" />:</label></td>
						<td valign="top" class="value">${fn:replace(evento.objetivoGeral,linefeed,
							'<br>')}</td>
					</tr>
					
					<tr class="prop">
						<td valign="top" class="name"><label for="objetivoEspecifico"><spring:message
									code="evento.objetivoEspecifico.label" />:</label></td>
						<td valign="top" class="value">${fn:replace(evento.objetivoEspecifico,linefeed,
							'<br>')}</td>
					</tr>
					<!--  
					<tr class="prop">
						<td valign="top" class="name"><label for="paisId"><spring:message code="tipoLocalizacaoEvento.pais.label" />:</label>
						</td>
						<td valign="top" class="value">${tipolocalizacaoevento.paisId}
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="uf"><spring:message code="tipoLocalizacaoEvento.uf.label" />:</label>
						</td>
						<td valign="top" class="value">${tipolocalizacaoevento.municipio.ufMunicipio}
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="cidadeId"><spring:message code="tipoLocalizacaoEvento.cidade.label" />:</label>
						</td>
						<td valign="top" class="value">${fn:toUpperCase(tipolocalizacaoevento.municipio.nome)}
						</td>
					</tr>
					<!-- Cidade de outro pa�s 
					<tr class="prop">
						<td valign="top" class="name"><label for="cidadeId"><spring:message code="tipoLocalizacaoEvento.cidade.label" />:</label>
						</td>
						<td valign="top" class="value">${tipolocalizacaoevento.cidadePais}
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label
							for="modalidade"><spring:message
									code="modulo.modalidade.label" />:</label></td>
						<td valign="top" class="value">${modulo.modalidade}</td>
					</tr>
					
					<tr class="prop">
						<td valign="top" class="name"><label
							for="tipoLocalizacaoEvento"><spring:message
									code="modulo.localizacao.label" />:</label></td>
						<td valign="top" class="value">${modulo.localizacaoId}</td>
					</tr>-->
					<tr class="prop">
						<td valign="top" class="name"><label for="eixoTematicoId"><spring:message
									code="evento.eixoTematico.label" />:</label></td>
						<td valign="top" class="value">${evento.eixoTematicoId}</td>
					</tr>
					
					<tr class="prop">
						<td valign="top" class="name"><label for="programas"><spring:message
									code="evento.programa.label.plural" />:</label></td>
						<td valign="top" class="value">${evento.programaLista}</td>
					</tr>
					
					<tr class="prop">
						<td valign="top" class="name"><label for="publicoAlvo"><spring:message
									code="evento.publicoAlvo.label" />:</label></td>
						<td valign="top" class="value">${evento.publicoAlvoLista}</td>
					</tr>
					
						<tr class="prop">
							<td valign="top" class="name"><label for="administrador"><spring:message
										code="evento.monitor.label" />:</label></td>
							<td valign="top" class="value">${evento.administrador}</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="conteudista1"><spring:message
										code="evento.conteudista1.label" />:</label></td>
							<td valign="top" class="value">${evento.conteudista1}</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="conteudista2"><spring:message
										code="evento.conteudista2.label" />:</label></td>
							<td valign="top" class="value">${evento.conteudista2}</td>
						</tr>
					
					
					<tr class="prop">
						<td valign="top" class="name"><label for="cargaHoraria"><spring:message
									code="evento.cargaHoraria.label" />:</label></td>
						<td valign="top" class="value">${evento.cargaHoraria}</td>
					</tr>
					
					<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
						<tr class="prop">
							<td valign="top" class="name"><label for="vagas">
								<spring:message	code="evento.vagas.label" />:</label>
							</td>
							<td valign="top" class="value">${evento.vagas}</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label
								for="permitePreInscricao"><spring:message
										code="evento.permitePreInscricao.label" />:</label></td>
							<td valign="top" class="value">${evento.permitePreInscricao}
							</td>
						</tr>
					</sec:authorize>
					
					<tr class="prop">
						<td valign="top" class="name"><label
							for="dataInicioPreInscricao"><spring:message
									code="evento.dataInicioPreInscricao.label" />:</label></td>
						<td valign="top" class="value"><fmt:formatDate
								pattern="dd/MM/yyyy" value="${evento.dataInicioPreInscricao}" />
						</td>
					</tr>
					
					<tr class="prop">
						<td valign="top" class="name"><label
							for="dataFimPreInscricao"><spring:message
									code="evento.dataFimPreInscricao.label" />:</label></td>
						<td valign="top" class="value"><fmt:formatDate
								pattern="dd/MM/yyyy" value="${evento.dataFimPreInscricao}" /></td>
					</tr>
					
					<tr class="prop">
						<td valign="top" class="name"><label for="dataInicioPrevisto"><spring:message
									code="evento.dataInicioPrevisto.label" />:</label></td>
						<td valign="top" class="value"><fmt:formatDate
								pattern="dd/MM/yyyy" value="${evento.dataInicioPrevisto}" /></td>
					</tr>
					
					<tr class="prop">
						<td valign="top" class="name"><label for="dataFimPrevisto"><spring:message
									code="evento.dataFimPrevisto.label" />:</label></td>
						<td valign="top" class="value"><fmt:formatDate
								pattern="dd/MM/yyyy" value="${evento.dataFimPrevisto}" /></td>
					</tr>
					<c:forEach items="${eventohistorico}" var="h">
						<tr class="prop">
							<td></td>
							<td>> Data de In�cio Previsto: <fmt:formatDate
									pattern="dd/MM/yyyy" value="${h.dataInicioPrevisto}" /><br />
								> Data de Fim Previsto: <fmt:formatDate pattern="dd/MM/yyyy"
									value="${h.dataFimPrevisto}" /></td>
						</tr>
					</c:forEach>
					
					<tr class="prop">
						<td valign="top" class="name"><label
							for="dataInicioRealizacao"><spring:message
									code="evento.dataInicioRealizacao.label" />:</label></td>
						<td valign="top" class="value"><fmt:formatDate
								pattern="dd/MM/yyyy" value="${evento.dataInicioRealizacao}" /></td>
					</tr>
					
					<tr class="prop">
						<td valign="top" class="name"><label for="dataFimRealizacao"><spring:message
									code="evento.dataFimRealizacao.label" />:</label></td>
						<td valign="top" class="value"><fmt:formatDate
								pattern="dd/MM/yyyy" value="${evento.dataFimRealizacao}" /></td>
					</tr>
					
					<%-- <tr class="prop">
						<td valign="top" class="name"><label for="localizacao"><spring:message
									code="evento.tipoLocalizacaoEvento.label" />:</label></td>
						<td valign="top" class="value">${evento.localizacaoId}</td>
					</tr> --%>
					
					<tr class="prop">
						<td valign="top" class="name"><label for="conteudo"><spring:message
									code="evento.conteudo.label" />:</label></td>
						<td valign="top" class="value">${fn:replace(evento.conteudo,linefeed,
							'<br>')}</td>
					</tr>

					<tr class="prop">
						<td valign="top" class="name"><label for="resultadoEsperado"><spring:message
									code="evento.resultadoEsperado.label" />:</label></td>
						<td valign="top" class="value">${evento.resultadoEsperado}</td>
					</tr>
					
					<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
						<tr class="prop">
							<td valign="top" class="name"><label for="observacoes">
								<spring:message	code="evento.observacoes.label" />:</label>
							</td>
							<td valign="top" class="value">
								${fn:replace(evento.observacoes,linefeed,'<br>')}
							</td>
						</tr>
					</sec:authorize>
					
					<tr class="prop">
						<td valign="top" class="name"><label for="observacoesPublicas">
							<spring:message	code="evento.observacoesPublicas.label" />:</label>
						</td>
						<td valign="top" class="value">
							${fn:replace(evento.observacoesPublicas,linefeed,'<br>')}
						</td>
					</tr>
					
					<tr class="prop">
						<td valign="top" class="name"><label for="provedor"><spring:message
									code="evento.provedorEvento.label.plural" />:</label></td>
						<td valign="top" class="value">${evento.provedoresLista}</td>
					</tr>
					
					<tr class="prop">
						<td valign="top" class="name"><label for="areaTribunalId"><spring:message
									code="evento.tipoAreaTribunal.label" />:</label></td>
						<td valign="top" class="value">${evento.areaTribunalId}</td>
					</tr>
					
					<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
						<tr class="prop">
							<td valign="top" class="name"><label for="permiteCertificado"><spring:message
										code="evento.permiteCertificado.label" />:</label></td>
							<td valign="top" class="value">${evento.permiteCertificado}</td>
						</tr>
					
						<tr class="prop">
							<td valign="top" class="name"><label for="publicado"><spring:message
										code="evento.publicado.label" />:</label></td>
							<td valign="top" class="value">${evento.publicado}</td>
						</tr>
						
						<c:if test="${evento.interessados != null and evento.interessados.size() > 0}">
							<tr class="prop">
								<td valign="top" class="name"><label for="interessados">Interessados:</label></td>
								<td valign="top" class="value">
									<ul>
									<c:forEach items="${evento.interessados}" var="interessado">
										<li>${interessado.nomeCpf}</li>
									</c:forEach>
									</ul>
								</td>
							</tr>
						</c:if>
					
						<tr class="prop">
							<td valign="top" class="name"><label for="identificador"><spring:message
										code="evento.identificador.label" />:</label></td>
							<td valign="top" class="value">${evento.identificador}</td>
						</tr>
					</sec:authorize>
					
					<%-- 
					<tr class="prop">
						<td valign="top" class="name"><label for="imagem"><spring:message
									code="evento.imagem.label" />:</label></td>
						<c:if test="${evento.imagem == null}" var="existe">
							<td>Sem Imagem</td>
						</c:if>
						<c:if test="${not existe}">
							<td valign="top" class="value"><c:url var="urlimagem"
									value="/evento/imagem/${evento.id}" /> <img width="383"
								height="400" src="${urlimagem}"></td>
						</c:if>
					</tr> 
					--%>
					
					<tr class="prop">
						<td valign="top" class="name"><label for="responsavelEvento"><spring:message
									code="evento.responsavelEvento.label" />:</label></td>
						<td valign="top" class="value">${evento.responsavelEvento}</td>
					</tr>
					
					<tr class="prop">
						<td valign="top" class="name"><label for="linkDoEvento">Link para o evento:</label></td>
						<td valign="top" class="value">${evento.linkEvento}</td>
					</tr>
					
					<tr class="prop">
						<td valign="top" class="name"><label for="linkDaGravacao">Link para a grava��o:</label></td>
						<td valign="top" class="value">${evento.linkGravacao}</td>
					</tr>
					
					<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
						<tr class="prop">
							<td valign="top" class="name"><label for="moduloUnico"><spring:message
										code="evento.modulounico.label" />:</label></td>
							<td valign="top" class="value">${evento.moduloUnico}</td>
						</tr>
					</sec:authorize>
					
					<tr class="prop">
						<td valign="top" class="name"><label for="modulos"><spring:message
									code="evento.modulos.label" />:</label></td>
						<td valign="top" class="value">
							<ul>
								<c:forEach items="${evento.moduloList}" var="modulo">
									<li>${modulo.titulo}</li>
								</c:forEach>
							</ul>
						</td>
					</tr>
					
					<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
						<tr class="prop">
							<td valign="top" class="name"><label for="dataCadastro"><spring:message
										code="evento.dataCadastro.label" />:</label></td>
							<td valign="top" class="value"><fmt:formatDate
									pattern="dd/MM/yyyy" value="${evento.dataCadastro}" /></td>
						</tr>
					</sec:authorize>
					
				</tbody>
			</table>
			
			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

				<sec:authorize ifNotGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
					
							</sec:authorize>
					<a href="<c:url value="/evento/meuseventos"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
							<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
					<a href="<c:url value="/evento/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
							</sec:authorize>

				</div>
			</div>
			
		</div>
		
	</div>
</body>
</html>