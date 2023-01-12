package id.kedukasi.core.request.batchDetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateBatchDetailRequest {

    @ApiModelProperty(example = "1", required = true)
    private Long batch;

    @ApiModelProperty(required = true)
    private List<NewBatchDetailRequest> list;

    @ApiModelProperty(example = "true", required = true)
    private boolean isDeleted;

    public UpdateBatchDetailRequest() {
    }

    public UpdateBatchDetailRequest( boolean isDeleted) {this.isDeleted = isDeleted;
    }
}
