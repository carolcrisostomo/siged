<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title><spring:message code="default.add.label" /></title>
</head>
<body>
<script type="text/javascript" src="<c:url value="/static/js/ajax.loading.js"/>"></script>
<script type="text/javascript">
	jQuery(document).ready(function($){	
		
		var loading = new SIGED.AjaxLoading();
		consultarIndicardoPorAnoESemestre($("#ano").val(), $("#semestre").val());
		
		$('#parcial_data').attr('disabled', 'disabled');
		$("input[name=parcial_ate]:radio").change(function () {
			if ($("#parcial_ate_hj").attr("checked")) {
				$('#parcial_data').val("");
	            $('#parcial_data').attr('disabled', 'disabled');
	        }
			if ($("#parcial_ate").attr("checked")) {
	            $('#parcial_data').removeAttr('disabled');
	            $('#parcial_data').focus();
	        }
		});
		
		if ($("#ano").val() == (new Date).getFullYear()) {
			if (($("#semestre").val() == 1 && (new Date).getMonth() + 1 < 7) || ($("#semestre").val() == 2 && (new Date).getMonth() + 1 >= 7)) {
				$("#parcial").show();
				$("#parcial_ate_hj").attr('checked', 'checked');
			} else {
				$("#parcial").hide();
				$("#parcial_ate_hj").removeAttr("checked");
				$("#parcial_ate").removeAttr("checked");
				$('#parcial_data').attr('disabled', 'disabled');
			}
		} else {
			$("#parcial").hide();
			$("#parcial_ate_hj").removeAttr("checked");
			$("#parcial_ate").removeAttr("checked");
			$('#parcial_data').attr('disabled', 'disabled');
		}
		
		$("#ano, #semestre").change(function() {
			if ($("#ano").val() == (new Date).getFullYear()) {
				if (($("#semestre").val() == 1 && (new Date).getMonth() + 1 < 7) || ($("#semestre").val() == 2 && (new Date).getMonth() + 1 >= 7)) {
					$("#parcial").show();
					$("#parcial_ate_hj").attr('checked', 'checked');
				} else {
					$("#parcial").hide();
					$("#parcial_ate_hj").removeAttr("checked");
					$("#parcial_ate").removeAttr("checked");
					$('#parcial_data').attr('disabled', 'disabled');
				}
			} else {
				$("#parcial").hide();
				$("#parcial_ate_hj").removeAttr("checked");
				$("#parcial_ate").removeAttr("checked");
				$('#parcial_data').attr('disabled', 'disabled');
			}
			
			consultarIndicardoPorAnoESemestre($("#ano").val(), $("#semestre").val());
		});
	
		function consultarIndicardoPorAnoESemestre(ano, semestre) {
			var url = "/ajax/meta/indicadorDesempenhoProdutividade/" + ano + "/" + semestre;
			loading.start();
			jQuery.ajax({
				type : "GET",
				url : url,
				success : function(indicador) {
					if (indicador) {
						$('#meta_ano').html(indicador.ano);
						$('#meta_semestre').html('.' + indicador.semestre);
						$('#meta_ind_capacitacao').html(indicador.indiceCapacitacao + '%');
						$('#meta_ind_avaliacao_reacao').html(indicador.indiceAvaliacaoReacao + '%');
					} else {
						$('#meta_ano').html('não encontradas');
						$('#meta_semestre').html('');
						$('#meta_ind_capacitacao').html('');
						$('#meta_ind_avaliacao_reacao').html('');
					}
					
					loading.stop();
				},
				error : function(data) {
					console.error(data);
					loading.stop();
				}
			});
		}
	});
</script>

	<div class="body">
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<% } %>
		
		<h1>Indicadores de Desempenho/Produtividade</h1>

		<c:if test="${mensagem != null}">
			<div class="message">${mensagem}</div>
		</c:if>
		<c:if test="${mensagemErro != null}">
			<div class="messageErro">${mensagemErro}</div>
		</c:if>

		<c:url var="url" value="/relatorio/indicadoresDesempenho/" />
		
		<form:form action="${url}" method="POST" modelAttribute="relIndicadoresDesempenho">
			
			<div class="filter">
			
				<div id="metas" style="margin: 10px">
					<h3>Metas <span id="meta_ano"></span><span id="meta_semestre"></span></h3>
					<div>
						<spring:message code="meta.desempenhoProdutividade.meta1.label" />: <span id="meta_ind_capacitacao"></span>
					</div>
					<div>
						<spring:message code="meta.desempenhoProdutividade.meta2.label" />: <span id="meta_ind_avaliacao_reacao"></span>
					</div>
				</div>
				
				<table>
					<tbody>
												
						<tr class="prop">
							<td valign="top" class="name" style="width: 40%;">
								<label for="semestre">Semestre</label>
							</td>
							<td valign="top">
								<form:select path="semestre" id="semestre">
									<form:options items="${semestreList}" />
								</form:select> <form:errors path="semestre" cssClass="error" />
							</td>
						</tr>
						
						<tr class="prop">							
							<td valign="top" class="name"><label for="ano">Ano</label></td>
							<td valign="top"><form:select path="ano" id="ano">
									<form:options items="${anoListIndicador}" />
								</form:select>
							</td>
						</tr>

						<tr class="prop">
							
							<td valign="top" class="name" style="width: 40%;"></td>
							
							<td valign="top" style="height: 20px;">
								<div id="parcial">									
									<form:radiobutton id="parcial_ate_hj" path="parcial_ate" value="0" label="Apurado até o dia de hoje" />									
									<br />									
									<form:radiobutton id="parcial_ate" path="parcial_ate" value="1" label="Apurado até" />
									
									<form:input path="parcial_data" id="parcial_data" alt="date" />									
									<form:errors path="parcial_data" cssClass="error" />						
								</div>
							</td>
						</tr>						
						
						<tr>
							<td><input id="filtrar" type="submit" class="botao" value="Filtrar" /></td>
							<td></td>
						</tr>
						
					</tbody>
				</table>
				
			</div>
		</form:form>
	</div>
</body>
</html>