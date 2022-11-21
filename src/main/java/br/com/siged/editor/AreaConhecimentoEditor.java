package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.AreaConhecimentoDAO;
import br.com.siged.entidades.AreaConhecimento;

public class AreaConhecimentoEditor extends PropertyEditorSupport {
	@Autowired
	private final AreaConhecimentoDAO areaconhecimentoDao;

	public AreaConhecimentoEditor(AreaConhecimentoDAO areaconhecimentoDao) {
		this.areaconhecimentoDao = areaconhecimentoDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(areaconhecimentoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		AreaConhecimento s = (AreaConhecimento) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
