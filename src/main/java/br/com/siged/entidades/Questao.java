package br.com.siged.entidades;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "QUESTAO")
@SequenceGenerator(name = "sequence", sequenceName = "seq_questao", allocationSize=1)
public class Questao implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
	
	@NotEmpty(message = " Campo Obrigatório")
	@Column(name = "descricao")
    private String descricao;
	
	@NotNull
	@JoinColumn(name = "tipo_id", referencedColumnName = "id")
	@ManyToOne @JsonIgnore
	private TipoQuestao tipoQuestao;
	
	@NotNull(message = " Campo Obrigatório")
	@Column(name = "ordem")
	private Long ordem;
	
	@NotNull(message = " Campo Obrigatório")
	@ManyToMany
	@JoinTable(name="QUESTAO_MODALIDADE", 
				joinColumns={@JoinColumn(name="questao_id", referencedColumnName = "id")}, 
				inverseJoinColumns={@JoinColumn(name="modalidade_id", referencedColumnName = "id")})
	@OrderBy("descricao ASC")
	private List<Modalidade> modalidades;
	
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

	public TipoQuestao getTipoQuestao() {
		return tipoQuestao;
	}

	public void setTipoQuestao(TipoQuestao tipoQuestao) {
		this.tipoQuestao = tipoQuestao;
	}

	public Long getOrdem() {
		return ordem;
	}

	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}

	public List<Modalidade> getModalidades() {
		return modalidades;
	}

	public void setModalidades(List<Modalidade> modalidades) {
		this.modalidades = modalidades;
	}
	
	public String getModalidadesAsString(){
		String modalidadesString = "";
		for (int i = 0; i < this.modalidades.size(); i++) {
			if(i < this.modalidades.size() - 1){
				modalidadesString += this.modalidades.get(i).getDescricao() + " / ";
			} else {
				modalidadesString += this.modalidades.get(i).getDescricao();
			}
			
		}
		
		return modalidadesString;
	}
	
	public boolean isEADOnly(){
		if(this.modalidades.size() == 1){
			if(modalidades.get(0).getId() == 2L){
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	public boolean isPresencialOnly(){
		if(this.modalidades.size() == 1){
			if(modalidades.get(0).getId() == 1L){
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	public boolean isModalidade(Modalidade modalidade) {
		for(Modalidade modal : this.modalidades) {
			if(modal.getId().equals(modalidade.getId())){
				return true;
			}
		}
		return false;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Questao)) {
            return false;
        }
        Questao other = (Questao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
	
	
}
