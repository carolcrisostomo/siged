package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.ModalidadeDAO;
import br.com.siged.entidades.Modalidade;

public class ModalidadeEditor extends PropertyEditorSupport {
	@Autowired
	private final ModalidadeDAO modalidadeDao;

	public ModalidadeEditor(ModalidadeDAO modalidadeDao) {
		this.modalidadeDao = modalidadeDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(modalidadeDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Modalidade s = (Modalidade) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
