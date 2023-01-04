package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.batchDetail.BatchDetailRequest;
import id.kedukasi.core.request.batchDetail.UpdateBatchDetailRequest;
import org.springframework.http.ResponseEntity;

public interface BatchDetailService {

    ResponseEntity<Result> updateBatchDetail(UpdateBatchDetailRequest batchDetail);

    ResponseEntity<Result> deleteBatchDetail(Long id);

    ResponseEntity<Result> createBatchDetail(BatchDetailRequest batchDetail);
    Result getDetailByIdBatch(Long id);
}
