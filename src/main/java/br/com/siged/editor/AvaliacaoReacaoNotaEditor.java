package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.AvaliacaoReacaoNotaDAO;
import br.com.siged.entidades.AvaliacaoReacaoNota;

public class AvaliacaoReacaoNotaEditor extends PropertyEditorSupport{
	@Autowired
	private final AvaliacaoReacaoNotaDAO avaliacaoreacaonotaDao;
	
	public AvaliacaoReacaoNotaEditor(AvaliacaoReacaoNotaDAO avaliacaoreacaonotaDao){
		this.avaliacaoreacaonotaDao = avaliacaoreacaonotaDao;
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(this.avaliacaoreacaonotaDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		AvaliacaoReacaoNota s = (AvaliacaoReacaoNota) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}
}
