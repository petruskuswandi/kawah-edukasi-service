package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.models.*;
import id.kedukasi.core.repository.BatchDetailRepository;
import id.kedukasi.core.repository.BatchRepository;
import id.kedukasi.core.repository.KelasRepository;
import id.kedukasi.core.repository.MentorRepository;
import id.kedukasi.core.request.BatchListOAS;
import id.kedukasi.core.request.batchDetail.BatchDetailRequest;
import id.kedukasi.core.request.batchDetail.ByIdBatchDetail;
import id.kedukasi.core.request.batchDetail.NewBatchDetailRequest;
import id.kedukasi.core.request.batchDetail.UpdateBatchDetailRequest;
import id.kedukasi.core.service.BatchDetailService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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
            BatchDetail ob = new BatchDetail();
            Optional<Batch> batchExist = batchRepository.findById(batchDetail.getBatch());
            if(batchExist.isEmpty()){
                result.setSuccess(false);
                result.setMessage("Error : Tidak ada Batch dengan id " + batchDetail.getBatch());
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }
            ob.setBatch(batchExist.get());
            Example<BatchDetail> example = Example.of(ob);
            List<BatchDetail> listBatchDetail = batchDetailRepository.findAll(example);

            List<BatchDetail> hasil = new ArrayList<>();

            for (int i = 0; i < listBatchDetail.size(); i++){

                BatchDetail updateBatchDetail = listBatchDetail.get(i);

                // set class
                Optional<Kelas> kelas = kelasRepository.findById(batchDetail.getList().get(i).getKelas());
                if (kelas.isEmpty()) {
                    result.setSuccess(false);
                    result.setMessage("Error: Kelas tidak ditemukan");
                    result.setCode(HttpStatus.BAD_REQUEST.value());
                } else {
                    updateBatchDetail.setKelas(kelas.get());
                }

                //set mentor
                Optional<Mentor> mentor = mentorRepository.findById(batchDetail.getList().get(i).getMentor());
                if (mentor.isEmpty()) {
                    result.setSuccess(false);
                    result.setMessage("Error: Mentor tidak ditemukan");
                    result.setCode(HttpStatus.BAD_REQUEST.value());
                } else {
                    updateBatchDetail.setMentor(mentor.get());
                }

                // save
                batchDetailRepository.save(updateBatchDetail);
                hasil.add(updateBatchDetail);
            }
                result.setMessage(batchDetail.getList().get(0).getKelas().toString()+ " "+batchDetail.getList().get(0).getMentor());
            result.setData(hasil);
                result.setCode(HttpStatus.OK.value());
            }catch (Exception e){
                logger.error(stringUtil.getError(e));
                result.setSuccess(false);
                result.setMessage(e.getCause().getCause().getMessage());
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
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
            result.setSuccess(false);
            result.setMessage(e.getCause().getCause().getMessage());
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> createBatchDetail(BatchDetailRequest batchDetail) {
        result = new Result();
        try {
            //set class
            List<NewBatchDetailRequest> classMentorList = batchDetail.getList();

            List<NewBatchDetailRequest> list = new ArrayList<>();

            for(int i = 0; i < classMentorList.size(); i++) {
                BatchDetail Batchdetail = new BatchDetail();

                SetPenambahanData setPenambahanData = new SetPenambahanData();

                // set batch
                Optional<Batch> batch = batchRepository.findById(batchDetail.getBatch());
                if (!batch.isPresent()) {
                    result.setSuccess(false);
                    result.setMessage("Error: Batch tidak ditemukan");
                    result.setCode(HttpStatus.BAD_REQUEST.value());
                }else {
                    Batchdetail.setBatch(batch.get());
                    setPenambahanData.setNamaBatch(batch.get().getBatchname());
                }

                NewBatchDetailRequest classMentor = classMentorList.get(i);

                // set class
                Optional<Kelas> kelas = kelasRepository.findById(classMentor.getKelas());
                if (!kelas.isPresent()) {
                    result.setSuccess(false);
                    result.setMessage("Error: Kelas tidak ditemukan");
                    result.setCode(HttpStatus.BAD_REQUEST.value());
                } else {
                    Batchdetail.setKelas(kelas.get());
                    setPenambahanData.setNamaClass(kelas.get().getClassname());
                }

                 //set mentor
                Optional<Mentor> mentor = mentorRepository.findById(classMentor.getMentor());
                if (!mentor.isPresent()) {
                    result.setSuccess(false);
                    result.setMessage("Error: Mentor tidak ditemukan");
                    result.setCode(HttpStatus.BAD_REQUEST.value());
                } else {
                    Batchdetail.setMentor(mentor.get());
                    setPenambahanData.setNamaMentor(mentor.get().getNamamentor());
                }

                // save
                batchDetailRepository.save(Batchdetail);
                list.add(classMentor);
            }

            result.setMessage("Berhasil membuat Batch Detail!");
            result.setCode(HttpStatus.OK.value());
        }catch (Exception e){
            logger.error(stringUtil.getError(e));
            result.setSuccess(false);
            result.setMessage(e.getCause().getCause().getMessage());
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public Result getDetailByIdBatch(Long batchId) {
        result = new Result();
        try {
            BatchDetail ob = new BatchDetail();
            Optional<Batch> batch = batchRepository.findById(batchId);
            if(batch.isEmpty()){
                result.setSuccess(false);
                result.setMessage("Error : Tidak ada Batch dengan id " + batchId);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }
            ob.setBatch(batch.get());
            Example<BatchDetail> example = Example.of(ob);
            List<BatchDetail> batchDetail = batchDetailRepository.findAll(example);

                ByIdBatchDetail response = new ByIdBatchDetail();

                response.batch_id = batch.get().getId();
                response.batchname = batch.get().getBatchname();
                response.description = batch.get().getDescription();
                response.startedtime = batch.get().getStartedtime();
                response.endedtime = batch.get().getEndedtime();
                response.created_by = batch.get().getCreated_by();

            List<BatchListOAS> list = new ArrayList<>();

            for (BatchDetail batchDetailExist : batchDetail){
                BatchListOAS request = new BatchListOAS();
                request.class_id = batchDetailExist.getKelas().getId();
                request.classname = batchDetailExist.getKelas().getClassname();
                request.mentor_id = batchDetailExist.getMentor().getId();
                request.nama_mentor = batchDetailExist.getMentor().getNamamentor();

                list.add(request);
            }

            response.list = list;

                result.setData(response);
        }catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
            return result;
    }

    public class SetPenambahanData{
        private String namaBatch;
        private String namaClass;
        private String namaMentor;

        public String getNamaBatch() {
            return namaBatch;
        }

        public void setNamaBatch(String namaBatch) {
            this.namaBatch = namaBatch;
        }

        public String getNamaClass() {
            return namaClass;
        }

        public void setNamaClass(String namaClass) {
            this.namaClass = namaClass;
        }

        public String getNamaMentor() {
            return namaMentor;
        }

        public void setNamaMentor(String namaMentor) {
            this.namaMentor = namaMentor;
        }
    }
}
