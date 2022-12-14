package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.models.Documents;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.repository.DocumentsRepository;
import id.kedukasi.core.request.DocumentsRequest;
import id.kedukasi.core.request.UpdateDocumentsRequest;
import id.kedukasi.core.service.DocumentsService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DocumentsServiceImpl implements DocumentsService {

    @Autowired
    StringUtil stringUtil;

    private Result result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    DocumentsRepository documentsRepository;
    @Override
    public ResponseEntity<Result> createDocument(DocumentsRequest documents) {
        result = new Result();

        Documents newDocuments = new Documents(documents.getUrl(), documents.getDirectory(), documents.getKey(),
                documents.getFiletype(), documents.getDocumentsName(), false, documents.getUserId(), documents.getRoleId());

        documentsRepository.save(newDocuments);

        result.setMessage("Berhasil membuat document baru!");
        result.setCode(HttpStatus.OK.value());

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> getAllDocuments() {
        result = new Result();
        try {
            Map<String, List<Documents>> items = new HashMap<>();
            items.put("items", documentsRepository.findAll(Sort.by(Sort.Direction.ASC,"id")));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> getDocumentById(Integer id) {
        result = new Result();
        try {
            Optional<Documents> document = documentsRepository.findById(id);
            if (!document.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada dokumen dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map<String, Documents> items = new HashMap<>();
                items.put("items", document.get());
                result.setData(items);
            }
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> updateDocuments(UpdateDocumentsRequest documents) {
        result = new Result();
        try {

            int documentsName = documentsRepository.findDocumentsname(documents.getDocumentsName().toLowerCase());
            if (documentsName > 0) {
                result.setMessage("Error: Nama Documents Telah Ada!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if (!documentsRepository.findById(documents.getId()).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada Documents dengan id " + documents.getId());
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Documents update = new Documents(documents.getId(), documents.getUrl(), documents.getDirectory(), documents.getKey(), documents.getFiletype(), documents.getDocumentsName(), documents.isDeleted(), documents.getUserId(), documents.getRoleId());

                documentsRepository.save(update);

                result.setMessage("Berhasil update documents!");
                result.setCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> deleteDocuments(Integer id) {
        result = new Result();
        try {
            Optional<Documents> documents = documentsRepository.findById(id);
            if (!documents.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada Documents dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                documentsRepository.deleteById(id);
                result.setMessage("Berhasil delete Documents!");
                result.setCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

}

