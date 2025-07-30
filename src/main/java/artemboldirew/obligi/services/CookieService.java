package artemboldirew.obligi.services;

import artemboldirew.obligi.dto.TokenPairDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    public static TokenPairDTO getAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        TokenPairDTO tokens = new TokenPairDTO();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    tokens.setAccessToken(cookie.getValue());
                } else if ("refreshToken".equals(cookie.getName())) {
                    tokens.setRefreshToken(cookie.getValue());
                }
            }
        }
        return tokens;
    }


}
