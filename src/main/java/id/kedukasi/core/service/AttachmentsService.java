package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.AttachmentsRequest;
import id.kedukasi.core.request.UpdateAttachmentsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public interface AttachmentsService {

    ResponseEntity<Result> getAttachmentsById(Long id);
    ResponseEntity<Result> getAllAttachments();
    ResponseEntity<Result> createAttachments(AttachmentsRequest attachments);
    ResponseEntity<Result> updateAttachments(UpdateAttachmentsRequest attachments);
    ResponseEntity<Result> deleteAttachments(Long id);
}
