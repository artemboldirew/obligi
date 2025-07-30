package artemboldirew.obligi.controllers;


import artemboldirew.obligi.dto.AccessTokenDTO;
import artemboldirew.obligi.dto.LoginDTO;
import artemboldirew.obligi.dto.RefreshTokenDTO;
import artemboldirew.obligi.dto.TokenPairDTO;
import artemboldirew.obligi.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello, i`m really working";
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginDTO dto, HttpServletResponse response) throws BadRequestException {
        authService.login(dto, response);
    }

    @PostMapping("/signup")
    public void signUp(@RequestBody LoginDTO dto, HttpServletResponse response) throws BadRequestException {
        authService.signUp(dto, response);
    }

    @PostMapping("/access")
    public AccessTokenDTO getAccessToken(HttpServletResponse response) {
        return authService.getAccessToken(response);
    }

    @PostMapping("/signout")
    public void signOut(HttpServletResponse response) {
        authService.signOut(response);
    }
}
