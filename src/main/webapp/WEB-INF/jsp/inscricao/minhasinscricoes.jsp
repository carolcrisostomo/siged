<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="inscricao.minhas.label" /></title>
</head>
<body>

<div class="body">	
	
	<spring:message code="inscricao.evento.label" var="coluna1" />
	<spring:message code="inscricao.participante.label" var="coluna2" />
	<spring:message code="inscricao.data.label" var="coluna3" />
	<spring:message code="inscricao.indicada.label" var="coluna4" />
	<spring:message code="inscricao.dataIndicacao.label" var="coluna5" />
	<spring:message code="inscricao.confirmada.label" var="coluna6" />
	<spring:message code="inscricao.chefe.label" var="coluna7" />
	<spring:message code="inscricao.dataConfirmacao.label" var="coluna8" />
	<h1><spring:message code="inscricao.minhas.label" /></h1>
		
	<% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>

	<sec:authorize ifAnyGranted="ROLE_SERVIDOR">
		<% if (request.getParameter("mensagem2")!=null) {%><div class="message"><%= request.getParameter("mensagem2")%></div><% } %>		
	</sec:authorize>	
            
    <div class="filter">
    
	    <c:url var="url" value="/inscricao/minhasinscricoes/search" />
	    
	    <form:form action="${url}" method="GET" modelAttribute="inscricaofiltro">
	    	<table>
	        	<tbody>
	     			<tr>
	            		<td>
	              			<label for="evento"><spring:message code="inscricao.evento.label" /></label>
	           		 	</td>
	            		<td valign="top" class="value">
	              			<form:select path="evento">
								<form:option value="0">TODOS</form:option>
	              				<form:options items="${eventoList}" itemLabel="nome" itemValue="id" />
	              			</form:select>
	          			</td>
	       			</tr>
					<tr>
	            		<td>
	                		<label for="periodo">Período</label>
	              		</td>
	              		<td valign="top" class="value">
	                		<form:input cssStyle="width:90px" id="data1" maxlength="255" path="data1" size="30" alt="date"/>
	               			a <form:input cssStyle="width:90px" id="data2" maxlength="255" path="data2" size="30" alt="date"/>
	            		</td>
	          		</tr>          	
	          	
	          		<sec:authorize ifAnyGranted="ROLE_SERVIDOR" >
		          		<tr>
		           	 		<td>
		             		 	<label for="indicada"><spring:message code="inscricao.indicada.label" /></label>
		            		</td>
		            		<td valign="top" class="value">
		              			<form:select path="indicada">
		              				<form:option value="">TODOS</form:option>
		              				<form:options items="${SNList}"/>
		              			</form:select>
		           			</td>
		          		</tr>
	          		</sec:authorize>          	
	          	
	          		<tr>
	           			<td>
	              			<label for="confirmada"><spring:message code="inscricao.confirmada.label" /></label>
	            		</td>
	            		<td valign="top" class="value">
	              			<form:select path="confirmada">
	              				<form:option value="">TODOS</form:option>
	              				<form:options items="${SNList}"/>
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
		</form:form>
    </div>
	
	<div class="list">
		
		<display:table uid="inscricao" name="${inscricoes}" defaultsort="2" defaultorder="descending" pagesize="10" requestURI="">
			
			<c:url var="url" value="/inscricao/${inscricao.id}/minhasinscricoes" />
			<c:url var="url2" value="/inscricao/${inscricao.id}" />
			
			<display:column property="eventoId" sortable="true" title="${coluna1}" maxLength="80" />
			
			<display:column property="data" sortable="true" title="${coluna3}" maxLength="50" format="{0,date,dd/MM/yyyy}"/>
			
			<sec:authorize ifAnyGranted="ROLE_SERVIDOR" >
				<display:column property="indicada" sortable="true" title="${coluna4}" maxLength="80" />
				<display:column property="dataIndicacao" sortable="true" title="${coluna5}" maxLength="50" format="{0,date,dd/MM/yyyy}"/>
				<display:column property="chefeId" sortable="true" title="${coluna7}" maxLength="80" />
			</sec:authorize>
			
			<display:column property="confirmada" sortable="true" title="${coluna6}" maxLength="80" />
			<display:column property="dataConfirmacao" sortable="true" title="${coluna8}" maxLength="80" format="{0,date,dd/MM/yyyy}"/>
			
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
				<c:if test="${inscricao.confirmada != 'S'}">
					<form:form action="${url}" method="DELETE">
						<input alt="<spring:message code="default.button.delete.label" />"
							src="<c:url value="/static/images/delete.png"/>"
							title="<spring:message code="default.button.delete.label" />"
							type="image"
							value="<spring:message code="default.button.delete.label" />"
							onclick="return confirm('<spring:message code="default.areyousure.message" />');" />
					</form:form>
				</c:if>
			</display:column>
		</display:table>
	</div>
	</div>
</body>
</html>
