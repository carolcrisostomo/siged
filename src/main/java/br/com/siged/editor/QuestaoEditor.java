package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.QuestaoDAO;
import br.com.siged.entidades.Questao;

public class QuestaoEditor extends PropertyEditorSupport{
	@Autowired
	private final QuestaoDAO questaoDao;
	
	public QuestaoEditor(QuestaoDAO questaoDao){
		this.questaoDao = questaoDao;
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(this.questaoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Questao s = (Questao) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}
}
