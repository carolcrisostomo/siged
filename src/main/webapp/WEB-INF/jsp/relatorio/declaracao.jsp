<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>	
		
	<spring:url value="/relatorio/procuraParticipanteAprovado" var="procuraParticipanteUrl" />

	<div class="body">
		<h1>Declaração de Participante</h1>
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>		
		
		<c:url var="url" value="/relatorio/declaracao/" />	
		
		<form:form action="${url}" method="POST"
			modelAttribute="relDeclaracao">
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
							<td valign="top">
								<form:select path="eventoId" id="eventoId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${eventoEmAndamentoList}" itemLabel="nome"
										itemValue="id" />
								</form:select>* <br />
							<form:errors path="eventoId" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="evento"><spring:message
										code="evento.participante.label" /></label></td>
							<td valign="top"><form:select path="participanteId"
									id="participanteId">
									<form:option value="0">Selecione...</form:option>
								</form:select>* <br />
							<form:errors path="participanteId" cssClass="error" /></td>
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
	<script type="text/javascript" src="<c:url value="/static/js/ajax.loading.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/participante.consulta.js"/>"></script>
	<script type="text/javascript">
		jQuery(function($){
			var participanteConsulta = new SIGED.ConsultaParticipantePorEvento('#participanteId', '#eventoId', '${procuraParticipanteUrl}');
			var loading = new SIGED.AjaxLoading();
			var eventoInput = $('#eventoId');
			var participanteInput = $('#participanteId');
			
			//participanteConsulta.event.bind('sucesso', function(event, totalParticipantes) {
				//participanteInput.prepend("<option value='0'>Selecione...</option>");
			//});
			
			if(eventoInput.val() != 0)
				participanteConsulta.buscar(loading);
			
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
</body>
</html>