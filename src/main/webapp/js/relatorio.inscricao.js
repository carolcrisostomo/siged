var SIGED = SIGED || {};

SIGED.RelatorioInscricao = (function(){
	
	function RelatorioInscricao(loading, participanteConsulta) {
		this.loading = loading;
		this.participanteConsulta = participanteConsulta;
		
		this.participante = jQuery('#participanteId');
		this.evento = jQuery('#eventoid');
		this.botaoBuscar = jQuery('#buscarParticipante');
		this.tipoParticipante = jQuery('#tipoParticipanteId');
		this.data1 = jQuery('#dataPreInscricao1');
		this.data2 = jQuery('#dataPreInscricao2');
		this.divErrorMsg = jQuery('.messageErro');
		this.tipoPrograma = jQuery('#programaId');
		
		this.respIndicacao = jQuery('#responsavelPelaIndicacaoId');
		this.agruparSetorTCE = jQuery('#agruparSetorTCEId');
		this.agruparOrgao = jQuery('#agruparOrgaoId');
		this.agruparDefault = jQuery('#agruparDefaultId');
		this.agruparPrograma = jQuery('#agruparProgramaId');
		
		this.esfera = jQuery('#esferaTr');
		this.checkTodasEsferas = jQuery('#checkTodasEsferas');
	}
	
	RelatorioInscricao.prototype.iniciar = function() {
		this.divErrorMsg.hide();
		this.participanteConsulta.buscar(this.loading, false, false, '');
		this.botaoBuscar.click(onBuscarParticipante.bind(this));
		
		verificarTipoParticipante.call(this, false);
		tipoParticipanteChangeEsfera.call(this);
		onEventoChange.call(this);

		this.tipoParticipante.change(onTipoParticipanteChange.bind(this));
		this.evento.change(onEventoChange.bind(this));

		verificarTipoPrograma.call(this, false);
		this.tipoPrograma.change(onTipoProgramaChange.bind(this));
		
		this.participanteConsulta.event.bind('erro', onErroBuscarParticipante.bind(this));
	}
	
	function onBuscarParticipante() {
		this.divErrorMsg.hide();
		this.participanteConsulta.buscar(this.loading, false, true, '');
	}
	
	function onEventoChange() {
		if(this.evento.val() != '0') {
			inputDisabled(this.data1, true);
			inputDisabled(this.data2, true);
		} else {
			inputDisabled(this.data1, false);
			inputDisabled(this.data2, false);
		}
	}
	
	function onTipoProgramaChange() {
		verificarTipoPrograma.call(this, true);
	}

	function verificarTipoPrograma() {
		if(this.tipoPrograma.val() == 0) {
			inputDisabled(this.agruparPrograma, false);
		} else {
			inputDisabled(this.agruparPrograma, true);
		}
	}

	function onTipoParticipanteChange() {
		verificarTipoParticipante.call(this, true);
		tipoParticipanteChangeEsfera.call(this);
	}

	function verificarTipoParticipante(verificarAgrupar) {
		if(this.tipoParticipante.val() == 1){
			participanteServidor.call(this, verificarAgrupar);
		} else if (this.tipoParticipante.val() == 2) {
			participanteJurisdicionado.call(this, verificarAgrupar);
		} else {
			participanteOutro.call(this, verificarAgrupar);
		}
	}
	
	function participanteServidor(verificarAgrupar) {
		inputDisabled(this.respIndicacao, false);
		inputDisabled(this.agruparSetorTCE, false);
		inputDisabled(this.agruparOrgao, true);
		
		if(verificarAgrupar) {
			if(this.agruparOrgao.is(":checked") )
				this.agruparDefault.attr('checked', true);
		}
	}
	
	function participanteJurisdicionado(verificarAgrupar) {
		inputDisabled(this.respIndicacao, true);
		inputDisabled(this.agruparOrgao, false);
		inputDisabled(this.agruparSetorTCE, true);
		
		if(verificarAgrupar) {
			if(this.agruparSetorTCE.is(":checked") )
				this.agruparDefault.attr('checked', true);
		}
	}
	
	function participanteOutro(verificarAgrupar) {
		inputDisabled(this.respIndicacao, true);
		inputDisabled(this.agruparSetorTCE, true);
		inputDisabled(this.agruparOrgao, true);
		this.respIndicacao.val('0');
		
		if(verificarAgrupar) {
			if(this.agruparSetorTCE.is(":checked") || this.agruparOrgao.is(":checked") )
				this.agruparDefault.attr('checked', true);
		}
		
	}
	
	function onErroBuscarParticipante(event, messageClient, messageDev) {
		console.error(messageDev);
		mostrarErro.call(this, messageClient);
	}
	
	function mostrarErro(message) {
		this.divErrorMsg.html(message);
		this.divErrorMsg.show();
	}
	
	function inputDisabled(campo, disabled){
		if(disabled){
			campo.attr({
				'disabled':'disabled'
			});
		} else {
			campo.attr({
				'disabled':''
			});
		}
		campo.trigger('chosen:updated');
	}
	
	function tipoParticipanteChangeEsfera(){
		if (this.tipoParticipante.val() == "2") {
			displayEsfera.call(this, true);
		} else {
			displayEsfera.call(this, false);
		}
	}
	
	function displayEsfera(mostrar){
		if(mostrar){
			this.esfera.show();	
		} else {
			this.esfera.hide();
			this.checkTodasEsferas.attr('checked', 'checked');
			
		}
	}
	
	return RelatorioInscricao;
	
}());

jQuery(function(){
	var participanteUrl = jQuery('#participanteId').attr('data-participante-url');
	
	var loading = new SIGED.AjaxLoading();
	var participanteConsulta = new SIGED.ConsultaParticipantePorNome('#participanteId', '#nomeParticipante', '#participanteErro', participanteUrl);
	
	var relatorioInscricao = new SIGED.RelatorioInscricao(loading, participanteConsulta);
	relatorioInscricao.iniciar();
});