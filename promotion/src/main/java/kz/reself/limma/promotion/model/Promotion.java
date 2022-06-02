package kz.reself.limma.promotion.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "promotion")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String description;

    private Boolean state;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "published_date")
    private Timestamp publishedDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "expiration_date")
    private Timestamp expirationDate;

    @Column(name = "organization_id")
    private Integer organizationId;

//    @JsonIgnore
//    @OneToMany(mappedBy = "promotion")
//    private List<PromotionImage> images = new ArrayList<>();
}
