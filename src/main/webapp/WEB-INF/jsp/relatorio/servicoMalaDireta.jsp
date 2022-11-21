<%@ include file="../includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<spring:url value="/ajax/malaDireta/logEnvio" var="urlLogEnvio" />
<spring:url value="/ajax/malaDireta/progresso" var="urlProgressoEnvio" />
</head>
<body>
	<div class="body">
		<h1>Serviço de Mala direta</h1>
		<c:if test="${param.mensagem != null}">
			<div class="message info">${param.mensagem}</div>
		</c:if>
		<div class="filter">
			<div class="container-progresso" style="margin: 0 auto; width: 100%; text-align: center;">
				<progress id='progress' max='1' value='0' style="display: inline-block; margin: 10px 0; width: 100%; height: 20px;"></progress>
			</div>
			
			<div class="container-log--envio">
				<ul></ul>
				<input id="mostrar_log" type="button" class="botao" value="Mostrar log" />
			</div>
		</div>
		
		
	</div>
	<script type="text/javascript">
		jQuery(document).ready(function($) {
			
			$('#mostrar_log').click(function(e) {
				e.preventDefault();
				var target = $(this);
				if(target.val() === 'Mostrar log') {
					target.val('Atualizar log');
				}
				atualizarLog();
			});
			
			progressoEnvio();
			var refreshProgresso = setInterval(progressoEnvio,10000);
			
			function atualizarLog() {
				var urlLog = '${urlLogEnvio}';
				var logEnvio = $('.container-log--envio ul')
				$.ajax({
					url : urlLog,
					success : function(log) {
						var len = log.length;
						var lista = "";
						for (var i = 0; i < len; i++) {
							lista += "<li>" + log[i] + "</li>"
						}
						
						logEnvio.html(lista);
					},
					error : function(error) {
						console.log(error);
					}
				});
			}
			
			function progressoEnvio() {
				var urlProgresso = '${urlProgressoEnvio}';
				var $progress = $('#progress');
				$.ajax({
					url : urlProgresso,
					success : function(progresso) {
						if(progresso == 1) {
							clearInterval(refreshProgresso);
						}
						$progress.val(progresso);
					},
					error : function(error) {
						console.log(error);
					}
				});
			}
		});
	</script>
</body>
</html>