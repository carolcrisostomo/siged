package br.com.siged.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.dao.AvaliacaoReacaoDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.InscricaoDAO;
import br.com.siged.dto.AvaliacaoReacaoDTO;
import br.com.siged.entidades.AvaliacaoReacao;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Inscricao;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.Usuario;
import br.com.siged.service.exception.AcessoNegadoException;
import br.com.siged.service.exception.BusinessException;
import br.com.siged.service.exception.RequisicaoMalFormuladaException;


/**
 * @author estag_12977 (Rafael de Castro)
 * Centraliza as regras de negocio da AvaliacaoReacao quando está relacionando com outras entidades
 */
@Service
public class AvaliacaoReacaoService {

	@Autowired
	private InscricaoDAO inscricaoDao;
	
	@Autowired
	private AvaliacaoReacaoDAO avaliacaoreacaoDao;
	
	@Autowired
	private EventoDAO eventoDao;
	
	@Autowired
	private ModuloService moduloService;
	
	@Autowired
	private EventoService eventoService;
	
	
	/**
	 * 
	 * Regras de negócio para criação da Avaliação de Reação
	 * 
	 * @param modulocons
	 * @param eventocons
	 * @param participantecons
	 * 
	 * @throws BusinessException 
	 * @throws ForaPeriodoAvaliacaoException 
	 * @throws ParticipanteNaoInscritoException 
	 */
	public void verificarParametros(Modulo modulocons, Evento eventocons, Participante participantecons) throws BusinessException {
		
		Usuario usuarioLogado = AuthenticationService.getUsuarioLogado();
		
		if(usuarioLogado == null || modulocons == null || eventocons == null)
			throw new RequisicaoMalFormuladaException();
		
		if(!modulocons.getEventoId().equals(eventocons))
			throw new BusinessException("O Módulo informado não faz parte do Evento");
		
		/**
		 * Usuários Administradores podem responder avaliações 
		 * por TEMPO INDETERMINADO (após 01 dia do término do Evento ou Módulo)
		 * e SEM IDENTIFICAR o Participante
		 */
		if(usuarioLogado.isAdministrador()){
			if(!eventoService.isPeriodoAvaliacaoReacaoADMIN(eventocons) && !moduloService.isPeriodoAvaliacaoReacaoADMIN(modulocons))
				throw new BusinessException("Fora do período de avaliação");
				
			if(participantecons != null && !eventocons.isParticipanteInscricaoConfirmada(participantecons))
				throw new BusinessException("O participante informado não está inscrito no evento");
		} else {
			if(!eventoService.isPeriodoAvaliacaoReacao(eventocons) && !moduloService.isPeriodoAvaliacaoReacao(modulocons)) {
				throw new BusinessException("Fora do período de avaliação");
			}
			
			if(participantecons == null)
				throw new RequisicaoMalFormuladaException();
			
			if(!participantecons.getCpf().equals(usuarioLogado.getCpf()))
				throw new AcessoNegadoException();
			
			if(!eventocons.isParticipanteInscricaoConfirmada(participantecons))
				throw new BusinessException("O participante informado não está inscrito no evento");
		}
	}
	
	/**
	 * Verifica se todas as avaliações possíveis para o Módulo já foram respondidas
	 * 
	 * @param avaliacaoReacao
	 * @return
	 */
	public boolean isNumeroAvaliacoesOk(AvaliacaoReacao avaliacaoReacao) {
		Modulo modulo = avaliacaoReacao.getModulo();
		Evento evento = modulo.getEventoId();
		return isNumeroAvaliacoesOk(modulo.getId(), evento.getId());
	}
	
	/**
	 * Verifica se todas as avaliações possíveis para o Módulo já foram respondidas
	 * 
	 * @param avaliacaoReacao
	 * @return
	 */
	public boolean isNumeroAvaliacoesOk(Long moduloId, Long eventoId) {
		List<Inscricao> inscricoes = inscricaoDao.findByEventoAndConfirmada(eventoId, "S");		
		Collection<AvaliacaoReacao> avalicacoesReacoesByModulos = avaliacaoreacaoDao.findAvaliacaoReacaoByCriteria(eventoId, moduloId, new Long(0), null, null);
		if (avalicacoesReacoesByModulos.size() + 1 <= inscricoes.size())
			return true;
		else
			return false;
	}
	
	/**
	 * Verifica se o Participante tem avaliacao pendente
	 * 
	 * @param eventoId
	 * @param participanteId
	 * @return
	 */
	public AvaliacaoReacaoDTO avaliacoesPendentes(Long participanteId, Long eventoId) {
		
		Evento evento = eventoDao.find(eventoId);
		
		AvaliacaoReacaoDTO avaliacaoDTO = new AvaliacaoReacaoDTO();
		
		if(evento == null) {
			return avaliacaoDTO;
		}
		
		List<Modulo> modulos = evento.getModuloList();
		List<Modulo> modulosPendentesDeAvaliacao = new ArrayList<>();
		
		for (Modulo modulo : modulos) {
			List<AvaliacaoReacao> avaliacoesRealizadas = avaliacaoreacaoDao.findAvaliacaoReacaoByCriteria(eventoId, modulo.getId(), participanteId, null, null);
			boolean isNumeroAvaliacoesOk = isNumeroAvaliacoesOk(modulo.getId(), eventoId);
			if(isNumeroAvaliacoesOk && avaliacoesRealizadas.isEmpty() && (eventoService.isPeriodoAvaliacaoReacao(evento) || moduloService.isPeriodoAvaliacaoReacao(modulo))) {
				modulosPendentesDeAvaliacao.add(modulo);
				avaliacaoDTO.incrementarPendentes();
			}
		}
		
		if(!modulosPendentesDeAvaliacao.isEmpty()) {
			avaliacaoDTO.setEventoId(eventoId);
			avaliacaoDTO.setModuloId(modulosPendentesDeAvaliacao.get(0).getId());
			avaliacaoDTO.setParticipanteId(participanteId);
		}
		
		return avaliacaoDTO;
	}
	
	@Transactional
	public void salvar(AvaliacaoReacao avaliacaoReacao) throws BusinessException {
	
		List<AvaliacaoReacao> list = this.avaliacaoreacaoDao.findByModuloAndParticipante(avaliacaoReacao.getModulo(), avaliacaoReacao.getParticipante());
		if(list != null && list.size() > 0) {
			throw new BusinessException("O participante já avaliou o módulo informado");			
		}
		
		avaliacaoreacaoDao.persist(avaliacaoReacao);
	}

}
