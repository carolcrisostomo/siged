package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.SituacaoInstrutorDAO;
import br.com.siged.entidades.SituacaoInstrutor;

public class SituacaoInstrutorEditor extends PropertyEditorSupport {
	@Autowired
	private final SituacaoInstrutorDAO situacaoinstrutorDao;

	public SituacaoInstrutorEditor(SituacaoInstrutorDAO situacaoinstrutorDao) {
		this.situacaoinstrutorDao = situacaoinstrutorDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(situacaoinstrutorDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		SituacaoInstrutor s = (SituacaoInstrutor) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
