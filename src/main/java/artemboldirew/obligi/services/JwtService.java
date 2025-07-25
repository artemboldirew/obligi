package artemboldirew.obligi.services;

import artemboldirew.obligi.dto.AccessTokenDTO;
import artemboldirew.obligi.dto.RefreshTokenDTO;
import artemboldirew.obligi.dto.TokenPairDTO;
import artemboldirew.obligi.entities.User;
import artemboldirew.obligi.repositories.AuthRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;

@Service
public class JwtService {

    @Value("${jwt.secret.access}")
    private String accessKey;

    @Value("${jwt.secret.refresh}")
    private String refreshKey;

    private final AuthRepository authRepository;

    public JwtService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public TokenPairDTO getTokens(Long id) {
//        Map<String, Long> payload  = new HashMap<>();
//        payload.put("id", id);
        SecretKey accessSecret = Keys.hmacShaKeyFor(accessKey.getBytes());
        SecretKey refreshSecret = Keys.hmacShaKeyFor(refreshKey.getBytes());
        String accessToken = Jwts.builder().subject(String.valueOf(id)).expiration(new Date(System.currentTimeMillis() + 86400000)).signWith(accessSecret, Jwts.SIG.HS256).compact();
        String refreshToken = Jwts.builder().subject(String.valueOf(id)).expiration(new Date(System.currentTimeMillis() + 1209600000)).signWith(refreshSecret, Jwts.SIG.HS256).compact();
        return new TokenPairDTO(accessToken, refreshToken);
    }

    public AccessTokenDTO getAccessToken(RefreshTokenDTO refreshTokenDTO) {
        SecretKey SECRET_KEY = Keys.hmacShaKeyFor(refreshKey.getBytes());
        try {
            Claims claims = Jwts.parser().decryptWith(SECRET_KEY).build().parseSignedClaims(refreshTokenDTO.getRefreshToken()).getPayload();
            Long id = Long.getLong(claims.getSubject());
            Optional<User> user = authRepository.findById(id);
            if (user.isEmpty()) {
                throw new BadRequestException("User is not exist");
            }
            if (!Objects.equals(user.get().getRefreshToken(), refreshTokenDTO.getRefreshToken())) {
                throw new IllegalArgumentException();
            }
            String accessToken = Jwts.builder().subject(String.valueOf(id)).expiration(new Date(System.currentTimeMillis() + 86400000)).signWith(SignatureAlgorithm.HS256, accessKey).compact();
            return new AccessTokenDTO(accessToken);
        } catch (ExpiredJwtException e) {
            // Токен просрочен
            throw new RuntimeException(e);
        } catch (UnsupportedJwtException e) {
            // Неподдерживаемый алгоритм
            throw new RuntimeException(e);
        } catch (MalformedJwtException e) {
            // Поврежденный токен
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            // Неверная подпись
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            // Пустой токен
            throw new RuntimeException(e);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    public Long validateAccessToken(String accessToken) {
        SecretKey SECRET_KEY = Keys.hmacShaKeyFor(accessKey.getBytes());
        try {
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(accessToken).getPayload();
            Long id = Long.parseLong(claims.getSubject());
            Optional<User> user = authRepository.findById(id);
            if (user.isEmpty()) {
                throw new BadRequestException("User is not exist");
            }
            return user.get().getId();
        } catch (ExpiredJwtException e) {
            // Токен просрочен
            throw new RuntimeException(e);
        } catch (UnsupportedJwtException e) {
            // Неподдерживаемый алгоритм
            throw new RuntimeException(e);
        } catch (MalformedJwtException e) {
            // Поврежденный токен
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            // Неверная подпись
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            // Пустой токен
            throw new RuntimeException(e);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Убираем "Bearer "
        }
        return null;
    }
}
