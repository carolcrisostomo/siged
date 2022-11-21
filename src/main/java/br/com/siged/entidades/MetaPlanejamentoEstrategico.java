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
@Table(name = "meta_planejamento_estrategico")
@SequenceGenerator(name = "sequence", sequenceName = "seq_meta_planejamento", allocationSize = 1)
public class MetaPlanejamentoEstrategico implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
	@Column(name = "id")
	private Long id;
	
	/**
	 * Meta 1: Quantidade de ações de integração promovidas
	 */
	@NotNull(message=" Campo Obrigatório")
	@Min(value = 0, message = " valor mínimo é 0")
	@Column(name = "qtd_acoes")
	private Integer quantidadeAcoes;
	
	/**
	 * Meta 2: Percentual de execução do plano de capacitação dos jurisdicionados
	 */
	@NotNull(message=" Campo Obrigatório")
	@Min(value = 0, message = " valor mínimo é 0%")
	@Max(value = 100, message = " valor máximo é 100%")
	@Column(name = "perc_juri_capacitados", precision = 19, scale = 2)
	private BigDecimal percentualJurisdicionadosCapacitados;
	
	/**
	 * Meta 3: Percentual de servidores capacitados para o uso de recursos tecnológicos
	 */
	@NotNull(message=" Campo Obrigatório")
	@Min(value = 0, message = " valor mínimo é 0%")
	@Max(value = 100, message = " valor máximo é 100%")
	@Column(name = "perc_serv_capacitados", precision = 19, scale = 2)
	private BigDecimal percentualServidoresCapacitados;
	
	/**
	 * Meta 4: Percentual de ações implementadas nos Planos de Ação dos Projetos correlacionados
	 * (Criação da Política de certificação profissional do servidor e Formação do Auditor do Século XXI)
	 */
	@NotNull(message=" Campo Obrigatório")
	@Min(value = 0, message = " valor mínimo é 0%")
	@Max(value = 100, message = " valor máximo é 100%")
	@Column(name = "perc_acoes_plano", precision = 19, scale = 2)
	private BigDecimal percentualAcoesDoPlano;
	
	@NotNull(message=" Campo Obrigatório")
	@Min(value = 2012, message = " Ano inválido")
	@Column(name = "ano")
	private Integer ano;
	
	public MetaPlanejamentoEstrategico() {}
	
	public MetaPlanejamentoEstrategico(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantidadeAcoes() {
		return quantidadeAcoes;
	}

	public void setQuantidadeAcoes(Integer quantidadeAcoes) {
		this.quantidadeAcoes = quantidadeAcoes;
	}

	public BigDecimal getPercentualJurisdicionadosCapacitados() {
		return percentualJurisdicionadosCapacitados;
	}

	public void setPercentualJurisdicionadosCapacitados(BigDecimal percentualJurisdicionadosCapacitados) {
		this.percentualJurisdicionadosCapacitados = percentualJurisdicionadosCapacitados;
	}

	public BigDecimal getPercentualServidoresCapacitados() {
		return percentualServidoresCapacitados;
	}

	public void setPercentualServidoresCapacitados(BigDecimal percentualServidoresCapacitados) {
		this.percentualServidoresCapacitados = percentualServidoresCapacitados;
	}

	public BigDecimal getPercentualAcoesDoPlano() {
		return percentualAcoesDoPlano;
	}

	public void setPercentualAcoesDoPlano(BigDecimal percentualAcoesDoPlano) {
		this.percentualAcoesDoPlano = percentualAcoesDoPlano;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}
	
	public String getPercentualJurisdicionadosCapacitadosPercent() {
		return percentualJurisdicionadosCapacitados.toString() + "%";
	}
	
	public String getPercentualServidoresCapacitadosPercent() {
		return percentualServidoresCapacitados.toString() + "%";
	}
	
	public String getPercentualAcoesDoPlanoPercent() {
		return percentualAcoesDoPlano.toString() + "%";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MetaPlanejamentoEstrategico))
			return false;
		MetaPlanejamentoEstrategico other = (MetaPlanejamentoEstrategico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
