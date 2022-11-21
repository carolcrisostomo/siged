package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.GrupoDAO;
import br.com.siged.entidades.externas.Grupo;

public class GrupoEditor extends PropertyEditorSupport {
	@Autowired
	private final GrupoDAO grupoDao;

	public GrupoEditor(GrupoDAO grupoDao) {
		this.grupoDao = grupoDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(grupoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Grupo s = (Grupo) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
