package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.SetorDAO;
import br.com.siged.entidades.externas.Setor;

public class SetorEditor extends PropertyEditorSupport {
	@Autowired
	private final SetorDAO setorDao;

	public SetorEditor(SetorDAO setorDao) {
		this.setorDao = setorDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(setorDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Setor s = (Setor) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
