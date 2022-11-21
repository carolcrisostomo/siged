<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="modulo.label" /></title>
<spring:url value="/ajax/procuraMunicipio" var="procuraCidadeUrl" />
</head>
<body>
	<script type="text/javascript"
		src="<c:url value="/static/js/ajax.loading.js"/>"></script>
	<script type="text/javascript">
	jQuery(document).ready(function($) {
		
		var load = new SIGED.AjaxLoading();
		
		
		


		
		if($("#eventoId").val() != 0){
			if("${readonly}") {
				$("#eventoId").mousedown(function(e){e.preventDefault();});
				$("#eventoId").keydown(function(e){e.preventDefault();});
			}
		}
		if($("#modalidade option:selected").val() == 2) {
			$("#frequencia").val(0).attr('readOnly', true);
		} else {
			if($("#frequencia").val() == 0)
				$("#frequencia").val(75);
			$("#frequencia").attr('readOnly', false);
		}
		
		$("#modalidade").change(function() {
			var localizacaoURL = '';
			if ($("#modalidade option:selected").val() == 2) {
			console.log('teste')
				$("#pais").hide();
				$("#ufLinha").hide();
   				$("#cidadeLinha").hide();
   		 		$("#cidadePaisLinha").hide();
				$("#frequencia").val(0).attr('readOnly', true);
				localizacaoURL = '/tipolocalizacaoevento/ead';
			} else {
				$("#pais").show();
				$("#ufLinha").show();
				if ($("#frequencia").val() == 0) {
					$("#frequencia").val(75);						
				}
				$("#frequencia").attr('readOnly', false);
				
				if ($("#paisId").val() == 1) {
					$("#ufLinha").show();
		   			$("#cidadeLinha").show();
		   			$("#cidadePaisLinha").hide();
		  		} else {
		   			$("#ufId").val(0);
		   			$("#cidadeId").html('<option value="0">Selecione...</option>').trigger('chosen:updated');
		   			$("#ufLinha").hide();
		   			$("#cidadeLinha").hide();
		   		 	$("#cidadePaisLinha").show();
		  		}    
				
				localizacaoURL = '/tipolocalizacaoevento/presencial';
			}
			
			if ($("#modalidade option:selected").val() == 0) {
				atualizarLocalizacoes([]);
			} else {
				load.start();
				jQuery.ajax({
					url: localizacaoURL,
					type: 'GET',
					contentType: 'application/json',
					success: function(localizacoes) {
						atualizarLocalizacoes(localizacoes);
					},
					error: function(error) {
						atualizarLocalizacoes([]);
					},
					complete: function() {
						load.stop();
					} 
				});					
			}
		});
		
		function atualizarLocalizacoes(localizacoes) {
			var html = '<option value="0">Selecione...</option>';
			var len = localizacoes.length;
			for ( var i = 0; i < len; i++) {
				html += '<option value="' + localizacoes[i].id + '">' + localizacoes[i].descricao + '</option>';
			}

			$("#localizacaoId").html(html).trigger('chosen:updated');
		}
		
		
		updateMunicipioSelected('${procuraCidadeUrl}', 'ufId', 'cidadeId', '${tipolocalizacaoevento.municipio.id}');
		
		if ($("#paisId").val() == 1) {
			$("#ufLinha").show();
   			$("#cidadeLinha").show();
   			$("#cidadePaisLinha").hide();
  		} else {
   			$("#ufId").val(0);
   			$("#cidadeId").html('<option value="0">Selecione...</option>').trigger('chosen:updated');
   			$("#ufLinha").hide();
   			$("#cidadeLinha").hide();
   		 	$("#cidadePaisLinha").show();
  		}    

  		$("#paisId").change(function(){
   			if ($("#paisId").val() == 1) {
    			$("#ufLinha").show();    			
    			$("#cidadeLinha").show(); 
    			$("#cidadePaisLinha").hide();
   			} else {
    			$("#ufId").val(0);
    			$("#cidadeId").html('<option value="0">Selecione...</option>').trigger('chosen:updated');
    			$("#ufLinha").hide();
    			$("#cidadeLinha").hide(); 
    			$("#cidadePaisLinha").show();
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
			<spring:message code="default.add.label" />
			-
			<spring:message code="modulo.label" />
		</h1>

		<c:url var="url" value="/modulo" />
		<form:form action="${url}" method="POST" modelAttribute="modulo">

			<input name="readonly" type="hidden" value="${readonly}" />
			<!-- <button type="button" class="btn btn-primary">
			<i class="bi bi-alexa"></i>
			Primary</button> -->
			<div class="dialog">
				<table>
					<tbody>
						<tr class="prop">
							<td valign="top" class="name"></td>
							<td style="text-align: right;">(*) Campos Obrigat�rios</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"></td>
							<td style="text-align: right;">(**) Campo Obrigat�rio para
								M�dulo na Modalidade Presencial</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="evento"><spring:message
										code="modulo.evento.label" />:</label></td>

							<td><form:select path="eventoId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${eventoList}" itemLabel="nome"
										itemValue="id" />
								</form:select>* <br /> <form:errors path="eventoId" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="titulo"><spring:message
										code="modulo.titulo.label" />:</label></td>
							<td><form:input cssStyle="width:350px" maxlength="255"
									path="titulo"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="30" />* <form:errors path="titulo" cssClass="error" /></td>
						</tr>
						<!-- PA�S -->
					
						<tr class="prop" id="pais">
							<td valign="top" class="name"><label for="paisId"><spring:message
										code="tipoLocalizacaoEvento.pais.label" />:</label></td>
							<td><form:select path="paisId" id="paisId">
									<form:options items="${listaPaises}" itemLabel="descricao"
										itemValue="id" />
								</form:select>* <form:errors path="paisId" cssClass="error" /></td>
						</tr>
						<!-- UF -->
						<tr class="prop" id="ufLinha">
							<td valign="top" class="name"><label for="uf"><spring:message
										code="tipoLocalizacaoEvento.uf.label" />:</label></td>
							<td><form:select path="ufMunicipio" id="ufId"
									onchange="updateMunicipio('${procuraCidadeUrl}', 'ufId', 'cidadeId')">
									<option value="0">Selecione...</option>
									<form:options items="${listaUfs}" itemLabel="nome"
										itemValue="uf" />
								</form:select>* <form:errors path="ufMunicipio" cssClass="error" /></td>
						</tr>
						<!-- CIDADE -->
						<tr class="prop" id="cidadeLinha">
							<td valign="top" class="name"><label for="cidadeId"><spring:message
										code="tipoLocalizacaoEvento.cidade.label" />:</label></td>
							<td><form:select path="municipio" id="cidadeId">
									<option value="0">Selecione...</option>
								</form:select>* <form:errors path="municipio" cssClass="error" /></td>
						</tr>
						<!-- Caso o Pa�s for diferente de Brasil inserir cidade manualmente-->
						<tr class="prop" id="cidadePaisLinha">
							<td valign="top" class=""><label for="cidadeId"><spring:message
										code="tipoLocalizacaoEvento.cidade.label" />:</label></td>
							<td><form:input cssStyle="width:400px" maxlength="100"
									path="cidadePais"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="100" />* <form:errors path="cidadePais" cssClass="error" />
							</td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label class="trigger"
								for="modalidade"><spring:message
										code="modulo.modalidade.label" />:</label></td>

							<td><form:select path="modalidade">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${modalidadeList}" itemLabel="descricao"
										itemValue="id" />
								</form:select>* <form:errors path="modalidade" cssClass="error" /></td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label
								for="tipoLocalizacaoEvento"><spring:message
										code="modulo.localizacao.label" />:</label></td>

							<td><form:select path="localizacaoId" id="localizacaoId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${localizacaoList}" itemLabel="descricao"
										itemValue="id" />
								</form:select>* <form:errors path="localizacaoId" cssClass="error" /></td>
						</tr>


						<tr class="prop">
							<td valign="top" class="name"><label for="cargaHoraria"><spring:message
										code="modulo.cargaHoraria2.label" />:</label></td>
							<td><form:input cssStyle="width:80px" maxlength="255"
									path="cargaHoraria" size="30" alt="integer"
									readonly="${readonly}" />* <form:errors path="cargaHoraria"
									cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="dataInicio"><spring:message
										code="modulo.dataInicio.label" />:</label></td>
							<td><form:input cssStyle="width:90px" maxlength="255"
									path="dataInicio" size="30" alt="date" />* <form:errors
									path="dataInicio" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="dataFim"><spring:message
										code="modulo.dataFim.label" />:</label></td>
							<td><form:input cssStyle="width:90px" maxlength="255"
									path="dataFim" size="30" alt="date" />* <form:errors
									path="dataFim" cssClass="error" /></td>
						</tr>
						<tr class="prop, horario">
							<td valign="top" class="name"><label for="horaInicioTurno1"><spring:message
										code="modulo.horaInicioTurno1de2.label" />:</label></td>
							<td><form:input cssStyle="width:50px" maxlength="255"
									path="horaInicioTurno1" size="30" alt="time" />** <form:errors
									path="horaInicioTurno1" cssClass="error" /></td>
						</tr>
						<tr class="prop, horario">
							<td valign="top" class="name"><label for="horaFimTurno1"><spring:message
										code="modulo.horaFimTurno1de2.label" />:</label></td>
							<td><form:input cssStyle="width:50px" maxlength="255"
									path="horaFimTurno1" size="30" alt="time" />** <form:errors
									path="horaFimTurno1" cssClass="error" /></td>
						</tr>
						<tr class="prop, horario">
							<td valign="top" class="name"><label for="horaInicioTurno2"><spring:message
										code="modulo.horaInicioTurno2de2.label" />:</label></td>
							<td><form:input cssStyle="width:50px" maxlength="255"
									path="horaInicioTurno2" size="30" alt="time" /> <form:errors
									path="horaInicioTurno2" cssClass="error" /></td>
						</tr>
						<tr class="prop, horario">
							<td valign="top" class="name"><label for="horaFimTurno2"><spring:message
										code="modulo.horaFimTurno2de2.label" />:</label></td>
							<td><form:input cssStyle="width:50px" maxlength="255"
									path="horaFimTurno2" size="30" alt="time" /> <form:errors
									path="horaFimTurno2" cssClass="error" /></td>
						</tr>
						<tr class="prop, horario">
							<td valign="top" class="name"><label for="horaInicioTurno3"><spring:message
										code="modulo.horaInicioTurno3de3.label" />:</label></td>
							<td><form:input cssStyle="width:50px" maxlength="255"
									path="horaInicioTurno3" size="30" alt="time" /> <form:errors
									path="horaInicioTurno3" cssClass="error" /></td>
						</tr>
						<tr class="prop, horario">
							<td valign="top" class="name"><label for="horaFimTurno3"><spring:message
										code="modulo.horaFimTurno3de3.label" />:</label></td>
							<td><form:input cssStyle="width:50px" maxlength="255"
									path="horaFimTurno3" size="30" alt="time" /> <form:errors
									path="horaFimTurno3" cssClass="error" /></td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="nota"><spring:message
										code="modulo.nota2.label" />:</label></td>
							<td><form:input cssStyle="width:40px" maxlength="255"
									path="nota" size="30" /> <form:errors path="nota"
									cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="frequencia"><spring:message
										code="modulo.frequenciaMinima2.label" />:</label></td>
							<td><form:input cssStyle="width:110px" maxlength="255"
									path="frequencia" size="30" value="75" />* <form:errors
									path="frequencia" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="observacao"><spring:message
										code="modulo.observacao.label" />:</label></td>

							<td><form:textarea path="observacao" cols="70" rows="5" />
								<form:errors path="observacao" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name" nowrap="nowrap"><label
								for="instrutor"><spring:message
										code="modulo.instrutor.label" />:<br />(Para selecionar <br />
									mais de um,<br /> segure a tecla<br /> Control e clique)</label></td>
							<td valign="top"><form:select id="instrutores"
									path="instrutorList" multiple="true" size="10">
									<form:options items="${instrutorList}" itemValue="id"
										itemLabel="nome"></form:options>
								</form:select>* <form:errors path="instrutorList" cssClass="error" /></td>
						</tr>
					</tbody>
				</table>
			</div>


			<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/modulo/"/>"><button type="button"
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
