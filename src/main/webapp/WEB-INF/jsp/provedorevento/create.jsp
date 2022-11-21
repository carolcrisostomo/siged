<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="provedorEvento.label" /></title>
<spring:url value="/ajax/procuraMunicipio" var="procuraMunicipioUrl" />
</head>
<body>
<script type="text/javascript"> 
	jQuery(document).ready(function($){		
		
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
  		
  		$(document).ajaxStop($.unblockUI);
 	});  
</script>

<div class="body">

<h1><spring:message code="default.add.label" /> - <spring:message code="provedorEvento.label" /></h1>
<% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<c:url var="url" value="/provedorevento" /> 

<form:form action="${url}"	method="POST" modelAttribute="provedorevento">
	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name">
					<label for="descricao"><spring:message	code="provedorEvento.descricao.label" />:</label>
				</td>
				<td>
					<form:input cssStyle="width:400px" maxlength="255" path="descricao" onkeyup="javascript:this.value=this.value.toUpperCase();" size="30" />* 
					<form:errors path="descricao" cssClass="error" />
				</td>
				<td valign="top" class="name">(*) Campos Obrigatórios</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name">
					<label for="cnpj"><spring:message code="provedorEvento.cnpj.label" />:</label>
				</td>
				<td>
					<form:input cssStyle="width:130px" maxlength="255" path="cnpj" size="30" alt="cnpj"/>
				</td>
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
			<tr class="prop">
				<td valign="top" class="name">
					<label for="logradouro"> <spring:message code="provedorEvento.logradouro.label" />:</label>
				</td>
				<td>
					<form:input cssStyle="width:400px" maxlength="255" path="logradouro" onkeyup="javascript:this.value=this.value.toUpperCase();" size="30" /> 
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name">
					<label for="numero"><spring:message	code="provedorEvento.numero.label" />:</label>
				</td>
				<td>
					<form:input cssStyle="width:60px" maxlength="255" path="numero" size="30" />
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name">
					<label for="complemento"><spring:message code="provedorEvento.complemento.label" />:</label>
				</td>
				<td>
					<form:input cssStyle="width:250px" maxlength="255" path="complemento" onkeyup="javascript:this.value=this.value.toUpperCase();" size="30" />
				</td>
			</tr>						
			<tr class="prop">
				<td valign="top" class="name">
					<label for="bairro"><spring:message code="provedorEvento.bairro.label" />:</label>
				</td>
				<td>
					<form:input cssStyle="width:250px" maxlength="255" path="bairro" onkeyup="javascript:this.value=this.value.toUpperCase();" size="30" />
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name">
					<label for="cep"><spring:message code="provedorEvento.cep.label" />:</label>
				</td>
				<td>
					<form:input cssStyle="width:80px" maxlength="255" path="cep" size="30" alt="cep"/>
				</td>
			</tr>			
			<tr class="prop">
				<td valign="top" class="name">
					<label for="contato"><spring:message code="provedorEvento.contato.label" />:</label>
				</td>
				<td>
					<form:input cssStyle="width:250px" maxlength="255" path="contato" onkeyup="javascript:this.value=this.value.toUpperCase();" size="30" />
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name">
					<label for="email"><spring:message code="provedorEvento.email.label" />:</label>
				</td>
				<td>
					<form:input cssStyle="width:250px" maxlength="255" path="email" size="30" />
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name">
					<label for="telefone"><spring:message code="provedorEvento.telefone.label" />:</label>
				</td>
				<td>
					<form:input cssStyle="width:110px" maxlength="255" path="telefone" size="30" alt="telefone"/>
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name">
					<label for="celular"><spring:message code="provedorEvento.celular.label" />:</label>
				</td>
				<td>
					<form:input cssStyle="width:110px" maxlength="255" path="celular" size="30" alt="telefone"/>
				</td>
			</tr>
		</tbody>
	</table>
	</div>
	
	
	
		<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/provedorevento/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="criar" type="submit" class="btn btn-primary">
 						<spring:message code="default.add.label" />
					</button>

				</div>
			</div>
	
</form:form></div>
</body>
</html>
