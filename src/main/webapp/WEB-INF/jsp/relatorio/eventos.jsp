<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
<spring:url value="/ajax/procuraMunicipio" var="procuraMunicipioUrl" />
</head>
<body>
	<script type="text/javascript">	
		function validaProvedorTerceiro($){
			if ($("#checkbox").is(':checked')) {
				$("#evento_provedor_id").attr('disabled', 'disabled');
			} else {
				$("#evento_provedor_id").removeAttr('disabled');
			}
			$("#evento_provedor_id").trigger('chosen:updated');
		}		
		function validaPais($){
			if ($("#paisId").val() == 1) {
				$("#ufLinha").show();
				$("#cidadeLinha").show();
			} else {
				$("#ufId").val(0);
				$("#cidadeId").html('<option value="0">TODOS</option>').trigger('chosen:updated');
				$("#ufLinha").hide();
				$("#cidadeLinha").hide();			
			}
		}
		function desabilitarEsfera( $, desabilitar ){
			if(desabilitar){				
				$("input[name='administracaoPublica']").attr('disabled', 'disabled');	
			} else {
				$("input[name='administracaoPublica']").removeAttr('disabled');				
			}
		}
		jQuery(document).ready(function($){			
			
			validaProvedorTerceiro($);
			validaPais($);
			updateMunicipioSelected('${procuraMunicipioUrl}', 'ufId', 'cidadeId', '${relEventos.municipioId}', true);
			
			$("#incluirTipo1").click(function(){
				if ($("#excluirTipo1").is(':checked')) {
					$("#incluirTipo1").attr('checked', false);
				} else if (!$("#incluirTipo2").is(':checked') && !$("#incluirTipo3").is(':checked') && !$("#incluirTipo4").is(':checked')) {
					$("#incluirTipo1").attr('checked', true);
				}				
			});
			$("#incluirTipo2").click(function(){
				if ($("#excluirTipo2").is(':checked')) {
					$("#incluirTipo2").attr('checked', false);
				} else if (!$("#incluirTipo1").is(':checked') && !$("#incluirTipo3").is(':checked') && !$("#incluirTipo4").is(':checked')) {
					$("#incluirTipo2").attr('checked', true);
				}				
			});
			$("#incluirTipo3").click(function(){
				if ($("#excluirTipo3").is(':checked')) {
					$("#incluirTipo3").attr('checked', false);
				}else if (!$("#incluirTipo1").is(':checked') && !$("#incluirTipo2").is(':checked') && !$("#incluirTipo4").is(':checked')) {
					$("#incluirTipo3").attr('checked', true);
				}				
			});
			$("#incluirTipo4").click(function(){
				if ($("#excluirTipo4").is(':checked')) {
					$("#incluirTipo4").attr('checked', false);
				}else if (!$("#incluirTipo1").is(':checked') && !$("#incluirTipo2").is(':checked') && !$("#incluirTipo3").is(':checked')) {
					$("#incluirTipo4").attr('checked', true);
				}				
			});			
			$("#excluirTipo1").click(function(){
				if ($("#incluirTipo1").is(':checked')) {
					$("#excluirTipo1").attr('checked', false);
				}
			});
			$("#excluirTipo2").click(function(){
				if ($("#incluirTipo2").is(':checked')) {
					$("#excluirTipo2").attr('checked', false);
				}
			});
			$("#excluirTipo3").click(function(){
				if ($("#incluirTipo3").is(':checked')) {
					$("#excluirTipo3").attr('checked', false);
				}
			});
			$("#excluirTipo4").click(function(){
				if ($("#incluirTipo4").is(':checked')) {
					$("#excluirTipo4").attr('checked', false);
				}
			});			
			$("#incluirTipoParticipante1").click(function(){
				if (!$("#incluirTipoParticipante2").is(':checked') && !$("#incluirTipoParticipante3").is(':checked') && !$("#incluirTipoParticipante4").is(':checked')) {
					$("#incluirTipoParticipante1").attr('checked', true);
				}				
			});
			$("#incluirTipoParticipante2").click(function(){
				
				if (!$("#incluirTipoParticipante1").is(':checked') && !$("#incluirTipoParticipante3").is(':checked') && !$("#incluirTipoParticipante4").is(':checked')) {
					$("#incluirTipoParticipante2").attr('checked', true);
					desabilitarEsfera( $, false );
				} else if ($("#incluirTipoParticipante2").is(':checked')) {
					desabilitarEsfera( $, false );
				} else {
					desabilitarEsfera( $, true );
				}				
				
			});
			$("#incluirTipoParticipante3").click(function(){
				if (!$("#incluirTipoParticipante1").is(':checked') && !$("#incluirTipoParticipante2").is(':checked') && !$("#incluirTipoParticipante4").is(':checked')) {
					$("#incluirTipoParticipante3").attr('checked', true);
				}				
			});
			$("#incluirTipoParticipante4").click(function(){
				if (!$("#incluirTipoParticipante1").is(':checked') && !$("#incluirTipoParticipante2").is(':checked') && !$("#incluirTipoParticipante3").is(':checked')) {
					$("#incluirTipoParticipante4").attr('checked', true);
				}				
			});
			
			$("#checkbox").click(function(){
				validaProvedorTerceiro($);
			});			
			$("#paisId").change(function(){
				validaPais($);
			});			
			$("#ufId").change(function(){
				updateMunicipio('${procuraMunicipioUrl}', 'ufId', 'cidadeId', true);
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
		<h1>Relatório de Eventos</h1>
		<div class="message">Selecione "Internet" em Localização do Evento para eventos a distância.</div>
		<c:if test="${mensagemRel != null}">
			<div class="message" id="msgId">
				<c:out value="${mensagemRel}" />
			</div>
		</c:if>
		<c:url var="url" value="/relatorio/eventos/" />
		<form:form action="${url}" method="POST" modelAttribute="relEventos"
			id="form1">
			<div class="filter">
				<table>
					<tbody>
						<tr class="prop">
							<td valign="top" class="name"><label for=evento_tipo_id><spring:message code="evento.tipoEvento.label" /></label></td>
							<td valign="top">
								<form:select path="tipoEventoId" id="evento_tipo_id">
									<form:option value="0">TODOS</form:option>
									<form:options items="${tipoEventoList}" itemLabel="descricao" itemValue="id" />
								</form:select>
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="evento_titulo">Título</label></td>
							<td valign="top"><form:input cssStyle="width:250px" path="tituloEvento" id="evento_titulo" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label><spring:message code="evento.publicoAlvo.label" /></label></td>
							<td valign="top">
								<fieldset>										   
								   	<div class="divcheck"> 
									    <form:checkbox class="check incluirTipo" path="incluirTipo1" id="incluirTipo1" /> 
										<label for="incluirTipo1"><spring:message code="tipoPublicoAlvo.servidor" /></label>
								   	</div>								   
								    <div class="divcheck">
								    	<form:checkbox class="check incluirTipo" path="incluirTipo2" id="incluirTipo2" /> 
										<label for="incluirTipo2"><spring:message code="tipoPublicoAlvo.jurisdicionado" /></label>
								   	</div>								   
								   	<div class="divcheck">
								    	<form:checkbox class="check incluirTipo" path="incluirTipo3" id="incluirTipo3" /> 
										<label for="incluirTipo3"><spring:message code="tipoPublicoAlvo.sociedade" /></label>
									</div>
									<div class="divcheck">
								    	<form:checkbox class="check incluirTipo" path="incluirTipo4" id="incluirTipo4" /> 
										<label for="incluirTipo4"><spring:message code="tipoPublicoAlvo.membro"/></label>
									</div>
								</fieldset>
								  
								<fieldset style="margin: 12px 0px 5px 0px;">
								  	<legend>Exceto</legend>
								    <div class="divcheck"> 
									    <form:checkbox class="check excluirTipo" path="excluirTipo1" id="excluirTipo1" /> 
										<label for="excluirTipo1"><spring:message code="tipoPublicoAlvo.servidor" /></label>
								   	</div>								   
								    <div class="divcheck">
								    	<form:checkbox class="check excluirTipo" path="excluirTipo2" id="excluirTipo2" /> 
										<label for="excluirTipo2"><spring:message code="tipoPublicoAlvo.jurisdicionado" /></label>
								   	</div>								   
								   	<div class="divcheck">
								    	<form:checkbox class="check excluirTipo" path="excluirTipo3" id="excluirTipo3" /> 
										<label for="excluirTipo3"><spring:message code="tipoPublicoAlvo.sociedade" /></label>
									</div>
									<div class="divcheck">
								    	<form:checkbox class="check excluirTipo" path="excluirTipo4" id="excluirTipo4" /> 
										<label for="excluirTipo4"><spring:message code="tipoPublicoAlvo.membro" /></label>
									</div>									
								</fieldset>							
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="eixoTematicoId"><spring:message code="evento.eixoTematico.label" /></label></td>
							<td valign="top">
							    <form:select path="eixoTematicoId">
									<form:option value="0">TODOS</form:option>
									<form:options items="${eixoTematicoList}" itemLabel="descricao"	itemValue="id" />
								</form:select></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="programaId"><spring:message code="evento.programa.label" /></label></td>
							<td valign="top">
							    <form:select path="programaId">
									<form:option value="0">TODOS</form:option>
									<form:options items="${programaList}" itemLabel="descricao"	itemValue="id" />
								</form:select></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="data_inicio_previsto">Período Previsto</label></td>
							<td valign="top"><form:input path="dataInicioPrevisto1"
									id="data_inicio_previsto" alt="date" /> a <form:input
									path="dataInicioPrevisto2" id="data_inicio_previsto2"
									alt="date" /> <form:errors path="dataInicioPrevisto1"
									cssClass="error" /> <form:errors path="dataInicioPrevisto2"
									cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="data1">Período Realização</label></td>
							<td valign="top"><form:input path="dataInicio1" id="data1"
									alt="date" /> a <form:input path="dataInicio2" id="data2"
									alt="date" /> <form:errors path="dataInicio1" cssClass="error" />
								<form:errors path="dataInicio2" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="localizacao"><spring:message
										code="evento.tipoLocalizacaoEvento.label" /></label></td>
							<td valign="top"><form:select path="localizacaoId"
									id="evento_localizacao_id">
									<form:option value="0">TODOS</form:option>
									<form:options items="${tipoLocalizacaoEventoList}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>
						<tr class="prop" id="provedorEvento">
							<td valign="top" class="name"><label for="evento_provedor_id"><spring:message code="evento.provedorEvento.label" /></label></td>
							<td valign="top">
								<form:select path="provedorId"	id="evento_provedor_id">
									<form:option value="0">TODOS</form:option>
									<form:options items="${provedorEventoList}" itemLabel="descricao" itemValue="id" />
								</form:select>
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<div class="divcheck">
									<form:checkbox class="check" value="1" id="checkbox" path="provedoresTerceiros" />
									<label for="checkbox">Terceiros</label>
								</div>									
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name">
								<label for="paisId">
									<spring:message code="evento.pais.label" />
								</label>
							</td>
							<td>
								<form:select path="paisId" id="paisId" >
			 						<form:option value="0">TODOS</form:option>
			 						<form:options items="${paisList}"  itemLabel="descricao" itemValue="id" />
			 					</form:select>
			 				</td>
						</tr>
						<tr class="prop" id="ufLinha">
							<td valign="top" class="name">
								<label for="ufId">
									<spring:message code="evento.uf.label" />
								</label>
							</td>
							<td>
								<form:select path="ufMunicipioId" id="ufId" >
									<option value="0">TODOS</option>
									<form:options items="${ufList}" itemLabel="nome" itemValue="uf" />
								</form:select>
							</td>
						</tr>
						<tr class="prop" id="cidadeLinha">
							<td valign="top" class="name">
								<label for="cidadeId">
									<spring:message code="evento.cidade.label" />
								</label>
							</td>
							<td>
								<form:select path="municipioId" id="cidadeId">
									<option value="0">TODOS</option>
								</form:select>
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="evento_situacao"><spring:message code="evento.situacao.label" /></label></td>
							<td valign="top">
								<form:select path="situacao" id="evento_situacao">
									<form:option value="0">TODOS</form:option>
									<form:options items="${situacaoList}" />
								</form:select>
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="modalidadeId"><spring:message code="evento.modalidade.label" /></label></td>
							<td>
								<form:select path="modalidadeId" id="modalidadeId">
									<form:option value="0">TODOS</form:option>
									<form:options items="${modalidadeList}" itemLabel="descricao" itemValue="id" />
								</form:select>
							</td>
						</tr>
						<tr class="prop" id="tipo_participante">
							<td valign="top" class="name">
								<label><spring:message code="participante.tipoPublicoAlvo.label" /></label>
							</td>
							<td valign="top">
								<fieldset>										   
								   	<div class="divcheck"> 
									    <form:checkbox class="check incluirTipo" path="incluirTipoParticipante1" id="incluirTipoParticipante1"/> 
										<label for="incluirTipoParticipante1"><spring:message code="tipoPublicoAlvo.servidor" /></label>
								   	</div>								   
								    <div class="divcheck">
								    	<form:checkbox class="check incluirTipo" path="incluirTipoParticipante2" id="incluirTipoParticipante2"/> 
										<label for="incluirTipoParticipante2"><spring:message code="tipoPublicoAlvo.jurisdicionado" /></label>
								   	</div>								   
								   	<div class="divcheck">
								    	<form:checkbox class="check incluirTipo" path="incluirTipoParticipante3" id="incluirTipoParticipante3"/> 
										<label for="incluirTipoParticipante3"><spring:message code="tipoPublicoAlvo.sociedade" /></label>
									</div>
									<div class="divcheck">
								    	<form:checkbox class="check incluirTipo" path="incluirTipoParticipante4" id="incluirTipoParticipante4"/> 
										<label for="incluirTipoParticipante4"><spring:message code="tipoPublicoAlvo.membro" /></label>
									</div>
								</fieldset>
							</td>
						</tr>
						<tr class="prop" id="esferaTr">
							<td valign="top" class="name"><label for=""><spring:message	code="administracaoPublica" />:</label></td>
							<td>
								<label>
									<form:radiobutton path="administracaoPublica" value="todas" id="checkTodasEsferas" checked="checked" /> 
									Todas
								</label>
								<label>
									<form:radiobutton path="administracaoPublica" value="estadual" /> 
									Estadual
								</label>
								<label>
									<form:radiobutton path="administracaoPublica"  value="municipal" /> 
									Municipal
								</label>
								<label>
									<form:radiobutton path="administracaoPublica"  value="demais casos" /> 
									Demais Casos
								</label>
							</td>
						</tr>
						<tr>
							<td valign="top" class="name" colspan="2">
								<div class="divcheck">
									<form:checkbox class="check" value="1" id="automatico" path="automatico"/> 
									<label for="automatico">Incluir quantidade de aprovações</label>
								</div>
							</td>
						</tr>
						<tr>
							<td><input id="filtrar" type="submit" class="botao" value="Filtrar" /></td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
		</form:form>
	</div>
</body>
</html>