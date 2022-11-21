package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.CidadeDAO;
import br.com.siged.entidades.Cidade;

public class CidadeEditor extends PropertyEditorSupport {
	@Autowired
	private final CidadeDAO cidadeDao;

	public CidadeEditor(CidadeDAO cidadeDao) {
		this.cidadeDao = cidadeDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(cidadeDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Cidade s = (Cidade) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
