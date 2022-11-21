package br.com.siged.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.siged.controller.validators.InscricaoValidator;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.InscricaoDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.ResponsavelSetorDAO;
import br.com.siged.dao.UsuarioDAO;
import br.com.siged.dao.UsuarioSCADAO;
import br.com.siged.dto.inscricao.InclusaoInscricaoLoteDTO;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.EventoExtra;
import br.com.siged.entidades.Inscricao;
import br.com.siged.entidades.Instrutor;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.ResponsavelSetor;
import br.com.siged.entidades.Usuario;
import br.com.siged.entidades.externas.Setor;
import br.com.siged.entidades.externas.UsuarioSCA;
import br.com.siged.filtro.Email;
import br.com.siged.service.exception.NaoPodeRealizarInscricaoException;
import br.com.siged.util.EmailUtil;
import br.com.siged.util.HibernateUtil;
import br.com.siged.util.Util;

/**
 * Concentra as regras de negócio para a entidade <tt>Inscricao</tt>. As regras complementares estão nas contraints da entidade e no validator.
 * @since 11/10/2018
 * @author Rafael de Castro
 * 
 * @see {@link InscricaoValidator}
 */
@Service
public class InscricaoService {

	public static final int LIMIT_INCLUSAO_LOTE = 20;
	
	static final Logger logger = Logger.getLogger(InscricaoService.class);

	@Autowired
	private Util util;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private InscricaoDAO inscricaoDao;
	@Autowired
	private UsuarioDAO usuarioDao;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private UsuarioSCADAO usuarioSCADao;
	@Autowired
	private ResponsavelSetorDAO responsavelSetorDao;
	
	/**
	 * Salva as inscrições cadastradas pelos Administradores do sistema verificando se trata de uma inscrição nova ou uma atualização de inscrição já existente. 
	 * 
	 * @param inscricao
	 * @throws NaoPodeRealizarInscricaoException
	 * @throws Exception lançada pelas rotinas de envio de e-mail
	 */
	public void salvarADMIN(Inscricao inscricao) throws NaoPodeRealizarInscricaoException, Exception {
		if(inscricao.isNovo()) {
			verificarPossibilidadePreInscricaoADMIN(inscricao.getEventoId(), inscricao.getParticipanteId());
			
			inscricao.setData(new Date());
			
			inscricao.setConfirmada("S");
			confirmarInscricao(inscricao);
			
			//Se Participante for Servidor
			if(inscricao.getParticipanteId().getTipoId().isServidor()){
						
				if(inscricao.getSolicitacaoId() != null){
					EventoExtra solicitacao = inscricao.getSolicitacaoId();
					
					if(solicitacao.getIndicada().equals("S"))
						inscricao.setIndicada("S");
					
					if(solicitacao.getChefeId() != null)
						inscricao.setChefeId(solicitacao.getChefeId());
						
					if(solicitacao.getDataIndicacao() != null)
						inscricao.setDataIndicacao(solicitacao.getDataIndicacao());
					
					if(solicitacao.getJustificativachefe() != null)
						inscricao.setJustificativachefe(solicitacao.getJustificativachefe());
					
					if(solicitacao.getJustificativa() != null)
						inscricao.setJustificativaParticipante(solicitacao.getJustificativa());
				}
			
			}else{			
				inscricao.setIndicada("-");
				
				//Ativa o usuário do participante na tabela do SIGED caso não seja servidor
				Usuario usuario = usuarioDao.findByLogin(inscricao.getParticipanteId().getCpf().replace(".", "").replace("-", ""));
				if (usuario != null) {
					if (!usuario.isEnabled()) {
						usuario.setEnabled(true);
						usuarioDao.merge(usuario);
						this.emailUtil.emailUsuario(usuario);
					}
				}
			}		

			inscricaoDao.persist(inscricao);
			
			this.emailUtil.emailInscricao(inscricao.getEventoId().getId(), inscricao.getParticipanteId());
		} else {
			atualizarADMIN(inscricao);
		}
		
	}
	
