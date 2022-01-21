package kz.reself.limma.filestorage.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Cant read file
 * @author Kalymbek Azamat
 * @version 1.0
 */
@Getter
public class ExpectationFailedException extends BaseException {

    public ExpectationFailedException(String message, String errorCode) {
        super(new ErrorResponse(message, errorCode, HttpStatus.EXPECTATION_FAILED, HttpStatus.EXPECTATION_FAILED.value()));
    }
}
