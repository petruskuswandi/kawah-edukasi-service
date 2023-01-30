package id.kedukasi.core.request;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SyillabusDetailRequest {
    
    @ApiModelProperty(example = "1", required = true)
    private Long kelasId;

    // @ApiModelProperty(example = "1", required = true)
    @ApiModelProperty(example = "[\"1\",\"2\",\"3\"]")
    private List<Long> syillabusId;
    // private Long syillabus;

    public SyillabusDetailRequest() {
    }

    
}
