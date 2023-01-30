package id.kedukasi.core.controller;

import id.kedukasi.core.service.FileHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@CrossOrigin
@RestController
public class FileHandleController {
    @Autowired
    FileHandleService fileHandleService;

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(
            @RequestPart(value = "file") MultipartFile multipartFile,
            HttpServletRequest request
    ) {
       return fileHandleService.uploadFile(multipartFile, request);
    }

    @GetMapping("/downloadFile/utility/{fileCode}")
    public ResponseEntity<?> downloadUtilityFile(@PathVariable("fileCode") String fileCode) throws IOException {
        return fileHandleService.downloadUtilityFile(fileCode);
    }

    @GetMapping("/previewFile/utility/{fileCode}")
    public ResponseEntity<?> previewUtilityFile(@PathVariable("fileCode") String fileCode) throws IOException {
        return fileHandleService.previewUtilityFile(fileCode);
    }


}
