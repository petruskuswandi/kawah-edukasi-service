package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.PesertaRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CalonPesertaService {
    Result getAllCalonPeserta(String uri);

    Result getCalonPesertaById(long id, String uri);

    ResponseEntity<?> updateCalonPeserta(PesertaRequest pesertaRequest,long kelasId, MultipartFile uploadImage);

    ResponseEntity<?> deleteCalonPeserta(boolean banned, long id, String uri);

    ResponseEntity<?> changeToPeserta(long id, String uri);

    ResponseEntity<?> changeStatusTes(Long statusTesOrd, long id, String uri);

    ResponseEntity<?> changeKelas(long calonPesertaId, long kelasId, String uri);

    Result filterByStatusTes(Long statusTesOrd);

    Result searchCalonPeserta(String keyword);

    Result sortAndPaging(Integer page, Integer size, Boolean ascending);
}
