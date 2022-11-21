package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.FormacaoAcademicaDAO;
import br.com.siged.entidades.FormacaoAcademica;

public class FormacaoAcademicaEditor extends PropertyEditorSupport {
	@Autowired
	private final FormacaoAcademicaDAO formacaoacademicaDao;

	public FormacaoAcademicaEditor(FormacaoAcademicaDAO formacaoacademicaDao) {
		this.formacaoacademicaDao = formacaoacademicaDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(formacaoacademicaDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		FormacaoAcademica s = (FormacaoAcademica) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
