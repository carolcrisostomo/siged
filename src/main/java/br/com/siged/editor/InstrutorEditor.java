package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.InstrutorDAO;
import br.com.siged.entidades.Instrutor;

public class InstrutorEditor extends PropertyEditorSupport {
	@Autowired
	private final InstrutorDAO instrutorDao;

	public InstrutorEditor(InstrutorDAO instrutorDao) {
		this.instrutorDao = instrutorDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(instrutorDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Instrutor s = (Instrutor) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
