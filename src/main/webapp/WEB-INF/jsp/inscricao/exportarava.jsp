<%@ include file="../includes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="inscricao.exportar.ava.title"/></title>
<spring:url value="/ajax/procuraEventoExportarParaAva" var="eventosUrl"></spring:url>
</head>
<body>
	<div class="body">
		<c:if test="${param.mensagem != null}">
			<div class="message">${param.mensagem}</div>
		</c:if>
		
		<div class="messageErro" style="display: none"></div>
		<h1><spring:message code="inscricao.exportar.ava.label"/></h1>

		<div class="filter">
			<c:url var="url" value="/inscricao/exportar/ava" />
			<form:form action="${url}" method="POST" modelAttribute="inscricaoAVAFiltro" class="formExportInscricoesAva">
				<table>
					<tbody>
						<%-- <tr>
							<td>
								<label for="titulo">Título Evento</label>
							</td>
							<td valign="top" class="value">
								<input type="text" id="tituloEvento" maxlength="255" name="titulo" size="30" style="width:250px" autocomplete="off"/>
								<input type="button" id="buscarEventos" class="search" style="width:250px"
									data-url="${eventosUrl}"/>
							</td>
						</tr> --%>
						<!-- EVENTO COMBO -->
						<tr class="prop">
							<td valign="top" class="name">
								<label for="eventoid"><spring:message code="inscricao.evento.label"/></label>
							</td>
							<td valign="top">
								<form:select path="eventoId" id="eventoid">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${eventos}" itemLabel="nome" itemValue="id" />
								</form:select>* <form:errors path="eventoId" cssClass="error" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="titulo">Curso no AVA</label>
							</td>
							<td valign="top" class="value">
								<form:input path="cursoNoAVA" id="cursoNoAVA" maxlength="255" size="10" cssStyle="width:50px" />* 
								<form:errors path="cursoNoAVA" cssClass="error" />
							</td>
						</tr>

						<tr>
							<td><input id="downloadCSV" type="submit" class="botao"
								value="Download CSV" /></td>
							<td></td>
						</tr>

					</tbody>
				</table>
			</form:form>
		</div>
			
	</div>
	<script type="text/javascript" src="<c:url value="/static/js/ajax.loading.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/evento.consulta.js"/>"></script>
	<script type="text/javascript">
		jQuery(document).ready(function($){
			var tituloEventoInput = $('#tituloEvento');
			
			$('.formExportInscricoesAva').keypress(function(event){
				var selector = tituloEventoInput.selector.replace('#', '').replace('.', '');
				if(event.originalEvent.srcElement.id == selector) {
					return event.keyCode != 13;
				}	
			});
			
			var loading = new SIGED.AjaxLoading();
			var eventoConsulta = new SIGED.ConsultaEventoPorTitulo('#eventoid');
			var divErrorMsg = $('.messageErro');
			
			$('#buscarEventos').click(function(event){
				divErrorMsg.hide();
				var titulo = $('#tituloEvento').val();
				var url = $(this).attr('data-url');
				eventoConsulta.buscar(loading, url, titulo);
			});
			
			eventoConsulta.event.bind('erro', function(event, messageClient, messageDev){
				console.error(messageDev);
				divErrorMsg.html(messageClient);
				divErrorMsg.show();
			});
			
			tituloEventoInput.keypress(function(event) {
				if(event.keyCode == 13) {
					$('#buscarEventos').trigger('click');
				}
			});
			
		});
	</script>
</body>
</html>