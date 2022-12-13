package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.StatusRequest;
import id.kedukasi.core.request.UpdateStatusRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Service
public interface StatusService {
    //implements create status
    public ResponseEntity<Result> createStatus(StatusRequest status);

    //implements update status
    ResponseEntity<Result> updateStatus(UpdateStatusRequest status);

    //implements get all status
    public ResponseEntity<Result> getAllStatus();

    //implements get status by id
    public ResponseEntity<Result> getStatusById(int id);  
    
    //implements delete status by id
    ResponseEntity<Result> deleteStatusById(int id);
}
