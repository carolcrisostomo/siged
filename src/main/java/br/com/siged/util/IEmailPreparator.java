package br.com.siged.util;

import org.springframework.mail.javamail.MimeMessagePreparator;

public interface IEmailPreparator {

	public static final String FROM = "ipctreinamento@tce.ce.gov.br";
	public static final String PERSONAL = "IPC - TCE-CE";
	public static final String ENCODING = "UTF-8";
	
	public MimeMessagePreparator prepare(final String assunto, final String texto, final boolean isHtml, String... emails) throws Exception;
	
	public MimeMessagePreparator prepare(final String assunto, final String texto, final boolean isHtml, final Anexo anexo, String... emails) throws Exception;
	
	public boolean isDEV();
}
