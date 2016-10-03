package entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Created by chris on 10/3/16.
 */
@Entity
public class AddressType {
    
    @Id
    @GeneratedValue
    @NotNull
    private Integer id;
    
    @NotBlank
    private String name;

    public AddressType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public AddressType() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
