package blaybus.hair_mvp.auth;

import java.io.IOException;

import blaybus.hair_mvp.exception.ErrorResponseCode;
import blaybus.hair_mvp.exception.code.AuthExceptionCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import io.jsonwebtoken.JwtException;

@Component
public class JwtFilterExceptionResolver implements FilterExceptionResolver<JwtException> {

    @Override
    public void setResponse(HttpServletResponse response, JwtException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter()
                .write(new ObjectMapper().writeValueAsString(
                        new AuthExceptionCode(ErrorResponseCode.NOT_VALID_TOKEN, ex.getMessage())));

    }
}