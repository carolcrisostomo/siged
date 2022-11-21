package br.com.siged.controller.validators;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

/**
 * Adiciona validações extras às já usadas nas constraints do BeanValidation (JSR-303)
 * 
 * @since 25/09/2017
 * @author Rafael de Castro
 */
public abstract class AbstractValidator implements org.springframework.validation.Validator {

	@Autowired
	private Validator validator;

	@Override
	public boolean supports(Class<?> clazz) {
		return suporta(clazz);
	}

	/**
	 * Valida as contraints e adiciona as validações extras
	 * 
	 * @param target Class que terá as propriedades validadas
	 * @param errors Para armazenar as propriedades que violaram as regras de validação
	 */
	@Override
	public void validate(Object target, Errors errors) {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target);
		for(ConstraintViolation<Object> constraintViolation : constraintViolations) {
			String propertyPath = constraintViolation.getPropertyPath().toString();
			String message = constraintViolation.getMessage();
			errors.rejectValue(propertyPath, "", message);
		}
		adicionarValidacoesExtras(target, errors);
	}
	
	protected abstract boolean suporta(Class<?> clazz);
	
	protected abstract void adicionarValidacoesExtras(Object target, Errors errors);

}
