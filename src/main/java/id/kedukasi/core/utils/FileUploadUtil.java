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

    public static String saveFile(String fileName, Integer userId, MultipartFile multipartFile) throws IOException {

        String proposedDir = createDir(userId);
        Path uploadDirectory = Paths.get(proposedDir);

        //Generate random string for fileCode
        String fileCode = RandomString.make(8);
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

    //Create directory logic
    public static String createDir(Integer userId) {

        ApplicationHome home = new ApplicationHome();
        String proposedDir = home.getDir().getAbsolutePath() + "/upload-files";
        if (userId != null) {
            proposedDir = home.getDir().getAbsolutePath() + "/upload-files/" + userId;
        }
        File finalDir = new File(proposedDir);
        if(!finalDir.exists()) {
            finalDir.mkdir();
        }
        return proposedDir;
    }

}
