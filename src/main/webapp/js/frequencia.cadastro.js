var SIGED = SIGED || {};

SIGED.FrequenciaCadastro = (function(){
	function FrequenciaCadastro(loading, moduloConsulta, participanteConsulta, turnoConsulta) {
		this.loading = loading;
		this.evento = jQuery('.js_frequencia_evento');
		this.modulo = jQuery('.js_frequencia_modulo');
		this.participante = jQuery('.js_frequencia_participante');
		this.divErrorMsg = jQuery('.messageErro');
		
		this.moduloConsulta = moduloConsulta;
		this.participanteConsulta = participanteConsulta;
		this.turnoConsulta = turnoConsulta;
	}
	
	FrequenciaCadastro.prototype.iniciar = function(){
		this.divErrorMsg.hide();
		this.evento.change(onEventoSelecionado.bind(this));
		this.modulo.change(onModuloSelecionado.bind(this));
		this.participanteConsulta.event.bind('sucesso', onConsultaParticipanteSuccess.bind(this));
		this.participanteConsulta.event.bind('erro', onConsultaParticipanteError.bind(this));
		this.moduloConsulta.event.bind('erro', onConsultaModuloError.bind(this));
		this.turnoConsulta.event.bind('erro', onConsultaTurnoError.bind(this));
	}
	
	function onEventoSelecionado() {
		this.divErrorMsg.hide();
		limpar.call(this);
		var eventoId = this.evento.val();
		if(eventoId != 0) {
			this.moduloConsulta.buscar(this.loading);
			this.participanteConsulta.buscar(this.loading);
		} else {
			this.participante.attr('size', 0);
		}
	}
	
	function onModuloSelecionado() {
		this.divErrorMsg.hide();
		var moduloId = this.modulo.val();
		if(moduloId != 0) {
			this.turnoConsulta.buscar(this.loading);
		} else {
			this.turnoConsulta.limpar();
		}
	}
	
	function onConsultaParticipanteSuccess(event, totalParticipantes) {
		this.participante.attr('size', totalParticipantes);
	}
	
	function onConsultaParticipanteError(event, messageClient, messageDev) {
		this.divErrorMsg.html(messageClient);
		this.divErrorMsg.show();
		console.error(messageDev);
	}
	
	function onConsultaModuloError(event, messageClient, messageDev) {
		this.divErrorMsg.html(messageClient);
		this.divErrorMsg.show();
		console.error(messageDev);
	}
	
	function onConsultaTurnoError(event, messageClient, messageDev) {
		this.divErrorMsg.html(messageClient);
		this.divErrorMsg.show();
		console.error(messageDev);
	}
	
	function limpar() {
		this.moduloConsulta.limpar();
		this.turnoConsulta.limpar();
		this.participanteConsulta.limpar();
	}
	
	return FrequenciaCadastro;
}());

jQuery(function(){
	var moduloUrl = jQuery('.js_frequencia_evento').attr('data-modulo-url');
	var participanteUrl = jQuery('.js_frequencia_evento').attr('data-participante-url');
	var turnoUrl = jQuery('.js_frequencia_modulo').attr('data-turno-url');
	
	var loading = new SIGED.AjaxLoading();
	var moduloConsulta = new SIGED.ConsultaModuloPorEvento('.js_frequencia_modulo', '.js_frequencia_evento', moduloUrl);
	var participanteConsulta = new SIGED.ConsultaParticipantePorEvento('.js_frequencia_participante', '.js_frequencia_evento', participanteUrl);
	var turnoConsulta = new SIGED.ConsultaTurnoPorModulo('.js_frequencia_turno', '.js_frequencia_modulo', turnoUrl);
	
	var frequencia = new SIGED.FrequenciaCadastro(loading, moduloConsulta, participanteConsulta, turnoConsulta);
	frequencia.iniciar();
});