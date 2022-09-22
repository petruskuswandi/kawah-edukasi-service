package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;

public interface WilayahService {
    Result getAllProvinsi(String uri);

    Result getProvinsiByID(Long provinsiId, String uri);

    Result getAllKotaInProvinsi(Long provinsiId, String uri);

    Result getKotaById(Long kotaId, String uri);

    Result getAllKecamatanInKota(Long kotaId, String uri);

    Result getkecamatanById(Long kecamatanId, String uri);

    Result getAllKelurahanInKecamatan(Long kecamatanId, String uri);

    Result getKelurahanById(Long kelurahanId, String uri);
}
