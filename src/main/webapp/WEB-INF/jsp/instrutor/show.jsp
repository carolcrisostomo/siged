<%@ include file="../includes.jsp" %>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <style type="text/css" media="screen">   
    @import url("<c:url value="/static/styles/style.css"/>");
  </style> 
<title><spring:message code="instrutor.label" /></title>
</head>
<body>
	
  		<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
  		<h1><spring:message code="default.button.show.label" /> - <spring:message code="instrutor.label" /></h1>
    	<c:url var="url" value="/instrutor/${instrutor.id}" />
    	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="cpf"><spring:message code="instrutor.cpf.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.cpf}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="nome"><spring:message code="instrutor.nome.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.nome}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="logradouro"><spring:message code="instrutor.logradouro.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.logradouro}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="numero"><spring:message code="instrutor.numero.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.numero}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="complemento"><spring:message code="instrutor.complemento.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.complemento}
				</td>
			</tr>
			<!--  
			<tr class="prop">
				<td valign="top" class="name"><label for="uf"><spring:message code="instrutor.uf.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.municipio.ufMunicipio}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="cidade"><spring:message code="instrutor.cidade.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.municipio.nome}
				</td>
			</tr>-->
			<tr class="prop">
				<td valign="top" class="name"><label for="pais"><spring:message code="provedorEvento.pais.label" />:</label></td>
				<td valign="top" class="value">${instrutor.paisId.getDescricao()}</td>
			</tr>
			<c:if test="${instrutor.paisId.getId() == 1}">
			<tr class="prop">
				<td valign="top" class="name"><label for="uf"><spring:message code="provedorEvento.uf.label" />:</label></td>
				<td valign="top" class="value">${instrutor.municipio.ufMunicipio}</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="cidade"><spring:message code="provedorEvento.cidade.label" />:</label></td>
				<td valign="top" class="value">${fn:toUpperCase(instrutor.municipio.nome)}</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="bairro"><spring:message code="instrutor.bairro.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.bairro}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="cep"><spring:message code="instrutor.cep.label" />:</label>
				</td> 
				<td valign="top" class="value">${instrutor.cep}
				</td>
			</tr>
			      </c:if>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="email"><spring:message code="instrutor.email.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.email}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="telefone"><spring:message code="instrutor.telefone1.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.telefone}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="celular"><spring:message code="instrutor.telefone2.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.celular}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="sexo"><spring:message code="instrutor.sexo.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.sexo}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="dataNascimento"><spring:message code="instrutor.dataNascimento.label" />:</label>
				</td>
				<td valign="top" class="value"><fmt:formatDate pattern="dd/MM/yyyy" value="${instrutor.dataNascimento}"/>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="nivelEscolaridade"><spring:message code="instrutor.nivelEscolaridade.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.nivelEscolaridadeId.descricao}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="formacaoAcademica"><spring:message code="instrutor.formacaoAcademica.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.formacaoAcademicaId.descricao}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="setor"><spring:message code="instrutor.setor.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.setorId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="instituicaoVinculo"><spring:message code="instrutor.instituicaoVinculo.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.instituicao}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="tipoInstrutor"><spring:message code="instrutor.tipoInstrutor.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.tipoId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="perfil"><spring:message code="instrutor.perfil.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.perfil}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="observacao"><spring:message code="instrutor.observacao.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.observacao}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="curriculo"><spring:message code="instrutor.curriculo.label" />:</label>
				</td>
				<td valign="top" class="value"><a href="<c:url value="/instrutor/curriculo/${instrutor.id}"/>" target="_blank">${instrutor.curriculoNome}</a>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="assinatura"><spring:message code="instrutor.assinatura.label" />:</label>
				</td>
				<td valign="top" class="value"><a href="<c:url value="/instrutor/assinatura/${instrutor.id}"/>" target="_blank">${instrutor.assinaturaNome}</a>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="situacaoInstrutor"><spring:message code="instrutor.situacaoInstrutor.label" />:</label>
				</td>
				<td valign="top" class="value">${instrutor.situacaoId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="dataCadastro"><spring:message code="instrutor.dataCadastro.label" />:</label>
				</td>
				<td valign="top" class="value"><fmt:formatDate pattern="dd/MM/yyyy - H:m:s" value="${instrutor.dataCadastro}"/>
				</td>
			</tr>
		</tbody>
		</table>
		
				<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/instrutor/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>

				</div>
			</div>
	</div>

</div>
</body>
</html>
