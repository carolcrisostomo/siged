<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="tipoLocalizacaoEvento.label" /></title>
</head>
<body>

	
		
<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<spring:message code="tipoLocalizacaoEvento.descricao.label" var="coluna1" /> 
<spring:message code="tipoLocalizacaoEvento.pais.label" var="coluna2" /> 
<spring:message code="tipoLocalizacaoEvento.uf.label" var="coluna3" /> 
<spring:message code="tipoLocalizacaoEvento.cidade.label" var="coluna4" /> 
<h1><spring:message code="tipoLocalizacaoEvento.label" /></h1>

<sec:authorize ifNotGranted="ROLE_ADMINISTRADORCONS">
			<a class="btn btn-primary btn-sm"
				href="<c:url value="/tipolocalizacaoevento/form"/>"> <i
				class="bi bi-plus-circle"></i> <spring:message
					code="default.add.label" />
			</a>

		</sec:authorize>

<div class="list"><display:table uid="tipolocalizacaoevento"
	name="${tiposlocalizacaoevento}" defaultsort="1"
	defaultorder="ascending" pagesize="10" requestURI="">
	<c:url var="url"
		value="/tipolocalizacaoevento/${tipolocalizacaoevento.id}" />

	<display:column property="descricao" sortable="true"
		title="${coluna1}"
		maxLength="80" />
	<display:column property="paisId" sortable="true"
		title="${coluna2}"
		maxLength="80" />
	<display:column property="municipio.ufMunicipio" sortable="true"
		title="${coluna3}"
		maxLength="80" />
	<display:column property="municipio.nome" class="caixaAlta" sortable="true"
		title="${coluna4}"
		maxLength="80" />
	<display:column class="crudlist">
		<form:form action="${url}" method="GET">
			<input alt="<spring:message code="default.button.show.label" />"
				src="<c:url value="/static/images/show.png"/>"
				title="<spring:message code="default.button.show.label" />"
				type="image"
				value="<spring:message code="default.button.show.label" />" />
		</form:form>
	</display:column>
	<sec:authorize ifNotGranted="ROLE_ADMINISTRADORCONS" >
		<display:column class="crudlist">
			<form:form action="${url}/form" method="GET">
				<input alt="<spring:message code="default.button.edit.label" />"
					src="<c:url value="/static/images/update.png"/>"
					title="<spring:message code="default.button.edit.label" />"
					type="image"
					value="<spring:message code="default.button.edit.label" />" />
			</form:form>
		</display:column>
		<display:column class="crudlist">
			<form:form action="${url}" method="DELETE">
				<input alt="<spring:message code="default.button.delete.label" />"
					src="<c:url value="/static/images/delete.png"/>"
					title="<spring:message code="default.button.delete.label" />"
					type="image"
					value="<spring:message code="default.button.delete.label" />"
					onclick="return confirm('<spring:message code="default.areyousure2.message" />');" />
			</form:form>
		</display:column>
	</sec:authorize>

</display:table></div>
</div>
<script type="text/javascript">
	var municipio = document.getElementsByClassName("caixaAlta");
	for (var i = 0; i < municipio.length; i++) {
		municipio[i].style.textTransform = "uppercase";
	}
</script>
</body>
</html>
