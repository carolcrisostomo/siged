<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ include file="../jsp/includes.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>IPC - SIGED</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1" />
	
	<link href="/static/js/bootstrap-5.2.0-dist/css/bootstrap.min.css" rel="stylesheet" >

	
 <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.3.0/font/bootstrap-icons.css"
    />
	<link rel="icon" href="<c:url value="/static/images/favicon.ico"/>" />
	<link rel="shortcut icon" href="<c:url value="/static/images/favicon.ico"/>" />
	
	<style type="text/css" media="all">@import url("<c:url value='/static/styles/apprise.css'/>");</style>
	<style type="text/css" media="all">@import url("<c:url value='/static/styles/jquery-ui-1.8.6.custom.css'/>");</style>
    <style type="text/css" media="all">@import url("<c:url value='/static/styles/jquery.ui.datepicker.css'/>");</style>
	<style type="text/css" media="all">@import url("<c:url value='/static/styles/modalbox.css'/>");</style>
	<style type="text/css" media="all">@import url("<c:url value='/static/styles/sweetalert.css'/>");</style>
	<style type="text/css" media="all">@import url("<c:url value='/static/styles/chosen.min.css'/>");</style>
	<style type="text/css" media="all">@import url("<c:url value='/static/styles/style.css'/>");</style>
	<style type="text/css" media="all">@import url("<c:url value='/static/styles/main.css'/>");</style>
	<style type="text/css" media="all">@import url("<c:url value='/static/styles/menu.css'/>");</style>

	<script src="/static/js/bootstrap-5.2.0-dist/css/bootstrap.min.css/bootstrap.bundle.min.js"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery-1.4.2.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery-ui-1.8.6.custom.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery.displaytag-ajax-1.2.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery.meio.mask.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/prototype.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/apprise-1.5.full.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/scriptaculous.js?load=builder,effects"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/modalbox.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/select.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/querystring-0.9.0-min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery.blockUI.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery/datepicker.1.8.24.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery.ui.datepicker-pt-BR.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery/placeholders.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery/sweetalert.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery/chosen.jquery.min.js"/>"></script>	
	
<script type="text/javascript">
	jQuery(document).ready(function($){		
		
/* 
		var title = document.getElementsByTagName('h1')
		var titleList = title[0].innerHTML.split(' - ')
		var breadcrumb = document.getElementsByClassName('breadcrumb')
		console.log(breadcrumb[0].innerHTML)
		var breadcrumbInnerHTML = ''
		
		for(var i = 0; i<titleList.length; i++){
			breadcrumbInnerHTML += '<li class="breadcrumb-item">' + titleList[i] + '</li>'
		}
		
		breadcrumb[0].innerHTML += breadcrumbInnerHTML */
		
		$('input:text').setMask();
				
		$(".toggle_container").hide();		

		$("#titulo.trigger").click(function() {
			$(this).toggleClass("active").next().slideToggle("slow");
		});

		$.ajaxSetup({ cache: false });
		
		$('input[alt="date"]').datepicker();
		
		$("#email").focusout(function(){
			
			$("#email").val($("#email").val().trim());			
			
			if ($("#email").val() != "") {
				if(!valida_email($("#email").val())) {						
					alert('E-mail Invlido.');
					$("#email").val("").focus();
				} 
			}		
		});
		
		$('select').addClass('chosen-select');
		
		$('.chosen-select').chosen({
			disable_search_threshold: 5,
			no_results_text: "Nenhum resultado corresponde a ",
			placeholder_text: "Selecione algumas opes"
		});
	
	});
</script>
</head>

<body>

