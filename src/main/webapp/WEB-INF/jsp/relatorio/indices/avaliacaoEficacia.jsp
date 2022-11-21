<%@ include file="../../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>
<div class="nav">
	<span class="menuButton"><a
	href="<c:url value="/relatorio/indices/avaliacaoReacao"/>" class="list">Avalia��o Rea��o</a></span>
	<span class="menuButton"><a
	href="<c:url value="/relatorio/indices/frequencia"/>" class="list">Frequ�ncia</a></span>
	<span class="menuButton"><a
	href="<c:url value="/relatorio/indices/indicacao"/>" class="list">Indica��o para Treinamento</a></span>		
</div>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1>Impressao de �ndices - Avalia��o Efic�cia</h1>
<c:url var="url" value="/relatorio/indices/avaliacaoEficacia" /> 
<form:form action="${url}" method="POST" modelAttribute="relIndiceAvaliacaoEficacia">
	<div class="filter">
	<table>
		<tbody>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="periodorealizacao">Per�odo Realiza��o</label></td>
				<td valign="top"><form:input path="dataInicio1" id="data1" alt="date" /> a 
				<form:input path="dataInicio2" id="data2" alt="date"/></td>
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