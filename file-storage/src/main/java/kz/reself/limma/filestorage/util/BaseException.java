package kz.reself.limma.filestorage.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Kalymbek Azamat
 * @version 1.0
 */
@Data
@AllArgsConstructor
public abstract class BaseException extends RuntimeException {
    private ErrorResponse errorResponse;

}
