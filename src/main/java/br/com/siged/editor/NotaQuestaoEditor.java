package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.NotaQuestaoDAO;
import br.com.siged.entidades.NotaQuestao;

public class NotaQuestaoEditor extends PropertyEditorSupport{
	@Autowired
	private final NotaQuestaoDAO notaquestaoDao;
	
	public NotaQuestaoEditor(NotaQuestaoDAO notaquestaoDao){
		this.notaquestaoDao = notaquestaoDao;
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(this.notaquestaoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		NotaQuestao s = (NotaQuestao) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}
}
