package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.TipoGastoDAO;
import br.com.siged.entidades.TipoGasto;

public class TipoGastoEditor extends PropertyEditorSupport {
	@Autowired
	private final TipoGastoDAO tipogastoDao;

	public TipoGastoEditor(TipoGastoDAO tipogastoDao) {
		this.tipogastoDao = tipogastoDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(tipogastoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		TipoGasto s = (TipoGasto) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
