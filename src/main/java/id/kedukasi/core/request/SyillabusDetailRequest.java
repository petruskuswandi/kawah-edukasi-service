package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SyillabusDetailRequest {
    
    @ApiModelProperty(example = "1", required = true)
    private Long kelas;

    @ApiModelProperty(example = "1", required = true)
    private Long syillabus;

    public SyillabusDetailRequest() {
    }

    
}
