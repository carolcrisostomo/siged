package br.com.siged.controller.validators;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import br.com.siged.dao.FrequenciaDAO;
import br.com.siged.dao.InscricaoDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Frequencia;
import br.com.siged.entidades.Instrutor;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Participante;
import br.com.siged.util.Util;
import br.com.siged.util.ValidatorUtil;

/**
 * Regras de validação para o Módulo
 * 
 * @since 25/09/2018
 * @author Rafael de Castro
 */
@Component(value = "moduloValidator")
public class ModuloValidator extends AbstractValidator {

	@Autowired
	private Util util;
	
	@Autowired
	private ValidatorUtil validatorUtil;
	
	@Autowired
	private FrequenciaDAO frequenciaDao;
	
	@Autowired
	private InscricaoDAO inscricaoDao;
	
	@Autowired
	private ParticipanteDAO participanteDao;

	@Override
	protected boolean suporta(Class<?> clazz) {
		return Modulo.class.equals(clazz);
	}
	
	@Override
	protected void adicionarValidacoesExtras(Object target, Errors errors) {
		Modulo modulo = (Modulo) target;
		
		garantirIntegridadeDatas(modulo, errors);
		
		garantirIntegridadeCargaHoraria(modulo, errors);
		
		garantirIntegridadeDosTurnos(modulo, errors);
		
		garantirIntegridadeInstrutores(modulo, errors);
		
		garantirIntegridadeLocalizacao(modulo, errors);

	}

	/**
	 * Regras de validação para as datas:<p/>
	 * 
	 * - Data Início do Modulo não pode ser depois da Data Fim<br/>
	 * - Data Início do Modulo não pode ser antes da Data Início do Evento<br/>
	 * - Data Fim do Modulo não pode ser depois da Data Fim do Evento<br/>
	 * 
	 * @param errors
	 * @param modulo
	 */
	private void garantirIntegridadeDatas(Modulo modulo, Errors errors) {
		if (modulo.getDataInicio() != null && modulo.getDataFim() != null) {
			if (modulo.getDataInicio().after(modulo.getDataFim())) {
				errors.rejectValue("dataInicio", "modulo.validator.datainicio.depoisdatafim");
			} else {
				Date dataInicioEvento = null;
				Date dataFimEvento = null;
				Date dataInicioModulo = util.getDataFormatada(modulo.getDataInicio());
				Date dataFimModulo = util.getDataFormatada(modulo.getDataFim());
				
				if(modulo.getEventoId().getDataInicioRealizacao() != null) {
					dataInicioEvento = util.getDataFormatada(modulo.getEventoId().getDataInicioRealizacao());
				} else {
					dataInicioEvento = util.getDataFormatada(modulo.getEventoId().getDataInicioPrevisto());
				}
				
				if(modulo.getEventoId().getDataFimRealizacao() != null) {
					dataFimEvento = util.getDataFormatada(modulo.getEventoId().getDataFimRealizacao());
				} else {
					dataFimEvento = util.getDataFormatada(modulo.getEventoId().getDataFimPrevisto());
				}
				
				if(dataInicioEvento != null && dataInicioModulo.before(dataInicioEvento)) {
					errors.rejectValue("dataInicio", "modulo.validator.datainicio.antesinicioevento");
				}
				
				if(dataFimEvento != null && dataFimModulo.after(dataFimEvento)) {
					errors.rejectValue("dataFim", "modulo.validator.datafim.depoisfimevento");
				}
			}
			
			if(modulo.getId() != null) {
				Date inicioModulo = util.getDataFormatada(modulo.getDataInicio());
				Date fimModulo = util.getDataFormatada(modulo.getDataFim());
				
				List<Frequencia> frequencias = frequenciaDao.findByModulo(modulo);
				
				frequenciasLoop:
				for(Frequencia frequencia : frequencias) {
					Date dataFrequencia = util.getDataFormatada(frequencia.getData());
					Boolean frequenciaImcompativel = false;
					if(dataFrequencia.before(inicioModulo)) {
						errors.rejectValue("dataInicio", "modulo.validator.datainicio.frequenciasIncompativeis");
						frequenciaImcompativel = true;
					}
					if(dataFrequencia.after(fimModulo)) {
						errors.rejectValue("dataFim", "modulo.validator.datafim.frequenciasIncompativeis");
						frequenciaImcompativel = true;
					}
					
					if(frequenciaImcompativel) {
						break frequenciasLoop;
					}
				}
			}
			
		}
	}
	
