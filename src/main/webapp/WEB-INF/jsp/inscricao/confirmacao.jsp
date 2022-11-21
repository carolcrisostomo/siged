<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
<spring:url value="/ajax/procuraInscricaoPendenteNoEvento" var="procuraParticipanteUrl" />
<spring:url value="/ajax/obterTotalDeVagasETotalDeInscritosNoEvento" var="vagasInscritosUrl" />
</head>
<body>
<script type="text/javascript"> 
	jQuery(document).ready(function($){ 
		
		$('#incricoesContainer select').chosen('destroy');
		 
		$("#selecionado").find('option').remove();
		$("#botaoConfirmar").attr( "disabled", true );
		if($("#eventoId").val() == 0)
			$(".message").remove();
  		
		updateInscricaoComConfirmacaoPendente('${procuraParticipanteUrl}', 'eventoId', 'pendente');		
		
		$("#eventoId").change(function(){   			
			$("#selecionado").find('option').remove();
			$("#botaoConfirmar").attr( "disabled", true );
			$(".message").remove();
			
  			updateInscricaoComConfirmacaoPendente('${procuraParticipanteUrl}', 'eventoId', 'pendente');
  			
  			if($("#eventoId").val() != 0){		
				$.getJSON(
					"${vagasInscritosUrl}",
					{eventoId: $("#eventoId").val()}, 
					function(vagasEInscricoes) {						
						$("#totalVagas").val(vagasEInscricoes[0]);
						$("#totalInscricoes").val(vagasEInscricoes[1]);						            	
					}
				);
			}
  		});
  		
		$(".botaoTransfere").click(function(){ 			
			
			if ($("#selecionado").find('option').length == 0)
				$("#botaoConfirmar").attr( "disabled", true );  						
			else			
				$("#botaoConfirmar").attr( "disabled", false );	
			
			$("#totalSeraoConfirmadas").val($("#selecionado").find('option').length);
  		});		
		
		$("#botaoConfirmar").click(function(){		
			
			var totalVagas = parseInt(document.getElementById("totalVagas").value);
			var totalInscricoes = parseInt(document.getElementById("totalInscricoes").value);
			var totalSeraoConfirmadas = parseInt(document.getElementById("totalSeraoConfirmadas").value);
			
			if(totalVagas < totalInscricoes + totalSeraoConfirmadas) {
				var resposta = confirm("Não há vagas suficientes. Confirmar mesmo assim?");
				if(resposta == true){					
					submeter();
				}					
			}else{							
				submeter();
			}												
  		});
		
		function submeter(){
			
			var listaSelecionados = document.getElementById("selecionado");
			var inscricoesId = "";			
			
			for(var i = 0; i < listaSelecionados.options.length; i++){
				inscricoesId += listaSelecionados.options[i].value + "," ;
			}
			
			document.getElementById("inscricoesId").value = inscricoesId;
			document.getElementById("formulario").submit();			
		}
 	});
	
</script>
<div class="body">
	<h1>Confirmação de pré-inscrições em lote</h1>
	<c:if test="${mensagemSucesso != null}"><div class="messageSucesso messageRemove"><c:out value="${mensagemSucesso}" /></div></c:if>
	<c:if test="${mensagemErro != null}"><div class="messageErro messageRemove"><c:out value="${mensagemErro}" /></div></c:if>	
	<c:if test="${mensagemAlerta != null}"><div class="messageAlerta messageRemove"><c:out value="${mensagemAlerta}" /></div></c:if>
		
	<c:url var="url" value="/inscricao/confirmacao" />
	<form:form id="formulario" action="${url}" method="POST" modelAttribute="eventoInscricao">
		<input type="hidden" id="inscricoesId" name="inscricoesId" value="" />
		<div class="dialog">
		<table>
			<tbody>
				<tr class="name">
					<td valign="top" class="name">
						<label for="evento"><spring:message code="evento.evento.label" /></label>
					</td>
					<td valign="top">
						<form:select path="id" id="eventoId">
							<form:option value="0">Selecione...</form:option>
							<form:options items="${eventoComInscricoesComConfirmacaoPedenteList}" itemLabel="nome" itemValue="id" />
						</form:select><br /><br />
					</td>					
				</tr>
				<tr id="incricoesContainer">
					<td colspan="2">
						<table style="border: 0px;">
							<tr>
								<td>
									<label>Confimações pendentes *</label>														
								</td>
								<td></td>
								<td>
									<label>Serão confirmadas</label>													
								</td>
							</tr>						
							<tr>
								<td style="width:400px;">									
									<select id="pendente" multiple="multiple" style="height:250px; width:380px;"></select>														
								</td>
								<td style="width:78px;">
									<input type="button" class="botaoTransfere" title="Adicionar" value="&rsaquo;" style="width:50px;" 
										onclick="transfereSelecionados('pendente','selecionado')"/><br /><br />
									<input type="button" class="botaoTransfere" title="Remover" value="&lsaquo;" style="width:50px;" 
										onclick="transfereSelecionados('selecionado', 'pendente')"/><br /><br />
									<input type="button" class="botaoTransfere" title="Adicionar todos" value="&raquo;" style="width:50px;" 
										onclick="transfereTodos('pendente','selecionado')"/><br /><br />
									<input type="button" class="botaoTransfere" title="Remover todos" value="&laquo;" style="width:50px;" 
										onclick="transfereTodos('selecionado', 'pendente')"/><br /><br />								
								</td>
								<td style="width:400px;">									
									<select id="selecionado" multiple="multiple" style="height:250px; width:380px;"></select>					
								</td>
							</tr>
							<tr>
								<td>
									<span>* No máximo, serão carregadas 100 pré-inscrições</span>
								</td>
								<td></td>
								<td></td>
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
						
						<label>Serão confirmadas </label>
						<input id="totalSeraoConfirmadas" style="text-align:right;" maxlength="4" size="2" value="0" readonly="readonly" /><br /><br />
					</td>					
				</tr>
				<tr>
					<td colspan="2">
						<br /><input type="button" id="botaoConfirmar" class="btn btn-primary" value="Confirmar pré-inscrições"/>
					</td>					
				</tr>				    		 
			</tbody>
		</table>
		
		</div>
	</form:form>
</div>
</body>
</html>