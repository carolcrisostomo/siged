package br.com.siged.dto.relatorio.participantesexternos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.siged.entidades.StatusDesempenho;

public class EventoDTO {

	private Long id;
	private String tipo;
	private String titulo;
	private Date inicioRealizacao;
	private Date fimRealizacao;
	private List<ParticipanteExternoDTO> participantesExternos;
	private Integer totalReprovacoes;
	private Boolean apurado;
	
	public EventoDTO(Long id, String tipo, String titulo, Date inicioRealizacao, Date fimRealizacao, Boolean apurado) {
		this.participantesExternos = new ArrayList<>();
		this.tipo = tipo;
		this.titulo = titulo;
		this.inicioRealizacao = inicioRealizacao;
		this.fimRealizacao = fimRealizacao;
		this.totalReprovacoes = 0;
		this.apurado = apurado;
	}
	
	public void addParticipanteExterno(ParticipanteExternoDTO participanteExterno) {
		this.participantesExternos.add(participanteExterno);
		if(participanteExterno.getStatusDesempenho().equals(StatusDesempenho.NENHUM) 
				|| participanteExterno.getStatusDesempenho().equals(StatusDesempenho.REPROVADO))
			totalReprovacoes++;
	}

	public Long getId() {
		return id;
	}

	public String getTipo() {
		return tipo;
	}

	public String getTitulo() {
		return titulo;
	}

	public Date getInicioRealizacao() {
		return inicioRealizacao;
	}

	public Date getFimRealizacao() {
		return fimRealizacao;
	}

	public List<ParticipanteExternoDTO> getParticipantesExternos() {
		return participantesExternos;
	}
	
	public Integer getTotalReprovacoes() {
		return this.totalReprovacoes;
	}
	
	public Boolean getApurado() {
		return this.apurado;
	}
	
	public Integer getTotalParticipantesExternos() {
		return this.participantesExternos.size();
	}
	
	public String getAproveitamento() {
		Double porcentagem = (Double.valueOf(getTotalReprovacoes()) / getTotalParticipantesExternos()) * 100;
		return String.format("%.1f", 100 - porcentagem) + " %";
	}
	
	@Override
	public String toString() {
		String retorno = this.tipo + " " + this.titulo + "\n";
		for(ParticipanteExternoDTO participante : this.participantesExternos) {
			retorno += ">>> Participante: " + participante + "\n";
		}
		
		retorno += "\n\n";
		return retorno;
	}
	
}
