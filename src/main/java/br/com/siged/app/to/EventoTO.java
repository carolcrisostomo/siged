package br.com.siged.app.to;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Modulo;

public class EventoTO {

	private Long id;
	private String nome;
	private String objetivoGeral;
	private String tipoEvento;
	private String eixoTematico;
	private String publicoAlvo;
	private String modalidade;
	private String cargaHoraria;
	private String localizacao;
	private String provedor;
	private String responsavelPeloEvento;
	private String dataInicioPrevisto;
	private String dataFimPrevisto;
	private String dataInicioPreInscricao;
	private String dataFimPreInscricao;
	private String dataInicioRealizacao;
	private String dataFimRealizacao;
	private String conteudo;
	private String linkCertificado;
	
	private List<ModuloTO> modulos = new ArrayList<ModuloTO>();
	
	private DesempenhoTO desempenho;
	
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	public EventoTO (Evento evento) throws Exception{
		
		this.id = evento.getId();
		this.nome = evento.getNome2();
		this.objetivoGeral = evento.getObjetivoGeral();
		this.tipoEvento = evento.getTipoId().getDescricao();
		try { this.eixoTematico = evento.getEixoTematicoId().getDescricao(); } catch (Exception e) {};
		this.publicoAlvo = evento.getPublicoAlvoLista();
		
		/*
		 * Campo localização no evento foi depreciado.
		this.modalidade = evento.getModalidadeId().getDescricao();
		 */
		this.modalidade = evento.getModalidadeModulosLista();
		
		this.cargaHoraria = evento.getCargaHoraria();
		
		/*
		 * Campo localização no evento foi depreciado.
		this.localizacao = evento.getLocalizacaoId().getDescricao();
		 */
		this.localizacao = evento.getLocalizacaoModulosLista();
		
		this.provedor = evento.getProvedoresLista();
		try { this.responsavelPeloEvento = evento.getResponsavelEvento().getNome(); } catch (Exception e) {};
		try { this.dataInicioPrevisto = dateFormat.format(evento.getDataInicioPrevisto()); } catch (Exception e) {};
		try { this.dataFimPrevisto = dateFormat.format(evento.getDataFimPrevisto()); } catch (Exception e) {};
		try { this.dataInicioPreInscricao = dateFormat.format(evento.getDataInicioPreInscricao()); } catch (Exception e) {};
		try { this.dataFimPreInscricao = dateFormat.format(evento.getDataInicioPreInscricao()); } catch (Exception e) {};
		try { this.dataInicioRealizacao = dateFormat.format(evento.getDataInicioRealizacao()); } catch (Exception e) {};
		try { this.dataFimRealizacao = dateFormat.format(evento.getDataFimRealizacao()); } catch (Exception e) {};
		this.conteudo = evento.getConteudo();
		
		for(Modulo modulo: evento.getModuloList()){
			modulos.add(new ModuloTO(modulo));
		}
		
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getObjetivoGeral() {
		return objetivoGeral;
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

	public String getEixoTematico() {
		return eixoTematico;
	}

	public String getPublicoAlvo() {
		return publicoAlvo;
	}

	public String getModalidade() {
		return modalidade;
	}

	public String getCargaHoraria() {
		return cargaHoraria;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public String getProvedor() {
		return provedor;
	}

	public String getResponsavelPeloEvento() {
		return responsavelPeloEvento;
	}

	public String getDataInicioPrevisto() {
		return dataInicioPrevisto;
	}

	public String getDataFimPrevisto() {
		return dataFimPrevisto;
	}

	public String getDataInicioPreInscricao() {
		return dataInicioPreInscricao;
	}

	public String getDataFimPreInscricao() {
		return dataFimPreInscricao;
	}

	public String getDataInicioRealizacao() {
		return dataInicioRealizacao;
	}

	public String getDataFimRealizacao() {
		return dataFimRealizacao;
	}

	public String getConteudo() {
		return conteudo;
	}

	public List<ModuloTO> getModulos() {
		return modulos;
	}
	
	public String getLinkCertificado(){
		return linkCertificado;
	}
	
	public void setLinkCertificado (String link){
		this.linkCertificado = link;
	}

	public DesempenhoTO getDesempenho() {
		return desempenho;
	}

	public void setDesempenho(DesempenhoTO desempenho) {
		this.desempenho = desempenho;
	}	
	
	
}
