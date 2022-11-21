package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.NivelEscolaridadeDAO;
import br.com.siged.entidades.NivelEscolaridade;

public class NivelEscolaridadeEditor extends PropertyEditorSupport {
	@Autowired
	private final NivelEscolaridadeDAO nivelescolaridadeDao;

	public NivelEscolaridadeEditor(NivelEscolaridadeDAO nivelescolaridadeDao) {
		this.nivelescolaridadeDao = nivelescolaridadeDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(nivelescolaridadeDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		NivelEscolaridade s = (NivelEscolaridade) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
