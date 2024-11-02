package ch.junggarde.api.model.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

@Accessors(chain = true)
@Getter
@Setter
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
public class AdministrativeMember {
    private UUID id;
    private UUID memberId;
    private Role role;
    private String jobTitle;
    private String description;
    private UUID imageId;
    private UUID supervisorId;

    public AdministrativeMember(UUID memberId, Role role, String jobTitle, String description, UUID imageId, String supervisorId) {
        this.id = UUID.randomUUID();
        this.memberId = memberId;
        this.role = role;
        this.jobTitle = jobTitle;
        this.description = description;
        this.imageId = imageId;
        if (supervisorId != null) {
            this.supervisorId = UUID.fromString(supervisorId);
        }
    }
}
