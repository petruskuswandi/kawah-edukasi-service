package id.kedukasi.core.controller;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.service.PesertaService;
import id.kedukasi.core.service.RolesService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping("/roles")
public class RolesController {
    @Autowired
    RolesService service;

    @Autowired
    HttpServletRequest request;

    @Autowired
    StringUtil stringUtil;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> getAllRoles() {
        String uri = stringUtil.getLogParam(request);
        logger.info(uri);
        return service.getAllRoles(uri);
    }

}
