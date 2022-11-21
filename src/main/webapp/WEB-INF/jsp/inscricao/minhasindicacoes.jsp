<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="inscricao.minhas2.label" /></title>

    <script type="text/javascript">
      jQuery(document).ready(function($) {

        $("#indicarpreinscricao").click(function() {
        $.ajax({
                url: "/inscricao/indicacao",
                data: "id=" + this.value,
                cache: false,
                success: function(html) {
                	$("#indicarpreinscricao").html("OK");
                }
              });
           });
         });
    </script>

</head>
<body>
<div class="body">
		<%
			if (request.getParameter("mensagemSucesso") != null) {
		%><div class="messageSucesso"><%=request.getParameter("mensagemSucesso")%></div>
		<%
			}
		%>
	
		<%
			if (request.getParameter("mensagemErro") != null) {
		%><div class="messageErro"><%=request.getParameter("mensagemErro")%></div>
		<%
			}
		%>
		
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>

<h1>Minhas Indica��es</h1>
<br />

<h3>Pr�-Inscri��es</h3>
<br />

<div class="list"><display:table uid="inscricao"
	name="${inscricoes}" defaultsort="3" defaultorder="descending"
	pagesize="10" requestURI="">
	<c:url var="url2" value="/inscricao/${inscricao.id}" />
	<c:url var="url3" value="/inscricao/indicacao/${inscricao.id}" />
	<c:url var="url" value="/inscricao/justificativa/${inscricao.id}" />
	<display:column property="participanteId" sortable="true"
		title="Participante"
		maxLength="80" />
	<display:column property="eventoId" sortable="true"
		title="Evento"
		maxLength="80" />
	<display:column property="data" sortable="true"
		title="Data Pr�-Inscri��o" maxLength="50" format="{0,date,dd/MM/yyyy}"/>

	<display:column property="dataIndicacao" sortable="true"
		title="Data Indica��o"
		maxLength="50" format="{0,date,dd/MM/yyyy}"/>
	<display:column property="indicada" sortable="true"
		title="Indicada"
		maxLength="80" />
	<display:column class="crudlist">
		<form:form action="${url2}" method="GET">
			<input alt="<spring:message code="default.button.show.label" />"
				src="<c:url value="/static/images/show.png"/>"
				title="<spring:message code="default.button.show.label" />"
				type="image"
				value="<spring:message code="default.button.show.label" />" />
		</form:form>
	</display:column>
	<display:column class="crudlist">
		<c:if test="${inscricao.confirmada == '-' && inscricao.indicada == 'N'}">
			<a href="${url}"  onclick="Modalbox.show(this.href, {title: this.title, width: 800, height: 300}); return false;">Indicar</a>
		</c:if>
		<c:if test="${inscricao.confirmada == '-' && inscricao.indicada == 'S'}">
			<a href="${url3}/cancelar" onclick="return confirm('<spring:message code="default.areyousure.message" />');">Cancelar</a>
		</c:if>
		<c:if test="${inscricao.confirmada == 'S'}">
			Confirmada
		</c:if>
		<c:if test="${inscricao.confirmada == 'N'}">
			N�o Confirmada
		</c:if>
	</display:column>
</display:table>
<br />
<br />
<h3>Solicita��es</h3>
<br />
<display:table uid="solicitacao"
	name="${solicitacoes}" defaultsort="3" defaultorder="descending"
	pagesize="10" requestURI="">
	<c:url var="url" value="/eventoextra/${solicitacao.id}" />
	<c:url var="url2" value="/eventoextra/justificativa/${solicitacao.id}" />
	<display:column property="solicitanteId" sortable="true"
		title="Solicitante"
		maxLength="80" />
	<display:column property="curso" sortable="true"
		title="Evento"
		maxLength="80" />
	<display:column property="dataInicio" sortable="true"
		title="Data Inicio" maxLength="50" format="{0,date,dd/MM/yyyy}"/>
	<display:column property="dataFim" sortable="true"
		title="Data Fim" maxLength="50" format="{0,date,dd/MM/yyyy}"/>
	<display:column property="indicada" sortable="true"
		title="Indicada" />
	<display:column class="crudlist">
		<form:form action="${url}" method="GET">
			<input alt="<spring:message code="default.button.show.label" />"
				src="<c:url value="/static/images/show.png"/>"
				title="<spring:message code="default.button.show.label" />"
				type="image"
				value="<spring:message code="default.button.show.label" />" />
		</form:form>
	</display:column>
	<display:column class="crudlist">
		<c:if test="${solicitacao.inscricaoList[0] == null && solicitacao.indicada == 'N'}">
			<a href="${url2}" onclick="Modalbox.show(this.href, {title: this.title, width: 800, height: 300}); return false;">Indicar</a>
		</c:if>
		<c:if test="${solicitacao.inscricaoList[0] == null && solicitacao.indicada == 'S'}">
			<a href="${url}/indicar/cancelar" onclick="return confirm('<spring:message code="default.areyousure.message" />');">Cancelar</a>
		</c:if>
		<c:if test="${solicitacao.inscricaoList[0] != null && solicitacao.inscricaoList[0].confirmada == 'S'}">
			Usada/Confirmada
		</c:if>
		<c:if test="${solicitacao.inscricaoList[0] != null && solicitacao.inscricaoList[0].confirmada != 'S'}">
			Usada/N�o Confirmada
		</c:if>
	</display:column>
</display:table>
<br />
<br />
<h3>Sugest�es do IPC</h3>
<br />

<display:table uid="sugestao"
	name="${sugestoes}" defaultsort="1" defaultorder="ascending"
	pagesize="10" requestURI="">
	<c:url var="url" value="/eventorecomendar/${sugestao.id}" />\
	<display:column property="setorId" sortable="true"
		title="Setor"
		maxLength="80" />
	<display:column property="titulo" sortable="true"
		title="Evento"
		maxLength="80" />
	<display:column property="dataInicio" sortable="true"
		title="Data Inicio" maxLength="50" format="{0,date,dd/MM/yyyy}"/>
	<display:column property="dataFim" sortable="true"
		title="Data Fim" maxLength="50" format="{0,date,dd/MM/yyyy}"/>
	<display:column property="aprovado" sortable="true"
		title="Aprovada" maxLength="50"/>
		
	<display:column class="crudlist">
	<display:column class="crudlist">
		<form:form action="${url}" method="GET">
			<input alt="<spring:message code="default.button.show.label" />"
				src="<c:url value="/static/images/show.png"/>"
				title="<spring:message code="default.button.show.label" />"
				type="image"
				value="<spring:message code="default.button.show.label" />" />
		</form:form>
	</display:column>
		<form:form action="${url}/formchefe" method="GET">
			<input alt="<spring:message code="default.button.save.label" />"
				src="<c:url value="/static/images/update.png"/>"
				title="<spring:message code="default.button.save.label" />"
				type="image"
				value="<spring:message code="default.button.save.label" />" />
		</form:form>
	</display:column>
</display:table>
<br />

</div>
</div>
</body>
</html>
