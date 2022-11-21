package br.com.siged.entidades;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.siged.entidades.externas.Municipio;
import br.com.siged.entidades.externas.UfMunicipio;

/**
 * 
 * @author Marcelo
 */
@Entity
@Table(name = "evento")
@NamedQueries({
		@NamedQuery(name = "Evento.findAll", query = "SELECT e FROM Evento e ORDER BY e.dataInicioPrevisto DESC"),
		/*
		 * DONE: __Modalidade Evento: Definir qual será a Modalidade do Evento na consulta (HQL)
		 * @deprecated Consulta substituída para verificar se o evento é presencial por meio do módulo
		@NamedQuery(name = "Evento.findPresencial", query = "SELECT e FROM Evento e WHERE e.modalidadeId.id = 1 ORDER BY e.dataInicioPrevisto DESC"),
		 */
		@NamedQuery(name = "Evento.findPresencial", query = "SELECT distinct e FROM Evento e inner join e.moduloList m WHERE m.modalidade = 1 ORDER BY e.dataInicioPrevisto DESC"),
		@NamedQuery(name = "Evento.findById", query = "SELECT e FROM Evento e WHERE e.id = :id"),
		@NamedQuery(name = "Evento.findEventosApurados", query = "SELECT e FROM Evento e WHERE e.id in (SELECT d.eventoId.id FROM Desempenho d) AND e.permiteCertificado = 'S' OR e.id = 481 ORDER BY e.dataInicioPrevisto DESC"),
		@NamedQuery(name = "Evento.findByNaoPrevisto", query = "SELECT e FROM Evento e WHERE e.publicado='S' and (e.dataInicioRealizacao <= trunc(current_date(),'DDD')) ORDER BY e.titulo"),
		/*
		 * DONE: __Modalidade Evento: Definir qual será a Modalidade do Evento na consulta (HQL)
		 * @deprecated Consulta substituída para verificar se o evento é presencial por meio do módulo
		@NamedQuery(name = "Evento.findPresencialByNaoPrevistoComModulo", query = "SELECT distinct e FROM Evento e inner join fetch e.moduloList WHERE e.modalidadeId.id = 1 AND e.dataInicioRealizacao <= trunc(current_date(),'DDD') ORDER BY e.dataInicioPrevisto DESC"),
		 */
		@NamedQuery(name = "Evento.findPresencialByNaoPrevistoComModulo", query = "SELECT distinct e FROM Evento e inner join fetch e.moduloList m WHERE m.modalidade = 1 AND e.dataInicioRealizacao <= trunc(current_date(),'DDD') ORDER BY e.dataInicioPrevisto DESC"),
		@NamedQuery(name = "Evento.findByNaoPrevistoComModuloTodos", query = "SELECT distinct e FROM Evento e inner join fetch e.moduloList WHERE (e.dataInicioRealizacao <= trunc(current_date(),'DDD')) ORDER BY e.dataInicioPrevisto DESC"),
		@NamedQuery(name = "Evento.findByPrevisto", query = "SELECT e FROM Evento e WHERE e.publicado='S' and (e.dataInicioRealizacao > trunc(current_date(),'DDD') or e.dataInicioRealizacao is null) and (e.dataInicioPrevisto is not null and e.dataInicioPrevisto > trunc(current_date(),'DDD')) ORDER BY e.dataInicioPrevisto DESC"),
		@NamedQuery(name = "Evento.findByEmAndamento", query = "SELECT e FROM Evento e WHERE e.publicado='S' and e.dataInicioRealizacao <= trunc(current_date(),'DDD') and e.dataFimRealizacao >= trunc(current_date(),'DDD') ORDER BY e.dataInicioPrevisto DESC"),
		@NamedQuery(name = "Evento.findByRealizado", query = "SELECT e FROM Evento e WHERE e.publicado='S' and e.dataFimRealizacao < trunc(current_date(),'DDD') ORDER BY e.dataInicioPrevisto DESC"),
		@NamedQuery(name = "Evento.findByPrevistoTodos", query = "SELECT e FROM Evento e WHERE (e.dataInicioRealizacao > trunc(current_date(),'DDD') or e.dataInicioRealizacao is null) and (e.dataInicioPrevisto is not null and e.dataInicioPrevisto > trunc(current_date(),'DDD')) ORDER BY e.dataInicioPrevisto DESC"),
		@NamedQuery(name = "Evento.findByEmAndamentoTodos", query = "SELECT e FROM Evento e WHERE e.dataInicioRealizacao <= trunc(current_date(),'DDD') and e.dataFimRealizacao >= trunc(current_date(),'DDD') ORDER BY e.dataInicioPrevisto DESC"),
		@NamedQuery(name = "Evento.findByRealizadoTodos", query = "SELECT e FROM Evento e WHERE e.dataFimRealizacao < trunc(current_date(),'DDD') ORDER BY e.dataInicioPrevisto DESC"),
		@NamedQuery(name = "Evento.findByAdministrador", query = "SELECT e FROM Evento e WHERE e.administrador = :administrador"),
		@NamedQuery(name = "Evento.findByCargaHoraria", query = "SELECT e FROM Evento e WHERE e.cargaHoraria = :cargaHoraria"),
		@NamedQuery(name = "Evento.findByConteudo", query = "SELECT e FROM Evento e WHERE e.conteudo = :conteudo"),
		@NamedQuery(name = "Evento.findByDataFimPrevisto", query = "SELECT e FROM Evento e WHERE e.dataFimPrevisto = :dataFimPrevisto"),
		@NamedQuery(name = "Evento.findByDataFimPreInscricao", query = "SELECT e FROM Evento e WHERE e.dataFimPreInscricao = :dataFimPreInscricao"),
		@NamedQuery(name = "Evento.findByDataFimRealizacao", query = "SELECT e FROM Evento e WHERE e.dataFimRealizacao = :dataFimRealizacao"),
		@NamedQuery(name = "Evento.findByDataInicioPrevisto", query = "SELECT e FROM Evento e WHERE e.dataInicioPrevisto = :dataInicioPrevisto"),
		@NamedQuery(name = "Evento.findByDataInicioPreInscricao", query = "SELECT e FROM Evento e WHERE e.dataInicioPreInscricao = :dataInicioPreInscricao"),
		@NamedQuery(name = "Evento.findByDataInicioRealizacao", query = "SELECT e FROM Evento e WHERE e.dataInicioRealizacao = :dataInicioRealizacao"),
		@NamedQuery(name = "Evento.findByIdentificador", query = "SELECT e FROM Evento e WHERE e.identificador = :identificador"),
		@NamedQuery(name = "Evento.findByObjetivoEspecifico", query = "SELECT e FROM Evento e WHERE e.objetivoEspecifico = :objetivoEspecifico"),
		@NamedQuery(name = "Evento.findByObjetivoGeral", query = "SELECT e FROM Evento e WHERE e.objetivoGeral = :objetivoGeral"),
		@NamedQuery(name = "Evento.findByObservacoes", query = "SELECT e FROM Evento e WHERE e.observacoes = :observacoes"),
		@NamedQuery(name = "Evento.findByTitulo", query = "SELECT e FROM Evento e WHERE e.titulo = :titulo"),
		@NamedQuery(name = "Evento.findByTutor1", query = "SELECT e FROM Evento e WHERE e.tutor1 = :tutor1"),
		@NamedQuery(name = "Evento.findByTutor2", query = "SELECT e FROM Evento e WHERE e.tutor2 = :tutor2"),
		@NamedQuery(name = "Evento.findByVagas", query = "SELECT e FROM Evento e WHERE e.vagas = :vagas"),
		@NamedQuery(name = "Evento.findByPublicado", query = "SELECT e FROM Evento e WHERE e.publicado = :publicado"),
		@NamedQuery(name = "Evento.findByConteudista1", query = "SELECT e FROM Evento e WHERE e.conteudista1 = :conteudista1"),
		@NamedQuery(name = "Evento.findByConteudista2", query = "SELECT e FROM Evento e WHERE e.conteudista2 = :conteudista2"),
		@NamedQuery(name = "Evento.findByDataCadastro", query = "SELECT e FROM Evento e WHERE e.dataCadastro = :dataCadastro"),
		@NamedQuery(name = "Evento.findEventoEficaciaAvaliada", query = "SELECT e FROM Evento e WHERE e.id in(SELECT a.eventoId FROM AvaliacaoEficacia a ) ORDER BY e.dataInicioPrevisto DESC"),
		@NamedQuery(name = "Evento.findByPermitePreInscricao", query = "SELECT e FROM Evento e WHERE e.permitePreInscricao = :permitePreInscricao ORDER BY e.titulo"),
		@NamedQuery(name = "Evento.findByEventoApurado", query = "SELECT e FROM Evento e WHERE e.id in (SELECT DISTINCT d.eventoId.id FROM Desempenho d) AND e.id NOT IN (SELECT DISTINCT d.eventoId.id FROM Desempenho d WHERE d.parcial = true) AND e.id = :id"),
		@NamedQuery(name = "Evento.findByEventoApuradoParcial", query = "SELECT e FROM Evento e WHERE e.id IN (SELECT DISTINCT d.eventoId.id FROM Desempenho d WHERE d.parcial = true) AND e.id = :id"),
		@NamedQuery(name = "Evento.findEventosAvaliacoes", query = "SELECT DISTINCT e FROM Evento e INNER JOIN e.moduloList m WHERE (SYSDATE > e.dataFimRealizacao + 1 OR SYSDATE > m.dataFim + 1) ORDER BY e.dataInicioPrevisto DESC "),
		@NamedQuery(name = "Evento.findEventosGastos", query = "SELECT e FROM Evento e WHERE e.id IN (SELECT DISTINCT g.eventoId.id FROM Gasto g) ORDER BY e.dataInicioPrevisto DESC"),
		@NamedQuery(name = "Evento.findEventosComCertificadosEmitidos", query = "SELECT e FROM Evento e WHERE e.id IN (SELECT DISTINCT ce.evento.id FROM CertificadoEmitido ce) ORDER BY e.dataInicioPrevisto DESC") })
