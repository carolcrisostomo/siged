package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.GastoDAO;
import br.com.siged.entidades.Gasto;

public class GastoEditor extends PropertyEditorSupport {
	@Autowired
	private final GastoDAO gastoDao;

	public GastoEditor(GastoDAO gastoDao) {
		this.gastoDao = gastoDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(gastoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Gasto s = (Gasto) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
