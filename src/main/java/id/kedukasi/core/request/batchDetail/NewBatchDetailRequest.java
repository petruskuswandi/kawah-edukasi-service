package id.kedukasi.core.request.batchDetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewBatchDetailRequest {

    @ApiModelProperty(required = true)
    private Long kelas;

    @ApiModelProperty(required = true)
    private Long mentor;

    public NewBatchDetailRequest() {
    }
}
