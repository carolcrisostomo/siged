package br.com.siged.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.tags.TableTagParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.siged.controller.validators.InscricaoValidator;
import br.com.siged.dao.AvaliacaoEficaciaDAO;
import br.com.siged.dao.AvaliacaoReacaoDAO;
import br.com.siged.dao.CertificadoDAO;
import br.com.siged.dao.CertificadoEmitidoDAO;
import br.com.siged.dao.CidadeDAO;
import br.com.siged.dao.DesempenhoDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.EventoExtraDAO;
import br.com.siged.dao.EventoRecomendarDAO;
import br.com.siged.dao.FrequenciaDAO;
import br.com.siged.dao.InscricaoDAO;
import br.com.siged.dao.NotaDAO;
import br.com.siged.dao.PaisDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.ResponsavelSetorDAO;
import br.com.siged.dao.TipoPublicoAlvoDAO;
import br.com.siged.dao.UfDAO;
import br.com.siged.dao.UsuarioSCADAO;
import br.com.siged.dao.pagination.DisplayTagPageable;
import br.com.siged.dto.inscricao.InclusaoInscricaoLoteDTO;
import br.com.siged.editor.CidadeEditor;
import br.com.siged.editor.EventoEditor;
import br.com.siged.editor.EventoExtraEditor;
import br.com.siged.editor.PaisEditor;
import br.com.siged.editor.ParticipanteEditor;
import br.com.siged.editor.TipoPublicoAlvoEditor;
import br.com.siged.editor.UfEditor;
import br.com.siged.editor.UsuarioSCAEditor;
import br.com.siged.entidades.AvaliacaoEficacia;
import br.com.siged.entidades.AvaliacaoReacao;
import br.com.siged.entidades.Certificado;
import br.com.siged.entidades.CertificadoEmitido;
import br.com.siged.entidades.Desempenho;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.EventoRecomendar;
import br.com.siged.entidades.Frequencia;
import br.com.siged.entidades.Inscricao;
import br.com.siged.entidades.Nota;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.ResponsavelSetor;
import br.com.siged.entidades.TipoPublicoAlvo;
import br.com.siged.entidades.Usuario;
import br.com.siged.entidades.externas.Setor;
import br.com.siged.entidades.externas.UsuarioSCA;
import br.com.siged.filtro.InscricaoAVAFiltro;
import br.com.siged.filtro.InscricaoFiltro;
import br.com.siged.service.AuthenticationService;
import br.com.siged.service.InscricaoService;
import br.com.siged.service.RelatorioService;
import br.com.siged.service.exception.HttpStatusException;
import br.com.siged.service.exception.NaoPodeRealizarInscricaoException;
import br.com.siged.util.EmailUtil;
import br.com.siged.util.HibernateUtil;

@Controller
@RequestMapping("/inscricao/**")
public class InscricaoController {
	
