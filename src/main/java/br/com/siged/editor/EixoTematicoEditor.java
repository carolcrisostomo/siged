package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.EixoTematicoDAO;
import br.com.siged.entidades.EixoTematico;

public class EixoTematicoEditor extends PropertyEditorSupport {
	@Autowired
	private final EixoTematicoDAO eixotematicoDao;

	public EixoTematicoEditor(EixoTematicoDAO eixotematicoDao) {
		this.eixotematicoDao = eixotematicoDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(eixotematicoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		EixoTematico s = (EixoTematico) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
