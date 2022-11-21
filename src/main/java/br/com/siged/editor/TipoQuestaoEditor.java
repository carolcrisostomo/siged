package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.TipoQuestaoDAO;
import br.com.siged.entidades.TipoQuestao;

public class TipoQuestaoEditor extends PropertyEditorSupport{
	@Autowired
	private final TipoQuestaoDAO tipoquestaoDao;
	
	public TipoQuestaoEditor(TipoQuestaoDAO tipoquestaoDao){
		this.tipoquestaoDao = tipoquestaoDao;
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(this.tipoquestaoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		TipoQuestao s = (TipoQuestao) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}
}
