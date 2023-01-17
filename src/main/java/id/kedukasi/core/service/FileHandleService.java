package id.kedukasi.core.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface FileHandleService {
    ResponseEntity<?> uploadFile(MultipartFile multipartFile, HttpServletRequest request);
    ResponseEntity<?> downloadUtilityFile(String fileCode) throws IOException;
    ResponseEntity<?> previewUtilityFile(String fileCode) throws IOException;
    ResponseEntity<?> resetAllFiles();
}
