package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.UsuarioSCADAO;
import br.com.siged.entidades.externas.UsuarioSCA;

public class UsuarioSCAEditor extends PropertyEditorSupport {
	@Autowired
	private final UsuarioSCADAO usuarioDao;

	public UsuarioSCAEditor(UsuarioSCADAO usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(usuarioDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		UsuarioSCA s = (UsuarioSCA) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getNome().toString();
		}
	}

}
