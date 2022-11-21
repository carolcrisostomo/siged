<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>
<h1>Impress�o de �ndices</h1>
<div class="nav">
	<span class="menuButton"><a
	href="<c:url value="/relatorio/indices/avaliacaoEficacia"/>" class="list">Avalia��o Efic�cia</a></span>
	<span class="menuButton"><a
	href="<c:url value="/relatorio/indices/avaliacaoReacao"/>" class="list">Avalia��o Rea��o</a></span>
	<span class="menuButton"><a
	href="<c:url value="/relatorio/indices/frequencia"/>" class="list">Freq��ncia</a></span>
	<span class="menuButton"><a
	href="<c:url value="/relatorio/indices/indicacao"/>" class="list">Indica��o para Treinamento</a></span>		
</div>
</body>
</html>