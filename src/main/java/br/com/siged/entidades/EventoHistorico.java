package br.com.siged.entidades;

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

@Entity
@Table(name = "evento_historico")
@NamedQueries({
    @NamedQuery(name = "EventoHistorico.findAll", query = "SELECT e FROM EventoHistorico e"),
    @NamedQuery(name = "EventoHistorico.findById", query = "SELECT e FROM EventoHistorico e WHERE e.id = :id"),
    @NamedQuery(name = "EventoHistorico.findByEvento", query = "SELECT e FROM EventoHistorico e WHERE e.eventoId = :evento ORDER BY e.id")})
@SequenceGenerator(name="sequence", sequenceName="seq_evento_historico", allocationSize=1)
public class EventoHistorico {
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotNull
    @JoinColumn(name = "evento_id", referencedColumnName = "id")
    @ManyToOne(optional = false) @JsonIgnore
    private Evento eventoId;
    
    @NotNull
    @Column(name = "data_inicio_previsto")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicioPrevisto;
    
    @NotNull
    @Column(name = "data_fim_previsto")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFimPrevisto;

	public EventoHistorico() {
		super();
	}

	public EventoHistorico(Evento eventoId, Date dataInicioPrevisto,
			Date dataFimPrevisto) {
		super();
		this.eventoId = eventoId;
		this.dataInicioPrevisto = dataInicioPrevisto;
		this.dataFimPrevisto = dataFimPrevisto;
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

	public Date getDataInicioPrevisto() {
		return dataInicioPrevisto;
	}

	public void setDataInicioPrevisto(Date dataInicioPrevisto) {
		this.dataInicioPrevisto = dataInicioPrevisto;
	}

	public Date getDataFimPrevisto() {
		return dataFimPrevisto;
	}

	public void setDataFimPrevisto(Date dataFimPrevisto) {
		this.dataFimPrevisto = dataFimPrevisto;
	}    

}
