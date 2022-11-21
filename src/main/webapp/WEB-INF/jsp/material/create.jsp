<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<spring:url value="/material/procuraModulo" var="procuraModuloUrl" />
</head>
<body>
	<script type="text/javascript">
		jQuery(document).ready(
				function($) {
					
					
					
					$('#material').change(function(event){
				    	var filePath = event.target.value
					    var fileName = filePath.substr(filePath.lastIndexOf('\\') + 1).split('.')[0]
				    	
				    	var curriculoFileName = document.getElementById('material-fileName')
				    	
				    	curriculoFileName.value = fileName
				    	
					});
					
					 $('#material-fileName').click(function(e){
					    	var assinaturaValue = document.getElementById('material-fileName')
					    	if(assinaturaValue.value == '/'){
							    $('#material').click();
					    	}
					    	else{
					    	document.getElementById('materialDownload').click();
					    	}

					    })
					

					$('#insert-material').click(function() {
						$('#material').click();
					});
					updateModuloSelected('${procuraModuloUrl}', 'eventoId',
							'moduloId', '${material.moduloId.id}', false);

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

				});
	</script>

	<div class="body">

		<c:if test="${param.mensagemErro != null}">
			<div class="messageErro">${param.mensagemErro}</div>
		</c:if>
		<div class="message info">
			<spring:message code="evento.material.envioemail.info" />
		</div>

		<h1>
			<spring:message code="default.add.label" />
			- Material Didático/Divulgação
		</h1>

		<c:url var="url" value="/material" />
		<form:form action="${url}" method="POST" modelAttribute="material"
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
								</form:select>* <br />
							<form:errors path="eventoId" cssClass="error" /></td>
						</tr>
						<tr class="prop" id="moduloLinha">
							<td valign="top" class="name"><label for="moduloId">Módulo</label></td>
							<td><form:select path="moduloId">
									<form:option value="0">Selecione...</form:option>
								</form:select>* <form:errors path="moduloId" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="descricao">Descrição</label>
							</td>
							<td><form:input cssStyle="width:400px" maxlength="60"
									path="descricao"
									onkeyup="javascript:this.value=this.value.toUpperCase();" />
								<form:errors path="descricao" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="arquivo">Material</label></td>
							<td><form:input id="material" cssStyle="display: none;"
									path="arquivo" type="file" />


								<div class="row g-3">

									<div class="col">
										<input id="material-fileName" class="form-control w-30"
											type="text" readonly value=${ material.arquivoOriginal} />
									</div>*
									<div class="col">
										<span class="menuButton"> <a id="insert-material"
											class="insert" href="" onclick="return false;"><spring:message
													code="default.button.insert_file.label" /></a>
										</span>
									</div>
								</div> <br />
								<p>
									<form:errors path="arquivo" cssClass="error" />
								</p> <span>Tamanho máximo do arquivo: 10 MB</span></td>
						</tr>
					</tbody>
				</table>
			</div>


			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/material/"/>"><button type="button"
							class="btn btn-outline-secondary">
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
