package id.kedukasi.core.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import id.kedukasi.core.models.Attachments;
import id.kedukasi.core.models.TypeDocuments;
import id.kedukasi.core.repository.AttachmentsRepository;
import id.kedukasi.core.repository.KelasRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.Syillabus;
import id.kedukasi.core.repository.SyillabusRepository;
import id.kedukasi.core.request.SyillabusRequest;
import id.kedukasi.core.request.UpdateSyllabusRequest;
import id.kedukasi.core.service.SyillabusService;
import id.kedukasi.core.utils.StringUtil;


@Service
public class SyillabusServiceImpl implements SyillabusService{
    
    @Autowired
    StringUtil stringUtil;

    private Result result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SyillabusRepository syillabusRepository;

    @Autowired
    AttachmentsRepository attachmentsRepository;
    @Autowired
    private KelasRepository kelasRepository;

    @Override
    public ResponseEntity<Result> createSyllabus(SyillabusRequest syillabus) {
        result = new Result();
       
        try{
            Syillabus checkSyillabus = syillabusRepository.findDellete(false, syillabus.getSyillabusName()).orElse(new Syillabus());
//            int syillabusName = syillabusRepository.findSyillabusName(syillabus.getSyillabusName().toLowerCase());
            
            if (checkSyillabus.getSyillabusName() != null) {
                result.setMessage("Error: nama syillabus sudah ada!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }
            if (syillabus.getSyillabusName().length()<3 || syillabus.getSyillabusName().isBlank() || syillabus.getSyillabusName().length()>50){
                result.setMessage("Error: nama syillabus tidak boleh kosong dan harus terdiri dari 3-50 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }
            if(syillabus.getDescription().length()>1000 || syillabus.getDescription().isBlank() ) {
                result.setMessage("Error: Deskripsi tidak boleh kosong dan Deskripsi harus kurang dari 1000 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }
            Syillabus newSyillabus = new Syillabus(syillabus.getSyillabusName(), syillabus.getDescription());

            //set Attachments
            for (Long attachmentId : syillabus.getAttachmentId()) {
                if(!attachmentsRepository.existsById(attachmentId)) {
                    result.setSuccess(false);
                    result.setMessage("Error: Attachment dengan ID " + attachmentId + " tidak ditemukan");
                    result.setCode(HttpStatus.BAD_REQUEST.value());
                    return ResponseEntity.badRequest().body(result);
                }else {
                    List<Attachments> attachments = attachmentsRepository.findAllById(syillabus.getAttachmentId());
                    newSyillabus.setAttachments(attachments);
                }
            }
            

            
          
            syillabusRepository.save(newSyillabus);
            result.setMessage("Berhasil membuat syllabus baru!");
            result.setCode(HttpStatus.OK.value());
        }catch (Exception e) {
            logger.error(stringUtil.getError(e));
            result.setSuccess(false);
            result.setMessage(e.getCause().getCause().getMessage());
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(result);

        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity updateSyillabus(UpdateSyllabusRequest syillabus) {
       result = new Result();
        try {
            int syillabusName = syillabusRepository.findSyillabusName(syillabus.getSyllabusName().toLowerCase());
             if (syillabusName != Integer.parseInt(null)) {
                 result.setMessage("Error: Nama Syillabus tidak boleh kosong ");
                 result.setCode(HttpStatus.BAD_REQUEST.value());
                 return ResponseEntity.badRequest().body(result);
             }
             if (syillabus.getSyllabusName().length()<3 || syillabus.getSyllabusName().length()>50 || syillabus.getSyllabusName().isBlank()){
                 result.setMessage("Error: Nama Syillabus tidak boleh kosong dan harus kurang dari 50 karakter");
                 result.setCode(HttpStatus.BAD_REQUEST.value());
                 return ResponseEntity.badRequest().body(result);
             }
            if (syillabus.getSyllabusName().length()<3 || syillabus.getSyllabusName().length()>100 || syillabus.getSyllabusName().isBlank() ) {
                result.setMessage("Error: Deskripsi harus kurang dari 1000 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }
            if (!syillabusRepository.findById(syillabus.getId()).isPresent()){
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada Syilabus dengan id " + syillabus.getId());
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Syillabus update = new Syillabus(syillabus.getId(),syillabus.getSyllabusName(), syillabus.getDescription(),syillabus.isSoftDelete());

                //set Attachments
      
                for (Long attachmentId : syillabus.getAttachmentId()) {
                    if(!attachmentsRepository.existsById(attachmentId)) {
                        result.setSuccess(false);
                        result.setMessage("Error: Attachment dengan ID " + attachmentId + " tidak ditemukan");
                        result.setCode(HttpStatus.BAD_REQUEST.value());
                        return ResponseEntity.badRequest().body(result);
                    }else {
                        List<Attachments> attachments = attachmentsRepository.findAllById(syillabus.getAttachmentId());
                        update.setAttachments(attachments);
                    }
                }

                syillabusRepository.save(update);

                result.setMessage("Berhasil update Syillabus!");
                result.setCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
              result.setSuccess(false);
            result.setMessage(e.getCause().getCause().getMessage());
            result.setCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public Result getAllSyillabus(String uri, String search, long limit, long offset) {
        result = new Result();
            if (limit == -99) {
                limit = kelasRepository.count();
            }
            if (offset == -99) {
                offset = 0;
            }
            if (search == null) {
                search = "";
            }
            try {
                Map items = new HashMap();
                List<Syillabus> syillabus = syillabusRepository.findAllSyillabus(search,  limit, offset);
                items.put("items", syillabus);
                items.put("total Data Result", syillabus.size());
                items.put("total Data", syillabusRepository.countSyillabusData());
                if (syillabus.size() == 0) {
                    result.setMessage("Data tidak ada");
                }
                result.setData(items);
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        return result;
    }


        @Override
    public ResponseEntity<Result> getSyillabusById(Long id) {
        result = new Result();
        try {
            Optional<Syillabus> syillabus = syillabusRepository.findById(id);

            if(!syillabus.isPresent()){
                result.setSuccess(false);
                result.setMessage("Error : Tidak ada syillabus dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }else if(syillabus.get().isDeleted()){
                result.setSuccess(false);
                result.setMessage("Error : Tidak ada syillabus dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            }else {
                Map<String, Syillabus> items = new HashMap<>();
                items.put("items", syillabus.get());
                result.setData(items);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> deleteSyillabus(Long id) {
       result = new Result();
       try {
        Optional<Syillabus> syilabus = syillabusRepository.findById(id);
        if (!syilabus.isPresent()){
            result.setSuccess(false);
            result.setMessage("Error: Tidak ada syilabus dengan id" + id);
            result.setCode(HttpStatus.BAD_REQUEST.value());

        } else {
            syillabusRepository.deletesyillabusAttachmentList(id);
            syillabusRepository.deleteById(id);
            result.setMessage("Berhasil delete syilabus");
            result.setCode(HttpStatus.OK.value());
        }
       } catch (Exception e){
        logger.error(stringUtil.getError(e));
       }
       return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> softDeleteSyillabus(Long id) {
        result = new Result();
        try {
            Optional<Syillabus> syilabus = syillabusRepository.findById(id);
            if (syilabus.isEmpty() || syilabus.get().isDeleted()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada syilabus dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }
            syilabus.get().setDeleted(true);
            syillabusRepository.save(syilabus.get());
            result.setMessage("Berhasil delete syilabus!");
            result.setCode(HttpStatus.OK.value());
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
            return ResponseEntity.badRequest().build();
        }
    }

   
}
