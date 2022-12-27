package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.BatchRequest;
import id.kedukasi.core.request.CreateBatchRequest;
import org.springframework.http.ResponseEntity;

public interface BatchService {

    Result getBatchData(String uri, String search, Integer limit, Integer page);

    Result getBatchById(Long id, String uri);
    Result getAllBatchRunning(String uri);

//    Result getAllClassByBatch(long batchId);

    ResponseEntity<?> updateBatch(BatchRequest batchRequest);

    ResponseEntity<?> createBatch(CreateBatchRequest createBatchRequest);

    ResponseEntity<?> deleteBatch(boolean banned, Long id, String uri);
}
