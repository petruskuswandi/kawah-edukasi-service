package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.ProgramRequest;
import id.kedukasi.core.request.UpdateProgramRequest;
import org.springframework.http.ResponseEntity;
public interface ProgramService {
    //implements create program
    ResponseEntity<Result> createProgram(ProgramRequest program);

    //implements update program
    ResponseEntity<Result> updateProgram(UpdateProgramRequest program);

    //implements get all program
    Result getAllProgram();

    //implements get by program id
    Result getProgramById(Long id);
}
