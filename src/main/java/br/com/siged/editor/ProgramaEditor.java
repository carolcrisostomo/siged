package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.ProgramaDAO;
import br.com.siged.entidades.Programa;

public class ProgramaEditor extends PropertyEditorSupport {

	@Autowired
	private final ProgramaDAO programaDao;
	
	public ProgramaEditor(ProgramaDAO programaDao) {
		this.programaDao = programaDao;
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(programaDao.findById(Long.parseLong(text)));
	}
	
	@Override
	public String getAsText() {
		Programa p = (Programa) getValue();
		if(p == null) {
			return null;
		} else {
			return p.getId().toString();
		}
	}
}
