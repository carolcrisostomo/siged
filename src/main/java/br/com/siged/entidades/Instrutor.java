
package br.com.siged.entidades;

import java.io.IOException;
import java.io.Serializable;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.AssertTrue;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import br.com.siged.entidades.externas.Municipio;
import br.com.siged.entidades.externas.Setor;
import br.com.siged.entidades.externas.UfMunicipio;

/**
 *
 * @author Marcelo
 */
@Entity
@Table(name = "instrutor")
@NamedQueries({
	@NamedQuery(name = "Instrutor.findAll", query = "SELECT i FROM Instrutor i ORDER BY i.nome"),
	@NamedQuery(name = "Instrutor.findById", query = "SELECT i FROM Instrutor i WHERE i.id = :id"),
	@NamedQuery(name = "Instrutor.findByBairro", query = "SELECT i FROM Instrutor i WHERE i.bairro = :bairro ORDER BY i.nome"),
	@NamedQuery(name = "Instrutor.findByCelular", query = "SELECT i FROM Instrutor i WHERE i.celular = :celular ORDER BY i.nome"),
	@NamedQuery(name = "Instrutor.findByCep", query = "SELECT i FROM Instrutor i WHERE i.cep = :cep ORDER BY i.nome"),
	@NamedQuery(name = "Instrutor.findByComplemento", query = "SELECT i FROM Instrutor i WHERE i.complemento = :complemento ORDER BY i.nome"),
	@NamedQuery(name = "Instrutor.findByCpf", query = "SELECT i FROM Instrutor i WHERE i.cpf = :cpf"),
	@NamedQuery(name = "Instrutor.findByDataNascimento", query = "SELECT i FROM Instrutor i WHERE i.dataNascimento = :dataNascimento ORDER BY i.nome"),
	@NamedQuery(name = "Instrutor.findByEmail", query = "SELECT i FROM Instrutor i WHERE i.email = :email ORDER BY i.nome"),
	@NamedQuery(name = "Instrutor.findByInstituicao", query = "SELECT i FROM Instrutor i WHERE i.instituicao = :instituicao ORDER BY i.nome"),
	@NamedQuery(name = "Instrutor.findByLogradouro", query = "SELECT i FROM Instrutor i WHERE i.logradouro = :logradouro ORDER BY i.nome"),
	@NamedQuery(name = "Instrutor.findByNome", query = "SELECT i FROM Instrutor i WHERE i.nome = :nome ORDER BY i.nome"),
	@NamedQuery(name = "Instrutor.findByNumero", query = "SELECT i FROM Instrutor i WHERE i.numero = :numero ORDER BY i.nome"),
	@NamedQuery(name = "Instrutor.findByObservacao", query = "SELECT i FROM Instrutor i WHERE i.observacao = :observacao ORDER BY i.nome"),
	@NamedQuery(name = "Instrutor.findBySexo", query = "SELECT i FROM Instrutor i WHERE i.sexo = :sexo ORDER BY i.nome"),
	@NamedQuery(name = "Instrutor.findByTelefone", query = "SELECT i FROM Instrutor i WHERE i.telefone = :telefone ORDER BY i.nome"),
	@NamedQuery(name = "Instrutor.findBySituacao", query = "SELECT i FROM Instrutor i WHERE i.situacaoId = :situacao ORDER BY i.nome"),
	@NamedQuery(name = "Instrutor.findByDataCadastro", query = "SELECT i FROM Instrutor i WHERE i.dataCadastro = :dataCadastro ORDER BY i.nome")})
