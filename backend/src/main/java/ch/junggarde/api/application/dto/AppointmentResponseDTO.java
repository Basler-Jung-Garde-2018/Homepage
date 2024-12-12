package ch.junggarde.api.application.dto;

import ch.junggarde.api.model.Appointment;

public record AppointmentResponseDTO(
        String id,
        String start,
        String end,
        String location,
        String name,
        String type
) {
    public static AppointmentResponseDTO fromDomainModel(Appointment appointment) {
        return new AppointmentResponseDTO(
                appointment.getId().toString(),
                appointment.getStart().toString(),
                appointment.getEnd().toString(),
                appointment.getLocation(),
                appointment.getName(),
                appointment.getType().toString()
        );
    }
}