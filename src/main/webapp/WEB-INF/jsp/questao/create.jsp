<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="tipoquestao.label" /></title>
</head>
<body>
<div class="body">
<% if (request.getParameter("mensagem")!=null) {%>
	<div class="message"><%= request.getParameter("mensagem")%></div>
<% } %>
<h1><spring:message code="default.add.label" /> - <spring:message code="questao.label" /></h1>

<c:url var="url" value="/questao" />
<form:form action="${url}" method="POST" modelAttribute="questao">
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
				<td valign="top" class="name">(*) Campos Obrigatórios</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name">
					<label for="tipoQuestao"><spring:message code="tipoquestao.label" />:</label>
				</td>
				<td>
					<form:select path="tipoQuestao"  >
						<form:option disabled="true" selected="true" value="0">Selecione...</form:option>
						<form:options items="${listaTiposQuestoes}" itemLabel="descricao" itemValue="id" />	
					</form:select>
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
					<form:select path="modalidades" multiple="true" >
						<form:option disabled="true" selected="true" value="0">Selecione...</form:option>
						<form:options items="${listaModalidades}" itemLabel="descricao" itemValue="id" />	
					</form:select>*
					<%-- <form:select multiple="true" path="modalidades" items="${listaModalidades}" itemLabel="descricao" itemValue="id" />* --%>
					<form:errors path="modalidades" cssClass="error" />
				</td>
			</tr>
		</tbody>
	</table>
	</div>
	
	
	<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/questao/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="criar" type="submit" class="btn btn-primary">
 						<spring:message code="default.add.label" />
					</button>

				</div>
			</div>
	
</form:form>
</div>
</body>
</html>
