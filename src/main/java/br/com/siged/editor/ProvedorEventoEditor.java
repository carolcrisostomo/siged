package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.ProvedorEventoDAO;
import br.com.siged.entidades.ProvedorEvento;

public class ProvedorEventoEditor extends PropertyEditorSupport {
	@Autowired
	private final ProvedorEventoDAO provedoreventoDao;

	public ProvedorEventoEditor(ProvedorEventoDAO provedoreventoDao) {
		this.provedoreventoDao = provedoreventoDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(provedoreventoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		ProvedorEvento s = (ProvedorEvento) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
