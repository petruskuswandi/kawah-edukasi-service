package id.kedukasi.core.utils;

import net.bytebuddy.utility.RandomString;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {
    static String uploadPath = null;
    public static String saveFile(String fileName, MultipartFile multipartFile) throws IOException {

        //Create directory logic
        ApplicationHome home = new ApplicationHome();
        String proposedDir = home.getDir().getAbsolutePath() + "/upload-files";
        File finalDir = new File(proposedDir);
        if(!finalDir.exists()) {
            finalDir.mkdir();
        }
        //End

        uploadPath = proposedDir;
        Path uploadDirectory = Paths.get(proposedDir);

        //Generate random string for fileCode
        String fileCode = RandomString.make(8);;
        //End

        //Save file
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadDirectory.resolve(fileCode + " - " + fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Error: Gagal menyimpan file " + fileName, ioe);
        }
        //End

        return fileCode;
    }

    public static String getUploadPath() {
        return uploadPath;
    }
}
