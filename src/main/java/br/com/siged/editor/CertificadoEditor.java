package br.com.siged.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.siged.dao.CertificadoDAO;
import br.com.siged.entidades.Certificado;

public class CertificadoEditor extends PropertyEditorSupport {
	@Autowired
	private final CertificadoDAO certificadoDao;

	public CertificadoEditor(CertificadoDAO certificadoDao) {
		this.certificadoDao = certificadoDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(certificadoDao.findById(Long.parseLong(text)));
	}

	@Override
	public String getAsText() {
		Certificado s = (Certificado) getValue();
		if (s == null) {
			return null;
		} else {
			return s.getId().toString();
		}
	}

}
