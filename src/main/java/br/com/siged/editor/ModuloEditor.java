package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.ModuloDAO;
import br.com.siged.entidades.Modulo;

public class ModuloEditor extends PropertyEditorSupport {
	@Autowired
	private final ModuloDAO moduloDao;

	public ModuloEditor(ModuloDAO moduloDao) {
		this.moduloDao = moduloDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(moduloDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Modulo s = (Modulo) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
