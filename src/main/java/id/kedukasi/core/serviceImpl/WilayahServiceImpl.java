package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.wilayah.MasterKecamatan;
import id.kedukasi.core.models.wilayah.MasterKelurahan;
import id.kedukasi.core.models.wilayah.MasterKota;
import id.kedukasi.core.models.wilayah.MasterProvinsi;
import id.kedukasi.core.repository.wilayah.KecamatanRepository;
import id.kedukasi.core.repository.wilayah.KelurahanRepository;
import id.kedukasi.core.repository.wilayah.KotaRepository;
import id.kedukasi.core.repository.wilayah.ProvinsiRepository;
import id.kedukasi.core.service.WilayahService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WilayahServiceImpl implements WilayahService {
    @Autowired
    ProvinsiRepository provinsiRepository;

    @Autowired
    KotaRepository kotaRepository;

    @Autowired
    KecamatanRepository kecamatanRepository;

    @Autowired
    KelurahanRepository kelurahanRepository;

    @Autowired
    StringUtil stringUtil;

    private Result result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Result getAllProvinsi(String uri) {
        result = new Result();
        try {
            Map items = new HashMap();
            items.put("items", provinsiRepository.findAll());
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getProvinsiByID(Integer provinsiId, String uri) {
        result = new Result();
        try {
            MasterProvinsi provinsi = provinsiRepository.findById(provinsiId).get();
            if (provinsi == null) {
                result.setSuccess(false);
                result.setMessage("cannot find provinsi");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map items = new HashMap();
                items.put("items", provinsi);
                result.setData(items);
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return result;
    }

    @Override
    public Result getAllKotaInProvinsi(Integer provinsiId, String uri) {
        result = new Result();
        try {
            Map items = new HashMap();
            MasterKota kota = new MasterKota();
            kota.setProvince_id(provinsiId);
            Example<MasterKota> example = Example.of(kota);
            items.put("items", kotaRepository.findAll(example));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getKotaById(Integer kotaId, String uri) {
        result = new Result();
        try {
            MasterKota kota = kotaRepository.findById(kotaId).get();
            if (kota == null) {
                result.setSuccess(false);
                result.setMessage("cannot find provinsi");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map items = new HashMap();
                items.put("items", kota);
                result.setData(items);
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return result;
    }

    @Override
    public Result getAllKecamatanInKota(Integer kotaId, String uri) {
        result = new Result();
        try {
            Map items = new HashMap();
            MasterKecamatan kecamatan = new MasterKecamatan();
            kecamatan.setKota_id(kotaId);
            Example<MasterKecamatan> example = Example.of(kecamatan);
            items.put("items", kecamatanRepository.findAll(example));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getkecamatanById(Integer kecamatanId, String uri) {
        result = new Result();
        try {
            MasterKecamatan kecamatan = kecamatanRepository.findById(kecamatanId).get();
            if (kecamatan == null) {
                result.setSuccess(false);
                result.setMessage("cannot find provinsi");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map items = new HashMap();
                items.put("items", kecamatan);
                result.setData(items);
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getAllKelurahanInKecamatan(Integer kecamatanId, String uri) {
        result = new Result();
        try {
            Map items = new HashMap();
            MasterKelurahan kelurahan = new MasterKelurahan();
            kelurahan.setKecamatan_id(kecamatanId);
            Example<MasterKelurahan> example = Example.of(kelurahan);
            items.put("items", kelurahanRepository.findAll(example));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getKelurahanById(Integer kelurahanId, String uri) {
        result = new Result();
        try {
            MasterKelurahan kelurahan = kelurahanRepository.findById(kelurahanId).get();
            if (kelurahan == null) {
                result.setSuccess(false);
                result.setMessage("cannot find provinsi");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map items = new HashMap();
                items.put("items", kelurahan);
                result.setData(items);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }
}
