package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.enums.EnumStatusPeserta;
import id.kedukasi.core.enums.EnumStatusTes;
import id.kedukasi.core.models.Kelas;
import id.kedukasi.core.models.Peserta;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.Status;
import id.kedukasi.core.repository.BatchRepository;
import id.kedukasi.core.repository.EducationRepository;
import id.kedukasi.core.repository.KelasRepository;
import id.kedukasi.core.repository.PesertaRepository;
import id.kedukasi.core.repository.StatusRepository;
import id.kedukasi.core.repository.wilayah.KecamatanRepository;
import id.kedukasi.core.repository.wilayah.KelurahanRepository;
import id.kedukasi.core.repository.wilayah.KotaRepository;
import id.kedukasi.core.repository.wilayah.ProvinsiRepository;
import id.kedukasi.core.service.CalonPesertaService;
import id.kedukasi.core.service.FilesStorageService;
import id.kedukasi.core.utils.FileUploadUtil;
import id.kedukasi.core.utils.GlobalUtil;
import id.kedukasi.core.utils.PathGeneratorUtil;
import id.kedukasi.core.utils.StringUtil;
import id.kedukasi.core.utils.ValidatorUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalonPesertaServiceImpl implements CalonPesertaService {

    //  @Value("${app.upload-file-path}")
    //  private String folderPath;
    @Value("${app.url.staging}")
    String baseUrl;


    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    PesertaRepository pesertaRepository;

    @Autowired
    KelasRepository kelasRepository;

    @Autowired
    BatchRepository batchRepository;

    @Autowired
    EducationRepository educationRepository;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    ProvinsiRepository provinsiRepository;

    @Autowired
    KotaRepository kotaRepository;

    @Autowired
    KecamatanRepository kecamatanRepository;

    @Autowired
    KelurahanRepository kelurahanRepository;

    @Autowired
    FilesStorageService storageService;

    @Autowired
    GlobalUtil globalUtil;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    ValidatorUtil validator;

    private Result result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public Result getAllCalonPeserta(String uri, String search,long limit,long offset) {
        result = new Result();
        //default value search param
        if(search == null){
            search = "";
        }
        //null long condition
        if(limit == -99){
            limit = pesertaRepository.getCountByStatus(EnumStatusPeserta.CALON.toString());
        }
        //null long condition
        if(offset == -99){
            offset = 0;
        }else if(offset > limit){
            result.setSuccess(false);
            result.setCode(HttpStatus.BAD_REQUEST.value());
            result.setMessage("data tidak ada");
   
        }
     
        try {
            Map items = new HashMap();
            List<Peserta> getDataCalon = pesertaRepository.getAllPagination(EnumStatusPeserta.CALON.toString(),false,search,limit,offset);
            items.put("items",getDataCalon);
            items.put("totalDataResult",getDataCalon.size());
            items.put("totalData",pesertaRepository.getCountByStatus(EnumStatusPeserta.CALON.toString()));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
            result.setSuccess(false);
            result.setCode(HttpStatus.BAD_REQUEST.value());
           
        }
        return result;
    }

    @Override
    public Result getAllBannedCalonPeserta(String uri) {
        result = new Result();
        try {
            Map items = new HashMap();
            Peserta calonPeserta = new Peserta();
            calonPeserta.setStatusPeserta(EnumStatusPeserta.CALON);
            calonPeserta.setBanned(true);
            Example<Peserta> example = Example.of(calonPeserta);
            items.put("items", pesertaRepository.findAll(example,Sort.by(Sort.Direction.ASC,"id")));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getCalonPesertaById(Long id, String uri) {
        result = new Result();
        try {
            if (!pesertaRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada peserta dengan id: " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (pesertaRepository.findById(id).get().getStatusPeserta().equals(EnumStatusPeserta.PESERTA)) {
                result.setSuccess(false);
                result.setMessage("Error: id " + id + " bukan calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }else if (pesertaRepository.findById(id).get().isBanned()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada calon peserta dengan id: " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map items = new HashMap();
                items.put("items", pesertaRepository.findById(id).get());
                result.setData(items);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public ResponseEntity<?> updateCalonPeserta(Long id, Long kelasId, Long batchId, String namaPeserta,
            String tanggalLahir,
            String jenisKelamin, String pendidikanTerakhir, String noHp, String email,
            MultipartFile uploadImage, Long provinsiId, Long kotaId, Long kecamatanId,
            Long kelurahanId, String alamatRumah, String motivasi, String kodeReferal, String nomorKtp,
            MultipartFile uploadCv, String jurusan, Integer status, Integer kesibukan, Boolean komitmen) {
        result = new Result();
        try {
            // cek email
            Peserta checkEmailPeserta = pesertaRepository.findByEmail(email).orElse(new Peserta());
            if (checkEmailPeserta.getEmail() != null && !Objects.equals(id, checkEmailPeserta.getId())) {
                result.setMessage("Error: Email sudah digunakan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            if (email.isBlank()) {
                result.setMessage("Error: Email tidak boleh kosong!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            if (nomorKtp.isBlank()) {
                result.setMessage("Error: KTP tidak boleh kosong!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                .badRequest()
                .body(result);
            }
            // cek username
            // Peserta checkNamaPeserta = pesertaRepository.findByNamaPeserta(namaPeserta).orElse(new Peserta());
            // if (checkNamaPeserta.getNamaPeserta() != null && !Objects.equals(id, checkNamaPeserta.getId())) {
            //     result.setMessage("Error: Username sudah digunakan!");
            //     result.setCode(HttpStatus.BAD_REQUEST.value());
            //     return ResponseEntity
            //             .badRequest()
            //             .body(result);
            // }
            if (namaPeserta.isBlank()) {
                result.setMessage("Error: Nama Peserta tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            // cek tanggal lahir
            if (tanggalLahir.isBlank()) {
                result.setMessage("Error: Tanggal lahir tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            Date tanggalLahirTypeDate = new SimpleDateFormat("dd-MM-yyyy").parse(tanggalLahir);
            // cek jenis kelamin
            if (jenisKelamin.isBlank()) {
                result.setMessage("Error: Jenis kelamin tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            // cek pendidikan terakhir
            // if (pendidikanTerakhir.isBlank()) {
            //     result.setMessage("Error: Pendidikan terakhir tidak boleh kosong");
            //     result.setCode(HttpStatus.BAD_REQUEST.value());
            //     return ResponseEntity
            //             .badRequest()
            //             .body(result);
            // }
            // cek alamat rumah
            if (alamatRumah.isBlank()) {
                result.setMessage("Error: Alamat rumah tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            // cek motivasi
            if (motivasi.isBlank()) {
                result.setMessage("Error: Motivasi tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            // cek nomer hp
            if (!validator.isPhoneValid(noHp)) {
                result.setMessage("Error: nomor telepon tidak boleh kosong dan gunakan format 08xxx/+628xxx/628xxx!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            Peserta checkNoHp = pesertaRepository.findByNoHp(noHp).orElse(new Peserta());
            if (checkNoHp.getNoHp() != null ) {
                result.setMessage("Error: No HP sudah digunakan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            Peserta checkNoKtp = pesertaRepository.findByNomorKtp(nomorKtp).orElse(new Peserta());
            if (checkNoKtp.getNomorKtp() != null) {
                result.setMessage("Error: Nomor Ktp sudah digunakan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            if (!validator.isNumeric(nomorKtp)||nomorKtp.length() < 16) {
                result.setMessage("Error : nomor KTP harus berupa angka dan minimal 16 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
             }
            // cek status peserta
            Peserta checkStatusPeserta = pesertaRepository.findById(id).orElse(new Peserta());
            if (checkStatusPeserta.getStatusPeserta() != null
                    && !Objects.equals(EnumStatusPeserta.CALON, checkStatusPeserta.getStatusPeserta())) {
                result.setMessage("Error: id: " + id + " bukan Calon Peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

          

            // register or update
            Peserta peserta = new Peserta(namaPeserta, tanggalLahirTypeDate, jenisKelamin, pendidikanTerakhir, noHp,
                    email,
                    alamatRumah, motivasi, kodeReferal, nomorKtp);

            peserta.setId(id);
            peserta.setStatusPeserta(EnumStatusPeserta.CALON);

            // set kelas
            if (!kelasRepository.findById(kelasId).isPresent()) {
            result.setSuccess(false);
            result.setMessage("Error: Tidak ada kelas dengan id " + kelasId);
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity
            .badRequest()
            .body(result);
            } else {
            peserta.setKelas(kelasRepository.findById(kelasId).get());


            }

            // set batch
            if (!batchRepository.findById(batchId).isPresent()) {
            result.setSuccess(false);
            result.setMessage("Error: Tidak ada batch dengan id " + batchId);
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity
            .badRequest()
            .body(result);
            } else {
            peserta.setBatch(batchRepository.findById(batchId).get());
            }

            // // set image
            //  if (uploadImage != null) {
            //     String nameImage = StringUtils.cleanPath(uploadImage.getOriginalFilename());
            //     nameImage = nameImage.replaceAll(" ", "_");
            //     peserta.setUploadImageName(nameImage);

            //     // format path name          
            //     String[] name = nameImage.split("\\.");
            //     if(!name[name.length-1].equalsIgnoreCase("jpg")&&!name[name.length-1].equalsIgnoreCase("png")){
            //         result.setSuccess(false);
            //         result.setMessage("Error: Format image harus jpg & png");
            //         result.setCode(HttpStatus.BAD_REQUEST.value());
            //         return ResponseEntity
            //                 .badRequest()
            //                 .body(result);
            //     }
            //      String namaImage = nomorKtp + "_" + namaPeserta + "." + name[name.length-1];
            //      namaImage = namaImage.replaceAll(" ", "_");
            //      String fileName = String.format(folderPath + "/" + namaImage);
            //      peserta.setUploadImagePath(fileName);
            //     // save file to folder
            //      String filePath = folderPath +"/image"+ File.separator + namaImage;
            //      OutputStream out = new FileOutputStream(filePath);
            //      out.write(uploadImage.getBytes());
            //      out.close();

            //  }

            //    // set cv 
            // if (uploadCv != null) {
            //     String nameFile = StringUtils.cleanPath(uploadCv.getOriginalFilename());
            //     nameFile = nameFile.replaceAll(" ", "_");
            //     peserta.setUploadImageName(nameFile);

            //     // format path name   
            // String[] name = nameFile.split("\\.");
            // if(!name[name.length-1].equalsIgnoreCase("pdf")&&!name[name.length-1].equalsIgnoreCase("docx")){
            //     result.setSuccess(false);
            //     result.setMessage("Error: format cv harus pdf dan docx");
            //     result.setCode(HttpStatus.BAD_REQUEST.value());
            //     return ResponseEntity
            //             .badRequest()
            //             .body(result);
            // }
            // String customNameCV = nomorKtp + "_" + namaPeserta + "." + name[name.length-1];
            // customNameCV = customNameCV.replaceAll(" ", "_");
            // peserta.setUploadCv(customNameCV);

            // peserta.setUploadCvPath(folderPath +"/" + customNameCV );

            // String filePath = folderPath +"/documents"+ File.separator + customNameCV;
            // OutputStream out = new FileOutputStream(filePath);
            // out.write(uploadCv.getBytes());
            // out.close();  

            // }

            
            //Saving Image process
            //Get Image name
            String imageName = StringUtils.cleanPath(uploadImage.getOriginalFilename());
            imageName = imageName.replaceAll(" ", "_");
            peserta.setUploadImageName(imageName);

            //Save file
            String fileCode = FileUploadUtil.saveFile(imageName, uploadImage);

            //Validasi Image size
            if (fileCode == null) {
                result.setCode(HttpStatus.BAD_REQUEST.value());
                result.setSuccess(false);
                result.setMessage("File harus kurang dari 7MB");
                return ResponseEntity.badRequest().body(result);
            }

            //Set path name
            peserta.setUploadImagePath(PathGeneratorUtil.generate(fileCode,baseUrl));
            //End

               
            //Saving CV process
            //Get CV name
            String cvName = StringUtils.cleanPath(uploadCv.getOriginalFilename());
            cvName = cvName.replaceAll(" ", "_");
            peserta.setUploadCv(cvName);

            //Save file
            String fileCodeCv = FileUploadUtil.saveFile(cvName, uploadImage);

            //Validasi file size
            if (fileCodeCv == null) {
                result.setCode(HttpStatus.BAD_REQUEST.value());
                result.setSuccess(false);
                result.setMessage("File harus kurang dari 7MB");
                return ResponseEntity.badRequest().body(result);
            }

            //Set CV name
            peserta.setUploadCvPath(PathGeneratorUtil.generate(fileCodeCv,baseUrl));
            //End

            //set pendidikan terkahir
            if (!educationRepository.findById(Integer.valueOf(pendidikanTerakhir)).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada pendidikan terakhir dengan id " + pendidikanTerakhir);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                peserta.setTingkat_pendidikan(educationRepository.findById(Integer.valueOf(pendidikanTerakhir)).get());
            }

            //set status
            Optional<Status> statusPeserta = statusRepository.findById(status);
            if (!statusPeserta.isPresent()) {
                result.setMessage("Error: Status Id Belum Ada!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                peserta.setStatus(statusRepository.findById(status).get());
            }

            //kesibukan 
            Optional<Status> statusPeserta2 = statusRepository.findById(kesibukan);
            if (!statusPeserta2.isPresent()) {
                result.setMessage("Error: Status Id Belum Ada!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                peserta.setKesibukan(statusRepository.findById(kesibukan).get());
            }
            //set jurusan
            peserta.setJurusan(jurusan);

            //set komitmen
            peserta.setKomitmen(komitmen);

            peserta.setStatusTes(null);
            

            // set provinsi
            if (!provinsiRepository.findById(provinsiId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada provinsi dengan id " + provinsiId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                peserta.setProvinsi(provinsiRepository.findById(provinsiId).get());
            }

            // set kota
            if (!kotaRepository.findById(kotaId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada kota dengan id " + kotaId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                peserta.setKota(kotaRepository.findById(kotaId).get());
            }

            // set kecamatan
            if (!kecamatanRepository.findById(kecamatanId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada kecamatan dengan id " + kecamatanId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                peserta.setKecamatan(kecamatanRepository.findById(kecamatanId).get());
            }

            // set kelurahan
            if (!kelurahanRepository.findById(kelurahanId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada kelurahan dengan id " + kelurahanId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                peserta.setKelurahan(kelurahanRepository.findById(kelurahanId).get());
            }
            logger.info("tes " + peserta.getId());
            pesertaRepository.save(peserta);

            result.setMessage(id == 0 ? "Berhasil membuat calon peserta baru!" : "Berhasil memperbarui calon peserta!");
            result.setCode(HttpStatus.OK.value());
        } catch (ParseException e) {
            result.setSuccess(false);
            result.setMessage("Error: Gunakan format \"dd-MM-yyyy\" pada tanggal lahir");
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity
                    .badRequest()
                    .body(result);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
            result.setSuccess(false);
            result.setMessage(e.getCause().getCause().getMessage());
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    

    @Override
    public ResponseEntity<?> deleteCalonPeserta(boolean banned, Long id, String uri) {
        result = new Result();
        try {
            if (!pesertaRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada calon peserta dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (pesertaRepository.findById(id).get().getStatusPeserta().equals(EnumStatusPeserta.PESERTA)) {
                result.setSuccess(false);
                result.setMessage("Error: id " + id + " bukan calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                pesertaRepository.deletePeserta(banned, id);
                if (banned) {
                    result.setMessage("Berhasil menghapus calon peserta");
                } else {
                    result.setMessage("Berhasil mengembalikan calon peserta");
                }
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> changeToPeserta(Long id,Integer statusId, String uri) {
        result = new Result();
        try {
            Peserta calonPeserta = pesertaRepository.findById(id).orElse(null);
            Status status = statusRepository.findById(statusId).orElse(null);

            if (!pesertaRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada calon peserta dengan id" + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (pesertaRepository.findById(id).get().getStatusPeserta().equals(EnumStatusPeserta.PESERTA)) {
                result.setSuccess(false);
                result.setMessage("Error: id " + id + " bukan calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }else if (!statusRepository.findById(statusId).isPresent()){
                result.setSuccess(false);
                result.setMessage("Error: Id Status tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }else if (pesertaRepository.findById(id).get().isBanned()) {
                result.setSuccess(false);
                result.setMessage("Error: Id Peserta tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                calonPeserta.setStatus(status);
                pesertaRepository.statusPeserta(EnumStatusPeserta.PESERTA, id);
                result.setMessage( "Berhasil memperbarui status!");
                result.setCode(HttpStatus.OK.value());
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
            result.setSuccess(false);
            result.setMessage(e.getCause().getCause().getMessage());
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    // @Override
    // public ResponseEntity<?> changeStatusTes(Long statusTesOrd, Long id, String uri) {
    //     result = new Result();
    //     try {
    //         if (!pesertaRepository.findById(id).isPresent()) {
    //             result.setSuccess(false);
    //             result.setMessage("Error: Tidak ada calon peserta dengan id " + id);
    //             result.setCode(HttpStatus.BAD_REQUEST.value());
    //         } else if (pesertaRepository.findById(id).get().getStatusPeserta().equals(EnumStatusPeserta.PESERTA)) {
    //             result.setSuccess(false);
    //             result.setMessage("Error: id " + id + " bukan calon peserta");
    //             result.setCode(HttpStatus.BAD_REQUEST.value());
    //         } else {
    //             if (statusTesOrd == 0) {
    //                 pesertaRepository.statusTes(EnumStatusTes.LULUS, id);
    //             } else if (statusTesOrd == 1) {
    //                 pesertaRepository.statusTes(EnumStatusTes.MELAKSANAKANTES, id);
    //             } else if (statusTesOrd == 2) {
    //                 pesertaRepository.statusTes(EnumStatusTes.MENUNGGUFOLLOWUP, id);
    //             } else {
    //                 result.setMessage(
    //                         "Error: gunakan 0 untuk Lulus, 1 untuk Melaksanakan Tes dan 2 untuk Menunggu Follow Up");
    //                 result.setCode(HttpStatus.BAD_REQUEST.value());
    //                 return ResponseEntity
    //                         .badRequest()
    //                         .body(result);
    //             }
    //         }
    //     } catch (Exception e) {
    //         logger.error(stringUtil.getError(e));
    //         result.setSuccess(false);
    //         result.setMessage(e.getCause().getCause().getMessage());
    //         result.setCode(HttpStatus.BAD_REQUEST.value());
    //         return ResponseEntity.badRequest().body(result);
    //     }
    //     return ResponseEntity.ok(result);
    // }
    @Override
    public ResponseEntity<?> changeStatusTes(Long calonPesertaId,Integer statusId, String uri) {
        result = new Result();
        try {
            Peserta calonPeserta = pesertaRepository.findById(calonPesertaId).orElse(null);
            Status status = statusRepository.findById(statusId).orElse(null);
            if (!pesertaRepository.findById(calonPesertaId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada calon peserta dengan id " + calonPesertaId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (pesertaRepository.findById(calonPesertaId).get().getStatusPeserta().equals(EnumStatusPeserta.PESERTA)) {
                result.setSuccess(false);
                result.setMessage("Error: id " + calonPesertaId + " bukan calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }else if (!statusRepository.findById(statusId).isPresent()){
                result.setSuccess(false);
                result.setMessage("Error: Id Status tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }else if (pesertaRepository.findById(calonPesertaId).get().isBanned()) {
                result.setSuccess(false);
                result.setMessage("Error: Id Peserta tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                calonPeserta.setStatusTes(status);
                pesertaRepository.save(calonPeserta);
                result.setMessage( "Berhasil memperbarui status!");
                result.setCode(HttpStatus.OK.value());
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
            result.setSuccess(false);
            result.setMessage(e.getCause().getCause().getMessage());
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> changeKelas(Long calonPesertaId, Long kelasId, String uri) {
        result = new Result();
        try {
            Peserta calonPeserta = pesertaRepository.findById(calonPesertaId).orElse(null);
   
             Kelas kelas = kelasRepository.findById(kelasId).orElse(null);
             if (!pesertaRepository.findById(calonPesertaId).isPresent()) {
                 result.setSuccess(false);
                 result.setMessage("Error: Tidak ada calon peserta dengan id " + calonPesertaId);
                 result.setCode(HttpStatus.BAD_REQUEST.value());
             } else if (!kelasRepository.findById(kelasId).isPresent()) {
                 result.setSuccess(false);
                 result.setMessage("Error: Tidak ada kelas dengan id " + kelasId);
                 result.setCode(HttpStatus.BAD_REQUEST.value());
             } else if (calonPeserta.getStatusPeserta().equals(EnumStatusPeserta.PESERTA)) {
                 result.setSuccess(false);
                 result.setMessage("Error: id " + calonPesertaId + " bukan calon peserta");
                 result.setCode(HttpStatus.BAD_REQUEST.value());
             } else {
                 Kelas kelas1 = kelasRepository.findById(kelasId).orElse(null);
                 calonPeserta.setKelas(kelas1);
                 pesertaRepository.save(calonPeserta);
                 result.setMessage( "Berhasil memperbarui Kelas!");
                 result.setCode(HttpStatus.OK.value());
             }

          
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
            result.setSuccess(false);
            result.setMessage(e.getCause().getCause().getMessage());
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    // @Override
    // public Result filterByStatusTes(Long statusTesOrd) {
    //     result = new Result();
    //     try {
    //         Map items = new HashMap();
    //         Peserta peserta = new Peserta();
    //         if (statusTesOrd == 0) {
    //             peserta.setStatusTes(EnumStatusTes.MELAKSANAKANTES);
    //             peserta.setStatusPeserta(EnumStatusPeserta.CALON);
    //             Example<Peserta> example = Example.of(peserta);
    //             items.put("items", pesertaRepository.findAll(example, Sort.by(Sort.Direction.ASC, "id")));
    //             result.setData(items);
    //         } else if (statusTesOrd == 1) {
    //             peserta.setStatusTes(EnumStatusTes.MENUNGGUFOLLOWUP);
    //             peserta.setStatusPeserta(EnumStatusPeserta.CALON);
    //             Example<Peserta> example = Example.of(peserta);
    //             items.put("items", pesertaRepository.findAll(example, Sort.by(Sort.Direction.ASC, "id")));
    //             result.setData(items);
    //         } else if (statusTesOrd == 2) {
    //             peserta.setStatusTes(EnumStatusTes.LULUS);
    //             peserta.setStatusPeserta(EnumStatusPeserta.CALON);
    //             Example<Peserta> example = Example.of(peserta);
    //             items.put("items", pesertaRepository.findAll(example, Sort.by(Sort.Direction.ASC, "id")));
    //             result.setData(items);
    //         } else {
    //             result.setMessage(
    //                     "Error: gunakan 0 untuk Melaksanakan Tes, 1 untuk Menunggu Follow Up dan 2 untuk Lulus");
    //             result.setCode(HttpStatus.BAD_REQUEST.value());
    //             return result;
    //         }
    //     } catch (Exception e) {
    //         logger.error(stringUtil.getError(e));
    //     }
    //     return result;
    // }

    @Override
    public Result searchCalonPeserta(String keyword) {
        result = new Result();
        try {
            Map items = new HashMap();

        List<Peserta> searchCalon = pesertaRepository.search(keyword.toLowerCase(), EnumStatusPeserta.CALON)
            .stream()
            .collect(Collectors.toList());
         if(searchCalon.size() == 0 || searchCalon == null || searchCalon.isEmpty()) {
            result.setSuccess(false);
            result.setMessage("keyword not found");
            result.setCode(HttpStatus.BAD_REQUEST.value());
        }
        else {
            items.put("items", pesertaRepository.search(keyword.toLowerCase(), EnumStatusPeserta.CALON));
            result.setData(items);    
        }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result sortAndPaging(Integer page, Integer size, Boolean ascending) {
        result = new Result();
        try {
            Map items = new HashMap();
            Peserta calonPeserta = new Peserta();
            calonPeserta.setStatusPeserta(EnumStatusPeserta.CALON);
            Example<Peserta> example = Example.of(calonPeserta);
            if (ascending) {
                Page<Peserta> pagePeserta = pesertaRepository.findAll(example,
                        PageRequest.of(page, size, Sort.by("id").ascending()));
                items.put("items", pagePeserta);
            } else {
                Page<Peserta> pagePeserta = pesertaRepository.findAll(example,
                        PageRequest.of(page, size, Sort.by("id").descending()));
                items.put("items", pagePeserta);
            }
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public ResponseEntity<?> setUploadImagePath(long id, MultipartFile uploadImagePath, String uri) {
        result = new Result();
        try {
            String filename = String.valueOf("upload_image_" + id).concat(".")
                    .concat(globalUtil.getExtensionByStringHandling(uploadImagePath.getOriginalFilename()).orElse(""));
            String filenameResult = storageService.save(uploadImagePath, filename);
            pesertaRepository.setUploadImagePath(filenameResult, id);
            result.setMessage("succes to save file ".concat(uploadImagePath.getOriginalFilename()));
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
