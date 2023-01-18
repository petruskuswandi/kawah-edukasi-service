package id.kedukasi.core.utils;

import id.kedukasi.core.models.Result;
import net.bytebuddy.utility.RandomString;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Arrays;

public class FileUploadUtil {

    private static final String[] documentTypes = {"pdf", "docx", "doc", "xls", "xlsx",
            "ppt", "pptx", "odp", "key", "odt"};
    private static final String[] imageTypes = {"png", "jpg", "bmp", "tif", "tiff", "jpeg"};

    private static enum CODE {
        IMG,//For images
        DOC,//For documents
        OTH //For other file formats
    }

    public static String saveFile(String fileName, MultipartFile multipartFile) throws IOException {

        //Validasi file size harus kurang dari 7MB
        if (multipartFile.getSize() > 7340032) {
            return null;
        }

        //Get file type of the file
        String fileTypeThreeChar = fileName.substring(fileName.length() - 3).toLowerCase();
        String fileTypeFourChar = fileName.substring(fileName.length() - 4).toLowerCase();

        //Check if file type is document
        boolean isDocument = Arrays.stream(documentTypes).anyMatch(fileTypeThreeChar::equals) ||
                Arrays.stream(documentTypes).anyMatch(fileTypeFourChar::equals);

        //Check if file type is image
        boolean isImage = Arrays.stream(imageTypes).anyMatch(fileTypeThreeChar::equals) ||
                Arrays.stream(imageTypes).anyMatch(fileTypeFourChar::equals);

        String proposedDir = createDir(isDocument, isImage);
        Path uploadDirectory = Paths.get(proposedDir);

        //Generate random string for fileCode
        String fileCode = RandomString.make(4);
        //End

        String fileTypeCode = null;

        //Save file
        try (InputStream inputStream = multipartFile.getInputStream()) {

            Path filePath = uploadDirectory.resolve( CODE.OTH + fileCode + " - " + fileName);

            //Add identification code
            fileTypeCode = CODE.OTH + fileCode;

            if (isDocument) {
                filePath = uploadDirectory.resolve( CODE.DOC + fileCode + " - " + fileName);
                fileTypeCode = CODE.DOC + fileCode;
            } else if (isImage) {
                filePath = uploadDirectory.resolve( CODE.IMG + fileCode + " - " + fileName);
                fileTypeCode = CODE.IMG + fileCode;
            }

            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Error: Gagal menyimpan file " + fileName, ioe);
        }
        //End

        return fileTypeCode;
    }

    //Create directory logic
    public static String createDir(boolean isDocument, boolean isImage) {

        ApplicationHome home = new ApplicationHome();
        String separator = File.separator;

        //If the file type is neither document/image it will be saved to 'others' folder
        String proposedDir = home.getDir().getAbsolutePath() + separator + "upload-files" + separator + "others";

        //Manage directory for different file types
        if (isDocument) {
            proposedDir = home.getDir().getAbsolutePath() + separator + "upload-files" + separator + "documents";
        } else if (isImage) {
            proposedDir = home.getDir().getAbsolutePath() + separator + "upload-files" + separator + "images";
        }

        File finalDir = new File(proposedDir);
        if(!finalDir.exists()) {
            finalDir.mkdirs(); //Create dir paths
        }

        return proposedDir;
    }
}
