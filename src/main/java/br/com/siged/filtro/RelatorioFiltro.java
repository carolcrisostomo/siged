package br.com.siged.filtro;

import java.util.Date;

public class RelatorioFiltro {

	private String cpf;
	private String instituicao;
	private String nomeParticipante;	
	private String ano;
	private String semestre;
	private String quadrimestre;
	private String data;
	private String turno;
	private String indicadorManual1;
	private String desempenho;
	private String indicada;
	private String confirmada;
	private String ufMunicipioId;
	private String indicadorManual2;
	private String indicadorManual3;
	private String tituloEvento;
	private String administracaoPublica;
	private Long cidadeId;
	private Long eventoId;
	private Long modulo;
	private Long instituicaoLong;
	private Long instrutorId;
	private Long localizacaoId;
	private Long paisId;
	private Long participanteId;
	private Long provedorId;
	private Long publicoAlvoId;
	private Long eixoTematicoId;
	private Long programaId;
	private Long setorId;
	private Long orgaoId;
	private Long localidadeId;
	private Long situacao;
	private Long tipoAreaTribunalId;
	private Long tipoGastoId;
	private Long tipoEventoId;
	private Long ufId;
	private Long modalidadeId;
	private Long tipoParticipanteId;	
	private Long responsavelPelaIndicacaoId;
	private Long municipioId;
	private Long agruparPor;
	private Date dataInicio1;
	private Date dataInicio2;
	private Date dataInicioPrevisto1;
	private Date dataInicioPrevisto2;
	private Date quadrimestre_data1;
	private Date quadrimestre_data2;
	private Date trimestre_data1;
	private Date trimestre_data2;
	private Date semestre_data1;
	private Date semestre_data2;
	private Date ano_data1;
	private Date ano_data2;
	private Date parcial_data;
	private Date dataPreInscricao1;
	private Date dataPreInscricao2;
	private Boolean parcial_ate;
    private Boolean automatico;
    private Boolean provedoresTerceiros;
    private Boolean incluirTipo1;
    private Boolean incluirTipo2;
    private Boolean incluirTipo3;
    private Boolean incluirTipo4;
    private Boolean excluirTipo1;
    private Boolean excluirTipo2;
    private Boolean excluirTipo3;
    private Boolean excluirTipo4;
    private Boolean incluirTipoParticipante1;
    private Boolean incluirTipoParticipante2;
    private Boolean incluirTipoParticipante3;
    private Boolean incluirTipoParticipante4;
    
	
	public Long getMunicipioId() {return municipioId;}
	public void setMunicipioId(Long municipioId) {this.municipioId = municipioId;}
	
	public String getUfMunicipioId() {return ufMunicipioId;}
	public void setUfMunicipioId(String ufMunicipioId) {this.ufMunicipioId = ufMunicipioId;}
	
	public Date getDataPreInscricao1() {return dataPreInscricao1;}
	public void setDataPreInscricao1(Date dataPreInscricao1) {this.dataPreInscricao1 = dataPreInscricao1;}
	
	public Date getDataPreInscricao2() {return dataPreInscricao2;}
	public void setDataPreInscricao2(Date dataPreInscricao2) {this.dataPreInscricao2 = dataPreInscricao2;}	
	
	public String getIndicada() {return indicada;}
	public void setIndicada(String indicada) {this.indicada = indicada;}
	
	public String getConfirmada() {return confirmada;}
	public void setConfirmada(String confirmada) {this.confirmada = confirmada;}
	
	public Long getResponsavelPelaIndicacaoId() {return responsavelPelaIndicacaoId;}
	public void setResponsavelPelaIndicacaoId(Long responsavelPelaIndicacaoId) {this.responsavelPelaIndicacaoId = responsavelPelaIndicacaoId;}
	
	public Long getCidadeId() {return cidadeId;}
	public void setCidadeId(Long cidadeId) {this.cidadeId = cidadeId;}
	
	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {this.cpf = cpf;}
	
	public Date getDataInicio1() {return dataInicio1;}
	public void setDataInicio1(Date dataInicio1) {this.dataInicio1 = dataInicio1;}
	
	public Date getDataInicio2() {return dataInicio2;}
	public void setDataInicio2(Date dataInicio2) {this.dataInicio2 = dataInicio2;}
	
	public Date getDataInicioPrevisto1() {return dataInicioPrevisto1;}
	public void setDataInicioPrevisto1(Date dataInicioPrevisto1) {this.dataInicioPrevisto1 = dataInicioPrevisto1;}
	
	public Date getDataInicioPrevisto2() {return dataInicioPrevisto2;}
	public void setDataInicioPrevisto2(Date dataInicioPrevisto2) {this.dataInicioPrevisto2 = dataInicioPrevisto2;}
	
	public Long getEventoId() {return eventoId;}
	public void setEventoId(Long eventoId) {this.eventoId = eventoId;}
	
