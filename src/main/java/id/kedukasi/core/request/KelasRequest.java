package id.kedukasi.core.request;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class KelasRequest {

    private Long id;

    private String classname ;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
