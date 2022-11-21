package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.ResponsavelSetorDAO;
import br.com.siged.entidades.ResponsavelSetor;

public class ResponsavelSetorEditor extends PropertyEditorSupport {
	@Autowired
	private final ResponsavelSetorDAO responsavelSetorDao;

	public ResponsavelSetorEditor(ResponsavelSetorDAO responsavelSetorDao) {
		this.responsavelSetorDao = responsavelSetorDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(responsavelSetorDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		ResponsavelSetor s = (ResponsavelSetor) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
