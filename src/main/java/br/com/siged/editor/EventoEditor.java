package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.EventoDAO;
import br.com.siged.entidades.Evento;

public class EventoEditor extends PropertyEditorSupport {
	@Autowired
	private final EventoDAO eventoDao;

	public EventoEditor(EventoDAO eventoDao) {
		this.eventoDao = eventoDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(eventoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Evento s = (Evento) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
