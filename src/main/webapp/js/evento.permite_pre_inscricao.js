var SIGED = SIGED || {};

SIGED.PermitePreInscricao = (function(){
	function PermitePreInscricao() {
		this.permitePreInscricaoSelect = jQuery('.js_permite_pre_inscricao_select');
		this.dataInicioPreInscricao = jQuery('.js_data_inicio_pre_inscricao');
		this.dataFimPreInscricao = jQuery('.js_data_fim_pre_inscricao');
	}
	
	PermitePreInscricao.prototype.iniciar = function(){
		onPermitePreInscricaoSelect.call(this);
		this.permitePreInscricaoSelect.change(onPermitePreInscricaoSelect.bind(this));
	}
	
	function onPermitePreInscricaoSelect() {
		var permite = this.permitePreInscricaoSelect.val();
		if(permite == 'S') {
			habilitarDatas.call(this);
		} else if(permite == 'N') {
			desabilitarDatas.call(this);
		}
	}
	
	function desabilitarDatas() {
		this.dataInicioPreInscricao.attr('disabled', 'disabled');
		this.dataInicioPreInscricao.css('background-color', '#dcdada');
		this.dataFimPreInscricao.attr('disabled', 'disabled');
		this.dataFimPreInscricao.css('background-color', '#dcdada');
		
	}
	
	function habilitarDatas() {
		this.dataInicioPreInscricao.removeAttr('disabled');
		this.dataInicioPreInscricao.css('background-color', '#fff');
		this.dataFimPreInscricao.removeAttr('disabled');
		this.dataFimPreInscricao.css('background-color', '#fff');
	}
	
	return PermitePreInscricao;
}());

jQuery(function() {
	var ppi = new SIGED.PermitePreInscricao();
	ppi.iniciar();
});