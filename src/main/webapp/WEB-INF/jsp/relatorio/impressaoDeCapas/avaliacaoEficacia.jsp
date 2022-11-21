<%@ include file="../../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>
<div class="avalizacaoEficaciaButton">
	<span class="menuButton"><a
	href="<c:url value="/relatorio/impressaoDeCapas/avaliacaoReacao"/>" class="list">Avaliação Reação</a></span>		
</div>
<div class="avaliacaoReacaoButton">
<span class="menuButton"><a
	href="<c:url value="/relatorio/impressaoDeCapas/frequencia"/>" class="list">Frequência</a></span>
</div>
<div class="indicacaoButton">
	<span class="menuButton"><a
	href="<c:url value="/relatorio/impressaoDeCapas/indicacao"/>" class="list">Indicação para Treinamento</a></span>
</div>

<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
</br>
<h1>Impressão de Capas - Avaliação Eficácia</h1>
<c:url var="url" value="/relatorio/impressaoDeCapas/avaliacaoEficacia" /> 
<form:form action="${url}" method="POST" modelAttribute="relImpressaoDeCapasAvaliacaoEficacia">
	<div class="filter">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message
					code="evento.evento.label" /></label></td>
				<td valign="top">
				<form:select path="eventoId" id="eventoid">
					<form:option value="0">TODOS</form:option>
					<form:options items="${eventoList}" itemLabel="nome" itemValue="id" />
				</form:select>
				</td>
			</tr>
			<tr>
                    <td>
                      <input id="filtrar" type="submit" class="botao"
						value="Filtrar" />
                    </td>
                    <td></td>
                  </tr>
		</tbody>
	</table>
	</div>
</form:form></div>
</body>
</html>