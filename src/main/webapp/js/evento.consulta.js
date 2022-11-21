var SIGED = SIGED || {};

SIGED.ConsultaEvento = (function(){
	function ConsultaEvento() {
		this.event = jQuery({});
	}
	
	ConsultaEvento.prototype.isCurso = function(loading, idEvento, callback, callbackError) {
		if (loading) {
			loading.start();			
		}
		this.url = '/ajax/evento/' + idEvento + '/isCurso';
		this.titulo = titulo;
		jQuery.ajax({
			type : "GET",
			url : this.url,
			contentType: 'application/json',
			success : function(isCurso) {
				this.event.trigger('isCurso', [isCurso]);
				callback(isCurso);
			}.bind(this),
			error : function(data) {
				loading.stop();
				var messageClient = "Erro na consulta"
				var messageDev = "Erro na consulta (url: " + this.url + ")";
				this.event.trigger('isCursoError', [messageClient, messageDev]);
			}.bind(this),
			complete : function() {
				if (loading) {			
					loading.stop();
				}
			}.bind(this)
		});
	}
	
	return ConsultaEvento;
	
}());

SIGED.ConsultaEventoPorTitulo = (function(){
	function ConsultaEventoPorTitulo(evento_hook) {
		this.url = '';
		this.titulo = '';
		this.clean = '<option value="0">Selecione...</option>';
		this.evento = jQuery(evento_hook);
		this.event = jQuery({});
	}
	
	ConsultaEventoPorTitulo.prototype.buscar = function(loading, url, titulo) {
		loading.start();
		this.url = url;
		this.titulo = titulo;
		jQuery.ajax({
			url : this.url,
			data : {titulo : this.titulo},
			contentType: 'application/json',
			success : onConsultaEventoSuccess.bind(this, loading),
			error : onConsultaError.bind(this, loading)
		});
	}
	ConsultaEventoPorTitulo.prototype.limpar = function() {
		this.evento.html(this.clean);
	}
	
	function onConsultaEventoSuccess(loading, eventos) {
		var html = this.clean;
		var totalEventos = eventos.length;
		for(var i = 0; i < eventos.length; i++) {
			html += '<option value="' + eventos[i].id + '">' + eventos[i].nome + '</option>';
		}
		this.evento.html(html).trigger('chosen:updated');
		loading.stop();
		
		this.event.trigger('sucesso', [totalEventos]);
	}
	
	function onConsultaError(loading, data){
		loading.stop();
		var messageClient = "Erro na consulta de participante"
		var messageDev = "Erro na consulta de evento (url: " + this.url + ", titulo: " + this.titulo + ")";
		this.event.trigger('erro', [messageClient, messageDev]);
	}
	
	return ConsultaEventoPorTitulo;
	
}());