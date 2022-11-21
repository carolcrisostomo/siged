package br.com.siged.filtro;

public class MetaDesempenhoProdutividadeFiltro {

	private Integer ano;
	
	private Integer semestre;

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getSemestre() {
		return semestre;
	}

	public void setSemestre(Integer semestre) {
		this.semestre = semestre;
	}
	
	public MetaDesempenhoProdutividadeFiltro() {
		
	}
	
	public MetaDesempenhoProdutividadeFiltro(Integer ano, Integer semestre) {
		this.ano = ano;
		this.semestre = semestre;
	}
}
