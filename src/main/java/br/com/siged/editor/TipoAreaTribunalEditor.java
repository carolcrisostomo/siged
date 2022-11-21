package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.TipoAreaTribunalDAO;
import br.com.siged.entidades.TipoAreaTribunal;

public class TipoAreaTribunalEditor extends PropertyEditorSupport {
	@Autowired
	private final TipoAreaTribunalDAO tipoareatribunalDao;

	public TipoAreaTribunalEditor(TipoAreaTribunalDAO tipoareatribunalDao) {
		this.tipoareatribunalDao = tipoareatribunalDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(tipoareatribunalDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		TipoAreaTribunal s = (TipoAreaTribunal) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
