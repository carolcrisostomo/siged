<%@ include file="../includes.jsp" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="programa.label" /></title>
</head>
<body>

  		<div class="body">
  		<c:if test="${param.mensagem != null}">
			<div class="message">${param.mensagem}</div>
		</c:if>
  		<h1><spring:message code="default.button.show.label" /> - <spring:message code="programa.label" /></h1>
    	<c:url var="url" value="/programa/${programa.id}" />
    	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="nome"><spring:message code="programa.descricao.label" />:</label>
				</td>
				<td valign="top" class="value">${programa.descricao}
				</td>
			</tr>
		</tbody>
		</table>
	</div>
	
    	<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/programa/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>

				</div>
			</div>
</div>
</body>
</html>
