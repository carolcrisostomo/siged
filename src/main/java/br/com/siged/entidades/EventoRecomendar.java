package br.com.siged.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.siged.entidades.externas.Municipio;
import br.com.siged.entidades.externas.Setor;
import br.com.siged.entidades.externas.UfMunicipio;

/**
 * 
 * @author Marcelo
 */
@Entity
@Table(name = "evento_recomendar")
@NamedQueries({
		@NamedQuery(name = "EventoRecomendar.findAll", query = "SELECT e FROM EventoRecomendar e"),
		@NamedQuery(name = "EventoRecomendar.findById", query = "SELECT e FROM EventoRecomendar e WHERE e.id = :id"),
		@NamedQuery(name = "EventoRecomendar.findByCargaHoraria", query = "SELECT e FROM EventoRecomendar e WHERE e.cargaHoraria = :cargaHoraria"),
		@NamedQuery(name = "EventoRecomendar.findByDataFim", query = "SELECT e FROM EventoRecomendar e WHERE e.dataFim = :dataFim"),
		@NamedQuery(name = "EventoRecomendar.findByDataInicio", query = "SELECT e FROM EventoRecomendar e WHERE e.dataInicio = :dataInicio"),
		@NamedQuery(name = "EventoRecomendar.findByObjetivo", query = "SELECT e FROM EventoRecomendar e WHERE e.objetivo = :objetivo"),
		@NamedQuery(name = "EventoRecomendar.findByObservacao", query = "SELECT e FROM EventoRecomendar e WHERE e.observacao = :observacao"),
		@NamedQuery(name = "EventoRecomendar.findBySetorId", query = "SELECT e FROM EventoRecomendar e WHERE e.setorId = :setorId order by e.dataInicio"),
		@NamedQuery(name = "EventoRecomendar.findByTitulo", query = "SELECT e FROM EventoRecomendar e WHERE e.titulo = :titulo"),
		@NamedQuery(name = "EventoRecomendar.findByValor", query = "SELECT e FROM EventoRecomendar e WHERE e.valor = :valor") })
@JsonIgnoreProperties(value = { "municipio", "paisId" })
@SequenceGenerator(name = "sequence", sequenceName = "seq_evento_recomendar", allocationSize = 1)
public class EventoRecomendar implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
	@Column(name = "id")
	private Long id;

	@NotEmpty(message = " Campo Obrigatório")
	@Column(name = "carga_horaria")
	private String cargaHoraria;

	@NotNull(message = " Campo Obrigatório")
	@Column(name = "data_fim")
	@Temporal(TemporalType.DATE)
	private Date dataFim;

	@NotNull(message = " Campo Obrigatório")
	@Column(name = "data_inicio")
	@Temporal(TemporalType.DATE)
	private Date dataInicio;

	@Column(name = "material")
	private byte[] material;

	@Column(name = "material_tipo")
	private String materialTipo;

	@Column(name = "material_nome")
	private String materialNome;

	@NotEmpty(message = " Campo Obrigatório")
	@Column(name = "objetivo")
	private String objetivo;

	@Column(name = "observacao", length = 2000)
	private String observacao;

	@NotEmpty(message = " Campo Obrigatório")
	@Column(name = "titulo")
	private String titulo;

	@Column(name = "valor")
	private BigDecimal valor;

	@JoinColumn(name = "setor_id", referencedColumnName = "IDSETOR")
	@ManyToOne
	@JsonIgnore
	private Setor setorId;

	@Column(name = "justificativa", length = 2000)
	private String justificativa;

	@Column(name = "justificativachefe", length = 2000)
	private String justificativachefe;

	@Column(name = "aprovado")
	private String aprovado;

	@JoinColumn(name = "pais_id", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Pais paisId;

	@JoinColumn(name = "cidade_id", referencedColumnName = "ID")
	@ManyToOne
	@JsonIgnore
	private Municipio municipio;

	@Transient
	@JsonIgnore
	private UfMunicipio ufMunicipio;

	@Column(name = "data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;

	public EventoRecomendar() {
	}

	public EventoRecomendar(Long id) {
		this.id = id;
	}

	public EventoRecomendar(Long id, String cargaHoraria, Date dataFim,
			Date dataInicio, byte[] material, String materialTipo,
			String materialNome, String objetivo, String observacao,
			String titulo, BigDecimal valor, Setor setorId,
			String justificativa, String aprovado,
			Pais paisId, Municipio municipio,
			UfMunicipio ufMunicipio, Date dataCadastro) {
		super();
		this.id = id;
		this.cargaHoraria = cargaHoraria;
		this.dataFim = dataFim;
		this.dataInicio = dataInicio;
		this.material = material;
		this.materialTipo = materialTipo;
		this.materialNome = materialNome;
		this.objetivo = objetivo;
		this.observacao = observacao;
		this.titulo = titulo;
		this.valor = valor;
		this.setorId = setorId;
		this.justificativa = justificativa;
		this.aprovado = aprovado;
		this.paisId = paisId;
		this.municipio = municipio;
		this.ufMunicipio = ufMunicipio;
		this.dataCadastro = dataCadastro;
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

	public byte[] getMaterial() {
		return material;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Setor getSetorId() {
		return setorId;
	}

	public void setSetorId(Setor setorId) {
		this.setorId = setorId;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public String getAprovado() {
		return aprovado;
	}

	public void setAprovado(String aprovado) {
		this.aprovado = aprovado;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {

		if (!(object instanceof EventoRecomendar)) {
			return false;
		}
		EventoRecomendar other = (EventoRecomendar) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.com.siged.entidades.EventoRecomendar[id=" + id + "]";
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

	public void setMaterial(byte[] material) {
		this.material = material;
	}

	public String getJustificativachefe() {
		return justificativachefe;
	}

	public void setJustificativachefe(String justificativachefe) {
		this.justificativachefe = justificativachefe;
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

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

}
