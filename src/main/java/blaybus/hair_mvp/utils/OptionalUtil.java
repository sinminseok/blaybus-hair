package blaybus.hair_mvp.utils;

import blaybus.hair_mvp.exception.code.NotFoundDataExceptionCode;

import java.util.Optional;

public class OptionalUtil {
    public static <T> T getOrElseThrow(final Optional<T> optional, final String message) {
        return optional.orElseThrow(() -> new NotFoundDataExceptionCode(message));
    }
}