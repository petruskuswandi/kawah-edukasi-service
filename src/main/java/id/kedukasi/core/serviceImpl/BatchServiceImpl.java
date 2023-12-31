package id.kedukasi.core.serviceImpl;


import com.fasterxml.jackson.annotation.JsonFormat;
import id.kedukasi.core.models.*;
import id.kedukasi.core.repository.BatchRepository;
import id.kedukasi.core.repository.UserRepository;
import id.kedukasi.core.request.BatchRequest;
import id.kedukasi.core.request.CreateBatchRequest;
import id.kedukasi.core.service.BatchService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BatchServiceImpl implements BatchService {
    @Autowired
    BatchRepository batchRepository;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    UserRepository userRepository;
    private Result result;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Result getBatchData(String uri, String search, Integer limit, Integer page) {
        result = new Result();

        if (search == null) {
            search = "";
        }
        try {
            Map items = new HashMap();
            List<Batch> batch = batchRepository.findBatchData(search.toLowerCase(), limit, page.intValue());
            items.put("items", batch);
            items.put("totalDataResult", batch.size());
            items.put("totalData", batchRepository.bannedfalse());
            if (batch.size() == 0){
                result.setMessage("Data is not found");
            }
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

    @Override
    public Result getBatchById(Long id, String uri){
        result = new Result();
        try{
            if (!batchRepository.findById(id).isPresent()){
                result.setSuccess(false);
                result.setMessage("cannot find batch");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }else {
                Map items = new HashMap();
                items.put("items", batchRepository.findById(id).get());
                result.setData(items);
            }
        }catch (Exception e){
            logger.error(stringUtil.getError(e));
        }

        return result;
    }

    public Result getAllBatchRunning(String uri) {
        result = new Result();
        try {
            if (batchRepository.findAllBatchRunning().isEmpty()){
                result.setSuccess(false);
                result.setMessage("cannot found batch running");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map<String, Object> items = new HashMap();
                String[] key = {"id", "batchname", "description", "banned", "banned_time", "started_time", "ended_time", "created_by", "created_time", "updated_time", "is_active"};
                List<Object[]> obj = batchRepository.findAllBatchRunning();
                List<Map<String, Object>> response = new ArrayList<>();
                for(Object[] c : obj) {
                    Map<String, Object> temp = new HashMap<>();
                    for (int i = 0; i < key.length; i++) {
                        if (c[i] instanceof Date){
                            temp.put(key[i], new SimpleDateFormat("yyyy-MM-dd").format(c[i]));
                        } else {
                            temp.put(key[i], c[i]);
                        }
                    }
                    if (temp.get("created_by") != null) {
                        temp.replace("created_by", batchRepository.finduserbyid());
                    }
                    response.add(temp);
                }
                items.put("items", response);
                result.setData(items);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }
    @Override
    public ResponseEntity<?> createBatch(CreateBatchRequest createBatchRequest) {
        result = new Result();
        try {
            // cek Batch name sudah di gunakan apa tidak
            Batch checkBatchname = batchRepository.findBanned(false, createBatchRequest.getBatchname()).orElse(new Batch());
            if (checkBatchname.getBatchname() != null) {
                result.setMessage("Error: Batch telah di gunakan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if (createBatchRequest.getBatchname().length() >= 30) {
                result.setMessage("Error: Nama Batch lebih dari 30 character");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            // cek nama batch kosong atau tidak
            if (createBatchRequest.getBatchname().isEmpty()){
                result.setMessage("Error: Batch Name is Null/Empty!");
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if(createBatchRequest.getDescription().isEmpty()) {
                result.setMessage("Error: Description is Null/Empty!");
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if (createBatchRequest.getStartedtime() == null) {
                result.setMessage("Error: Started Time is Null/Empty");
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if (createBatchRequest.getEndedtime() == null){
                result.setMessage("Error: Ended Time is Null/Empty");
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            // cek Stardate tidak boleh lebih besar dari end date
            if(createBatchRequest.getStartedtime().after(createBatchRequest.getEndedtime())){
                result.setMessage("Error : Start date tidak boleh lebih besar dari end date ");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            Optional<User> user = Optional.ofNullable(userRepository.findById(createBatchRequest.getCreated_by()));
            if (user == null){
                result.setSuccess(false);
                result.setMessage("Error: id User tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                User user_id = userRepository.findById(createBatchRequest.getCreated_by());
                createBatchRequest.setCreated_by(Math.toIntExact(user_id.getId()));
            }

            Batch batchbaru = new Batch(createBatchRequest.getBatchname(), createBatchRequest.getDescription(),
                    createBatchRequest.getStartedtime(),createBatchRequest.getEndedtime());
            Date date = new Date();
            batchbaru.setCreated_time(date);
            batchbaru.setCreated_by(user.get());
            batchRepository.save(batchbaru);

            result.setMessage("Batch registered successfully!");
            result.setCode(HttpStatus.OK.value());
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }


    @Override
    public ResponseEntity<?> updateBatch(BatchRequest batchRequest) {
        result = new Result();
        try {
            // cek Batch name sudah di gunakan apa tidak
            Batch batch = new Batch();
            Batch checkBatchname = batchRepository.findIdValidationName(batchRequest.getBatchname(), batchRequest.getId()).orElse(new Batch());
            if (checkBatchname.getBatchname() != null) {
                result.setMessage("Error: Batch telah di gunakan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            } else {
                batch.setBatchname(batchRequest.getBatchname());
            }


            if (!batchRepository.existsById(batchRequest.getId())){
                result.setMessage("Error: Batch id is not found");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            // cek nama batch kosong atau tidak
            if (batchRequest.getBatchname().isEmpty()){
                result.setMessage("Error: Batch Name is Null/Empty!");
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if(batchRequest.getDescription().isEmpty()) {
                result.setMessage("Error: Description is Null/Empty!");
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            // cek Stardate tidak boleh lebih besar dari end date
            if(batchRequest.getStartedtime().after(batchRequest.getEndedtime())){
                result.setMessage("Error : Start date tidak boleh lebih besar dari end date");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            if (batchRequest.getEndedtime() == null){
                result.setMessage("Error: Ended Time is Null/Empty");
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if (batchRequest.getStartedtime() == null){
                result.setMessage("Error: Started Time is Null/Empty");
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            Optional<User> user = Optional.ofNullable(userRepository.findById(batchRequest.getCreated_by()));
            if (!user.isPresent()){
                result.setSuccess(false);
                result.setMessage("Error: id User tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                User user_id = userRepository.findById(batchRequest.getCreated_by());
                batchRequest.setCreated_by(Math.toIntExact(user_id.getId()));
            }

            Batch batchbaru = new Batch(batchRequest.getBatchname(), batchRequest.getDescription(),
                    batchRequest.getStartedtime(),batchRequest.getEndedtime());
            Date date = new Date();

            batchbaru.setUpdated_time(date);
            batchbaru.setCreated_by(user.get());
            batchbaru.setId(batchRequest.getId());
            batchRepository.save(batchbaru);

            result.setMessage("Batch updated successfully!");
            result.setCode(HttpStatus.OK.value());
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }


    @Override
    public ResponseEntity<?> deleteBatch(boolean banned, Long id, String uri) {
        result = new Result();
        try {
            batchRepository.deleteBatch(banned, id);
            if (batchRepository.existsById(id)) {
                result.setMessage(banned == true ? "Batch success delete!" : "Batch success backup!");
            } else {
                result.setCode(HttpStatus.BAD_REQUEST.value());
                result.setMessage("Id its not found");
                result.setSuccess(false);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }
}
