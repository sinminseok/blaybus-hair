package blaybus.hair_mvp.exception.code;

import blaybus.hair_mvp.exception.ErrorResponseCode;
import blaybus.hair_mvp.exception.ExceptionBase;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AccessTokenExceptionCode extends ExceptionBase {

    public AccessTokenExceptionCode(ErrorResponseCode errorResponseCode, @Nullable String message) {
        this.errorCode = errorResponseCode;
        this.errorMessage = message;
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}
