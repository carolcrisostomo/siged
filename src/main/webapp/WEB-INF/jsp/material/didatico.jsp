<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
	<div class="body">
		
		<h1>${evento.nome2}</h1>
		<br />
		<div class="list" style="max-height: 350px; overflow: auto;">
			<display:table uid="material" name="${materiais}" requestURI="">
				<display:column property="moduloId.titulo" title="M�dulo"
					maxLength="80" />
				<display:column title="Material Did�tico" maxLength="80">
					<a style="color: #006699; font-weight: normal;"
						href="<c:url value="/material/arquivo/${material.id}"/>"
						target="_parent"> ${material.descricao} </a>
				</display:column>
			</display:table>
		</div>
	</div>
</body>
</html>
