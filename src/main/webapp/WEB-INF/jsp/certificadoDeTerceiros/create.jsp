<%@ include file="../includes.jsp"%>



<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="certificado.label" /></title>
<spring:url value="/certificadoDeTerceiros/procuraServidores"
	var="procuraParticipanteUrl" />
</head>
<body>

	<script type="text/javascript">
		jQuery(document).ready(
				function($) {

					$('#insert-certificado').click(function() {
						$('#certificado').click();
					});
					
					$('#certificado').change(function(event){
						var filePath = event.target.value
					    var fileName = filePath.substr(filePath.lastIndexOf('\\') + 1).split('.')[0]
				    	
				    	var certificadoFileName = document.getElementById('certificado-filename')
				    	
				    	certificadoFileName.value = fileName
						
					})
					
					$('#certificado-filename').click(function() {
						$('#certificado').click();
					});
					$(document).ajaxStop($.unblockUI);
					updateParticipante('${procuraParticipanteUrl}', 'eventoId',
							'participanteId');

					$("#eventoId").change(
							function() {
								updateParticipante('${procuraParticipanteUrl}',
										'eventoId', 'participanteId');
							});

				});
	</script>
	<div class="body">
		<%
		if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
		}
		%>
		<h1>
			<spring:message code="default.add.label" />
			-
			<spring:message code="certificado.label" />
		</h1>

		<c:url var="url" value="/certificadoDeTerceiros" />
		<form:form action="${url}" method="POST" modelAttribute="certificado"
			id="form" enctype="multipart/form-data">
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
										code="certificado.evento.label" />:</label></td>
							<td><form:select path="eventoId" id="eventoId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${eventosRealizadosTerceirosList}"
										itemLabel="nome" itemValue="id" />
								</form:select>* <br />
							<form:errors path="eventoId" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="participante"><spring:message
										code="certificado.participante.label" />:</label></td>
							<td><form:select path="participanteId" id="participanteId"
									itemLabel="nomeCpf" itemValue="id">
									<form:option value="0">Selecione...</form:option>
								</form:select>* <form:errors path="participanteId" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="certificado"><spring:message
										code="certificado.arquivo.label" />:</label></td>
							<td>
								<p class="formatosArquivo">Formatos aceitos: .JPG e .PDF</p>
								<div class="row g-3">

									<div class="col">
										<input id="certificado-filename" class="form-control w-30"
											type="text" readonly  />
									</div>
									<div class="col">
										<span class="menuButton"> <a id="insert-certificado"
											class="insert" href="" onclick="return false;"><spring:message
													code="default.button.insert_file.label" /></a>
										</span>
									</div>
								</div> <form:input id="certificado" cssStyle="display: none;"
									path="arquivo" size="30" type="file" />* <span
								class="menuButton"> 

							</span>
								<p>
									<form:errors path="arquivo" cssClass="error" />
								</p>
							</td>
						</tr>
					</tbody>
				</table>
			</div>


			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/certificadoDeTerceiros/"/>"><button
							type="button" class="btn btn-outline-secondary">
							<spring:message code="default.button.list.label" />
						</button></a>
					<button id="criar" type="submit" class="btn btn-primary">
						<spring:message code="default.add.label" />
					</button>

				</div>
			</div>
		</form:form>
	</div>


</body>
</html>
