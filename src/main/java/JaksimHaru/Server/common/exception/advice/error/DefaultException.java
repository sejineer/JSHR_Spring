package JaksimHaru.Server.common.exception.advice.error;

import JaksimHaru.Server.common.exception.advice.payload.ErrorCode;
import lombok.Getter;

@Getter
public class DefaultException extends RuntimeException {

    private ErrorCode errorCode;

    public DefaultException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public DefaultException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
