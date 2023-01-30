package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
public class BatchRequest {
    private  Long id;

    private String batchname;

    private String description;

    @ApiModelProperty(example = "2000-12-01", required = true)
    private Date startedtime;

    @ApiModelProperty(example = "2000-12-12", required = true)
    private Date endedtime;
    @ApiModelProperty(example = "1", required = true)
    private Integer created_by;

}
