package br.com.siged.entidades;

import java.io.Serializable;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "evento_material")
@NamedQueries({
	  @NamedQuery(name = "EventoMaterial.findAll", query = "SELECT m FROM EventoMaterial m ORDER BY m.arquivoTce DESC"),
	  @NamedQuery(name = "EventoMaterial.findByEventoETipo", query = "SELECT m FROM EventoMaterial m WHERE m.eventoId.id = :eventoId AND m.materialTipo = :materialTipo ORDER BY m.moduloId.dataInicio DESC, m.descricao")	  
})
@SequenceGenerator(name = "sequence", sequenceName = "seq_material_didatico", allocationSize=1)
public class EventoMaterial implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final Long TIPO_MATERIAL_DIDATICO = 1L;
	
	private static final Long TIPO_MATERIAL_DIVULGACAO = 2L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @Column(name = "id")
    private Long id;
    
	@NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "evento_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JsonIgnore
    private Evento eventoId;    
	
    @JoinColumn(name = "modulo_id", referencedColumnName = "id")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JsonIgnore
    private Modulo moduloId;
     
	@Column(name = "arquivo_tipo") 
    private String arquivoTipo;
    
	@Column(name = "arquivo_original") 
    private String arquivoOriginal;
	
	@Column(name = "arquivo_tce") 
    private String arquivoTce;
	
	@Column(name = "material_tipo") 
    private Long materialTipo;
	
	@Column(name = "descricao") 
    private String descricao;
	
	@Transient
    private byte[] arquivo;
	
	public EventoMaterial(){
		
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

	public Modulo getModuloId() {
		return moduloId;
	}

	public void setModuloId(Modulo moduloId) {
		this.moduloId = moduloId;
	}

	public String getArquivoTipo() {
		return arquivoTipo;
	}

	public void setArquivoTipo(String arquivoTipo) {
		this.arquivoTipo = arquivoTipo;
	}

	public String getArquivoOriginal() {
		return arquivoOriginal;
	}

	public void setArquivoOriginal(String arquivoOriginal) {
		this.arquivoOriginal = arquivoOriginal;
	}

	public String getArquivoTCE() {
		return arquivoTce;
	}

	public void setArquivoTCE(String arquivoTCE) {
		this.arquivoTce = arquivoTCE;
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
	
	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}
	
	public boolean isMaterialDidatico() {
		if(this.materialTipo != null && this.materialTipo.equals(TIPO_MATERIAL_DIDATICO))
			return true;
		
		return false;
	}
	
	public boolean isMaterialDivulgacao() {
		if(this.materialTipo != null && this.materialTipo.equals(TIPO_MATERIAL_DIVULGACAO))
			return true;
		
		return false;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof EventoMaterial)) {
            return false;
        }
        EventoMaterial material = (EventoMaterial) object;
        if ((this.id == null && material.id != null) || (this.id != null && !this.id.equals(material.id))) {
            return false;
        }
        return true;
    }
}
