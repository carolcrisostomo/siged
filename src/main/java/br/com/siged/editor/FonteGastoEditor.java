package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.FonteGastoDAO;
import br.com.siged.entidades.FonteGasto;

public class FonteGastoEditor extends PropertyEditorSupport {
	@Autowired
	private final FonteGastoDAO fontegastoDao;

	public FonteGastoEditor(FonteGastoDAO fontegastoDao) {
		this.fontegastoDao = fontegastoDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(fontegastoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		FonteGasto s = (FonteGasto) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
