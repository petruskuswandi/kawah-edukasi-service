package id.kedukasi.core.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProgram_name() {
        return program_name;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSoft_delete() {
        return soft_delete;
    }

    public void setSoft_delete(boolean soft_delete) {
        this.soft_delete = soft_delete;
    }
}
