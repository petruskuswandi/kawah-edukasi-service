package id.kedukasi.core.controller;

import id.kedukasi.core.service.FileHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
public class FileHandleController {
    @Autowired
    FileHandleService fileHandleService;

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(
            @RequestPart(value = "file", required = true) MultipartFile multipartFile
    ) {
       return fileHandleService.uploadFile(multipartFile);
    }

    @GetMapping("/downloadFile/utility/{fileCode}")
    public ResponseEntity<?> downloadUtilityFile(@PathVariable("fileCode") String fileCode) {
        return fileHandleService.downloadUtilityFile(fileCode);
    }

    @GetMapping("/downloadFile/{userId}/{fileCode}")
    public ResponseEntity<?> downloadUserFile(
            @PathVariable("fileCode") String fileCode,
            @PathVariable("userId") Integer userId
    ) {
        return fileHandleService.downloadUserFile(userId, fileCode);
    }

    @GetMapping("/previewFile/utility/{fileCode}")
    public ResponseEntity<?> previewUtilityFile(@PathVariable("fileCode") String fileCode) throws IOException {
        return fileHandleService.previewUtilityFile(fileCode);
    }

    @GetMapping("/previewFile/{userId}/{fileCode}")
    public ResponseEntity<?> previewUserFile(
            @PathVariable("fileCode") String fileCode,
            @PathVariable("userId") Integer userId
    ) throws IOException {
        return fileHandleService.previewUserFile(userId, fileCode);
    }


}
