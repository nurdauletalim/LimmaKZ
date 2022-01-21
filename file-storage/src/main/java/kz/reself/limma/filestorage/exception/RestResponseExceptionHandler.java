package kz.reself.limma.filestorage.exception;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Log
class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleForbiddenActionException(BadRequestException ex, WebRequest request) {
        return new ResponseEntity<>(createErrorObject(ex, true, ex.getArgs()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(createErrorObject(ex, true, ex.getArgs()), HttpStatus.NOT_FOUND);
    }

    private ErrorWithoutNull createErrorObject(RuntimeException exception, Boolean print, String[] args) {
        String rawMsg = exception.getMessage();

        if (print) {
            logger.error(exception.getMessage());
        }

        return new ErrorWithoutNull(rawMsg);
    }
}
