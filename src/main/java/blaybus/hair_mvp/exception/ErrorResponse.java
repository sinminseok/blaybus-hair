package blaybus.hair_mvp.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final boolean success;
    private final String message;
    private final String errorCode;

    public ErrorResponse(ExceptionBase exception) {
        this.success = false;
        this.message = exception.getErrorMessage();
        this.errorCode = String.valueOf(exception.getErrorCode().getCode());
    }
}