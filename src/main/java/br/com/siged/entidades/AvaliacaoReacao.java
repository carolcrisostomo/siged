package br.com.siged.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import br.com.siged.util.comparator.AvaliacaoReacaoNotaComparator;

@Entity
@Table(name = "AVALIACAO_REACAO")
@SequenceGenerator(name = "sequence", sequenceName = "seq_avaliacao_reacao", allocationSize = 1)
public class AvaliacaoReacao implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence")
    @Column(name = "id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "participante_id", referencedColumnName = "id")
	private Participante participante;
	
	@NotNull(message = " Campo Obrigatório")
	@ManyToOne
	@JoinColumn(name = "modulo_id", referencedColumnName = "id")
	private Modulo modulo;
	
	@Column(name = "observacao")
	private String observacao;
	
	@Column(name = "data_cadastro")
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy="avaliacaoReacao", cascade = CascadeType.ALL)
    private List<AvaliacaoReacaoNota> avaliacoesReacaoNota;
	
	public AvaliacaoReacao(){
		
	}
	
	public AvaliacaoReacao(Long id){
		this.id = id;
	}
	
	public AvaliacaoReacao(
			Long id,
			Participante participante,
			Modulo modulo,
			String observacao,
			Date dataCadastro,
			List<AvaliacaoReacaoNota> avaliacoesReacaoNota
			){
		super();
		this.id = id;
		this.participante = participante;
		this.modulo = modulo;
		this.observacao = observacao;
		this.dataCadastro = dataCadastro;
		this.avaliacoesReacaoNota = avaliacoesReacaoNota;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Participante getParticipante() {
		return participante;
	}

	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	public List<AvaliacaoReacaoNota> getAvaliacoesReacaoNota() {
		return avaliacoesReacaoNota;
	}

	public void setAvaliacoesReacaoNota(List<AvaliacaoReacaoNota> avaliacoesReacaoNota) {
		this.avaliacoesReacaoNota = avaliacoesReacaoNota;
	}
	
	public int getTotalRespondidas(){
		int total = 0;
		for (AvaliacaoReacaoNota avn : avaliacoesReacaoNota) {
			if(avn.getQuestao() != null && avn.getNotaQuestao() != null){
				total++;
			}
		}
		
		return total;
	}
	
	public int getTotalSatisfatorias(){
		int total = 0;
		
		for (AvaliacaoReacaoNota avn : avaliacoesReacaoNota) {
			if(avn.getQuestao() != null && avn.getNotaQuestao() != null){
				if(avn.getNotaQuestao().getDescricao().equals("Bom") || avn.getNotaQuestao().getDescricao().equals("Excelente")){
					total++;
				}
			}
		}
		
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
	
	public Map<Integer, List<AvaliacaoReacaoNota>> agruparAvaliacoesInstrutores() {
		Map<Integer, List<AvaliacaoReacaoNota>> map = new HashMap<>();
		Modulo modulocons = this.getModulo();
		
		List<AvaliacaoReacaoNota> avaliacoesInstrutor = new ArrayList<>();
		List<AvaliacaoReacaoNota> avaliacoesInstrutor2 = new ArrayList<>();
		List<AvaliacaoReacaoNota> avaliacoesInstrutor3 = new ArrayList<>();
		
		Instrutor instrutor = new Instrutor();
		Instrutor instrutor2 = new Instrutor();
		Instrutor instrutor3 = new Instrutor();
		
		int i = 1;
		for (Instrutor inst : modulocons.getInstrutorList()) {
			if (i == 1) {
				instrutor = inst;
			}
			if (i == 2) {
				instrutor2 = inst;
			}
			if (i == 3) {
				instrutor3 = inst;
			}
			i = i + 1;
		}
		
		for (AvaliacaoReacaoNota avn : this.getAvaliacoesReacaoNota()) {
			if(avn.getInstrutor() != null){
				if(avn.getInstrutor().getId() == instrutor.getId()){
					avaliacoesInstrutor.add(avn);
				} else if (avn.getInstrutor().getId() == instrutor2.getId()){
					avaliacoesInstrutor2.add(avn);
				} else if (avn.getInstrutor().getId() == instrutor3.getId()){
					avaliacoesInstrutor3.add(avn);
				}
			}
		}
		
		Collections.sort(avaliacoesInstrutor, AvaliacaoReacaoNotaComparator.OrderByOrdemQuestao.asc());
		Collections.sort(avaliacoesInstrutor2, AvaliacaoReacaoNotaComparator.OrderByOrdemQuestao.asc());
		Collections.sort(avaliacoesInstrutor3, AvaliacaoReacaoNotaComparator.OrderByOrdemQuestao.asc());
		
		map.put(1, avaliacoesInstrutor);
		map.put(2, avaliacoesInstrutor2);
		map.put(3, avaliacoesInstrutor3);
		
		return map;
	}
	
	public List<AvaliacaoReacaoNota> agruparAvaliacoesPeloTipoQuestao(TipoQuestao tipoQuestao) {
		List<AvaliacaoReacaoNota> arns = new ArrayList<>();
		
		for(AvaliacaoReacaoNota arn : this.avaliacoesReacaoNota) {
			if(arn.getQuestao().getTipoQuestao().equals(tipoQuestao)) {
				arns.add(arn);
			}
		}
		
		Collections.sort(arns, AvaliacaoReacaoNotaComparator.OrderByOrdemQuestao.asc());
		
		return arns;
	}
	
	public boolean isNenhumaQuestaoRespondida(){
		if(this.avaliacoesReacaoNota == null) {
			return true;
		} else if (this.avaliacoesReacaoNota.size() < 1){
			return true;
		}
		
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

        if (!(object instanceof AvaliacaoReacao)) {
            return false;
        }
        AvaliacaoReacao other = (AvaliacaoReacao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
	
}
