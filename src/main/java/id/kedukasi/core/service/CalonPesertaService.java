package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public interface CalonPesertaService {
    Result getAllCalonPeserta(String uri);

    Result getCalonPesertaById(long id, String uri);

    ResponseEntity<?> updateCalonPeserta(Long id, Long kelasId, String namaPeserta, Date tanggalLahir,
                                         String jenisKelamin, String pendidikanTerakhir, String noHp, String email,
                                         MultipartFile uploadImage, Integer provinsi, Integer kota, Integer kecamatan,
                                         Integer kelurahan, String alamatRumah, String motivasi, String kodeReferal);

    ResponseEntity<?> deleteCalonPeserta(boolean banned, long id, String uri);

    ResponseEntity<?> changeToPeserta(long id, String uri);

    ResponseEntity<?> changeStatusTes(Long statusTesOrd, long id, String uri);

    ResponseEntity<?> changeKelas(long calonPesertaId, long kelasId, String uri);

    Result filterByStatusTes(Long statusTesOrd);

    Result searchCalonPeserta(String keyword);

    Result sortAndPaging(Integer page, Integer size, Boolean ascending);
}
