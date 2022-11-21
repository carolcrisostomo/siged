package br.com.siged.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.displaytag.pagination.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import br.com.siged.dao.CidadeDAO;
import br.com.siged.dao.FormacaoAcademicaDAO;
import br.com.siged.dao.InstrutorDAO;
import br.com.siged.dao.MunicipioDAO;
import br.com.siged.dao.NivelEscolaridadeDAO;
import br.com.siged.dao.PaisDAO;
import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.dao.SetorDAO;
import br.com.siged.dao.SituacaoInstrutorDAO;
import br.com.siged.dao.TipoInstrutorDAO;
import br.com.siged.dao.UfDAO;
import br.com.siged.dao.UfMunicipioDAO;
import br.com.siged.dao.pagination.DisplayTagPageable;
import br.com.siged.editor.CidadeEditor;
import br.com.siged.editor.FormacaoAcademicaEditor;
import br.com.siged.editor.MunicipioEditor;
import br.com.siged.editor.NivelEscolaridadeEditor;
import br.com.siged.editor.PaisEditor;
import br.com.siged.editor.SetorEditor;
import br.com.siged.editor.SituacaoInstrutorEditor;
import br.com.siged.editor.TipoInstrutorEditor;
import br.com.siged.editor.UfEditor;
import br.com.siged.editor.UfMunicipioEditor;
import br.com.siged.entidades.FormacaoAcademica;
import br.com.siged.entidades.Instrutor;
import br.com.siged.entidades.NivelEscolaridade;
import br.com.siged.entidades.Pais;
import br.com.siged.entidades.Participante;
import br.com.siged.entidades.SituacaoInstrutor;
import br.com.siged.entidades.TipoInstrutor;
import br.com.siged.entidades.externas.Setor;
import br.com.siged.entidades.externas.UfMunicipio;
import br.com.siged.filtro.InstrutorFiltro;
import br.com.siged.service.InstrutorService;
import br.com.siged.service.exception.NaoPodeEnviarEmailException;

@Controller
@RequestMapping("/instrutor/**")
public class InstrutorController {
	
	@Autowired
	private InstrutorDAO instrutorDao;
	@Autowired
	private PaisDAO paisDao;
	@Autowired
	private CidadeDAO cidadeDao;
	@Autowired
	private UfDAO ufDao;
	@Autowired
	private NivelEscolaridadeDAO nivelEscolaridadeDao;
	@Autowired
	private FormacaoAcademicaDAO formacaoAcademicaDao;
	@Autowired
	private TipoInstrutorDAO tipoInstrutorDao;
	@Autowired
	private SetorDAO setorDao;
	@Autowired
	private SituacaoInstrutorDAO situacaoInstrutorDao;
	@Autowired
	private ParticipanteDAO participanteDao;
	@Autowired
	private MunicipioDAO municipioDao;
	@Autowired
	private UfMunicipioDAO ufMunicipioDao;			
	
