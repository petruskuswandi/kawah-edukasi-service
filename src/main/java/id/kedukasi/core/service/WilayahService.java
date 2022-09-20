package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;

public interface WilayahService {
    Result getAllProvinsi(String uri);

    Result getProvinsiByID(Integer provinsiId, String uri);

    Result getAllKotaInProvinsi(Integer provinsiId, String uri);

    Result getKotaById(Integer kotaId, String uri);

    Result getAllKecamatanInKota(Integer kotaId, String uri);

    Result getkecamatanById(Integer kecamatanId, String uri);

    Result getAllKelurahanInKecamatan(Integer kecamatanId, String uri);

    Result getKelurahanById(Integer kelurahanId, String uri);
}
