package br.com.siged.util;

import java.io.UnsupportedEncodingException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component(value = "emailPreparatorPROD")
public class EmailPreparatorPROD implements IEmailPreparator {

	@Override
	public MimeMessagePreparator prepare(final String assunto, final String texto, final boolean isHtml, final String... emails) throws Exception {
		
		return new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				final InternetAddress[] addressTo = getAddress(emails);
				if(addressTo.length == 1)
					mimeMessage.setRecipient(Message.RecipientType.BCC, addressTo[0]);
				else if(addressTo.length > 1)
					mimeMessage.setRecipients(Message.RecipientType.BCC, addressTo);
				
				mimeMessage.setFrom(new InternetAddress(FROM, PERSONAL));
				mimeMessage.setSubject(assunto, ENCODING);
				
				if (isHtml)
					mimeMessage.setText(texto, ENCODING, "html");
				else
					mimeMessage.setText(texto, ENCODING);
			}
		};
	}

	@Override
	public MimeMessagePreparator prepare(final String assunto, final String texto, final boolean isHtml, final Anexo anexo, final String... emails) throws Exception {
		return new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = mimeMessageHelperFactory(mimeMessage, assunto, texto, isHtml, true);
				final InternetAddress[] addressTo = getAddress(emails);
				if(addressTo.length == 1)
					helper.setBcc(addressTo[0]);
				else if(addressTo.length > 1)
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

	private InternetAddress[] getAddress(String... emails) throws AddressException {
		InternetAddress[] addressTo = new InternetAddress[emails.length];
		for (int i = 0; i < emails.length; i++) {
			addressTo[i] = new InternetAddress(emails[i]);
		}
		
		return addressTo;
	}

	@Override
	public boolean isDEV() {
		return false;
	}
}
