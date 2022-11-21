package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.NotaDAO;
import br.com.siged.entidades.Nota;

public class NotaEditor extends PropertyEditorSupport {
	@Autowired
	private final NotaDAO notaDao;

	public NotaEditor(NotaDAO notaDao) {
		this.notaDao = notaDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(notaDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Nota s = (Nota) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
