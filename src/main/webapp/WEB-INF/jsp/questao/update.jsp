<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="default.button.edit.label" /> - <spring:message code="questao.label" /></title>
</head>
<body>

<div class="body">
	<% if (request.getParameter("mensagem")!=null) {%>
		<div class="message"><%= request.getParameter("mensagem")%></div>
	<% } %>
	<h1><spring:message code="default.button.edit.label" /> - <spring:message code="questao.label" /></h1>
	<c:url var="url" value="/questao/${questao.id}" />
	
	<form:form action="${url}" method="PUT" modelAttribute="questao">
		<div class="dialog">
			<table>
				<tbody>
					<tr class="prop">
						<td valign="top" class="name">
							<label for="descricao"><spring:message code="questao.descricao.label" />:</label>
						</td>
						<td>
							<form:textarea path="descricao" rows="3" cols="6"/>* 
							<form:errors path="descricao" cssClass="error" />
						</td>
						<td valign="top" class="name">(*) Campos Obrigat�rios</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name">
							<label for="tipoQuestao"><spring:message code="tipoquestao.label" />:</label>
						</td>
						<td>
							<form:select path="tipoQuestao" items="${listaTiposQuestoes}" itemLabel="descricao" itemValue="id" />* 
							<form:errors path="tipoQuestao" cssClass="error" />
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name">
							<label for="ordem"><spring:message code="questao.ordem.label" />:</label>
						</td>
						<td>
							<form:input cssStyle="width:250px" maxlength="255" path="ordem" size="10" />* 
							<form:errors path="ordem" cssClass="error" />
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name">
							<label for="modalidades"><spring:message code="questao.modalidades.label" />:</label>
						</td>
						<td>
							<form:select multiple="true" path="modalidades" items="${listaModalidades}" itemLabel="descricao" itemValue="id" />* 
							<form:errors path="modalidades" cssClass="error" />
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
		
				<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/questao/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="atualizar" type="submit" class="btn btn-primary">
 						<spring:message code="default.button.save.label" />
					</button>

				</div>
			</div>
		</sec:authorize>
		
		<form:hidden path="id" />
	</form:form>
</div>
</body>
</html>
