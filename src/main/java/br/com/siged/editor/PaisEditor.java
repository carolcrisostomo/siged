package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.PaisDAO;
import br.com.siged.entidades.Pais;

public class PaisEditor extends PropertyEditorSupport {
	@Autowired
	private final PaisDAO paisDao;

	public PaisEditor(PaisDAO paisDao) {
		this.paisDao = paisDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(paisDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Pais s = (Pais) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
