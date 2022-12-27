package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;
@Getter
@Setter
public class CreateBatchRequest {
    @NotBlank
    private String batchname;

    @NotBlank
    private String description;

    @NotBlank
    @ApiModelProperty(example = "2000-12-01", required = true)
    private Date startedtime;

    @NotBlank
    @ApiModelProperty(example = "2000-12-12", required = true)
    private Date endedtime;
}
