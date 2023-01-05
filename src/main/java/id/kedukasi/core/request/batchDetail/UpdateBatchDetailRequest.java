package id.kedukasi.core.request.batchDetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBatchDetailRequest {

    @ApiModelProperty(example = "1", required = true)
    private Long id;

    @ApiModelProperty(example = "1", required = true)
    private Long batch;

    @ApiModelProperty(example = "1", required = true)
    private Long kelas;

    @ApiModelProperty(example = "1", required = true)
    private Long mentor;

    @ApiModelProperty(example = "true", required = true)
    private boolean isDeleted;

    public UpdateBatchDetailRequest() {
    }

    public UpdateBatchDetailRequest(Long id, boolean isDeleted) {
        this.id = id;
        this.isDeleted = isDeleted;
    }
}
