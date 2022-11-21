package br.com.siged.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

import br.com.siged.dao.EventoExtraDAO;
import br.com.siged.dao.InscricaoDAO;
import br.com.siged.dao.ResponsavelSetorDAO;
import br.com.siged.dao.SetorDAO;
import br.com.siged.dao.UsuarioSCADAO;
import br.com.siged.editor.ResponsavelSetorEditor;
import br.com.siged.editor.SetorEditor;
import br.com.siged.editor.UsuarioSCAEditor;
import br.com.siged.entidades.EventoExtra;
import br.com.siged.entidades.Inscricao;
import br.com.siged.entidades.ResponsavelSetor;
import br.com.siged.entidades.externas.UsuarioSCA;
import br.com.siged.filtro.ResponsavelSetorFiltro;

@Controller
@RequestMapping("/responsavelsetor/**")
public class ResponsavelSetorController {
	@Autowired
	private SetorDAO setorDao;
	@Autowired
	private ResponsavelSetorDAO responsavelSetorDao;
	@Autowired
	private UsuarioSCADAO usuarioDao;
	@Autowired
	private InscricaoDAO inscricaoDao;
	@Autowired
	private EventoExtraDAO eventoExtraDao;
	

	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Setor.class, new SetorEditor(setorDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.ResponsavelSetor.class, new ResponsavelSetorEditor(responsavelSetorDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.externas.UsuarioSCA.class, new UsuarioSCAEditor(usuarioDao));
    }
	
	@RequestMapping(value = "/responsavelsetor/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		modelMap.addAttribute("responsavelSetor", responsavelSetorDao.find(id));
		return "responsavelsetor/show";
	}	

	@RequestMapping(value = "/responsavelsetor", method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("setorList", setorDao.findAll());
		modelMap.addAttribute("responsavelSetorfiltro", new ResponsavelSetor());
		modelMap.addAttribute("responsaveis", responsavelSetorDao.findAll());
		return "responsavelsetor/list";
	}
	
	@RequestMapping(value = "/responsavelsetor/search", method = RequestMethod.GET)
	public String search(ResponsavelSetorFiltro responsavelSetorfiltro, ModelMap modelMap, ServletRequest Request, ServletResponse Response) throws IOException {
		modelMap.addAttribute("setorList", setorDao.findAll());
		modelMap.addAttribute("responsavelSetorfiltro", responsavelSetorfiltro);
		modelMap.addAttribute("responsaveis", responsavelSetorDao.findResponsavelSetorByCriteria(responsavelSetorfiltro.getSetor()));
		return "responsavelsetor/list";
	}
	
	@RequestMapping(value = "/responsavelsetor/{id}", method = RequestMethod.DELETE)
	@Transactional
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		
		ResponsavelSetor rs = responsavelSetorDao.find(id);
		List<ResponsavelSetor> rsAtivo = responsavelSetorDao.findAtivoByResponsavel(rs.getResponsavel());
		
		for (ResponsavelSetor responsavelSetor : rsAtivo) {
			if( responsavelSetor.getSetor().getId() == rs.getSetor().getId()){
				model.addAttribute("mensagemErro", "Ação não permitida. O Responsável encontra-se ativo.");
				return "redirect:/responsavelsetor";
			}
		}		
		
