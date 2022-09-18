package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.PesertaRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PesertaService {
    Result getAllPeserta(String uri);

    Result getPesertaById(long id, String uri);

    ResponseEntity<?> updatePeserta(PesertaRequest pesertaRequest);

    ResponseEntity<?> deletePeserta(boolean banned, long id, String uri);

    ResponseEntity<?> statusPeserta(Long statusPesertaOrd, long id, String uri);

    ResponseEntity<?> statusTes(Long statusTesOrd, long id, String uri);

    ResponseEntity<?> addPesertaToKelas(long pesertaId, long kelasId, String uri);

    ResponseEntity<?> updateUploadImageBlob(long id, MultipartFile profilePicture, String uri);

    ResponseEntity<?> updateUploadImageFolder(long id, MultipartFile profilePicture, String uri);

    Result filterByStatusPeserta(Long statusPesertaOrd);

    Result search(String keyword);

    Result sortAndPaging(Integer page, Integer size, Boolean ascending);
}
