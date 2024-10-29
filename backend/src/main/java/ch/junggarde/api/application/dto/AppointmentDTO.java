package ch.junggarde.api.application.dto;

import ch.junggarde.api.model.Appointment;

public record AppointmentDTO(
        String id,
        String date,
        String location,
        String name,
        String type
) {
    public static AppointmentDTO fromDomainModel(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId().toString(),
                appointment.getDate().toString(),
                appointment.getLocation(),
                appointment.getName(),
                appointment.getType().toString()
        );
    }
}