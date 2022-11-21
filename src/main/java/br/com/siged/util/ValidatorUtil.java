package br.com.siged.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class ValidatorUtil {
	private Pattern pattern;
	private Matcher matcher;

	private static final String TIME24HOURS_PATTERN = "([0-1][0-9]|2[0-3]):[0-5][0-9]";
	
	private static final String EMAIL_PATTERN = ".+@.+\\.[a-z]+";
	
	private static final String CPF_PATTERN = "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}-?[0-9]{2}";
	
	private static final String URL_PATTERN = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	public boolean validarHora(final String hora) {
		if(hora == null)
			return false;
		
		pattern = Pattern.compile(TIME24HOURS_PATTERN);
		
		return validar(hora);
	}
	
	public boolean validarEmail(final String email) {
		if(email == null)
			return false;
		
		pattern = Pattern.compile(EMAIL_PATTERN);
		
		return validar(email);
	}
	
	public boolean validarCPF(final String cpf) {
		if(cpf == null)
			return false;
		
		pattern = Pattern.compile(CPF_PATTERN);
		
		return validar(cpf);
	}
	
	public boolean validarURL(final String url) {
		if(url == null)
			return false;
		
		pattern = Pattern.compile(URL_PATTERN);
		
		return validar(url);
	}
	
	private boolean validar(final String value) {
		matcher = pattern.matcher(value);
		return matcher.matches();
	}
}
