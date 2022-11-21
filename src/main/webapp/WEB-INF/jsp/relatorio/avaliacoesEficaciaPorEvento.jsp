<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
	<h1>Avaliações de Eficácia por Evento</h1>
	<c:url var="url" value="/relatorio/avaliacoesEficaciaPorEvento/" /> 
	<form:form action="${url}" method="POST" modelAttribute="relAvaliacoesEficaciaPorEvento">
	<div class="filter">
	<table>
		<tbody>
			<tr>
				<td></td>
				<td style="text-align:right;" valign="top" class="name">(*) Campos Obrigatórios</td>
			</tr>				
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message
					code="avaliacao.evento.label" /></label></td>
				<td valign="top">
				<form:select path="eventoId" id="evento" >
					<form:option value="0">Selecione...</form:option>
					<form:options items="${eventosComEficaciaAvaliada}" itemLabel="nome" itemValue="id" />
				</form:select>*
				<form:errors path="eventoId" cssClass="error" />
				</td>
			</tr>			
	      	<tr>
                <td>
                   <input id="filtrar" type="submit" class="botao"value="Filtrar" />
                </td>
                <td></td>
			</tr>
		</tbody>
	</table>
	</div>
</form:form></div>
</body>
</html>