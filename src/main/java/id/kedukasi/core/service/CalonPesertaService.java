package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CalonPesertaService {
    //Result getAllCalonPeserta(String uri, String search);

    Result getAllCalonPeserta(String uri, String search,long limit,long offset);

    Result getAllBannedCalonPeserta(String uri);

    Result getCalonPesertaById(Long id, String uri);

    ResponseEntity<?> updateCalonPeserta(Long id, Long kelasId,Long batchId, String namaPeserta, String tanggalLahir,
                                         String jenisKelamin, String pendidikanTerakhir, String noHp, String email,
                                         MultipartFile uploadImage, Long provinsi, Long kota, Long kecamatan,
                                         Long kelurahan, String alamatRumah, String motivasi, String kodeReferal, String nomorKtp, MultipartFile uploadCv, String jurusan, Integer status, Integer kegiatan, Boolean komitmen);

    ResponseEntity<?> deleteCalonPeserta(boolean banned, Long id, String uri);

    ResponseEntity<?> changeToPeserta(Long id,Integer statusId, String uri);

    ResponseEntity<?> changeStatusTes(Long statusTesOrd, Long id, String uri);

    ResponseEntity<?> changeKelas(Long calonPesertaId, Long kelasId, String uri);

    Result filterByStatusTes(Long statusTesOrd);

    Result searchCalonPeserta(String keyword);

    Result sortAndPaging(Integer page, Integer size, Boolean ascending);

    ResponseEntity<?> setUploadImagePath(long id, MultipartFile uploadImagePath, String uri);
}
