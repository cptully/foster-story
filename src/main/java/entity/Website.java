package entity;

import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


/**
 * Created by chris on 10/3/16.
 */
@Entity
public class Website {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    private User user;

    @URL
    private URL url;

    private String oauth;

    public Website(String name, User user, URL url) {
        this.name = name;
        this.url = url;
        this.user = user;
    }

    public Website() {
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

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getOauth() {
        return oauth;
    }

    public void setOauth(String oauth) {
        this.oauth = oauth;
    }
}
