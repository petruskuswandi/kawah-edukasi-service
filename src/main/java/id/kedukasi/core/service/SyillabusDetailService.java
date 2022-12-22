package id.kedukasi.core.service;

import org.springframework.http.ResponseEntity;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.SyillabusDetailRequest;
import id.kedukasi.core.request.UpdateSyillabusDetailRequest;

public interface SyillabusDetailService {
    
    ResponseEntity<Result> createSyillabusDetail(SyillabusDetailRequest syillabusDetail);

    ResponseEntity<Result> getAllSyillabusDetail();

    ResponseEntity<Result> getSyillabusDetailById(Long id);

    ResponseEntity<Result> deleteSyillabusDetail(Long id);

    ResponseEntity<Result> updateSyillabusDetail(UpdateSyillabusDetailRequest syillabusDetail);

}
