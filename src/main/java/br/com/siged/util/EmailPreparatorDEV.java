package br.com.siged.util;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component(value = "emailPreparatorDEV")
public class EmailPreparatorDEV implements IEmailPreparator{
	
	private @Value("${siged.email.preparator.emailsTeste}") String[] emailsTeste;

	@Override
	public MimeMessagePreparator prepare(final String assunto, final String texto, final boolean isHtml, final String... emails) throws Exception {
		return new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = mimeMessageHelperFactory(mimeMessage, assunto, texto, isHtml, false);
				final InternetAddress[] addressTo = getAddress();
				helper.setBcc(addressTo);
			}
		};
	}

	@Override
	public MimeMessagePreparator prepare(final String assunto, final String texto, final boolean isHtml, final Anexo anexo, final String... emails) throws Exception {
		return new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = mimeMessageHelperFactory(mimeMessage, assunto, texto, isHtml, true);
				final InternetAddress[] addressTo = getAddress();
				helper.setBcc(addressTo);
				helper.addAttachment(anexo.getCompleteFilename(), new ByteArrayDataSource(anexo.getFile(), anexo.getContentType()));
			}
		};
	}

	private MimeMessageHelper mimeMessageHelperFactory(MimeMessage mimeMessage, String assunto, String texto, boolean html, boolean multipart) throws MessagingException, UnsupportedEncodingException {
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, multipart, ENCODING);
		helper.setFrom(FROM, PERSONAL);
		helper.setSubject(assunto);
		helper.setText(texto, html);
		
		return helper;
	}
	
	private InternetAddress[] getAddress() throws AddressException {
		InternetAddress[] addressTo = new InternetAddress[emailsTeste.length];
		for (int i = 0; i < emailsTeste.length; i++) {
			addressTo[i] = new InternetAddress(emailsTeste[i]);
		}
		
		return addressTo;
	}

	@Override
	public boolean isDEV() {
		return true;
	}
}
