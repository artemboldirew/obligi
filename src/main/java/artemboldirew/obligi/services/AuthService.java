package artemboldirew.obligi.services;

import artemboldirew.obligi.dto.LoginDTO;
import artemboldirew.obligi.dto.TokenPairDTO;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public TokenPairDTO login(LoginDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        TokenPairDTO tokens = new TokenPairDTO("access", "refresh");
        return tokens;
    }
}
