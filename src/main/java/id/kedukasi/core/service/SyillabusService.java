package id.kedukasi.core.service;

import org.springframework.http.ResponseEntity;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.SyillabusRequest;
import id.kedukasi.core.request.UpdateSyllabusRequest;


public interface SyillabusService {
    
    ResponseEntity<Result> createSyllabus(SyillabusRequest syillabus);

    ResponseEntity<Result> updateSyillabus(UpdateSyllabusRequest syillabus);

    Result getAllSyillabus(String uri, String search, long limit, long offset);

    ResponseEntity<Result> getSyillabusById(Long id);

    ResponseEntity<Result> deleteSyillabus(Long id);

    ResponseEntity<Result> softDeleteSyillabus(Long id);
}
