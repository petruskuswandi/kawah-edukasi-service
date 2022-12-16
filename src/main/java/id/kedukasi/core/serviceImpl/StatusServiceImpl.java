package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.Status;
import id.kedukasi.core.repository.StatusRepository;
import id.kedukasi.core.request.StatusRequest;
import id.kedukasi.core.request.UpdateStatusRequest;
import id.kedukasi.core.service.StatusService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class StatusServiceImpl implements StatusService {
    @Autowired
    StringUtil stringUtil;

    private Result result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StatusRepository statusRepository;
    @Override
    public ResponseEntity<Result> createStatus(StatusRequest status) {
        result = new Result();
        try {
            int statusNameFlag = 0;
            Boolean inputNullFlag = true;
            Boolean inputNullDescription = true;
            Boolean inputNullStatusName = true;
            String errorUniqueStatusNameAndFlagMessage = "";
            String errorNotBlankFlagMessage = "";
            String errorNotBlankDescriptionMessage = "";
            String errorNotBlankStatusNameMessage = "";
            String errorNotNullStatusNameMessage = "";
            String errorNotNullDescriptionMessage = "";
            String errorNotNullFlagMessage = "";

            if(status.getFlag() != null && status.getStatusName() != null){
                statusNameFlag = statusRepository.findStatusNameFlag(status.getStatusName().toLowerCase(), status.getFlag().toLowerCase());
            }

            if(status.getFlag() != null){
                inputNullFlag = false;
            }

            if(status.getDescription() != null){
                inputNullDescription = false;
            }

            if(status.getStatusName() != null){
                inputNullStatusName = false;
            }

            if (statusNameFlag > 0) {
                errorUniqueStatusNameAndFlagMessage = "Kombinasi Nama dan Flag pada Status sudah ada!";
            }

            // int statusLength = statusRepository.findAll().size();
            // int statusInitialId = statusRepository.findAll().get(0).getId();
            // int statusLastId = statusRepository.findAll().get(statusLength-1).getId();
            // if (statusLength > 0) {
            //     String statusName;
            //     String flag;

            //     for(int _id = statusInitialId; _id < statusLastId+1; _id++){
            //         if(statusRepository.findById(_id).isPresent()){
            //             statusName = statusRepository.findById(_id).get().getStatusName();
            //             flag = statusRepository.findById(_id).get().getFlag();
            //             if(statusName.equals(status.getStatusName()) && flag.equals(status.getFlag()) ){
            //                 errorUniqueStatusNameAndFlagMessage = "Sudah Ada Nama Status dan Flag yang Sama! pada Id = " + _id;
            //                 break;
            //             }
            //         }
            //     } 
            // }

            if(inputNullFlag != true){
                if(status.getFlag().isBlank() || status.getFlag().isEmpty()) {
                    errorNotBlankFlagMessage = "Flag tidak boleh kosong dan harus kurang dari 30 karakter, ";
                }
            }

            if(inputNullDescription != true){
                if(status.getDescription().isBlank()  || status.getDescription().isEmpty()) {
                    errorNotBlankDescriptionMessage = "Deskripsi Status tidak boleh kosong, ";
                }
            }
    
            if(inputNullStatusName != true){
                if(status.getStatusName().length()>50 || status.getStatusName().isBlank() || status.getStatusName().isEmpty()) {
                    errorNotBlankStatusNameMessage = "Nama Status tidak boleh kosong dan harus kurang dari 50 karakter, ";
                }
            }

            if(status.getFlag() == null){
                errorNotNullFlagMessage = "Flag tidak boleh null, ";
            }

            if(status.getDescription() == null){
                errorNotNullDescriptionMessage = "Deskripsi Status tidak boleh null, ";
            }

            if(status.getStatusName() == null){
                errorNotNullStatusNameMessage = "Nama Status tidak boleh null, ";
            }

            if (errorUniqueStatusNameAndFlagMessage != "" || errorNotBlankFlagMessage != "" || errorNotBlankDescriptionMessage != "" || errorNotBlankStatusNameMessage != ""  || errorNotNullFlagMessage != ""  || errorNotNullDescriptionMessage != ""  || errorNotNullStatusNameMessage != "") {
                result.setSuccess(false);
                result.setMessage("Error: "+ errorUniqueStatusNameAndFlagMessage + errorNotBlankFlagMessage + errorNotBlankDescriptionMessage + errorNotBlankStatusNameMessage + errorNotNullFlagMessage + errorNotNullDescriptionMessage + errorNotNullStatusNameMessage);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            Status newStatus = new Status(status.getStatusName().toUpperCase(), status.getDescription(), status.getFlag().toUpperCase(), false);

            statusRepository.save(newStatus);

            result.setMessage("Berhasil membuat status baru!");
            result.setCode(HttpStatus.OK.value());

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> updateStatus(UpdateStatusRequest status) {
        result = new Result();
        try {
            // int statusNameFlag = 0;
            Boolean inputNullFlag = true;
            Boolean inputNullDescription = true;
            Boolean inputNullStatusName = true;
            // int existingId = 0;
            String errorUniqueStatusNameAndFlagMessage = "";
            String errorNotBlankFlagMessage = "";
            String errorNotBlankDescriptionMessage = "";
            String errorNotBlankStatusNameMessage = "";
            String errorNotNullStatusNameMessage = "";
            String errorNotNullDescriptionMessage = "";
            String errorNotNullFlagMessage = "";

            // if(status.getFlag() != null && status.getStatusName() != null){
            //     statusNameFlag = statusRepository.findStatusNameFlag(status.getStatusName().toLowerCase(), status.getFlag().toLowerCase());
            //     existingId = statusRepository.findIdStatusByNameAndFlag(status.getStatusName().toLowerCase(), status.getFlag().toLowerCase());
            // }

            if(status.getFlag() != null){
                inputNullFlag = false;
            }

            if(status.getDescription() != null){
                inputNullDescription = false;
            }

            if(status.getStatusName() != null){
                inputNullStatusName = false;
            }

            if(status.getFlag() != null){
                inputNullFlag = false;
            }

            // if (statusNameFlag > 0 && status.getId() != existingId) {      
            //     errorUniqueStatusNameAndFlagMessage = "Kombinasi Nama dan Flag di Status sudah ada pada id "+existingId+"!";
            // }

            int statusLength = statusRepository.findAll().size();
            int statusInitialId = statusRepository.findAll().get(0).getId();
            int statusLastId = statusRepository.findAll().get(statusLength-1).getId();
            int putId = status.getId();
            String putFlag;
            String putDescription;
            String putStatusName;

            if(status.getFlag() == null){
                putFlag = statusRepository.findById(putId).get().getFlag();
                // errorNotNullFlagMessage = "Flag tidak boleh null, ";
            } else {
                putFlag = status.getFlag();
            }

            if(status.getDescription() == null){
                putDescription = statusRepository.findById(putId).get().getDescription();
                // errorNotNullDescriptionMessage = "Deskripsi Status tidak boleh null, ";
            } else {
                putDescription = status.getDescription();
            }

            if(status.getStatusName() == null){
                putStatusName = statusRepository.findById(putId).get().getStatusName();
                //errorNotNullStatusNameMessage = "Nama Status tidak boleh null, ";
            } else {
                putStatusName = status.getStatusName();
            }

            if (statusLength > 0) {
                String statusName;
                String flag;

                for(int _id = statusInitialId; _id < statusLastId+2; _id++){
                    if(statusRepository.findById(_id).isPresent()){
                        statusName = statusRepository.findById(_id).get().getStatusName();
                        flag = statusRepository.findById(_id).get().getFlag();
                        if(statusName.equals(putStatusName) && flag.equals(putFlag) ){
                            if(putId != _id){
                                errorUniqueStatusNameAndFlagMessage = "Sudah Ada Kombinasi Nama Status dan Flag yang Sama! pada Id = " + _id;
                                break;
                            }
                        }
                    }
                } 
            }

            if(inputNullFlag != true){
                if(status.getFlag().isBlank() || status.getFlag().isEmpty()) {
                    errorNotBlankFlagMessage = "Flag tidak boleh kosong dan harus kurang dari 30 karakter, ";
                }
            }

            if(inputNullDescription != true){
                if(status.getDescription().isBlank()  || status.getDescription().isEmpty()) {
                    errorNotBlankDescriptionMessage = "Deskripsi Status tidak boleh kosong, ";
                }
            }
    
            if(inputNullStatusName != true){
                if(status.getStatusName().length()>50 || status.getStatusName().isBlank() || status.getStatusName().isEmpty()) {
                    errorNotBlankStatusNameMessage = "Nama Status tidak boleh kosong dan harus kurang dari 50 karakter, ";
                }
            }

            if (errorUniqueStatusNameAndFlagMessage != "" || errorNotBlankFlagMessage != "" || errorNotBlankDescriptionMessage != "" || errorNotBlankStatusNameMessage != ""  || errorNotNullFlagMessage != ""  || errorNotNullDescriptionMessage != ""  || errorNotNullStatusNameMessage != "") {
                result.setSuccess(false);
                result.setMessage("Error: "+ errorUniqueStatusNameAndFlagMessage + errorNotBlankFlagMessage + errorNotBlankDescriptionMessage + errorNotBlankStatusNameMessage + errorNotNullFlagMessage + errorNotNullDescriptionMessage + errorNotNullStatusNameMessage);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if (!statusRepository.findById(status.getId()).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada Status dengan id " +status.getId());
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Status update = new Status(putId,putStatusName, putDescription, putFlag, status.getisDeleted());

                statusRepository.save(update);

                result.setMessage("Berhasil update status!");
                result.setCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> getAllStatus() {
        result = new Result();
        try {
            Map<String, List<Status>> items = new HashMap<>();
            items.put("items", statusRepository.findAll(Sort.by(Sort.Direction.ASC,"id")));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> getStatusById(int id) {
        result = new Result();
        try {
            Optional<Status> status = statusRepository.findById(id);
            if (!status.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada status dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map<String, Status> items = new HashMap<>();
                items.put("items", status.get());
                result.setData(items);
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> deleteStatusById(int id) {
        result = new Result();
        try {
            Optional<Status> status = statusRepository.findById(id);
            if (!status.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada status dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                statusRepository.deleteById(id);
                result.setMessage("Berhasil delete status!");
                result.setCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }
}


