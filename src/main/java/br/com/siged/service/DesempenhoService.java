package br.com.siged.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import br.com.siged.dao.DesempenhoDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.FrequenciaDAO;
import br.com.siged.dao.InscricaoDAO;
import br.com.siged.dao.ModuloDAO;
import br.com.siged.dao.NotaDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.entidades.Desempenho;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Frequencia;
import br.com.siged.entidades.Inscricao;
import br.com.siged.entidades.Modalidade;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Nota;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.StatusDesempenho;
import br.com.siged.entidades.TipoPublicoAlvo;
import br.com.siged.event.CertificadosInvalidadosEvent;
import br.com.siged.event.MotivoCertificadosInvalidados;
import br.com.siged.service.exception.NaoPodeApurarEventoException;
import br.com.siged.service.exception.NaoPodeEnviarEmailException;
import br.com.siged.util.Util;

@Service
public class DesempenhoService {
	
	@Autowired
	private DesempenhoDAO desempenhoDao;
	@Autowired
	private FrequenciaDAO frequenciaDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private InscricaoDAO inscricaoDao;
	@Autowired
	private NotaDAO notaDao;
	@Autowired
	private ModuloDAO moduloDao;
	@Autowired
	private Util util;
	@Autowired
	private EventoService eventoService;
	@Autowired
	private ModuloService moduloService;
	@Autowired
	private ApplicationEventPublisher publisher;
	
	/**
	 * @author estag_12977 (Rafael Castro)
	 * @param participante
	 * @param evento
	 * @return
	 */
	public StatusDesempenho statusDesempenhoParticipanteNoEvento(Participante participante, Evento evento) {
		if(participante != null && evento != null) {
			List<Desempenho> desempenhoParcial = desempenhoDao.findByEventoAndParticipanteAndParcial(evento, participante);
			if(desempenhoParcial.size() > 0) {
				return StatusDesempenho.PARCIAL;
			}
			
			List<Desempenho> desempenhoReprovado = desempenhoDao.findByEventoAndParticipanteAndAprovado(evento, participante, "N");
			if (desempenhoReprovado.size() > 0) {
				return StatusDesempenho.REPROVADO;
			} else {
				List<Desempenho> desempenhoAprovado = desempenhoDao.findByEventoAndParticipanteAndAprovado(evento, participante, "S");
				if (desempenhoAprovado.size() > 0) {
					return StatusDesempenho.APROVADO;
				} else {
					return StatusDesempenho.NENHUM;
				}
			}
		}
		
		return StatusDesempenho.REPROVADO;
	}
	
	public String notaDoParticipanteNoEvento(Participante participante, Evento evento) {
		List<Desempenho> desempenhoParticipanteNoEvento = new ArrayList<>();
		if(participante != null && evento != null) {
			desempenhoParticipanteNoEvento = desempenhoDao.findByEventoAndParticipante(evento, participante);
		}
		
		double nota = 0.0;
		String notaFinal = "";
		boolean calcularMedia = false;
		
		if(desempenhoParticipanteNoEvento.size() == 1){
			notaFinal = desempenhoParticipanteNoEvento.get(0).getNota();
		} else if (desempenhoParticipanteNoEvento.size() > 1) {
			calcularMedia = true;
			for (Desempenho desempenho : desempenhoParticipanteNoEvento) {
				if(!desempenho.getNota().equals("-")){
					String notaNoModulo = desempenho.getNota().replace(",", ".");
					nota += Double.parseDouble(notaNoModulo);
				} else {
					notaFinal = "-";
				}
			}
		}
		
		if(calcularMedia && !notaFinal.equals("-")){
			nota = nota/(double)desempenhoParticipanteNoEvento.size();
			notaFinal = util.formataDouble(nota).replace(",", ".");
		}
		
		return notaFinal;
	}

