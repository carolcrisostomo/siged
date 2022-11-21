<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Trocar Senha</title>
</head>
<body>
	<div class="body">
		<h1>Trocar Senha</h1>
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>
		<c:if test="${mensagemSucesso != null}">
			<div class="messageSucesso"><c:out value="${mensagemSucesso}"></c:out></div>
		</c:if>	
		<c:url var="url" value="/usuario/trocarsenha" />
		<form:form action="${url}" method="PUT"
			modelAttribute="usuarioexterno">
			<div class="dialog">
				<table>
					<tbody>
						<tr class="prop">
							<td valign="top" class="name"><label for="senha">Nova
									senha</label></td>
							<td><form:password maxlength="255" path="password"	size="30" /> <form:errors path="password" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="repetirsenha">Repita
									a nova senha</label></td>
							<td><form:password maxlength="255" path="repetirsenha" size="30" /> <form:errors path="repetirsenha" cssClass="error" /></td>						
						</tr>
					</tbody>
				</table>
			</div>
			
		
				<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">
					<button  id="atualizar" type="submit" class="btn btn-primary" onclick="confirma()">
 						<spring:message code="default.button.save.label" />
					</button>

				</div>
			</div>
			
			
		<%-- 	<div class="senhaButton">
				<input id="atualizar" type="submit" class="senhaButton"
					value="<spring:message code="default.button.save.label" />"
					onclick="confirma()" />
			</div>
			 --%>
			<form:hidden path="id" />
		</form:form>
	</div>
</body>
</html>
