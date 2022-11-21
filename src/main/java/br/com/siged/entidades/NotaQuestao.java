package br.com.siged.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "NOTA_QUESTAO")
@SequenceGenerator(name = "sequence", sequenceName = "seq_nota_questao", allocationSize = 1)
public class NotaQuestao implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static final String EXCELENTE = "Excelente";
	public static final String BOM = "Bom";
	public static final String REGULAR = "Regular";
	public static final String INSUFICIENTE = "Insuficiente";
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
	
	@Column(name = "descricao")
    private String descricao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof NotaQuestao)) {
            return false;
        }
        NotaQuestao other = (NotaQuestao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }

}
