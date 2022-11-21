package br.com.siged.dto.relatorio.avaliacaoreacao;

/**
 * Classe para transportar quantitativo de respostas das questões
 * @author Rafael de Castro
 *
 */
public class QuestaoDTO {

	private String descricao;
	
	private Integer excelente;
	
	private Integer bom;
	
	private Integer regular;
	
	private Integer insuficiente;
	
	private Integer totalNotas;
	
	public QuestaoDTO(){
		this.excelente = 0;
		this.bom = 0;
		this.regular = 0;
		this.insuficiente = 0;
		this.totalNotas = 0;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getExcelente() {
		return excelente;
	}

	public void setExcelente(Integer excelente) {
		this.excelente = excelente;
	}

	public Integer getBom() {
		return bom;
	}

	public void setBom(Integer bom) {
		this.bom = bom;
	}

	public Integer getRegular() {
		return regular;
	}

	public void setRegular(Integer regular) {
		this.regular = regular;
	}

	public Integer getInsuficiente() {
		return insuficiente;
	}

	public void setInsuficiente(Integer insuficiente) {
		this.insuficiente = insuficiente;
	}
	
	public Integer getTotalNotas() {
		atualizarTotalNotas();
		return totalNotas;
	}

	public void atualizarTotalNotas() {
		this.totalNotas = this.calcularTotalNotas();
	}

	private Integer calcularTotalNotas(){
		return this.excelente + this.bom + this.regular + this.insuficiente;
	}
	
	@Override
	public String toString() {
		return this.descricao + ": "
				+ "Excelente(" + this.excelente + ") "
				+ "Bom(" + this.bom + ") "
				+ "Regular(" + this.regular + ") "
				+ "insuficiente(" + this. insuficiente + ")";
	}
}
