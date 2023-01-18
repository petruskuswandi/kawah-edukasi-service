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
            Boolean inputNullSubFlag = true;
            Boolean inputNullFlag = true;
            Boolean inputNullDescription = true;
            Boolean inputNullStatusName = true;
            String errorUniqueStatusNameAndFlagAndSubFlagMessage = "";
            String errorNotBlankSubFlagMessage = "";
            String errorNotBlankFlagMessage = "";
            String errorNotBlankDescriptionMessage = "";
            String errorNotBlankStatusNameMessage = "";
            String errorNotNullStatusNameMessage = "";
            String errorNotNullDescriptionMessage = "";
            String errorNotNullFlagMessage = "";
            String errorNotNullSubFlagMessage = "";

            if(status.getSubFlag() != null && status.getFlag() != null && status.getStatusName() != null){
                statusNameFlag = statusRepository.findStatusNameFlagSubFlag(status.getStatusName().toLowerCase(), status.getFlag().toLowerCase(), status.getSubFlag().toLowerCase());
            }

            if(status.getSubFlag() != null){
                inputNullSubFlag = false;
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
                errorUniqueStatusNameAndFlagAndSubFlagMessage = "Kombinasi Nama Status, Flag, dan Sub Flag pada Status sudah ada!";
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

            if(inputNullSubFlag != true){
                if(status.getSubFlag().isBlank() || status.getSubFlag().isEmpty()) {
                    errorNotBlankFlagMessage = "Sub Flag tidak boleh kosong dan harus kurang dari 30 karakter, ";
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

            if(status.getSubFlag() == null){
                errorNotNullSubFlagMessage = "Sub Flag tidak boleh null, ";
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

            if (errorUniqueStatusNameAndFlagAndSubFlagMessage != "" || errorNotBlankSubFlagMessage != "" || errorNotBlankFlagMessage != "" || errorNotBlankDescriptionMessage != "" || errorNotBlankStatusNameMessage != ""  || errorNotNullSubFlagMessage != "" || errorNotNullFlagMessage != ""  || errorNotNullDescriptionMessage != ""  || errorNotNullStatusNameMessage != "") {
                result.setSuccess(false);
                result.setMessage("Error: "+ errorUniqueStatusNameAndFlagAndSubFlagMessage+ errorNotBlankSubFlagMessage  + errorNotBlankFlagMessage + errorNotBlankDescriptionMessage + errorNotBlankStatusNameMessage + errorNotNullSubFlagMessage + errorNotNullFlagMessage + errorNotNullDescriptionMessage + errorNotNullStatusNameMessage);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            Status newStatus = new Status(status.getStatusName(), status.getDescription(), status.getFlag(), status.getSubFlag(),false);

            statusRepository.save(newStatus);

            result.setMessage("Berhasil membuat status baru!");
            result.setCode(HttpStatus.OK.value());

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
    public ResponseEntity<Result> updateStatus(UpdateStatusRequest status) {
        result = new Result();
        try {
            // int statusNameFlag = 0;
            Boolean inputNullSubFlag = true;
            Boolean inputNullFlag = true;
            Boolean inputNullDescription = true;
            Boolean inputNullStatusName = true;
            // int existingId = 0;
            String errorUniqueStatusNameAndFlagAndSubFlagMessage = "";
            String errorNotBlankSubFlagMessage = "";
            String errorNotBlankFlagMessage = "";
            String errorNotBlankDescriptionMessage = "";
            String errorNotBlankStatusNameMessage = "";
            String errorNotNullStatusNameMessage = "";
            String errorNotNullDescriptionMessage = "";
            String errorNotNullFlagMessage = "";
            String errorNotNullSubFlagMessage = "";

            // if(status.getSubFlag() != null && status.getFlag() != null && status.getStatusName() != null){
            //     statusNameFlag = statusRepository.findStatusNameFlagSubFlag(status.getStatusName().toLowerCase(), status.getFlag().toLowerCase(), status.getSubFlag().toLowerCase());
            //     existingId = statusRepository.findIdStatusByNameAndFlagAndSubFlag(status.getStatusName().toLowerCase(), status.getFlag().toLowerCase());
            // }

            if(status.getSubFlag() != null){
                inputNullSubFlag = false;
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

            // if (statusNameFlag > 0 && status.getId() != existingId) {      
            //     errorUniqueStatusNameAndFlagMessage = "Kombinasi Nama dan Flag di Status sudah ada pada id "+existingId+"!";
            // }

            int statusLength = statusRepository.findAll().size();
            int statusInitialId = statusRepository.findAll().get(0).getId();
            int statusLastId = statusRepository.findAll().get(statusLength-1).getId();
            int putId = status.getId();
            String putSubFlag;
            String putFlag;
            String putDescription;
            String putStatusName;

            if(status.getSubFlag() == null){
                putSubFlag = statusRepository.findById(putId).get().getSubFlag();
                // errorNotNullFlagMessage = "Sub Flag tidak boleh null, ";
            } else {
                putSubFlag = status.getSubFlag();
            }

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
                String subFlag;

                for(int _id = statusInitialId; _id < statusLastId+2; _id++){
                    if(statusRepository.findById(_id).isPresent()){
                        statusName = statusRepository.findById(_id).get().getStatusName().toLowerCase();
                        flag = statusRepository.findById(_id).get().getFlag().toLowerCase();
                        subFlag = statusRepository.findById(_id).get().getSubFlag().toLowerCase();
                        if(statusName.equals(putStatusName.toLowerCase()) && flag.equals(putFlag.toLowerCase()) && subFlag.equals(putSubFlag.toLowerCase()) ){
                            if(putId != _id){
                                errorUniqueStatusNameAndFlagAndSubFlagMessage = "Sudah Ada Kombinasi Nama Status, Flag dan Sub Flag yang Sama! pada Id = " + _id;
                                break;
                            }
                        }
                    }
                } 
            }

            if(inputNullSubFlag != true){
                if(status.getSubFlag().isBlank() || status.getSubFlag().isEmpty()) {
                    errorNotBlankSubFlagMessage = "Sub Flag tidak boleh kosong dan harus kurang dari 30 karakter, ";
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

            if (errorUniqueStatusNameAndFlagAndSubFlagMessage != "" || errorNotBlankSubFlagMessage != "" || errorNotBlankFlagMessage != "" || errorNotBlankDescriptionMessage != "" || errorNotBlankStatusNameMessage != ""  || errorNotNullSubFlagMessage != "" || errorNotNullFlagMessage != ""  || errorNotNullDescriptionMessage != ""  || errorNotNullStatusNameMessage != "") {
                result.setSuccess(false);
                result.setMessage("Error: "+ errorUniqueStatusNameAndFlagAndSubFlagMessage+ errorNotBlankSubFlagMessage  + errorNotBlankFlagMessage + errorNotBlankDescriptionMessage + errorNotBlankStatusNameMessage + errorNotNullSubFlagMessage + errorNotNullFlagMessage + errorNotNullDescriptionMessage + errorNotNullStatusNameMessage);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if (!statusRepository.findById(status.getId()).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: id Status tidak ditemukan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Status update = new Status(putId,putStatusName, putDescription, putFlag, putSubFlag, status.getisDeleted());

                statusRepository.save(update);

                result.setMessage("Berhasil update status!");
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
    public ResponseEntity<Result> getAllStatus() {
        result = new Result();
        try {
            Map<String, List<Status>> items = new HashMap<>();
            items.put("items", statusRepository.findAll(Sort.by(Sort.Direction.ASC,"id")));
            result.setData(items);
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
    public ResponseEntity<Result> getStatus(String flag, String subFlag, Integer limit, Integer page) {
        result = new Result();

        Integer total = statusRepository.totalUndeletedStatus();
        if (total == null) { total = 0; }

        int jumlahPage = (int) Math.ceil(total.intValue() / (double) limit);
        if (limit < 1) { limit = 1; }
        if (page < 1) { page = 1; }
        if (jumlahPage < 1) { jumlahPage = 1; }
        if (page > jumlahPage) { page = jumlahPage; }
        if (flag == null) { flag = ""; }
        if (subFlag == null) { subFlag = ""; }

        try {
            Map<String, Object> items = new HashMap<>();
            List<Status> status = statusRepository.getStatus(flag.toLowerCase(), subFlag.toLowerCase(), limit, page);
            items.put("items", status);
            items.put("totalDataResult", status.size());
            items.put("totalData", total);
            result.setData(items);
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
    public ResponseEntity<Result> getStatusById(int id) {
        result = new Result();
        try {
            Optional<Status> status = statusRepository.findById(id);
            if (!status.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: id Status tidak ditemukan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map<String, Status> items = new HashMap<>();
                items.put("items", status.get());
                result.setData(items);
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
    public ResponseEntity<Result> deleteStatusById(int id) {
        result = new Result();
        try {
            Optional<Status> status = statusRepository.findById(id);
            if (!status.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: id Status tidak ditemukan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                statusRepository.deleteById(id);
                result.setMessage("Berhasil delete status!");
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
    public ResponseEntity<Result> softDeleteStatus(int id, boolean deleted) {
        result = new Result();

        try {
            int response = statusRepository.deleteStatus(id, deleted);
            if (response == 1) {
                result.setCode(HttpStatus.OK.value());
                result.setSuccess(true);
                if (deleted == true) {
                    result.setMessage("Status berhasil di soft delete.");
                } else {
                    result.setMessage("Status berhasil di un-soft delete.");
                }
            } else {
                result.setCode(HttpStatus.BAD_REQUEST.value());
                result.setSuccess(false);
                result.setMessage("Error: id Status tidak ditemukan!");
                return ResponseEntity.badRequest().body(result);
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

    

}