	/**
	 * Regras de validação referente a carga horária:<p/>
	 * 
	 * - Carga Horária do módulo não pode ser diferente do evento quando o mesmo é módulo único e ainda não tem nenhum módulo cadastrado<br/>
	 * - Carga Horária dos Módulos não pode ser maior que a Carga Horária do Evento<br/>
	 * - Carga Horária do módulo deve ser > 0
	 * 
	 * @param modulo
	 * @param errors
	 */
	private void garantirIntegridadeCargaHoraria(Modulo modulo, Errors errors) {
		if(modulo.getEventoId() != null && (modulo.getCargaHoraria() != null && !modulo.getCargaHoraria().isEmpty())) {
			try {
				int cargaHorariaDoEvento = Integer.valueOf(modulo.getEventoId().getCargaHoraria());
				int cargaHorariaDosModulos = 0;
				int cargaHorariaDoModulo = Integer.valueOf(modulo.getCargaHoraria());
				for(Modulo moduloDoEvento : modulo.getEventoId().getModuloList()) {
					if(!moduloDoEvento.getId().equals(modulo.getId())) {
						cargaHorariaDosModulos += Integer.valueOf(moduloDoEvento.getCargaHoraria());
					}
				}
				if(cargaHorariaDoModulo == 0) {
					errors.rejectValue("cargaHoraria", "modulo.validator.cargahoraria.igualazero");
				} else if(modulo.getEventoId().getModuloUnico().equals("S") && modulo.getEventoId().getModuloList().isEmpty() && cargaHorariaDoModulo != cargaHorariaDoEvento) {
					errors.rejectValue("cargaHoraria", "modulo.validator.cargahoraria.modulounicodiferentedoevento", new Object[] {cargaHorariaDoEvento}, "modulo.validator.cargahoraria.modulounicodiferentedoevento");
				} else if((cargaHorariaDoModulo + cargaHorariaDosModulos) > cargaHorariaDoEvento)
					errors.rejectValue("cargaHoraria", "modulo.validator.cargahoraria.maiorqueadoevento", new Object[] {cargaHorariaDoEvento}, "modulo.validator.cargahoraria.maiorqueadoevento");
				
			} catch (NumberFormatException e) {
				errors.rejectValue("cargaHoraria", "modulo.validator.cargahoraria.formatoinvalido");
			}
		}
	}
	
	/**
	 * Regras de validação referente aos turnos:<p/>
	 * 
	 * - Hora Início e Hora Fim do Turno 1 são obrigatórias quando o Módulo é Presencial<br/>
	 * - Hora Início do Turno X não pode ser > que a Hora Fim do Turno X (X de 1 a 3 se houver)<br/>
	 * - Hora Fim do Turno X é obrigatória quando informada hora Início do Turno X (X de 1 a 3 se houver)<br/>
	 * - Hora Início do Turno X é obrigatória quando informada hora Fim do Turno X (X de 1 a 3 se houver)<br/>
	 * - Hora Início e Hora Fim Turno 2 são obrigatórias quando informadas Hora Início e Hora Fim do Turno 3<br/>
	 * - Hora Fim Turno 1 tem que ser <= que a Hora Início do Turno 2 (se houver)<br/>
	 * - Hora Fim Turno 2 (se houver) tem que ser <= que a Hora Início do Turno 3 (se houver)<p/>
	 * 
	 * @param errors
	 * @param modulo
	 */
	private void garantirIntegridadeDosTurnos(Modulo modulo, Errors errors) {
		/**
		 * Hora Início e Hora Fim do Turno 1 são obrigatórias quando o Módulo é Presencial
		 */
		if (modulo.getModalidade() != null && modulo.getModalidade().isPresencial()) {
			ValidationUtils.rejectIfEmpty(errors, "horaInicioTurno1", "default.null.message");
			ValidationUtils.rejectIfEmpty(errors, "horaFimTurno1", "default.null.message");
		}
		
		Map<String, String> turnos = modulo.getHorasDosTurnosMap(modulo);
		Map<String, Date> turnosHoras = new HashMap<>();
		
		for(String key : turnos.keySet()) {
			if(validatorUtil.validarHora(turnos.get(key))) {
				try {
					turnosHoras.put(key, util.parseHora(turnos.get(key)));
				} catch (ParseException e) {
					errors.rejectValue(key, "modulo.validator.horaturno.formatoinvalido");
				}
			} else if(turnos.get(key) == null || turnos.get(key).isEmpty()) {
				turnosHoras.put(key, null);
			} else {
				errors.rejectValue(key, "modulo.validator.horaturno.formatoinvalido");
			}
		}
		
		/**
		 * Hora Início do Turno X não pode ser > que a Hora Fim do Turno X (X de 1 a 3 se houver)
		 * Hora Fim do Turno X é obrigatória quando informada hora Início do Turno X (X de 1 a 3 se houver)
		 * Hora Início do Turno X é obrigatória quando informada hora Fim do Turno X (X de 1 a 3 se houver)
		 * Hora Início e Hora Fim Turno 2 são obrigatórias quando informadas Hora Início e Hora Fim do Turno 3
		 */
		for(int i = 1; i <= 3; i++) {
			String horaInicioTurno = "horaInicioTurno" + i;
			String horaFimTurno = "horaFimTurno" + i;
			if ((turnosHoras.get(horaInicioTurno) != null && turnosHoras.get(horaFimTurno) != null)) {
				if(turnosHoras.get(horaInicioTurno).after(turnosHoras.get(horaFimTurno))) {
					errors.rejectValue(horaInicioTurno, "modulo.validator.horaturno.horainiciodepoishorafim");
				}
				if(i == 3) {
					if(turnosHoras.get("horaInicioTurno2") == null)
						errors.rejectValue("horaInicioTurno2", "modulo.validator.horaturno.turno3semturno2");
					if(turnosHoras.get("horaFimTurno2") == null)
						errors.rejectValue("horaFimTurno2", "modulo.validator.horaturno.turno3semturno2");
				}
			} else if(turnosHoras.get(horaFimTurno) == null && turnosHoras.get(horaInicioTurno) != null) {
				errors.rejectValue(horaFimTurno, "modulo.validator.horaturno.semhorafim");
			} else if(turnosHoras.get(horaInicioTurno) == null &&turnosHoras.get(horaFimTurno) != null) {
				errors.rejectValue(horaInicioTurno, "modulo.validator.horaturno.semhorainicio");
			}
		}
		
		/**
		 * Hora Fim Turno 1 tem que ser <= que a Hora Início do Turno 2 (se houver)
		 * Hora Fim Turno 2 (se houver) tem que ser <= que a Hora Início do Turno 3 (se houver)
		 */
		if((turnosHoras.get("horaFimTurno1") != null && turnosHoras.get("horaInicioTurno2") != null) && turnosHoras.get("horaFimTurno1").after(turnosHoras.get("horaInicioTurno2")))
			errors.rejectValue("horaFimTurno1", "modulo.validator.horaturno.fimdeturnodepoisdoiniciodoproximoturno", new Object[] {1, 2}, "modulo.validator.horaturno.fimdeturnodepoisdoiniciodoproximoturno");
		
		if((turnosHoras.get("horaFimTurno2") != null && turnosHoras.get("horaInicioTurno3") != null) && turnosHoras.get("horaFimTurno2").after(turnosHoras.get("horaInicioTurno3")))
			errors.rejectValue("horaFimTurno2", "modulo.validator.horaturno.fimdeturnodepoisdoiniciodoproximoturno", new Object[] {2, 3}, "modulo.validator.horaturno.fimdeturnodepoisdoiniciodoproximoturno");
		
	}
	
