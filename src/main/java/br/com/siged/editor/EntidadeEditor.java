package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.EntidadeDAO;
import br.com.siged.entidades.externas.Entidade;

public class EntidadeEditor extends PropertyEditorSupport {
	@Autowired
	private final EntidadeDAO entidadeDAO;

	public EntidadeEditor(EntidadeDAO entidadeDAO) {
		this.entidadeDAO = entidadeDAO;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(entidadeDAO.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Entidade s = (Entidade) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getIdentidade().toString();
		}
	}

}
