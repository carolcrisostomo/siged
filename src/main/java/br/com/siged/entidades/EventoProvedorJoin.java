package br.com.siged.entidades;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "evento_provedor_join")
public class EventoProvedorJoin {

	@EmbeddedId
	private EventoProvedorJoinId id;

	public EventoProvedorJoinId getId() {
		return id;
	}

	public void setId(EventoProvedorJoinId id) {
		this.id = id;
	}
}
