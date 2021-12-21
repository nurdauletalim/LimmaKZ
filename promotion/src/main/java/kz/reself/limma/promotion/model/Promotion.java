package kz.reself.limma.promotion.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "promotion")
@Data
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String description;

    private Boolean state;

    @Column(name = "published_date")
    private Timestamp publishedDate;

    @Column(name = "expiration_date")
    private Timestamp expirationDate;

    @Column(name = "organization_id")
    private int organizationId;

    @JsonManagedReference
    @OneToMany(mappedBy = "promotion", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<PromotionImage> images = new ArrayList<>();
}
