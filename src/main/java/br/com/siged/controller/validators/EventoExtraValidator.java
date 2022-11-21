package br.com.siged.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import br.com.siged.entidades.EventoExtra;
import br.com.siged.util.ValidatorUtil;

@Component(value = "eventoExtraValidator")
public class EventoExtraValidator extends AbstractValidator {
	
	@Autowired
	private ValidatorUtil validatorUtil;

	@Override
	protected boolean suporta(Class<?> clazz) {
		return EventoExtra.class.equals(clazz);
	}

	@Override
	protected void adicionarValidacoesExtras(Object target, Errors errors) {
		EventoExtra eventoextra = (EventoExtra) target;
		
		validarData(eventoextra, errors);

		validarLocal(eventoextra, errors);
		
		validarSiteEMaterial(eventoextra, errors);
	}

	private void validarSiteEMaterial(EventoExtra eventoextra, Errors errors) {
		byte[] material = eventoextra.getMaterial();
		if(eventoextra.getSite() != null && !eventoextra.getSite().isEmpty()) {
			if(!this.validatorUtil.validarURL(eventoextra.getSite())) {
				errors.rejectValue("site", "eventoextra.validator.site.invalido");
			}
		} else if(material == null || material.length == 0) {
			errors.rejectValue("material", "eventoextra.validator.material.campoobrigatorio");
		}
	}

	private void validarLocal(EventoExtra eventoextra, Errors errors) {
		if (eventoextra.getPaisId() != null
				&& eventoextra.getPaisId().getId() == 1) {

			if (eventoextra.getUfMunicipio() == null
					|| eventoextra.getUfMunicipio().getUf().equals("0")) {
				errors.rejectValue("ufMunicipio", "default.null.message");
			}
			if (eventoextra.getMunicipio() == null
					|| eventoextra.getMunicipio().getId() == 0) {
				errors.rejectValue("municipio", "default.null.message");
			}
		}
	}

	private void validarData(EventoExtra eventoextra, Errors errors) {
		if (eventoextra.getDataInicio() != null
				&& eventoextra.getDataFim() != null) {
			if (eventoextra.getDataInicio().after(eventoextra.getDataFim())) {
				errors.rejectValue("dataInicio", "eventoextra.validator.dataInicio.maiorquefinal");
			}
		}
	}

	
}
