var SIGED = SIGED || {};

SIGED.ConsultaParticipantePorFiltro = (function(){
	function ConsultaParticipantePorFiltro(url) {
		this.url = url;
		this.event = jQuery({});
	}
	
	ConsultaParticipantePorFiltro.prototype.buscar = function(loading, filtro) {
		loading.start();
		jQuery.ajax({
			type : "POST",
			url : this.url,
			dataType: "json",
			contentType : 'application/json',
			success : onConsultaParticipanteSuccess.bind(this, loading),
			error : onConsultaParticipanteError.bind(this, loading, filtro),
			data : JSON.stringify(filtro)
		});
	}
	
	function onConsultaParticipanteSuccess(loading, participantes) {
		loading.stop();
		this.event.trigger('sucesso', [participantes]);
	}
	
	function onConsultaParticipanteError(loading, filtro, data){
		loading.stop();
		var messageClient = "Erro na consulta de participante";
		var messageDev = "Erro na consulta de participante (url: " + this.url + ", filtro: " + filtro + ")";
		this.event.trigger('erro', [messageClient, messageDev]);
	}
	
	return ConsultaParticipantePorFiltro;
}());

SIGED.ConsultaParticipantePorEvento = (function(){
	function ConsultaParticipantePorEvento(participante_hook, evento_hook, url) {
		this.url = url;
		this.participante = jQuery(participante_hook);
		this.evento = jQuery(evento_hook);
		this.event = jQuery({});
	}
	
	ConsultaParticipantePorEvento.prototype.buscar = function(loading) {
		loading.start();
		jQuery.ajax({
			url : this.url,
			data : {eventoId: this.evento.val()},
			contentType: 'application/json',
			success : onConsultaParticipanteSuccess.bind(this, loading),
			error : onConsultaError.bind(this, loading)
		});
	}
	ConsultaParticipantePorEvento.prototype.limpar = function() {
		this.participante.html('').trigger('chosen:updated');
	}
	
	function onConsultaParticipanteSuccess(loading, data) {
		html = '<option value="0">Selecione...</option>';
		var totalParticipantes = data.length;
		for ( var i = 0; i < data.length; i++) {
			html += '<option value="' + data[i].id + '">' + data[i].nomeCpf + '</option>';
		}
		this.participante.html(html).trigger('chosen:updated');
		loading.stop();
		
		this.event.trigger('sucesso', [totalParticipantes]);
	}
	
	function onConsultaError(loading, data){
		loading.stop();
		var messageClient = "Erro na consulta de participante";
		var messageDev = "Erro na consulta de participante (url: " + this.url + ", eventoId: " + this.evento.val() + ")";
		this.event.trigger('erro', [messageClient, messageDev]);
	}
	
	return ConsultaParticipantePorEvento;
	
}());

SIGED.ConsultaParticipantePorNome = (function(){
	function ConsultaParticipantePorNome(participante_hook, participanteNome_hook, participanteErro, url) {
		this.participante = jQuery(participante_hook);
		this.participanteNome = jQuery(participanteNome_hook);
		this.participanteErro = jQuery(participanteErro);
		this.url = url;
		this.forceSelect = false;
		this.participanteId = '';
		this.event = jQuery({});
	}
	
	ConsultaParticipantePorNome.prototype.buscar = function(loading, forceSelect, mostrarAlerta, participanteId) {
		this.forceSelect = forceSelect;
		this.participanteId = participanteId;
		var nome = replaceSpecialChars(this.participanteNome.val());
		
		if(nome.length > 2) {
			loading.start();
			jQuery.ajax({
				url : this.url,
				data : {nome : nome},
				success : onConsultaParticipanteSuccess.bind(this, loading),
				error : onConsultaError.bind(this, loading)
			});
		} else if (mostrarAlerta) {
			loading.stop();
			alert("Digite pelo menos 3 caracteres!");
		}
		
	}
	
	function onConsultaParticipanteSuccess(loading, data) {
		var html;
		if (this.forceSelect) {
			html = '<option value="0">Selecione...</option>';
		} else {
			html = '<option value="0">TODOS</option>';
		}
		
		var len = data.length;
		for ( var i = 0; i < len; i++) {
			if (data[i].id == this.participanteId) {
				html += '<option value="' + data[i].id + '" selected>'
						+ data[i].nomeCpf + '</option>';
			} else {
				html += '<option value="' + data[i].id + '">'
						+ data[i].nomeCpf + '</option>';
			}
		}
		
		if(len == 0){
			this.participanteErro.html(' Nenhum participante encontrado').trigger('chosen:updated');
		}else{
			this.participanteErro.html('').trigger('chosen:updated');
		}
		
		this.participante.html(html).trigger('chosen:updated');
		loading.stop();
	}
	
	function onConsultaError(loading, data){
		loading.stop();
		var messageClient = "Erro na consulta de participante"
		var messageDev = "Erro na consulta de participante (url: " + this.url + ", nome: " + this.participanteNome.val() + ")";
		this.event.trigger('erro', [messageClient, messageDev]);
	}
	
	function replaceSpecialChars(str) {
		
		var specialChars = [
						    {val:"a",let:"áàãâä"},
						    {val:"e",let:"éèêë"},
						    {val:"i",let:"íìîï"},
						    {val:"o",let:"óòõôö"},
						    {val:"u",let:"úùûü"},
						    {val:"c",let:"ç"},
						    {val:"A",let:"ÁÀÃÂÄ"},
						    {val:"E",let:"ÉÈÊË"},
						    {val:"I",let:"ÍÌÎÏ"},
						    {val:"O",let:"ÓÒÕÔÖ"},
						    {val:"U",let:"ÚÙÛÜ"},
						    {val:"C",let:"Ç"},
						    {val:"",let:"?!()"}
						    ];
		
		var $spaceSymbol = ' ';
		var regex;
		var returnString = str;
		for (var i = 0; i < specialChars.length; i++) {
			regex = new RegExp("["+specialChars[i].let+"]", "g");
			returnString = returnString.replace(regex, specialChars[i].val);
			regex = null;
		}
		return returnString.replace(/\s/g,$spaceSymbol);
	}
	
	return ConsultaParticipantePorNome;
	
}()); 