<%@ include file="../includes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="evento.label" /></title>
<spring:url value="/avaliacaoreacao/formaluno/" var="avaliacaoReacaoAluno" />
<spring:url value="/certificado/impressao" var="certificadoUrl" />
<spring:url value="/evento/meuseventos" var="meusEventosUrl" />
</head>
<body>
	<script type="text/javascript">
		jQuery(document).ready(function($) {
			var meuseventos = new SIGED.MeusEventos();
			meuseventos.iniciar();
			
			if('${sessionScope.certificado_path}' != null && '${sessionScope.certificado_path}' != '') {
		        // $("#iframe").attr("src", '${certificadoUrl}${sessionScope.certificado_path}');
				window.location='${certificadoUrl}${sessionScope.certificado_path}';
			}
			
			var avaliacaoReacaoPendente = new SIGED.AvaliacaoReacaoPendente();
			avaliacaoReacaoPendente.autobind();
		});
	</script>
	<iframe id="iframe" style="display:none;"></iframe>
	<div class="body">
		<h1>
			<spring:message code="evento.meus.label" />
		</h1>		
		
		<div class="message info">Para a emissão do certificado é necessária a aprovação do participante.</div>
		<div class="message info">Para eventos de mais de um módulo, a nota e frequência são as médias dos módulos.</div>
		
		<c:if test="${param.mensagemSucesso != null}">
			<div class="messageSucesso">${param.mensagemSucesso}</div>
		</c:if>
		<c:if test="${param.mensagemErro != null}">
			<div class="messageErro">${param.mensagemErro}</div>
		</c:if>
		<c:if test="${param.mensagem != null}">
			<div class="message info">${param.mensagem}</div>
		</c:if>
		
		<spring:message code="evento.titulo.label" var="coluna1" />
		<spring:message code="evento.dataInicioRealizacao.label" var="coluna2" />
		<spring:message code="evento.dataFimRealizacao.label" var="coluna3" />

		<div class="filter">
			<c:url var="url" value="/evento/meuseventos" />

			<form:form action="${url}" method="GET" modelAttribute="eventofiltro"
				id="form">
				<table>
					<tbody>
						<tr>
							<td><label for="titulo">Título</label></td>
							<td valign="top" class="value"><form:input
									cssStyle="width:250px" id="titulo" maxlength="255"
									path="titulo" size="30"
									onkeyup="this.value=this.value.toUpperCase()" /></td>
						</tr>

						<tr>
							<td><label for="realizacao">Período de Realização</label></td>
							<td valign="top" class="value"><form:input
									cssStyle="width:90px" id="realizacao1" maxlength="255"
									path="realizacao1" size="30" alt="date" /> a <form:input
									cssStyle="width:90px" id="realizacao2" maxlength="255"
									path="realizacao2" size="30" alt="date" /></td>
						</tr>
						<tr>
							<td><label for="tipoId"><spring:message
										code="tipoEvento.label" /></label></td>
							<td valign="top" class="value"><form:select
									path="tipoEvento">
									<form:option value="0">TODOS</form:option>
									<form:options items="${tipoEventoList}" itemLabel="descricao"
										itemValue="id" />
								</form:select></td>
						</tr>
						<tr>
							<td><label for="provedorId"><spring:message
										code="evento.provedorEvento.label" /></label></td>
							<td valign="top" class="value"><form:select
									path="provedorId">
									<form:option value="0">TODOS</form:option>
									<form:options items="${provedorEventoList}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>
						<tr>
							<td></td>
							<td valign="top" class="value">
								<form:input type="checkbox" path="comoInstrutor" />
								<label class="instrutorLabel" for="comoInstrutor" style="cursor: pointer ">Como Instrutor</label>

								<form:input type="checkbox" path="semAcoes" style="margin-left: 15px;"/>
								<label class="semAcoesLabel" for="semAcoes" style="cursor: pointer">Exibir Carga horária, Modalidade e Provedores</label>
							</td>		
						</tr>
						
						<tr>
							<td><input id="filtrar" type="submit" class="botao"
								value="Filtrar" /></td>
							<td valign="bottom">
								
							</td>
						</tr>

					</tbody>


				</table>
			</form:form>
		</div>
		
		<c:if test="${eventosComoParticipante}">
			<div class="list" id="comoPart">
				<display:table uid="evento" name="${eventos}" sort="external" partialList="true" size="${eventos.fullListSize}"
					requestURI="${meusEventosUrl}">
					<c:url var="url" value="/evento/${evento.id}" />
					<c:url var="url2"
						value="/certificado/impressao/${evento.id}/${participante.id}" />
					<c:url var="url3"
						value="/certificadoDeTerceiros/arquivo/${evento.id}/${participante.id}" />
					<c:url var="urlAvaliacaoPendenteAjax"
						value="/avaliacaoreacao/avaliacoesPendentes/${participante.id}/${evento.id}" />
	
					<display:column property="nome" title="${coluna1}" sortable="true" sortProperty="titulo"
						maxLength="80" />
	
					<display:column property="dataInicioRealizacao" sortable="true" sortProperty="dataInicioRealizacao"
						title="${coluna2}" maxLength="50" format="{0,date,dd/MM/yyyy}" />
	
					<display:column property="dataFimRealizacao" sortable="true" sortProperty="dataFimRealizacao"
						title="${coluna3}" maxLength="50" format="{0,date,dd/MM/yyyy}" />
						
					<display:column property="notaParticipanteNoEvento"
						style="text-align : center;" title="Nota" maxLength="50"/>
					<display:column property="frequenciaParticipanteNoEvento"
						style="text-align : center;" title="Freq(%)" maxLength="50"/>
	
					<display:column property="aprovado" sortable="false"
						style="text-align : center;" title="Aprovado" maxLength="80" />
	
					<display:column class="crudlist" title="Certificado"
						style="text-align : center;">
	
						<jsp:useBean id="agora" class="java.util.Date" />
	
						<c:if
							test="${evento.permiteCertificado == 'S' && evento.dataFimRealizacao lt agora && evento.id != 481}">
	
							<c:if
								test="${(evento.temProvedoresCertificado() || evento.certificadoPersonalizado) && evento.aprovado == 'S'}">
								<form:form action="${url2}" method="GET" id="form_emissao_certificado_${evento.id}" >
									<input alt="Permitido" class="input_emissao_certificado"
										data-evento="${evento.id}"
										data-participante="${participante.id}"
										data-urlAjax="${urlAvaliacaoPendenteAjax}"
										data-urlAvaliacao="${avaliacaoReacaoAluno}"
										src="<c:url value="/static/images/tick.png"/>"
										title="Emitir Certificado (versão impressa com conteúdo programático no verso)" type="image" value="Permitido" />
								</form:form>
							</c:if>
							<c:if
								test="${(!evento.temProvedoresCertificado() && !evento.certificadoPersonalizado) && 
										(evento.aprovado != 'N' && evento.hasCertificadoTerceiro(participante))}">
								<form:form action="${url3}" method="GET">
									<input alt="Permitido"
										src="<c:url value="/static/images/tickazul.png"/>"
										title="Visualizar Certificado de Terceiro" type="image"
										value="Permitido" />
								</form:form>
							</c:if>
	
						</c:if>
	
					</display:column>
	
					<display:column class="crudlist" style="text-align : center;" title="Desempenho">
	
						<c:if test="${evento.aprovado != '-'}">
							<a
								href="<c:url value="/evento/desempenhos/${evento.id}/?n=${random}"/>"
								title="Desempenho"
								onclick="Modalbox.show(this.href, {title: this.title, width: 810, height: 500}); return false;">
								<img width="22px" height="22px"
								src="<c:url value="/static/images/icon_desempenho.png"/>"
								id="icon_desempenho" />
							</a>
						</c:if>
					</display:column>
	
					<display:column class="crudlist" style="text-align : center;" title="Exibir">
						<form:form action="${url}" method="GET">
							<input alt="<spring:message code="default.button.show.label" />"
								src="<c:url value="/static/images/show.png"/>"
								title="<spring:message code="default.button.show.label" />"
								type="image"
								value="<spring:message code="default.button.show.label" />" />
						</form:form>
					</display:column>
	
					<display:column class="crudlist" style="text-align : center;" title="Material Didático">
						<c:if test="${evento.eventoComMaterialDidatico}">
							<a
								href="<c:url value="/material/didatico/${evento.id}/?n=${random}"/>"
								title="Material Didático"
								onclick="Modalbox.show(this.href, {title: this.title, width: 810, height: 300}); return false;">
								<img width="22px" height="22px"
								src="<c:url value="/static/images/icon_material.png"/>"
								id="icon_material" />
							</a>
						</c:if>
					</display:column>
	
				</display:table>
	
			</div>
			
			<div class="list" id="comoPartSemAcoes" style="display: none">
				<display:table uid="evento" name="${eventos}" sort="external" partialList="true" size="${eventos.fullListSize}"
					requestURI="${meusEventosUrl}">
					<display:column property="nome" title="${coluna1}" sortable="true" sortProperty="titulo"
						maxLength="80" />
	
					<display:column property="dataInicioRealizacao" sortable="true" sortProperty="dataInicioRealizacao"
						title="${coluna2}" maxLength="50" format="{0,date,dd/MM/yyyy}" />
	
					<display:column property="dataFimRealizacao" sortable="true" sortProperty="dataFimRealizacao"
						title="${coluna3}" maxLength="50" format="{0,date,dd/MM/yyyy}" />
						
					<display:column property="cargaHoraria"
						style="text-align : center;" title="Carga horária" maxLength="50"/>
					
					<display:column property="modalidadeId.descricao" 
						style="text-align : center;" title="Modalidade" maxLength="50"/>
						
					<display:column property="provedoresLista" 
						style="text-align : left;" title="Provedores" maxLength="50"/>
						
					<display:column property="notaParticipanteNoEvento" 
						style="text-align : center;" title="Nota" maxLength="50"/>
						
					<display:column property="frequenciaParticipanteNoEvento" 
						style="text-align : center;" title="Freq(%)" maxLength="50"/>
	
					<display:column property="aprovado"
						style="text-align : center;" title="Aprovado" maxLength="80" />
	
				</display:table>
	
			</div>
		
		</c:if>
		
		<c:if test="${!eventosComoParticipante}">
			<div class="list" id="comoInstr">
				<display:table uid="evento" name="${eventos}" sort="external" partialList="true" size="${eventos.fullListSize}"
					requestURI="${meusEventosUrl}">
					<c:url var="url" value="/evento/${evento.id}" />
					<c:url var="url2" value="/certificado/impressaoInstrutorLogado/${evento.id}" />
					<display:column property="nome" title="${coluna1}" sortable="true" sortProperty="titulo"
						maxLength="70" />
	
					<display:column property="dataInicioRealizacao" sortable="true" sortProperty="dataInicioRealizacao"
						title="${coluna2}" maxLength="80" format="{0,date,dd/MM/yyyy}" />
	
					<display:column property="dataFimRealizacao" sortable="true" sortProperty="dataFimRealizacao"
						title="${coluna3}" maxLength="80" format="{0,date,dd/MM/yyyy}" />
						
					<display:column class="crudlist" title="Certificado Instrutor" style="text-align : center;">
						<c:if test="${evento.aprovado == 'S'}">
							<form:form action="${url2}" method="GET">
								<input alt="Permitido"
									src="<c:url value="/static/images/tick.png"/>"
									title="Certificado de Instrutor" type="image" value="Permitido" />
							</form:form>
						</c:if>
					</display:column>
	
					<display:column class="crudlist" style="text-align : center;" title="Exibir">
						<form:form action="${url}" method="GET">
							<input alt="<spring:message code="default.button.show.label" />"
								src="<c:url value="/static/images/show.png"/>"
								title="<spring:message code="default.button.show.label" />"
								type="image"
								value="<spring:message code="default.button.show.label" />" />
						</form:form>
					</display:column>
	
				</display:table>
			</div>
		</c:if>
		
	</div>
	
	<script type="text/javascript" src="<c:url value="/static/js/avaliacaoreacao.pendente.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/evento.meuseventos.js"/>"></script>
</body>
</html>