	/**
	 * Na atualização de inscrição pelo Administrador a restrição é somente em relação ao usuário logado ter o perfil correspondente.
	 * 
	 * @param inscricao
	 * @throws NaoPodeRealizarInscricaoException
	 * @throws IOException
	 * @throws Exception lançada pelas rotinas de envio de e-mail
	 */
	private void atualizarADMIN(Inscricao inscricao) throws NaoPodeRealizarInscricaoException, IOException, Exception {
		Usuario usuarioLogado = AuthenticationService.getUsuarioLogadoOrNull();
		if(usuarioLogado == null || !usuarioLogado.isAdministrador())
			throw new NaoPodeRealizarInscricaoException("Acesso restrito aos Administradores do sistema", true);
		
		Inscricao inscricaoMerge = inscricaoDao.find(inscricao.getId());
		final Boolean isConfirmada = !inscricaoMerge.getConfirmada().equals("S") && inscricao.getConfirmada().equals("S");
		final Boolean isNegada = !inscricaoMerge.getConfirmada().equals("N") && inscricao.getConfirmada().equals("N");
		final Boolean isSemStatus = !inscricaoMerge.getConfirmada().equals("-") && inscricao.getConfirmada().equals("-");
		
		//Informações que precisam ser atualizadas: confirmada, justificativanaoconfirmacao, justificativa, chefe
		inscricaoMerge.setConfirmada(inscricao.getConfirmada());
		inscricaoMerge.setJustificativanaoconfirmacao(inscricao.getJustificativanaoconfirmacao());
		inscricaoMerge.setJustificativa(inscricao.getJustificativa());
		if (inscricao.getChefeId() != null)
			inscricaoMerge.setChefeId(inscricao.getChefeId());
		
		//seta a devida data de confirmação
		if(isConfirmada) {
			confirmarInscricao(inscricaoMerge);
		} else if(isNegada || isSemStatus) {
			inscricaoMerge.setDataConfirmacao(null);
		}
		
		inscricaoDao.merge(inscricaoMerge);

		if (isConfirmada)
			this.emailUtil.emailInscricao(inscricaoMerge.getEventoId().getId(), inscricaoMerge.getParticipanteId());
		
		if (isNegada)
			this.emailUtil.emailInscricaoNegada(inscricaoMerge.getEventoId().getId(), inscricaoMerge.getParticipanteId(), inscricaoMerge.getJustificativanaoconfirmacao());
		
		//Ativa o usuário do participante na tabela do SIGED caso não seja servidor
		if(!inscricaoMerge.getParticipanteId().getTipoId().isServidor()){
			Usuario usuario = usuarioDao.findByLogin(inscricaoMerge.getParticipanteId().getCpf().replace(".", "").replace("-", ""));
			if (usuario != null) {
				if (!usuario.isEnabled()) {
					usuario.setEnabled(true);
					usuarioDao.merge(usuario);
					this.emailUtil.emailUsuario(usuario);
				}
			}
		}
	}

	/**
	 * Verificação para o caso de pré-inscrições feitas por usuários com perfil de Aluno ou deslogados
	 * @param evento Precisa somente do evento, pois o participante é carregado da sessão do usuário
	 */
	public void verificarPossibilidadeDePreInscricaoAluno(Evento evento) throws NaoPodeRealizarInscricaoException  {
		if(evento == null)
			throw new NaoPodeRealizarInscricaoException("Evento não encontrado");
		
		if(evento.getPermitePreInscricao().equals("N"))
			throw new NaoPodeRealizarInscricaoException("Evento não permite pré-inscrição");
		
		if(util.getTomorrowDate(evento.getDataFimPreInscricao()).getTime() <= System.currentTimeMillis())
			throw new NaoPodeRealizarInscricaoException("Evento fora do prazo de pré-inscrição");
		
		if(evento.getInscricaoList().size() >= evento.getTotalVagasMargem())
			throw new NaoPodeRealizarInscricaoException("As vagas do Evento já foram preenchidas");
		
		Usuario usuarioLogado = AuthenticationService.getUsuarioLogadoOrNull();
		Participante participante = null;
		if(usuarioLogado != null && usuarioLogado.isAluno()) {
			participante = participanteDao.findByCpf(usuarioLogado.getCpf());
			if(!evento.inPublicoAlvo(participante))
				throw new NaoPodeRealizarInscricaoException("Tipo do participante não compatível com o público-alvo do evento.");
			
			Inscricao inscricaoExistente = inscricaoDao.findByEventoAndParticipante(evento.getId(), participante.getId());

			if (inscricaoExistente != null)
				throw new NaoPodeRealizarInscricaoException("Já existe pré-inscrição para este participante neste evento.");
			
			verificarInstrutor(evento, participante);
		}
		
		if(evento.getPublicado().equals("N") || (evento.getIsRestrito() && !evento.inInteressados(participante)))
			throw new NaoPodeRealizarInscricaoException("Não é possível fazer inscrição no Evento", true);
	}
	