<div id="centro">

	<div id="header">
			
		<a id="logo_siged_topo" href="http://siged.ipc.tce.ce.gov.br/" target="_blank">
			<img alt="Logo SIGED" src="<c:url value="/static/images/siged_logo.png"/>" />
		</a>
		<a id="logo_tce_topo" href="https://www.tce.ce.gov.br/" target="_blank">
			<img alt="Logo TCE-CE" src="<c:url value="/static/images/tce_logo_vertical.png"/>" />
		</a>	
		<a id="logo_ipc_topo" href="http://www.ipc.tce.ce.gov.br/" target="_blank">
			<img alt="Logo IPC" src="<c:url value="/static/images/ipc_logo_vertical.png"/>" />
		</a>
		
		<sec:authorize ifNotGranted="ROLE_UNKNOWN">
			<div id="login">
				<div id="login_left">					
					<div id="login_right">											
							
						<sec:authorize ifAnyGranted="ROLE_ALUNO,ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS,ROLE_INSTRUTOR,ROLE_CHEFE,ROLE_SERVIDOR">
							<div class="bemVindoDiv">
								<span class="bemVindoLabel" >Bem Vindo, <%= request.getSession().getAttribute("USUARIO") %></span>						
							</div>
							<div class="botaoSairDiv">
								<sec:authorize ifNotGranted="ROLE_UNKNOWN"><a class="botaoSair" href="/j_spring_security_logout" title="Sair"></a></sec:authorize>						
							</div>
						</sec:authorize>						
										
						<sec:authorize ifAllGranted="ROLE_ANONYMOUS">
							<form action="/usuario/login" method="post">
								<span class="loginLabel">Login</span>
								<input class="login" type='text' name='j_username' id='j_username' placeholder="Usurio" />
								<input class="login" type='password' name='j_password' id='j_password' placeholder="Senha" />
								<input class="submit" type='submit' value='Entrar'/>
							</form>
						</sec:authorize>
					</div>
				</div>
			</div>			
		</sec:authorize>
	
	</div>

	<div id="titulosiged">
		<h2><spring:message code="aplicativo.nome" /></h2>
	</div>

	<div id="omenu">

		<sec:authorize ifNotGranted="ROLE_UNKNOWN">
		<sec:authorize ifNotGranted="ROLE_PASSWORD_CHANGES_REQUIRED">
		<sec:authorize ifNotGranted="ROLE_UPDATE_EMAIL_REQUIRED">
			<ul id="menu">
				
				<li style="padding: 0;"><img class="" alt="" src="<c:url value="/static/images/omenu_left.png"/>" /></li>
				
				<li class="evento"><a href="#"><spring:message	code="evento.label" /></a>
					<ul id="eventos">
						<li><img class="corner_inset_left" alt="" src="<c:url value="/static/images/corner_inset_left.png"/>" />
							<a id="previstos" href="<c:url value="/evento/previstos"/>"><spring:message	code="evento.previstos" /></a>
							
							<ul>
								<li><a href="<c:url value="/evento/eventos/previstos/1"/>">Presencial</a></li>
								<li><a href="<c:url value="/evento/eventos/previstos/2"/>">A Distncia (EAD)</a></li>
							</ul>
							
							<img class="corner_inset_right" alt="" src="<c:url value="/static/images/corner_inset_right.png"/>" />
						</li>
			
						<li><a id="emandamento" href="<c:url value="/evento/emandamento"/>"><spring:message code="evento.emandamento" /></a>
							<ul>
								<li><a href="<c:url value="/evento/eventos/emandamento/1"/>">Presencial</a></li>
								<li><a href="<c:url value="/evento/eventos/emandamento/2"/>">A Distncia (EAD)</a></li>
							</ul>
						</li>
						
						<li><a id="realizados" href="<c:url value="/evento/realizados"/>"><spring:message code="evento.realizados" /></a>
							<ul>
								<li><a href="<c:url value="/evento/eventos/realizados/1"/>">Presencial</a></li>
								<li><a href="<c:url value="/evento/eventos/realizados/2"/>">A Distncia (EAD)</a></li>
							</ul>
						</li>
						
						<sec:authorize ifAnyGranted="ROLE_ALUNO,ROLE_CHEFE">
							<li><a href="<c:url value="/evento/meuseventos"/>"><spring:message code="evento.meus.label" /></a></li>
							<!-- <li><a href="<c:url value="/avaliacao/minhasavaliacoes"/>"><spring:message code="avaliacao.minhas.label" /></a></li> -->
							<li><a href="<c:url value="/avaliacaoreacao/minhasavaliacoes"/>"><spring:message code="avaliacao.minhas.label" /></a></li>			
						</sec:authorize>
						
						<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
							<li><a href="<c:url value="/evento/"/>"><spring:message	code="evento.cadastro.label" /></a></li>
							<li><a href="<c:url value="/modulo/"/>"><spring:message	code="modulo.label" /></a></li>
							<li><a href="<c:url value="/material/"/>">Material Didtico / Divulgao</a></li>
							<li><a href="<c:url value="/gasto/"/>"><spring:message code="gasto.label" /></a></li>										
							<!--<li><a href="<c:url value="/avaliacao/"/>"><spring:message code="avaliacao.label" /></a></li> -->
							<li><a href="<c:url value="/avaliacaoreacao/"/>"><spring:message code="avaliacao.label" /></a></li>
							<li><a href="<c:url value="/eventorecomendar/"/>"><spring:message code="eventoRecomendar.label" /></a></li>
							
							<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
								<li><a href="<c:url value="/evento/apurardesempenho/"/>"><spring:message code="evento.apurarDesempenho.label" /></a></li>
								<li><a href="<c:url value="/evento/comunicado"/>">Enviar Comunicado</a></li>
								<li><a href="<c:url value="/evento/divulgarevento/"/>"><spring:message code="evento.divulgarEvento.label" /></a></li>
							</sec:authorize>			
						</sec:authorize>
						
						<sec:authorize ifAnyGranted="ROLE_ALUNO,ROLE_CHEFE">				
							<li><a href="<c:url value="/certificado/impressao/"/>"> <spring:message code="evento.emitirCertificado.label" /></a></li>	
						</sec:authorize>
						
						<li class="last">
							<img class="corner_left" alt="" src="<c:url value="/static/images/corner_left.png"/>" /> 
							<img class="middle" alt="" src="<c:url value="/static/images/dot.gif"/>" />
							<img class="corner_right" alt="" src="<c:url value="/static/images/corner_right.png"/>" />
						</li>
					</ul>	
				</li>
				
				<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS,ROLE_SERVIDOR,ROLE_ALUNO,ROLE_CHEFE">
				
					<li class="evento">
						<a href="#"><spring:message code="participante.label" /></a>
						<ul id="participantes2">
							<li><img class="corner_inset_left" alt="" src="<c:url value="/static/images/corner_inset_left.png"/>" />
								<img class="corner_inset_right" alt="" src="<c:url value="/static/images/corner_inset_right.png"/>" />
								
								<sec:authorize ifAnyGranted="ROLE_ALUNO,ROLE_SERVIDOR,ROLE_CHEFE">
									<a href="<c:url value="/inscricao/minhasinscricoes"/>"><spring:message code="inscricao.minhas.label" /></a>
								</sec:authorize>
								
								<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
									<a href="<c:url value="/participante/"/>"><spring:message code="participante.cadastro.label" /></a>
								</sec:authorize>
							</li>						
							<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
								<li>
									<a href="<c:url value="/inscricao/"/>"><spring:message code="inscricao.label" /></a>
									<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
										<ul>
											<li><a href="<c:url value="/inscricao/inclusaolote/"/>">Incluso em lote</a></li>
											<li><a href="<c:url value="/inscricao/confirmacao/"/>">Confirmao em lote</a></li>
										</ul>
									</sec:authorize>
								</li>
								<li><a href="<c:url value="/nota/"/>"><spring:message code="nota.label" /></a></li>
								<li><a href="<c:url value="/frequencia/"/>"><spring:message	code="frequencia.label" /></a></li>
							</sec:authorize>
							<sec:authorize ifAnyGranted="ROLE_SERVIDOR">
								<li><a href="<c:url value="/eventoextra/"/>"><spring:message code="eventoExtra.minhas.label" /></a></li>
							</sec:authorize>
							<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
								<li><a href="<c:url value="/eventoextra/"/>"><spring:message code="eventoExtra.label" /></a></li>
							</sec:authorize>			
							<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR">
								<li><a href="#"><spring:message code="relatorio.certificados.label" /></a>
									<ul>
										<li><a href="<c:url value="/certificadoDeTerceiros/"/>"><spring:message code="certificado.label" /></a></li>
										<li><a href="<c:url value="/certificadoEmitido"/>"><spring:message code="certificadoemitido.label" /></a></li>
										<li><a href="<c:url value="/certificado/impressao/"/>"> <spring:message code="evento.emitirCertificado.label" /></a></li>	
									</ul>
								</li>

								<%-- <li><a href="<c:url value="/avaliacaoeficacia/"/>"><spring:message code="avaliacaoEficacia.label" /></a></li>--%>
							</sec:authorize>	
							<sec:authorize ifAnyGranted="ROLE_ADMINISTRADORCONS">
								<li><a href="#"><spring:message code="relatorio.certificados.label" /></a>
									<ul>
										<li><a href="<c:url value="/certificadoDeTerceiros/"/>"><spring:message code="certificado.label" /></a></li>
										<li><a href="<c:url value="/certificadoEmitido"/>"><spring:message code="certificadoemitido.label" /></a></li>		
									</ul>
								</li>
								<%--<li><a href="<c:url value="/avaliacaoeficacia/"/>"><spring:message code="avaliacaoEficacia.label" /></a></li>--%>
							</sec:authorize>						
						<sec:authorize ifAnyGranted="ROLE_CHEFE">
								<li><a href="<c:url value="/inscricao/minhasindicacoes"/>"><spring:message code="inscricao.minhas2.label" /></a></li>
								<%-- <li><a href="<c:url value="/avaliacaoeficacia/minhasavaliacoes"/>"><spring:message code="avaliacaoEficacia.minhas.label" /></a></li> --%>
							</sec:authorize>
							<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
								<li><a href="<c:url value="/inscricao/exportar/ava"/>">Exportar para o AVA</a></li>
							</sec:authorize>
							<sec:authorize ifAnyGranted="ROLE_RESET_PASSWORD">
								<li><a href="<c:url value="/usuario/resetarSenha"/>">Resetar Senha</a></li>
							</sec:authorize>
							
							<li class="last">
								<img class="corner_left" alt="" src="<c:url value="/static/images/corner_left.png"/>" /> 
								<img class="middle" alt="" src="<c:url value="/static/images/dot.gif"/>" />
								<img class="corner_right" alt="" src="<c:url value="/static/images/corner_right.png"/>" />
							</li>
						</ul>			
					</li>
				</sec:authorize>	
					
				<li class="evento">
					<a href="#"> <spring:message code="instrutor.label" /></a>			
					
					<ul id="instrutores">
						<li><img class="corner_inset_left" alt="" src="<c:url value="/static/images/corner_inset_left.png"/>" />
							
							<sec:authorize ifNotGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
								<a href="<c:url value="/instrutor/form"/>"><spring:message code="instrutor.preCadastro.label" /></a> 
							</sec:authorize>
							<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
								<a href="<c:url value="/instrutor/"/>"><spring:message code="instrutor.cadastro.label" /></a>
							</sec:authorize>
							<img class="corner_inset_right" alt="" src="<c:url value="/static/images/corner_inset_right.png"/>" />
						</li>
						
						<%-- <li><a href="<c:url value="/static/docs/Modelo_Projeto_Evento-Curso_IPC.doc"/>">Modelo de Projeto de Curso/Evento</a></li> --%>
						
						<li class="last">
							<img class="corner_left" alt="" src="<c:url value="/static/images/corner_left.png"/>" /> 
							<img class="middle" alt="" src="<c:url value="/static/images/dot.gif"/>" />
							<img class="corner_right" alt="" src="<c:url value="/static/images/corner_right.png"/>" />
						</li>
					</ul>				
				</li>
			
				<sec:authorize ifAnyGranted="ROLE_ALUNO,ROLE_SERVIDOR">									
					<li>
						<a href="#"><spring:message code="relatorio.label" /></a><ul id="help">
							<li>						
			           	 		<img class="corner_inset_left" alt="" src="<c:url value="/static/images/corner_inset_left.png"/>"/>
			       	    		<a href="<c:url value="/relatorio/eventosPorParticipante/"/>">Eventos</a>
			           			<img class="corner_inset_right" alt="" src="<c:url value="/static/images/corner_inset_right.png"/>"/>
							</li>
							<li>
			       	    		<a href="<c:url value="/certificado/impressaoInstrutorLogado/"/>">Certificado de Instrutor</a>
							</li>											
							<li class="last">
			           			<img class="corner_left" alt="" src="<c:url value="/static/images/corner_left.png"/>"/>
			           			<img class="middle" alt="" src="<c:url value="/static/images/dot.gif"/>"/>
			           			<img class="corner_right" alt="" src="<c:url value="/static/images/corner_right.png"/>"/>
			       			</li>
			     		</ul>
			    	</li>    						
				</sec:authorize>
					
				<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
					<sec:authorize ifNotGranted="ROLE_SERVIDOR,ROLE_ALUNO,ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
						<li><a href="<c:url value="/usuario/esquecisenha"/>">Esqueci minha senha</a></li>
						<li><a href="<c:url value="/certificadoEmitido/verificar/form"/>">Validar certificado</a></li>
					</sec:authorize>
				</sec:authorize>	
				
				<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
						<li><a href="#"><spring:message code="relatorio.label" /></a>
							<ul id="help">
								<li>						
			                        <img class="corner_inset_left" alt="" src="<c:url value="/static/images/corner_inset_left.png"/>"/>
			                        <a href="<c:url value="/relatorio/atividadesTrimestralAnualIPC/"/>"><spring:message code="relatorio.atividadesTrimestralAnualIPC.label" /></a>
			                        <img class="corner_inset_right" alt="" src="<c:url value="/static/images/corner_inset_right.png"/>"/>
								</li>
								<%-- <li><a href="<c:url value="/relatorio/avaliacoesEficaciaTreinamentoEventoParticipante/"/>"><spring:message code="relatorio.avaliacaoEficaciaEventoParticipante.label" /></a></li> --%>
								<%-- <li><a href="<c:url value="/relatorio/avaliacoesEficaciaPorEvento/"/>"><spring:message code="relatorio.avaliacaoEficaciaPorEvento.label" /></a></li>  --%>
								
								 <li><a href="<c:url value="/relatorio/avaliacoesReacaoParticipantePorEvento/"/>"><spring:message code="relatorio.avaliacaoParticipanteEvento.label" /></a></li> 
								<!-- <li><a href="<c:url value="/relatorio/impressaoDeCapas/"/>"><spring:message code="relatorio.capasDeEventos.label" /></a></li> 	-->		
								<li><a href="<c:url value="/relatorio/impressaoCrachas/"/>"><spring:message code="relatorio.crachas.label" /></a></li>
								<li><a href="<c:url value="/relatorio/cronograma/"/>"><spring:message code="relatorio.cronograma.label" /></a></li>
								<li><a href="<c:url value="/certificado/impressaoInstrutor/"/>"><spring:message code="relatorio.certificadosInstrutores.label" /></a></li>
								<li><a href="<c:url value="/relatorio/impressaoDeclaracoes/"/>"><spring:message code="relatorio.declaracoes.label" /></a></li>
								<li><a href="<c:url value="/relatorio/eventos/"/>"><spring:message code="relatorio.evento.label" /></a></li>
								<li><a href="<c:url value="/relatorio/eventosPorInstrutor/"/>"><spring:message code="relatorio.eventoInstrutor.label" /></a></li>
								<li><a href="<c:url value="/relatorio/eventosPorParticipante/"/>"><spring:message code="relatorio.eventoParticipante.label" /></a></li>
								<li><a href="<c:url value="/relatorio/fichaTecnicaDeEvento/"/>"><spring:message code="relatorio.fichaTecnicaDeEvento.label" /></a></li>
								<li><a href="<c:url value="/relatorio/gastos/"/>"><spring:message code="relatorio.gasto.label" /></a></li>					
								<li>
									<a href="#"><spring:message code="relatorio.indicadores.label" /></a>
									<ul>
										<li><a href="<c:url value="/relatorio/indicadoresPlanejamento/"/>"><spring:message code="relatorio.indicadoresPlanejamento.label" /></a></li>
										<li><a href="<c:url value="/relatorio/indicadoresDesempenho/"/>"><spring:message code="relatorio.indicadoresDesempenho.label" /></a></li>
										<li><a href="<c:url value="/relatorio/indicadoresOutros/"/>"><spring:message code="relatorio.indicadoresOutros.label" /></a></li>
									</ul>
								</li>					
								<li><a href="<c:url value="/relatorio/participantes/"/>"><spring:message code="relatorio.participantes.label" /></a></li>
								<li><a href="<c:url value="/relatorio/participantesExternos/"/>"><spring:message code="relatorio.participantesExternos.label" /></a></li>
								<li><a href="<c:url value="/relatorio/inscricoes/"/>"><spring:message code="relatorio.preInscricoes.label" /></a></li>
								<li><a href="<c:url value="/relatorio/registroFrequencia/"/>"><spring:message code="relatorio.registroFrequencia.label" /></a></li>
								<li><a href="<c:url value="/relatorio/participantesPorTipoPorEvento/"/>"><spring:message code="relatorio.resumoEvento.label" /></a></li>
								<li><a href="<c:url value="/relatorio/solicitacoes/"/>"><spring:message code="relatorio.solicitacoes.label" /></a></li>												
									
								<li class="last">
			                        <img class="corner_left" alt="" src="<c:url value="/static/images/corner_left.png"/>"/>
			                        <img class="middle" alt="" src="<c:url value="/static/images/dot.gif"/>"/>
			                        <img class="corner_right" alt="" src="<c:url value="/static/images/corner_right.png"/>"/>
			                    </li>
			                </ul>
			            </li>
				</sec:authorize>
				
				<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS">
					<li>
						<a href="#"><spring:message code="tabelas.label" /></a>
						<ul id="help">
							<li><img class="corner_inset_left" alt="" src="<c:url value="/static/images/corner_inset_left.png"/>" /> 
								<a href="<c:url value="/areaconhecimento/"/>"><spring:message code="areaConhecimento.label" /></a> 
								<img class="corner_inset_right" alt="" src="<c:url value="/static/images/corner_inset_right.png"/>" />
							</li>
							<li><a href="<c:url value="/eixotematico/"/>"><spring:message code="eixoTematico.label" /></a></li>
							<li><a href="<c:url value="/programa/"/>"><spring:message code="programa.label" /></a></li>
							<%-- <li><a href="<c:url value="/cidade/"/>"><spring:message code="cidade.label" /></a></li> --%>
							<li><a href="<c:url value="/pais/"/>"><spring:message code="pais.label" /></a></li>
							<li><a href="<c:url value="/fontegasto/"/>"><spring:message code="fonteGasto.label" /></a></li>
							<li><a href="<c:url value="/formacaoacademica/"/>"><spring:message code="formacaoAcademica.label" /></a></li>
							<li><a href="<c:url value="/provedorevento/"/>"><spring:message code="provedorEvento.label" /></a></li>
							<li><a href="<c:url value="/questao/"/>"><spring:message code="questao.label" /></a></li>
							<li><a href="<c:url value="/tipoevento/"/>"><spring:message code="tipoEvento.label" /></a></li>
							<li><a href="<c:url value="/tipogasto/"/>"><spring:message code="tipoGasto.label" /></a></li>
							<li><a href="<c:url value="/tipolocalizacaoevento/"/>"><spring:message code="tipoLocalizacaoEvento.label" /></a></li>
							<li><a href="<c:url value="/responsavelsetor/"/>"><spring:message code="responsavelSetor.label" /></a></li>
							<li>
								<a href="#"><spring:message code="meta.indicadores.label" /></a>
								<ul>
									<li><a href="<c:url value="/meta/planejamentoEstrategico/"/>"><spring:message code="meta.planejamentoEstrategico.label" /></a></li>
									<li><a href="<c:url value="/meta/desempenhoProdutividade/"/>"><spring:message code="meta.desempenhoProdutividade.label" /></a></li>
								</ul>
							</li>
							<li>Tabelas Estticas</li>
							<li><a href="<c:url value="/modalidade/"/>"><spring:message	code="modalidade.label" /></a></li>
							<li><a href="<c:url value="/nivelescolaridade/"/>"><spring:message code="nivelEscolaridade.label" /></a></li>
							<li><a href="<c:url value="/setor/"/>"><spring:message code="setor.label" /></a></li>
							<li><a href="<c:url value="/situacaoinstrutor/"/>"><spring:message code="situacaoInstrutor.label" /></a></li>
							<li><a href="<c:url value="/tipoinstrutor/"/>"><spring:message code="tipoInstrutor.label" /></a></li>
							<li><a href="<c:url value="/tipoquestao/"/>"><spring:message code="tipoquestao.label" /></a></li>
							<li><a href="<c:url value="/tipoareatribunal/"/>"><spring:message code="tipoAreaTribunal.label" /></a></li>
							<li><a href="<c:url value="/tipopublicoalvo/"/>"><spring:message code="tipoPublicoAlvo.label" /></a></li>
							
							<li class="last">
								<img class="corner_left" alt=""	src="<c:url value="/static/images/corner_left.png"/>" /> 
								<img class="middle" alt="" src="<c:url value="/static/images/dot.gif"/>" />
								<img class="corner_right" alt="" src="<c:url value="/static/images/corner_right.png"/>" />
							</li>
						</ul>
					</li>
				</sec:authorize>
				
				<sec:authorize ifNotGranted="ROLE_ANONYMOUS,ROLE_SERVIDOR">
					<li><a href="<c:url value="/usuario/trocarsenha"/>">Trocar Senha</a></li>
				</sec:authorize>
				
				<sec:authorize ifAnyGranted="ROLE_ADMINISTRADOR,ROLE_ADMINISTRADORCONS,ROLE_SERVIDOR,ROLE_ALUNO">
					<li><a href="#">Ajuda</a>
						<ul id="help">
							<li>
								<img class="corner_inset_left" alt="" src="<c:url value="/static/images/corner_inset_left.png"/>" />					
								<a href="<c:url value="/static/docs/Material_Treinamento_Servidores.pdf"/>"  target="_blank">Material de Treinamento (Servidores/Membros TCE-CE)</a></li>
								<li><a href="<c:url value="/static/docs/Material_Treinamento_Participante_Externo.pdf"/>" target="_blank">Material de Treinamento (Participantes Externos)</a></li>
								<li style="color: #fff; font-size: 11px; font-weight: bold">Verso: 3.9.24</br>Autor: TCE-CE</li>
								<li class="last"><img class="corner_left" alt="" src="<c:url value="/static/images/corner_left.png"/>" /> <img class="middle" alt="" src="<c:url value="/static/images/dot.gif"/>" />
								<img class="corner_right" alt="" src="<c:url value="/static/images/corner_right.png"/>" />
							</li>
						</ul>
					</li>
				</sec:authorize>
				
				<li style="padding: 0;"><img class="" alt="" src="<c:url value="/static/images/omenu_right.png"/>" /></li>
			
			</ul>
		</sec:authorize>
		</sec:authorize>
		</sec:authorize>
	
	</div>


	<div id="geral">
		<div class="bordaBox">
				<div class="bordaBox2">				
					<div class="conteudo">
					<decorator:body /></div>				
				</div>
		</div>
	</div>


	<div id="footer" style="height: 1000px;"></div>
	
	
</div>
<%-- <script type="text/javascript" src="<c:url value="/static/js/custom-select.js"/>"></script> --%>
</body>
</html>
