<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
<spring:url value="/ajax/obterTotalDeVagasETotalDeInscritosNoEvento" var="vagasInscritosUrl" />
<spring:url value="/ajax/procuraParticipantePorNome" var="procuraParticipantePorNomeUrl" />
<spring:url value="/ajax/procuraParticipantePorNomeOuCPF" var="procuraParticipantePorNomeOuCPFUrl" />
</head>
<body>

<div class="body">
	
	<h1>Inclusão de pré-inscrições em lote</h1>
	
	<c:if test="${mensagemSucesso != null}"><div class="messageSucesso messageRemove"><c:out value="${mensagemSucesso}" /></div></c:if>	
	<c:if test="${mensagemErro != null}"><div class="messageErro messageRemove"><c:out value="${mensagemErro}" /></div></c:if>	
	<c:if test="${mensagemAlerta != null}"><div class="messageAlerta messageRemove"><c:out value="${mensagemAlerta}" /></div></c:if>	
		
	
	<c:url var="url" value="/inscricao/inclusaolote" />
	
	<form:form id="formulario" action="${url}" method="POST" modelAttribute="inscricao">
		
		<input type="hidden" id="participantesId" name="participantesId" value="" />
				
		<div class="dialog">
			<table>
				<tbody>
					<tr class="name">
						<td valign="top" class="name">
							<label for="evento"><spring:message code="evento.evento.label" /></label>
						</td>
						<td valign="top">
							<form:select path="eventoId" id="eventoId">
								<form:option value="0">Selecione...</form:option>
								<form:options items="${eventoList}" itemLabel="nome" itemValue="id" />
							</form:select>
							<br /><form:errors path="eventoId" cssClass="error" /><br /><br />
						</td>					
					</tr>
					<tr id="incricoesContainer">
						<td colspan="2">
							<table style="border: 0px;">
								<tr>
									<td>
										<label>Participantes</label>																											
									</td>
									<td></td>
									<td>
										<label>Serão incluídas e confirmadas *</label>									
									</td>
								</tr>							
								<tr>
									<td style="width:400px;">
										<form:input	cssStyle="width:370px; margin-bottom: 3px;" maxlength="255" path="nomeParticipante" placeholder="Nome ou CPF"/>								
										<input id="buscarParticipante" class="search campo" type="button" />								
										<select id="naoinscritos" multiple="multiple" style="height:250px; width:380px;"></select>														
									</td>
									<td style="width:78px;">
										<input id="botao1" type="button" title="Adicionar" value="&rsaquo;" style="width:50px;"/><br /><br />
										<input id="botao2" type="button" title="Remover" value="&lsaquo;" style="width:50px;"/><br /><br />
										<input id="botao3" type="button" title="Adicionar todos" value="&raquo;" style="width:50px;"/><br /><br />
										<input id="botao4" type="button" title="Remover todos" value="&laquo;" style="width:50px;"/><br /><br />								
									</td>
									<td style="width:400px;">									
										<select id="selecionados" multiple="multiple" style="height:271px; width:380px;"></select>													
									</td>
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td><span>* Limite de 20 pré-inscrições por vez</span></td>
								</tr>							
							</table>
						</td>
					</tr>
					<tr>
						<td style="text-align:right; padding-right:46px" colspan="2">
							<label>Vagas ofertadas no Evento </label>
							<input id="totalVagas" style="text-align:right;" maxlength="4" size="2" value="0" readonly="readonly"/><br /><br />
							
							<label>Inscrições confirmadas </label>
							<input id="totalInscricoes" style="text-align:right;" maxlength="4" size="2" value="0" readonly="readonly" /><br /><br />
							
							<label>Serão incluídas e confirmadas </label>
							<input id="totalSeraoConfirmadas" style="text-align:right;" maxlength="4" size="2" value="0" readonly="readonly" /><br /><br />
						</td>					
					</tr>
					<tr>
						<td colspan="2">
							<br /><input type="button" id="botaoIncluir" class="btn btn-primary" value="Incluir pré-inscrições"/>
						</td>					
					</tr>				    		 
				</tbody>
			</table>		
		</div>
	</form:form>
	
</div>
<script type="text/javascript" src="<c:url value="/static/js/nomeOuCpf.mask.js"/>"></script>
<script type="text/javascript"> 
	jQuery(document).ready(function($){ 
		
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
			
			var naoinscritos = $('#naoinscritos :selected').length;
			var selecionados = $('#selecionados').children('option').length;
			
			if (selecionados + naoinscritos <= 20){
				transfereSelecionados('naoinscritos', 'selecionados');
				$("#totalSeraoConfirmadas").val($("#selecionados").children('option').length);
			}else{
				alert("O limite de 20 pré-inscrições a serem incluídas foi ultrapassado.")
			}
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
				
		$("#botaoIncluir").click(function(){		
			
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