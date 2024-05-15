package org.careconnect.careconnectbooking.repo;

import org.careconnect.careconnectcommon.entity.BookingAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingAppointmentRepository extends JpaRepository<BookingAppointment,Long> {

    List<BookingAppointment> findByDoctorId(long doctorId);

    BookingAppointment findByPatientIdAndDoctorIdAndAppointmentDate(long patientId, long doctorId, LocalDate date);
}
