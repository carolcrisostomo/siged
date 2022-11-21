package br.com.siged.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.siged.controller.validators.EventoValidator;
import br.com.siged.dao.CertificadoEmitidoDAO;
import br.com.siged.dao.DesempenhoDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.EventoHistoricoDAO;
import br.com.siged.dao.ModuloDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.entidades.CertificadoEmitido;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.EventoHistorico;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.StatusDesempenho;
import br.com.siged.filtro.Email;
import br.com.siged.service.exception.NaoPodeEnviarEmailException;
import br.com.siged.util.EmailUtil;
import br.com.siged.util.Util;

/**
 * Concentra as regras de negócio para a entidade <tt>Evento</tt>. As regras complementares estão nas contraints da entidade e no validator.
 * @author Rafael de Castro
 * 
 * @see {@link EventoValidator}
 */
@Service
public class EventoService {
	
	@Autowired
	private Util util;
	@Autowired
	private ModuloService moduloService;
	@Autowired
	private DesempenhoService desempenhoService;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private CertificadoEmitidoDAO certificadoEmitidoDao;
	@Autowired
	private EventoHistoricoDAO eventoHistoricoDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private ModuloDAO moduloDao;
	@Autowired
	private DesempenhoDAO desempenhoDao;
	@Autowired
	private EmailUtil emailUtil;
	
	public void salvar(Evento evento) {
		if (evento.getPermitePreInscricao().equalsIgnoreCase("N")) {
			evento.setDataInicioPreInscricao(null);
			evento.setDataFimPreInscricao(null);
		}

		if(evento.getId() != null) {
			this.atualizar(evento);
		} else {
			evento.setDataCadastro(new Date());
			eventoDao.persist(evento);
		}
		
	}
	
	private void atualizar(Evento evento) throws NaoPodeEnviarEmailException {
		Evento eventoantigo = eventoDao.find(evento.getId());
		
		List<CertificadoEmitido> certificadosEmitidos = certificadoEmitidoDao.findByEventoId(evento.getId());	
		
		List<Participante> participantesComCertificadosDeletados = null;
		
		/**
		 * @since 23/05/2019: Conforme Ticket#-2019043010000276 a revogação de certificados quando houver mudança de localização será realizada no método atualizar
		 * da classe ModuloService, verificando se de fato o mudou a localização do módulo
		 */
		if ( certificadosEmitidos != null && certificadosEmitidos.size() > 0
				&& ( !evento.getTipoId().getId().equals(eventoantigo.getTipoId().getId())
						|| !evento.getTitulo().equals(eventoantigo.getTitulo())
						/*
						 * @deprecated Não fazer mais essa verificação, pois o trecho no certificado que referência o local de realização será estático.
						|| !evento.getLocalizacaoId().getId().equals(eventoantigo.getLocalizacaoId().getId())
						*/
						|| !evento.getCargaHoraria().equals(eventoantigo.getCargaHoraria())
						|| !evento.getDataInicioRealizacao().equals(eventoantigo.getDataInicioRealizacao())
						|| !evento.getDataFimRealizacao().equals(eventoantigo.getDataFimRealizacao()))){		
		
			participantesComCertificadosDeletados = participanteDao.findParticipantesQueEmitiramCertificadosNoEvento(evento.getId());
			certificadoEmitidoDao.deleteByEventoId(evento.getId());
			// TODO publicar event para certificados invalidados do Evento.
		}		
		
		if (evento.getDataInicioPrevisto() != null && evento.getDataFimPrevisto() != null) {
			if (evento.getDataInicioPrevisto().compareTo(eventoantigo.getDataInicioPrevisto()) != 0 || evento.getDataFimPrevisto().compareTo(eventoantigo.getDataFimPrevisto()) != 0 ) {
				EventoHistorico eventohistorico = new EventoHistorico(evento, eventoantigo.getDataInicioPrevisto(), eventoantigo.getDataFimPrevisto());
				eventoHistoricoDao.persist(eventohistorico);
			}
		}

		if (evento.getModuloUnico().compareToIgnoreCase("S") == 0) {			
			
			List<Modulo> modulos = moduloDao.findByEventoId(evento.getId());
			
			if(modulos.size() == 1){
				Modulo modulo = moduloDao.findByEvento(evento);
				if(modulo != null){
					modulo.setDataInicio(evento.getDataInicioPrevisto());
					modulo.setDataFim(evento.getDataFimPrevisto());
					modulo.setCargaHoraria(evento.getCargaHoraria());
		
					if (evento.getDataInicioRealizacao() != null) {
						modulo.setDataInicio(evento.getDataInicioRealizacao());
						modulo.setDataFim(evento.getDataFimRealizacao());
					}
					moduloDao.merge(modulo);
				}
			}
		}
		
		/*
		 * Campo modalidade no evento foi depreciado
		if(evento.getModalidadeId() != null && eventoantigo.getModalidadeId() != null) {
			
			//Se mudar a modalidade do Evento então a localização dos módulos deve ser a mesma do Evento
			if(!eventoantigo.getModalidadeId().getId().equals(evento.getModalidadeId().getId())) {
				boolean isEAD = evento.getModalidadeId().isEAD(); 
				for(Modulo modulo : moduloDao.findByEventoId(evento.getId())) {
					modulo.setLocalizacaoId(evento.getLocalizacaoId());
					if(isEAD) {
						modulo.setHoraInicioTurno1("");
						modulo.setHoraFimTurno1("");
						modulo.setHoraInicioTurno2("");
						modulo.setHoraFimTurno2("");
						modulo.setHoraInicioTurno3("");
						modulo.setHoraFimTurno3("");
					}
					moduloDao.merge(modulo);
				}
			}
		}
		 */
		
		Email email = new Email();
		email.setTipoDoEvento(eventoantigo.getTipoId().getDescricao());
		email.setTitulo(eventoantigo.getTitulo());

		eventoDao.merge(evento);
		
		if (participantesComCertificadosDeletados != null && participantesComCertificadosDeletados.size() > 0){
			try {
				String to = "";
				
				for (Participante participante : participantesComCertificadosDeletados) {
					if(participante.getEmail() != null)
						to += participante.getEmail() + ",";
				}			
				
				email.setTo(to);
				emailUtil.emailCertificadosInvalidadosDoEvento(email);
			} catch (Exception e) {				
				e.printStackTrace();
				throw new NaoPodeEnviarEmailException("Ocorreu um erro ao enviar o email aos participantes sobre os certificados invalidados.");
			}
		}
		
	}
	
