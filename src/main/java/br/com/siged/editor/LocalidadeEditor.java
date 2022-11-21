package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.LocalidadeDAO;
import br.com.siged.entidades.externas.Localidade;

public class LocalidadeEditor extends PropertyEditorSupport {
	
	@Autowired
	private final LocalidadeDAO localidadeDAO;

	public LocalidadeEditor(LocalidadeDAO localidadeDAO) {
		this.localidadeDAO = localidadeDAO;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(localidadeDAO.find(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Localidade l = (Localidade) getValue();
		if (l == null) {
			return null;
		} else {
			return l.getIdLocalidade().toString();
		}
	}

}
