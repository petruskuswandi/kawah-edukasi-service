package id.kedukasi.core.utils;

import javax.servlet.http.HttpServletRequest;

public class PathGeneratorUtil {


    public static String generate(String fileCode, HttpServletRequest request) {

        String fileType = fileCode.substring(0, 3);
        String hostName = request.getServerName();
        int port = request.getServerPort();
        String schema = request.getScheme();

        if (fileType.equals("IMG")) {
            return schema + "://" + hostName + ":" + port + "/previewFile/utility/" + fileCode;
        }

        return schema + "://" + hostName + ":" + port + "/downloadFile/utility/" + fileCode;
    }

}
