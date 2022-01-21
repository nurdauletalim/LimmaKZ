package kz.reself.limma.filestorage.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class ErrorWithoutNull extends Error {
    public ErrorWithoutNull(String message) {
        super(message);
    }
}
