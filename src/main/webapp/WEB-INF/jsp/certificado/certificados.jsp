<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>

	<spring:url value="/relatorio/procuraParticipanteAprovado" var="procuraParticipanteUrl" />
	<spring:url value="/avaliacaoreacao/avaliacoesPendentes" var="avaliacaoPendenteUrl" />
	<spring:url value="/avaliacaoreacao/formaluno/" var="avaliacaoReacaoAluno" />

	<div class="body">
	
		<h1><spring:message code="evento.emitirCertificado.label" /></h1>
				
		<div class="message">A versão impressa do certificado contém o conteúdo programático do evento no verso.</div>
				
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>
		<c:if test="${mensagem!=null}">
			<div class="message">${mensagem}</div>
		</c:if>
				
		<c:if test="${mensagemRel != null}">
			<div class="message" id="msgId">
				<c:out value="${mensagemRel}" />
			</div>
		</c:if>
		
		<c:if test="${mensagemErro != null}">
			<div class="messageErro">${mensagemErro}</div>
		</c:if>
		
		<c:url var="url" value="/certificado/impressao/" />
		
		<form:form action="${url}" method="POST"
			modelAttribute="relCertificado" id="form1" class="form_emissao_certificado">
			<div class="filter">
				<table>
					<tbody>
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">(*)
								Campos Obrigatórios</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label for="evento"><spring:message
										code="evento.evento.label" /></label></td>
							<td valign="top"><form:select path="eventoId" id="eventoId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${eventos}" itemLabel="nome"
										itemValue="id" />
								</form:select>*<br />
							<form:errors path="eventoId" cssClass="error" /></td>
						</tr>
						
						<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
							<tr class="prop">
								<td valign="top" class="name"><label for="evento"><spring:message
											code="evento.participante.label" /></label></td>
								<td valign="top"><form:select path="participanteId"
										id="participanteId">
										<form:option value="0">Selecione...</form:option>
									</form:select>*<br />
								<form:errors path="participanteId" cssClass="error" /></td>
							</tr>						
						</sec:authorize>
						<sec:authorize ifAnyGranted="ROLE_ALUNO">							
							<form:hidden path="participanteId" id="participante_evento" value="${sessionScope.PARTICIPANTE_ID}" />
						</sec:authorize>
						
						<tr>
							<td><input id="filtrar" type="submit" class="botao input_emissao_certificado"
								value="Emitir Certificado" /></td>
							<td></td>
						</tr>
					
					</tbody>
				</table>
			</div>
		</form:form>
	</div>
	<sec:authorize ifAnyGranted="ROLE_ALUNO">
	<script type="text/javascript" src="<c:url value="/static/js/avaliacaoreacao.pendente.js"/>"></script>
	<script type="text/javascript">
		jQuery(function($){
			var avaliacaoReacaoPendente = new SIGED.AvaliacaoReacaoPendente();
			var inputEmissao = $('.input_emissao_certificado');
			
			inputEmissao.click(function(event){
				event.preventDefault();
				var eventoInput = $('#eventoId');
				var participanteInput = $('#participante_evento');
				var urlConsulta = '${avaliacaoPendenteUrl}' + '/' + participanteInput.val() + '/' + eventoInput.val();
				var urlAvaliacao = '${avaliacaoReacaoAluno}';
				if(eventoInput.val() != 0)
					avaliacaoReacaoPendente.buscar(urlConsulta, urlAvaliacao, '.form_emissao_certificado');
			});
		});
	</script>
	</sec:authorize>
	
	<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
	<script type="text/javascript" src="<c:url value="/static/js/ajax.loading.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/participante.consulta.js"/>"></script>
	<script type="text/javascript">
		jQuery(function($){
			var participanteConsulta = new SIGED.ConsultaParticipantePorEvento('#participanteId', '#eventoId', '${procuraParticipanteUrl}');
			var loading = new SIGED.AjaxLoading();
			var eventoInput = $('#eventoId');
			var participanteInput = $('#participanteId');
			
			/* participanteConsulta.event.bind('sucesso', function(event, totalParticipantes) {
				participanteInput.prepend("<option value='0'>Selecione...</option>");
			}); */
			
			if(eventoInput.val() != 0) {
				participanteConsulta.buscar(loading);
			}
				
			eventoInput.change(function(event){
				var eventoId = $(this).val();
				if(eventoId != 0) {
					participanteConsulta.buscar(loading);
				} else {
					participanteConsulta.limpar();
					participanteInput.append("<option value='0'>Selecione...</option>");
				}
			});
		});
	</script>
	</sec:authorize>
</body>
</html>