package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class UpdateEducationRequest {
    @NotNull(message = "name null")
    @ApiModelProperty(example = "SMP", required = true)
    private String name;
    @NotNull(message = "description null")
    @ApiModelProperty(example = "Sekolah Menengah Pertama", required = true)
    private String description;
}
