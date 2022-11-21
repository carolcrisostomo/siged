<%@ include file="../includes.jsp"%>

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Indicadores</title>
</head>

<body>
	<script type="text/javascript">
		jQuery.mask.masks.percent = { mask: '99,991?', type: 'reverse' }
	</script>
	<div class="body">
 		<c:if test="${param.mensagem != null}">
			<div class="message">${param.mensagem}</div>
		</c:if>
		<c:if test="${param.mensagemErro != null}">
			<div class="messageErro">${param.mensagemErro}</div>
		</c:if>
		<h1>
			<spring:message code="default.add.label" />
			-
			<spring:message code="meta.desempenhoProdutividade.label" />
		</h1>

		<c:url var="url" value="/meta/desempenhoProdutividade/" />
		<form:form action="${url}" method="POST" modelAttribute="meta">
			<div class="dialog">
				<table>
					<tbody>
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">(*) Campos Obrigatórios</td>
						</tr>
						
						
						<tr class="prop">
							<td valign="top" class="name">
								<label for="ano">Ano:</label>
							</td>
							<td>
								<form:select path="ano">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${anosList}" />
								</form:select>
								* <form:errors path="ano" cssClass="error" />
							</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name">
								<label for="semestre">Semestre:</label>
							</td>
							<td>
								<form:select path="semestre">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${semestresList}" />
								</form:select>
								* <form:errors path="semestre" cssClass="error" />
							</td>
						</tr>
						
						
						<tr class="prop">
							<td valign="top" class="name">
								<label for="indiceCapacitacao"><spring:message code="meta.desempenhoProdutividade.meta1.label" />:</label>
							</td>
							<td>
								<form:input cssStyle="width:110px" path="indiceCapacitacao" alt="percent"/>%
									* <form:errors path="indiceCapacitacao" cssClass="error" />
							</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name">
								<label for="indiceAvaliacaoReacao"><spring:message code="meta.desempenhoProdutividade.meta2.label" />:</label>
							</td>
							<td>
								<form:input cssStyle="width:110px" path="indiceAvaliacaoReacao" alt="percent"/>%
									* <form:errors path="indiceAvaliacaoReacao" cssClass="error" />
							</td>
						</tr>
						
					</tbody>
				</table>
			</div>

	<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/meta/desempenhoProdutividade/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="criar" type="submit" class="btn btn-primary">
 						<spring:message code="default.add.label" />
					</button>

				</div>
			</div>
		
		</form:form>
	</div>
</body>
</html>
