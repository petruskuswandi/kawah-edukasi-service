package id.kedukasi.core.service;


import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.SaveEducationRequest;
import id.kedukasi.core.request.UpdateEducationRequest;
import org.springframework.http.ResponseEntity;

public interface EducationService {
    ResponseEntity<Result> saveEducation(SaveEducationRequest request);
    ResponseEntity<Result> updateEducation(UpdateEducationRequest request, int id);
    ResponseEntity<Result> getById(int id);
    ResponseEntity<Result> getAll();
    ResponseEntity<Result> deleteEducation(int id);
}
