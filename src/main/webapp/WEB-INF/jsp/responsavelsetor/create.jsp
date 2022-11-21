<%@ include file="../includes.jsp"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="responsavelSetor.label" /></title>
<spring:url value="/responsavelsetor/procuraResponsavelSetorAtivo" var="procuraResponsavelSetorAtivoUrl" />
<spring:url value="/responsavelsetor/procuraUsuariosBySetor" var="procuraUsuariosBySetorUrl" />
</head>
<body>

	<script type="text/javascript">
		jQuery(document).ready(function($){			
			
			function atualizaCombos(){				
				if($('#setor').val() != 0)
					updateResponsavel('${procuraUsuariosBySetorUrl}','${procuraResponsavelSetorAtivoUrl}','setor', 'responsavel','responsavelImediato');			
			}
			
			atualizaCombos();
			
			$('#setor').change(function(){
				atualizaCombos();
			});
		});	
	</script>

	<div class="body">
		
		<%
			if (request.getParameter("mensagemErro") != null) {
		%><div class="messageErro"><%=request.getParameter("mensagemErro")%></div>
		<%
			}
		%>
		
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>
		
		<h1>
			<spring:message code="default.add.label" />
			-
			<spring:message code="responsavelSetor.label" />
		</h1>

		<c:url var="url" value="/responsavelsetor" />
		
		<form:form action="${url}" method="POST" modelAttribute="responsavelSetor">
			
			<div class="dialog">
				<table>
					<tbody>
						<tr class="prop">
							<td valign="top" class="name"></td>
							<td style="text-align: right;">(*) Campos Obrigatórios</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="evento"><spring:message
										code="responsavelSetor.setor.descricao.label" />:</label></td>

							<td><form:select path="setor">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${setorList}" itemLabel="descricao" itemValue="id" />
								</form:select>* <form:errors path="setor" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="evento"><spring:message
										code="responsavelSetor.responsavel.nome.label" />:</label></td>

							<td><form:select path="responsavel">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${responsavel}" itemLabel="nome" itemValue="id" />
								</form:select>* <form:errors path="responsavel" cssClass="error" /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="evento"><spring:message
										code="responsavelSetor.responsavelImediato.nome.label" />:</label></td>

							<td><form:select path="responsavelImediato">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${responsavelImediato}" itemLabel="nome" itemValue="id" />
								</form:select> <form:errors path="responsavelImediato" cssClass="error" /></td>
						</tr>
					</tbody>
				</table>
			</div>
			
	<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/responsavelsetor/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>
					<button  id="criar" type="submit" class="btn btn-primary">
 						<spring:message code="default.add.label" />
					</button>

				</div>
			</div>
			
		</form:form>
	</div>
</body>
</html>