	public String getInstituicao() {return instituicao;}
	public void setInstituicao(String instituicao) {this.instituicao = instituicao;}
	
	public Long getInstituicaoLong() {return instituicaoLong;}
	public void setInstituicaoLong(Long instituicaoLong) {this.instituicaoLong = instituicaoLong;}
	
	public Long getInstrutorId() {return instrutorId;}
	public void setInstrutorId(Long instrutorId) {this.instrutorId = instrutorId;}
	
	public Long getLocalizacaoId() {return localizacaoId;}
	public void setLocalizacaoId(Long localizacaoId) {this.localizacaoId = localizacaoId;}
	
	public Long getPaisId() {return paisId;}
	public void setPaisId(Long paisId) {this.paisId = paisId;}
	
	public Long getParticipanteId() {return participanteId;}
	public void setParticipanteId(Long participanteId) {this.participanteId = participanteId;}
	
	public Long getProvedorId() {return provedorId;}
	public void setProvedorId(Long provedorId) {this.provedorId = provedorId;}
	
	public Long getPublicoAlvoId() {return publicoAlvoId;}
	public void setPublicoAlvoId(Long publicoAlvoId) {this.publicoAlvoId = publicoAlvoId;}
	
	public Long getEixoTematicoId() {return eixoTematicoId;}
	public void setEixoTematicoId(Long eixoTematicoId) {this.eixoTematicoId = eixoTematicoId;}
	
	public Long getProgramaId() { return programaId; }
	public void setProgramaId(Long programaId) { this.programaId = programaId; }
	
	public Long getSetorId() {return setorId;}
	public void setSetorId(Long setorId) {this.setorId = setorId;}
	
	public Long getOrgaoId() {return orgaoId;}
	public void setOrgaoId(Long setorId) {this.orgaoId = setorId;}
	
	public Long getSituacao() {return situacao;}
	public void setSituacao(Long situacao) {this.situacao = situacao;}
	
	public Long getTipoAreaTribunalId() {return tipoAreaTribunalId;}
	public void setTipoAreaTribunalId(Long tipoAreaTribunalId) {this.tipoAreaTribunalId = tipoAreaTribunalId;}
	
	public Long getTipoGastoId() {return tipoGastoId;}
	public void setTipoGastoId(Long tipoGastoId) {this.tipoGastoId = tipoGastoId;}
	
	public Long getTipoEventoId() {return tipoEventoId;}
	public void setTipoEventoId(Long tipoEventoId) {this.tipoEventoId = tipoEventoId;}
	
	public Long getUfId() {return ufId;}
	public void setUfId(Long ufId) {this.ufId = ufId;}
	
	public String getData() {return data;}
	public void setData(String data) {this.data = data;}
	
	public String getTurno() {return turno;}
	public void setTurno(String turno) {this.turno = turno;}
	
	public Date getSemestre_data1() {return semestre_data1;}
	public void setSemestre_data1(Date semestreData1) {semestre_data1 = semestreData1;}
	
	public Date getSemestre_data2() {return semestre_data2;}
	public void setSemestre_data2(Date semestreData2) {semestre_data2 = semestreData2;}
	
	public Date getAno_data1() {return ano_data1;}
	public void setAno_data1(Date anoData1) {ano_data1 = anoData1;}
	
	public Date getAno_data2() {return ano_data2;}
	public void setAno_data2(Date anoData2) {ano_data2 = anoData2;}
	
	public Date getTrimestre_data1() {return trimestre_data1;}
	public void setTrimestre_data1(Date trimestreData1) {trimestre_data1 = trimestreData1;}
	
	public Date getTrimestre_data2() {return trimestre_data2;}
	public void setTrimestre_data2(Date trimestreData2) {trimestre_data2 = trimestreData2;}
	
	public String getAno() {return ano;}
	public void setAno(String ano) {this.ano = ano;}
	
	public String getSemestre() {return semestre;}
	public void setSemestre(String semestre) {this.semestre = semestre;}
	
	public Boolean getAutomatico() {return automatico;}
	public void setAutomatico(Boolean automatico) {this.automatico = automatico;}
	
	public Boolean getProvedoresTerceiros() {return provedoresTerceiros;}
	public void setProvedoresTerceiros(Boolean provedoresTerceiros) {this.provedoresTerceiros = provedoresTerceiros;}
	
	public String getIndicadorManual1() {return indicadorManual1;}
	public void setIndicadorManual1(String indicadorManual1) {this.indicadorManual1 = indicadorManual1;}
	
	public String getIndicadorManual2() {return indicadorManual2;}
	public void setIndicadorManual2(String indicadorManual2) {this.indicadorManual2 = indicadorManual2;}
	
	public String getIndicadorManual3() {return indicadorManual3;}
	public void setIndicadorManual3(String indicadorManual3) {this.indicadorManual3 = indicadorManual3;}
	
