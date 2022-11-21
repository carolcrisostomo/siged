package br.com.siged.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="AVALIACAO_REACAO_NOTA")
@SequenceGenerator(name = "sequence", sequenceName = "seq_avaliacao_reacao_nota", allocationSize = 1)
public class AvaliacaoReacaoNota implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
	private Long id;
	
	@NotNull(message = " Campo Obrigatório")
	@ManyToOne
	@JoinColumn(name="avaliacao_id", referencedColumnName="id")
	private AvaliacaoReacao avaliacaoReacao;
	
	@NotNull(message = " Campo Obrigatório")
	@ManyToOne
	@JoinColumn(name = "questao_id", referencedColumnName = "id")
	private Questao questao;
	
	@NotNull(message = " Campo Obrigatório")
	@ManyToOne
	@JoinColumn(name = "nota_id", referencedColumnName = "id")
	private NotaQuestao notaQuestao;
	
	@ManyToOne
	@JoinColumn(name = "instrutor_id", referencedColumnName = "id")
	private Instrutor instrutor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AvaliacaoReacao getAvaliacaoReacao() {
		return avaliacaoReacao;
	}

	public void setAvaliacaoReacao(AvaliacaoReacao avaliacaoReacao) {
		this.avaliacaoReacao = avaliacaoReacao;
	}

	public Questao getQuestao() {
		return questao;
	}

	public void setQuestao(Questao questao) {
		this.questao = questao;
	}

	public NotaQuestao getNotaQuestao() {
		return notaQuestao;
	}

	public void setNotaQuestao(NotaQuestao notaQuestao) {
		this.notaQuestao = notaQuestao;
	}

	public Instrutor getInstrutor() {
		return instrutor;
	}

	public void setInstrutor(Instrutor instrutor) {
		this.instrutor = instrutor;
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof AvaliacaoReacaoNota)) {
            return false;
        }
        AvaliacaoReacaoNota other = (AvaliacaoReacaoNota) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
