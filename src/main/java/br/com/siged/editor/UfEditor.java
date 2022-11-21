package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.UfDAO;
import br.com.siged.entidades.Uf;

public class UfEditor extends PropertyEditorSupport {
	@Autowired
	private final UfDAO ufDao;

	public UfEditor(UfDAO ufDao) {
		this.ufDao = ufDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(ufDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Uf s = (Uf) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
