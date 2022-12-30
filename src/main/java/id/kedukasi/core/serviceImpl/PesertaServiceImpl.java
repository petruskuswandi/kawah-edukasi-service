package id.kedukasi.core.serviceImpl;

import com.google.gson.Gson;
import com.lowagie.text.DocumentException;
import id.kedukasi.core.enums.EnumStatusPeserta;
import id.kedukasi.core.models.*;
import id.kedukasi.core.models.wilayah.MasterKecamatan;
import id.kedukasi.core.models.wilayah.MasterKelurahan;
import id.kedukasi.core.models.wilayah.MasterKota;
import id.kedukasi.core.models.wilayah.MasterProvinsi;
import id.kedukasi.core.repository.*;
import id.kedukasi.core.repository.wilayah.KecamatanRepository;
import id.kedukasi.core.repository.wilayah.KelurahanRepository;
import id.kedukasi.core.repository.wilayah.KotaRepository;
import id.kedukasi.core.repository.wilayah.ProvinsiRepository;
import id.kedukasi.core.request.RegisterRequest;
import id.kedukasi.core.service.EmailService;
import id.kedukasi.core.service.PesertaService;
import id.kedukasi.core.utils.StringUtil;
import id.kedukasi.core.utils.UploadUtil;
import id.kedukasi.core.utils.ValidatorUtil;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PesertaServiceImpl implements PesertaService {
    @Value("${app.upload-file-path}")
    private String pathUpload;

    @Autowired
    PesertaRepository pesertaRepository;

    @Autowired
    KelasRepository kelasRepository;

    @Autowired
    BatchRepository batchRepository;

    @Autowired
    ProvinsiRepository provinsiRepository;

    @Autowired
    EducationRepository educationRepository;

    @Autowired
    KotaRepository kotaRepository;

    @Autowired
    KecamatanRepository kecamatanRepository;

    @Autowired
    KelurahanRepository kelurahanRepository;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    ValidatorUtil validator;

    @Autowired
    EmailService emailService;

    private Result result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    StatusRepository statusRepository;

    @Autowired
    EntityManager em;



    // @Override
    // @Transactional
    // public Result getAllPeserta(String uri) {
    // result = new Result();
    // try {
    // Map items = new HashMap();
    // Peserta peserta = new Peserta();
    // peserta.setStatusPeserta(EnumStatusPeserta.PESERTA);
    // peserta.setBanned(false);
    // Example<Peserta> example = Example.of(peserta);
    // items.put("items",
    // pesertaRepository.findAll(example,Sort.by(Sort.Direction.ASC,"id")));
    // result.setData(items);
    // } catch (Exception e) {
    // logger.error(stringUtil.getError(e));
    // }
    // return result;
    // }
    @Override
    @Transactional
    public Result getAllPeserta(String uri, String search, long limit, long offset) {
        result = new Result();
        // default value search param
        if (search == null) {
            search = "";
        }
        // null long condition
        if (limit == -99) {
            limit = pesertaRepository.count();
        }
        // null long condition
        if (offset == -99) {
            offset = 0;
        }
        StringBuilder sb = new StringBuilder();
        try {
            Map items = new HashMap();
            List<Peserta> peserta = pesertaRepository.getAllPagination(EnumStatusPeserta.PESERTA.toString(), false,
                    search, limit, offset);
            items.put("items", peserta);
            items.put("totalDataResult", peserta.size());
            items.put("totalData", pesertaRepository.getCountByStatus(EnumStatusPeserta.PESERTA.toString()));

            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getAllBannedPeserta(String uri) {
        result = new Result();
        try {
            Map items = new HashMap();
            Peserta peserta = new Peserta();
            peserta.setStatusPeserta(EnumStatusPeserta.PESERTA);
            peserta.setBanned(true);
            Example<Peserta> example = Example.of(peserta);
            items.put("items", pesertaRepository.findAll(example, Sort.by(Sort.Direction.ASC, "id")));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getPesertaById(Long id, String uri) {
        result = new Result();
        try {
            if (!pesertaRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada peserta dengan id: " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (pesertaRepository.findById(id).get().getStatusPeserta().equals(EnumStatusPeserta.CALON)) {
                result.setSuccess(false);
                result.setMessage("Error: id " + id + " bukan calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (pesertaRepository.findById(id).get().isBanned()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada peserta dengan id: " + id);
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

    public class SetPenambahanData {
        private String namaProgram;
        private String namaProvinsi;
        private String namaKota;
        private String namaKecamatan;
        private String namaKelurahan;
        private String laptop;
        private String komitmen;
        private String siapBekerja;
        private String tgllahir;

        private String namaBatch;
        private String kesibukan;

        public String getKesibukan() {
            return kesibukan;
        }

        public void setKesibukan(String kesibukan) {
            this.kesibukan = kesibukan;
        }

        public String getNamaBatch() {
            return namaBatch;
        }

        public void setNamaBatch(String namaBatch) {
            this.namaBatch = namaBatch;
        }

        public String getTgllahir() {
            return tgllahir;
        }

        public void setTgllahir(String tgllahir) {
            this.tgllahir = tgllahir;
        }

        public String getLaptop() {
            return laptop;
        }

        public void setLaptop(String laptop) {
            this.laptop = laptop;
        }

        public String getKomitmen() {
            return komitmen;
        }

        public void setKomitmen(String komitmen) {
            this.komitmen = komitmen;
        }

        public String getSiapBekerja() {
            return siapBekerja;
        }

        public void setSiapBekerja(String siapBekerja) {
            this.siapBekerja = siapBekerja;
        }

        public SetPenambahanData() {
        }

        public String getNamaProgram() {
            return namaProgram;
        }

        public void setNamaProgram(String namaProgram) {
            this.namaProgram = namaProgram;
        }

        public String getNamaProvinsi() {
            return namaProvinsi;
        }

        public void setNamaProvinsi(String namaProvinsi) {
            this.namaProvinsi = namaProvinsi;
        }

        public String getNamaKota() {
            return namaKota;
        }

        public void setNamaKota(String namaKota) {
            this.namaKota = namaKota;
        }

        public String getNamaKecamatan() {
            return namaKecamatan;
        }

        public void setNamaKecamatan(String namaKecamatan) {
            this.namaKecamatan = namaKecamatan;
        }

        public String getNamaKelurahan() {
            return namaKelurahan;
        }

        public void setNamaKelurahan(String namaKelurahan) {
            this.namaKelurahan = namaKelurahan;
        }
    }

    @Override
    public ResponseEntity<Result> registerPeserta(RegisterRequest old, String jsonString, List<MultipartFile> files)
            throws ParseException, MessagingException, IOException, DocumentException {

        Map<String, String> keteranganLain = new HashMap<>();

        SetPenambahanData setPenambahanData = new SetPenambahanData();

        // Peserta peserta = new Peserta();

        Peserta registerPeserta = new Peserta();
        result = new Result();
        result.setMessage("ok");

        Gson g = new Gson();
        RegisterRequest p = g.fromJson(jsonString, RegisterRequest.class);

        // cek email
        Peserta checkEmailPeserta = pesertaRepository.findByEmail(p.getEmail()).orElse(new Peserta());
        if (checkEmailPeserta.getEmail() != null) {
            result.setMessage("Error: Email sudah digunakan!");
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity
                    .badRequest()
                    .body(result);
        }

        Peserta checkNoHpPeserta = pesertaRepository.findByNoHp(p.getNoHp()).orElse(new Peserta());
        if (checkNoHpPeserta.getNoHp() != null) {
            result.setMessage("Error: No HP sudah digunakan!");
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity
                    .badRequest()
                    .body(result);
        }

        if (files.size() == 0) {
            result.setMessage("Error: Bad Request untuk File Upload");
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(result);
        }

        if (files.size() != 3) {
            result.setMessage("Error: Bad Request untuk File Upload, Tidak Boleh Lebih dari 3 dan kurang dari 3");
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(result);
        }

        // check status
        Optional<Status> statusPeserta = statusRepository.findBystatusName("REGISTER");
        if (!statusPeserta.isPresent()) {
            result.setMessage("Error: Status Belum Ada!");
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity
                    .badRequest()
                    .body(result);
        }

        Optional<Batch> batch = batchRepository.findById(p.getBatch());
        if (!batch.isPresent()) {
            result.setSuccess(false);
            result.setMessage("Error: Tidak ada program dengan id " + p.getBatch());
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(result);
        } else {
            registerPeserta.setBatch(batch.get());
            setPenambahanData.setNamaBatch(batch.get().getBatchname());
        }


        Optional<MasterProvinsi> provinsi = provinsiRepository.findById(p.getProvinsi());
        // set provinsi
        if (!provinsi.isPresent()) {
            result.setSuccess(false);
            result.setMessage("Error: Tidak ada provinsi dengan id " + p.getProvinsi());
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity
                    .badRequest()
                    .body(result);
        } else {
            registerPeserta.setProvinsi(provinsi.get());
            setPenambahanData.setNamaProvinsi(provinsi.get().getName());
        }

        // set kota
        Optional<MasterKota> kota = kotaRepository.findById(p.getKota());
        if (!kota.isPresent()) {
            result.setSuccess(false);
            result.setMessage("Error: Tidak ada kota dengan id " + p.getKota());
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity
                    .badRequest()
                    .body(result);
        } else {
            registerPeserta.setKota(kota.get());
            setPenambahanData.setNamaKota(kota.get().getName());
        }

        // set kecamatan
        Optional<MasterKecamatan> kecamatan = kecamatanRepository.findById(p.getKecamatan());
        if (!kecamatan.isPresent()) {
            result.setSuccess(false);
            result.setMessage("Error: Tidak ada kecamatan dengan id " + p.getKecamatan());
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity
                    .badRequest()
                    .body(result);
        } else {
            registerPeserta.setKecamatan(kecamatan.get());
            setPenambahanData.setNamaKecamatan(kecamatan.get().getName());
        }

        // set kelurahan
        Optional<MasterKelurahan> kelurahan = kelurahanRepository.findById(p.getKelurahan());
        if (!kelurahan.isPresent()) {
            result.setSuccess(false);
            result.setMessage("Error: Tidak ada kelurahan dengan id " + p.getKelurahan());
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity
                    .badRequest()
                    .body(result);
        } else {
            registerPeserta.setKelurahan(kelurahan.get());
            setPenambahanData.setNamaKelurahan(kelurahan.get().getName());
        }

        if (p.getPemrograman() == 3) {
            if (p.getKeteranganPemrograman().isEmpty()) {
                result.setSuccess(false);
                result.setMessage("Error: Keterangan Pemrograman Tidak Boleh Kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }
        }

        if (p.isStatBootcamp() == true) {
            if (p.getNamaBootcamp().isEmpty()) {
                result.setSuccess(false);
                result.setMessage("Error: Nama Bootcamp Tidak Boleh Kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }
        }

        /**
         * 1. Kuliah/Sekolah
         * 2. kerja
         * 3. tidak keduanya
         */

        setPenambahanData.setKesibukan("tidak Keduanya");

        if (p.getKesibukan() == 1) {
            setPenambahanData.setKesibukan("Kuliah/Sekolah");
        } else if (p.getKesibukan() == 2) {
            setPenambahanData.setKesibukan("kerja");
        }

        setPenambahanData.setLaptop("Ya");
        setPenambahanData.setKomitmen("Ya");
        setPenambahanData.setSiapBekerja("Ya");

        if (p.isLaptop() == false) {
            setPenambahanData.setLaptop("Tidak");
        }

        if (p.isKomitmen() == false) {
            setPenambahanData.setKomitmen("Tidak");
        }

        if (p.isSiapBekerja() == false) {
            setPenambahanData.setSiapBekerja(
                    "tidak");
        }



        registerPeserta.setEmail(p.getEmail());
        registerPeserta.setStatTwibbon(p.isStatTwibbon());
        registerPeserta.setLinkTwiitbon(p.getLinkTwiitbon());
        registerPeserta.setPemrograman(p.getPemrograman());
        registerPeserta.setKeteranganPemrograman(p.getKeteranganPemrograman());
        registerPeserta.setStatBootcamp(p.isStatBootcamp());
        registerPeserta.setNamaBootcamp(p.getNamaBootcamp());

        /**
         * selanjut nya
         */

        registerPeserta.setNamaPeserta(p.getNamaPeserta());
        Date tanggalLahirTypeDate = new SimpleDateFormat("dd/MM/yyy").parse(p.getTanggalLahir());
        setPenambahanData.setTgllahir(p.getTanggalLahir());
        registerPeserta.setTanggalLahir(tanggalLahirTypeDate);
        registerPeserta.setAlamatRumah(p.getAlamatRumah());
        registerPeserta.setSekolahUniversitas(p.getSekolahUniversitas());
        registerPeserta.setJurusan(p.getJurusan());
        registerPeserta.setTahunLulus(p.getTahunLulus());
        registerPeserta.setNoHp(p.getNoHp());
        registerPeserta.setUserInstagram(p.getUserInstagram());
        registerPeserta.setAlasan(p.getAlasan());
        registerPeserta.setKelebihanKekurangan(p.getKelebihanKekurangan());
        registerPeserta.setKesibukan(p.getKesibukan());
        registerPeserta.setLaptop(p.isLaptop());
        registerPeserta.setKomitmen(p.isKomitmen());
        registerPeserta.setSiapBekerja(p.isSiapBekerja());
        registerPeserta.setStatusPeserta(EnumStatusPeserta.REGISTER);
        registerPeserta.setStatus(statusPeserta.get());


        Peserta pesertabaru = pesertaRepository.save(registerPeserta);
        Map<String, Object> dataPictures = new HashMap<>();
        Map<String, String> pictures = new HashMap<>();

        String pathfile = "/src/main/resources/static/upload.documents/";
        String id = String.valueOf(UUID.randomUUID());
        int[] idx = { 0 };
        files.forEach(file -> {
            idx[0]++;
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String extension = FilenameUtils.getExtension(fileName).toLowerCase();
            String key = UUID.randomUUID() + "." + extension;

            saveFile(file, registerPeserta, String.valueOf(pesertabaru.getId()), "REGISTER", idx, key, pathfile);
            pictures.put(String.valueOf(idx[0]), idx[0] + "_" + pesertabaru.getId() + "_" + "REGISTER" + "_" + key);

        });

        emailService.sendRegisterMail(pictures, setPenambahanData, registerPeserta, pathfile);

        result.setMessage("Registrasi Berhasil");
        result.setCode(HttpStatus.OK.value());
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<Result> saveFile(MultipartFile file, Peserta registerPeserta, String id, String action,
                                           int[] idx, String key, String pathfile) {
        result = new Result();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // FileUpload FileDB = null;

        try {
            String extension = FilenameUtils.getExtension(fileName).toLowerCase();
            // String key = UUID.randomUUID() + "." + extension;

            // FileDB = new FileUpload(registerPeserta,key, file.getContentType(),fileName,
            // file.getBytes(),
            // "REGISTER");

            Path currentPath = Paths.get(".");
            Path absolutePath = currentPath.toAbsolutePath();
            String setPath = absolutePath + pathfile;
            byte[] bytes = file.getBytes();
            Path path = Paths.get(setPath + idx[0] + "_"
                    + id + "_" + action + "_" + key);
            Files.write(path, bytes);
            // fileUploadRepository.save(FileDB);
        } catch (IOException e) {
            result.setMessage("Error: Bad Request Untuk File Upload!");
            result.setCode(HttpStatus.BAD_REQUEST.value());
            ResponseEntity
                    .badRequest()
                    .body(result);
        }

        return null;
    }

    @Override
    public ResponseEntity<?> updatePeserta(Long id, Long kelasId, Long batchId, String namaPeserta, String tanggalLahir,
                                           String jenisKelamin, String pendidikanTerakhir, String noHp, String email,
                                           MultipartFile uploadImage, Long provinsiId, Long kotaId, Long kecamatanId,
                                           Long kelurahanId, String alamatRumah, String motivasi, String kodeReferal, String nomorKtp,
                                           MultipartFile uploadCv, Integer kesibukan, Integer scoreTetsAwal, Integer scoreTestAkhir,
                                           Integer status, String namaProject, String jurusan) {

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
            // cek username
            Peserta checkNamaPeserta = pesertaRepository.findByNamaPeserta(namaPeserta).orElse(new Peserta());
            if (checkNamaPeserta.getNamaPeserta() != null && !Objects.equals(id, checkNamaPeserta.getId())) {
                result.setMessage("Error: Username sudah digunakan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
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
            if (pendidikanTerakhir.isBlank()) {
                result.setMessage("Error: Pendidikan terakhir tidak boleh kosong");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
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
            // cek status peserta
            Peserta checkStatusPeserta = pesertaRepository.findById(id).orElse(new Peserta());
            if (checkStatusPeserta.getStatusPeserta() != null
                    && !Objects.equals(EnumStatusPeserta.PESERTA, checkStatusPeserta.getStatusPeserta())) {
                result.setMessage("Error: id: " + id + " bukan Peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            Peserta peserta = new Peserta(namaPeserta, tanggalLahirTypeDate, jenisKelamin, pendidikanTerakhir, noHp,email,alamatRumah, motivasi, kodeReferal, nomorKtp);

            peserta.setId(id);
            peserta.setStatusPeserta(EnumStatusPeserta.PESERTA);

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

            // set image
            if (uploadImage != null) {
                // peserta.setUploadImagePath(IOUtils.toByteArray(uploadImage.getInputStream()));
                String nameImage = StringUtils.cleanPath(uploadImage.getOriginalFilename());
                peserta.setUploadImageName(nameImage);
                String[] image = nameImage.split("\\.");
                String format = image[image.length-1];
                if (!format.equalsIgnoreCase("jpg")&&!format.equalsIgnoreCase("png")) {
                    result.setSuccess(false);
                    result.setMessage("Error: File Image harus format jpg atau png");
                    result.setCode(HttpStatus.BAD_REQUEST.value());
                    return ResponseEntity
                            .badRequest()
                            .body(result);
                }
                //save Image
                UploadUtil.saveImage(pathUpload, namaPeserta, uploadImage, nomorKtp, peserta, format);

            }
            // set cv
            if (uploadCv != null) {
                //get original name
                String nameCV = StringUtils.cleanPath(uploadCv.getOriginalFilename());
                //proses validasi file pdf
                String[] name = nameCV.split("\\.");
                if (!name[name.length-1].equals("pdf")){
                    result.setSuccess(false);
                    result.setMessage("Error: File CV harus format pdf");
                    result.setCode(HttpStatus.BAD_REQUEST.value());
                    return ResponseEntity
                            .badRequest()
                            .body(result);
                }
                //save CV
                UploadUtil.saveCV(pathUpload, namaPeserta, nomorKtp, uploadCv, peserta);
            }
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
            // if (!statusRepository.findById(status){

            // }
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


            peserta.setScoreTetsAwal(scoreTetsAwal);
            peserta.setScoreTestAkhir(scoreTestAkhir);
            // peserta.setStatusTes(statusTes);
            peserta.setNamaProject(namaProject);
            peserta.setJurusan(jurusan);
            // peserta.setUploadCv(uploadCv

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


            pesertaRepository.save(peserta);

            result.setMessage(id == 0 ? "Berhasil membuat peserta baru!" : "Berhasil memperbarui peserta!");
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
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> deletePeserta(boolean banned, Long id, String uri) {
        result = new Result();
        try {
            if (!pesertaRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada calon peserta dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (pesertaRepository.findById(id).get().getStatusPeserta().equals(EnumStatusPeserta.CALON)) {
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
    public ResponseEntity<?> changeToCalonPeserta(Long id, String uri) {
        result = new Result();
        try {
            if (!pesertaRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada calon peserta dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (pesertaRepository.findById(id).get().getStatusPeserta().equals(EnumStatusPeserta.CALON)) {
                result.setSuccess(false);
                result.setMessage("Error: id " + id + " bukan calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                pesertaRepository.statusPeserta(EnumStatusPeserta.CALON, id);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> changeKelas(Long pesertaId, Long kelasId, String uri) {
        result = new Result();
        try {
            Peserta peserta = pesertaRepository.findById(pesertaId).get();
            Kelas kelas = kelasRepository.findById(kelasId).get();
            if (!pesertaRepository.findById(pesertaId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada peserta dengan id " + pesertaId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (!kelasRepository.findById(kelasId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada kelas dengan id " + kelasId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (peserta.getStatusPeserta().equals(EnumStatusPeserta.CALON)) {
                result.setSuccess(false);
                result.setMessage("Error: id " + pesertaId + " bukan calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                // peserta.setKelas(kelas);
                pesertaRepository.save(peserta);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public Result searchPeserta(String keyword) {
        result = new Result();
        try {
            Map items = new HashMap();
            items.put("items", pesertaRepository.search(keyword, EnumStatusPeserta.PESERTA));
            result.setData(items);
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
            Peserta peserta = new Peserta();
            peserta.setStatusPeserta(EnumStatusPeserta.PESERTA);
            Example<Peserta> example = Example.of(peserta);
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
}
