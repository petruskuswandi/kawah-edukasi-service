package id.kedukasi.core.controller;

import com.google.gson.Gson;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.ProgramRequest;
import id.kedukasi.core.request.UpdateProgramRequest;
import id.kedukasi.core.service.ProgramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping("/program")
public class ProgramController {

    @Autowired
    ProgramService service;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping(value = "/uploadfile-object", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFileObject(
            @Valid @RequestPart String jsonString,
            @Valid @RequestPart List<MultipartFile> files) {

        Gson g = new Gson();
        ProgramRequest p = g.fromJson(jsonString, ProgramRequest.class);

    }

    @PostMapping("/create")
    public ResponseEntity<Result> createProgram(@Valid @RequestBody ProgramRequest program) {
        return service.createProgram(program);
    }

    @PutMapping("/update")
    public ResponseEntity<Result> updateProgram(@Valid @RequestBody UpdateProgramRequest program) {
        return service.updateProgram(program);
    }

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public Result getAllProgram() {
        return service.getAllProgram();
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Result getProgramById(@PathVariable("id") Long id) {
        return service.getProgramById(id);
    }

}
