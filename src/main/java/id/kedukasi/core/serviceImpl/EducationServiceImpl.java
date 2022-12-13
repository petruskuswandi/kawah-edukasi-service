package id.kedukasi.core.serviceImpl;


import id.kedukasi.core.models.Education;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.repository.EducationRepository;
import id.kedukasi.core.request.SaveEducationRequest;
import id.kedukasi.core.request.UpdateEducationRequest;
import id.kedukasi.core.service.EducationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
@Slf4j
public class EducationServiceImpl implements EducationService {
    @Autowired
    EducationRepository repository;

    private Result result;

    @Override
    public ResponseEntity<Result> saveEducation(SaveEducationRequest req) {
        log.info("save education service");
        result = new Result();
        Pattern p = Pattern.compile("[a-zA-Z0-9 ]+");
        Matcher m = p.matcher(req.getName());
        Matcher m2 = p.matcher(req.getDescription());
        boolean isName = m.matches();
        boolean isDesc = m2.matches();

        if (!isName) {
            result.setCode(HttpStatus.BAD_REQUEST.value());
            result.setMessage("Name hanya boleh huruf dan angka");
            result.setSuccess(false);
            return ResponseEntity.ok(result);
        }
        if (!isDesc) {
            result.setCode(HttpStatus.BAD_REQUEST.value());
            result.setMessage("Description hanya boleh huruf dan angka");
            result.setSuccess(false);
            return ResponseEntity.ok(result);
        }

        try {
            Education education = new Education();
            education.setName(req.getName());
            education.setDescription(req.getDescription());
            repository.save(education);
            result.setCode(HttpStatus.OK.value());
            result.setMessage("Success save education");
            result.setData(education);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }

    }

    @Override
    public ResponseEntity<Result> updateEducation(UpdateEducationRequest req, int id) {
        log.info("update education service");

        result = new Result();
        Pattern p = Pattern.compile("[a-zA-Z0-9 ]+");
        Matcher m = p.matcher(req.getName());
        Matcher m2 = p.matcher(req.getDescription());
        boolean isName = m.matches();
        boolean isDesc = m2.matches();

        if (!isName) {
            result.setCode(HttpStatus.BAD_REQUEST.value());
            result.setMessage("Name hanya boleh huruf dan angka");
            result.setSuccess(false);
            return ResponseEntity.ok(result);
        }
        if (!isDesc) {
            result.setCode(HttpStatus.BAD_REQUEST.value());
            result.setMessage("Description hanya boleh huruf dan angka");
            result.setSuccess(false);
            return ResponseEntity.ok(result);
        }

        try {
            Optional<Education> educationById = repository.findById(id);
            Education education = educationById.get();
            education.setName(req.getName());
            education.setDescription(req.getDescription());

            repository.save(education);

            result.setCode(HttpStatus.OK.value());
            result.setMessage("Success update education");
            result.setData(education);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public ResponseEntity<Result> getById(int id) {
        log.info("get by id education service");

        result = new Result();
        try {
            Optional<Education> byId = repository.findById(id);

            if (!byId.isPresent()) {
                result.setCode(HttpStatus.BAD_REQUEST.value());
                result.setMessage("Id tidak ditemukan");
                result.setSuccess(false);
                return ResponseEntity.ok(result);
            } else {
                result.setCode(HttpStatus.OK.value());
                result.setMessage("Success");
                result.setSuccess(true);
                result.setData(byId);
                return ResponseEntity.ok(result);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public ResponseEntity<Result> getAll() {
        log.info("get all education service");

        result = new Result();
        try {
            //find all data
            List<Education> allEducation = repository.findAll();

            //set response
            result.setCode(HttpStatus.OK.value());
            result.setMessage("Success");
            result.setSuccess(true);
            result.setData(allEducation);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public ResponseEntity<Result> deleteEducation(int id) {
        log.info("deleted education service");

        result = new Result();
        try {
            //find by id
            Optional<Education> byId = repository.findById(id);

            if (!byId.isPresent()) {
                result.setCode(HttpStatus.BAD_REQUEST.value());
                result.setMessage("Id tidak ditemukan");
                result.setSuccess(false);
                return ResponseEntity.ok(result);
            } else {
                //set true
                byId.get().setDeleted(true);
                repository.save(byId.get());

                result.setCode(HttpStatus.OK.value());
                result.setMessage("Success delete id : "+id);
                result.setSuccess(true);
                result.setData(byId);
                return ResponseEntity.ok(result);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
