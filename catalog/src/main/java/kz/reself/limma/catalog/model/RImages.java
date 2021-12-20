package kz.reself.limma.catalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RImages {
    //TODO: refactor this class
    private String url;
    private byte[] content;
    private Integer id;
    private Integer size;
    private String name;

}
