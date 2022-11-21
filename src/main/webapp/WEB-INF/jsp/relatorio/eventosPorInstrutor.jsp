<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<h1>Relatório de Eventos por Instrutor</h1>
<c:if test="${mensagemRel != null}">
   	<div class="message" id="msgId"><c:out value="${mensagemRel}" /></div>
</c:if>
<c:url var="url" value="/relatorio/eventosPorInstrutor/" /> 
<form:form action="${url}" method="POST" modelAttribute="relEventosPorInstrutor" id="form1">
	<div class="filter">
	<table>
		<tbody>
			<tr>
				<td></td>
				<td style="text-align:right;" valign="top" class="name">(*) Campos Obrigatórios</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="eventos">Instrutor</label></td>
				<td valign="top">
				<form:select path="instrutorId" id="instrutor">
					<form:option value="0">Selecione...</form:option>
					<form:options items="${instrutorList}" itemLabel="nome" itemValue="id" />
				</form:select>*	<form:errors path="instrutorId" cssClass="error" />
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="eventos"><spring:message
					code="evento.tipoEvento.label" /></label></td>
				<td valign="top">
				<form:select path="tipoEventoId" id="tipo_evento">
					<form:option value="0">TODOS</form:option>
					<form:options items="${tipoEventoList}" itemLabel="descricao" itemValue="id" />
				</form:select>
				</td>
			</tr>			
			<tr class="prop">
				<td valign="top" class="name"><label for="periodoprevisto">Período de Realização</label></td>
				<td valign="top">
					<form:input path="dataInicio1" id="data1" alt="date"/> a 
					<form:input path="dataInicio2" id="data2" alt="date"/>
					<form:errors path="dataInicio2" cssClass="error" />
				</td>
			</tr>					
			<tr class="prop">
				<td valign="top" class="name">
					<label for="setor"><spring:message code="evento.situacao.label" /></label>
				</td>
				<td valign="top">
				<form:select path="situacao" id="evento_situacao">
					<form:option value="0">TODOS</form:option>
					<form:options items="${situacaoList}"/>
				</form:select>
				</td>
			</tr>
			<tr>
            	<td>
                	<input id="filtrar" type="submit" class="botao" value="Filtrar" />
              	</td>
                <td></td>
    	   	</tr>
  		</tbody>
	</table>
	</div>
</form:form></div>
</body>
</html>