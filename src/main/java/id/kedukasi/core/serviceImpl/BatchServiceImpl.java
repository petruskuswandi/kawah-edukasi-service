package id.kedukasi.core.serviceImpl;


import id.kedukasi.core.models.Batch;
import id.kedukasi.core.models.Kelas;
import id.kedukasi.core.models.Mentor;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.repository.BatchRepository;
import id.kedukasi.core.repository.KelasRepository;
import id.kedukasi.core.repository.MentorRepository;
import id.kedukasi.core.request.BatchRequest;
import id.kedukasi.core.service.BatchService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    KelasRepository kelasRepository;

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    BatchRepository batchRepository;

    @Autowired
    StringUtil stringUtil;

    private Result result;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Result getAllBatch(String uri){
        result = new Result();
        try{
            Map items = new HashMap();
            items.put("items", batchRepository.findAll());
            result.setData(items);
        }catch (Exception e){
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

    // find classby batch
//    @Override
//    public Result getClassBybatch(Long id, String uri) {
//        result =  new Result();
//        try {
//
//        } catch (Exception e) {
//            logger.error(stringUtil.getError(e));
//        }
//        return null;
//
//    }
//
//    // find mentor class by batch
//    @Override
//    public Result getMentorClassByBatch(Long id, String uri) {
//        result = new Result();
//        try {
//
//        }catch (Exception e){
//            logger.error(stringUtil.getError(e));
//        }
//        return null;
//    }

    @Override
    public ResponseEntity<?> updateBatch(BatchRequest batchRequest) {
        result = new Result();
        try {
            // cek Batch name sudah di gunakan apa tidak
            Batch checkBatchname = batchRepository.findByBatchname(batchRequest.getBatchname()).orElse(new Batch());
            if (checkBatchname.getBatchname()!= null && !Objects.equals(batchRequest.getId(), checkBatchname.getId())) {
                result.setMessage("Error: Batch telah di gunakan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
            // cek Stardate tidak boleh lebih besar dari end date
            if(batchRequest.getStartedtime().after(batchRequest.getEndedtime())){
                result.setMessage("Error : Start date tidak boleh lebih besar dari end date ");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }
//            if(batchRequest.getStartedtime()!=null && batchRequest.getEndedtime()!=null){
//                result.setMessage("Error : batch harus di masuki data tanggal ");
//                result.setCode(HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity
//                        .badRequest()
//                        .body(result);
//            }

            Batch batchbaru = new Batch(batchRequest.getBatchname(), batchRequest.getDescription(),
                    batchRequest.getStartedtime(),batchRequest.getEndedtime());

            Kelas kelas = kelasRepository.findById(batchRequest.getClassname()).get();
            batchbaru.setClassname(kelas);

            Mentor mentor = mentorRepository.findById(batchRequest.getMentorname()).get();
            batchbaru.setMentorname(mentor);

            batchbaru.setId(batchRequest.getId());
            batchRepository.save(batchbaru);

            result.setMessage(batchRequest.getId() == 0 ? "Batch registered successfully!" : "Batch updated successfully!");
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
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }





}
