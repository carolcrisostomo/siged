package br.com.siged.entidades;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import br.com.siged.entidades.externas.Municipio;
import br.com.siged.entidades.externas.UfMunicipio;
import br.com.siged.entidades.externas.UsuarioSCA;

/**
 *
 * @author Marcelo
 */
@Entity
@Table(name = "evento_extra")
@NamedQueries({
    @NamedQuery(name = "EventoExtra.findAll", query = "SELECT e FROM EventoExtra e ORDER BY e.dataInicio DESC"),
    @NamedQuery(name = "EventoExtra.findById", query = "SELECT e FROM EventoExtra e WHERE e.id = :id"),
    @NamedQuery(name = "EventoExtra.findBySolicitanteLogin", query = "SELECT e FROM EventoExtra e WHERE e.solicitanteId = :solicitante ORDER BY e.dataInicio DESC"),
    @NamedQuery(name = "EventoExtra.findBySolicitante", query = "SELECT e FROM EventoExtra e WHERE e.solicitanteId = :solicitante and e.indicada = 'S' ORDER BY e.dataInicio DESC"),
    @NamedQuery(name = "EventoExtra.findBySolicitanteSemInscricao", query = "SELECT e FROM EventoExtra e WHERE e.solicitanteId = :solicitante and e.indicada = 'S' and e.inscricaoList IS EMPTY ORDER BY e.dataInicio DESC"),
    @NamedQuery(name = "EventoExtra.findByCurso", query = "SELECT e FROM EventoExtra e WHERE e.curso = :curso ORDER BY e.dataInicio DESC"),
    @NamedQuery(name = "EventoExtra.findByDataFim", query = "SELECT e FROM EventoExtra e WHERE e.dataFim = :dataFim ORDER BY e.dataInicio DESC"),
    @NamedQuery(name = "EventoExtra.findByDataInicio", query = "SELECT e FROM EventoExtra e WHERE e.dataInicio = :dataInicio ORDER BY e.dataInicio DESC"),
    @NamedQuery(name = "EventoExtra.findByHorario", query = "SELECT e FROM EventoExtra e WHERE e.horario = :horario ORDER BY e.dataInicio DESC"),
    @NamedQuery(name = "EventoExtra.findBySite", query = "SELECT e FROM EventoExtra e WHERE e.site = :site ORDER BY e.dataInicio DESC"),
    @NamedQuery(name = "EventoExtra.findByTurno", query = "SELECT e FROM EventoExtra e WHERE e.turno = :turno ORDER BY e.dataInicio DESC"),
    @NamedQuery(name = "EventoExtra.findByChefe", query = "SELECT e FROM EventoExtra e WHERE e.chefeId = :chefe ORDER BY e.dataInicio DESC"),
    @NamedQuery(name = "EventoExtra.findByChefeAndIndicada", query = "SELECT e FROM EventoExtra e WHERE e.chefeId.id = :chefeId AND e.indicada = :indicada ORDER BY e.dataInicio DESC"),
    @NamedQuery(name = "EventoExtra.findByValor", query = "SELECT e FROM EventoExtra e WHERE e.valor = :valor ORDER BY e.dataInicio DESC"),
    @NamedQuery(name = "EventoExtra.findByDataCadastro", query = "SELECT e FROM EventoExtra e WHERE e.dataCadastro = :dataCadastro ORDER BY e.dataInicio DESC"),
    @NamedQuery(name = "EventoExtra.findByProvedor", query = "SELECT e FROM EventoExtra e WHERE e.provedor = :provedor ORDER BY e.dataInicio DESC")})
