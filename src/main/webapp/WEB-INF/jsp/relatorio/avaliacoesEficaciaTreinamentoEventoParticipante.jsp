<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
<spring:url value="/avaliacaoeficacia/procuraParticipanteEficaciaAvaliadaByEventoId" var="procuraParticipanteUrl" />
<spring:url value="/avaliacaoeficacia/procuraParticipanteComAvaliacaodeEficacia" var="procuraParticipanteUrl2" />
</head>
<body>

<script type="text/javascript">
	window.onload = function() {
		
		var eventoId = document.getElementById("evento");
		var data1    = document.getElementById("data1");
		var data2    = document.getElementById("data2");
		
		function desabilitaEvento() {
			if (data1.value != "" && data2.value != "") {
				eventoId.disabled = true;
			}
			else{
				eventoId.disabled = false;
			}
		};
		
		eventoId.onchange = function() {
			if (eventoId.value != 0) {
				updateParticipante('${procuraParticipanteUrl}', 'evento', 'participanteId', true);
				data1.disabled = true;
				data2.disabled = true;
				data1.value = "";
				data2.value = "";
				data1.style.backgroundColor = "#efefef";
				data2.style.backgroundColor = "#efefef";
			} else {
				updateParticipante('${procuraParticipanteUrl2}', 'evento', 'participanteId', true);
				data1.disabled = false;
				data2.disabled = false;
				data1.style.backgroundColor = "#fff";
				data2.style.backgroundColor = "#fff";
			} 
		};
		
		data1.onchange = function() {
			desabilitaEvento();
		};
		
		data2.onchange = function() {
			desabilitaEvento();
		};
		
		if (data1.value != "" && data2.value != "") {
			desabilitaEvento();
		}
	}
</script>

<%-- <div class="nav"><span class="menuButton"><a
	href="<c:url value="/relatorio/avaliacoesEficaciaTreinamentoEventoParticipante/"/>" class="list"><spring:message
	code="default.button.list.label" /></a></span></div> --%>
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
	<h1>Relatório de Avaliações de Eficácia Realizadas</h1>
	<c:if test="${mensagemRel != null}">
   		<div class="message" id="msgId"><c:out value="${mensagemRel}" /></div>
  	</c:if>
	<c:url var="url" value="/relatorio/avaliacoesEficaciaTreinamentoEventoParticipante/" /> 
	<form:form action="${url}" method="POST" modelAttribute="relAvaliacoesEficaciaTreinamentoEventoParticipante" id="form1">
	<div class="filter">
	<table>
		<tbody>		
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message
					code="avaliacao.evento.label" /></label></td>
				<td valign="top">
				<form:select path="eventoId" id="evento">
					<form:option value="0">TODOS</form:option>
					<form:options items="${nomeEventoAvaliacaoEficaciaList}" itemLabel="nome" itemValue="id" />
				</form:select>
				</td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message
					code="avaliacao.participante.label" /></label></td>
				<td valign="top">
				<form:select path="participanteId">
					<form:option value="0">TODOS</form:option>
					<form:options items="${nomeParticipanteAvaliacaoEficaciaList}" itemLabel="nomeCpf" itemValue="id" />
				</form:select>
				</td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="periodorealizacao">Período Realização</label></td>
				<td valign="top"><form:input path="dataInicio1" id="data1" alt="date" onchange="desabilitaEvento()" /> a 
				<form:input path="dataInicio2" id="data2" alt="date" onchange="desabilitaEvento()" />
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