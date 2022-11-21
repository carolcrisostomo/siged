<%@ include file="../includes.jsp" %>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <style type="text/css" media="screen">   
    @import url("<c:url value="/static/styles/style.css"/>");
  </style> 
<title><spring:message code="provedorEvento.label" /></title>
</head>
<body>

<div class="body">	
	<h1><spring:message code="default.button.show.label" /> - <spring:message code="provedorEvento.label" /></h1>
	<% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
  	<c:url var="url" value="/provedorevento/${provedorevento.id}" />
  	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="descricao"><spring:message code="provedorEvento.descricao.label" />:</label></td>
				<td valign="top" class="value">${provedorevento.descricao}</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="cnpj"><spring:message code="provedorEvento.cnpj.label" />:</label></td>
				<td valign="top" class="value">${provedorevento.cnpj}</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="pais"><spring:message code="provedorEvento.pais.label" />:</label></td>
				<td valign="top" class="value">${provedorevento.paisId}</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="uf"><spring:message code="provedorEvento.uf.label" />:</label></td>
				<td valign="top" class="value">${provedorevento.municipio.ufMunicipio}</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="cidade"><spring:message code="provedorEvento.cidade.label" />:</label></td>
				<td valign="top" class="value">${fn:toUpperCase(provedorevento.municipio.nome)}</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="logradouro"><spring:message code="provedorEvento.logradouro.label" />:</label></td>
				<td valign="top" class="value">${provedorevento.logradouro}</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="numero"><spring:message code="provedorEvento.numero.label" />:</label></td>
				<td valign="top" class="value">${provedorevento.numero}</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="complemento"><spring:message code="provedorEvento.complemento.label" />:</label></td>
				<td valign="top" class="value">${provedorevento.complemento}</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="bairro"><spring:message code="provedorEvento.bairro.label" />:</label></td>
				<td valign="top" class="value">${provedorevento.bairro}</td>
			</tr>			
			<tr class="prop">
				<td valign="top" class="name"><label for="cep"><spring:message code="provedorEvento.cep.label" />:</label></td>
				<td valign="top" class="value">${provedorevento.cep}</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="contato"><spring:message code="provedorEvento.contato.label" />:</label></td>
				<td valign="top" class="value">${provedorevento.contato}</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="email"><spring:message code="provedorEvento.email.label" />:</label></td>
				<td valign="top" class="value">${provedorevento.email}</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="telefone"><spring:message code="provedorEvento.telefone.label" />:</label></td>
				<td valign="top" class="value">${provedorevento.telefone}</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="celular"><spring:message code="provedorEvento.celular.label" />:</label></td>
				<td valign="top" class="value">${provedorevento.celular}</td>
			</tr>
		</tbody>
		</table>
	</div>
		<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/provedorevento/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>

				</div>
			</div>
</div>
</body>
</html>
