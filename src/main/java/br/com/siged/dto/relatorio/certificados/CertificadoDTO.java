package br.com.siged.dto.relatorio.certificados;

public class CertificadoDTO {

	private String situacao;
	private String local;
	private String periodo;
	private String cargaHoraria;
	private String conteudoProgramatico;
	private String dataFimRealizacaoPorExtenso;
	private String filename;
	private boolean isEventoEAD;
	
	public CertificadoDTO() {
		super();
	}
	
	public CertificadoDTO(String filename) {
		super();
		this.filename = filename;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(String cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public String getConteudoProgramatico() {
		return conteudoProgramatico;
	}

	public void setConteudoProgramatico(String conteudoProgramatico) {
		this.conteudoProgramatico = conteudoProgramatico;
	}

	public String getDataFimRealizacaoPorExtenso() {
		return dataFimRealizacaoPorExtenso;
	}

	public void setDataFimRealizacaoPorExtenso(String dataFimRealizacaoPorExtenso) {
		this.dataFimRealizacaoPorExtenso = dataFimRealizacaoPorExtenso;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public boolean isEventoEAD() {
		return isEventoEAD;
	}

	public void setEventoEAD(boolean isEventoEAD) {
		this.isEventoEAD = isEventoEAD;
	}
	
	@Override
	public String toString() {
		return this.situacao + ", realizado em " + this.local + ", " + this.periodo + ", com carga horária de " +this.cargaHoraria + " horas.";
	}
	
}
