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
	$('#parcial_data').attr('disabled', 'disabled');
	consultarIndicardoPorAno($("#ano").val());
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
		$("#parcial").show();
		$("#parcial_ate_hj").attr('checked', 'checked');		
	} else {
		$("#parcial").hide();
		$("#parcial_ate_hj").removeAttr("checked");
		$("#parcial_ate").removeAttr("checked");
		$('#parcial_data').attr('disabled', 'disabled');
	}
	
	$("#ano").change(function() {
		if ($("#ano").val() == (new Date).getFullYear()) {
			$("#parcial").show();
			$("#parcial_ate_hj").attr('checked', 'checked');		
		} else {
			$("#parcial").hide();
			$("#parcial_ate_hj").removeAttr("checked");
			$("#parcial_ate").removeAttr("checked");
			$('#parcial_data').attr('disabled', 'disabled');
		}
		
		consultarIndicardoPorAno($("#ano").val());
	});
	
	function parseNumero(number) {
		var string = number.toString()
		string = string.replace(",", "");
		string = string.replace(".", ",");
		return string;
	}
	
	function consultarIndicardoPorAno(ano) {
		var url = "/ajax/meta/indicadorPlanejamentoEstrategico/" + ano;
		loading.start();
		jQuery.ajax({
			type : "GET",
			url : url,
			success : function(indicador) {
				if (indicador) {
					$('#meta_ano').html(indicador.ano);
					$('#meta_quantidade_acoes').html(indicador.quantidadeAcoes);
					$('#meta_perc_juri_capacitados').html(indicador.percentualJurisdicionadosCapacitados + '%');
					$('#meta_perc_serv_capacitados').html(indicador.percentualServidoresCapacitados + '%');
					$('#meta_perc_acoes_plano').html(indicador.percentualAcoesDoPlano + '%');	
				} else {
					$('#meta_ano').html('não encontradas');
					$('#meta_quantidade_acoes').html('');
					$('#meta_perc_juri_capacitados').html('');
					$('#meta_perc_serv_capacitados').html('');
					$('#meta_perc_acoes_plano').html('');
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
		
		<h1>Indicadores do Planejamento Estratégico</h1>

		<c:if test="${mensagem != null}">
			<div class="message">${mensagem}</div>
		</c:if>
		<c:if test="${mensagemErro != null}">
			<div class="messageErro">${mensagemErro}</div>
		</c:if>

		<c:url var="url" value="/relatorio/indicadoresPlanejamento/" />
		
		<form:form action="${url}" method="POST" modelAttribute="relIndicadoresPlanejamento">
			
			<div class="filter">
				
				<div id="metas" style="margin: 10px">
					<h3>Metas <span id="meta_ano"></span></h3>
					<div>
						<spring:message code="meta.planejamentoEstrategico.meta1.label" />: <span id="meta_quantidade_acoes"></span>
					</div>
					<div>
						<spring:message code="meta.planejamentoEstrategico.meta2.label" />: <span id="meta_perc_juri_capacitados"></span>
					</div>
					<div>
						<spring:message code="meta.planejamentoEstrategico.meta3.label" />: <span id="meta_perc_serv_capacitados"></span>
					</div>
					<div>
						<spring:message code="meta.planejamentoEstrategico.meta4.label" />: <span id="meta_perc_acoes_plano"></span>
					</div>
				</div>
				
				<table>
					<tbody>
						
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
							<td>
								<div id="indicadorManual1Label">
									<p>Quantidade de ações de integração promovidas</p>
								</div>
							</td>
							<td>
								<div id="indicadorManual1Input">
									<input type="text" id="indicadorManual1" name="IndicadorManual1" size="5" />
								</div>
							</td>
						</tr>
												
						<tr>
							<td>
								<div id="indicadorManual2Label">
									<p>Percentual de servidores capacitados para o uso de recursos tecnológicos</p>
								</div>
							</td>
							<td>
								<div id="indicadorManual2Input">
									<input type="text" id="indicadorManual2" name="IndicadorManual2" size="5" />
								</div>
							</td>
						</tr>
						
						<tr>
							<td>
								<div id="indicadorManual3Label">
									<p>Percentual de ações implementadas nos Planos de Ação dos Projetos correlacionados (Criação da Política de certificação profissional do servidor e Formação do Auditor do Século XXI)</p>
								</div>
							</td>
							<td>
								<div id="indicadorManual3Input">
									<input type="text" id="indicadorManual3" name="IndicadorManual3" size="5" />
								</div>
							</td>
						</tr>
						
						<tr>
							<td>
								<input id="filtrar" type="submit" class="botao" value="Filtrar" style="text-align: center"/>
							</td>
						</tr>
						
					</tbody>
				</table>
				
			</div>
		</form:form>
	</div>
</body>
</html>