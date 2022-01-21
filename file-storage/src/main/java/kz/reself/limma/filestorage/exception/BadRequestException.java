package kz.reself.limma.filestorage.exception;

public class BadRequestException extends CustomException {
    public BadRequestException(String title, String[] args) {
        super(title, args);
    }
}
