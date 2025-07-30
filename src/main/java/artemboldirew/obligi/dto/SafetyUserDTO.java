package artemboldirew.obligi.dto;

import artemboldirew.obligi.entities.Role;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SafetyUserDTO {
    private Long id;

    @Email
    private String email;
    private Role role;
    private LocalDateTime createdAt;

}
