package org.careconnect.careconnectbooking.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

public class TimeRangeValidator implements ConstraintValidator<ValidAppointmentTime, LocalTime> {

    @Override
    public void initialize(ValidAppointmentTime constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(18, 0);

        return !value.isBefore(startTime) && !value.isAfter(endTime);
    }
}
