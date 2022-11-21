package br.com.siged.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.siged.entidades.externas.UsuarioSCA;

/**
 *
 * @author Marcelo
 */
@Entity
@Table(name = "avaliacao_eficacia")
@NamedQueries({
    @NamedQuery(name = "AvaliacaoEficacia.findAll", query = "SELECT a FROM AvaliacaoEficacia a ORDER BY a.eventoId.dataInicioPrevisto DESC"),
    @NamedQuery(name = "AvaliacaoEficacia.findById", query = "SELECT a FROM AvaliacaoEficacia a WHERE a.id = :id"),
    @NamedQuery(name = "AvaliacaoEficacia.findByResponsavel", query = "SELECT a FROM AvaliacaoEficacia a WHERE a.responsavel = :responsavel ORDER BY a.dataCadastro DESC"),
    @NamedQuery(name = "AvaliacaoEficacia.findBySetor", query = "SELECT a FROM AvaliacaoEficacia a WHERE a.participanteId.setorId.id = :setorId AND a.participanteId.cpf <> :cpf ORDER BY a.eventoId.dataInicioPrevisto DESC"),
    @NamedQuery(name = "AvaliacaoEficacia.findByObservacao", query = "SELECT a FROM AvaliacaoEficacia a WHERE a.observacao = :observacao"),
    @NamedQuery(name = "AvaliacaoEficacia.findByEventoIdAndParticipanteId", query = "SELECT a FROM AvaliacaoEficacia a WHERE a.participanteId.id = :participanteId AND a.eventoId.id = :eventoId"),
    @NamedQuery(name = "AvaliacaoEficacia.findByDataCadastro", query = "SELECT a FROM AvaliacaoEficacia a WHERE a.dataCadastro = :dataCadastro"),
    @NamedQuery(name = "AvaliacaoEficacia.findByDesempenhoServico", query = "SELECT a FROM AvaliacaoEficacia a WHERE a.desempenhoServico = :desempenhoServico"),
    @NamedQuery(name = "AvaliacaoEficacia.findByMelhoria", query = "SELECT a FROM AvaliacaoEficacia a WHERE a.melhoria = :melhoria")})
@SequenceGenerator(name="sequence", sequenceName="seq_avaliacao_eficacia" , allocationSize=1)
@JsonIgnoreProperties(value={"eventoId", "participanteId", "responsavel"})
public class AvaliacaoEficacia implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@Column(name = "observacao", length = 2000)
    private String observacao;
    
	@Column(name = "data_cadastro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;
	
	//@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "desempenho_servico")
    private String desempenhoServico;
    
    //@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "melhoria")
    private String melhoria;
    
    @NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "evento_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JsonIgnore
    private Evento eventoId;
    
    @NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "participante_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JsonIgnore
    private Participante participanteId;
    
    @JoinColumn(name = "RESPONSAVEL_ID", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	@JsonIgnore
	private UsuarioSCA responsavel;

    public AvaliacaoEficacia() {
    }

    public AvaliacaoEficacia(Long id) {
        this.id = id;
    }

    public AvaliacaoEficacia(Long id, String observacao) {
        this.id = id;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getDesempenhoServico() {
        return desempenhoServico;
    }

    public void setDesempenhoServico(String desempenhoServico) {
        this.desempenhoServico = desempenhoServico;
    }

    public String getMelhoria() {
        return melhoria;
    }

    public void setMelhoria(String melhoria) {
        this.melhoria = melhoria;
    }

    public Evento getEventoId() {
        return eventoId;
    }

    public void setEventoId(Evento eventoId) {
        this.eventoId = eventoId;
    }

    public Participante getParticipanteId() {
        return participanteId;
    }

    public void setParticipanteId(Participante participanteId) {
        this.participanteId = participanteId;
    }
    
    public UsuarioSCA getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(UsuarioSCA responsavel) {
		this.responsavel = responsavel;
	}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof AvaliacaoEficacia)) {
            return false;
        }
        AvaliacaoEficacia other = (AvaliacaoEficacia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.siged.entidades.AvaliacaoEficacia[id=" + id + "]";
    }

}