	public String frequenciaDoParticipanteNoEvento(Participante participante, Evento evento) {
		List<Desempenho> desempenhoParticipanteNoEvento = new ArrayList<>();
		if(participante != null && evento != null) {
			desempenhoParticipanteNoEvento = desempenhoDao.findByEventoAndParticipante(evento, participante);
		}

		double frequencia = 0.0;
		String frequenciaFinal = "";
		boolean calcularMedia = false;

		if(desempenhoParticipanteNoEvento.size() == 1){
			frequenciaFinal = desempenhoParticipanteNoEvento.get(0).getFrequencia();
		} else if (desempenhoParticipanteNoEvento.size() > 1) {
			calcularMedia = true;
			for (Desempenho desempenho : desempenhoParticipanteNoEvento) {
				if(desempenho.getModuloId().getModalidade().isPresencial() && !desempenho.getFrequencia().equals("-")){
					String frequenciaNoModulo = desempenho.getFrequencia().replace(",", ".");
					frequencia += Double.parseDouble(frequenciaNoModulo);
				} else {
					frequenciaFinal = "-";
				}
				
			}
		}
		
		if(calcularMedia && !frequenciaFinal.equals("-")){
			frequencia = (double)frequencia/desempenhoParticipanteNoEvento.size();
			frequenciaFinal = util.formataDouble(frequencia).replace(",", ".");
		}
		
		return frequenciaFinal;
	}
	
	public String maiorNotaDoParticipanteNoEvento(Participante participante, Evento evento) {
		List<Desempenho> desempenhoParticipanteNoEvento = new ArrayList<>();
		if(participante != null && evento != null)
			desempenhoParticipanteNoEvento = desempenhoDao.findByEventoAndParticipante(evento, participante);
		
		String notaDoEvento = "-";
		
		for (Desempenho desempenho : desempenhoParticipanteNoEvento) {
			String notaDoModulo = desempenho.getNota();
			if(notaDoModulo != null){
				notaDoModulo = notaDoModulo.replace(",", ".");
				if (notaDoEvento.equals("-") || (!notaDoModulo.equals("-") && Double.parseDouble(notaDoModulo) < Double.parseDouble(notaDoEvento)))
					notaDoEvento = notaDoModulo;
			}
		}
		
		if(notaDoEvento.equals("-"))
			return notaDoEvento;
		
		return String.format("%.1f", Double.parseDouble(notaDoEvento));
	}
	
