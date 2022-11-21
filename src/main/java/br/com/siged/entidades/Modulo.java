package br.com.siged.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.siged.entidades.externas.Municipio;
import br.com.siged.entidades.externas.UfMunicipio;

/**
 *
 * @author Marcelo
 */
@Entity
@Table(name = "modulo")
@NamedQueries({
    @NamedQuery(name = "Modulo.findAll", query = "SELECT m FROM Modulo m"),
    @NamedQuery(name = "Modulo.findById", query = "SELECT m FROM Modulo m WHERE m.id = :id"),
    @NamedQuery(name = "Modulo.findByCargaHoraria", query = "SELECT m FROM Modulo m WHERE m.cargaHoraria = :cargaHoraria"),
    @NamedQuery(name = "Modulo.findByDataFim", query = "SELECT m FROM Modulo m WHERE m.dataFim = :dataFim"),
    @NamedQuery(name = "Modulo.findByDataInicio", query = "SELECT m FROM Modulo m WHERE m.dataInicio = :dataInicio"),
    @NamedQuery(name = "Modulo.findByTitulo", query = "SELECT m FROM Modulo m WHERE m.titulo = :titulo"),
    @NamedQuery(name = "Modulo.findByNota", query = "SELECT m FROM Modulo m WHERE m.nota = :nota"),
    @NamedQuery(name = "Modulo.findByDataCadastro", query = "SELECT m FROM Modulo m WHERE m.dataCadastro = :dataCadastro"),
    @NamedQuery(name = "Modulo.findByHoraFimTurno1", query = "SELECT m FROM Modulo m WHERE m.horaFimTurno1 = :horaFimTurno1"),
    @NamedQuery(name = "Modulo.findByHoraFimTurno2", query = "SELECT m FROM Modulo m WHERE m.horaFimTurno2 = :horaFimTurno2"),
    @NamedQuery(name = "Modulo.findByHoraInicioTurno1", query = "SELECT m FROM Modulo m WHERE m.horaInicioTurno1 = :horaInicioTurno1"),
    @NamedQuery(name = "Modulo.findByHoraInicioTurno2", query = "SELECT m FROM Modulo m WHERE m.horaInicioTurno2 = :horaInicioTurno2"),
    @NamedQuery(name = "Modulo.findByEventoId", query = "SELECT m FROM Modulo m WHERE m.eventoId.id = :eventoId ORDER BY m.dataInicio, m.id"),
    @NamedQuery(name = "Modulo.findByEvento", query = "SELECT m FROM Modulo m WHERE m.eventoId = :evento"), 
    @NamedQuery(name = "Modulo.findByEventoIdByNota", query = "SELECT m FROM Modulo m WHERE m.eventoId.id = :eventoId and m.nota is not null"),
    @NamedQuery(name = "Modulo.findByObservacao", query = "SELECT m FROM Modulo m WHERE m.observacao = :observacao")})