	@Autowired
	private InscricaoDAO inscricaoDao;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private UsuarioSCADAO usuarioSCADao;
	@Autowired
	private EventoExtraDAO eventoextraDao;
	@Autowired
	private EventoRecomendarDAO eventorecomendarDao;
	@Autowired
	private PaisDAO paisDao;
	@Autowired
	private CidadeDAO cidadeDao;
	@Autowired
	private UfDAO ufDao;
	@Autowired
	private FrequenciaDAO frequenciaDao;
	@Autowired
	private NotaDAO notaDao;
	@Autowired
	private AvaliacaoReacaoDAO avaliacaoreacaoDao;
	@Autowired
	private AvaliacaoEficaciaDAO avaliacaoEficaciaDao;
	@Autowired
	private EmailUtil emailUtil;	
	@Autowired
	private CertificadoDAO certificadoDao;
	@Autowired
	private CertificadoEmitidoDAO certificadoEmitidoDao;
	@Autowired
	private DesempenhoDAO desempenhoDao;
	@Autowired
	private TipoPublicoAlvoDAO tipoPublicoAlvoDao;
	@Autowired
	private ResponsavelSetorDAO responsavelSetorDao;
	@Autowired
	private RelatorioService relatorioService;
	@Autowired
	private InscricaoValidator inscricaoValidator;
	@Autowired
	private InscricaoService inscricaoService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(br.com.siged.entidades.Evento.class, new EventoEditor(eventoDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Participante.class, new ParticipanteEditor(participanteDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.EventoExtra.class, new EventoExtraEditor(eventoextraDao));
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
		dataBinder.registerCustomEditor(br.com.siged.entidades.externas.UsuarioSCA.class, new UsuarioSCAEditor(usuarioSCADao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Pais.class, new PaisEditor(paisDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Cidade.class, new CidadeEditor(cidadeDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.Uf.class, new UfEditor(ufDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.TipoPublicoAlvo.class, new TipoPublicoAlvoEditor(tipoPublicoAlvoDao));
		
		if(dataBinder.getTarget() instanceof Inscricao)
			dataBinder.setValidator(inscricaoValidator);
	}
	
	@RequestMapping(value = "/inscricao/{id}/enviarEmailConfirmacao", method = RequestMethod.GET)
	@ResponseBody
	public HttpStatus enviarEmailConfirmacao(@PathVariable("id") Long id, @RequestParam("email") String email) {
		
		Inscricao inscricao = inscricaoDao.find(id);
		if(inscricao == null) {
			return HttpStatus.BAD_REQUEST;
		}
		if(!inscricao.getConfirmada().equals("S")) {
			return HttpStatus.BAD_REQUEST;
		}
		Participante participante = inscricao.getParticipanteId();
		try {
			/**
			 * Se não informar o e-mail, então usa o cadastrado do participante
			 */
			if(!email.isEmpty()) {
				participante.setEmail(email);
			}
			emailUtil.emailInscricao(inscricao.getEventoId().getId(), inscricao.getParticipanteId());
			return HttpStatus.OK;
		} catch (Exception e) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

	
	/**
	 * Trecho de código estava lançando <tt>"javax.persistence.NonUniqueResultException: result returns more than one elements"</tt>
	 * quando o Participante tem Usuário na base do SIGED e no SCA com login sendo o CPF
	 * </p>
	<pre>
	Inscricao inscricao = inscricaoDao.find(id);
		 
	Participante participante = participanteDao.findByCpf(usuarioSCADao.findByLogin(request.getRemoteUser()).getCpf());
		 
	if ( inscricao.getParticipanteId().equals(participante) ) {
		modelMap.addAttribute("exibirBotoes", true);	
	}
	</pre>
	 * @since corrigido em 06/09/2018 [issue #2822]
	 */
	@RequestMapping(value = "/inscricao/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap, HttpServletRequest request) {		
		Usuario usuarioLogado = AuthenticationService.getUsuarioLogado();
		Inscricao inscricao = inscricaoDao.find(id);
		
		if(inscricao == null || usuarioLogado == null)
			throw new HttpStatusException(HttpStatus.NOT_FOUND, "Inscrição não encontrada");
		
		if(usuarioLogado.isAluno() && !usuarioLogado.isChefe()) {
			Participante participante = participanteDao.findByCpf(usuarioLogado.getCpf());
			if (inscricao.getParticipanteId().getId().equals(participante.getId())) {
				modelMap.addAttribute("exibirBotoes", true);
			} else {
				throw new HttpStatusException(HttpStatus.FORBIDDEN);
			}
		}
		
		modelMap.addAttribute("inscricao", inscricao);
		
		return "inscricao/show";
	}	

	@RequestMapping(value = "/inscricao", method = RequestMethod.GET)
	public String list(ModelMap modelMap, HttpServletRequest request) {
		
		InscricaoFiltro inscricaofiltro = (InscricaoFiltro) request.getSession().getAttribute("inscricaofiltro");

		if (inscricaofiltro != null) {
			DisplayTagPageable pageable = new DisplayTagPageable(request);
			PaginatedList displayTagList = inscricaoDao.filtrar(inscricaofiltro, pageable);
			
			modelMap.addAttribute("inscricaofiltro", inscricaofiltro);
			modelMap.addAttribute("inscricoes", displayTagList);
			
		} else {
			modelMap.addAttribute("inscricaofiltro", new InscricaoFiltro());
		}

		return "inscricao/list";
	}
	
	@RequestMapping(value = "/inscricao/search", method = RequestMethod.GET)
	public String search(InscricaoFiltro inscricaofiltro, ModelMap modelMap, HttpServletRequest request) throws IOException {
		boolean export = request.getParameter(TableTagParameters.PARAMETER_EXPORTING) != null;

		DisplayTagPageable pageable = new DisplayTagPageable(request);
		PaginatedList displayTagList = inscricaoDao.filtrar(inscricaofiltro, pageable, export);
		
		
		modelMap.addAttribute("inscricaofiltro", inscricaofiltro);
		modelMap.addAttribute("inscricoes", displayTagList);	
		
		// request.getSession().setAttribute("inscricaofiltro", inscricaofiltro);
		
		return "inscricao/list";
	}
	
	@RequestMapping(value = "/inscricao/limpar", method = RequestMethod.GET)
	public String limparGet(ModelMap modelMap, HttpServletRequest request) {		
		request.getSession().setAttribute("inscricaofiltro", null);	
		return "redirect:/inscricao";
	}
	
	@RequestMapping(value = "/inscricao/confirmacao", method = RequestMethod.GET)
	public String confirmacaoGet(ModelMap modelMap) {
		modelMap.addAttribute("eventoInscricao", new Evento());
		modelMap.addAttribute("eventoComInscricoesComConfirmacaoPedenteList", inscricaoDao.findEventoByConfirmacaoPendente());
		return "inscricao/confirmacao";
	}
	
	@RequestMapping(value = "/inscricao/confirmacao", method = RequestMethod.POST)
	public String confirmacaoPost(@ModelAttribute("eventoInscricao") @Valid Evento evento, BindingResult result, ModelMap modelMap, HttpServletRequest request) {	
		
		String inscricoesId[] = request.getParameter("inscricoesId").split(",");		
		
		this.inscricaoService.confirmacaoLote(evento, inscricoesId);
		
		modelMap.addAttribute("eventoInscricao", evento);
		modelMap.addAttribute("eventoComInscricoesComConfirmacaoPedenteList", inscricaoDao.findEventoByConfirmacaoPendente());
		modelMap.addAttribute("mensagemSucesso", "Inscrições confirmadas com sucesso!");
		
		return "inscricao/confirmacao";
	}
	
	@RequestMapping(value = "/inscricao/inclusaolote", method = RequestMethod.GET)
	public String inclusaoLote(ModelMap modelMap) {
		modelMap.addAttribute("inscricao", new Inscricao());
		return "inscricao/createlote";
	}
	
	@RequestMapping(value = "/inscricao/inclusaolote", method = RequestMethod.POST)
	public String inclusaoLotePost(@ModelAttribute("inscricao") Inscricao inscricao, BindingResult result, ModelMap modelMap, HttpServletRequest request) {	
		
		if (result.hasErrors()) 
			return "inscricao/createlote";

		String participantesId[] = request.getParameter("participantesId").split(",");
		try {
			InclusaoInscricaoLoteDTO inclusaoLote = inscricaoService.inclusaoLote(inscricao.getEventoId(), participantesId);
			
			int incluidas = inclusaoLote.incluidas();
			int naoIncluidas = inclusaoLote.naoIncluidas();
			
			if(incluidas == 0){
				modelMap.addAttribute("mensagemErro", "Nenhuma pré-inscrição incluída! Possíveis motivos: Tentativa de inclusão de inscrição já cadastrada, tipo do participante incompatível com o público-alvo do evento ou participante é instrutor no evento!");			
			}else if ( incluidas > 0 && naoIncluidas > 0){
				modelMap.addAttribute("mensagemAlerta", incluidas + "  pré-inscrição(ões) foi(ram) incluída(s) com sucesso. "
														+ naoIncluidas + "  pré-inscrição(ões) não foi(ram) incluída(s) por tentativa de inclusão de inscrição já cadastrada, tipo do participante incompatível com o público-alvo do evento ou participante é instrutor no evento!");
			}else if(incluidas > 0 && naoIncluidas == 0){
				modelMap.addAttribute("mensagemSucesso","Pré-inscrições incluídas com sucesso!");
			}	
		} catch (Exception e) {
			modelMap.addAttribute("mensagemErro", e.getMessage());
		}
		
		return "inscricao/createlote";
	}
	
	/**
	 * Excluir inscrição causa exclusão em cascata de Frequencia, Nota, AvaliacaoReacao, Certificado e Desempenho
	 * Alteração em 29/11/2016: substitui as avalicoes antigas pela nova AvaliacaoReacao @author Rafael de Castro (estag_12977)
	 * 
	 * @param id pathvariable para localizar a inscrição a ser deletada
	 */
	@RequestMapping(value = "/inscricao/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		Inscricao inscricao = inscricaoDao.find(id);
		Usuario usuarioLogado = AuthenticationService.getUsuarioLogado();
		
		if(inscricao == null || usuarioLogado == null)
			throw new HttpStatusException(HttpStatus.NOT_FOUND, "Inscrição não encontrada");
		
		if(!usuarioLogado.isAdministrador())
			throw new HttpStatusException(HttpStatus.FORBIDDEN);
		
		List<Frequencia> frequencias = frequenciaDao.findByEventoAndParticipante(inscricao.getEventoId(), inscricao.getParticipanteId());
		if (frequencias != null && frequencias.size() > 0) {
			for (Frequencia frequenciaDel: frequencias) {
				List<Participante> frequenciaInicializada = new ArrayList<>();
				for(Participante participante : frequenciaDel.getParticipanteList()) {
					frequenciaInicializada.add(HibernateUtil.unproxy(participante));
				}
				frequenciaDel.setParticipanteList(frequenciaInicializada);
				
				if (frequenciaDel.getParticipanteList().contains(inscricao.getParticipanteId())) {
					frequenciaDel.getParticipanteList().remove(inscricao.getParticipanteId());
				}
				
				if (frequenciaDel.getParticipanteList().size() == 0) {
					frequenciaDao.remove(frequenciaDel);
				} else if (frequenciaDel.getParticipanteList().size() > 0) {
					frequenciaDao.merge(frequenciaDel);
				}
			}
		}
		
		List<Nota> notas = notaDao.findByEventoAndParticipante(inscricao.getEventoId(), inscricao.getParticipanteId());
		if (notas != null && notas.size() > 0) {
			for (Nota notaDel: notas) {
				notaDao.remove(notaDel);
			}
		}
		
		List<AvaliacaoReacao> avaliacoes = avaliacaoreacaoDao.findByEventoAndParticipante(inscricao.getEventoId(), inscricao.getParticipanteId());
		if (avaliacoes != null && avaliacoes.size() > 0) {
			for (AvaliacaoReacao avaliacaoDel: avaliacoes) {
				avaliacaoreacaoDao.remove(avaliacaoDel);
			}
		}
		
		AvaliacaoEficacia avaliacaoEficacia = avaliacaoEficaciaDao.findByEventoIdAndParticipanteId(inscricao.getEventoId().getId(), inscricao.getParticipanteId().getId());
		if (avaliacaoEficacia != null)
			avaliacaoEficaciaDao.remove(avaliacaoEficacia);
		
		List<Certificado> certificados = certificadoDao.findByEventoAndParticipante(inscricao.getEventoId(), inscricao.getParticipanteId());
		if (certificados != null && certificados.size() > 0) {
			for (Certificado certificadoDel: certificados) {
				certificadoDao.remove(certificadoDel);
			}
		}
		
		certificadoEmitidoDao.deleteByEventoIdAndParticipanteId(inscricao.getEventoId().getId(), inscricao.getParticipanteId().getId());
		
		List<Desempenho> desempenhos = desempenhoDao.findByEventoAndParticipante(inscricao.getEventoId(), inscricao.getParticipanteId());
		if (desempenhos != null && desempenhos.size() > 0) {
			for (Desempenho desempenhoDel: desempenhos) {
				desempenhoDao.remove(desempenhoDel);
			}
		}
		
		inscricaoDao.remove(inscricao);
		return "redirect:/inscricao";
	}

	@RequestMapping(value = "/inscricao/{id}/minhasinscricoes", method = RequestMethod.DELETE)
	public String deleteminhasinscricoes(@PathVariable("id") Long id) {
		Usuario usuarioLogado = AuthenticationService.getUsuarioLogado();
		Inscricao inscricao = inscricaoDao.find(id);
		
		if(inscricao == null || usuarioLogado == null)
			throw new HttpStatusException(HttpStatus.NOT_FOUND, "Inscrição não encontrada");
		
		if(usuarioLogado.isAluno()) {
			Participante participante = participanteDao.findByCpf(usuarioLogado.getCpf());
			if (!inscricao.getParticipanteId().getId().equals(participante.getId()))
				throw new HttpStatusException(HttpStatus.FORBIDDEN);
		}
		
		if(!inscricao.getConfirmada().equals("S")) {
			List<Frequencia> frequencias = frequenciaDao.findByEventoAndParticipante(inscricao.getEventoId(), inscricao.getParticipanteId());
			List<Nota> notas = notaDao.findByEventoAndParticipante(inscricao.getEventoId(), inscricao.getParticipanteId());
			List<AvaliacaoReacao> avaliacoesReacao = avaliacaoreacaoDao.findByEventoAndParticipante(inscricao.getEventoId(), inscricao.getParticipanteId());
			AvaliacaoEficacia avaliacaoEficacia = avaliacaoEficaciaDao.findByEventoIdAndParticipanteId(inscricao.getEventoId().getId(), inscricao.getParticipanteId().getId());
			List<Certificado> certificados = certificadoDao.findByEventoAndParticipante(inscricao.getEventoId(), inscricao.getParticipanteId());
			List<CertificadoEmitido> certificadosEmitidos = certificadoEmitidoDao.findByEventoIdAndParticipanteId(inscricao.getEventoId().getId(), inscricao.getParticipanteId().getId());
			List<Desempenho> desempenhos = desempenhoDao.findByEventoAndParticipante(inscricao.getEventoId(), inscricao.getParticipanteId());
			
			if ((frequencias != null && frequencias.size() > 0) || (notas != null && notas.size() > 0) || (desempenhos != null && desempenhos.size() > 0))
				throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Não é possível deletar a inscricão. Existem frequências, notas ou desempenhos já lançados");
			
			if ((avaliacoesReacao != null && avaliacoesReacao.size() > 0) || avaliacaoEficacia != null)
				throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Não é possível deletar a inscricão. Existem avaliações já lançadas");
			
			if((certificados != null && certificados.size() > 0) || (certificadosEmitidos != null && certificadosEmitidos.size() > 0))
				throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Não é possível deletar a inscricão. Existem certificados já emitidos");
			
			inscricaoDao.remove(inscricao);
		} else {
			throw new HttpStatusException(HttpStatus.FORBIDDEN);
		}
		
		return "redirect:/inscricao/minhasinscricoes";
	}

	@RequestMapping(value = "/inscricao/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("inscricao", new Inscricao());
		return "inscricao/create";
	}

	@RequestMapping(value = "/inscricao", method = RequestMethod.POST)
	public String create(@ModelAttribute("inscricao") @Valid Inscricao inscricao, BindingResult result, ModelMap modelMap, HttpServletRequest request) throws IOException, Exception  {
		
		if (result.hasErrors())
			return "inscricao/create";
		
		try {
			inscricaoService.salvarADMIN(inscricao);
		} catch (NaoPodeRealizarInscricaoException ex) {
			if(ex.isAcessoNegado()) {
				throw new HttpStatusException(HttpStatus.FORBIDDEN);
			} else {
				modelMap.addAttribute("mensagemErro", ex.getMessage());
				return "redirect:/inscricao/form";
			}	
		}
		
		return "redirect:/inscricao";
	}

	@RequestMapping(value = "/inscricao/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		
		Inscricao inscricao = inscricaoDao.find(id);
		
		int totalDeInscritos = 0;
		
		List<Inscricao> inscricoesNoEvento = inscricaoDao.findByEventoAndConfirmada(inscricao.getEventoId().getId(), "S");
		
		if (inscricoesNoEvento != null)
			totalDeInscritos = inscricoesNoEvento.size();
		
		modelMap.addAttribute("totalDeVagas", inscricao.getEventoId().getTotalVagas());		
		modelMap.addAttribute("totalDeInscritos", totalDeInscritos);
		modelMap.addAttribute("inscricao", inscricao);
		modelMap.addAttribute("listaParticipante", participanteDao.findByEventoId(Long.parseLong(inscricaoDao.find(id).getEventoId().getId().toString())));
		
		return "inscricao/update";
	}
	
	@RequestMapping(value = "/inscricao/{id}", method = RequestMethod.PUT)
	public String update(@ModelAttribute("inscricao") @Valid Inscricao inscricao, BindingResult result, ModelMap modelMap) throws IOException, Exception {
		
		if (result.hasErrors())
			return "inscricao/update";

		try {
			inscricaoService.salvarADMIN(inscricao);
		} catch (NaoPodeRealizarInscricaoException ex) {
			if(ex.isAcessoNegado()) {
				throw new HttpStatusException(HttpStatus.FORBIDDEN);
			} else {
				modelMap.addAttribute("mensagemErro", ex.getMessage());
				return "redirect:/inscricao/" + inscricao.getId() + "/form";
			}	
		}
		
		return "redirect:/inscricao";
	}

	@RequestMapping(value = "/inscricao/justificativa/{id}", method = RequestMethod.GET)
	public String justificativa(@PathVariable("id") Long id, ModelMap modelMap) {
		Inscricao inscricao = inscricaoDao.find(id);
		modelMap.addAttribute("inscricao", inscricao);
		return "inscricao/justificativa";
	}

	@RequestMapping(value = "/inscricao/indicacao", method = RequestMethod.POST)
	public String indicacao(@ModelAttribute("inscricao") @Valid Inscricao inscricaopost, BindingResult result, ModelMap modelMap) {
		Inscricao inscricao = inscricaoDao.find(inscricaopost.getId());
		
		if(inscricaopost.getJustificativachefe() == null){
			modelMap.addAttribute("mensagemErro","Você precisa justificar a indicação!");
			modelMap.addAttribute("inscricao", inscricaopost);
			return "redirect:/inscricao/minhasindicacoes";
		}
		inscricao.setJustificativachefe(inscricaopost.getJustificativachefe());
		inscricao.setIndicada("S");
		inscricao.setDataIndicacao(new Date());
		inscricaoDao.merge(inscricao);
		return "redirect:/inscricao/minhasindicacoes";
	}

	@RequestMapping(value = "/inscricao/indicacao/{id}/cancelar", method = RequestMethod.GET)
	public String indicacaocancelar(@PathVariable("id") Long id) {
		
		try{
		
			Inscricao inscricao = inscricaoDao.find(id);
			
			inscricao.setIndicada("N");
			inscricao.setJustificativachefe("");
			inscricao.setDataIndicacao(null);
			
			UsuarioSCA chefe = null;			
			
			if (inscricao.getParticipanteId().getTipoId().getId() == 1) {
				
				Setor setor = inscricao.getParticipanteId().getSetorId();
				
				ResponsavelSetor responsavelSetor = responsavelSetorDao.findAtivoBySetorId(setor.getId());
				
				UsuarioSCA usuario = usuarioSCADao.findByCpf(inscricao.getParticipanteId().getCpf());
				
				if (usuario.equals(responsavelSetor.getResponsavel())) {
					chefe = responsavelSetor.getResponsavelImediato();
				} else {
					chefe = responsavelSetor.getResponsavel();
				}				
			}
			
			inscricao.setChefeId(chefe);
			
			inscricaoDao.merge(inscricao);
			return "redirect:/inscricao/minhasindicacoes";
		
		}catch (Exception e){
			return "redirect:/inscricao/minhasindicacoes?mensagemErro=Não foi possível cancelar a indicação. Favor entrar em contato com o IPC.";
		}	
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/inscricao/minhasinscricoes", method = RequestMethod.GET)
	public String minhasinscricoes(ModelMap modelMap, HttpServletRequest request) {
		List <Inscricao> inscricaoList;
		
		if(request.getAttribute("inscricoes") == null){		
			Participante participante = (Participante) request.getSession().getAttribute("PARTICIPANTE");
			inscricaoList = inscricaoDao.findByParticipanteCpf(participante.getCpf());		
		}else{
			inscricaoList = (List <Inscricao>) request.getAttribute("inscricoes");
		}
		
		if (request.isUserInRole("ROLE_SERVIDOR")) {			
			
			UsuarioSCA naoExigido = new UsuarioSCA (0L, "Não exigido");

			for(int i = 0; i < inscricaoList.size(); i++){		
				if (inscricaoList.get(i).getChefeId() != null && inscricaoList.get(i).getChefeId().getId() == 0L)
					inscricaoList.get(i).setChefeId(naoExigido);
			}
		}
		
		modelMap.addAttribute("inscricoes", inscricaoList);
		
		if (request.getAttribute("inscricaofiltro") == null)
			modelMap.addAttribute("inscricaofiltro", new InscricaoFiltro());
		else
			modelMap.addAttribute("inscricaofiltro", request.getAttribute("inscricaofiltro"));
		
		return "inscricao/minhasinscricoes";
	}

	@RequestMapping(value = "/inscricao/minhasinscricoes/search", method = RequestMethod.GET)
	public String minhasInscricoesSearch(InscricaoFiltro inscricaofiltro, ModelMap modelMap, HttpServletRequest request, ServletResponse Response) throws IOException {

		Participante participante = (Participante) request.getSession().getAttribute("PARTICIPANTE");
		
		List <Inscricao> inscricaoList = (List <Inscricao>) inscricaoDao.findMinhasInscricaoByCriteria(inscricaofiltro.getEvento(), inscricaofiltro.getData1(), inscricaofiltro.getData2(), inscricaofiltro.getIndicada(), inscricaofiltro.getConfirmada(), participante);
				
		request.setAttribute("inscricaofiltro", inscricaofiltro);		
		request.setAttribute("inscricoes", inscricaoList);
		
		return "forward:/inscricao/minhasinscricoes";
	}

	@RequestMapping(value = "/inscricao/minhasindicacoes", method = RequestMethod.GET)
	public String minhasindicacoes(ModelMap modelMap, HttpServletRequest request) {
		modelMap.addAttribute("inscricoes", inscricaoDao.findByChefe(request.getRemoteUser()));
		modelMap.addAttribute("solicitacoes", eventoextraDao.findByChefe(request.getRemoteUser()));
		
		List<ResponsavelSetor> rs = responsavelSetorDao.findAtivoByResponsavel(usuarioSCADao.findByLogin(request.getRemoteUser()));
		List<EventoRecomendar> sugestoes = new ArrayList<>();
		
		for (ResponsavelSetor responsavelSetor : rs) {
			if (responsavelSetor.getSetor() != null)
				sugestoes.addAll(eventorecomendarDao.findBySetorId(responsavelSetor.getSetor().getId().toString()));
		}
		
		if (sugestoes.size() > 0){		
			modelMap.addAttribute("sugestoes", sugestoes);			
		}		
		
		return "inscricao/minhasindicacoes";
	}
	
	@RequestMapping(value = "/inscricao/exportar/ava", method = RequestMethod.GET)
	public String exportarToAVAForm(ModelMap model) {
		model.addAttribute("inscricaoAVAFiltro", new InscricaoAVAFiltro());
		model.addAttribute("eventos", eventoDao.findForInscricoesSIGEDtoAVA(""));
		return "inscricao/exportarava";
	}
	
	@RequestMapping(value = "/inscricao/exportar/ava", method = RequestMethod.POST)
	public String exportarToAVACSV(@ModelAttribute("inscricaoAVAFiltro") @Valid InscricaoAVAFiltro inscricaoAVAFiltro, BindingResult result, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		if(inscricaoAVAFiltro.getEventoId() == null || inscricaoAVAFiltro.getEventoId() == 0)
			result.addError(new FieldError("inscricaoAVAFiltro", "eventoId", " Campo obrigatório"));
		
		if(inscricaoAVAFiltro.getCursoNoAVA() == null || inscricaoAVAFiltro.getCursoNoAVA().isEmpty())
			result.addError(new FieldError("inscricaoAVAFiltro", "cursoNoAVA", " Campo obrigatório"));
		
		if(result.hasErrors()) {
			model.addAttribute("eventos", eventoDao.findForInscricoesSIGEDtoAVA(""));
			return "inscricao/exportarava";
		}
		
		byte[] csv = relatorioService.inscricoesToAVA(inscricaoAVAFiltro.getEventoId(), inscricaoAVAFiltro.getCursoNoAVA());
		relatorioService.abrirCSV(csv, inscricaoAVAFiltro.getCursoNoAVA(), response);
		
		return null;
	}
	
	@ModelAttribute("eventoList")
	public Collection<Evento> populateEventos() {
		return eventoDao.findAll();
	}	

	@ModelAttribute("SNList")
	public Map<String,String> populateSN() {
		Map<String,String> populate = new LinkedHashMap<String,String>();
		populate.put("-", "-");
		populate.put("S", "S");
		populate.put("N", "N");
		return populate;
	}

	@ModelAttribute("chefeList")
	public Collection<UsuarioSCA> populateChefe() {
		return usuarioSCADao.findServidores();
	}
		
	@ModelAttribute("tipoParticipanteList")
	public Collection<TipoPublicoAlvo> populateTipoParticipante() {
		return tipoPublicoAlvoDao.findAll();
	}
	
}
