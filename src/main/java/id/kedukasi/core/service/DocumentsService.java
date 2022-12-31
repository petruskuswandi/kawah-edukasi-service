package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.DocumentsRequest;
import id.kedukasi.core.request.UpdateDocumentsRequest;
import org.springframework.http.ResponseEntity;

public interface DocumentsService {
    ResponseEntity<Result> getDocumentByUserId(Long id);
    // implements update documents
    ResponseEntity<Result> updateDocuments(UpdateDocumentsRequest documents);

    // implements delete documents
    ResponseEntity<Result> deleteDocuments(Integer id);

    // implements create documents
    ResponseEntity<Result> createDocument(DocumentsRequest documents);

    // implements get documents by id
    ResponseEntity<Result> getDocumentById(Integer id);
}