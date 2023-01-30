package id.kedukasi.core.utils;

import javax.servlet.http.HttpServletRequest;

public class PathGeneratorUtil {

    public static String generate(String fileCode, String baseUrl) {

        String fileType = fileCode.substring(0, 3);

        if (fileType.equals("IMG")) {
            return baseUrl + "/previewFile/utility/" + fileCode;
        }

        return baseUrl + "/downloadFile/utility/" + fileCode;
    }

}
