package ch.junggarde.api.application;

import ch.junggarde.api.adapter.out.AppointmentRepository;
import ch.junggarde.api.application.dto.AppointmentDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class AppointmentService {
    @Inject
    AppointmentRepository appointmentRepository;

    public List<AppointmentDTO> getAppointments() {
       return appointmentRepository.findAll().stream().map(AppointmentDTO::fromDomainModel).toList();
    }
}