	/**
	 * Verificação para o caso de inscrições feitas por usuários com perfil de Administrador
	 * @param evento
	 * @param participante Precisa informar participante pois está considerando a sessão do administrador
	 */
	public void verificarPossibilidadePreInscricaoADMIN(Evento evento, Participante participante) {
		if(evento == null)
			throw new NaoPodeRealizarInscricaoException("Evento não encontrado");
		
		if(participante == null)
			throw new NaoPodeRealizarInscricaoException("Participante não encontrado");
		
		Usuario usuarioLogado = AuthenticationService.getUsuarioLogadoOrNull();
		if(usuarioLogado == null || !usuarioLogado.isAdministrador())
			throw new NaoPodeRealizarInscricaoException("Acesso restrito aos Administradores do sistema", true);
		
		if(!evento.inPublicoAlvo(participante))
			throw new NaoPodeRealizarInscricaoException("Tipo do participante não compatível com o público-alvo do evento.");
		
		Inscricao inscricaoExistente = inscricaoDao.findByEventoAndParticipante(evento.getId(), participante.getId());

		if (inscricaoExistente != null)
			throw new NaoPodeRealizarInscricaoException("Já existe pré-inscrição para este participante neste evento.");
	}
	
	/**
	 * Rotina para setar a data de confirmação da inscrição. 
	 * A data de confirmação não existia nos primeiros registros de inscricão, então é preciso verificar a data de controle para evitar inconsistências para inscrições mais antigas.
	 * @param inscricao
	 */
	public void confirmarInscricao(Inscricao inscricao) {
    	if(inscricao.getDataConfirmacao() == null 
    			&& (inscricao.getData() != null && inscricao.getData().after(new Date(Inscricao.DATA_CONTROLE_CONFIRMACAO)))){
    		inscricao.setDataConfirmacao(new Date());
    	}
	}
	
	public InclusaoInscricaoLoteDTO inclusaoLote(Evento evento, String ... participantesId) throws IOException, Exception {
		InclusaoInscricaoLoteDTO inclusaoLote = new InclusaoInscricaoLoteDTO();
		
		for (String id : participantesId) {
			
			Participante participante = participanteDao.find(new Long(id));

			try {
				verificarPossibilidadePreInscricaoADMIN(evento, participante);
				verificarInstrutor(evento, participante);
				
				if (inclusaoLote.incluidas() <= LIMIT_INCLUSAO_LOTE){
					Inscricao novaInscricao = new Inscricao();
					novaInscricao.setEventoId(evento);
					novaInscricao.setParticipanteId(participante);					
					novaInscricao.setData(new Date());
					
					novaInscricao.setConfirmada("S");
					this.confirmarInscricao(novaInscricao);
					
					/*
					 * Se não for Servidor
					 */
					if(!participante.getTipoId().isServidor()){
						novaInscricao.setIndicada("-");
						Usuario usuarioSIGED = usuarioDao.findByLogin(participante.getCpf().replace(".", "").replace("-", ""));
						
						if (usuarioSIGED != null) {
							if (!usuarioSIGED.isEnabled()) {
								usuarioSIGED.setEnabled(true);
								usuarioDao.merge(usuarioSIGED);
								this.emailUtil.emailUsuario(usuarioSIGED);
							}
						}
					}		
		
					inscricaoDao.persist(novaInscricao);
					this.emailUtil.emailInscricao(evento.getId(), participante);
					
					inclusaoLote.addIncluida(participante);
				
				} else {
					inclusaoLote.addNaoIncluida(participante, "Limite de " + LIMIT_INCLUSAO_LOTE + "incrições por vez atingido");
				}
			} catch (NaoPodeRealizarInscricaoException e) {
				inclusaoLote.addNaoIncluida(participante, e.getMessage());
			}
			
		}
		
		return inclusaoLote;
	}
	
