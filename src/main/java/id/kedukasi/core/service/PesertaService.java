package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public interface PesertaService {

    Result getAllPeserta(String uri);

    Result getAllBannedPeserta(String uri);

    Result getPesertaById(Long id, String uri);

    ResponseEntity<?> updatePeserta(Long id, Long kelasId,Long batchId, String namaPeserta, String tanggalLahir,
                                    String jenisKelamin, String pendidikanTerakhir, String noHp, String email,
                                    MultipartFile uploadImage, Long provinsi, Long kota, Long kecamatan,
                                    Long kelurahan, String alamatRumah, String motivasi, String kodeReferal);

    ResponseEntity<?> deletePeserta(boolean banned, Long id, String uri);

    ResponseEntity<?> changeToCalonPeserta(Long id, String uri);

    ResponseEntity<?> changeKelas(Long pesertaId, Long kelasId, String uri);

    Result searchPeserta(String keyword);

    Result sortAndPaging(Integer page, Integer size, Boolean ascending);
}
