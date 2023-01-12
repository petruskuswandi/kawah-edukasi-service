package id.kedukasi.core.request.batchDetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BatchDetailRequest {

    @ApiModelProperty(example = "1", required = true)
    private Long batch;

   @ApiModelProperty(required = true)
   private List<NewBatchDetailRequest> list;

    public BatchDetailRequest() {
    }
}
