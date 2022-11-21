<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Esqueci Minha Senha</title>
</head>
<body>
	<div class="body">
		<h1>Esqueci Minha Senha</h1>
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>
		<div class="message">Por favor, informe o CPF e o endere�o de E-mail
			cadastrados. Uma nova senha ser� enviada para este endere�o.</div>
		<div class="message">Se voc� � servidor do TCE ou administrador do sistema deve solicitar
			sua senha junto � Secretaria de TI (ela � a mesma para todos os
			sistemas).</div>		
		<c:url var="url" value="/usuario/esquecisenha" />
		<form:form action="${url}" method="POST" modelAttribute="usuarioexterno">
			<div class="dialog">
				<table>
					<tbody>
						<tr>
							<td style="width: 10%;"></td>
							<td style="text-align: right;" valign="top" class="name">(*) Campos Obrigat�rios</td>
						</tr>
						<tr>
							<td style="width: 10%;">
								<label for="cpf"><spring:message code="usuario.cpf.label" /></label>
							</td>
							<td><form:input cssStyle="width:110px" maxlength="50"
									path="cpf" size="30" alt="cpf" />* <form:errors path="cpf"
									cssClass="error" /></td>
						</tr>
						<tr>
							<td style="width: 10%;">
								<label for="email"><spring:message code="usuario.email.label" /></label>
							</td>
							<td><form:input cssStyle="width:250px" maxlength="50"
									path="email" size="30" name="email" />* <form:errors
									path="email" cssClass="error" /></td>							
						</tr>
					</tbody>
				</table>
			</div>
			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<button  id="atualizar" type="submit" class="btn btn-primary">
 						Enviar
					</button>

				</div>
			</div>
		</form:form>
	</div>
</body>
</html>
