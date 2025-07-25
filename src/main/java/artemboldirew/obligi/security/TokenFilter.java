package artemboldirew.obligi.security;

import artemboldirew.obligi.entities.User;
import artemboldirew.obligi.repositories.AuthRepository;
import artemboldirew.obligi.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthRepository authRepository;

    public TokenFilter(JwtService jwtService, AuthRepository authRepository) {
        this.jwtService = jwtService;
        this.authRepository = authRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Извлекаем токен из запроса
            String token = JwtService.getTokenFromRequest(request);

            if (token != null) {
                // Проверяем токен
                Long id = jwtService.validateAccessToken(token);
                Optional<User> user = authRepository.findById(id);
                UserDetailsImp userDetailsImp = UserDetailsImp.build(user.get());
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsImp, null, userDetailsImp.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Добавляем данные токена (Claims) в контекст безопасности или объект запроса
            }
        } catch (Exception e) {
            System.out.println("Токен не валиден");
            // Обработка ошибок токена (например, отправляем 401 Unauthorized)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage());
            return;
        }

        // Продолжаем выполнение запроса
        filterChain.doFilter(request, response);
    }
}
