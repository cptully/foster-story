package entity;

import com.sun.tools.classfile.Annotation;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by chris on 10/3/16.
 */
@Entity
public class User {
    @Id
    @GeneratedValue
    @NotNull
    private Integer id;

    @NotNull
    private String firstName;
    private String middelName;
    private String lastName;

    @OneToMany
    private Phone phone;

    @OneToMany
    private Address address;

    @OneToMany
    @JoinColumn(name = "user_id")
    private Website website;

    private String username;

    @Email
    private String email;
}