@SequenceGenerator(name="sequence", sequenceName="seq_instrutor", allocationSize=1)
@JsonIgnoreProperties(value={"curriculo","projeto", "moduloList", "formacaoAcademicaId", "nivelEscolaridadeId", "setorId", "situacaoId", "tipoId", "assinatura","paisId","municipio","UfMunicipio"})
public class Instrutor implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final Long DIVERSOS = 50l;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
	@Column(name = "id")
	private Long id;
	
	@Column(name = "bairro")
	private String bairro;
	
	@Column(name = "celular")
	private String celular;
	
	@Column(name = "cep")
	private String cep;
	
	@Column(name = "complemento")
	private String complemento;
	
	@Column(name = "cpf")
	private String cpf;
	
	@JsonIgnore
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "curriculo") 
	private byte[] curriculo;
	
	@Column(name = "curriculo_tipo") 
	private String curriculoTipo;
	
	@Column(name = "curriculo_nome") 
	private String curriculoNome;
	
	@Column(name = "data_nascimento")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataNascimento;
	
	@NotEmpty(message=" Campo Obrigatório")
	@Column(name = "email")
	private String email;
	
	@Column(name = "instituicao")
	private String instituicao;
	
	@Column(name = "logradouro")
	private String logradouro;
	
	@NotEmpty(message=" Campo Obrigatório")    
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "numero")
	private String numero;
	
	@Column(name = "observacao", length = 2000)
	private String observacao;
	
	@JsonIgnore
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "projeto") 
	private byte[] projeto;
	
	@Column(name = "projeto_tipo") 
	private String projetoTipo;
	
	@Column(name = "projeto_nome") 
	private String projetoNome;
	
	@NotEmpty(message=" Campo Obrigatório")
	@Column(name = "sexo")
	private String sexo;
	
	@NotEmpty(message=" Campo Obrigatório")
	@Column(name = "telefone")
	private String telefone;
	
	@Column(name = "data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;
	
	@ManyToMany(mappedBy = "instrutorList") @JsonIgnore
	private List<Modulo> moduloList;
	
	
	@JoinColumn(name = "formacao_academica_id", referencedColumnName = "id")
	@ManyToOne @JsonIgnore
	private FormacaoAcademica formacaoAcademicaId;
	
	
	@JoinColumn(name = "nivel_escolaridade_id", referencedColumnName = "id")	
	@ManyToOne @JsonIgnore
	private NivelEscolaridade nivelEscolaridadeId;
	
	@JoinColumn(name = "setor_id", referencedColumnName = "IDSETOR")
	@ManyToOne @JsonIgnore
	private Setor setorId;
	
	@JoinColumn(name = "situacao_id", referencedColumnName = "id")
	@ManyToOne @JsonIgnore
	private SituacaoInstrutor situacaoId;	
	
	@JoinColumn(name = "tipo_id", referencedColumnName = "id")
	@ManyToOne(optional = false) @JsonIgnore
	private TipoInstrutor tipoId;
	
	@JsonIgnore
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "assinatura") 
	private byte[] assinatura;
	
	@Column(name = "assinatura_tipo") 
	private String assinaturaTipo;
	
	@Column(name = "assinatura_nome") 
	private String assinaturaNome;
	
	@Column(name = "perfil", length = 2000)
	private String perfil;    
	
	@JoinColumn(name = "pais_id", referencedColumnName = "id")
    @ManyToOne @JsonIgnore
    private Pais paisId;
	
	@JoinColumn(name = "cidade_id", referencedColumnName = "ID")
    @ManyToOne @JsonIgnore
    private Municipio municipio;

	@Transient @JsonIgnore
    private UfMunicipio ufMunicipio;
	
	@Transient @JsonIgnore
	private String justificativa;

	public Instrutor() {
	}

	public Instrutor(Long id) {
		this.id = id;
	}

	public Instrutor(Long id, String bairro, String celular, String cep, String complemento, String cpf, byte[] curriculo, Date dataNascimento, String email, String instituicao, String logradouro, String nome, String numero, String observacao, byte[] projeto, String sexo, String telefone, String perfil, Municipio municipio, UfMunicipio ufMunicipio, String justificativa) {
		this.id = id;
		this.bairro = bairro;
		this.celular = celular;
		this.cep = cep;
		this.complemento = complemento;
		this.cpf = cpf;
		this.curriculo = curriculo;
		this.dataNascimento = dataNascimento;
		this.email = email;
		this.instituicao = instituicao;
		this.logradouro = logradouro;
		this.nome = nome;
		this.numero = numero;
		this.observacao = observacao;
		this.projeto = projeto;
		this.sexo = sexo;
		this.telefone = telefone;
		this.perfil = perfil;
		this.municipio = municipio;
		this.ufMunicipio = ufMunicipio;
		this.justificativa = justificativa;
	}
	public Instrutor(Long id, String bairro, String celular, String cep, String complemento, String cpf, byte[] curriculo, Date dataNascimento, String email, String instituicao, String logradouro, String nome, String numero, String observacao, byte[] projeto, String sexo, String telefone, String perfil,  String justificativa) {
		this.id = id;
		this.bairro = bairro;
		this.celular = celular;
		this.cep = cep;
		this.complemento = complemento;
		this.cpf = cpf;
		this.curriculo = curriculo;
		this.dataNascimento = dataNascimento;
		this.email = email;
		this.instituicao = instituicao;
		this.logradouro = logradouro;
		this.nome = nome;
		this.numero = numero;
		this.observacao = observacao;
		this.projeto = projeto;
		this.sexo = sexo;
		this.telefone = telefone;
		this.perfil = perfil;
		this.justificativa = justificativa;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
	public Pais getPaisId() {
		return paisId;
	}
	
	public void setPaisId(Pais paisId) {
		this.paisId = paisId;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public byte[] getCurriculo() {
		return curriculo;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public byte[] getProjeto() {
		return projeto;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public List<Modulo> getModuloList() {
		return moduloList;
	}

	public void setModuloList(List<Modulo> moduloList) {
		this.moduloList = moduloList;
	}

	public FormacaoAcademica getFormacaoAcademicaId() {
		return formacaoAcademicaId;
	}

	public void setFormacaoAcademicaId(FormacaoAcademica formacaoAcademicaId) {
		this.formacaoAcademicaId = formacaoAcademicaId;
	}

	public NivelEscolaridade getNivelEscolaridadeId() {
		return nivelEscolaridadeId;
	}

	public void setNivelEscolaridadeId(NivelEscolaridade nivelEscolaridadeId) {
		this.nivelEscolaridadeId = nivelEscolaridadeId;
	}

	public Setor getSetorId() {
		return setorId;
	}

	public void setSetorId(Setor setorId) {
		this.setorId = setorId;
	}

	public SituacaoInstrutor getSituacaoId() {
		return situacaoId;
	}

	public void setSituacaoId(SituacaoInstrutor situacaoId) {
		this.situacaoId = situacaoId;
	}

	public TipoInstrutor getTipoId() {
		return tipoId;
	}

	public void setTipoId(TipoInstrutor tipoId) {
		this.tipoId = tipoId;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {

		if (!(object instanceof Instrutor)) {
			return false;
		}
		Instrutor other = (Instrutor) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return nome;
	}

	public String getCurriculoTipo() {
		return curriculoTipo;
	}

	public void setCurriculoTipo(String curriculoTipo) {
		this.curriculoTipo = curriculoTipo;
	}

	public String getCurriculoNome() {
		return curriculoNome;
	}

	public void setCurriculoNome(String curriculoNome) {
		this.curriculoNome = curriculoNome;
	}

	public String getProjetoTipo() {
		return projetoTipo;
	}

	public void setProjetoTipo(String projetoTipo) {
		this.projetoTipo = projetoTipo;
	}

	public String getProjetoNome() {
		return projetoNome;
	}

	public void setProjetoNome(String projetoNome) {
		this.projetoNome = projetoNome;
	}

	public void setCurriculo(byte[] curriculo) {
		this.curriculo = curriculo;
	}

	public void setProjeto(byte[] projeto) {
		this.projeto = projeto;
	}

	public byte[] getAssinatura() {
		return assinatura;
	}

	public void setAssinatura(byte[] assinatura) {
		this.assinatura = assinatura;
	}

	public void setAssinatura(MultipartFile assinatura) throws IOException {
		this.assinatura = assinatura.getBytes();
	}

	public String getAssinaturaTipo() {
		return assinaturaTipo;
	}

	public void setAssinaturaTipo(String assinaturaTipo) {
		this.assinaturaTipo = assinaturaTipo;
	}

	public String getAssinaturaNome() {
		return assinaturaNome;
	}

	public void setAssinaturaNome(String assinaturaNome) {
		this.assinaturaNome = assinaturaNome;
	}
	
	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
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
	
	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
	
	public boolean isDiversos() {
		if(id != null && id.equals(DIVERSOS))
			return true;
		return false;
	}

}
