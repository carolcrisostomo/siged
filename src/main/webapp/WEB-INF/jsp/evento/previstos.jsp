<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="evento.label" /></title>
</head>
<body>
	<div class="body">
	
		<%
			if (request.getParameter("mensagemSucesso") != null) {
		%><div class="messageSucesso"><%=request.getParameter("mensagemSucesso")%></div>
		<%
			}
		%>
	
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

		<c:if test="${modalidade != null}">
			<h1>Eventos Previstos - ${modalidade.descricao}</h1>
		</c:if>
		<c:if test="${modalidade == null}">
			<h1>Eventos Previstos</h1>
		</c:if>

		<c:if test="${fn:length(eventos) == 0}">
			<c:if test="${modalidade == null}">
				<div class="message">Não há eventos previstos.</div>
			</c:if>
			<c:if test="${modalidade != null && modalidade.id == 1}">
				<div class="message">Não há eventos presenciais previstos.</div>
			</c:if>
			<c:if test="${modalidade != null && modalidade.id == 2}">
				<div class="message">Não há eventos a distância (EAD)
					previstos.</div>
			</c:if>
		</c:if>

		<c:if test="${fn:length(eventos) > 0}">
			<div class="message">Clique no "+" para detalhes do evento.</div>
		</c:if>
		<sec:authorize ifAllGranted="ROLE_ANONYMOUS">
			<div class="message" id="naoLogado">Eventos abertos para todos os públicos-alvos. Para visualizar eventos ofertados para seu tipo de participante específico, efetue o login no sistema!</div>
		</sec:authorize>
		
		<c:url var="url" value="/evento/previstos" />
		<c:url var="url2" value="/evento/previstos/justificativa" />
		<c:forEach items="${eventos}" var="evento">
			<div id="titulo" class="trigger">
				<h2>
					${evento.tipoId} ${evento.titulo} -
					<fmt:formatDate pattern="dd/MM/yyyy" value="${evento.dataInicioPrevisto}" />
					a
					<fmt:formatDate pattern="dd/MM/yyyy" value="${evento.dataFimPrevisto}" />
					<c:if test="${evento.dataFimPreInscricao < hoje}">
						<div class="inscricoesEncerradas">
							<span style="font-size: 11px; vertical-align: bottom;">PRÉ-INSCRIÇÕES ENCERRADAS</span>
						</div>
					</c:if>
					<c:if test="${evento.dataFimPreInscricao >= hoje && fn:length(evento.inscricaoList) >= evento.totalVagasMargem}">
						<div class="inscricoesEncerradas">
							<span style="font-size: 11px; vertical-align: bottom;">LIMITE DE VAGAS ATINGIDO</span>
						</div>
					</c:if>
				</h2>
			</div>
			<div class="toggle_container">
				<div class="block">
					<ul class="esq">
						
						<%-- <c:if test="${evento.imagem != null}">
								<c:url var="urlimagem" value="/evento/imagem/${evento.id}" />
								<li>
									<img width="383" height="400" src="${urlimagem}">
								</li>
							</c:if> --%>
						
						<c:if test="${evento.objetivoGeral != null}">
							<h3>Objetivo Geral</h3>
							<li>
								<p>
									<c:out	value="${fn:replace(evento.objetivoGeral,linefeed, '<br>')}" escapeXml="false" />
								</p>
							</li>
						</c:if>
						
						<h3>Detalhes</h3>
						<li>
							<p>
								<c:if test="${evento.objetivoEspecifico != null}">
																
									<b>Objetivos Específicos: </b>
									<br />
									<c:out value="${fn:replace(evento.objetivoEspecifico,linefeed, '<br>')}" escapeXml="false" />
									<br />
									
								</c:if>
								
								<b>Tipo do Evento: </b>${evento.tipoId} 
								<br />
																
								<c:if test="${evento.resultadoEsperado != null}">
									
									<b>Resultados Esperados: </b>
									<c:out value="${fn:replace(evento.resultadoEsperado,linefeed, '<br>')}"	escapeXml="false" />
									<br />
									
								</c:if>
								
								<c:if test="${evento.eixoTematicoId != null}">
									
									<b>Eixo Temático: </b>
									<c:out value="${fn:replace(evento.eixoTematicoId,linefeed, '<br>')}" escapeXml="false" />
									<br />
									
								</c:if>								
								
								
								<b>Público Alvo: </b>
								<c:forEach var="p" items="${evento.publicoAlvoList}" varStatus="status">
			    	  				${p.descricao}${not status.last ? ',' : ''}
   								</c:forEach>
								<br /> 
								
								
								<b>Modalidade: </b>${evento.modalidadeModulosLista} 
								<br /> 
								
																
								<b>Carga Horária: </b>${evento.cargaHoraria}								
								<br /> 
																
								
								<b>Localização: </b>${evento.localizacaoModulosLista}<br />
								<%-- <c:if test="${evento.localizacaoId.endereco == null}">
									<c:if test="${evento.modalidadeId.id == 1}">
										<c:choose>
											<c:when test="${evento.localizacaoId.municipio != null}">, ${fn:toUpperCase(evento.localizacaoId.municipio.nome)}-${evento.localizacaoId.municipio.ufMunicipio}, ${evento.localizacaoId.paisId}</c:when>
											<c:otherwise>, ${evento.localizacaoId.paisId}</c:otherwise>
										</c:choose>
									</c:if>
								</c:if>
								<br />
								<c:if test="${evento.localizacaoId.endereco != null}">
									<b>Endereço: </b>
									<c:out value="${fn:replace(evento.localizacaoId.endereco,linefeed, '<br>')}" escapeXml="false" />
									<c:if test="${evento.modalidadeId.id == 1}">
										<c:choose>
											<c:when test="${evento.localizacaoId.municipio != null}">, ${fn:toUpperCase(evento.localizacaoId.municipio.nome)}-${evento.localizacaoId.municipio.ufMunicipio}, ${evento.localizacaoId.paisId}</c:when>
											<c:otherwise>, ${evento.localizacaoId.paisId}</c:otherwise>
										</c:choose>
									</c:if>
									<br />
								</c:if> --%>
									
									
									
								<b><spring:message code="evento.provedorEvento.label.plural" />: </b> ${evento.provedoresLista} 
								<br />								
								
								
								<c:if test="${evento.responsavelEvento != null}">
									
									<b>Responsável pelo Evento: </b>
									<c:out value="${fn:replace(evento.responsavelEvento.nome,linefeed, '<br>')}" escapeXml="false" />
									<br />
									
								</c:if>
							
							
								<b>Período Previsto: </b>
								<fmt:formatDate pattern="dd/MM/yyyy" value="${evento.dataInicioPrevisto}" /> a <fmt:formatDate pattern="dd/MM/yyyy" value="${evento.dataFimPrevisto}" />
								<br />								
								
								
								<b>Período Pré-Inscrição: </b>
								<c:if test="${evento.dataInicioPreInscricao != null && evento.dataFimPreInscricao != null}">
									<fmt:formatDate pattern="dd/MM/yyyy" value="${evento.dataInicioPreInscricao}" /> a <fmt:formatDate pattern="dd/MM/yyyy" value="${evento.dataFimPreInscricao}" />
								</c:if>
								<br /> 
								
								
								<b>Período Realização: </b>
								<c:if test="${evento.dataInicioRealizacao != null && evento.dataFimRealizacao != null}">
									<fmt:formatDate pattern="dd/MM/yyyy" value="${evento.dataInicioRealizacao}" /> a <fmt:formatDate pattern="dd/MM/yyyy" value="${evento.dataFimRealizacao}" />
								</c:if>
								
								
								<c:if test="${evento.linkEvento != null}">
									<br />
									<b>Link para o Evento: </b>
									<c:out value="${fn:replace(evento.linkEvento,linefeed, '<br>')}" escapeXml="false" />
								</c:if>
								<br /> 
								
								
								<c:if test="${evento.linkGravacao != null}">
									<br />
									<b>Link para a Gravação: </b>
									<c:out value="${fn:replace(evento.linkGravacao,linefeed, '<br>')}" escapeXml="false" />
								</c:if>
								
							</p>
						</li>
						
						<c:if test="${evento.conteudo != null}">
							<h3>
								<spring:message code="evento.conteudo.label" />
							</h3>
							<li>
								<p>
									<c:out value="${fn:replace(evento.conteudo,linefeed, '<br>')}" escapeXml="false" />
								</p>
							</li>
						</c:if>
					</ul>
					
					<ul>
						
						<c:if test="${evento.moduloList[0] != null}">
							<h3>Módulos</h3>
						</c:if>
						
						<c:forEach items="${evento.moduloList}" var="m">
							<li>
								<p>
									<b>${m.titulo}</b>
									
									<br /> 
									<br /> 
									
									<b>Período: </b>
									<fmt:formatDate pattern="dd/MM/yyyy" value="${m.dataInicio}" /> a <fmt:formatDate pattern="dd/MM/yyyy" value="${m.dataFim}" />
									<br />
									
									<b>Modalidade: </b>${m.modalidade} 
									<br />
									
									<b>Carga horária: </b>${m.cargaHoraria}
									<br />

									<c:if
										test="${m.horaInicioTurno1 != null && m.horaFimTurno1 != null}">
										<b>${ m.temSomentePrimeiroTurno() ? 'Horário:' : 'Horário 1° Turno:' } </b>${m.horaInicioTurno1} - ${m.horaFimTurno1}
              							<br />
									</c:if>

									<c:if
										test="${m.horaInicioTurno2 != null && m.horaFimTurno2 != null}">
										<b>Horário 2° Turno: </b>${m.horaInicioTurno2} - ${m.horaFimTurno2}
										<br />
									</c:if>

									<c:if
										test="${m.horaInicioTurno3 != null && m.horaFimTurno3 != null}">
										<b>Horário 3° Turno: </b>${m.horaInicioTurno3} - ${m.horaFimTurno3}
										<br />
									</c:if>

									<c:if test="${m.temFrequencia}">
										<b>Frequência Mínima: </b>${m.frequencia}%
										<br />
									</c:if>
									
									<c:if test="${m.temNota}">
										<b>Nota Mínima: </b>${m.nota} 
										<br />
									</c:if>

									<b>Localização: </b>${m.localizacaoComCidadeEUF}
									<br />									
									<c:if test="${!m.enderecoComCidadeUFEPais.isEmpty()}">
										<b>Endereço: </b>
										${m.enderecoComCidadeUFEPais}
										<br />
									</c:if>
									
									<b>Instrutor(es): </b>
									<c:forEach var="ins" items="${m.instrutorList}" varStatus="status"> 
										${ins.nome}${not status.last ? ',' : ''}
  									</c:forEach>
  									<br />
  									
  									<c:if test="${m.observacao != null}">
										<b>Observações: </b>
										<c:out value="${fn:replace(m.observacao,linefeed, '<br>')}" escapeXml="false" /> 
										<br />
									</c:if>
  									
								</p>
								
							</li>
						</c:forEach>

						<c:if test="${evento.eventoComMaterialDivulgacao}">
							<h3>Material de Divulgação</h3>
							<li>
								<table style="border: none;">
									<c:forEach items="${evento.materialDivulgacaoList}"	var="material" varStatus="i">
										<c:if test="${i.index % 2 != 0}">
											<tr class="odd">
										</c:if>
										<c:if test="${i.index % 2 == 0}">
											<tr class="even">
										</c:if>
										<td><a style="color: #006699; font-weight: normal;"
											href="<c:url value="/material/arquivo/${material.id}"/>"
											title="${material.descricao}" 
											target="_parent">${material.descricao}</a></td>
										</tr>
									</c:forEach>
								</table>
							</li>
						</c:if>
						
						<c:if test="${evento.observacoesPublicas != null}">
							<h3>Observações</h3>
							<li>
								<p>
									<c:out value="${fn:replace(evento.observacoesPublicas,linefeed, '<br>')}"	escapeXml="false" />
								</p>
							</li>
						</c:if>						
						
						<div class="acoes">
							<h3>Ações</h3>
							<li id="avalia">
							
								<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
									<c:if test="${fn:length(evento.gastoList) > 0}">
										<!-- BOTÃO DE GASTOS -->
										<a
											href="<c:url value="/evento/gastos/${evento.id}/?n=${random}"/>"
											title="Gastos"
											onclick="Modalbox.show(this.href, {title: this.title, width: 810, height: 500}); return false;">
											<img src="<c:url value="/static/images/icon_gastos.png"/>"
											id="icon_avaliacao" />
										</a>
									</c:if>
									<c:if test="${evento.eventoComParticipante}">
										<!-- BOTÃO DE PARTICIPANTES -->
										<a
											href="<c:url value="/evento/participantes/${evento.id}/?n=${random}"/>"
											title="Participantes"
											onclick="Modalbox.show(this.href, {title: this.title, width: 810, height: 500}); return false;">
											<img
											src="<c:url value="/static/images/icon_participantes.png"/>"
											id="icon_participantes" />
										</a>
									</c:if>
								</sec:authorize>
							</li>
						</div>
						
						<c:if test="${evento.permitePreInscricao == 'S' 
													&& evento.dataInicioPreInscricao <= hoje 
													&& evento.dataFimPreInscricao >= hoje
													&& fn:length(evento.inscricaoList) < evento.totalVagasMargem}">
							
							<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
								<div style="margin: 33px 2px">
									<!-- BOTÃO DE PRÉ-INSCRIÇÃO -->
									<a id="linkPreInscricao" href="<c:url value="/evento/inscricao/${evento.id}"/>" title="Pré-Inscrição">										
										<span>FAÇA SUA PRÉ-INSCRIÇÃO</span>										
									</a>
								</div>
							</sec:authorize>
							
							<sec:authorize ifAnyGranted="ROLE_ALUNO,ROLE_SERVIDOR">
								
								<c:if test="${fn:indexOf(evento.publicoAlvoLista, sessionScope.TIPO_PARTICIPANTE) >= 0}">
									<c:set var="fezInscricao" value="${false}" scope="page" />
									<c:forEach var="eventoIdInscrito" items="${eventoIdInscritoList}">
										<c:if test="${evento.id==eventoIdInscrito}">
											<c:set var="fezInscricao" value="${true}" scope="page" />
										</c:if>
									</c:forEach>
									
									<c:set var="tipoParticipanteId" value="${sessionScope.PARTICIPANTE.tipoId.id}" scope="page" />
									<c:if test="${not fezInscricao and (tipoParticipanteId == 1 or tipoParticipanteId == 4)}">
										<div style="margin: 33px 2px">
											<!-- BOTÃO DE PRÉ-INSCRIÇÃO -->
											<a id="linkPreInscricao" class="pre-inscricao--js-hook" href="<c:url value="/evento/justificativa/${evento.id}"/>" 
												title="Pré-Inscrição"
												data-url-consulta="/evento/${evento.id}/possibilidadeInscricaoAluno">	
												<span>FAÇA SUA PRÉ-INSCRIÇÃO</span>										
											</a>
										</div>
									</c:if>
									
									<c:if test="${not fezInscricao and (tipoParticipanteId == 2 or tipoParticipanteId == 3)}">
										<div style="margin: 33px 2px">
											<!-- BOTÃO DE PRÉ-INSCRIÇÃO -->
											<a id="linkPreInscricao" class="pre-inscricao--js-hook" href="<c:url value="/evento/${evento.id}/inscricaoLogado"/>" 
												title="Pré-Inscrição"
												data-url-consulta="/evento/${evento.id}/possibilidadeInscricaoAluno"
												<span>FAÇA SUA PRÉ-INSCRIÇÃO</span>										
											</a>
										</div>
									</c:if>
								</c:if>
								
							</sec:authorize>
							
						</c:if>
						
					</ul>
				</div>
			</div>
		</c:forEach>
	</div>
	<script type="text/javascript" src="<c:url value="/static/js/ajax.loading.js"/>"></script>
	<script type="text/javascript">
		jQuery(document).ready(function($) {
			var loading = new SIGED.AjaxLoading();
			var acoesDivs = $(".acoes");
	
			acoesDivs.each(function(pos, div) {
				var $div = $(div);
				if ($.trim($div.children('#avalia').html()).length == 0)
					$div.remove();
			});
			
			$('.pre-inscricao--js-hook').click(function(event) {
				event.preventDefault();
				var target = $(event.currentTarget);
				var eventoId = target.attr('data-evento-id');
				var modalUrl = target.attr('href');
				var modalTitle = target.attr('title');
				var consultaURL = target.attr('data-url-consulta');
				
				loading.start();
				jQuery.ajax({
					url : consultaURL,
					success : function(data) {
						var isServidor = "${tipoParticipanteId == 1 or tipoParticipanteId == 4}" == 'true';
						var isExterno = "${tipoParticipanteId == 2 or tipoParticipanteId == 3}" == 'true';
						if (isServidor) {
							loading.stop();
							Modalbox.show(modalUrl, {title: modalTitle, width: 800, height: 300});
						} else if (isExterno) {
							jQuery.ajax({
								url: modalUrl,
								type: 'POST',
								contentType: 'application/json',
								success: function(message) {
									swal({
										title: "Pré-inscrição realizada com sucesso!",
										text: decodeURIComponent(message),
										type: "success",
										allowEscapeKey: true,
										allowOutsideClick: true,
										confirmButtonText: "OK",
										confirmButtonColor: "#2A6098"
									});
								},
								error : function(error) {
									swal({
										title: "Pré-inscrição não realizada!",
										text: decodeURIComponent(error.response),
										type: "error",
										allowEscapeKey: true,
										allowOutsideClick: true,
										confirmButtonText: "Fechar",
										confirmButtonColor: "#F27474"
									});
								},
								complete : function() {
									loading.stop();	
								}
							});
						}
					},
					error: function(error) {
						loading.stop();
						swal({
							title: "Pré-inscrição não realizada!",
							text: decodeURIComponent(error.response),
							type: "error",
							allowEscapeKey: true,
							allowOutsideClick: true,
							confirmButtonText: "Fechar",
							confirmButtonColor: "#F27474"
						});
					}
				});
			});
		});
	</script>	
</body>
</html>
