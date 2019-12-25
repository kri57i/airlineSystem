package com.kristi.account.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object>{

	private String firstFieldName;
	private String secondFieldName;
	
	//Initialization
	@Override
	public void initialize(final FieldMatch constraintAnnotation) {
		firstFieldName = constraintAnnotation.first();
		secondFieldName = constraintAnnotation.second();
	}
	
	/*
	 * Overriding the isValid() method that returns a boolean value whether the fields are valid or not
	 */
	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		try {
		final Object firstObject = BeanUtils.getProperty(value, firstFieldName);
		final Object secondObject = BeanUtils.getProperty(value, secondFieldName);
		return firstObject == null 
				&& secondObject == null || firstObject != null 
				&& firstObject.equals(secondObject);
		}
		catch(final Exception ignoreException) {}
		//validation successful
		return true;
	}
	
}
