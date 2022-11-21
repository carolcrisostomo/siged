var SIGED = SIGED || {};

SIGED.MeusEventos = (function(){
	function MeusEventos() {
		this.partContainerComAcoes = jQuery('#comoPart');
		this.partContainerSemAcoes = jQuery('#comoPartSemAcoes');
		this.instrutorContainer = jQuery("#comoInstr");
		
		this.semAcoesCheck = jQuery('#semAcoes');
		this.semAcoesLabel = jQuery('.semAcoesLabel');
		this.instrutorCheck = jQuery('#comoInstrutor');
		this.instrutorLabel = jQuery('.instrutorLabel');
		
		this.partInforContainer = jQuery('.message.info');
	}
	
	MeusEventos.prototype.iniciar = function(){
		if(this.instrutorCheck.val() == 0) {
			this.instrutorContainer.hide();
		} else {
			this.partInforContainer.hide();
			marcar.call(this, this.instrutorCheck);
			
			this.semAcoesCheck.val(0);
			desmarcar.call(this, this.semAcoesCheck);
			desabilitar.call(this, this.semAcoesCheck, this.semAcoesLabel);
		}
		
		if(this.semAcoesCheck.val() == 0) {
			this.partContainerComAcoes.show();
			this.partContainerSemAcoes.hide();
		} else {
			this.partContainerComAcoes.hide();
			this.partContainerSemAcoes.show();
			marcar.call(this, this.semAcoesCheck);
			this.instrutorCheck.val(0);
			desmarcar.call(this, this.instrutorCheck);
		}
		
		this.instrutorCheck.change(onInstrutorChange.bind(this));
		this.semAcoesCheck.change(onSemAcoesCheckChange.bind(this));
	}
	
	function onInstrutorChange() {
		if (this.instrutorCheck.val() == 0) {
			this.instrutorCheck.val(1);
			desmarcar.call(this, this.semAcoesCheck);
			desabilitar.call(this, this.semAcoesCheck, this.semAcoesLabel);
		} else {
			this.instrutorCheck.val(0);
			habilitar.call(this, this.semAcoesCheck, this.semAcoesLabel);
		}
	}
	
	function onSemAcoesCheckChange() {
		if (this.semAcoesCheck.val() == 0) {
			this.semAcoesCheck.val(1);
			
			this.partContainerComAcoes.hide();
			this.partContainerSemAcoes.show();
			
			desmarcar.call(this, this.instrutorCheck);
			desabilitar.call(this, this.instrutorCheck, this.instrutorLabel);
		} else {
			this.semAcoesCheck.val(0);
			this.partContainerComAcoes.show();
			this.partContainerSemAcoes.hide();
			habilitar.call(this, this.instrutorCheck, this.instrutorLabel);
		}
	}
	
	function desmarcar(check) {
		check.attr("checked", false);
	}
	
	function marcar(check) {
		check.attr("checked", true);
	}
	
	function desabilitar(check, label) {
		check.attr('disabled', 'disabled');
		label.css('color', '#ccc');
	}
	
	function habilitar(check, label) {
		check.removeAttr('disabled');
		label.css('color', '#666');
	}
	
	return MeusEventos;
}());