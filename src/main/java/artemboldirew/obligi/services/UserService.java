package artemboldirew.obligi.services;

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

    public User getUserById(Long id) throws BadRequestException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new BadRequestException("User is not exist");
        }
        return user.get();
    }

}
