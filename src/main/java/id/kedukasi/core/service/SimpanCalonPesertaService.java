package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface SimpanCalonPesertaService {



//    ResponseEntity<?> updateCalonPeserta(Long id, Long programId, Long batchId, String namaPeserta, String nomorKtp, String kodePeserta, String tanggalLahir,
//                                          String jenisKelamin, String pendidikanTerakhir, String noHp, String email,
//                                         MultipartFile uploadImage, Long provinsi, Long kota, Long kecamatan,
//                                         Long kelurahan, String alamatRumah, String tanggalMulai, String tanggalAkhir);

    ResponseEntity<?> updateCalonPeserta(Long id, Long kelasId,Long batchId, String namaPeserta, String tanggalLahir,
                                         String jenisKelamin, String pendidikanTerakhir, String noHp, String email,
                                         MultipartFile uploadImage, Long provinsi, Long kota, Long kecamatan,
                                         Long kelurahan, String alamatRumah, String motivasi, String kodeReferal, String nomorKtp, MultipartFile uploadCv);

    Result getAllCalonPeserta(String uri);

    Result getAllBannedCalonPeserta(String uri);

    Result getCalonPesertaById(Long id, String uri);
    ResponseEntity<?> deleteCalonPeserta(boolean banned, Long id, String uri);

    ResponseEntity<?> changeToPeserta(Long id, String uri);

    ResponseEntity<?> changeStatusTes(Long statusTesOrd, Long id, String uri);

    ResponseEntity<?> changeKelas(Long calonPesertaId, Long kelasId, String uri);

    Result filterByStatusTes(Long statusTesOrd);

    Result searchCalonPeserta(String keyword);

    Result sortAndPaging(Integer page, Integer size, Boolean ascending);
}