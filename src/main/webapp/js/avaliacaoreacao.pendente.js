var SIGED = SIGED || {};

SIGED.AvaliacaoReacaoPendente = (function() {
	
	function AvaliacaoReacaoPendente() {
		this.inputEmissaoCertificado = jQuery(".input_emissao_certificado");
	}
	
	AvaliacaoReacaoPendente.prototype.autobind = function() {
		this.inputEmissaoCertificado.click(onInputClicado.bind(this));
	}
	
	function onInputClicado(event) {
		event.preventDefault();
		var inputClicado = jQuery(event.currentTarget);
		var evento_id = inputClicado.attr('data-evento');
		var participante_id = inputClicado.attr('data-participante');
		var url = inputClicado.attr('data-urlAjax');
		var urlAvaliacao = inputClicado.attr('data-urlAvaliacao');
		
		this.buscar(url, urlAvaliacao, '#form_emissao_certificado_' + evento_id);
	}
	
	AvaliacaoReacaoPendente.prototype.buscar = function(urlConsulta, urlAvaliacao, form_emissao_certificado_hook) {
		var form = jQuery(form_emissao_certificado_hook);
		
		jQuery.ajax({
			url : urlConsulta,
			contentType: 'application/json',
			success : onConsultaAvaliacaoConcluida.bind(this, urlAvaliacao, form),
			error : onConsultaAvaliacaoErro.bind(this, form)
		});
	}
	
	function onConsultaAvaliacaoConcluida(urlAvaliacao, form, data) {
		if(data.pendentes > 0) {
			// Existe 1 avaliação de reação pendente para este evento. Para a obtenção do certificado é necessário realizar a avaliação!
			// Existem X avaliações de reação pendentes para este evento. Para a obtenção do certificado é necessário realizar a avaliação!
			if(data.pendentes == 1) {
				mensagem = "Existe " + data.pendentes + " avalia\u00E7\u00E3o de rea\u00E7\u00E3o pendente para este evento. Para a obten\u00E7\u00E3o do certificado \u00e9 necess\u00e1rio realizar a avalia\u00E7\u00E3o!";
			} else {
				mensagem = "Existem " + data.pendentes + " avalia\u00E7\u00F5es de rea\u00E7\u00E3o pendentes para este evento. Para a obten\u00E7\u00E3o do certificado \u00e9 necess\u00e1rio realizar a avalia\u00E7\u00E3o!";
			}
            swal({
                  title: "Avalia\u00E7\u00E3o de rea\u00E7\u00E3o pendente",
                  text: mensagem,
                  type: "warning",
                  showCancelButton: false,
                  confirmButtonColor: "#2A6098",
                  confirmButtonText: "Avaliar!",
                  // cancelButtonText: "N\u00E3o quero avaliar",
                  closeOnConfirm: true,
                  closeOnCancel: false
                },
            function(isConfirm){
                if (isConfirm) {
                    window.location.href = urlAvaliacao + data.eventoId + "/" + data.moduloId + "/" + data.participanteId + "/?gcda=true";
                } else {
                    // form.submit();
                }
            });
			// window.location.href = urlAvaliacao + data.eventoId + "/" + data.moduloId + "/" + data.participanteId + "/?gcda=true";
		} else {
			form.submit();
		}		
	}

	function onConsultaAvaliacaoErro(form, data) {
		form.submit();
	}
	
	return AvaliacaoReacaoPendente;
	
}());