	public Long getModulo() {return modulo;}
	public void setModulo(Long modulo) {this.modulo = modulo;}
	
	public Date getParcial_data() {return parcial_data;}
	public void setParcial_data(Date parcial_data) {this.parcial_data = parcial_data;}
	
	public Boolean getParcial_ate() {return parcial_ate;}
	public void setParcial_ate(Boolean parcial_ate) {this.parcial_ate = parcial_ate;}
	
	public String getDesempenho() {return desempenho;}
	public void setDesempenho(String desempenho) {this.desempenho = desempenho;}
	
	public Long getModalidadeId() {return modalidadeId;}
	public void setModalidadeId(Long modalidadeId) {this.modalidadeId = modalidadeId;}
	
	public Long getTipoParticipanteId() {return tipoParticipanteId;}
	public void setTipoParticipanteId(Long tipoParticipanteId) {this.tipoParticipanteId = tipoParticipanteId;}
	
	public String getQuadrimestre() {return quadrimestre;}
	public void setQuadrimestre(String quadrimestre) {this.quadrimestre = quadrimestre;}
	
	public Date getQuadrimestre_data1() {return quadrimestre_data1;}
	public void setQuadrimestre_data1(Date quadrimestre_data1) {this.quadrimestre_data1 = quadrimestre_data1;}
	
	public Date getQuadrimestre_data2() {return quadrimestre_data2;}
	public void setQuadrimestre_data2(Date quadrimestre_data2) {this.quadrimestre_data2 = quadrimestre_data2;}
	
	public String getNomeParticipante() {return nomeParticipante;}
	public void setNomeParticipante(String nomeParticipante) {this.nomeParticipante = nomeParticipante;}
	
	public Boolean getIncluirTipo1() {return incluirTipo1;}
	public void setIncluirTipo1(Boolean incluirTipo1) {this.incluirTipo1 = incluirTipo1;}
	
	public Boolean getIncluirTipo2() {return incluirTipo2;}
	public void setIncluirTipo2(Boolean incluirTipo2) {this.incluirTipo2 = incluirTipo2;}
	
	public Boolean getIncluirTipo3() {return incluirTipo3;}
	public void setIncluirTipo3(Boolean incluirTipo3) {this.incluirTipo3 = incluirTipo3;}
	
	public Boolean getExcluirTipo1() {return excluirTipo1;}
	public void setExcluirTipo1(Boolean excluirTipo1) {this.excluirTipo1 = excluirTipo1;}
	
	public Boolean getExcluirTipo2() {return excluirTipo2;}
	public void setExcluirTipo2(Boolean excluirTipo2) {this.excluirTipo2 = excluirTipo2;}
	
	public Boolean getExcluirTipo3() {return excluirTipo3;}
	public void setExcluirTipo3(Boolean excluirTipo3) {this.excluirTipo3 = excluirTipo3;}
	
	public Boolean getIncluirTipoParticipante1() {return incluirTipoParticipante1;}
	public void setIncluirTipoParticipante1(Boolean incluirTipoParticipante1) {this.incluirTipoParticipante1 = incluirTipoParticipante1;}
	
	public Boolean getIncluirTipoParticipante2() {return incluirTipoParticipante2;}
	public void setIncluirTipoParticipante2(Boolean incluirTipoParticipante2) {this.incluirTipoParticipante2 = incluirTipoParticipante2;}
	
	public Boolean getIncluirTipoParticipante3() {return incluirTipoParticipante3;}
	public void setIncluirTipoParticipante3(Boolean incluirTipoParticipante3) {this.incluirTipoParticipante3 = incluirTipoParticipante3;}
	
	public Boolean getIncluirTipo4() {return incluirTipo4;}
	public void setIncluirTipo4(Boolean incluirTipo4) {this.incluirTipo4 = incluirTipo4;}
	
	public Boolean getExcluirTipo4() {return excluirTipo4;}
	public void setExcluirTipo4(Boolean excluirTipo4) {this.excluirTipo4 = excluirTipo4;}
	
	public Boolean getIncluirTipoParticipante4() {return incluirTipoParticipante4;}
	public void setIncluirTipoParticipante4(Boolean incluirTipoParticipante4) {this.incluirTipoParticipante4 = incluirTipoParticipante4;}
	
	public Long getAgruparPor() {return agruparPor;}
	public void setAgruparPor(Long agruparPor) {this.agruparPor = agruparPor;}
	
	public String getTituloEvento() {return tituloEvento;}
	public void setTituloEvento(String tituloEvento) {this.tituloEvento = tituloEvento;}
	
	public String getAdministracaoPublica() {return administracaoPublica;}
	public void setAdministracaoPublica(String administracaoPublica) {this.administracaoPublica = administracaoPublica;}
	
	public Long getLocalidadeId() {return localidadeId;}
	public void setLocalidadeId(Long localidadeId) {this.localidadeId = localidadeId;}	

}
