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
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author Rodrigo
 * @deprecated - Substituir por AvaliacaoReacao
 */
@Entity
@Table(name = "avaliacao")
@NamedQueries({
    @NamedQuery(name = "Avaliacao.findAll", query = "SELECT a FROM Avaliacao a ORDER BY a.eventoId.dataInicioPrevisto DESC"),
    @NamedQuery(name = "Avaliacao.findByParticipanteCpf", query = "SELECT a FROM Avaliacao a WHERE a.participanteId.cpf = :cpf ORDER BY a.eventoId.dataInicioPrevisto DESC"),
    @NamedQuery(name = "Avaliacao.findById", query = "SELECT a FROM Avaliacao a WHERE a.id = :id"),
    @NamedQuery(name = "Avaliacao.findByObservacao", query = "SELECT a FROM Avaliacao a WHERE a.observacao = :observacao"),
    @NamedQuery(name = "Avaliacao.findByEventoAndParticipante", query = "SELECT a FROM Avaliacao a WHERE a.eventoId = :evento and a.participanteId = :participante"),
    @NamedQuery(name = "Avaliacao.findByModulo", query = "SELECT a FROM Avaliacao a WHERE a.moduloId = :modulo"),
    @NamedQuery(name = "Avaliacao.findByDataCadastro", query = "SELECT a FROM Avaliacao a WHERE a.dataCadastro = :dataCadastro"),
    @NamedQuery(name = "Avaliacao.findByQuestaoAutoAvaliacao1", query = "SELECT a FROM Avaliacao a WHERE a.questaoAutoAvaliacao1 = :questaoAutoAvaliacao1"),
    @NamedQuery(name = "Avaliacao.findByQuestaoAutoAvaliacao2", query = "SELECT a FROM Avaliacao a WHERE a.questaoAutoAvaliacao2 = :questaoAutoAvaliacao2"),
    @NamedQuery(name = "Avaliacao.findByQuestaoConteudo1", query = "SELECT a FROM Avaliacao a WHERE a.questaoConteudo1 = :questaoConteudo1"),
    @NamedQuery(name = "Avaliacao.findByQuestaoConteudo2", query = "SELECT a FROM Avaliacao a WHERE a.questaoConteudo2 = :questaoConteudo2"),
    @NamedQuery(name = "Avaliacao.findByQuestaoConteudo3", query = "SELECT a FROM Avaliacao a WHERE a.questaoConteudo3 = :questaoConteudo3"),
    @NamedQuery(name = "Avaliacao.findByQuestaoInstrutor1", query = "SELECT a FROM Avaliacao a WHERE a.questaoInstrutor1 = :questaoInstrutor1"),
    @NamedQuery(name = "Avaliacao.findByQuestaoInstrutor2", query = "SELECT a FROM Avaliacao a WHERE a.questaoInstrutor2 = :questaoInstrutor2"),
    @NamedQuery(name = "Avaliacao.findByQuestaoInstrutor3", query = "SELECT a FROM Avaliacao a WHERE a.questaoInstrutor3 = :questaoInstrutor3"),
    @NamedQuery(name = "Avaliacao.findByQuestaoInstrutor4", query = "SELECT a FROM Avaliacao a WHERE a.questaoInstrutor4 = :questaoInstrutor4"),
    @NamedQuery(name = "Avaliacao.findByQuestaoInstrutor5", query = "SELECT a FROM Avaliacao a WHERE a.questaoInstrutor5 = :questaoInstrutor5"),
    @NamedQuery(name = "Avaliacao.findByQuestaoInstrutor6", query = "SELECT a FROM Avaliacao a WHERE a.questaoInstrutor6 = :questaoInstrutor6")})
