package artemboldirew.obligi.security;

import artemboldirew.obligi.entities.Role;
import artemboldirew.obligi.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class UserDetailsImp implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String refreshToken;

    public UserDetailsImp(Long id, String email, String password, Role role, LocalDateTime createdAt, LocalDateTime updatedAt, String refreshToken) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.refreshToken = refreshToken;
    }

    public static UserDetailsImp build(User user) {
        return new UserDetailsImp(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), user.getCreatedAt(), user.getUpdatedAt(), user.getRefreshToken());
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
