<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="eventoExtra.label" /></title>
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

<div class="nav">
	<span class="menuButton">
		<a href="<c:url value="/eventoextra/"/>" class="list">
			<spring:message	code="default.button.list.label" />
		</a>
	</span>
</div>

<div class="body">

	<h1><spring:message code="default.add.label" /> - <spring:message code="eventoExtra.label" /></h1>	
	<% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>

	<c:url var="url" value="/eventoextra" /> 

	<form:form action="${url}" method="POST" modelAttribute="eventoextra" enctype="multipart/form-data">
		<div class="dialog">
			<table>
				<tbody>
					<tr class="prop">
						<td valign="top" class="name">
							<label for="curso">
								<spring:message	code="eventoExtra.curso.label" />:
							</label>
						</td>
						<td>
							<form:input cssStyle="width:400px" maxlength="255" path="curso" 
								onkeyup="javascript:this.value=this.value.toUpperCase();" size="30" />* 
							<form:errors path="curso" cssClass="error" />
						</td>						
						<td valign="top" class="name">(*) Campos Obrigatórios</td>
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
								<spring:message code="eventoExtra.material2.label" />:
							</label>
						</td>
						<td>
							<form:input cssStyle="width:250px" maxlength="255" path="material" size="30" type="file"/><span class="material--obrigatorio">* </span>
							<form:errors path="material" cssClass="error" />						
						</td>
					</tr>		
					<tr class="prop">
						<td valign="top" class="name">
							<label for="dataInicio">
								<spring:message code="eventoExtra.dataInicio.label" />:
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
								<spring:message code="eventoExtra.dataFim.label" />:
							</label>
						</td>
						<td>
							<form:input cssStyle="width:90px" maxlength="255" path="dataFim" size="30" alt="date" autocomplete="off"/>* 
							<form:errors path="dataFim" cssClass="error" />
						</td>
					</tr>					
					<tr class="prop">
						<td valign="top" class="name">
							<label for="valor">
								<spring:message	code="eventoExtra.valor2.label" /> (R$):
							</label>
						</td>
						<td><form:input cssStyle="width:90px" maxlength="255" path="valor" size="30" alt="decimal"/></td>
					</tr>					
					<tr class="prop">
						<td valign="top" class="name">
							<label for="cargaHoraria">
								<spring:message code="eventoExtra.cargaHoraria.label" />:
							</label>
						</td>
						<td>
							<form:input cssStyle="width:90px" maxlength="255" path="horario" size="30" alt="integer"/>* 
							<form:errors path="horario" cssClass="error" />
						</td>
					</tr>					
					<tr class="prop">
						<td valign="top" class="name">
							<label for="paisId">
								<spring:message code="eventoExtra.pais.label" />:
							</label>
						</td>		
						<td>
							<form:select path="paisId" id="paisId" >
		 						<form:option value="0">Selecione...</form:option>
		 						<form:options items="${paisList}"  itemLabel="descricao" itemValue="id" />
		 					</form:select>* <form:errors path="paisId" cssClass="error" />
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
								<spring:message code="eventoExtra.turno.label" />:
							</label>
						</td>
						<td><form:select path="turno" items="${turnoList}"  multiple="true"  /></td>
					</tr>
					
					<tr class="prop">
						<td valign="top" class="name">
							<label for="provedor">
								<spring:message code="eventoExtra.provedor.label" />:
							</label>
						</td>
						<td>
							<form:input cssStyle="width:250px" maxlength="255"	path="provedor" onkeyup="javascript:this.value=this.value.toUpperCase();" size="30" />							
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
				</tbody>
			</table>
		</div>
		<div class="buttons">
			<input id="criar" type="submit" class="save" value="<spring:message code="default.add.label" />" />
		</div>		
	</form:form>	
</div>
</body>
</html>
