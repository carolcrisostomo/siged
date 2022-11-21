package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.InscricaoDAO;
import br.com.siged.entidades.Inscricao;

public class InscricaoEditor extends PropertyEditorSupport {
	@Autowired
	private final InscricaoDAO inscricaoDao;

	public InscricaoEditor(InscricaoDAO inscricaoDao) {
		this.inscricaoDao = inscricaoDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(inscricaoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Inscricao s = (Inscricao) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
