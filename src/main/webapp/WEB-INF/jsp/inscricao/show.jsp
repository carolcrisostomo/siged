<%@ include file="../includes.jsp" %>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <style type="text/css" media="screen">   
    @import url("<c:url value="/static/styles/style.css"/>");
  </style> 
<title><spring:message code="inscricao.label" /></title>
</head>
<body>
	
    
	<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
	<h1><spring:message code="default.button.show.label" /> - <spring:message code="inscricao.label" /></h1>
   	<c:url var="url" value="/inscricao/${inscricao.id}" />
   	<div class="dialog">
	
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message code="inscricao.evento.label" />:</label>
				</td>
				<td valign="top" class="value">${inscricao.eventoId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="participante"><spring:message code="inscricao.participante.label" />:</label>
				</td>
				<td valign="top" class="value">${inscricao.participanteId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="email"><spring:message code="participante.email.label" />:</label>
				</td>
				<td valign="top" class="value">${inscricao.participanteId.email}
				</td>
			</tr>
			 <tr class="prop">
				<td valign="top" class="name"><label for="tipoId">Tipo Participante:</label>
				</td>
				<td valign="top" class="value">${inscricao.participanteId.tipoId}
				</td>
			</tr>
			<c:if test="${inscricao.participanteId.tipoId.id eq 3}">
				<tr class="prop">
					<td valign="top" class="name"><label for="entidade"><spring:message code="participante.entidade.label" />:</label>
					</td>
					<td valign="top" class="value">${inscricao.participanteId.entidade}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="profissao"><spring:message code="participante.profissao.label" />:</label>
					</td>
					<td valign="top" class="value">${inscricao.participanteId.profissao}
					</td>
				</tr>
			</c:if>
			<c:if test="${inscricao.participanteId.tipoId.id eq 1 or inscricao.participanteId.tipoId.id eq 4}">
				<tr class="prop">
					<td valign="top" class="name"><label for="setor"><spring:message code="participante.setor.label" />:</label>
					</td>
					<td valign="top" class="value">${inscricao.participanteId.setorId.descricao}
				</td>
			</tr>
			</c:if>
			<c:if test="${inscricao.participanteId.tipoId.id eq 2}">
		 		<tr class="prop">
					<td valign="top" class="name"><label for="orgaoId">�rg�o:</label>
					</td>
					<td valign="top" class="value">${inscricao.participanteId.orgaoId.dsentidade}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="lotacao"><spring:message code="participante.lotacao.label" />:</label>
					</td>
					<td valign="top" class="value">${inscricao.participanteId.lotacao}
					</td>
				</tr>
		 	</c:if>
		 	<tr class="prop">
				<td valign="top" class="name"><label for="data"><spring:message code="inscricao.data.label" />:</label>
				</td>
				<td valign="top" class="value"><fmt:formatDate pattern="dd/MM/yyyy" value="${inscricao.data}"/>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="confirmada"><spring:message code="inscricao.justificativaparticipante.label" />:</label>
				</td>
				<td valign="top" class="value">${inscricao.justificativaParticipante}
				</td>
			</tr>
		
			<c:if test="${inscricao.participanteId.tipoId.id eq 1 or inscricao.participanteId.tipoId.id eq 4}">			
				<tr class="prop">
					<td valign="top" class="name"><label for="indicada"><spring:message code="inscricao.indicada.label" />:</label>
					</td>
					<td valign="top" class="value">${inscricao.indicada}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="dataIndicacao"><spring:message code="inscricao.dataIndicacao.label" />:</label>
					</td>
					<td valign="top" class="value"><fmt:formatDate pattern="dd/MM/yyyy" value="${inscricao.dataIndicacao}"/>
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="chefe"><spring:message code="inscricao.chefe.label" />:</label>
					</td>
					<c:if test="${inscricao.chefeId != null && inscricao.chefeId.id == 0}">
						<td valign="top" class="value">N�o exigido</td>
					</c:if>
					<c:if test="${inscricao.chefeId == null || inscricao.chefeId.id != 0}">
						<td valign="top" class="value">${inscricao.chefeId}</td>
					</c:if>
					
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="justificativa">Justificativa Chefe:</label>
					</td>
					<td valign="top" class="value">${inscricao.justificativachefe}
					</td>
				</tr>
			</c:if>
		
			<tr class="prop">
				<td valign="top" class="name"><label for="confirmada"><spring:message code="inscricao.confirmada.label" />:</label>
				</td>
				<td valign="top" class="value">${inscricao.confirmada}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="dataConfirmacao"><spring:message code="inscricao.dataConfirmacao.label" />:</label>
				</td>
				<td valign="top" class="value"><fmt:formatDate pattern="dd/MM/yyyy" value="${inscricao.dataConfirmacao}"/>
				</td>
			</tr>
			
			<c:if test="${inscricao.confirmada eq 'N'}">
				<tr class="prop">
					<td valign="top" class="name"><label for="justificativanaoconfirmacao"><spring:message code="inscricao.justificativanaoconfirmacao.label" />:</label>
					</td>
					<td valign="top" class="value">${inscricao.justificativanaoconfirmacao}
					</td>
				</tr>
			</c:if>
			
			<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
				<tr class="prop">
					<td valign="top" class="name"><label for="justificativa"><spring:message code="inscricao.justificativa.label" />:</label>
					</td>
					<td valign="top" class="value">${inscricao.justificativa}
					</td>
				</tr>
			</sec:authorize>
			
			<c:if test="${inscricao.solicitacaoId ne null}">
				<tr class="prop">
					<td valign="top" class="name"><label for="indicada">Solicita��o:</label>
					</td>
					<td valign="top" class="value">${inscricao.solicitacaoId.cursoEData}
					</td>
				</tr>
			</c:if>		
	</tbody>
	</table>
	</div>
		<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/inscricao/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>

				</div>
			</div>
   
	<c:if test="${inscricao.confirmada != 'S' && exibirBotoes}">
		<div class="buttons">
			<c:url var="url" value="/inscricao/${inscricao.id}/minhasinscricoes" />	       
	       	
	       	<form:form action="${url}" method="DELETE">
	       		<input alt="<spring:message code="default.button.delete.label" />" title="<spring:message code="default.button.delete.label" />" type="submit" value="<spring:message code="default.button.delete.label" />" class="delete" onclick="return confirm('<spring:message code="default.areyousure.message" />');"/>
	       	</form:form>
		
		</div>
    </c:if>
</div>
</body>
</html>
