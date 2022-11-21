<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="usuario.label" /></title>
</head>
<body>
	
	<div class="body">				
		<h1>
			Resetar Senha
		</h1>
		
		<c:if test="${param.mensagem != null}">
			<div class="message">${param.mensagem}</div>
		</c:if>
		<c:if test="${param.mensagemErro != null}">
			<div class="messageErro">${param.mensagemErro}</div>
		</c:if>
		<div class="filter">
			<c:url var="url" value="/usuario/resetarSenha" />
			<form:form action="${url}" method="POST" modelAttribute="usuarioexterno">
			<div class="dialog">
				<table>
					<tbody>
						<tr>
							<td style="width: 10%;"></td>
							<td style="text-align: right;" valign="top" class="name">(*) Campos Obrigatórios</td>
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
 						Resetar senha
					</button>

				</div>
			</div>
		</form:form>
		</div>
	</div>
</body>
</html>
