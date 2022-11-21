<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="default.button.edit.label" /> - <spring:message
		code="avaliacao.label" /></title>
<spring:url value="/ajax/procuraMunicipio" var="procuraMunicipioUrl" />

</head>
<body>
	<script type="text/javascript">
		jQuery(document)
				.ready(
						function($) {

							updateMunicipioSelected('${procuraMunicipioUrl}',
									'ufMunicipio', 'municipio',
									'${provedorevento.municipio.id}');

							if ($("#municipio").val() != 0
									&& $("#municipio").val() != null) {
								$("#paisId").val(1);
							}

							if ($("#paisId").val() == 1) {
								$("#ufLinha").show();
								$("#cidadeLinha").show();
								$("#bairro").show();
								$("#cep").show();
							} else {
								$("#ufMunicipio").val(0);
								$("#municipio")
										.html(
												'<option value="0">Selecione...</option>')
										.trigger('chosen:updated');
								$("#ufLinha").hide();
								$("#cidadeLinha").hide();
								$("#bairro").hide();
								$("#cep").hide();
							}

							$("#paisId")
									.change(
											function() {
												if ($("#paisId").val() == 1) {
													$("#ufLinha").show();
													$("#cidadeLinha").show();
													$("#bairro").show();
													$("#cep").show();
												} else {
													$("#ufMunicipio").val(0);
													$("#municipio")
															.html(
																	'<option value="0">Selecione...</option>')
															.trigger(
																	'chosen:updated');
													$("#ufLinha").hide();
													$("#cidadeLinha").hide();
													$("#bairro").hide();
													$("#cep").hide();
												}
											});

							$(document).ajaxStop($.unblockUI);
						});
	</script>
	<script type="text/javascript">
		jQuery(document)
				.ready(
						function($) {

							$('#insert-assinatura').click(function() {
								$('#assinatura').click();
							});

							$('#insert-curriculo').click(function() {
								$('#curriculo').click();
							});

							$('#curriculo')
									.change(
											function(event) {
												var filePath = event.target.value
												var fileName = filePath
														.substr(
																filePath
																		.lastIndexOf('\\') + 1)
														.split('.')[0]

												var curriculoFileName = document
														.getElementById('curriculo-fileName')

												curriculoFileName.value = fileName

											});

							$('#assinatura')
									.change(
											function(event) {
												var filePath = event.target.value
												var fileName = filePath
														.substr(
																filePath
																		.lastIndexOf('\\') + 1)
														.split('.')[0]

												var assinaturaFileName = document
														.getElementById('assinatura-fileName')

												assinaturaFileName.value = fileName

											});

							$('#assinatura-fileName')
									.click(
											function(e) {
												var assinaturaValue = document
														.getElementById('assinatura-fileName')
												if (assinaturaValue.value == '/') {
													$('#assinatura').click();
												} else {
													document
															.getElementById(
																	'assinaturaDownload')
															.click();
												}

											})
							$('#curriculo-fileName')
									.click(
											function() {

												var curriculoValue = document
														.getElementById('curriculo-fileName')
												if (curriculoValue.value == '/') {
													$('#curriculo').click();
												} else {
													document
															.getElementById(
																	'curriculoDownload')
															.click();
												}

											})

							$('#deleteAssinatura')
									.click(
											function() {
												var form = document
														.getElementById("formDeleteAssinatura")
												form.submit()

											})

							$('#deleteCurriculo')
									.click(
											function() {
												var form = document
														.getElementById("formDeleteCurriculo")
												form.submit()

											})

							if ($("#situacaoId").val() == 3) {
								$("#linhaJustificativa").show();
							} else {
								$("#linhaJustificativa").hide();
							}

							$("#situacaoId").change(function() {
								if ($("#situacaoId").val() == 3) {
									$("#linhaJustificativa").show();
								} else {
									$("#linhaJustificativa").hide();
								}
							});

							$(document).ajaxStop($.unblockUI);
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
			<spring:message code="default.button.edit.label" />
			-
			<spring:message code="instrutor.label" />
		</h1>
		<c:url var="url" value="/instrutor/${instrutor.id}/update" />
		<form:form action="${url}" method="POST" modelAttribute="instrutor"
			enctype="multipart/form-data">
			<div class="dialog">
				<table>
					<tbody>
						<tr class="prop">
							<td valign="top" class="name"><label for="cpf"><spring:message
										code="instrutor.cpf.label" />:</label></td>
							<td><form:input cssStyle="width:110px" maxlength="255"
									path="cpf" size="30" alt="cpf" /> <form:errors path="cpf"
									cssClass="error" /></td>
							<td valign="top" class="name">(*) Campos Obrigatrios</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="nome"><spring:message
										code="instrutor.nome.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="255"
									id="name" path="nome"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="30" />* <form:errors path="nome" cssClass="error" /></td>

						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="logradouro"><spring:message
										code="instrutor.logradouro.label" />:</label></td>
							<td><form:input cssStyle="width:400px" maxlength="255"
									path="logradouro"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="30" /> <form:errors path="logradouro" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="numero"><spring:message
										code="instrutor.numero.label" />:</label></td>
							<td><form:input cssStyle="width:60px" maxlength="255"
									path="numero" size="30" /> <form:errors path="numero"
									cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="complemento"><spring:message
										code="instrutor.complemento.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="255"
									path="complemento"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="30" /> <form:errors path="complemento" cssClass="error" /></td>
						</tr>
						<!--  
			<tr class="prop">
				<td valign="top" class="name">
					<label for="ufMunicipio"><spring:message code="participante.uf.label" />:</label>
				</td>
				<td>
					<form:select path="ufMunicipio"  onchange="updateMunicipio('${procuraMunicipioUrl}', 'ufMunicipio', 'municipio')">
						
						<form:options items="${ufMunicipioList}" itemLabel="nome" itemValue="uf"/>
					</form:select>* <form:errors path="ufMunicipio" cssClass="error" />
				</td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="cidade"><spring:message
					code="instrutor.cidade.label" />:</label></td>

				<td><form:select path="municipio" id="municipio" >
							<option value="${instrutor.municipio.id}">${instrutor.municipio.nome}</option>
					</form:select>*<form:errors path="municipio" cssClass="error" /></td>
			-->
						<tr class="prop">
							<td valign="top" class="name"><label for="paisId"><spring:message
										code="provedorEvento.pais.label" />:</label></td>
							<td><form:select path="paisId" id="paisId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${listaPais}" itemLabel="descricao"
										itemValue="id" />
								</form:select></td>
						</tr>
						<tr class="prop" id="ufLinha">
							<td valign="top" class="name"><label for="uf"><spring:message
										code="provedorEvento.uf.label" />:</label></td>
							<td><form:select path="ufMunicipio" id="ufMunicipio"
									onchange="updateMunicipio('${procuraMunicipioUrl}', 'ufMunicipio', 'municipio')">
									<option value="0">Selecione...</option>
									<form:options items="${listaUf}" itemLabel="nome"
										itemValue="uf" />
								</form:select>* <form:errors path="ufMunicipio" cssClass="error" /></td>
						</tr>
						<tr class="prop" id="cidadeLinha">
							<td valign="top" class="name"><label for="municipio"><spring:message
										code="provedorEvento.cidade.label" />:</label></td>
							<td><form:select path="municipio" id="municipio">

								</form:select>* <form:errors path="municipio" cssClass="error" /></td>
						</tr>

						</tr>
						<tr class="prop" id="bairro">
							<td valign="top" class="name"><label for="bairro"><spring:message
										code="instrutor.bairro.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="255"
									path="bairro"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="30" /> <form:errors path="bairro" cssClass="error" /></td>
						</tr>
						<tr class="prop" id="cep">
							<td valign="top" class="name"><label for="cep"><spring:message
										code="instrutor.cep.label" />:</label></td>
							<td><form:input cssStyle="width:80px" maxlength="255"
									path="cep" size="30" alt="cep" /> <form:errors path="cep"
									cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="email"><spring:message
										code="instrutor.email.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="255"
									path="email" size="30" />* <form:errors path="email"
									cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="telefone"><spring:message
										code="instrutor.telefone.label" />:</label></td>
							<td><form:input cssStyle="width:110px" maxlength="255"
									path="telefone" size="30" alt="telefone" />* <form:errors
									path="telefone" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="celular"><spring:message
										code="instrutor.celular.label" />:</label></td>
							<td><form:input cssStyle="width:110px" maxlength="255"
									path="celular" size="30" alt="celular" /> <form:errors
									path="celular" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="sexo"><spring:message
										code="instrutor.sexo.label" />:</label></td>

							<td><form:select path="sexo" items="${sexoList}" />*</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="dataNascimento"><spring:message
										code="instrutor.dataNascimento.label" />:</label></td>
							<td><form:input cssStyle="width:90px" maxlength="255"
									path="dataNascimento" size="30" alt="date" /> <form:errors
									path="dataNascimento" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="nivelEscolaridade"><spring:message
										code="instrutor.nivelEscolaridade.label" />:</label></td>

							<td><form:select path="nivelEscolaridadeId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${nivelEscolaridadeList}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="formacaoAcademica"><spring:message
										code="instrutor.formacaoAcademica.label" />:</label></td>

							<td><form:select path="formacaoAcademicaId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${formacaoAcademicaList}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="setor"><spring:message
										code="instrutor.setor.label" />:</label></td>

							<td><form:select path="setorId">
									<form:option value="9999">Selecione...</form:option>
									<form:option value="9999">No se Aplica</form:option>
									<form:options items="${setorList}" itemLabel="descricao"
										itemValue="id" />
								</form:select></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label
								for="instituicaoVinculo"><spring:message
										code="instrutor.instituicaoVinculo.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="255"
									path="instituicao"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="30" /> <form:errors path="instituicao" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="tipoInstrutor"><spring:message
										code="instrutor.tipoInstrutor.label" />:</label></td>

							<td><form:select path="tipoId" items="${tipoInstrutorList}"
									itemLabel="descricao" itemValue="id" />*</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="perfil"><spring:message
										code="instrutor.perfil.label" />:</label></td>

							<td><form:textarea path="perfil" cols="70" rows="5"
									maxlength="2000"
									onkeyup="javascript:this.value=this.value.toUpperCase();" /> <form:errors
									path="perfil" cssClass="error" /></td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="observacao"><spring:message
										code="instrutor.observacao.label" />:</label></td>

							<td><form:textarea path="observacao" cols="70" rows="5"
									maxlength="2000"
									onkeyup="javascript:this.value=this.value.toUpperCase();" /> <form:errors
									path="observacao" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="curriculo">

									<spring:message code="instrutor.curriculo.label" />:
							</label></td>
							<td><form:input id="curriculo" cssStyle="display:none"
									maxlength="255" path="curriculo" size="30" type="file" />
								<div class="row g-3">
									<div class="col">
										<c:if test="${instrutor.curriculo != null}">
											<a id="curriculoDownload"
												href="<c:url  value="/instrutor/curriculo/${instrutor.id}"/>"
												target="_blank">${instrutor.curriculoNome}</a>

										</c:if>
										<c:if test="${instrutor.curriculo == null}">
											<input id="curriculo-fileName" class="form-control w-30"
												type="text" readonly value=${ instrutor.curriculoNome } />

										</c:if>
									</div>
									<div class="col">
										<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
											<span class="menuButton"> <c:if
													test="${instrutor.curriculo != null}">
													<a id="deleteCurriculo" href="" onclick="return false;"
														class="delete"><spring:message
															code="default.button.delete_file.label" /></a>
												</c:if> <c:if test="${instrutor.curriculo == null}">
													<a id="insert-curriculo" href="" onclick="return false;"
														class=insert><spring:message
															code="default.button.insert_file.label" /></a>
												</c:if>

											</span>
										</sec:authorize>
									</div>
								</div>
								<p>
									<form:errors path="curriculo" cssClass="error" />
								</p></td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="assinatura"><spring:message
										code="instrutor.assinatura.label" />:</label></td>
							<td><form:input cssStyle="display:none;" id="assinatura"
									maxlength="255" path="assinatura" var="assinatura" size="30"
									type="file" />

								<div class="row g-3">
									<div class="col">

										<c:if test="${instrutor.assinatura != null}">
											<a id="assinaturaDownload"
												href="<c:url value="/instrutor/assinatura/${instrutor.id}"/>"
												target="_blank">${instrutor.assinaturaNome}</a>

										</c:if>
										<c:if test="${instrutor.assinatura == null}">
											<input id="assinatura-fileName" class="form-control w-30"
												type="text" readonly value=${ instrutor.assinaturaNome } />

										</c:if>

									</div>
									<div class="col">
										<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
											<span class="menuButton"> <c:if
													test="${instrutor.assinatura != null}">
													<a id="deleteAssinatura" href="" onclick="return false;"
														class="delete"><spring:message
															code="default.button.delete_file.label" /></a>
												</c:if> <c:if test="${instrutor.assinatura == null}">
													<a id="insert-assinatura" href="" onclick="return false;"
														class=insert><spring:message
															code="default.button.insert_file.label" /></a>
												</c:if>

											</span>
										</sec:authorize>
									</div>
								</div>



								<p>
									<form:errors path="assinatura" cssClass="error" />
								</p></td>
						</tr>
						<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">

							<tr class="prop">
								<td valign="top" class="name"><label
									for="situacaoInstrutor"><spring:message
											code="instrutor.situacaoInstrutor.label" />:</label></td>

								<td><form:select path="situacaoId"
										items="${situacaoInstrutorList}" itemLabel="descricao"
										itemValue="id" /></td>


								<c:if test="${not cadastrado}">

								</c:if>
							</tr>

							<tr class="prop" id="linhaJustificativa">
								<td valign="top" class="name"><label for="justificativa">
										<spring:message code="eventoExtra.justificativa.label" />:
								</label></td>
								<td><form:textarea rows="5" cols="70" name="justificativa"
										path="justificativa"></form:textarea>* <form:errors
										path="justificativa" cssClass="error" /></td>
							</tr>

						</sec:authorize>

					</tbody>
				</table>
			</div>


			<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">

				<div class="mx-auto" style="width: 400px;">
					<div class="d-flex justify-content-around p-2 ">

						<a href="<c:url value="/instrutor/"/>"><button type="button"
								class="btn btn-outline-secondary">
								<spring:message code="default.button.list.label" />
							</button></a>
						<button id="atualizar" type="submit" class="btn btn-primary">
							<spring:message code="default.button.save.label" />
						</button>

					</div>
				</div>
			</sec:authorize>


			<form:hidden path="id" />
			<form:hidden path="dataCadastro" />
		</form:form>

		<form:form id="formDeleteAssinatura"
			action="/instrutor/${instrutor.id}/assinatura" method="DELETE">
		</form:form>

		<form:form id="formDeleteCurriculo"
			action="/instrutor/${instrutor.id}/curriculo" method="DELETE">
		</form:form>
	</div>

</body>


</html>