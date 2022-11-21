package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.EventoExtraDAO;
import br.com.siged.entidades.EventoExtra;

public class EventoExtraEditor extends PropertyEditorSupport {
	@Autowired
	private final EventoExtraDAO eventoextraDao;

	public EventoExtraEditor(EventoExtraDAO eventoextraDao) {
		this.eventoextraDao = eventoextraDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(eventoextraDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		EventoExtra s = (EventoExtra) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
