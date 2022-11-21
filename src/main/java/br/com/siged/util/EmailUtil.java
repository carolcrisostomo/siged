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
			+ "cursos a distância (85-3488-4855/5915), Programa TCEDUC (85-3488-4855/5915) e outros assuntos (85-3488-1793/1789)";
	
	public static final String TEXTO_INFORMACOES_GERAIS = "Para informações gerais sobre o IPC e suas atividades, "
			+ "consulte nosso sítio institucional em <a href='http://www.ipc.tce.ce.gov.br' target='_blank'>www.ipc.tce.ce.gov.br</a> e acompanhe nossas mídias sociais";
	
	public void emailDivulgarEvento(Email email) throws Exception {
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		final Evento evento = eventoDao.find(email.getEventoId());

		List<Modulo> modulos = moduloDao.findByEventoId(evento.getId());

		StringBuilder texto = new StringBuilder();
		
		texto.append("<font name='Arial' size='small'>");

		texto.append("Prezado(a) Senhor(a),<br /><br />"
				+ "O IPC divulga a realização do evento " + evento.getTipoId()
				+ " " + evento.getTitulo() + ", conforme  detalhes abaixo:"
				+ "<br /><br />");

		if(!evento.getPublicoAlvoLista().equals("")){
			texto.append("<strong>Público Alvo: </strong>" + evento.getPublicoAlvoLista()
					+ "<br /><br />");			
		}
		
		texto.append("<strong>Modalidade: </strong>" + evento.getModalidadeModulosLista().toUpperCase() + "<br /><br />");
		
		if (evento.getCargaHoraria() != null) {
			texto.append("<strong>Carga Horária: </strong>"	+ evento.getCargaHoraria() 
					+ "<br /><br />");
		}
		if (evento.getDataInicioPrevisto() != null && evento.getDataFimPrevisto() != null) {
			texto.append("<strong>Período: </strong>"
					+ formato.format(evento.getDataInicioPrevisto()) + " a "
					+ formato.format(evento.getDataFimPrevisto())
					+ "<br /><br />");
		}
		
		if (modulos.size() > 0) {
			if (modulos.size() > 1) {
				texto.append("<strong>Horário: </strong>CONFORME MÓDULOS ABAIXO<br /><br />");
			} else {
				Modulo modulo = modulos.get(0);				
				if (modulo.getHoraInicioTurno1() != null && modulo.getHoraFimTurno1() != null) {
					texto.append("<strong>Horário: </strong>" + modulo.getTurnoLista());
					texto.append("<br /><br />");
				}
			}
		}
		
		if (evento.getDataInicioPreInscricao() != null && evento.getDataFimPreInscricao() != null) {
			texto.append("<strong>Pré-Inscrição: </strong>" + formato.format(evento.getDataInicioPreInscricao())
					+ " a " + formato.format(evento.getDataFimPreInscricao())
					+ "<br /><br />");
		}
		
		texto.append("<strong>Quantidade de vagas: </strong>" + evento.getTotalVagas());
		texto.append("<br /><br />");
		
		texto.append("<strong>Localização: </strong>" + evento.getLocalizacaoModulosLista());
		texto.append("<br /><br />");
		
		//Link do evento:
		if(evento.getLinkEvento() != null && !evento.getLinkEvento().isEmpty()) {
			texto.append("<strong>Link para o evento: </strong>");
			texto.append(evento.getLinkEvento());
			texto.append("<br /><br />");
		}
		
		//Link de gravação do evento:
		if(evento.getLinkGravacao() != null && !evento.getLinkGravacao().isEmpty()) {
			texto.append("<strong>Link para a gravação: </strong>");
			texto.append(evento.getLinkGravacao());
			texto.append("<br /><br />");
		}
		
		if (modulos.size() > 1) {
			
			for (Modulo modulo : modulos) {
				texto.append("<strong>Módulo: </strong>" + modulo.getTitulo() + "<br /><br />");
				texto.append("<strong>Carga Horária: </strong>"	+ modulo.getCargaHoraria() + "<br /><br />");
				texto.append("<strong>Período: </strong>" + formato.format(modulo.getDataInicio()) + " a "
						+ formato.format(modulo.getDataFim()) + "<br /><br />");
				
				if (modulo.getHoraInicioTurno1() != null && modulo.getHoraFimTurno1() != null) {
					texto.append("<strong>Horário: </strong>" + modulo.getTurnoLista());							
					texto.append("<br /><br />");
				}
				
				/**
				 * Campo modalidade no evento foi depreciado. Agora é na entidade Modulo
				 */
				texto.append("<strong>Modalidade: </strong>" + modulo.getModalidade().getDescricao());
				texto.append("<br /><br />");
				
				texto.append("<strong>Localização: </strong>" + modulo.getLocalizacaoComCidadeEUF());
				texto.append("<br /><br />");
				if (!modulo.getEnderecoComCidadeUFEPais().isEmpty()) {
					texto.append("<strong>Endereço: </strong>" + modulo.getEnderecoComCidadeUFEPais());
					texto.append("<br /><br />");
				}
				
				if(modulo.getTemFrequencia()){
					texto.append("<strong>Frequência Mínima: </strong>" + modulo.getFrequencia() + "%" + "<br /><br />");
				}
				if(modulo.getTemNota()){
					texto.append("<strong>Nota Mínima: </strong>" + modulo.getNota() + "<br /><br />");
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
					texto.append("<strong>Observação do módulo: </strong>" + modulo.getObservacao() + "<br /><br />");
				}
			}
		
		} else {
			/**
			 * @since 24/05/2019
			 * Ticket#-2019052110000675 - Manutenção corretiva e-mail divulgação de evento SIGED
			 * 
			 * 1: Quando o evento tiver módulo único, excluir as linhas Modalidade e Localização na parte do módulo pois está vindo repetido. 
			 *    Já tem na parte do evento. Deixe apenas as linhas de Frequência Mínima e Instrutor(es) nessa parte do módulo (para módulo único claro).
			 * 2: Excluir o texto " - O participante deverá comunicar a impossibilidade ...e eventos;". Apenas este parágrafo nesta parte do e-mail.
			 */
			if (modulos.size() == 1) {
				Modulo modulo = modulos.get(0);
				
				if(modulo.getTemFrequencia()){
					texto.append("<strong>Frequência Mínima: </strong>" + modulo.getFrequencia() + "%" + "<br /><br />");
				}
				if(modulo.getTemNota()){
					texto.append("<strong>Nota Mínima: </strong>" + modulo.getNota() + "<br /><br />");
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
					texto.append("<strong>Observação do módulo: </strong>" + modulo.getObservacao() + "<br /><br />");
				}
			}
		}
		if (evento.getEixoTematicoId() != null) {
			texto.append("<strong>Eixo Temático: </strong>" + evento.getEixoTematicoId() + "<br /><br />");
		}
		if (evento.getObjetivoGeral() != null) {
			texto.append("<strong>Objetivo Geral: </strong>" + evento.getObjetivoGeral() + "<br /><br />");
		}
		if (evento.getObjetivoEspecifico() != null) {
			texto.append("<strong>Objetivos Específicos: </strong>"	+ evento.getObjetivoEspecifico() + "<br /><br />");
		}
		if (evento.getConteudo() != null) {
			texto.append("<strong>Conteúdo Programático: </strong>" + evento.getConteudo() + "<br /><br />");
		}
		if (evento.getResultadoEsperado() != null) {
			texto.append("<strong>Resultados Esperados: </strong>" + evento.getResultadoEsperado() + "<br /><br />");
		}		
		if (evento.getProvedores() != null) {
			texto.append("<strong>Provedor(es): </strong>" + evento.getProvedoresLista() + "<br /><br />");
		}
		if (evento.getResponsavelEvento() != null) {
			texto.append("<strong>Responsável pelo Evento: </strong>" + evento.getResponsavelEvento().getNome() + "<br /><br />");
		}		
		if (evento.getObservacoesPublicas() != null){
			texto.append("<strong>Observações: </strong>" + evento.getObservacoesPublicas() + "<br /><br />");
		}
		texto.append("<strong>Informações Complementares: </strong> <br />"
				+ " - A pré-inscrição no evento deverá ser realizada no Sistema de Gestão Educacional do IPC, em"
				+ " link disponível no seu sítio institucional (www.ipc.tce.ce.gov.br);"
				+ "<br />"
				+ " - No caso de servidores do TCE, a indicação deverá ser feita pelo chefe do participante no"
				+ " sistema até 7 (sete) dias antes do início do evento;"
				+ "<br />"
				+ " - O participante será informado por e-mail da confirmação ou não de sua inscrição;"
				+ "<br />"
				+ " - Será emitido certificado para o participante que comprovar frequência igual ou superior ao"
				+ " mínimo exigido, e for aprovado por nota (quando aplicável)."
				+ "<br /><br />");
		
		if (!(email.getObservacoes().equals(""))) {
			texto.append("<strong>Informações Adicionais: </strong>" + email.getObservacoes() + "<br /><br />");
		}
		texto.append("Você poderá consultar mais informações diretamente no nosso \"Sistema de Gestão Educacional\" ou entrando em contato conosco por " + TEXTO_CONTATOS + ".<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Plácido Castelo<br />"
				+ "Tribunal de Contas do Estado do Ceará<br />"
				+ "<br /><i>E-mail gerado automaticamente, Favor não responder.</i><br />"
				+ "</font>");

		final String textoFinal = texto.toString();
		final String assunto = "Divulgação de evento: "	+ evento.getTipoId() + " " + evento.getTitulo();
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
				+ "Sua pré-inscrição no evento <b>\""
				+ evento.getTipoId()
				+ " "
				+ evento.getTitulo()
				+ "\"</b> foi recebida!<br />"
				+ "Você receberá um e-mail sobre a confirmação ou não de sua inscrição. Favor aguardar.<br />"
				+ "<br />";
				
		 		if(isUserTCE) {
		 		texto += "Importante: Para que a sua participação seja confirmada, é necessária a indicação de sua chefia (realizada por meio deste sistema), além do atendimento de pré-requisitos e condições específicas requeridas para o evento";				
		 		}
		 		
		 		texto +=  "<br />"
		 		+ " Você poderá consultar mais informações diretamente no nosso Sistema de Gestão Educacional ou entrando em contato conosco por " + TEXTO_CONTATOS + ".<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Plácido Castelo<br />"
				+ "Tribunal de Contas do Estado do Ceará<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i><br />"
				+ "</font>";
		final String assunto = "Recebimento de pré-inscrição: "	+ evento.getTipoId() + " " + evento.getTitulo();
		
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
							+ "Uma pré-inscrição foi realizada por <b>"
							+ participante.getNome()
							+ "</b> "
							+ "no evento <b>\""
							+ evento.getTipoId()
							+ " "
							+ evento.getTitulo()
							+ "\"</b>.<br />"
							+ "Favor verificar na tela \"Minhas Indicações\" para proceder a respectiva indicação.<br />"
							+ "<br />"
							+ "Atenciosamente,<br />"
							+ "Instituto Plácido Castelo<br />"
							+ "Tribunal de Contas do Estado do Ceará<br />"
							+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i><br />"
							+ "</font>";
					final String assuntoChefe = "Realização de pré-inscrição: " + evento.getTipoId() + " " + evento.getTitulo();
					
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
		
		final String assunto = "Confirmação de Login";
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ usuario.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Um login para o nosso <a href='http://siged.ipc.tce.ce.gov.br/' target='_blank'>Sistema de Gestão Educacional</a> (SIGED) foi criado para você, conforme dados abaixo:<br />"
				+ "<br />"
				+ "Login: "
				+ usuario.getUsername()
				+ " <br />"
				+ "Senha (inicial): 12345 <br />"
				+ "<br />"
				+ "Você será solicitado a trocar a senha por uma outra, que seja apenas de seu conhecimento, na primeira vez que fizer login no sistema.<br />"
				+ "<br />"
				+ "Você poderá consultar mais informações diretamente no nosso <a href='http://siged.ipc.tce.ce.gov.br/' target='_blank'>Sistema de Gestão Educacional</a> (SIGED) ou entrando em contato conosco por " + TEXTO_CONTATOS + ".<br /> "
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Plácido Castelo<br />"
				+ "Tribunal de Contas do Estado do Ceará<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i>"
				+ "</font>";
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, usuario.getEmail());
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void emailSolicitacao(final EventoExtra eventoExtra,	final Participante participante, final UsuarioSCA chefe) throws Exception {
			
		final String assunto = "Realização de solicitação";
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ chefe.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Uma solicitação foi realizada por <b>"
				+ participante.getNome()
				+ "</b> "
				+ "no evento <b>\""
				+ eventoExtra.getCurso()
				+ "\"</b>.<br />"
				+ "Favor verificar na tela \"Minhas Indicações\" para proceder a analisar a demanda.<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Plácido Castelo<br />"
				+ "Tribunal de Contas do Estado do Ceará<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i><br />"
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
		
		final String assunto = "Aviso de sugestão de evento: " + eventorecomendar.getTitulo();
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ chefe.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Uma sugestão para o evento "
				+ eventorecomendar.getTitulo()
				+ " foi feita para o seu setor. <br />"
				+ "<br />"
				+ "Você poderá analisar a sugestão diretamente no nosso \"Sistema de Gestão Educacional\". <br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Plácido Castelo<br />"
				+ "Tribunal de Contas do Estado do Ceará<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i><br />"
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
			complementoEAD = "Você receberá em breve, antes do início do evento, todas as informações necessárias para o acesso ao ambiente virtual onde serão realizadas as atividades. Favor aguardar!<br /><br />";
		}
					
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ participante.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Sua inscrição no evento <b>\""
				+ evento.getTipoId()
				+ " "
				+ evento.getTitulo()
				+ "\"</b> foi confirmada" + dataConfirmacao + "!<br />"
				+ "<br />"
				+ complementoEAD
				+ "Você poderá consultar mais informações sobre o evento na Ficha Técnica anexa, diretamente no nosso \"Sistema de Gestão Educacional\" "
				+ "ou entrando em contato conosco por " + TEXTO_CONTATOS + ".<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Plácido Castelo<br />"
				+ "Tribunal de Contas do Estado do Ceará<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i><br />"
				+ "</font>";
		
		final String assunto = "Confirmação de inscrição: "	+ evento.getTipoId() + " " + evento.getTitulo();
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
			complementoEAD = "Você receberá em breve, antes do início do evento, todas as informações necessárias para o acesso ao ambiente virtual onde serão realizadas as atividades. Favor aguardar!<br /><br />";
		}
		
		String dataConfirmacao = " em " + util.formataData(new Date());
		
		final String assunto = "Confirmação de inscrição: "	+ evento.getTipoId() + " " + evento.getTitulo();
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a),<br />"
				+ "<br />"
				+ "Sua inscrição no evento <b>\""
				+ evento.getTipoId()
				+ " "
				+ evento.getTitulo()
				+ "\"</b> foi confirmada" + dataConfirmacao + "!<br />"
				+ "<br />"
				+ complementoEAD
				+ "Você poderá consultar mais informações sobre o evento na Ficha Técnica anexa, diretamente no nosso \"Sistema de Gestão Educacional\" "
				+ "ou entrando em contato conosco por " + TEXTO_CONTATOS + ".<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Plácido Castelo<br />"
				+ "Tribunal de Contas do Estado do Ceará<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i><br />"
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
		final String assunto = "Não confirmação de inscrição: "	+ evento.getTipoId() + " " + evento.getTitulo();
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ participante.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Sua pré-inscrição no evento <b>\""
				+ evento.getTipoId()
				+ " "
				+ evento.getTitulo()
				+ "\"</b> infelizmente não foi aceita.<br />"
				+ "<br />"
				+ "Motivo: "
				+ motivoNaoConfirmacao
				+ "<br />"
				+ "<br />"
				+ "Qualquer informação adicional, entre em contato conosco por " + TEXTO_CONTATOS + "."
				+ "<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Plácido Castelo<br />"
				+ "Tribunal de Contas do Estado do Ceará<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i><br />"
				+ "</font>";
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, participante.getEmail());
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void emailPreCadastro(Instrutor instrutor) throws Exception {
		
		final String assunto = "Recebimento de pré-cadastro de instrutor";
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ instrutor.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Seu pré-cadastro para instrutor do IPC foi recebido!<br />"
				+ "<br />"
				+ "Agradecemos o seu interesse! <br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Plácido Castelo<br />"
				+ "Tribunal de Contas do Estado do Ceará<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i><br />"
				+ "</font>";
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, instrutor.getEmail());
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}

	}

	public void emailInstrutorNaoAceito(final Instrutor instrutor, final String justificativa) throws Exception {

		final String assunto = "Cadastro de instrutor não aceito";
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ instrutor.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Seu pré-cadastro para instrutor do IPC não foi aprovado.<br />"
				+ "<br />Motivo:"
				+ "<br />"
				+ justificativa
				+ "<br />"
				+ "<br />Agradecemos o seu interesse!<br />"
				+ "<br />" + TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Plácido Castelo<br />"
				+ "Tribunal de Contas do Estado do Ceará<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i><br />"
				+ "</font>";
		
		MimeMessagePreparator preparator = emailPreparator.prepare(assunto, texto, true, instrutor.getEmail());
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void emailConfirmacaoIpcInstrutor(final Instrutor instrutor)	throws Exception {

		final String assunto = "Confirmação de cadastro de instrutor";
		final String texto = "<font name='Arial' size='small'>"
				+ "Prezado(a) Senhor(a) "
				+ instrutor.getNome()
				+ ",<br />"
				+ "<br />"
				+ "Você foi incluido no nosso banco de instrutores e poderá ser convidado a ministrar cursos e palestras promovidos pelo IPC. <br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Plácido Castelo<br />"
				+ "Tribunal de Contas do Estado do Ceará<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i><br />"
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
				+ "Você poderá consultar mais informações diretamente no nosso \"Sistema de Gestão Educacional\" ou entrando em contato conosco por " + TEXTO_CONTATOS + ".<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Plácido Castelo<br />"
				+ "Tribunal de Contas do Estado do Ceará<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i><br />"
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
				+ "Uma nova senha foi gerada para seu usuário. Acesse o nosso \"Sistema de Gestão Educacional\" com os seguintes dados: <br /><br />"
				+ "Usuário: "
				+ usuario.getUsername()
				+ "<br />"
				+ "Senha: "
				+ novaSenha
				+ "<br /><br />"
				+ "Recomendamos mudar a senha após o primeiro acesso.<br />"
				+ "<br />"
				+ "Você poderá consultar mais informações diretamente no nosso \"Sistema de Gestão Educacional\" ou entrando em contato conosco por " + TEXTO_CONTATOS + ".<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Plácido Castelo<br />"
				+ "Tribunal de Contas do Estado do Ceará<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i><br />"
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
				+ "Informamos que devido a alterações nas informações e/ou condições do "
				+ email.getTipoDoEvento() + " " + email.getTitulo()
				+ ", seus certificados emitidos no referido evento foram invalidados."
				+ "<br /><br />"
				+ "Para obtenção de um novo certificado com as informações atualizadas, você precisa emití-lo novamente no sistema SIGED."
				+ "<br /><br />"
				+ "Para mais informações, favor entrar em contato conosco por " + TEXTO_CONTATOS + ".<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Plácido Castelo<br />"
				+ "Tribunal de Contas do Estado do Ceará<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i><br />"
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
				+ "Informamos que o certificado de código "
				+ email.getCodigoCertificado()
				+ " referente ao evento " + email.getTipoDoEvento() + " " + email.getTitulo()
				+ " foi invalidado."
				+ "<br /><br />"
				+ "Para obtenção de um novo certificado, você precisa emití-lo novamente no sistema SIGED."
				+ "<br /><br />"
				+ "Para mais informações, favor entrar em contato conosco por " + TEXTO_CONTATOS + ".<br />"
				+ "<br />"
				+ TEXTO_INFORMACOES_GERAIS + ".<br />"
				+ "<br />"
				+ "Atenciosamente,<br />"
				+ "Instituto Plácido Castelo<br />"
				+ "Tribunal de Contas do Estado do Ceará<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i><br />"
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
				+ "Informamos que alterações de nota e/ou frequência no "
				+ email.getTipoDoEvento() + " " + email.getTitulo()
				+ " ocasionaram sua Reprovação, portanto seu certificado emitido no referido evento foi inválidado."
				+ "<br /><br />"
				+ "Para mais informações, favor entrar em contato conosco por meio e-mail (ipc@tce.ce.gov.br) ou telefone (85-3488-1793 / 5915).<br />"
				+ "<br />"										
				+ "Atenciosamente,<br />"
				+ "Instituto Plácido Castelo<br />"
				+ "Tribunal de Contas do Estado do Ceará<br />"
				+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i><br />"
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
	 * Email enviado para os participantes sempre que um material didático é incluido no Evento em {@link EventoMaterialController#create(EventoMaterial, BindingResult, MultipartFile, ModelMap)}
	 * 
	 * @param listaEmails Lista de emails para envios
	 * @throws Exception Lançada pelo emailPreparator
	 * 
	 * @since 09/11/2018
	 */
	public void emailInclusaoMaterialDidatico(Evento evento, List<String> listaEmails, String nomeDoMaterial) throws Exception {
		
		if(!listaEmails.isEmpty()) {
			String[] emailsTo = new String[listaEmails.size()];
			final String[] emails = listaEmails.toArray(emailsTo);
			
			String assunto = "Inclusão de material didático";
			String texto = "<font name='Arial' size='small'>"
					+ "Prezado(a) Senhor(a),<br />"
					+ "<br />"
					+ "Informamos que um material didático foi adicionado ao evento " + evento.getNome() + " no SIGED: " + nomeDoMaterial + "."
					+ "<br /><br />"
					+ "O material encontra-se disponível para consulta pelos participantes em: Menu \"Eventos\" -> \"Meus Eventos\" (nas informações sobre o evento na coluna \"Material Didático\")."
					+ "<br /><br />"
					+ "Você poderá consultar mais informações diretamente no nosso Sistema de Gestão Educacional ou entrando em contato conosco por " + TEXTO_CONTATOS + "."
					+ "<br /><br />"
					+ TEXTO_INFORMACOES_GERAIS + "."
					+ "<br /><br />"										
					+ "Atenciosamente,<br />"
					+ "Instituto Plácido Castelo<br />"
					+ "Tribunal de Contas do Estado do Ceará<br />"
					+ "<br /><i>E-mail gerado automaticamente, favor não responder.</i><br />"
					+ "</font>";			
			
			enviar("Inclusão de material didático do evento " + evento.getNome(), assunto, texto, true, emails);
		}
	}
	
	/**
	 * Se a quantidade de emails for maior que o limit de emails por vez, dispara os emails via thread para controlar o intervalo entre os envios.</p>
	 * Se não precisar controlar o intervalo, envia todos os email de uma vez.
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