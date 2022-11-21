package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.TipoEventoDAO;
import br.com.siged.entidades.TipoEvento;

public class TipoEventoEditor extends PropertyEditorSupport {
	@Autowired
	private final TipoEventoDAO tipoeventoDao;

	public TipoEventoEditor(TipoEventoDAO tipoeventoDao) {
		this.tipoeventoDao = tipoeventoDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(tipoeventoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		TipoEvento s = (TipoEvento) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
