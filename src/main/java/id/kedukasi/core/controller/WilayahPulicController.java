package id.kedukasi.core.controller;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.service.WilayahService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping("/publicwilayah")
public class WilayahPulicController {

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

    @GetMapping(value = "/publickota/{provinsiId}", produces = APPLICATION_JSON_VALUE)
    public Result getAllKotaInProvinsi(@PathVariable("provinsiId") Long provinsiId) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllKotaInProvinsi(provinsiId, uri);
    }

    @GetMapping(value = "/kecamatan/{kotaId}", produces = APPLICATION_JSON_VALUE)
    public Result getAllKecamatanInKota(@PathVariable("kotaId") Long kotaId) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllKecamatanInKota(kotaId, uri);
    }

    @GetMapping(value = "/kelurahan/{kecamatanId}", produces = APPLICATION_JSON_VALUE)
    public Result getAllKelurahanInKecamatan(@PathVariable("kecamatanId") Long kecamatanId) {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllKelurahanInKecamatan(kecamatanId, uri);
    }

}
