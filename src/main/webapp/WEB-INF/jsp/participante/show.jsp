<%@ include file="../includes.jsp" %>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  	<style type="text/css" media="screen">   
    	@import url("<c:url value="/static/styles/style.css"/>");
  	</style> 
	<title><spring:message code="participante.label" /></title>
</head>
<body>
	
  		<div class="body">
  			<% if (request.getParameter("mensagem")!=null) {%>
  				<div class="message"><%= request.getParameter("mensagem")%></div><% } %>
  	  	<h1><spring:message code="default.button.show.label" /> - <spring:message code="participante.label" /></h1>
    	
    	<c:url var="url" value="/participante/${participante.id}" />
    	    	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="tipoPublicoAlvo"><spring:message code="participante.tipoPublicoAlvo.label" />:</label>
				</td>
				<td valign="top" class="value">${participante.tipoId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="nome"><spring:message code="participante.nome.label" />:</label>
				</td>
				<td valign="top" class="value">${participante.nome}
				</td>
			</tr>			
			<tr class="prop">
				<td valign="top" class="name"><label for="cpf"><spring:message code="participante.cpf.label" />:</label>
				</td>
				<td valign="top" class="value">${participante.cpfFormatado}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="formacaoAcademica"><spring:message code="participante.formacao.label" />:</label>
				</td>
				<td valign="top" class="value">${participante.formacaoId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="nivelEscolaridade"><spring:message code="participante.escolaridade.label" />:</label>
				</td>
				<td valign="top" class="value">${participante.escolaridadeId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="email"><spring:message code="participante.email.label" />:</label>
				</td>
				<td valign="top" class="value">${participante.email}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="telefone"><spring:message code="participante.telefone.label" />:</label>
				</td>
				<td valign="top" class="value">${participante.telefone}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="celular"><spring:message code="participante.celular.label" />:</label>
				</td>
				<td valign="top" class="value">${participante.celular}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="pais"><spring:message code="provedorEvento.pais.label" />:</label></td>
				<td valign="top" class="value">${participante.paisId}</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="ufMunicipio"><spring:message
					code="participante.uf.label" />:</label></td>
				<td valign="top" class="value">${participante.municipio.ufMunicipio}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="municipio"><spring:message
					code="participante.municipio.label" />:</label></td>
				<td valign="top" class="value">${participante.municipio.nome}
				</td>
			</tr>
			<c:if test="${participante.tipoId.id eq 1 or participante.tipoId.id eq 4}">
				<tr class="prop">
					<td valign="top" class="name"><label for="setor"><spring:message code="participante.setor.label" />:</label>
					</td>
					<td valign="top" class="value">${participante.setorId}
					</td>
				</tr>
			</c:if>
			<c:if test="${participante.tipoId.id eq 2}">			
				<tr class="prop">
					<td valign="top" class="name"><label for="matricula"><spring:message code="participante.matricula.label" />:</label>
					</td>
					<td valign="top" class="value">${participante.matricula}
					</td>
				</tr>			
				<tr class="prop">
					<td valign="top" class="name"><label for="cargo"><spring:message code="participante.cargo.label" />:</label>
					</td>
					<td valign="top" class="value">${participante.cargo}
					</td>
				</tr>			
				<tr class="prop">
					<td valign="top" class="name"><label><spring:message code="administracaoPublica" />:</label>
					</td>					
					<td valign="top" class="value">${fn:toUpperCase(participante.administracaoPublica)}</td>					
				</tr>
				<c:if test="${participante.localidade ne null }">
					<tr class="prop">
						<td valign="top" class="name"><label>Munic�pio:</label></td>
						<td valign="top" class="value">${participante.localidade.dsLocalidade}</td>						
					</tr>
				</c:if>
				<tr class="prop">
					<td valign="top" class="name"><label for="orgao"><spring:message code="participante.orgao.label" />:</label>
					</td>
					<c:if test="${participante.orgaoId.identidade ne 0 }">
						<td valign="top" class="value">${participante.orgaoId.dsentidade}</td>
					</c:if>
				</tr>
				<!-- 
				<tr class="prop">
					<td valign="top" class="name"><label for="lotacao"><spring:message code="participante.lotacao.label" />:</label>
					</td>
					<td valign="top" class="value">${participante.lotacao}
					</td>
				</tr>
				-->			
			</c:if>
			<c:if test="${participante.tipoId.id eq 3}">
				<tr class="prop">
					<td valign="top" class="name"><label for="profissao"><spring:message code="participante.profissao.label" />:</label>
					</td>
					<td valign="top" class="value">${participante.profissao}
					</td>
				</tr>				
				
				<tr class="prop">
					<td valign="top" class="name"><label for="entidade"><spring:message code="participante.entidade.label" />:</label>
					</td>
					<td valign="top" class="value">${participante.entidade}
					</td>
				</tr>
			</c:if>				
			
			<tr class="prop">
				<td valign="top" class="name"><label for="observacao"><spring:message code="participante.observacao.label" />:</label>
				</td>
				<td valign="top" class="value">${participante.observacao}
				</td>
			</tr>	
			
			<tr class="prop">
				<td valign="top" class="name"><label for="dataCadastro"><spring:message
							code="participante.dataCadastro.label" />:</label></td>
				<td valign="top" class="value"><fmt:formatDate
								pattern="dd/MM/yyyy" value="${participante.dataCadastro}" /></td>
			</tr>
			
		</tbody>
		</table>
	</div>

<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/participante/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>

				</div>
			</div>
	</div>
</body>
</html>
