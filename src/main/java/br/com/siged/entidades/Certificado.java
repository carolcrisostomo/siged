package br.com.siged.entidades;

import java.io.IOException;
import java.io.Serializable;

import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Marcelo
 */
@Entity
@Table(name = "certificado")
@NamedQueries({
    @NamedQuery(name = "Certificado.findAll", query = "SELECT new Certificado(c.id, c.eventoId, c.participanteId, c.arquivoTipo, c.arquivoNome) FROM Certificado c ORDER BY c.eventoId.dataInicioPrevisto DESC, c.participanteId.nome"),
    @NamedQuery(name = "Certificado.findById", query = "SELECT c FROM Certificado c WHERE c.id = :id"),
    @NamedQuery(name = "Certificado.findByEventoAndParticipante", query = "SELECT c FROM Certificado c WHERE c.eventoId = :evento and c.participanteId = :participante"),
    @NamedQuery(name = "Certificado.findByEvento", query = "SELECT new Certificado(c.id, c.eventoId, c.participanteId, c.arquivoTipo, c.arquivoNome) FROM Certificado c WHERE c.eventoId = :evento")})
@SequenceGenerator(name="sequence", sequenceName="seq_certificado", allocationSize=1)
public class Certificado implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "evento_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JsonIgnore
    private Evento eventoId;
    
	@NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "participante_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JsonIgnore
    private Participante participanteId;
    
	@Column(name = "arquivo")
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private byte[] arquivo;
    
	@Column(name = "arquivo_tipo") 
    private String arquivoTipo;
    
	@Column(name = "arquivo_nome") 
    private String arquivoNome;

    public Certificado() {
    }

    public Certificado(Long id) {
        this.id = id;
    }
    
    public Certificado(Long id, Evento eventoId, Participante participanteId, String arquivoTipo, String arquivoNome) {
		this.id = id;
		this.eventoId = eventoId;
		this.participanteId = participanteId;
		this.arquivoTipo = arquivoTipo;
		this.arquivoNome = arquivoNome;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Evento getEventoId() {
        return eventoId;
    }

    public void setEventoId(Evento eventoId) {
        this.eventoId = eventoId;
    }

    public Participante getParticipanteId() {
        return participanteId;
    }

    public void setParticipanteId(Participante participanteId) {
        this.participanteId = participanteId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Certificado)) {
            return false;
        }
        Certificado other = (Certificado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(MultipartFile arquivo) throws IOException {
		this.arquivo = arquivo.getBytes();
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	
	public String getArquivoTipo() {
		return arquivoTipo;
	}

	public void setArquivoTipo(String arquivoTipo) {
		this.arquivoTipo = arquivoTipo;
	}

	public String getArquivoNome() {
		return arquivoNome;
	}

	public void setArquivoNome(String arquivoNome) {
		this.arquivoNome = arquivoNome;
	}

}
