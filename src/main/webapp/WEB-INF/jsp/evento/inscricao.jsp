<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="participante.label" /></title>
</head>
<body>
	<c:url var="url" value="/evento/inscricao/${eventoId}" />
	<c:set var="eServidor" value="0" />	
	<spring:url value="/ajax/procuraMunicipio" var="procuraMunicipioUrl" />
	<spring:url value="/ajax/procuraOrgao" var="procuraOrgaoUrl" />
	<spring:url value="/usuario/procuraUsuario" var="procuraUsuarioUrl" />	
	
	<script type="text/javascript">
		jQuery(document).ready(function($){

			updateMunicipioSelected('${procuraMunicipioUrl}', 'ufMunicipio', 'municipio', '${participante.municipio.id}');
			
						
			//Inicia tela com alguns campos ocultos
			$(".jur").hide();
			$(".soc").hide();
			
			if($("#tipoId option:selected").val() == 2) {
				$(".jur").show();
			} else {
				if ($("#tipoId option:selected").val() == 3) {					
					$(".soc").show();
				}				
			}			
						
			//Ao alterar o tipo de participante, exibe campos inicialmente ocultos
			$("#tipoId").change(function() {
				
				$(".jur").hide();
				$(".soc").hide();
				
				if("${publicoAlvo}".indexOf($("#tipoId option:selected").html()) == -1 && $("#tipoId option:selected").val() != 0){
					alert("Não é possível realizar a sua pré-inscrição! Tipo do participante não compatível com o público-alvo do evento.");
					$("#tipoId").val(0).focus();					
				}else{
					if($("#tipoId option:selected").val() == 2) {
						$(".jur").show();				
					} else {
						if ($("#tipoId option:selected").val() == 3) {
							$(".soc").show();
						}				
					}
				}
			});
				
			
			atualizaOrgao();			
			$('input[name=administracaoPublica]' ).change(function() {
				atualizaOrgao(true);
		    });
			
			function atualizaOrgao(limpaSelecao) {
				
				var radioMarcado = $('input[name=administracaoPublica]:checked' ).val(); 
				
				if (radioMarcado == 'estadual') {
		        	$('.localidadeTr').hide();
		        	updateOrgao('${procuraOrgaoUrl}', 'orgaoId', 1, '${participante.orgaoId.identidade}');
		        }
		        else if (radioMarcado == 'municipal') {
		        	$('.localidadeTr').show();
		        	
		        	if(limpaSelecao)
		        		$('#localidade').val(0);
		        	
		        	updateOrgao('${procuraOrgaoUrl}', 'orgaoId', $('#localidade').val(), '${participante.orgaoId.identidade}');		        	
		        } else {
		        	$('.localidadeTr').hide();
		        }
			}
			
			
			$('#localidade' ).change(function() {
		        updateOrgao('${procuraOrgaoUrl}', 'orgaoId', $('#localidade').val());
		    });
			
			
			
			$("#cpf").focusout(function(){
				var url = "${procuraUsuarioUrl}";
		
				if ($("#cpf").val() != "") {
					if(!valida_cpf($("#cpf").val())) {						
						alert('CPF Inválido');
						$("#cpf").val("").focus();
					} else {
						$.getJSON(url, {cpf: $("#cpf").val()}, function(usuario) {
							
							if (usuario != null) {
								
								if(usuario.servidor == 1 && usuario.ativo == 1){
									alert("Efetue login no sistema com o usuário e senha de rede do TCE-CE para fazer sua pré-inscrição.");
								}else{
									
									if(usuario.servidor == 0 && usuario.ativo == 0){
										alert("Seu cadastramento está em análise. Favor aguardar confirmação do seu login para poder fazer sua pré-inscrição. Para mais informações, favor entrar em contato com o IPC.");
									}else{
										
										if(usuario.servidor == 0 && usuario.ativo == 1){
						            		alert("Efetue login no sistema com seu CPF e senha para fazer sua pré-inscrição.");									
										}	
									}																	
								}								
								
								$("#cpf").val("").focus();
				            }
						});
					}
				}		
			});
			
			//
			if ($("#escolaridadeId").val() == 0 || $("#escolaridadeId").val() == 1 || $("#escolaridadeId").val() == 2 ) {
			$("#formacaoAcademicaLinha").hide();
   					
	  		} else {
	   			//$("#formacaoAcademicaLinha").val(0);
	   			//$("#cidadeId").html('<option value="0">Selecione...</option>').trigger('chosen:updated');
	   			$("#formacaoAcademicaLinha").show();
	  		}
			
	  		$("#escolaridadeId").change(function(){
	   			if ($("#escolaridadeId").val() == 0 || $("#escolaridadeId").val() == 1 || $("#escolaridadeId").val() == 2 ) {
	    			$("#formacaoAcademicaLinha").hide();    			
	    			
	   			} else {
	    			//$("#formacaoAcademicaLinha").val(0);
	    			//$("#cidadeId").html('0').trigger('chosen:updated');
	    			$("#formacaoAcademicaLinha").show();
	   			}
	  		});
			
			$(document).ajaxStop($.unblockUI);			
		});	
	
	</script>

	<div class="body">
		<c:if test="${param.mensagem != null}">
			<div class="message">${param.mensagem}</div>
		</c:if>
		<c:if test="${param.mensagemErro != null}">
			<div class="messageErro">${param.mensagemErro}</div>
		</c:if>
		<h1>Fazer Pré-inscrição</h1>		
		
		<div class="message">Se você já é cadastrado ou é servidor do
			TCE-CE, efetue login no sistema para fazer sua pré-inscrição.</div>		

		<form:form action="${url}" method="POST" modelAttribute="participante">
			<div class="dialog">
				<table>
					<tbody>
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">(*) Campos Obrigatórios</td>
						</tr>
						<tr>
							<td valign="top" class="name"><label for="evento"><spring:message
											code="evento.evento.label" />: </label></td>
							<td class="name" style="padding-bottom: 10px;"><strong>${nomeEvento}</strong></td>							
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="cpf"><spring:message
											code="participante.cpf.label" />:</label></td>
							<td><form:input cssStyle="width:110px" maxlength="50"
									path="cpf" size="30" alt="cpf" />* <form:errors path="cpf"
									cssClass="error" /></td>							
						</tr>
						<tr class="prop" id="selectTipoId">
							<td valign="top" class="name"><label for="tipoPublicoAlvo"><spring:message
										code="participante.tipoPublicoAlvo.label" />:</label></td>
							<td><form:select path="tipoId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${tipoPublicoAlvoList}"
										itemLabel="descricao" itemValue="id" />
								</form:select>* <form:errors path="tipoId" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="nome"><spring:message
										code="participante.nome.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="100"
									path="nome"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="100" />* <form:errors path="nome" cssClass="error" /></td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label for="nivelEscolaridade"><spring:message
										code="participante.escolaridade.label" />:</label></td>
							<td><form:select path="escolaridadeId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${nivelEscolaridadeList}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>					
						<tr class="prop" id="formacaoAcademicaLinha">
							<td valign="top" class="name"><label for="formacaoAcademica"><spring:message
										code="participante.formacao.label" />:</label></td>
							<td><form:select path="formacaoId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${formacaoAcademicaList}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="email"><spring:message
										code="participante.email.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="50"
									path="email" size="30" />* <form:errors path="email"
									cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="telefone"><spring:message
										code="participante.telefone.label" />:</label></td>
							<td><form:input cssStyle="width:110px" maxlength="50"
									path="telefone" size="30" alt="telefone" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="celular"><spring:message
										code="participante.celular.label" />:</label></td>
							<td><form:input cssStyle="width:110px" maxlength="50"
									path="celular" size="30" alt="celular" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="ufMunicipio"><spring:message
										code="participante.uf.label" />:</label></td>
							<td><form:select path="ufMunicipio"	onchange="updateMunicipio('${procuraMunicipioUrl}', 'ufMunicipio', 'municipio')">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${ufMunicipioList}" itemLabel="nome" itemValue="uf" />
								</form:select>* <form:errors path="ufMunicipio" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="municipio"><spring:message
										code="participante.municipio.label" />:</label></td>
							<td><form:select path="municipio">
									<form:option value="0">Selecione...</form:option>
								</form:select>* <form:errors path="municipio" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="observacao"><spring:message
										code="participante.justificativa.label" />:</label></td>

							<td><form:textarea path="observacao" cols="70" rows="5" /></td>
						</tr>
						<tr class="prop, jur">
							<td valign="top" class="name"><label for="matricula"><spring:message
										code="participante.matricula.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="30"
									path="matricula"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="100" /><form:errors path="matricula" cssClass="error" /></td>
						</tr>
						<tr class="prop, jur">
							<td valign="top" class="name"><label for="cargo"><spring:message
										code="participante.cargo.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="70"
									path="cargo"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="70" /></td>
						</tr>						
						
						<tr class="prop, jur">
							<td valign="top" class="name"><label for=""><spring:message	code="administracaoPublica" />:</label></td>
							<td>
								<label>
									<form:radiobutton path="administracaoPublica" value="estadual"/> 
									Estadual
								</label>
								<label>
									<form:radiobutton path="administracaoPublica"  value="municipal"/> 
									Municipal
								</label>
							</td>
						</tr>
						
						<tr class="prop, jur, localidadeTr">
							<td valign="top" class="name">
								<label for="localidadeId">Município:</label>
							</td>
							<td><form:select path="localidade">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${localidadeList}" itemLabel="dsLocalidade" itemValue="idLocalidade" />
								</form:select>
							</td>
						</tr>												
						
						<tr class="prop, jur">
							<td valign="top" class="name"><label for="orgao">
								<spring:message	code="participante.orgao.label" />:</label>
							</td>
							<td><form:select path="orgaoId">
									<form:option value="0">Selecione...</form:option>									
								</form:select>*
								<br /><form:errors path="orgaoId" cssClass="error" />
							</td>
						</tr>
						
						<tr class="prop, jur">
							<td valign="top" class="name">
								<label for="lotacao"><spring:message code="participante.lotacao.label" />:</label>
							</td>
							<td>
								<form:input cssStyle="width:250px" maxlength="100"
									path="lotacao" size="100"
									onkeyup="javascript:this.value=this.value.toUpperCase();" />*
									<form:errors path="lotacao" cssClass="error" />
							</td>
						</tr>												
						
						<tr class="prop, soc">
							<td valign="top" class="name"><label for="entidade"><spring:message
										code="participante.entidade.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="100"
									path="entidade" size="100"
									onkeyup="javascript:this.value=this.value.toUpperCase();" /></td>
						</tr>
						<tr class="prop, soc">
							<td valign="top" class="name"><label for="profissao"><spring:message
										code="participante.profissao.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="70"
									path="profissao" size="70"
									onkeyup="javascript:this.value=this.value.toUpperCase();" />
									<form:errors path="profissao" cssClass="error"/>
							</td>
						</tr>
				</table>
			</div>
			<div class="buttons">
				<input id="criar" type="submit" class="save"
					value="<spring:message code="default.add.label" />" />
			</div>
		</form:form>
	</div>
</body>
</html>
