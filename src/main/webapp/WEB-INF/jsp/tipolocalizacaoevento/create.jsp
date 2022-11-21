<%@ include file="../includes.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="tipoLocalizacaoEvento.label" /></title>
<spring:url value="/tipolocalizacaoevento/procuraEstado" var="procuraEstadoUrl" />
<spring:url value="/ajax/procuraMunicipio" var="procuraCidadeUrl" />
<spring:url value="/ajax/procuraMunicipio" var="procuraMunicipioUrl" />


<body>

<script type="text/javascript"> 
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
    			$("#cidadeId").html('0').trigger('chosen:updated');
    			$("#ufLinha").hide();
    			$("#cidadeLinha").hide(); 
    			$("#cidadePaisLinha").show();
   			}
  		});
  		
  		$(document).ajaxStop($.unblockUI);
 	});  
</script>

<div class="body">	
	
<h1><spring:message code="default.add.label" /> - <spring:message code="tipoLocalizacaoEvento.label" /></h1>
<% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>

<c:url var="url" value="/tipolocalizacaoevento" /> 

<form:form action="${url}" method="POST" modelAttribute="tipolocalizacaoevento">
	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name">
					<label for="descricao"><spring:message	code="tipoLocalizacaoEvento.descricao.label" />:</label>
				</td>
				<td>
					<form:input cssStyle="width:400px" maxlength="100" path="descricao" onkeyup="javascript:this.value=this.value.toUpperCase();" size="100" />* 
					<form:errors path="descricao" cssClass="error" />
				</td>
				<td valign="top" class="name">(*) Campos Obrigatórios</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name">
					<label for="endereco"><spring:message code="tipoLocalizacaoEvento.endereco.label" />:</label>
				</td>
				<td>
					<form:input cssStyle="width:400px" maxlength="100" path="endereco" onkeyup="javascript:this.value=this.value.toUpperCase();" size="100" />
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name">
					<label for="paisId"><spring:message	code="tipoLocalizacaoEvento.pais.label" />:</label>
				</td>
				<td>
					<form:select path="paisId" id="paisId">
 					    <form:options items="${listaPaises}" itemLabel="descricao" itemValue="id" />
					</form:select>* <form:errors path="paisId" cssClass="error" />
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
			<!-- Caso o País for diferente de Brasil inserir cidade manualmente-->
			<tr class="prop" id="cidadePaisLinha">
				<td valign="top" class="">
					<label for="cidadeId"><spring:message code="tipoLocalizacaoEvento.cidade.label" />:</label>
				</td>
				<td>
					<form:input cssStyle="width:400px" maxlength="100" path="cidadePais" onkeyup="javascript:this.value=this.value.toUpperCase();" size="100" />*
					<form:errors path="cidadePais" cssClass="error" />
				</td>
			</tr>
			
			<tr class="prop" id="tiposLocalizacaoModalidadesLinha">
				<td valign="top" class="name">
					<label for="tipoLocalizacaoModalidade"><spring:message code="tipoLocalizacaoEvento.tipoLocalizacaoModalidade.label" />:</label>
				</td>
				<td>
					
					<form:select path="tipoLocalizacaoModalidade" id="tipoLocalizacaoModalidade" >
						<c:forEach var="tipoLocalizacaoModalidade" items="${tiposLocalizacaoModalidades}">
							<form:option value="${tipoLocalizacaoModalidade}">${tipoLocalizacaoModalidade.label}</form:option>
						</c:forEach>
					</form:select>* <form:errors path="tipoLocalizacaoModalidade" cssClass="error" />
				</td>
			</tr>
		</tbody>
	</table>
	</div>
	
	
	<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/tipolocalizacaoevento/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="criar" type="submit" class="btn btn-primary">
 						<spring:message code="default.add.label" />
					</button>

				</div>
			</div>
			
			

</form:form></div>
</body>

