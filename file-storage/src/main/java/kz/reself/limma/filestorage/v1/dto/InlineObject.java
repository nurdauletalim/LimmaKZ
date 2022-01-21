package kz.reself.limma.filestorage.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.core.io.Resource;

import javax.validation.Valid;
import java.util.Objects;

/**
 * InlineObject
 */
public class InlineObject {
    @JsonProperty("file")
    private Resource file;

    public InlineObject file(Resource file) {
        this.file = file;
        return this;
    }

    /**
     * Get file
     *
     * @return file
     */
    @ApiModelProperty(value = "")
    @Valid
    public Resource getFile() {
        return file;
    }

    public void setFile(Resource file) {
        this.file = file;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InlineObject inlineObject = (InlineObject) o;
        return Objects.equals(this.file, inlineObject.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class InlineObject {\n");

        sb.append("    file: ").append(toIndentedString(file)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

