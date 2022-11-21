package br.com.siged.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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

/**
 *
 * @author Marcelo
 */
@Entity
@Table(name = "gasto")
@NamedQueries({	
    @NamedQuery(name = "Gasto.findAll", query = "SELECT g FROM Gasto g ORDER BY g.eventoId.dataInicioPrevisto DESC"),   
    @NamedQuery(name = "Gasto.findById", query = "SELECT g FROM Gasto g WHERE g.id = :id"),
    @NamedQuery(name = "Gasto.findByNumeroEmpenho", query = "SELECT g FROM Gasto g WHERE g.numeroEmpenho = :numeroEmpenho"),
    @NamedQuery(name = "Gasto.findByNumeroProcesso", query = "SELECT g FROM Gasto g WHERE g.numeroProcesso = :numeroProcesso"),
    @NamedQuery(name = "Gasto.findByObservacao", query = "SELECT g FROM Gasto g WHERE g.observacao = :observacao"),
    @NamedQuery(name = "Gasto.findByDataEmpenho", query = "SELECT g FROM Gasto g WHERE g.dataEmpenho = :dataEmpenho"),
    @NamedQuery(name = "Gasto.findByEvento", query = "SELECT g FROM Gasto g WHERE g.eventoId = :evento"),
    @NamedQuery(name = "Gasto.findByValor", query = "SELECT g FROM Gasto g WHERE g.valor = :valor")})
@SequenceGenerator(name="sequence", sequenceName="seq_gasto", allocationSize=1)
public class Gasto implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@Column(name = "numero_empenho")
    private String numeroEmpenho;
    
	@Column(name = "numero_processo")
    private String numeroProcesso;
    
	@Column(name = "observacao", length = 2000)
    private String observacao;
    
	@Column(name = "data_empenho")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEmpenho;
    
	@NotNull(message=" Campo Obrigatório")
    @Column(name = "valor", precision = 19, scale = 2)
    private BigDecimal valor;
    
	@NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "evento_id", referencedColumnName = "id")
    @ManyToOne(optional = false) @JsonIgnore
    private Evento eventoId;
    
	@NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "fonte_id", referencedColumnName = "id")
    @ManyToOne @JsonIgnore
    private FonteGasto fonteId;
    
	@JoinColumn(name = "participante_id", referencedColumnName = "id")
    @ManyToOne @JsonIgnore
    private Participante participanteId;
    
	@NotNull(message=" Campo Obrigatório")	
    @JoinColumn(name = "tipo_id", referencedColumnName = "id")
    @ManyToOne(optional = false) @JsonIgnore
    private TipoGasto tipoId;
	
	@ManyToOne
	@JoinColumn(name = "instrutor_id", referencedColumnName = "id")
    @JsonIgnore
    private Instrutor instrutor;

    public Gasto() {
    }

    public Gasto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroEmpenho() {
        return numeroEmpenho;
    }

    public void setNumeroEmpenho(String numeroEmpenho) {
        this.numeroEmpenho = numeroEmpenho;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Date getDataEmpenho() {
        return dataEmpenho;
    }

    public void setDataEmpenho(Date dataEmpenho) {
        this.dataEmpenho = dataEmpenho;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Evento getEventoId() {
        return eventoId;
    }

    public void setEventoId(Evento eventoId) {
        this.eventoId = eventoId;
    }

    public FonteGasto getFonteId() {
        return fonteId;
    }

    public void setFonteId(FonteGasto fonteId) {
        this.fonteId = fonteId;
    }

    public Participante getParticipanteId() {
        return participanteId;
    }

    public void setParticipanteId(Participante participanteId) {
        this.participanteId = participanteId;
    }

    public TipoGasto getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoGasto tipoId) {
        this.tipoId = tipoId;
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

        if (!(object instanceof Gasto)) {
            return false;
        }
        Gasto other = (Gasto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.siged.entidades.Gasto[id=" + id + "]";
    }

}