	public String maiorFrequenciaDoParticipanteNoEvento(Participante participante, Evento evento) {
		List<Desempenho> desempenhoParticipanteNoEvento = new ArrayList<>();
		if(participante != null && evento != null)
			desempenhoParticipanteNoEvento = desempenhoDao.findByEventoAndParticipante(evento, participante);
		
		String frequenciaDoEvento = "-";
		
		for (Desempenho desempenho : desempenhoParticipanteNoEvento) {
			String frequenciaDoModulo = desempenho.getFrequencia();
			if(frequenciaDoModulo != null){
				frequenciaDoModulo = frequenciaDoModulo.replace(",", ".");
				if (frequenciaDoEvento.equals("-") || (!frequenciaDoModulo.equals("-") && Double.parseDouble(frequenciaDoModulo) < Double.parseDouble(frequenciaDoEvento)))
					frequenciaDoEvento = frequenciaDoModulo;				
			}
		}
		
		if(frequenciaDoEvento.equals("-"))
			return frequenciaDoEvento;
		
		return String.format("%.1f", Double.parseDouble(frequenciaDoEvento));
	}
	
	
	/**
	 * Verifica todas as condições para que o Evento, ou seus Módulos, possa ser apurado
	 * @param evento
	 * @param modelMap
	 * @throws NaoPodeApurarEventoException
	 */
	public void apurarEvento(Evento evento, ModelMap modelMap) throws NaoPodeApurarEventoException {
		verificarRestricaoApuracao(evento);
		
		/**
		 * Para eventos encerrados, então a apuração será total 
		 */	
		if(eventoService.isRealizado(evento)) {
			Evento eventoApurado = eventoDao.findByEventoApurado(evento.getId());
			String emailsErrors = "";
			
			apurarDesempenho(evento, modelMap);
			
			/**
			 * Verifica se algum participante mudou o resultado (aprovado/não aprovado). 
			 * Essa verificação é necessária para inválidar os certificados emitidos pelo participante antes de ficar reprovado
			 */
			for(Participante participante : participanteDao.findByEventoId(evento.getId())) {
				StatusDesempenho status = statusDesempenhoParticipanteNoEvento(participante, evento);
				if(eventoApurado != null && status.equals(StatusDesempenho.REPROVADO)) {
					try {
						publisher.publishEvent(new CertificadosInvalidadosEvent(this, evento, participante, MotivoCertificadosInvalidados.PARTICIPANTE_REPROVADO));
					} catch (NaoPodeEnviarEmailException e) {
						emailsErrors += e.getMessage() + ", ";
					}
				}
			}
			
			if(!emailsErrors.equals(""))
				modelMap.addAttribute("emailErrorMsg", "Ocorreu um erro ao enviar o email sobre certificados invalidados para: " + emailsErrors);
		
		/** 
		 * Para eventos em andamento, então a apuração será parcial, apurando somente os módulos encerrados
		 */	
		} else if (eventoService.isEmAndamento(evento) && eventoService.temModuloRealizado(evento)) {
			
			/**
			 * Para os módulos que estiverem encerrados, verifica também se existe pendência de notas ou frequências não lançadas, caso o módulo exiga um ou outro
			 */
			List<Desempenho> desempenhoResultado = new ArrayList<Desempenho>();
			List<Modulo> modulosParaApurar = new ArrayList<>();
			Map<Long, String> modulosParaNaoApurar = new HashMap<>();
			for(Modulo modulo : evento.getModuloList()) {
				if(moduloService.isRealizado(modulo)) {
					List<Frequencia> frequencias = frequenciaDao.findByModulo(modulo);
					List<Nota> notas = notaDao.findByModulo(modulo);
					if(modulo.getTemFrequencia() && frequencias.isEmpty())
						modulosParaNaoApurar.put(modulo.getId(), "Módulo não apurado: " + modulo.getTitulo() + " (sem nenhuma frequência lançada)");
					
					if(modulo.getTemNota() && notas.isEmpty()) {
						if(modulosParaNaoApurar.containsKey(modulo.getId()))
							modulosParaNaoApurar.put(modulo.getId(), modulosParaNaoApurar.get(modulo.getId()).replace(")", "") + " e sem nenhuma nota lançada)");
						else
							modulosParaNaoApurar.put(modulo.getId(), "Módulo não apurado: " + modulo.getTitulo() + " (sem nenhuma nota lançada)");
					}
					
					if(!modulosParaNaoApurar.containsKey(modulo.getId()))
						modulosParaApurar.add(modulo);
				}
			}
			
			if(modulosParaApurar.size() == evento.getModuloList().size()) {
				apurarDesempenho(evento, modelMap);
			} else if(modulosParaApurar.size() > 0) {
				desempenhoDao.deleteByEventoId(evento.getId());
				for(Modulo modulo : modulosParaApurar) {
					apurarDesempenho(modulo, modelMap, desempenhoResultado);
				}
				if(modelMap != null) {
					modelMap.addAttribute("desempenho", desempenhoResultado);
					modelMap.addAttribute("parcial", true);
					modelMap.addAttribute("modulosNaoApurados", modulosParaNaoApurar.values());
					modelMap.addAttribute("modulosApurados", modulosParaApurar);
					modelMap.addAttribute("evento", evento);
				}
			} else {
				throw new NaoPodeApurarEventoException("Nenhum módulo do evento pode ser apurado por não ter frequências ou notas lançadas");
			}
			
		} else {
			throw new NaoPodeApurarEventoException("Evento não pode ser apurado");
		}
	}

	/**
	 * Verifica se há restrições para a apuração do Evento
	 * @param evento
	 * @throws NaoPodeApurarEventoException
	 */
	private void verificarRestricaoApuracao(Evento evento) throws NaoPodeApurarEventoException {
		List<Inscricao> inscricoesConfirmadas = inscricaoDao.findByEventoAndConfirmada(evento.getId(), "S");
		if(inscricoesConfirmadas.isEmpty()) {
			throw new NaoPodeApurarEventoException("Evento sem nenhuma pré-inscrição confirmada");
		}
		
		if(evento.getDataInicioRealizacao() == null || evento.getDataFimRealizacao() == null) {
			throw new NaoPodeApurarEventoException("Evento sem data de realização");
		}
		
		String publicoAlvo = evento.getPublicoAlvoLista();
		for(Inscricao inscricao : inscricoesConfirmadas) {
			TipoPublicoAlvo tipoParticipante = inscricao.getParticipanteId().getTipoId();
			if(!publicoAlvo.contains(tipoParticipante.getDescricao())) {
				throw new NaoPodeApurarEventoException("Tipo do participante " + inscricao.getParticipanteId().getNome() + " não compatível com os tipos do público-alvo do evento");
			}
		}
		
		List<Modulo> modulos = moduloDao.findByEventoId(evento.getId());
		if (modulos.isEmpty())
			throw new NaoPodeApurarEventoException("Evento em nenhum módulo cadastrado");
	}
	
