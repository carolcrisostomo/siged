var SIGED = SIGED || {};

SIGED.GastoInstrutor = (function(){
	
	function GastoInstrutor() {
		this.selectTipoGasto = jQuery("#tipoId");
		this.selectInstrutor = jQuery("#instrutorId");
		this.selectEvento = jQuery("#eventoId");
		this.optionDefault = decode_utf8("<option value='0'>Não se Aplica</option>");
		this.spinner = jQuery(".spinnerLoad");
	}
	
	GastoInstrutor.prototype.iniciar = function() {
		onGastoChange.call(this);
		this.selectTipoGasto.change(onGastoChange.bind(this));
		this.selectEvento.change(onGastoChange.bind(this));
	}
	
	function onGastoChange() {
		var tipoGasto = this.selectTipoGasto.val();
		var eventoId = this.selectEvento.val();
		var url = this.selectInstrutor.attr("data-url");
		var interno = this.selectInstrutor.attr("data-interno");
		var externo = this.selectInstrutor.attr("data-externo");
		
		if((tipoGasto == 7 || tipoGasto == 8) && (eventoId != null && eventoId > 0)) {
			if(tipoGasto == 7) {
				url += "/" + interno + "/" + eventoId;
			} else if(tipoGasto == 8) {
				url += "/" + externo + "/" + eventoId;
			}
			mostrarLoad.call(this);
			
			jQuery.ajax({
				url : url ,
				contentType: 'application/json',
				success : onBuscaInstrutoresConcluida.bind(this),
				error: onBuscaInstrutoresError.bind(this)
			});
		} else {
			resetAll.call(this);
		}
	}
	
	function onBuscaInstrutoresConcluida(instrutores) {
		var instrutorSelected = this.selectInstrutor.attr("data-selected");
		if(instrutores.length > 0){
			options = "<option value='0'>Selecione...</option>";
			instrutores.each(function(i){
				if(instrutorSelected == i.id) {
					options += "<option value='" + i.id + "' selected='selected'>" + i.nome + "</option>";
				} else {
					options += "<option value='" + i.id + "'>" + i.nome + "</option>";
				}
			});
			esconderLoad.call(this);
			habilitarInstrutor.call(this);
			this.selectInstrutor.html(options).trigger('chosen:updated');
		} else {
			resetAll.call(this);
		}
		
	}
	
	function onBuscaInstrutoresError() {
		alert("Error na consulta");
		resetAll.call(this);
	}
	
	function resetAll(){
		this.selectInstrutor.html(this.optionDefault).trigger('chosen:updated');
		desabilitarInstrutor.call(this);
		esconderLoad.call(this);
	}
	
	function desabilitarInstrutor(){
		this.selectInstrutor.attr("disabled", "disabled");
		this.selectInstrutor.trigger('chosen:updated');
	}
	
	function habilitarInstrutor(){
		this.selectInstrutor.attr("disabled", false);
		this.selectInstrutor.trigger('chosen:updated');
	}
	
	function mostrarLoad(){
		this.spinner.css("display", "inline-block");
	}
	
	function esconderLoad(){
		this.spinner.css("display", "none");
	}
	
	function encode_utf8(s) {
		return unescape(encodeURIComponent(s));
	}
	
	function decode_utf8(s) {
		return decodeURIComponent(escape(s));
	}
	
	return GastoInstrutor;
	
}());

jQuery(function() {
	var gastoInstrutor = new SIGED.GastoInstrutor();
	gastoInstrutor.iniciar();
});