package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.MunicipioDAO;
import br.com.siged.entidades.externas.Municipio;

public class MunicipioEditor extends PropertyEditorSupport {
	@Autowired
	private final MunicipioDAO municipioDAO;

	public MunicipioEditor(MunicipioDAO municipioDAO) {
		this.municipioDAO = municipioDAO;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(municipioDAO.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Municipio s = (Municipio) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
