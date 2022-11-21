package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.FrequenciaDAO;
import br.com.siged.entidades.Frequencia;

public class FrequenciaEditor extends PropertyEditorSupport {
	@Autowired
	private final FrequenciaDAO frequenciaDao;

	public FrequenciaEditor(FrequenciaDAO frequenciaDao) {
		this.frequenciaDao = frequenciaDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(frequenciaDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Frequencia s = (Frequencia) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
