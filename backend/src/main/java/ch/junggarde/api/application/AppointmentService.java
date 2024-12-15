package ch.junggarde.api.application;

import ch.junggarde.api.adapter.out.persistance.AppointmentRepository;
import ch.junggarde.api.application.dto.AppointmentRequestDTO;
import ch.junggarde.api.application.dto.AppointmentResponseDTO;
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

    public List<AppointmentResponseDTO> getPublicAppointments() {
        return appointmentRepository.findAllPublic().stream().map(AppointmentResponseDTO::fromDomainModel).toList();
    }

    public List<AppointmentResponseDTO> getAppointments() {
        return appointmentRepository.findAll().stream().map(AppointmentResponseDTO::fromDomainModel).toList();
    }

    public AppointmentResponseDTO saveAppointment(AppointmentRequestDTO appointmentRequestDTO) {
        Appointment appointment = new Appointment(
                UUID.randomUUID(),
                LocalDateTime.parse(appointmentRequestDTO.start()),
                LocalDateTime.parse(appointmentRequestDTO.end()),
                appointmentRequestDTO.location(),
                appointmentRequestDTO.name(),
                AppointmentType.valueOf(appointmentRequestDTO.type()),
                appointmentRequestDTO.published()
        );
        appointmentRepository.saveAppointment(appointment);
        return AppointmentResponseDTO.fromDomainModel(appointment);
    }
}