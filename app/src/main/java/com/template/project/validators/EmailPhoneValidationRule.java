package com.template.project.validators;

import com.mobsandgeeks.saripaar.AnnotationRule;

import org.apache.commons.lang3.math.NumberUtils;

import commons.validator.routines.EmailValidator;

public class EmailPhoneValidationRule extends AnnotationRule<CustomEmailPhoneValidator, String> {
 
    protected EmailPhoneValidationRule(CustomEmailPhoneValidator haggle) {
        super(haggle);
    }
 
    @Override
    public boolean isValid(String emailPhone) {
 
        boolean isValid = false;

        if (emailPhone.length() <=0){
            return true;
        }

        if (EmailValidator.getInstance().isValid(emailPhone)){
            isValid = true;
        }
        if (NumberUtils.isDigits(emailPhone)){
            isValid = true;
        }
        return isValid;
    }
}