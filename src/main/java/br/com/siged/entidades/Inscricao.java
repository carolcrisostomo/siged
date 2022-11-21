package br.com.siged.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import br.com.siged.entidades.externas.UsuarioSCA;

/**
 *
 * @author Marcelo
 */
@Entity
@Table(name = "inscricao")
@NamedQueries({
    @NamedQuery(name = "Inscricao.findAll", query = "SELECT i FROM Inscricao i ORDER BY i.data"),
    @NamedQuery(name = "Inscricao.findById", query = "SELECT i FROM Inscricao i WHERE i.id = :id"),
    @NamedQuery(name = "Inscricao.findByConfirmada", query = "SELECT i FROM Inscricao i WHERE i.confirmada = :confirmada"),
    @NamedQuery(name = "Inscricao.findByData", query = "SELECT i FROM Inscricao i WHERE i.data = :data"),
    @NamedQuery(name = "Inscricao.findByParticipanteCpf", query = "SELECT i FROM Inscricao i WHERE i.participanteId.cpf = :cpf"),
    @NamedQuery(name = "Inscricao.findByIndicada", query = "SELECT i FROM Inscricao i WHERE i.indicada = :indicada"),
    @NamedQuery(name = "Inscricao.findByDataIndicacao", query = "SELECT i FROM Inscricao i WHERE i.dataIndicacao = :dataIndicacao"),
    @NamedQuery(name = "Inscricao.findByEventoAndParticipante", query = "SELECT i FROM Inscricao i WHERE i.eventoId = :evento and i.participanteId = :participante"),
    @NamedQuery(name = "Inscricao.findByChefe", query = "SELECT i FROM Inscricao i WHERE i.chefeId = :chefe ORDER BY i.data DESC"),
    @NamedQuery(name = "Inscricao.findByChefeAndIndicadaAndConfirmada", query = "SELECT i FROM Inscricao i WHERE i.chefeId.id = :chefe and i.indicada = :indicada and i.confirmada = :confirmada ORDER BY i.data DESC"),
    @NamedQuery(name = "Inscricao.findBySolicitacaoId", query = "SELECT i FROM Inscricao i WHERE i.solicitacaoId.id = :solicitacaoId"),
    @NamedQuery(name = "Inscricao.findByEventoAndConfirmada", query = "SELECT i FROM Inscricao i WHERE i.eventoId = :evento and i.confirmada = :confirmada ORDER BY i.data"),
    @NamedQuery(name = "Inscricao.findByEventoAndConfirmadaOrdenadaPorParticipante", query = "SELECT i FROM Inscricao i WHERE i.eventoId = :evento and i.confirmada = :confirmada ORDER BY i.participanteId.nome"),
	@NamedQuery(name = "Inscricao.findEventoIdByParticipanteId", query = "SELECT DISTINCT i.eventoId.id FROM Inscricao i WHERE i.participanteId.id = :participanteId")})

@JsonIgnoreProperties(value={"eventoId", "participanteId", "chefeId", "solicitacaoId"})

