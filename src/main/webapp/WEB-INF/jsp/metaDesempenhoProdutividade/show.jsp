<%@ include file="../includes.jsp" %>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Indicadores</title>
</head>
<body>
	
 		<div class="body">
 			<c:if test="${param.mensagem != null}">
			<div class="message">${param.mensagem}</div>
		</c:if>
		<c:if test="${param.mensagemErro != null}">
			<div class="messageErro">${param.mensagemErro}</div>
		</c:if>
 	  	<h1><spring:message code="meta.desempenhoProdutividade.label" /></h1>
   	
   		<c:url var="url" value="/meta/desempenhoProdutividade/${meta.id}" />
  	    	<div class="dialog">
			<table>
				<tbody>
					<tr class="prop">
						<td valign="top" class="name"><label for="ano">Ano:</label>
						</td>
						<td valign="top" class="value">${meta.ano}
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="semestre">Semestre:</label>
						</td>
						<td valign="top" class="value">${meta.semestre}
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="semestre"><spring:message code="meta.desempenhoProdutividade.meta1.label" />:</label>
						</td>
						<td valign="top" class="value">${meta.indiceCapacitacaoPercent}
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="semestre"><spring:message code="meta.desempenhoProdutividade.meta2.label" />:</label>
						</td>
						<td valign="top" class="value">${meta.indiceAvaliacaoReacaoPercent}
						</td>
					</tr>
				</tbody>
			</table>
		</div>
			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/meta/desempenhoProdutividade/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>

				</div>
			</div>
	</div>
</body>
</html>
