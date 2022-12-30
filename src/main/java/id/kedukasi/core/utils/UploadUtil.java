package id.kedukasi.core.utils;

import id.kedukasi.core.models.Peserta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class UploadUtil {

    public static void saveCV(String pathUpload, String namaPeserta, String nomorKtp, MultipartFile uploadCv, Peserta peserta) throws IOException {
        //save upload CV
        String customNameCV = nomorKtp + "_" + namaPeserta + ".pdf";
        peserta.setUploadCv(customNameCV);
        //save column upload cv path
        peserta.setUploadCvPath(pathUpload + "/documents/" + customNameCV);
        //save to folder
        String filePath = pathUpload + "/documents" + File.separator + customNameCV;
        OutputStream out = new FileOutputStream(filePath);
        out.write(uploadCv.getBytes());
        out.close();
    }

    public static void saveImage(String pathUpload, String namaPeserta, MultipartFile uploadImage, String nomorKtp, Peserta peserta, String format) throws IOException {
        String customNameImage = "profile_" + nomorKtp + "_" + namaPeserta + "." + format;
        //save column upload image path
        peserta.setUploadImagePath(pathUpload + "/image/" + customNameImage);
        //save to folder
        String filePath = pathUpload + "/image" + File.separator + customNameImage;
        OutputStream out = new FileOutputStream(filePath);
        out.write(uploadImage.getBytes());
        out.close();
    }
}