	@Autowired
	private InstrutorService instrutorService;
	
	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(br.com.siged.entidades.Pais.class, new PaisEditor(paisDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Cidade.class, new CidadeEditor(cidadeDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.Uf.class, new UfEditor(ufDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.NivelEscolaridade.class, new NivelEscolaridadeEditor(nivelEscolaridadeDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.FormacaoAcademica.class, new FormacaoAcademicaEditor(formacaoAcademicaDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.TipoInstrutor.class, new TipoInstrutorEditor(tipoInstrutorDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Setor.class, new SetorEditor(setorDao));
        dataBinder.registerCustomEditor(br.com.siged.entidades.SituacaoInstrutor.class, new SituacaoInstrutorEditor(situacaoInstrutorDao));
        dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
        dataBinder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        dataBinder.registerCustomEditor(br.com.siged.entidades.externas.UfMunicipio.class, new UfMunicipioEditor(ufMunicipioDao));
		dataBinder.registerCustomEditor(br.com.siged.entidades.externas.Municipio.class, new MunicipioEditor(municipioDao));
    }
	

	@RequestMapping(value = "/instrutor/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, ModelMap modelMap) {
		Instrutor instrutor = instrutorDao.findById(id);
		
		if (instrutor.getSetorId() == null) {
			Setor setor = new Setor();
			setor.setDescricao("não se Aplica");
			instrutor.setSetorId(setor);
		}
		
		modelMap.addAttribute("instrutor", instrutor);
		return "instrutor/show";
	}	

	@RequestMapping(value = "/instrutor", method = RequestMethod.GET)
	public String list(ModelMap modelMap, HttpServletRequest request) {
		InstrutorFiltro instrutorfiltro = new InstrutorFiltro();
		DisplayTagPageable pageable = new DisplayTagPageable(request);
		PaginatedList displayTagList = instrutorDao.filtrar(instrutorfiltro, pageable);
		
		modelMap.addAttribute("instrutorfiltro", instrutorfiltro);
		modelMap.addAttribute("instrutores", displayTagList);
		
		return "instrutor/list";
	}	
	
	@RequestMapping(value = "/instrutor/search", method = RequestMethod.GET)
	public String search(InstrutorFiltro instrutorfiltro, ModelMap modelMap, ServletRequest request, ServletResponse Response) throws IOException {
		DisplayTagPageable pageable = new DisplayTagPageable(request);
		PaginatedList displayTagList = instrutorDao.filtrar(instrutorfiltro, pageable);
		
		
		modelMap.addAttribute("instrutorfiltro", instrutorfiltro);
		modelMap.addAttribute("instrutores", displayTagList);
		return "instrutor/list";
	}
	

	@RequestMapping(value = "/instrutor/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, ModelMap model) {
		try {
			instrutorDao.remove(instrutorDao.find(id));
			model.addAttribute("mensagem","Excluído com sucesso!");
		} catch (Exception e) {
			model.addAttribute("mensagem","O registro não pode ser excluído pois está em uso.");
		}
		
		return "redirect:/instrutor";
	}
	
	@RequestMapping(value = "/instrutor/{id}/assinatura", method = RequestMethod.DELETE)
	public String deleteAssinatura(@PathVariable("id") Long id, ModelMap model) {
		try {
			
			Instrutor instrutor = instrutorDao.find(id);
			instrutor.setAssinatura(new byte[0]);
			instrutor.setAssinaturaNome("");
			instrutor.setAssinaturaTipo("");
			instrutorDao.persist(instrutor);
			model.addAttribute("mensagem","Excluído com sucesso!");
		} catch (Exception e) {
			model.addAttribute("mensagem","O registro não pode ser excluído pois está em uso.");
		}
		
		return "redirect:/instrutor/" + id + "/form";
	}
	
	@RequestMapping(value = "/instrutor/{id}/curriculo", method = RequestMethod.DELETE)
	public String deleteCurriculo(@PathVariable("id") Long id, ModelMap model) {
		try {
			
			Instrutor instrutor = instrutorDao.find(id);

			
			instrutor.setCurriculo(new byte[0]);
			instrutor.setCurriculoNome("");
			instrutor.setCurriculoTipo("");
			instrutorDao.persist(instrutor);
			model.addAttribute("mensagem","Excluído com sucesso!");
		} catch (Exception e) {
			model.addAttribute("mensagem","O registro não pode ser excluído pois está em uso.");
		}
		
		return "redirect:/instrutor/" + id + "/form";
	}

	@RequestMapping(value = "/instrutor/form", method = RequestMethod.GET)
	public String form(ModelMap modelMap, HttpServletRequest request) {
		modelMap.addAttribute("instrutor", instrutorService.associarAoUsuarioLogado());
		return "instrutor/create";
	}
	
	@RequestMapping(value = "/instrutor", method = RequestMethod.POST)
	public String create(@ModelAttribute("instrutor") @Valid Instrutor instrutor, BindingResult result, @RequestParam("curriculo") MultipartFile curriculo, @RequestParam("assinatura") MultipartFile assinatura, ModelMap model, HttpServletRequest request) throws IOException, Exception {
						
		if(request.isUserInRole("ROLE_ADMINISTRADOR") && instrutor.getTipoId() == null) {
			result.addError(new FieldError("instrutor", "tipoId", "Campo Obrigatório"));
		}else{
			if(request.isUserInRole("ROLE_SERVIDOR") && instrutor.getTipoId() == null) {
				instrutor.setTipoId( new TipoInstrutor(81L));
			}else if(!request.isUserInRole("ROLE_SERVIDOR") && instrutor.getTipoId() == null) {
				instrutor.setTipoId( new TipoInstrutor(82L));
			}
		}
		if (curriculo.getSize() != 0L && !(curriculo.getOriginalFilename().contains(".pdf") ||  curriculo.getOriginalFilename().contains(".jpg"))) { 
			result.addError(new FieldError("instrutor", "curriculo", "Formato de arquivo invalido"));
		}
		
		if (assinatura.getSize() != 0L && !(assinatura.getOriginalFilename().contains(".pdf") ||  assinatura.getOriginalFilename().contains(".jpg"))) {
			result.addError(new FieldError("instrutor", "assinatura", "Formato de arquivo invalido"));
		}

		
		if(!request.isUserInRole("ROLE_ADMINISTRADOR") && instrutor.getCpf().equals("")) {
			result.addError(new FieldError("instrutor", "cpf", "Campo Obrigatório"));
		}
		/*if (instrutor.getUfMunicipio() == null) {
			result.addError(new FieldError("instrutor", "ufMunicipio", "Campo Obrigatório"));
		}
		if (instrutor.getMunicipio() == null) {
			result.addError(new FieldError("instrutor", "municipio", "Campo Obrigatório"));
		}*/
		if(instrutor.getPaisId() != null && instrutor.getPaisId().getId() == 1 ) {
			
			if(instrutor.getUfMunicipio() == null || instrutor.getUfMunicipio().getUf().equals("0")) {
				result.addError(new FieldError("instrutor", "ufMunicipio", "Campo Obrigatório"));
			}
			if(instrutor.getMunicipio() == null || instrutor.getMunicipio().getId().equals("0")) {
				result.addError(new FieldError("instrutor", "municipio", "Campo Obrigatório"));
			}
		}
		
		if (result.hasErrors()) {
			return "instrutor/create";
        }
		
		try {
			instrutorService.salvar(instrutor, curriculo, null);
			model.addAttribute("mensagem","Incluído com sucesso!");
		} catch (NaoPodeEnviarEmailException e) {
			model.addAttribute("mensagem","Incluído com sucesso!" + " " + e.getMessage());
		}
		
		if (request.isUserInRole("ROLE_ADMINISTRADOR")) {
			return "redirect:/instrutor";
		} else {
			return "redirect:/evento/previstos";
		}
	}

	@RequestMapping(value = "/instrutor/{id}/form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
		Instrutor instrutor = instrutorDao.find(id);
		
		if (instrutor.getSetorId() == null){
			Setor setor= new Setor();
			setor.setId(new Long(9999));
			instrutor.setSetorId(setor);
		}
		if(instrutor.getPaisId().getId() == 1) {
			try {
				
		instrutor.setUfMunicipio(ufMunicipioDao.findByUf(instrutor.getMunicipio().getUfMunicipio()));	
			} catch(Exception  e) {
				System.out.println(e);
			}
		}
		
		modelMap.addAttribute("instrutor", instrutor);	

		return "instrutor/update";
	}

	@RequestMapping(value = "/instrutor/{id}/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("instrutor") @Valid Instrutor instrutor, BindingResult result, @RequestParam("curriculo") MultipartFile curriculo, 
			@RequestParam("assinatura") MultipartFile assinatura, ModelMap model, HttpServletRequest request) throws IOException, Exception {
		
		if (instrutor.getSituacaoId().getId() == 3 && instrutor.getJustificativa().equals("")) {			
			result.addError(new FieldError("instrutor", "justificativa", "Campo Obrigatório"));
			return "instrutor/update";
		}

		
		if (curriculo.getSize() != 0L && !(curriculo.getOriginalFilename().contains(".pdf") ||  curriculo.getOriginalFilename().contains(".jpg"))) {
			result.addError(new FieldError("instrutor", "curriculo", "Formato de arquivo invalido"));
			return "instrutor/update";

		}

		if (assinatura.getSize() != 0L && !(assinatura.getOriginalFilename().contains(".pdf") ||  assinatura.getOriginalFilename().contains(".jpg"))) {
			result.addError(new FieldError("instrutor", "assinatura", "Formato de arquivo invalido"));
			return "instrutor/update";

		}
		if (instrutor.getNome().equals("")) {
			result.addError(new FieldError("instrutor", "nome", ""));
			return "instrutor/update";
		}
		if (instrutor.getTelefone().equals("")) {
			result.addError(new FieldError("instrutor", "telefone", ""));
			return "instrutor/update";
		}
		if (instrutor.getEmail().equals("")) {
			result.addError(new FieldError("instrutor", "email", ""));
			return "instrutor/update";
		}
		
		try {
			instrutorService.salvar(instrutor, curriculo, assinatura);
			model.addAttribute("mensagem","Alterado com sucesso!");
		} catch (NaoPodeEnviarEmailException e) {
			model.addAttribute("mensagem","Alterado com sucesso!" + " " + e.getMessage());
		}
		
		return "redirect:/instrutor";
	}	
	
    @RequestMapping("/instrutor/curriculo/{id}")
    public String download(@PathVariable("id")
            Long instrutorId, HttpServletResponse response) {
 
        Instrutor instrutor = instrutorDao.findById(instrutorId);
        try {
            response.setHeader("Content-Disposition", "inline;filename=\"" +instrutor.getCurriculoNome()+ "\"");
            OutputStream out = response.getOutputStream();
            response.setContentType(instrutor.getCurriculoTipo());
            IOUtils.copy(new ByteArrayInputStream(instrutor.getCurriculo()), out);
            out.flush();
            out.close();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return null;
    }    
    
    @RequestMapping("/instrutor/projeto/{id}")
    public String download2(@PathVariable("id")
            Long instrutorId, HttpServletResponse response) {
 
    	Instrutor instrutor = instrutorDao.findById(instrutorId);
        try {
            response.setHeader("Content-Disposition", "inline;filename=\"" +instrutor.getProjetoNome()+ "\"");
            OutputStream out = response.getOutputStream();
            response.setContentType(instrutor.getProjetoTipo());
            IOUtils.copy(new ByteArrayInputStream(instrutor.getProjeto()), out);
            out.flush();
            out.close();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return null;
    }

    @RequestMapping("/instrutor/assinatura/{id}")
    public String download3(@PathVariable("id")
            Long instrutorId, HttpServletResponse response) {
 
    	Instrutor instrutor = instrutorDao.findById(instrutorId);
        try {
            response.setHeader("Content-Disposition", "inline;filename=\"" +instrutor.getAssinaturaNome()+ "\"");
            OutputStream out = response.getOutputStream();
            response.setContentType(instrutor.getAssinaturaTipo());
            IOUtils.copy(new ByteArrayInputStream(instrutor.getAssinatura()), out);
            out.flush();
            out.close();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return null;
    }		
		
	@RequestMapping(value = "/instrutor/procuraInstrutor", method = RequestMethod.GET)
    @ResponseBody
    public HashMap<String, String> procuraInstrutor(@RequestParam(value="cpf") String cpf) {
    	
    	Instrutor instrutor = instrutorDao.findByCpf(cpf);
   
    	HashMap<String, String> instrutorRetorno = new HashMap<String, String>();
    	
    	if (instrutor != null) {
    		instrutorRetorno.put("situacao", String.valueOf(instrutor.getSituacaoId().getId()));
    		return instrutorRetorno;   	
    	} 
		return null;
    }	
	
	@RequestMapping(value = "/instrutor/bloqueiaCampos", method = RequestMethod.GET)
    @ResponseBody
    public HashMap<String, String> bloqueiaCampos(@RequestParam(value="cpf") String cpf) {    	
    	String cpfAlterado = cpf.replace(".", "").replace("-", "");
    	Participante participante = participanteDao.findByCpf(cpfAlterado);
   
    	HashMap<String, String> participanteRetorno = new HashMap<String, String>();
    	
    	if (participante != null) {
    		participanteRetorno.put("cpf", cpf);
    		participanteRetorno.put("nome", participante.getNome());
    		participanteRetorno.put("email", participante.getEmail());
    		participanteRetorno.put("telefone", participante.getTelefone());
    		if (participante.getEscolaridadeId() != null) {
    			participanteRetorno.put("escolaridade",String.valueOf(participante.getEscolaridadeId().getId()));
    		}
    		if (participante.getFormacaoId() != null) {
    			participanteRetorno.put("formacao", String.valueOf(participante.getFormacaoId().getId()));
    		}	    		
    		if (participante.getMunicipio() != null) {
    			participanteRetorno.put("municipio", String.valueOf(participante.getMunicipio().getId()));
	    		participanteRetorno.put("uf", ufMunicipioDao.findByUf(participante.getMunicipio().getUfMunicipio()).getUf());
    		}
    		if (participante.getSetorId() != null) {
    			participanteRetorno.put("setorId",String.valueOf(participante.getSetorId().getId()));
    		}
    		return participanteRetorno;   	
    	} 
		return null;
    }
	
	
	
	@ModelAttribute("nivelEscolaridadeList")
	public Collection<NivelEscolaridade> populateNivelEscolaridades() {
		return nivelEscolaridadeDao.findAll();
	}
	
	@ModelAttribute("formacaoAcademicaList")
	public Collection<FormacaoAcademica> populateFormacaoAcademicas() {
		return formacaoAcademicaDao.findAll();
	}
	
	@ModelAttribute("tipoInstrutorList")
	public Collection<TipoInstrutor> populateTipoInstrutors() {
		return tipoInstrutorDao.findAll();
	}
	
	@ModelAttribute("setorList")
	public Collection<Setor> populateSetors() {
		return setorDao.findAll();
	}
	
	@ModelAttribute("situacaoInstrutorList")
	public Collection<SituacaoInstrutor> populateSituacaoInstrutors() {
		return situacaoInstrutorDao.findAll();
	}
	
	@ModelAttribute("sexoList")
	public Map<String,String> populateSexos() {
		
		Map<String,String> populate = new LinkedHashMap<String, String>();
		populate.put("M", "M");
		populate.put("F", "F");
		return populate;
		
	}
	
	@ModelAttribute("listaPais")
	public Collection<Pais> populatePaises() {
		return paisDao.findAll();
    }
	
	@ModelAttribute("listaUf")
    public Collection<UfMunicipio> populateUfs() {
		return ufMunicipioDao.findAll();
	}	

		
	@ModelAttribute("ufMunicipioList")
	public Collection<UfMunicipio> populateUfMunicipio() {
		return ufMunicipioDao.findAll();
	}    
	
}