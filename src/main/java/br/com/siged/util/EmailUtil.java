package br.com.siged.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import br.com.siged.controller.EventoMaterialController;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.InscricaoDAO;
import br.com.siged.dao.ModuloDAO;
import br.com.siged.dao.ResponsavelSetorDAO;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.EventoExtra;
import br.com.siged.entidades.EventoMaterial;
import br.com.siged.entidades.EventoRecomendar;
import br.com.siged.entidades.Inscricao;
import br.com.siged.entidades.Instrutor;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.ResponsavelSetor;
import br.com.siged.entidades.Usuario;
import br.com.siged.entidades.externas.UsuarioSCA;
import br.com.siged.filtro.Email;
import br.com.siged.mailing.MalaDireta;
import br.com.siged.service.RelatorioService;

public class EmailUtil {

	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ModuloDAO moduloDao;
	@Autowired
	private ResponsavelSetorDAO responsavelSetorDao;
	@Autowired
	private InscricaoDAO inscricaoDao;
	@Autowired
	private Util util;
	@Autowired
	private RelatorioService relatorioService;
	
	@Autowired
	@Qualifier("emailPreparatorAlias")
	private IEmailPreparator emailPreparator;
	
	@Autowired
	private JavaMailSender mailSender;
	public JavaMailSender getMailSender() {return mailSender;}
	public void setMailSender(JavaMailSender mailSender) {this.mailSender = mailSender;}
	
	public boolean isDEVMode() {
		return emailPreparator.isDEV();
	}
	
	public static final String TEXTO_CONTATOS = "e-mail (ipc@tce.ce.gov.br) ou telefone: cursos presenciais (85-3488-5915/1793), "
			+ "cursos a dist??ncia (85-3488-4855/5915), Programa TCEDUC (85-3488-4855/5915) e outros assuntos (85-3488-1793/1789)";
	
	public static final String TEXTO_INFORMACOES_GERAIS = "Para informa????es gerais sobre o IPC e suas atividades, "
			+ "consulte nosso s??tio institucional em <a href='http://www.ipc.tce.ce.gov.br' target='_blank'>www.ipc.tce.ce.gov.br</a> e acompanhe nossas m??dias sociais";
	
