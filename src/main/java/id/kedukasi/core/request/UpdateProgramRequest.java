package id.kedukasi.core.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Data
public class UpdateProgramRequest {
    @NotNull
    private Long id;

    @NotBlank
    private String program_name;

    @NotBlank
    private String description;

    private boolean soft_delete;

    public UpdateProgramRequest() {
    }

    public UpdateProgramRequest(Long id, String program_name, String description, boolean soft_delete) {
        this.id = id;
        this.program_name = program_name;
        this.description = description;
        this.soft_delete = soft_delete;
    }


}
