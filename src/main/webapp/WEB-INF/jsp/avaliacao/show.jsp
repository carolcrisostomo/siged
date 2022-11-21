<%@ include file="../includes.jsp" %>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <style type="text/css" media="screen">   
    @import url("<c:url value="/static/styles/style.css"/>");
  </style> 
<title><spring:message code="avaliacao.label" /></title>
</head>
<body>
	<div class="nav">
		<sec:authorize ifAnyGranted="ROLE_SERVIDOR"> 
    	<span class="menuButton"><a href="<c:url value="/avaliacao/minhasavaliacoes"/>" class="list">Listagem</a></span>
    	</sec:authorize>
    	<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR"> 
    	<span class="menuButton"><a href="<c:url value="/avaliacao"/>" class="list"><spring:message code="default.button.list.label" /></a></span>
    	</sec:authorize>
    </div>
  		<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
  		<h1><spring:message code="default.button.show.label" /> - <spring:message code="avaliacao.label" /></h1>
    	<c:url var="url" value="/avaliacao/${avaliacao.id}" />
    	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message code="avaliacao.evento.label" />:</label>
				</td>
				<td valign="top" class="value">${avaliacao.eventoId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="modulo"><spring:message code="avaliacao.modulo.label" />:</label>
				</td>
				<td valign="top" class="value">${avaliacao.moduloId}
				</td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="participante"><spring:message code="avaliacao.participante.label" />:</label>
				</td>
				<td valign="top" class="value">
				<c:if test="${avaliacao.participanteId != null}">
					${avaliacao.participanteId}
					</c:if>
				<c:if test="${avaliacao.participanteId == null}">
					N�O IDENTIFICADO
				</c:if>	
				</td>
			</tr>
		<c:if test="${avaliacao.instrutorId != null }">
				<tr class="prop">
					<td valign="top" class="name" colspan="2">
					<h3>Avalia��o do Instrutor 1</h3>
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="instrutor"><spring:message code="avaliacao.instrutor.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.instrutorId}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor1"><spring:message code="avaliacao.questaoInstrutor1.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor1}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor2"><spring:message code="avaliacao.questaoInstrutor2.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor2}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor3"><spring:message code="avaliacao.questaoInstrutor3.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor3}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor4"><spring:message code="avaliacao.questaoInstrutor4.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor4}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor5"><spring:message code="avaliacao.questaoInstrutor5.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor5}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor6"><spring:message code="avaliacao.questaoInstrutor6.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor6}
					</td>
				</tr>
				<c:if test="${avaliacao.instrutor2Id != null }">
				<tr class="prop">
					<td valign="top" class="name" colspan="2">
					<h3>Avalia��o do Instrutor 2</h3>
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="instrutor"><spring:message code="avaliacao.instrutor.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.instrutor2Id}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor1"><spring:message code="avaliacao.questaoInstrutor1.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor21}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor2"><spring:message code="avaliacao.questaoInstrutor2.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor22}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor3"><spring:message code="avaliacao.questaoInstrutor3.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor23}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor4"><spring:message code="avaliacao.questaoInstrutor4.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor24}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor5"><spring:message code="avaliacao.questaoInstrutor5.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor25}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor6"><spring:message code="avaliacao.questaoInstrutor6.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor26}
					</td>
				</tr>
				</c:if>
				<c:if test="${avaliacao.instrutor3Id != null}">
				<tr class="prop">
					<td valign="top" class="name" colspan="2">
					<h3>Avalia��o do Instrutor 3</h3>
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="instrutor"><spring:message code="avaliacao.instrutor.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.instrutor3Id}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor1"><spring:message code="avaliacao.questaoInstrutor1.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor31}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor2"><spring:message code="avaliacao.questaoInstrutor2.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor32}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor3"><spring:message code="avaliacao.questaoInstrutor3.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor33}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor4"><spring:message code="avaliacao.questaoInstrutor4.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor34}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor5"><spring:message code="avaliacao.questaoInstrutor5.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor35}
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoInstrutor6"><spring:message code="avaliacao.questaoInstrutor6.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoInstrutor36}
					</td>
				</tr>
				</c:if>
		</c:if>
			<tr class="prop">
				<td valign="top" class="name" colspan="2">
				<h3>Avalia��o do Conte�do</h3>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="questaoConteudo1"><spring:message code="avaliacao.questaoConteudo1.label" />:</label>
				</td>
				<td valign="top" class="value">${avaliacao.questaoConteudo1}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="questaoConteudo2"><spring:message code="avaliacao.questaoConteudo2.label" />:</label>
				</td>
				<td valign="top" class="value">${avaliacao.questaoConteudo2}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="questaoConteudo3"><spring:message code="avaliacao.questaoConteudo3.label" />:</label>
				</td>
				<td valign="top" class="value">${avaliacao.questaoConteudo3}
				</td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name" colspan="2">
				<h3>Auto-Avalia��o</h3>
				</td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="questaoAutoAvaliacao1"><spring:message code="avaliacao.questaoAutoAvaliacao1.label" />:</label>
				</td>
				<td valign="top" class="value">${avaliacao.questaoAutoAvaliacao1}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="questaoAutoAvaliacao2"><spring:message code="avaliacao.questaoAutoAvaliacao2.label" />:</label>
				</td>
				<td valign="top" class="value">${avaliacao.questaoAutoAvaliacao2}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name" colspan="2">
			<h3>Avalia��o Log�stica</h3> 
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="questaoOrganizacao"><spring:message code="avaliacao.questaoOrganizacao.label" />:</label>
				</td>
				<td valign="top" class="value">${avaliacao.questaoOrganizacao}
				</td>
			</tr>
			
			<c:if test="${evento.modalidadeId.id ==1}">
				<tr class="prop">
					<td valign="top" class="name"><label for="questaoEspacoFisico"><spring:message code="avaliacao.questaoEspacoFisico.label" />:</label>
					</td>
					<td valign="top" class="value">${avaliacao.questaoEspacoFisico}
					</td>
				</tr>
			</c:if>
			<tr class="prop">
				<td valign="top" class="name"><label for="observacao"><spring:message code="avaliacao.observacao.label" />:</label>
				</td>
				<td valign="top" class="value">${avaliacao.observacao}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="datainclusao"><spring:message code="avaliacao.datainclusao.label" />:</label>
				</td>
				<td valign="top" class="value"><fmt:formatDate pattern="dd/MM/yyyy" value="${avaliacao.dataCadastro}"/>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="">Resultado da avalia��o: </label>
				</td>
				<td valign="top" class="value">
					<c:if test="${avaliacao.satisfatoria}">
						<b>Satisfat�ria</b> 
					</c:if>
					<c:if test="${!avaliacao.satisfatoria}">
						<b>N�o satisfat�ria</b> 
					</c:if>
					( ${avaliacao.totalSatisfatorias} satisfat�rias de ${avaliacao.totalRespondidas} respondidas )
				</td>
			</tr>
	</tbody>
	</table>
	</div>
	<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
	<div class="buttons">
	<c:url var="url" value="/avaliacao/${avaliacao.id}" />
            
            <form:form action="${url}" method="DELETE">
            <input alt="<spring:message code="default.button.delete.label" />" title="<spring:message code="default.button.delete.label" />" type="submit" value="<spring:message code="default.button.delete.label" />" class="delete" onclick="return confirm('<spring:message code="default.areyousure.message" />');"/>
            </form:form>
        </div>
     </sec:authorize>
</div>
</body>
</html>
