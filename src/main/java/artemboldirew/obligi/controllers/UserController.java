package artemboldirew.obligi.controllers;

import artemboldirew.obligi.dto.SafetyUserDTO;
import artemboldirew.obligi.entities.User;
import artemboldirew.obligi.services.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{id}")
    public SafetyUserDTO getUserById(@PathVariable Long id) throws BadRequestException {
        return userService.getUserById(id);
    }
}
