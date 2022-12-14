package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.TypeDocumentsRequest;
import id.kedukasi.core.request.UpdateTypeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface TypeDocumentsService {

    public ResponseEntity<Result> createTypeDocument (TypeDocumentsRequest type);

    public ResponseEntity<Result> getAllType();
    
    public ResponseEntity<Result> getTypeById(int id);

    public ResponseEntity<Result> updateTypeDocument(UpdateTypeRequest type);

    public ResponseEntity<Result> deleteTypeDocument(int id);
    
}