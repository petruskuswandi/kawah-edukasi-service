package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.User;
import id.kedukasi.core.repository.UserRepository;
import id.kedukasi.core.response.FileUploadResponse;
import id.kedukasi.core.service.FileHandleService;
import id.kedukasi.core.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Service
public class FileHandleServiceImpl implements FileHandleService {

    @Autowired
    UserRepository userRepository;
    private Result result;

    @Value("${app.url.staging}")
    String baseUrl;

    @Override
    public ResponseEntity<?> uploadFile(MultipartFile multipartFile, HttpServletRequest request) {
        result = new Result();

        Long size = multipartFile.getSize();
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Result result = new Result();

        try {
            String fileCode = FileUploadUtil.saveFile(fileName, multipartFile);

            //Validasi file size
            if (fileCode == null) {
                result.setCode(HttpStatus.BAD_REQUEST.value());
                result.setSuccess(false);
                result.setMessage("File harus kurang dari 7MB");
                return ResponseEntity.badRequest().body(result);
            }

            FileUploadResponse response = new FileUploadResponse();
            response.setFileName(fileName);
            response.setFileCode(fileCode);
            response.setSize(size);

            //Path generator
            response.setDownloadUri(PathGeneratorUtil.generate(fileCode, baseUrl));
            result.setMessage("Data berhasil disimpan, harap catat file code/download uri karena record tidak disimpan dalam db!");
            result.setData(response);
            System.out.println(baseUrl);
            return ResponseEntity.ok(result);

        } catch (IOException io) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @Override
    public ResponseEntity<?> downloadUtilityFile(String fileCode) throws IOException {
        result = new Result();
        Resource fileAsResource = null;

        try {
            fileAsResource = FileDownloadUtil.getFileAsResource(fileCode);
            if (fileAsResource == null) {
                result.setSuccess(false);
                result.setCode(400);
                result.setMessage("File dengan kode " + fileCode + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        return responses("ccc", "cccc", fileAsResource);
    }

    @Override
    public ResponseEntity<?> previewUtilityFile(String fileCode) throws IOException {

        result = new Result();

        Resource fileAsResource = null;

        try {
            fileAsResource = FileDownloadUtil.getFileAsResource(fileCode);
            if (fileAsResource == null) {
                result.setSuccess(false);
                result.setCode(400);
                result.setMessage("File dengan kode " + fileCode + " tidak ditemukan");
                return ResponseEntity.badRequest().body(result);
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        String endFileName = fileAsResource.getFilename();
        String fileTypeThreeChar = endFileName.substring(endFileName.length() - 3);
        String fileTypeFourChar = endFileName.substring(endFileName.length() - 4);

        return responses(fileTypeThreeChar, fileTypeFourChar, fileAsResource);
    }

    @Override
    public ResponseEntity<?> resetAllFiles() {
        return null;
    }

    private ResponseEntity<?> responses(
            String fileTypeThreeChar,
            String fileTypeFourChar,
            Resource fileAsResource)
            throws IOException
            {
        String headerValue = "Content Disposition; filename=\"" + fileAsResource.getFilename() + "\"";
        if (fileTypeThreeChar.equalsIgnoreCase("jpg") || fileTypeFourChar.equalsIgnoreCase("jpeg")
                || fileTypeThreeChar.equalsIgnoreCase("png")) {

            return ResponseEntity.ok()
                    .contentLength(fileAsResource.contentLength())
                    .contentType(MediaType.IMAGE_JPEG)
                    .contentType(MediaType.IMAGE_PNG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(fileAsResource);

        } else if (fileTypeThreeChar.equalsIgnoreCase("pdf")) {

            return ResponseEntity.ok()
                    .contentLength(fileAsResource.contentLength())
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(fileAsResource);
        } else {

            String contentType = "application/octet-stream";
            String headerValueNew = "attachment; filename=\"" + fileAsResource.getFilename() + "\"";

            return ResponseEntity.ok().
                    contentType(MediaType.parseMediaType(contentType)).
                    header(HttpHeaders.CONTENT_DISPOSITION, headerValueNew).
                    body(fileAsResource);
        }
    }

}
