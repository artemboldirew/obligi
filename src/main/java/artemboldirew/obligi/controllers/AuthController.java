package artemboldirew.obligi.controllers;


import artemboldirew.obligi.dto.LoginDTO;
import artemboldirew.obligi.dto.TokenPairDTO;
import artemboldirew.obligi.entities.User;
import artemboldirew.obligi.services.AuthService;
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
    public TokenPairDTO login(@RequestBody LoginDTO dto) {
        return authService.login(dto);
    }
}
