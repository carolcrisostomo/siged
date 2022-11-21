package br.com.siged.dto.inscricao;

import java.util.ArrayList;
import java.util.List;

import br.com.siged.entidades.Participante;

public class InclusaoInscricaoLoteDTO {

	private List<InscricaoLoteDTO> incluidas;
	private List<InscricaoLoteDTO> naoIncluidas;
	
	public InclusaoInscricaoLoteDTO() {
		this.incluidas = new ArrayList<>();
		this.naoIncluidas = new ArrayList<>();
	}

	public void addIncluida(Participante participante) {
		InscricaoLoteDTO incluida = new InscricaoLoteDTO();
		incluida.setConfirmada(true);
		
		this.incluidas.add(incluida);
	}
	
	public void addNaoIncluida(Participante participante, String motivo) {
		InscricaoLoteDTO naoIncluida = new InscricaoLoteDTO();
		naoIncluida.setConfirmada(false);
		naoIncluida.setMotivo(motivo);
		
		this.naoIncluidas.add(naoIncluida);
	}
	
	public int incluidas() {
		return this.incluidas.size();
	}
	
	public int naoIncluidas() {
		return this.naoIncluidas.size();
	}
}
