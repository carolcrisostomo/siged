package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.TipoInstrutorDAO;
import br.com.siged.entidades.TipoInstrutor;

public class TipoInstrutorEditor extends PropertyEditorSupport {
	@Autowired
	private final TipoInstrutorDAO tipoinstrutorDao;

	public TipoInstrutorEditor(TipoInstrutorDAO tipoinstrutorDao) {
		this.tipoinstrutorDao = tipoinstrutorDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(tipoinstrutorDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		TipoInstrutor s = (TipoInstrutor) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
