package ch.junggarde.api.application;

import ch.junggarde.api.adapter.out.persistance.AppointmentRepository;
import ch.junggarde.api.application.dto.AppointmentDTO;
import ch.junggarde.api.model.Appointment;
import ch.junggarde.api.model.AppointmentType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Slf4j
public class AppointmentService {
    @Inject
    AppointmentRepository appointmentRepository;

    public List<AppointmentDTO> getPublicAppointments() {
        return appointmentRepository.findAllPublic().stream().map(AppointmentDTO::fromDomainModel).toList();
    }

    public List<AppointmentDTO> getAppointments() {
        return appointmentRepository.findAll().stream().map(AppointmentDTO::fromDomainModel).toList();
    }

    public AppointmentDTO saveAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment(
                UUID.randomUUID(),
                LocalDateTime.parse(appointmentDTO.start()),
                LocalDateTime.parse(appointmentDTO.end()),
                appointmentDTO.location(),
                appointmentDTO.name(),
                AppointmentType.valueOf(appointmentDTO.type()),
                appointmentDTO.published()
        );
        appointmentRepository.saveAppointment(appointment);
        return AppointmentDTO.fromDomainModel(appointment);
    }
}