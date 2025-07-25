package artemboldirew.obligi.repositories;

import artemboldirew.obligi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
