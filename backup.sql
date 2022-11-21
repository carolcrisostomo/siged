--
-- PostgreSQL database dump
--

-- Dumped from database version 15.0 (Debian 15.0-1.pgdg110+1)
-- Dumped by pg_dump version 15.0

-- Started on 2022-11-05 15:23:13

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 321 (class 1259 OID 19314)
-- Name: srh_tb_funcional; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.srh_tb_funcional (
    id integer NOT NULL,
    ativofp integer NOT NULL,
    status integer NOT NULL,
    datasaida date,
    cpf character varying(15) NOT NULL,
    idocupacao integer NOT NULL,
    idpessoal integer NOT NULL,
    idsetordesignado integer,
    idsetor integer
);


ALTER TABLE public.srh_tb_funcional OWNER TO siged;

--
-- TOC entry 320 (class 1259 OID 19313)
-- Name: SRH_TB_FUNCIONAL_id_seq; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public."SRH_TB_FUNCIONAL_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."SRH_TB_FUNCIONAL_id_seq" OWNER TO siged;

--
-- TOC entry 3851 (class 0 OID 0)
-- Dependencies: 320
-- Name: SRH_TB_FUNCIONAL_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: siged
--

ALTER SEQUENCE public."SRH_TB_FUNCIONAL_id_seq" OWNED BY public.srh_tb_funcional.id;


