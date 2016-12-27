package com.template.project.validators;

import com.mobsandgeeks.saripaar.annotation.ValidateUsing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ValidateUsing(EmailPhoneValidationRule.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CustomEmailPhoneValidator {
 
    public int messageResId()   default -1;                     // Mandatory attribute
    // public String message()     default ICsts.WRONG_PHONE_NUMBER;   // Mandatory attribute
    public String message()     default "";   // Mandatory attribute
    public int sequence()       default -1;                     // Mandatory attribute
}