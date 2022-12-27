package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.models.*;
import id.kedukasi.core.repository.BatchDetailRepository;
import id.kedukasi.core.repository.BatchRepository;
import id.kedukasi.core.repository.KelasRepository;
import id.kedukasi.core.repository.MentorRepository;
import id.kedukasi.core.request.BatchDetailRequest;
import id.kedukasi.core.request.UpdateBatchDetailRequest;
import id.kedukasi.core.service.BatchDetailService;
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
public class BatchDetailServiceImpl implements BatchDetailService {

    @Autowired
    StringUtil stringUtil;

    private Result result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BatchDetailRepository batchDetailRepository;

    @Autowired
    BatchRepository batchRepository;

    @Autowired
    KelasRepository kelasRepository;

    @Autowired
    MentorRepository mentorRepository;

    @Override
    public ResponseEntity<Result> updateBatchDetail(UpdateBatchDetailRequest batchDetail)
    {
        result = new Result();
        try {

            if (!batchDetailRepository.findById(batchDetail.getId()).isPresent()){
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada Batch Detail dengan id " + batchDetail.getId());
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }else {

                BatchDetail Batchdetail = new BatchDetail(batchDetail.getId(), batchDetail.isDeleted());

                // set batch
                Batch batch = batchRepository.findById(batchDetail.getBatch()).get();
                if (!batchRepository.findById(batch.getId()).isPresent()) {
                    result.setSuccess(false);
                    result.setMessage("Batch tidak ditemukan");
                    result.setCode(HttpStatus.BAD_REQUEST.value());
                }else {
                    Batchdetail.setBatch(batch);
                }

                // set class
                Kelas kelas = kelasRepository.findById(batchDetail.getKelas()).get();
                if (!kelasRepository.findById(kelas.getId()).isPresent()){
                    result.setSuccess(false);
                    result.setMessage("Error: Tidak ada Kelas dengan id " + kelas.getId());
                    result.setCode(HttpStatus.BAD_REQUEST.value());
                } else {
                    Batchdetail.setKelas(kelas);
                }

                // set mentor
                Mentor mentor = mentorRepository.findById(batchDetail.getMentor()).get();
                if (!mentorRepository.findById(mentor.getId()).isPresent()) {
                    result.setSuccess(false);
                    result.setMessage("Mentor tidak ditemukan");
                    result.setCode(HttpStatus.BAD_REQUEST.value());
                }else {
                    Batchdetail.setMentor(mentor);
                }

                batchDetailRepository.save(Batchdetail);

                result.setMessage("Berhasil update Batch Detail!");
                result.setCode(HttpStatus.OK.value());
            }
        }catch (Exception e){
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> deleteBatchDetail(Long id) {
        result = new Result();
        try {
            Optional<BatchDetail> batchDetail = batchDetailRepository.findById(id);
            if (!batchDetail.isPresent()){
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada  Batch Detail dengan id" + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());

            } else {
                batchDetailRepository.deleteById(id);
                result.setMessage("Berhasil delete  Batch Detail");
                result.setCode(HttpStatus.OK.value());
            }
        } catch (Exception e){
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> createBatchDetail(BatchDetailRequest batchDetail) {
        result = new Result();
        try {
            BatchDetail Batchdetail = new BatchDetail();

            // set batch
           Optional<Batch> batch = batchRepository.findById(batchDetail.getBatch());
            if (!batch.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Batch tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }else {
                Batch batch1 = batchRepository.findById(batchDetail.getBatch()).get();
                Batchdetail.setBatch(batch1);
            }

            // set class
            Optional<Kelas> kelas = kelasRepository.findById(batchDetail.getKelas());
            if (!kelas.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Class tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }else {
                Kelas kelas1 = kelasRepository.findById(batchDetail.getKelas()).get();
                Batchdetail.setKelas(kelas1);
            }

            // set mentor
            Optional<Mentor> mentor = mentorRepository.findById(batchDetail.getMentor());
            if (!mentor.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Mentor tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }else {
                Mentor mentor1 = mentorRepository.findById(batchDetail.getMentor()).get();
                Batchdetail.setMentor(mentor1);
            }

            batchDetailRepository.save(Batchdetail);

            result.setMessage("Berhasil membuat Batch Detail baru!");
            result.setCode(HttpStatus.OK.value());
        }catch (Exception e){
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> getAllBatchDetail() {
        result = new Result();
        try {
            Map<String, List<BatchDetail>> items = new HashMap<>();
            BatchDetail batchDetail = new BatchDetail();
            Example<BatchDetail> example = Example.of(batchDetail);
            items.put("items", batchDetailRepository.findAll(example, Sort.by(Sort.Direction.ASC,"id")));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> getBatchDetailId(Long id) {
        result = new Result();
        try {
            Optional<BatchDetail> batchDetail = batchDetailRepository.findById(id);
            if(!batchDetail.isPresent()){
                result.setSuccess(false);
                result.setMessage("Error : Tidak ada Batch Detail dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map<String, BatchDetail> items = new HashMap<>();
                items.put("items", batchDetail.get());
                result.setData(items);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }
}
