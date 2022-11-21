package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.UfMunicipioDAO;
import br.com.siged.entidades.externas.UfMunicipio;

public class UfMunicipioEditor extends PropertyEditorSupport {
	@Autowired
	private final UfMunicipioDAO ufMunicipioDAO;

	public UfMunicipioEditor(UfMunicipioDAO ufMunicipioDAO) {
		this.ufMunicipioDAO = ufMunicipioDAO;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(ufMunicipioDAO.findByUf(text));
	}

	@Override
	public String getAsText() {
		UfMunicipio s = (UfMunicipio) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getUf();
		}
	}

}
