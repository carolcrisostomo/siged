<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="default.button.edit.label" /> - <spring:message code="evento.label" /></title>
<spring:url value="/ajax/procuraParticipantePorNome" var="procuraParticipantePorNomeUrl" />
<spring:url value="/ajax/procuraMunicipio" var="procuraCidadeUrl" />
</head>
<body>
	<script type="text/javascript">
		/* jQuery(document).ready(function($){
			
			$("#atualizarEvento").click(function(){
				
				if ('${temCertificadoEmitido}'){
				
					if(confirm("Caso os campos T�tulo, Tipo do Evento, "
							+ "Carga Hor�ria, Data de In�cio da Realiza��o e Data de Fim da Realiza��o "
							+ "tenham sido alterados, todos os certificados emitidos para este evento "
							+ "at� o momento ser�o invalidados. Confirma a atualiza��o mesmo assim?"))
						$('form').submit();
						
						
				}else{
					$('form').submit();
				}
				
			});
			
		}); */
		jQuery(document).ready(function($){  
			
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
			<spring:message code="default.button.edit.label" />
			-
			<spring:message code="evento.label" />
		</h1>
		<c:url var="url" value="/evento/${evento.id}/update" />
		<form:form action="${url}" method="POST" modelAttribute="evento" enctype="multipart/form-data" id="updateEventoForm">
			<div class="dialog">
				<table>
					<tbody>
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">(*)
								Campos Obrigat�rios</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label for="titulo"><spring:message
										code="evento.titulo.label" />:</label></td>
							<td><form:input cssStyle="width:500px" maxlength="255"
									path="titulo"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="30" />* <form:errors path="titulo" cssClass="error" /></td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="tipoId"><spring:message
										code="evento.tipoEvento.label" />:</label></td>

							<td><form:select path="tipoId" items="${tipoEventoList}"
									itemLabel="descricao" itemValue="id" />* <form:errors
									path="tipoId" cssClass="error" /></td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="objetivoGeral"><spring:message
										code="evento.objetivoGeral.label" />:</label></td>

							<td><form:textarea path="objetivoGeral"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									cols="70" rows="5" maxlength="1000"/>* <form:errors path="objetivoGeral"
									cssClass="error" /></td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label
								for="objetivoEspecifico"><spring:message
										code="evento.objetivoEspecifico.label" />:</label></td>

							<td><form:textarea path="objetivoEspecifico"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									cols="70" rows="5" maxlength="1000"/> <form:errors path="objetivoEspecifico"
									cssClass="error" /></td>
						</tr>
						<!--  
						<tr class="prop">
							<td valign="top" class="name">
								<label for="paisId"><spring:message	code="tipoLocalizacaoEvento.pais.label" />:</label>
							</td>
							<td>
								<form:select path="paisId" id="paisId">
			 						<form:option value="0">Selecione...</form:option>
			 					    <form:options items="${listaPaises}" itemLabel="descricao" itemValue="id" />
								</form:select>*	<form:errors path="paisId" cssClass="error" />
					 		</td>
						</tr>
						<tr class="prop" id="ufLinha">
							<td valign="top" class="name">
								<label for="uf"><spring:message	code="tipoLocalizacaoEvento.uf.label" />:</label>
							</td>
							<td>
								<form:select path="ufMunicipio" id="ufId" onchange="updateMunicipio('${procuraCidadeUrl}', 'ufId', 'cidadeId')" >
									<option value="0">Selecione...</option>
									<form:options items="${listaUfs}" itemLabel="nome" itemValue="uf" />
								</form:select>*	<form:errors path="ufMunicipio" cssClass="error" />
							</td>
						</tr>
						<tr class="prop" id="cidadeLinha">
							<td valign="top" class="name">
								<label for="cidadeId"><spring:message code="tipoLocalizacaoEvento.cidade.label" />:</label>
							</td>
							<td>
								<form:select path="municipio" id="cidadeId" >
									<option value="0">Selecione...</option>
								</form:select>* <form:errors path="municipio" cssClass="error" />
							</td>
						</tr>
						<!-- Caso o Pa�s for diferente de Brasil inserir cidade manualmente
						<tr class="prop" id="cidadePaisLinha">
							<td valign="top" class="">
								<label for="cidadeId"><spring:message code="tipoLocalizacaoEvento.cidade.label" />:</label>
							</td>
							<td>
								<form:input cssStyle="width:400px" maxlength="100" path="cidadePais" onkeyup="javascript:this.value=this.value.toUpperCase();" size="100" />* 
								<form:errors path="cidadePais" cssClass="error" />
							</td>
						</tr>-->

						<tr class="prop">
							<td valign="top" class="name"><label for="eixoTematicoId"><spring:message
										code="evento.eixoTematico.label" />:</label></td>

							<td><form:select path="eixoTematicoId"
									items="${eixoTematicoList}" itemLabel="descricao"
									itemValue="id" />* <form:errors path="eixoTematicoId"
									cssClass="error" /></td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name">
								<label for="programas"><spring:message code="evento.programa.label.plural" />:</label>
							</td>

							<td>
								<form:select path="programas" id="programas" items="${programaList}" itemLabel="descricao" itemValue="id" multiple="true"/>
								<form:errors path="programas" cssClass="error" />
							</td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="publicoAlvo"><spring:message
										code="evento.publicoAlvo.label" />:</label></td>

							<td><form:select path="publicoAlvoList" id="publicoAlvoList"
									items="${tipoPublicoAlvoList2}" itemLabel="descricao"
									multiple="true" itemValue="id" />* <form:errors
									path="publicoAlvoList" cssClass="error" /></td>
						</tr>						

						<%-- <!-- Campo modalidade no evento foi depreciado. -->
						<tr class="prop">
							<td valign="top" class="name"><label for="modalidade"><spring:message
										code="evento.modalidade.label" />:</label></td>

							<td><form:select path="modalidadeId">
									<form:option value="0">Selecione...</form:option>									
									<form:options items="${modalidadeList}" itemLabel="descricao"
										itemValue="id" />
								</form:select>* <form:errors path="modalidadeId" cssClass="error" /></td>									
						</tr>
						
						<tr class="prop, ead">
							<td valign="top" class="name"><label for="administrador"><spring:message
										code="evento.monitor.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="100"
									path="administrador"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="100" /> <form:errors path="administrador"
									cssClass="error" /></td>
						</tr>

						<tr class="prop, ead">
							<td valign="top" class="name"><label for="conteudista1"><spring:message
										code="evento.conteudista1.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="100"
									path="conteudista1"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="100" /> <form:errors path="conteudista1"
									cssClass="error" /></td>
						</tr>

						<tr class="prop, ead">
							<td valign="top" class="name"><label for="conteudista2"><spring:message
										code="evento.conteudista2.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="100"
									path="conteudista2"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="100" /> <form:errors path="conteudista2"
									cssClass="error" /></td>
						</tr>
						--%>
										
						<tr class="prop">
							<td valign="top" class="name"><label for="cargaHoraria"><spring:message
										code="evento.cargaHoraria.label" />:</label></td>
							<td><form:input cssStyle="width:80px" maxlength="255"
									path="cargaHoraria" size="30" alt="integer" />* <form:errors
									path="cargaHoraria" cssClass="error" /></td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label for="vagas"><spring:message
										code="evento.vagas.label" />:</label></td>
							<td><form:input cssStyle="width:50px" maxlength="255"
									path="vagas" size="30" alt="integer" />* <form:errors
									path="vagas" cssClass="error" /></td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label
								for="permitePreInscricao"><spring:message
										code="evento.permitePreInscricao.label" />:</label></td>

							<td><form:select path="permitePreInscricao" class="js_permite_pre_inscricao_select"
									items="${SNList}" />* <form:errors path="permitePreInscricao"
									cssClass="error" /></td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label
								for="dataInicioPreInscricao"><spring:message
										code="evento.dataInicioPreInscricao.label" />:</label></td>
							<td><form:input cssStyle="width:90px" maxlength="255"
									path="dataInicioPreInscricao" size="30" alt="date" class="js_data_inicio_pre_inscricao" /> <form:errors
									path="dataInicioPreInscricao" cssClass="error" /></td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label
								for="dataFimPreInscricao"><spring:message
										code="evento.dataFimPreInscricao.label" />:</label></td>
							<td><form:input cssStyle="width:90px" maxlength="255"
									path="dataFimPreInscricao" size="30" alt="date" class="js_data_fim_pre_inscricao" /> <form:errors
									path="dataFimPreInscricao" cssClass="error" /></td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label
								for="dataInicioPrevisto"><spring:message
										code="evento.dataInicioPrevisto.label" />:</label></td>
							<td><form:input cssStyle="width:90px" maxlength="255"
									path="dataInicioPrevisto" size="30" alt="date" />* <form:errors
									path="dataInicioPrevisto" cssClass="error" /></td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label for="dataFimPrevisto"><spring:message
										code="evento.dataFimPrevisto.label" />:</label></td>
							<td><form:input cssStyle="width:90px" maxlength="255"
									path="dataFimPrevisto" size="30" alt="date" />* <form:errors
									path="dataFimPrevisto" cssClass="error" /></td>
						</tr>

						<c:forEach items="${eventohistorico}" var="h">
							<tr class="prop">
								<td></td>
								<td>> Data de In�cio Previsto: <fmt:formatDate
										pattern="dd/MM/yyyy" value="${h.dataInicioPrevisto}" /><br />
									> Data de Fim Previsto: <fmt:formatDate pattern="dd/MM/yyyy"
										value="${h.dataFimPrevisto}" /></td>
							</tr>
						</c:forEach>

						<tr class="prop">
							<td valign="top" class="name"><label
								for="dataInicioRealizacao"><spring:message
										code="evento.dataInicioRealizacao.label" />:</label></td>
							<td><form:input cssStyle="width:90px" maxlength="255"
									path="dataInicioRealizacao" size="30" alt="date" /> <form:errors
									path="dataInicioRealizacao" cssClass="error" /></td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label for="dataFimRealizacao"><spring:message
										code="evento.dataFimRealizacao.label" />:</label></td>
							<td><form:input cssStyle="width:90px" maxlength="255"
									path="dataFimRealizacao" size="30" alt="date" /> <form:errors
									path="dataFimRealizacao" cssClass="error" /></td>
						</tr>
						
						<%-- <!-- Campo localizacao no evento foi depreciado. -->
						<tr class="prop">
							<td valign="top" class="name"><label for="localizacaoId"><spring:message
										code="evento.tipoLocalizacaoEvento.label" />:</label></td>
									
							<td><form:select path="localizacaoId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${tipoLocalizacaoEventoList}"
										itemLabel="descricao" itemValue="id" />
								</form:select>* <form:errors path="localizacaoId" cssClass="error" /></td>
						</tr> 
						--%>

						<tr class="prop">
							<td valign="top" class="name"><label for="conteudo"><spring:message
										code="evento.conteudo.label" />:</label></td>

							<td><form:textarea path="conteudo"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									cols="70" rows="5" maxlength="4000" />* <form:errors path="conteudo"
									cssClass="error" /></td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="resultadoEsperado"><spring:message
										code="evento.resultadoEsperado.label" />:</label></td>

							<td><form:textarea path="resultadoEsperado"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									cols="70" rows="5" maxlength="2000" /> <form:errors
									path="resultadoEsperado" cssClass="error" /></td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="observacoes">
								<spring:message code="evento.observacoes.label" />:</label>
							</td>

							<td><form:textarea path="observacoes" cols="70" rows="5" maxlength="2000"/>
								<form:errors path="observacoes" cssClass="error" /></td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name">
								<label for="observacoesPublicas"><spring:message code="evento.observacoesPublicas.label" />:</label>
							</td>
							<td>
								<form:textarea path="observacoesPublicas" cols="70" rows="5" maxlength="2000"/>
								<form:errors path="observacoesPublicas" cssClass="error" />
							</td>
						</tr>
						
						
						<!-- PROVEDOR ManyToMany -->
						<tr class="prop">
							<td valign="top" class="name">
								<label for="provedores"><spring:message code="evento.provedorEvento.label.plural" />:</label>
							</td>

							<td>
								<form:select path="provedores" items="${provedorEventoList}" itemLabel="descricao" itemValue="id" multiple="true" size="20"/>* 
								<form:errors path="provedores" cssClass="error" />
							</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label for="areaTribunalId"><spring:message
										code="evento.tipoAreaTribunal.label" />:</label></td>

							<td><form:select path="areaTribunalId"
									items="${tipoAreaTribunalList}" itemLabel="descricao"
									itemValue="id" />* <form:errors path="areaTribunalId"
									cssClass="error" /></td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label
								for="permiteCertificado"><spring:message
										code="evento.permiteCertificado.label" />:</label></td>

							<td><form:select path="permiteCertificado" items="${SNList}" />*
								<form:errors path="permiteCertificado" cssClass="error" /></td>
						</tr>
						
						<!-- PUBLICADO -->
						<tr class="prop">
							<td valign="top" class="name"><label for="publicado"><spring:message code="evento.publicado.label" />:</label></td>
							<td>
								<form:select path="publicado" items="${SNList}" />*
								<form:errors path="publicado" cssClass="error" />
							</td>
						</tr>
						
						<!-- PUBLICADO PARA -->
						<tr class="prop" id="isRestritoContainer">
							<td valign="top" class="name">
								<label for="isRestrito">Publicado para:</label>
							</td>
							<td>
								<form:select path="isRestrito" id="isRestrito">
									<form:option value="0">TODOS</form:option>
									<form:option value="1">Restringir</form:option>
								</form:select>* 
								<form:errors path="isRestrito" cssClass="error" />
							</td>
						</tr>
						
						<!-- LISTA INTERESSADOS -->
						<tr id="interessadosContainer">
							<td colspan="2">
								<table style="border: 0px;">
									<tr>
										<td>
											<label>Interessados</label>																											
										</td>
										<td></td>
										<td>
											<label>Ser�o adicionados como interessados</label>
										</td>
									</tr>							
									<tr>
										<td style="width:400px;">
											<input id="nomeParticipante" style="width:370px; margin-bottom: 3px;" maxlength="255" name="nomeParticipante"/>								
											<input id="buscarParticipante" class="search campo" type="button" />								
											<select id="naoselecionados" multiple="multiple" style="height:250px; width:380px;"></select>														
										</td>
										<td style="width:78px;">
											<input id="botao1" type="button" title="Adicionar" value="&rsaquo;" style="width:50px;"/><br /><br />
											<input id="botao2" type="button" title="Remover" value="&lsaquo;" style="width:50px;"/><br /><br />
											<input id="botao3" type="button" title="Adicionar todos" value="&raquo;" style="width:50px;"/><br /><br />
											<input id="botao4" type="button" title="Remover todos" value="&laquo;" style="width:50px;"/><br /><br />								
										</td>
										<td style="width:400px;">									
											<form:select id="selecionados" 
												path="interessados" items="${interessadosList}" itemLabel="nomeCpf" itemValue="id"
												multiple="multiple" style="height:271px; width:380px;"></form:select>													
										</td>
										<input type="hidden" id="interessadosHidden" name="interessadosHidden" value="" />
									</tr>
									<tr>
										<td></td>
										<td></td>
									</tr>							
								</table>
							</td>
						</tr>
						
						<!-- LINK DO EVENTO -->
						<tr class="prop">
							<td valign="top" class="name">
								<label>Link para o evento:</label>
							</td>
							<td>
								<form:input
									cssStyle="width:500px"
									maxlength="255"
									path="linkEvento"
									size="30"
								/>
							</td>
						</tr>
						
						<!-- LINK DA GRAVA��O -->
						<tr class="prop">
							<td valign="top" class="name">
								<label>Link para a grava��o:</label>
							</td>
							<td>
								<form:input
									cssStyle="width:500px"
									maxlength="255"
									path="linkGravacao"
									size="30"
								/>
							</td>
						</tr>
						
						<tr class="prop">
							<td valign="top" class="name"><label for="identificador"><spring:message
										code="evento.identificador.label" />:</label></td>
							<td><form:input cssStyle="width:80px" maxlength="255"
									path="identificador" size="30" /> <form:errors
									path="identificador" cssClass="error" /></td>							
						</tr>

						<!-- LISTA RESPONSAVEL PELO EVENTO -->
						<tr class="prop">
							<td valign="top" class="name"><label for="responsavelEvento"><spring:message
										code="evento.responsavelEvento.label" />:</label></td>

							<td><form:select path="responsavelEvento">
									<form:options items="${responsavelEventoList}"
										itemLabel="nomeCpf" itemValue="id" />
								</form:select>* <form:errors path="responsavelEvento" cssClass="error" /></td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="moduloUnico"><spring:message
										code="evento.modulounico.label" />:</label></td>

							<td><form:select path="moduloUnico" items="${SNList}" />* <form:errors
									path="moduloUnico" cssClass="error" /></td>
						</tr>
						
					</tbody>
				</table>
				
				
		
		<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
		
				<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/evento/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="atualizarEvento" type="submit" class="btn btn-primary">
 						<spring:message code="default.button.save.label" />
					</button>

				</div>
			</div>
		</sec:authorize>
			</div>			
			
			<form:hidden path="id" />
			<form:hidden path="dataCadastro" />
			<form:hidden path="certificadoPersonalizadoName"/>
		</form:form>
	</div>
	<script type="text/javascript">
		if ("${evento.publicoAlvoList}" != "") {			
			var tiposSelecionados = "${evento.publicoAlvoList}";
			tiposSelecionados = tiposSelecionados.substr(1, tiposSelecionados.length-2);
			
			var tiposSelecionados2 = tiposSelecionados.split(", ");
	
			var select = document.getElementById("publicoAlvoList");
	
			for (var i = 0; i < select.options.length; i++) {
				for (var j = 0; j < tiposSelecionados2.length; j++) {
					if (select.options[i].text == tiposSelecionados2[j]) {
						select.options[i].selected = true;
					}
				}	
			}
		}
		if ("${evento.provedores}" != "") {			
			var provedoresSelecionados = "${evento.provedores}";
			provedoresSelecionados = provedoresSelecionados.substr(1, provedoresSelecionados.length-2);
			provedoresSelecionados = provedoresSelecionados.split(", ");
			
			var selectProvedores = document.getElementById("provedores");
			
			for (var i = 0; i < selectProvedores.length; i++) {
				for (var j = 0; j < provedoresSelecionados.length; j++) {
					if (selectProvedores.options[i].text == provedoresSelecionados[j]) {
						selectProvedores.options[i].selected = true;
					}
				}	
			}
		}
	</script>
	<script type="text/javascript" src="<c:url value="/static/js/evento.permite_pre_inscricao.js"/>"></script>
	<script type="text/javascript"> 
		jQuery(document).ready(function($){ 
			
			$('#interessadosContainer select').chosen('destroy');
	  		
			updateParticipantePorNomeSelectMultiple('${procuraParticipantePorNomeUrl}', 'nomeParticipante', 'naoselecionados', '');
			
			var publicado = $('#publicado');
			var restrito = $('#isRestrito');
			var isRestritoContainer = $('#isRestritoContainer');
			var interessadosContainer = $('#interessadosContainer');
			
			function verificarPublicacao() {
				if(publicado.val() == 'N') {
					isRestritoContainer.hide();
					restrito.val(0);
					verificarInteressados();
				} else if(publicado.val() == 'S') {
					isRestritoContainer.show();
				}
			}
			
			function verificarInteressados() {
				if(restrito.val() == 1) {
					interessadosContainer.show();
				}
				else {
					$("#botao4").trigger('click');
					interessadosContainer.hide();
				}
					
			}
			
			verificarPublicacao();
			verificarInteressados();
			
			publicado.change(function(e) {
				verificarPublicacao();
			});
			
			restrito.change(function(e) {
				verificarInteressados()
			});
			
			
			$('#nomeParticipante').keypress(function (e) {
				var key = e.which;
				if(key == 13){  // the enter key code
					updateParticipantePorNomeSelectMultiple('${procuraParticipantePorNomeUrl}', 'nomeParticipante', 'naoselecionados', 'true');			    
				}
			});
	
			$("#buscarParticipante").click(function($){
				updateParticipantePorNomeSelectMultiple('${procuraParticipantePorNomeUrl}', 'nomeParticipante', 'naoselecionados', 'true');
			});
			
			$("#botao1").click(function(){
				
				$("#naoEncontrado").remove();			
				
				var naoselecionados = $('#naoselecionados :selected').length;
				var selecionados = $('#selecionados').children('option').length;
				
				transfereSelecionados('naoselecionados', 'selecionados');
				$("#totalSeraoConfirmadas").val($("#selecionados").children('option').length);
	  		});
			
			$("#botao2").click(function(){ 
				
				$("#naoEncontrado").remove();			
				
				transfereSelecionados('selecionados', 'naoselecionados');
				
				$("#totalSeraoConfirmadas").val($("#selecionados").children('option').length);
	  		});
			
			$("#botao3").click(function(){ 
				
				$("#naoEncontrado").remove();
				
				var naoselecionados = $('#naoselecionados').children('option').length;
				var selecionados = $('#selecionados').children('option').length;
				
				transfereTodos('naoselecionados', 'selecionados');
				$("#totalSeraoConfirmadas").val($("#selecionados").children('option').length);
	  		});
			
			$("#botao4").click(function(){
				
				$("#naoEncontrado").remove();
				
				transfereTodos('selecionados', 'naoselecionados');
	
				$("#totalSeraoConfirmadas").val($("#selecionados").children('option').length);
	  		});
					
			$("#atualizarEvento").click(function(e){
				e.preventDefault();
				
				if ('${temCertificadoEmitido}'){
					if(confirm("Caso os campos T�tulo, Tipo do Evento, "
							+ "Carga Hor�ria, Data de In�cio da Realiza��o e Data de Fim da Realiza��o "
							+ "tenham sido alterados, todos os certificados emitidos para este evento "
							+ "at� o momento ser�o invalidados. Confirma a atualiza��o mesmo assim?")) {
						document.getElementById("updateEventoForm").submit();
					}
				} else {
					var listaSelecionados = document.getElementById("selecionados");
					
					var participantesId = "";
					
					for(var i = 0; i < listaSelecionados.options.length; i++){
						participantesId += listaSelecionados.options[i].value + "," ;
					}
					if(publicado.val() == 'S' && restrito.val() == 1 && participantesId == "") {
						alert("Informe os participante interessados do evento");
					} else {
						document.getElementById("interessadosHidden").value = participantesId;
						document.getElementById("updateEventoForm").submit();
					}
				}
				
	  		});	
			
			$(document).ajaxStop($.unblockUI);
	 	
		});	
	</script>
</body>
</html>
