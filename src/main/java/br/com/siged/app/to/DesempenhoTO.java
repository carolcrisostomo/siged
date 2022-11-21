package br.com.siged.app.to;

public class DesempenhoTO {
	
	private String desempenho;
	private String nota;
	private String frequencia;
	
	
	public DesempenhoTO(String desempenho, String nota, String frequencia) {
		this.desempenho = desempenho;
		this.nota = nota;
		this.frequencia = frequencia;
	}
	
	
	public String getDesempenho() {
		return desempenho;
	}
	public String getNota() {
		return nota;
	}
	public String getFrequencia() {
		return frequencia;
	}
	
	

}
