<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>

<script type="text/javascript">

</script>

<div class="body">
	<c:if test="${param.mensagem != null}">
		<div class="message">${param.mensagem}</div>
	</c:if>
	<c:if test="${param.mensagemErro != null}">
		<div class="messageErro">${param.mensagemErro}</div>
	</c:if>
	<h1><spring:message code="relatorio.atividadesTrimestralAnualIPC.label" /></h1>
	<c:url var="url" value="/relatorio/atividadesTrimestralAnualIPC/" /> 
	<form:form action="${url}" method="POST" modelAttribute="relAtividadesTrimestralAnualIPC" id="form1">
		<div class="filter">
			<table>
				<tbody>		
					<tr class="prop">
						<td valign="top" class="name"><label for="trimestre_data1">Período Realização</label></td>
						<td valign="top">
							<form:input path="dataInicio1" id="dataInicio1" alt="date" />
							<form:errors path="dataInicio1" cssClass="error" /> a 
							<form:input path="dataInicio2" id="dataInicio2" alt="date" />
							<form:errors path="dataInicio2" cssClass="error" />
						</td>
					</tr>
					
			      	<tr>
		                <td>
		                   <input id="filtrar" type="submit" class="botao" value="Filtrar" />
		                </td>
		                <td></td>
		                </tr>
				</tbody>
			</table>
		</div>
	</form:form>
</div>
</body>
</html>