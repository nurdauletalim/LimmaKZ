package kz.reself.limma.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ApplicationDTO {
    private Integer id;

    private String contact;
    private String name;
    private String email;
    private Timestamp registered;


    private Integer productId;
    @JsonIgnore
    private Product product;

    private Integer managerId;
    @JsonIgnore
    private Account account;

    private String comment;


    private ProductApplicationStatus status;
    private Timestamp taken;
    private Timestamp closed;

    private byte[] image;

}