	public void emailDivulgarEvento(Email email) throws Exception {
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		final Evento evento = eventoDao.find(email.getEventoId());

		List<Modulo> modulos = moduloDao.findByEventoId(evento.getId());

		StringBuilder texto = new StringBuilder();
		
		texto.append("<font name='Arial' size='small'>");

		texto.append("Prezado(a) Senhor(a),<br /><br />"
				+ "O IPC divulga a realiza????o do evento " + evento.getTipoId()
				+ " " + evento.getTitulo() + ", conforme  detalhes abaixo:"
				+ "<br /><br />");

		if(!evento.getPublicoAlvoLista().equals("")){
			texto.append("<strong>P??blico Alvo: </strong>" + evento.getPublicoAlvoLista()
					+ "<br /><br />");			
		}
		
		texto.append("<strong>Modalidade: </strong>" + evento.getModalidadeModulosLista().toUpperCase() + "<br /><br />");
		
		if (evento.getCargaHoraria() != null) {
			texto.append("<strong>Carga Hor??ria: </strong>"	+ evento.getCargaHoraria() 
					+ "<br /><br />");
		}
		if (evento.getDataInicioPrevisto() != null && evento.getDataFimPrevisto() != null) {
			texto.append("<strong>Per??odo: </strong>"
					+ formato.format(evento.getDataInicioPrevisto()) + " a "
					+ formato.format(evento.getDataFimPrevisto())
					+ "<br /><br />");
		}
		
		if (modulos.size() > 0) {
			if (modulos.size() > 1) {
				texto.append("<strong>Hor??rio: </strong>CONFORME M??DULOS ABAIXO<br /><br />");
			} else {
				Modulo modulo = modulos.get(0);				
				if (modulo.getHoraInicioTurno1() != null && modulo.getHoraFimTurno1() != null) {
					texto.append("<strong>Hor??rio: </strong>" + modulo.getTurnoLista());
					texto.append("<br /><br />");
				}
			}
		}
		
		if (evento.getDataInicioPreInscricao() != null && evento.getDataFimPreInscricao() != null) {
			texto.append("<strong>Pr??-Inscri????o: </strong>" + formato.format(evento.getDataInicioPreInscricao())
					+ " a " + formato.format(evento.getDataFimPreInscricao())
					+ "<br /><br />");
		}
		
		texto.append("<strong>Quantidade de vagas: </strong>" + evento.getTotalVagas());
		texto.append("<br /><br />");
		
		texto.append("<strong>Localiza????o: </strong>" + evento.getLocalizacaoModulosLista());
		texto.append("<br /><br />");
		
		//Link do evento:
		if(evento.getLinkEvento() != null && !evento.getLinkEvento().isEmpty()) {
			texto.append("<strong>Link para o evento: </strong>");
			texto.append(evento.getLinkEvento());
			texto.append("<br /><br />");
		}
		
		//Link de grava????o do evento:
		if(evento.getLinkGravacao() != null && !evento.getLinkGravacao().isEmpty()) {
			texto.append("<strong>Link para a grava????o: </strong>");
			texto.append(evento.getLinkGravacao());
			texto.append("<br /><br />");
		}
		
		if (modulos.size() > 1) {
			
			for (Modulo modulo : modulos) {
				texto.append("<strong>M??dulo: </strong>" + modulo.getTitulo() + "<br /><br />");
				texto.append("<strong>Carga Hor??ria: </strong>"	+ modulo.getCargaHoraria() + "<br /><br />");
				texto.append("<strong>Per??odo: </strong>" + formato.format(modulo.getDataInicio()) + " a "
						+ formato.format(modulo.getDataFim()) + "<br /><br />");
				
				if (modulo.getHoraInicioTurno1() != null && modulo.getHoraFimTurno1() != null) {
					texto.append("<strong>Hor??rio: </strong>" + modulo.getTurnoLista());							
					texto.append("<br /><br />");
				}
				
				/**
				 * Campo modalidade no evento foi depreciado. Agora ?? na entidade Modulo
				 */
				texto.append("<strong>Modalidade: </strong>" + modulo.getModalidade().getDescricao());
				texto.append("<br /><br />");
				
				texto.append("<strong>Localiza????o: </strong>" + modulo.getLocalizacaoComCidadeEUF());
				texto.append("<br /><br />");
				if (!modulo.getEnderecoComCidadeUFEPais().isEmpty()) {
					texto.append("<strong>Endere??o: </strong>" + modulo.getEnderecoComCidadeUFEPais());
					texto.append("<br /><br />");
				}
				
				if(modulo.getTemFrequencia()){
					texto.append("<strong>Frequ??ncia M??nima: </strong>" + modulo.getFrequencia() + "%" + "<br /><br />");
				}
				if(modulo.getTemNota()){
					texto.append("<strong>Nota M??nima: </strong>" + modulo.getNota() + "<br /><br />");
				}
				List<Instrutor> instrutoresList = modulo.getInstrutorList();
				String instrutores = "";
				String perfilInstrutores = "";
				for (int i = 0; i < instrutoresList.size(); i++) {
					if ((i + 1) == instrutoresList.size())
						instrutores += instrutoresList.get(i).getNome();
					else
						instrutores += instrutoresList.get(i).getNome() + ", ";
					
					if (instrutoresList.get(i).getPerfil() != null && instrutoresList.get(i).getPerfil() != "")
						perfilInstrutores += instrutoresList.get(i).getNome()
						+ "<br /> - "
						+ instrutoresList.get(i).getPerfil()
						+ "<br /><br />";
				}
				texto.append("<strong>Instrutor(es): </strong>" + instrutores + "<br /><br />");
				if (perfilInstrutores != "") {
					texto.append("<strong>Perfil Do(s) Instrutor(es): </strong><br /><br />" + perfilInstrutores);
				}
				if(modulo.getObservacao() != null) {
					texto.append("<strong>Observa????o do m??dulo: </strong>" + modulo.getObservacao() + "<br /><br />");
				}
			}
		
		} else {
			/**
			 * @since 24/05/2019
			 * Ticket#-2019052110000675 - Manuten????o corretiva e-mail divulga????o de evento SIGED
			 * 
			 * 1: Quando o evento tiver m??dulo ??nico, excluir as linhas Modalidade e Localiza????o na parte do m??dulo pois est?? vindo repetido. 
			 *    J?? tem na parte do evento. Deixe apenas as linhas de Frequ??ncia M??nima e Instrutor(es) nessa parte do m??dulo (para m??dulo ??nico claro).
			 * 2: Excluir o texto " - O participante dever?? comunicar a impossibilidade ...e eventos;". Apenas este par??grafo nesta parte do e-mail.
			 */
			if (modulos.size() == 1) {
				Modulo modulo = modulos.get(0);
				
				if(modulo.getTemFrequencia()){
					texto.append("<strong>Frequ??ncia M??nima: </strong>" + modulo.getFrequencia() + "%" + "<br /><br />");
				}
				if(modulo.getTemNota()){
					texto.append("<strong>Nota M??nima: </strong>" + modulo.getNota() + "<br /><br />");
				}				
				List<Instrutor> instrutoresList = modulo.getInstrutorList();
				String instrutores = "";
				String perfilInstrutores = "";
				for (int i = 0; i < instrutoresList.size(); i++) {
					if ((i + 1) == instrutoresList.size())
						instrutores += instrutoresList.get(i).getNome();
					else
						instrutores += instrutoresList.get(i).getNome() + ", ";
					
					if (instrutoresList.get(i).getPerfil() != null && instrutoresList.get(i).getPerfil() != "")
						perfilInstrutores += instrutoresList.get(i).getNome()
						+ "<br /> - "
						+ instrutoresList.get(i).getPerfil()
						+ "<br /><br />";
				}
				texto.append("<strong>Instrutor(es): </strong>" + instrutores + "<br /><br />");
				if (perfilInstrutores != "") {
					texto.append("<strong>Perfil Do(s) Instrutor(es): </strong><br /><br />" + perfilInstrutores);
				}
				if(modulo.getObservacao() != null) {
					texto.append("<strong>Observa????o do m??dulo: </strong>" + modulo.getObservacao() + "<br /><br />");
				}
			}
		}
		if (evento.getEixoTematicoId() != null) {
			texto.append("<strong>Eixo Tem??tico: </strong>" + evento.getEixoTematicoId() + "<br /><br />");
		}
		if (evento.getObjetivoGeral() != null) {
			texto.append("<strong>Objetivo Geral: </strong>" + evento.getObjetivoGeral() + "<br /><br />");
		}
		if (evento.getObjetivoEspecifico() != null) {
			texto.append("<strong>Objetivos Espec??ficos: </strong>"	+ evento.getObjetivoEspecifico() + "<br /><br />");
		}
		if (evento.getConteudo() != null) {
			texto.append("<strong>Conte??do Program??tico: </strong>" + evento.getConteudo() + "<br /><br />");
		}
		if (evento.getResultadoEsperado() != null) {
			texto.append("<strong>Resultados Esperados: </strong>" + evento.getResultadoEsperado() + "<br /><br />");
		}		
		if (evento.getProvedores() != null) {
			texto.append("<strong>Provedor(es): </strong>" + evento.getProvedoresLista() + "<br /><br />");
		}
		if (evento.getResponsavelEvento() != null) {
			texto.append("<strong>Respons??vel pelo Evento: </strong>" + evento.getResponsavelEvento().getNome() + "<br /><br />");
		}		
		if (evento.getObservacoesPublicas() != null){
			texto.append("<strong>Observa????es: </strong>" + evento.getObservacoesPublicas() + "<br /><br />");
		}
		texto.append("<strong>Informa????es Complementares: </strong> <br />"
				+ " - A pr??-inscri????o no evento dever?? ser realizada no Sistema de Gest??o Educacional do IPC, em"
				+ " link dispon??vel no seu s??tio institucional (www.ipc.tce.ce.gov.br);"
				+ "<br />"
				+ " - No caso de servidores do TCE, a indica????o dever?? ser feita pelo chefe do participante no"
				+ " sistema at?? 7 (sete) dias antes do in??cio do evento;"
				+ "<br />"
				+ " - O participante ser?? informado por e-mail da confirma????o ou n??o de sua inscri????o;"
				+ "<br />"
				+ " - Ser?? emitido certificado para o participante que comprovar frequ??ncia igual ou superior ao"
				+ " m??nimo exigido, e for aprovado por nota (quando aplic??vel)."
				+ "<br /><br />");
		
		if (!(email.getObservacoes().equals(""))) {
			texto.append("<strong>Informa????es Adicionais: </strong>" + email.getObservacoes() + "<br /><br />");
		}
		texto.append("Voc?? poder?? consultar mais informa????es diretamente no nosso \"Sistema de Gest??o Educacional\" ou entrando em contato conosco por " + TEXTO_CONTATOS + ".<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Pl??cido Castelo<br />"
				+ "Tribunal de Contas do Estado do Cear??<br />"
				+ "<br /><i>E-mail gerado automaticamente, Favor n??o responder.</i><br />"
				+ "</font>");

		final String textoFinal = texto.toString();
		final String assunto = "Divulga????o de evento: "	+ evento.getTipoId() + " " + evento.getTitulo();
		String[] emails = email.getTo().split(",");
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, textoFinal, true, emails);
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getCause());
		}
	}

	public void emailPreInscricao(Long id, final Participante participante)	throws Exception {

		final boolean isUserTCE = participante.getOrgaoId().isTCE();

		final Evento evento = eventoDao.find(id);
		 	String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ participante.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Obrigado pelo seu interesse em nossos eventos.<br />"
				+ "Sua pr??-inscri????o no evento <b>\""
				+ evento.getTipoId()
				+ " "
				+ evento.getTitulo()
				+ "\"</b> foi recebida!<br />"
				+ "Voc?? receber?? um e-mail sobre a confirma????o ou n??o de sua inscri????o. Favor aguardar.<br />"
				+ "<br />";
				
		 		if(isUserTCE) {
		 		texto += "Importante: Para que a sua participa????o seja confirmada, ?? necess??ria a indica????o de sua chefia (realizada por meio deste sistema), al??m do atendimento de pr??-requisitos e condi????es espec??ficas requeridas para o evento";				
		 		}
		 		
		 		texto +=  "<br />"
		 		+ " Voc?? poder?? consultar mais informa????es diretamente no nosso Sistema de Gest??o Educacional ou entrando em contato conosco por " + TEXTO_CONTATOS + ".<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Pl??cido Castelo<br />"
				+ "Tribunal de Contas do Estado do Cear??<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i><br />"
				+ "</font>";
		final String assunto = "Recebimento de pr??-inscri????o: "	+ evento.getTipoId() + " " + evento.getTitulo();
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, participante.getEmail());
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}

		if (participante.getTipoId().getId() == 1 && participante.getSetorId() != null) {
			
			ResponsavelSetor rs = responsavelSetorDao.findAtivoBySetorId(participante.getSetorId().getId());

			if (rs != null) {
			
				final UsuarioSCA chefe = participante.getCpf().equals(rs.getResponsavel().getCpf()) ? rs.getResponsavelImediato() : rs.getResponsavel();
	
				if (chefe != null && chefe.getEmail() != null) {
					final String textoChefe = "<font name='Arial' size='small'>"
							+ "Prezado(a) Senhor(a) "
							+ chefe.getNome()
							+ ",<br />"
							+ "<br />"
							+ "Uma pr??-inscri????o foi realizada por <b>"
							+ participante.getNome()
							+ "</b> "
							+ "no evento <b>\""
							+ evento.getTipoId()
							+ " "
							+ evento.getTitulo()
							+ "\"</b>.<br />"
							+ "Favor verificar na tela \"Minhas Indica????es\" para proceder a respectiva indica????o.<br />"
							+ "<br />"
							+ "Atenciosamente,<br />"
							+ "Instituto Pl??cido Castelo<br />"
							+ "Tribunal de Contas do Estado do Cear??<br />"
							+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i><br />"
							+ "</font>";
					final String assuntoChefe = "Realiza????o de pr??-inscri????o: " + evento.getTipoId() + " " + evento.getTitulo();
					
					preparator = emailPreparator.prepare(assuntoChefe, textoChefe, true, chefe.getEmail());
					try {
						this.mailSender.send(preparator);
					} catch (MailException ex) {
						System.err.println(ex.getMessage());
					}
				}
			}
		}
	}

	public void emailUsuario(final Usuario usuario) throws Exception {
		
		final String assunto = "Confirma????o de Login";
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ usuario.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Um login para o nosso <a href='http://siged.ipc.tce.ce.gov.br/' target='_blank'>Sistema de Gest??o Educacional</a> (SIGED) foi criado para voc??, conforme dados abaixo:<br />"
				+ "<br />"
				+ "Login: "
				+ usuario.getUsername()
				+ " <br />"
				+ "Senha (inicial): 12345 <br />"
				+ "<br />"
				+ "Voc?? ser?? solicitado a trocar a senha por uma outra, que seja apenas de seu conhecimento, na primeira vez que fizer login no sistema.<br />"
				+ "<br />"
				+ "Voc?? poder?? consultar mais informa????es diretamente no nosso <a href='http://siged.ipc.tce.ce.gov.br/' target='_blank'>Sistema de Gest??o Educacional</a> (SIGED) ou entrando em contato conosco por " + TEXTO_CONTATOS + ".<br /> "
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Pl??cido Castelo<br />"
				+ "Tribunal de Contas do Estado do Cear??<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i>"
				+ "</font>";
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, usuario.getEmail());
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void emailSolicitacao(final EventoExtra eventoExtra,	final Participante participante, final UsuarioSCA chefe) throws Exception {
			
		final String assunto = "Realiza????o de solicita????o";
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ chefe.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Uma solicita????o foi realizada por <b>"
				+ participante.getNome()
				+ "</b> "
				+ "no evento <b>\""
				+ eventoExtra.getCurso()
				+ "\"</b>.<br />"
				+ "Favor verificar na tela \"Minhas Indica????es\" para proceder a analisar a demanda.<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Pl??cido Castelo<br />"
				+ "Tribunal de Contas do Estado do Cear??<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i><br />"
				+ "</font>";
		
		List<String> emailsList = new ArrayList<>();
		if (chefe.getEmail() != null)
			emailsList.add(chefe.getEmail());
		emailsList.add("ipctreinamento@tce.ce.gov.br");
		emailsList.add("ipc@tce.ce.gov.br");
		String[] emailsArr = new String[emailsList.size()];
		final String[] emails = emailsList.toArray(emailsArr);

		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, emails);
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}

	}

	public void emailSugestao(final EventoRecomendar eventorecomendar, final UsuarioSCA chefe) throws Exception {
		
		final String assunto = "Aviso de sugest??o de evento: " + eventorecomendar.getTitulo();
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ chefe.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Uma sugest??o para o evento "
				+ eventorecomendar.getTitulo()
				+ " foi feita para o seu setor. <br />"
				+ "<br />"
				+ "Voc?? poder?? analisar a sugest??o diretamente no nosso \"Sistema de Gest??o Educacional\". <br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Pl??cido Castelo<br />"
				+ "Tribunal de Contas do Estado do Cear??<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i><br />"
				+ "</font>";
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, chefe.getEmail());
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}

	}

	public void emailInscricao(Long id, final Participante participante) throws IOException, Exception {
		
		final Evento evento = eventoDao.find(id);
		final Inscricao inscricao = inscricaoDao.findByEventoAndParticipante(id, participante.getId());
		String dataConfirmacao = "";
		
		if(inscricao != null && inscricao.getDataConfirmacao() != null) {
			dataConfirmacao = " em " + util.formataData(inscricao.getDataConfirmacao());
		}
		
		String complementoEAD = "";
		
		if (evento.temModuloEAD()) {
			complementoEAD = "Voc?? receber?? em breve, antes do in??cio do evento, todas as informa????es necess??rias para o acesso ao ambiente virtual onde ser??o realizadas as atividades. Favor aguardar!<br /><br />";
		}
					
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ participante.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Sua inscri????o no evento <b>\""
				+ evento.getTipoId()
				+ " "
				+ evento.getTitulo()
				+ "\"</b> foi confirmada" + dataConfirmacao + "!<br />"
				+ "<br />"
				+ complementoEAD
				+ "Voc?? poder?? consultar mais informa????es sobre o evento na Ficha T??cnica anexa, diretamente no nosso \"Sistema de Gest??o Educacional\" "
				+ "ou entrando em contato conosco por " + TEXTO_CONTATOS + ".<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Pl??cido Castelo<br />"
				+ "Tribunal de Contas do Estado do Cear??<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i><br />"
				+ "</font>";
		
		final String assunto = "Confirma????o de inscri????o: "	+ evento.getTipoId() + " " + evento.getTitulo();
		final byte[] file = relatorioService.fichaTecnicaDoEvento(evento.getId());
		final Anexo anexo = new Anexo("ficha_tecnica_evento_" + evento.getId(), file, Anexo.FileType.PDF);
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, anexo, participante.getEmail());
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void emailInscricaoConfirmacaoLote(Email email) throws IOException, Exception {
		
		final Evento evento = eventoDao.find(email.getEventoId());
		
		String complementoEAD = "";
				
		if (evento.temModuloEAD()) {
			complementoEAD = "Voc?? receber?? em breve, antes do in??cio do evento, todas as informa????es necess??rias para o acesso ao ambiente virtual onde ser??o realizadas as atividades. Favor aguardar!<br /><br />";
		}
		
		String dataConfirmacao = " em " + util.formataData(new Date());
		
		final String assunto = "Confirma????o de inscri????o: "	+ evento.getTipoId() + " " + evento.getTitulo();
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a),<br />"
				+ "<br />"
				+ "Sua inscri????o no evento <b>\""
				+ evento.getTipoId()
				+ " "
				+ evento.getTitulo()
				+ "\"</b> foi confirmada" + dataConfirmacao + "!<br />"
				+ "<br />"
				+ complementoEAD
				+ "Voc?? poder?? consultar mais informa????es sobre o evento na Ficha T??cnica anexa, diretamente no nosso \"Sistema de Gest??o Educacional\" "
				+ "ou entrando em contato conosco por " + TEXTO_CONTATOS + ".<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Pl??cido Castelo<br />"
				+ "Tribunal de Contas do Estado do Cear??<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i><br />"
				+ "</font>";
		final String[] emails = email.getTo().split(",");
		
		final byte[] file = relatorioService.fichaTecnicaDoEvento(evento.getId());
		final Anexo anexo = new Anexo("ficha_tecnica_evento_" + evento.getId(), file, Anexo.FileType.PDF);
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, anexo, emails);
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void emailInscricaoNegada(Long id, final Participante participante, final String inscricao) throws Exception {
		
		final Evento evento = eventoDao.find(id);
		
		String motivoNaoConfirmacao = inscricao;
		final String assunto = "N??o confirma????o de inscri????o: "	+ evento.getTipoId() + " " + evento.getTitulo();
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ participante.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Sua pr??-inscri????o no evento <b>\""
				+ evento.getTipoId()
				+ " "
				+ evento.getTitulo()
				+ "\"</b> infelizmente n??o foi aceita.<br />"
				+ "<br />"
				+ "Motivo: "
				+ motivoNaoConfirmacao
				+ "<br />"
				+ "<br />"
				+ "Qualquer informa????o adicional, entre em contato conosco por " + TEXTO_CONTATOS + "."
				+ "<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Pl??cido Castelo<br />"
				+ "Tribunal de Contas do Estado do Cear??<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i><br />"
				+ "</font>";
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, participante.getEmail());
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void emailPreCadastro(Instrutor instrutor) throws Exception {
		
		final String assunto = "Recebimento de pr??-cadastro de instrutor";
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ instrutor.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Seu pr??-cadastro para instrutor do IPC foi recebido!<br />"
				+ "<br />"
				+ "Agradecemos o seu interesse! <br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Pl??cido Castelo<br />"
				+ "Tribunal de Contas do Estado do Cear??<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i><br />"
				+ "</font>";
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, instrutor.getEmail());
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}

	}

	public void emailInstrutorNaoAceito(final Instrutor instrutor, final String justificativa) throws Exception {

		final String assunto = "Cadastro de instrutor n??o aceito";
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ instrutor.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Seu pr??-cadastro para instrutor do IPC n??o foi aprovado.<br />"
				+ "<br />Motivo:"
				+ "<br />"
				+ justificativa
				+ "<br />"
				+ "<br />Agradecemos o seu interesse!<br />"
				+ "<br />" + TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Pl??cido Castelo<br />"
				+ "Tribunal de Contas do Estado do Cear??<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i><br />"
				+ "</font>";
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, instrutor.getEmail());
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void emailConfirmacaoIpcInstrutor(final Instrutor instrutor)	throws Exception {

		final String assunto = "Confirma????o de cadastro de instrutor";
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ instrutor.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Voc?? foi incluido no nosso banco de instrutores e poder?? ser convidado a ministrar cursos e palestras promovidos pelo IPC. <br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Pl??cido Castelo<br />"
				+ "Tribunal de Contas do Estado do Cear??<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i><br />"
				+ "</font>";
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, instrutor.getEmail());
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void emailComunicado(Email email) throws Exception {
		
		final Evento evento = eventoDao.find(email.getEventoId());

		String[] emails = email.getTo().split(",");
		final String mensagem = email.getMensagem();
		final String assunto = email.getTitulo();
		
		String subtexto = "";
		if(evento != null)
			subtexto += " aos participantes do evento " + evento.getNome2();
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a),<br />"
				+ "<br />"
				+ "O IPC comunica"+ subtexto +": <br />"
				+ "<br />"
				+ mensagem
				+ "<br /><br />"
				+ "Voc?? poder?? consultar mais informa????es diretamente no nosso \"Sistema de Gest??o Educacional\" ou entrando em contato conosco por " + TEXTO_CONTATOS + ".<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Pl??cido Castelo<br />"
				+ "Tribunal de Contas do Estado do Cear??<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i><br />"
				+ "</font>";

		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, emails);
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void emailEsqueciSenha(final Usuario usuario, final String novaSenha) throws Exception {
		
		final String assunto = "Nova senha de acesso";
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ usuario.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Uma nova senha foi gerada para seu usu??rio. Acesse o nosso \"Sistema de Gest??o Educacional\" com os seguintes dados: <br /><br />"
				+ "Usu??rio: "
				+ usuario.getUsername()
				+ "<br />"
				+ "Senha: "
				+ novaSenha
				+ "<br /><br />"
				+ "Recomendamos mudar a senha ap??s o primeiro acesso.<br />"
				+ "<br />"
				+ "Voc?? poder?? consultar mais informa????es diretamente no nosso \"Sistema de Gest??o Educacional\" ou entrando em contato conosco por " + TEXTO_CONTATOS + ".<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Pl??cido Castelo<br />"
				+ "Tribunal de Contas do Estado do Cear??<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i><br />"
				+ "</font>";
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, usuario.getEmail());
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}

	}
	
	
	public void emailCertificadosInvalidadosDoEvento(final Email email) throws Exception {		

		String[] emails = email.getTo().split(",");
		final String assunto = "Certificados invalidados";
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a),<br />"
				+ "<br />"
				+ "Informamos que devido a altera????es nas informa????es e/ou condi????es do "
				+ email.getTipoDoEvento() + " " + email.getTitulo()
				+ ", seus certificados emitidos no referido evento foram invalidados."
				+ "<br /><br />"
				+ "Para obten????o de um novo certificado com as informa????es atualizadas, voc?? precisa emit??-lo novamente no sistema SIGED."
				+ "<br /><br />"
				+ "Para mais informa????es, favor entrar em contato conosco por " + TEXTO_CONTATOS + ".<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Pl??cido Castelo<br />"
				+ "Tribunal de Contas do Estado do Cear??<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i><br />"
				+ "</font>";
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, emails);
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	
	public void emailCertificadoInvalidadoDoEvento(final Email email) throws Exception {
		final String assunto = "Certificado invalidado";
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ email.getNomeParticipante() + ",<br />"
				+ "<br />"
				+ "Informamos que o certificado de c??digo "
				+ email.getCodigoCertificado()
				+ " referente ao evento " + email.getTipoDoEvento() + " " + email.getTitulo()
				+ " foi invalidado."
				+ "<br /><br />"
				+ "Para obten????o de um novo certificado, voc?? precisa emit??-lo novamente no sistema SIGED."
				+ "<br /><br />"
				+ "Para mais informa????es, favor entrar em contato conosco por " + TEXTO_CONTATOS + ".<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Pl??cido Castelo<br />"
				+ "Tribunal de Contas do Estado do Cear??<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i><br />"
				+ "</font>";
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, email.getTo());
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	public void emailCertificadosInvalidadosPorReprovacao(final Email email) throws Exception {

		final String assunto = "Certificados invalidados";
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a),<br />"
				+ email.getNomeParticipante()
				+ "<br />"
				+ "Informamos que altera????es de nota e/ou frequ??ncia no "
				+ email.getTipoDoEvento() + " " + email.getTitulo()
				+ " ocasionaram sua Reprova????o, portanto seu certificado emitido no referido evento foi inv??lidado."
				+ "<br /><br />"
				+ "Para mais informa????es, favor entrar em contato conosco por meio e-mail (ipc@tce.ce.gov.br) ou telefone (85-3488-1793 / 5915).<br />"
				+ "<br />"										
				+ "Atenciosamente,<br />"
				+ "Instituto Pl??cido Castelo<br />"
				+ "Tribunal de Contas do Estado do Cear??<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i><br />"
				+ "</font>";
		String[] emails = email.getTo().split(",");

		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, emails);
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	public void enviarEmail(List<String> listaEmail, final String assunto, final String texto, final boolean isHtml) throws Exception {		
		
		String[] emailsTo = new String[listaEmail.size()];
		final String[] emails = listaEmail.toArray(emailsTo);
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, isHtml, emails);
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * Email enviado para os participantes sempre que um material did??tico ?? incluido no Evento em {@link EventoMaterialController#create(EventoMaterial, BindingResult, MultipartFile, ModelMap)}
	 * 
	 * @param listaEmails Lista de emails para envios
	 * @throws Exception Lan??ada pelo emailPreparator
	 * 
	 * @since 09/11/2018
	 */
	public void emailInclusaoMaterialDidatico(Evento evento, List<String> listaEmails, String nomeDoMaterial) throws Exception {
		
		if(!listaEmails.isEmpty()) {
			String[] emailsTo = new String[listaEmails.size()];
			final String[] emails = listaEmails.toArray(emailsTo);
			
			String assunto = "Inclus??o de material did??tico";
			String texto = "<font name='Arial' size='small'>"
					+ "Prezado(a) Senhor(a),<br />"
					+ "<br />"
					+ "Informamos que um material did??tico foi adicionado ao evento " + evento.getNome() + " no SIGED: " + nomeDoMaterial + "."
					+ "<br /><br />"
					+ "O material encontra-se dispon??vel para consulta pelos participantes em: Menu \"Eventos\" -> \"Meus Eventos\" (nas informa????es sobre o evento na coluna \"Material Did??tico\")."
					+ "<br /><br />"
					+ "Voc?? poder?? consultar mais informa????es diretamente no nosso Sistema de Gest??o Educacional ou entrando em contato conosco por " + TEXTO_CONTATOS + "."
					+ "<br /><br />"
					+ TEXTO_INFORMACOES_GERAIS + "."
					+ "<br /><br />"										
					+ "Atenciosamente,<br />"
					+ "Instituto Pl??cido Castelo<br />"
					+ "Tribunal de Contas do Estado do Cear??<br />"
					+ "<br /><i>E-mail gerado automaticamente, favor n??o responder.</i><br />"
					+ "</font>";			
			
			enviar("Inclus??o de material did??tico do evento " + evento.getNome(), assunto, texto, true, emails);
		}
	}
	
	/**
	 * Se a quantidade de emails for maior que o limit de emails por vez, dispara os emails via thread para controlar o intervalo entre os envios.</p>
	 * Se n??o precisar controlar o intervalo, envia todos os email de uma vez.
	 * 
	 * @param logInfo texto que vai constar no log do tomcat
	 * @param assunto
	 * @param texto
	 * @param isHtml
	 * @param emails
	 * @throws MailException
	 * @throws Exception
	 * 
	 * @since 09/11/2018
	 */
	private void enviar(final String logInfo, final String assunto, final String texto, final boolean isHtml, String... emails) throws MailException, Exception {
		if(emails.length > MalaDireta.LIMIT) {
			MalaDireta malaDireta = new MalaDireta(mailSender, emailPreparator, logInfo, assunto, texto, true, emails);
			malaDireta.enviar();
		} else {
			this.mailSender.send(emailPreparator.prepare(assunto, texto, true, emails));
		}
	}
	
}