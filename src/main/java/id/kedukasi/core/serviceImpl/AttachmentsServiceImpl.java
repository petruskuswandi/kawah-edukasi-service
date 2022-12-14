package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.models.Attachments;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.repository.AttachmentsRepository;
import id.kedukasi.core.request.AttachmentsRequest;
import id.kedukasi.core.request.UpdateAttachmentsRequest;
import id.kedukasi.core.service.AttachmentsService;
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
public class AttachmentsServiceImpl implements AttachmentsService {

    @Autowired
    StringUtil stringUtil;

    @Autowired
    AttachmentsRepository attachmentsRepository;

    private Result result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public ResponseEntity<Result> getAttachmentsById(Long id) {
        result = new Result();
        try {
            Optional<Attachments> attachments = attachmentsRepository.findById(id);
            if (!attachments.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada attachments dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map<String, Attachments> items = new HashMap<>();
                items.put("items", attachments.get());
                result.setData(items);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> getAllAttachments() {
        result = new Result();
        try {
            Map<String, List<Attachments>> items = new HashMap<>();
            items.put("item", attachmentsRepository.findAll(Sort.by(Sort.Direction.ASC,"id")));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> createAttachments(AttachmentsRequest attachments) {
        result = new Result();

        Attachments newAttachments = new Attachments(attachments.getUrl(), attachments.getDirectory(), attachments.getKey(),
                attachments.getFiletype(), attachments.getFilename(), false);

        attachmentsRepository.save(newAttachments);

        result.setMessage("Berhasil membuat document baru!");
        result.setCode(HttpStatus.OK.value());

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> updateAttachments(UpdateAttachmentsRequest attachments) {
        result = new Result();
        try {
            int attachmentsName = attachmentsRepository.findAttachmentsname(attachments.getFile_name().toLowerCase());
            if (attachmentsName > 0) {
                result.setMessage("Error: Nama Attachments Telah Ada!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if (!attachmentsRepository.findById(attachments.getId()).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada Attachments dengan id " + attachments.getId());
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {

                Attachments update = new Attachments(attachments.getId(), attachments.getUrl(), attachments.getDirectory(), attachments.getKey(), attachments.getFiletype(), attachments.getFile_name(), false);

                attachmentsRepository.save(update);

                result.setMessage("Berhasil update attachments!");
                result.setCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> deleteAttachments (Long id){
        result = new Result();
        try {
            Optional<Attachments> attachments = attachmentsRepository.findById(id);
            if (!attachments.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada Attachments dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                attachmentsRepository.deleteById(id);
                result.setMessage("Berhasil delete Attachments!");
                result.setCode(HttpStatus.OK.value());
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }
}
