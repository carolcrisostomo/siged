<%@ include file="../includes.jsp"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="main" />
	<title><spring:message code="default.add.label" /></title>
</head>
<body>	
	<div class="body">
		<h1>Certificado de Instrutor</h1>
		<c:if test="${param.mensagem != null}">
			<div class="message">${param.mensagem}</div>
		</c:if>
		<c:if test="${param.mensagemErro != null}">
			<div class="messageErro">${param.mensagemErro}</div>
		</c:if>
		<c:url var="url" value="/certificado/impressaoInstrutor/" />
		<spring:url value="/ajax/instrutor/procuraPorModulo" var="procuraInstrutorUrl" />
		<spring:url value="/ajax/modulo/procuraRealizadosPorEvento" var="procuraModuloUrl" />
		<form:form action="${url}" method="POST" modelAttribute="relCertificado">
			<div class="filter">
				<table>
					<tbody>
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">(*) Campos Obrigatórios</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name">
								<label for="evento"><spring:message code="evento.evento.label" /></label>
							</td>
							<td valign="top">
								<form:select path="eventoId" id="eventoId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${eventosParaCertificadoInstrutor}" itemLabel="nome" itemValue="id" />
								</form:select>* <br />
								<form:errors path="eventoId" cssClass="error" />
							</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label for="moduloId"><spring:message
								code="evento.modulo.label" /></label>
							</td>
							<td valign="top">
								<form:select path="modulo" id="moduloId">
									<form:option value="0">Selecione...</form:option>
								</form:select>* <br/><form:errors path="modulo" cssClass="error" />
							</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name">
								<label for="instrutor">Instrutor</label>
							</td>
							<td valign="top">
								<form:select path="instrutorId" id="instrutorId">
									<form:option value="0">Selecione...</form:option>
								</form:select>* <br />
								<form:errors path="instrutorId" cssClass="error" />
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
	<script type="text/javascript" src="<c:url value="/static/js/ajax.loading.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/modulo.consulta.js"/>"></script>
	<script type="text/javascript">
		jQuery(function($){
			var moduloConsulta = new SIGED.ConsultaModuloPorEvento('#moduloId', '#eventoId', '${procuraModuloUrl}');
			var instrutorConsulta = new SIGED.ConsultaInstrutorPorModulo('#instrutorId', '#moduloId', '${procuraInstrutorUrl}');
			var loading = new SIGED.AjaxLoading();
			var moduloInput = $('#moduloId');
			var eventoInput = $('#eventoId');
			var instutorInput = $('#instrutorId');
			
			if(eventoInput.val() != 0)
				moduloConsulta.buscar(loading);
			
			moduloConsulta.event.bind('sucesso', function(event, totalModulos) {
				if(totalModulos == 0)
					alert("O evento em andamento não tem nenhum módulo encerrado");
				
				if(moduloInput.val() != 0)
					instrutorConsulta.buscar(loading);
			});
			
			eventoInput.change(function(event){
				var eventoId = $(this).val();
				moduloConsulta.limpar();
				instrutorConsulta.limpar();
				if(eventoId != 0)
					moduloConsulta.buscar(loading);
			});
			
			moduloInput.change(function(event){
				var moduloId = $(this).val();
				instrutorConsulta.limpar();
				if(moduloId != 0)
					instrutorConsulta.buscar(loading);
			});
			
		});
	</script>
</body>
</html>