@JsonIgnoreProperties(value = { "modalidadeId", "provedorId", "areaTribunalId",
		"tipoId", "localizacaoId", "responsavelEvento", "eixoTematicoId", "programas",
		"publicoAlvoList", "moduloList", "inscricaoList", "materialList",
		"eventoHistoricoList", "avaliacaoEficaciaList",
		"certificadoList", "gastoList", "provedores", "interessados"})
@SequenceGenerator(name = "sequence", sequenceName = "seq_evento", allocationSize = 1)
public class Evento implements Serializable {

	private static final long serialVersionUID = 1L;

	Calendar cal = Calendar.getInstance();
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
	@Column(name = "id")
	private Long id;

	@Column(name = "administrador")
	private String administrador;

	@NotEmpty(message = " Campo Obrigatório")
	@Column(name = "carga_horaria")
	private String cargaHoraria;

	@NotEmpty(message = " Campo Obrigatório")
	@Column(name = "conteudo")
	private String conteudo;

	@NotNull(message = " Campo Obrigatório")
	@Column(name = "data_fim_previsto")
	@Temporal(TemporalType.DATE)
	private Date dataFimPrevisto;

	@Column(name = "data_fim_pre_inscricao")
	@Temporal(TemporalType.DATE)
	private Date dataFimPreInscricao;

