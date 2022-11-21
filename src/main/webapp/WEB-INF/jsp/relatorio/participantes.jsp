<%@ include file="../includes.jsp"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="main" />
	<title><spring:message code="default.add.label" /></title>
	<spring:url value="/ajax/procuraOrgao" var="procuraOrgaoUrl" />
</head>
<body>
	
	<script type="text/javascript">
		
		function ignorar($) {
			switch (document.getElementById('tipo_publico_alvo_id').value) {
			case "1":
				document.getElementById('setor').disabled = false;
				desabilitarOrgao(true);
				break;
			case "2":
				document.getElementById('setor').disabled = true;
				desabilitarOrgao(false);				
				break;
			default:
				document.getElementById('setor').disabled = true;
				desabilitarOrgao(true);				
			}
			document.getElementById('setor').dispatchEvent(new CustomEvent('chosen:updated'));
		}		

		function ignorarDesempenho() {
			if (document.getElementById('eventoId').value == 0) {
				document.getElementById('desempenho').disabled = true;
			} else {
				document.getElementById('desempenho').disabled = false;
			}
			document.getElementById('desempenho').dispatchEvent(new CustomEvent('chosen:updated'));
		}

		function apagarErro() {
			if (document.getElementById('eventoId.errors') != null)
				document.getElementById('eventoId.errors').innerHTML = "";
		}
		
		function desabilitarOrgao(desabilitado){
			document.getElementById('orgaoId').disabled = desabilitado;
			document.getElementById('orgaoId').dispatchEvent(new CustomEvent('chosen:updated'));	
			document.getElementById('localidadeId').disabled = desabilitado;
			document.getElementById('localidadeId').dispatchEvent(new CustomEvent('chosen:updated'));
			
			var checks = document.getElementsByName('administracaoPublica');
			for (i = 0; i < checks.length; i++) {
				checks[i].disabled = desabilitado;			    
			}
		}		
		
		function atualizarOrgao($, limpaSelecao) {
			
			var radioMarcado = $('input[name=administracaoPublica]:checked' ).val(); 
			
			if (radioMarcado == 'estadual') {
	        	$('.localidadeTr').hide();
	        	updateOrgao('${procuraOrgaoUrl}', 'orgaoId', 1, '${relParticipantes.orgaoId}', true);
	        }
	        else if (radioMarcado == 'municipal') {
	        	$('.localidadeTr').show();
	        	
	        	if(limpaSelecao)
	        		$('#localidadeId').val(0);
	        	
	        	updateOrgao('${procuraOrgaoUrl}', 'orgaoId', $('#localidadeId').val(), '${relParticipantes.orgaoId}', true);	        	
	        	
	        } else {
	        	$('.localidadeTr').hide();
	        	$('#orgaoId').html('<option value="0">TODOS</option>').trigger('chosen:updated');
	        }
			
		}
		
		jQuery(document).ready(function($){
			
			$('#tipo_publico_alvo_id').val(0);
			$('#setor').val(-1);
			$('#orgaoId').val(0);
			
			ignorar($);
			
			ignorarDesempenho();
			
			atualizarOrgao($);			
			
			$('#tipo_publico_alvo_id' ).change(function() {
		        ignorar($);
		    });			
			
			$('input[name=administracaoPublica]' ).change(function() {
				atualizarOrgao($, true);
		    });			
			
			$('#localidadeId' ).change(function() {
		        updateOrgao('${procuraOrgaoUrl}', 'orgaoId', $('#localidadeId').val(), 0, true);
		    });			
			
			$('#eventoId' ).change(function() {
				apagarErro();
				ignorarDesempenho();
		    });			
			
			$(document).ajaxStop($.unblockUI);			
			
		});
		
	</script>
	<div class="body">
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>
		<h1>Relatório de Participantes</h1>
		<c:if test="${mensagemRel != null}">
   			<div class="message" id="msgId"><c:out value="${mensagemRel}" /></div>
		</c:if>
		<c:url var="url" value="/relatorio/participantes/" />
		<form:form action="${url}" method="POST" id="form1"
			modelAttribute="relParticipantes">
			<div class="filter">
				<table>
					<tbody>
						<tr class="prop">
							<td valign="top" class="name"><label for="publicoAlvo"><spring:message
										code="participante.tipoPublicoAlvo.label" /></label></td>
							<td valign="top"><form:select path="publicoAlvoId" id="tipo_publico_alvo_id" >
									<form:option value="0">TODOS</form:option>
									<form:options items="${tipoPublicoAlvoList}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label for=""><spring:message	code="administracaoPublica" />:</label></td>
							<td>
								<label>
									<form:radiobutton path="administracaoPublica" value="todas" checked="checked" /> 
									Todas
								</label>
								<label>
									<form:radiobutton path="administracaoPublica" value="estadual" /> 
									Estadual
								</label>
								<label>
									<form:radiobutton path="administracaoPublica"  value="municipal" /> 
									Municipal
								</label>
								<label>
									<form:radiobutton path="administracaoPublica"  value="demais casos" /> 
									Demais Casos
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
								<form:select path="orgaoId" onchange="ignorarSetor()">
									<form:option value="0">TODOS</form:option>									
								</form:select>
							</td>
						</tr>						
						
						<tr class="prop">
							<td valign="top" class="name"><label for="setor"><spring:message
										code="participante.setor.label" /></label></td>
							<td valign="top"><form:select path="setorId" id="setor"	>
									<form:option value="-1">TODOS</form:option>
									<form:options items="${setorList}" itemLabel="descricao"
										itemValue="id" />
								</form:select></td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label for="evento"><spring:message
										code="participante.evento.label" /></label></td>
							<td valign="top"><form:select path="eventoId" id="eventoId">
									<form:option value="0">TODOS</form:option>
									<form:options items="${eventoList}" itemLabel="nome"
										itemValue="id" />
								</form:select><br />
							<form:errors path="eventoId" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="desempenho">Desempenho</label></td>
							<td valign="top"><form:select path="desempenho" id="desempenho">
									<form:option value="TODOS">TODOS</form:option>
									<form:option value="APROVADOS">APROVADOS</form:option>
									<form:option value="REPROVADOS">REPROVADOS</form:option>
								</form:select></td>
						</tr>
						<tr>
							<td><input id="filtrar" type="submit" class="botao"
								value="Filtrar" /></td>
							<td></td>
						</tr>

					</tbody>
				</table>
			</div>
		</form:form>
	</div>
</body>
</html>