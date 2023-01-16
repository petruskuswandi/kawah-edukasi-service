package id.kedukasi.core.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileHandleService {
    ResponseEntity<?> uploadFile(MultipartFile multipartFile);
    ResponseEntity<?> downloadUserFile(Integer userId, String fileCode) throws IOException;
    ResponseEntity<?> downloadUtilityFile(String fileCode) throws IOException;
    ResponseEntity<?> previewUserFile(Integer userId, String fileCode) throws IOException;
    ResponseEntity<?> previewUtilityFile(String fileCode) throws IOException;
    ResponseEntity<?> resetAllFiles();
}
