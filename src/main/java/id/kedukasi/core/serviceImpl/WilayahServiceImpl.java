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
import org.springframework.data.domain.Sort;
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
            items.put("items", provinsiRepository.findAll(Sort.by(Sort.Direction.ASC,"id")));
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
            if (!provinsiRepository.findById(provinsiId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada provinsi dengan id " + provinsiId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map items = new HashMap();
                items.put("items", provinsiRepository.findById(provinsiId).get());
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
            MasterKota kota = new MasterKota();
            kota.setProvince_id(provinsiId);
            Example<MasterKota> example = Example.of(kota);
            items.put("items", kotaRepository.findAll(example,Sort.by(Sort.Direction.ASC,"id")));
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
            if (!kotaRepository.findById(kotaId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada kota dengan id " + kotaId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map items = new HashMap();
                items.put("items", kotaRepository.findById(kotaId).get());
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
            MasterKecamatan kecamatan = new MasterKecamatan();
            kecamatan.setKota_id(kotaId);
            Example<MasterKecamatan> example = Example.of(kecamatan);
            items.put("items", kecamatanRepository.findAll(example,Sort.by(Sort.Direction.ASC,"id")));
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
            if (!kecamatanRepository.findById(kecamatanId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada kecamatan dengan id " + kecamatanId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map items = new HashMap();
                items.put("items", kecamatanRepository.findById(kecamatanId).get());
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
            MasterKelurahan kelurahan = new MasterKelurahan();
            kelurahan.setKecamatan_id(kecamatanId);
            Example<MasterKelurahan> example = Example.of(kelurahan);
            items.put("items", kelurahanRepository.findAll(example,Sort.by(Sort.Direction.ASC,"id")));
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
            if (!kelurahanRepository.findById(kelurahanId).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada kelurahan dengan id " + kelurahanId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map items = new HashMap();
                items.put("items", kelurahanRepository.findById(kelurahanId).get());
                result.setData(items);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }
}
