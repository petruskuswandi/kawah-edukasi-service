package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.KelasRequest;
import org.springframework.http.ResponseEntity;

public interface KelasService {
    Result getAllClass(String uri);

    Result getClassById(Long id, String uri);

    ResponseEntity<?> updateClass(KelasRequest kelasRequest);

    ResponseEntity<?> deleteClass(boolean banned, Long id, String uri);
}
