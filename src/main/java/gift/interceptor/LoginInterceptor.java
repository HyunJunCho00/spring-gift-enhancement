package gift.interceptor;

import gift.exception.InvalidTokenException;
import gift.security.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    private static final String BEARER_PREFIX = "Bearer ";

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            throw new InvalidTokenException("인증 헤더가 없거나 형식이 올바르지 않습니다.");
        }
        String token = authHeader.substring(BEARER_PREFIX.length());
        try {
            jwtTokenProvider.validateToken(token);
            Long memberId=jwtTokenProvider.getSubject(token);
            request.setAttribute("memberId", memberId);
            return true;
        } catch (JwtException | NumberFormatException e) {
            throw new InvalidTokenException("토큰이 유효하지 않습니다.");
        }
    }
}
