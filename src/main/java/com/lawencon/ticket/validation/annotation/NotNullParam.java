package com.lawencon.ticket.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotNull;

@NotNull()
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullParam {
    String message() default "cannot be null";

    String fieldName() default "";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}

