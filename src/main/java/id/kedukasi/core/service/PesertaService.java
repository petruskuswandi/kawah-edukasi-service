package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface PesertaService {

    Result getAllPeserta(String uri);

    Result getAllBannedPeserta(String uri);

    Result getPesertaById(Long id, String uri);

    ResponseEntity<Result> registerPeserta(RegisterRequest registerRequest ,String jsonString, List<MultipartFile> files) throws ParseException, MessagingException, IOException;

    ResponseEntity<?> updatePeserta(Long id, Long kelasId,Long batchId, String namaPeserta, String tanggalLahir,
                                    String jenisKelamin, String pendidikanTerakhir, String noHp, String email,
                                    MultipartFile uploadImage, Long provinsi, Long kota, Long kecamatan,
                                    Long kelurahan, String alamatRumah, String motivasi, String kodeReferal, String nomorKtp);

    ResponseEntity<?> deletePeserta(boolean banned, Long id, String uri);

    ResponseEntity<?> changeToCalonPeserta(Long id, String uri);

    ResponseEntity<?> changeKelas(Long pesertaId, Long kelasId, String uri);

    Result searchPeserta(String keyword);

    Result sortAndPaging(Integer page, Integer size, Boolean ascending);
}
