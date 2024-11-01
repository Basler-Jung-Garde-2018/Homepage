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
}
