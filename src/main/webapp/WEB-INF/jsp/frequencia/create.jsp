<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />

<title><spring:message code="frequencia.label" /></title>
<spring:url value="/frequencia/procuraModulo" var="procuraModuloUrl" />
<spring:url value="/frequencia/procuraParticipante" var="procuraParticipanteUrl" />
<spring:url value="/frequencia/procuraTurno" var="procuraTurnoUrl" />
</head>

<body>
	<div class="body">
		<c:if test="${param.mensagem != null}">
			<div class="message">${param.mensagem}</div>
		</c:if>
		<div class="messageErro"></div>
		<h1>
			<spring:message code="default.add.label" />
			-
			<spring:message code="frequencia.label" />
		</h1>

		<c:url var="url" value="/frequencia" />
		<form:form action="${url}" id="formulario" method="POST" modelAttribute="frequencia">
			<div class="dialog">
				<table>
					<tbody>
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">(*)
								Campos Obrigatórios</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="evento"><spring:message
										code="frequencia.evento.label" />:</label></td>
							<td><form:select path="eventoId" id="eventoId"
									class="js_frequencia_evento"
									data-modulo-url="${procuraModuloUrl}"
									data-participante-url="${procuraParticipanteUrl}">
									<form:option value="0">Selecione...</form:option>
									<c:forEach items="${eventoList}" var="evento">
										<form:option value="${evento.id}">${evento.nome}</form:option>
									</c:forEach>
								</form:select>* <br /><form:errors path="eventoId" cssClass="error" />							
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="modulo"><spring:message
										code="frequencia.modulo.label" />:</label></td>
							<td><form:select path="moduloId"
									class="js_frequencia_modulo"
									data-turno-url="${procuraTurnoUrl}">
									<form:option value="0">Selecione...</form:option>
								</form:select>* <form:errors path="moduloId" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="data"><spring:message
										code="frequencia.data.label" />:</label></td>
							<td><form:input cssStyle="width:90px" maxlength="255"
									path="data" size="30" alt="date" />* <form:errors path="data"
									cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="turno"><spring:message
										code="frequencia.turno.label" />:</label></td>
							<td><form:select path="turno" class="js_frequencia_turno"/>* <form:errors path="turno"
									cssClass="error" /></td>
						</tr>
					
						
						<tr id="incricoesContainer">
					<td colspan="2">
						<table style="border: 0px;">
							<tr>
								<td>
									<label>Alunos a serem confirmados *</label>														
								</td>
								<td></td>
								<td>
									<label>Alunos presentes</label>													
								</td>
							</tr>						
							<tr>
								<td style="width:400px;">									
								
								
									<select id="pendente"  multiple="multiple" style="height:250px; width:380px;">
									
									</select>														
								</td>
								<td style="width:78px;">
									<input type="button" class="botaoTransfere" title="Adicionar" value="&rsaquo;" style="width:50px;" 
										onclick="transfereSelecionados('pendente','participantes')"/><br /><br />
									<input type="button" class="botaoTransfere" title="Remover" value="&lsaquo;" style="width:50px;" 
										onclick="transfereSelecionados('participantes', 'pendente')"/><br /><br />
									<input type="button" class="botaoTransfere" title="Adicionar todos" value="&raquo;" style="width:50px;" 
										onclick="transfereTodos('pendente','participantes')"/><br /><br />
									<input type="button" class="botaoTransfere" title="Remover todos" value="&laquo;" style="width:50px;" 
										onclick="transfereTodos('participantes', 'pendente')"/><br /><br />								
								</td>
								<td style="width:400px;">									
									<select id="participantes" name="participanteList"  multiple="multiple" style="height:250px; width:380px;">
									
									</select>					
								</td>
							</tr>
							<tr>
								<td>
								<form:errors path="participanteList" cssClass="error" />
								
								</td>
								<td></td>
								<td></td>
							</tr>							
						</table>
					</td>
				</tr>
				
					</tbody>
				</table>
			</div>
			
			
			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/frequencia/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="criar" type="button" class="btn btn-primary">
 						<spring:message code="default.add.label" />
					</button>

				</div>
			</div>
		</form:form>
	</div>
	<script type="text/javascript" src="<c:url value="/static/js/ajax.loading.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/modulo.consulta.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/participante.consulta.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/frequencia.cadastro.js"/>"></script>
	
	<script type="text/javascript"> 

	function atualizar(lookupUrl, parentSelectElementId, childSelectElementId) {
		var parentSelectRef = jQuery('#' + parentSelectElementId);
		var childSelectRef = jQuery('#' + childSelectElementId);
		

		jQuery.getJSON(lookupUrl, {
			eventoId : parentSelectRef.val()
		}, function(data) {		
			var html = '';
			var len = data.length;
			for ( var i = 0; i < len; i++) {
				html += '<option value="' + data[i].id + '">' + data[i].nomeCpf + '</option>';
			}
			childSelectRef.html(html).trigger('chosen:updated');
			
		});
	}
	
	jQuery(document).ready(function($){ 
		
	

		
		$("#criar").click(function(){ 
			
  			var childSelectRef = jQuery('#formulario');
			var form = document.getElementById("formulario")
			var participantElement = document.getElementById("participantes")
			
			for(var i = 0; i < participantElement.options.length; i++){
				 var input = document.createElement("input");
                input.style= "display: none"
                input.name = "participanteList";
                input.value =  participantElement.options[i].value;

                form.appendChild(input);
			}
			
		
			form.submit();
			
  			

		})
		$("#eventoId").change(function(){ 
			
  			atualizar('${procuraParticipanteUrl}', 'eventoId', 'pendente');

		})
		
		$('#incricoesContainer select').chosen('destroy');
		 
		if($("#eventoId").val() != 0){
			$("#botaoIncluir").attr( "disabled", false );
			$.getJSON(
				"${vagasInscritosUrl}",
				{eventoId: $("#eventoId").val()}, 
				function(vagasEInscricoes) {						
					$("#totalVagas").val(vagasEInscricoes[0]);
					$("#totalInscricoes").val(vagasEInscricoes[1]);						            	
				}
			);
		}else{
			$(".messageRemove").remove();
			$("#botaoIncluir").attr( "disabled", true );
		}
			
  		
		updateParticipantePorNomeSelectMultiple('${procuraParticipantePorNomeUrl}', 'nomeParticipante', 'naoinscritos', '');
		
		
		$("#eventoId").change(function(){   
  			updateInscricaoComConfirmacaoPendente('${procuraParticipanteUrl}', 'eventoId', 'pendente');

			$(".messageRemove").remove();
			  			  			
  			if($("#eventoId").val() != 0){
  				$("#botaoIncluir").attr( "disabled", false );
				$.getJSON(
					"${vagasInscritosUrl}",
					{eventoId: $("#eventoId").val()}, 
					function(vagasEInscricoes) {						
						$("#totalVagas").val(vagasEInscricoes[0]);
						$("#totalInscricoes").val(vagasEInscricoes[1]);						            	
					}
				);
			}else{
				$("#botaoIncluir").attr( "disabled", true )
			}
  		});
		
		
		$('#nomeParticipante').keypress(function (e) {
			var key = e.which;
			if(key == 13){  // the enter key code
				updateParticipantePorNomeSelectMultiple('${procuraParticipantePorNomeOuCPFUrl}', 'nomeParticipante', 'naoinscritos', 'true');			    
			}
		});
		
		var nomeOuCpfMask = new SIGED.NomeOuCpfMask('#nomeParticipante');
		nomeOuCpfMask.iniciar();

		
		$("#buscarParticipante").click(function($){
			updateParticipantePorNomeSelectMultiple('${procuraParticipantePorNomeOuCPFUrl}', 'nomeParticipante', 'naoinscritos', 'true');
		});
			
		
		$("#botao1").click(function(){
			
			$("#naoEncontrado").remove();			
			
				alert("teste")
				transfereSelecionados('naoinscritos', 'selecionados');
		
  		});
		
		$("#botao2").click(function(){ 
			
			$("#naoEncontrado").remove();			
			
			transfereSelecionados('selecionados', 'naoinscritos');
			
			$("#totalSeraoConfirmadas").val($("#selecionados").children('option').length);
  		});
		
		$("#botao3").click(function(){ 
			
			$("#naoEncontrado").remove();
			
			var naoinscritos = $('#naoinscritos').children('option').length;
			var selecionados = $('#selecionados').children('option').length;
			
			if (selecionados + naoinscritos <= 20){
				transfereTodos('naoinscritos', 'selecionados');
				$("#totalSeraoConfirmadas").val($("#selecionados").children('option').length);
			}else{
				alert("O limite de 20 pré-inscrições a serem incluídas foi ultrapassado.")
			}
  		});
		
		$("#botao4").click(function(){
			
			$("#naoEncontrado").remove();
			
			transfereTodos('selecionados', 'naoinscritos');

			$("#totalSeraoConfirmadas").val($("#selecionados").children('option').length);
  		});
				
		$("#criar").click(function(e){	
			
			var selecionados = $('#selecionados').children('option').length;
			
			if (selecionados > 0) {
			
				var totalVagas = parseInt(document.getElementById("totalVagas").value);
				var totalInscricoes = parseInt(document.getElementById("totalInscricoes").value);
				var totalSeraoConfirmadas = parseInt(document.getElementById("totalSeraoConfirmadas").value);
				
				if(totalVagas < totalInscricoes + totalSeraoConfirmadas) {
					var resposta = confirm("Não há vagas suficientes. Incluir mesmo assim?");
					if(resposta == true){					
						submeter();
					}					
				}else{							
					submeter();
				}
				
			} else {
				alert("Nenhum participante selecionado.");
			}					
				
  		});
		
		function submeter(){
			
			var listaSelecionados = document.getElementById("selecionados");
						
			var participantesId = "";			
			
			for(var i = 0; i < listaSelecionados.options.length; i++){
				participantesId += listaSelecionados.options[i].value + "," ;
			}
			
			document.getElementById("participantesId").value = participantesId;
			document.getElementById("formulario").submit();
						
		}				
		
		$(document).ajaxStop($.unblockUI);
 	
	});	
</script>
</body>
</html>
