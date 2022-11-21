package br.com.siged.controller.validators;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import br.com.siged.dao.MetaPlanejamentoEstrategicoDAO;
import br.com.siged.entidades.MetaPlanejamentoEstrategico;

@Component(value = "metaPlanejamentoEstrategicoValidator")
public class MetaPlanejamentoEstrategicoValidator extends AbstractValidator {

	@Autowired
	private MetaPlanejamentoEstrategicoDAO metaPlanejamentoEstrategicoDAO;
	
	@Override
	protected boolean suporta(Class<?> clazz) {
		return MetaPlanejamentoEstrategico.class.equals(clazz);
	}
	
	@Override
	protected void adicionarValidacoesExtras(Object target, Errors errors) {
		MetaPlanejamentoEstrategico meta = (MetaPlanejamentoEstrategico) target;
		
		validaSeMetasMaiorQue100(meta, errors);
		validaSeMetaJaExiste(meta, errors);

	}

	private void validaSeMetasMaiorQue100(MetaPlanejamentoEstrategico meta, Errors errors) {
		
		if (meta.getPercentualAcoesDoPlano() != null && meta.getPercentualAcoesDoPlano().compareTo(new BigDecimal(100)) > 0) {
			errors.rejectValue("percentualAcoesDoPlano", "meta.planejamentoEstrategico.validator.metaMaiorQue100");
		}
		
		if (meta.getPercentualJurisdicionadosCapacitados() != null && meta.getPercentualJurisdicionadosCapacitados().compareTo(new BigDecimal(100)) > 0) {
			errors.rejectValue("percentualJurisdicionadosCapacitados", "meta.desempenhoProdutividade.validator.metaMaiorQue100");
		}
		
		if (meta.getPercentualServidoresCapacitados() != null && meta.getPercentualServidoresCapacitados().compareTo(new BigDecimal(100)) > 0) {
			errors.rejectValue("percentualServidoresCapacitados", "meta.desempenhoProdutividade.validator.metaMaiorQue100");
		}
		
	}

	private void validaSeMetaJaExiste(MetaPlanejamentoEstrategico indicador, Errors errors) {
		MetaPlanejamentoEstrategico indicadorExistente = metaPlanejamentoEstrategicoDAO.findByAno(indicador.getAno());
		
		if (indicador.getId() == null && indicadorExistente != null) {
			errors.rejectValue("ano", "meta.planejamentoEstrategico.validator.metajaincluida");
		}
		
	}
}
