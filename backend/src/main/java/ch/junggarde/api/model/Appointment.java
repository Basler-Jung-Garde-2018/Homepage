package ch.junggarde.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.UUID;

@Accessors(chain = true)
@Getter
@Setter
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    private UUID id;
    private LocalDateTime date;
    private String location;
    private String name;
    private AppointmentType type;

    public Appointment(LocalDateTime date, String location, String name, AppointmentType type) {
        this.id = UUID.randomUUID();
        this.date = date;
        this.location = location;
        this.name = name;
        this.type = type;
    }
}