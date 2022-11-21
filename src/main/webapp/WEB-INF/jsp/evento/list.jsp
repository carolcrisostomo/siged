<%@ include file="../includes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="evento.label" /></title>
<spring:url value="/evento/search" var="eventosUrl"></spring:url>
</head>
<body>
	<script type="text/javascript">
		jQuery(document)
				.ready(
						function($) {
							
							//var dataEventoRealizadoInicio = document.getElementById("realizacao1")
							//var dataEventoRealizadoFim = document.getElementById("realizacao2")
							
							var todayDate = new Date()
							var defaultTargetDate = new Date()
							
							defaultTargetDate.setDate(1)
							defaultTargetDate.setMonth(defaultTargetDate.getMonth() - 6);
							
							console.log({inicio: dataEventoRealizadoInicio.value,
								fim: dataEventoRealizadoFim.value})
							
							
							
							if(dataEventoRealizadoFim.value == "") dataEventoRealizadoFim.value = todayDate.toLocaleDateString('pt-BR')
							if(dataEventoRealizadoInicio.value == "") dataEventoRealizadoInicio.value = defaultTargetDate.toLocaleDateString('pt-BR')
							

							if ("${fn:length(eventos.list)}" > 0) {
								var table = document.getElementById("evento");
								var tbody = table.getElementsByTagName("tbody")[0];
								var rows = tbody.getElementsByTagName("tr");
								for (var i = 0; i < rows.length; i++) {
									var value = rows[i]
											.getElementsByTagName("td")[7].firstChild.nodeValue;
									if (value == 'N') {
										rows[i].style.backgroundColor = "#ffffdd";
									}
								}
							}
						});
	</script>

	<div class="body">
		<%
		if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
		}
		%>
		<spring:message code="evento.titulo.label" var="coluna1" />
		<spring:message code="evento.dataInicioPrevisto.label" var="coluna2" />
		<spring:message code="evento.dataFimPrevisto.label" var="coluna3" />
		<spring:message code="evento.dataInicioRealizacao.label" var="coluna4" />
		<spring:message code="evento.dataFimRealizacao.label" var="coluna5" />
		<spring:message code="evento.cargaHoraria2.label" var="coluna6" />
		<spring:message code="evento.publicoAlvo.label" var="coluna7" />
		<h1>
			<spring:message code="evento.label" />
		</h1>

		<sec:authorize ifNotGranted="ROLE_ADMINISTRADORCONS">
			<a class="btn btn-primary btn-sm"
				href="<c:url value="/evento/form"/>"> <i
				class="bi bi-plus-circle"></i> <spring:message
					code="default.add.label" />
			</a>

		</sec:authorize>

		<div class="filter">
			<c:url var="url" value="/evento/search" />
			<form:form action="${url}" method="GET" modelAttribute="eventofiltro">
				<table>
					<tbody>
						<tr>
							<td><label for="titulo">Título</label></td>
							<td valign="top" class="value"><form:input
									cssStyle="width:250px" id="titulo" maxlength="255"
									path="titulo" size="30" /></td>
						</tr>
						<tr>
							<td><label for="publicoAlvoId"><spring:message
										code="tipoPublicoAlvo.label" /></label></td>
							<td valign="top" class="value"><form:select
									path="tipoPublicoAlvo">
									<form:option value="0">TODOS</form:option>
									<form:options items="${tipoPublicoAlvoList2}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>
						<tr>
							<td><label for="periodo">Período Previsto</label></td>
							<td valign="top" class="value"><form:input
									cssStyle="width:90px" id="previsto1" maxlength="255"
									path="previsto1" size="30" alt="date" /> a <form:input
									cssStyle="width:90px" id="previsto2" maxlength="255"
									path="previsto2" size="30" alt="date" /></td>
						</tr>
						<tr>
							<td><label for="realizacao">Período Realização</label></td>
							<td valign="top" class="value"><form:input
									cssStyle="width:90px" id="realizacao1" maxlength="255"
									path="realizacao1" size="30" alt="date" /> a <form:input
									cssStyle="width:90px" id="realizacao2" maxlength="255"
									path="realizacao2" size="30" alt="date" /></td>
						</tr>
						<tr>
							<td><label for="tipoId"><spring:message
										code="tipoEvento.label" /></label></td>
							<td valign="top" class="value"><form:select
									path="tipoEvento">
									<form:option value="0">TODOS</form:option>
									<form:options items="${tipoEventoList}" itemLabel="descricao"
										itemValue="id" />
								</form:select></td>
						</tr>
						<tr>
							<td><label for="provedorId"><spring:message
										code="evento.provedorEvento.label" /></label></td>
							<td valign="top" class="value"><form:select
									path="provedorId">
									<form:option value="0">TODOS</form:option>
									<form:options items="${provedorEventoList}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>

						<tr>
							<td>

								<button id="filtrar" type="submit"
									class="btn btn-primary btn-sm">Filtrar</button>
							</td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</form:form>
		</div>

		<c:if test="${eventos != null}">

			<div class="list">
					
				<display:table export="true" uid="evento" name="${eventos}" sort="external"
					 size="${eventos.fullListSize}"
					requestURI="${eventosUrl}">

					<display:setProperty name="export.types" value="csv" />
					<display:setProperty name="basic.show.header" value="true" />
					<c:url var="url" value="/evento/${evento.id}" />
					<c:url var="urlModulos"
						value="/modulo/search/byevento/${evento.id}" />

					<display:column property="nome2" title="${coluna1}" maxLength="40"
						sortable="true" sortProperty="titulo" />

					<display:column property="dataInicioPrevisto" title="${coluna2}"
						maxLength="50" format="{0,date,dd/MM/yyyy}" sortable="true"
						sortProperty="dataInicioPrevisto" />
					<display:column property="dataFimPrevisto" title="${coluna3}"
						maxLength="50" format="{0,date,dd/MM/yyyy}" sortable="true"
						sortProperty="dataFimPrevisto" />

					<display:column property="dataInicioRealizacao" title="${coluna4}"
						maxLength="50" format="{0,date,dd/MM/yyyy}" sortable="true"
						sortProperty="dataInicioRealizacao" />
					<display:column property="dataFimRealizacao" title="${coluna5}"
						maxLength="50" format="{0,date,dd/MM/yyyy}" sortable="true"
						sortProperty="dataFimRealizacao" />

					<display:column property="cargaHoraria"
						style="text-align : center;" title="${coluna6}" maxLength="80" />
					<display:column property="publicoAlvoLista" title="${coluna7}"
						maxLength="80" />
					<display:column property="publicado" class="hidden"
						headerClass="hidden" />

					<display:column class="crudlist" media="html"
					>
						<form:form action="${urlModulos}" method="GET">
							<input
								alt="<spring:message code="default.button.modulos.label" />"
								src="<c:url value="/static/images/modulos.png"/>"
								title="<spring:message code="default.button.modulos.label" />"
								type="image"
								value="<spring:message code="default.button.modulos.label" />" />
						</form:form>
					</display:column>
					<display:column class="crudlist" media="html"
					>
						<form:form action="${url}" method="GET">
							<input alt="<spring:message code="default.button.show.label" />"
								src="<c:url value="/static/images/show.png"/>"
								title="<spring:message code="default.button.show.label" />"
								type="image"
								value="<spring:message code="default.button.show.label" />" />
						</form:form>
					</display:column>
					<sec:authorize ifNotGranted="ROLE_ADMINISTRADORCONS">
						<display:column class="crudlist" media="html"
						>
							<form:form action="${url}/form" method="GET">
								<input alt="<spring:message code="default.button.edit.label" />"
									src="<c:url value="/static/images/update.png"/>"
									title="<spring:message code="default.button.edit.label" />"
									type="image"
									value="<spring:message code="default.button.edit.label" />" />
							</form:form>
						</display:column>
					</sec:authorize>
					<display:column class="crudlist" media="html"
					>
						<c:if test="${evento.desempenho == 'S' && evento.id != 481}">
							<a
								href="<c:url value="/evento/desempenhos/${evento.id}/?n=${random}" />"
								title="Desempenhos"
								onclick="Modalbox.show(this.href, {title: this.title, width: 810, height: 500}); return false;">
								<img width="auto" height="22px"
								src="<c:url value="/static/images/icon_desempenho.png"/>"
								id="icon_desempenho" />
							</a>
						</c:if>
					</display:column>
					<sec:authorize ifNotGranted="ROLE_ADMINISTRADORCONS">
						<display:column class="crudlist" media="html"
						>
							<form:form action="${url}" method="DELETE">
								<input
									alt="<spring:message code="default.button.delete.label" />"
									src="<c:url value="/static/images/delete.png"/>"
									title="<spring:message code="default.button.delete.label" />"
									type="image"
									value="<spring:message code="default.button.delete.label" />"
									onclick="return confirm('<spring:message code="default.areyousure2.message" />');" />
							</form:form>
						</display:column>
					</sec:authorize>

				</display:table>
			</div>

		</c:if>

	</div>

</body>
</html>
