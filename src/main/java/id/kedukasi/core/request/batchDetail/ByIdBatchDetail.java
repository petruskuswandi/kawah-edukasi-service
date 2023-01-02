package id.kedukasi.core.request.batchDetail;

import id.kedukasi.core.request.BatchListOAS;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class ByIdBatchDetail {

    @Schema(example = "1")
    public Long batch_id;

    @Schema(example = "")
    public String batchname;

    @Schema(example = "")
    public String description;

    @Schema
    public Date startedtime;

    @Schema()
    public Date endedtime;

    @Schema(example = "")
    public String created_by;

    @Schema()
    public List<BatchListOAS> list;
}
