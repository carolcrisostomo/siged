package br.com.siged.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.dao.MetaPlanejamentoEstrategicoDAO;
import br.com.siged.entidades.MetaPlanejamentoEstrategico;

@Service
public class MetaPlanejamentoEstrategicoService {

	@Autowired
	private MetaPlanejamentoEstrategicoDAO metaPlanejamentoEstrategicoDao;
	
	@Transactional
	public void salvar(MetaPlanejamentoEstrategico meta) {
		if (meta.getId() != null) {
			atualizar(meta);
		} else {
			metaPlanejamentoEstrategicoDao.persist(meta);
		}
	}
	
	@Transactional
	public void excluir(MetaPlanejamentoEstrategico meta) {
		metaPlanejamentoEstrategicoDao.remove(meta);
	}
	
	private void atualizar(MetaPlanejamentoEstrategico meta) {
		MetaPlanejamentoEstrategico metaDoBanco = metaPlanejamentoEstrategicoDao.find(meta.getId());
		if (metaDoBanco != null) {
			metaDoBanco.setQuantidadeAcoes(meta.getQuantidadeAcoes());
			metaDoBanco.setPercentualJurisdicionadosCapacitados(meta.getPercentualJurisdicionadosCapacitados());
			metaDoBanco.setPercentualServidoresCapacitados(meta.getPercentualServidoresCapacitados());
			metaDoBanco.setPercentualAcoesDoPlano(meta.getPercentualAcoesDoPlano());
			
			metaPlanejamentoEstrategicoDao.merge(metaDoBanco);
		}
	}
}
