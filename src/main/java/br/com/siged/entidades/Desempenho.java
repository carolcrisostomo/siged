package br.com.siged.entidades;

import java.io.Serializable;

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


@Entity
@Table(name = "desempenho")
@NamedQueries({
	@NamedQuery(name = "Desempenho.findById", query = "SELECT d FROM Desempenho d WHERE d.id = :id"),
    @NamedQuery(name = "Desempenho.findByEvento", query = "SELECT d FROM Desempenho d WHERE d.eventoId = :evento ORDER BY moduloId, participanteId.nome"),
    @NamedQuery(name = "Desempenho.findByEventoId", query = "SELECT d FROM Desempenho d WHERE d.eventoId.id = :eventoId"),
    @NamedQuery(name = "Desempenho.findByModulo", query = "SELECT d FROM Desempenho d WHERE d.moduloId = :modulo ORDER BY moduloId, participanteId.nome"),
    @NamedQuery(name = "Desempenho.findByEventoAndSetor", query = "SELECT d FROM Desempenho d WHERE d.eventoId = :evento and d.participanteId.setorId = :setor ORDER BY moduloId, participanteId"),
    @NamedQuery(name = "Desempenho.findByEventoAndParticipanteAndAprovado", query = "SELECT d FROM Desempenho d WHERE d.eventoId = :evento and d.participanteId = :participante and d.aprovado = :aprovado ORDER BY moduloId, participanteId"),
    @NamedQuery(name = "Desempenho.findByEventoAndParticipante", query = "SELECT d FROM Desempenho d WHERE d.eventoId = :evento and d.participanteId = :participante ORDER BY moduloId, participanteId"),
    @NamedQuery(name = "Desempenho.findByEventoAndParticipanteAndParcial", query = "SELECT d FROM Desempenho d WHERE d.eventoId = :evento and d.participanteId = :participante and d.parcial = true ORDER BY moduloId, participanteId"),
    @NamedQuery(name = "Desempenho.deleteByEventoId", query = "DELETE FROM Desempenho d WHERE d.eventoId = :evento")})
@SequenceGenerator(name="sequence", sequenceName="seq_desempenho", allocationSize=1)
public class Desempenho implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@JoinColumn(name = "evento_id", referencedColumnName = "id")
    @ManyToOne
    private Evento eventoId;
    
	@JoinColumn(name = "modulo_id", referencedColumnName = "id")
    @ManyToOne
    private Modulo moduloId;
    
	@JoinColumn(name = "participante_id", referencedColumnName = "id")
    @ManyToOne
    private Participante participanteId;
    
	@Column(name = "nota")
    private String nota;
    
	@Column(name = "frequencia")
    private String frequencia;
    
	@Column(name = "aprovado")
    private String aprovado;
	
	@Column(name = "parcial")
	private Boolean parcial;

    public Desempenho() {
    }

    public Desempenho(Long id) {
        this.id = id;
    }

    public Desempenho(Evento eventoId, Modulo moduloId,
			Participante participanteId, String nota, String frequencia,
			String aprovado) {
		super();
		this.eventoId = eventoId;
		this.moduloId = moduloId;
		this.participanteId = participanteId;
		this.nota = nota;
		this.frequencia = frequencia;
		this.aprovado = aprovado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(String frequencia) {
		this.frequencia = frequencia;
	}

	public String getAprovado() {
		return aprovado;
	}

	public void setAprovado(String aprovado) {
		this.aprovado = aprovado;
	}

	public Boolean getParcial() {
		return parcial;
	}

	public void setParcial(Boolean parcial) {
		this.parcial = parcial;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Desempenho)) {
            return false;
        }
        Desempenho other = (Desempenho) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
