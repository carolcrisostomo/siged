<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1>Relatório de Solicitações</h1>
<c:if test="${mensagemRel != null}">
	<div class="message" id="msgId"><c:out value="${mensagemRel}" /></div>
</c:if>
<c:url var="url" value="/relatorio/solicitacoes/" /> 
<form:form action="${url}" method="POST" modelAttribute="relSolicitacoes">
	<div class="filter">
	<table>
		<tbody>
			<%-- <tr class="prop">
				<td class="name">
					<label for="eventoId"><spring:message code="relatorio.evento.label" /></label>	
				</td>
				<td valign="top">
					<form:select path="eventoId" id="eventoId">
						<form:option value="0">TODOS</form:option>
						<form:options items="${solicitacoesList}" itemLabel="curso" itemValue="id" />
					</form:select>
				</td>						
			</tr> --%>
		    <tr class="prop">
				<td class="name">
					<label for="participanteId"><spring:message code="relatorio.participante.label" /></label>	
				</td>
				<td valign="top">
					<form:select path="participanteId" id="participanteId">
						<form:option value="0">TODOS</form:option>
						<form:options items="${usuarioList}" itemLabel="nome" itemValue="id" />
					</form:select>
				</td>						
			</tr>
			<tr class="prop">
				<td class="name">
					<label for="responsavelPelaIndicacaoId"><spring:message code="eventoExtra.chefe.label" /></label>	
				</td>
				<td valign="top">
					<form:select path="responsavelPelaIndicacaoId" id="responsavelPelaIndicacaoId">
						<form:option value="0">TODOS</form:option>
						<form:options items="${chefeList}" itemLabel="nome" itemValue="id" />
					</form:select>
				</td>						
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="periodorealizacao">Período</label></td>
				<td valign="top"><form:input path="dataInicio1" id="data1" alt="date" /> a 
				<form:input path="dataInicio2" id="data2" alt="date"/>
				<form:errors path="dataInicio1" cssClass="error" />
				<form:errors path="dataInicio2" cssClass="error" /></td>
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