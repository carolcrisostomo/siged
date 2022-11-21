var SIGED = SIGED || {};

SIGED.NomeOuCpfMask = (function(){
	
	function NomeOuCpfMask(input_hook) {
		this.input = jQuery(input_hook);
		this.event = jQuery({});
		this.masked = false;
		this.ctrlKey = 17;
		this.cmdKey = 91;
		this.vKey = 86;
	}
	
	NomeOuCpfMask.prototype.iniciar = function() {
		this.input.keyup(aoDigitarUp.bind(this));
		this.input.keydown(aoDigitarDown.bind(this));
	}
	
	function aoDigitarUp(e) {
		if(e.keyCode == this.vKey) {
			var texto = this.input.val();
			if(texto.match(/^[0-9]{3}.?[0-9]{3}.?[0-9]{3}-?[0-9]{2}/) && !this.masked) {
				this.input.setMask('999.999.999-99');
				this.event.trigger('cpf');				
				this.masked = true;
			}
		}
	}
	
	function aoDigitarDown(e) {
		if(e.keyCode >= 96 && e.keyCode <= 105) {
			if(!this.masked) {
				this.input.setMask('999.999.999-99');
				this.event.trigger('cpf');				
				this.masked = true;
			}
		} else if(e.keyCode != this.ctrlKey && e.keyCode != this.cmdKey && e.keyCode != 8 && e.keyCode != 9 && e.keyCode != 46) {
			if(this.masked) {
				this.input.unsetMask().val('');
				this.event.trigger('nome')
				this.masked = false;
			}
		} 
	}
	
	return NomeOuCpfMask;
	
}());