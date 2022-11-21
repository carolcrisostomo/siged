<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="instrutor.label" /></title>

<spring:url value="/ajax/procuraMunicipio" var="procuraMunicipioUrl" />
<spring:url value="/instrutor/procuraInstrutor" var="procuraInstrutorUrl" />

</head>
<body>

	<sec:authorize ifAnyGranted="ROLE_ALUNO,ROLE_SERVIDOR">
		<script type="text/javascript">
			jQuery(document).ready(
				function($) {
					bloqueiaCamposInstrutor(document.getElementById('cpf').value);
				}
			);
		</script>
	</sec:authorize>
	
		<script type="text/javascript">
	jQuery(document).ready(function($){		
		
		
		   $('#curriculo').change(function(event){
		    	var filePath = event.target.value
			    var fileName = filePath.substr(filePath.lastIndexOf('\\') + 1).split('.')[0]
		    	
		    	var curriculoFileName = document.getElementById('curriculo-fileName')
		    	
		    	curriculoFileName.value = fileName
		    	
			});

		    $('#assinatura').change(function(event){
		    	var filePath = event.target.value
			    var fileName = filePath.substr(filePath.lastIndexOf('\\') + 1).split('.')[0]
		    	
		    	var assinaturaFileName = document.getElementById('assinatura-fileName')
		    	
		    	assinaturaFileName.value = fileName
		    	
			});

		    
		    $('#assinatura-fileName').click(function(e){
		    	var assinaturaValue = document.getElementById('assinatura-fileName')
		    	if(assinaturaValue.value == '/'){
				    $('#assinatura').click();
		    	}
		    	else{
		    	document.getElementById('assinaturaDownload').click();
		    	}

		    }
				)
	 $('#curriculo-fileName').click(function(){

	 	var curriculoValue = document.getElementById('curriculo-fileName')
		if(curriculoValue.value == '/'){
		    $('#curriculo').click();
		}
		else{
		document.getElementById('curriculoDownload').click();
		}

		    }
				)

		
		
		if ($("#paisId").val() == 1) {
			$("#ufLinha").show();    			
			$("#cidadeLinha").show();
			$("#bairro").show();
			$("#cep").show();
		} else {			
			$("#ufId").val(0);
			$("#cidadeId").html('<option value="0">Selecione...</option>').trigger('chosen:updated');							
			$("#ufLinha").hide();
			$("#cidadeLinha").hide();
			$("#bairro").hide();
			$("#cep").hide();
		}
		
  		$("#paisId").change(function(){
  			if ($("#paisId").val() == 1) {
  				$("#ufLinha").show();    			
  				$("#cidadeLinha").show();	
  				$("#bairro").show();
  				$("#cep").show();

  			} else {			
  				$("#ufId").val(0);
  				$("#cidadeId").html('<option value="0">Selecione...</option>').trigger('chosen:updated');							
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
		jQuery(document).ready(
			function($) {
			
				$('#insert-assinatura').click(function(){
				    $('#assinatura').click();
				});
				
				$('#insert-curriculo').click(function(){
				    $('#curriculo').click();
				});

				
				$("#cpf").focusout(	function() {
						
					var url = "${procuraInstrutorUrl}";
	
					if ($("#cpf").val() != "") {

						if (!valida_cpf($("#cpf").val())) {
							alert("CPF invlido");
							$("#cpf").val("");
							$("#cpf").focus();
						} else {
							$.getJSON(url, {
								cpf : $("#cpf").val()
							},function(data) {
								if (data != null) {
									if (data.situacao == 2) {
										alert("Seu cadastramento est em Anlise. Favor aguardar confirmao. Para mais informaes, favor entrar em contato com o IPC.");
									} else if (data.situacao == 1) {
										alert("Instrutor j cadastrado!");
									}
									$("#cpf").val("");
									history.go(-1);
								}
							});
						}

					}
				});				
				
				$(document).ajaxStop($.unblockUI);
			});
	</script>
	
	<div class="body">
		<%
			if (request.getParameter("mensagem")!=null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>
		<h1>
			<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
				<spring:message code="default.add.label" />
			</sec:authorize>
			<sec:authorize ifAnyGranted="ROLE_ANONYMOUS,ROLE_SERVIDOR,ROLE_ALUNO">
				<spring:message code="default.precadastrado.label" />
			</sec:authorize>
			-
			<spring:message code="instrutor.label" />
		</h1>

		<c:url var="url" value="/instrutor" />
		<form:form action="${url}" method="POST" modelAttribute="instrutor"
			enctype="multipart/form-data">

			<div class="dialog">
				<table>
					<tbody>
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">(*)
								Campos Obrigatrios</td>
						</tr>
						<!-- CPF -->
						<tr class="prop">
							<td valign="top" class="name"><label for="cpf"><spring:message code="instrutor.cpf.label" />:</label></td>
							<td><form:input cssStyle="width:110px" maxlength="255" path="cpf" size="30" alt="cpf" /><sec:authorize ifNotGranted="ROLE_ADMINISTRADOR">*</sec:authorize>
								<form:errors path="cpf" cssClass="error" /></td>
						</tr>

						<!-- NOME -->
						<tr class="prop">
							<td valign="top" class="name"><label for="nome"><spring:message	code="instrutor.nome.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="255"
									id="name"
									path="nome"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="30"  />* <form:errors path="nome"
									cssClass="error" /></td>
						</tr>

						<!-- LOGRADOURO -->
						<tr class="prop">
							<td valign="top" class="name"><label for="logradouro"><spring:message code="instrutor.logradouro.label" />:</label></td>
							<td><form:input cssStyle="width:400px" maxlength="255"
									path="logradouro"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="30" /> <form:errors path="logradouro" cssClass="error" />
							</td>
						</tr>

						<!-- NMERO -->
						<tr class="prop">
							<td valign="top" class="name"><label for="numero"><spring:message code="instrutor.numero.label" />:</label></td>
							<td><form:input cssStyle="width:60px" maxlength="255"
									path="numero" size="30" /> <form:errors path="numero"
									cssClass="error" /></td>
						</tr>

						<!-- COMPLEMENTO -->
						<tr class="prop">
							<td valign="top" class="name"><label for="complemento"><spring:message code="instrutor.complemento.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="255"
									path="complemento"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="30" /> <form:errors path="complemento" cssClass="error" />
							</td>
						</tr>
						
						<!-- CARREGA PAS -->
						<tr class="prop">
							<td valign="top" class="name">
								<label for="paisId"><spring:message	code="provedorEvento.pais.label" />:</label>
							</td>
							<td>
								<form:select path="paisId" id="paisId">
			 					    <form:options items="${listaPais}" itemLabel="descricao" itemValue="id" />
								</form:select>
					 		</td>
						</tr>					

						<!-- CARREGA ESTADOS
						<tr class="prop">
							<td valign="top" class="name"><label for="ufMunicipio"><spring:message code="participante.uf.label" />:</label></td>
							<td><form:select path="ufMunicipio"	onchange="updateMunicipio('${procuraMunicipioUrl}', 'ufMunicipio', 'municipio')">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${ufMunicipioList}" itemLabel="nome" itemValue="uf" />
								</form:select>* <form:errors path="ufMunicipio" cssClass="error" /></td>
						</tr> -->
						<tr class="prop" id="ufLinha">
							<td valign="top" class="name">
								<label for="uf"><spring:message	code="provedorEvento.uf.label" />:</label>
							</td>
							<td>
								<form:select path="ufMunicipio" id="ufId" onchange="updateMunicipio('${procuraMunicipioUrl}', 'ufId', 'cidadeId')" >
									<option value="0">Selecione...</option>
									<form:options items="${listaUf}" itemLabel="nome" itemValue="uf" />
								</form:select>*	<form:errors path="ufMunicipio" cssClass="error" />
							</td>
						</tr>						

						<!-- CARREGA CIDADES 
						<tr class="prop">
							<td valign="top" class="name"><label for="municipio"><spring:message code="participante.municipio.label" />:</label></td>
							<td><form:select path="municipio">
									<c:choose>
										<c:when test="${instrutor.municipio.id != null}">
											<form:option value="${instrutor.municipio.id}">${instrutor.municipio.nome}</form:option>
										</c:when>
										<c:otherwise>
											<form:option value="0">Selecione...</form:option>
										</c:otherwise>
									</c:choose>
								</form:select>* <form:errors path="municipio" cssClass="error" />
							</td>
						</tr>-->
					<tr class="prop" id="cidadeLinha">
						<td valign="top" class="name">
							<label for="cidadeId"><spring:message code="provedorEvento.cidade.label" />:</label>
						</td>
						<td>
							<form:select path="municipio" id="cidadeId" >
								<option value="0">Selecione...</option>
							</form:select>* <form:errors path="municipio" cssClass="error" />
						</td>
					</tr>		

						<!-- BAIRRO -->
						<tr class="prop" id="bairro">
							<td valign="top" class="name"><label for="bairro"><spring:message code="instrutor.bairro.label" />:</label></td>
							<td>
								<form:input cssStyle="width:250px" maxlength="255" path="bairro" onkeyup="javascript:this.value=this.value.toUpperCase();" size="30" /> <form:errors path="bairro" cssClass="error" />
							</td>
						</tr>

						<!-- CEP -->
						<tr class="prop" id="cep">
							<td valign="top" class="name"><label for="cep"> <spring:message code="instrutor.cep.label" />:</label></td>
							<td><form:input cssStyle="width:80px" maxlength="255"
									path="cep" size="30" alt="cep" /> <form:errors path="cep"
									cssClass="error" /></td>
						</tr>

						<!-- EMAIL -->
						<tr class="prop">
							<td valign="top" class="name"><label for="email"><spring:message code="instrutor.email.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="255" path="email" size="30" />* <form:errors path="email" cssClass="error" /></td>
						</tr>

						<!-- TELEFONE_1 -->
						<tr class="prop">
							<td valign="top" class="name"><label for="telefone"><spring:message	code="instrutor.telefone.label" />:</label></td>
							<td><form:input cssStyle="width:110px" maxlength="255"
									path="telefone" size="30" alt="telefone" />* <form:errors
									path="telefone" cssClass="error" /></td>
						</tr>

						<!-- TELEFONE_2 -->
						<tr class="prop">
							<td valign="top" class="name"><label for="celular"><spring:message code="instrutor.celular.label" />:</label></td>
							<td><form:input cssStyle="width:110px" maxlength="255"
									path="celular" size="30" alt="celular" /> <form:errors
									path="celular" cssClass="error" /></td>
						</tr>

						<!-- SEXO -->
						<tr class="prop">
							<td valign="top" class="name"><label for="sexo"> <spring:message code="instrutor.sexo.label" />:
							</label></td>
							<!-- SEXO -->
							<td><form:select path="sexo">
									<form:option value="">Selecione...</form:option>
									<form:options items="${sexoList}" />
								</form:select>* <form:errors path="sexo" cssClass="error" /></td>
						</tr>

						<!-- DATA NASCIMENTO -->
						<tr class="prop">
							<td valign="top" class="name"><label for="dataNascimento"><spring:message code="instrutor.dataNascimento.label" />:</label></td>
							<td><form:input cssStyle="width:90px" maxlength="255"
									path="dataNascimento" size="30" alt="date" /> <form:errors
									path="dataNascimento" cssClass="error" /></td>
						</tr>

						<!-- NIVEL DE ESCOLARIDADE -->
						<tr class="prop">
							<td valign="top" class="name"><label for="nivelEscolaridade"><spring:message code="instrutor.nivelEscolaridade.label" />:</label></td>
							<td><form:select path="nivelEscolaridadeId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${nivelEscolaridadeList}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>

						<!-- FORMAO ACADEMICA -->
						<tr class="prop">
							<td valign="top" class="name"><label for="formacaoAcademica"><spring:message code="instrutor.formacaoAcademica.label" />:</label></td>
							<td><form:select path="formacaoAcademicaId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${formacaoAcademicaList}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>

						<!-- SETOR -->
						<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
							<tr class="prop">
								<td valign="top" class="name"><label for="setor"><spring:message code="instrutor.setor.label" />:</label></td>
								<td><form:select path="setorId">
										<form:option value="9999">Selecione...</form:option>
										<form:option value="9999">No se Aplica</form:option>
										<form:options items="${setorList}" itemLabel="descricao"
											itemValue="id" />
									</form:select></td>
							</tr>
						</sec:authorize>

						<sec:authorize ifAnyGranted="ROLE_SERVIDOR">
							<tr class="prop">
								<td valign="top" class="name"><label for="setor"> <spring:message code="instrutor.setor.label" />:
								</label></td>
								<td><form:select path="setorId">
										<form:option value="0">Selecione...</form:option>
										<form:option value="9999">No se Aplica</form:option>
										<form:options items="${setorList}" itemLabel="descricao"
											itemValue="id" />
									</form:select></td>
							</tr>
						</sec:authorize>

						<!-- INSTITUIO VINCULO -->
						<tr class="prop">
							<td valign="top" class="name"><label
								for="instituicaoVinculo"><spring:message code="instrutor.instituicaoVinculo.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="255"
									path="instituicao"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="30" /> <form:errors path="instituicao" cssClass="error" />
							</td>
						</tr>

						<!-- TIPO -->
						<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
							<tr class="prop">
								<td valign="top" class="name"><label for="tipoInstrutor"><spring:message code="instrutor.tipoInstrutor.label" />:</label></td>
								<td><form:select path="tipoId">
										<form:option value="0">Selecione...</form:option>
										<form:options items="${tipoInstrutorList}" itemLabel="descricao" itemValue="id" />
									</form:select>* <form:errors path="tipoId" cssClass="error" /></td>
							</tr>
						</sec:authorize>


						<!-- PERFIL -->
						<tr class="prop">
							<td valign="top" class="name"><label for="perfil"> <spring:message code="instrutor.perfil.label" />: </label></td>
							<td><form:textarea path="perfil" cols="70" rows="5"	maxlength="2000" /> <form:errors path="perfil" cssClass="error" /></td>
						</tr>

						<!-- OBSERVAO -->
						<tr class="prop">
							<td valign="top" class="name"><label for="observacao"><spring:message code="instrutor.observacao.label" />:</label></td>
							<td><form:textarea path="observacao" cols="70" rows="5" maxlength="2000" /> <form:errors path="observacao" cssClass="error" /></td>
						</tr>

						<!-- CURRICULO -->
						<tr class="prop">
							<td valign="top" class="name"><label for="curriculo"><spring:message code="instrutor.curriculo.label" />:</label></td>
							<td>

							<p class="formatosArquivo">Formatos aceitos: .PDF</p>
							<div class="row g-3">
								
								<div class="col">
													<input  id="curriculo-fileName" class="form-control w-30" type="text" readonly value=${ instrutor.curriculoNome } />
								</div>
								  <div class="col">
								  								<span class="menuButton">
								  
									<a id="insert-curriculo" class="insert"
				href="" onclick="return false;" ><spring:message
						code="default.button.insert_file.label" /></a>
						</span>
								</div>
								</div>
							<form:input id="curriculo" title=" " cssStyle="display:none;" maxlength="255" path="curriculo" size="30" type="file" />  
							
							<p><form:errors path="curriculo" cssClass="error" /></p></td>
						</tr>

						<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
							<!-- ASSINATURA -->
							<tr class="prop">
								<td valign="top" class="name"><label for="assinatura"><spring:message code="instrutor.assinatura.label" />:</label></td>
								<td><p class="formatosArquivo">Formatos aceitos: .JPG</p>
													<div class="row g-3">
								
								<div class="col">
													<input  id="assinatura-fileName" class="form-control w-30" type="text" readonly value=${ instrutor.assinaturaNome } />
								</div>
								  <div class="col">
								  								<span class="menuButton">
								  
									<a id="insert-assinatura" class="insert"
				href="" onclick="return false;" ><spring:message
						code="default.button.insert_file.label" /></a>
						</span>
								</div>
								</div>
								
								<form:input id="assinatura"  cssStyle="display:none;" maxlength="255" path="assinatura" size="30" type="file" /> 			
								<span class="menuButton">
							</span> <p> <form:errors path="assinatura" cssClass="error" /></p></td>
							</tr>

							<tr class="prop">
								<td valign="top" class="name"><label for="situacaoInstrutor"><spring:message code="instrutor.situacaoInstrutor.label" />:</label></td>
								<form:hidden path="situacaoId" value="1" />
								<td>Cadastrado</td>
							</tr>
						</sec:authorize>

						<sec:authorize ifNotGranted="ROLE_ADMINISTRADOR">
							<form:hidden path="situacaoId" value="2" />
						</sec:authorize>

					</tbody>
				</table>
			</div>

			
			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/instrutor/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="criar" type="submit" class="btn btn-primary">
 						<spring:message code="default.add.label" />
					</button>

				</div>
			</div>
		</form:form>
	</div>	

</body>
</html>
