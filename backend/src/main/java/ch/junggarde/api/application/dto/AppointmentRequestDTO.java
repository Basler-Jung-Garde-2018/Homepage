package ch.junggarde.api.application.dto;

public record AppointmentRequestDTO(
        String start,
        String end,
        String location,
        String name,
        String type,
        boolean published
) {
}