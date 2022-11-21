<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<spring:url value="/material/procuraModulo" var="procuraModuloUrl" />
</head>
<body>
	<script type="text/javascript">
		jQuery(document).ready(
				function($) {
					updateModuloSelected('${procuraModuloUrl}', 'eventoId',
							'moduloId', '${material.moduloId.id}', false);

					$('#insert-material').click(function() {
						$('#material').click();
					});
					
					$('#deleteArquivo')
					.click(
							function() {
								var form = document
										.getElementById("formDeleteArquivo")
								form.submit()

							})


					if ($("#materialTipo").val() == 1) {
						$("#moduloLinha").show();
					} else {
						$("#moduloLinha").hide();
						$("#moduloId").val(0);
					}

					$("#eventoId").change(
							function() {
								updateModulo('${procuraModuloUrl}', 'eventoId',
										'moduloId');
							});

					$("#materialTipo").change(function() {
						if ($("#materialTipo").val() == 1) {
							$("#moduloLinha").show();
						} else {
							$("#moduloLinha").hide();
							$("#moduloId").val(0);
						}
					});
					
					
					
					
					$('#insertArquivo').click(function() {
						$('#material').click();
					});
					

					$('#material')
							.change(
									function(event) {
										var filePath = event.target.value
										var fileName = filePath
												.substr(
														filePath
																.lastIndexOf('\\') + 1)
												.split('.')[0]

										var materialFileName = document
												.getElementById('arquivoNome')

										materialFileName.innerHTML = fileName
										
	
									});
					
					$('#material-fileName')
					.click(
							function(e) {
								var materialValue = document
										.getElementById('material-fileName')
								if (materialValue.value == '/') {
									$('#material').click();
								} else {
									document
											.getElementById(
													'materialDownload')
											.click();
								}

							})

				});
	</script>

	<div class="body">
		<h1>
			<spring:message code="default.button.edit.label" />
			- Material Did�tico / Divulga��o
		</h1>
		<c:url var="url" value="/material/${material.id}/update" />
		<form:form action="${url}" id="materialForm" method="POST" modelAttribute="material"
			enctype="multipart/form-data">
			<form:hidden path="id" />
			<div class="dialog">
				<table>
					<tbody>
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">(*)
								Campos Obrigat�rios</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="materialTipo">Tipo
									Material</label></td>
							<td><form:select path="materialTipo">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${materialTipoList}" />
								</form:select>* <form:errors path="materialTipo" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="eventoId">Evento</label></td>
							<td><form:select path="eventoId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${eventoList}" itemLabel="nome"
										itemValue="id" />
								</form:select>* <form:errors path="eventoId" cssClass="error" /></td>
						</tr>
						<tr class="prop" id="moduloLinha">
							<td valign="top" class="name"><label for="moduloId">M�dulo</label></td>
							<td><form:select path="moduloId">
									<form:option value="0">Selecione...</form:option>
								</form:select>* <form:errors path="moduloId" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="descricao">Descri��o</label>
							</td>
							<td><form:input cssStyle="width:400px" maxlength="100"
									path="descricao"
									onkeyup="javascript:this.value=this.value.toUpperCase();" />*
								<form:errors path="descricao" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="arquivo">Material</label></td>
							<td>
							
							
														<span>Tamanho m�ximo do arquivo: 10 MB</span>
							<div class="row g-3">
									<div class="col">

											<a id="arquivoNome" href="<c:url value="/material/arquivo/${material.id}"/>">${material.arquivoOriginal}</a>

									</div>
									<div class="col">
										<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
											<span class="menuButton"> 
													<a id="insertArquivo" href="" onclick="return false;"
														class=insert><spring:message
															code="default.button.insert_file.label" /></a>
											</span>
										</sec:authorize>
									</div>
								</div>
							
							
							
							
							<form:input id="material" cssStyle="display: none;"
									path="arquivo" type="file" />

							</span> <br /> <form:errors path="arquivo" cssClass="error" /></td>
							</p>
							</td>

						</tr>
					</tbody>
				</table>
			</div>


			<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">

				<div class="mx-auto" style="width: 400px;">
					<div class="d-flex justify-content-around p-2 ">

						<a href="<c:url value="/material/"/>"><button type="button"
								class="btn btn-outline-secondary">
								<spring:message code="default.button.list.label" />
							</button></a>
						<button id="atualizar" type="submit" class="btn btn-primary">
							<spring:message code="default.button.save.label" />
						</button>

					</div>
				</div>
			</sec:authorize>
		</form:form>
	</div>
	
</body>
</html>
