package kz.reself.limma.filestorage.exception;

public class ResourceNotFoundException extends CustomException {
    public ResourceNotFoundException(String title, String[] args) {
        super(title, args);
    }
}