	public void confirmacaoLote(Evento evento, String ... inscricoesId) {
		List<Inscricao> inscricoes = new ArrayList<Inscricao>(inscricoesId.length);
		
		for (String id : inscricoesId) {
			inscricoes.add(inscricaoDao.find(new Long(id)));
		}
		
		String emails = "";
		
		for (Inscricao inscricao : inscricoes) {
			if(inscricao.getConfirmada().equals("-")){
				inscricao.setConfirmada("S");
				confirmarInscricao(inscricao);
				inscricaoDao.merge(inscricao);
				if(inscricao.getParticipanteId().getEmail() != null){
					emails += inscricao.getParticipanteId().getEmail() + ",";
				}
				
				if(inscricao.getParticipanteId().getTipoId().getId() != 1){
					//Ativando usuários externos
					Usuario usuario = usuarioDao.findByLogin(inscricao.getParticipanteId().getCpf().replace(".", "").replace("-", ""));
					if (usuario != null) {
						if (!usuario.isEnabled()) {
							usuario.setEnabled(true);
							usuarioDao.merge(usuario);
							try {
								this.emailUtil.emailUsuario(usuario);
							} catch (Exception e) {
								logger.error("Não foi possível enviar o e-mail de ativação para o usuário externo " + usuario.getId());
							}
						}
					}
				}
			}
		}
		
		Email email = new Email();
		email.setEventoId(evento.getId());
		email.setTo(emails);
		
		try {
			if(!emails.equals("")) {
				this.emailUtil.emailInscricaoConfirmacaoLote(email);
			}
				
		} catch (Exception e) {			
			logger.error("Não foi possível enviar o e-mail de confirmação em lote do evento " + evento.getId());
		}
	}
	
	public void verificarInstrutor(Evento evento, Participante participante) throws NaoPodeRealizarInscricaoException {
		if(evento == null) {
			throw new NaoPodeRealizarInscricaoException("Evento não informado.");
		}
		
		if(participante == null) {
			throw new NaoPodeRealizarInscricaoException("Participante não informado.");
		}
		
		for(Instrutor instrutor : evento.getInstrutores()) {
			String cpfInstrutor = instrutor.getCpf() != null ? instrutor.getCpf().replace(".", "").replace("-", "") : "";
			String cpfParticipante = participante.getCpf().replace(".", "").replace("-", "");
			if(cpfInstrutor.equals(cpfParticipante)) {
				throw new NaoPodeRealizarInscricaoException("Participante é instrutor do evento!");
			}
		}
	}
	
	public Inscricao inscricaoLogado(Long idEvento, String justificativaParticipante) throws NaoPodeRealizarInscricaoException {

		Evento evento = eventoDao.find(idEvento);
		verificarPossibilidadeDePreInscricaoAluno(evento);
		Usuario usuarioLogado = AuthenticationService.getUsuarioLogadoOrNull();
		
		Participante participante = participanteDao.findByCpf(usuarioLogado.getCpf());

		UsuarioSCA chefe = null;
		
		Setor setor = HibernateUtil.unproxy(participante.getSetorId());
		if (participante.getTipoId().isServidor() && setor != null) {
			ResponsavelSetor responsavelSetor = responsavelSetorDao.findAtivoBySetorId(setor.getId());
			if (responsavelSetor != null) {
				UsuarioSCA usuario = usuarioSCADao.findByCpf(participante.getCpf());
				if (usuario.equals(responsavelSetor.getResponsavel())) {
					chefe = responsavelSetor.getResponsavelImediato();
				} else {
					chefe = responsavelSetor.getResponsavel();
				}
			} else {
				throw new NaoPodeRealizarInscricaoException("Seu setor não possui responsável cadastrado no SIGED. Favor entrar em contato com o IPC.");
			}
		}
		
		String indicada;			
		if(participante.getTipoId().isServidor()){
			indicada = "N";
		}else{
			indicada = "-";
		}

		Inscricao novaInscricao = new Inscricao(null, "-", null, new Date(), indicada, null, evento, participante, chefe, null);
		if (justificativaParticipante != null && !justificativaParticipante.isEmpty()) {
			novaInscricao.setJustificativaParticipante(justificativaParticipante);		
		}
		
		inscricaoDao.persist(novaInscricao);
		
		try {
			emailUtil.emailPreInscricao(idEvento, participante);
		} catch (Exception e) {
			logger.warn("Não foi possível enviar e-mail de pré-inscrição para o participante " + novaInscricao.getParticipanteId().getNome(), e);
		}
		
		return novaInscricao;
	}
}
