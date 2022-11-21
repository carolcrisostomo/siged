package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.AvaliacaoDAO;
import br.com.siged.entidades.Avaliacao;

/**
 * 
 * @deprecated - Substituir por AvaliacaoReacaoEditor
 *
 */
public class AvaliacaoEditor extends PropertyEditorSupport {
	@Autowired
	private final AvaliacaoDAO avaliacaoDao;

	public AvaliacaoEditor(AvaliacaoDAO avaliacaoDao) {
		this.avaliacaoDao = avaliacaoDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(avaliacaoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Avaliacao s = (Avaliacao) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
