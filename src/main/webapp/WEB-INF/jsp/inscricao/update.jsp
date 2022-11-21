<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="default.button.edit.label" /> - <spring:message code="inscricao.label" /></title>
</head>
<body>
	<script type="text/javascript">	
		jQuery(document).ready(function($){
			
			if($("#confirmada").val() == "N") {
				$("#justificar").show();
			} else {
				$("#justificar").hide();
			}
			
			$("#confirmada").change(function() {
				
				if($("#confirmada").val() == "N") {
					$("#justificar").show();
				}else{
					$("#justificar").hide();
				}
				
				if($("#confirmada").val() == "S") {
					if("${totalDeVagas < totalDeInscritos + 1}" == "true"){
						alert("J� existem ${totalDeInscritos} inscri��es confirmadas para ${totalDeVagas} vagas ofertadas.");
					}
				}
			});
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
			<spring:message code="inscricao.label" />
		</h1>
		<c:url var="url" value="/inscricao/${inscricao.id}" />
		<form:form action="${url}" method="PUT" modelAttribute="inscricao">
			<div class="dialog">
				<table>
					<tbody>
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">(*) Campos Obrigat�rios</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="evento"><spring:message
										code="inscricao.evento.label" />:</label></td>
							<td>${inscricao.eventoId}</td>
							<form:hidden path="eventoId" />							
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="participante"><spring:message
										code="inscricao.participante.label" />:</label></td>
							<td>${inscricao.participanteId}</td>
							<form:hidden path="participanteId" />
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="email"><spring:message
										code="participante.email.label" />:</label></td>
							<td valign="top" class="value">${inscricao.participanteId.email}
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="tipoId">Tipo
									Participante:</label></td>
							<td valign="top" class="value">${inscricao.participanteId.tipoId}
							</td>
						</tr>
						<c:if test="${inscricao.participanteId.tipoId.id eq 3}">
							<tr class="prop">
								<td valign="top" class="name"><label for="entidade"><spring:message code="participante.entidade.label" />:</label>
								</td>
								<td valign="top" class="value">${inscricao.participanteId.entidade}
								</td>
							</tr>
							<tr class="prop">
								<td valign="top" class="name"><label for="profissao"><spring:message code="participante.profissao.label" />:</label>
								</td>
								<td valign="top" class="value">${inscricao.participanteId.profissao}
								</td>
							</tr>
						</c:if>
						<c:if test="${inscricao.participanteId.tipoId.id eq 1 or inscricao.participanteId.tipoId.id eq 4}">
							<tr class="prop">
								<td valign="top" class="name"><label for="setor"><spring:message
											code="participante.setor.label" />:</label></td>
								<td valign="top" class="value">${inscricao.participanteId.setorId}
								</td>
							</tr>
						</c:if>
						<c:if test="${inscricao.participanteId.tipoId.id eq 2}">
							<tr class="prop">
								<td valign="top" class="name"><label for="orgaoId">�rg�o:</label>
								</td>
								<td valign="top" class="value">${inscricao.participanteId.orgaoId.dsentidade}
								</td>
							</tr>
							<tr class="prop">
								<td valign="top" class="name"><label for="lotacao"><spring:message code="participante.lotacao.label" />:</label>
								</td>
								<td valign="top" class="value">${inscricao.participanteId.lotacao}
								</td>
							</tr>
						</c:if>
						<tr class="prop">
							<td valign="top" class="name"><label for="data"><spring:message
										code="inscricao.data.label" />:</label></td>
							<td><fmt:formatDate pattern="dd/MM/yyyy"
									value="${inscricao.data}" /></td>
							<form:hidden path="data" />
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="confirmada"><spring:message
										code="inscricao.justificativaparticipante.label" />:</label></td>
							<td valign="top" class="value">${inscricao.justificativaParticipante}
							</td>
						</tr>
						<c:if test="${inscricao.participanteId.tipoId.id eq 1 or inscricao.participanteId.tipoId.id eq 4}">
							<tr class="prop">
								<td valign="top" class="name"><label for="indicada"><spring:message
											code="inscricao.indicada.label" />:</label></td>
								<td>${inscricao.indicada}</td>
								<form:hidden path="indicada" />
							</tr>
							<tr class="prop">
								<td valign="top" class="name"><label for="dataIndicacao"><spring:message
											code="inscricao.dataIndicacao.label" />:</label></td>
								<td><fmt:formatDate pattern="dd/MM/yyyy"
										value="${inscricao.dataIndicacao}" /></td>
								<form:hidden path="dataIndicacao" />
							</tr>							
							<tr class="prop">
								<td valign="top" class="name"><label for="confirmada"><spring:message
											code="inscricao.chefe.label" />:</label></td>
								<td><c:if test="${inscricao.indicada eq 'N'}">
										<form:select path="chefeId" >											
											<form:option value="0">Selecione...</form:option>											
											<form:options items="${chefeList}" itemValue="id" itemLabel="nome"/>
										</form:select> 
									</c:if>
									<c:if test="${inscricao.indicada eq 'S'}">
										${inscricao.chefeId}
									</c:if>
								</td>
							</tr>						
							<tr class="prop">
								<td valign="top" class="name"><label for=justificativachefe><spring:message
											code="inscricao.justchefe.label" />:</label></td>
								<td>${inscricao.justificativachefe}</td>
								<form:hidden path="justificativachefe" />
							</tr>

						</c:if>
						<tr class="prop">
							<td valign="top" class="name">
								<label for="confirmada"><spring:message code="inscricao.confirmada.label" />:</label>
							</td>
							<td>
								<form:select path="confirmada" items="${SNList}" />*
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label><spring:message code="inscricao.dataConfirmacao.label" />:</label></td>
							<td><fmt:formatDate pattern="dd/MM/yyyy" value="${inscricao.dataConfirmacao}" /></td>
							<form:hidden path="dataConfirmacao" />
						</tr>					
						<tr class="prop" id="justificar">
			      			<td valign="top" class="name">
			       			<label for="justificativanaoconfirmacao">
			        			<spring:message code="inscricao.justificativanaoconfirmacao.label" />:
			       			</label>
			      			</td>  
			      			<td>
			       				<form:textarea rows="5" cols="70" path="justificativanaoconfirmacao" maxlength="2000"/>*
			       				<form:errors path="justificativanaoconfirmacao" cssClass="error" />
			     			</td>
     					</tr>     							
						<tr class="prop">
							<td valign="top" class="name"><label for="justificativa"><spring:message
										code="inscricao.justificativa.label" />:</label></td>

							<td><form:textarea path="justificativa" cols="70" rows="5" maxlength="2000" /></td>
						</tr>						
					</tbody>
				</table>
			</div>
			
		
		<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
		
				<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/inscricao/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
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