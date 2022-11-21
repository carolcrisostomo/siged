<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="pais.label" /></title>
</head>
<body>
	<div class="body">
		<c:if test="${param.mensagem != null}">
			<div class="message">${param.mensagem}</div>
		</c:if>
		<c:if test="${modulosApurados != null && modulosApurados.size() > 0}">
			<c:forEach items="${modulosApurados}" var="modulo">
				<div class="messageSucesso">Módulo apurado com sucesso: ${modulo.titulo}</div>
			</c:forEach>
		</c:if>
		<c:if test="${modulosNaoApurados != null && modulosNaoApurados.size() > 0}">
			<c:forEach items="${modulosNaoApurados}" var="moduloErrorMsg">
				<div class="messageAlerta">${moduloErrorMsg}</div>
			</c:forEach>
		</c:if>
		<c:if test="${emailErrorMsg != null}">
			<div class="messageAlerta">${emailErrorMsg}</div>
		</c:if>
		<h1>Desempenho</h1>
		<br />
		<div class="list">
			<display:table uid="desempenho" name="${desempenho}" defaultsort="1"
				defaultorder="ascending" pagesize="50"
				requestURI="/evento/${evento.id}/divulgardesempenhopagination">

				<display:column property="eventoId" sortable="true" title="Evento"
					maxLength="80" />
				<display:column property="moduloId" sortable="true" title="Módulo"
					maxLength="80" />
				<display:column property="participanteId" sortable="true"
					title="Participante" maxLength="80" />
				<display:column property="nota" sortable="true" title="Nota"
					maxLength="80" />
				<display:column sortable="true" title="Frequencia (%)"
					maxLength="80">${fn:replace(desempenho.frequencia, '.00', '')}</display:column>
				<display:column property="aprovado" sortable="true" title="Aprovado"
					maxLength="80" />

			</display:table>
		</div>
		
		<c:if test="${parcial}">
			<h1>Apuração parcial até o presente momento!</h1>
		</c:if>
		<c:if test="${!parcial}">
			<h1>Desempenho geral no evento</h1>
			<br />
			<div class="list">
				<display:table uid="desempenhoFinal" name="${desempenhoFinal}"
					defaultsort="1" defaultorder="ascending" pagesize="10"
					requestURI="/evento/${evento.id}/divulgardesempenhopagination">
					<display:column property="participanteId" sortable="true"
						title="Participante" maxLength="80" />
					<display:column property="aprovado" sortable="true" title="Aprovado"
						maxLength="80" />
				</display:table>
			</div>		
			<div class="paginateButtons">
				Total de participantes: <c:out value="${totalDeParticipantes}"/> <br />
				<c:if test="${totalDeParticipantes gt 0}">		
						Total de aprovados: <c:out value="${totalDeAprovados}"/> <br />
						Taxa de aproveitamento: <c:out value="${taxaDeAproveitamento}"/> %
				</c:if>
			</div>
		</c:if>	
	</div>
</body>
</html>
