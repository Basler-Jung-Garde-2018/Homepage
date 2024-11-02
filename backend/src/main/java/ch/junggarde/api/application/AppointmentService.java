package ch.junggarde.api.application;

import ch.junggarde.api.adapter.out.persistance.AppointmentRepository;
import ch.junggarde.api.application.dto.AppointmentDTO;
import ch.junggarde.api.model.Appointment;
import ch.junggarde.api.model.AppointmentType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Log4j2
public class AppointmentService {
    @Inject
    AppointmentRepository appointmentRepository;

    public List<AppointmentDTO> getAppointments() {
        return appointmentRepository.findAll().stream().map(AppointmentDTO::fromDomainModel).toList();
    }

    public List<AppointmentDTO> saveAppointments(List<AppointmentDTO> appointmentDTOS) {
        if (appointmentDTOS.isEmpty()) {
            return new ArrayList<>();
        }

        List<Appointment> appointmentList = appointmentDTOS.stream().map(
                appointmentDTO -> new Appointment(
                        LocalDateTime.parse(appointmentDTO.date()),
                        appointmentDTO.location(), appointmentDTO.name(),
                        AppointmentType.valueOf(appointmentDTO.type()))
        ).toList();
        appointmentRepository.saveAppointments(appointmentList);
        return appointmentList.stream().map(AppointmentDTO::fromDomainModel).toList();
    }
}