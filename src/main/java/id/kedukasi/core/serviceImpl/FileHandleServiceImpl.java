package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.User;
import id.kedukasi.core.repository.UserRepository;
import id.kedukasi.core.response.FileUploadResponse;
import id.kedukasi.core.service.FileHandleService;
import id.kedukasi.core.utils.FileDownloadUtil;
import id.kedukasi.core.utils.FileUploadUtil;
import id.kedukasi.core.utils.PathGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class FileHandleServiceImpl implements FileHandleService {

    @Autowired
    UserRepository userRepository;
    private Result result;

    @Override
    public ResponseEntity<?> uploadFile(MultipartFile multipartFile) {
        result = new Result();

        Long size = multipartFile.getSize();
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Result result = new Result();

        try {
            String fileCode = FileUploadUtil.saveFile(fileName, null, multipartFile);
            FileUploadResponse response = new FileUploadResponse();
            response.setFileName(fileName);
            response.setFileCode(fileCode);
            response.setSize(size);
            response.setDownloadUri(PathGeneratorUtil.generate(null, fileCode));
            result.setData(response);
            return ResponseEntity.ok(result);
        } catch (IOException io) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @Override
    public ResponseEntity<?> downloadUserFile(Integer userId, String fileCode) {
        result = new Result();
        Resource fileAsResource = null;

        //Find user
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId));
        if (user.isEmpty()) {
            result.setSuccess(false);
            result.setCode(400);
            result.setMessage("User dengan " + userId + " tidak ditemukan");
            return ResponseEntity.badRequest().body(result);
        }

        try {
            fileAsResource = FileDownloadUtil.getFileAsResource(fileCode, userId);
            if (fileAsResource == null) {
                result.setSuccess(false);
                result.setCode(400);
                result.setMessage("File dengan kode " + fileCode + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + fileAsResource.getFilename() + "\"";

        return ResponseEntity.ok().
                contentType(MediaType.parseMediaType(contentType)).
                header(HttpHeaders.CONTENT_DISPOSITION, headerValue).
                body(fileAsResource);
    }

    @Override
    public ResponseEntity<?> downloadUtilityFile(String fileCode) {
        result = new Result();
        Resource fileAsResource = null;

        try {
            fileAsResource = FileDownloadUtil.getFileAsResource(fileCode, null);
            if (fileAsResource == null) {
                result.setSuccess(false);
                result.setCode(400);
                result.setMessage("File dengan kode " + fileCode + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + fileAsResource.getFilename() + "\"";

        return ResponseEntity.ok().
                contentType(MediaType.parseMediaType(contentType)).
                header(HttpHeaders.CONTENT_DISPOSITION, headerValue).
                body(fileAsResource);
    }

    @Override
    public ResponseEntity<?> previewUserFile(Integer userId, String fileCode) throws IOException {

        result = new Result();
        Resource fileAsResource = null;

        //Find user
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId));
        if (user.isEmpty()) {
            result.setSuccess(false);
            result.setCode(400);
            result.setMessage("User dengan " + userId + " tidak ditemukan");
            return ResponseEntity.badRequest().body(result);
        }

        try {
            fileAsResource = FileDownloadUtil.getFileAsResource(fileCode, userId);
            if (fileAsResource == null) {
                result.setSuccess(false);
                result.setCode(400);
                result.setMessage("File dengan kode " + fileCode + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        String headerValue = "Content Disposition; filename=\"" + fileAsResource.getFilename() + "\"";
        String endFileName = fileAsResource.getFilename();
        String fileTypeThreeChar = endFileName.substring(endFileName.length() - 3);
        String fileTypeFourChar = endFileName.substring(endFileName.length() - 4);

        if (fileTypeThreeChar.equals("jpg") || fileTypeFourChar.equals("jpeg")
                || fileTypeThreeChar.equals("png") || fileTypeThreeChar.equals("JPG")
                || fileTypeFourChar.equals("JPEG") || fileTypeThreeChar.equals("PNG")) {

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentLength(fileAsResource.contentLength())
                    .contentType(MediaType.IMAGE_JPEG)
                    .contentType(MediaType.IMAGE_PNG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(fileAsResource);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(fileAsResource.contentLength())
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(fileAsResource);

    }

    @Override
    public ResponseEntity<?> previewUtilityFile(String fileCode) throws IOException {

        result = new Result();

        Resource fileAsResource = null;

        try {
            fileAsResource = FileDownloadUtil.getFileAsResource(fileCode, null);
            if (fileAsResource == null) {
                result.setSuccess(false);
                result.setCode(400);
                result.setMessage("File dengan kode " + fileCode + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        String headerValue = "Content Disposition; filename=\"" + fileAsResource.getFilename() + "\"";
        String endFileName = fileAsResource.getFilename();
        String fileTypeThreeChar = endFileName.substring(endFileName.length() - 3);
        String fileTypeFourChar = endFileName.substring(endFileName.length() - 4);

        if (fileTypeThreeChar.equals("jpg") || fileTypeFourChar.equals("jpeg")
                || fileTypeThreeChar.equals("png") || fileTypeThreeChar.equals("JPG")
                || fileTypeFourChar.equals("JPEG") || fileTypeThreeChar.equals("PNG")) {

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentLength(fileAsResource.contentLength())
                    .contentType(MediaType.IMAGE_JPEG)
                    .contentType(MediaType.IMAGE_PNG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(fileAsResource);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(fileAsResource.contentLength())
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(fileAsResource);
    }

    @Override
    public ResponseEntity<?> resetAllFiles() {
        return null;
    }

}