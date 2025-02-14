package blaybus.hair_mvp.auth.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import blaybus.hair_mvp.auth.FilterExceptionResolver;
import blaybus.hair_mvp.auth.RequestMatcherHolder;
import blaybus.hair_mvp.auth.dto.AccessTokenPayload;
import blaybus.hair_mvp.auth.dto.LoginResponse;
import blaybus.hair_mvp.auth.dto.RefreshTokenPayload;
import blaybus.hair_mvp.auth.service.LoginService;
import blaybus.hair_mvp.constants.JwtMetadata;
import blaybus.hair_mvp.domain.user.entity.RefreshToken;
import blaybus.hair_mvp.domain.user.repository.RefreshTokenRepository;
import blaybus.hair_mvp.exception.ErrorResponseCode;
import blaybus.hair_mvp.exception.code.AuthExceptionCode;
import blaybus.hair_mvp.utils.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final LoginService loginService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final FilterExceptionResolver<JwtException> jwtFilterExceptionResolver;
    private final RequestMatcherHolder requestMatcherHolder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            validateRefreshToken(request, response);
            validateAccessToken(request, response, filterChain);
        } catch (JwtException ex) {
            jwtFilterExceptionResolver.setResponse(response, ex);
        }

    }


    private void validateRefreshToken(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final Optional<String> refreshToken = getRefreshTokenFromCookie(request);
        if(refreshToken.isEmpty()) return;
        jwtService.verifyToken(refreshToken.get());
        //인증 통과
        Optional<RefreshToken> byToken = refreshTokenRepository.findByToken(refreshToken.get());
        if(byToken.isEmpty()){
            throw new AuthExceptionCode(ErrorResponseCode.FAIL_LOGIN, "refreshToken 인증 실패");
        }
        LoginResponse loginResponse = loginService.login(byToken.get().getUser().getEmail());
        response.addHeader(HttpHeaders.SET_COOKIE, loginResponse.getAccessTokenCookie().getValue());
        response.addHeader(HttpHeaders.AUTHORIZATION, loginResponse.getRefreshToken());
        SuccessResponse apiResponse = new SuccessResponse(false,  "JWT Reissue", null);
        String responseBody = objectMapper.writeValueAsString(apiResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(responseBody);
        response.getWriter().flush();
        response.getWriter().close();
        return;
    }

    private void validateAccessToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String accessToken = getAccessTokenFromCookie(request);
        Claims claims = jwtService.verifyToken(accessToken);
        AccessTokenPayload accessTokenPayload = jwtService.createAccessTokenPayload(claims);
        var email = accessTokenPayload.email();
        var role = accessTokenPayload.roleEnum().name();
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, List.of(authority));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private Optional<String> getRefreshTokenFromCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(JwtMetadata.REFRESH_TOKEN)) {
                return Optional.ofNullable(cookie.getValue());
            }
        }
        return Optional.empty();
    }

    private String getAccessTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new JwtException("Missing cookie");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(JwtMetadata.ACCESS_TOKEN)) {
                if (cookie.getValue() == null || cookie.getValue().isEmpty()) {
                    throw new JwtException("Empty Access Token in Cookie");
                }
                return cookie.getValue();
            }
        }
        throw new JwtException("Missing Token");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        RequestMatcher requestMatchers = requestMatcherHolder.getRequestMatchersByMinRole(null);
        return requestMatchers.matches(request);
    }
}