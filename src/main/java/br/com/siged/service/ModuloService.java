package br.com.siged.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.dao.CertificadoEmitidoDAO;
import br.com.siged.dao.EventoDAO;
import br.com.siged.dao.ModuloDAO;
import br.com.siged.entidades.CertificadoEmitido;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Modulo;
import br.com.siged.event.CertificadosInvalidadosEvent;
import br.com.siged.event.MotivoCertificadosInvalidados;
import br.com.siged.service.exception.NaoPodeEnviarEmailException;
import br.com.siged.service.exception.NaoPodeSalvarModuloException;
import br.com.siged.util.Util;

@Service
public class ModuloService {
	
	@Autowired
	private Util util;
	@Autowired
	private EventoDAO eventoDao;
	@Autowired
	private ModuloDAO moduloDao;
	@Autowired
	private CertificadoEmitidoDAO certificadoEmitidoDao;
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Transactional
	public void salvar(Modulo modulo) throws NaoPodeSalvarModuloException {
		if(modulo == null)
			throw new NaoPodeSalvarModuloException("Módulo não informado");
		
		if(modulo.getEventoId() == null)
			throw new NaoPodeSalvarModuloException("Evento não informado");
		
		/*if (!modulo.getModalidade().isPresencial()) {
			modulo.setHoraInicioTurno1("");
			modulo.setHoraFimTurno1("");
			modulo.setHoraInicioTurno2("");
			modulo.setHoraFimTurno2("");
			modulo.setHoraInicioTurno3("");
			modulo.setHoraFimTurno3("");
		}*/
		
		if(modulo.getId() != null) {
			atualizar(modulo);
		} else {
			Evento evento = eventoDao.find(modulo.getEventoId().getId());
			List<Modulo> modulos = moduloDao.findByEventoId(evento.getId());
			
			if(modulos.size() > 0 && evento.getModuloUnico().compareToIgnoreCase("S") == 0){
				evento.setModuloUnico("N");
				eventoDao.merge(evento);			
			}		
			 
			//modulo.setDataCadastro(new Date());
			moduloDao.persist(modulo);
		}
	}
	
	private void atualizar(Modulo modulo) {
		List<CertificadoEmitido> certificadosEmitidos = certificadoEmitidoDao.findByEventoId(modulo.getEventoId().getId());
		
		Modulo moduloAntigo = moduloDao.find(modulo.getId());
		
		if (certificadosEmitidos != null && certificadosEmitidos.size() > 0 && !modulo.getInstrutorList().equals(moduloAntigo.getInstrutorList())){
			try {
				publisher.publishEvent(new CertificadosInvalidadosEvent(this, modulo.getEventoId(), null, MotivoCertificadosInvalidados.ALTERACAO_INSTRUTORES));
			} catch (NaoPodeEnviarEmailException e) {
				throw new NaoPodeSalvarModuloException(e.getMessage());
			}
		} else if (!modulo.getLocalizacaoId().getId().equals(moduloAntigo.getLocalizacaoId().getId())) {
			try {
				publisher.publishEvent(new CertificadosInvalidadosEvent(this, modulo.getEventoId(), null, MotivoCertificadosInvalidados.ALTERACAO_LOCALIZACAO));				
			} catch (NaoPodeEnviarEmailException e) {
				throw new NaoPodeSalvarModuloException(e.getMessage());
			}
		}
		
		moduloDao.merge(modulo);
	}
	
	public boolean isPeriodoAvaliacaoReacao(Modulo modulo){
		if(modulo.getDataFim() == null) {
			return false;
		}
		Calendar dataAtual = Calendar.getInstance();
		
		Calendar dataStart = Calendar.getInstance();
		dataStart.setTime(modulo.getDataFim());
		dataStart.add(Calendar.DATE, +1);
		
		Calendar dataEnd = Calendar.getInstance();
		dataEnd.setTime(modulo.getDataFim());
		dataEnd.add(Calendar.DATE, +31);
		
		return dataAtual.compareTo(dataStart) > 0 && dataAtual.compareTo(dataEnd) <= 0;
	}
	
	public boolean isPeriodoAvaliacaoReacaoADMIN(Modulo modulo){
		if(modulo.getDataFim() == null) {
			return false;
		}
		Calendar dataAtual = Calendar.getInstance();
		
		Calendar dataStart = Calendar.getInstance();
		dataStart.setTime(modulo.getDataFim());
		dataStart.add(Calendar.DATE, +1);
		
		return dataAtual.compareTo(dataStart) > 0;
	}
	
	public boolean isRealizado(Modulo modulo) {
		Date hoje = util.getDataFormatada(new Date());
		Date dataFim = util.getDataFormatada(modulo.getDataFim());
		return hoje.compareTo(dataFim) > 0;
	}
	
}
