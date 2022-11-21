package br.com.siged.controller.validators;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import br.com.siged.dao.MetaDesempenhoProdutividadeDAO;
import br.com.siged.entidades.MetaDesempenhoProdutividade;

@Component(value = "metaDesempenhoProdutividadeValidator")
public class MetaDesempenhoProdutividadeValidator extends AbstractValidator {

	@Autowired
	private MetaDesempenhoProdutividadeDAO metaDesempenhoProdutividadeDao;
	
	@Override
	protected boolean suporta(Class<?> clazz) {
		return MetaDesempenhoProdutividade.class.equals(clazz);
	}
	
	@Override
	protected void adicionarValidacoesExtras(Object target, Errors errors) {
		MetaDesempenhoProdutividade meta = (MetaDesempenhoProdutividade) target;
		
		validaSeMetasMaiorQue100(meta, errors);
		validaSeMetaJaExiste(meta, errors);

	}

	private void validaSeMetasMaiorQue100(MetaDesempenhoProdutividade meta, Errors errors) {
		
		if (meta.getIndiceCapacitacao() != null && meta.getIndiceCapacitacao().compareTo(new BigDecimal(100)) > 0) {
			errors.rejectValue("indiceCapacitacao", "meta.desempenhoProdutividade.validator.metaMaiorQue100");
		}
		
		if (meta.getIndiceAvaliacaoReacao() != null && meta.getIndiceAvaliacaoReacao().compareTo(new BigDecimal(100)) > 0) {
			errors.rejectValue("avaliacaoReacao", "meta.desempenhoProdutividade.validator.metaMaiorQue100");
		}
		
	}

	private void validaSeMetaJaExiste(MetaDesempenhoProdutividade meta, Errors errors) {
		MetaDesempenhoProdutividade metaExistente = metaDesempenhoProdutividadeDao.findByAnoAndSemestre(meta.getAno(), meta.getSemestre());
		
		if (meta.getId() == null && metaExistente != null) {
			errors.rejectValue("ano", "meta.desempenhoProdutividade.validator.metajaincluida");
			errors.rejectValue("semestre", "meta.desempenhoProdutividade.validator.metajaincluida");
		}
		
	}
}
