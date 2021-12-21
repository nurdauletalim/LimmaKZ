package kz.reself.limma.catalog.model;

import lombok.Data;

@Data
public class ModelDTO {
    private int id;
    private String value;
    private String displayName;
    public ModelDTO(Object[] model){
        id = (Integer) model[0];
        displayName = (String) model[1];
    }
}
