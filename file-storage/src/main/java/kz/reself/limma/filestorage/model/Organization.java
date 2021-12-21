package kz.reself.limma.filestorage.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "organization")
@Data
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String city;
}
