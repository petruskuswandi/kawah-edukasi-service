package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class SaveEducationRequest {
    @NotNull(message = "name null")
    @ApiModelProperty(example = "SMA", required = true)
    private String name;
    @NotNull(message = "description null")
    @ApiModelProperty(example = "Sekolah Menengah Atas ", required = true)
    private String description;
}
