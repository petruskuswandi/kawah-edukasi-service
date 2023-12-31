package id.kedukasi.core.controller;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.service.WilayahService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping("/wilayah")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
public class WilayahController {

    @Autowired
    WilayahService service;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    HttpServletRequest request;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/provinsi/all", produces = APPLICATION_JSON_VALUE)
    public Result allProvinsi() {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllProvinsi(uri);
    }

    @GetMapping(value = "/provinsi/{provinsiId}", produces = APPLICATION_JSON_VALUE)
    public Result getProvinsiByID(@PathVariable("provinsiId") Long provinsiId) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getProvinsiByID(provinsiId, uri);
    }

    @GetMapping(value = "/provinsi/{provinsiId}/kota/all", produces = APPLICATION_JSON_VALUE)
    public Result getAllKotaInProvinsi(@PathVariable("provinsiId") Long provinsiId) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllKotaInProvinsi(provinsiId, uri);
    }

    @GetMapping(value = "/kota/{kotaId}", produces = APPLICATION_JSON_VALUE)
    public Result getKotaById(@PathVariable("kotaId") Long kotaId) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getKotaById(kotaId, uri);
    }

    @GetMapping(value = "/kota/{kotaId}/kecamatan/all", produces = APPLICATION_JSON_VALUE)
    public Result getAllKecamatanInKota(@PathVariable("kotaId") Long kotaId) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllKecamatanInKota(kotaId, uri);
    }

    @GetMapping(value = "/kecamatan/{kecamatanId}", produces = APPLICATION_JSON_VALUE)
    public Result getkecamatanById(@PathVariable("kecamatanId") Long kecamatanId) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getkecamatanById(kecamatanId, uri);
    }

    @GetMapping(value = "/kecamatan/{kecamatanId}/kelurahan/all", produces = APPLICATION_JSON_VALUE)
    public Result getAllKelurahanInKecamatan(@PathVariable("kecamatanId") Long kecamatanId) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllKelurahanInKecamatan(kecamatanId, uri);
    }

    @GetMapping(value = "/kelurahan/{kelurahanId}", produces = APPLICATION_JSON_VALUE)
    public Result getKelurahanById(@PathVariable("kelurahanId") Long kelurahanId) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getKelurahanById(kelurahanId, uri);
    }
}