@JsonIgnoreProperties(value={"solicitanteId","material","inscricaoList", "chefeId", "municipio", "paisId"})
@SequenceGenerator(name="sequence", sequenceName="seq_evento_extra", allocationSize=1)
public class EventoExtra implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@ManyToOne(fetch = FetchType.LAZY) @JsonIgnore
    @JoinColumn(name = "solicitante_id", referencedColumnName = "id")
    private UsuarioSCA solicitanteId;
    
	@ManyToOne(fetch = FetchType.LAZY) @JsonIgnore
    @JoinColumn(name = "chefe_id", referencedColumnName = "id")
    private UsuarioSCA chefeId;    
    
	@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "curso")
    private String curso;
    
	@NotNull(message=" Campo Obrigatório")
    @Column(name = "data_fim")
    @Temporal(TemporalType.DATE)
    private Date dataFim;
    
	@NotNull(message=" Campo Obrigatório")
    @Column(name = "data_inicio")
    @Temporal(TemporalType.DATE)
    private Date dataInicio;
    
	@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "horario")
    private String horario;
    
	@Column(name = "material")
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private byte[] material;
    
	@Column(name = "material_tipo") 
    private String materialTipo;
    
	@Column(name = "material_nome") 
    private String materialNome;
    
	@NotEmpty(message=" Campo Obrigatório")
	@Column(name = "justificativa", length = 2000)
    private String justificativa;
    
	@Column(name = "justificativachefe", length = 2000)
    private String justificativachefe;
	
	@Column(name = "obs_ipc", length = 2000)
    private String observacaoIpc;
	    
	@Column(name = "site")
    private String site;
    
	@Column(name = "turno")
    private String turno;
    
	@NotNull(message=" Campo Obrigatório")
    @Column(name = "valor")
    private BigDecimal valor;
    
	@Column(name = "data_cadastro")
    @Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;
    
	@Column(name = "provedor")
    private String provedor;
    
	@Column(name = "indicada")
    private String indicada;
    
	@Column(name = "data_indicacao")
    @Temporal(TemporalType.TIMESTAMP)
	private Date dataIndicacao;	
    
	@NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "pais_id", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY) @JsonIgnore
    private Pais paisId;
	
	@JoinColumn(name = "cidade_id", referencedColumnName = "ID")
    @ManyToOne @JsonIgnore
    private Municipio municipio;

	@Transient @JsonIgnore
    private UfMunicipio ufMunicipio;
    
	@JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "solicitacaoId")
    private List<Inscricao> inscricaoList;

    public EventoExtra() {
    }

    public EventoExtra(Long id) {
        this.id = id;
    }

	public EventoExtra(Long id, UsuarioSCA solicitanteId, UsuarioSCA chefeId,
			String curso, Date dataFim, Date dataInicio, String horario,
			byte[] material, String materialTipo, String materialNome,
			String justificativa, String observacaoIpc, String site, String turno, BigDecimal valor,
			Date dataCadastro, String provedor, String indicada, Pais paisId, Municipio municipio, UfMunicipio ufMunicipio) {
		super();
		this.id = id;
		this.solicitanteId = solicitanteId;
		this.chefeId = chefeId;
		this.curso = curso;
		this.dataFim = dataFim;
		this.dataInicio = dataInicio;
		this.horario = horario;
		this.material = material;
		this.materialTipo = materialTipo;
		this.materialNome = materialNome;
		this.justificativa = justificativa;
		this.observacaoIpc = observacaoIpc;
		this.site = site;
		this.turno = turno;
		this.valor = valor;
		this.dataCadastro = dataCadastro;
		this.provedor = provedor;
		this.indicada = indicada;
		this.paisId = paisId;
		this.municipio = municipio;
		this.ufMunicipio = ufMunicipio;
	}

	public UsuarioSCA getSolicitanteId() {
		return solicitanteId;
	}

	public void setSolicitanteId(UsuarioSCA solicitanteId) {
		this.solicitanteId = solicitanteId;
	}

	public UsuarioSCA getChefeId() {
		return chefeId;
	}

	public void setChefeId(UsuarioSCA chefeId) {
		this.chefeId = chefeId;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurso() {
        return curso;
    }
    public String getCursoEData() {
    	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");    	
    	String retorno = curso + " (" + formato.format(dataInicio) + " a " + formato.format(dataFim) + ")";
    	
    	if(this.dataIndicacao != null)
    		retorno += " Indicada em " + formato.format(dataIndicacao);
    	
        return retorno;
    }
    
    public void setCurso(String curso) {
        this.curso = curso;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public byte[] getMaterial() {
		return material;
	}

	public void setMaterial(MultipartFile material) throws IOException {
		this.material = material.getBytes();
	}

	public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
    
	public String getObservacaoIpc() {
		return observacaoIpc;
	}

	public void setObservacaoIpc(String observacaoIpc) {
		this.observacaoIpc = observacaoIpc;
	}

	public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String[] getTurno() {
    	if (turno != null) {
    		return turno.split(",");
    	} else {
    		return null;
    	}
    }
    
    public String getTurnoString() {
        return turno;
    }

//    public void setTurnoString(String[] turno) {
//    	String turnoaux = new String();
//    	for (String i : turno) {
//    		turnoaux += i + ", ";
//		}
//        this.turno = turnoaux;
//    }
    
    public void setTurno(String[] turno) {
    	String turnoaux = new String();
    	for (String i : turno) {
    		turnoaux += i + ",";
		}
    	
		int tamanho = turnoaux.length() - 1;
		if (tamanho == -1) {
			tamanho = 0;
		}
    	
        this.turno = turnoaux.substring(0, tamanho);
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getProvedor() {
        return provedor;
    }

    public void setProvedor(String provedor) {
        this.provedor = provedor;
    }

    public Pais getPaisId() {
        return paisId;
    }

    public void setPaisId(Pais paisId) {
        this.paisId = paisId;
    }    
    
    public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public UfMunicipio getUfMunicipio() {
		return ufMunicipio;
	}

	public void setUfMunicipio(UfMunicipio ufMunicipio) {
		this.ufMunicipio = ufMunicipio;
	}

	public String getIndicada() {
		return indicada;
	}

	public void setIndicada(String indicada) {
		this.indicada = indicada;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof EventoExtra)) {
            return false;
        }
        EventoExtra other = (EventoExtra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return curso;
    }

	public String getMaterialTipo() {
		return materialTipo;
	}

	public void setMaterialTipo(String materialTipo) {
		this.materialTipo = materialTipo;
	}

	public String getMaterialNome() {
		return materialNome;
	}

	public void setMaterialNome(String materialNome) {
		this.materialNome = materialNome;
	}

	public List<Inscricao> getInscricaoList() {
		
		Collections.sort(this.inscricaoList, new Comparator<Inscricao>(){

			@Override
			public int compare(Inscricao i1, Inscricao i2) {
				if(i1.getConfirmada() == "S")
					return -1;
				else if(i2.getConfirmada() == "S")
					return -1;
				
				return 0;
			}
			
		});
		
		return inscricaoList;
	}

	public void setInscricaoList(List<Inscricao> inscricaoList) {
		this.inscricaoList = inscricaoList;
	}

	public void setMaterial(byte[] material) {
		this.material = material;
	}

	public Date getDataIndicacao() {
		return dataIndicacao;
	}

	public void setDataIndicacao(Date dataIndicacao) {
		this.dataIndicacao = dataIndicacao;
	}

	public String getJustificativachefe() {
		return justificativachefe;
	}

	public void setJustificativachefe(String justificativachefe) {
		this.justificativachefe = justificativachefe;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}
	
}
