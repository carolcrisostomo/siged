package br.com.siged.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.siged.dao.InstrutorDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.SituacaoInstrutorDAO;
import br.com.siged.dao.UfMunicipioDAO;
import br.com.siged.entidades.Instrutor;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.SituacaoInstrutor;
import br.com.siged.entidades.Usuario;
import br.com.siged.entidades.externas.Setor;
import br.com.siged.service.exception.NaoPodeEnviarEmailException;
import br.com.siged.util.EmailUtil;

@Service
public class InstrutorService {
	
	@Autowired
	private InstrutorDAO instrutorDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private UfMunicipioDAO ufMunicipioDao;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private SituacaoInstrutorDAO situacaoInstrutorDao;

	public Instrutor associarAoUsuarioLogado() {
		
		Instrutor instrutor = new Instrutor();
		Usuario usuarioLogado;
		try {
			usuarioLogado = AuthenticationService.getUsuarioLogado();
		} catch (ClassCastException e) {
			usuarioLogado = null;
		}
		
		if(usuarioLogado != null && (usuarioLogado.hasAuthority("ROLE_ALUNO") || usuarioLogado.hasAuthority("ROLE_SERVIDOR"))) {
			Participante participante = participanteDao.findByCpf(usuarioLogado.getCpf());			
			instrutor.setCpf(participante.getCpf());
			instrutor.setNome(participante.getNome());
			instrutor.setEmail(participante.getEmail());
			instrutor.setCelular(participante.getCelular());
			instrutor.setTelefone(participante.getTelefone());
			instrutor.setNivelEscolaridadeId(participante.getEscolaridadeId());
			instrutor.setFormacaoAcademicaId(participante.getFormacaoId());
			instrutor.setSetorId(participante.getSetorId());
			if (participante.getMunicipio() != null) {
				instrutor.setMunicipio(participante.getMunicipio());
				instrutor.setUfMunicipio(ufMunicipioDao.findByUf(participante.getMunicipio().getUfMunicipio()));
			}
		}
		
		return instrutor;
	}
	
	public void salvar(Instrutor instrutor, MultipartFile curriculo, MultipartFile assinatura) throws NaoPodeEnviarEmailException, Exception {
		
		if(instrutor.getId() != null) {
			atualizar(instrutor, curriculo, assinatura);
		} else {
			if (instrutor.getSetorId()!= null && instrutor.getSetorId().getId()==9999){
				Setor setor= new Setor();
				setor.setId(0L);
				instrutor.setSetorId(setor);
			}
			
			if (curriculo != null) {
				instrutor.setCurriculo(curriculo.getBytes());
				instrutor.setCurriculoNome(curriculo.getOriginalFilename());
				instrutor.setCurriculoTipo(curriculo.getContentType());
			}		
			
			instrutor.setDataCadastro(new Date());
			Boolean cadastroConfirmado = confirmarCadastro(instrutor);
			System.out.println(instrutor);
			instrutorDao.persist(instrutor);
			
			if (cadastroConfirmado) {
				try {
					emailUtil.emailConfirmacaoIpcInstrutor(instrutor);
				} catch (Exception e) {
					throw new NaoPodeEnviarEmailException("Não foi possível enviar o email de confirmação de cadastro");
				}
			} else {
				try {
					emailUtil.emailPreCadastro(instrutor);
				} catch (Exception e) {
					throw new NaoPodeEnviarEmailException("Não foi possível enviar o email de pré-cadastro");
				}
			}
		}
	}
	
	public void atualizar(Instrutor instrutor, MultipartFile curriculo, MultipartFile assinatura) throws NaoPodeEnviarEmailException, Exception {
		
		Instrutor instrutorantigo = instrutorDao.find(instrutor.getId());
		
		if (curriculo.getOriginalFilename() != "") {			
			instrutor.setCurriculo(curriculo.getBytes());
			instrutor.setCurriculoNome(curriculo.getOriginalFilename());
			instrutor.setCurriculoTipo(curriculo.getContentType());
		} else {				 
			instrutor.setCurriculo(instrutorantigo.getCurriculo());
			instrutor.setCurriculoNome(instrutorantigo.getCurriculoNome());
			instrutor.setCurriculoTipo(instrutorantigo.getCurriculoTipo());
		}
		
		if (assinatura.getOriginalFilename() != "") {			
			instrutor.setAssinatura(assinatura.getBytes());
			instrutor.setAssinaturaNome(assinatura.getOriginalFilename());
			instrutor.setAssinaturaTipo(assinatura.getContentType());
		} else {			
			instrutor.setAssinatura(instrutorantigo.getAssinatura());
			instrutor.setAssinaturaNome(instrutorantigo.getAssinaturaNome());
			instrutor.setAssinaturaTipo(instrutorantigo.getAssinaturaTipo());
		}		
		
		if (instrutor.getSetorId() != null  &&  instrutor.getSetorId().getId()==9999){
			Setor setor = null;
			instrutor.setSetorId(setor);
		}
	
		if(instrutor.getSituacaoId() == null ){
			instrutor.setSituacaoId(instrutorantigo.getSituacaoId());
		}
		
		Boolean cadastroConfirmado = confirmarCadastro(instrutor);
		
		instrutorDao.merge(instrutor);
		
		if(cadastroConfirmado) {
			try {
				emailUtil.emailConfirmacaoIpcInstrutor(instrutor);
			} catch (Exception e) {
				throw new NaoPodeEnviarEmailException("Não foi possível enviar o email de confirmação de cadastro");
			}
		} else if(instrutor.getSituacaoId() != null && instrutor.getSituacaoId().isNaoAceito()) {
			instrutorDao.remove(instrutorDao.find(instrutor.getId()));
			try {
				emailUtil.emailInstrutorNaoAceito(instrutor,  instrutor.getJustificativa());
			} catch (Exception e) {
				throw new NaoPodeEnviarEmailException("Não foi possível enviar o email de instrutor não aceito");
			}
			
		}
	}
	
	private Boolean confirmarCadastro(Instrutor instrutor) {
		SituacaoInstrutor confirmado = situacaoInstrutorDao.findById(SituacaoInstrutor.CADASTRADO);
		SituacaoInstrutor naoConfirmado = situacaoInstrutorDao.findById(SituacaoInstrutor.PRE_CADASTRADO);
		
		if(instrutor.getId() != null) {
			Instrutor instrutorantigo = instrutorDao.find(instrutor.getId());
			if(instrutorantigo.getSituacaoId() != null && instrutorantigo.getSituacaoId().isPreCadastrado() 
					&& instrutor.getSituacaoId() != null && instrutor.getSituacaoId().isCadastrado())  {
				
				instrutor.setDataCadastro(new Date());
				return true;
			} else {
				instrutor.setDataCadastro(instrutorantigo.getDataCadastro());
				return false;
			}
		} else {
			Usuario usuarioLogado;
			try {
				usuarioLogado = AuthenticationService.getUsuarioLogado();
			} catch (ClassCastException e) {
				usuarioLogado = null;
			}
			
			if (usuarioLogado != null && usuarioLogado.hasAuthority("ROLE_ADMINISTRADOR")) {
				instrutor.setSituacaoId(confirmado);
				return true;
			} else {
				instrutor.setSituacaoId(naoConfirmado);
				return false;
			}
		}
	}
}
