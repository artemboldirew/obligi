package artemboldirew.obligi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenPairDTO {
    private String accessToken;
    private String refreshToken;
}
