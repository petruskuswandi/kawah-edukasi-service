package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.PesertaRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public interface PesertaService {

    Result getAllPeserta(String uri);

    Result getPesertaById(long id, String uri);

    ResponseEntity<?> updatePeserta(Long id, Long kelasId, String namaPeserta, Date tanggalLahir,
                                    String jenisKelamin, String pendidikanTerakhir, String noHp, String email,
                                    MultipartFile uploadImage, Integer provinsi, Integer kota, Integer kecamatan,
                                    Integer kelurahan, String alamatRumah, String motivasi, String kodeReferal);

    ResponseEntity<?> deletePeserta(boolean banned, long id, String uri);

    ResponseEntity<?> changeToCalonPeserta(long id, String uri);

    ResponseEntity<?> changeKelas(long pesertaId, long kelasId, String uri);

    Result searchPeserta(String keyword);

    Result sortAndPaging(Integer page, Integer size, Boolean ascending);

    Result cekNoHP(String noHp);
}
