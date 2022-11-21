package br.com.siged.dto.relatorio.avaliacaoreacao;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe para transportar dados quantitativo das questões das Avaliações de Reação do agrupando por Módulo
 * @author Rafael de Castro
 *
 */
public class ModuloDTO {
	
	private String titulo;
	
	private Integer quantidadeAvaliacoes;
	private Integer quantidadeSatisfatorias;
	
	private List<TipoQuestaoDTO> tiposQuestoes;
	private List<Comentario> listaComentarios;
	private List<GraficoDTO> graficoDataSource;
	
	public ModuloDTO(){
		this.quantidadeAvaliacoes = 0;
		this.quantidadeSatisfatorias = 0;
		this.tiposQuestoes = new ArrayList<>();
		this.listaComentarios = new ArrayList<>();
		this.graficoDataSource = new ArrayList<>();
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Integer getQuantidadeAvaliacoes() {
		return quantidadeAvaliacoes;
	}

	public void setQuantidadeAvaliacoes(Integer quantidadeAvaliacoes) {
		this.quantidadeAvaliacoes = quantidadeAvaliacoes;
	}

	public Integer getQuantidadeSatisfatorias() {
		return quantidadeSatisfatorias;
	}

	public void setQuantidadeSatisfatorias(Integer quantidadeSatisfatorias) {
		this.quantidadeSatisfatorias = quantidadeSatisfatorias;
	}

	public List<TipoQuestaoDTO> getTiposQuestoes() {
		return tiposQuestoes;
	}

	public void setTiposQuestoes(List<TipoQuestaoDTO> tiposQuestoes) {
		this.tiposQuestoes = tiposQuestoes;
	}
	
	public List<Comentario> getListaComentarios() {
		return listaComentarios;
	}

	public void setListaComentarios(List<Comentario> listaComentarios) {
		this.listaComentarios = listaComentarios;
	}

	public Integer getTotalQuestoesExcelente(){
		int total = 0;
		for (TipoQuestaoDTO tipo : this.tiposQuestoes) {
			total += tipo.totalQuestoesExcelente();
		}
		return total;
	}
	
	public Integer getTotalQuestoesBom(){
		int total = 0;
		for (TipoQuestaoDTO tipo : this.tiposQuestoes) {
			total += tipo.totalQuestoesBom();
		}
		return total;
	}
	
	public Integer getTotalQuestoesRegular(){
		int total = 0;
		for (TipoQuestaoDTO tipo : this.tiposQuestoes) {
			total += tipo.totalQuestoesRegular();
		}
		return total;
	}
	
	public Integer getTotalQuestoesInsuficiente(){
		int total = 0;
		for (TipoQuestaoDTO tipo : this.tiposQuestoes) {
			total += tipo.totalQuestoesInsuficiente();
		}
		return total;
	}

	public List<GraficoDTO> getGraficoDataSource() {
		return graficoDataSource;
	}

	public Integer getTotalQuestoesRespondidas(){
		return getTotalQuestoesExcelente() + getTotalQuestoesBom() + getTotalQuestoesRegular() + getTotalQuestoesInsuficiente();
	}
	
	public String getPercentualSatisfatorias(){
		Double porcentagem = (Double.valueOf(this.quantidadeSatisfatorias) / this.quantidadeAvaliacoes) * 100;
		return String.format("%.1f",porcentagem) + "%";
	}
	
	public String getPercentualExcelente() {
		Double porcentagem = (Double.valueOf(this.getTotalQuestoesExcelente()) / this.getTotalQuestoesRespondidas()) * 100;
		return String.format("%.0f",porcentagem) + "%";
	}
	public String getPercentualBom() {
		Double porcentagem = (Double.valueOf(this.getTotalQuestoesBom()) / this.getTotalQuestoesRespondidas()) * 100;
		return String.format("%.0f",porcentagem) + "%";
	}
	public String getPercentualRegular() {
		Double porcentagem = (Double.valueOf(this.getTotalQuestoesRegular()) / this.getTotalQuestoesRespondidas()) * 100;
		return String.format("%.0f",porcentagem) + "%";
	}
	public String getPercentualInsuficiente() {
		Double porcentagem = (Double.valueOf(this.getTotalQuestoesInsuficiente()) / this.getTotalQuestoesRespondidas()) * 100;
		return String.format("%.0f",porcentagem) + "%";
	}
	
	public void incrementarSatisfatorias(){
		this.quantidadeSatisfatorias++;
	}
	
	public void incluirGrafico() {
		graficoDataSource = new ArrayList<>();
		
		GraficoDTO grafico = new GraficoDTO();
		grafico.setPercentualExcelente(this.getPercentualExcelente());
		grafico.setPercentualBom(this.getPercentualBom());
		grafico.setPercentualRegular(this.getPercentualRegular());
		grafico.setPercentualInsuficiente(this.getPercentualInsuficiente());
		grafico.setPercentualSatisfatorias(this.getPercentualSatisfatorias());
		
		grafico.setTotalExcelente(this.getTotalQuestoesExcelente());
		grafico.setTotalBom(this.getTotalQuestoesBom());
		grafico.setTotalRegular(this.getTotalQuestoesRegular());
		grafico.setTotalInsuficiente(this.getTotalQuestoesInsuficiente());
		grafico.setTotalRespondidas(this.getTotalQuestoesRespondidas());
		
		graficoDataSource.add(grafico);
	}
	
	@Override
	public String toString() {
		String retorno = "Modulo: " + this.titulo + "\n";
		retorno += "Quantidade Avaliaçoes: " + this.quantidadeAvaliacoes + "\n";
		for(TipoQuestaoDTO tipo : this.tiposQuestoes){
			retorno += tipo.toString() + "\n";
		}
		
		for(Comentario comentario : this.listaComentarios){
			retorno += comentario.toString() + "\n";
		}
		
		retorno += "##################\n";
		
		return retorno;
	}
	
}
