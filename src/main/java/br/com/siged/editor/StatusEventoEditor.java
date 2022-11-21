package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import br.com.siged.entidades.StatusEvento;

public class StatusEventoEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		StatusEvento statusEvento = null;
		for (StatusEvento status : StatusEvento.values()) {
			if (status.getFiltro().equals(text)) {
				statusEvento = status;
				break;
			}
		}
		setValue(statusEvento);
	}
}
