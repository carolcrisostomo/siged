package br.com.siged.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.siged.dao.MetaDesempenhoProdutividadeDAO;
import br.com.siged.entidades.MetaDesempenhoProdutividade;

@Service
public class MetaDesempenhoProdutividadeService {

	@Autowired
	private MetaDesempenhoProdutividadeDAO metaDesempenhoProdutividadeDao;
	
	@Transactional
	public void salvar(MetaDesempenhoProdutividade meta) {
		if (meta.getId() != null) {
			atualizar(meta);
		} else {
			metaDesempenhoProdutividadeDao.persist(meta);
		}
	}
	
	@Transactional
	public void excluir(MetaDesempenhoProdutividade meta) {
		metaDesempenhoProdutividadeDao.remove(meta);
	}
	
	private void atualizar(MetaDesempenhoProdutividade meta) {
		MetaDesempenhoProdutividade metaDoBanco = metaDesempenhoProdutividadeDao.find(meta.getId());
		if (metaDoBanco != null) {
			metaDoBanco.setIndiceCapacitacao(meta.getIndiceCapacitacao());
			metaDoBanco.setIndiceAvaliacaoReacao(meta.getIndiceAvaliacaoReacao());
			metaDesempenhoProdutividadeDao.merge(metaDoBanco);
		}
	}
	
}
