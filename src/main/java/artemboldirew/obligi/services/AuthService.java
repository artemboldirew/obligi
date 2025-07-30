package artemboldirew.obligi.services;

import artemboldirew.obligi.dto.AccessTokenDTO;
import artemboldirew.obligi.dto.LoginDTO;
import artemboldirew.obligi.dto.RefreshTokenDTO;
import artemboldirew.obligi.dto.TokenPairDTO;
import artemboldirew.obligi.entities.User;
import artemboldirew.obligi.repositories.AuthRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final JwtService jwtService;

    public AuthService(AuthRepository authRepository, JwtService jwtService) {
        this.jwtService = jwtService;
        this.authRepository = authRepository;
    }

    @Transactional
    public void login(LoginDTO dto, HttpServletResponse response) throws BadRequestException {
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder(12);
        String email = dto.getEmail();
        String password = dto.getPassword();
        User user = authRepository.findByEmail(email);
        if (user == null) {
            throw new BadRequestException("User is not exist");
        }
        boolean isLegal = bc.matches(password, user.getPassword());
        if (!isLegal) {
            throw new BadRequestException("Bad password");
        }
        TokenPairDTO tokens = jwtService.getTokens(user.getId());
        addHttpOnlyCookie(response, "accessToken", tokens.getAccessToken(), 24 * 60 * 60);
        addHttpOnlyCookie(response, "refreshToken", tokens.getRefreshToken(), 24 * 60 * 60 * 30);
        user.setRefreshToken(tokens.getRefreshToken());
    }
    @Transactional
    public void signUp(LoginDTO dto, HttpServletResponse response) throws BadRequestException {
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder(12);
        String email = dto.getEmail();
        String password = dto.getPassword();
        User existUser = authRepository.findByEmail(email);
        if (existUser != null) {
            throw new BadRequestException("User is already exist");
        }
        String hash = bc.encode(password);
        User user = new User(email, hash);
        TokenPairDTO tokens = jwtService.getTokens(user.getId());
        addHttpOnlyCookie(response, "accessToken", tokens.getAccessToken(), 24 * 60 * 60);
        addHttpOnlyCookie(response, "refreshToken", tokens.getRefreshToken(), 24 * 60 * 60 * 30);
        user.setRefreshToken(tokens.getRefreshToken());
        authRepository.save(user);
        //return tokens;
    }

    public AccessTokenDTO getAccessToken(HttpServletResponse response) {
        AccessTokenDTO accessToken = jwtService.getAccessToken(refreshTokenDTO);
        addHttpOnlyCookie(response, "accessToken", accessToken.getAccessToken(), 24 * 60 * 60);
    }

    public void signOut(HttpServletResponse response) {
        addHttpOnlyCookie(response, "accessToken", null, 0);
        addHttpOnlyCookie(response, "refreshToken", null, 0);
    }

    private void addHttpOnlyCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }


}
