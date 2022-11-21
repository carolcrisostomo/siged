package br.com.siged.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Inscricao;
import br.com.siged.entidades.Instrutor;
import br.com.siged.entidades.Participante;

@Component("inscricaoValidator")
public class InscricaoValidator extends AbstractValidator {

	@Override
	protected boolean suporta(Class<?> clazz) {
		return Inscricao.class.equals(clazz);
	}

	@Override
	protected void adicionarValidacoesExtras(Object target, Errors errors) {
		Inscricao inscricao = (Inscricao) target;
		
		if (inscricao.getConfirmada() != null 
				&& inscricao.getConfirmada().equals("N") 
				&& (inscricao.getJustificativanaoconfirmacao() == null || inscricao.getJustificativanaoconfirmacao().isEmpty())) {
			errors.rejectValue("justificativanaoconfirmacao", "default.null.message");
		}
		
		if (inscricao.getParticipanteId() != null && inscricao.getEventoId() != null) {
			Participante participante = inscricao.getParticipanteId();
			Evento evento = inscricao.getEventoId();
			
			for(Instrutor instrutor : evento.getInstrutores()) {
				String cpfInstrutor = instrutor.getCpf() != null ? instrutor.getCpf().replace(".", "").replace("-", "") : "";
				String cpfParticipante = participante.getCpf().replace(".", "").replace("-", "");
				if(cpfInstrutor.equals(cpfParticipante)) {
					errors.rejectValue("participanteId", "inscricao.validator.participante.instrutor");
				}
			}
		}
			
	}

}
