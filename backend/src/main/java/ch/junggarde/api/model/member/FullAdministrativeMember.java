package ch.junggarde.api.model.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Accessors(chain = true)
@Getter
@Setter
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
public class FullAdministrativeMember {
    private UUID id;
    private String firstname;
    private String lastname;
    private Function function;
    private Role role;
    private String jobTitle;
    private String description;
    private String base64;
    private UUID supervisorId;
    private List<FullAdministrativeMember> subordinates = new ArrayList<>();

    public FullAdministrativeMember(UUID id, String firstname, String lastname, Function function, Role role, String jobTitle, String description, String base64, UUID supervisorId) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.function = function;
        this.role = role;
        this.jobTitle = jobTitle;
        this.description = description;
        this.base64 = base64;
        this.supervisorId = supervisorId;
    }

}
