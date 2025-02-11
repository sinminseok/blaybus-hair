package blaybus.hair_mvp.exception.code;


import blaybus.hair_mvp.exception.ErrorResponseCode;
import blaybus.hair_mvp.exception.ExceptionBase;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundDataExceptionCode extends ExceptionBase {

    public NotFoundDataExceptionCode(@Nullable String message) {
        this.errorCode = ErrorResponseCode.NOT_FOUND;
        this.errorMessage = message;
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
