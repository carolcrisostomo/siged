package br.com.siged.util;

import java.text.Normalizer;

public class StringUtil {

	public static final String COM_ACENTOS = "âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚçÇ";
	public static final String SEM_ACENTOS = "aaaaAAAAeeEEiIoooOOOuuUUcC";
	
	public static String removerAcentos(String texto) {
		return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
	
	public static String sqlTranslateQuery(String field, String parametro) {
		String semAcentos = removerAcentos(parametro);
		return "UPPER(translate("+ field + ", '" + COM_ACENTOS + "', '" + SEM_ACENTOS + "')) LIKE UPPER('%"+ semAcentos.trim().replace(" ", "%") +"%')";
	}
}
