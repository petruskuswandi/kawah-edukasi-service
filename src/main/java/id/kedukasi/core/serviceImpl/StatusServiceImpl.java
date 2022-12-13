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
            int statusName = statusRepository.findStatusname(status.getStatus_name().toLowerCase());
            String errorUniqueStatusNameMessage = "";
            String errorNotBlankFlagMessage = "";
            String errorNotBlankDescriptionMessage = "";
            String errorNotBlankStatusNameMessage = "";

            if (statusName > 0) {
                errorUniqueStatusNameMessage = "Nama Status Telah Ada!, ";
            }

            if(status.getFlag().isBlank() || status.getFlag().isEmpty()) {
                errorNotBlankFlagMessage = "Flag tidak boleh kosong dan harus kurang dari 30 karakter, ";
            }

            if(status.getDescription().isBlank()  || status.getDescription().isEmpty()) {
                errorNotBlankDescriptionMessage = "Deskripsi Status tidak boleh kosong, ";
            }

            if(status.getStatus_name().length()>50 || status.getStatus_name().isBlank() || status.getStatus_name().isEmpty()) {
                errorNotBlankStatusNameMessage = "Nama Status tidak boleh kosong dan harus kurang dari 50 karakter";
            }

            if (errorUniqueStatusNameMessage != "" || errorNotBlankFlagMessage != "" || errorNotBlankDescriptionMessage != "" || errorNotBlankStatusNameMessage != "") {
                result.setSuccess(false);
                result.setMessage("Error: "+ errorUniqueStatusNameMessage + errorNotBlankFlagMessage + errorNotBlankDescriptionMessage + errorNotBlankStatusNameMessage);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            Status newStatus = new Status(status.getStatus_name().toUpperCase(), status.getDescription(), status.getFlag().toUpperCase(), false);

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
            String errorNotBlankFlagMessage = "";
            String errorNotBlankDescriptionMessage = "";
            String errorNotBlankStatusNameMessage = "";

            if(status.getFlag().isBlank() || status.getFlag().isEmpty()) {
                errorNotBlankFlagMessage = "Flag tidak boleh kosong dan harus kurang dari 30 karakter, ";
            }

            if(status.getDescription().isBlank()  || status.getDescription().isEmpty()) {
                errorNotBlankDescriptionMessage = "Deskripsi Status tidak boleh kosong, ";
            }

            if(status.getStatus_name().length()>50 || status.getStatus_name().isBlank() || status.getStatus_name().isEmpty()) {
                errorNotBlankStatusNameMessage = "Nama Status tidak boleh kosong dan harus kurang dari 50 karakter";
            }

            if (errorNotBlankFlagMessage != "" || errorNotBlankDescriptionMessage != "" || errorNotBlankStatusNameMessage != "") {
                result.setSuccess(false);
                result.setMessage("Error: " + errorNotBlankFlagMessage + errorNotBlankDescriptionMessage + errorNotBlankStatusNameMessage);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if (!statusRepository.findById(status.getId()).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada Status dengan id " +status.getId());
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Status update = new Status(status.getId(),status.getStatus_name(), status.getDescription(), status.getFlag(), status.getisDeleted());

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
