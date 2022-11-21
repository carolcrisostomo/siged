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
	window.onload = function() {
		var eventoId 		= document.getElementById("eventoId");
		var participanteId 	= document.getElementById('participanteId');
		var instrutorId 	= document.getElementById('instrutorId');
		var tipoGastoId 	= document.getElementById('tipogastoid');
		var data1    		= document.getElementById("data1");
		var data2    		= document.getElementById("data2");
		var setorId 		= document.getElementById('setorId');
		var orgaoId 		= document.getElementById('orgaoId');
		var automatico 		= document.getElementById('automatico');
		
		if(eventoId.value == 0){
			automatico.disabled = true;
			automatico.checked = false;
			desabilitarAll();
		}
		
		eventoId.onchange = function() {
			desabilitaPeriodo();
			if(eventoId.value != 0){
				automatico.disabled = false;
			} else {
				automatico.disabled = true;
				automatico.checked = false;
				desabilitarAll();
			}
		};
		
		data1.onchange = function() {
			desabilitaEvento();
		};
		
		data2.onchange = function() {
			desabilitaEvento();
		};
		
		automatico.onchange = function() {
			if(this.checked == true){
				habilitarAll();
				participanteId.value = 0;
				instrutorId.value = 0;
				tipoGastoId.value = 0;
				setorId.value = -1;
				orgaoId.value = 0;
			} else {
				desabilitarAll();
			}
		};
		
		function desabilitarAll() {
			participanteId.disabled = instrutorId.disabled = tipoGastoId.disabled = setorId.disabled = orgaoId.disabled = false;
			participanteId.dispatchEvent(new CustomEvent('chosen:updated'));
			instrutorId.dispatchEvent(new CustomEvent('chosen:updated'));
			tipoGastoId.dispatchEvent(new CustomEvent('chosen:updated'));
			setorId.dispatchEvent(new CustomEvent('chosen:updated'));
			orgaoId.dispatchEvent(new CustomEvent('chosen:updated'));
		}
		
		function habilitarAll() {
			participanteId.disabled = instrutorId.disabled = tipoGastoId.disabled = setorId.disabled = orgaoId.disabled = true;
			participanteId.dispatchEvent(new CustomEvent('chosen:updated'));
			instrutorId.dispatchEvent(new CustomEvent('chosen:updated'));
			tipoGastoId.dispatchEvent(new CustomEvent('chosen:updated'));
			setorId.dispatchEvent(new CustomEvent('chosen:updated'));
			orgaoId.dispatchEvent(new CustomEvent('chosen:updated'));
		}
		
	};
	
	jQuery(document).ready(function($){
		
		atualizaOrgao();			
		$('input[name=administracaoPublica]' ).change(function() {
			atualizaOrgao(true);
	    });
		
		function atualizaOrgao(limpaSelecao) {
			
			var radioMarcado = $('input[name=administracaoPublica]:checked' ).val(); 
			
			if (radioMarcado == 'estadual') {
	        	$('.localidadeTr').hide();
	        	updateOrgao('${procuraOrgaoUrl}', 'orgaoId', 1, '${relGastos.orgaoId}', true);
	        }
	        else if (radioMarcado == 'municipal') {
	        	$('.localidadeTr').show();
	        	
	        	if(limpaSelecao)
	        		$('#localidadeId').val(0);
	        	
	        	updateOrgao('${procuraOrgaoUrl}', 'orgaoId', $('#localidadeId').val(), '${relGastos.orgaoId}', true);		        	
	        } else {
	        	$('.localidadeTr').hide();
	        }
		}
		
		
		$('#localidadeId' ).change(function() {
	        updateOrgao('${procuraOrgaoUrl}', 'orgaoId', $('#localidadeId').val(), 0, true);
	    });
		
		$(document).ajaxStop($.unblockUI);
		
	});
	
	
</script>

<div class="body">

	<% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
	
	<h1>Relatório de Gastos</h1>
	
	<c:if test="${mensagemRel != null}">
		<div class="message" id="msgId"><c:out value="${mensagemRel}" /></div>
	</c:if>
	
	<c:url var="url" value="/relatorio/gastos/" /> 
	
	<form:form action="${url}" method="POST" modelAttribute="relGastos" id="form1">
		
		<div class="filter">
		
			<table>
				<tbody>
					<tr class="prop">
						<td valign="top" class="name"><label for="evento"><spring:message
							code="gasto.evento.label" /></label></td>
						<td valign="top">
						<form:select path="eventoId" id="eventoId" >
							<form:option value="0">TODOS</form:option>
							<form:options items="${eventoGastoList}" itemLabel="nome" itemValue="id" />
						</form:select>
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name">
							<label for="evento"><spring:message	code="evento.participante.label" /></label>
						</td>
						<td valign="top">
							<form:select path="participanteId" id="participanteId">
								<form:option value="0">TODOS</form:option>
								<form:options items="${servidorEMembriosTceList}" itemLabel="nomeCpf" itemValue="id" />
							</form:select>
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name">
							<label for="instrutorId"><spring:message code="gasto.instrutor.label" /></label>
						</td>
						<td valign="top">
							<form:select path="instrutorId" id="instrutorId">
								<form:option value="0">TODOS</form:option>
								<form:options items="${instrutorList}" itemLabel="nome" itemValue="id" />
							</form:select>
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name"><label for="tipoGasto"><spring:message
							code="gasto.tipoGasto.label" /></label></td>
						<td valign="top">
						<form:select path="tipoGastoId" id="tipogastoid">
							<form:option value="0">TODOS</form:option>
							<form:options items="${tipoGastoList}" itemLabel="descricao" itemValue="id" />
						</form:select>
						</td>
					</tr>		
					<tr class="prop">
						<td valign="top" class="name"><label for="periodorealizacao">Período</label></td>
						<td valign="top">
							<form:input path="dataInicio1" id="data1" alt="date" /> a 
							<form:input path="dataInicio2" id="data2" alt="date" />
							<form:errors path="dataInicio2" cssClass="error" />	
						</td>
					</tr>
					<tr class="prop">
						<td valign="top" class="name">
							<label for="setorId"><spring:message code="participante.setor.label" /></label>
						</td>
						<td valign="top">
							<form:select path="setorId">
								<form:option value="-1">TODOS</form:option>
								<form:options items="${setorList}" itemLabel="descricao" itemValue="id" />
							</form:select>
						</td>
					</tr>			
					
					
					<tr class="prop">
						<td valign="top" class="name"><label for=""><spring:message	code="administracaoPublica" />:</label></td>
						<td>
							<label>
								<form:radiobutton path="administracaoPublica" value="estadual"/> 
								Estadual
							</label>
							<label>
								<form:radiobutton path="administracaoPublica"  value="municipal"/> 
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
								<form:option value="0">TODOS</form:option>									
							</form:select>
						</td>
					</tr>
					
					
					<tr>
						<td valign="top" class="name" colspan="2">
							<div class="divcheck">
								<form:checkbox class="check" value="1" id="automatico" path="automatico"/> 
								<label for="automatico">Simplificado</label>
							</div>
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