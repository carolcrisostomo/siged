<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="default.button.edit.label" /> - <spring:message
		code="eventoRecomendar.label" /></title>
<spring:url value="/ajax/procuraMunicipio" var="procuraMunicipioUrl" />
</head>
<body>
	<script type="text/javascript">
		jQuery(document).ready(	function($) {

			updateMunicipioSelected('${procuraMunicipioUrl}', 'ufId', 'cidadeId', '${eventorecomendar.municipio.id}');

			if ($("#paisId").val() == 1) {
				$("#ufLinha").show();
				$("#cidadeLinha").show();
			} else {
				$("#ufId").val(0);
				$("#cidadeId").html('<option value="0">Selecione...</option>').trigger('chosen:updated');
				$("#ufLinha").hide();
				$("#cidadeLinha").hide();
			}

			$("#paisId").change(function() {
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
		<h1>
			<spring:message code="default.button.edit.label" />
			-
			<spring:message code="eventoRecomendar.label" />
		</h1>
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>
		<c:url var="url"
			value="/eventorecomendar/${eventorecomendar.id}/update" />
		<form:form action="${url}" method="POST"
			modelAttribute="eventorecomendar" enctype="multipart/form-data">
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
										code="eventoRecomendar.titulo.label" />:</label></td>
							<td><form:input cssStyle="width:400px" maxlength="255"
									path="titulo"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									size="30" />* <form:errors path="titulo" cssClass="error" />
							</td>
							
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="objetivo"><spring:message
										code="eventoRecomendar.objetivo.label" />:</label></td>

							<td><form:textarea path="objetivo"
									onkeyup="javascript:this.value=this.value.toUpperCase();"
									cols="70" rows="5" /> <form:errors path="objetivo"
									cssClass="error" />*</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="cargaHoraria"><spring:message
										code="eventoRecomendar.cargaHoraria.label" />:</label></td>
							<td><form:input cssStyle="width:90px" maxlength="255"
									path="cargaHoraria" size="30" alt="integer" />* <form:errors
									path="cargaHoraria" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="dataInicio"><spring:message
										code="eventoRecomendar.dataInicio.label" />:</label></td>
							<td><form:input cssStyle="width:90px" maxlength="255"
									path="dataInicio" size="30" alt="date" />* <form:errors
									path="dataInicio" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="dataFim"><spring:message
										code="eventoRecomendar.dataFim.label" />:</label></td>
							<td><form:input cssStyle="width:90px" maxlength="255"
									path="dataFim" size="30" alt="date" />* <form:errors
									path="dataFim" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="paisId"> <spring:message
										code="eventoRecomendar.pais.label" />:
							</label></td>
							<td><form:select path="paisId" id="paisId"
									items="${paisList}" itemLabel="descricao" itemValue="id">
								</form:select></td>
						</tr>
						<tr class="prop" id="ufLinha">
							<td valign="top" class="name"><label for="uf"> <spring:message
										code="eventoRecomendar.uf.label" />:
							</label></td>
							<td><form:select path="ufMunicipio" id="ufId"
									onchange="updateMunicipio('${procuraMunicipioUrl}', 'ufId', 'cidadeId')">
									<option value="0">Selecione...</option>
									<form:options items="${ufList}" itemLabel="nome" itemValue="uf" />
								</form:select>* <form:errors path="ufMunicipio" cssClass="error" /></td>
						</tr>
						<tr class="prop" id="cidadeLinha">
							<td valign="top" class="name"><label for="cidade"> <spring:message
										code="eventoRecomendar.cidade.label" />:
							</label></td>
							<td><form:select path="municipio" id="cidadeId">
									<option value="0">Selecione...</option>
								</form:select>* <form:errors path="municipio" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="material"><spring:message
										code="eventoRecomendar.material2.label" />:</label></td>
							<td><form:input cssStyle="width:250px" maxlength="255"
									path="material" size="30" type="file" /> <a
								href="<c:url value="/eventorecomendar/material/${eventorecomendar.id}"/>"
								target="_blank">${eventorecomendar.materialNome}</a> <form:errors
									path="material" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="valor"><spring:message
										code="eventoRecomendar.valor2.label" /> (R$):</label></td>
							<c:set var="aux"
								value="<fmt:formatNumber value='${eventorecomendar.valor * 100}'/>" />
							<td><form:input cssStyle="width:110px" maxlength="255"
									path="valor" size="30" alt="decimal" value="${aux}" /> <form:errors
									path="valor" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="setor"><spring:message
										code="eventoRecomendar.setor.label" />:</label></td>
							<td><form:select path="setorId" items="${setorList}"
									itemLabel="descricao" itemValue="id" /></td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="observacao"><spring:message
										code="eventoRecomendar.observacao.label" />:</label></td>

							<td><form:textarea path="observacao" cols="70" rows="5" />
								<form:errors path="observacao" cssClass="error" /></td>
						</tr>
						<sec:authorize ifAnyGranted="ROLE_CHEFE">
							<tr class="prop">
								<td valign="top" class="name"><h1>�rea Chefe</h1></td>
								<td></td>
							</tr>

							<tr class="prop">
								<td valign="top" class="name"><label for="aprovado"><spring:message
											code="eventoRecomendar.aprovado.label" />:</label></td>
								<td><form:select path="aprovado" items="${SNList}" /></td>
							</tr>
							<tr class="prop">
								<td valign="top" class="name"><label for="justificativa"><spring:message
											code="eventoRecomendar.justificativa.label" />:</label></td>

								<td><form:textarea path="justificativa" cols="70" rows="5" />
									<form:errors path="justificativa" cssClass="error" /></td>
							</tr>
						</sec:authorize>
						<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
							<tr class="prop">
								<td valign="top" class="name"><h1>�rea Chefe</h1></td>
								<td></td>
							</tr>

							<tr class="prop">
								<td valign="top" class="name"><label for="aprovado"><spring:message
											code="eventoRecomendar.aprovado.label" />:</label></td>
								<td>${eventorecomendar.aprovado} <form:hidden
										path="aprovado" /></td>
							</tr>
							<tr class="prop">
								<td valign="top" class="name"><label for="justificativa"><spring:message
											code="eventoRecomendar.justificativa.label" />:</label></td>

								<td>${eventorecomendar.justificativa} <form:hidden
										path="justificativa" /></td>
							</tr>
							<%-- <tr class="prop">
								<td valign="top" class="name"><label
									for="justificativachefe">Justificativa Chefe:</label></td>
								<td valign="top" class="value">${eventorecomendar.justificativachefe}<form:hidden
										path="justificativachefe" />
								</td>
							</tr> --%>
						</sec:authorize>
					</tbody>
				</table>
			</div>
			
		
		<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
		
				<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/eventorecomendar/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
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