@SequenceGenerator(name="sequence", sequenceName="seq_inscricao", allocationSize = 1)
public class Inscricao implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	// dd/MM/yyyy -> 11/12/2017 - controle para data de confirmação de inscrição
	// as inscrições criadas a partir dessa data terão data de confirmação != null
	public static final long DATA_CONTROLE_CONFIRMACAO = 1512961200000L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
	
	@Column(name = "data")
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
    
	@Column(name = "confirmada")
    private String confirmada;
	
	@Column(name = "data_confirmacao")
	private Date dataConfirmacao;
	
	// JUSTIFICATIVA NAO CONFIRMACAO
	@Column(name = "justificativanaoconfirmacao")
    private String justificativanaoconfirmacao;
    
    // OBSERVACAO IPC
    @Column(name = "justificativa", length=2000)
    @Basic(fetch = FetchType.LAZY)
    private String justificativa;
    
    // JUSTIFICATIVA DO ALUNO
    @Column(name = "justificativaparticipante", length=2000)
    @Basic(fetch = FetchType.LAZY)
    private String justificativaParticipante;
    
    @Column(name = "indicada")
    private String indicada;
    
    @Column(name = "data_indicacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataIndicacao;
    
    @Column(name = "justificativachefe")
    private String justificativachefe;
    
    @NotNull(message="Campo Obrigatório")
    @JoinColumn(name = "evento_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY) @JsonIgnore
    private Evento eventoId;
    
    @NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "participante_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY) @JsonIgnore
    private Participante participanteId;
    
    @JoinColumn(name = "chefe_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, optional = true) @JsonIgnore
    private UsuarioSCA chefeId;
    
    @JoinColumn(name = "solicitacao_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, optional = true) @JsonIgnore
    private EventoExtra solicitacaoId;
    
    @Transient
    private String nomeParticipante;
    
    @Transient
    private String nomeCpfParticipante;
    

	public Inscricao() {
    }

    public Inscricao(Long id) {
        this.id = id;
    }

	public Inscricao(Long id, String confirmada, String justificativanaoconfirmacao, Date data, String indicada,
			Date dataIndicacao, Evento eventoId, Participante participanteId,
			UsuarioSCA chefeId, String justificativaParticipante) {
		super();
		this.id = id;
		this.confirmada = confirmada;
		this.justificativanaoconfirmacao = justificativanaoconfirmacao;
		this.data = data;
		this.indicada = indicada;
		this.dataIndicacao = dataIndicacao;
		this.eventoId = eventoId;
		this.participanteId = participanteId;
		this.chefeId = chefeId;
		this.justificativaParticipante = justificativaParticipante;
	}

	public UsuarioSCA getChefeId() {
		return chefeId;
	}

	public void setChefeId(UsuarioSCA chefeId) {
		this.chefeId = chefeId;
	}

	public Long getId() {
        return id;
    }

    public String getJustificativachefe() {
		return justificativachefe;
	}

	public void setJustificativachefe(String justificativachefe) {
		this.justificativachefe = justificativachefe;
	}

	public void setId(Long id) {
        this.id = id;
    }
	
	public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getConfirmada() {
        return confirmada;
    }

    public void setConfirmada(String confirmada) {
    	this.confirmada = confirmada;
    }

	public Date getDataConfirmacao() {
		return dataConfirmacao;
	}
	
	public void setDataConfirmacao(Date date) {
		this.dataConfirmacao = date;
	}

	/*public void setDataConfirmacao(Date dataConfirmacao) {
		this.dataConfirmacao = dataConfirmacao;
	}*/

	public String getJustificativanaoconfirmacao() {
		return justificativanaoconfirmacao;
	}

	public void setJustificativanaoconfirmacao(String justificativanaoconfirmacao) {
		this.justificativanaoconfirmacao = justificativanaoconfirmacao;
	}

    public String getIndicada() {
        return indicada;
    }

    public void setIndicada(String indicada) {
        this.indicada = indicada;
    }

    public Date getDataIndicacao() {
        return dataIndicacao;
    }

    public void setDataIndicacao(Date dataIndicacao) {
        this.dataIndicacao = dataIndicacao;
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

    public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Inscricao)) {
            return false;
        }
        Inscricao other = (Inscricao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.siged.entidades.Inscricao[id=" + id + "]";
    }

	public EventoExtra getSolicitacaoId() {
		return solicitacaoId;
	}

	public void setSolicitacaoId(EventoExtra solicitacaoId) {
		this.solicitacaoId = solicitacaoId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getJustificativaParticipante() {
		return justificativaParticipante;
	}

	public void setJustificativaParticipante(String justificativaParticipante) {
		this.justificativaParticipante = justificativaParticipante;
	}
	
	public String getNomeParticipante() {
		return nomeParticipante;
	}

	public void setNomeParticipante(String nomeParticipante) {
		this.nomeParticipante = nomeParticipante;
	}
	
	public String getNomeCpfParticipante() {
		if (this.participanteId != null )
			return participanteId.getNomeCpf();		
		return "";
	}
	
	public Boolean isNovo() {
		if(this.id != null)
			return false;
		return true;
	}

}
