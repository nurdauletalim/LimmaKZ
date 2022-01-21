package kz.reself.limma.filestorage.enam;

public enum FileSize {
    SMALL(600, 350),
    MEDIUM(1440, 850);
    private Integer width;
    private Integer height;

    FileSize(Integer width, Integer height) {
        this.width = width;
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
}
