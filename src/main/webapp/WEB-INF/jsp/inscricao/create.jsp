<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="inscricao.label" /></title>
<spring:url value="/ajax/procuraParticipantePorNome" var="procuraParticipantePorNomeUrl" />
<spring:url value="/ajax/procuraParticipantePorNomeOuCPF" var="procuraParticipantePorNomeOuCPFUrl" />
<spring:url	value="/ajax/procuraParticipanteSolicitacaoSemInscricao" var="procuraParticipanteSolicitacaoUrl" />
<spring:url	value="/ajax/obterTotalDeVagasETotalDeInscritosNoEvento" var="vagasInscritosUrl" />
</head>
<body>
	<div class="body">
		<h1>
			<spring:message code="default.add.label" />
			-
			<spring:message code="inscricao.label" />
		</h1>

		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
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
			if (request.getParameter("mensagemSucesso") != null) {
		%><div class="messageSucesso"><%=request.getParameter("mensagemSucesso")%></div>
		<%
			}
		%>

		
		
		<c:url var="url" value="/inscricao" />
		
		<form:form id="formulario" action="${url}" method="POST" modelAttribute="inscricao">
			<div class="dialog">
				<table>
					<tbody>
						
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">(*)
								Campos Obrigatórios</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label for="evento">
								<spring:message code="inscricao.evento.label" /></label>
							</td>
							<td><form:select path="eventoId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${eventoList}" itemLabel="nome" itemValue="id" />
								</form:select>*<br />
								<form:errors path="eventoId" cssClass="error" />
							</td>
						</tr>
						
						<tr class="prop">
							
							<td valign="top" class="name">
								<label for="participante"><spring:message code="inscricao.participante.label" /></label>
							</td>
							<td>
								<form:input	cssStyle="width:250px" maxlength="255" path="nomeParticipante" id="nomeParticipante" placeholder="Nome ou CPF"/>
								
								<input id="buscarParticipante" class="search" type="button" />
								
								<form:select path="participanteId" cssStyle="width:450px">
									<form:option value="0">Selecione...</form:option>
								</form:select>* <form:errors path="participanteId" cssClass="error" />
								<span id="participanteErro" class="error"></span>
							</td>
						
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="solicitacao"><spring:message
										code="inscricao.solicitacao.label" /></label></td>
							<td><form:select path="solicitacaoId">
									<form:option value="0">Não se Aplica</form:option>
								</form:select></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="justificativa"><spring:message
										code="inscricao.justificativa.label" /></label></td>

							<td><form:textarea path="justificativa" cols="70" rows="5" maxlength="2000" /></td>
						</tr>
					</tbody>
				</table>
			</div>

			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/inscricao/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="criar" type="button" class="btn btn-primary">
 						<spring:message code="default.add.label" />
					</button>

				</div>
			</div>
			
		</form:form>
	</div>
	<script type="text/javascript" src="<c:url value="/static/js/nomeOuCpf.mask.js"/>"></script>
	<script type="text/javascript"> 
		jQuery(document).ready(function($){	
			
			updateParticipantePorNome('${procuraParticipantePorNomeOuCPFUrl}', 'nomeParticipante', 'participanteId', 'participanteErro', '${inscricao.participanteId.id}', '', '');
			updateParticipanteSolicitacao('${procuraParticipanteSolicitacaoUrl}', '${inscricao.participanteId.id}', 'solicitacaoId');			
			
			$("#buscarParticipante").click(function($){
				updateParticipantePorNome('${procuraParticipantePorNomeOuCPFUrl}', 'nomeParticipante', 'participanteId', 'participanteErro', '', '', 'true');
			});
			
			var nomeOuCpfMask = new SIGED.NomeOuCpfMask('#nomeParticipante');
			nomeOuCpfMask.iniciar();
			
			$("#participanteId").change(function(){
				updateParticipanteSolicitacao('${procuraParticipanteSolicitacaoUrl}', $("#participanteId").val(), 'solicitacaoId');
			});
			
			$("#criar").click(function(){
				
				if($("#eventoId").val() != 0 && $("#participanteId").val() != 0){
					$.getJSON(
						"${vagasInscritosUrl}",
						{eventoId: $("#eventoId").val()}, 
						function(vagasEInscricoes) {						
							if(vagasEInscricoes[0] < vagasEInscricoes[1] + 1) {
								var resposta = confirm("Já existem " + vagasEInscricoes[1] + " inscrições confirmadas para " + vagasEInscricoes[0] + " vagas ofertadas. Incluir mesmo assim?");
								if(resposta == true){					
									$("#formulario").submit();
								}					
							}else{							
								$("#formulario").submit();
							}
						}
					);
				}else{				
					$("#formulario").submit();
				}
		 	});
			
			
			$(document).ajaxStop($.unblockUI);			
			
		});	
	</script>
</body>
</html>
