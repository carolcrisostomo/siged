<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="certificadoemitido.validar.label" /></title>
</head>
<body>
	<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
		<div class="nav">
			<span class="menuButton"><a href="<c:url value="/nota/form"/>"
				class="create"><spring:message code="default.add.label" /></a></span>
		</div>
	</sec:authorize>
	<div class="body">
				
		<spring:message code="certificadoemitido.evento.label" var="coluna1" />
		<spring:message code="certificadoemitido.participante.label" var="coluna2" />
		<spring:message code="certificadoemitido.dataemissao.label" var="coluna3" />
		<spring:message code="certificadoemitido.codigo.label" var="coluna4" />
		
		<h1>
			<spring:message code="certificadoemitido.validar.label" />
		</h1>		

		<c:if test="${mensagemSucesso != null}">
			<div class="messageSucesso">${mensagemSucesso}</div>
		</c:if>
		<c:if test="${mensagemErro != null}">
			<div class="messageErro">${mensagemErro}</div>
		</c:if>
		<c:if test="${mensagem != null}">
			<div class="message info">${mensagem}</div>
		</c:if>

		<div class="filter">
			<c:url var="url" value="/certificadoEmitido/verificar" />
			<form:form action="${url}" method="POST" modelAttribute="certificadoEmitido">
				<table>
					<tbody>						
						<tr>
							<td style="width: 16%;"><label for="codigoverificacao"><spring:message
										code="certificadoemitido.codigo.label" /></label></td>
							<td><form:input cssStyle="width:250px"
											path="codigoVerificacao"
											onkeyup="javascript:this.value=this.value.toUpperCase();"																					
											alt="codigo-certificado"/>
								<c:if test="${mensagem != null}">
									<span class="error">${mensagem}</span>
								</c:if>
							
							</td>
							
						</tr>
						
						<tr>
							<td><input id="filtrar" type="submit" class="botao"	value="Filtrar" /></td>
							<td></td>
						</tr>

					</tbody>


				</table>
			</form:form>
		</div>
		<div class="list">
			<display:table uid="certificadoEmitido" name="${certificadosEmitidos}" pagesize="10" requestURI="">				
				<display:column property="evento" sortable="true"
					title="${coluna1}" maxLength="80" />
				<display:column property="participante" sortable="true"
					title="${coluna2}" maxLength="80" />
				<display:column sortable="true" title="${coluna3}" maxLength="80" >
					<fmt:formatDate value="${certificadoEmitido.dataEmissao}" pattern="dd/MM/yyyy HH:mm:ss"/>
				</display:column>
				<display:column property="codigoVerificacaoComMascara" sortable="true" title="${coluna4}"
					maxLength="80" />
				<display:column class="crudlist">
					<a
							href="<c:url value="/certificado/certificadoEmitido/${certificadoEmitido.codigoVerificacao}" />"
							title="<spring:message code="evento.emitirCertificado.label" />">
							<img width="auto" height="25px"
							src="<c:url value="/static/images/certificado.png"/>"
							id="icon_certificado" />
					</a>					
				</display:column>
			</display:table>
		</div>
	</div>
</body>
</html>
