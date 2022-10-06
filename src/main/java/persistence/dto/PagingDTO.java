package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PagingDTO {
    String name;
    int startPoint;
    public PagingDTO(){}
    public PagingDTO(int startPoint, String name){
        this.startPoint = startPoint;
        this.name = name;
    }
}
