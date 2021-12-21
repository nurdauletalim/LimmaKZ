package kz.reself.limma.review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "review")
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String reviewText;
    @ManyToOne
    @JoinColumn(name = "organization_id")
    @JsonIgnore
    private Organization organization;
}
