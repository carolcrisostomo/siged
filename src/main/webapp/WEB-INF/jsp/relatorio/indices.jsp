<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>
<h1>Impressão de Índices</h1>
<div class="nav">
	<span class="menuButton"><a
	href="<c:url value="/relatorio/indices/avaliacaoEficacia"/>" class="list">Avaliação Eficácia</a></span>
	<span class="menuButton"><a
	href="<c:url value="/relatorio/indices/avaliacaoReacao"/>" class="list">Avaliação Reação</a></span>
	<span class="menuButton"><a
	href="<c:url value="/relatorio/indices/frequencia"/>" class="list">Freqüência</a></span>
	<span class="menuButton"><a
	href="<c:url value="/relatorio/indices/indicacao"/>" class="list">Indicação para Treinamento</a></span>		
</div>
</body>
</html>