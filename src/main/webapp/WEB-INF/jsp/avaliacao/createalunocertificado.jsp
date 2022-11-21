<%@ include file="../includes.jsp"%>


<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="avaliacao.label" /></title>
<spring:url value="/avaliacao/procuraModulo"
	var="procuraModuloUrl" />
<spring:url value="/avaliacao/procuraParticipante"
	var="procuraParticipanteUrl" />
<spring:url value="/avaliacao/procuraInstrutor"
	var="procuraInstrutorUrl" />	
</head>

<body>
<script type="text/javascript">
	/* jQuery(document).ready(function($) {
		$('#continuar').click(function() {
		});
	}); */
</script>
<div class="buttons">
<span class="menuButton"><a id="continuar" onclick="Modalbox.hide();" href="<c:url value="/certificado/impressao/${evento.id}/${participante.id}"/>" class="list">Continuar</a></span>
</div>
<div class="body">
<h1><spring:message code="default.add.label" /> - <spring:message
	code="avaliacao.label" /></h1>
<% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<c:if test="${mensagemErro != null}">
	<div class="messageErro">${mensagemErro}</div>
</c:if>
<c:url var="url" value="/avaliacao/aluno" /> <form:form action="${url}"
	method="POST" modelAttribute="avaliacao">
	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message
					code="avaliacao.evento.label" />:</label></td>
				<td>
					${evento}
					<form:hidden path="eventoId" value="${evento.id}" />
				</td>
				<td valign="top" class="name">(*) Campos Obrigatórios</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="modulo"><spring:message
					code="avaliacao.modulo.label" />:</label></td>

				<td>
					${modulo}
					<form:hidden path="moduloId" value="${modulo.id}" />
				</td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="participante"><spring:message
					code="avaliacao.participante.label" />:</label></td>

				<td>
					${participante.nomeCpf}
					<form:hidden path="participanteId" value="${participante.id}" />
				</td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name" colspan="2">
				<h3>Avaliação do Instrutor 1</h3>
				</td>
			</tr>
			
			
			<tr class="prop">
				<td valign="top" class="name"><label for="instrutor"><spring:message
					code="avaliacao.instrutor.label" />:</label></td>

				<td>
					${instrutor}
					<form:hidden path="instrutorId" value="${instrutor.id}" />
				</td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor1"><spring:message
					code="avaliacao.questaoInstrutor1.label" />:</label></td>

				<td><form:select path="questaoInstrutor1"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor1" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor2"><spring:message
					code="avaliacao.questaoInstrutor2.label" />:</label></td>

				<td><form:select path="questaoInstrutor2"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor2" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor3"><spring:message
					code="avaliacao.questaoInstrutor3.label" />:</label></td>

				<td><form:select path="questaoInstrutor3"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor3" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor4"><spring:message
					code="avaliacao.questaoInstrutor4.label" />:</label></td>

				<td><form:select path="questaoInstrutor4"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor4" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor5"><spring:message
					code="avaliacao.questaoInstrutor5.label" />:</label></td>

				<td><form:select path="questaoInstrutor5"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor5" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor6"><spring:message
					code="avaliacao.questaoInstrutor6.label" />:</label></td>

				<td><form:select path="questaoInstrutor6"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor6" cssClass="error" /></td>
			</tr>
			<!-- Verifica se existe o instrutor2 -->
			<c:if test="${instrutor2 != null ||Instrutor2Id}">
			<tr class="prop">
				<td valign="top" class="name" colspan="2">
				<h3>Avaliação do Instrutor 2</h3>
				</td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="instrutor"><spring:message
					code="avaliacao.instrutor.label" />:</label></td>

				<td>
					${instrutor2}
					<form:hidden path="instrutor2Id" value="${instrutor2.id}" />
				</td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor1"><spring:message
					code="avaliacao.questaoInstrutor1.label" />:</label></td>

				<td><form:select path="questaoInstrutor21"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor21" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor2"><spring:message
					code="avaliacao.questaoInstrutor2.label" />:</label></td>

				<td><form:select path="questaoInstrutor22"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor22" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor3"><spring:message
					code="avaliacao.questaoInstrutor3.label" />:</label></td>

				<td><form:select path="questaoInstrutor23"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor23" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor4"><spring:message
					code="avaliacao.questaoInstrutor4.label" />:</label></td>

				<td><form:select path="questaoInstrutor24"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor24" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor5"><spring:message
					code="avaliacao.questaoInstrutor5.label" />:</label></td>

				<td><form:select path="questaoInstrutor25"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor25" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor6"><spring:message
					code="avaliacao.questaoInstrutor6.label" />:</label></td>

				<td><form:select path="questaoInstrutor26"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor26" cssClass="error" /></td>
			</tr>
			</c:if>
			<!-- Verifica se existe o instrutor2 -->
			<c:if test="${instrutor3 != null ||Instrutor3Id}">
			<tr class="prop">
				<td valign="top" class="name" colspan="2">
				<h3>Avaliação do Instrutor 3</h3>
				</td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="instrutor"><spring:message
					code="avaliacao.instrutor.label" />:</label></td>

				<td>
					${instrutor3}
					<form:hidden path="instrutor3Id" value="${instrutor3.id}" />
				</td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor1"><spring:message
					code="avaliacao.questaoInstrutor1.label" />:</label></td>

				<td><form:select path="questaoInstrutor31"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor31" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor2"><spring:message
					code="avaliacao.questaoInstrutor2.label" />:</label></td>

				<td><form:select path="questaoInstrutor32"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor32" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor3"><spring:message
					code="avaliacao.questaoInstrutor3.label" />:</label></td>

				<td><form:select path="questaoInstrutor33"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor33" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor4"><spring:message
					code="avaliacao.questaoInstrutor4.label" />:</label></td>

				<td><form:select path="questaoInstrutor34"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor34" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor5"><spring:message
					code="avaliacao.questaoInstrutor5.label" />:</label></td>

				<td><form:select path="questaoInstrutor35"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor35" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoInstrutor6"><spring:message
					code="avaliacao.questaoInstrutor6.label" />:</label></td>

				<td><form:select path="questaoInstrutor36"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoInstrutor36" cssClass="error" /></td>
			</tr>
			</c:if>

			<tr class="prop">
				<td valign="top" class="name" colspan="2">
				<h3>Avaliação do Conteúdo</h3>
				</td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoConteudo1"><spring:message
					code="avaliacao.questaoConteudo1.label" />:</label></td>

				<td><form:select path="questaoConteudo1"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoConteudo1" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoConteudo2"><spring:message
					code="avaliacao.questaoConteudo2.label" />:</label></td>

				<td><form:select path="questaoConteudo2"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoConteudo2" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="questaoConteudo3"><spring:message
					code="avaliacao.questaoConteudo3.label" />:</label></td>

				<td><form:select path="questaoConteudo3"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoConteudo3" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name" colspan="2">
				<h3>Auto-Avaliação</h3>
				</td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label
					for="questaoAutoAvaliacao1"><spring:message
					code="avaliacao.questaoAutoAvaliacao1.label" />:</label></td>

				<td><form:select path="questaoAutoAvaliacao1"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoAutoAvaliacao1" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label
					for="questaoAutoAvaliacao2"><spring:message
					code="avaliacao.questaoAutoAvaliacao2.label" />:</label></td>

				<td><form:select path="questaoAutoAvaliacao2"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoAutoAvaliacao2" cssClass="error" /></td>
			</tr>
		
		<tr class="prop">
				<td valign="top" class="name" colspan="2">
			<h3>Avaliação Logística</h3> 
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label
					for="questaoOrganizacao"><spring:message
					code="avaliacao.questaoOrganizacao.label" />:</label></td>

				<td><form:select path="questaoOrganizacao"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoOrganizacao" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label
					for="questaoEspacoFisico"><spring:message
					code="avaliacao.questaoEspacoFisico.label" />:</label></td>

				<td><form:select path="questaoEspacoFisico"
					items="${respostasQuestoes}" />* <form:errors
					path="questaoEspacoFisico" cssClass="error" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="observacao"><spring:message
					code="avaliacao.observacao.label" />:</label></td>

				<td><form:textarea path="observacao" cols="70" rows="5" /> <form:errors
					path="observacao" cssClass="error" /></td>
			</tr>

		</tbody>
	</table>
	</div>
	<div class="buttons">
	<input id="criar" type="submit" class="save"
		value="<spring:message code="default.add.label" />" />
	</div>
</form:form></div>
</body>
</html>
