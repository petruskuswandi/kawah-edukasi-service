package id.kedukasi.core.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import id.kedukasi.core.models.Attachments;
import id.kedukasi.core.models.TypeDocuments;
import id.kedukasi.core.repository.AttachmentsRepository;
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

    @Override
    public ResponseEntity<Result> createSyllabus(SyillabusRequest syillabus) {
        result = new Result();
       
        try{
            int syillabusName = syillabusRepository.findSyillabusName(syillabus.getSyillabusName().toLowerCase());
            
            if (syillabusName > 0) {
                result.setMessage("Error: nama syillabus sudah ada!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }
            
            if(syillabus.getDescription().length()>500 ) {
                result.setMessage("Error: Deskripsi harus kurang dari 500 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }
            Syillabus newSyillabus = new Syillabus(syillabus.getSyillabusName(), syillabus.getDescription(),false);
         
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
            // if (syillabusName > 0) {
            //     result.setMessage("Error: Nama Syillabus Telah Ada!");
            //     result.setCode(HttpStatus.BAD_REQUEST.value());
            //     return ResponseEntity.badRequest().body(result);
            // }
            if (syillabus.getDescription().length()>500 ) {
                result.setMessage("Error: Deskripsi harus kurang dari 500 karakter");
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
    public ResponseEntity<Result> getAllSyillabus() {
       result = new Result();
       try {
        Map<String, List<Syillabus>> items = new HashMap<>();
        Syillabus syillabus = new Syillabus();
        Example<Syillabus> example = Example.of(syillabus);
        items.put("items", syillabusRepository.findAll(example, Sort.by(Sort.Direction.ASC,"id")));
        result.setData(items);
       } catch (Exception e) {
        logger.error(stringUtil.getError(e));
       }
       return ResponseEntity.ok(result);
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