	@Column(name = "data_fim_realizacao")
	@Temporal(TemporalType.DATE)
	private Date dataFimRealizacao;

	@NotNull(message = " Campo Obrigatório")
	@Column(name = "data_inicio_previsto")
	@Temporal(TemporalType.DATE)
	private Date dataInicioPrevisto;

	@Column(name = "data_inicio_pre_inscricao")
	@Temporal(TemporalType.DATE)
	private Date dataInicioPreInscricao;

	@Column(name = "data_inicio_realizacao")
	@Temporal(TemporalType.DATE)
	private Date dataInicioRealizacao;

	@Column(name = "identificador")
	private String identificador;

	@Column(name = "objetivo_especifico")
	private String objetivoEspecifico;

	@NotEmpty(message = " Campo Obrigatório")
	@Column(name = "objetivo_geral")
	private String objetivoGeral;

	@Column(name = "observacoes", length = 2000)
	@Basic(fetch = FetchType.LAZY)
	private String observacoes;
	
	@Column(name = "observacoes_publicas", length = 2000)
	@Basic(fetch = FetchType.LAZY)
	private String observacoesPublicas;

	@NotEmpty(message = " Campo Obrigatório")
	@Column(name = "titulo")
	private String titulo;

	@Column(name = "tutor1")
	private String tutor1;

	@Column(name = "tutor2")
	private String tutor2;

	@NotEmpty(message = " Campo Obrigatório")
	@Column(name = "vagas")
	private String vagas;

	@NotEmpty(message = " Campo Obrigatório")
	@Column(name = "publicado")
	private String publicado;

	@Column(name = "conteudista1")
	private String conteudista1;

	@Column(name = "conteudista2")
	private String conteudista2;
	
	@NotNull
	@Column(name = "modulo_unico")
	private String moduloUnico;

	@Column(name = "resultado_esperado", length = 2000)
	@Basic(fetch = FetchType.LAZY)
	private String resultadoEsperado;
	
