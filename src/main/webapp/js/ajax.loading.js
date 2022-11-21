var SIGED = SIGED || {};

SIGED.AjaxLoading = (function(){
	
	function AjaxLoading() {
		this.mensagem = '<br /><img src="/static/images/spinner.gif" /><h1>Aguarde ...</h1><br />';
	}
	
	AjaxLoading.prototype.setMensagem = function(message){
		if(message) {
			this.mensagem = '<br /><img src="/static/images/spinner.gif" /><h1>' + message + '</h1><br />';
		} else {
			this.mensagem = '<br /><img src="/static/images/spinner.gif" /><h1>Aguarde ...</h1><br />';
		}
	}
	
	AjaxLoading.prototype.start = function(){
		jQuery.blockUI({
			message : this.mensagem
		});
	}
	
	AjaxLoading.prototype.stop = function(){
		jQuery(document).ajaxStop(jQuery.unblockUI);
	}
	
	return AjaxLoading;
	
}());