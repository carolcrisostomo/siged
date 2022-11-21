<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="inscricao.label" /></title>
<spring:url value="/inscricao/limpar" var="urlLimpar" />
<spring:url value="/ajax/procuraParticipantePorNome" var="procuraParticipantePorNomeUrl" />
<spring:url value="/ajax/procuraParticipantePorNomeOuCPF" var="procuraParticipantePorNomeOuCPFUrl" />
<spring:url value="/inscricao/search" var="inscricoesUrl" />
</head>
<body>
	

	<div class="body">
	
		<h1><spring:message code="inscricao.label" /></h1>
		
		<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_SERVIDOR">
			<a class="btn btn-primary btn-sm"
				href="<c:url value="/inscricao/form"/>"> <i
				class="bi bi-plus-circle"></i> <spring:message
					code="default.add.label" />
			</a>

		</sec:authorize>
	
		<% if (request.getParameter("mensagem")!=null) {%>
			<div class="message"><%= request.getParameter("mensagem")%>
			</div>
		<% } %>
	
		<spring:message code="inscricao.evento.label" var="coluna1" />
		<spring:message code="inscricao.participante.label" var="coluna2" />
		<spring:message code="inscricao.data.label.abreviacao" var="coluna3" />
		<spring:message code="inscricao.indicada.label.abreviacao" var="coluna4" />
		<spring:message code="inscricao.dataIndicacao.label.abreviacao" var="coluna5" />
		<spring:message code="inscricao.confirmada.label.abreviacao" var="coluna6" />
		<spring:message code="inscricao.dataConfirmacao.label.abreviacao" var="coluna7" />

		<div class="filter">
            
			<c:url var="url" value="/inscricao/search" />            
            
            <form:form id="form" action="${url}" method="GET" modelAttribute="inscricaofiltro">
           		
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
                      			<label for="periodo">Per�odo</label>
                    		</td>
                    		<td valign="top" class="value">
                      			<form:input cssStyle="width:90px" id="data1" maxlength="255" path="data1" size="30" alt="date"/>
                       			a <form:input cssStyle="width:90px" id="data2" maxlength="255" path="data2" size="30" alt="date"/>
                    		</td>
                  		</tr>
                  		
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
                      			<label for="participanteId"><spring:message code="inscricao.participante.label" /></label>
                    		</td>
                    		<td valign="top" class="value">
                    			
                    			<form:input	cssStyle="width:250px" maxlength="255" path="nomeParticipante" id="nomeParticipante" placeholder="Nome ou CPF"/>
								
								<input id="buscarParticipante" class="search" type="button" />
                      			
                      			<form:select path="participanteId" itemValue="id" itemLabel="nomeCpf" cssStyle="width:450px">
					  				<form:option value="0">TODOS</form:option>                      				
                      			</form:select><span id="participanteErro" class="error"></span>
                    		</td>
                  		</tr>
                  		
                  		<tr>
	                    	<td>
	                      		<label for="tipoParticipanteId">Tipo Participante</label>
	                    	</td>
	                    	<td valign="top" class="value">
	                      		<form:select path="tipoParticipanteId">
						  			<form:option value="0">TODOS</form:option>
	                      			<form:options items="${tipoParticipanteList}" itemLabel="descricao" itemValue="id" />
	                      		</form:select>
	                    	</td>
                  		</tr>
                  		
                  		<tr>
                    		<td >
                      		
                      			
								<button id="filtrar" type="submit"
									class="btn btn-primary btn-sm">Filtrar</button>
									
									
								<button id="limpar" type="button"	class="btn btn-primary btn-sm">Limpar</button>
                    		</td>                    		
                  		</tr>                  
                  
                	</tbody>
              	</table>
			</form:form>
			
		</div>
		
		<c:if test="${inscricoes != null}">
			<div class="list">
				<display:table uid="inscricao" export="true" name="${inscricoes}" sort="external" partialList="true" size="${inscricoes.fullListSize}" 
					requestURI="${inscricoesUrl}">
		
					<c:url var="url" value="/inscricao/${inscricao.id}" />
					
					<display:column property="eventoId" sortable="true" title="${coluna1}" maxLength="70" sortProperty="e.titulo" />
					<display:column property="participanteId" sortable="true" title="${coluna2}" maxLength="70" sortProperty="p.nome"/>
					<display:column property="participanteId.tipoId.abreviacao" sortable="true" title="Tipo" maxLength="70" sortProperty="t.abreviacao"
									style="text-align:center" headerClass="table--header__centralizada" />
					
					<display:column property="data" sortable="true" title="${coluna3}" maxLength="50" format="{0,date,dd/MM/yyyy}" 
									style="text-align:center" headerClass="table--header__centralizada" />
					
					<display:column property="indicada" sortable="true" title="${coluna4}" maxLength="80" 
									style="text-align:center" headerClass="table--header__centralizada" />
									
					<display:column property="dataIndicacao" sortable="true" title="${coluna5}" maxLength="50" format="{0,date,dd/MM/yyyy}" 
									style="text-align:center" headerClass="table--header__centralizada" />
									
					<display:column property="confirmada" sortable="true" title="${coluna6}" maxLength="80" 
									style="text-align:center" headerClass="table--header__centralizada" />
									
					<display:column property="dataConfirmacao" sortable="true" title="${coluna7}" maxLength="50" format="{0,date,dd/MM/yyyy}" 
									style="text-align:center" headerClass="table--header__centralizada"/>
					
					<sec:authorize ifAllGranted="ROLE_ADMINISTRADOR">
						<display:column class="crudlist" media="html">
							<c:if test="${inscricao.confirmada.equals('S')}">
								<a href="${url}/enviarEmailConfirmacao" class="js_enviar_email" data-imgcontrol="${inscricao.id}">
									<img src="<c:url value="/static/images/send_email.png"/>"
										alt="Reenviar e-mail de confirma��o"
										title="Reenviar e-mail de confirma��o"
										value="Reenviar e-mail de confirma��o"
										class="js_email_img_${inscricao.id}" />
								</a>
							</c:if>
						</display:column>
					</sec:authorize>
					<display:column class="crudlist" media="html">
						<form:form action="${url}" method="GET">
							<input alt="<spring:message code="default.button.show.label" />"
								src="<c:url value="/static/images/show.png"/>"
								title="<spring:message code="default.button.show.label" />"
								type="image"
								value="<spring:message code="default.button.show.label" />" />
						</form:form>
					</display:column>
					<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
						<display:column class="crudlist" media="html">
							<form:form action="${url}/form" method="GET">
								<input alt="<spring:message code="default.button.edit.label" />"
									src="<c:url value="/static/images/update.png"/>"
									title="<spring:message code="default.button.edit.label" />"
									type="image"
									value="<spring:message code="default.button.edit.label" />" />
							</form:form>
						</display:column>
						<display:column class="crudlist" media="html">
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
	
				</display:table>
			</div>
		</c:if>	
	</div>
	<script type="text/javascript" src="<c:url value="/static/js/inscricao.enviar_email_confirmacao.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/nomeOuCpf.mask.js"/>"></script>
	<script type="text/javascript">		
		jQuery(document).ready(function($) {
			
			updateParticipantePorNome('${procuraParticipantePorNomeOuCPFUrl}', 'nomeParticipante', 'participanteId', 'participanteErro', '${inscricaofiltro.participanteId}', 'true', '');
			
			$("#buscarParticipante").click(function($){
				updateParticipantePorNome('${procuraParticipantePorNomeOuCPFUrl}', 'nomeParticipante', 'participanteId', 'participanteErro', '', 'true', 'true');
			});
			
			$("#limpar").click(function() {				
				$("#form").attr("action","${urlLimpar}");
				$("#form").submit();
			});						
			
			$(document).ajaxStop($.unblockUI);
			
			var nomeOuCpfMask = new SIGED.NomeOuCpfMask('#nomeParticipante');
			nomeOuCpfMask.iniciar();
			/* nomeOuCpfMask.event.bind('cpf',function() {
				console.log('Input CPF');
			});
			nomeOuCpfMask.event.bind('nome',function() {
				console.log('Input Nome');
			}); */
		});		
	</script>
</body>
</html>
