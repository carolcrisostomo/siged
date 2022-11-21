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
	
  	<div class="body">
  		<% if (request.getParameter("mensagem")!=null) {%>
  			<div class="message"><%= request.getParameter("mensagem")%></div>
  		<% } %>
  		<h1><spring:message code="default.button.show.label" /> - <spring:message code="avaliacao.label" /></h1>
    	<c:url var="url" value="/avaliacaoreacao/${avaliacaoReacao.id}" />
    	<div class="dialog">
			<table>
				<tbody>
					<tr class="prop">
						<td valign="top" class="name3"><label for="evento">
							<spring:message code="avaliacao.evento.label" />:</label>
						</td>
						<td valign="top" class="value3">
							${avaliacaoReacao.modulo.eventoId}
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name3">
							<label for="modulo"><spring:message code="avaliacao.modulo.label" />:</label>
						</td>
						<td valign="top" class="value3">
							${avaliacaoReacao.modulo}
						</td>
					</tr>
		
					<tr class="prop">
						<td valign="top" class="name3">
							<label for="participante"><spring:message code="avaliacao.participante.label" />:</label>
						</td>
						<td valign="top" class="value3">
						<c:if test="${avaliacaoReacao.participante != null}">
							${avaliacaoReacao.participante}
						</c:if>
						<c:if test="${avaliacaoReacao.participante == null}">
							N�O IDENTIFICADO
						</c:if>	
						</td>
					</tr>
					<c:if test="${instrutor != null}">
						<tr class="prop">
							<td valign="top" class="name3" colspan="2">
							<h3>Avalia��o do Instrutor 1</h3>
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name3">
								<label for="instrutor"><spring:message code="avaliacao.instrutor.label" />:</label>
							</td>
							<td valign="top" class="value3">
								${instrutor}
							</td>
						</tr>
						<c:forEach items="${avaliacoesInstrutor}" var="avaliacao" varStatus="status">
							<tr class="prop">
								<td valign="top" class="name3">
									<label for="instrutor">${avaliacao.questao.descricao}</label>
								</td>
								<c:if test="${avaliacao.notaQuestao.descricao != null}">
									<td valign="top" class="value3">
										${avaliacao.notaQuestao.descricao}
									</td>
								</c:if>
								<c:if test="${avaliacao.notaQuestao.descricao == null}">
									<td valign="top" class="value3">
										 - 
									</td>
								</c:if>
							</tr>
						</c:forEach>
					</c:if>
					<c:if test="${instrutor2 != null}">
						<tr class="prop">
							<td valign="top" class="name3" colspan="2">
							<h3>Avalia��o do Instrutor 2</h3>
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name3">
								<label for="instrutor"><spring:message code="avaliacao.instrutor.label" />:</label>
							</td>
							<td valign="top" class="value3">
								${instrutor2}
							</td>
						</tr>
						<c:forEach items="${avaliacoesInstrutor2}" var="avaliacao" varStatus="status">
							<tr class="prop">
								<td valign="top" class="name3">
									<label for="instrutor">${avaliacao.questao.descricao}</label>
								</td>
								<c:if test="${avaliacao.notaQuestao.descricao != null}">
									<td valign="top" class="value3">
										${avaliacao.notaQuestao.descricao}
									</td>
								</c:if>
								<c:if test="${avaliacao.notaQuestao.descricao == null}">
									<td valign="top" class="value3">
										 - 
									</td>
								</c:if>
							</tr>
						</c:forEach>
					</c:if>
					<c:if test="${instrutor3 != null}">
						<tr class="prop">
							<td valign="top" class="name3" colspan="2">
							<h3>Avalia��o do Instrutor 3</h3>
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name3">
								<label for="instrutor"><spring:message code="avaliacao.instrutor.label" />:</label>
							</td>
							<td valign="top" class="value3">
								${instrutor3}
							</td>
						</tr>
						<c:forEach items="${avaliacoesInstrutor3}" var="avaliacao" varStatus="status">
							<tr class="prop">
								<td valign="top" class="name3">
									<label for="instrutor">${avaliacao.questao.descricao}</label>
								</td>
								<c:if test="${avaliacao.notaQuestao.descricao != null}">
									<td valign="top" class="value3">
										${avaliacao.notaQuestao.descricao}
									</td>
								</c:if>
								<c:if test="${avaliacao.notaQuestao.descricao == null}">
									<td valign="top" class="value3">
										 - 
									</td>
								</c:if>
							</tr>
						</c:forEach>
					</c:if>
					
					
					<tr class="prop">
						<td valign="top" class="name3" colspan="2">
						<h3>Avalia��o do Conte�do</h3>
						</td>
					</tr>
					<c:forEach items="${avaliacoesConteudo}" var="avaliacao" varStatus="status">
						<tr class="prop">
							<td valign="top" class="name3">
								<label for="instrutor">${avaliacao.questao.descricao}</label>
							</td>
							<c:if test="${avaliacao.notaQuestao.descricao != null}">
								<td valign="top" class="value3">
									${avaliacao.notaQuestao.descricao}
								</td>
							</c:if>
							<c:if test="${avaliacao.notaQuestao.descricao == null}">
								<td valign="top" class="value3">
									 - 
								</td>
							</c:if>
						</tr>
					</c:forEach>
					
					<tr class="prop">
						<td valign="top" class="name3" colspan="2">
						<h3>Auto-Avalia��o</h3>
						</td>
					</tr>
					<c:forEach items="${avaliacoesAutoAvaliacao}" var="avaliacao" varStatus="status">
						<tr class="prop">
							<td valign="top" class="name3">
								<label for="instrutor">${avaliacao.questao.descricao}</label>
							</td>
							<c:if test="${avaliacao.notaQuestao.descricao != null}">
								<td valign="top" class="value3">
									${avaliacao.notaQuestao.descricao}
								</td>
							</c:if>
							<c:if test="${avaliacao.notaQuestao.descricao == null}">
								<td valign="top" class="value3">
									 - 
								</td>
							</c:if>
						</tr>
					</c:forEach>
					
					<tr class="prop">
						<td valign="top" class="name3" colspan="2">
						<h3>Avalia��o Log�stica</h3> 
						</td>
					</tr>
					<c:forEach items="${avaliacoesLogistica}" var="avaliacao" varStatus="status">
						<tr class="prop">
							<td valign="top" class="name3">
								<label for="instrutor">${avaliacao.questao.descricao}</label>
							</td>
							<c:if test="${avaliacao.notaQuestao.descricao != null}">
								<td valign="top" class="value3">
									${avaliacao.notaQuestao.descricao}
								</td>
							</c:if>
							<c:if test="${avaliacao.notaQuestao.descricao == null}">
								<td valign="top" class="value3">
									 - 
								</td>
							</c:if>
						</tr>
					</c:forEach>
					
					
					<tr class="prop">
						<td valign="top" class="name3"><label for="observacao"><spring:message code="avaliacao.observacao.label" />:</label>
						</td>
						<td valign="top" class="value3">${avaliacaoReacao.observacao}
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name3"><label for="datainclusao"><spring:message code="avaliacao.datainclusao.label" />:</label>
						</td>
						<td valign="top" class="value3"><fmt:formatDate pattern="dd/MM/yyyy" value="${avaliacaoReacao.dataCadastro}"/>
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name3"><label for="">Resultado da avalia��o: </label>
						</td>
						<td valign="top" class="value3">
							<c:if test="${avaliacaoReacao.satisfatoria}">
								<b>Satisfat�ria</b> 
							</c:if>
							<c:if test="${!avaliacaoReacao.satisfatoria}">
								<b>N�o satisfat�ria</b> 
							</c:if>
							( ${avaliacaoReacao.totalSatisfatorias} satisfat�rias de ${avaliacaoReacao.totalRespondidas} respondidas )
						</td>
					</tr>
				</tbody>
			</table>
			</div>
			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/avaliacaoreacao/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>

				</div>
			</div>
	</div>
</body>
</html>