	/**
	 * Apura somente o Módulo
	 * @param modulo
	 * @param modelMap
	 * @param desempenhoResultado
	 */
	private void apurarDesempenho(Modulo modulo, ModelMap modelMap, List<Desempenho> desempenhoResultado) {
		
		List<Desempenho> desempenhoList = desempenhoDao.findNotaAndFrequenciaByModuloId(modulo);

		for (Desempenho desempenho : desempenhoList) {
			Desempenho desempenhoPersist = this.calcularDesempenho(desempenho, true);
			desempenhoDao.persist(desempenhoPersist);
			desempenhoResultado.add(desempenhoPersist);
		}
		
	}
	
	/**
	 * Apura o Evento
	 * @param evento
	 * @param modelMap
	 */
	private void apurarDesempenho(Evento evento, ModelMap modelMap) {
		desempenhoDao.deleteByEventoId(evento.getId());

		List<Desempenho> desempenhoFinalList = new ArrayList<Desempenho>();
		List<Desempenho> desempenhoResultado = new ArrayList<Desempenho>();	
		int totalDeAprovados = 0;

		List<Desempenho> desempenhoList = desempenhoDao.findNotaAndFrequenciaByEventoId(evento);
		if (desempenhoList.size() == 0) {
			desempenhoList = desempenhoDao.findFrequenciaByEventoId(evento);
		}		

		for (Desempenho desempenho: desempenhoList) {
			Desempenho desempenhoPersist = this.calcularDesempenho(desempenho, false);
			desempenhoDao.persist(desempenhoPersist);
			desempenhoResultado.add(desempenhoPersist);
		}
		
		List<Inscricao> inscricoes = inscricaoDao.findByEventoAndConfirmada(evento.getId(), "S");
		for(Inscricao inscricao : inscricoes) {
			Participante participante = inscricao.getParticipanteId();
			StatusDesempenho statusDesempenho = statusDesempenhoParticipanteNoEvento(participante, evento);
			if(statusDesempenho.equals(StatusDesempenho.APROVADO)) {
				totalDeAprovados++;				
			}
			Desempenho df = new Desempenho();
			df.setParticipanteId(participante);
			df.setAprovado(statusDesempenho.getAprovado());
			desempenhoFinalList.add(df);
		}

		if(modelMap != null) {
			modelMap.addAttribute("desempenho", desempenhoResultado);
			modelMap.addAttribute("parcial", false);
			modelMap.addAttribute("desempenhoFinal", desempenhoFinalList);
			modelMap.addAttribute("totalDeParticipantes", desempenhoFinalList.size());
			if( desempenhoFinalList.size() > 0 ){
				modelMap.addAttribute("totalDeAprovados",totalDeAprovados);
				modelMap.addAttribute("taxaDeAproveitamento",String.format("%.1f",(1.0*totalDeAprovados/desempenhoFinalList.size())*100));
			}
			modelMap.addAttribute("evento", evento);
		}
	}
	
	private Desempenho calcularDesempenho(Desempenho desempenho, Boolean parcial) {
		String frequenciaPerc = "0";
		String aprovadoN;
		String aprovadoF;
		String aprovado;		
		String frequenciaModuloPerc;
		long fm = 0;
		
		Modulo m = desempenho.getModuloId();

		//Frequencias possiveis no modulo
		fm = frequenciaDao.findByModulo(m).size();

		//Percentual minimo de frequencia no modulo 
		frequenciaModuloPerc = m.getFrequencia();

		frequenciaPerc = util.calculaFrequenciaPerc(String.valueOf(desempenho.getFrequencia()), String.valueOf(fm));
		aprovadoF = util.calculaFrequencia(frequenciaPerc, frequenciaModuloPerc);
		aprovadoN = util.calculaNota(desempenho.getNota(), desempenho.getModuloId().getNota());
		aprovado = util.calculaAprovacao(aprovadoN, aprovadoF);

		Desempenho desempenhoPersist = new Desempenho(desempenho.getEventoId(), 
				desempenho.getModuloId(), 
				desempenho.getParticipanteId(), 
				desempenho.getNota(), 
				frequenciaPerc, 
				aprovado);
		desempenhoPersist.setParcial(parcial);
		
		return desempenhoPersist;
	}
}
