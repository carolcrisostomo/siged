var SIGED = SIGED || {};

SIGED.InscricaoEmailConfirmacao = (function() {
	
	function InscricaoEmailConfirmacao() {
		this.botaoEnviarEmail = jQuery('.js_enviar_email');
		this.images_path = "/static/images/";
		this.image = null;
	}
	
	InscricaoEmailConfirmacao.prototype.iniciar = function() {
		this.botaoEnviarEmail.click(onBotaoClicado.bind(this));
	}
	
	function onBotaoClicado(event) {
		event.preventDefault();
		var botaoClicado = jQuery(event.currentTarget);
		var url = botaoClicado.attr('href');
		this.image = jQuery(".js_email_img_" + botaoClicado.attr('data-imgcontrol'));
		
		swal({
			  title: "Deseja enviar o e-mail de confirma\u00E7\u00E3o?",
			  text: "Clique em \"Sim\" para enviar o e-mail de confirma\u00E7\u00E3o de inscri\u00E7\u00E3o para o participante ou outro interessado (neste caso, informe o e-mail abaixo)",
			  type: "input",
			  allowEscapeKey: true,
			  allowOutsideClick: true,
			  showCancelButton: true,
			  confirmButtonColor: "#2A6098",
			  confirmButtonText: "Sim",
			  cancelButtonText: "N\u00E3o",
			  closeOnConfirm: false,
			  closeOnCancel: true,
			  inputPlaceholder: "E-mail"
		},
		function(inputValue){
			if (inputValue === false) {
				return false;
			}
			if (inputValue === "") {
				url = url + '?email=';
			} else {
				if (validateEmail(inputValue)) {
					url = url + '?email=' + inputValue;
				} else {
					swal.showInputError("E-mail inv\u00E1lido");
					return false;
				}
			}
			spinnerStart.call(this);
			jQuery.ajax({
				url : url,
				contentType: 'application/json',
				success : onEmailEnviado.bind(this),
				error : onEmailEnviadoErro.bind(this)
			});
			swal.close();
		}.bind(this));
		
	}
	
	function onEmailEnviado(http_status) {
		if(http_status === 'OK') {
			spinnerStop.call(this);
			swal({
				title: "Sucesso!",
				text: "E-mail de confirma\u00E7\u00E3o enviado com sucesso.",
				type: "success",
				allowEscapeKey: true,
				allowOutsideClick: true,
				confirmButtonText: "OK",
				confirmButtonColor: "#2A6098"
			});
		} else if(http_status === 'BAD_REQUEST') {
			spinnerStop.call(this);
			swal({
				title: "E-mail n\u00E3o enviado!",
				text: "Par\u00E2metros inv\u00E1lidos na requisi\u00E7\u00E3o.",
				type: "warning",
				allowEscapeKey: true,
				allowOutsideClick: true,
				confirmButtonText: "Fechar",
				confirmButtonColor: "#F8BB86"
			});
		} else {
			spinnerStop.call(this);
			swal({
				title: "E-mail n\u00E3o enviado!",
				text: "Erro ao processar o envio.",
				type: "error",
				allowEscapeKey: true,
				allowOutsideClick: true,
				confirmButtonText: "Fechar",
				confirmButtonColor: "#F27474"
			});
		}
	}

	function onEmailEnviadoErro(data) {
		spinnerStop.call(this);
		swal({
			  title: "E-mail n\u00E3o enviado!",
			  text: "Erro ao processar requisi\u00E7\u00E3o.",
			  type: "error",
			  allowEscapeKey: true,
			  allowOutsideClick: true,
			  confirmButtonText: "Fechar",
			  confirmButtonColor: "#F27474"
		});
	}
	
	function spinnerStart() {
		if(this.image != null) {
			this.image.attr('src', this.images_path + "spinner.gif");
		}
	}
	
	function spinnerStop() {
		if(this.image != null) {
			this.image.attr('src', this.images_path + "send_email.png");
		}
	}
	
	function validateEmail(email) {
	    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	    return re.test(String(email).toLowerCase());
	}
	
	return InscricaoEmailConfirmacao;
	
}());

jQuery(function() {
	var inscricaoEmailConfirmacao = new SIGED.InscricaoEmailConfirmacao();
	inscricaoEmailConfirmacao.iniciar();
});