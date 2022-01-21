package kz.reself.limma.filestorage.exception;

abstract class CustomException extends RuntimeException {

    private String type;
    private String title;
    private String detail;
    private String instance;
    private String[] args;

    public CustomException(String title, String[] args) {
        super(title);
        this.type = "";
        this.title = title;
        this.detail = "";
        this.instance = "";
        this.args = args;
    }
    public CustomException(String type, String title, String detail, String instance, String[] args) {
        super(title);
        this.type = type;
        this.title = title;
        this.detail = detail;
        this.instance = instance;
        this.args = args;
    }

    public String getTitle() {
        return title;
    }

    public CustomException setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getType() {
        return type;
    }

    public CustomException setType(String type) {
        this.type = type;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public CustomException setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public String getInstance() {
        return instance;
    }

    public CustomException setInstance(String instance) {
        this.instance = instance;
        return this;
    }

    public String[] getArgs() {
        return args;
    }

    public CustomException setArgs(String[] args) {
        this.args = args;
        return this;
    }
}
