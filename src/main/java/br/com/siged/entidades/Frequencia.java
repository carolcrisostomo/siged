package br.com.siged.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Marcelo
 */
@Entity
@Table(name = "frequencia")
@NamedQueries({
    @NamedQuery(name = "Frequencia.findAll", query = "SELECT f FROM Frequencia f ORDER BY f.eventoId.dataInicioPrevisto DESC"),
    @NamedQuery(name = "Frequencia.findById", query = "SELECT f FROM Frequencia f WHERE f.id = :id"),
    @NamedQuery(name = "Frequencia.findByData", query = "SELECT f FROM Frequencia f WHERE f.data = :data"),
    @NamedQuery(name = "Frequencia.findByEvento", query = "SELECT f FROM Frequencia f WHERE f.eventoId = :evento"),
    @NamedQuery(name = "Frequencia.findByModulo", query = "SELECT f FROM Frequencia f WHERE f.moduloId = :modulo"),
    @NamedQuery(name = "Frequencia.findByEventoAndParticipante", query = "SELECT f FROM Frequencia f join f.participanteList p WHERE f.eventoId = :evento and p = :participante"),
    @NamedQuery(name = "Frequencia.findByEventoAndModuloAndParticipante", query = "SELECT f FROM Frequencia f join f.participanteList p WHERE f.eventoId = :evento and f.moduloId = :modulo and p = :participante"),
    @NamedQuery(name = "Frequencia.findByEventoAndModuloAndData", query = "SELECT f FROM Frequencia f WHERE f.eventoId = :evento and f.moduloId = :modulo and f.data = :data and f.turno = :turno"),
    @NamedQuery(name = "Frequencia.findByTurno", query = "SELECT f FROM Frequencia f WHERE f.turno = :turno")})
@JsonIgnoreProperties(value={"eventoId", "moduloId", "participanteList", "participanteId"})
@SequenceGenerator(name="sequence", sequenceName="seq_frequencia", allocationSize=1)
public class Frequencia implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotNull(message=" Campo Obrigatório")
    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    
	@NotNull(message=" Campo Obrigatório")
	@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "turno")
    private String turno;
    
	@NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "evento_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JsonIgnore
    private Evento eventoId;
    
	@NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "modulo_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JsonIgnore
    private Modulo moduloId;
    
	@NotNull(message=" Campo Obrigatório")
    @ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnore
    @JoinTable(name = "frequencia_participante", joinColumns = {
	    @JoinColumn(name = "frequencia_id", referencedColumnName = "id")}, inverseJoinColumns = {
	    @JoinColumn(name = "participante_id", referencedColumnName = "id")})
	@OrderBy("nome ASC")
    private List<Participante> participanteList;
    
    private Participante participanteId;
    
    public Frequencia() {
    }

    public Frequencia(Long id) {
        this.id = id;
    }

    public Frequencia(Long id, Date data, String presenca) {
        this.id = id;
        this.data = data;

    }

    public Long getId() {
        return id;
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

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Evento getEventoId() {
        return eventoId;
    }

    public void setEventoId(Evento eventoId) {
        this.eventoId = eventoId;
    }

    public Modulo getModuloId() {
        return moduloId;
    }

    public void setModuloId(Modulo moduloId) {
        this.moduloId = moduloId;
    }
    
    public Participante getParticipanteId() {
		return participanteId;
	}

	public void setParticipanteId(Participante participanteId) {
		this.participanteId = participanteId;
	}

	@JsonIgnore
    public List<Participante> getParticipanteList() {
        return participanteList;
    }
    @JsonIgnore
    public void setParticipanteList(List<Participante> participanteList) {
        this.participanteList = participanteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Frequencia)) {
            return false;
        }
        Frequencia other = (Frequencia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.siged.entidades.Frequencia[id=" + id + "]";
    }

}