	/**
	 * Regras de validação para os instrutores:<p/>
	 * - Instrutor é obrigatório e não pode ter mais de 03 no Módulo
	 * 
	 * @param errors
	 * @param modulo
	 */
	private void garantirIntegridadeInstrutores(Modulo modulo, Errors errors) {
		if (modulo.getInstrutorList() == null) {
			errors.rejectValue("instrutorList", "modulo.validator.instrutorlist.vazio");
		} else {
//			if (modulo.getInstrutorList().size() > 3) {
//				errors.rejectValue("instrutorList", "modulo.validator.instrutorlist.commaisde3");
//			}
			
			loop:
			if(modulo.getEventoId() != null) {
				Evento evento = modulo.getEventoId();
				for(Instrutor instrutor : modulo.getInstrutorList()) {
					String cpf = instrutor.getCpf() != null ? instrutor.getCpf().replace(".", "").replace("-", "") : "";
					Participante participante = participanteDao.findByCpf(cpf);
					
					if (participante != null && inscricaoDao.findByEventoAndParticipante(evento.getId(), participante.getId()) != null) {
						errors.rejectValue("instrutorList", "inscricao.validator.participante.instrutor");
						break loop;
					}
				}				
			}
		}
	}
	
	/**
	 * Regras de validação para localização: <p/>
	 * - Se a modalidade é EAD, a localização deve ser Internet
	 * - Se a modalidade é Presencial, a localização deve ser diferente de Internet
	 * 
	 * @param modulo
	 * @param errors
	 */
	private void garantirIntegridadeLocalizacao(Modulo modulo, Errors errors) {
		
		if(modulo.getModalidade() != null && modulo.getLocalizacaoId() != null) {
			if(modulo.getModalidade().isEAD() && !modulo.getLocalizacaoId().isInternet()) {
				errors.rejectValue("localizacaoId", "modulo.validator.localizacao.localizacaointernetparamoduloead");
			} else if(modulo.getModalidade().isPresencial() && !modulo.getLocalizacaoId().isPresencial()) {
				errors.rejectValue("localizacaoId", "modulo.validator.localizacao.localizacaodiferenteinternetparamodulopresencial");
			}
		}
	}
}
