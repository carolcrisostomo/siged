package br.com.siged.filtro;

public class EventoMaterialFiltro {
	
	private Long eventoId;
	private Long moduloId;
	private Long materialTipo;
	private String descricao;
	
	public Long getEventoId() {
		return eventoId;
	}
	public void setEventoId(Long eventoId) {
		this.eventoId = eventoId;
	}
	public Long getModuloId() {
		return moduloId;
	}
	public void setModuloId(Long moduloId) {
		this.moduloId = moduloId;
	}
	public Long getMaterialTipo() {
		return materialTipo;
	}
	public void setMaterialTipo(Long materialTipo) {
		this.materialTipo = materialTipo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
