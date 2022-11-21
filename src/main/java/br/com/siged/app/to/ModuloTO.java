package br.com.siged.app.to;

import java.text.SimpleDateFormat;

import br.com.siged.entidades.Modulo;

public class ModuloTO {
	
	private String titulo;
	private String inicio;
	private String fim;
	private String cargaHoraria;
	private String inicioPrimeiroTurno;
	private String fimPrimeiroTurno;
	private String inicioSegundoTurno;
	private String fimSegundoTurno;
	private String frequenciaMinima;
	private String notaMinima;
	private String localizacao;
	private String instrutores;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");	
	
	public ModuloTO( Modulo modulo ){
		
		this.titulo = modulo.getTitulo();
		try { this.inicio = dateFormat.format(modulo.getDataInicio()); } catch (Exception e) {};
		try { this.fim = dateFormat.format(modulo.getDataFim()); } catch (Exception e) {};
		this.cargaHoraria = modulo.getCargaHoraria();
		this.inicioPrimeiroTurno = modulo.getHoraInicioTurno1();
		this.fimPrimeiroTurno = modulo.getHoraFimTurno1();
		this.inicioSegundoTurno = modulo.getHoraInicioTurno2();
		this.fimSegundoTurno = modulo.getHoraFimTurno2();
		this.frequenciaMinima = modulo.getFrequencia();
		this.notaMinima = modulo.getNota();
		try { this.localizacao = modulo.getLocalizacaoId().getDescricao(); } catch (Exception e) {};
		this.instrutores = modulo.getInstrutorLista();	
	}

	public String getTitulo(){
		return titulo;
	}
	
	public String getInicio() {
		return inicio;
	}

	public String getFim() {
		return fim;
	}

	public String getCargaHoraria() {
		return cargaHoraria;
	}

	public String getInicioPrimeiroTurno() {
		return inicioPrimeiroTurno;
	}

	public String getFimPrimeiroTurno() {
		return fimPrimeiroTurno;
	}

	public String getInicioSegundoTurno() {
		return inicioSegundoTurno;
	}

	public String getFimSegundoTurno() {
		return fimSegundoTurno;
	}

	public String getFrequenciaMinima() {
		return frequenciaMinima;
	}

	public String getNotaMinima() {
		return notaMinima;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public String getInstrutores() {
		return instrutores;
	}

}
