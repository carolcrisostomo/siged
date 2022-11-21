<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>

<spring:url value="/ajax/participante/buscarInscritos" var="buscarInscritosUrl" />
</head>
<body>
	<div class="body">
		<h1>Enviar Comunicado</h1>		
		<c:if test="${aviso != null}">
			<div class="messageSucesso" id="aviso"><c:out value="${aviso}"></c:out></div>
		</c:if>	
		<div class="message" id="aviso2" style="display: none;">
			Não há participantes do tipo indicado com inscrição confirmada no Evento selecionado.
		</div>
		<c:url var="url" value="/evento/comunicado" />
		<form:form action="${url}" method="POST" modelAttribute="email" >						
			<form:hidden path="to" value=""/>
			<div class="dialog">
				<table>
					<tbody>
						<tr>
							<td></td>
							<td style="text-align: right;" valign="top" class="name">(*) Campos Obrigatórios</td>
						</tr>
						<tr class="name">
							<td valign="top" class="name"><label for="evento"><spring:message
										code="evento.evento.label" /></label></td>
							<td valign="top"><form:select path="eventoId">
									<form:option value="0">Selecione...</form:option>
									<form:options items="${eventosAll}" itemLabel="nome"
										itemValue="id" />
								</form:select>*<br />
								<form:errors path="eventoId" cssClass="error" />
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><label for="titulo">Assunto</label></td>
							<td><form:input cssStyle="width:450px" maxlength="255"
									path="titulo" />* <form:errors path="titulo" cssClass="error" /></td>
						</tr>
						<tr class="name">
							<td valign="top" class="name"><label for="comunicado">Comunicado</label></td>
							<td valign="top"><form:textarea id="comunicado" rows="3"
									cssStyle="width:450px; height:150px;" path="mensagem" />* 
									<form:errors path="mensagem" cssClass="error" /></td>
						</tr>
						<tr>
                   	 		<td>
                      			<label for="indicada"><spring:message code="inscricao.indicada.label" /></label>
                    		</td>
	                    	<td valign="top" class="value">
	                      		<form:select path="" id="indicada">
	                      			<form:option value="">TODOS</form:option>
	                      			<form:options items="${SNEnum}"/>
	                      		</form:select>
	                    	</td>
                  		</tr>
                  		
                  		<tr>
                    		<td>
                      			<label for="confirmada"><spring:message code="inscricao.confirmada.label" /></label>
                    		</td>
                    		<td valign="top" class="value">
		                    	<form:select path="" id="confirmada">
		                      		<form:option value="">TODOS</form:option>
		                      		<form:options items="${SNEnum}"/>
                      			</form:select>
                    		</td>
                  		</tr>
                  		
                  		<tr>
                    		<td>
                      			<label for="aprovado"><spring:message code="inscricao.aprovado.label" /></label>
                    		</td>
                    		<td valign="top" class="value">
		                    	<form:select path="" id="aprovado">
		                      		<form:option value="">TODOS</form:option>
		                      		<form:options items="${SNEnum}"/>
                      			</form:select>
                    		</td>
                  		</tr>
                  		
						<tr>
							<td><label for="tipoParticipanteId">Tipo Participante</label></td>
							<td valign="top" class="value"><form:select
									path="" id="tipoParticipanteId">
									<form:option value="0">TODOS</form:option>
									<form:options items="${tipoPublicoAlvoList2}"
										itemLabel="descricao" itemValue="id" />
								</form:select></td>
						</tr>						
						<tr id="trParticipanteTable" style="display: none;">
							<td colspan="2"><br /><label for="comunicado">Participante</label><br />
								<br />
								<div id="tabelaDiv" class="list" style="max-height: 250px; overflow: auto;">
									<table id="participanteTable" >
										<thead>
											<tr>
												<th><input type='checkbox' id='selectall' value='' style="margin-top: 5px;"/></th>
												<th>Nome</th>
												<th>CPF</th>
												<th>E-mail</th>
											</tr>
										</thead>
										<tbody></tbody>
									</table>										
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<br /><input type="button" id="enviar" class="botao" value="Enviar" disabled="disabled"/>
							</td>					
						</tr>
					</tbody>
				</table>
			</div>
		</form:form>
	</div>
	<script type="text/javascript" src="<c:url value="/static/js/ajax.loading.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/participante.consulta.js"/>"></script>
	<script type="text/javascript">
		jQuery(document).ready(function($) {
			var participanteConsulta = new SIGED.ConsultaParticipantePorFiltro("${buscarInscritosUrl}");
			var loading = new SIGED.AjaxLoading();
			
			participanteConsulta.event.bind('sucesso', function(event, participantes) {
				updateParticipanteGrid(participantes);
			});
			
			$('*').click(function(){
				$('#aviso').remove();
			});
			
			if($("#eventoId").val() != 0){
				var filtro = criarFiltro();
				participanteConsulta.buscar(loading, filtro);
			}
			
			function criarFiltro() {
				var filtro = {
					evento: $("#eventoId").val(),
					tipoParticipanteId: $("#tipoParticipanteId").val(),
					indicada: $("#indicada").val(),
					confirmada: $("#confirmada").val(),
					aprovado: $("#aprovado").val()
				};
				
				return filtro;
			}
			
			function updateParticipanteGrid(data){
				if(data.length > 0){
					$('#trParticipanteTable').show();
					$('#aviso2').hide();
					$('#enviar').attr('disabled', false);

					var html = '';

					for (var i = 0; i < data.length; i++) {
						var corDaLinha = (i % 2 == 0) ? "odd" : "even";
						var email = data[i].email;
						if (email == null) email = "";
						
						html += '<tr class="' + corDaLinha + '">';
						html += '<td style="width:15px;" ><input type="checkbox" value="'+ email + '" onclick="desmarcaSelectAll();" /></td>';
						html += '<td>'+ data[i].nome +'</td>';
						html += '<td>'+ data[i].cpfFormatado +'</td>';								
						html += '<td>'+ email +'</td></tr>';							
					}							
					$('#participanteTable').children('tbody').html(html);

				}else{ 
					$('#trParticipanteTable').hide();
					$('#enviar').attr('disabled', true);
					if($("#eventoId").val() > 0){
						$('#aviso2').show();								
					}							
				}
				
				$("#selectall").attr("checked", false);
			}

			$("#selectall").click(function() {
				if ($("#selectall").is(':checked')) {
					$('input[type="checkbox"]').attr("checked", true);
				} else {
					$('input[type="checkbox"]').attr("checked", false);
				}
			});			
					
			$("#eventoId, #tipoParticipanteId, #indicada, #confirmada, #aprovado").change(function() {
				if($("#eventoId").val() == 0 && $("#tipoParticipanteId").val() == 0){
					$('#trParticipanteTable').hide();
					$('#enviar').attr('disabled', true);
				}else{
					if($("#eventoId").val() != 0 || $("#tipoParticipanteId").val() != 0){
						var filtro = criarFiltro();
						participanteConsulta.buscar(loading, filtro);
					}						
				}
			});			
			
			$("#enviar").click(function(){
				
				var selecionados = $("input:checked");
								
				var emails = "";
				
				if($("#selectall").is(':checked') || !$("#selectall").is(':checked') && selecionados.length > 0){
					
					selecionados.each(function(pos, checkbox){
						var $checkbox = $(checkbox);
						var email = $checkbox.val();
						if(email != '')
							emails += email + ",";
					});
					
					if(emails.indexOf(",", 0) > 0){						
						$("#to").val(emails);
						$('form').submit();											
					}
				}			
			});
			
		});
		
		function desmarcaSelectAll(){
			document.getElementById("selectall").checked = false;				
		}
	</script>
</body>
</html>