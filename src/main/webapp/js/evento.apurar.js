var SIGED = SIGED || {};

SIGED.ApuracaoDesempenhoModulo = (function() {
	
	function ApuracaoDesempenhoModulo(ajax) {
		this.eventoSelect = jQuery('.js_evento_select');
		this.botaoApurar = jQuery('#apurar');
		this.divApurar = jQuery('#apurar-div');
		this.status = "";
		this.eventoRealizadoUrl = this.eventoSelect.attr('data-evento_realizado_url');
		this.eventoApuradoUrl = this.eventoSelect.attr('data-evento_apurado_url');
		this.ajax = ajax;
	}
	
	ApuracaoDesempenhoModulo.prototype.iniciar = function() {
		desabilitarBotaoApurar.call(this);
		this.eventoSelect.change(onEventoSelect.bind(this));
	}
	
	function onEventoSelect(event) {
		var evento_id = this.eventoSelect.val();
		this.status = '';
		if(evento_id != 0) {
			ajaxStart.call(this);
			jQuery.ajax({
				url : this.eventoRealizadoUrl,
				data : {id: evento_id},
				contentType: 'application/json',
				success : onConsultaEventoRealizadoSuccess.bind(this),
				error : onConsultaError.bind(this)
			});
		} else {
			limparDivApurar.call(this);
			desabilitarBotaoApurar.call(this);
		}
	}
	
	function onConsultaEventoRealizadoSuccess(isRealizado) {
		if(isRealizado) {
			this.status = 'Evento realizado: ';
		} else {
			this.status = 'Evento em andamento: ';
		}
		
		var evento_id = this.eventoSelect.val();
		jQuery.ajax({
			url : this.eventoApuradoUrl,
			data : {id: evento_id},
			contentType: 'application/json',
			success : onConsultaEventoApuradoSuccess.bind(this),
			error : onConsultaError.bind(this)
		});
	}
	
	function onConsultaEventoApuradoSuccess(statusApuracao) {
		ajaxStop.call(this);
		
		if(statusApuracao == 1) {
			this.status += 'j\u00E1 apurado';
		} else if(statusApuracao == 2) {
			this.status += 'apurado parcialmente';
		} else {
			this.status += 'n\u00E3o apurado';
		}
		mensagemDivApurar.call(this, this.status)
	}
	
	function onConsultaError() {
		ajaxStop.call(this);
		this.status=''
		mensagemDivApurar.call(this, "Erro na consulta. Tente Novamente")
	}
	
	function mensagemDivApurar(mensagem) {
		var html = '<span style="color: red; font-weight: bold;">' + mensagem + '</span>';
		this.divApurar.html(html);
	}
	
	function limparDivApurar(){
		this.divApurar.html('');
	}
	
	function ajaxStart(){
		desabilitarBotaoApurar.call(this);
		this.ajax.start();
	}
	
	function ajaxStop(){
		if(this.eventoSelect.val() != 0) {
			habilitarBotaoApurar.call(this);
		}
		this.ajax.stop();
	}
	
	function habilitarBotaoApurar(){
		this.botaoApurar.removeAttr('disabled');
	}
	
	function desabilitarBotaoApurar(){
		this.botaoApurar.attr('disabled', 'disabled');
	}
	
	return ApuracaoDesempenhoModulo;
	
}());

jQuery(function() {
	var ajaxLoadind = new SIGED.AjaxLoading();
	var apuracaoDesempenhoModulo = new SIGED.ApuracaoDesempenhoModulo(ajaxLoadind);
	apuracaoDesempenhoModulo.iniciar();
});