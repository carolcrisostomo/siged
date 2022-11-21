<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="default.button.edit.label" /> - <spring:message code="eventoExtra.label" /></title>
	<spring:url value="/ajax/procuraMunicipio" var="procuraMunicipioUrl" />		
</head>
<body>
<script type="text/javascript">	
	jQuery(document).ready(function($){		
		
		updateMunicipioSelected('${procuraMunicipioUrl}', 'ufId', 'cidadeId', '${eventoextra.municipio.id}');
		
		if ($("#paisId").val() == 1) {
			$("#ufLinha").show();
			$("#cidadeLinha").show();
		} else {
			$("#ufId").val(0);
			$("#cidadeId").html('<option value="0">Selecione...</option>').trigger('chosen:updated');
			$("#ufLinha").hide();
			$("#cidadeLinha").hide();			
		}				
		
		$("#paisId").change(function(){
			if ($("#paisId").val() == 1) {
				$("#ufLinha").show();
				$("#cidadeLinha").show();
			} else {
				$("#ufId").val(0);
				$("#cidadeId").html('<option value="0">Selecione...</option>').trigger('chosen:updated');
				$("#ufLinha").hide();
				$("#cidadeLinha").hide();				
			}
		});
		
		verificarMaterialObrigatorio();
		
		$('#site').change(function() {
			verificarMaterialObrigatorio();
		});
		
		function verificarMaterialObrigatorio() {
			var inputSite = $('#site');
			console.log(inputSite.val());
			if(!inputSite.val()) {
				$('.material--obrigatorio').show();
			} else {
				$('.material--obrigatorio').hide();
			}
		}
		
		$(document).ajaxStop($.unblockUI);		
	});		
</script>



