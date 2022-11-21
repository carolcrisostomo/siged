package br.com.siged.dto.relatorio.avaliacaoreacao;

public class GraficoDTO {

	private Integer totalExcelente = 0;
	private Integer totalBom = 0;
	private Integer totalRegular = 0;
	private Integer totalInsuficiente = 0;
	private Integer totalRespondidas = 0;
	
	private String percentualExcelente;
	private String percentualBom;
	private String percentualRegular;
	private String percentualInsuficiente;
	private String percentualSatisfatorias;
	
	
	public Integer getTotalExcelente() {
		return totalExcelente;
	}
	public void setTotalExcelente(Integer totalExcelente) {
		this.totalExcelente = totalExcelente;
	}
	public Integer getTotalBom() {
		return totalBom;
	}
	public void setTotalBom(Integer totalBom) {
		this.totalBom = totalBom;
	}
	public Integer getTotalRegular() {
		return totalRegular;
	}
	public void setTotalRegular(Integer totalRegular) {
		this.totalRegular = totalRegular;
	}
	public Integer getTotalInsuficiente() {
		return totalInsuficiente;
	}
	public void setTotalInsuficiente(Integer totalInsuficiente) {
		this.totalInsuficiente = totalInsuficiente;
	}
	public Integer getTotalRespondidas() {
		return totalRespondidas;
	}
	public void setTotalRespondidas(Integer totalRespondidas) {
		this.totalRespondidas = totalRespondidas;
	}
	public String getPercentualExcelente() {
		return percentualExcelente;
	}
	public void setPercentualExcelente(String percentualExcelente) {
		this.percentualExcelente = percentualExcelente;
	}
	public String getPercentualBom() {
		return percentualBom;
	}
	public void setPercentualBom(String percentualBom) {
		this.percentualBom = percentualBom;
	}
	public String getPercentualRegular() {
		return percentualRegular;
	}
	public void setPercentualRegular(String percentualRegular) {
		this.percentualRegular = percentualRegular;
	}
	public String getPercentualInsuficiente() {
		return percentualInsuficiente;
	}
	public void setPercentualInsuficiente(String percentualInsuficiente) {
		this.percentualInsuficiente = percentualInsuficiente;
	}
	public String getPercentualSatisfatorias() {
		return percentualSatisfatorias;
	}
	public void setPercentualSatisfatorias(String percentualSatisfatorias) {
		this.percentualSatisfatorias = percentualSatisfatorias;
	}
	
}
