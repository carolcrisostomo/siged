<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Atualizar E-mail</title>
</head>
<body>
	<div class="body">
		<h1>Atualizar E-mail</h1>
		<div class="message"><p>Seja bem-vindo ao Sistema de Gest�o Educacional. Para melhorar nossa comunica��o, e garantirmos a sua efetiva participa��o em nossos cursos, gostar�amos que realizasse a atualiza��o do seu e-mail cadastrado, para darmos continuidade ao seu acesso. Agradecemos a vossa aten��o.</p></div>
		<c:url var="url" value="/usuario/atualizaremail" />
		<form:form action="${url}" method="PUT" modelAttribute="usuarioexterno">
			<div class="dialog">
				<table>
					<tbody>											
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">(*)Campo Obrigat�rio</td>
						</tr>						
						<tr class="prop">
							<td valign="top" class="name"><label for="email"><spring:message code="participante.email.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="50" path="email" size="30" />* <form:errors path="email" cssClass="error" /></td>
						</tr>						
					</tbody>
				</table>
			</div>
			<div class="buttons">
				<input id="atualizar" type="submit" class="edit" value="<spring:message code="default.button.save.label" />" onclick="confirma()" />
			</div>
			<form:hidden path="id" />
		</form:form>
	</div>
</body>
</html>