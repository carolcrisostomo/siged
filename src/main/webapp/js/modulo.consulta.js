var SIGED = SIGED || {};

SIGED.ConsultaModuloPorEvento = (function(){
	function ConsultaModuloPorEvento(modulo_hook, evento_hook, url) {
		this.url = url;
		this.modulo = jQuery(modulo_hook);
		this.evento = jQuery(evento_hook);
		this.optionClean = '<option value="0">Selecione...</option>';
		this.event = jQuery({});
	}
	
	ConsultaModuloPorEvento.prototype.buscar = function(loading) {
		loading.start();
		jQuery.ajax({
			url : this.url,
			data : {eventoId: this.evento.val()},
			contentType: 'application/json',
			success : onConsultaModuloSuccess.bind(this, loading),
			error : onConsultaError.bind(this, loading)
		});
	}
	
	ConsultaModuloPorEvento.prototype.limpar = function() {
		this.modulo.html(this.optionClean).trigger('chosen:updated');
		this.event.trigger('limpo');
	}
	
	ConsultaModuloPorEvento.prototype.setOptionDefault = function(optionDefault) {
		this.optionClean = optionDefault;
	}
	
	function onConsultaModuloSuccess(loading, data) {
		var html = this.optionClean;
		var quantidadeModulos = data.length;
		for (var i = 0; i < data.length; i++) {
			html += '<option value="' + data[i].id + '">' + data[i].titulo + '</option>';
		}
		this.modulo.html(html).trigger('chosen:updated');
		loading.stop();
		
		this.event.trigger('sucesso', [quantidadeModulos]);
	}
	
	function onConsultaError(loading, data){
		loading.stop();
		var messageClient = "Erro na consulta do m\u00F3dulo";
		var messageDev = "Erro na consulta do m\u00F3dulo (url: " + this.url + ", eventoId: " + this.evento.val() + ")";
		this.event.trigger('erro', [messageClient, messageDev]);
	}
	
	return ConsultaModuloPorEvento;
	
}());

SIGED.ConsultaTurnoPorModulo = (function(){
	function ConsultaTurnoPorModulo(turno_hook, modulo_hook, url) {
		this.url = url;
		this.modulo = jQuery(modulo_hook);
		this.turno = jQuery(turno_hook);
		this.event = jQuery({});
	}
	
	ConsultaTurnoPorModulo.prototype.buscar = function(loading) {
		loading.start();
		jQuery.ajax({
			url : this.url,
			data : {moduloId: this.modulo.val()},
			contentType: 'application/json',
			success : onConsultaTurnoSuccess.bind(this, loading),
			error : onConsultaError.bind(this, loading)
		});
	}
	ConsultaTurnoPorModulo.prototype.limpar = function() {
		this.turno.html('').trigger('chosen:updated');
	}
	
	function onConsultaTurnoSuccess(loading, data) {
		var html = '';
		var len = data.turnos.length;
		for ( var i = 0; i < len; i++) {
			html += '<option value="' + data.turnos[i] + '">' + data.turnos[i] + '</option>';
		}
		this.turno.html(html).trigger('chosen:updated');
		loading.stop();
	}
	
	function onConsultaError(loading, data){
		loading.stop();
		var messageClient = "Erro na consulta do turno";
		var messageDev = "Erro na consulta do turno (url: " + this.url + ", moduloId: " + this.modulo.val() + ")";
		this.event.trigger('erro', [messageClient, messageDev]);
	}
	
	return ConsultaTurnoPorModulo;
	
}());


SIGED.ConsultaInstrutorPorModulo = (function(){
	function ConsultaInstrutorPorModulo(instrutor_hook, modulo_hook, url) {
		this.url = url;
		this.instrutor = jQuery(instrutor_hook);
		this.modulo = jQuery(modulo_hook);
		this.optionClean = '<option value="0">Selecione...</option>';
		this.event = jQuery({});
	}
	
	ConsultaInstrutorPorModulo.prototype.buscar = function(loading) {
		loading.start();
		jQuery.ajax({
			url : this.url,
			data : {moduloId: this.modulo.val()},
			contentType: 'application/json',
			success : onConsultaInstrutorSuccess.bind(this, loading),
			error : onConsultaError.bind(this, loading)
		});
	}
	ConsultaInstrutorPorModulo.prototype.limpar = function() {
		this.instrutor.html(this.optionClean).trigger('chosen:updated');
		this.event.trigger('limpo');
	}
	
	ConsultaInstrutorPorModulo.prototype.setOptionDefault = function(optionDefault) {
		this.optionClean = optionDefault;
	}
	
	function onConsultaInstrutorSuccess(loading, data) {
		var html = this.optionClean;
		var len = data.length;
		for ( var i = 0; i < len; i++) {
			html += '<option value="' + data[i].id + '">' + data[i].nome + '</option>';
		}
		this.instrutor.html(html).trigger('chosen:updated');
		loading.stop();
		
		this.event.trigger('sucesso');
	}
	
	function onConsultaError(loading, data){
		loading.stop();
		var messageClient = "Erro na consulta do instrutor";
		var messageDev = "Erro na consulta do instrutor (url: " + this.url + ", moduloId: " + this.modulo.val() + ")";
		this.event.trigger('erro', [messageClient, messageDev]);
	}
	
	return ConsultaInstrutorPorModulo;
	
}());