<div class="body">

	<h1><spring:message code="default.button.edit.label" /> - <spring:message code="eventoExtra.label" /></h1>
	<% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
	<c:url var="url" value="/eventoextra/${eventoextra.id}/update" /> 
	
	<form:form action="${url}" method="POST" modelAttribute="eventoextra" enctype="multipart/form-data">
		<div class="dialog">		
		<table>
			<tbody>
				<tr>
					<td></td>
					<td style="text-align:right;" valign="top" class="name">(*) Campos Obrigat�rios</td>
				</tr>		
				<tr class="prop">
					<td valign="top" class="name">
						<label for="solicitanteId">
							<spring:message	code="eventoExtra.solicitante.label" />:
						</label>
					</td>
					<td>
						<c:out value="${eventoextra.solicitanteId}" />
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><label for="setor"><spring:message code="eventoExtra.setor.label" />:</label></td>
					<td valign="top" class="value">${participante.setorId.descricao}</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">
						<label for="curso">
							<spring:message	code="eventoExtra.curso.label" />:
						</label>
					</td>
					<td>
						<form:input cssStyle="width:400px" maxlength="255" path="curso" onkeyup="javascript:this.value=this.value.toUpperCase();" size="30" />* 
						<form:errors path="curso" cssClass="error" />
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">
						<label for="site">
							<spring:message	code="eventoExtra.site.label" />:
						</label>
					</td>
					<td>
						<form:input cssStyle="width:250px" maxlength="255" path="site" size="30" /> 
						<form:errors path="site" cssClass="error" />
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">
						<label for="material">
							<spring:message	code="eventoExtra.material2.label" />:
						</label>
					</td>
					<td>
						<form:input cssStyle="width:250px" maxlength="255"	path="material" size="30" type="file"/><span class="material--obrigatorio">* </span>
						<c:if test="${eventoextra.materialNome != null}">
							<br /><br />
							Arquivo Atual: <a href="<c:url value="/eventoextra/material/${eventoextra.id}"/>" target="_blank">${eventoextra.materialNome}</a>
						</c:if>							
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">
						<label for="dataInicio">
							<spring:message	code="eventoExtra.dataInicio.label" />:
						</label>
					</td>
					<td>
						<form:input cssStyle="width:90px" maxlength="255" path="dataInicio" size="30" alt="date" autocomplete="off"/>* 
						<form:errors path="dataInicio" cssClass="error" />
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">
						<label for="dataFim">
							<spring:message	code="eventoExtra.dataFim.label" />:
						</label>
					</td>
					<td>
						<form:input cssStyle="width:90px" maxlength="255" path="dataFim" size="30" alt="date" autocomplete="off"/>* 
						<form:errors path="dataFim"	cssClass="error" />
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">
						<label for="valor">
							<spring:message code="eventoExtra.valor2.label" /> (R$):
						</label>
					</td>
					<c:set var="aux" value="<fmt:formatNumber value='${eventoextra.valor * 100}'/>" />
					<td>
						<form:input cssStyle="width:90px" maxlength="255" path="valor" size="30" alt="decimal" value="${aux}" />
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">
						<label for="cargaHoraria">
							<spring:message	code="eventoExtra.cargaHoraria.label" />:
						</label>
					</td>
					<td>
						<form:input cssStyle="width:90px" maxlength="255" path="horario" size="30" alt="integer"/>* 
						<form:errors path="horario"	cssClass="error" />
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">
						<label for="paisId">
							<spring:message code="eventoExtra.pais.label" />:
						</label>
					</td>
					<td>
						<form:select path="paisId" id="paisId" items="${paisList}" itemLabel="descricao" itemValue="id"> 							
 						</form:select>
 					</td>
				</tr>
				<tr class="prop" id="ufLinha">						
					<td valign="top" class="name">
						<label for="uf">
							<spring:message code="eventoExtra.uf.label" />:
						</label>
					</td>
					<td>							
						<form:select path="ufMunicipio" id="ufId" onchange="updateMunicipio('${procuraMunicipioUrl}', 'ufId', 'cidadeId')">
							<option value="0">Selecione...</option>
							<form:options items="${ufList}"  itemLabel="nome" itemValue="uf" />
						</form:select>* <form:errors path="ufMunicipio" cssClass="error" />							
					</td>						
				</tr>															
				<tr class="prop" id="cidadeLinha">
					<td valign="top" class="name">
						<label for="cidade">
							<spring:message code="eventoExtra.cidade.label" />:
						</label>
					</td>						
					<td>
						<form:select path="municipio" id="cidadeId">
							<option value="0">Selecione...</option>									
						</form:select>* <form:errors path="municipio" cssClass="error" />
					</td>
				</tr>						
				<tr class="prop">
					<td valign="top" class="name">
						<label for="turno">
							<spring:message	code="eventoExtra.turno.label" />:
						</label>
					</td>
					<td>
						<form:select path="turno" items="${turnoList}" multiple="true" />
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">
						<label for="provedor">
							<spring:message	code="eventoExtra.provedor.label" />:
						</label>
					</td>
					<td>
						<form:input cssStyle="width:250px" maxlength="255"	path="provedor" onkeyup="javascript:this.value=this.value.toUpperCase();" size="30" /> 
						<form:errors path="provedor" cssClass="error" />
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">
						<label for="justificativa">
							<spring:message	code="eventoExtra.justificativa.label" />:
						</label>
					</td>
					<td>
						<form:textarea path="justificativa" cols="70" rows="5" />*
						<form:errors path="justificativa" cssClass="error" />
					</td>
				</tr>			
				<tr class="prop">
					<td valign="top" class="name">
						<label for="dataFim">
							<spring:message code="eventoExtra.indicada.label" />:
						</label>
					</td>
					<td>${eventoextra.indicada}</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">
						<label for="dataFim">
							<spring:message code="eventoExtra.dataIndicacao.label" />:
						</label>
					</td>
					<td>
						<fmt:formatDate pattern="dd/MM/yyyy" value="${eventoextra.dataIndicacao}"/>
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">
						<label for="chefeId">
							<spring:message	code="eventoExtra.chefe.label" />:
						</label>
					</td>
					<td>
						<form:hidden path="indicada" />
						<form:hidden path="dataIndicacao" />
						<sec:authorize ifAnyGranted="ROLE_SERVIDOR">
							${eventoextra.chefeId}
						</sec:authorize>
						<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
							<form:select path="chefeId" id="chefeId" items="${chefeList}" itemLabel="nome" itemValue="id" ></form:select> 
						</sec:authorize>
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">
						<label for="justificativa">Justificativa Chefe:</label>
					</td>
					<td>${eventoextra.justificativachefe}
						<form:hidden path="justificativachefe" />
					</td>
				</tr>
				
				<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
					<tr class="prop">
						<td valign="top" class="name">
							<label for="observacaoIpc"><spring:message code="eventoExtra.observacaoIpc.label" />:</label>
						</td>
						<td>
							<form:textarea path="observacaoIpc" rows="20" cols="5"/>
						</td>
					</tr>
				</sec:authorize>
				
			</tbody>
		</table>
	</div>
	<form:hidden path="dataCadastro" />
	<input type="hidden" name="_method" value="PUT"/>
	<
		
		<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
		
				<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/eventoextra/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="atualizar" type="submit" class="btn btn-primary">
 						<spring:message code="default.button.save.label" />
					</button>

				</div>
			</div>
		</sec:authorize>
	<form:hidden path="id" />
</form:form>
</div>
</body>
</html>
