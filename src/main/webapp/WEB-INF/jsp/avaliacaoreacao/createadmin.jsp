<%@ include file="../includes.jsp"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title><spring:message code="avaliacao.label" /></title>
</head>

<body>
	<div class="nav">
		<span class="menuButton">
			<a href="<c:url value="/avaliacaoreacao/"/>" class="list"><spring:message code="default.button.list.label" /></a>
		</span>
	</div>
	<div class="body">
		<% if (request.getParameter("mensagem") != null) { %>
			<div class="message"><%=request.getParameter("mensagem")%></div>
		<% } %>

		<div class="message">Pelo menos um dos campos deve ser preenchido (fora a observação)</div>

		<c:if test="${mensagem!=null}">
			<div class="messageErro">${mensagem}</div>
		</c:if>

		<h1>
			<spring:message code="default.add.label" />
			-
			<spring:message code="avaliacao.label" />
		</h1>

		<c:url var="url" value="/avaliacaoreacao/admin" />
		<form:form action="${url}" method="POST" modelAttribute="avaliacaoReacao">
			<div class="dialog">

				<table>
					<tbody>
						<tr class="prop">
							<td valign="top" class="name3">
								<label for="evento"><spring:message code="avaliacao.evento.label" />:</label>
							</td>
							<td>${evento}</td>							
						</tr>
						<tr class="prop">
							<td valign="top" class="name3">
								<label for="modulo"><spring:message code="avaliacao.modulo.label" />:</label>
							</td>
							<td>
								${modulo}
								<form:hidden path="modulo" value="${modulo.id}" />
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name3">
								<label for="participante"><spring:message code="avaliacao.participante.label" />:</label>
							</td>

							<td>
								<c:if test="${participante.nomeCpf != null}">
									${participante.nomeCpf}
								</c:if>
								<c:if test="${participante.nomeCpf == null}">
									NÃO IDENTIFICADO
								</c:if>
								<form:hidden path="participante" value="${participante.id}" /></td>
						</tr>
						
						<c:set value="${0}" var="index"/>
						
						<!-- Verifica se existe o instrutor -->
						<c:if test="${instrutor != 'DIVERSOS'}">
							<tr class="prop">
								<td valign="top" class="name3" colspan="2">
									<h3>Avaliação do Instrutor 1</h3>
								</td>
							</tr>
							<tr class="prop">
								<td valign="top" class="name3"><label for="instrutor"><spring:message
										code="avaliacao.instrutor.label" />:</label></td>
								<td>
									${instrutor}
								</td>
							</tr>
							<c:forEach items="${questoesInstrutorList}" var="questao">
								<c:if test="${(modulo.modalidade.id == 1 and !questao.EADOnly) or 
												(modulo.modalidade.id == 2 and !questao.presencialOnly)}">
									<tr class="prop">
										<form:hidden path="avaliacoesReacaoNota[${index}].questao" value="${questao.id}" />
										<form:hidden path="avaliacoesReacaoNota[${index}].instrutor" value="${instrutor.id}" />
										<td valign="top" class="name3">
											<label for="avaliacoesReacaoNota${index}.notaQuestao">${questao.descricao}:</label>
										</td>
										<td>
											<form:select path="avaliacoesReacaoNota[${index}].notaQuestao">
											    <form:option value="0" label="Selecione..."/>
											    <form:options items="${notasQuestoesList}" itemLabel="descricao" itemValue="id"/>
											</form:select>*
											<form:errors path="avaliacoesReacaoNota[${index}].notaQuestao" cssClass="error" />
										</td>
									</tr>
									<c:set value="${index + 1}" var="index"/>
								</c:if>
							</c:forEach>
						</c:if>
						
						<!-- Verifica se existe o instrutor2 -->
						<c:if test="${instrutor2 != null ||Instrutor2Id}">
							<tr class="prop">
								<td valign="top" class="name3" colspan="2">
									<h3>Avaliação do Instrutor 2</h3>
								</td>
							</tr>
							<tr class="prop">
								<td valign="top" class="name3"><label for="instrutor"><spring:message
										code="avaliacao.instrutor.label" />:</label></td>
								<td>
									${instrutor2}
								</td>
							</tr>
							<c:forEach items="${questoesInstrutorList}" var="questao">
								<c:if test="${(modulo.modalidade.id == 1 and !questao.EADOnly) or 
												(modulo.modalidade.id == 2 and !questao.presencialOnly)}">
									<tr class="prop">
										<form:hidden path="avaliacoesReacaoNota[${index}].questao" value="${questao.id}" />
										<form:hidden path="avaliacoesReacaoNota[${index}].instrutor" value="${instrutor2.id}" />
										<td valign="top" class="name3">
											<label for="avaliacoesReacaoNota${index}.notaQuestao">${questao.descricao}:</label>
										</td>
										<td>
											<form:select path="avaliacoesReacaoNota[${index}].notaQuestao">
											    <form:option value="0" label="Selecione..."/>
											    <form:options items="${notasQuestoesList}" itemLabel="descricao" itemValue="id"/>
											</form:select>*
											<form:errors path="avaliacoesReacaoNota[${index}].notaQuestao" cssClass="error" />
										</td>
									</tr>
									<c:set value="${index + 1}" var="index"/>
								</c:if>
							</c:forEach>
						</c:if>
						
						<!-- Verifica se existe o instrutor3 -->
						<c:if test="${instrutor3 != null ||Instrutor3Id}">
							<tr class="prop">
								<td valign="top" class="name3" colspan="2">
									<h3>Avaliação do Instrutor 3</h3>
								</td>
							</tr>
							<tr class="prop">
								<td valign="top" class="name3"><label for="instrutor"><spring:message
										code="avaliacao.instrutor.label" />:</label></td>
								<td>
									${instrutor3}
								</td>
							</tr>
							<c:forEach items="${questoesInstrutorList}" var="questao">
								<c:if test="${(modulo.modalidade.id == 1 and !questao.EADOnly) or 
												(modulo.modalidade.id == 2 and !questao.presencialOnly)}">
									<tr class="prop">
										<form:hidden path="avaliacoesReacaoNota[${index}].questao" value="${questao.id}" />
										<form:hidden path="avaliacoesReacaoNota[${index}].instrutor" value="${instrutor3.id}" />
										<td valign="top" class="name3">
											<label for="avaliacoesReacaoNota${index}.notaQuestao">${questao.descricao}:</label>
										</td>
										<td>
											<form:select path="avaliacoesReacaoNota[${index}].notaQuestao">
											    <form:option value="0" label="Selecione..."/>
											    <form:options items="${notasQuestoesList}" itemLabel="descricao" itemValue="id"/>
											</form:select>*
											<form:errors path="avaliacoesReacaoNota[${index}].notaQuestao" cssClass="error" />
										</td>
									</tr>
									<c:set value="${index + 1}" var="index"/>
								</c:if>
							</c:forEach>
						</c:if>

						<!-- Avaliação do Conteúdo -->
						<tr class="prop">
							<td valign="top" class="name3" colspan="2">
								<h3>Avaliação do Conteúdo</h3>
							</td>
						</tr>
						<c:forEach items="${questoesConteudoList}" var="questao">
							<c:if test="${(modulo.modalidade.id == 1 and !questao.EADOnly) or 
											(modulo.modalidade.id == 2 and !questao.presencialOnly)}">
								<tr class="prop">
									<form:hidden path="avaliacoesReacaoNota[${index}].questao" value="${questao.id}" />
									<td valign="top" class="name3">
										<label for="avaliacoesReacaoNota${index}.notaQuestao">${questao.descricao}:</label>
									</td>
									<td>
										<form:select path="avaliacoesReacaoNota[${index}].notaQuestao">
											<form:option value="0" label="Selecione..."/>
											<form:options items="${notasQuestoesList}" itemLabel="descricao" itemValue="id"/>
										</form:select>*
										<form:errors path="avaliacoesReacaoNota[${index}].notaQuestao" cssClass="error" />
									</td>
								</tr>
								<c:set value="${index + 1}" var="index"/>
							</c:if>
						</c:forEach>

						<!-- Auto-Avaliação -->
						<tr class="prop">
							<td valign="top" class="name3" colspan="2">
								<h3>Auto-Avaliação</h3>
							</td>
						</tr>
						<c:forEach items="${questoesAutoAvaliacaoList}" var="questao">
							<c:if test="${(modulo.modalidade.id == 1 and !questao.EADOnly) or 
											(modulo.modalidade.id == 2 and !questao.presencialOnly)}">
								<tr class="prop">
									<form:hidden path="avaliacoesReacaoNota[${index}].questao" value="${questao.id}" />
									<td valign="top" class="name3">
										<label for="avaliacoesReacaoNota${index}.notaQuestao">${questao.descricao}:</label>
									</td>
									<td>
										<form:select path="avaliacoesReacaoNota[${index}].notaQuestao">
											<form:option value="0" label="Selecione..."/>
											<form:options items="${notasQuestoesList}" itemLabel="descricao" itemValue="id"/>
										</form:select>*
										<form:errors path="avaliacoesReacaoNota[${index}].notaQuestao" cssClass="error" />
									</td>
								</tr>
								<c:set value="${index + 1}" var="index"/>
							</c:if>
						</c:forEach>

						<!-- Avaliação Logística -->
						<tr class="prop">
							<td valign="top" class="name3" colspan="2">
								<h3>Avaliação Logística</h3>
							</td>
						</tr>
						<c:forEach items="${questoesLogisticaList}" var="questao">
							<c:if test="${(modulo.modalidade.id == 1 and !questao.EADOnly) or 
											(modulo.modalidade.id == 2 and !questao.presencialOnly)}">
								<tr class="prop">
									<form:hidden path="avaliacoesReacaoNota[${index}].questao" value="${questao.id}" />
									<td valign="top" class="name3">
										<label for="avaliacoesReacaoNota${index}.notaQuestao">${questao.descricao}:</label>
									</td>
									<td>
										<form:select path="avaliacoesReacaoNota[${index}].notaQuestao">
											<form:option value="0" label="Selecione..."/>
											<form:options items="${notasQuestoesList}" itemLabel="descricao" itemValue="id"/>
										</form:select>*
										<form:errors path="avaliacoesReacaoNota[${index}].notaQuestao" cssClass="error" />
									</td>
								</tr>
								<c:set value="${index + 1}" var="index"/>
							</c:if>
						</c:forEach>
						
						<tr class="prop">
							<td valign="top" class="name3">
								<label for="observacao"><spring:message code="avaliacao.observacao.label" />:</label>
							</td>
							<td>
								<form:textarea path="observacao" cols="70" rows="5" />
								<form:errors path="observacao" cssClass="error" />
							</td>
						</tr>

					</tbody>
				</table>
			</div>
			<div class="buttons">
				<input id="criar" type="submit" class="save"
					value="<spring:message code="default.add.label" />" />
			</div>
		</form:form>
	</div>
</body>
</html>
