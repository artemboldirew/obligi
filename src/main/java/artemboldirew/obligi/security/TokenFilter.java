package artemboldirew.obligi.security;

import artemboldirew.obligi.entities.User;
import artemboldirew.obligi.repositories.AuthRepository;
import artemboldirew.obligi.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
            String token = null;
            // Извлекаем токен из запроса
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("accessToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        System.out.println(token);
                    }
                }
            }

            if (token != null) {
                // Проверяем токен
                Long id = jwtService.validateAccessToken(token);
                Optional<User> user = authRepository.findById(id);
                UserDetailsImp userDetailsImp = UserDetailsImp.build(user.get());
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsImp, null, userDetailsImp.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Добавляем данные токена (Claims) в контекст безопасности или объект запроса
            }
            else {
                // Обработка ошибок токена (например, отправляем 401 Unauthorized)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (Exception e) {
            // Обработка ошибок токена (например, отправляем 401 Unauthorized)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage());
            return;
        }

        // Продолжаем выполнение запроса
        filterChain.doFilter(request, response);
    }
}
