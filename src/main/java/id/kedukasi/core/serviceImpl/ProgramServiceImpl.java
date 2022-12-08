package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.models.Program;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.repository.ProgramRepository;
import id.kedukasi.core.request.ProgramRequest;
import id.kedukasi.core.request.UpdateProgramRequest;
import id.kedukasi.core.service.ProgramService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class ProgramServiceImpl implements ProgramService {
    @Autowired
    StringUtil stringUtil;

    private Result result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProgramRepository programRepository;
    @Override
    public ResponseEntity<Result> createProgram(ProgramRequest program) {
        result = new Result();
        try {
            int programName = programRepository.findProgramname(program.getProgram_name().toLowerCase());


            if (programName > 0) {
                result.setMessage("Error: Nama Program Telah Ada!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if(program.getDescription().length()>50 || program.getDescription().isBlank()  || program.getDescription().isEmpty()) {
                result.setMessage("Error: Deskripsi Program tidak boleh kosong dan harus kurang dari 50 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(program.getProgram_name().length()>30 || program.getProgram_name().isBlank() || program.getProgram_name().isEmpty()) {
                result.setMessage("Error: Nama Program tidak boleh kosong dan harus kurang dari 30 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            Program newProgram = new Program(program.getProgram_name(), program.getDescription(), false);

            programRepository.save(newProgram);

            result.setMessage("Berhasil membuat program baru!");
            result.setCode(HttpStatus.OK.value());

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity updateProgram(UpdateProgramRequest program) {
        result = new Result();
        try {

            int programName = programRepository.findProgramname(program.getProgram_name().toLowerCase());
            if (programName > 0) {
                result.setMessage("Error: Nama Program Telah Ada!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if(program.getDescription().length()>50 || program.getDescription().isBlank() || program.getDescription().isEmpty()) {
                result.setMessage("Error: Deskripsi Program tidak boleh kosong dan harus kurang dari 50 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if(program.getProgram_name().length()>30 || program.getProgram_name().isBlank() || program.getProgram_name().isEmpty()) {
                result.setMessage("Error: Nama Program tidak boleh kosong dan harus kurang dari 30 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }


            if (!programRepository.findById(program.getId()).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada Program dengan id " +program.getId());
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Program update = new Program(program.getId(),program.getProgram_name(), program.getDescription(),
                        program.isSoft_delete());

                programRepository.save(update);

                result.setMessage("Berhasil update program baru!");
                result.setCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public Result getAllProgram() {
        result = new Result();
        try {
            Map<String, List<Program>> items = new HashMap<>();
            Program program = new Program();
            Example<Program> example = Example.of(program);
            items.put("items", programRepository.findAll(example, Sort.by(Sort.Direction.ASC,"id")));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getProgramById(Long id) {
        result = new Result();
        try {
            Optional<Program> program = programRepository.findById(id);
            if (!program.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada program dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map<String, Program> items = new HashMap<>();
                items.put("items", program.get());
                result.setData(items);
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }
}
