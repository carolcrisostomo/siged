package br.com.siged.entidades;

import java.io.Serializable;

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
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Marcelo
 */
@Entity
@Table(name = "nota")
@NamedQueries({
    @NamedQuery(name = "Nota.findAll", query = "SELECT n FROM Nota n ORDER BY n.eventoId.dataInicioPrevisto DESC"),
    @NamedQuery(name = "Nota.findById", query = "SELECT n FROM Nota n WHERE n.id = :id"),
    @NamedQuery(name = "Nota.findByNota", query = "SELECT n FROM Nota n WHERE n.nota = :nota"),
    @NamedQuery(name = "Nota.findByEventoAndModuloAndParticipante", query = "SELECT n FROM Nota n WHERE n.eventoId = :evento and n.moduloId = :modulo and n.participanteId = :participante"),
    @NamedQuery(name = "Nota.findByEventoAndParticipante", query = "SELECT n FROM Nota n WHERE n.eventoId = :evento and n.participanteId = :participante"),
    @NamedQuery(name = "Nota.findByModulo", query = "SELECT n FROM Nota n WHERE n.moduloId = :modulo"),
    @NamedQuery(name = "Nota.findByEvento", query = "SELECT n FROM Nota n WHERE n.eventoId = :evento")})
@JsonIgnoreProperties(value={"eventoId", "moduloId", "participanteId"})
@SequenceGenerator(name="sequence", sequenceName="seq_nota", allocationSize=1)
public class Nota implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "nota")
    private String nota;
    
	@NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "evento_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JsonIgnore
    private Evento eventoId;
    
	@NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "modulo_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JsonIgnore
    private Modulo moduloId;
    
	@NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "participante_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JsonIgnore
    private Participante participanteId;

    public Nota() {
    }

    public Nota(Long id) {
        this.id = id;
    }

    public Nota(Long id, String nota) {
        this.id = id;
        this.nota = nota;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Nota)) {
            return false;
        }
        Nota other = (Nota) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nota;
    }

}
