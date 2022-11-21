<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="default.button.edit.label" /> - <spring:message code="gasto.label" /></title>
<spring:url value="/gasto/procuraServidoresEMembros" var="procuraParticipanteUrl" />
<spring:url value="/gasto/procuraInstrutoresPorTipoEEvento" var="procuraInstrutoresUrl" />
</head>
<body>
<div class="body">
	
	<% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
	<h1><spring:message code="default.button.edit.label" /> - <spring:message code="gasto.label" /></h1>
	
	<c:url var="url" value="/gasto/${gasto.id}" /> <form:form action="${url}" method="PUT" modelAttribute="gasto">
	
	<div class="dialog">
	
	<table>
		<tbody>
			
			<tr>
				<td></td>
				<td style="text-align:right;" valign="top" class="name">(*) Campos Obrigat�rios</td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="evento"><spring:message
					code="gasto.evento.label" />:</label></td>
				<td>
					<form:select path="eventoId" id="eventoId" onchange="updateParticipanteGasto('${procuraParticipanteUrl}', 'eventoId', 'participanteId')">
 						<form:option value="0">Selecione...</form:option> 						
 						<form:options items="${eventoList}" itemLabel="nome" itemValue="id" /> 						
 					</form:select>* <br />
 					<form:errors path="eventoId" cssClass="error" />
				</td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="tipoGasto"><spring:message
					code="gasto.tipoGasto.label" />:</label></td>
				<td>
					<form:select path="tipoId" id="tipoId">
						<form:option value="0">Selecione...</form:option>
						<form:options items="${tipoGastoList}" itemValue="id" itemLabel="descricao" />
					</form:select>*
					<form:errors path="tipoId" cssClass="error" />
				</td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="valor"><spring:message
					code="gasto.valor.label" />:</label>
				</td>
				
				<td><form:input cssStyle="width:110px" maxlength="255"
					path="valor" size="30" alt="decimal" value="${valorFormatado}" />* <form:errors path="valor"
					cssClass="error" />
				</td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="fonteGasto"><spring:message
					code="gasto.fonteGasto.label" />:</label></td>
				<td>
					<form:select path="fonteId">
						<form:option value="0">Selecione...</form:option>
						<form:options items="${fonteGastoList }" itemValue="id" itemLabel="descricao" />
					</form:select>*
					<form:errors path="fonteId" cssClass="error" />
				</td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="numeroEmpenho"><spring:message
					code="gasto.numeroEmpenho.label" />:</label></td>
				<td><form:input cssStyle="width:150px" maxlength="255"
					path="numeroEmpenho" size="30" /> <form:errors
					path="numeroEmpenho" cssClass="error" /></td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="dataEmpenho"><spring:message
					code="gasto.dataEmpenho.label" />:</label></td>
				<td><form:input cssStyle="width:90px" maxlength="255"
					path="dataEmpenho" size="30" alt="date"/> <form:errors path="dataEmpenho"
					cssClass="error" /></td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="numeroProcesso"><spring:message
					code="gasto.numeroProcesso.label" />:</label></td>
				<td><form:input cssStyle="width:150px" maxlength="255"
					path="numeroProcesso" size="30" />* <form:errors
					path="numeroProcesso" cssClass="error" /></td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="participante"><spring:message
					code="gasto.participante.label" />:</label></td>				 
				<td>
					<form:select path="participanteId">
				  		<form:option value="0">N�o se Aplica</form:option>
                     	<form:options items="${listaParticipante}" itemLabel="nomeCpf" itemValue="id" />
                     </form:select>
                     <form:errors path="participanteId" cssClass="error" />
                </td>							 
			</tr>
			
			<tr class="prop">
			    <td valign="top" class="name">
			        <label for="instrutores"><spring:message code="gasto.instrutor.label" />:</label>
			    </td>
			    <td>
			        <form:select path="instrutor" id="instrutorId" itemLabel="nome" itemValue="id" disabled="${disableSelectInstrutor}"
			        	data-interno="81"
			        	data-externo="82"
			        	data-url="${procuraInstrutoresUrl}"
			        	data-selected="${selected}">
			        	<form:option value="0">N�o se Aplica</form:option>
			        	<form:options items="${listaInstrutores}" itemLabel="nome" itemValue="id" />
			        </form:select>
			        <div class="spinnerLoad">
			    		<img src="/static/images/spinner.gif" />
			    	</div>
			        
			        <form:errors path="instrutor" cssClass="error" />
			    </td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="observacao"><spring:message
					code="gasto.observacao.label" />:</label></td>
				<td><form:textarea path="observacao" cols="70" rows="5" /> <form:errors
					path="observacao" cssClass="error" /></td>
			</tr>

		</tbody>
	</table>
	</div>
	
		
		<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
		
				<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/gasto/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
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
<script type="text/javascript" src="<c:url value="/static/js/gasto.selectInstrutor.js"/>"></script>
</body>
</html>
