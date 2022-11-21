package br.com.siged.dto.relatorio.certificados;

public class CertificadoInstrutorDTO extends CertificadoDTO {

	private String nomeInstrutor;
	private boolean isModuloUnico;
	private String modalidadeDescricao;
	
	public CertificadoInstrutorDTO() {
		super();
	}
	
	public CertificadoInstrutorDTO(String filename) {
		super(filename);
	}
	
	public String getNomeInstrutor() {
		return nomeInstrutor;
	}
	public void setNomeInstrutor(String nomeInstrutor) {
		this.nomeInstrutor = nomeInstrutor;
	}
	public boolean isModuloUnico() {
		return isModuloUnico;
	}
	public void setModuloUnico(boolean isModuloUnico) {
		this.isModuloUnico = isModuloUnico;
	}
	public String getModalidadeDescricao() {
		return modalidadeDescricao;
	}
	public void setModalidadeDescricao(String modalidadeDescricao) {
		this.modalidadeDescricao = modalidadeDescricao;
	}

	@Override
	public String toString() {
		return "Certificamos que " + this.nomeInstrutor + " " + super.toString();
	}
	
}
