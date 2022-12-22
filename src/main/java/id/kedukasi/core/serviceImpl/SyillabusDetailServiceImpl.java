package id.kedukasi.core.serviceImpl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import id.kedukasi.core.models.Kelas;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.Syillabus;
import id.kedukasi.core.models.SyillabusDetail;
import id.kedukasi.core.repository.KelasRepository;
import id.kedukasi.core.repository.SyillabusDetailRepository;
import id.kedukasi.core.repository.SyillabusRepository;
import id.kedukasi.core.request.SyillabusDetailRequest;
import id.kedukasi.core.request.UpdateSyillabusDetailRequest;
import id.kedukasi.core.service.SyillabusDetailService;
import id.kedukasi.core.utils.StringUtil;

@Service
public class SyillabusDetailServiceImpl implements SyillabusDetailService{

    @Autowired
    StringUtil stringUtil;

    private Result result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SyillabusDetailRepository syillabusDetailRepository;

    @Autowired
    KelasRepository kelasRepository;

    @Autowired
    SyillabusRepository syillabusRepository;

    @Override
    public ResponseEntity<Result> createSyillabusDetail(SyillabusDetailRequest syillabusDetail) {

        result = new Result();
        try {
            SyillabusDetail Syillabusdetail = new SyillabusDetail();

            // set syillabus
            Optional<Syillabus> syillabus = syillabusRepository.findById(syillabusDetail.getSyillabus());
            if (!syillabus.isPresent()){
                result.setSuccess(false);
                result.setMessage("Error : Syillabus tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Syillabus syillabus2 = syillabusRepository.findById(syillabusDetail.getSyillabus()).get();
                Syillabusdetail.setSyillabus(syillabus2);
            }

            // set class
            Optional<Kelas> kelas = kelasRepository.findById(syillabusDetail.getKelas());
            if (!kelas.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Class tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else{
                Kelas kelas2 = kelasRepository.findById(syillabusDetail.getKelas()).get();
                Syillabusdetail.setKelas(kelas2);
            }

            syillabusDetailRepository.save(Syillabusdetail);
            
            result.setMessage("Berhasil Membuat syillabus detail baru");
            result.setCode(HttpStatus.OK.value());

        }catch (Exception e){
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
      
    }

    @Override
    public ResponseEntity<Result> getAllSyillabusDetail() {
       result = new Result();
       try {
            Map<String, List<SyillabusDetail>> items = new HashMap<>();
            SyillabusDetail syillabusDetail = new SyillabusDetail();
            Example<SyillabusDetail> example = Example.of(syillabusDetail);
            items.put("items", syillabusDetailRepository.findAll(example, Sort.by(Sort.Direction.ASC,"id")));
            result.setData(items);
       } catch (Exception e){
        logger.error(stringUtil.getError(e));
       }
       return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> getSyillabusDetailById(Long id) {
       result = new Result();
       try {
            Optional<SyillabusDetail> syillabusDetail = syillabusDetailRepository.findById(id);
            if(!syillabusDetail.isPresent()){
                result.setSuccess(false);
                result.setMessage("Error : Tidak ada Syillabus Detail dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map<String, SyillabusDetail> items = new HashMap<>();
                items.put("items", syillabusDetail.get());
                result.setData(items);
            }
       } catch (Exception e){
            logger.error(stringUtil.getError(e));
       }
    return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> deleteSyillabusDetail(Long id) {
        result = new Result();
        try{
            Optional<SyillabusDetail> syillabusDetail = syillabusDetailRepository.findById(id);
            if(!syillabusDetail.isPresent()){
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada  Syillabus Detail dengan id" + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                syillabusDetailRepository.deleteById(id);
                result.setMessage("Berhasil delete Syillabus Detail");
                result.setCode(HttpStatus.OK.value());
            }
        } catch (Exception e){
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
       
    }
    

    @Override
    public ResponseEntity<Result> updateSyillabusDetail(UpdateSyillabusDetailRequest syillabusDetail) {
        result = new Result();
        try {
            if(!syillabusDetailRepository.findById(syillabusDetail.getId()).isPresent()){
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada Syillabus Detail dengan id " + syillabusDetail.getId());
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                SyillabusDetail Syillabusdetail = new SyillabusDetail(syillabusDetail.getId(), syillabusDetail.isDeleted());

                Syillabus syillabus = syillabusRepository.findById(syillabusDetail.getSyillabus()).get();
                if (!syillabusRepository.findById(syillabus.getId()).isPresent()) {
                    result.setSuccess(false);
                    result.setMessage("Syillabus tidak ditemukan");
                    result.setCode(HttpStatus.BAD_REQUEST.value());
                }else {
                    Syillabusdetail.setSyillabus(syillabus);
                }

                Kelas kelas = kelasRepository.findById(syillabusDetail.getKelas()).get();
                if (!kelasRepository.findById(kelas.getId()).isPresent()){
                    result.setSuccess(false);
                    result.setMessage("Error: Tidak ada Kelas dengan id " + kelas.getId());
                    result.setCode(HttpStatus.BAD_REQUEST.value());
                } else {
                    Syillabusdetail.setKelas(kelas);
                }
                syillabusDetailRepository.save(Syillabusdetail);
                result.setMessage("Berhasil update Syillabus Detail!");
                result.setCode(HttpStatus.OK.value());
            }
        }catch (Exception e){
            logger.error(stringUtil.getError(e));
    }
    return ResponseEntity.ok(result);
}
}
