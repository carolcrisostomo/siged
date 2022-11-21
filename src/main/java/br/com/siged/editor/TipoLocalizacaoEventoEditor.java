package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.TipoLocalizacaoEventoDAO;
import br.com.siged.entidades.TipoLocalizacaoEvento;

public class TipoLocalizacaoEventoEditor extends PropertyEditorSupport {
	@Autowired
	private final TipoLocalizacaoEventoDAO tipolocalizacaoeventoDao;

	public TipoLocalizacaoEventoEditor(TipoLocalizacaoEventoDAO tipolocalizacaoeventoDao) {
		this.tipolocalizacaoeventoDao = tipolocalizacaoeventoDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(tipolocalizacaoeventoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		TipoLocalizacaoEvento s = (TipoLocalizacaoEvento) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
