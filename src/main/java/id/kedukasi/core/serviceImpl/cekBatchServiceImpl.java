package id.kedukasi.core.serviceImpl;


import id.kedukasi.core.models.Batch;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.repository.BatchRepository;
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
public class cekBatchServiceImpl implements BatchService {

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
        } catch (Exception e){
            logger.error(stringUtil.getError(e));
        }

        return result;
    }

    @Override
    public Result getBatchById(long id, String uri){
        result = new Result();
        try{
            Batch batch = batchRepository.findById(id);
            if (batch == null){
                result.setSuccess(false);
                result.setMessage("cannot find batch");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }else {
                Map items = new HashMap();
                items.put("items", batchRepository.findById(id));
                result.setData(items);
            }
        }catch (Exception e){
            logger.error(stringUtil.getError(e));
        }

        return result;
    }

    @Override
    public ResponseEntity<?> updateBatch(BatchRequest batchRequest) {
        result = new Result();
        try {
            Batch checkBatchname = batchRepository.findByBatchname(batchRequest.getBatchname()).orElse(new Batch());
            if (checkBatchname.getBatchname()!= null && !Objects.equals(batchRequest.getId(), checkBatchname.getId())) {
                result.setMessage("Error: Batch name is already in use!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            Batch batchBaru = new Batch(batchRequest.getBatchname(), batchRequest.getAlamarrumahmentor(), batchRequest.getMentorid(), batchRequest.getClassid(), batchRequest.getStarDate(),batchRequest.getEndDate());

            batchBaru.setId(batchRequest.getId());
            batchRepository.save(batchBaru);

            result.setMessage(batchRequest.getId() == 0 ? "Batch registered successfully!" : "Class updated successfully!");
            result.setCode(HttpStatus.OK.value());
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }


    @Override
    public ResponseEntity<?> deleteBatch(boolean banned, long id, String uri) {
        result = new Result();
        try {
            batchRepository.deleteBatch(banned, id);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }





}
