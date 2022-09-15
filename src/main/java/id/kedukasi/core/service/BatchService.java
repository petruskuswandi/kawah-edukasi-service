package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.BatchRequest;
import org.springframework.http.ResponseEntity;

public interface BatchService {

    Result getAllBatch(String uri);

    Result getBatchById(long id, String uri);

    ResponseEntity<?> updateBatch(BatchRequest batchRequest);

    ResponseEntity<?> deleteBatch(boolean banned, long id, String uri);
}
