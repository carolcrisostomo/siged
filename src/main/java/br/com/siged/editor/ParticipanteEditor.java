package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.entidades.Participante;

public class ParticipanteEditor extends PropertyEditorSupport {
	@Autowired
	private final ParticipanteDAO participanteDao;

	public ParticipanteEditor(ParticipanteDAO participanteDao) {
		this.participanteDao = participanteDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(participanteDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Participante s = (Participante) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
