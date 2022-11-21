<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
<spring:url value="/ajax/procuraParticipantePorNome" var="procuraParticipantePorNomeUrl" />
</head>
<body>
	<div class="body">
		<c:if test="${param.mensagem != null}">
			<div class="message">${param.mensagem}</div>
		</c:if>
		<div class="messageErro" style="display:none"></div>
		<h1>Relatório de Pré-Inscrições</h1>
		<c:if test="${mensagemRel != null}">
			<div class="message" id="msgId">
				<c:out value="${mensagemRel}" />
			</div>
		</c:if>
		<c:url var="url" value="/relatorio/inscricoes/" />
		<form:form action="${url}" method="POST"
			modelAttribute="relInscricoes" id="form1">
			<div class="filter">
				<table>
					<tbody>
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">
							(*) Pelo menos um destes campos ou um agrupamento é obrigatório.
							</td>
						</tr>
						<!-- EVENTO COMBO -->
						<tr class="prop">
							<td valign="top" class="name"><label for="eventoid"><spring:message	code="inscricao.evento.label" /></label></td>
							<td valign="top"><form:select path="eventoId" id="eventoid">
									<form:option value="0">TODOS</form:option>
									<form:options items="${eventoList}" itemLabel="nome"
										itemValue="id" />
								</form:select> * <form:errors path="eventoId" cssClass="error" /></td>
						</tr>
						
						<!-- TIPO EVENTO COMBO -->
						<tr class="prop">
							<td valign="top" class="name"><label for=evento_tipo_id><spring:message code="evento.tipoEvento.label" /></label></td>
							<td valign="top">
								<form:select path="tipoEventoId" id="evento_tipo_id">
									<form:option value="0">TODOS</form:option>
									<form:options items="${tipoEventoList}" itemLabel="descricao" itemValue="id" />
								</form:select>
							</td>
						</tr>
						
						<!-- TIPO PARTICIPANTE COMBO -->
						<tr class="prop">
							<td valign="top" class="name"><label for="tipoParticipanteId"><spring:message
										code="participante.tipoPublicoAlvo.label" /></label></td>
							<td valign="top"><form:select path="tipoParticipanteId" id="tipoParticipanteId">
									<form:option value="0">TODOS</form:option>
									<form:options items="${tipoPublicoAlvoList}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>
						
						<!-- ESFERA RADIOS -->
						<tr class="prop" id="esferaTr">
							<td valign="top" class="name"><label for=""><spring:message	code="administracaoPublica" />:</label></td>
							<td>
								<label>
									<form:radiobutton path="administracaoPublica" value="todas" id="checkTodasEsferas"  /> 
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

						<!-- PARTICIPANTE COMBO -->
						<tr class="prop">
                    		<td valign="top" class="name">
                      			<label for="participanteId"><spring:message code="evento.participante.label" /></label>
                    		</td>
                    		<td valign="top" class="value">
                    			
                    			<form:input	cssStyle="width:90px" maxlength="255" path="nomeParticipante"/>
								
								<input id="buscarParticipante" class="search" type="button" />
                      			
                      			<form:select path="participanteId" data-participante-url="${procuraParticipantePorNomeUrl}" cssStyle="width:450px">
					  				<form:option value="0">TODOS</form:option>                      				
                      			</form:select> * <span id="participanteErro" class="error"></span>
                    		</td>
                 		</tr>
                 		
                 		<!-- PROGRAMA COMBO -->
                 		<tr class="prop">
                 			<td valign="top" class="name">
                 				<label for="programaId"><spring:message code="programa.label" /></label>
                 			</td>
                 			<td valign="top" class="value">
                 				<form:select path="programaId" id="programaId">
                 					<form:option value="0">TODOS</form:option>
                 					<form:options items="${programaList}" itemLabel="descricao" itemValue="id" />
                 				</form:select>
                 			</td>
                 		</tr>

						<!-- INDICADA COMBO -->
						<tr class="prop">
							<td valign="top" class="name">
								<label for="indicadaid"><spring:message code="inscricao.indicada.label" /></label>
							</td>
							<td valign="top" class="value">
								<form:select path="indicada" id="indicadaid">
									<form:option value="0">TODOS</form:option>
									<form:option value="-">-</form:option>
									<form:options items="${SNList}" />
								</form:select>
							</td>
						</tr>

						<!-- CONFIRMADA COMBO -->
						<tr class="prop">
							<td valign="top" class="name"><label for="confirmadaid"><spring:message
										code="inscricao.confirmada.label" /></label></td>
							<td valign="top" class="value"><form:select
									path="confirmada" id="confirmadaid">
									<form:option value="0">TODOS</form:option>
									<form:option value="-">-</form:option>
									<form:options items="${SNList}" />
								</form:select></td>
						</tr>

						<!-- RESP.INDICAÇÃO COMBO -->
						<tr class="prop">
							<td valign="top" class="name"><label for="responsavelPelaIndicacaoId">
							<spring:message code="inscricao.chefe.label" /></label></td>
							<td valign="top">
								<form:select path="responsavelPelaIndicacaoId" id="responsavelPelaIndicacaoId">
									<form:option value="0">TODOS</form:option>
									<form:options items="${chefeList}" itemLabel="nome" itemValue="id" />
								</form:select></td>
						</tr>

						<!-- PERÍODO -->
						<tr class="prop">
							<td valign="top" class="name"><label for="periodoprevisto">Período Pré-inscrição</label></td>
							<td valign="top">
								<form:input path="dataPreInscricao1"
									id="dataPreInscricao1" alt="date"/> a 
								<form:input path="dataPreInscricao2" 
									id="dataPreInscricao2" alt="date" /> * 
								<form:errors path="dataPreInscricao1" cssClass="error" />
								<form:errors path="dataPreInscricao2" cssClass="error" />
							</td>
						</tr>
						
						<!-- AGRUPAR POR -->
						<tr>
							<td valign="top" class="name"><label>Agrupar</label></td>
							<td valign="top">
								<label><form:radiobutton path="agruparPor" value="0" checked='checked' id="agruparDefaultId"/> Não agrupar </label>
								<label><form:radiobutton path="agruparPor" value="1"/> Agrupar por Participante </label>
								<label><form:radiobutton path="agruparPor" value="2" id="agruparSetorTCEId"/> Agrupar por setor TCE-CE </label>
								<label><form:radiobutton path="agruparPor" value="3" id="agruparOrgaoId"/> Agrupar por órgão</label><br/><br/>
								<label><form:radiobutton path="agruparPor" value="4" id="agruparProgramaId"/> Agrupar por programa</label>
								<label>*</label>
								<form:errors path="agruparPor" cssClass="error" />
							</td>
						</tr>

						<tr>
							<td><input id="filtrar" type="submit" class="botao"
								value="Filtrar" /></td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
		</form:form>
	</div>
	<script type="text/javascript" src="<c:url value="/static/js/ajax.loading.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/participante.consulta.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/relatorio.inscricao.js"/>"></script>	
</body>
</html>