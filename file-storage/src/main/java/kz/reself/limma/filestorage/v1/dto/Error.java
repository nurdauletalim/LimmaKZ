package kz.reself.limma.filestorage.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * https://tools.ietf.org/html/rfc7807
 */
@ApiModel(description = "https://tools.ietf.org/html/rfc7807")

public class Error {
    @JsonProperty("type")
    private String type;

    @JsonProperty("title")
    private String title;

    @JsonProperty("detail")
    private String detail;

    @JsonProperty("instance")
    private String instance;

    public Error type(String type) {
        this.type = type;
        return this;
    }

    /**
     * A URI reference that identifies the problem type.  This specification encourages that, when dereferenced, it provide human-readable documentation for the problem type. When this member is not present, its value is assumed to be \"about:blank\".
     *
     * @return type
     */
    @ApiModelProperty(example = "https://example.com/probs/out-of-credit", value = "A URI reference that identifies the problem type.  This specification encourages that, when dereferenced, it provide human-readable documentation for the problem type. When this member is not present, its value is assumed to be \"about:blank\".")


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Error title(String title) {
        this.title = title;
        return this;
    }

    /**
     * A short, human-readable summary of the problem type.  It SHOULD NOT change from occurrence to occurrence of the problem, except for purposes of localization (e.g., using proactive content negotiation);
     *
     * @return title
     */
    @ApiModelProperty(example = "You do not have enough credit.", required = true, value = "A short, human-readable summary of the problem type.  It SHOULD NOT change from occurrence to occurrence of the problem, except for purposes of localization (e.g., using proactive content negotiation);")
    @NotNull


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Error detail(String detail) {
        this.detail = detail;
        return this;
    }

    /**
     * A human-readable explanation specific to this occurrence of the problem.
     *
     * @return detail
     */
    @ApiModelProperty(example = "Your current balance is 30, but that costs 50.", value = "A human-readable explanation specific to this occurrence of the problem.")


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Error instance(String instance) {
        this.instance = instance;
        return this;
    }

    /**
     * A URI reference that identifies the specific occurrence of the problem.  It may or may not yield further information if dereferenced.
     *
     * @return instance
     */
    @ApiModelProperty(example = "/account/12345/msgs/abc", value = "A URI reference that identifies the specific occurrence of the problem.  It may or may not yield further information if dereferenced.")


    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Error error = (Error) o;
        return Objects.equals(this.type, error.type) &&
                Objects.equals(this.title, error.title) &&
                Objects.equals(this.detail, error.detail) &&
                Objects.equals(this.instance, error.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, title, detail, instance);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Error {\n");

        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    detail: ").append(toIndentedString(detail)).append("\n");
        sb.append("    instance: ").append(toIndentedString(instance)).append("\n");
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

