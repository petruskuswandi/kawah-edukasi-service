package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.TypeDocuments;
import id.kedukasi.core.repository.TypeDocumentsRepository;
import id.kedukasi.core.request.TypeDocumentsRequest;
import id.kedukasi.core.request.UpdateTypeRequest;
import id.kedukasi.core.service.TypeDocumentsService;
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
public  class TypeDocumentsServiceImpl implements TypeDocumentsService {

    @Autowired
    StringUtil stringUtil;
    @Autowired
    TypeDocumentsRepository typeDocumentsRepository;

    private Result result;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ResponseEntity<Result> createTypeDocument(TypeDocumentsRequest type) {
        result = new Result();
        try {
            int typeName = typeDocumentsRepository.findtypeName(type.getType_Name().toLowerCase());

            if (typeName > 0) {
                result.setMessage("Error: Nama Type Document Telah Ada!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if (type.getType_Name().length() > 10 || type.getType_Name().isBlank() || type.getType_Name().isEmpty()) {
                result.setMessage("Error: Nama Type Document tidak boleh kosong dan harus kurang dari 10 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if (type.getDescription().length() > 100 || type.getDescription().isBlank() || type.getDescription().isEmpty()) {
                result.setMessage("Error: Deskripsi Type Document tidak boleh kosong dan harus kurang dari 100 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            TypeDocuments newTypeDocuments = new TypeDocuments(type.getType_Name(), type.getDescription(), false);

            typeDocumentsRepository.save(newTypeDocuments);

            result.setMessage("Berhasil membuat Type Document baru!");
            result.setCode(HttpStatus.OK.value());

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> getAllType() {
        result = new Result();
        try {
            Map<String, List<TypeDocuments>> items = new HashMap<>();
            items.put("items", typeDocumentsRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> getTypeById(int id) {
        result = new Result();
        try {
            Optional<TypeDocuments> type = typeDocumentsRepository.findById(id);
            if (!type.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Tidak ada type dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map<String, TypeDocuments> items = new HashMap<>();
                items.put("items", type.get());
                result.setData(items);
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> updateTypeDocument(UpdateTypeRequest type) {
        result = new Result();
        try {

            int typeName= typeDocumentsRepository.findtypeName(type.getType_Name().toLowerCase());
            if (typeName > 0) {
                result.setMessage("Error: Nama Type Document Telah Ada!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity
                        .badRequest()
                        .body(result);
            }

            if  (type.getDescription().isBlank() || type.getDescription().isEmpty()) {
                result.setMessage("Error: Deskripsi Program tidak boleh kosong ");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if (type.getType_Name().length() > 10 || type.getType_Name().isBlank() || type.getType_Name().isEmpty()) {
                result.setMessage("Error: Nama Program tidak boleh kosong dan harus kurang dari 10 karakter");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }


            if (!typeDocumentsRepository.findById(type.getId()).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada Type Document dengan id " + type.getId());
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                TypeDocuments update = new TypeDocuments(type.getId(), type.getType_Name(), type.getDescription(),
                        type.isDeleted());

                typeDocumentsRepository.save(update);

                result.setMessage("Berhasil update Type Document!");
                result.setCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }

        return ResponseEntity.ok(result);
    }



    @Override
    public ResponseEntity<Result> deleteTypeDocument(int id) {
        result = new Result();
        try {
            Optional<TypeDocuments> program = typeDocumentsRepository.findById(id);
            if (!program.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada Type Document dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                typeDocumentsRepository.deleteById(id);
                result.setMessage("Berhasil delete Document!");
                result.setCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }
}