	@Column(name = "data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;
	
	
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
	
//	@Column(name = "imagem")
//	@Basic(fetch = FetchType.LAZY)
//	@JsonIgnore
//	private byte[] imagem;
//
//	@Column(name = "imagem_tipo")
//	private String imagemTipo;
//
//	@Column(name = "imagem_nome")
//	private String imagemNome;

	@Column(name = "permite_pre_inscricao")
	private String permitePreInscricao;
	
	/*
	@NotNull(message = " Campo Obrigatório")
	@JoinColumn(name = "modalidade_id", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Modalidade modalidade;
	
	*/
	@Deprecated
	//@NotNull(message = " Campo Obrigatório")
	@JoinColumn(name = "modalidade_id", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Modalidade modalidadeId;
	

	//@NotNull(message = " Campo Obrigatório")
	@JoinColumn(name = "provedor_id", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private ProvedorEvento provedorId;
	
	
	@NotNull(message = " Campo Obrigatório")
	@ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinTable(name = "evento_provedor_join", 
				joinColumns = { @JoinColumn(nullable = false, name = "evento_id", referencedColumnName = "id") }, 
				inverseJoinColumns = { @JoinColumn(nullable = false, name = "provedor_id", referencedColumnName = "id") })
	private List<ProvedorEvento> provedores;
	
	@NotNull(message = " Campo Obrigatório")
	@JoinColumn(name = "area_tribunal_id", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private TipoAreaTribunal areaTribunalId;

	@NotNull(message = " Campo Obrigatório")
	@JoinColumn(name = "tipo_id", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private TipoEvento tipoId;

	/*
	@Deprecated
	//@NotNull(message = " Campo Obrigatório")
	@JoinColumn(name = "localizacao_id", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private TipoLocalizacaoEvento localizacaoId;
	*/	
	@NotNull(message=" Campo Obrigatório")
	@JoinColumn(name = "localizacao_id", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY) 
	@JsonIgnore
	private TipoLocalizacaoEvento localizacaoId;
	
	@NotNull(message = " Campo Obrigatório")
	@JoinColumn(name = "responsavel_evento", referencedColumnName = "id")
	@ManyToOne
	@JsonIgnore
	@Fetch(FetchMode.SELECT)
	private Participante responsavelEvento;

	@NotNull(message = " Campo Obrigatório")
	@JoinColumn(name = "eixo_tematico_id", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private EixoTematico eixoTematicoId;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinTable(name = "evento_programa",
			joinColumns = { @JoinColumn(nullable = false, name = "evento_id", referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(nullable = false, name = "programa_id", referencedColumnName = "id") })
	private List<Programa> programas;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinTable(name = "evento_interessado",
			joinColumns = { @JoinColumn(nullable = false, name = "evento_id", referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(nullable = false, name = "participante_id", referencedColumnName = "id") })
	private List<Participante> interessados;

	@NotNull(message = " Campo Obrigatório")
	@ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinTable(name = "evento_tipopublico", joinColumns = { @JoinColumn(nullable = false, name = "evento_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(nullable = false, name = "publico_alvo_id", referencedColumnName = "id") })
	private List<TipoPublicoAlvo> publicoAlvoList;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "eventoId")
	@OrderBy("dataInicio, id")
	private List<Modulo> moduloList;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "eventoId")
	private List<Inscricao> inscricaoList;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "eventoId")
	private List<EventoHistorico> eventoHistoricoList;

	/*@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "eventoId")
	private List<Avaliacao> avaliacaoList;
	*/

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "eventoId")
	private List<AvaliacaoEficacia> avaliacaoEficaciaList;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "eventoId")
	private List<Certificado> certificadoList;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "eventoId")
	private List<Gasto> gastoList;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "eventoId")
	private List<EventoMaterial> materialList;
	
	@Column(name = "certificado_personalizado")
	private String certificadoPersonalizado;
	
	@Column(name = "permite_certificado")
	private String permiteCertificado;

	@Column(name = "link_evento")
	//@Transient
	private String linkEvento;
	
	@Column(name = "link_gravacao")
	//@Transient
	private String linkGravacao;
	
	@Transient
	private String aprovado;

	@Transient
	private String avaliado;

	@Transient
	private String desempenho;
	
	@Transient
	private String frequenciaParticipanteNoEvento;
	
	@Transient
	private String notaParticipanteNoEvento;
	
	@Transient
	private Boolean isRestrito = false;

	public Evento() {
	}

	public Evento(Long id) {
		this.id = id;
	}
	
	public Evento(Calendar cal, Long id, String administrador,
			String cargaHoraria, String conteudo, Date dataFimPrevisto,
			Date dataFimPreInscricao, Date dataFimRealizacao,
			Date dataInicioPrevisto, Date dataInicioPreInscricao,
			Date dataInicioRealizacao, String identificador,
			String objetivoEspecifico, String objetivoGeral,
			String observacoes, String titulo, String tutor1, String tutor2,
			String vagas, String publicado, String conteudista1,
			String conteudista2, Date dataCadastro, String permitePreInscricao,
			String permiteCertificado, Modalidade modalidadeId,
			ProvedorEvento provedorId, TipoAreaTribunal areaTribunalId,
			EixoTematico eixoTematicoId, TipoEvento tipoId,
			Participante responsavelEvento, String resultadoEsperado,
			TipoLocalizacaoEvento localizacaoId,
			List<TipoPublicoAlvo> publicoAlvoList, List<Modulo> moduloList,
			List<Inscricao> inscricaoList, List<EventoMaterial> materialList, List<ProvedorEvento> provedores) {
		super();
		this.cal = cal;
		this.id = id;
		this.administrador = administrador;
		this.cargaHoraria = cargaHoraria;
		this.conteudo = conteudo;
		this.dataFimPrevisto = dataFimPrevisto;
		this.dataFimPreInscricao = dataFimPreInscricao;
		this.dataFimRealizacao = dataFimRealizacao;
		this.dataInicioPrevisto = dataInicioPrevisto;
		this.dataInicioPreInscricao = dataInicioPreInscricao;
		this.dataInicioRealizacao = dataInicioRealizacao;
		this.identificador = identificador;
		this.objetivoEspecifico = objetivoEspecifico;
		this.objetivoGeral = objetivoGeral;
		this.observacoes = observacoes;
		this.titulo = titulo;
		this.tutor1 = tutor1;
		this.tutor2 = tutor2;
		this.vagas = vagas;
		this.publicado = publicado;
		this.conteudista1 = conteudista1;
		this.conteudista2 = conteudista2;
		this.dataCadastro = dataCadastro;
//		this.imagem = imagem;
//		this.imagemTipo = imagemTipo;
//		this.imagemNome = imagemNome;
		this.permitePreInscricao = permitePreInscricao;
		this.permiteCertificado = permiteCertificado;
		this.modalidadeId = null;
		this.provedorId = provedorId;
		this.areaTribunalId = areaTribunalId;
		this.tipoId = tipoId;
		this.responsavelEvento = responsavelEvento;
		this.resultadoEsperado = resultadoEsperado;
		this.eixoTematicoId = eixoTematicoId;
		this.localizacaoId = null;
		this.publicoAlvoList = publicoAlvoList;
		this.moduloList = moduloList;
		this.inscricaoList = inscricaoList;
		this.materialList = materialList;
		this.provedores = provedores;
	}


	public Evento(Calendar cal, Long id, String administrador,
			String cargaHoraria, String conteudo, Date dataFimPrevisto,
			Date dataFimPreInscricao, Date dataFimRealizacao, 
			Date dataInicioPrevisto, Date dataInicioPreInscricao,
			Date dataInicioRealizacao, String identificador,
			String objetivoEspecifico, String objetivoGeral,
			String observacoes, String titulo, String tutor1, String tutor2,
			String vagas, String cidadePais, String publicado, String conteudista1,
			String conteudista2, Date dataCadastro, String permitePreInscricao,
			String permiteCertificado, Modalidade modalidadeId,
			ProvedorEvento provedorId, TipoAreaTribunal areaTribunalId,
			EixoTematico eixoTematicoId, TipoEvento tipoId,
			Participante responsavelEvento, String resultadoEsperado,
			TipoLocalizacaoEvento localizacaoId,
			List<TipoPublicoAlvo> publicoAlvoList, List<Modulo> moduloList,
			List<Inscricao> inscricaoList, List<EventoMaterial> materialList, List<ProvedorEvento> provedores) {
		super();
		this.cal = cal;
		this.id = id;
		this.administrador = administrador;
		this.cargaHoraria = cargaHoraria;
		this.conteudo = conteudo;
		this.dataFimPrevisto = dataFimPrevisto;
		this.dataFimPreInscricao = dataFimPreInscricao;
		this.dataFimRealizacao = dataFimRealizacao;
		this.dataInicioPrevisto = dataInicioPrevisto;
		this.dataInicioPreInscricao = dataInicioPreInscricao;
		this.dataInicioRealizacao = dataInicioRealizacao;
		this.identificador = identificador;
		this.objetivoEspecifico = objetivoEspecifico;
		this.objetivoGeral = objetivoGeral;
		this.observacoes = observacoes;
		this.titulo = titulo;
		this.tutor1 = tutor1;
		this.tutor2 = tutor2;
		this.vagas = vagas;
		this.cidadePais = cidadePais;
		this.publicado = publicado;
		this.conteudista1 = conteudista1;
		this.conteudista2 = conteudista2;
		this.dataCadastro = dataCadastro;
//		this.imagem = imagem;
//		this.imagemTipo = imagemTipo;
//		this.imagemNome = imagemNome;
		this.permitePreInscricao = permitePreInscricao;
		this.permiteCertificado = permiteCertificado;
	//	this.modalidadeId = null;
		this.provedorId = provedorId;
		this.areaTribunalId = areaTribunalId;
		this.tipoId = tipoId;
		this.responsavelEvento = responsavelEvento;
		this.resultadoEsperado = resultadoEsperado;
		this.eixoTematicoId = eixoTematicoId;
		//this.localizacaoId = null;
		this.localizacaoId = localizacaoId;
		this.publicoAlvoList = publicoAlvoList;
		this.moduloList = moduloList;
		this.inscricaoList = inscricaoList;
		this.materialList = materialList;
		this.provedores = provedores;
	}

	public List<Gasto> getGastoList() {
		return gastoList;
	}

	public void setGastoList(List<Gasto> gastoList) {
		this.gastoList = gastoList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAdministrador() {
		return administrador;
	}

	public void setAdministrador(String administrador) {
		this.administrador = administrador;
	}

	public String getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(String cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Date getDataFimPrevisto() {
		return dataFimPrevisto;
	}

	public void setDataFimPrevisto(Date dataFimPrevisto) {
		this.dataFimPrevisto = dataFimPrevisto;
	}

	public Date getDataFimPreInscricao() {
		return dataFimPreInscricao;
	}

	public void setDataFimPreInscricao(Date dataFimPreInscricao) {
		this.dataFimPreInscricao = dataFimPreInscricao;
	}

	public Date getDataFimRealizacao() {
		return dataFimRealizacao;
	}

	public void setDataFimRealizacao(Date dataFimRealizacao) {
		this.dataFimRealizacao = dataFimRealizacao;
	}

	public Date getDataInicioPrevisto() {
		return dataInicioPrevisto;
	}

	public void setDataInicioPrevisto(Date dataInicioPrevisto) {
		this.dataInicioPrevisto = dataInicioPrevisto;
	}

	public Date getDataInicioPreInscricao() {
		return dataInicioPreInscricao;
	}

	public void setDataInicioPreInscricao(Date dataInicioPreInscricao) {
		this.dataInicioPreInscricao = dataInicioPreInscricao;
	}

	public Date getDataInicioRealizacao() {
		return dataInicioRealizacao;
	}

	public void setDataInicioRealizacao(Date dataInicioRealizacao) {
		this.dataInicioRealizacao = dataInicioRealizacao;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getObjetivoEspecifico() {
		return objetivoEspecifico;
	}

	public void setObjetivoEspecifico(String objetivoEspecifico) {
		this.objetivoEspecifico = objetivoEspecifico;
	}

	public String getObjetivoGeral() {
		return objetivoGeral;
	}

	public void setObjetivoGeral(String objetivoGeral) {
		this.objetivoGeral = objetivoGeral;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
	
	public String getObservacoesPublicas() {
		return observacoesPublicas;
	}

	public void setObservacoesPublicas(String observacoesPublicas) {
		this.observacoesPublicas = observacoesPublicas;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getNome() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		return tipoId + " " + titulo + " ("
				+ formato.format(dataInicioPrevisto) + " a "
				+ formato.format(dataFimPrevisto) + ")";
	}

	public String getNome2() {
		return tipoId + " " + titulo;
	}

	public String getTutor1() {
		return tutor1;
	}

	public void setTutor1(String tutor1) {
		this.tutor1 = tutor1;
	}

	public String getTutor2() {
		return tutor2;
	}

	public void setTutor2(String tutor2) {
		this.tutor2 = tutor2;
	}

	public String getVagas() {
		return this.vagas;
	}

	public void setVagas(String vagas) {
		this.vagas = vagas;
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
	
	public String getCidadePais() {
	    	return cidadePais;
	}
	
	public void setCidadePais(String cidadePais) {
	    	this.cidadePais = cidadePais;
	}

	public String getPublicado() {
		return publicado;
	}

	public void setPublicado(String publicado) {
		this.publicado = publicado;
	}
	/*
	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}
	*/
	public String getConteudista1() {
		return conteudista1;
	}

	public void setConteudista1(String conteudista1) {
		this.conteudista1 = conteudista1;
	}

	public String getConteudista2() {
		return conteudista2;
	}

	public void setConteudista2(String conteudista2) {
		this.conteudista2 = conteudista2;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}	

	public String getPermitePreInscricao() {
		return permitePreInscricao;
	}

	public void setPermitePreInscricao(String permitePreInscricao) {
		this.permitePreInscricao = permitePreInscricao;
	}

	public String getPermiteCertificado() {
		return permiteCertificado;
	}

	public void setPermiteCertificado(String permiteCertificado) {
		this.permiteCertificado = permiteCertificado;
	}
	
	/**
	 * Depreciado desde 07/12/2018. Campo foi transferido para Módulo.<br/>
	 * Usar {@link #getModalidadeModulosLista()} para retornar como String<br/> 
	 * Usar {@link #isEAD()} ou {@link #isPresencial()} para verificar se é Presencial ou EAD
	 */
	
	
	@Deprecated
	public Modalidade getModalidadeId() {
		return modalidadeId;
	}

	/**
	 * Depreciado desde 07/12/2018. Campo foi transferido para Módulo.
	 */
	@Deprecated
	public void setModalidadeId(Modalidade modalidadeId) {
		this.modalidadeId = modalidadeId;
	}
	
	public ProvedorEvento getProvedorId() {
		return provedorId;
	}

	public void setProvedorId(ProvedorEvento provedorId) {
		this.provedorId = provedorId;
	}
	
	@JsonIgnore
	public List<ProvedorEvento> getProvedores() {
		return provedores;
	}

	@JsonIgnore
	public void setProvedores(List<ProvedorEvento> provedores) {
		this.provedores = provedores;
	}
	
	@JsonIgnore
	public List<Participante> getInteressados() {
		return interessados;
	}

	@JsonIgnore
	public void setInteressados(List<Participante> interessados) {
		this.interessados = interessados;
	}

	public String getProvedoresLista() {
		String aux = "";
		for (ProvedorEvento provedor : this.provedores) {
			aux = aux + provedor.getDescricao() + ", ";
		}
		int tamanho = aux.length() - 2;
		if (tamanho == -2) {
			tamanho = 0;
		}
		return aux.substring(0, tamanho);
		
	}

	public TipoAreaTribunal getAreaTribunalId() {
		return areaTribunalId;
	}

	public void setAreaTribunalId(TipoAreaTribunal areaTribunalId) {
		this.areaTribunalId = areaTribunalId;
	}

	public TipoEvento getTipoId() {
		return tipoId;
	}

	public void setTipoId(TipoEvento tipoId) {
		this.tipoId = tipoId;
	}

	public Participante getResponsavelEvento() {
		return responsavelEvento;
	}

	public void setResponsavelEvento(Participante responsavelEvento) {
		this.responsavelEvento = responsavelEvento;
	}

	public String getResultadoEsperado() {
		return resultadoEsperado;
	}

	public void setResultadoEsperado(String resultadoEsperado) {
		this.resultadoEsperado = resultadoEsperado;
	}

	public EixoTematico getEixoTematicoId() {
		return eixoTematicoId;
	}

	public void setEixoTematicoId(EixoTematico eixoTematicoId) {
		this.eixoTematicoId = eixoTematicoId;
	}

	@JsonIgnore
	public List<Programa> getProgramas() {
		return programas;
	}

	@JsonIgnore
	public void setProgramas(List<Programa> programas) {
		this.programas = programas;
	}
	
	public String getProgramaLista() {
		String aux = "";
		for (Programa programa : this.programas) {
			aux = aux + programa.getDescricao() + ", ";
		}
		int tamanho = aux.length() - 2;
		if (tamanho == -2) {
			tamanho = 0;
		}
		return aux.substring(0, tamanho);
		
	}

	/**
	 * Depreciado desde 07/12/2018. Campo foi centralizado em Módulo.<br/>
	 * Usar {@link #getLocalizacaoModulosLista()}
	 */
	@Deprecated
	public TipoLocalizacaoEvento getLocalizacaoId() {
		return localizacaoId;
	}

	/**
	 * Depreciado desde 07/12/2018. Campo foi centralizado em Módulo.
	 */
	@Deprecated
	public void setLocalizacaoId(TipoLocalizacaoEvento localizacaoId) {
		this.localizacaoId = localizacaoId;
	}

	@JsonIgnore
	public List<EventoHistorico> getEventoHistoricoList() {
		return eventoHistoricoList;
	}

	@JsonIgnore
	public void setEventoHistoricoList(List<EventoHistorico> eventoHistoricoList) {
		this.eventoHistoricoList = eventoHistoricoList;
	}

	@JsonIgnore
	public List<Modulo> getModuloList() {
		return moduloList;
	}

	@JsonIgnore
	public void setModuloList(List<Modulo> moduloList) {
		this.moduloList = moduloList;
	}

	public String getCertificadoPersonalizadoName() {
		return certificadoPersonalizado;
	}

	public void setCertificadoPersonalizadoName(String certificadoPersonalizado) {
		this.certificadoPersonalizado = certificadoPersonalizado;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {

		if (!(object instanceof Evento)) {
			return false;
		}
		Evento other = (Evento) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		return tipoId + " " + titulo + " ("
				+ formato.format(dataInicioPrevisto) + " a "
				+ formato.format(dataFimPrevisto) + ")";
	}
	
//	public byte[] getImagem() {
//		return imagem;
//	}
//
//	public void setImagem(MultipartFile imagem) throws IOException {
//		this.imagem = imagem.getBytes();
//	}
//	
//	public void setImagem(byte[] imagem) {
//		this.imagem = imagem;
//	}
//	
//	public String getImagemTipo() {
//		return imagemTipo;
//	}
//
//	public void setImagemTipo(String imagemTipo) {
//		this.imagemTipo = imagemTipo;
//	}
//
//	public String getImagemNome() {
//		return imagemNome;
//	}
//
//	public void setImagemNome(String imagemNome) {
//		this.imagemNome = imagemNome;
//	}

	public List<TipoPublicoAlvo> getPublicoAlvoList() {
		return publicoAlvoList;
	}

	public String getPublicoAlvoLista() {
		String aux = "";
		for (TipoPublicoAlvo pa : publicoAlvoList) {
			aux = aux + pa.getDescricao() + ", ";
		}
		int tamanho = aux.length() - 2;
		if (tamanho == -2) {
			tamanho = 0;
		}
		return aux.substring(0, tamanho);
	}
	
	public String getInteressadosLista() {
		String aux = "";
		for (Participante pa : interessados) {
			aux = aux + pa. getNomeCpf() + ", ";
		}
		int tamanho = aux.length() - 2;
		if (tamanho == -2) {
			tamanho = 0;
		}
		return aux.substring(0, tamanho);
	}
	
	public String getModalidadeModulosLista() {
		String aux = "";
		List<Long> adicionados = new ArrayList<>();
		for(Modulo modulo : moduloList) {
			if(modulo.getModalidade() != null) {
				if(!adicionados.contains(modulo.getModalidade().getId())) {
					aux += modulo.getModalidade().getDescricao()  + ", ";
					adicionados.add(modulo.getModalidade().getId());
				}	
			}
		}
		
		int tamanho = aux.length() - 2;
		if (tamanho == -2) {
			tamanho = 0;
		}
		return aux.substring(0, tamanho);
	}
	
	public String getLocalizacaoModulosLista() {
		String aux = "";
		List<Long> adicionados = new ArrayList<>();
		for(Modulo modulo : moduloList) {
			if(modulo.getLocalizacaoId() != null) {
				if(!adicionados.contains(modulo.getLocalizacaoId().getId())) {
					aux += modulo.getLocalizacaoComCidadeEUF() + "; ";
					adicionados.add(modulo.getLocalizacaoId().getId());
				}				
			}
		}
		
		int tamanho = aux.length() - 2;
		if (tamanho == -2) {
			tamanho = 0;
		}
		return aux.substring(0, tamanho);
	}

	public void setPublicoAlvoList(List<TipoPublicoAlvo> publicoAlvoList) {
		this.publicoAlvoList = publicoAlvoList;
	}

	public List<Inscricao> getInscricaoList() {
		return inscricaoList;
	}

	public void setInscricaoList(List<Inscricao> inscricaoList) {
		this.inscricaoList = inscricaoList;
	}

	public String getModuloUnico() {
		return moduloUnico;
	}

	public void setModuloUnico(String moduloUnico) {
		this.moduloUnico = moduloUnico;
	}

	public String getAprovado() {
		return aprovado;
	}

	public void setAprovado(String aprovado) {
		this.aprovado = aprovado;
	}

	public String getAvaliado() {
		return avaliado;
	}

	public void setAvaliado(String avaliado) {
		this.avaliado = avaliado;
	}

	public String getDesempenho() {
		return desempenho;
	}

	public void setDesempenho(String desempenho) {
		this.desempenho = desempenho;
	}

	public String getFrequenciaParticipanteNoEvento() {
		return frequenciaParticipanteNoEvento;
	}

	public void setFrequenciaParticipanteNoEvento(String frequenciaParticipanteNoEvento) {
		this.frequenciaParticipanteNoEvento = frequenciaParticipanteNoEvento;
	}

	public String getNotaParticipanteNoEvento() {
		return notaParticipanteNoEvento;
	}

	public void setNotaParticipanteNoEvento(String notaParticipanteNoEvento) {
		this.notaParticipanteNoEvento = notaParticipanteNoEvento;
	}
	
	public Boolean getIsRestrito() {
		if(interessados != null && interessados.size() > 0) {
			return true;
		}
		return isRestrito;
	}

	public void setIsRestrito(Boolean isRestrito) {
		if(interessados != null && interessados.size() > 0) {
			this.isRestrito = true;
		} else {
			this.isRestrito = isRestrito;
		}
	}

	/*public List<Avaliacao> getAvaliacaoList() {
		return avaliacaoList;
	}

	public void setAvaliacaoList(List<Avaliacao> avaliacaoList) {
		this.avaliacaoList = avaliacaoList;
	}*/

	public List<AvaliacaoEficacia> getAvaliacaoEficaciaList() {
		return avaliacaoEficaciaList;
	}

	public void setAvaliacaoEficaciaList(
			List<AvaliacaoEficacia> avaliacaoEficaciaList) {
		this.avaliacaoEficaciaList = avaliacaoEficaciaList;
	}

	public List<Certificado> getCertificadoList() {
		return certificadoList;
	}

	public void setCertificadoList(List<Certificado> certificadoList) {
		this.certificadoList = certificadoList;
	}
	
	public boolean hasCertificadoTerceiro(Participante participante) {
		for(Certificado certificado : certificadoList) {
			if(certificado != null && certificado.getParticipanteId().equals(participante))
				return true;
		}
		return false;
	}
	
	public boolean isEventoComParticipante() {		

		for (Inscricao inscricao : inscricaoList)
			if (inscricao.getConfirmada().equals("S"))
				return true;			

		return false;
	}
	
	public boolean isEventoComMaterialDivulgacao() {		
		
		for (EventoMaterial material : this.materialList) 
			if(material.getMaterialTipo() == 2L)
				return true;		
		
		return false;
	}
	
	public boolean isEventoComMaterialDidatico() {	
		
		for (EventoMaterial material : this.materialList)
			if(material.getMaterialTipo() == 1L)
				return true;		
		
		return false;
	}
	
	public boolean isParticipanteInscricaoConfirmada(Participante participante) {
		if(participante == null) {
			return false;
		}
		for (Inscricao inscricao : this.inscricaoList) {
			if(inscricao.getParticipanteId() != null && inscricao.getParticipanteId().getId().equals(participante.getId())){
				if(inscricao.getConfirmada() != null && inscricao.getConfirmada().toUpperCase().equals("S")){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isEAD() {
		return this.moduloList != null && this.moduloList.size() > 0 && !this.temModuloPresencial();
	}
	
	public boolean isPresencial() {
		return this.temModuloPresencial();
	}
	
	public boolean temModuloEAD() {
		for(Modulo modulo : moduloList) {
			if(modulo.getModalidade() != null && modulo.getModalidade().isEAD()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean temModuloPresencial() {
		for(Modulo modulo : moduloList) {
			if(modulo.getModalidade() != null && modulo.getModalidade().isPresencial()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean temProvedoresCertificado() {
		if (this.provedores == null) {
			return false;
		}
		for (ProvedorEvento provedor : this.provedores) {
			if (ProvedorEvento.PROVEDORES_CERTIFICADO.contains(provedor.getId())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean temProvedorIPC() {
		if(this.provedores != null) {
			for(ProvedorEvento provedor : this.provedores) {
				if(provedor.isIPC())
					return true;
			}
		}
		return false;
	}
	
	public boolean temProvedorRedeEscolas() {
		if(this.provedores != null) {
			for(ProvedorEvento provedor : this.provedores) {
				if(provedor.isRedeEscolas())
					return true;
			}
		}
		return false;
	}
	
	public boolean temProvedorIRB() {
		if(this.provedores != null) {
			for(ProvedorEvento provedor : this.provedores) {
				if(provedor.isIRB())
					return true;
			}
		}
		return false;
	}
	
	
	public boolean somenteProvedorIPC() {
		if(this.provedores != null && this.provedores.size() == 1) {
			if(this.provedores.get(0).isIPC()){
				return true;
			}
		}
		return false;
	}
	
	public boolean somenteProvedorRedeEscolas() {
		if(this.provedores != null && this.provedores.size() == 1) {
			if(this.provedores.get(0).isRedeEscolas()){
				return true;
			}
		}
		return false;
	}
	
	public List<Instrutor> getInstrutoresComAssinatura() {
		List<Instrutor> instrutorComAssinaturaList = new ArrayList<Instrutor>();
		
		for(Instrutor instrutor : this.getInstrutores()) {
			if(instrutor.getAssinatura() != null) {
				instrutorComAssinaturaList.add(instrutor);
			}
		}
		
		return instrutorComAssinaturaList;
	}
	
	public List<Instrutor> getInstrutores() {
		List<Instrutor> instrutorList = new ArrayList<Instrutor>();
		for (Modulo m : this.getModuloList()) {
			for (Instrutor i : m.getInstrutorList()) {
				if (!instrutorList.contains(i)) {
					instrutorList.add(i);
				}
			}
		}
		
		return instrutorList;
	}
	
	public List<EventoMaterial> getMaterialList() {
		return materialList;
	}

	public void setMaterialList(List<EventoMaterial> materialList) {
		this.materialList = materialList;
	}
	
	public List<EventoMaterial> getMaterialDivulgacaoList() {
		
		List<EventoMaterial> materialDivulgacaoList = new ArrayList<EventoMaterial>();
		
		for (EventoMaterial material : this.materialList) {
			if(material.getMaterialTipo() == 2)
				materialDivulgacaoList.add(material);
		}
		
		return materialDivulgacaoList;
	}
	
	public boolean isCertificadoPersonalizado(){
		if(this.certificadoPersonalizado != null && !this.certificadoPersonalizado.equals("")){
			return true;
		}
		
		return false;
	}
	
	public Integer getTotalVagas() {
		return Integer.valueOf(this.vagas.replace(".", ""));
	}
	
	public double getTotalVagasMargem() {
		return this.getTotalVagas() * 1.1;
	}
	
	public int getTotalInscricoesConfirmadas() {
		Set<Long> participantesIds = new HashSet<>();
		for(Inscricao inscricao : this.inscricaoList) {
			if(inscricao.getConfirmada().equals("S"))
				participantesIds.add(inscricao.getParticipanteId().getId());
		}
		
		return participantesIds.size();
	}
	
	public int getTotalInscricoesConfirmadas(Long... tiposId) {
		Set<Long> participantesIds = new HashSet<>();
		
		if(tiposId.length < 1)
			return getTotalInscricoesConfirmadas();
		
		/**
		 * Cannot modify returned list
		 */
		List<Long> tipos = Arrays.asList(tiposId);
		
		for(Inscricao inscricao : this.inscricaoList) {
			if(inscricao.getConfirmada().equals("S") 
				&& tipos.contains(inscricao.getParticipanteId().getTipoId().getId())) {
				participantesIds.add(inscricao.getParticipanteId().getId());
			}
		}
		
		return participantesIds.size();
	}
	
	public boolean inPublicoAlvo(Participante participante) {
		if(participante != null && participante.getTipoId() != null) {
			for(TipoPublicoAlvo publicoAlvo : this.publicoAlvoList) {
				if(publicoAlvo.getId().equals(participante.getTipoId().getId()))
					return true;
			}
		}
		return false;
	}
	
	public boolean inInteressados(Participante participante) {
		if(participante != null && participante.getId() != null && this.interessados != null) {
			for(Participante interessado : this.interessados) {
				if(interessado.getId().equals(participante.getId()))
					return true;
			}
		}
		return false;
	}
	
	public void setLinkEvento(String linkEvento) {
		this.linkEvento = linkEvento;
	}
	
	public String getLinkEvento() {
		return this.linkEvento;
	}
	
	public void setLinkGravacao(String linkGravacao) {
		this.linkGravacao = linkGravacao;
	}
	
	public String getLinkGravacao() {
		return this.linkGravacao;
	}
	
}
