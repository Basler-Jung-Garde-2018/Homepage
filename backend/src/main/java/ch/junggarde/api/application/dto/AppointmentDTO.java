package ch.junggarde.api.application.dto;

import ch.junggarde.api.model.Appointment;
import jakarta.annotation.Nullable;

public record AppointmentDTO(
        @Nullable
        String id,
        String start,
        String end,
        String location,
        String name,
        String type,
        boolean published
) {
    public static AppointmentDTO fromDomainModel(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId().toString(),
                appointment.getStart().toString(),
                appointment.getEnd().toString(),
                appointment.getLocation(),
                appointment.getName(),
                appointment.getType().toString(),
                appointment.isPublished()
        );
    }
}