		responsavelSetorDao.remove(rs);
		return "redirect:/responsavelsetor";
	}

	@RequestMapping(value = "/responsavelsetor/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap) {
		modelMap.addAttribute("setorList", setorDao.findAll());
		modelMap.addAttribute("responsavelSetor", new ResponsavelSetor());
		return "responsavelsetor/create";
	}

	@RequestMapping(value = "/responsavelsetor", method = RequestMethod.POST)
	@Transactional
	public String create(@ModelAttribute("responsavelSetor") @Valid ResponsavelSetor responsavelSetor, BindingResult result, ModelMap modelMap) {
		
		if (responsavelSetor.getSetor().getId() == 0) {
			result.addError(new FieldError("setor", "setor", "Campo Obrigatório"));			
		} else if (responsavelSetor.getResponsavel().getId() == 0 || responsavelSetor.getResponsavel().getId() == 109) {
			result.addError(new FieldError("responsavelSetor", "responsavel", "Campo Obrigatório"));			
		} else if (responsavelSetor.getResponsavelImediato().getId() != 0	
				&& responsavelSetor.getResponsavelImediato().getCpf().equals(responsavelSetor.getResponsavel().getCpf())){
			result.addError(new FieldError("responsavelSetor", "responsavelImediato", "O Responsável Imediato deve ser diferente do Responsável selecionado"));
		}		
				
		if (result.hasErrors()) {
			modelMap.addAttribute("setorList", setorDao.findAll());
			return "responsavelsetor/create";
        }
		
		List<ResponsavelSetor> rsList = responsavelSetorDao.findBySetorId(responsavelSetor.getSetor());
		
		boolean responsavelJaCadastrado = false;
		
		for (ResponsavelSetor rs : rsList) {
			if(rs.getResponsavel().getId() == responsavelSetor.getResponsavel().getId()){
				responsavelJaCadastrado = true;
				break;
			}
		}		
		
		if (!responsavelJaCadastrado) {
			if (rsList == null || rsList.size() == 0)				
				responsavelSetor.setAtivo(true);
			else
				responsavelSetor.setAtivo(false);
				
			responsavelSetorDao.persist(responsavelSetor);		
		}
		
		return "redirect:/responsavelsetor";
	}

	@RequestMapping(value = "/responsavelsetor/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		
		ResponsavelSetor rs = responsavelSetorDao.find(id);
		
		modelMap.addAttribute("responsavelSetor", rs);		
		
		modelMap.addAttribute("responsavelImediatoList", usuarioDao.findResponsavelIndicacao());				
		
		return "responsavelsetor/update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	@Transactional
	public String update(@ModelAttribute("responsavelSetor") @Valid ResponsavelSetor responsavelSetor, BindingResult result, ModelMap modelMap) {
				
		ResponsavelSetor rs = responsavelSetorDao.find(responsavelSetor.getId());
		
		if(responsavelSetor.getResponsavelImediato().getId() != 0	
				&& responsavelSetor.getResponsavelImediato().getCpf().equals(rs.getResponsavel().getCpf()))
			result.addError(new FieldError("responsavelSetor", "responsavelImediato", "O Responsável Imediato deve ser diferente do Responsável selecionado"));
		
		if (result.hasErrors()) {
			responsavelSetor.setResponsavel(rs.getResponsavel());
			responsavelSetor.setSetor(rs.getSetor());
			modelMap.addAttribute("responsavelSetor", responsavelSetor);
			modelMap.addAttribute("responsavelImediatoList", usuarioDao.findResponsavelIndicacao());
			return "responsavelsetor/update";
        }		
		
		ResponsavelSetor responsavelAtivo = responsavelSetorDao.find(responsavelSetor.getId());
		
		
		// TRANSFERE AS INDICAÇÕES DE PRÉ-INSCRIÇÕES E SOLICITAÇÕES FEITAS PELO RESPONSÁVEL ATIVO QUE ESTAVAM COM SEU ANTIGO RESPONSÁVEL IMEDIATO PARA O NOVO RESPONSÁVEL IMEDIATO
		
		// PRÉ-INSCRIÇÕES
				
		List<Inscricao> inscricoesASeremIndicadas = inscricaoDao.findByChefeAndIndicadaAndConfirmada(responsavelAtivo.getResponsavelImediato().getId(), "N", "-");
		
		for (Inscricao i: inscricoesASeremIndicadas) {
			
			if(i.getParticipanteId().getCpf().equals(responsavelAtivo.getResponsavel().getCpf())){
				
				i.setChefeId(responsavelSetor.getResponsavelImediato());					
			
				inscricaoDao.merge(i);
			}
		}
		
		
		// SOLICITAÇÕES
		
		List<EventoExtra> solicitacoesASeremIndicadas = eventoExtraDao.findByChefeAndIndicada(responsavelAtivo.getResponsavelImediato().getId(), "N");
		
		for(EventoExtra solicitacao : solicitacoesASeremIndicadas){
			
			if(solicitacao.getInscricaoList() == null || solicitacao.getInscricaoList().size() == 0){
				
				if(solicitacao.getSolicitanteId().equals(responsavelAtivo.getResponsavel())){
					
					solicitacao.setChefeId(responsavelSetor.getResponsavelImediato());							
				
					eventoExtraDao.merge(solicitacao);
				}
			}
		}
		
		
		responsavelAtivo.setResponsavelImediato(responsavelSetor.getResponsavelImediato());
		
		responsavelSetorDao.merge(responsavelAtivo);
		
				
		return "redirect:/responsavelsetor";
	}
	
	@RequestMapping(value = "/responsavelsetor/{id}/status", method = RequestMethod.GET)
	@Transactional
	public String status(@PathVariable("id") Long id, ModelMap modelMap, ResponsavelSetorFiltro responsavelSetorfiltro) {
				
		// RESPONSÁVEL INATIVO SELECIONADO NA TELA
		ResponsavelSetor novoResponsavelAtivo = responsavelSetorDao.find(id);		
		
		// RESPONSÁVEL ATIVO ARMAZENADO NO BANCO
		ResponsavelSetor antigoResponsavelAtivo = responsavelSetorDao.findAtivoBySetorId(novoResponsavelAtivo.getSetor().getId());
		
		List<ResponsavelSetor> responsaveisDoSetor;

		novoResponsavelAtivo.setAtivo(true);
		
		if (antigoResponsavelAtivo != null) {
			
			novoResponsavelAtivo.setResponsavelImediato(antigoResponsavelAtivo.getResponsavelImediato());
			
			List<Inscricao> inscricoes;
			List<EventoExtra> solicitacoes;
			
			// TRANSFERE AS INDICAÇÕES DO ANTIGO RESPONSÁVEL ATIVO PARA O NOVO RESPONSÁVEL ATIVO
			
				// PRÉ-INSCRIÇÕES
				
				inscricoes = inscricaoDao.findByChefeAndIndicadaAndConfirmada(antigoResponsavelAtivo.getResponsavel().getId(), "N", "-");
				
				for (Inscricao i: inscricoes) {
					
					if(i.getParticipanteId().getCpf().equals(novoResponsavelAtivo.getResponsavel().getCpf()))
						i.setChefeId(novoResponsavelAtivo.getResponsavelImediato());
					else
						i.setChefeId(novoResponsavelAtivo.getResponsavel());
					
					inscricaoDao.merge(i);
				}
				
				
				// SOLICITAÇÕES
				
				solicitacoes = eventoExtraDao.findByChefeAndIndicada(antigoResponsavelAtivo.getResponsavel().getId(), "N");
				
				for(EventoExtra solicitacao : solicitacoes){
					
					if(solicitacao.getInscricaoList() == null || solicitacao.getInscricaoList().size() == 0){
						
						if(solicitacao.getSolicitanteId().equals(novoResponsavelAtivo.getResponsavel()))
							solicitacao.setChefeId(novoResponsavelAtivo.getResponsavelImediato());
						else
							solicitacao.setChefeId(novoResponsavelAtivo.getResponsavel());
						
						eventoExtraDao.merge(solicitacao);					
					}
				}
			
			
			
			// TRANSFERE AS INDICAÇÕES DE PRÉ-INSCRIÇÕES E SOLICITAÇÕES FEITAS PELO ANTIGO RESPONSAVEL ATIVO QUE ESTAVAM COM SEU RESPONSÁVEL IMEDIATO PARA O NOVO RESPONSÁVEL ATIVO
			
				// PRÉ-INSCRIÇÕES
				
				inscricoes = inscricaoDao.findByChefeAndIndicadaAndConfirmada(antigoResponsavelAtivo.getResponsavelImediato().getId(), "N", "-");
				
				for (Inscricao i: inscricoes) {
					
					if(i.getParticipanteId().getCpf().equals(antigoResponsavelAtivo.getResponsavel().getCpf())){
						
						i.setChefeId(novoResponsavelAtivo.getResponsavel());					
					
						inscricaoDao.merge(i);
					}
				}
				
				
				// SOLICITAÇÕES
				
				solicitacoes = eventoExtraDao.findByChefeAndIndicada(antigoResponsavelAtivo.getResponsavelImediato().getId(), "N");
				
				for(EventoExtra solicitacao : solicitacoes){
					
					if(solicitacao.getInscricaoList() == null || solicitacao.getInscricaoList().size() == 0){
						
						if(solicitacao.getSolicitanteId().equals(antigoResponsavelAtivo.getResponsavel())){
							
							solicitacao.setChefeId(novoResponsavelAtivo.getResponsavel());							
						
							eventoExtraDao.merge(solicitacao);
						}
					}
				}
		}

		responsavelSetorDao.merge(novoResponsavelAtivo);
		
		
		responsaveisDoSetor = responsavelSetorDao.findBySetorId(novoResponsavelAtivo.getSetor());
		if (responsaveisDoSetor.contains(novoResponsavelAtivo)) {
			responsaveisDoSetor.remove(novoResponsavelAtivo);
		}
		
		for (ResponsavelSetor rs: responsaveisDoSetor) {
			rs.setResponsavelImediato(novoResponsavelAtivo.getResponsavel());
			rs.setAtivo(false);
			responsavelSetorDao.merge(rs);
		}		
		
		
		responsavelSetorfiltro.setSetor(novoResponsavelAtivo.getSetor());
		
		modelMap.addAttribute("setorList", setorDao.findAll());
		modelMap.addAttribute("responsavelSetorfiltro", responsavelSetorfiltro);
		modelMap.addAttribute("responsaveis", responsavelSetorDao.findResponsavelSetorByCriteria(novoResponsavelAtivo.getSetor()));
		
		return "responsavelsetor/list";
	}
	
	@RequestMapping(value = "/responsavelsetor/procuraResponsavelSetorAtivo", method = RequestMethod.GET)
    @ResponseBody
    public List<UsuarioSCA> procuraResponsavelSetorAtivo(@RequestParam(value="setorId") Long setorId) {
        List<UsuarioSCA> uList = new ArrayList<UsuarioSCA>();
       	ResponsavelSetor rs = responsavelSetorDao.findAtivoBySetorId(setorId);
        if (rs == null) {
        	uList.addAll(usuarioDao.findResponsavelIndicacao());
        	return uList;
        } else {
        	uList.add(rs.getResponsavel());
        	return uList;
        }
    }
	
	@RequestMapping(value = "/responsavelsetor/procuraUsuariosBySetor", method = RequestMethod.GET)
    @ResponseBody
    public List<UsuarioSCA> procuraUsuariosBySetor(@RequestParam(value="setorId") Long setorId) {
        return usuarioDao.findServidorBySetorId(setorId);
    }

}