@JsonIgnoreProperties(value={"instrutorId", "instrutor2Id", "instrutor3Id", "eventoId", "moduloId", "participanteId"})
@SequenceGenerator(name="sequence", sequenceName="seq_avaliacao", allocationSize=1)
public class Avaliacao implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
    private Long id;
    
	@Column(name = "observacao", length = 2000)
    private String observacao;

	//@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "questao_auto_avaliacao1")
    private String questaoAutoAvaliacao1;
    
    //@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "questao_auto_avaliacao2")
    private String questaoAutoAvaliacao2;
    
    //@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "questao_organizacao")
    private String questaoOrganizacao;
    
    //@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "questao_espacofisico")
    private String questaoEspacoFisico;
    
    @Column(name = "data_cadastro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;
    
    //@NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "instrutor_id", referencedColumnName = "id")
    @ManyToOne(optional = false,fetch=FetchType.LAZY) @JsonIgnore
    private Instrutor instrutorId;
    
    //@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "questao_conteudo1")
    private String questaoConteudo1;
    
    //@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "questao_conteudo2")
    private String questaoConteudo2;
    
    //@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "questao_conteudo3")
    private String questaoConteudo3;
    
    //@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "questao_instrutor1")
    private String questaoInstrutor1;
    
    //@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "questao_instrutor2")
    private String questaoInstrutor2;
    
    //@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "questao_instrutor3")
    private String questaoInstrutor3;
    
    //@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "questao_instrutor4")
    private String questaoInstrutor4;
    
    //@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "questao_instrutor5")
    private String questaoInstrutor5;
    
    //@NotEmpty(message=" Campo Obrigatório")
    @Column(name = "questao_instrutor6")
    private String questaoInstrutor6;
    
    @JoinColumn(name = "instrutor2_id", referencedColumnName = "id")
    @ManyToOne(optional = false,fetch=FetchType.LAZY) @JsonIgnore
    private Instrutor instrutor2Id;
    
    @Column(name = "questao_instrutor21")
    private String questaoInstrutor21;
    
    @Column(name = "questao_instrutor22")
    private String questaoInstrutor22;
    
    @Column(name = "questao_instrutor23")
    private String questaoInstrutor23;
    
    @Column(name = "questao_instrutor24")
    private String questaoInstrutor24;
    
    @Column(name = "questao_instrutor25")
    private String questaoInstrutor25;
    
    @Column(name = "questao_instrutor26")
    private String questaoInstrutor26;
    
    @JoinColumn(name = "instrutor3_id", referencedColumnName = "id")
    @ManyToOne(optional = false,fetch=FetchType.LAZY) @JsonIgnore
    private Instrutor instrutor3Id;
    
    @Column(name = "questao_instrutor31")
    private String questaoInstrutor31;
    
    @Column(name = "questao_instrutor32")
    private String questaoInstrutor32;
    
    @Column(name = "questao_instrutor33")
    private String questaoInstrutor33;
    
    @Column(name = "questao_instrutor34")
    private String questaoInstrutor34;
    
    @Column(name = "questao_instrutor35")
    private String questaoInstrutor35;
    
    @Column(name = "questao_instrutor36")
    private String questaoInstrutor36;
    
    @NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "evento_id", referencedColumnName = "id")
    @ManyToOne(optional = false,fetch=FetchType.LAZY) @JsonIgnore
    private Evento eventoId;
    
    @NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "modulo_id", referencedColumnName = "id")
    @ManyToOne(optional = false,fetch=FetchType.LAZY) @JsonIgnore
    private Modulo moduloId;
    
    //@NotNull(message=" Campo Obrigatório")
    @JoinColumn(name = "participante_id", referencedColumnName = "id")
    @ManyToOne(optional = false,fetch=FetchType.LAZY) @JsonIgnore
    private Participante participanteId;
    
    public Avaliacao() {
    }

    public Avaliacao(Long id) {
        this.id = id;
    }

    public Avaliacao(Long id, String observacao, String questaoAutoAvaliacao1,
			String questaoAutoAvaliacao2, Instrutor instrutorId,
			String questaoConteudo1, String questaoConteudo2,
			String questaoConteudo3, String questaoInstrutor1,
			String questaoInstrutor2, String questaoInstrutor3,
			String questaoInstrutor4, String questaoInstrutor5,
			String questaoInstrutor6, Instrutor instrutor2Id,
			String questaoInstrutor21, String questaoInstrutor22,
			String questaoInstrutor23, String questaoInstrutor24,
			String questaoInstrutor25, String questaoInstrutor26,
			Instrutor instrutor3Id, String questaoInstrutor31,
			String questaoInstrutor32, String questaoInstrutor33,
			String questaoInstrutor34, String questaoInstrutor35,
			String questaoInstrutor36, Evento eventoId, Modulo moduloId,
			Participante participanteId, String questaoEspacoFisico, String questaoOrganizacao) {
		super();
		this.id = id;
		this.observacao = observacao;
		this.questaoAutoAvaliacao1 = questaoAutoAvaliacao1;
		this.questaoAutoAvaliacao2 = questaoAutoAvaliacao2;
		this.instrutorId = instrutorId;
		this.questaoConteudo1 = questaoConteudo1;
		this.questaoConteudo2 = questaoConteudo2;
		this.questaoConteudo3 = questaoConteudo3;
		this.questaoInstrutor1 = questaoInstrutor1;
		this.questaoInstrutor2 = questaoInstrutor2;
		this.questaoInstrutor3 = questaoInstrutor3;
		this.questaoInstrutor4 = questaoInstrutor4;
		this.questaoInstrutor5 = questaoInstrutor5;
		this.questaoInstrutor6 = questaoInstrutor6;
		this.instrutor2Id = instrutor2Id;
		this.questaoInstrutor21 = questaoInstrutor21;
		this.questaoInstrutor22 = questaoInstrutor22;
		this.questaoInstrutor23 = questaoInstrutor23;
		this.questaoInstrutor24 = questaoInstrutor24;
		this.questaoInstrutor25 = questaoInstrutor25;
		this.questaoInstrutor26 = questaoInstrutor26;
		this.instrutor3Id = instrutor3Id;
		this.questaoInstrutor31 = questaoInstrutor31;
		this.questaoInstrutor32 = questaoInstrutor32;
		this.questaoInstrutor33 = questaoInstrutor33;
		this.questaoInstrutor34 = questaoInstrutor34;
		this.questaoInstrutor35 = questaoInstrutor35;
		this.questaoInstrutor36 = questaoInstrutor36;
		this.eventoId = eventoId;
		this.moduloId = moduloId;
		this.participanteId = participanteId;
		this.questaoOrganizacao = questaoOrganizacao;
		this.questaoEspacoFisico = questaoEspacoFisico;
    }

	public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getObservacao() {return observacao;}
    public void setObservacao(String observacao) {this.observacao = observacao;}

    public String getQuestaoAutoAvaliacao1() {return questaoAutoAvaliacao1;}
    public void setQuestaoAutoAvaliacao1(String questaoAutoAvaliacao1) {this.questaoAutoAvaliacao1 = questaoAutoAvaliacao1;}

    public String getQuestaoAutoAvaliacao2() {return questaoAutoAvaliacao2;}
    public void setQuestaoAutoAvaliacao2(String questaoAutoAvaliacao2) {this.questaoAutoAvaliacao2 = questaoAutoAvaliacao2;}

    public String getQuestaoConteudo1() {return questaoConteudo1;}
    public void setQuestaoConteudo1(String questaoConteudo1) {this.questaoConteudo1 = questaoConteudo1;}

    public String getQuestaoConteudo2() {return questaoConteudo2;}
    public void setQuestaoConteudo2(String questaoConteudo2) {this.questaoConteudo2 = questaoConteudo2;}

    public String getQuestaoConteudo3() {return questaoConteudo3;}
    public void setQuestaoConteudo3(String questaoConteudo3) {this.questaoConteudo3 = questaoConteudo3;}

    public String getQuestaoInstrutor1() {return questaoInstrutor1;}

    public void setQuestaoInstrutor1(String questaoInstrutor1) {this.questaoInstrutor1 = questaoInstrutor1;}

    public String getQuestaoInstrutor2() {return questaoInstrutor2;}
    public void setQuestaoInstrutor2(String questaoInstrutor2) {this.questaoInstrutor2 = questaoInstrutor2;}

    public String getQuestaoInstrutor3() {return questaoInstrutor3;}
    public void setQuestaoInstrutor3(String questaoInstrutor3) {this.questaoInstrutor3 = questaoInstrutor3;}

    public String getQuestaoInstrutor4() {return questaoInstrutor4;}
    public void setQuestaoInstrutor4(String questaoInstrutor4) {this.questaoInstrutor4 = questaoInstrutor4;}

    public String getQuestaoInstrutor5() {return questaoInstrutor5;}
    public void setQuestaoInstrutor5(String questaoInstrutor5) {this.questaoInstrutor5 = questaoInstrutor5;}

    public String getQuestaoInstrutor6() {return questaoInstrutor6;}
    public void setQuestaoInstrutor6(String questaoInstrutor6) {this.questaoInstrutor6 = questaoInstrutor6;}

    public Evento getEventoId() {return eventoId;}
    public void setEventoId(Evento eventoId) {this.eventoId = eventoId;}
    
    public Instrutor getInstrutorId() {return instrutorId;}
    public void setInstrutorId(Instrutor instrutorId) {this.instrutorId = instrutorId;}

    public Modulo getModuloId() {return moduloId;}
    public void setModuloId(Modulo moduloId) {this.moduloId = moduloId;}

    public Participante getParticipanteId() {return participanteId;}
    public void setParticipanteId(Participante participanteId) {this.participanteId = participanteId;}
    
    public Date getDataCadastro() {return dataCadastro;}
	public void setDataCadastro(Date dataCadastro) {this.dataCadastro = dataCadastro;}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Avaliacao)) {
            return false;
        }
        Avaliacao other = (Avaliacao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.siged.entidades.Avaliacao[id=" + id + "]";
    }

	public Instrutor getInstrutor2Id() {return instrutor2Id;}
	public void setInstrutor2Id(Instrutor instrutor2Id) {this.instrutor2Id = instrutor2Id;}

	public String getQuestaoInstrutor21() {return questaoInstrutor21;}
	public void setQuestaoInstrutor21(String questaoInstrutor21) {this.questaoInstrutor21 = questaoInstrutor21;}

	public String getQuestaoInstrutor22() {return questaoInstrutor22;}
	public void setQuestaoInstrutor22(String questaoInstrutor22) {this.questaoInstrutor22 = questaoInstrutor22;}

	public String getQuestaoInstrutor23() {return questaoInstrutor23;}
	public void setQuestaoInstrutor23(String questaoInstrutor23) {this.questaoInstrutor23 = questaoInstrutor23;}

	public String getQuestaoInstrutor24() {return questaoInstrutor24;}
	public void setQuestaoInstrutor24(String questaoInstrutor24) {this.questaoInstrutor24 = questaoInstrutor24;}

	public String getQuestaoInstrutor25() {return questaoInstrutor25;}
	public void setQuestaoInstrutor25(String questaoInstrutor25) {this.questaoInstrutor25 = questaoInstrutor25;}

	public String getQuestaoInstrutor26() {return questaoInstrutor26;}
	public void setQuestaoInstrutor26(String questaoInstrutor26) {this.questaoInstrutor26 = questaoInstrutor26;}

	public Instrutor getInstrutor3Id() {return instrutor3Id;}
	public void setInstrutor3Id(Instrutor instrutor3Id) {this.instrutor3Id = instrutor3Id;}

	public String getQuestaoInstrutor31() {return questaoInstrutor31;}
	public void setQuestaoInstrutor31(String questaoInstrutor31) {this.questaoInstrutor31 = questaoInstrutor31;}

	public String getQuestaoInstrutor32() {return questaoInstrutor32;}
	public void setQuestaoInstrutor32(String questaoInstrutor32) {this.questaoInstrutor32 = questaoInstrutor32;}

	public String getQuestaoInstrutor33() {return questaoInstrutor33;}
	public void setQuestaoInstrutor33(String questaoInstrutor33) {this.questaoInstrutor33 = questaoInstrutor33;}

	public String getQuestaoInstrutor34() {return questaoInstrutor34;}
	public void setQuestaoInstrutor34(String questaoInstrutor34) {this.questaoInstrutor34 = questaoInstrutor34;}

	public String getQuestaoInstrutor35() {return questaoInstrutor35;}
	public void setQuestaoInstrutor35(String questaoInstrutor35) {this.questaoInstrutor35 = questaoInstrutor35;}

	public String getQuestaoInstrutor36() {return questaoInstrutor36;}
	public void setQuestaoInstrutor36(String questaoInstrutor36) {this.questaoInstrutor36 = questaoInstrutor36;}
	
	public String getQuestaoOrganizacao() {return questaoOrganizacao;}
	public void setQuestaoOrganizacao(String questaoOrganizacao) {this.questaoOrganizacao = questaoOrganizacao;}

	public String getQuestaoEspacoFisico() {return questaoEspacoFisico;}
	public void setQuestaoEspacoFisico(String questaoEspacoFisico) {this.questaoEspacoFisico = questaoEspacoFisico;}
	
	public int getTotalRespondidas(){
		int total = 0;
																																	
		if (questaoAutoAvaliacao1 != null && !questaoAutoAvaliacao1.equals(""))
			total++;
		if (questaoAutoAvaliacao2 != null && !questaoAutoAvaliacao2.equals(""))
			total++;
		if (questaoConteudo1 != null && !questaoConteudo1.equals(""))
			total++;
		if (questaoConteudo2 != null && !questaoConteudo2.equals(""))
			total++;
		if (questaoConteudo3 != null && !questaoConteudo3.equals(""))
			total++;
		if (questaoEspacoFisico != null && !questaoEspacoFisico.equals(""))
			total++;
		if (questaoInstrutor1 != null && !questaoInstrutor1.equals(""))
			total++;
		if (questaoInstrutor2 != null && !questaoInstrutor2.equals(""))
			total++;
		if (questaoInstrutor21 != null && !questaoInstrutor21.equals(""))
			total++;
		if (questaoInstrutor22 != null && !questaoInstrutor22.equals(""))
			total++;
		if (questaoInstrutor23 != null && !questaoInstrutor23.equals(""))
			total++;
		if (questaoInstrutor24 != null && !questaoInstrutor24.equals(""))
			total++;
		if (questaoInstrutor25 != null && !questaoInstrutor25.equals(""))
			total++;
		if (questaoInstrutor26 != null && !questaoInstrutor26.equals(""))
			total++;
		if (questaoInstrutor3 != null && !questaoInstrutor3.equals(""))
			total++;
		if (questaoInstrutor31 != null && !questaoInstrutor31.equals(""))
			total++;
		if (questaoInstrutor32 != null && !questaoInstrutor32.equals(""))
			total++;
		if (questaoInstrutor33 != null && !questaoInstrutor33.equals(""))
			total++;
		if (questaoInstrutor34 != null && !questaoInstrutor34.equals(""))
			total++;
		if (questaoInstrutor35 != null && !questaoInstrutor35.equals(""))
			total++;
		if (questaoInstrutor36 != null && !questaoInstrutor36.equals(""))
			total++;
		if (questaoInstrutor4 != null && !questaoInstrutor4.equals(""))
			total++;
		if (questaoInstrutor5 != null && !questaoInstrutor5.equals(""))
			total++;
		if (questaoInstrutor6 != null && !questaoInstrutor6.equals(""))
			total++;
		if (questaoOrganizacao != null && !questaoOrganizacao.equals(""))
			total++;	
		
		return total;
	}
	
	public int getTotalSatisfatorias(){
		int total = 0;
																																	
		if (questaoAutoAvaliacao1 != null && (questaoAutoAvaliacao1.equals("Bom") || questaoAutoAvaliacao1.equals("Excelente")))
			total++;
		if (questaoAutoAvaliacao2 != null && (questaoAutoAvaliacao2.equals("Bom") || questaoAutoAvaliacao2.equals("Excelente")))
			total++;
		if (questaoConteudo1 != null && (questaoConteudo1.equals("Bom") || questaoConteudo1.equals("Excelente")))
			total++;
		if (questaoConteudo2 != null && (questaoConteudo2.equals("Bom") || questaoConteudo2.equals("Excelente")))
			total++;
		if (questaoConteudo3 != null && (questaoConteudo3.equals("Bom") || questaoConteudo3.equals("Excelente")))
			total++;
		if (questaoEspacoFisico != null && (questaoEspacoFisico.equals("Bom") || questaoEspacoFisico.equals("Excelente")))
			total++;
		if (questaoInstrutor1 != null && (questaoInstrutor1.equals("Bom") || questaoInstrutor1.equals("Excelente")))
			total++;
		if (questaoInstrutor2 != null && (questaoInstrutor2.equals("Bom") || questaoInstrutor2.equals("Excelente")))
			total++;
		if (questaoInstrutor21 != null && (questaoInstrutor21.equals("Bom") || questaoInstrutor21.equals("Excelente")))
			total++;
		if (questaoInstrutor22 != null && (questaoInstrutor22.equals("Bom") || questaoInstrutor22.equals("Excelente")))
			total++;
		if (questaoInstrutor23 != null && (questaoInstrutor23.equals("Bom") || questaoInstrutor23.equals("Excelente")))
			total++;
		if (questaoInstrutor24 != null && (questaoInstrutor24.equals("Bom") || questaoInstrutor24.equals("Excelente")))
			total++;
		if (questaoInstrutor25 != null && (questaoInstrutor25.equals("Bom") || questaoInstrutor25.equals("Excelente")))
			total++;
		if (questaoInstrutor26 != null && (questaoInstrutor26.equals("Bom") || questaoInstrutor26.equals("Excelente")))
			total++;
		if (questaoInstrutor3 != null && (questaoInstrutor3.equals("Bom") || questaoInstrutor3.equals("Excelente")))
			total++;
		if (questaoInstrutor31 != null && (questaoInstrutor31.equals("Bom") || questaoInstrutor31.equals("Excelente")))
			total++;
		if (questaoInstrutor32 != null && (questaoInstrutor32.equals("Bom") || questaoInstrutor32.equals("Excelente")))
			total++;
		if (questaoInstrutor33 != null && (questaoInstrutor33.equals("Bom") || questaoInstrutor33.equals("Excelente")))
			total++;
		if (questaoInstrutor34 != null && (questaoInstrutor34.equals("Bom") || questaoInstrutor34.equals("Excelente")))
			total++;
		if (questaoInstrutor35 != null && (questaoInstrutor35.equals("Bom") || questaoInstrutor35.equals("Excelente")))
			total++;
		if (questaoInstrutor36 != null && (questaoInstrutor36.equals("Bom") || questaoInstrutor36.equals("Excelente")))
			total++;
		if (questaoInstrutor4 != null && (questaoInstrutor4.equals("Bom") || questaoInstrutor4.equals("Excelente")))
			total++;
		if (questaoInstrutor5 != null && (questaoInstrutor5.equals("Bom") || questaoInstrutor5.equals("Excelente")))
			total++;
		if (questaoInstrutor6 != null && (questaoInstrutor6.equals("Bom") || questaoInstrutor6.equals("Excelente")))
			total++;
		if (questaoOrganizacao != null && (questaoOrganizacao.equals("Bom") || questaoOrganizacao.equals("Excelente")))
			total++;	
		
		return total;
	}
	
	public boolean isSatisfatoria(){
		
		boolean satisfatoria = false;
		
		int totalRespondidas = getTotalRespondidas();
		int totalSatisfatorias = getTotalSatisfatorias();
		double porcentagem = (1.0 * totalSatisfatorias / totalRespondidas) * 100;		
		
		if(totalRespondidas > 0 && porcentagem >= 70)
			satisfatoria = true;
		
		return satisfatoria;	
	}

}
