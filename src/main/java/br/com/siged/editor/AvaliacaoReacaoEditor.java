package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.AvaliacaoReacaoDAO;
import br.com.siged.entidades.AvaliacaoReacao;

public class AvaliacaoReacaoEditor extends PropertyEditorSupport{
	@Autowired
	private final AvaliacaoReacaoDAO avaliacaoreacaoDao;
	
	public AvaliacaoReacaoEditor(AvaliacaoReacaoDAO avaliacaoreacaoDao){
		this.avaliacaoreacaoDao = avaliacaoreacaoDao;
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(this.avaliacaoreacaoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		AvaliacaoReacao s = (AvaliacaoReacao) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}
}
