package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.TipoPublicoAlvoDAO;
import br.com.siged.entidades.TipoPublicoAlvo;

public class TipoPublicoAlvoEditor extends PropertyEditorSupport {
	@Autowired
	private final TipoPublicoAlvoDAO tipopublicoalvoDao;

	public TipoPublicoAlvoEditor(TipoPublicoAlvoDAO tipopublicoalvoDao) {
		this.tipopublicoalvoDao = tipopublicoalvoDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(tipopublicoalvoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		TipoPublicoAlvo s = (TipoPublicoAlvo) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