	public boolean isPeriodoAvaliacaoReacao(Evento evento) {
		if(evento.getDataFimRealizacao() == null) {
			return false;
		}
		Calendar dataAtual = Calendar.getInstance();
		
		Calendar dataStart = Calendar.getInstance();
		dataStart.setTime(evento.getDataFimRealizacao());
		dataStart.add(Calendar.DATE, +1);
		
		Calendar dataEnd = Calendar.getInstance();
		dataEnd.setTime(evento.getDataFimRealizacao());
		dataEnd.add(Calendar.DATE, +31);
		
		return dataAtual.compareTo(dataStart) > 0 && dataAtual.compareTo(dataEnd) <= 0;
	}
	
	public boolean isPeriodoAvaliacaoReacaoADMIN(Evento evento){
		if(evento.getDataFimRealizacao() == null) {
			return false;
		}
		Calendar dataAtual = Calendar.getInstance();
		
		Calendar dataStart = Calendar.getInstance();
		dataStart.setTime(evento.getDataFimRealizacao());
		dataStart.add(Calendar.DATE, +1);
		
		return dataAtual.compareTo(dataStart) > 0;
	}
	
	public Boolean isRealizado(Evento evento) {
		Date hoje = util.getDataFormatada(new Date());
		Date dataFim = util.getDataFormatada(evento.getDataFimRealizacao());
		return dataFim.compareTo(hoje) < 0;
	}
	
	public Boolean isEmAndamento(Evento evento) {
		Date hoje = util.getDataFormatada(new Date());
		Date dataInicio = util.getDataFormatada(evento.getDataInicioRealizacao());
		Date dataFim = util.getDataFormatada(evento.getDataFimRealizacao());
		return dataInicio.compareTo(hoje) <= 0 && dataFim.compareTo(hoje) >= 0;
	}
	
	public Boolean temModuloRealizado(Evento evento) {
		for(Modulo modulo : evento.getModuloList()) {
			if(moduloService.isRealizado(modulo))
				return true;
		}
		return false;
	}
	
	public void populateDesempenhoTransientInfo(List<Evento> meuseventos, Participante participante) {
		for (Evento evento : meuseventos) {

			StatusDesempenho statusDesempenho = desempenhoService.statusDesempenhoParticipanteNoEvento(participante, evento);
			evento.setAprovado(statusDesempenho.getAprovado());
			
			String notaFinal = desempenhoService.notaDoParticipanteNoEvento(participante, evento);
			String frequenciaFinal = desempenhoService.frequenciaDoParticipanteNoEvento(participante, evento);
			
			evento.setNotaParticipanteNoEvento(notaFinal);
			evento.setFrequenciaParticipanteNoEvento(frequenciaFinal);
			
		}
	}
	
	public void populateDesempenhoTransientInfo(List<Evento> eventos) {
		for (Evento evento : eventos) {
			if (desempenhoDao.findByEvento(evento).size() != 0)
				evento.setDesempenho("S");
			else
				evento.setDesempenho("N");
		}
	}
	
	public void populateInstrutorTransientInfo(List<Evento> eventos) {
		for(Evento evento : eventos) {
			if((evento.getDataInicioRealizacao() != null && evento.getDataFimRealizacao() != null) 
					&& (isRealizado(evento) || temModuloRealizado(evento)) 
					&& evento.temProvedorIPC()) {
				evento.setAprovado("S");
			} else {
				evento.setAprovado("N");
			}
		}
	}
	
	public Modulo primeiroModuloDoEvento(Evento evento) {
		Modulo primeiroModulo = null;
		if(evento != null && evento.getModuloList() != null) {
			for(Modulo modulo : evento.getModuloList()) {
				if(primeiroModulo == null || (primeiroModulo != null && modulo.getDataInicio().before(primeiroModulo.getDataInicio()))) {
					primeiroModulo = modulo;
				}
			}
		}
		
		return primeiroModulo;
	}
	
	public Modulo ultimoModuloDoEvento(Evento evento) {
		Modulo ultimoModulo = null;
		if(evento != null && evento.getModuloList() != null) {
			for(Modulo modulo : evento.getModuloList()) {
				if(ultimoModulo == null || (ultimoModulo != null && modulo.getDataFim().after(ultimoModulo.getDataFim()))) {
					ultimoModulo = modulo;
				}
			}
		}
		
		return ultimoModulo;
	}
}