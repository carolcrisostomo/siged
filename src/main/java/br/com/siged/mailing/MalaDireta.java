package br.com.siged.mailing;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;

import br.com.siged.service.exception.BusinessException;
import br.com.siged.util.IEmailPreparator;

/**
 * Thread para envio de e-mails em quantidades acima do LIMIT. A cada LIMIT é dado um sleep(INTERVAL) para retomar o envio.
 * @author rafael.castro
 *
 */
public class MalaDireta implements Runnable {

	static final Logger logger = Logger.getLogger(MalaDireta.class);
	
	public static final int LIMIT = 100;
	public static final long INTERVAL = 90000l;
	public static final String THREAD_NAME = "mala-direta-service";
	
	private JavaMailSender mailSender;
	private IEmailPreparator preparator;
	
	private final String assunto;
	private final String texto;
	private final boolean isHtml;
	private final String[] emails;
	
	private static String logInfo = "no detail";
	private static Thread service;
	private static int amount;
	private static int total;
	
	public MalaDireta(JavaMailSender mailSender, IEmailPreparator preparator, String log, final String assunto, final String texto, final boolean isHtml, String... emails) throws BusinessException {
		if(isAlive()) {
			throw new BusinessException(status());
		}
		this.mailSender = mailSender;
		this.preparator = preparator;
		this.assunto = assunto;
		this.texto = texto;
		this.isHtml = isHtml;
		this.emails = emails;
		
		if(this.emails != null) {
			total = this.emails.length;
		} else {
			total = 0;
		}
		amount = 0;
		logInfo = log;
	}
	
	public void enviar() throws BusinessException {
		if(isAlive()) {
			throw new BusinessException(status());
		}
		service = new Thread(this, THREAD_NAME);
		service.start();
	}
	
	@Override
	public void run() {
		try {
			Map<Integer, String[]> mapa = mapearEmails();
			int total = this.emails.length;
			for(Integer key : mapa.keySet()) {
				amount += mapa.get(key).length;
				logger.info(String.format("[RUNNING: %s] Enviando e-mails %d-%d...", logInfo, amount, total));
				
				this.mailSender.send(this.preparator.prepare(assunto, texto, isHtml, mapa.get(key)));
				Thread.sleep(INTERVAL);
				
				logger.info(String.format("[RUNNING: %s] E-mails %d-%d enviados.", logInfo, amount, total));
			}
			
		} catch (Exception ex) {
			logger.error(String.format("[RUNNING: %s] Erro no envio do e-mail.", logInfo), ex);
		} finally {
			logger.info(String.format("[RUNNING: %s] mala direta foi encerrada.", logInfo));
		}
	}
	
	public static boolean isAlive() {
		if(service != null) {
			return service.isAlive();
		}
		return false;
	}
	
	public static String enviados() {
		Double porcentagem = (Double.valueOf(amount) / total) * 100;
		return String.format("%.0f",porcentagem) + "%";
	}
	
	public static Double progresso() {
		return Double.valueOf(amount) / total;
	}
	
	public static String status() {
		if(isAlive()) {
			return "Envio de e-mails em execução. " + logInfo + ". " + enviados() + " dos e-mails enviados.";
		}
		return "Not Running";
	}
	
	public static List<String> log() {
		List<String> out = new ArrayList<>();
		FileAppender appender = (FileAppender) Logger.getRootLogger().getAppender("logFileOut");

		try {			
			for(String filename : Files.readAllLines(Paths.get(appender.getFile()), StandardCharsets.UTF_8)) {
				if(filename.contains(THREAD_NAME)) {
					int begin = filename.indexOf(",");
					int end = filename.indexOf("[RUNNING:");
					String ini = filename.substring(0, begin);
					String fim = filename.substring(end, filename.length());
					String logFinal = (ini + " - " + fim).replaceAll("RUNNING: ", "");
					out.add(logFinal);
				}
			}
		} catch (IOException e) {
			out = new ArrayList<>();
			out.add("Não foi possível ler o log do envio da mala-direta");
			return out;
		}
		return out;
		
	}
	
	private Map<Integer, String[]> mapearEmails() {
		Map<Integer, String[]> mapa = new HashMap<>();
		
		int key = 1;
		int from = 0;
		for(int i = 1; i <= this.emails.length; i++) {
			if(i % LIMIT == 0 || i == this.emails.length) {
				mapa.put(key++, Arrays.copyOfRange(this.emails, from, i));
				from = i;
			}
		}
		
		return mapa;
	}
}
