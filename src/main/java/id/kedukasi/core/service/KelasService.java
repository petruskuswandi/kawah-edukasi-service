package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.KelasRequest;
import id.kedukasi.core.request.UpdateKelasRequest;
import org.springframework.http.ResponseEntity;

public interface KelasService {
    Result getAllClass(String uri, String search,long limit,long offset);


    Result getAllBannedKelas(String uri);

//    Result getAllBatchByKelas(long idKelas);

    Result getClassById(Long id, String uri);

    Result getProgramRunning(String uri);

    ResponseEntity<Result> updateClass(UpdateKelasRequest kelasRequest);

    ResponseEntity<Result> createClass(KelasRequest Request);

    ResponseEntity<?> deleteClass(boolean banned, Long id, String uri);
}
