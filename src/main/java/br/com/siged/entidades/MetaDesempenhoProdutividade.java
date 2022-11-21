package br.com.siged.entidades;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "meta_desempenho_produtividade")
@SequenceGenerator(name = "sequence", sequenceName = "seq_meta_desempenho", allocationSize = 1)
public class MetaDesempenhoProdutividade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
	@Column(name = "id")
	private Long id;
	
	/**
	 * Meta 1: Índice de cumprimento do plano de capacitação para o período (%)
	 */
	@NotNull(message=" Campo Obrigatório")
	@Min(value = 0, message = " valor mínimo é 0%")
	@Max(value = 100, message = " valor máximo é 100%")
	@Column(name = "ind_capacitacao", precision = 19, scale = 2)
	private BigDecimal indiceCapacitacao;
	
	/**
	 * Meta 2: Índice de satisfação obtido nas avaliações de reação de treinamento (%)
	 */
	@NotNull(message=" Campo Obrigatório")
	@Min(value = 0, message = " valor mínimo é 0%")
	@Max(value = 100, message = " valor máximo é 100%")
	@Column(name = "ind_avaliacao_reacao", precision = 19, scale = 2)
	private BigDecimal indiceAvaliacaoReacao;
	
	@NotNull(message=" Campo Obrigatório")
	@Min(value = 2012, message = " Ano inválido")
	@Column(name = "ano")
	private Integer ano;
	
	@NotNull(message=" Campo Obrigatório")
	@Min(value = 1, message = " Semestre inválido")
	@Max(value = 2, message = " Semestre inválido")
	@Column(name = "semestre")
	private Integer semestre;
	
	public MetaDesempenhoProdutividade() {}
	
	public MetaDesempenhoProdutividade(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getIndiceCapacitacao() {
		return indiceCapacitacao;
	}

	public void setIndiceCapacitacao(BigDecimal indiceCapacitacao) {
		this.indiceCapacitacao = indiceCapacitacao;
	}

	public BigDecimal getIndiceAvaliacaoReacao() {
		return indiceAvaliacaoReacao;
	}

	public void setIndiceAvaliacaoReacao(BigDecimal indiceAvaliacaoReacao) {
		this.indiceAvaliacaoReacao = indiceAvaliacaoReacao;
	}

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
	
	public String getIndiceCapacitacaoPercent() {
		return indiceCapacitacao.toString() + "%";
	}
	
	public String getIndiceAvaliacaoReacaoPercent() {
		return indiceAvaliacaoReacao.toString() + "%";
	}
	
}
