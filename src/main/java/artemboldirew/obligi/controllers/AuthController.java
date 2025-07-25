package artemboldirew.obligi.controllers;


import artemboldirew.obligi.dto.AccessTokenDTO;
import artemboldirew.obligi.dto.LoginDTO;
import artemboldirew.obligi.dto.RefreshTokenDTO;
import artemboldirew.obligi.dto.TokenPairDTO;
import artemboldirew.obligi.services.AuthService;
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
    public TokenPairDTO login(@RequestBody LoginDTO dto) throws BadRequestException {
        return authService.login(dto);
    }

    @PostMapping("/signup")
    public TokenPairDTO signUp(@RequestBody LoginDTO dto) throws BadRequestException {
        return authService.signUp(dto);
    }

    @PostMapping("/access")
    public AccessTokenDTO getAccessToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        return authService.getAccessToken(refreshTokenDTO);
    }
}
