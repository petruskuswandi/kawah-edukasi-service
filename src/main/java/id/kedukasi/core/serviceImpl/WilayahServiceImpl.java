package id.kedukasi.core.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.wilayah.Kecamatan;
import id.kedukasi.core.models.wilayah.Kelurahan;
import id.kedukasi.core.models.wilayah.Kota;
import id.kedukasi.core.models.wilayah.Provinsi;
import id.kedukasi.core.repository.wilayah.KecamatanRepository;
import id.kedukasi.core.repository.wilayah.KelurahanRepository;
import id.kedukasi.core.repository.wilayah.KotaRepository;
import id.kedukasi.core.repository.wilayah.ProvinsiRepository;
import id.kedukasi.core.service.WilayahService;
import id.kedukasi.core.utils.StringUtil;

@Service
public class WilayahServiceImpl implements WilayahService{
    
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
    public Result getProvinsiByID(Long provinsiId, String uri) {
        result = new Result();
        try {
            Provinsi provinsi = provinsiRepository.findById(provinsiId).get();
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
    public Result getAllKotaInProvinsi(Long provinsiId, String uri) {
        result = new Result();
        try {
            Map items = new HashMap();
            Kota kota = new Kota();
            kota.setProvince_id(provinsiId);;
            Example<Kota> example = Example.of(kota);
            items.put("items", kotaRepository.findAll(example));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getKotaById(Long kotaId, String uri) {
        result = new Result();
        try {
            Kota kota = kotaRepository.findById(kotaId).get();
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
    public Result getAllKecamatanInKota(Long kotaId, String uri) {
        result = new Result();
        try {
            Map items = new HashMap();
            Kecamatan kecamatan = new Kecamatan();
            kecamatan.setKota_id(kotaId);
            Example<Kecamatan> example = Example.of(kecamatan);
            items.put("items", kecamatanRepository.findAll(example));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getkecamatanById(Long kecamatanId, String uri) {
        result = new Result();
        try {
            Kecamatan kecamatan = kecamatanRepository.findById(kecamatanId).get();
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
    public Result getAllKelurahanInKecamatan(Long kecamatanId, String uri) {
        result = new Result();
        try {
            Map items = new HashMap();
            Kelurahan kelurahan = new Kelurahan();
            kelurahan.setKecamatan_id(kecamatanId);
            Example<Kelurahan> example = Example.of(kelurahan);
            items.put("items", kelurahanRepository.findAll(example));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getKelurahanById(Long kelurahanId, String uri) {
        result = new Result();
        try {
            Kelurahan kelurahan = kelurahanRepository.findById(kelurahanId).get();
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
