package id.kedukasi.core.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownloadUtil {

    private static Path foundFile;
    public static Resource getFileAsResource(String fileCode) throws IOException {

        //Validate file path
        if (FileUploadUtil.getUploadPath() == null) {
            return null;
        }

        //Get file path
        Path uploadDirectory = Paths.get(FileUploadUtil.getUploadPath());

        //Search file
        Files.list(uploadDirectory).forEach(file -> {
            if (file.getFileName().toString().startsWith(fileCode)) {
                foundFile = file;
                return;
            }
        });

        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }

        return null;
    }

}
