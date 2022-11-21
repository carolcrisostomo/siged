package br.com.siged.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.MaskFormatter;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Controller;

import br.com.siged.entidades.Modulo;


//teste commit

@Controller
public class Util {

	@SuppressWarnings("unchecked")
	public <T> List<T> initializeAndUnproxy(List<T> list) {
		List<T> listReturn = new ArrayList<T>();
		if (list == null) {
			throw new NullPointerException("Entity passed for initialization is null");
		}
		for (T entity : list) {
			Hibernate.initialize(entity);
			if (entity instanceof HibernateProxy) {
				entity = (T) ((HibernateProxy) entity)
						.getHibernateLazyInitializer().getImplementation();
			}
			listReturn.add(entity);
		}
		return listReturn;
	}

	public long countTurnos(String horaInicioTurno1, String horaFimTurno1,
			String horaInicioTurno2, String horaFimTurno2) {
		long turno;
		if (horaInicioTurno2 != null && horaFimTurno2 != null) {
			turno = 2;
		} else if (horaInicioTurno1 != null && horaFimTurno1 != null) {
			turno = 1;
		} else {
			turno = 0;
		}
		return turno;
	}

	public long daysBetween(Date startDate, Date endDate) {
		Calendar calStartDate = Calendar.getInstance();
		Calendar calEndDate = Calendar.getInstance();
		calStartDate.setTime(startDate);
		calEndDate.setTime(endDate);
		Calendar date = (Calendar) calStartDate.clone();
		long daysBetween = 0;
		while (date.before(calEndDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween + 1;
	}
	
	public Date getDataFormatada(Date data) {
		try {
			return getFormatoData().parse(getFormatoData().format(data));
		} catch (ParseException e) {
			throw new RuntimeException("Erro na conversão de data");
		}
	}

	public String calculaFrequenciaPerc(String frequenciaParticipanteTurno,
			String frequenciaModuloTurno) {
		String frequenciaPerc;
		if (!frequenciaModuloTurno.equals("0") && frequenciaModuloTurno != null) {
			Double frequenciaAux = (Double
					.parseDouble(frequenciaParticipanteTurno) * (double) 100 / Double
					.parseDouble(frequenciaModuloTurno));
			frequenciaPerc = new DecimalFormat("0.00").format(frequenciaAux)
					.replace(",", ".");
		} else {
			frequenciaPerc = "0.0";
		}
		return frequenciaPerc;
	}

	public String calculaFrequencia(String frequenciaParticipante,
			String frequenciaModulo) {
		String aprovadoF;
		if (frequenciaModulo != null && !frequenciaModulo.equals("0")) {
			BigDecimal frequenciaP = new BigDecimal(frequenciaParticipante);
			BigDecimal frequenciaM = new BigDecimal(frequenciaModulo);
			if (frequenciaP.compareTo(frequenciaM) == -1) {
				aprovadoF = "N";
			} else {
				aprovadoF = "S";
			}
		} else {
			aprovadoF = "S";
		}
		return aprovadoF;
	}

	public String calculaNota(String notaParticipante, String notaModulo) {
		String aprovadoN;
		aprovadoN = "S";
		if (notaModulo != null && !notaModulo.equals("0")) {
			if (notaParticipante.equals("-")) {
				notaParticipante = "0";
			}
			BigDecimal notaP = new BigDecimal(
					notaParticipante.replace(",", "."));
			BigDecimal notaM = new BigDecimal(notaModulo.replace(",", "."));
			if (notaP.compareTo(notaM) == -1) {
				aprovadoN = "N";
			}
		}
		return aprovadoN;
	}

	public String calculaAprovacao(String aprovadoN, String aprovadoF) {
		String aprovado;
		if (aprovadoN.equals("S") && aprovadoF.equals("S")) {
			aprovado = "S";
		} else {
			aprovado = "N";
		}
		return aprovado;
	}	

	public ComparadorPorData getComparadorPorData() {
		return new ComparadorPorData();
	}
	
	public String getNovoNomeArquivo(){	
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		
		return format.format(new Date());
	}
	
	public String getAnoArquivo(String nomeArquivo) {
		String source = FilenameUtils.removeExtension(nomeArquivo);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		try {
			Date dataArquivo = format.parse(source);
			format = new SimpleDateFormat("yyyy");
			return format.format(dataArquivo);
		} catch (ParseException e) {
			return getAnoAtual();
		}
	}
	
	public String getAnoAtual(){	
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		
		return format.format(new Date());
	}
	
	public Date getTomorrowDate(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.DATE, 1);
	    return cal.getTime();
	}
	
	public static String format(String pattern, Object value) {
	    MaskFormatter mask;
	    try {
	        mask = new MaskFormatter(pattern);
	        mask.setValueContainsLiteralCharacters(false);
	        return mask.valueToString(value);
	    } catch (ParseException e) {
	    	return String.valueOf(value);
	    }
	}
	
	public String gerarSenhaAleatoria() {
		String[] caract = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
				"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
				"w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H",
				"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
				"U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "0" };

		String senha = "";

		for (int x = 0; x < 6; x++) {
			int j = (int) (Math.random() * caract.length);
			senha += caract[j];
		}

		return senha;
	}	
	
	public String gerarCodigoVerificacao() {
		Random ran = new Random();
			String[] letras = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
				"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7",
				"8", "9" };
		String codigo = "";
			for (int i = 0; i < 19; i++) {
			if ((i + 1) % 5 == 0 && (i + 1 < 19))
				codigo += ".";
			else {
				int a = ran.nextInt(letras.length);
				codigo += letras[a];
			}
		}
		return codigo;
	}	
	
	public String formataData(Date data){
		return getFormatoData().format(data);
	}
	
	public String formataDouble(Double numero){
		return getFormatoDecimal().format(numero);
	}
	
	public String formataMonetario(Double valor){
		return getFormatoMonetario().format(valor);
	}
	
	public Date parseDate(String data) throws ParseException {
		return getFormatoData().parse(data);
	}
	
	public Date parseHora(String data) throws ParseException {
		return getFormatoHora().parse(data);
	}
	
	public boolean validaEmail(String email) {
		
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
		Pattern pattern = Pattern.compile(emailPattern);
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
		
	}
	
	public String formatarCpf(String cpf) {

		if (cpf.indexOf(".") != -1) {
			cpf = cpf.replace(".", "").replace("-", "");
		} else {
			String parte1 = cpf.substring(0, 3);
			String parte2 = cpf.substring(3, 6);
			String parte3 = cpf.substring(6, 9);
			String parte4 = cpf.substring(9, 11);
			cpf = parte1 + "." + parte2 + "." + parte3 + "-" + parte4;
		}

		return cpf;
	}
	
	private SimpleDateFormat getFormatoData() {
		return new SimpleDateFormat("dd/MM/yyyy");
	}
	
	private SimpleDateFormat getFormatoHora() {
		return new SimpleDateFormat("HH:mm");
	}
	
	private DecimalFormat getFormatoDecimal() {
		return new DecimalFormat("0.##");
	}
	
	private DecimalFormat getFormatoMonetario() {
		return new DecimalFormat("R$ #,###,###.00;R$ -#,###,###.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
	}
	
}

class ComparadorPorData implements Comparator<Modulo> {
	public int compare(Modulo o1, Modulo o2) {
		if (o1.getDataInicio().before(o2.getDataInicio()))
			return -1;
		else if (o1.getDataInicio().after(o2.getDataInicio()))
			return +1;
		else
			return 0;
	}
}
