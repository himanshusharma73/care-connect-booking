package org.careconnect.careconnectbooking.util;

import org.careconnect.careconnectbooking.service.DoctorService;
import org.careconnect.careconnectbooking.service.PatientService;
import org.careconnect.careconnectcommon.entity.BookingAppointment;
import org.careconnect.careconnectcommon.response.BookingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CopyProperties {

    @Autowired
    PatientService patientService;

    @Autowired
    DoctorService doctorService;

    public BookingResponse copyPropertiesToBooking(BookingAppointment bookingAppointment){
        BookingResponse bookingResponse=new BookingResponse();
        bookingResponse.setPatient(patientService.getPatientById(bookingAppointment.getPatientId()));
        bookingResponse.setStatus(bookingAppointment.getStatus());
        bookingResponse.setAppointmentDate(bookingAppointment.getAppointmentDate());
        bookingResponse.setAppointmentId(bookingAppointment.getAppointmentId());
        bookingResponse.setAppointmentStartTime(bookingAppointment.getAppointmentStartTime());
        bookingResponse.setAppointmentEndTime(bookingAppointment.getAppointmentEndTime());
        bookingResponse.setDoctor(doctorService.getDoctorById(bookingAppointment.getDoctorId()));
        bookingResponse.getPatient().setPatientIllness(patientService.getPatientIllness(bookingAppointment.getPatientId()));
        return bookingResponse;
    };
}
