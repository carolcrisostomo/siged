package br.com.siged.controller.validators;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.entidades.Participante;

/**
 * Regras de validação para participante
 * 
 * @since 09/10/2018
 * @author Rafael de Castro
 *
 */
@Component(value = "participanteValidator")
public class ParticipanteValidator extends AbstractValidator{

	@Autowired
	private ParticipanteDAO participanteDao;
	
	@Override
	protected boolean suporta(Class<?> clazz) {
		return Participante.class.equals(clazz);
	}

	@Override
	protected void adicionarValidacoesExtras(Object target, Errors errors) {
		Participante participante = (Participante) target;
		
		if (participante.getTipoId() == null){			
			errors.rejectValue("tipoId", "default.null.message");		
		}else{ 
			verificarParticipanteJurisdicionado(errors, participante);
		}
		
		verificarLocal(errors, participante);
		
		verificarEmailAndCPF(errors, participante);
	}

	private void verificarEmailAndCPF(Errors errors, Participante participante) {
		Participante participanteJaCadastrado = participanteDao.findByCpf(participante.getCpf());
		List<Participante> participantes = participanteDao.findByEmail(participante.getEmail());
		
		if(participante.isNovo()) {
			if(participanteJaCadastrado != null)
				errors.rejectValue("cpf", "participante.validator.cpf.jacadastrado");
			
			if(participantes.size() > 0 )
				errors.rejectValue("email", "participante.validator.email.jacadastrado");
			
		} else {
			if(participanteJaCadastrado != null && !participanteJaCadastrado.getId().equals(participante.getId()))
				errors.rejectValue("cpf", "participante.validator.cpf.jacadastrado");
			
			for (Participante outroParticipante : participantes) {
				if(!outroParticipante.getId().equals(participante.getId()) && outroParticipante.getEmail().equals(participante.getEmail()) ){
					errors.rejectValue("email", "participante.validator.email.jacadastrado");
					break;
				}
			}
		}
	}

	private void verificarLocal(Errors errors, Participante participante) {
		if (participante.getUfMunicipio() == null)
			errors.rejectValue("ufMunicipio", "default.null.message");
		
		if (participante.getMunicipio() == null)
			errors.rejectValue("municipio", "default.null.message");
	}

	private void verificarParticipanteJurisdicionado(Errors errors, Participante participante) {
		if (participante.getTipoId().isJurisdicionado()){
			
//			if(participante.getMatricula() != null && participante.getMatricula().equals(""))				
//				errors.rejectValue("matricula", "default.null.message");
			
			if(participante.getOrgaoId() != null && participante.getOrgaoId().getIdentidade() == 0)				
				errors.rejectValue("orgaoId", "default.null.message");
			
			if(participante.getLotacao() == null || participante.getLotacao().equals(""))
				errors.rejectValue("lotacao", "default.null.message");				
		}
	}

}