--
-- TOC entry 221 (class 1259 OID 18455)
-- Name: area_conhecimento; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.area_conhecimento (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.area_conhecimento OWNER TO siged;

--
-- TOC entry 222 (class 1259 OID 18460)
-- Name: avaliacao; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.avaliacao (
    id bigint NOT NULL,
    data_cadastro timestamp without time zone,
    observacao character varying(2000),
    questao_auto_avaliacao1 character varying(255),
    questao_auto_avaliacao2 character varying(255),
    questao_conteudo1 character varying(255),
    questao_conteudo2 character varying(255),
    questao_conteudo3 character varying(255),
    questao_espacofisico character varying(255),
    questao_instrutor1 character varying(255),
    questao_instrutor2 character varying(255),
    questao_instrutor21 character varying(255),
    questao_instrutor22 character varying(255),
    questao_instrutor23 character varying(255),
    questao_instrutor24 character varying(255),
    questao_instrutor25 character varying(255),
    questao_instrutor26 character varying(255),
    questao_instrutor3 character varying(255),
    questao_instrutor31 character varying(255),
    questao_instrutor32 character varying(255),
    questao_instrutor33 character varying(255),
    questao_instrutor34 character varying(255),
    questao_instrutor35 character varying(255),
    questao_instrutor36 character varying(255),
    questao_instrutor4 character varying(255),
    questao_instrutor5 character varying(255),
    questao_instrutor6 character varying(255),
    questao_organizacao character varying(255),
    evento_id bigint NOT NULL,
    instrutor2_id bigint NOT NULL,
    instrutor3_id bigint NOT NULL,
    instrutor_id bigint NOT NULL,
    modulo_id bigint NOT NULL,
    participante_id bigint NOT NULL
);


ALTER TABLE public.avaliacao OWNER TO siged;

--
-- TOC entry 223 (class 1259 OID 18467)
-- Name: avaliacao_eficacia; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.avaliacao_eficacia (
    id bigint NOT NULL,
    data_cadastro timestamp without time zone,
    desempenho_servico character varying(255),
    melhoria character varying(255),
    observacao character varying(2000),
    evento_id bigint NOT NULL,
    participante_id bigint NOT NULL,
    responsavel_id bigint
);


ALTER TABLE public.avaliacao_eficacia OWNER TO siged;

--
-- TOC entry 214 (class 1259 OID 18422)
-- Name: avaliacao_reacao; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.avaliacao_reacao (
    id bigint NOT NULL,
    data_cadastro date,
    observacao character varying(255),
    modulo_id bigint NOT NULL,
    participante_id bigint
);


ALTER TABLE public.avaliacao_reacao OWNER TO siged;

--
-- TOC entry 215 (class 1259 OID 18427)
-- Name: avaliacao_reacao_nota; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.avaliacao_reacao_nota (
    id bigint NOT NULL,
    avaliacao_id bigint NOT NULL,
    instrutor_id bigint,
    nota_id bigint NOT NULL,
    questao_id bigint NOT NULL
);


ALTER TABLE public.avaliacao_reacao_nota OWNER TO siged;

--
-- TOC entry 224 (class 1259 OID 18474)
-- Name: certificado; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.certificado (
    id bigint NOT NULL,
    arquivo bytea,
    arquivo_nome character varying(255),
    arquivo_tipo character varying(255),
    evento_id bigint NOT NULL,
    participante_id bigint NOT NULL
);


ALTER TABLE public.certificado OWNER TO siged;

--
-- TOC entry 225 (class 1259 OID 18481)
-- Name: certificado_emitido; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.certificado_emitido (
    id bigint NOT NULL,
    codigoverificacao character varying(255),
    data_emissao timestamp without time zone,
    evento_id bigint,
    participante_id bigint
);


ALTER TABLE public.certificado_emitido OWNER TO siged;

--
-- TOC entry 226 (class 1259 OID 18486)
-- Name: cidade; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.cidade (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL,
    uf_id bigint NOT NULL
);


ALTER TABLE public.cidade OWNER TO siged;

--
-- TOC entry 227 (class 1259 OID 18491)
-- Name: desempenho; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.desempenho (
    id bigint NOT NULL,
    aprovado character varying(255),
    frequencia character varying(255),
    nota character varying(255),
    parcial boolean,
    evento_id bigint,
    modulo_id bigint,
    participante_id bigint
);


ALTER TABLE public.desempenho OWNER TO siged;

--
-- TOC entry 228 (class 1259 OID 18498)
-- Name: eixo_tematico; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.eixo_tematico (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.eixo_tematico OWNER TO siged;

--
-- TOC entry 229 (class 1259 OID 18503)
-- Name: evento; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.evento (
    id bigint NOT NULL,
    administrador character varying(255),
    cal timestamp without time zone,
    carga_horaria character varying(255) NOT NULL,
    certificado_personalizado character varying(255),
    cidade_pais character varying(255) NOT NULL,
    conteudista1 character varying(255),
    conteudista2 character varying(255),
    conteudo character varying(255) NOT NULL,
    data_cadastro timestamp without time zone,
    data_fim_pre_inscricao date,
    data_fim_previsto date NOT NULL,
    data_fim_realizacao date,
    data_inicio_pre_inscricao date,
    data_inicio_previsto date NOT NULL,
    data_inicio_realizacao date,
    identificador character varying(255),
    link_evento character varying(255),
    link_gravacao character varying(255),
    modulo_unico character varying(255) NOT NULL,
    objetivo_especifico character varying(255),
    objetivo_geral character varying(255) NOT NULL,
    observacoes character varying(2000),
    observacoes_publicas character varying(2000),
    permite_certificado character varying(255),
    permite_pre_inscricao character varying(255),
    publicado character varying(255) NOT NULL,
    resultado_esperado character varying(2000),
    titulo character varying(255) NOT NULL,
    tutor1 character varying(255),
    tutor2 character varying(255),
    vagas character varying(255) NOT NULL,
    area_tribunal_id bigint NOT NULL,
    eixo_tematico_id bigint NOT NULL,
    localizacao_id bigint NOT NULL,
    modalidade_id bigint,
    cidade_id bigint,
    pais_id bigint,
    provedor_id bigint,
    responsavel_evento bigint NOT NULL,
    tipo_id bigint NOT NULL
);


ALTER TABLE public.evento OWNER TO siged;

--
-- TOC entry 230 (class 1259 OID 18510)
-- Name: evento_extra; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.evento_extra (
    id bigint NOT NULL,
    curso character varying(255) NOT NULL,
    data_cadastro timestamp without time zone,
    data_fim date NOT NULL,
    data_indicacao timestamp without time zone,
    data_inicio date NOT NULL,
    horario character varying(255) NOT NULL,
    indicada character varying(255),
    justificativa character varying(2000) NOT NULL,
    justificativachefe character varying(2000),
    material bytea,
    material_nome character varying(255),
    material_tipo character varying(255),
    obs_ipc character varying(2000),
    provedor character varying(255),
    site character varying(255),
    turno character varying(255),
    valor numeric(19,2) NOT NULL,
    chefe_id bigint,
    cidade_id bigint,
    pais_id bigint NOT NULL,
    solicitante_id bigint
);


ALTER TABLE public.evento_extra OWNER TO siged;

--
-- TOC entry 231 (class 1259 OID 18517)
-- Name: evento_historico; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.evento_historico (
    id bigint NOT NULL,
    data_fim_previsto timestamp without time zone NOT NULL,
    data_inicio_previsto timestamp without time zone NOT NULL,
    evento_id bigint NOT NULL
);


ALTER TABLE public.evento_historico OWNER TO siged;

--
-- TOC entry 232 (class 1259 OID 18522)
-- Name: evento_interessado; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.evento_interessado (
    evento_id bigint NOT NULL,
    participante_id bigint NOT NULL
);


ALTER TABLE public.evento_interessado OWNER TO siged;

--
-- TOC entry 233 (class 1259 OID 18525)
-- Name: evento_material; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.evento_material (
    id bigint NOT NULL,
    arquivo_original character varying(255),
    arquivo_tce character varying(255),
    arquivo_tipo character varying(255),
    descricao character varying(255),
    material_tipo bigint,
    evento_id bigint NOT NULL,
    modulo_id bigint
);


ALTER TABLE public.evento_material OWNER TO siged;

--
-- TOC entry 234 (class 1259 OID 18532)
-- Name: evento_programa; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.evento_programa (
    evento_id bigint NOT NULL,
    programa_id bigint NOT NULL
);


ALTER TABLE public.evento_programa OWNER TO siged;

--
-- TOC entry 235 (class 1259 OID 18535)
-- Name: evento_provedor_join; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.evento_provedor_join (
    evento_id bigint NOT NULL,
    provedor_id bigint NOT NULL
);


ALTER TABLE public.evento_provedor_join OWNER TO siged;

--
-- TOC entry 236 (class 1259 OID 18540)
-- Name: evento_recomendar; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.evento_recomendar (
    id bigint NOT NULL,
    aprovado character varying(255),
    carga_horaria character varying(255) NOT NULL,
    data_cadastro timestamp without time zone,
    data_fim date NOT NULL,
    data_inicio date NOT NULL,
    justificativa character varying(2000),
    justificativachefe character varying(2000),
    material bytea,
    material_nome character varying(255),
    material_tipo character varying(255),
    objetivo character varying(255) NOT NULL,
    observacao character varying(2000),
    titulo character varying(255) NOT NULL,
    valor numeric(19,2),
    cidade_id bigint,
    pais_id bigint,
    setor_id bigint
);


ALTER TABLE public.evento_recomendar OWNER TO siged;

--
-- TOC entry 237 (class 1259 OID 18547)
-- Name: evento_tipopublico; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.evento_tipopublico (
    evento_id bigint NOT NULL,
    publico_alvo_id bigint NOT NULL
);


ALTER TABLE public.evento_tipopublico OWNER TO siged;

--
-- TOC entry 238 (class 1259 OID 18550)
-- Name: fonte_gasto; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.fonte_gasto (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.fonte_gasto OWNER TO siged;

--
-- TOC entry 239 (class 1259 OID 18555)
-- Name: formacao_academica; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.formacao_academica (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.formacao_academica OWNER TO siged;

--
-- TOC entry 240 (class 1259 OID 18560)
-- Name: frequencia; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.frequencia (
    id bigint NOT NULL,
    data timestamp without time zone NOT NULL,
    participanteid bytea,
    turno character varying(255) NOT NULL,
    evento_id bigint NOT NULL,
    modulo_id bigint NOT NULL
);


ALTER TABLE public.frequencia OWNER TO siged;

--
-- TOC entry 241 (class 1259 OID 18567)
-- Name: frequencia_participante; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.frequencia_participante (
    frequencia_id bigint NOT NULL,
    participante_id bigint NOT NULL
);


ALTER TABLE public.frequencia_participante OWNER TO siged;

--
-- TOC entry 242 (class 1259 OID 18570)
-- Name: gasto; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.gasto (
    id bigint NOT NULL,
    data_empenho timestamp without time zone,
    numero_empenho character varying(255),
    numero_processo character varying(255),
    observacao character varying(2000),
    valor numeric(19,2) NOT NULL,
    evento_id bigint NOT NULL,
    fonte_id bigint NOT NULL,
    instrutor_id bigint,
    participante_id bigint,
    tipo_id bigint NOT NULL
);


ALTER TABLE public.gasto OWNER TO siged;

--
-- TOC entry 243 (class 1259 OID 18577)
-- Name: inscricao; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.inscricao (
    id bigint NOT NULL,
    confirmada character varying(255),
    data timestamp without time zone,
    data_confirmacao timestamp without time zone,
    data_indicacao timestamp without time zone,
    indicada character varying(255),
    justificativa character varying(2000),
    justificativaparticipante character varying(2000),
    justificativachefe character varying(255),
    justificativanaoconfirmacao character varying(255),
    chefe_id bigint,
    evento_id bigint NOT NULL,
    participante_id bigint NOT NULL,
    solicitacao_id bigint
);


ALTER TABLE public.inscricao OWNER TO siged;

--
-- TOC entry 244 (class 1259 OID 18584)
-- Name: instrutor; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.instrutor (
    id bigint NOT NULL,
    assinatura bytea,
    assinatura_nome character varying(255),
    assinatura_tipo character varying(255),
    bairro character varying(255),
    celular character varying(255),
    cep character varying(255),
    complemento character varying(255),
    cpf character varying(255),
    curriculo bytea,
    curriculo_nome character varying(255),
    curriculo_tipo character varying(255),
    data_cadastro timestamp without time zone,
    data_nascimento timestamp without time zone,
    email character varying(255) NOT NULL,
    instituicao character varying(255),
    logradouro character varying(255),
    nome character varying(255) NOT NULL,
    numero character varying(255),
    observacao character varying(2000),
    perfil character varying(2000),
    projeto bytea,
    projeto_nome character varying(255),
    projeto_tipo character varying(255),
    sexo character varying(255) NOT NULL,
    telefone character varying(255) NOT NULL,
    formacao_academica_id bigint,
    cidade_id bigint,
    nivel_escolaridade_id bigint,
    pais_id bigint,
    setor_id bigint,
    situacao_id bigint,
    tipo_id bigint NOT NULL
);


ALTER TABLE public.instrutor OWNER TO siged;

--
-- TOC entry 245 (class 1259 OID 18591)
-- Name: meta_desempenho_produtividade; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.meta_desempenho_produtividade (
    id bigint NOT NULL,
    ano integer NOT NULL,
    ind_avaliacao_reacao numeric(19,2) NOT NULL,
    ind_capacitacao numeric(19,2) NOT NULL,
    semestre integer NOT NULL,
    CONSTRAINT meta_desempenho_produtividade_ano_check CHECK ((ano >= 2012)),
    CONSTRAINT meta_desempenho_produtividade_ind_avaliacao_reacao_check CHECK ((ind_avaliacao_reacao <= (100)::numeric)),
    CONSTRAINT meta_desempenho_produtividade_ind_capacitacao_check CHECK ((ind_capacitacao >= (0)::numeric)),
    CONSTRAINT meta_desempenho_produtividade_semestre_check CHECK ((semestre <= 2))
);


ALTER TABLE public.meta_desempenho_produtividade OWNER TO siged;

--
-- TOC entry 246 (class 1259 OID 18600)
-- Name: meta_planejamento_estrategico; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.meta_planejamento_estrategico (
    id bigint NOT NULL,
    ano integer NOT NULL,
    perc_acoes_plano numeric(19,2) NOT NULL,
    perc_juri_capacitados numeric(19,2) NOT NULL,
    perc_serv_capacitados numeric(19,2) NOT NULL,
    qtd_acoes integer NOT NULL,
    CONSTRAINT meta_planejamento_estrategico_ano_check CHECK ((ano >= 2012)),
    CONSTRAINT meta_planejamento_estrategico_perc_acoes_plano_check CHECK ((perc_acoes_plano >= (0)::numeric)),
    CONSTRAINT meta_planejamento_estrategico_perc_juri_capacitados_check CHECK ((perc_juri_capacitados >= (0)::numeric)),
    CONSTRAINT meta_planejamento_estrategico_perc_serv_capacitados_check CHECK ((perc_serv_capacitados <= (100)::numeric)),
    CONSTRAINT meta_planejamento_estrategico_qtd_acoes_check CHECK ((qtd_acoes >= 0))
);


ALTER TABLE public.meta_planejamento_estrategico OWNER TO siged;

--
-- TOC entry 247 (class 1259 OID 18610)
-- Name: modalidade; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.modalidade (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.modalidade OWNER TO siged;

--
-- TOC entry 248 (class 1259 OID 18615)
-- Name: modulo; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.modulo (
    id bigint NOT NULL,
    carga_horaria character varying(255) NOT NULL,
    cidade_pais character varying(255) NOT NULL,
    data_cadastro timestamp without time zone,
    data_fim timestamp without time zone NOT NULL,
    data_inicio timestamp without time zone NOT NULL,
    frequencia character varying(255) NOT NULL,
    hora_fim_turno1 character varying(255),
    hora_fim_turno2 character varying(255),
    hora_fim_turno3 character varying(255),
    hora_inicio_turno1 character varying(255),
    hora_inicio_turno2 character varying(255),
    hora_inicio_turno3 character varying(255),
    nota character varying(255),
    observacao character varying(2000),
    titulo character varying(255) NOT NULL,
    evento_id bigint NOT NULL,
    localizacao_id bigint NOT NULL,
    modalidade_id bigint NOT NULL,
    cidade_id bigint,
    pais_id bigint
);


ALTER TABLE public.modulo OWNER TO siged;

--
-- TOC entry 249 (class 1259 OID 18622)
-- Name: modulo_instrutor; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.modulo_instrutor (
    modulo_id bigint NOT NULL,
    instrutor_id bigint NOT NULL
);


ALTER TABLE public.modulo_instrutor OWNER TO siged;

--
-- TOC entry 250 (class 1259 OID 18625)
-- Name: nivel_escolaridade; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.nivel_escolaridade (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.nivel_escolaridade OWNER TO siged;

--
-- TOC entry 251 (class 1259 OID 18631)
-- Name: nota; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.nota (
    id bigint NOT NULL,
    nota character varying(255) NOT NULL,
    evento_id bigint NOT NULL,
    modulo_id bigint NOT NULL,
    participante_id bigint NOT NULL
);


ALTER TABLE public.nota OWNER TO siged;

--
-- TOC entry 216 (class 1259 OID 18432)
-- Name: nota_questao; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.nota_questao (
    id bigint NOT NULL,
    descricao character varying(255)
);


ALTER TABLE public.nota_questao OWNER TO siged;

--
-- TOC entry 252 (class 1259 OID 18636)
-- Name: pais; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.pais (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.pais OWNER TO siged;

--
-- TOC entry 253 (class 1259 OID 18641)
-- Name: parametro; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.parametro (
    id bigint NOT NULL,
    descricao character varying(255),
    nome character varying(255),
    valor character varying(255)
);


ALTER TABLE public.parametro OWNER TO siged;

--
-- TOC entry 254 (class 1259 OID 18648)
-- Name: participante; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.participante (
    id bigint NOT NULL,
    cargo character varying(255),
    celular character varying(255),
    cpf character varying(255) NOT NULL,
    data_cadastro timestamp without time zone,
    email character varying(255) NOT NULL,
    entidade character varying(255),
    lotacao character varying(255),
    matricula character varying(255),
    nome character varying(255) NOT NULL,
    observacao character varying(2000),
    profissao character varying(70),
    responsavelevento boolean,
    telefone character varying(255),
    cidade_id bigint,
    escolaridade_id bigint,
    formacao_id bigint,
    municipio_id bigint,
    orgao_id bigint,
    pais_id bigint,
    setor_id bigint,
    tipo_id bigint
);


ALTER TABLE public.participante OWNER TO siged;

--
-- TOC entry 255 (class 1259 OID 18657)
-- Name: programa; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.programa (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.programa OWNER TO siged;

--
-- TOC entry 256 (class 1259 OID 18662)
-- Name: provedor_evento; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.provedor_evento (
    id bigint NOT NULL,
    agencia character varying(255),
    bairro character varying(255),
    banco character varying(255),
    celular character varying(255),
    cep character varying(255),
    cnpj character varying(255),
    complemento character varying(255),
    conta_corrente character varying(255),
    contato character varying(255),
    descricao character varying(255) NOT NULL,
    email character varying(255),
    logradouro character varying(255),
    numero character varying(255),
    telefone character varying(255),
    cidade_id bigint,
    pais_id bigint
);


ALTER TABLE public.provedor_evento OWNER TO siged;

--
-- TOC entry 217 (class 1259 OID 18437)
-- Name: questao; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.questao (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL,
    ordem bigint NOT NULL,
    tipo_id bigint NOT NULL
);


ALTER TABLE public.questao OWNER TO siged;

--
-- TOC entry 218 (class 1259 OID 18442)
-- Name: questao_modalidade; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.questao_modalidade (
    questao_id bigint NOT NULL,
    modalidade_id bigint NOT NULL
);


ALTER TABLE public.questao_modalidade OWNER TO siged;

--
-- TOC entry 219 (class 1259 OID 18445)
-- Name: responsavel_setor; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.responsavel_setor (
    id bigint NOT NULL,
    fl_responsavel_ativo boolean,
    responsavel_id bigint,
    responsavel_imediato_id bigint,
    setor_id bigint
);


ALTER TABLE public.responsavel_setor OWNER TO siged;

--
-- TOC entry 257 (class 1259 OID 18669)
-- Name: sapjava_entidade; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.sapjava_entidade (
    identidade bigint NOT NULL,
    cnpj character varying(255),
    dsbairroentidade character varying(255),
    dscepentidade character varying(255),
    dscidadeentidade character varying(255),
    dsemailentidade character varying(255),
    dsenderecoentidade character varying(255),
    dsentidade character varying(255),
    dsentidadesigla character varying(255),
    dsestadoentidade character varying(255),
    dsfaxentidade character varying(255),
    dsfoneentidade character varying(255),
    dssenha character varying(255),
    fiscalizado integer,
    idsetor integer,
    idsetororigem integer,
    tpentidadeesfera integer,
    idlocalidade bigint
);


ALTER TABLE public.sapjava_entidade OWNER TO siged;

--
-- TOC entry 258 (class 1259 OID 18676)
-- Name: sapjava_localidade; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.sapjava_localidade (
    idlocalidade bigint NOT NULL,
    dslocalidade character varying(255)
);


ALTER TABLE public.sapjava_localidade OWNER TO siged;

--
-- TOC entry 259 (class 1259 OID 18681)
-- Name: sapjava_setor; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.sapjava_setor (
    idsetor bigint NOT NULL,
    flativo integer,
    nmsetor character varying(255),
    idsetorsuperior integer,
    dsabreviaturasetor character varying(255),
    tparea integer,
    tpsetor bigint
);


ALTER TABLE public.sapjava_setor OWNER TO siged;

--
-- TOC entry 260 (class 1259 OID 18688)
-- Name: sca_grupo; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.sca_grupo (
    id bigint NOT NULL,
    nome character varying(255),
    sistema integer
);


ALTER TABLE public.sca_grupo OWNER TO siged;

--
-- TOC entry 261 (class 1259 OID 18693)
-- Name: sca_usuario; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.sca_usuario (
    id bigint NOT NULL,
    ativo boolean,
    cpf character varying(255),
    dataalteracao date,
    datainclusao date,
    email character varying(255),
    login character varying(255),
    nome character varying(255),
    observacao character varying(2000),
    senha character varying(255),
    senhaexpirada boolean,
    tipo integer
);


ALTER TABLE public.sca_usuario OWNER TO siged;

--
-- TOC entry 274 (class 1259 OID 19266)
-- Name: seq_area_conhecimento; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_area_conhecimento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_area_conhecimento OWNER TO siged;

--
-- TOC entry 275 (class 1259 OID 19267)
-- Name: seq_avaliacao; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_avaliacao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_avaliacao OWNER TO siged;

--
-- TOC entry 276 (class 1259 OID 19268)
-- Name: seq_avaliacao_eficacia; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_avaliacao_eficacia
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_avaliacao_eficacia OWNER TO siged;

--
-- TOC entry 277 (class 1259 OID 19269)
-- Name: seq_avaliacao_reacao; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_avaliacao_reacao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_avaliacao_reacao OWNER TO siged;

--
-- TOC entry 278 (class 1259 OID 19270)
-- Name: seq_avaliacao_reacao_nota; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_avaliacao_reacao_nota
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_avaliacao_reacao_nota OWNER TO siged;

--
-- TOC entry 279 (class 1259 OID 19271)
-- Name: seq_certificado; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_certificado
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_certificado OWNER TO siged;

--
-- TOC entry 280 (class 1259 OID 19272)
-- Name: seq_certificado_emitido; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_certificado_emitido
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_certificado_emitido OWNER TO siged;

--
-- TOC entry 281 (class 1259 OID 19273)
-- Name: seq_cidade; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_cidade
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_cidade OWNER TO siged;

--
-- TOC entry 282 (class 1259 OID 19274)
-- Name: seq_desempenho; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_desempenho
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_desempenho OWNER TO siged;

--
-- TOC entry 283 (class 1259 OID 19275)
-- Name: seq_eixo_tematico; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_eixo_tematico
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_eixo_tematico OWNER TO siged;

--
-- TOC entry 284 (class 1259 OID 19276)
-- Name: seq_evento; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_evento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_evento OWNER TO siged;

--
-- TOC entry 285 (class 1259 OID 19277)
-- Name: seq_evento_extra; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_evento_extra
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_evento_extra OWNER TO siged;

--
-- TOC entry 286 (class 1259 OID 19278)
-- Name: seq_evento_historico; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_evento_historico
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_evento_historico OWNER TO siged;

--
-- TOC entry 287 (class 1259 OID 19279)
-- Name: seq_evento_recomendar; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_evento_recomendar
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_evento_recomendar OWNER TO siged;

--
-- TOC entry 288 (class 1259 OID 19280)
-- Name: seq_fonte_gasto; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_fonte_gasto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_fonte_gasto OWNER TO siged;

--
-- TOC entry 289 (class 1259 OID 19281)
-- Name: seq_formacao_academica; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_formacao_academica
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_formacao_academica OWNER TO siged;

--
-- TOC entry 290 (class 1259 OID 19282)
-- Name: seq_frequencia; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_frequencia
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_frequencia OWNER TO siged;

--
-- TOC entry 291 (class 1259 OID 19283)
-- Name: seq_gasto; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_gasto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_gasto OWNER TO siged;

--
-- TOC entry 292 (class 1259 OID 19284)
-- Name: seq_grupo; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_grupo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_grupo OWNER TO siged;

--
-- TOC entry 293 (class 1259 OID 19285)
-- Name: seq_inscricao; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_inscricao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_inscricao OWNER TO siged;

--
-- TOC entry 294 (class 1259 OID 19286)
-- Name: seq_instrutor; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_instrutor
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_instrutor OWNER TO siged;

--
-- TOC entry 295 (class 1259 OID 19287)
-- Name: seq_material_didatico; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_material_didatico
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_material_didatico OWNER TO siged;

--
-- TOC entry 296 (class 1259 OID 19288)
-- Name: seq_meta_desempenho; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_meta_desempenho
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_meta_desempenho OWNER TO siged;

--
-- TOC entry 297 (class 1259 OID 19289)
-- Name: seq_meta_planejamento; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_meta_planejamento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_meta_planejamento OWNER TO siged;

--
-- TOC entry 298 (class 1259 OID 19290)
-- Name: seq_modalidade; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_modalidade
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_modalidade OWNER TO siged;

--
-- TOC entry 299 (class 1259 OID 19291)
-- Name: seq_modulo; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_modulo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_modulo OWNER TO siged;

--
-- TOC entry 300 (class 1259 OID 19292)
-- Name: seq_nivel_escolaridade; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_nivel_escolaridade
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_nivel_escolaridade OWNER TO siged;

--
-- TOC entry 301 (class 1259 OID 19293)
-- Name: seq_nota; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_nota
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_nota OWNER TO siged;

--
-- TOC entry 302 (class 1259 OID 19294)
-- Name: seq_nota_questao; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_nota_questao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_nota_questao OWNER TO siged;

--
-- TOC entry 303 (class 1259 OID 19295)
-- Name: seq_pais; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_pais
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_pais OWNER TO siged;

--
-- TOC entry 304 (class 1259 OID 19296)
-- Name: seq_participante; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_participante
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_participante OWNER TO siged;

--
-- TOC entry 305 (class 1259 OID 19297)
-- Name: seq_programa; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_programa
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_programa OWNER TO siged;

--
-- TOC entry 306 (class 1259 OID 19298)
-- Name: seq_provedor_evento; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_provedor_evento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_provedor_evento OWNER TO siged;

--
-- TOC entry 307 (class 1259 OID 19299)
-- Name: seq_questao; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_questao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_questao OWNER TO siged;

--
-- TOC entry 308 (class 1259 OID 19300)
-- Name: seq_responsavel_setor; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_responsavel_setor
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_responsavel_setor OWNER TO siged;

--
-- TOC entry 309 (class 1259 OID 19301)
-- Name: seq_situacao_instrutor; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_situacao_instrutor
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_situacao_instrutor OWNER TO siged;

--
-- TOC entry 310 (class 1259 OID 19302)
-- Name: seq_situacao_modulo; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_situacao_modulo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_situacao_modulo OWNER TO siged;

--
-- TOC entry 311 (class 1259 OID 19303)
-- Name: seq_tipo_area_tribunal; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_tipo_area_tribunal
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_tipo_area_tribunal OWNER TO siged;

--
-- TOC entry 312 (class 1259 OID 19304)
-- Name: seq_tipo_evento; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_tipo_evento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_tipo_evento OWNER TO siged;

--
-- TOC entry 313 (class 1259 OID 19305)
-- Name: seq_tipo_gasto; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_tipo_gasto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_tipo_gasto OWNER TO siged;

--
-- TOC entry 314 (class 1259 OID 19306)
-- Name: seq_tipo_instrutor; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_tipo_instrutor
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_tipo_instrutor OWNER TO siged;

--
-- TOC entry 315 (class 1259 OID 19307)
-- Name: seq_tipo_localizacao_evento; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_tipo_localizacao_evento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_tipo_localizacao_evento OWNER TO siged;

--
-- TOC entry 316 (class 1259 OID 19308)
-- Name: seq_tipo_publico_alvo; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_tipo_publico_alvo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_tipo_publico_alvo OWNER TO siged;

--
-- TOC entry 317 (class 1259 OID 19309)
-- Name: seq_tipo_questao; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_tipo_questao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_tipo_questao OWNER TO siged;

--
-- TOC entry 318 (class 1259 OID 19310)
-- Name: seq_uf; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_uf
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_uf OWNER TO siged;

--
-- TOC entry 319 (class 1259 OID 19311)
-- Name: seq_usuario; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.seq_usuario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_usuario OWNER TO siged;

--
-- TOC entry 262 (class 1259 OID 18700)
-- Name: situacao_instrutor; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.situacao_instrutor (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.situacao_instrutor OWNER TO siged;

--
-- TOC entry 263 (class 1259 OID 18705)
-- Name: situacao_modulo; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.situacao_modulo (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.situacao_modulo OWNER TO siged;

--
-- TOC entry 264 (class 1259 OID 18710)
-- Name: srh_tb_municipio; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.srh_tb_municipio (
    id bigint NOT NULL,
    nome character varying(255),
    uf character varying(25) NOT NULL
);


ALTER TABLE public.srh_tb_municipio OWNER TO siged;

--
-- TOC entry 323 (class 1259 OID 19321)
-- Name: srh_tb_ocupacao; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.srh_tb_ocupacao (
    id integer NOT NULL,
    descricao character varying(50) NOT NULL,
    tipoocupacao integer
);


ALTER TABLE public.srh_tb_ocupacao OWNER TO siged;

--
-- TOC entry 322 (class 1259 OID 19320)
-- Name: srh_tb_ocupacao_id_seq; Type: SEQUENCE; Schema: public; Owner: siged
--

CREATE SEQUENCE public.srh_tb_ocupacao_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.srh_tb_ocupacao_id_seq OWNER TO siged;

--
-- TOC entry 3852 (class 0 OID 0)
-- Dependencies: 322
-- Name: srh_tb_ocupacao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: siged
--

ALTER SEQUENCE public.srh_tb_ocupacao_id_seq OWNED BY public.srh_tb_ocupacao.id;


--
-- TOC entry 325 (class 1259 OID 19332)
-- Name: srh_tb_pessoal; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.srh_tb_pessoal (
    id integer NOT NULL,
    cpf character varying(15),
    celular character varying(15),
    email character varying(200),
    nomecompleto character varying(200),
    telefone character varying(15)
);


ALTER TABLE public.srh_tb_pessoal OWNER TO siged;

--
-- TOC entry 324 (class 1259 OID 19327)
-- Name: srh_tb_tipoocupacao; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.srh_tb_tipoocupacao (
    id integer NOT NULL
);


ALTER TABLE public.srh_tb_tipoocupacao OWNER TO siged;

--
-- TOC entry 265 (class 1259 OID 18715)
-- Name: srh_tb_uf; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.srh_tb_uf (
    uf character varying(25) NOT NULL,
    nome character varying(255)
);


ALTER TABLE public.srh_tb_uf OWNER TO siged;

--
-- TOC entry 266 (class 1259 OID 18720)
-- Name: tipo_area_tribunal; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.tipo_area_tribunal (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.tipo_area_tribunal OWNER TO siged;

--
-- TOC entry 267 (class 1259 OID 18725)
-- Name: tipo_evento; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.tipo_evento (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.tipo_evento OWNER TO siged;

--
-- TOC entry 268 (class 1259 OID 18730)
-- Name: tipo_gasto; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.tipo_gasto (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.tipo_gasto OWNER TO siged;

--
-- TOC entry 269 (class 1259 OID 18735)
-- Name: tipo_instrutor; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.tipo_instrutor (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.tipo_instrutor OWNER TO siged;

--
-- TOC entry 270 (class 1259 OID 18740)
-- Name: tipo_localizacao_evento; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.tipo_localizacao_evento (
    id bigint NOT NULL,
    cidade_pais character varying(255),
    descricao character varying(255) NOT NULL,
    endereco character varying(255),
    tpmodalidade integer NOT NULL,
    cidade_id bigint,
    pais_id bigint
);


ALTER TABLE public.tipo_localizacao_evento OWNER TO siged;

--
-- TOC entry 271 (class 1259 OID 18747)
-- Name: tipo_publico_alvo; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.tipo_publico_alvo (
    id bigint NOT NULL,
    abreviacao character varying(255),
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.tipo_publico_alvo OWNER TO siged;

--
-- TOC entry 220 (class 1259 OID 18450)
-- Name: tipo_questao; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.tipo_questao (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.tipo_questao OWNER TO siged;

--
-- TOC entry 272 (class 1259 OID 18754)
-- Name: uf; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.uf (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL,
    pais_id bigint NOT NULL
);


ALTER TABLE public.uf OWNER TO siged;

--
-- TOC entry 273 (class 1259 OID 18759)
-- Name: usuario; Type: TABLE; Schema: public; Owner: siged
--

CREATE TABLE public.usuario (
    id bigint NOT NULL,
    ativo boolean,
    cpf character varying(255),
    dataalteracao date,
    datainclusao date,
    email character varying(255),
    forcaratualizacaoemail boolean,
    nome character varying(255),
    observacao character varying(2000),
    senha character varying(255),
    senhaexpirada boolean,
    tipo integer,
    login character varying(255)
);


ALTER TABLE public.usuario OWNER TO siged;

--
-- TOC entry 3475 (class 2604 OID 19317)
-- Name: srh_tb_funcional id; Type: DEFAULT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.srh_tb_funcional ALTER COLUMN id SET DEFAULT nextval('public."SRH_TB_FUNCIONAL_id_seq"'::regclass);


--
-- TOC entry 3476 (class 2604 OID 19324)
-- Name: srh_tb_ocupacao id; Type: DEFAULT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.srh_tb_ocupacao ALTER COLUMN id SET DEFAULT nextval('public.srh_tb_ocupacao_id_seq'::regclass);


--
-- TOC entry 3597 (class 2606 OID 19319)
-- Name: srh_tb_funcional SRH_TB_FUNCIONAL_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.srh_tb_funcional
    ADD CONSTRAINT "SRH_TB_FUNCIONAL_pkey" PRIMARY KEY (id);


--
-- TOC entry 3499 (class 2606 OID 18459)
-- Name: area_conhecimento area_conhecimento_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.area_conhecimento
    ADD CONSTRAINT area_conhecimento_pkey PRIMARY KEY (id);


--
-- TOC entry 3503 (class 2606 OID 18473)
-- Name: avaliacao_eficacia avaliacao_eficacia_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao_eficacia
    ADD CONSTRAINT avaliacao_eficacia_pkey PRIMARY KEY (id);


--
-- TOC entry 3501 (class 2606 OID 18466)
-- Name: avaliacao avaliacao_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao
    ADD CONSTRAINT avaliacao_pkey PRIMARY KEY (id);


--
-- TOC entry 3489 (class 2606 OID 18431)
-- Name: avaliacao_reacao_nota avaliacao_reacao_nota_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao_reacao_nota
    ADD CONSTRAINT avaliacao_reacao_nota_pkey PRIMARY KEY (id);


--
-- TOC entry 3487 (class 2606 OID 18426)
-- Name: avaliacao_reacao avaliacao_reacao_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao_reacao
    ADD CONSTRAINT avaliacao_reacao_pkey PRIMARY KEY (id);


--
-- TOC entry 3507 (class 2606 OID 18485)
-- Name: certificado_emitido certificado_emitido_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.certificado_emitido
    ADD CONSTRAINT certificado_emitido_pkey PRIMARY KEY (id);


--
-- TOC entry 3505 (class 2606 OID 18480)
-- Name: certificado certificado_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.certificado
    ADD CONSTRAINT certificado_pkey PRIMARY KEY (id);


--
-- TOC entry 3509 (class 2606 OID 18490)
-- Name: cidade cidade_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.cidade
    ADD CONSTRAINT cidade_pkey PRIMARY KEY (id);


--
-- TOC entry 3511 (class 2606 OID 18497)
-- Name: desempenho desempenho_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.desempenho
    ADD CONSTRAINT desempenho_pkey PRIMARY KEY (id);


--
-- TOC entry 3513 (class 2606 OID 18502)
-- Name: eixo_tematico eixo_tematico_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.eixo_tematico
    ADD CONSTRAINT eixo_tematico_pkey PRIMARY KEY (id);


--
-- TOC entry 3517 (class 2606 OID 18516)
-- Name: evento_extra evento_extra_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_extra
    ADD CONSTRAINT evento_extra_pkey PRIMARY KEY (id);


--
-- TOC entry 3519 (class 2606 OID 18521)
-- Name: evento_historico evento_historico_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_historico
    ADD CONSTRAINT evento_historico_pkey PRIMARY KEY (id);


--
-- TOC entry 3521 (class 2606 OID 18531)
-- Name: evento_material evento_material_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_material
    ADD CONSTRAINT evento_material_pkey PRIMARY KEY (id);


--
-- TOC entry 3515 (class 2606 OID 18509)
-- Name: evento evento_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento
    ADD CONSTRAINT evento_pkey PRIMARY KEY (id);


--
-- TOC entry 3523 (class 2606 OID 18539)
-- Name: evento_provedor_join evento_provedor_join_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_provedor_join
    ADD CONSTRAINT evento_provedor_join_pkey PRIMARY KEY (evento_id, provedor_id);


--
-- TOC entry 3525 (class 2606 OID 18546)
-- Name: evento_recomendar evento_recomendar_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_recomendar
    ADD CONSTRAINT evento_recomendar_pkey PRIMARY KEY (id);


--
-- TOC entry 3527 (class 2606 OID 18554)
-- Name: fonte_gasto fonte_gasto_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.fonte_gasto
    ADD CONSTRAINT fonte_gasto_pkey PRIMARY KEY (id);


--
-- TOC entry 3529 (class 2606 OID 18559)
-- Name: formacao_academica formacao_academica_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.formacao_academica
    ADD CONSTRAINT formacao_academica_pkey PRIMARY KEY (id);


--
-- TOC entry 3531 (class 2606 OID 18566)
-- Name: frequencia frequencia_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.frequencia
    ADD CONSTRAINT frequencia_pkey PRIMARY KEY (id);


--
-- TOC entry 3533 (class 2606 OID 18576)
-- Name: gasto gasto_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.gasto
    ADD CONSTRAINT gasto_pkey PRIMARY KEY (id);


--
-- TOC entry 3535 (class 2606 OID 18583)
-- Name: inscricao inscricao_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.inscricao
    ADD CONSTRAINT inscricao_pkey PRIMARY KEY (id);


--
-- TOC entry 3537 (class 2606 OID 18590)
-- Name: instrutor instrutor_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.instrutor
    ADD CONSTRAINT instrutor_pkey PRIMARY KEY (id);


--
-- TOC entry 3539 (class 2606 OID 18599)
-- Name: meta_desempenho_produtividade meta_desempenho_produtividade_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.meta_desempenho_produtividade
    ADD CONSTRAINT meta_desempenho_produtividade_pkey PRIMARY KEY (id);


--
-- TOC entry 3541 (class 2606 OID 18609)
-- Name: meta_planejamento_estrategico meta_planejamento_estrategico_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.meta_planejamento_estrategico
    ADD CONSTRAINT meta_planejamento_estrategico_pkey PRIMARY KEY (id);


--
-- TOC entry 3543 (class 2606 OID 18614)
-- Name: modalidade modalidade_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.modalidade
    ADD CONSTRAINT modalidade_pkey PRIMARY KEY (id);


--
-- TOC entry 3545 (class 2606 OID 18621)
-- Name: modulo modulo_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.modulo
    ADD CONSTRAINT modulo_pkey PRIMARY KEY (id);


--
-- TOC entry 3547 (class 2606 OID 18629)
-- Name: nivel_escolaridade nivel_escolaridade_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.nivel_escolaridade
    ADD CONSTRAINT nivel_escolaridade_pkey PRIMARY KEY (id);


--
-- TOC entry 3549 (class 2606 OID 18635)
-- Name: nota nota_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.nota
    ADD CONSTRAINT nota_pkey PRIMARY KEY (id);


--
-- TOC entry 3491 (class 2606 OID 18436)
-- Name: nota_questao nota_questao_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.nota_questao
    ADD CONSTRAINT nota_questao_pkey PRIMARY KEY (id);


--
-- TOC entry 3551 (class 2606 OID 18640)
-- Name: pais pais_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.pais
    ADD CONSTRAINT pais_pkey PRIMARY KEY (id);


--
-- TOC entry 3553 (class 2606 OID 18647)
-- Name: parametro parametro_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.parametro
    ADD CONSTRAINT parametro_pkey PRIMARY KEY (id);


--
-- TOC entry 3555 (class 2606 OID 18656)
-- Name: participante participante_cpf_key; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.participante
    ADD CONSTRAINT participante_cpf_key UNIQUE (cpf);


--
-- TOC entry 3557 (class 2606 OID 18654)
-- Name: participante participante_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.participante
    ADD CONSTRAINT participante_pkey PRIMARY KEY (id);


--
-- TOC entry 3559 (class 2606 OID 18661)
-- Name: programa programa_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.programa
    ADD CONSTRAINT programa_pkey PRIMARY KEY (id);


--
-- TOC entry 3561 (class 2606 OID 18668)
-- Name: provedor_evento provedor_evento_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.provedor_evento
    ADD CONSTRAINT provedor_evento_pkey PRIMARY KEY (id);


--
-- TOC entry 3493 (class 2606 OID 18441)
-- Name: questao questao_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.questao
    ADD CONSTRAINT questao_pkey PRIMARY KEY (id);


--
-- TOC entry 3495 (class 2606 OID 18449)
-- Name: responsavel_setor responsavel_setor_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.responsavel_setor
    ADD CONSTRAINT responsavel_setor_pkey PRIMARY KEY (id);


--
-- TOC entry 3563 (class 2606 OID 18675)
-- Name: sapjava_entidade sapjava_entidade_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.sapjava_entidade
    ADD CONSTRAINT sapjava_entidade_pkey PRIMARY KEY (identidade);


--
-- TOC entry 3565 (class 2606 OID 18680)
-- Name: sapjava_localidade sapjava_localidade_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.sapjava_localidade
    ADD CONSTRAINT sapjava_localidade_pkey PRIMARY KEY (idlocalidade);


--
-- TOC entry 3567 (class 2606 OID 18687)
-- Name: sapjava_setor sapjava_setor_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.sapjava_setor
    ADD CONSTRAINT sapjava_setor_pkey PRIMARY KEY (idsetor);


--
-- TOC entry 3569 (class 2606 OID 18692)
-- Name: sca_grupo sca_grupo_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.sca_grupo
    ADD CONSTRAINT sca_grupo_pkey PRIMARY KEY (id);


--
-- TOC entry 3571 (class 2606 OID 18699)
-- Name: sca_usuario sca_usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.sca_usuario
    ADD CONSTRAINT sca_usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 3573 (class 2606 OID 18704)
-- Name: situacao_instrutor situacao_instrutor_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.situacao_instrutor
    ADD CONSTRAINT situacao_instrutor_pkey PRIMARY KEY (id);


--
-- TOC entry 3575 (class 2606 OID 18709)
-- Name: situacao_modulo situacao_modulo_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.situacao_modulo
    ADD CONSTRAINT situacao_modulo_pkey PRIMARY KEY (id);


--
-- TOC entry 3577 (class 2606 OID 18714)
-- Name: srh_tb_municipio srh_tb_municipio_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.srh_tb_municipio
    ADD CONSTRAINT srh_tb_municipio_pkey PRIMARY KEY (id);


--
-- TOC entry 3599 (class 2606 OID 19326)
-- Name: srh_tb_ocupacao srh_tb_ocupacao_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.srh_tb_ocupacao
    ADD CONSTRAINT srh_tb_ocupacao_pkey PRIMARY KEY (id);


--
-- TOC entry 3603 (class 2606 OID 19336)
-- Name: srh_tb_pessoal srh_tb_pessoal_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.srh_tb_pessoal
    ADD CONSTRAINT srh_tb_pessoal_pkey PRIMARY KEY (id);


--
-- TOC entry 3601 (class 2606 OID 19331)
-- Name: srh_tb_tipoocupacao srh_tb_tipoocupacao_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.srh_tb_tipoocupacao
    ADD CONSTRAINT srh_tb_tipoocupacao_pkey PRIMARY KEY (id);


--
-- TOC entry 3579 (class 2606 OID 18719)
-- Name: srh_tb_uf srh_tb_uf_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.srh_tb_uf
    ADD CONSTRAINT srh_tb_uf_pkey PRIMARY KEY (uf);


--
-- TOC entry 3581 (class 2606 OID 18724)
-- Name: tipo_area_tribunal tipo_area_tribunal_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.tipo_area_tribunal
    ADD CONSTRAINT tipo_area_tribunal_pkey PRIMARY KEY (id);


--
-- TOC entry 3583 (class 2606 OID 18729)
-- Name: tipo_evento tipo_evento_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.tipo_evento
    ADD CONSTRAINT tipo_evento_pkey PRIMARY KEY (id);


--
-- TOC entry 3585 (class 2606 OID 18734)
-- Name: tipo_gasto tipo_gasto_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.tipo_gasto
    ADD CONSTRAINT tipo_gasto_pkey PRIMARY KEY (id);


--
-- TOC entry 3587 (class 2606 OID 18739)
-- Name: tipo_instrutor tipo_instrutor_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.tipo_instrutor
    ADD CONSTRAINT tipo_instrutor_pkey PRIMARY KEY (id);


--
-- TOC entry 3589 (class 2606 OID 18746)
-- Name: tipo_localizacao_evento tipo_localizacao_evento_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.tipo_localizacao_evento
    ADD CONSTRAINT tipo_localizacao_evento_pkey PRIMARY KEY (id);


--
-- TOC entry 3591 (class 2606 OID 18753)
-- Name: tipo_publico_alvo tipo_publico_alvo_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.tipo_publico_alvo
    ADD CONSTRAINT tipo_publico_alvo_pkey PRIMARY KEY (id);


--
-- TOC entry 3497 (class 2606 OID 18454)
-- Name: tipo_questao tipo_questao_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.tipo_questao
    ADD CONSTRAINT tipo_questao_pkey PRIMARY KEY (id);


--
-- TOC entry 3593 (class 2606 OID 18758)
-- Name: uf uf_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.uf
    ADD CONSTRAINT uf_pkey PRIMARY KEY (id);


--
-- TOC entry 3595 (class 2606 OID 18765)
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 3655 (class 2606 OID 19026)
-- Name: evento_recomendar fk1ac1a2084886cc3e; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_recomendar
    ADD CONSTRAINT fk1ac1a2084886cc3e FOREIGN KEY (setor_id) REFERENCES public.sapjava_setor(idsetor);


--
-- TOC entry 3656 (class 2606 OID 19031)
-- Name: evento_recomendar fk1ac1a208c772aa7; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_recomendar
    ADD CONSTRAINT fk1ac1a208c772aa7 FOREIGN KEY (cidade_id) REFERENCES public.srh_tb_municipio(id);


--
-- TOC entry 3657 (class 2606 OID 19021)
-- Name: evento_recomendar fk1ac1a208f64bdda; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_recomendar
    ADD CONSTRAINT fk1ac1a208f64bdda FOREIGN KEY (pais_id) REFERENCES public.pais(id);


--
-- TOC entry 3646 (class 2606 OID 18976)
-- Name: evento_historico fk1c3f254631edb9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_historico
    ADD CONSTRAINT fk1c3f254631edb9a FOREIGN KEY (evento_id) REFERENCES public.evento(id);


--
-- TOC entry 3653 (class 2606 OID 19016)
-- Name: evento_provedor_join fk1da7dd9431edb9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_provedor_join
    ADD CONSTRAINT fk1da7dd9431edb9a FOREIGN KEY (evento_id) REFERENCES public.evento(id);


--
-- TOC entry 3654 (class 2606 OID 19011)
-- Name: evento_provedor_join fk1da7dd94a9ef2ccf; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_provedor_join
    ADD CONSTRAINT fk1da7dd94a9ef2ccf FOREIGN KEY (provedor_id) REFERENCES public.provedor_evento(id);


--
-- TOC entry 3651 (class 2606 OID 19006)
-- Name: evento_programa fk25056a4731edb9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_programa
    ADD CONSTRAINT fk25056a4731edb9a FOREIGN KEY (evento_id) REFERENCES public.evento(id);


--
-- TOC entry 3652 (class 2606 OID 19001)
-- Name: evento_programa fk25056a4771da769a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_programa
    ADD CONSTRAINT fk25056a4771da769a FOREIGN KEY (programa_id) REFERENCES public.programa(id);


--
-- TOC entry 3673 (class 2606 OID 19116)
-- Name: instrutor fk2aefdae31abd81f; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.instrutor
    ADD CONSTRAINT fk2aefdae31abd81f FOREIGN KEY (nivel_escolaridade_id) REFERENCES public.nivel_escolaridade(id);


--
-- TOC entry 3674 (class 2606 OID 19126)
-- Name: instrutor fk2aefdae4886cc3e; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.instrutor
    ADD CONSTRAINT fk2aefdae4886cc3e FOREIGN KEY (setor_id) REFERENCES public.sapjava_setor(idsetor);


--
-- TOC entry 3675 (class 2606 OID 19136)
-- Name: instrutor fk2aefdae7cbda07a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.instrutor
    ADD CONSTRAINT fk2aefdae7cbda07a FOREIGN KEY (situacao_id) REFERENCES public.situacao_instrutor(id);


--
-- TOC entry 3676 (class 2606 OID 19111)
-- Name: instrutor fk2aefdaec024cd11; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.instrutor
    ADD CONSTRAINT fk2aefdaec024cd11 FOREIGN KEY (formacao_academica_id) REFERENCES public.formacao_academica(id);


--
-- TOC entry 3677 (class 2606 OID 19131)
-- Name: instrutor fk2aefdaec6abc8e0; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.instrutor
    ADD CONSTRAINT fk2aefdaec6abc8e0 FOREIGN KEY (tipo_id) REFERENCES public.tipo_instrutor(id);


--
-- TOC entry 3678 (class 2606 OID 19141)
-- Name: instrutor fk2aefdaec772aa7; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.instrutor
    ADD CONSTRAINT fk2aefdaec772aa7 FOREIGN KEY (cidade_id) REFERENCES public.srh_tb_municipio(id);


--
-- TOC entry 3679 (class 2606 OID 19121)
-- Name: instrutor fk2aefdaef64bdda; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.instrutor
    ADD CONSTRAINT fk2aefdaef64bdda FOREIGN KEY (pais_id) REFERENCES public.pais(id);


--
-- TOC entry 3627 (class 2606 OID 18886)
-- Name: certificado_emitido fk2b64377331edb9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.certificado_emitido
    ADD CONSTRAINT fk2b64377331edb9a FOREIGN KEY (evento_id) REFERENCES public.evento(id);


--
-- TOC entry 3628 (class 2606 OID 18881)
-- Name: certificado_emitido fk2b643773cf416e7a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.certificado_emitido
    ADD CONSTRAINT fk2b643773cf416e7a FOREIGN KEY (participante_id) REFERENCES public.participante(id);


--
-- TOC entry 3622 (class 2606 OID 18861)
-- Name: avaliacao_eficacia fk2bfdd59b1ac60b59; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao_eficacia
    ADD CONSTRAINT fk2bfdd59b1ac60b59 FOREIGN KEY (responsavel_id) REFERENCES public.sca_usuario(id);


--
-- TOC entry 3623 (class 2606 OID 18866)
-- Name: avaliacao_eficacia fk2bfdd59b31edb9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao_eficacia
    ADD CONSTRAINT fk2bfdd59b31edb9a FOREIGN KEY (evento_id) REFERENCES public.evento(id);


--
-- TOC entry 3624 (class 2606 OID 18856)
-- Name: avaliacao_eficacia fk2bfdd59bcf416e7a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao_eficacia
    ADD CONSTRAINT fk2bfdd59bcf416e7a FOREIGN KEY (participante_id) REFERENCES public.participante(id);


--
-- TOC entry 3687 (class 2606 OID 19191)
-- Name: nota fk33afee31edb9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.nota
    ADD CONSTRAINT fk33afee31edb9a FOREIGN KEY (evento_id) REFERENCES public.evento(id);


--
-- TOC entry 3688 (class 2606 OID 19186)
-- Name: nota fk33afeeb1f8d3fa; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.nota
    ADD CONSTRAINT fk33afeeb1f8d3fa FOREIGN KEY (modulo_id) REFERENCES public.modulo(id);


--
-- TOC entry 3689 (class 2606 OID 19181)
-- Name: nota fk33afeecf416e7a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.nota
    ADD CONSTRAINT fk33afeecf416e7a FOREIGN KEY (participante_id) REFERENCES public.participante(id);


--
-- TOC entry 3630 (class 2606 OID 18906)
-- Name: desempenho fk35069be631edb9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.desempenho
    ADD CONSTRAINT fk35069be631edb9a FOREIGN KEY (evento_id) REFERENCES public.evento(id);


--
-- TOC entry 3631 (class 2606 OID 18901)
-- Name: desempenho fk35069be6b1f8d3fa; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.desempenho
    ADD CONSTRAINT fk35069be6b1f8d3fa FOREIGN KEY (modulo_id) REFERENCES public.modulo(id);


--
-- TOC entry 3632 (class 2606 OID 18896)
-- Name: desempenho fk35069be6cf416e7a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.desempenho
    ADD CONSTRAINT fk35069be6cf416e7a FOREIGN KEY (participante_id) REFERENCES public.participante(id);


--
-- TOC entry 3610 (class 2606 OID 18796)
-- Name: questao fk51d80070a5c99ac2; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.questao
    ADD CONSTRAINT fk51d80070a5c99ac2 FOREIGN KEY (tipo_id) REFERENCES public.tipo_questao(id);


--
-- TOC entry 3664 (class 2606 OID 19081)
-- Name: gasto fk5d94b1419b18c46; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.gasto
    ADD CONSTRAINT fk5d94b1419b18c46 FOREIGN KEY (tipo_id) REFERENCES public.tipo_gasto(id);


--
-- TOC entry 3665 (class 2606 OID 19071)
-- Name: gasto fk5d94b1430824cc2; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.gasto
    ADD CONSTRAINT fk5d94b1430824cc2 FOREIGN KEY (fonte_id) REFERENCES public.fonte_gasto(id);


--
-- TOC entry 3666 (class 2606 OID 19086)
-- Name: gasto fk5d94b1431edb9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.gasto
    ADD CONSTRAINT fk5d94b1431edb9a FOREIGN KEY (evento_id) REFERENCES public.evento(id);


--
-- TOC entry 3667 (class 2606 OID 19076)
-- Name: gasto fk5d94b143cf2cfa; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.gasto
    ADD CONSTRAINT fk5d94b143cf2cfa FOREIGN KEY (instrutor_id) REFERENCES public.instrutor(id);


--
-- TOC entry 3668 (class 2606 OID 19066)
-- Name: gasto fk5d94b14cf416e7a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.gasto
    ADD CONSTRAINT fk5d94b14cf416e7a FOREIGN KEY (participante_id) REFERENCES public.participante(id);


--
-- TOC entry 3647 (class 2606 OID 18986)
-- Name: evento_interessado fk6a895cb931edb9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_interessado
    ADD CONSTRAINT fk6a895cb931edb9a FOREIGN KEY (evento_id) REFERENCES public.evento(id);


--
-- TOC entry 3648 (class 2606 OID 18981)
-- Name: evento_interessado fk6a895cb9cf416e7a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_interessado
    ADD CONSTRAINT fk6a895cb9cf416e7a FOREIGN KEY (participante_id) REFERENCES public.participante(id);


--
-- TOC entry 3642 (class 2606 OID 18961)
-- Name: evento_extra fk6aa1f4667e8ce86; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_extra
    ADD CONSTRAINT fk6aa1f4667e8ce86 FOREIGN KEY (solicitante_id) REFERENCES public.sca_usuario(id);


--
-- TOC entry 3643 (class 2606 OID 18966)
-- Name: evento_extra fk6aa1f466976cbda; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_extra
    ADD CONSTRAINT fk6aa1f466976cbda FOREIGN KEY (chefe_id) REFERENCES public.sca_usuario(id);


--
-- TOC entry 3644 (class 2606 OID 18971)
-- Name: evento_extra fk6aa1f466c772aa7; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_extra
    ADD CONSTRAINT fk6aa1f466c772aa7 FOREIGN KEY (cidade_id) REFERENCES public.srh_tb_municipio(id);


--
-- TOC entry 3645 (class 2606 OID 18956)
-- Name: evento_extra fk6aa1f466f64bdda; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_extra
    ADD CONSTRAINT fk6aa1f466f64bdda FOREIGN KEY (pais_id) REFERENCES public.pais(id);


--
-- TOC entry 3604 (class 2606 OID 18771)
-- Name: avaliacao_reacao fk720c9b09b1f8d3fa; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao_reacao
    ADD CONSTRAINT fk720c9b09b1f8d3fa FOREIGN KEY (modulo_id) REFERENCES public.modulo(id);


--
-- TOC entry 3605 (class 2606 OID 18766)
-- Name: avaliacao_reacao fk720c9b09cf416e7a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao_reacao
    ADD CONSTRAINT fk720c9b09cf416e7a FOREIGN KEY (participante_id) REFERENCES public.participante(id);


--
-- TOC entry 3649 (class 2606 OID 18996)
-- Name: evento_material fk72841d1131edb9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_material
    ADD CONSTRAINT fk72841d1131edb9a FOREIGN KEY (evento_id) REFERENCES public.evento(id);


--
-- TOC entry 3650 (class 2606 OID 18991)
-- Name: evento_material fk72841d11b1f8d3fa; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_material
    ADD CONSTRAINT fk72841d11b1f8d3fa FOREIGN KEY (modulo_id) REFERENCES public.modulo(id);


--
-- TOC entry 3625 (class 2606 OID 18876)
-- Name: certificado fk745f3fb131edb9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.certificado
    ADD CONSTRAINT fk745f3fb131edb9a FOREIGN KEY (evento_id) REFERENCES public.evento(id);


--
-- TOC entry 3626 (class 2606 OID 18871)
-- Name: certificado fk745f3fb1cf416e7a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.certificado
    ADD CONSTRAINT fk745f3fb1cf416e7a FOREIGN KEY (participante_id) REFERENCES public.participante(id);


--
-- TOC entry 3701 (class 2606 OID 19256)
-- Name: tipo_localizacao_evento fk7671bbf3c772aa7; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.tipo_localizacao_evento
    ADD CONSTRAINT fk7671bbf3c772aa7 FOREIGN KEY (cidade_id) REFERENCES public.srh_tb_municipio(id);


--
-- TOC entry 3702 (class 2606 OID 19251)
-- Name: tipo_localizacao_evento fk7671bbf3f64bdda; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.tipo_localizacao_evento
    ADD CONSTRAINT fk7671bbf3f64bdda FOREIGN KEY (pais_id) REFERENCES public.pais(id);


--
-- TOC entry 3658 (class 2606 OID 19041)
-- Name: evento_tipopublico fk79364d4831edb9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_tipopublico
    ADD CONSTRAINT fk79364d4831edb9a FOREIGN KEY (evento_id) REFERENCES public.evento(id);


--
-- TOC entry 3659 (class 2606 OID 19036)
-- Name: evento_tipopublico fk79364d488936593; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento_tipopublico
    ADD CONSTRAINT fk79364d488936593 FOREIGN KEY (publico_alvo_id) REFERENCES public.tipo_publico_alvo(id);


--
-- TOC entry 3660 (class 2606 OID 19051)
-- Name: frequencia fk7e9d249531edb9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.frequencia
    ADD CONSTRAINT fk7e9d249531edb9a FOREIGN KEY (evento_id) REFERENCES public.evento(id);


--
-- TOC entry 3661 (class 2606 OID 19046)
-- Name: frequencia fk7e9d2495b1f8d3fa; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.frequencia
    ADD CONSTRAINT fk7e9d2495b1f8d3fa FOREIGN KEY (modulo_id) REFERENCES public.modulo(id);


--
-- TOC entry 3606 (class 2606 OID 18791)
-- Name: avaliacao_reacao_nota fk89574364323eeafd; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao_reacao_nota
    ADD CONSTRAINT fk89574364323eeafd FOREIGN KEY (avaliacao_id) REFERENCES public.avaliacao_reacao(id);


--
-- TOC entry 3607 (class 2606 OID 18776)
-- Name: avaliacao_reacao_nota fk895743643cf2cfa; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao_reacao_nota
    ADD CONSTRAINT fk895743643cf2cfa FOREIGN KEY (instrutor_id) REFERENCES public.instrutor(id);


--
-- TOC entry 3608 (class 2606 OID 18781)
-- Name: avaliacao_reacao_nota fk8957436451f109ce; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao_reacao_nota
    ADD CONSTRAINT fk8957436451f109ce FOREIGN KEY (nota_id) REFERENCES public.nota_questao(id);


--
-- TOC entry 3609 (class 2606 OID 18786)
-- Name: avaliacao_reacao_nota fk89574364c0c2a9ba; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao_reacao_nota
    ADD CONSTRAINT fk89574364c0c2a9ba FOREIGN KEY (questao_id) REFERENCES public.questao(id);


--
-- TOC entry 3690 (class 2606 OID 19226)
-- Name: participante fk89fff7924409ed3a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.participante
    ADD CONSTRAINT fk89fff7924409ed3a FOREIGN KEY (cidade_id) REFERENCES public.cidade(id);


--
-- TOC entry 3691 (class 2606 OID 19206)
-- Name: participante fk89fff7924886cc3e; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.participante
    ADD CONSTRAINT fk89fff7924886cc3e FOREIGN KEY (setor_id) REFERENCES public.sapjava_setor(idsetor);


--
-- TOC entry 3692 (class 2606 OID 19221)
-- Name: participante fk89fff7927850f89c; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.participante
    ADD CONSTRAINT fk89fff7927850f89c FOREIGN KEY (tipo_id) REFERENCES public.tipo_publico_alvo(id);


--
-- TOC entry 3693 (class 2606 OID 19216)
-- Name: participante fk89fff79283e5493e; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.participante
    ADD CONSTRAINT fk89fff79283e5493e FOREIGN KEY (municipio_id) REFERENCES public.srh_tb_municipio(id);


--
-- TOC entry 3694 (class 2606 OID 19211)
-- Name: participante fk89fff792afaf2a5c; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.participante
    ADD CONSTRAINT fk89fff792afaf2a5c FOREIGN KEY (escolaridade_id) REFERENCES public.nivel_escolaridade(id);


--
-- TOC entry 3695 (class 2606 OID 19196)
-- Name: participante fk89fff792c063a0ac; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.participante
    ADD CONSTRAINT fk89fff792c063a0ac FOREIGN KEY (formacao_id) REFERENCES public.formacao_academica(id);


--
-- TOC entry 3696 (class 2606 OID 19231)
-- Name: participante fk89fff792c68452e0; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.participante
    ADD CONSTRAINT fk89fff792c68452e0 FOREIGN KEY (orgao_id) REFERENCES public.sapjava_entidade(identidade);


--
-- TOC entry 3697 (class 2606 OID 19201)
-- Name: participante fk89fff792f64bdda; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.participante
    ADD CONSTRAINT fk89fff792f64bdda FOREIGN KEY (pais_id) REFERENCES public.pais(id);


--
-- TOC entry 3700 (class 2606 OID 19246)
-- Name: sapjava_entidade fk9792d477d63bb6f; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.sapjava_entidade
    ADD CONSTRAINT fk9792d477d63bb6f FOREIGN KEY (idlocalidade) REFERENCES public.sapjava_localidade(idlocalidade);


--
-- TOC entry 3662 (class 2606 OID 19061)
-- Name: frequencia_participante fk9b9316fccf416e7a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.frequencia_participante
    ADD CONSTRAINT fk9b9316fccf416e7a FOREIGN KEY (participante_id) REFERENCES public.participante(id);


--
-- TOC entry 3663 (class 2606 OID 19056)
-- Name: frequencia_participante fk9b9316fcdfbf2b9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.frequencia_participante
    ADD CONSTRAINT fk9b9316fcdfbf2b9a FOREIGN KEY (frequencia_id) REFERENCES public.frequencia(id);


--
-- TOC entry 3629 (class 2606 OID 18891)
-- Name: cidade fkaee65724c23b1a1a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.cidade
    ADD CONSTRAINT fkaee65724c23b1a1a FOREIGN KEY (uf_id) REFERENCES public.uf(id);


--
-- TOC entry 3633 (class 2606 OID 18931)
-- Name: evento fkb307e11528d25d86; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento
    ADD CONSTRAINT fkb307e11528d25d86 FOREIGN KEY (responsavel_evento) REFERENCES public.participante(id);


--
-- TOC entry 3634 (class 2606 OID 18946)
-- Name: evento fkb307e1154237496f; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento
    ADD CONSTRAINT fkb307e1154237496f FOREIGN KEY (tipo_id) REFERENCES public.tipo_evento(id);


--
-- TOC entry 3635 (class 2606 OID 18921)
-- Name: evento fkb307e115757faa29; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento
    ADD CONSTRAINT fkb307e115757faa29 FOREIGN KEY (area_tribunal_id) REFERENCES public.tipo_area_tribunal(id);


--
-- TOC entry 3636 (class 2606 OID 18911)
-- Name: evento fkb307e115a1829b9b; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento
    ADD CONSTRAINT fkb307e115a1829b9b FOREIGN KEY (localizacao_id) REFERENCES public.tipo_localizacao_evento(id);


--
-- TOC entry 3637 (class 2606 OID 18941)
-- Name: evento fkb307e115a9ef2ccf; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento
    ADD CONSTRAINT fkb307e115a9ef2ccf FOREIGN KEY (provedor_id) REFERENCES public.provedor_evento(id);


--
-- TOC entry 3638 (class 2606 OID 18926)
-- Name: evento fkb307e115b4cbaa11; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento
    ADD CONSTRAINT fkb307e115b4cbaa11 FOREIGN KEY (eixo_tematico_id) REFERENCES public.eixo_tematico(id);


--
-- TOC entry 3639 (class 2606 OID 18951)
-- Name: evento fkb307e115c772aa7; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento
    ADD CONSTRAINT fkb307e115c772aa7 FOREIGN KEY (cidade_id) REFERENCES public.srh_tb_municipio(id);


--
-- TOC entry 3640 (class 2606 OID 18936)
-- Name: evento fkb307e115f4fbe17a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento
    ADD CONSTRAINT fkb307e115f4fbe17a FOREIGN KEY (modalidade_id) REFERENCES public.modalidade(id);


--
-- TOC entry 3641 (class 2606 OID 18916)
-- Name: evento fkb307e115f64bdda; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.evento
    ADD CONSTRAINT fkb307e115f64bdda FOREIGN KEY (pais_id) REFERENCES public.pais(id);


--
-- TOC entry 3685 (class 2606 OID 19176)
-- Name: modulo_instrutor fkb5ce83253cf2cfa; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.modulo_instrutor
    ADD CONSTRAINT fkb5ce83253cf2cfa FOREIGN KEY (instrutor_id) REFERENCES public.instrutor(id);


--
-- TOC entry 3686 (class 2606 OID 19171)
-- Name: modulo_instrutor fkb5ce8325b1f8d3fa; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.modulo_instrutor
    ADD CONSTRAINT fkb5ce8325b1f8d3fa FOREIGN KEY (modulo_id) REFERENCES public.modulo(id);


--
-- TOC entry 3680 (class 2606 OID 19161)
-- Name: modulo fkc04ba67631edb9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.modulo
    ADD CONSTRAINT fkc04ba67631edb9a FOREIGN KEY (evento_id) REFERENCES public.evento(id);


--
-- TOC entry 3681 (class 2606 OID 19146)
-- Name: modulo fkc04ba676a1829b9b; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.modulo
    ADD CONSTRAINT fkc04ba676a1829b9b FOREIGN KEY (localizacao_id) REFERENCES public.tipo_localizacao_evento(id);


--
-- TOC entry 3682 (class 2606 OID 19166)
-- Name: modulo fkc04ba676c772aa7; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.modulo
    ADD CONSTRAINT fkc04ba676c772aa7 FOREIGN KEY (cidade_id) REFERENCES public.srh_tb_municipio(id);


--
-- TOC entry 3683 (class 2606 OID 19156)
-- Name: modulo fkc04ba676f4fbe17a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.modulo
    ADD CONSTRAINT fkc04ba676f4fbe17a FOREIGN KEY (modalidade_id) REFERENCES public.modalidade(id);


--
-- TOC entry 3684 (class 2606 OID 19151)
-- Name: modulo fkc04ba676f64bdda; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.modulo
    ADD CONSTRAINT fkc04ba676f64bdda FOREIGN KEY (pais_id) REFERENCES public.pais(id);


--
-- TOC entry 3613 (class 2606 OID 18816)
-- Name: responsavel_setor fkc35e0ec61ac60b59; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.responsavel_setor
    ADD CONSTRAINT fkc35e0ec61ac60b59 FOREIGN KEY (responsavel_id) REFERENCES public.sca_usuario(id);


--
-- TOC entry 3614 (class 2606 OID 18811)
-- Name: responsavel_setor fkc35e0ec64886cc3e; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.responsavel_setor
    ADD CONSTRAINT fkc35e0ec64886cc3e FOREIGN KEY (setor_id) REFERENCES public.sapjava_setor(idsetor);


--
-- TOC entry 3615 (class 2606 OID 18821)
-- Name: responsavel_setor fkc35e0ec6aa9dd984; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.responsavel_setor
    ADD CONSTRAINT fkc35e0ec6aa9dd984 FOREIGN KEY (responsavel_imediato_id) REFERENCES public.sca_usuario(id);


--
-- TOC entry 3616 (class 2606 OID 18851)
-- Name: avaliacao fkd935d09931edb9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao
    ADD CONSTRAINT fkd935d09931edb9a FOREIGN KEY (evento_id) REFERENCES public.evento(id);


--
-- TOC entry 3617 (class 2606 OID 18846)
-- Name: avaliacao fkd935d0993cf2cfa; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao
    ADD CONSTRAINT fkd935d0993cf2cfa FOREIGN KEY (instrutor_id) REFERENCES public.instrutor(id);


--
-- TOC entry 3618 (class 2606 OID 18831)
-- Name: avaliacao fkd935d099ac7f62a4; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao
    ADD CONSTRAINT fkd935d099ac7f62a4 FOREIGN KEY (instrutor2_id) REFERENCES public.instrutor(id);


--
-- TOC entry 3619 (class 2606 OID 18826)
-- Name: avaliacao fkd935d099ac7fd703; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao
    ADD CONSTRAINT fkd935d099ac7fd703 FOREIGN KEY (instrutor3_id) REFERENCES public.instrutor(id);


--
-- TOC entry 3620 (class 2606 OID 18841)
-- Name: avaliacao fkd935d099b1f8d3fa; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao
    ADD CONSTRAINT fkd935d099b1f8d3fa FOREIGN KEY (modulo_id) REFERENCES public.modulo(id);


--
-- TOC entry 3621 (class 2606 OID 18836)
-- Name: avaliacao fkd935d099cf416e7a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.avaliacao
    ADD CONSTRAINT fkd935d099cf416e7a FOREIGN KEY (participante_id) REFERENCES public.participante(id);


--
-- TOC entry 3669 (class 2606 OID 19106)
-- Name: inscricao fke5a6e52531edb9a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.inscricao
    ADD CONSTRAINT fke5a6e52531edb9a FOREIGN KEY (evento_id) REFERENCES public.evento(id);


--
-- TOC entry 3670 (class 2606 OID 19101)
-- Name: inscricao fke5a6e525515286f0; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.inscricao
    ADD CONSTRAINT fke5a6e525515286f0 FOREIGN KEY (solicitacao_id) REFERENCES public.evento_extra(id);


--
-- TOC entry 3671 (class 2606 OID 19096)
-- Name: inscricao fke5a6e525976cbda; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.inscricao
    ADD CONSTRAINT fke5a6e525976cbda FOREIGN KEY (chefe_id) REFERENCES public.sca_usuario(id);


--
-- TOC entry 3672 (class 2606 OID 19091)
-- Name: inscricao fke5a6e525cf416e7a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.inscricao
    ADD CONSTRAINT fke5a6e525cf416e7a FOREIGN KEY (participante_id) REFERENCES public.participante(id);


--
-- TOC entry 3698 (class 2606 OID 19241)
-- Name: provedor_evento fke6c90229c772aa7; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.provedor_evento
    ADD CONSTRAINT fke6c90229c772aa7 FOREIGN KEY (cidade_id) REFERENCES public.srh_tb_municipio(id);


--
-- TOC entry 3699 (class 2606 OID 19236)
-- Name: provedor_evento fke6c90229f64bdda; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.provedor_evento
    ADD CONSTRAINT fke6c90229f64bdda FOREIGN KEY (pais_id) REFERENCES public.pais(id);


--
-- TOC entry 3703 (class 2606 OID 19261)
-- Name: uf fke91f64bdda; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.uf
    ADD CONSTRAINT fke91f64bdda FOREIGN KEY (pais_id) REFERENCES public.pais(id);


--
-- TOC entry 3611 (class 2606 OID 18806)
-- Name: questao_modalidade fkfd0dd649c0c2a9ba; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.questao_modalidade
    ADD CONSTRAINT fkfd0dd649c0c2a9ba FOREIGN KEY (questao_id) REFERENCES public.questao(id);


--
-- TOC entry 3612 (class 2606 OID 18801)
-- Name: questao_modalidade fkfd0dd649f4fbe17a; Type: FK CONSTRAINT; Schema: public; Owner: siged
--

ALTER TABLE ONLY public.questao_modalidade
    ADD CONSTRAINT fkfd0dd649f4fbe17a FOREIGN KEY (modalidade_id) REFERENCES public.modalidade(id);


-- Completed on 2022-11-05 15:23:14

--
-- PostgreSQL database dump complete
--

