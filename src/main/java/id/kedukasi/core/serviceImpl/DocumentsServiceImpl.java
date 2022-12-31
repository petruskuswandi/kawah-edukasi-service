package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.models.*;
import id.kedukasi.core.repository.DocumentsRepository;
import id.kedukasi.core.repository.StatusRepository;
import id.kedukasi.core.repository.TypeDocumentsRepository;
import id.kedukasi.core.repository.UserRepository;
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

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TypeDocumentsRepository typeDocumentsRepository;
    @Override
    public ResponseEntity<Result> createDocument(DocumentsRequest documents) {
        result = new Result();
        try {

            //Set status
            Optional<Status> status = statusRepository.findById(documents.getStatus());
            Documents newDocuments = new Documents();
            if (!(status.isPresent())) {
                result.setSuccess(false);
                result.setMessage("Status dengan id " + documents.getStatus() + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            } else {
                newDocuments.setStatus(status.get());
            }

            //Set user
            Optional<User> user = Optional.ofNullable(userRepository.findById(documents.getUser()));
            if (!(user.isPresent())) {
                result.setSuccess(false);
                result.setMessage("User dengan id " + documents.getUser() + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            } else {
                newDocuments.setUser(user.get());
            }

            newDocuments.setPathName(documents.getPathName());
            newDocuments.setFileName(documents.getFileName());

            documentsRepository.save(newDocuments);
            result.setMessage("Berhasil membuat document baru");
            result.setCode(HttpStatus.OK.value());
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
            return ResponseEntity.badRequest().build();
        }
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

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Result> getDocumentByUserId(Long id) {
        result = new Result();
        try {
            Optional<User> user = userRepository.findById(id);
            if (!(user.isPresent())) {
                result.setSuccess(false);
                result.setMessage("User dengan id " + id + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            }

            List<Documents> userDocuments = documentsRepository.findAllByUserId(id);
            if (userDocuments.isEmpty()) {
                result.setSuccess(false);
                result.setMessage("User dengan id " + id + " tidak memiliki Document");
                return ResponseEntity.badRequest().body(result);
            }

            result.setData(userDocuments);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Result> updateDocuments(UpdateDocumentsRequest updateDocuments) {
        result = new Result();
        try {
            Optional<Documents> documentsOld = documentsRepository.findById(updateDocuments.getId());
            if (!(documentsOld.isPresent())) {
                result.setSuccess(false);
                result.setMessage("Document dengan id " + updateDocuments.getId() + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            }
            //Set status
            Optional<Status> status = statusRepository.findById(updateDocuments.getStatus());
            if (!(status.isPresent())) {
                result.setSuccess(false);
                result.setMessage("Status dengan id " + updateDocuments.getStatus() + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            } else {
                documentsOld.get().setStatus(status.get());
            }

            //Set user
            Optional<User> user = Optional.ofNullable(userRepository.findById(updateDocuments.getUser()));
            if (!(user.isPresent())) {
                result.setSuccess(false);
                result.setMessage("User dengan id " + updateDocuments.getUser() + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            } else {
                documentsOld.get().setUser(user.get());
            }

            documentsOld.get().setPathName(updateDocuments.getPathName());
            documentsOld.get().setFileName(updateDocuments.getFileName());

            documentsRepository.save(documentsOld.get());
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
            return ResponseEntity.badRequest().build();
        }
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

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
            return ResponseEntity.badRequest().build();
        }
    }
}

