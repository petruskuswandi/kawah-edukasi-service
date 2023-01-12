package id.kedukasi.core.controller;

import id.kedukasi.core.response.FileUploadResponse;
import id.kedukasi.core.utils.FileDownloadUtil;
import id.kedukasi.core.utils.FileUploadUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileHandleController {

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(
            @RequestPart("file") MultipartFile multipartFile) {

        Long size = multipartFile.getSize();

        //Validate file size
        if (size > 10000000) {
            return new ResponseEntity<>("Error: File harus kurang dari 10MB", HttpStatus.BAD_REQUEST);
        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());


        try {
            String fileCode = FileUploadUtil.saveFile(fileName, multipartFile);
            FileUploadResponse response = new FileUploadResponse();

            response.setFileName(fileName);
            response.setFileCode(fileCode);
            response.setSize(size);
            response.setDownloadUri("/downloadFile/" + fileCode);

            return new ResponseEntity<FileUploadResponse>(response, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/downloadFile/{fileCode}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileCode") String fileCode) {

        Resource fileAsResource = null;

        try {
            fileAsResource = FileDownloadUtil.getFileAsResource(fileCode);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        if (fileAsResource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + fileAsResource.getFilename() + "\"";

        return ResponseEntity.ok().
                contentType(MediaType.parseMediaType(contentType)).
                header(HttpHeaders.CONTENT_DISPOSITION, headerValue).
                body(fileAsResource);
    }
}
