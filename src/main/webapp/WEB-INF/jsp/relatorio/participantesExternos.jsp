<%@ include file="../includes.jsp"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="main" />
	<title><spring:message code="default.add.label" /></title>
	<spring:url value="/ajax/procuraOrgao" var="procuraOrgaoUrl" />
</head>
<body>
<div class="body">

	<script type="text/javascript">
	
		jQuery(document).ready(function($){
			
			atualizaOrgao();			
			$('input[name=administracaoPublica]' ).change(function() {
				atualizaOrgao(true);
		    });
			
			function atualizaOrgao(limpaSelecao) {
				
				var radioMarcado = $('input[name=administracaoPublica]:checked' ).val(); 
				
				if (radioMarcado == 'estadual') {
		        	$('.localidadeTr').hide();
		        	updateOrgao('${procuraOrgaoUrl}', 'orgaoId', 1, '${relParticipantesExternos.orgaoId}');
		        }
		        else if (radioMarcado == 'municipal') {
		        	$('.localidadeTr').show();
		        	
		        	if(limpaSelecao)
		        		$('#localidadeId').val(0);
		        	
		        	updateOrgao('${procuraOrgaoUrl}', 'orgaoId', $('#localidadeId').val(), '${relParticipantesExternos.orgaoId}');		        	
		        } else {
		        	$('.localidadeTr').hide();
		        }
			}
			
			
			$('#localidadeId' ).change(function() {
		        updateOrgao('${procuraOrgaoUrl}', 'orgaoId', $('#localidadeId').val(), 0);
		    });
			
			$(document).ajaxStop($.unblockUI);
			
		});
	
	</script>

	<% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>

	<h1>Relatório de Participantes Externos</h1>

	<c:if test="${mensagemRel != null}">
   		<div class="message" id="msgId"><c:out value="${mensagemRel}" /></div>
	</c:if>
	
	<c:url var="url" value="/relatorio/participantesExternos/" /> 
	
	<form:form action="${url}" method="POST" modelAttribute="relParticipantesExternos" id="form1">
		<div class="filter">
		<table>
			<tbody>
				<tr>
					<td></td>
					<td style="text-align:right;" valign="top" class="name">(*) Campo Obrigatório</td>				
				</tr>				
								
				<tr class="prop">
					<td valign="top" class="name"><label for=""><spring:message	code="administracaoPublica" />:</label></td>
					<td>
						<label>
							<form:radiobutton path="administracaoPublica" value="estadual" /> 
							Estadual
						</label>
						<label>
							<form:radiobutton path="administracaoPublica"  value="municipal" /> 
							Municipal
						</label>
					</td>
				</tr>
				
				<tr class="prop, localidadeTr">
					<td valign="top" class="name">
						<label for="localidadeId">Município:</label>
					</td>
					<td>
						<form:select path="localidadeId">
							<form:option value="0">Selecione...</form:option>
							<form:options items="${localidadeList}" itemLabel="dsLocalidade" itemValue="idLocalidade" />
						</form:select>
					</td>
				</tr>												
				
				<tr class="prop">
					<td valign="top" class="name"><label for="orgao">
						<spring:message	code="participante.orgao.label" />:</label>
					</td>
					<td>
						<form:select path="orgaoId">
							<form:option value="0">Selecione...</form:option>									
						</form:select>*<br/>
						<form:errors path="orgaoId" cssClass="error" />
					</td>
				</tr>
				
				<tr class="prop">
					<td valign="top" class="name"><label for="evento"><spring:message
						code="participante.evento.label" /></label></td>
					<td valign="top">
						<form:select path="eventoId" id="eventoId" onchange="desabilitaPeriodo()">
							<form:option value="0">TODOS</form:option>
							<form:options items="${eventoRealizadoList}" itemLabel="nome" itemValue="id" />
						</form:select>
					</td>
				</tr>
									
	    	 	<tr class="prop">
					<td valign="top" class="name"><label for="periodorealizacao">Período</label></td>
					<td valign="top">
						<form:input path="dataInicio1" id="data1" alt="date" onkeyup="desabilitaEvento()" /> a 
						<form:input path="dataInicio2" id="data2" alt="date" onkeyup="desabilitaEvento()" />
						<form:errors path="dataInicio2" cssClass="error" />	
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
		</div>
	</form:form>
</div>
</body>
</html>