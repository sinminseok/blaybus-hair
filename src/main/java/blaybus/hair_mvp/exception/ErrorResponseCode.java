package blaybus.hair_mvp.exception;

/**
 * 에러 상태 코드 관리
 */
public enum ErrorResponseCode {
    NOT_VALID_TOKEN(4011),
    FAIL_SEND_TOKEN(4012),
    FAIL_LOGIN(4013),
    FAIL_GOOGLE_TOKEN(4031),
    NOT_FOUND(4041),
    INVALID_DATA(4042),
    NOT_MATCH_PASSWORD(5001);

    private final int code;

    ErrorResponseCode(int c) {
        this.code = c;
    }

    public int getCode() {
        return this.code;
    }
}
