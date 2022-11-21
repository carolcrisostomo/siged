<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="default.button.edit.label" /> - <spring:message code="participante.label" /></title>

	<spring:url value="/ajax/procuraMunicipio" var="procuraMunicipioUrl" />
	<spring:url value="/ajax/procuraOrgao" var="procuraOrgaoUrl" />
</head>

<body>
<script type="text/javascript">
jQuery(document).ready(function($) {
	
	updateMunicipioSelected('${procuraMunicipioUrl}', 'ufMunicipio', 'municipio', '${participante.municipio.id}');
	
	if ($("#municipio").val() != 0 && $("#municipio").val() != null) {
		$("#paisId").val(1);
	}
	
	if ($("#paisId").val() == 1) {
		$("#ufLinha").show();    			
		$("#cidadeLinha").show();						
	} else {			
		$("#ufMunicipio").val(0);
		$("#municipio").html('<option value="0">Selecione...</option>').trigger('chosen:updated');							
		$("#ufLinha").hide();
		$("#cidadeLinha").hide();									
	} 
	
		 $("#paisId").change(function(){		
			if ($("#paisId").val() == 1) {
				$("#ufLinha").show();    			
				$("#cidadeLinha").show();						
			} else {
				$("#ufMunicipio").val(0);
				$("#municipio").html('<option value="0">Selecione...</option>').trigger('chosen:updated');							
				$("#ufLinha").hide();
				$("#cidadeLinha").hide();									
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
	<script type="text/javascript">
	
	
	updateMunicipioSelected('${procuraMunicipioUrl}', 'ufMunicipio', 'municipio', '${participante.municipio.id}');
			//Inicia tela com alguns campos ocultos
			$(".jur").hide();
			$(".soc").hide();
			if ($("#tipoId option:selected").val() == 2) {
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
				if ($("#tipoId option:selected").val() == 2) {
					$(".jur").show();
				} else {
					if ($("#tipoId option:selected").val() == 3) {
						$(".soc").show();
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
			<spring:message code="participante.label" />
		</h1>
		<c:url var="url" value="/participante/${participante.id}" />
		<form:form action="${url}" method="PUT" modelAttribute="participante">
			<div class="dialog">
				<table>
					<tbody>
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">(*) Campos Obrigat�rios</td>
						</tr>
						<tr class="prop">
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
							<td valign="top" class="name"><label for="cpf"><spring:message
										code="participante.cpf.label" />:</label></td>
							<td>${participante.cpfFormatado} <form:hidden path="cpf" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="nivelEscolaridade"><spring:message
										code="participante.escolaridade.label" />:</label></td>
							<td><form:select path="escolaridadeId"
									items="${nivelEscolaridadeList}" itemLabel="descricao"
									itemValue="id" /></td>
						</tr>
						<tr class="prop" id="formacaoAcademicaLinha">
							<td valign="top" class="name"><label for="formacaoAcademica"><spring:message
										code="participante.formacao.label" />:</label></td>
							<td><form:select path="formacaoId"
									items="${formacaoAcademicaList}" itemLabel="descricao"
									itemValue="id" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="email"><spring:message
										code="participante.email.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="50"
									path="email" size="30" />* <form:errors path="email" cssClass="error" /></td>
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
						<td valign="top" class="name">
							<label for="paisId"><spring:message	code="provedorEvento.pais.label" />:</label>
						</td>
						<td>
							<form:select path="paisId" id="paisId">
		 						<form:option value="0">Selecione...</form:option>
		 					    <form:options items="${listaPais}" itemLabel="descricao" itemValue="id" />
							</form:select>
				 		</td>
						</tr>
						
						<tr class="prop"  id="ufLinha">
							<td valign="top" class="name"><label for="ufMunicipio"><spring:message
										code="participante.uf.label" />:</label></td>
							<td><form:select path="ufMunicipio" id="ufMunicipio"
									onchange="updateMunicipio('${procuraMunicipioUrl}', 'ufMunicipio', 'municipio')">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${ufMunicipioList}" itemLabel="nome"
										itemValue="uf" />
								</form:select>* <form:errors path="ufMunicipio" cssClass="error" /></td>
						</tr> 
						<!--  
						<tr class="prop" id="ufLinha">
						<td valign="top" class="name">
							<label for="uf"><spring:message	code="provedorEvento.uf.label" />:</label>
						</td>
						<td>
							<form:select path="ufMunicipio" id="ufMunicipio" onchange="updateMunicipio('${procuraMunicipioUrl}', 'ufMunicipio', 'municipio')" >
								<option value="0">Selecione...</option>
								<form:options items="${listaUf}" itemLabel="nome" itemValue="uf" />
							</form:select>*	<form:errors path="ufMunicipio" cssClass="error" />
						</td>
					</tr>-->
						<tr class="prop" id="cidadeLinha">
							<td valign="top" class="name"><label for="municipio"><spring:message
										code="participante.municipio.label" />:</label></td>
							<td><form:select path="municipio" id="municipio">
									<form:option value="0">Selecione...</form:option>
								</form:select>* <form:errors path="municipio" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="observacao"><spring:message
										code="participante.observacao.label" />:</label></td>
							<td><form:textarea path="observacao" cols="70" rows="5" /></td>
						</tr>
						<tr class="prop, jur">
							<td valign="top" class="name"><label for="matricula"><spring:message
										code="participante.matricula.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="100"
									path="matricula" size="100" /> 
									<form:errors path="matricula" cssClass="error" />
							</td>
						</tr>
						<tr class="prop, jur">
							<td valign="top" class="name"><label for="cargo"><spring:message
										code="participante.cargo.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="100"
									path="cargo"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="100" /></td>
						</tr>
						
						<tr class="prop, jur">
							<td valign="top" class="name"><label for=""><spring:message	code="administracaoPublica" />:</label></td>
							<td>
								<label>
									<form:radiobutton path="administracaoPublica" value="estadual" /> 
									Estadual
								</label>
								<label>
									<form:radiobutton path="administracaoPublica"  value="municipal" /> 
									Municipal
								</label>
							</td>
						</tr>
						
						<tr class="prop, jur, localidadeTr">
							<td valign="top" class="name">
								<label for="localidadeId">Munic�pio:</label>
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
						
						<!-- 
						<tr class="prop, jur">
							<td valign="top" class="name"><label for="lotacao"><spring:message
										code="participante.lotacao.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="100"
									path="lotacao" size="100"
									onkeyup="javascript:this.value=this.value.toUpperCase();" />*
									<form:errors path="lotacao" cssClass="error" /></td>
						</tr>
						-->
						<tr class="prop, soc">
							<td valign="top" class="name"><label for="entidade"><spring:message
										code="participante.entidade.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="100"
									path="entidade"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="100" /></td>
						</tr>
						<tr class="prop, soc">
							<td valign="top" class="name"><label for="profissao"><spring:message
										code="participante.profissao.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="70"
									path="profissao"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="100" /></td>
						</tr>
					</tbody>
				</table>
			</div>			

		
				<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/participante/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="atualizar" type="submit" class="btn btn-primary">
 						<spring:message code="default.button.save.label" />
					</button>

				</div>
			</div>
			<form:hidden path="id" />
			<form:hidden path="responsavelEvento"/>
		</form:form>
		
		
	</div>

</body>
</html>
