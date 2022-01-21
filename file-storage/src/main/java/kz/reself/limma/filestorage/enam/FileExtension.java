package kz.reself.limma.filestorage.enam;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets FileExtension
 */
public enum FileExtension {

    JPG("JPG"),

    PNG("PNG"),

    SVG("SVG"),

    PDF("PDF"),

    XML("XML");

    private String value;

    FileExtension(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static FileExtension fromValue(String value) {
        for (FileExtension b : FileExtension.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

