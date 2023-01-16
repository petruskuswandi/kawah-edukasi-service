package id.kedukasi.core.utils;

public class PathGeneratorUtil {

    public static String generate(Integer userId, String fileCode) {
        if (userId != null) {
            return "/downloadFile/" + userId + "/" + fileCode;
        }
        return "/downloadFile/utility/" + fileCode;
    }

}
