<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
<spring:url value="/avaliacaoreacao/procuraModuloComAvaliacoes" var="procuraModuloUrl" />
</head>
<body>
	<div class="body">
		<c:if test="${param.mensagem != null}">
			<div class="message">${param.mensagem}</div>
		</c:if>
		<h1><spring:message code="relatorio.avaliacaoParticipanteEvento.label" /></h1>
		<c:url var="url" value="/relatorio/avaliacoesReacaoParticipantePorEvento/" /> 
		<form:form action="${url}" method="POST" modelAttribute="relAvaliacoesReacaoParticipantePorEvento">
			<div class="filter">
				<table>
					<tbody>
						<tr>
							<td></td>
							<td style="text-align:right;" valign="top" class="name">(*) Campos Obrigatórios</td>
						</tr>		
						<tr class="prop">
							<td valign="top" class="name"><label for="evento"><spring:message
								code="avaliacao.evento.label" /></label></td>
							<td valign="top">
							<form:select path="eventoId" id="eventoid">
								<form:option value="0">Selecione...</form:option>
								<form:options items="${eventoAvaliadoList}" itemLabel="nome" itemValue="id" />
							</form:select>*	<br/><form:errors path="eventoId" cssClass="error" />
							</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label for="evento"><spring:message
								code="avaliacao.modulo.label" /></label></td>
							<td valign="top">
							<form:select path="modulo" id="moduloid">
								<form:option value="0">TODOS</form:option>
							</form:select><br/><form:errors path="modulo" cssClass="error" />
							</td>
						</tr>
									
						<tr>				
							<td colspan="2" style="padding: 15px 6px;">
								<form:checkbox  id="checkbox" path="automatico" cssStyle="float: left;"/>
								<span style="margin-left: 5px;">Exibir comentários adicionais dos participantes</span>
							</td>
						</tr>
				      	<tr>
			               	<td>
			                  <input id="filtrar" type="submit" class="botao"	value="Filtrar" />
			                </td>
			                <td></td>
			    		</tr>
					</tbody>
				</table>
			</div>
		</form:form>
	</div>
	<script type="text/javascript" src="<c:url value="/static/js/ajax.loading.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/modulo.consulta.js"/>"></script>
	<script type="text/javascript">
		jQuery(function($){
			var moduloConsulta = new SIGED.ConsultaModuloPorEvento('#moduloid', '#eventoid', '${procuraModuloUrl}');
			var loading = new SIGED.AjaxLoading();
			var moduloInput = $('#moduloid');
			var eventoInput = $('#eventoid');
			
			moduloConsulta.setOptionDefault("<option value='0' selected='selected'>TODOS</option>");
			
			if(eventoInput.val() != 0)
				moduloConsulta.buscar(loading);
			
			eventoInput.change(function(event){
				var eventoId = $(this).val();
				if(eventoId != 0) {
					moduloConsulta.buscar(loading);
				} else {
					moduloConsulta.limpar();
				}
			});
			
		});
	</script>
</body>
</html>