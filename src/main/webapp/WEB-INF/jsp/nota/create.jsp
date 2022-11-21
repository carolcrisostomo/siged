<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="nota.label" /></title>
<spring:url value="/nota/procuraModulo" var="procuraModuloUrl" />
<spring:url value="/nota/procuraParticipante" var="procuraParticipanteUrl" />
<spring:url value="/nota/form" var="processCSVFileUrl" />
</head>
<body>

	<script type="text/javascript">
		jQuery(document).ready(function($) {
			var file = null;
			var fileext = '';
			jQuery('.csvFileContainer').show();
			//$('#list').hide();
			$('#mensagem').hide();
			if($('#moduloId').val() == 0 || $('#eventoId').val() == 0)
				$('#criar').attr('disabled', 'disabled').css('color', '#c3c3c3');

			$('#eventoId').change(function() {
				$('#mensagem').hide();
				$('#list').hide();
				$('#list').html('');
				$('#criar').attr('disabled', 'disabled').css('color', '#c3c3c3');
			});

			$('#moduloId').change(function() {
				if ($('#moduloId').val() != 0) {
					updateParticipanteNotaGrid('${procuraParticipanteUrl}', 'moduloId','list');
				} else {
					$('#list').hide();
					$('#list').html('');
					$('#criar').attr('disabled', 'disabled').css('color', '#c3c3c3');
				}
			});
			
			$('#calcularMedia').change(function() {
				if($(this).val() == 'true') {
					$('.mediaValor').show();
				} else {
					$('.mediaValor').hide();
				}
			});
			
			jQuery('#CSVfile').change(function() {
				file = $(this)[0].files[0];
				var ext = file.name.split('.');
				fileext = ext[ext.length - 1];
			});
			jQuery('#uploadCSVfileButton').click(function(event) {
				event.preventDefault;
				if(file == null) {
					$('.fileError').html('Arquivo não selecionado');
				} else if (file.size > 100000) {
					$('.fileError').html('Arquivo deve ser menor que 100KB');
				} else if (fileext == '' || fileext != 'csv') {
					$('.fileError').html('O Arquivo precisa ser do tipo CSV');
				} else if ($('#moduloId').val() == 0 || $('#eventoId').val() == 0) {
					$('.fileError').html('Informe o Módulo e o Evento');
				} else {
					jQuery("#form").attr("action", '${processCSVFileUrl}');
					jQuery("#form").submit();
				}
			});
		});
	</script>
	<div class="body">

		<div class="message" id="mensagem">
			<!-- Conteúdo dinâmico -->
		</div>
		<c:if test="${mensagemErro != null}">
			<div class="messageErro">${mensagemErro}</div>
		</c:if>
		<h1>
			<spring:message code="default.add.label" />
			-
			<spring:message code="nota.label" />
		</h1>

		<c:url var="url" value="/nota" />
		<form:form action="${url}" method="POST" modelAttribute="nota" id="form" enctype="multipart/form-data">
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
										code="nota.evento.label" />:</label></td>
							<td><form:select path="eventoId" id="eventoId"
									onchange="updateModulo('${procuraModuloUrl}', 'eventoId', 'moduloId');">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${eventoList}" itemLabel="nome" itemValue="id" />
								</form:select>* <form:errors path="eventoId" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name">
								<label for="modulo"><spring:message code="nota.modulo.label" />:</label>
							</td>
							<td>
								<form:select path="moduloId" id="moduloId" itemLabel="titulo" itemValue="id">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${modulos}" itemLabel="titulo" itemValue="id" />
								</form:select>
								
								* <form:errors path="moduloId" cssClass="error" /></td>
						</tr>

						<tr class="prop csvFileContainer" style="display: none">
							<td valign="top" class="name"><label for="file">Arquivo CSV</label></td>
							<td>
								<input id="CSVfile" style="width:270px" name="file" type="file" title="Somente arquivos do tipo CSV"/>
								<select name="calcularMedia" id="calcularMedia" style="padding:4px">
									<option value="false">NÃO CALCULAR MÉDIA</option>
									<option value="true">CALCULAR MÉDIA</option>
								</select>
								
								<input type="number" name="mediaValor" placeholder="Média por" class="mediaValor" style="padding:5px 4px; width: 75px; display: none"/>
								
								<input id="uploadCSVfileButton" type="button" class="botao" value="Processar"
								style="margin-top: 0" />
								<div style="display: block; padding: 5px 0" class="fileError error">
								</div>
								<div style="display: block">
									<span>Tamanho máximo do arquivo: 100KB</span>
								</div>
							</td>
						</tr>


						<tr>
							<td><br></td>
						</tr>

					</tbody>
				</table>
			</div>

			<div id="list" class="list" style="max-height: 300px; overflow: auto;">
				<c:if test="${notas != null && notas.size() > 0}">
					<table id="nota">
						<thead>
							<tr>
								<th><font color="black" size="1em">Evento</font></th>
								<th><font color="black" size="1em">Módulo</font></th>
								<th><font color="black" size="1em">Participante</font></th>
								<th><font color="black" size="1em">Nota</font></th>
							</tr>
						<thead>
						<tbody>
						<c:forEach items="${notas}" varStatus="count" var="nota">
							<c:if test="${count.index % 2 == 0}">
							<tr class="odd">
							</c:if>
							<c:if test="${count.index % 2 != 0}">
							<tr class="even">
							</c:if>
								<td>${nota.eventoId.nome}</td>
								<td>${nota.moduloId.titulo}</td>
								<td>${nota.participanteId.nomeCpf}
									<input type="hidden" name="participanteId" value="${nota.participanteId.id}">
								</td>
								<td>
									<input type="text" name="nota" value="${nota.nota}"
										onkeypress="validate(event)" size="2" maxLength="4"/>
								</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</c:if>
			</div>
			
			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/nota/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="criar" type="submit" class="btn btn-primary">
 						<spring:message code="default.add.label" />
					</button>

				</div>
			</div>
		</form:form>
	</div>
	<script type="text/javascript">
		//Essa função faz com que os inputs de nota aceitem apenas números e vírgula
		function validate(evt) {
			var theEvent = evt || window.event;
			var key = theEvent.keyCode || theEvent.which;
			key = String.fromCharCode(key);
			var regex = /[0-9]|\,/;
			if (!regex.test(key)) {
				theEvent.returnValue = false;
				if (theEvent.preventDefault)
					theEvent.preventDefault();
			}
		}
	</script>
</body>
</html>
