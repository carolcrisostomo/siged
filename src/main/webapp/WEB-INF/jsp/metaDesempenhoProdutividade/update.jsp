<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="default.button.edit.label" /> - Indicadores</title>
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
			<spring:message code="default.button.edit.label" />
			-
			<spring:message code="meta.desempenhoProdutividade.label" />
		</h1>
		<c:url var="url" value="/meta/desempenhoProdutividade/${meta.id}" />
		<form:form action="${url}" method="PUT" modelAttribute="meta">
			<div class="dialog">
				<table>
					<tbody>
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">(*) Campos Obrigat�rios</td>
						</tr>
						
						
						<tr class="prop">
							<td valign="top" class="name">
								<label for="ano">Ano:</label>
							</td>
							<td>
								${meta.ano}
							</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name">
								<label for="semestre">Semestre:</label>
							</td>
							<td>
								${meta.semestre}
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
					<button  id="atualizar" type="submit" class="btn btn-primary">
 						<spring:message code="default.button.save.label" />
					</button>

				</div>
			</div>
		
			<form:hidden path="id" />
			<form:hidden path="ano" />
			<form:hidden path="semestre" />
		</form:form>
	</div>

</body>
</html>
