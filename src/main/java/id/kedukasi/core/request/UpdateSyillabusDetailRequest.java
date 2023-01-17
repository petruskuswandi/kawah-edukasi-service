package id.kedukasi.core.request;

import id.kedukasi.core.models.Kelas;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSyillabusDetailRequest {

    @ApiModelProperty(example = "1", required = true)
    private Long id;

    
    @ApiModelProperty(example = "1", required = true)
    private Long kelas;
    
    @ApiModelProperty(example = "1", required = true)
    private Long syillabus;
    
    @ApiModelProperty(example = "false", required = true)
    private boolean isDeleted;
    
    public UpdateSyillabusDetailRequest() {
    }

    public UpdateSyillabusDetailRequest(Long id, boolean isDeleted) {
        this.id = id;
        this.isDeleted = isDeleted;
    }

    
}
