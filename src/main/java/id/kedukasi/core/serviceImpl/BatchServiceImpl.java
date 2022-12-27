package id.kedukasi.core.serviceImpl;


import id.kedukasi.core.models.*;
import id.kedukasi.core.repository.BatchRepository;
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

import java.util.*;

@Service
public class BatchServiceImpl implements BatchService {
    @Autowired
    BatchRepository batchRepository;

    @Autowired
    StringUtil stringUtil;

    private Result result;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Result getBatchData(String uri, String search, Integer limit, Integer page) {
        result = new Result();

        int jumlahpage = (int) Math.ceil(batchRepository.count() /(double) limit);

        if (limit < 1) {
            limit = 1;
        }

        if (page > jumlahpage) {
            page = jumlahpage;
        }

        if (page < 1 ) {
            page = 1;
        }

        if (search == null) {
            search = "";
        }
        try {
            Map items = new HashMap();
            List<Batch> batch = batchRepository.findBatchData(search, limit, page.intValue());
            items.put("items", batch);
            items.put("totalDataResult", batch.size());
            items.put("totalData", batchRepository.count());

            if (batch.size() == 0) {
                result.setMessage("Maaf Data Batch yang Anda cari tidak tersedia");
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

            Map items = new HashMap();
            Batch batch = new Batch();
            batch.setBanned(false);
            Example<Batch> example = Example.of(batch);
            items.put("items", batchRepository.findAll(example, Sort.by(Sort.Direction.ASC,"batchname")));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return result;
    }

//    @Override
//    public Result getAllClassByBatch(long batchId) {
//        result = new Result();
//        Optional<Batch> batch = batchRepository.findById(batchId);
//        if(!batch.isPresent()){
//            result.setCode(404);
//            result.setMessage("Batch Tidak Ada");
//            return result;
//        }
////        List<Long> kelas_id = batchRepository.getAllClassByBatch(batchId);
////        List<Kelas> kelas = kelasRepository.findAllById(kelas_id);
//        result.setCode(200);
//        result.setMessage("Berhasil Ambil Kelas");
//        result.setData(kelas);
//        return result;
//    }

    @Override
    public ResponseEntity<?> createBatch(CreateBatchRequest createBatchRequest) {
        result = new Result();
        try {
            // cek Batch name sudah di gunakan apa tidak
            Batch checkBatchname = batchRepository.findByBatchname(createBatchRequest.getBatchname()).orElse(new Batch());
            if (checkBatchname.getBatchname()!= null) {
                result.setMessage("Error: Batch telah di gunakan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
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
            Batch batchbaru = new Batch(createBatchRequest.getBatchname(), createBatchRequest.getDescription(),
                    createBatchRequest.getStartedtime(),createBatchRequest.getEndedtime());
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

            Batch batchbaru = new Batch(batchRequest.getBatchname(), batchRequest.getDescription(),
                    batchRequest.getStartedtime(),batchRequest.getEndedtime());

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
            result.setMessage(banned == true ? "Batch success delete!" : "Batch success backup!");
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }
}
