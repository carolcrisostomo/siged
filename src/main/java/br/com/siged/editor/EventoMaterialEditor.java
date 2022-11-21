package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.EventoMaterialDAO;
import br.com.siged.entidades.EventoMaterial;

public class EventoMaterialEditor extends PropertyEditorSupport {
	@Autowired
	private final EventoMaterialDAO eventoMaterialDao;

	public EventoMaterialEditor(EventoMaterialDAO eventoMaterialDao) {
		this.eventoMaterialDao = eventoMaterialDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(eventoMaterialDao.find(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		EventoMaterial m = (EventoMaterial) getValue();
		if (m == null) {
			return null;
		} else {
			return m.getId().toString();
		}
	}

}
