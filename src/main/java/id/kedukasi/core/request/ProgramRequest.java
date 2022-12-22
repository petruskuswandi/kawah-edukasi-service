package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class ProgramRequest {
    @NotEmpty
    @NotNull
    @NotBlank
    @ApiModelProperty(example = "nama program", required = true)
    private String programName;
    @NotEmpty
    @NotNull
    @NotBlank
    @ApiModelProperty(example = "deskripsi", required = true)
    private String description;

    public ProgramRequest() {
    }

    public ProgramRequest(String program_name, String description) {
        this.programName = program_name;
        this.description = description;
    }

    public String getProgram_name() {
        return programName;
    }

    public void setProgram_name(String program_name) {
        this.programName = program_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
