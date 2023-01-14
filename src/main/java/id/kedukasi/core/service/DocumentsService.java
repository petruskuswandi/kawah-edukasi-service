package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.DocumentsRequest;
import id.kedukasi.core.request.UpdateDocumentsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentsService {
    ResponseEntity<Result> getDocumentByUserId(Long id);
    // implements update documents
    ResponseEntity<Result> updateDocuments(Integer documentId, Integer userId, Integer statusId, MultipartFile multipartFile);

    // implements delete documents
    ResponseEntity<Result> deleteDocuments(Integer id);

    // implements create documents
    ResponseEntity<Result> createDocument(Integer userId, Integer statusId, MultipartFile multipartFile);

    // implements get documents by id
    ResponseEntity<Result> getDocumentById(Integer id);
}