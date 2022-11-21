/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.siged.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

import br.com.siged.util.Util;

/**
 * 
 * @author André
 */
@Entity
@Table(name = "certificado_emitido")
@NamedQueries({
		@NamedQuery(name = "CertificadoEmitido.findAll", query = "SELECT ce FROM CertificadoEmitido ce"),
		@NamedQuery(name = "CertificadoEmitido.findById", query = "SELECT ce FROM CertificadoEmitido ce WHERE ce.id = :id"),
		@NamedQuery(name = "CertificadoEmitido.findByCodigoVerificacao", query = "SELECT ce FROM CertificadoEmitido ce WHERE ce.codigoVerificacao = :codigoVerificacao"),
		@NamedQuery(name = "CertificadoEmitido.findByEventoId", query = "SELECT ce FROM CertificadoEmitido ce WHERE ce.evento.id = :eventoId"),
		@NamedQuery(name = "CertificadoEmitido.findByParticipanteId", query = "SELECT ce FROM CertificadoEmitido ce WHERE ce.participante.id = :participanteId"),
		@NamedQuery(name = "CertificadoEmitido.findByEventoIdAndParticipanteId", query = "SELECT ce FROM CertificadoEmitido ce WHERE ce.evento.id = :eventoId AND ce.participante.id = :participanteId")})
@SequenceGenerator(name = "sequence", sequenceName = "seq_certificado_emitido", allocationSize = 1)
public class CertificadoEmitido implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
	@Column(name = "id")
	private Long id;

	@Column(name = "codigoverificacao")
	private String codigoVerificacao;

	@Column(name = "data_emissao", insertable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEmissao;
	
	@JoinColumn(name = "evento_id", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Evento evento;
	
	@JoinColumn(name = "participante_id", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Participante participante;

	public CertificadoEmitido() {
	}

	public CertificadoEmitido(String codigoVerificacao, Evento evento,
			Participante participante) {
		super();
		this.codigoVerificacao = codigoVerificacao;
		this.evento = evento;
		this.participante = participante;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoVerificacao() {
		if(codigoVerificacao != null)		
			return codigoVerificacao.replace(".", "");
	
		return null;
	}

	public void setCodigoVerificacao(String codigoVerificacao) {
		this.codigoVerificacao = codigoVerificacao;
	}
	
	public String getCodigoVerificacaoComMascara() {		
		if (codigoVerificacao != null)
    		return Util.format("AAAA.AAAA.AAAA.AAAA", codigoVerificacao);
    	
    	return null;    	
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Participante getParticipante() {
		return participante;
	}

	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

}
