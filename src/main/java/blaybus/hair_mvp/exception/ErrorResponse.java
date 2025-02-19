package blaybus.hair_mvp.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final boolean success;
    private final String message;
    private final String data;

    public ErrorResponse(ExceptionBase exception) {
        this.success = false;
        this.message = exception.getErrorMessage();
        this.data = String.valueOf(exception.getErrorCode().getCode());
    }
}