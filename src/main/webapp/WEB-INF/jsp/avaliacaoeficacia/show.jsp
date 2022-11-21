<%@ include file="../includes.jsp" %>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <style type="text/css" media="screen">   
    @import url("<c:url value="/static/styles/style.css"/>");
  </style> 
<title><spring:message code="avaliacaoEficacia.label" /></title>
</head>
<body>
	<div class="nav">
	<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
    	<span class="menuButton"><a href="<c:url value="/avaliacaoeficacia/"/>" class="list"><spring:message code="default.button.list.label" /></a></span>
    	<span class="menuButton"><a href="<c:url value="/avaliacaoeficacia/form"/>" class="create"><spring:message code="default.add.label" /></a></span>
    </sec:authorize>
	<sec:authorize ifAnyGranted="ROLE_CHEFE">
		<span class="menuButton"><a href="<c:url value="/avaliacaoeficacia/minhasavaliacoes"/>" class="list"><spring:message code="avaliacaoEficacia.listagem.label" /></a></span>
	</sec:authorize>
    </div>
  		<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
  		<h1><spring:message code="default.button.show.label" /> - <spring:message code="avaliacaoEficacia.label" /></h1>
    	<c:url var="url" value="/avaliacaoeficacia/${avaliacaoeficacia.id}" />
    	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message code="avaliacaoEficacia.evento.label" />:</label>
				</td>
				<td valign="top" class="value">${avaliacaoeficacia.eventoId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="participante"><spring:message code="avaliacaoEficacia.participante.label" />:</label>
				</td>
				<td valign="top" class="value">${avaliacaoeficacia.participanteId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="responsavel">Respons�vel:</label>
				</td>
				<td valign="top" class="value">${avaliacaoeficacia.responsavel}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="melhoria"><spring:message code="avaliacaoEficacia.melhoria.label" />:</label>
				</td>
				<td valign="top" class="value">${avaliacaoeficacia.melhoria}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="desempenhoservico"><spring:message code="avaliacaoEficacia.desempenhoServico.label" />:</label>
				</td>
				<td valign="top" class="value">${avaliacaoeficacia.desempenhoServico}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="observacao"><spring:message code="avaliacaoEficacia.observacao.label" />:</label>
				</td>
				<td valign="top" class="value">${avaliacaoeficacia.observacao}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="datainclusao"><spring:message code="avaliacao.datainclusao.label" />:</label>
				</td>
				<td valign="top" class="value"><fmt:formatDate pattern="dd/MM/yyyy" value="${avaliacaoeficacia.dataCadastro}"/>
				</td>
			</tr>
		</tbody>
		</table>
	</div>
	
       <sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
	<div class="buttons">
	<!--  c:url var="url" value="/avaliacaoeficacia/${avaliacaoeficacia.id}" />
            <form:form action="${url}/form" method="GET">
                <input alt="<spring:message code="default.button.edit.label" />" title="<spring:message code="default.button.edit.label" />" type="submit" value="<spring:message code="default.button.edit.label" />" class="edit"/>
            </form:form>-->

            <form:form action="${url}" method="DELETE">
            <input alt="<spring:message code="default.button.delete.label" />" title="<spring:message code="default.button.delete.label" />" type="submit" value="<spring:message code="default.button.delete.label" />" class="delete" onclick="return confirm('<spring:message code="default.areyousure.message" />');"/>
            </form:form>
        </div>
        </sec:authorize>
</div>
</body>
</html>