@SequenceGenerator(name="sequence", sequenceName="seq_modulo")
@JsonIgnoreProperties(value={"instrutorList", "eventoId", "localizacaoId", "avalicoesReacaoList", "modalidade"}) 
public class Modulo implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "carga_horaria")
    private String cargaHoraria;
    
	@NotNull(message=" Campo Obrigatório")
    @Column(name = "data_fim")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFim;
    
	@NotNull(message=" Campo Obrigatório")
    @Column(name = "data_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicio;
    
	@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "titulo")
    private String titulo;
    
	@Column(name = "nota")
    private String nota;
    
	@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "frequencia")
    private String frequencia;
    
	@Column(name = "data_cadastro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;
    
	@Column(name = "hora_fim_turno1")
    private String horaFimTurno1;
    
    @Column(name = "hora_fim_turno2")
    private String horaFimTurno2;
    
    @Column(name = "hora_fim_turno3")
    private String horaFimTurno3;
    
    @Column(name = "hora_inicio_turno1")
    private String horaInicioTurno1;
    
    @Column(name = "hora_inicio_turno2")
    private String horaInicioTurno2;
    
    @Column(name = "hora_inicio_turno3")
    private String horaInicioTurno3;
    
    @Column(name = "observacao", length = 2000)
    private String observacao;
    
    @JoinColumn(name = "pais_id", referencedColumnName = "id")
    @ManyToOne @JsonIgnore
    private Pais paisId;
	
	@JoinColumn(name="cidade_id", referencedColumnName="ID")
	@ManyToOne @JsonIgnore
	private Municipio municipio;
	
	@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "cidade_pais")
    private String cidadePais;
	
	@Transient @JsonIgnore
	private UfMunicipio UfMunicipio;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "modulo_instrutor", joinColumns = {
    @JoinColumn(name = "modulo_id", referencedColumnName = "id")}, inverseJoinColumns = {
    @JoinColumn(name = "instrutor_id", referencedColumnName = "id")})
    @JsonIgnore
    private List<Instrutor> instrutorList;
    
    @NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "evento_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonIgnore
    private Evento eventoId;
    
    @NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "localizacao_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY) 
    @JsonIgnore
    private TipoLocalizacaoEvento localizacaoId;
    
    
	@OneToMany(fetch = FetchType.LAZY, mappedBy="modulo", cascade = CascadeType.ALL)
	@JsonIgnore
    private List<AvaliacaoReacao> avalicoesReacaoList;
	
	@NotNull(message = " Campo Obrigatório")
	@JoinColumn(name = "modalidade_id", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Modalidade modalidade;
	
	@PrePersist @PreUpdate
	public void PrePersistUpdate() {
		this.horaInicioTurno1 = forcarFormatoHoraTurno(this.horaInicioTurno1);
		this.horaInicioTurno2 = forcarFormatoHoraTurno(this.horaInicioTurno2);
		this.horaInicioTurno3 = forcarFormatoHoraTurno(this.horaInicioTurno3);
		
		this.horaFimTurno1 = forcarFormatoHoraTurno(this.horaFimTurno1);
		this.horaFimTurno2 = forcarFormatoHoraTurno(this.horaFimTurno2);
		this.horaFimTurno3 = forcarFormatoHoraTurno(this.horaFimTurno3);
	}

    public Modulo() {
    }

    public Modulo(Long id) {
        this.id = id;
    }
    
    public Modulo(Long id, String cargaHoraria, Date dataFim, Date dataInicio,
			String titulo, String nota, String frequencia, Date dataCadastro,
			String horaFimTurno1, String horaFimTurno2, String horaFimTurno3,
			String horaInicioTurno1, String horaInicioTurno2, String horaInicioTurno3,
			String observacao, Evento eventoId,
			TipoLocalizacaoEvento localizacaoId) {
		super();
		this.id = id;
		this.cargaHoraria = cargaHoraria;
		this.dataFim = dataFim;
		this.dataInicio = dataInicio;
		this.titulo = titulo;
		this.nota = nota;
		this.frequencia = frequencia;
		this.dataCadastro = dataCadastro;
		this.horaFimTurno1 = horaFimTurno1;
		this.horaFimTurno2 = horaFimTurno2;
		this.horaFimTurno3 = horaFimTurno3;
		this.horaInicioTurno1 = horaInicioTurno1;
		this.horaInicioTurno2 = horaInicioTurno2;
		this.horaInicioTurno3 = horaInicioTurno3;
		this.observacao = observacao;
		this.eventoId = eventoId;
		this.localizacaoId = localizacaoId;
	}



    public Modulo(Long id, String cargaHoraria, Date dataFim, Date dataInicio,
			String titulo, String nota, String frequencia, Date dataCadastro,
			String horaFimTurno1, String horaFimTurno2, String horaFimTurno3,
			String horaInicioTurno1, String horaInicioTurno2, String horaInicioTurno3,
			String observacao, Evento eventoId,
			TipoLocalizacaoEvento localizacaoId, String cidadePais) {
		super();
		this.id = id;
		this.cargaHoraria = cargaHoraria;
		this.dataFim = dataFim;
		this.dataInicio = dataInicio;
		this.titulo = titulo;
		this.nota = nota;
		this.frequencia = frequencia;
		this.dataCadastro = dataCadastro;
		this.horaFimTurno1 = horaFimTurno1;
		this.horaFimTurno2 = horaFimTurno2;
		this.horaFimTurno3 = horaFimTurno3;
		this.horaInicioTurno1 = horaInicioTurno1;
		this.horaInicioTurno2 = horaInicioTurno2;
		this.horaInicioTurno3 = horaInicioTurno3;
		this.observacao = observacao;
		this.eventoId = eventoId;
		this.localizacaoId = localizacaoId;
		this.cidadePais = cidadePais;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(String cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
    public String getCidadePais() {
    	return cidadePais;
    }
    public void setCidadePais(String cidadePais) {
    	this.cidadePais = cidadePais;
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
		return UfMunicipio;
	}

	public void setUfMunicipio(UfMunicipio ufMunicipio) {
		UfMunicipio = ufMunicipio;
	}
    
    private String forcarFormatoHoraTurno(String horaTurno) {
    	if(horaTurno != null && !horaTurno.contains(":") && horaTurno.length() == 2) {
    		return horaTurno + ":00";
    	}
    	return horaTurno;
    }

    public String getHoraFimTurno1() {
        return this.forcarFormatoHoraTurno(horaFimTurno1);
    }

    public void setHoraFimTurno1(String horaFimTurno1) {
        this.horaFimTurno1 = horaFimTurno1;
    }

    public String getHoraFimTurno2() {
        return this.forcarFormatoHoraTurno(horaFimTurno2);
    }

    public void setHoraFimTurno2(String horaFimTurno2) {
        this.horaFimTurno2 = horaFimTurno2;
    }    
    
    public String getHoraFimTurno3() {
		return this.forcarFormatoHoraTurno(horaFimTurno3);
	}

	public void setHoraFimTurno3(String horaFimTurno3) {
		this.horaFimTurno3 = horaFimTurno3;
	}

	public String getHoraInicioTurno1() {
		return this.forcarFormatoHoraTurno(horaInicioTurno1);
    }

    public void setHoraInicioTurno1(String horaInicioTurno1) {
        this.horaInicioTurno1 = horaInicioTurno1;
    }

    public String getHoraInicioTurno2() {
        return this.forcarFormatoHoraTurno(horaInicioTurno2);
    }

    public void setHoraInicioTurno2(String horaInicioTurno2) {
        this.horaInicioTurno2 = horaInicioTurno2;
    }    
    
    public String getHoraInicioTurno3() {
		return this.forcarFormatoHoraTurno(horaInicioTurno3);
	}

	public void setHoraInicioTurno3(String horaInicioTurno3) {
		this.horaInicioTurno3 = horaInicioTurno3;
	}

	public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    
    public List<Instrutor> getInstrutorList() {
        return instrutorList;
    }
    
    public void setInstrutorList(List<Instrutor> instrutorList) {
        this.instrutorList = instrutorList;
    }
    
    public String getInstrutorLista() {
    	String instrutores = "";
		for (Instrutor instrutor : instrutorList) {
			instrutores = instrutores + instrutor.getNome() + ", ";
		}
		int tamanho = instrutores.length() - 2;
		if (tamanho == -2) {
			tamanho = 0;
		}
		return instrutores.substring(0, tamanho);
    }

    public Evento getEventoId() {
        return eventoId;
    }

    public void setEventoId(Evento eventoId) {
        this.eventoId = eventoId;
    }

    public TipoLocalizacaoEvento getLocalizacaoId() {
        return localizacaoId;
    }

    public void setLocalizacaoId(TipoLocalizacaoEvento localizacaoId) {
        this.localizacaoId = localizacaoId;
    }
    
    public List<AvaliacaoReacao> getAvalicoesReacaoList() {
		return avalicoesReacaoList;
	}

	public void setAvalicoesReacaoList(List<AvaliacaoReacao> avalicoesReacaoList) {
		this.avalicoesReacaoList = avalicoesReacaoList;
	}

	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}

	public String getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(String frequencia) {
		this.frequencia = frequencia;
	}
	
	public boolean getTemFrequencia() {
		if(frequencia == null || frequencia.equals("0"))
			return false;
		 return true;
	}
	 
	public boolean getTemNota() {
		if(nota == null || nota.equals("0"))
			return false;
		 return true;
	}
	
	public String getTurnoLista(){
		
		String turnos = "";
		
		if(getHoraInicioTurno1() != null) {
			turnos += getHoraInicioTurno1() + " - " + getHoraFimTurno1() + ", ";
		}
		
		if(getHoraInicioTurno2() != null) {
			turnos += getHoraInicioTurno2() + " - " + getHoraFimTurno2() + ", ";
		}

		if(getHoraInicioTurno3() != null) {
			turnos += getHoraInicioTurno3() + " - " + getHoraFimTurno3() + ", " ;
		}
		
		if (turnos.length() > 2) {
			turnos = turnos.substring(0, turnos.length() - 2);
		}
		
		return turnos;
	}
	
	public Map<String, String> getHorasDosTurnosMap(Modulo modulo) {
		Map<String, String> turnos = new HashMap<>();
		turnos.put("horaInicioTurno1", modulo.getHoraInicioTurno1());
		turnos.put("horaInicioTurno2", modulo.getHoraInicioTurno2());
		turnos.put("horaInicioTurno3", modulo.getHoraInicioTurno3());
		
		turnos.put("horaFimTurno1", modulo.getHoraFimTurno1());
		turnos.put("horaFimTurno2", modulo.getHoraFimTurno2());
		turnos.put("horaFimTurno3", modulo.getHoraFimTurno3());
		
		return turnos;
	}
	
	public String getCidadeEUF() {
		String localizacao = "";
		if (!localizacaoId.isInternet() 
				&& localizacaoId.getMunicipio() != null 
				&& localizacaoId.getMunicipio().getUfMunicipio() != null) {
			localizacao += localizacaoId.getMunicipio().getNome().toUpperCase() + "-" + localizacaoId.getMunicipio().getUfMunicipio(); 
		}
		return localizacao;
	}
	
	public String getLocalizacaoComCidadeEUF() {
		String localizacao = localizacaoId.getDescricao();
		if (!localizacaoId.isInternet() 
				&& localizacaoId.getMunicipio() != null 
				&& localizacaoId.getMunicipio().getUfMunicipio() != null) {
			localizacao += ", " + localizacaoId.getMunicipio().getNome().toUpperCase() + "-" + localizacaoId.getMunicipio().getUfMunicipio(); 
		}
		return localizacao;
	}
	
	public String getEnderecoComCidadeUFEPais() {
		String localizacao = "";
		if (!localizacaoId.isInternet()
				&& localizacaoId.getEndereco() != null
				&& localizacaoId.getMunicipio() != null 
				&& localizacaoId.getMunicipio().getUfMunicipio() != null
				&& localizacaoId.getPaisId() != null) {
			localizacao += localizacaoId.getEndereco()
					+ ", " + localizacaoId.getMunicipio().getNome().toUpperCase() 
					+ "-" + localizacaoId.getMunicipio().getUfMunicipio()
					+ ", "
					+ localizacaoId.getPaisId().getDescricao();
		}
		return localizacao;
	}
	
	public boolean temSomentePrimeiroTurno() {
		Map<String, String> turnos = getHorasDosTurnosMap(this);
		int quantidade = 0;
		for (Entry<String, String> turno : turnos.entrySet()) {
			if (turno.getValue() != null && !turno.getValue().isEmpty()) {
				quantidade++;
			}
		}
		
		return turnos.containsKey("horaInicioTurno1") && turnos.containsKey("horaFimTurno1") && quantidade == 2;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Modulo)) {
            return false;
        }
        Modulo other = (Modulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return titulo;
    }

}
