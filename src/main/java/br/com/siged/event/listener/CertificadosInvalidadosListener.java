package br.com.siged.event.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import br.com.siged.dao.CertificadoEmitidoDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.entidades.CertificadoEmitido;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Participante;
import br.com.siged.event.CertificadosInvalidadosEvent;
import br.com.siged.event.MotivoCertificadosInvalidados;
import br.com.siged.filtro.Email;
import br.com.siged.service.exception.NaoPodeEnviarEmailException;
import br.com.siged.util.EmailUtil;

/**
 * 
 * @author rafael.castro
 * Listener para verificar quando ocorre evento que inválida todos os certificados de um Evento ou certificados de um Participante no Evento.
 */
@Component
public class CertificadosInvalidadosListener implements ApplicationListener<CertificadosInvalidadosEvent>{
	
	@Autowired
	private CertificadoEmitidoDAO certificadoEmitidoDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private EmailUtil emailUtil;

	@Override
	public void onApplicationEvent(CertificadosInvalidadosEvent event) {
		Evento evento = event.getEvento();
		Participante participante = event.getParticipante();
		if(event.getMotivo().equals(MotivoCertificadosInvalidados.PARTICIPANTE_REPROVADO)) {
			if(participante != null && evento != null)
				invalidarParaParticipanteNoEvento(participante, evento);
		} else if (event.getMotivo().equals(MotivoCertificadosInvalidados.ALTERACAO_LOCALIZACAO)
				|| event.getMotivo().equals(MotivoCertificadosInvalidados.ALTERACAO_INSTRUTORES)) {
			if(evento != null) {
				invalidar(evento);
			}
		}
	}

	/**
	 * Inválida certificados do Participante no Evento. Pode ocorrer, por exemplo, quando o Participante emitiu certificado e ficou reprovado depois de uma nova apuração.
	 * @param participante
	 * @param evento
	 * @throws NaoPodeEnviarEmailException
	 */
	private void invalidarParaParticipanteNoEvento(Participante participante, Evento evento) throws NaoPodeEnviarEmailException {
		List<CertificadoEmitido> certificados = certificadoEmitidoDao.findByEventoIdAndParticipanteId(evento.getId(), participante.getId());
		
		if(certificados != null && !certificados.isEmpty()) {
			for(CertificadoEmitido certificado : certificados) {
				certificadoEmitidoDao.remove(certificado);
			}
			try {
				Email email = new Email();
				email.setTipoDoEvento(evento.getTipoId().getDescricao());
				email.setTitulo(evento.getTitulo());
				email.setNomeParticipante(participante.getNome());
				email.setTo(participante.getEmail());
				emailUtil.emailCertificadosInvalidadosPorReprovacao(email);
			} catch (Exception e) {
				throw new NaoPodeEnviarEmailException(participante.getEmail());
			}
		}
	}
	
	/**
	 * Inválida todos os certificados emitidos no Evento. Esse caso deve ser chamado quando há alteração de instrutores no módulo
	 * @param evento
	 * @throws NaoPodeEnviarEmailException
	 */
	private void invalidar(Evento evento) throws NaoPodeEnviarEmailException {
		List<Participante> participantesComCertificadosDeletados = participanteDao.findParticipantesQueEmitiramCertificadosNoEvento(evento.getId());
		
		if (participantesComCertificadosDeletados != null && participantesComCertificadosDeletados.size() > 0){
			certificadoEmitidoDao.deleteByEventoId(evento.getId());
			try {
				Email email = new Email();
				email.setTipoDoEvento(evento.getTipoId().getDescricao());
				email.setTitulo(evento.getTitulo());			
				
				String to = "";
				
				for (Participante participante : participantesComCertificadosDeletados) {
					if(participante.getEmail() != null)
						to += participante.getEmail() + ",";
				}			
				
				email.setTo(to);
				
				emailUtil.emailCertificadosInvalidadosDoEvento(email);
			} catch (Exception e) {				
				throw new NaoPodeEnviarEmailException("Ocorreu um erro ao enviar o email aos participantes sobre os certificados invalidados.");
			}
			
		}
	}
}
