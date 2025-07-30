package artemboldirew.obligi.services;

import artemboldirew.obligi.dto.SafetyUserDTO;
import artemboldirew.obligi.entities.User;
import artemboldirew.obligi.repositories.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SafetyUserDTO getUserById(Long id) throws BadRequestException {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new BadRequestException("User is not exist");
        }
        User user = userOpt.get();
        SafetyUserDTO userToClient = new SafetyUserDTO(user.getId(), user.getEmail(), user.getRole(), user.getCreatedAt());
        return userToClient;
    }

}
