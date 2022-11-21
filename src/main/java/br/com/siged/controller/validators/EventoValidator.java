package br.com.siged.controller.validators;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import br.com.siged.dao.EventoDAO;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Inscricao;
import br.com.siged.entidades.Modulo;
import br.com.siged.service.EventoService;
import br.com.siged.util.Util;

@Component(value = "eventoValidator")
public class EventoValidator extends AbstractValidator {
	
	@Autowired
	private EventoDAO eventoDao;
	
	@Autowired
	private EventoService eventoService;
	
	@Autowired
	private Util util;

	@Override
	protected boolean suporta(Class<?> clazz) {
		return Evento.class.equals(clazz);
	}

	@Override
	protected void adicionarValidacoesExtras(Object target, Errors errors) {
		Evento evento = (Evento) target;
		
		if (evento.getPermitePreInscricao().equalsIgnoreCase("S")) {
			if (evento.getDataInicioPreInscricao() == null) {
				errors.rejectValue("dataInicioPreInscricao", "evento.validator.data.preinscricao.obrigatoriasepermitepreinscricao");
			}
			if (evento.getDataFimPreInscricao() == null) {
				errors.rejectValue("dataFimPreInscricao", "evento.validator.data.preinscricao.obrigatoriasepermitepreinscricao");
			}
		}
		if (evento.getDataInicioPreInscricao() != null && evento.getDataFimPreInscricao() != null) {
			if (evento.getDataInicioPreInscricao().after(evento.getDataFimPreInscricao())) {
				errors.rejectValue("dataInicioPreInscricao", "evento.validator.data.iniciodepoisdofim");
			}
		}
		if (evento.getDataInicioPrevisto() != null && evento.getDataFimPrevisto() != null) {
			if (evento.getDataInicioPrevisto().after(evento.getDataFimPrevisto())) {
				errors.rejectValue("dataInicioPrevisto", "evento.validator.data.iniciodepoisdofim");
			}
		}
		
		if (evento.getDataInicioRealizacao() != null && evento.getDataFimRealizacao() == null) {
			errors.rejectValue("dataFimRealizacao", "evento.validator.data.iniciosemfim");
		}
		if (evento.getDataFimRealizacao() != null && evento.getDataInicioRealizacao() == null) {
			errors.rejectValue("dataInicioRealizacao", "evento.validator.data.fimseminicio");
		}
		
		if (evento.getDataInicioRealizacao() != null && evento.getDataFimRealizacao() != null) {
			if (evento.getDataInicioRealizacao().after(evento.getDataFimRealizacao()) && !evento.getDataInicioRealizacao().equals(evento.getDataFimRealizacao())) {
				errors.rejectValue("dataInicioRealizacao", "evento.validator.data.iniciodepoisdofim");
			}
		}
		
		if(evento.getId() != null) {
			Evento eventoantigo = eventoDao.find(evento.getId());
			
			if (eventoantigo.getInscricaoList() != null){
				for (Inscricao inscricao : eventoantigo.getInscricaoList()) {
					if(!evento.getPublicoAlvoLista().contains(inscricao.getParticipanteId().getTipoId().getDescricao()) && inscricao.getConfirmada().equals("S")){
						errors.rejectValue("publicoAlvoList", "evento.validator.publico.inscricoesexistentes");
						break;
					}
				}
			}
			
			naoPodeTerInicioDepoisDoPrimeiroModulo(evento, eventoantigo, errors);
			naoPodeTerFimAntesDoUltimoModulo(evento, eventoantigo, errors);
			
			if(eventoantigo.getModuloList() != null) {
				int cargaHorariaDoEvento = Integer.valueOf(evento.getCargaHoraria());
				Integer cargaHorariaDosModulos = 0;
				for (Modulo modulo : eventoantigo.getModuloList()) {
					int cargaHorariaDoModulo = 0;
					if(modulo.getCargaHoraria() != null && !modulo.getCargaHoraria().isEmpty()) {
						cargaHorariaDoModulo = Integer.valueOf(modulo.getCargaHoraria());
					}
					cargaHorariaDosModulos += cargaHorariaDoModulo;
				}
				
				if(cargaHorariaDoEvento < cargaHorariaDosModulos) {
					errors.rejectValue("cargaHoraria", "evento.validator.cargahoraria.menorquedosmodulos", new Object[] {cargaHorariaDosModulos}, "evento.validator.cargahoraria.menorquedosmodulos");
				}
			}
			
		}
		/*
		 * Campo modalidade no evento foi depreciado
		if(evento.getModalidadeId() != null && evento.getModalidadeId().isEAD()) {
			if(evento.getLocalizacaoId() != null && !evento.getLocalizacaoId().isInternet()) {
				errors.rejectValue("localizacaoId", "evento.validator.localizacao.localizacaodeveserinternet");
			}
		}
		*/
	}

	private void naoPodeTerInicioDepoisDoPrimeiroModulo(Evento evento, Evento eventoAntigo, Errors errors) {
		Modulo primeiroModulo = eventoService.primeiroModuloDoEvento(eventoAntigo);
		
		if(primeiroModulo != null) {
			Date inicioModulo = util.getDataFormatada(primeiroModulo.getDataInicio());
			// String primeiroModuloDetalhe = "(" + primeiroModulo.getTitulo() + " - " + util.formataData(primeiroModulo.getDataInicio()) + " à " + util.formataData(primeiroModulo.getDataFim()) + ")";
			
			Date inicioEvento = util.getDataFormatada(evento.getDataInicioPrevisto());
			String field = "dataInicioPrevisto";
			if(evento.getDataInicioRealizacao() != null) {
				field = "dataInicioRealizacao";
				inicioEvento = util.getDataFormatada(evento.getDataInicioRealizacao());
			}
			
			if(inicioEvento != null && inicioEvento.after(inicioModulo)) {
				errors.rejectValue(field, "evento.validator.data.iniciodepoisdoprimeiromodulo");
			}
			
		}
	}
	
	private void naoPodeTerFimAntesDoUltimoModulo(Evento evento, Evento eventoAntigo, Errors errors) {
		Modulo ultimoModulo = eventoService.ultimoModuloDoEvento(eventoAntigo);
		
		if(ultimoModulo != null) {
			Date fimDoModulo = util.getDataFormatada(ultimoModulo.getDataFim());
			// String ultimoModuloDetalhe = "(" + ultimoModulo.getTitulo() + " - " + util.formataData(ultimoModulo.getDataInicio()) + " à " + util.formataData(ultimoModulo.getDataFim()) + ")";
			
			Date fimEvento = util.getDataFormatada(evento.getDataFimPrevisto());
			String field = "dataFimPrevisto";
			if(evento.getDataFimRealizacao() != null) {
				field = "dataFimRealizacao";
				fimEvento = util.getDataFormatada(evento.getDataFimRealizacao());
			}
			
			if(fimEvento != null && fimEvento.before(fimDoModulo)) {
				errors.rejectValue(field, "evento.validator.data.fimantesdoultimomodulo");
			}
			
		}
	} 

}
