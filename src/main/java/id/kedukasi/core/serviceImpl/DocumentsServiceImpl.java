package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.models.*;
import id.kedukasi.core.repository.DocumentsRepository;
import id.kedukasi.core.repository.StatusRepository;
import id.kedukasi.core.repository.TypeDocumentsRepository;
import id.kedukasi.core.repository.UserRepository;
import id.kedukasi.core.request.UpdateDocumentsRequest;
import id.kedukasi.core.service.DocumentsService;
import id.kedukasi.core.utils.FileUploadUtil;
import id.kedukasi.core.utils.PathGeneratorUtil;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

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
    public ResponseEntity<Result> createDocument(Integer userId, Integer statusId, MultipartFile multipartFile) {
        result = new Result();
        try {
            //Set status
            Optional<Status> status = statusRepository.findById(statusId);
            Documents newDocuments = new Documents();
            if (status.isEmpty() || status.get().isDeleted()) {
                result.setSuccess(false);
                result.setMessage("Status dengan id " + statusId + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            } else {
                newDocuments.setStatus(status.get());
            }

            //Set user
            Optional<User> user = Optional.ofNullable(userRepository.findById(userId));
            if (user.isEmpty() || user.get().isBanned()) {
                result.setSuccess(false);
                result.setMessage("User dengan id " + userId + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            } else {
                newDocuments.setUser(user.get());
            }

            //Get file name
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            //Save file
            String fileCode = FileUploadUtil.saveFile(fileName, userId, multipartFile);

            newDocuments.setPathName(PathGeneratorUtil.generate(userId, fileCode));
            newDocuments.setFileName(fileName);

            documentsRepository.save(newDocuments);
            result.setMessage("Berhasil membuat document baru");
            result.setCode(HttpStatus.OK.value());
            result.setData(newDocuments);
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
            if (document.isEmpty() || document.get().isBanned()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada dokumen dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            } else {
                Map<String, Documents> items = new HashMap<>();
                items.put("items", document.get());
                result.setData(items);
                return ResponseEntity.ok(result);
            }

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
            if (user.isEmpty()) {
                result.setSuccess(false);
                result.setMessage("User dengan id " + id + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            }

            List<Documents> userDocuments = documentsRepository.findAllUndeletedByUserId(id);
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
    public ResponseEntity<Result> updateDocuments(Integer documentId, Integer userId, Integer statusId, MultipartFile multipartFile) {
        result = new Result();
        try {
            Optional<Documents> documentsOld = documentsRepository.findById(documentId);
            if (documentsOld.isEmpty() || documentsOld.get().isBanned()) {
                result.setSuccess(false);
                result.setMessage("Document dengan id " + documentId + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            }
            //Set status
            Optional<Status> status = statusRepository.findById(statusId);
            if (status.isEmpty() || status.get().isDeleted()) {
                result.setSuccess(false);
                result.setMessage("Status dengan id " + statusId + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            } else {
                documentsOld.get().setStatus(status.get());
            }

            //Set user
            Optional<User> user = Optional.ofNullable(userRepository.findById(userId));
            if (user.isEmpty() || user.get().isBanned()) {
                result.setSuccess(false);
                result.setMessage("User dengan id " + userId + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            } else {
                documentsOld.get().setUser(user.get());
            }

            //Get file name
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            //Save file
            String fileCode = FileUploadUtil.saveFile(fileName, userId, multipartFile);

            documentsOld.get().setPathName(PathGeneratorUtil.generate(userId, fileCode));
            documentsOld.get().setFileName(fileName);
            documentsOld.get().setUpdatedTime(new Date());


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
            if (documents.isEmpty() || documents.get().isBanned()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada Documents dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }
            documents.get().setBanned(true);
            documents.get().setBannedTime(new Date());
            documentsRepository.save(documents.get());
            result.setMessage("Berhasil delete Documents!");
            result.setCode(HttpStatus.OK.value());
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
            return ResponseEntity.badRequest().build();
        }
    }
}

