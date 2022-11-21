<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="formacaoAcademica.label" /></title>
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
			<spring:message code="default.add.label" />
			-
			<spring:message code="formacaoAcademica.label" />
		</h1>

		<c:url var="url" value="/formacaoacademica" />
		<form:form action="${url}" method="POST"
			modelAttribute="formacaoacademica">
			<div class="dialog">
				<table>
					<tbody>
						<tr class="prop">
							<td valign="top" class="name"><label for="nome"><spring:message
										code="formacaoAcademica.descricao.label" />:</label></td>

							<td><form:input cssStyle="width:400px" maxlength="255"
									path="descricao"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="30" />* <form:errors path="descricao" cssClass="error" /></td>
							<td valign="top" class="name">(*) Campos Obrigatórios</td>
						</tr>

					</tbody>
				</table>
			</div>


			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/formacaoacademica/"/>"><button
							type="button" class="btn btn-outline-secondary">
							<spring:message code="default.button.list.label" />
						</button></a>
					<button id="criar" type="submit" class="btn btn-primary">
						<spring:message code="default.add.label" />
					</button>

				</div>
			</div>
		</form:form>
	</div>
</body>
</html>
