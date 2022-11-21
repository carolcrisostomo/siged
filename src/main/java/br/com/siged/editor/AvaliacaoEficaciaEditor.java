package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.AvaliacaoEficaciaDAO;
import br.com.siged.entidades.AvaliacaoEficacia;

public class AvaliacaoEficaciaEditor extends PropertyEditorSupport {
	@Autowired
	private final AvaliacaoEficaciaDAO avaliacaoeficaciaDao;

	public AvaliacaoEficaciaEditor(AvaliacaoEficaciaDAO avaliacaoeficaciaDao) {
		this.avaliacaoeficaciaDao = avaliacaoeficaciaDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(avaliacaoeficaciaDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		AvaliacaoEficacia s = (AvaliacaoEficacia) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
