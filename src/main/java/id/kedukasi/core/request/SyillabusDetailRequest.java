package id.kedukasi.core.request;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SyillabusDetailRequest {
    
    @ApiModelProperty(example = "1", required = true)
    private Long kelas;

    @ApiModelProperty(example = "1", required = true)
    // @ApiModelProperty(example = "[\"item1\",\"item2\",\"item3\"]")
    // private List<Long> syillabus;
    private Long syillabus;

    public SyillabusDetailRequest() {
    }

    
}
