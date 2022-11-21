package br.com.siged.service.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import br.com.siged.service.AuthenticationService;
import br.com.siged.util.EmailUtil;

@Service
public class ExceptionHandler {

	static final Logger logger = Logger.getLogger(ExceptionHandler.class);

	@Autowired
	private EmailUtil emailUtil;	
	
	public Object handle(ProceedingJoinPoint pjp) throws Throwable {
		try {
			
			return pjp.proceed();
		
		} catch (Throwable e) {
			
			Throwable excecaoRaiz = e;
			while (excecaoRaiz.getCause() != null) {
				excecaoRaiz = excecaoRaiz.getCause();
			}
			
			if(e instanceof HttpStatusException) {
				HttpStatusException httperror = ((HttpStatusException) e);
				HttpStatus status = httperror.getStatusCode();
				if(status.equals(HttpStatus.FORBIDDEN))
					return "redirect:/evento/previstos/acessonegado";
				
				if(status.equals(HttpStatus.BAD_REQUEST)) {
					if(!httperror.getStatusText().equals("BAD_REQUEST"))
						return "redirect:/evento/previstos?mensagemErro=" + httperror.getStatusText();
					else
						return "redirect:/evento/previstos?mensagemErro=Não foi possível processar a requisição";
				}
				
				if(status.equals(HttpStatus.NOT_FOUND)) {
					if(!httperror.getStatusText().equals("NOT_FOUND"))
						return "redirect:/evento/previstos?mensagemErro=" + httperror.getStatusText();
					else
						return "redirect:/evento/previstos?mensagemErro=Página não encontrada";
				}
					
			}
			
			if (!(e instanceof BusinessException || e instanceof AuthenticationException)) {
				String idErro = gerarNumeroErro();
				logger.error(getMessagemLogErro(excecaoRaiz, idErro));
				e.printStackTrace();
				
				if(!emailUtil.isDEVMode())
					enviarEmailEquipeSIGED(e, idErro);
				
				return "redirect:/errors/erro?mensagem=Ocorreu um erro inesperado (Código "+ idErro +").";
			}
			
			return pjp.proceed();
		}
	}

	private String getMessagemLogErro(Throwable excecaoRaiz, String idErro) {
		StringBuilder messagemLog = new StringBuilder()
				.append("Erro ")
				.append(idErro)
				.append(": ")
				.append(excecaoRaiz.getMessage())
				.append("\r\n\tStack Trace: ")
				.append(getStackTraceAPartirDaExcecao(excecaoRaiz));
		
		return messagemLog.toString();
	}

	private void enviarEmailEquipeSIGED(Throwable e, String idErro) throws IOException, Exception {
		List<String> listaEmail = new ArrayList<String>();
		listaEmail.add("felipe.augusto@tce.ce.gov.br");
		listaEmail.add("rafael.castro@tce.ce.gov.br");
		listaEmail.add("eduardo.maia@tce.ce.gov.br");
		String stackTrace = getStackTraceAPartirDaExcecao(e);

		StringBuilder corpoEmail = new StringBuilder("Ocorreu um erro inesperado no SIGED.");
		
		corpoEmail.append("\n\nHost: ");
		corpoEmail.append(InetAddress.getLocalHost().getHostAddress());
		
		corpoEmail.append("\n\nHórario: ");
		corpoEmail.append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
		
		corpoEmail.append("\n\nUsuário: ");
		
		try {
			corpoEmail.append(AuthenticationService.getUsuarioLogado().getNome());
			corpoEmail.append(" (id: ").append(AuthenticationService.getUsuarioLogado().getId()).append(")");
		}catch (Exception ex) {
			corpoEmail.append("Não logado");
		}
				
		corpoEmail.append("\n\nNúmero gerado: ");
		corpoEmail.append(idErro);
		
		corpoEmail.append("\n\nMensagem: ");
		corpoEmail.append(e.getMessage());
		
		corpoEmail.append("\n\nStack Trace: ");
		corpoEmail.append(stackTrace);
		
		emailUtil.enviarEmail(listaEmail, "Erro no SIGED", corpoEmail.toString(), false);
	}

	private String getStackTraceAPartirDaExcecao(Throwable e) {
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		String stackTrace = writer.toString();
		return stackTrace;
	}
	
	private String gerarNumeroErro() {
		String idErro = String.valueOf(System.currentTimeMillis());
		if (idErro.length() > 6) {
			idErro = idErro.substring(idErro.length() - 6, idErro.length());
		}
		return (idErro);
	}
}

