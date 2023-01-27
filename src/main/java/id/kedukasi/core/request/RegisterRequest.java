package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;

public class RegisterRequest {
    @ApiModelProperty(example = "nama peserta", required = true)
    private String namaPeserta;
    @ApiModelProperty(example = "09/02/2000", required = true)
    //@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$", message = "Format Tgl Tidak Sesuai")
    private String tanggalLahir;
    @ApiModelProperty(example = "Alamat", required = true)
    private String alamatRumah;
    @ApiModelProperty(example = "082115294333", required = true)
    private String noHp;
    @ApiModelProperty(example = "11", required = true)
    private Long provinsi;
    @ApiModelProperty(example = "1101", required = true)
    private Long kota;
    @ApiModelProperty(example = "1101010", required = true)
    private Long kecamatan;
    @ApiModelProperty(example = "1101010001", required = true)
    private Long kelurahan;

    @ApiModelProperty(example = "1", required = true)
    private Long batch;

    @ApiModelProperty(example = "1", required = true)
    private Long class_id;

    @ApiModelProperty(example = "udindev@gmail.com", required = true)
    private String email;
    @ApiModelProperty(example = "true", required = true)
    private boolean statTwibbon;

    @ApiModelProperty(example = "", required = true)
    private String linkTwiitbon;
    @ApiModelProperty(example = "1", required = true)
    private Integer pemrograman;
    private String keteranganPemrograman;
    private boolean statBootcamp;
    private String namaBootcamp;

    @ApiModelProperty(example = "Universitas Teknologi Bandung", required = true)
    private String sekolahUniversitas;

    @ApiModelProperty(example = "Manajemen Informatika", required = true)
    private String jurusan;

    @ApiModelProperty(example = "2000", required = true)
    private String tahunLulus;


    @ApiModelProperty(example = "@userinstagram", required = true)
    private String userInstagram;

    @ApiModelProperty(example = "alasan", required = true)
    private String alasan;

    @ApiModelProperty(example = "kelebihan / kekurangan", required = true)
    private String kelebihanKekurangan;

    @ApiModelProperty(example = "1", required = true)
    private Integer kesibukan;
    private boolean laptop;
    private boolean komitmen;
    private boolean siapBekerja;


    public Long getClass_id() {
        return class_id;
    }

    public void setClass_id(Long class_id) {
        this.class_id = class_id;
    }

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public String getLinkTwiitbon() {
        return linkTwiitbon;
    }

    public void setLinkTwiitbon(String linkTwiitbon) {
        this.linkTwiitbon = linkTwiitbon;
    }

    public Long getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(Long provinsi) {
        this.provinsi = provinsi;
    }

    public Long getKota() {
        return kota;
    }

    public void setKota(Long kota) {
        this.kota = kota;
    }

    public Long getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(Long kecamatan) {
        this.kecamatan = kecamatan;
    }

    public Long getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(Long kelurahan) {
        this.kelurahan = kelurahan;
    }

    public String getNamaPeserta() {
        return namaPeserta;
    }

    public void setNamaPeserta(String namaPeserta) {
        this.namaPeserta = namaPeserta;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getAlamatRumah() {
        return alamatRumah;
    }

    public void setAlamatRumah(String alamatRumah) {
        this.alamatRumah = alamatRumah;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }


    public boolean isStatBootcamp() {
        return statBootcamp;
    }

    public void setStatBootcamp(boolean statBootcamp) {
        this.statBootcamp = statBootcamp;
    }

    public String getNamaBootcamp() {
        return namaBootcamp;
    }

    public void setNamaBootcamp(String namaBootcamp) {
        this.namaBootcamp = namaBootcamp;
    }

    public String getSekolahUniversitas() {
        return sekolahUniversitas;
    }

    public void setSekolahUniversitas(String sekolahUniversitas) {
        this.sekolahUniversitas = sekolahUniversitas;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public String getTahunLulus() {
        return tahunLulus;
    }

    public void setTahunLulus(String tahunLulus) {
        this.tahunLulus = tahunLulus;
    }

    public String getUserInstagram() {
        return userInstagram;
    }

    public void setUserInstagram(String userInstagram) {
        this.userInstagram = userInstagram;
    }

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }

    public String getKelebihanKekurangan() {
        return kelebihanKekurangan;
    }

    public void setKelebihanKekurangan(String kelebihanKekurangan) {
        this.kelebihanKekurangan = kelebihanKekurangan;
    }

    public Integer getKesibukan() {
        return kesibukan;
    }

    public void setKesibukan(Integer kesibukan) {
        this.kesibukan = kesibukan;
    }

    public boolean isLaptop() {
        return laptop;
    }

    public void setLaptop(boolean laptop) {
        this.laptop = laptop;
    }

    public boolean isKomitmen() {
        return komitmen;
    }

    public void setKomitmen(boolean komitmen) {
        this.komitmen = komitmen;
    }

    public boolean isSiapBekerja() {
        return siapBekerja;
    }

    public void setSiapBekerja(boolean siapBekerja) {
        this.siapBekerja = siapBekerja;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStatTwibbon() {
        return statTwibbon;
    }

    public void setStatTwibbon(boolean statTwibbon) {
        this.statTwibbon = statTwibbon;
    }

    public Integer getPemrograman() {
        return pemrograman;
    }

    public void setPemrograman(Integer pemrograman) {
        this.pemrograman = pemrograman;
    }

    public String getKeteranganPemrograman() {
        return keteranganPemrograman;
    }

    public void setKeteranganPemrograman(String keteranganPemrograman) {
        this.keteranganPemrograman = keteranganPemrograman;
    }
}
