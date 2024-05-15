package org.careconnect.careconnectbooking.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeRangeValidator.class)
public @interface ValidAppointmentTime {

    String message() default "Appointment time must be between 10:00 AM and 6:00 PM";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
